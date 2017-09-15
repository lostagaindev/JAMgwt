package com.lostagain.JamGwt.JargScene.SceneObjects;

import com.allen_sauer.gwt.voices.client.Sound;
import com.lostagain.Jam.Factorys.IsJamSoundObject;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.audio.GwtAudioController;

public class GwtSoundObject implements IsJamSoundObject {

	//the internal class we use to implement the sound
	//we basically are wrapping this classes functions to conform to our IsJamSoundObject interface
	private	Sound newSoundImplementation;	
	
	//constructor to make a new object andsetup the internal implementation
	public GwtSoundObject(String soundFileName) {
		String stype = JAM.defaultSoundType;

		// note: we should override for all extensions in case the default
		// type doesn't match extension
		if (soundFileName.toLowerCase().endsWith(".mp3")) {
			stype = Sound.MIME_TYPE_AUDIO_MPEG_MP3;
		}
		if (soundFileName.toLowerCase().endsWith(".aac")) {
			stype = Sound.MIME_TYPE_AUDIO_MP4_MP4A_40_2;
		}
		if (soundFileName.toLowerCase().endsWith(".ogg")) {
			stype = Sound.MIME_TYPE_AUDIO_OGG_VORBIS;
		}
		
		newSoundImplementation = GwtAudioController.soundController.createSound(stype,soundFileName);
		
	}

	@Override
	public void setVolume(int vol) {		
		newSoundImplementation.setVolume(vol);		
		
	}

	
	@Override
	public void play() {
		newSoundImplementation.play();

	}

	@Override
	public void stop() {
		newSoundImplementation.stop();
	}

	/**
	 * not strictly needed I think? Just in case we need a way to remove sounds from memory?
	 * 
	 */
	@Override
	public void clear() {
		newSoundImplementation.stop();
		newSoundImplementation = null;
	}

	@Override
	public void resume() {
		newSoundImplementation.play();
	}

	@Override
	public void setLooping(boolean b) {
		newSoundImplementation.setLooping(b);
	}


}
