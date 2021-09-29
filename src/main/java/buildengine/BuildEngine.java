package buildengine;

import buildengine.core.Engine;
import buildengine.imgui.ImGuiWindow;
import buildengine.editor.Console;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

/**
 * <h1>BuildEngine</h1>
 * This class contains utility methods for creating and managing the main {@code Engine} instance and
 * global information like the current build version.
 * @implNote This is the main way to create an {@code Engine} instance. Initializing
 * the engine any other way is not recommended.
 * @author Kai van Maurik
 * @since 1.0
 */
public class BuildEngine {

    // Suppresses default constructor, ensuring non-instantiability.
    private BuildEngine() {}

    /**
     * The current version of BuildEngine as {@code String}.<br>
     * <i>Format: {@code [major].[minor]} </i>
     */
    public static final String VERSION = "1.0";

    /**
     * The current build of BuildEngine as {@code String}. A build describes the sub-version of the main version.
     * In addition, the build describes the month, year and status (s for stable and e for experimental).<br>
     * <i>Format: {@code [month].[year].[build][version-status]}</i>
     */
    public static final String BUILD = "10.2021.1e";

    /**
     * Describes the stability of the current build.
     */
    public static final boolean IS_STABLE = false;

    /**
     * A {@code String} containing the BuildEngine's branch, version and build.
     */
    public static final String NAME = "BuildEngine-GL v" + VERSION + " build " + BUILD;

    /**
     * The main static {@code Engine} instance.
     */
    private static Engine engine;

    /**
     * Initializes a new {@code Engine} instance, using the {@link BuildEngine#NAME BuildEngine name} as window
     * title, using the standard window resolution (1280x720).
     * @throws IllegalStateException if there already is another instance of Engine running.
     */
    public static void create() {
        create(NAME);
    }

    /**
     * Initializes a new {@code Engine} instance, using the standard window resolution (1280x720).
     * @param windowTitle The window title as {@code String}
     * @throws IllegalStateException if there already is another instance of Engine running.
     */
    public static void create(String windowTitle) {
        create(windowTitle, Window.RESOLUTION_SD);
    }

    /**
     * Initializes a new {@code Engine} instance.
     * @param windowTitle The window title as {@code String}
     * @param resolution The window resolution in pixels. To use some default resolutions use the following {@code Window} class
     *                   constants: {@link Window#RESOLUTION_SD RESOLUTION_SD}, {@link Window#RESOLUTION_HD RESOLUTION_HD} or
     *                   {@link Window#RESOLUTION_4K RESOLUTION_4K}.
     * @throws IllegalStateException if there already is another instance of Engine running.
     */
    public static void create(String windowTitle, Vector2i resolution) {
        Console.log(NAME);
        if(engine != null)
            throw new IllegalStateException("Can't create another engine instance: another instance is already running.");
        if(!IS_STABLE)
            Console.warn("WARNING: Using an unstable build of BuildEngine.");
        engine = new Engine(new ImGuiWindow(windowTitle, resolution));
        engine.getThread().start();
    }

    /** Safely destroys the current engine instance */
    public static void destroy() {
        if(engine == null)
            return;
        GLFW.glfwWindowShouldClose(engine.getWindow().getId());
    }

    /**
     * Gets the current static {@code Engine} instance
     * @return the current {@code Engine} instance, or {@code null} if no {@code Engine} was created.
     * @see BuildEngine#create(String, Vector2i)
     */
    public static Engine getEngine() {
        return engine;
    }
}
