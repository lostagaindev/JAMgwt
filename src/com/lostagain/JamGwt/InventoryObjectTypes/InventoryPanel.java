package com.lostagain.JamGwt.InventoryObjectTypes;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem.IconMode;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isPopupTypeImplementation;

import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.Event;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.GridConstrainedDropController;
import com.allen_sauer.gwt.dnd.client.DragHandler;

/*** 
 * Big, rather complex, panel designed for inventory management.
 * Its a popup, with drag and drop support AND it can create inventory items of various types
 * AND it supports multiple panels with drag and drop between them. AND
 * actions can happen when items are dropped on eachother 
 ***/
//TODO : slowly move what we can to InventoryPanelCore leaving the visual stuff here
public class InventoryPanel extends InventoryPanelCore implements isPopupTypeImplementation, hasOpenDefault, hasCloseDefault {

	public static Logger Log = Logger.getLogger("JAM.InventoryPanel");

	/**
	 * The visual panel containing the contents
	 **/
	VerticalPanel panelContents = new VerticalPanel();


	/**
	 * find the drop controller for a given inventory icon
	 * Drop controllers control the drag/drop aspect of the inventory, but shouldn't do anything game-specific, only visual
	 **/
	public final HashMap<IsInventoryItem,ItemDropController> itemDropControllerMap = new HashMap<IsInventoryItem,ItemDropController>();


	Label titleLabel = new Label(InventorysName);
	AbsolutePanel MainInventoryBox = new AbsolutePanel();

	int old_left = 0;
	int old_top = 0;

	// last position dropped to, this is stored as a linear position as if all the positions of the inventory panel were in a long horizontal line
	int LinerPos = 0;


	// drag controller
	public PickupDragController dragController = new PickupDragController(RootPanel.get(), false); // used to be MainInventoryBox, true

	// Create a drop target on which we can drop items
	AbsolutePanel targetPanel = new AbsolutePanel();

	// DropController dropController = new
	// AbsolutePositionDropController(targetPanel);

	GridConstrainedDropController dropController = new GridConstrainedDropController(targetPanel, 100, 100);
	GridConstrainedDropController textModeDropController = new GridConstrainedDropController(targetPanel, 100, 30);



	static Boolean isDragging  = false;
	static Boolean cancelClick = false; // while dragging this makes sure no
	// clicks are counted

	boolean reorder = true;
	
	public InventoryPanel this_inventory = this;

