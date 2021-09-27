package buildengine.input;

import buildengine.BuildEngine;
import org.joml.Vector2f;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {

    public static int BUTTON_COUNT = 5;

    private final boolean[] down, press, release;
    private double scroll;
    private double xPos, yPos, lastX, lastY;

    public Mouse() {
        scroll = 0.0d;
        xPos = 0.0d;
        yPos = 0.0d;
        lastX = 0.0d;
        lastY = 0.0d;

        down = new boolean[BUTTON_COUNT];
        press = new boolean[BUTTON_COUNT];
        release = new boolean[BUTTON_COUNT];
    }

    /** Updates after input circle */
    public void pollEvents() {
        for (int i = 0; i < BUTTON_COUNT; i++) {
            if(release[i] && !down[i])
                release[i] = false;
            if(press[i])
                press[i] = false;
        }
        scroll = 0;
        lastX = xPos;
        lastY = yPos;
    }

    /** Resets all values */
    public void reset() {
        Arrays.fill(down, false);
        Arrays.fill(press, false);
        Arrays.fill(release, false);
        scroll = 0;
        xPos = 0;
        yPos = 0;
    }

    public boolean isButtonDown(int glfw_button) {
        if(glfw_button >= BUTTON_COUNT)
            return false;
        return this.down[glfw_button];
    }

    public boolean isButtonPressed(int glfw_button) {
        if(glfw_button >= BUTTON_COUNT)
            return false;
        return this.press[glfw_button];
    }

    public boolean isButtonReleased(int glfw_button) {
        if(glfw_button >= BUTTON_COUNT)
            return false;
        return this.release[glfw_button];
    }

    public Vector2f getScreenPosition() {
        return BuildEngine.getEngine().getWindow().toNormalizedCoordinates(xPos, yPos);
    }

    public Vector2f getWorldPosition() {
        return BuildEngine.getEngine().getStage().getCamera().convertToWorldCoordinates(getScreenPosition());
    }

    public float getScroll() {
        return (float ) scroll;
    }

    public float getDeltaX() {
        return (float) (lastX - xPos);
    }

    public float getDeltaY() {
        return (float) (lastY - yPos);
    }

    public float getX() {
        return (float) xPos;
    }

    public float getY() {
        return (float) yPos;
    }
    
    // Static Mouse Handling

    public static void mouse_position_callback(long window, double xpos, double ypos) {
        if(Input.isSleeping())
            return;
        Input.getMouse().lastX = Input.getMouse().xPos;
        Input.getMouse().lastY = Input.getMouse().yPos;
        Input.getMouse().xPos = xpos;
        Input.getMouse().yPos = ypos;
    }

    public static void mouse_button_callback(long window, int button, int action, int mods) {
        if(Input.isSleeping())
            return;
        if(button >= Mouse.BUTTON_COUNT) {
            System.err.println("WARNING: Mouse button not callback not registered. " +
                    "Outisde of the defined scope of " + BUTTON_COUNT);
            return;
        }

        if(action == GLFW_PRESS) {
            Input.getMouse().down[button] = true;
            Input.getMouse().press[button] = true;
        }
        if(action == GLFW_RELEASE) {
            Input.getMouse().down[button] = false;
            Input.getMouse().release[button] = true;
        }
    }

    public static void scroll_callback(long window, double xOffset, double yOffset) {
        if(Input.isSleeping())
            return;
        Input.getMouse().scroll = yOffset;
    }
}
