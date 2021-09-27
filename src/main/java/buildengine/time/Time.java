package buildengine.time;

public class Time {

    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }

    public static float getStartTime() {
        return (float) (timeStarted * 1E-9);
    }

}
