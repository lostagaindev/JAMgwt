package com.lostagain.JamGwt.JAMtext;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.resources.client.TextResource;

public class CommonText {

	public enum StringNames{
		loadingStrings
	}
	

	/**
	 * Overrides file loads to use internal files.
	 * File manager checks these files first before loading anything from the file system
	 * 
	 * The url is relative to the route of the Game Data Files_#### directory, as that directory should be hidden from client
	 * side code by using a php relay.
	 * 
	 */	
	static ImmutableMap<String,TextResource> gamesFileOverrides = ImmutableMap.of(
			//relative url, data
			
			//below is the toggle for inlineing hte main game controll script, which is a simple way to make it uneditable
			//easily for offline games
			//	"Game Controll Script/Main_Game_Controll.txt",ThroneText.StaticText.mainGameControll()
	); 
	

	
}
