package buildengine.audio;

import java.util.Random;

/**
 * AudioClip with repeating functionality. While the sound effect should be played, use {@see SoundEffect#play();}
 * This can be called in update();
 */
public class SoundEffect {
	
	private final AudioClip[] sounds;
	
	public SoundEffect(AudioClip... sounds) {
		this.sounds = sounds;
	}

	public void setVolume(double volume) {
		for(AudioClip audioClip : sounds) {
			audioClip.setVolume(volume);
		}
	}
	
	public void play() {
		for (AudioClip sound : sounds)
			if (sound.isRunning())
				return;
		
		Random r = new Random();
		sounds[r.nextInt(sounds.length)].play(false);
	}
}
