package com.lostagain.JamGwt.Sprites;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * InternalSprites are a set of sprites designed to be used for internal animations
 * Unlike IconPacks, these are not for interface use and only have 1 size
 * 
 * 
 * @author Tom
 *
 */
public interface ThornInternalSprites extends ClientBundle {
	
		
	//water splash
	@Source("com/lostagain/JamGwt/JargScene/watersplash/sbc3waterslash0.png")
	ImageResource waterSplash0();
	
	@Source("com/lostagain/JamGwt/JargScene/watersplash/sbc3waterslash1.png")
	ImageResource waterSplash1();
	
	@Source("com/lostagain/JamGwt/JargScene/watersplash/sbc3waterslash2.png")
	ImageResource waterSplash2();
	
	@Source("com/lostagain/JamGwt/JargScene/watersplash/sbc3waterslash3.png")
	ImageResource waterSplash3();
	
	@Source("com/lostagain/JamGwt/JargScene/watersplash/sbc3waterslash4.png")
	ImageResource waterSplash4();

}
