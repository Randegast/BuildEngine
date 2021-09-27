package buildengine.time;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Scheduler {

    private static Scheduler scheduler;

    public static Scheduler get() {
        if(scheduler == null)
            scheduler = new Scheduler();
        return scheduler;
    }

    private final List<Event> events;
    private final List<Event> queue;

    private boolean paused;

    private Scheduler() {
        events = new ArrayList<>();
        queue = new ArrayList<>();
        paused = false;
    }

    public void update() {
        if(paused)
            return;
        // Adding events
        for(Iterator<Event> ir = queue.iterator(); ir.hasNext();) {
            Event e = ir.next();
            events.add(e);
            ir.remove();
        }
        // Updating events
        for(Iterator<Event> ir = events.iterator(); ir.hasNext();) {
            Event e = ir.next();
            if(e.isDestroyed())
                ir.remove();
            e.update();
        }
    }

    public static void addEvent(Event event) {
        get().queue.add(event);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

}
