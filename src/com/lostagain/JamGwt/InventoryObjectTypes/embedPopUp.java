package com.lostagain.JamGwt.InventoryObjectTypes;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

import lostagain.nl.spiffyresources.client.spiffycore.SpiffyFunctions;


public class embedPopUp extends SimplePanel  implements hasCloseDefault,IsInventoryItemPopupContent,
 hasOpenDefault, isInventoryItemImplementation {
 boolean Magnifable = false;
	
 String sourceurl="";
 String sourcesizeX="";
 String sourcesizeY="";
 
	public embedPopUp(String URL, String sizeX, String sizeY,String EmbedCode){
		
		sourceurl = URL;
		sourcesizeX = sizeX;
		sourcesizeY= sizeY;
		
		
		HTML frame = new HTML (EmbedCode);
		//frame.setSize("100%", "100%");
		this.add(frame);
		
		this.setStylePrimaryName("notepadback");
		this.setSize( sourcesizeX+"px",sourcesizeY+"px");
		
		
	}

	public void CloseDefault() {
		
	}

	public void OpenDefault() {
		// TODO: mondernise style setting
		// has to have grey back, and be set to fixed zdepth
		 RootPanel.get().add(JAM.fadeback,0, 0);
       //ControlPanel.ShowDefault();
     DOM.setStyleAttribute(JAM.fadeback.getElement(), "zIndex", ""+(JAMcore.z_depth_max+1));
     DOM.setStyleAttribute(JAM.fadeback.getElement(), "z-index", ""+(JAMcore.z_depth_max+1));
     JAMcore.z_depth_max = JAMcore.z_depth_max+1;
    
		
	
		
		
	}

	public String POPUPTYPE() {
		return "EMBED";
	}
	public boolean POPUPONCLICK() {
		return true;
	}
	public void RecheckSize() {
		
	}

	public boolean DRAGABLE() {
		return false;
	}
	public boolean MAGNIFYABLE() {
		return Magnifable ;
	}

	public String getSourceURL() {
		return sourceurl;
	}

	public int sourcesizeX() {
		//safely remove any CSS first
		String removedAnyCss = SpiffyFunctions.StripCSSfromNumber(sourcesizeX);
		return Integer.parseInt(removedAnyCss);
	}

	public int sourcesizeY() {
		//safely remove any CSS first
		String removedAnyCss = SpiffyFunctions.StripCSSfromNumber(sourcesizeY);	
		return  Integer.parseInt(removedAnyCss);
	}

	@Override
	public String getState() {
		return null;
	}

	@Override
	public void loadState(String state) {
		
	}
	
	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}

	@Override
	public int getExpectedZIndex() {
		return 1900;
	}
}
