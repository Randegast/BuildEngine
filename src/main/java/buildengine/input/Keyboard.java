package buildengine.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {

    public static final int BUTTON_COUNT = 350;

    private final boolean[] down, press, release;
    private static List<CharacterListener> charListeners;

    public Keyboard() {
        down = new boolean[BUTTON_COUNT];
        press = new boolean[BUTTON_COUNT];
        release = new boolean[BUTTON_COUNT];

        charListeners = new ArrayList<>();
    }

    /** Updates after input circle */
    public void pollEvents() {
        for (int i = 0; i < BUTTON_COUNT; i++) {
            if(release[i] && !down[i])
                release[i] = false;
            if(press[i])
                press[i] = false;
        }
    }

    /** Resets all values */
    public void reset() {
        Arrays.fill(down, false);
        Arrays.fill(press, false);
        Arrays.fill(release, false);
        clearCharacterListeners();
    }

    public boolean isKeyDown(int glfw_key) {
        if(glfw_key >= BUTTON_COUNT)
            return false;
        return down[glfw_key];
    }

    public boolean isKeyPressed(int glfw_key) {
        if(glfw_key >= BUTTON_COUNT)
            return false;
        return press[glfw_key];
    }

    public boolean isKeyReleased(int glfw_key) {
        if(glfw_key >= BUTTON_COUNT)
            return false;
        return release[glfw_key];
    }

    public void clearCharacterListeners() {
        charListeners.clear();
    }

    public void registerCharacterListener(CharacterListener listener) {
        if(charListeners.contains(listener)) {
            System.err.println("WARNING: Tried to add duplicate character listeners.");
            return;
        }
        charListeners.add(listener);
    }

    public static void key_callback(long window, int key, int scancode, int action, int mods) {
        if(Input.isSleeping() && key <= 250) // Making an exception for keycodes bigger than 250
                                            // (F1 - F12 keys, escape and some other keys idk...)
            return;
        if(key >= BUTTON_COUNT)
            return;

        if(action == GLFW_PRESS) {
            Input.getKeyboard().down[key] = true;
            Input.getKeyboard().press[key] = true;
        }
        if(action == GLFW_RELEASE) {
            Input.getKeyboard().down[key] = false;
            Input.getKeyboard().release[key] = true;
        }
    }

    public static void character_callback(long window, int character) {
        if(Input.isSleeping())
            return;
        for(CharacterListener listener : charListeners)
            listener.keyTyped((char) character);
    }
}
