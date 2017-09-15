package com.lostagain.JamGwt.JAMtext;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;
import com.lostagain.JamGwt.IconPacks.JamImages;
import com.google.common.collect.ImmutableMap;

/** just like images, easily changes text */
public class MWaCText extends CommonText {

	static  MWaCTextResources StaticText = ( MWaCTextResources) GWT
			.create( MWaCTextResources.class);
	
	
	//Store all strings in hashmap with language extensions for them
	//All these strings must exist in the file class specified above
	static ImmutableMap<String,TextResource> gamesInternalText = ImmutableMap.of(
				
			
			//	"EN_loadingStrings",MWaCText.StaticText.enloadingStrings()
			
			//always store language extensions as lower case
			"en_"+StringNames.loadingStrings.toString(),MWaCText.StaticText.enloadingStrings(),				
			"nl_"+StringNames.loadingStrings.toString(),MWaCText.StaticText.nlloadingStrings()
	); 
}

