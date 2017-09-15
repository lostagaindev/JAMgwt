package com.lostagain.JamGwt.InventoryObjectTypes;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;


public class moviePopUp extends SimplePanel  implements hasCloseDefault,
 hasOpenDefault, IsInventoryItemPopupContent,isInventoryItemImplementation {
String HTMLString = "<object classid=\"clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B\" codebase=http://www.apple.com/qtactivex/qtplugin.cab#version=6,0,2,0 width=\"SIZE_X\" height=\"SIZE_Y\" align=\"middle\"><param name=\"src\"value=\"FILENAME.MOV\" /><param name=\"autoplay\" value=\"false\" /><embed src=\"FILENAME.MOV\" autoplay=\"false\" width=\"SIZE_X\" height=\"SIZE_Y\" align=\"middle\" bgcolor=\"gray\" pluginspage=\"http://www.apple.com/quicktime/download/\"></embed></object>";

	public moviePopUp(String URL,String size_xs,String size_ys){
		
		//String sizes cant be percentages, only numbers allowed;
		size_xs=size_xs.replaceAll("[^0-9]", "");
		size_ys=size_ys.replaceAll("[^0-9]", "");
		int size_x = Integer.parseInt(size_xs);
		int size_y = Integer.parseInt(size_ys);
		
		//replace url		
		HTMLString=HTMLString.replaceAll("FILENAME.MOV", URL);
		HTMLString=HTMLString.replaceAll("SIZE_X", size_x+"");
		HTMLString=HTMLString.replaceAll("SIZE_Y", 12+size_y+"");
		
		
		HTML frame = new HTML (HTMLString);
		frame.setSize("100%", "100%");
		this.add(frame);
		;
		this.setSize( 2+size_x+"px",12+size_y+"px");
		
		
		
	}

	public void CloseDefault() {
		
	}

	public void OpenDefault() {
		
	}

	public String POPUPTYPE() {
		return "MOVIE";
	}
	public boolean POPUPONCLICK() {
		return true;
	}
	public void RecheckSize() {
		
	}
	public boolean MAGNIFYABLE() {
		return false;
	}
	public boolean DRAGABLE() {
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
		return -1;
	}
}
