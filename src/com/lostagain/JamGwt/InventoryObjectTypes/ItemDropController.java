/*
 * SimpleDropController is Copyright 2008 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lostagain.JamGwt.InventoryObjectTypes;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.OptionalImplementations;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;

/**
 * 
 * ItemDropController manages the dropping of items on another.
 * It then works out what to  do if this happens.
 * 
 **/
public final class ItemDropController extends SimpleDropController {

	public static Logger Log = Logger.getLogger("JAM.ItemDropController");

	
	private static final String CSS_DEMO_BIN_DRAGGABLE_ENGAGE = "demo-bin-draggable-engage";


	//=(InventoryPanel)(this.getDropTarget().getParent().getParent().getParent()); /** this is the inventory panel it springs back to if dropped somewhere invalid **/ 
	//public Widget gwtWidget;
	public String itemName;

	private InventoryItem associatedIcon;

	/*
	public ItemDropController(Widget test){	

		//public ItemDropController(InventoryIcon dropTarget){	
		super(test);


		this.gwtWidget = test;
		//   itemName = ((InventoryIcon)(test)).getName();

		//we now have to use some complex casting unfortunately due to InventoryIcons not being widgets anymore
		itemName =  ((SceneObjectVisual.MyFocusPanel)(test)).getParentSceneObjectAsInventoryIcon().getName();





		//itemName = test.Name;

		//(InventoryPanel)(test.getParent().getParent().getParent()); /** this is the inventory panel it springs back to if dropped somewhere invalid  

	}*/
	public ItemDropController(InventoryItem newpop) {
		super(newpop.getInternalGwtWidget());
		itemName =  newpop.getName();
		associatedIcon = newpop;
	}
	
	@Override
	public void onDrop(DragContext context) {

		Log.info("something dropped");

		//we get the item being dropped, and the item its being dropped onto
		//we now have to use some complex casting unfortunately due to InventoryIcons not being widgets anymore
		InventoryItem Item1  =  ((SceneObjectVisual.MyFocusPanel)(context.draggable)).getParentSceneObjectAsInventoryIcon();
		InventoryItem Item2  =  ((SceneObjectVisual.MyFocusPanel)(this.getDropTarget())).getParentSceneObjectAsInventoryIcon();
		//Item2 can just use associatedIcon?

		//the inventory panel one of them came from
		InventoryPanel ip = Item1.NativeInventoryPanel;
		
		//(InventoryPanel)(this.getDropTarget().getParent().getParent().getParent());
		
		//now the drop has happened the item that was held should be put in the next free slot
		//and remove from where it was
	//	ip.itemSpace[ip.old_LinerPos]=false;
		
		int pos = ip.getNextFreeLinearPosition();

		//remove from where ever it was before
		ip.removeFromLinearSlotSpace(Item1);
		
		//Add it to the next free slot
		Log.info("Setting to pos:"+pos);
		ip.targetPanel.add(context.draggable,
						   ip.getPositionXForSlot(pos),
						   ip.getPositionYForSlot(pos));		
	//	ip.itemSpace[pos]=true;		
		//ip.itemSpace_new[ip.old_LinerPos]=null;
		
		ip.itemSpace_new[pos]=Item1;
		
		super.onDrop(context);
		// ip.dragController.unregisterDropController(context.dropController);
		//ip.dragController.unregisterDropController(this);

		//Log.info("\n is attached? = "+this.gwtWidget.isAttached());

		// Item Mix trigger


		InventoryPanel.testForItemMix(Item1, Item2);




	}


	//The lower functions are to do with the mechanism of the Drag and Drop library
	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);    

		for (Widget widget : context.selectedWidgets) {
			widget.addStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
		}

		//change the mouse
		if (InventoryPanelCore.isItemBeingHeldOrDragged()){
			IsInventoryItem itemBeingHeldOrDragged = InventoryPanelCore.getCurrentItemBeingHeldOrDragged();
			Log.info("_________________ONMOUSEOVER when holding "+itemBeingHeldOrDragged.getName());			
			OptionalImplementations.setMouseCursorToHoldingOver(itemBeingHeldOrDragged,this.associatedIcon);
		}
	}

	@Override
	public void onLeave(DragContext context) {	

		for (Widget widget : context.selectedWidgets) {
			widget.removeStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
		}
		super.onLeave(context);

		//change the mouse back

		//If we are currently holding a item we should chance the mouse cursor back to reflect it not being over this
		if (InventoryPanelCore.isItemBeingHeldOrDragged()){
			Log.info("_________________ONMOUSEOUT when holding");
			IsInventoryItem itemBeingHeldOrDragged = InventoryPanelCore.getCurrentItemBeingHeldOrDragged();
			OptionalImplementations.setMouseCursorTo(itemBeingHeldOrDragged);
		}	
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		super.onPreviewDrop(context);    
	}

}
