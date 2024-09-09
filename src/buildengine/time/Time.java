package buildengine.time;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Time {
	
	public static float deltaTime;
	private double lastLoopTime;
	
	private static List<Event> events;
	private static List<Event> queue;
	private long lastTime;
	private long currentTime;
	private long totalTime;
	
	private boolean paused;
	
	public Time() {
		events = new ArrayList<>();
		queue = new ArrayList<>();
		paused = false;
	}
	
	public void init() {
		lastLoopTime = getTime();
		lastTime = -1;
		totalTime = 0;
		lastTime = System.currentTimeMillis();
		deltaTime = 0.15f;
	}
	
	public void update() {
		currentTime = System.currentTimeMillis();
		totalTime += currentTime - lastTime;
		lastTime = currentTime;
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

	public float getElapsedTime() {
		double time = getTime();
		float elapsedTime = (float) (time - lastLoopTime);
		lastLoopTime = time;
		return elapsedTime;
	}

	public static void addEvent(Event event) {
		queue.add(event);
	}

	public static void clearEvents() {
		for(Event event : events)
			event.setDestroyed(true);
	}

	public double getLastLoopTime() {
		return lastLoopTime;
	}

	public double getTime() {
		return System.nanoTime() / 1000_000_000.0d;
	}

	public  long getTotalTime() {
		return totalTime;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}
