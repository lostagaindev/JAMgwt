package com.lostagain.JamGwt.JAMtext;

import com.google.gwt.resources.client.TextResource;
import com.lostagain.Jam.JAMcore;

/**
 * Class for managing internal text of the game engine.
 * Used for interface settings and other text burnt in at compile time.
 * 
 * Note; this class should extend the text file for your specific game.
 * eg. 
 * ThroneText, 
 * LandOfEdgeAndHueText,
 * MWaCText etc.
 * 
 * That file specifies the text hashmap array this one uses and itself references all the text resources
 * 
 * @author Tom
 *
 */
public class JamText extends MWaCText {

	
	/**
	 * gets the text for the current language in use (JAM.LanguageExtension)
	 * @param TextStringName
	 * @return
	 */
	public static String getTextForCurrentLanaguage(StringNames TextStringName){
				
		return getTextForLanaguage(JAMcore.LanguageExtension, TextStringName.toString());
		
		
	}
	
	
	
	/**
	 * Gets the text for the following name and language extension.
	 * Currently the only internal text is "loadingstrings"
	 * 
	 * @param LanExtension
	 * @param TextStringName
	 * @return
	 * 
	 **/
	public static String getTextForLanaguage(String LanExtension, String TextStringName){
		//lans are always stored lower case for internal text
		String name = LanExtension.toLowerCase()+"_"+TextStringName;
		TextResource whatever = gamesInternalText.get(name);
		if (whatever==null){
			return "no text \n for specified \n language:"+LanExtension+" and \n name:"+TextStringName;
		}
		String result = whatever.getText();
		
		
		return result;
		
		
	}
	
	
	/**
	 * This function checks for a external file override.
	 * That is, a internal file that is supposed to replace a external one.
	 * If none is found "null" is returned;
	 * 
	 * fileurl,data
	 **/
	public static String getExternalFileOverride(String fileurl){
		
		TextResource overridetext = gamesFileOverrides.get(fileurl);
		
		if (overridetext==null){
			return null;
		}
		
		return overridetext.getText();
	}
	
}
