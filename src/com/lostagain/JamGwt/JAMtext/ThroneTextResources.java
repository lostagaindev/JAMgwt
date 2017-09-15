package com.lostagain.JamGwt.JAMtext;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface ThroneTextResources extends ClientBundle {

	
	
	@Source("ThroneLoadingStrings.txt")
	TextResource enloadingStrings();

	@Source("nlThroneLoadingStrings.txt")
	TextResource nlloadingStrings();
	

	@Source("inlinedFiles/Main_Game_Controll.txt")
	TextResource mainGameControll();

}
