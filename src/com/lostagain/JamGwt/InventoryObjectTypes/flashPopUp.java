package com.lostagain.JamGwt.InventoryObjectTypes;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

import lostagain.nl.spiffyresources.client.spiffycore.SpiffyFunctions;


public class flashPopUp extends SimplePanel  implements hasCloseDefault,IsInventoryItemPopupContent,
 hasOpenDefault, isInventoryItemImplementation {
//String HTMLString = "<object width=\"SIZE_X\" height=\"SIZE_Y\"><param name=\"movie\" value=\"FILENAME.SWF\"><embed src=\"FILENAME.SWF\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" width=\"SIZE_X\" height=\"SIZE_Y\"></embed></object>";

	//String HTMLString = "<object height=\"698\" width=\"894\"> <param  name=\"movie\" value=\"InventoryItems/flashdemo/flashdemo.swf\"> <embed height=\"698\" width=\"894\" type=\"application/x-shockwave-flash\" src=\"InventoryItems/flashdemo/flashdemo.swf\"></embed> </object>";

	String HTMLString = "<OBJECT classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,40,0\" WIDTH=\"SIZE_X\" HEIGHT=\"SIZE_Y\" id=\"myMovieName\"><PARAM NAME=movie VALUE=\"FILENAME.SWF\"><PARAM NAME=quality VALUE=high><PARAM NAME=bgcolor VALUE=#FFFFFF><EMBED src=\"FILENAME.SWF\" quality=high bgcolor=#FFFFFF WIDTH=\"SIZE_X\" HEIGHT=\"SIZE_Y\" NAME=\"FILENAME.SWF\" ALIGN=\"\" TYPE=\"application/x-shockwave-flash\" PLUGINSPAGE=\"http://www.macromedia.com/go/getflashplayer\"></EMBED></OBJECT>";
 public boolean Magnifable = false;
 
	
 String sourceurl="";
 String sourcesizeX="";
 String sourcesizeY="";
 
	public flashPopUp(String URL, String sizeX, String sizeY){
		
		sourceurl = URL;
		sourcesizeX = sizeX;
		sourcesizeY= sizeY;
		
		//replace url		
		HTMLString=HTMLString.replaceAll("FILENAME.SWF", URL);

		int width =  (int) (Window.getClientWidth()*0.6);
		int height =  (int) (Window.getClientHeight()*0.9);
		
		HTMLString=HTMLString.replaceAll("SIZE_X", width+"PX");
		HTMLString=HTMLString.replaceAll("SIZE_Y", height+"PX");
		
		HTML frame = new HTML (HTMLString);
		frame.setSize("100%", "100%");
		this.add(frame);
		
		
		this.setSize( width+"px",height+"px");
		
		//MyApplication.DebugWindow.setText("size="+sizeX);
		
	}

	public void CloseDefault() {
		
	}

	public void OpenDefault() {
		
	
		
		
	}

	public String POPUPTYPE() {
		return "FLASH";
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
	
		return Integer.parseInt(removedAnyCss);
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
		return -1;
	}
}
