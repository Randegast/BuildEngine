package buildengine;

import buildengine.core.Engine;
import buildengine.imgui.ImGuiWindow;
import buildengine.editor.Console;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

/**
 * Main static class for the engine
 */
public class BuildEngine {

    private BuildEngine() {}

    /** Version: [major].[minor][extra] */
    public static final String VERSION = "0.1e";
    /** Build: [month].[year].[build] */
    public static final String BUILD = "09.2021.1";
    /** Stable build */
    public static final boolean stable = false;

    /** @return A string containing info about the current build */
    public static String welcome() {
        return "BuildEngine-GL v" + VERSION + " build " + BUILD;
    }

    private static Engine engine;

    public static Engine create() {
        return create(welcome());
    }
    public static Engine create(String windowTitle) {
        return create(windowTitle, Window.RESOLUTION_SD);
    }
    public static Engine create(String windowTitle, Vector2i resolution) {
        return create(new ImGuiWindow(windowTitle, resolution));
    }

    /**
     * Creates a new instance of Engine and starts it. Deploys a warning
     * if on an unstable build.
     * @param window   The window object to render to
     * @throws IllegalStateException If there already is another instance of Engine running.
     * @return The created engine
     */
    public static Engine create(Window window) {
        Console.log(BuildEngine.welcome());
        if(engine != null)
            throw new IllegalStateException("Can't create another engine instance: another instance is already running.");
        if(!stable)
            Console.warn("WARNING: Using an unstable build of BuildEngine.");
        engine = new Engine(window);
        engine.getThread().start();
        return getEngine();
    }

    /** Safely destroys the current engine instance */
    public static void destroy() {
        if(engine == null)
            return;
        GLFW.glfwWindowShouldClose(engine.getWindow().getId());
    }

    /** @return The current Engine instance */
    public static Engine getEngine() {
        return engine;
    }
}
