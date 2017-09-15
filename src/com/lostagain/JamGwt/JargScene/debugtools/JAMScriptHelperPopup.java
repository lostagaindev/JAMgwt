package com.lostagain.JamGwt.JargScene.debugtools;

import com.lostagain.Jam.InstructionProcessing.CommandLine.GameCommands;
import lostagain.nl.spiffyresources.client.spiffygwt.GenericScriptHelperPopup;

import lostagain.nl.spiffyresources.client.spiffygwt.GenericScriptHelperPopup;

public class JAMScriptHelperPopup extends GenericScriptHelperPopup {

	public JAMScriptHelperPopup() {
		super(GameCommands.values());
				super.getElement().getStyle().setZIndex(9999999); //arb high
				super.getElement().getStyle().setBackgroundColor("rgb(166, 139, 255)");
				
				//Note; currently we are only using "- " to detect the start of messages being typed
				//we probably should look for just "-" as well? 
				//(unless we are only looking for one character anyway?---should check)
				optionalSpacesAfterStartMaker = true;
				
	}


}
