package com.lostagain.JamGwt.JargScene.SceneObjects;

import com.google.gwt.dom.client.Style.Display;
import com.lostagain.Jam.Interfaces.IsBamfImage;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;

public class BamfImage extends GWTAnimatedIcon implements IsBamfImage {

	public BamfImage(String location) {
		super(location,1);
		
		//should be above everything (simple gwt method - high zindex)
		int defaultZindex = 90000;		
		setStyleName("BAMFPopup");
		getElement().setId("_BAMFPopup_");
		getElement().getStyle().setZIndex(defaultZindex);

		getElement().getStyle().clearDisplay();
	}

	@Override
	public void setOpacity(double opacity) {

		getElement().getStyle().setOpacity(opacity / 100.0);
		
		if (opacity==0){
			getElement().getStyle().setDisplay(Display.NONE);
		} else {

			getElement().getStyle().clearDisplay();
		}

	}

}
