package buildengine.input;

public class Input {

    private static boolean sleep;

    private static Mouse mouse;
    private static Keyboard keyboard;

    private Input() {}

    public static void setSleep(boolean sleep) {
        Input.sleep = sleep;
    }

    public static void reset() {
        getMouse().reset();
        getKeyboard().reset();
    }

    public static void pollEvents() {
        getMouse().pollEvents();
        getKeyboard().pollEvents();
    }

    public static Mouse getMouse() {
        if(mouse == null)
            mouse = new Mouse();

        return mouse;
    }

    public static Keyboard getKeyboard() {
        if(keyboard == null)
            keyboard = new Keyboard();

        return keyboard;
    }

    public static boolean isSleeping() {
        return sleep;
    }
}
