package com.lostagain.JamGwt.GwtJamImplementations;


import java.util.logging.Logger;


import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.lostagain.Jam.Interfaces.HasMouseCursorChangeImplementation;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryItem;

import lostagain.nl.spiffyresources.client.SpiffyImageUtilitys;

public class GwtMouseCursorManagement implements HasMouseCursorChangeImplementation {

	static Logger Log = Logger.getLogger("JamGwt.GwtMouseCursorManagement");

	
	/** A string representing the games default cursor 
	 * It should point to the highest priority cursor first, then a fallback one if it fails.
	 * **/
	public static final String DefaultCursorString = "url(./GameIcons/prettycursor.cur),pointer";

	@Override
	public void setMouseCursorToDefault() {		
		setMouseImage(SpiffyImageUtilitys.smallArrowDataURL,GwtMouseCursorManagement.DefaultCursorString);
	}

	
	public void setMouseFromImage(String imageURL) 
	{
		
		//make temp image
		final Image tempImage = new Image(imageURL);
	
		
		tempImage.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				setMouseImage(tempImage,GwtMouseCursorManagement.DefaultCursorString);
				//remove image after its down, as cursor doesn't need it.
				tempImage.removeFromParent();
			}
		});

	}
		
		
	
	/** sets the mouses image by supplying an image (slow) **/
	//Only used atm in the instruction process for one command - not even sure if that command is ever used
	//maybe just remove it? replace with a url function?
	public static void setMouseImage(Image image,String defaultCursor) {
			
		if (image==null){
			//return to default
			RootPanel.getBodyElement().getStyle().setProperty("cursor", defaultCursor);
	
		} else{			
			//get small image
	
			JAM.GwtLog.info("set mouse cursor to:"+image.getUrl()+ "isattached:"+image.isAttached());
			String dataURL = SpiffyImageUtilitys.getDataURLFromImage(image,64,64,SpiffyImageUtilitys.SmallArrow);
	
	
			if (dataURL!=null){
				RootPanel.getBodyElement().getStyle().setProperty("cursor", "url("+dataURL+"),"+defaultCursor);
				JAM.GwtLog.info("set cursor to:"+RootPanel.getBodyElement().getStyle().getProperty("cursor"));
			} else {
				RootPanel.getBodyElement().getStyle().setProperty("cursor", defaultCursor);
				JAM.GwtLog.info("data url is null");
	
			}				
	
		}
	
	}

	/** sets the mouses image by supplying a css cursor value 
	 * this will overright ALL values in the cursor property **/
	public static void setMouseImage(String cursorValue) {
		RootPanel.getBodyElement().getStyle().setProperty("cursor", cursorValue);
	
	}

	/** sets the mouses image by supplying a dataurl (fast) **/
	public static void setMouseImage(String dataURL,String defaultCursor) {
	
		if (dataURL==null){
			//return to default
			RootPanel.getBodyElement().getStyle().setProperty("cursor", defaultCursor);
	
		} else{			
			RootPanel.getBodyElement().getStyle().setProperty("cursor", "url("+dataURL+"),"+defaultCursor);
	
		}
	
	}

	@Override
	public void setMouseCursorToHolding(IsInventoryItem holdThis) {
		
		Log.info("setting mouse holding image");
		
		String dataURL = ((InventoryItem)holdThis).getDataURL(); //we cast to the implementation as we are using DataURL which is easier/better then retrieving images from url
		if (dataURL==null || dataURL.isEmpty()){
			Log.warning("DataURL not yet ready"); //should have a callback system for this
				
		}
		GwtMouseCursorManagement.setMouseImage(dataURL,GwtMouseCursorManagement.DefaultCursorString);
		
	}


	@Override
	public void setMouseCursorToHoldingOver(IsInventoryItem holdThis, IsInventoryItem overThis) {
		Log.info("setting mouse holding over");
		
		
		String dataURL = ((InventoryItem)holdThis).getDataURLWithPlus(); //we cast to the implementation as we are using DataURL which is easier/better then retrieving images from url
		
		GwtMouseCursorManagement.setMouseImage(dataURL,GwtMouseCursorManagement.DefaultCursorString);
		
	}

}
