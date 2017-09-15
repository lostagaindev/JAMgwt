package com.lostagain.JamGwt.JAMtext;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;
import com.lostagain.JamGwt.IconPacks.JamImages;
import com.google.common.collect.ImmutableMap;

/** stores the text names and array for one game - has to have a matching resource file that points to the text contents */
public class ThroneText extends CommonText {

	static  ThroneTextResources StaticText = ( ThroneTextResources) GWT
			.create( ThroneTextResources.class);
	
	
	//public enum StringNames{
	////	loadingStrings
	//}
	
	//Store all strings in hashmap with language extensions for them
	//All these strings must exist in the file class specified above
	//Note; We use the enums above for the bit after the language extension to prevent typos
	//Other code can then reference those stringnames rather then type the string themselves
	static ImmutableMap<String,TextResource> gamesInternalText = ImmutableMap.of(
			//always store language extensions as lower case
				"en_"+StringNames.loadingStrings.toString(),ThroneText.StaticText.enloadingStrings(),				
				"nl_"+StringNames.loadingStrings.toString(),ThroneText.StaticText.nlloadingStrings()
	); 
	

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
			
			//below is the toggle for inlineing hte main game control script, which is a simple way to make it uneditable
			//easily for offline games
			//	"Game Controll Script/Main_Game_Controll.txt",ThroneText.StaticText.mainGameControll()
	); 
	
}

