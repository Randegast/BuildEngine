package buildengine.input;

import buildengine.core.Display;

/**
 * Contains static instances of Mouse and Keyboard.
 * Main way to register and access mouse and keyboard input.
 * @see Mouse
 * @see Keyboard
 */
public class Input {

    // Keyboard and mouse instances
    private static final Mouse mouse = new Mouse();
    private static final Keyboard keyboard = new Keyboard();

    private Input() {}

    /** Mouse & Keyboard init and adding listeners to display */
    public static void init(Display display) {
        mouse.init();
        keyboard.init();
        display.getCanvas().addMouseListener(mouse);
        display.getCanvas().addMouseMotionListener(mouse);
        display.getCanvas().addMouseWheelListener(mouse);
        display.addKeyListener(keyboard);
    }

    /** Updating mouse and keyboard after input loop */
    public static void pollEvents() {
        mouse.pollEvents();
        keyboard.pollEvents();
    }

    /** Reset all input */
    public static void reset() {
        mouse.reset();
        keyboard.reset();
    }
    
    // Getters and setters

    public static Mouse getMouse() {
        return mouse;
    }

    public static Keyboard getKeyboard() {
        return keyboard;
    }
}
