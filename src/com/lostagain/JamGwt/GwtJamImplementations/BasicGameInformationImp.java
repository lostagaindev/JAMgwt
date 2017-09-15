package com.lostagain.JamGwt.GwtJamImplementations;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.lostagain.Jam.Interfaces.BasicGameInformation;





public class BasicGameInformationImp implements BasicGameInformation {

	/** 
	 * Gets settings from html file.
	 * Settings from here gets sent to the JAMCore  
	 * **/
	public static Dictionary theme = Dictionary.getDictionary("gamesettings");
	
	/**
	 * GWT.getHostPageBaseURL(),,
			, ,,,,);
	 */

	@Override
	public int getCurrentGameStageWidth() {
		return Window.getClientWidth();
	}

	@Override
	public int getCurrentGameStageHeight() {
		return Window.getClientHeight();
	}

	@Override
	public String getHomedirectory() {
		return GWT.getHostPageBaseURL();
	}
	
	static public String staticGetHomedirectory() {
		return GWT.getHostPageBaseURL();
	}
	@Override
	public String getSecureFolderLocation() {
		return 	theme.get("localloc");
	}
	
	public static String staticGetLocalFolderLocation() {
		return theme.get("localloc");
	}
	
	public static String staticGetAnimatedSpriteMode() {
		return theme.get("spritemode");
	}
	
	@Override
	public String getLanguageExtension() {
		return theme.get("language");
	}

	@Override
	public boolean getRequiredLogin() {
		return 	Boolean.parseBoolean(theme.get("requirelogin"));
	}
	

	@Override
	public boolean getHasServerSave() {
		return Boolean.parseBoolean(theme.get("hasserversave"));
	}

	@Override
	public boolean getDisableAutoSave() {
		return 	Boolean.parseBoolean(theme.get("disableautosave"));
	}

	@Override
	public String getQuality() {
		return 
				theme.get("quality");
	}

	@Override
	public String getDebugSetting() {
		return 
				theme.get("debug");
	}

	

	@Override
	public String getAudioLocation() {
		return getHomedirectory()+ "audio/";
	}

	@Override
	public String getGameName() {
		return theme.get("gamename");
	}


	
	public double getMaximumShrinkFactor() {
		return 1;
	}

	@Override
	public String getSemanticsLocation() {
		if(theme.keySet().contains("semanticfile")){
			return theme.get("semanticfile");
		}		
		return "";
	}

	@Override
	public boolean setToNeverRequestFilesWithPost() {
		if(theme.keySet().contains("neverusepost")){
			return Boolean.parseBoolean(theme.get("neverusepost"));
		}	
		return false;
	}

	
	
}