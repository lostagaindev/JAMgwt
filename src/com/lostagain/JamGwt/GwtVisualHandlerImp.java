package com.lostagain.JamGwt;

import java.util.logging.Logger;

import com.lostagain.Jam.SceneObjects.Interfaces.IsInterfaceVisualHandler;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryItem;

public class GwtVisualHandlerImp implements IsInterfaceVisualHandler {
	static Logger Log = Logger.getLogger("JAM.GwtVisualHandlerImp");

	@Override
	public void setHeldItemVisualiserVisible(boolean status) {
		Log.info("___________setHeldItemVisualiserVisible= " + status );				

		JAM.heldItemBox.setVisible(status);

	}

	@Override
	public void setHeldItemVisualisation(IsInventoryItem holdThis) {

		JAM.heldItemBox.setCurrentInventorySource(holdThis.getNativeInventoryPanel());
		JAM.heldItemBox.setCurrentImage(((InventoryItem)holdThis).getDataURL());
		
	}

	
	@Override
	public String getCurrentFeedbackText() {
		return JAM.Feedback.getCurrentText();
	}
	
	@Override
	public void setCurrentFeedbackText(String text,boolean withoutSound) {
		JAM.Feedback.setText(text,withoutSound);		
	}
	
	@Override
	public void setCurrentFeedbackText(String text) {
		setCurrentFeedbackText(text,false);
		
	}
	@Override
	public void setCurrentFeedbackTextDelay(int delay) {
		JAM.Feedback.setDelay(delay);	
	}

	@Override
	public void setCurrentFeedbackTextSpeed(int parseInt) {
		JAM.Feedback.setSpeed(parseInt);	
	}

	@Override
	public void setCurrentFeedbackRunAfter(Runnable runnable) {
		JAM.Feedback.setRunAfterTextSet(runnable);
				
	}

	@Override
	public void setFeedbackKeyBeep(String soundName) {
		JAM.Feedback.setCUSTOM_KEY_BEEP(soundName);
	}

	@Override
	public void setFeedbackSpaceKeyBeep(String soundName) {
		JAM.Feedback.setCUSTOM_SPACEKEY_BEEP(soundName);
	}

}
