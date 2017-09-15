package com.lostagain.JamGwt;

import java.util.logging.Logger;


import com.google.gwt.user.client.Window;
import com.lostagain.Jam.BasicGameFunctions;
import com.lostagain.Jam.RequiredImplementations;

import lostagain.nl.spiffyresources.client.IsSpiffyGenericLogger;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyLogBox;

public class BasicGameFunctionsImp implements BasicGameFunctions {

	static Logger Log = Logger.getLogger("JAM.BasicGameFunctionsImp");
	
	@Override
	public void setWindowTitle(String title) {
		
		Window.setTitle(title);
	}

	@Override
	public void resetGame() {
		
		Log.info("clearing visual chapter list");	
		//clear and recrate chapter list
		//(the following could be done better by giving ChapterList a proper clear function)
		JAM.ChapterList.VTabPanel.clear();
		JAM.ChapterList.clear();
		JAM.ChapterList.removeFromParent();
		JAM.ChapterList = new VerticalTabs();
		JAM.ChapterList.setSize("100%", "100%"); 
		JAM.ChapterList.addStyleName("standard_message_back"); 
		RequiredImplementations.PositionByTag(JAM.ChapterList, "chapterlist");
		//---
	}

	@Override
	public IsSpiffyGenericLogger getLogger(){
		return SpiffyLogBox.createLogBox(true);
	}

	@Override
	public void postDatabaseLoadSetup() {
		//put text IDs into control panel (as we have to wait for the database to load in order to know what lans are avliable!)
		JAM.ControlPanel.updateLans(); //TODO: we probably need a generic callback that other implementations can access when the text database is loaded
	
	}
	
	
}
