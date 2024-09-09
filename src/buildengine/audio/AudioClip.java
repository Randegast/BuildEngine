package buildengine.audio;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Basic audio functionality
 */
public class AudioClip {
	
	private Clip clip;
	
	public AudioClip() {
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public AudioClip(AudioInputStream ais) {
		try {
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (LineUnavailableException | IOException e) {
			System.err.println("Error while trying to create/open AudioClip.");
			e.printStackTrace();
		}
	}
	
	// Clip management
	
	public void play(boolean repeat) {
		if(clip == null)
			return;
		stop();
		clip.setFramePosition(0);
		if(repeat)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
	}
	
	public void stop() {
		if(clip.isRunning())
			clip.stop();
	}
	
	public void close() {
		stop();
		if(clip.isOpen())
			clip.close();
	}
	
	// Audio management
	
	public void setVolume(double amt0_1) {
		FloatControl gainControl = (FloatControl) clip
		        .getControl(FloatControl.Type.MASTER_GAIN);
		    double gain = amt0_1;
		    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
		    gainControl.setValue(dB);
	}
	
	public void setMute(boolean mute) {
		BooleanControl muteControl = (BooleanControl) clip
		        .getControl(BooleanControl.Type.MUTE);
		    muteControl.setValue(mute);
	}
	
	// Getters and setters
	
	public boolean isRunning() {
		return clip.isRunning();
	}
	
	public Clip getClip() {
		return clip;
	}

}
