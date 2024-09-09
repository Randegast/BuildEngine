package buildengine.core;

import buildengine.graphics.Draw;
import buildengine.graphics.renderer.TransitionRenderer;
import buildengine.input.Input;
import buildengine.time.Time;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * This is the class containing the game_loop thread and all the
 * managing of the display, time and rendering.
 *
 * <p>
 *     Since version v1.0s don't create an instance of Engine on your own.
 *     Instead use {@see BuildEngine.create()}.
 * </p>
 *
 * @author Kai v. Maurik
 * @see buildengine.BuildEngine#create(String, Dimension, boolean, boolean)
 */
public class Engine implements Runnable {

	public static float DEFAULT_FPS = 63, DEFAULT_FIXED_UPDATES_PER_SECOND = 60;
	private static final int BUFFER_COUNT = 2;

	// Run settings
	private float targetFps, targetFixedUpdates;

	// Stats
	private static int fpsCount, upsCount;
	private boolean running = false;
	
	// Objects
	private final Thread thread;
	private final Display display;
	private Stage stage;
	private final Time time;

	// Loop management
	private final boolean vSync;

	// Variable initialisation
	public Engine(String windowTitle, Dimension resolution, boolean borderless, boolean vSync) {
		thread = new Thread(this, "game_loop");
		display = new Display(windowTitle, resolution, borderless);
		stage = new Stage(new TransitionRenderer());
		time = new Time();

		targetFps = DEFAULT_FPS;
		targetFixedUpdates = DEFAULT_FIXED_UPDATES_PER_SECOND;

		this.vSync = vSync;
	}

	private void init() {
		time.init();
		Input.init(display);
		display.resize();
		display.setVisible(true);
	}
	
	private void update() {
		time.update();
		stage.update();
		Input.pollEvents();
	}
	
	private void render() {
		// Initialise buffer strategy
		BufferStrategy bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(BUFFER_COUNT);
			return;
		}
		// Prepare drawing
		Graphics g = bs.getDrawGraphics();
		g.clearRect(0, 0, display.getSize().width, display.getSize().height);
		if(!(g instanceof Graphics2D))
			return;
		// Graphics 2D Converting
		Graphics2D g2d = (Graphics2D) g;

		// Stage drawing
		Draw.prepare(g2d);
		stage.render();

		// End drawing
		bs.show();
		g.dispose();
	}
	
	// Main game loop
	@Override
	public void run() {
		
		init();

		float elapsedTime;
		float accumulator = 0;
		float interval = 1f / targetFixedUpdates;

		int fpsCounter = 0;
		int upsCounter = 0;
		double lastTime = time.getTime();

		while(running) {
			elapsedTime = time.getElapsedTime();
			Time.deltaTime = elapsedTime;
			accumulator += elapsedTime;

			// Update
			update();

			// Fixed Update
			while(accumulator >= interval) {
				stage.fixedUpdate();
				accumulator -= interval;
				upsCounter++;
			}

			// Rendering
			render();
			fpsCounter++;

			// Data Collecting
			if(time.getTime() - lastTime >= 1) {
				lastTime = time.getTime();
				fpsCount = fpsCounter;
				upsCount = upsCounter;
				fpsCounter = 0;
				upsCounter = 0;
			}

			// Syncing
			if(!vSync)
				sync();
		}

		stage.cleanUp();
		System.exit(0);
	}

	/**
	 * Switches to a different stage. Formally that means first calling the {@code begin()} function of the given
	 * stage, than actually changing the current stage. Finally the old stage's {@code cleanUp()} function is being
	 * called.
	 *
	 * This method provides no transition effects or load time like the {@code Scene} objects do, and thus this
	 * should only be used outside of game purposes.
	 *
	 * @param stage the new stage.
	 */
	public void swapStage(Stage stage) {
		if(stage == null)
			stage = new Stage(new TransitionRenderer());
		stage.begin();
		Stage oldStage = this.stage;
		this.stage = stage;
		oldStage.cleanUp();
	}
	
	// Thread handling
	public void start() {
		if(running)
			return;
		running = true;
		thread.start();
	}
	
	public void stop() {
		if(!running)
			return;
		running = false;
	}

	public void sync() {
		float loopSlot = 1f / targetFps;
		double endTime = time.getLastLoopTime() + loopSlot;
		while(time.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ignored) {}
		}
	}
	
	// Getters and setters

	public float getTargetFps() {
		return targetFps;
	}

	public void setTargetFps(float targetFps) {
		this.targetFps = targetFps;
	}

	public float getTargetFixedUpdates() {
		return targetFixedUpdates;
	}

	public void setTargetFixedUpdates(float targetFixedUpdates) {
		this.targetFixedUpdates = targetFixedUpdates;
	}

	public static int getFpsCount() {
		return fpsCount;
	}
	
	public static int getUpsCount() {
		return upsCount;
	}

	public Display getDisplay() {
		return display;
	}

	public Stage getStage() {
		return stage;
	}

	public Time getTime() {
		return time;
	}
}