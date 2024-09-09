package buildengine.audio;

import buildengine.core.Debug;

public class Music {
	
	private AudioClip music = new AudioClip();
	private boolean active = false;
	
	public Music() {}
	
	public void start() {
		if(Debug.isMusicMuted()) return;
		if(!music.getClip().isOpen())
			return;
		
		if(active)
			music.stop();
		if(!active)
			music.play(true);
		active = !active;
	}
	
	public void change(AudioClip audio) {
		if(Debug.isMusicMuted()) return;
		music.stop();
		music.close();
		music = audio;
		music.play(true);
	}

	public void setVolume(double volume) {
		if(Debug.isMusicMuted()) return;
		music.setVolume(volume);
	}
	
	public AudioClip getAudioClip() {
		return music;
	}

}
