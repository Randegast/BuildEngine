package buildengine.time;

public abstract class Event {

    protected final Runnable action;
    protected boolean destroyed;

    public Event(Runnable action) {
        this.action = action;
        destroyed = false;
    }

    public abstract void update();

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

}
