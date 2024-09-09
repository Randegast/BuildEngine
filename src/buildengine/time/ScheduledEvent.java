package buildengine.time;

public class ScheduledEvent extends Event {

	private long timer;

    private long currentTime;

	/**
	 * Create a new scheduled event
	 * @param timer in milliseconds
	 * @param action to execute after the timer ran out
	 */
	public ScheduledEvent(Runnable action, long timer) {
		super(action);
		this.timer = timer;
		currentTime = System.currentTimeMillis();
	}

	@Override
	public void update() {
		if(destroyed)
			return;
        long lastTime = currentTime;
		currentTime = System.currentTimeMillis();
		timer -= currentTime - lastTime;
		if(timer <= 0) {
			action.run();
			setDestroyed(true);
		}
	}
	
	public long getProgress() {
		return timer;
	}

}
