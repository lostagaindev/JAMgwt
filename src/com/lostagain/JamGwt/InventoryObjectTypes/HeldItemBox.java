package com.lostagain.JamGwt.InventoryObjectTypes;

import java.util.logging.Logger;

import org.eclipse.jetty.util.log.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.RequiredImplementations;

/**
 * This box, if visible, will show the current held item.
 * Clicking it will drop the item back into the inventory
 * 
 * Note; when not holding anything this box will be set to take up no space and be invisible.
 * 
 * @author Tom
 *
 */
public class HeldItemBox extends Image {


	static Logger Log = Logger.getLogger("JAM.InventoryObjectTypes.HeldItemBox");
	//private String currentSourceInventoryName="";
	
	
	public HeldItemBox() {
		super();
		
		super.addStyleName("HeldItemBox");
		
		//we are hidden by default
		setVisible(false);
		
		this.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//if click we drop what we are holding (which in turn hides this box)
				InventoryPanelCore.unholdItem();
				
			}
		});
		
		
		
	}

	/**
	 * Remember to setCurrentInventorySource first! 
	 * @param url
	 */
	public void setCurrentImage(String url){
		
		if (!this.isAttached()){
			Log.info("no item picked box on html page....so why should I bother setting the image?");			
			return;
		}		
		
		super.setUrl(url);
		
		
	}

	/**
	 * sets the inventory icon we should be over. (that is, the source inventory to what we are currently holding)
	 * This should be checked or ran at the same time as setting the image for what we are holding
	 * NOTE: this function will crash if we arnt attached to a parent object
	 * 
	 * @param nativeInventoryPanel
	 */
	public void setCurrentInventorySource(InventoryPanelCore panel) {
		String inventorysName = panel.InventorysName.replace(" ", "_"); //ensure any spaces in the name are replaced with underscores
				
		//change the box its positioned into
		RequiredImplementations.PositionByTag(this, "itempicked_"+inventorysName);	
			
	}
	
	
	
	
}
