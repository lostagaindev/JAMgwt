package com.lostagain.JamGwt.Sprites;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class MeryllInternalAnimations {

	static MeryllInternalSprites InternalSprites = (MeryllInternalSprites) GWT
			.create(MeryllInternalSprites.class);

	// crumbles!
	final static AbstractImagePrototype[] CrumbleAnimation = {
			AbstractImagePrototype.create(InternalSprites.cookieCrumble0()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble1()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble2()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble3()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble4()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble5()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble6()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble7()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble8()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble9()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble10()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble11()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble12()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble13()),
			AbstractImagePrototype.create(InternalSprites.cookieCrumble14())		

	};
	
	//loading
	final static AbstractImagePrototype[] CookieLoadingAnimation = {
			AbstractImagePrototype.create(InternalSprites.cookieloading0()),
			AbstractImagePrototype.create(InternalSprites.cookieloading1()),
			AbstractImagePrototype.create(InternalSprites.cookieloading2()),
			AbstractImagePrototype.create(InternalSprites.cookieloading3()),
			AbstractImagePrototype.create(InternalSprites.cookieloading4()),
			AbstractImagePrototype.create(InternalSprites.cookieloading5()),
			AbstractImagePrototype.create(InternalSprites.cookieloading6()),
			AbstractImagePrototype.create(InternalSprites.cookieloading7()),
			AbstractImagePrototype.create(InternalSprites.cookieloading8()),

			AbstractImagePrototype.create(InternalSprites.cookieloading9()),
			AbstractImagePrototype.create(InternalSprites.cookieloading10()),

	};
	
	// Fire!
	final static AbstractImagePrototype[] FireAnimation = {
			AbstractImagePrototype.create(InternalSprites.fireloop0050()),
			AbstractImagePrototype.create(InternalSprites.fireloop0054()),
			AbstractImagePrototype.create(InternalSprites.fireloop0058()),
			AbstractImagePrototype.create(InternalSprites.fireloop0062()),
			AbstractImagePrototype.create(InternalSprites.fireloop0066()),
			AbstractImagePrototype.create(InternalSprites.fireloop0070()),
			AbstractImagePrototype.create(InternalSprites.fireloop0074()),
			AbstractImagePrototype.create(InternalSprites.fireloop0078()),
			AbstractImagePrototype.create(InternalSprites.fireloop0082()),
			AbstractImagePrototype.create(InternalSprites.fireloop0086()),
			AbstractImagePrototype.create(InternalSprites.fireloop0090()),
			AbstractImagePrototype.create(InternalSprites.fireloop0094()),
			AbstractImagePrototype.create(InternalSprites.fireloop0100())		

	};

	//sparks!	
	final static AbstractImagePrototype[] SparkAnimation = {
			AbstractImagePrototype.create(InternalSprites.spark00()),
			AbstractImagePrototype.create(InternalSprites.spark01()),
			AbstractImagePrototype.create(InternalSprites.spark02()),
	};

	//impact!	 (set to cartoony, there is other options)
	final static AbstractImagePrototype[] ImpactAnimation = {
			AbstractImagePrototype.create(InternalSprites.impact_cartoony00()),
			AbstractImagePrototype.create(InternalSprites.impact_cartoony01()),
			AbstractImagePrototype.create(InternalSprites.impact_cartoony02()),
			AbstractImagePrototype.create(InternalSprites.impact_cartoony03()),

	};



	/**
	 * the above specified animations need to be given a associated name so the game script can access them (ie, in a sprite object)
	 */
	final static HashMap<String, AbstractImagePrototype[]> allGameSpecificAnimations = new HashMap<String, AbstractImagePrototype[]>(){
		{
			//scene animations
			put("fire",FireAnimation);
			put("crumble",CrumbleAnimation);
			put("sparks",SparkAnimation);
			put("impact",ImpactAnimation);
			put("loading",CookieLoadingAnimation); //all games probably should have this
		}
	};
}
