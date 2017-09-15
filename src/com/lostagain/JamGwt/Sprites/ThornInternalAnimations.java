package com.lostagain.JamGwt.Sprites;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ThornInternalAnimations {
	
	static ThornInternalSprites InternalSprites = (ThornInternalSprites) GWT
			.create(ThornInternalSprites.class);
			
	// crumbles!
				final static AbstractImagePrototype[] WaterSplash = {
					
					AbstractImagePrototype.create(InternalSprites.waterSplash0()),
					AbstractImagePrototype.create(InternalSprites.waterSplash1()),
					AbstractImagePrototype.create(InternalSprites.waterSplash2()),
					AbstractImagePrototype.create(InternalSprites.waterSplash3()),
					AbstractImagePrototype.create(InternalSprites.waterSplash4())		
				
				};
				
				final static HashMap<String, AbstractImagePrototype[]> allGameSpecificAnimations = new HashMap<String, AbstractImagePrototype[]>(){
					{
					put("watersplash",WaterSplash); //<watersplash>
					}
				};
}
