package buildengine.input;

import buildengine.core.Debug;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * All keyboard handling.
 * @see Input for the static instance
 */
public class Keyboard implements KeyListener {

    private static final int KEY_COUNT = 6455;

    private boolean[] pressed, released, hold;
    private List<KeyInputReader> inputReaders;

    public void init() {
        released = new boolean[KEY_COUNT];
        pressed = new boolean[KEY_COUNT];
        hold = new boolean[KEY_COUNT];

        inputReaders = new ArrayList<>();
    }

    public void pollEvents() {
        for (int i = 0; i < KEY_COUNT; i++) {
            if(released[i] && !hold[i])
                released[i] = false;
            if(pressed[i])
                pressed[i] = false;
        }
    }

    public void reset() {
        Arrays.fill(released, false);
        Arrays.fill(pressed, false);
        Arrays.fill(hold, false);
    }

    // Retrieving data

    /**
     * Checks if a key is currently being clicked in any form.
     * @param key	The keycode of the key that's possibly being clicked.
     * @param type  The action of the click. For example {@code ClickType.HOLD}
     * @return If the key is being pressed or not.
     */
    private boolean isClicked(int key, ClickType type) {
        switch (type) {
            case NO_ACTION -> { return !pressed[key] && !released[key] && !hold[key]; }
            case PRESSED -> {
                return pressed[key];
            }
            case RELEASED -> {
                return released[key];
            }
            case HOLD -> {
                return hold[key];
            }
        }
        return false;
    }

    public boolean isDown(int key) {
        return isClicked(key, ClickType.HOLD);
    }

    public boolean isPressed(int key) {
        return isClicked(key, ClickType.PRESSED);
    }

    public boolean isReleased(int key) {
        return isClicked(key, ClickType.RELEASED);
    }

    public boolean isAltDown() {
        return isClicked(KeyEvent.VK_ALT, ClickType.HOLD);
    }

    public boolean isShiftDown() {
        return isClicked(KeyEvent.VK_SHIFT, ClickType.HOLD);
    }

    public boolean isControlDown() {
        return isClicked(KeyEvent.VK_CONTROL, ClickType.HOLD);
    }

    public void registerKeyTypeListener(KeyInputReader listener) {
        inputReaders.add(listener);
    }

    // Events

    @Override
    public void keyTyped(KeyEvent e) {
        for(KeyInputReader inputReader : inputReaders)
            inputReader.inputReceived(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(isOutOfScope(e.getKeyCode()))
            return;
        hold[e.getKeyCode()] = true;
        pressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(isOutOfScope(e.getKeyCode()))
            return;
        if(!released[e.getKeyCode()] && !pressed[e.getKeyCode()])
            released[e.getKeyCode()] = true;
        hold[e.getKeyCode()] = false;
    }

    private boolean isOutOfScope(int key) {
        if(key >= KEY_COUNT) {
            Debug.msg("Did not handle key event (out of scope) for key " + key);
            return true;
        }
        return false;
    }
}
