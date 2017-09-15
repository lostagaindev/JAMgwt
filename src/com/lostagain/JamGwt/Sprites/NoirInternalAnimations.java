package com.lostagain.JamGwt.Sprites;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class NoirInternalAnimations {
	
	static NoirInternalSprites InternalSprites = (NoirInternalSprites) GWT
			.create(NoirInternalSprites.class);
			
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
				
				final static HashMap<String, AbstractImagePrototype[]> allAnimations = new HashMap<String, AbstractImagePrototype[]>(){
					{
					put("fire",FireAnimation);
					put("crumble",CrumbleAnimation);
					}
				};
}
