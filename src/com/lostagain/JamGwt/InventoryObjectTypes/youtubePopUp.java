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


public class youtubePopUp extends SimplePanel  implements hasCloseDefault,
 hasOpenDefault, IsInventoryItemPopupContent,isInventoryItemImplementation {
String HTMLString = "<object width=\"SIZE_X\" height=\"SIZE_Y\"><param name=\"movie\" value=\"YOUTUBEURL&hl=en&fs=1&color1=0x234900&color2=0x4e9e00\"></param><param name=\"allowFullScreen\" value=\"false\"></param><param name=\"allowscriptaccess\" value=\"always\"></param><embed src=\"YOUTUBEURL&hl=en&fs=1&color1=0x234900&color2=0x4e9e00\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" width=\"SIZE_X\" height=\"SIZE_Y\"></embed></object>";

	public youtubePopUp(String URL,int size_x,int size_y){
		
		//fix for you tube embed url	
		URL=URL.replaceAll("watch\\?v\\=", "v/");
		
		//replace url	
		HTMLString=HTMLString.replaceAll("YOUTUBEURL", URL);
		HTMLString=HTMLString.replaceAll("SIZE_X", size_x+"");
		HTMLString=HTMLString.replaceAll("SIZE_Y", 12+size_y+"");
		
		JAMcore.GameLogger.info(HTMLString);
		
		HTML frame = new HTML (HTMLString);
		frame.setSize("100%", "100%");
		this.add(frame);
		
		
		
		this.setSize( 2+size_x+"px",12+size_y+"px");
		
		
		
	}

	public void CloseDefault() {
		
	}

	public void OpenDefault() {
		// has to have grey back, and be set to fixed zdepth
		 RootPanel.get().add(JAM.fadeback,0, 0);
        //ControlPanel.ShowDefault();
      DOM.setStyleAttribute(JAM.fadeback.getElement(), "zIndex", ""+(JAMcore.z_depth_max+1));
      DOM.setStyleAttribute(JAM.fadeback.getElement(), "z-index", ""+(JAMcore.z_depth_max+1));
      JAMcore.z_depth_max = JAMcore.z_depth_max+1;
     
		
	}

	public String POPUPTYPE() {
		return "MOVIE";
	}
	public boolean POPUPONCLICK() {
		return true;
	}
	public void RecheckSize() {
		
	}

	public boolean DRAGABLE() {
		return true;
	}
	public boolean MAGNIFYABLE() {
		return false;
	}

	@Override
	public String getSourceURL() {
		return null;
	}

	@Override
	public int sourcesizeX() {
		return 0;
	}

	@Override
	public int sourcesizeY() {
		return 0;
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
