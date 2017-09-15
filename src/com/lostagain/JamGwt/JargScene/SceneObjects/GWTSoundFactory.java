package com.lostagain.JamGwt.JargScene.SceneObjects;

import com.lostagain.Jam.Factorys.IsJamSoundObject;
import com.lostagain.Jam.Factorys.SoundFactory;

public class GWTSoundFactory extends SoundFactory {
	
	
	/**
	 * must be run before anything else will work
	 * (gives the superclass this instance)
	 */
	static public void setup(){
		setup(new GWTSoundFactory());
	}
		
	

	@Override
	public
	IsJamSoundObject createJamSoundObject(String soundFileName) {		
		return new GwtSoundObject(soundFileName);
	}

	
	//Thats it!
	//All this is, is a method to return a implementation of SoundObject specific to the GWT platform
	//Note; no where else in the code should even know or care about GWTSoundObjects, they only care that "something" they
	//have supports IsJamSoundObject functions.
	
}
