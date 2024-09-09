package buildengine.graphics.renderer;

import buildengine.Unstable;
import buildengine.graphics.Sprite;
import buildengine.utils.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * Basic animation utility.
 * Sprite has its own Animation utility.
 * @see Sprite
 */
public class Animation {

	// Settings
	private final BufferedImage[] frames;
	private boolean playOnlyOnce, playing;

	// Animating
	private long frameInterval; // Animation speed
	private long internalTimer;
	private int index;

	/**
	 * Create instance
	 * @param speed The speed in ms it takes to go to the next frame
	 * @param frames The images that makes up the animation
	 */
	public Animation(long speed, BufferedImage[] frames) {
		this(speed, frames, false);
	}
	
	public Animation(long speed, BufferedImage[] frames, boolean playOnlyOnce) {
		this.frames = frames;
		this.playOnlyOnce = playOnlyOnce;
		this.playing = true;
		this.frameInterval = speed;

		internalTimer = 0;
		index = 0;
	}

	@Unstable(concerning = "NOT_TESTED")
	public void reverse() {
		for(int i=0; i<frames.length/2; i++){
			BufferedImage frame = frames[i];
			frames[i] = frames[frames.length -i -1];
			frames[frames.length -i -1] = frame;
		}
	}

	public void flipFramesHorizontally() {
		for (int i = 0; i < frames.length; i++)
			frames[i] = ImageUtils.flipHorizontally(frames[i]);
	}

	public void flipFramesVertically() {
		for (int i = 0; i < frames.length; i++)
			frames[i] = ImageUtils.flipVertically(frames[i]);
	}
	
	// Getters and setters

	public BufferedImage getCurrentFrame() {
		return frames[index];
	}

	public BufferedImage[] getFrames() {
		return frames;
	}

	public boolean isPlayingOnlyOnce() {
		return playOnlyOnce;
	}

	public void setPlayOnlyOnce(boolean playOnlyOnce) {
		this.playOnlyOnce = playOnlyOnce;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	// Animation

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getFrameInterval() {
		return frameInterval;
	}

	public void setFrameInterval(long frameInterval) {
		this.frameInterval = frameInterval;
	}

	public long getInternalTimer() {
		return internalTimer;
	}

	public void setInternalTimer(long internalTimer) {
		this.internalTimer = internalTimer;
	}
}
