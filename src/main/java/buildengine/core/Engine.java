package buildengine.core;

import buildengine.Window;
import buildengine.editor.Console;
import buildengine.input.Input;
import buildengine.time.Scheduler;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * This is the class containing the game_loop thread and all the
 * managing of the display, time and rendering.
 *
 * <p>
 *     Since version v1.0s don't create an instance of Engine.
 *     Instead use {@see BuildEngine.create()}.
 * </p>
 *
 * @author Kai v. Maurik
 */
public class Engine {

	/** By default, fixed update functions will update 60 times a second */
	public static float FIXED_UPDATES_PER_SECOND = 60;

	/** The game loop thread (with gl context) */
	private final Thread thread;
	/** The GLFW window to run the engine*/
	private final Window window;

	/** The current stage to be updated and rendered */
	private Stage stage;

	private float deltaTime;

	/**
	 * Creates an engine instance.
	 * @param window The window object the engine runs in
	 */
	public Engine(Window window) {
		this.window = window;
		this.thread = new Thread(this::run, "game_loop");

		stage = new Stage();
	}

	/**
	 * Initialize the engine on the current thread. The GLFW window is initialized and with that the GL context
	 * (on this thread). After that the OpenGL capabilities are created and some default options are set for the
	 * clear color and blending. At the end of this method the current stage will actually begin and all its
	 * systems' {@code begin()} function will be called.
	 */
	private void initialize() {
		Console.log("Using LWJGL " + Version.getVersion());

		window.init();

		glClearColor(.2f,.2f,.2f,1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		stage.begin();
	}

	/**
	 * Game Loop
	 *
	 * Responsible for running the Engine and its components. This function consists of a total of 3 stages. One
	 * before the game loop (the initialisation stage), the game loop and the clean up/exit stage.<br><br>
	 *
	 * This runnable function starts by calling all initialisation methods. Then thanks to an accumulator it runs
	 * a while loop in sync with the refresh rate of the window. The loop will only end when GLFW receives a request
	 * for the window to close. This while loop consists of five stages as listed bellow.
	 * <p>
	 *     The first part of the while loop runs all the logic of the engine. The input is gathered at the beginning.
	 *     After that the stage (and scheduler) is being updated (fixed and constant).
	 * </p>
	 * <p>
	 *     Secondly the rendering is being done. This simply means glClearing the color and depth buffers, and calling
	 *     the render function of the current stage. All other render logic is the responsibility of the stage itself.
	 * </p>
	 * <p>
	 *	   When we come to the third part the game logic and rendering is done and the input is being cleared in
	 *	   preparation for the next circle, and the window buffer is swapped to show the updates.
	 * </p>
	 * <p>
	 *     For the fourth part any potential OpenGL errors are being broadcast and shut down the program.
	 *     TODO (Dev note) I don't actually know if I should crash the program. If you run into issues, let me know.
	 * </p>
	 * <p>
	 *     The final part updates the time variables used to run the loop. This is handled last so the time reflects the
	 *     duration of one loop, and not any delays caused by other threads.
	 * </p>
	 * Finally, if the while loop gets broken out of the cleanup function of the current stage will be called and the
	 * window will be terminated.
	 *
	 * Error handing is done by printing to the console when catching an error. When java doesn't crash because of the
	 * error the program will simply exit and clean up.
	 */
	public void run() {

		initialize();

		try {

			float elapsedTime = 0.0f;
			float lastLoopTime = (float) glfwGetTime();
			float accumulator = 0.0f;
			float interval = 1f / FIXED_UPDATES_PER_SECOND;

			while(!glfwWindowShouldClose(window.getId())) {
				// Updating
				glfwPollEvents();

				stage.update(elapsedTime);
				Scheduler.get().update();

				while(accumulator >= interval) {
					stage.fixedUpdate();
					accumulator -= interval;
				}

				// Rendering
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				stage.render();

				// Finalizing
				Input.pollEvents();
				glfwSwapBuffers(window.getId());

				// Error
				int error = GL11.glGetError();
				if(error != GL_NO_ERROR)
					throw new RuntimeException("OpenGL ran into an unexpected error. Error code: " + error);

				// Time
				float time = (float) glfwGetTime();
				elapsedTime = time - lastLoopTime;
				accumulator += elapsedTime;
				lastLoopTime = time;
				deltaTime = elapsedTime;
			}
		} catch (Exception e) {
			// Error handling through console
			Console.err("The main thread has run into an error: " + e);
			Console.err("See crash dump for full error log.");
			StackTraceElement[] trace = e.getStackTrace();
			for (StackTraceElement traceElement : trace)
				Console.err("\tat " + traceElement);
		}

		stage.cleanUp();
		window.terminate();
	}

	/**
	 * Switches to a different stage. Formally that means first calling the {@code begin()} function of the given
	 * stage, than actually changing the current stage. Finally, the old stage's {@code cleanUp()} function is being
	 * called.
	 *
	 * This method provides no transition effects or load time like the {@code Scene} objects do, and thus this
	 * should only be used outside game purposes.
	 *
	 * @param stage the new stage.
	 */
	public void swapStage(Stage stage) {
		if(stage == null)
			stage = new Stage();
		stage.begin();
		Stage oldStage = this.stage;
		this.stage = stage;
		oldStage.cleanUp();
	}

	// Getters and setters

	public Stage getStage() {
		return stage;
	}

	public Thread getThread() {
		return thread;
	}

	public Window getWindow() {
		return window;
	}

	public float getDeltaTime() {
		return deltaTime;
	}
}