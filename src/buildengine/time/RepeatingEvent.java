package buildengine.time;

public class RepeatingEvent extends Event {

    private long interval;
    private boolean paused;

    private long timer;
    private long currentTime;
    private boolean requestReset;

    /**
     * Create a new scheduled event
     * @param interval in milliseconds
     * @param action to execute after the timer ran out
     */
    public RepeatingEvent(Runnable action, long interval) {
        super(action);
        restart(interval);
    }

    public void restart() {
        restart(interval);
    }

    public void restart(long interval) {
        if(interval <= 0)
            //noinspection UnusedAssignment
            paused = true;

        this.timer = interval;
        this.interval = interval;

        paused = false;
        requestReset = true;
    }

    @Override
    public void update() {
        if(paused)
            return;
        if(requestReset) {
            timer = interval;
            currentTime = System.currentTimeMillis();
            requestReset = false;
            destroyed = false;
        }
        if(destroyed)
            return;
        long lastTime = currentTime;
        currentTime = System.currentTimeMillis();
        timer -= currentTime - lastTime;
        if(timer <= 0) {
            action.run();
            timer = interval;
        }
    }

    public void resetProgress() {
        if(!destroyed) {
            requestReset = true;
            return;
        }
        timer = interval;
        currentTime = System.currentTimeMillis();
    }

    public long getProgress() {
        return timer;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public long getInterval() {
        return interval;
    }
}