	public InventoryPanel(String name,IconMode mode) {
		super(name,mode); //eventually move a lot of this non-visual stuff to thesuperclass				
		Log.info("Making new inventory with name:" + name+" sized:"+RInventorySizeY);

		//CurrentInventoryMode = mode;
		titleLabel.setText(InventorysName);
		titleLabel.setStylePrimaryName("unselectable");

		// make topbar
		// topBar NewTopBar = new topBar("Inventory",this);

		// add title
		// add(NewTopBar);
		// default size
		panelContents.setSize(RInventorySizeX + "px", RInventorySizeY + "px");
		// set default style
		panelContents.setStyleName("inventory");
		panelContents.addStyleName("unselectable");

		// add main box
		panelContents.add(MainInventoryBox);
		MainInventoryBox.setStylePrimaryName("unselectable");
		MainInventoryBox.setSize(RInventorySizeX + "px", (RInventorySizeY - 20)
				+ "px");

		// drag set up
		// Positioned is always constrained to the boundary panel
		// Use 'true' to also constrain the draggable or drag proxy to the
		// boundary panel
		// create a DropController for each drop target on which draggable
		// widgets
		// can be dropped

		// Don't forget to register each DropController with a DragController
		if (CurrentInventoryMode==IconMode.Text){
			iconSizeY=30;
			dragController.registerDropController(textModeDropController);

		}else {
			iconSizeY=100;
			dragController.registerDropController(dropController);
		}
		//dragController.registerDropController(dropController);
		dragController.setBehaviorDragStartSensitivity(3);
		MainInventoryBox.add(targetPanel, 1, 1);
		targetPanel.setSize("100%", "100%");
		targetPanel.setStylePrimaryName("unselectable");

		// make target clickable so it rises to front
		targetPanel.sinkEvents(Event.ONCLICK);

		targetPanel.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// raise inventory panel to front
				if (!InventoryItem.iconclickedrecently){

					Log.info("settingInventoryFrames ZIndexTop");
					JAMcore.allInventoryFrames.get(InventorysName).setZIndexTop();

				} //
				InventoryItem.iconclickedrecently=false;
			}

		}, ClickEvent.getType());

		dragController.setBehaviorConstrainedToBoundaryPanel(false); // used to
		// be
		// true

		dragController.setBehaviorMultipleSelection(false);

		dragController.addDragHandler(new DragHandler() {

			@Override
			public void onDragEnd(DragEndEvent event) {


				// (ItemDropController)(event.getSource());
				Widget Source = (Widget) (event.getSource());
				InventoryItem currentlyDragged = (InventoryItem)currentlyDraggedItem;

				Log.info("Drag end for "+currentlyDragged.getName());

				// Now we retrieve the style in the Dom where it says LEFT:
				// ###px; and TOP ###px
				int Left = Source.getElement().getOffsetLeft();
				int Top  = Source.getElement().getOffsetTop();
				int LeftLimit = dragController.getBoundaryPanel().getOffsetWidth();

				LinerPos = (((Top / 100) * LeftLimit) + Left) / 100;

				//	itemSpace[old_LinerPos] = false;
				//	itemSpace[LinerPos]     = true;

				//itemSpace_new[old_LinerPos] = null;
				removeFromLinearSlotSpace(currentlyDragged);				
				itemSpace_new[LinerPos]     = currentlyDragged;

				if (JAMcore.DebugMode){
					debugLinearSlotSpace();
				}


				currentlyDragged.setCssAsDragging(false);


				setHeldItemVariablesAndVisualsOff();
				//--

				// we set the old one to false

				//	JAMcore.GameLogger.info("-drag end-");
				// Window.alert("set "+LinerPos+" to true. Set "+old_LinerPos+" to false");

				/**
				 * // get current pos? :? // ItemDropController Source =
				 * 
				 * 
				 * // work out the liner slot position and set it to true if its
				 * empty.
				 * 
				 * LinerPos = (((Top / 100) * LeftLimit) + Left) / 100;
				 * 
				 * // seems to alternative between being detected and not. //Is
				 * it effecting the landing region when it shouldnt?
				 * 
				 * 
				 * if (itemSpace[LinerPos]==false){
				 * 
				 * 
				 * Window.alert("-no item under it at "+LinerPos+"-");
				 * 
				 * itemSpace[LinerPos] = true;
				 * 
				 * // we set the old one to false itemSpace[old_LinerPos] =
				 * false;
				 * 
				 * System.out.println(" from " + old_LinerPos + "  to " +
				 * LinerPos + " "); } else {
				 * 
				 * Window.alert(
				 * "-item under it so we have to move it elsewhere:"
				 * +LinerPos+"-"); //we put it in the next free slot //find slot
				 * 
				 * /** //Debuging stuff
				 * 
				 * //Print complete itemspace int i=0; while
				 * (i<itemSpace.length){ i++;
				 * MyApplication.DebugWindow.info("-"+itemSpace[i]+"-"); }
				 * 
				 * 
				 * // work out next gap in itemlist.
				 * 
				 * 
				 * int pos = 0;
				 * 
				 * while (itemSpace[pos] == true) { pos = pos + 1; }
				 * Window.alert(pos+" is "+itemSpace[pos]);
				 * 
				 * 
				 * int NextPosX = ((itemList.size()) * 100) % RInventorySizeX;
				 * int NextPosY = 100 * ((((itemList.size()) * 100) - NextPosX)
				 * / RInventorySizeX);
				 * 
				 * int LeftLimit2 = dragController.getBoundaryPanel()
				 * .getOffsetWidth();
				 * 
				 * // work out the liner slot position and set it to true.
				 * LinerPos = (((NextPosY / 100) * LeftLimit2) + NextPosX) /
				 * 100;
				 * 
				 * itemSpace[LinerPos] = true; itemSpace[old_LinerPos] = false;
				 * //int NextPosX = ((pos) * 100) % RInventorySizeX; //int
				 * NextPosY = (((pos*100)-NextPosX)/RInventorySizeX);
				 * 
				 * 
				 * //int NextX = //get old position //int OldLeft =
				 * (old_LinerPos*100) % RInventorySizeX; //int OldTop = 100 *
				 * (((old_LinerPos * 100) - OldLeft) / RInventorySizeX);
				 * 
				 * Source.getElement().getStyle().setPropertyPx("left",NextPosX)
				 * ; Source.getElement().getStyle().setPropertyPx("top",
				 * NextPosY);
				 * 
				 * Window.setTitle("icon put back to "+NextPosX+";"+NextPosY+"("
				 * +LinerPos+")");
				 * 
				 * 
				 * }
				 **/
				// end of drag
				isDragging = false;
				// Window.setTitle("not dragging");

			}

			@Override
			public void onDragStart(DragStartEvent event) {

				// Window.alert("drag start...");
				Log.info(" Drag start");

				isDragging  = true;
				cancelClick = true;

				// store old co-ordinate liner flag.
				Widget Source = (Widget) (event.getSource());

				// Now we retrieve the style in the Dom where it says LEFT:
				// ###px; and TOP ###px
				// int Left = Source.getElement().getOffsetLeft();
				// int Top = Source.getElement().getOffsetTop();
				//old_LinerPos = getSlotPositionOf(Source);


				//(new) when dragging we have the same visually effects as holding
				//Theres two possible ways to get the item dragged from this event.
				//We know its widget (above) and we could cast this in a horrible way to get the IsInventoryIcon
				//However, this is horrible because the widget actually belongs to the item, there not one and the same
				//eg, the casting is ;  	InventoryIcon Item1  =  ((SceneObjectVisual.MyFocusPanel)(context.draggable)).getParentSceneObjectAsInventoryIcon();
				//messy.
				//Instead we could try to get the drop controller from the context and then use our hashmap to get the item from that
				//this might mean changing the hashmap to a bimap so we can search by drop controller to get the inventoryitem

				IsInventoryItem itemDragged  =  ((SceneObjectVisual.MyFocusPanel)(Source)).getParentSceneObjectAsInventoryIcon();

				old_LinerPos = getLinearSlotPosition(itemDragged);

				//
				Log.info(" Dragging item from position : "+old_LinerPos);

				((InventoryItem)itemDragged).setCssAsDragging(true);
				setHeldItemVisualsAndVariablesOn(itemDragged);				
				//--------------------
				//(we have to remember to disable these after)


			}

			@Override
			public void onPreviewDragEnd(DragEndEvent event)
					throws VetoDragException {

				// display order check
				/*
				 * String OrderCheck = ""; int i =0;
				 * 
				 * while (i<itemSpace.length){ String status = "0"; if
				 * (itemSpace[i]){ status = "1"; } OrderCheck = OrderCheck
				 * +"-"+status; i++; }
				 */
				// Window.alert("order check:"+OrderCheck);

				// work out if it will be at the wrong position.

			}

			@Override
			public void onPreviewDragStart(DragStartEvent event)
					throws VetoDragException {
				// Window.alert("dragging_veto");
			}

		});

	}

	@Override
	public void OpenDefault() {
		super.OpenDefault();

		// has to also trigger when opened for the first time!
		ResizeAndReorder();

		Log.info("inventory newly  opened, so we register all items on screen as dropcontrollers");

		// Register all drop controllers
		Iterator<InventoryPanelCore> inventorys = JAMcore.allInventorys.values().iterator();


		while (inventorys.hasNext()) {
			//we should remove casts like this when we can
			InventoryPanel inventoryPanel = (InventoryPanel) inventorys.next();

			// if its attached
			if (inventoryPanel.panelContents.isAttached()) {
				Log.info("registering items from:" + inventoryPanel.InventorysName);

				// get the other inventory's items and register them as drops
				//Iterator<ItemDropController> drops = inventoryPanel.itemDropControllerList.iterator();

				Iterator<IsInventoryItem> icons = inventoryPanel.inventorysCurrentItems.iterator();

				while (icons.hasNext()) {

					//get the controller associated with this icon
					ItemDropController itemDropController = inventoryPanel.itemDropControllerMap.get(icons.next());  //(ItemDropController) drops.next();

					this.dragController.registerDropController(itemDropController);

				}
				Log.info("registering items to:" + inventoryPanel.InventorysName);

				// now we get the items on this inventory and register them as
				// drops on the other in
				//Iterator<ItemDropController> thisPanelsdrops = this.itemDropControllerList.iterator();
				Iterator<IsInventoryItem> thisPanelsIcons = this.inventorysCurrentItems.iterator();

				while (thisPanelsIcons.hasNext()) {

					//ItemDropController itemDropController = (ItemDropController) thisPanelsdrops
					//		.next();
					ItemDropController itemDropController = this.itemDropControllerMap.get(thisPanelsIcons.next()); 

					Log.info("registering item:" + itemDropController.itemName);

					inventoryPanel.dragController.registerDropController(itemDropController);

				}

			} else {
				Log.info("ignoring items from:" + inventoryPanel.InventorysName
						+ " as it is not attached");

			}

		}
		// set its default drop (its area)
		if (CurrentInventoryMode==IconMode.Text){

			iconSizeY=30;
			dragController.registerDropController(textModeDropController);


		}else {

			iconSizeY=100;

			Log.info("registering drop controller");

			dragController.registerDropController(dropController);

		}
		// Window.alert("order check:"+OrderCheck);

	}

	public void setIconsToMode(IconMode mode){

		CurrentInventoryMode = mode;

		for (Iterator<IsInventoryItem> it = inventorysCurrentItems.iterator(); it
				.hasNext();) {

			IsInventoryItem currentItem = it.next();



			//this can become a interface rather then a cast one the enum is also moved to JamCore
			((InventoryItem)currentItem).setIconMode(mode);


		}

		//old;
		//for (Iterator<ItemDropController> it = itemDropControllerList.iterator(); it
		//		.hasNext();) {

		//	ItemDropController currentItem = it.next();

		//((InventoryIcon)currentItem.getDropTarget()).setIconMode(mode);

		//	((SceneObjectVisual.MyFocusPanel)currentItem.getDropTarget()).getParentSceneObjectAsInventoryIcon().setIconMode(mode);

		//}

	}

	public void ResizeAndReorder() {



		// if the screen size has got smaller we flag that things must be
		// reordered.
		if (Math.abs((ScreenSizeX) - (RequiredImplementations.getCurrentGameStageWidth())) > 30) {
			reorder = true;
			// MyApplication.DebugWindow.info("reorder needed "
			// + Math.abs((ScreenSizeX) - (Window.getClientWidth())));
		}

		//	ScreenSizeX = Window.getClientWidth();
		//	ScreenSizeY = Window.getClientHeight();


		ScreenSizeX = RequiredImplementations.getCurrentGameStageWidth();
		ScreenSizeY = RequiredImplementations.getCurrentGameStageHeight();

		// app size
		InventorySizeX = (100 * (ScreenSizeX / 150));

		// InventorySizeY = (100 * (Window.getClientHeight() / 150));
		// size based on number of items
		// InventorySizeX = (100 * (Window.getClientWidth() / 150));
		// number of items high
		//	int noih = 100 + (Math.round(itemList.size() / (InventorySizeX / 100))) * 100;
		int noih = iconSizeY + (Math.round(inventorysCurrentItems.size() / (InventorySizeX / iconSizeX))) * iconSizeY;



		InventorySizeY = noih;
		// MyApplication.DebugWindow.info("\n inventory size = "+noih);
		// InventorySizeY = (100 * (Window.getClientHeight() / 150));

		if (CurrentInventoryMode==IconMode.Text){
			roundToX = 100;
			roundToY = 30;

		}else {
			roundToX = 100;
			roundToY = 100;
		}


		RInventorySizeX = (int) ((InventorySizeX + (0.5 * roundToX)) / roundToX)
				* roundToX;
		RInventorySizeY = 20
				+ (int) ((InventorySizeY + (0.5 * roundToY)) / roundToY)
				* roundToY;

		// int posX = ScreenSizeX / 2 - RInventorySizeX / 2;
		// int posY = ScreenSizeY / 2 - RInventorySizeY / 2;

		// default size
		panelContents.setSize(RInventorySizeX + "px", RInventorySizeY + "px");
		MainInventoryBox.setSize(RInventorySizeX + "px", (RInventorySizeY - 20)
				+ "px");
		targetPanel.setSize("100%", "100%");

		Log.info("inventory with name:" + this.InventorysName+" sized Y:"+RInventorySizeY);


		// RootPanel.get().add(this, posX, posY);

		if (reorder == true) {
			int loop = 0;
			int PosX = 0;
			int PosY = 0;
			for (Iterator<IsInventoryItem> it = inventorysCurrentItems.iterator(); it
					.hasNext();) {

				// (Iterator<ItemDropController> it = itemDropControllerList.iterator(); it
				//		.hasNext();) {


				//	ItemDropController currentItem = it.next();

				IsInventoryItem currentItem = it.next();


				// get position for loop

				PosX = (loop * iconSizeX) % RInventorySizeX;

				// PosY = (loop * 100) - PosX;

				PosY = ((int) Math.floor((loop * iconSizeX) / RInventorySizeX)) * iconSizeY;

				JAMcore.GameLogger.info("\n reordering " + PosY
						+ " should not be more then" + RInventorySizeY);

				//targetPanel.setWidgetPosition(currentItem.getDropTarget(),
				//		PosX, PosY);

				//temp cast?
				targetPanel.setWidgetPosition(((InventoryItem)currentItem).getInternalGwtWidget(),PosX, PosY);

				//	itemSpace[loop] = true;

				itemSpace_new[loop] = currentItem;

				// MyApplication.DebugWindow.info(loop + "=set too true ");

				loop = loop + 1;

				reorder = false;

			}

		}

		// display order check
		/*
		String OrderCheck = "";
		int i = 0;

		while (i < itemSpace.length) {
			String status = "0";
			if (itemSpace[i]) {
				status = "1";
			}
			OrderCheck = OrderCheck + "-" + status;
			i++;
		}*/
	}

	@Override
	public void CloseDefault() {
		super.CloseDefault();
		
		
		// unregister all drop controllers attached to the items stored on this
		// inventory
		//Iterator<ItemDropController> items = itemDropControllerList.iterator();
		Iterator<IsInventoryItem> icons = inventorysCurrentItems.iterator();

		while (icons.hasNext()) {

			ItemDropController itemDropController = itemDropControllerMap.get(icons.next());


			//ItemDropController itemDropController = (ItemDropController) items
			//		.next();

			Log.info("got item:" + itemDropController.itemName);
			InventoryPanel.unRegisterItemAsDropOnAllInventoryPanels(itemDropController);
		}
		// ---------------
		// we then clear this panels own handlers
		Log.info("clearing panels own drops");
		dragController.unregisterDropControllers();
	}

	static private void registerItemAsDropOnAllInventoryPanels(
			DropController newitem) {
		Iterator<InventoryPanelCore> inventorys = JAMcore.allInventorys
				.values().iterator();

		while (inventorys.hasNext()) {
			InventoryPanel inventoryPanel = (InventoryPanel) inventorys.next();
			//Log.info("checking iventory is open" + inventoryPanel.Title);

			// check inventory is open
			if (inventoryPanel.panelContents.isAttached()) {
				Log.info("adding item as inventory drop controller:"
						+ inventoryPanel.InventorysName);

				// inventoryPanel.addAdditionalDropController(newitem);

				inventoryPanel.dragController.registerDropController(newitem);

			} else {
				// do nothing
				// inventorys should automaticaly attach all items when they are
				// opened
				// the above is just for if a new item is added when one is
				// already open
			}
		}
	}

	/** unregisters a drop controll from all open inventories **/
	static private void unRegisterItemAsDropOnAllInventoryPanels(
			ItemDropController newitem) {

		Iterator<InventoryPanelCore> inventorys = JAMcore.allInventorys
				.values().iterator();


		while (inventorys.hasNext()) {
			InventoryPanel currentInventoryPanel = (InventoryPanel) inventorys
					.next();

			//	Log.info("checking inventory is open:"
			//			+ currentInventoryPanel.Title);
			if (currentInventoryPanel.panelContents.isAttached()) {

				Log.info("removing " + newitem.itemName
						+ " from inventory drop controller:"
						+ currentInventoryPanel.InventorysName);
				
				currentInventoryPanel.removeAdditionalDropController(newitem);

			} else {

				// do nothing
				Log.info("inventory not open so skipping");
				// inventorys should automatically attach all items when they
				// are opened
				// the above is just for if a new item is removed when one is
				// already open
			}

		}
	}





	/* if implemented needs to be updated as the list is of inventoryicons now, and the map associates them with drop controllers
	public void RemoveThisItem(ItemDropController Item) {

		Item.getDropTarget().removeFromParent();

		targetPanel.remove(Item.getDropTarget());

		// dragController.unregisterDropController(Item);

		// unregister on all panels
		// dragController.unregisterDropController(currentItem);

		Log.info("- removing " + Item.itemName + " via objecy");
		dragController.makeNotDraggable(Item.getDropTarget());

		InventoryPanel.unRegisterItemAsDropOnAllInventoryPanels(Item);

		itemDropControllerList.remove(Item);

		itemDropControllerMap.remove(itemDropControllerMap.get(Item));

	}*/

	@Override
	public void ClearInventory() {
		Log.info("- Removing all " + inventorysCurrentItems.size() + " items");


		Iterator<IsInventoryItem> it = inventorysCurrentItems.iterator();

		while (it.hasNext()) {

			//ItemDropController currentItem = it.next(); // No downcasting
			// required.

			IsInventoryItem currentIcon = it.next(); 
			
			
			ItemDropController currentItem = itemDropControllerMap.get(currentIcon);

			Log.info("-removing " + currentIcon.getName());

			// remove its position
			//System.out.println("blah3");
			// Top = currentItem.getDropTarget().getElement().getOffsetTop();
			//System.out.println("blah4");

			//Log.info("blah4");


			//Left = currentItem.getDropTarget().getElement().getOffsetLeft();
			//int LeftLimit = dragController.getBoundaryPanel().getOffsetWidth();

			// work out the liner slot position and set it to false.

			//	old_LinerPos = (((Top / 100) * LeftLimit) + Left) / 100; //wrong pos?
			//	itemSpace[old_LinerPos] = false;

			//itemSpace_new[old_LinerPos] = null;;
			removeFromLinearSlotSpace(currentIcon);

			InventoryPanel.unRegisterItemAsDropOnAllInventoryPanels(currentItem);

			// we delete it.
			currentItem.getDropTarget().removeFromParent();

			targetPanel.remove(currentItem.getDropTarget());
			// dragController.unregisterDropController(currentItem);
			// unregister on all panels
			// dragController.unregisterDropController(currentItem);

		}

		if (JAMcore.DebugMode){
			debugLinearSlotSpace();
		}

		inventorysCurrentItems.clear();
		itemDropControllerMap.clear();

	}


	
	@Override
	public void physicallyRemoveItem(IsInventoryItem icon) {
		

		ItemDropController itemToRemove = itemDropControllerMap.get(icon);

		if (!itemDropControllerMap.containsKey(icon)){
			Log.severe("object does not have ItemDropController");
			return;
		}

		dragController.makeNotDraggable(itemToRemove.getDropTarget());

		// we delete it.
		itemToRemove.getDropTarget().removeFromParent();

		targetPanel.remove(itemToRemove.getDropTarget());

		//remove from lists
		itemDropControllerMap.remove(icon);
		
		Log.info("removing item and resetting cache:");
		Log.info("removing item:" + itemToRemove.itemName);

		// dragController.unregisterDropController(currentItem);
		// remove it from everywhere as a drop
		InventoryPanel.unRegisterItemAsDropOnAllInventoryPanels(itemToRemove);

		// this works for current panel regardless of if its open (is that right?)
		this.removeAdditionalDropController(itemToRemove);
	}

	public void addAdditionalDropController(DropController newdropController) {
		Log.info("registering new drop controller");
		dragController.registerDropController(newdropController);
	}

	public void removeAdditionalDropController(ItemDropController dropController) {

		this.dragController.unregisterDropController(dropController);
		Log.info("removed a " + dropController.itemName + " controller from "+ this.InventorysName);

	}


	@Override
	public boolean DRAGABLE() {
		return true;
	}

	@Override
	public boolean POPUPONCLICK() {
		return false;
	}

	@Override
	public String POPUPTYPE() {
		return null;
	}

	@Override
	public void RecheckSize() {
	}

	public boolean MAGNIFYABLE() {
		return false;
	}

	/**
	 * Don't use this anymore, make a different method to get all the items if needed.<br>
	 * currently this is (wrongly) used to remove a object, and to list it on the debugger<br>
	 * The second needs to work in a way that takes isInventoryIcons instead<br>
	 **/
	/*
	@Deprecated
	public static Collection<? extends SceneObject> getAllInventoryItems() {

		ArrayList<InventoryIcon> alltheitems = new ArrayList<InventoryIcon>();
		Iterator<InventoryPanelCore> invenit = JAMcore.allInventorys.values().iterator();

		while (invenit.hasNext()) {

			InventoryPanelCore inventory = invenit.next();

			for (IsInventoryIcon idc : inventory.inventorysCurrentItems) {

				//InventoryIcon item =  (InventoryIcon) idc.getDropTarget(); 

				//eventually we may need a double cast. First to MyFocusPanel and then use getParent to get the SceneObjectUI object from it

				//InventoryIcon item =  (InventoryIcon)(((SceneObjectVisual.MyFocusPanel) idc.getDropTarget()).getParentSceneObject()); //eventually we may need a double cast. First to MyFocusPanel and then use getParent to get the SceneObjectUI object from it

				IsInventoryIcon item = idc;


				alltheitems.add((InventoryIcon)item); //bad casting

			}

		}

		return alltheitems;
	}*/




	/**
	 * returns a GWT Widget type, cast as needed
	 */
	@Override
	public Object getVisualRepresentation() {
		return panelContents.asWidget();
	}

	@Override
	public Widget asWidget() {
		return  panelContents.asWidget();
	}

	/**
	 * Attaches a already created inventoryitem to this inventorypanel
	 * 
	 * @param runwhendone
	 * @param TriggerPopUpAfterLoading
	 * @param preparedItem
	 */
	public void attachPreparedInventoryItemToPanel(
			final Runnable runwhendone,
			final boolean TriggerPopUpAfterLoading,
			IsInventoryItem alreadyPreparedItem) {

		InventoryItem preparedItem = (InventoryItem) alreadyPreparedItem;		
		ItemDropController newitem;

		newitem = new ItemDropController( preparedItem);

		// add the actions (ahouldnt be needed now, its handled by SceneSpriteObject that inventor icons extend now
		//newpop.loadActionsFromActionList(itemsActions);

		// move the label
		targetPanel.add(newitem.getDropTarget());


		int targetSlot = getNextFreeLinearPosition();

		Log.info("targetSlot = "+targetSlot);

		//	int NextPosX = this_inventory.NextFreeSlotX();
		//	int NextPosY = this_inventory.NextFreeSlotY();

		int NextPosX = this_inventory.getPositionXForSlot(targetSlot);
		int NextPosY = this_inventory.getPositionYForSlot(targetSlot);


		targetPanel.setWidgetPosition(newitem.getDropTarget(),	NextPosX, NextPosY);

		// make the label draggable
		dragController.makeDraggable(newitem.getDropTarget());

		// add item as a drop controller on all inventorys
		// dragController.registerDropController(newitem);


		InventoryPanel.registerItemAsDropOnAllInventoryPanels(newitem);

		// add the item					
		itemDropControllerMap.put(preparedItem,newitem);

		
		addAlreadyAttachItemLogically(preparedItem, targetSlot);

		if (JAMcore.DebugMode){
			debugLinearSlotSpace();
		}

		//JAMcore.GameLogger.info(LinerPos+ "=set too true ");

		// and, if needbe, popit up
		if (TriggerPopUpAfterLoading) {

			Log.info("triggering popup");
			preparedItem.triggerPopup();
		}

		//set any runnables to go after the icons image has loaded
		//this should only fire at some point after its first attached
		if (preparedItem.loadedPic){

			Log.info("triggering runwhendone");						
			if (runwhendone!=null){ //without this null check runnables that are null seem to behave VERY strangly. Somehow the runwhendone gets the value of whats set in "AddItemAndHold" - despite the fact no logs for it are triggered and that function isnt triggered from anywhere
				runwhendone.run();
			} else {
				Log.info("no runwhendone to trigger");	
			}

		} else {						
			Log.info("setting runwhendone to run once loaded");
			preparedItem.setRunwhenloaded(runwhendone); //if not loaded yet, this will fire when loaded
		}
	}

	/**
	 * gets the slot position of this item on the inventory
	 * @param Source
	 * @return 
	 */
	@Deprecated
	protected int getSlotPositionOf(Widget Source) {
		int LeftLimit = dragController.getBoundaryPanel().getOffsetWidth();
		old_left = Source.getElement().getOffsetLeft();
		old_top  = Source.getElement().getOffsetTop();

		//Work out the old liner slot position
		int pos = (((old_top / 100) * LeftLimit) + old_left) / 100;

		return pos;
	}

