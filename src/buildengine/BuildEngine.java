package buildengine;

import buildengine.core.Engine;

import java.awt.*;

/**
 * Main static class for the engine
 */
public class BuildEngine {

    @SuppressWarnings("unused")
    private BuildEngine() {}

    /** Version: [major].[minor][extra] */
    public static final String VERSION = "1s";
    /** Build: [month].[year].[build] */
    public static final String BUILD = "09.2024.1";
    /** Stable build */
    public static final boolean stable = false;

    /** Default foreground color */
    public static final Color COLOR = new Color(94, 203, 220);
    /** Default background color */
    public static final Color BACKGROUND_COLOR = new Color(5,95,95);
    /** Default font */
    public static final Font FONT = new Font("Courier New", Font.PLAIN, 15);

    /** @return A string containing info about the current build */
    public static String welcome() {
        return "BuildEngine JAVA v" + VERSION + " build " + BUILD + " " + (stable ? "STABLE" : "EXPERIMENTAL");
    }

    // Engine instance
    private static Engine engine;

    /**
     * Creates a new instance of Engine and starts it. Deploys a warning
     * if on an unstable build.
     * @param windowTitle   The title of the display
     * @param resolution    The resolution of the display, in pixels.
     *                      Use {@code Display.RESOLUTION}.
     * @param borderless    Start display borderless
     * @param vSync         Enable vSync
     * @throws IllegalStateException If there already is another instance of Engine running.
     * @return The created engine
     */
    public static Engine create(String windowTitle, Dimension resolution, boolean borderless, boolean vSync) {
        if(engine != null)
            throw new IllegalStateException("Can't create another engine instance: another instance is already running.");
        if(!stable)
            System.err.println("WARNING: Using an unstable build of BuildEngine.");
        engine = new Engine(windowTitle, resolution, borderless, vSync);
        engine.start();
        return getEngine();
    }

    /** Safely destroys the current engine instance */
    public static void destroy() {
        if(engine == null)
            return;
        engine.stop();
        engine = null;
    }

    /** @return The current Engine instance */
    public static Engine getEngine() {
        return engine;
    }
}
