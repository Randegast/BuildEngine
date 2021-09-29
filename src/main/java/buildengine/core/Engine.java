package buildengine.core;

import buildengine.Window;
import buildengine.input.Input;
import buildengine.time.Scheduler;
import org.lwjgl.Version;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * An {@code Engine} is the root implementation of BuildEngine.
 * <p>
 *     The class contains all the root components of the game, like
 *     the game loop {@code Thread} and the GLFW {@code Window} object.
 *     It also contains the root for the ECS implementation: The {@code Stage} object.
 * </p>
 * <p>
 *     The {@code run()} method contains the {@code while} loop. This loop updates and
 *     renders the components.
 * </p>
 * @author Kai van Maurik
 * @since 1.0
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
	/**
	 * The delta time variable. The time between each loop circle.
	 */
	private float deltaTime;

	private boolean running;

	/**
	 * Initializes a newly created {@code Engine} object.
	 * @param window The window object the engine uses.
	 */
	public Engine(Window window) {
		this.window = window;
		this.thread = new Thread(this::run, "BuildEngine:MAIN");

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
	 * <h1>Game Loop</h1>
	 *
	 * Responsible for running the Engine and its components. This function consists of a total of 3 stages. One
	 * before the game loop (the initialisation stage), the game loop and the clean up/exit stage.<br><br>
	 *
	 * This function starts by calling all initialisation methods. Afterwards, thanks to an accumulator, it runs
	 * a while loop in sync with the refresh rate of the window. The loop will only end when GLFW receives a request
	 * for the window to close. This while loop consists of five stages as listed bellow.
	 * <ul>
	 * <li>
	 *     The first part of the while loop runs all the logic of the engine. The input is gathered at the beginning.
	 *     After that the stage (and scheduler) is being updated (fixed and constant).
	 * </li>
	 * <li>
	 *     Secondly the rendering is being done. This simply means glClearing the color and depth buffers, and calling
	 *     the render function of the current stage. All other render logic is the responsibility of the stage itself.
	 * </li>
	 * <li>
	 *	   When we come to the third part the game logic and rendering is done and the input is being cleared in
	 *	   preparation for the next circle, and the window buffer is swapped to show the updates.
	 * </li>
	 * <li>
	 *     For the fourth part any potential OpenGL errors are being broadcast and shut down the program.
	 * </li>
	 * <li>
	 *     The final part updates the time variables used to run the loop. This is handled last so the time reflects the
	 *     duration of one loop, and not any delays caused by other threads.
	 * </li>
	 * </ul>
	 * Finally, if the while loop gets broken out of the cleanup function of the current stage will be called and the
	 * window will be terminated.
	 *
	 * Error handing is done by printing to the console when catching an error. When java doesn't crash because of the
	 * error the program will simply exit and clean up.
	 */
	public void run() {

		running = true;
		initialize();

		try {

			float elapsedTime = 0.0f;
			float lastLoopTime = (float) glfwGetTime();
			float accumulator = 0.0f;
			float interval = 1f / FIXED_UPDATES_PER_SECOND;

			while(running && !glfwWindowShouldClose(window.getId())) {
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
				int error = glGetError();
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
	 * @implNote This method provides no transition effects or load time like the {@link Stage#queueScene queueScene}
	 * method does, and thus this should only be used outside game purposes.
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

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	// Getters and setters

	/**
	 * Gets the current stage the engine is updating
	 * @return the current {@code Stage} object.
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * Gets the current thread the engine is running on
	 * @return the current {@code Thread} object.
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * Gets the current window the engine is rendering to
	 * @return the current {@code Window} object.
	 */
	public Window getWindow() {
		return window;
	}

	/**
	 * Gets the time the last tick of the game loop took.
	 * @return the delta time variable.
	 */
	public float getDeltaTime() {
		return deltaTime;
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}
}