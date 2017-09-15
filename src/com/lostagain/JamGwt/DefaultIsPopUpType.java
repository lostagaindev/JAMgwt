package com.lostagain.JamGwt;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isPopupTypeImplementation;

public class DefaultIsPopUpType implements isPopupTypeImplementation,hasCloseDefault,hasOpenDefault {

	boolean draggable = false;
	private Widget contents;

	static Logger Log = Logger.getLogger("JAM.DefaultIsPopUpType");
	
			
	public DefaultIsPopUpType(Widget contents,boolean draggable) {
		super();
		this.draggable = draggable;
		this.contents  = contents;
	}

	

	@Override
	public Widget asWidget() {
		return contents;
	}

	@Override
	public boolean POPUPONCLICK() {
		return false;
	}

	@Override
	public boolean DRAGABLE() {
		return draggable;
	}

	@Override
	public String POPUPTYPE() {
		return null;
	}

	@Override
	public void RecheckSize() {

	}
	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}

	@Override
	public void OpenDefault() {
		((hasOpenDefault)contents).OpenDefault();
	}

	@Override
	public void CloseDefault() {

		Log.info("hasCloseDefault:");
		((hasCloseDefault)contents).CloseDefault();
	}

}