/**
	@Deprecated
	private IsInventoryItemPopupContent createInventoryItemsPopupContents_old(String ItemsType, final String ItemsName,
			String ItemsDiscription, String ItemsURL, String ItemsTitle, String size_x, String size_y,
			boolean is_magnifiable, String Embed) {
		
		InventoryItem newInventoryIcon;
		IsInventoryItemPopupContent ItemPopUp;
		

		ItemsType = ItemsType.toLowerCase();

		switch (ItemsType) {
		case "picture":
		{
			int rsize_x = Integer.parseInt(size_x);
			int rsize_y = Integer.parseInt(size_y);

			ItemPopUp = new ImagePopUp("InventoryItems/" + ItemsName + "/" + ItemsName+".jpg", ItemsDiscription, rsize_x, rsize_y);

			break;
		}
		case "movie":
		{
			ItemPopUp = new moviePopUp("InventoryItems/" + ItemsName + "/" + ItemsName+ ".mov", size_x, size_y);

			break;
		}
		case "youtube":
		{
			ItemPopUp = new youtubePopUp(ItemsURL, 425,	345);

			break;
		}
		case "embed":
		{
			ItemPopUp = new embedPopUp(ItemsURL, size_x,		size_y, Embed);

			JAMcore.GameLogger.info(" embed code="	+ Embed);
			

			break;
		}
		case "pngpicture":
		{
			int rsize_x = Integer.parseInt(size_x);
			int rsize_y = Integer.parseInt(size_y);
			ItemPopUp = new ImagePopUp(			"InventoryItems/" + ItemsName + "/" + ItemsName			+ ".png", ItemsDiscription, rsize_x, rsize_y);
		//	newInventoryIcon = new InventoryItem(ItemPopUp,  title,				this_inventory,iconsstate,ItemDataString);


			break;
		}
		case "flash":
		{
			flashPopUp FlashItem = new flashPopUp("InventoryItems/"	+ ItemsName + "/" + ItemsName + ".swf", size_x,	size_y);
			ItemPopUp = FlashItem;
			//newInventoryIcon = new InventoryItem(ItemPopUp,  title,					this_inventory,iconsstate,ItemDataString);

			if (is_magnifiable) {
				FlashItem.Magnifable = true;
				JAMcore.GameLogger.info("flashitem mag set to.."	+ FlashItem.Magnifable);
			}


			break;
		}
		case "toggleitemgroup":
		{
			// increase the left-to-load list (as tigs have sub-items)
			JAMcore.itemsLeftToLoad++;
			Log.info("increaseing items left to load to:"		+ JAMcore.itemsLeftToLoad);
			toggleImageGroupPopUp TIGitem = new toggleImageGroupPopUp(
					ItemsName, ItemsDiscription, size_x, size_y,false);

			ItemPopUp = TIGitem;
			//newInventoryIcon = new InventoryItem(ItemPopUp,  title,	this_inventory,iconsstate,ItemDataString);

			if (is_magnifiable) {
				TIGitem.Magnifable = true;
				JAMcore.GameLogger.info("flashitem mag set to.."	+ TIGitem.Magnifable);
			}
			break;
		}
		case "pngtoggleitemgroup":
		{
			// increase the left-to-load list(as tigs have sub-items)
			JAMcore.itemsLeftToLoad++;
			Log.info("increaseing items left to load to:"		+ JAMcore.itemsLeftToLoad);
			toggleImageGroupPopUp TIGitem = new toggleImageGroupPopUp(ItemsName, ItemsDiscription, size_x, size_y,true);

			ItemPopUp = TIGitem;
		//	newInventoryIcon = new InventoryItem(ItemPopUp,  title,this_inventory,iconsstate,ItemDataString);

			if (is_magnifiable) {
				TIGitem.Magnifable = true;
				JAMcore.GameLogger.info("flashitem mag set to.."+ TIGitem.Magnifable);
			}
			break;
		}
		case "overlay":
		{
			ItemPopUp = new overlayPopUp();
			break;
		}
		case "magnifyingglass":
		{
			ToolPopUp mag = new ToolPopUp(ItemsDiscription, ItemsTitle);
			mag.tooltype = "MAGNIFYINGGLASS";
			mag.POPUPONCLICK = false;
			ItemPopUp = mag;
			break;
		}
		case "textscroll":
		{
			textScroller textScroll = new textScroller(		"InventoryItems/" + ItemsName + "/" + ItemsName		+ ".html");
			Log.info("\n ----make text scroller"							+ ItemsDiscription);
			ItemPopUp = textScroll;
			break;
		}
		case "dummy":
		{
			ItemPopUp = new DummyPopUp(ItemsTitle);
			break;
		}
		case "concept":
		{
			ItemPopUp = new ConceptPopUp(ItemsDiscription,	ItemsTitle);
			break;
		}
		default:
		{
			// default concept widget
			ItemPopUp = new ConceptPopUp(ItemsName,ItemsTitle);
			break;
		}

		}
		return ItemPopUp;
	}

**/






}
