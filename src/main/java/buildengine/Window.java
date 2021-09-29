package buildengine;

import buildengine.input.Keyboard;
import buildengine.input.Mouse;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    public static Vector2i RESOLUTION_SD = new Vector2i(1280, 720),
                           RESOLUTION_HD = new Vector2i(1920, 1080),
                           RESOLUTION_4K = new Vector2i(3840, 2160);

    private final String title;
    private final Vector2i size;

    // Window handle
    protected long windowId;
    protected String glslVersion;

    public Window(String title, Vector2i size) {
        this.title = title;
        this.size = size;
    }

    public void init() {
        // Window
        GLFWErrorCallback.createPrint(System.err);
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Version
        glslVersion = "#version 330";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }

        // Window hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        // Creating window
        windowId = glfwCreateWindow(size.x, size.y, title, NULL, NULL);
        if(windowId == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Centering
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowId, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    windowId,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        // Input
        glfwSetCursorPosCallback(windowId, Mouse::mouse_position_callback);
        glfwSetMouseButtonCallback(windowId, Mouse::mouse_button_callback);
        glfwSetScrollCallback(windowId, Mouse::scroll_callback);
        glfwSetKeyCallback(windowId, Keyboard::key_callback);
        glfwSetCharCallback(windowId, Keyboard::character_callback);
        glfwSetFramebufferSizeCallback(windowId, Window::resizeCallback);

        // Finalizing
        glfwMakeContextCurrent(windowId);
        glfwSwapInterval(1); // enable vSync

        // Create capabilities before showing window (showing window -> resizeCallBack -> glViewport)
        GL.createCapabilities();

        // Reveling
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwShowWindow(windowId);
    }

    /**
     * Translate pixel coordinates to OpenGL normalized coordinates. Formally, divides the coordinates by the
     * size of the window, multiplying by two and subtracting one. Finally, the y coordinate is flipped because
     * opengl is bottom left centered.
     * @param x The x coordinate to translate
     * @param y The y coordinate to translate
     * @return The translated normalized coordinates. Center of the screen will be (0,0), bottom left corner
     * being (-1,-1) and top right being (1,1).
     */
    public Vector2f toNormalizedCoordinates(double x, double y) {
        return new Vector2f((float) (x / size.x) * 2 - 1, (float) ((y / size.y) * 2 - 1) * -1);
    }

    public static void resizeCallback(long window, int width, int height) {
        if(glfwGetCurrentContext() == window)
            GL12.glViewport(0, 0, width, height);
        if(BuildEngine.getEngine().getWindow().getId() == window)
            BuildEngine.getEngine().getWindow().size.set(width, height);
        if(BuildEngine.getEngine().getStage() == null)
            return;
        BuildEngine.getEngine().getStage().resize(width, height);
    }

    public void terminate() {
        glfwFreeCallbacks(windowId);
        glfwDestroyWindow(windowId);

        glfwTerminate();
        glfwSetErrorCallback(null);
    }

    public long getId() {
        return windowId;
    }

    public String getTitle() {
        return title;
    }

    public Vector2i getSize() {
        return size;
    }
}
