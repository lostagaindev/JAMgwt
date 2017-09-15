package com.lostagain.JamGwt;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.InstructionProcessing.ActionSet.TriggerType;
import com.lostagain.Jam.InstructionProcessing.CommandList;
import com.lostagain.Jam.Interfaces.IsPopupPanel;
import com.lostagain.Jam.SceneObjects.PropertySet;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryIcon;
import com.lostagain.Jam.SceneObjects.Interfaces.hasUserActions;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;
import com.darkflame.client.SpiffyImageUtilitys;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.lostagain.JamGwt.InventoryObjectTypes.imagePopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.DummyPopUp;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryPanel;



/** This is literally one of the icons you see in a inventory.<br>
 * Like SceneObjects, they can have there own actions.<br>
 * They can also have associated InventoryObjectType popup objects 
 * (like tigs, movies,images whatever)<br>
 * and be displayed either as a picture or a bit of text.<br>
 * How they are displayed is controlled by the currentMode variable <br> **/
public class InventoryIcon_bak extends FocusPanel implements IsInventoryIcon,
		hasUserActions {

	static Logger Log = Logger.getLogger("JAM.InventoryIcon");
	
	/** The inventory items name. This should be the same as the directory its stored in within "InventoryItems"
	 * Note; This is no the same as the tooltip "title" that might have been assigned to it **/
	public String Name = "";

	/** If the Inventory item is a representation of a sceneobject, this is the objects name**/
	public String associatedSceneObject = null;
	
	/** If the icon is tied to a popup object, this will tell if you if its popped up or not.
	 *  ie. If the inventory item is an image, is the image open?*/
	Boolean popedup = false;

	/**  the tooltip "title" that might have been assigned to this icon in its  jam file**/
	String Title = "";
	
	/** Description of the item**/
	Label Discription = new Label("");
	
	/** the InventoryIcons image **/	
	Image Picture = new Image();
	
	/** Is the InventoryIcons image loaded yet? **/	
	private Boolean loadedPic = false;
	
	/** KeepHeld mode - normally a item is dropped back into the inventory after use.
	 * This mode determains if it should be kept held  on certain conditions **/
	public enum KeepHeldMode {
		never,onuse
	}
	
	
	public KeepHeldMode keepheld = KeepHeldMode.never;
	
	/*** The objects properties.<br>
	 * <br>
	 * This is a full semantically power system. You can add and remove<br>
	 * Properties, and a test for a property will also automatically count all it subclass's<br>
	 * NOTE: PROPERTIES ON ITEMS DONT SAVE AT THE MOMENT. STARTUP PROPERTIES ONLY. **/
	public PropertySet objectsProperties = new PropertySet();
	
	
	/** The DataURL of a 64x64 version of the image
	 * This will be generated when the url of this icon is set and the image first preloaded.
	 * This DataURL is typically used for an cursor item when the item is held **/
	private String DataURLofIcon = null;
	
	
	// screen size
	int ScreenSizeX = Window.getClientWidth();
	int ScreenSizeY = Window.getClientHeight();
	//int InventorySizeX = (100 * (Window.getClientWidth() / 150));
	//int InventorySizeY = (100 * (Window.getClientHeight() / 150));

	//int roundTo = 100;
	//int RInventorySizeX = (int) ((InventorySizeX + (0.5 * roundTo)) / roundTo)
	//		* roundTo;
	//int RInventorySizeY = 20
		//	+ (int) ((InventorySizeY + (0.5 * roundTo)) / roundTo) * roundTo;
	
/** the inventory panel which this widget natively belongs too **/
public InventoryPanel NativeInventoryPanel = null;
	
	// associated Widget to popup
	Widget PopUp = new Widget();
	int fixedxdepth = -1;
	/** simple stores this current icon when created so it can be refered to easier in this code **/
	InventoryIcon_bak iconroute;
	final PopUpWithShadow ItemWithShadow;

	// actions to run under certain connections (most common with right click)
	public ActionList itemsActions = new ActionList();

	
	
	
	static boolean iconclickedrecently=false;
	
	enum IconMode {		
		Image,Text,CaptionedImage		
	}
	
	IconMode currentMode = IconMode.Image; //defaults to image icons
	
	Label titlelab;
	
	public InventoryIcon_bak(final IsPopupPanel ItemPopUp, final String Iconlocation,
			final String IconTitle, InventoryPanel sourceInventory) {

		NativeInventoryPanel = sourceInventory;

		PopUp = (Widget) ItemPopUp;
		Name = Iconlocation;
		Title = IconTitle;
		iconroute = this;
		
		 titlelab=new Label(Title);
		 titlelab.getElement().getStyle().setBorderWidth(2, Unit.PX);
		 
			
		 titlelab.addMouseOverHandler(new MouseOverHandler(){

			@Override
			public void onMouseOver(MouseOverEvent event) {
				titlelab.getElement().getStyle().setFontWeight(FontWeight.BOLD);
				
			}
			 
		 });
		 titlelab.addMouseOutHandler(new MouseOutHandler(){

				@Override
				public void onMouseOut(MouseOutEvent event) {
					titlelab.getElement().getStyle().clearFontWeight();
					
				}
				 
			 });
		 
		this.setSize("100px", "100px");
		Picture.setPixelSize(100, 100);
		
		if (ItemPopUp != null) {
			ItemWithShadow = new PopUpWithShadow(iconroute, "30%", "25%",
					Title, (IsPopupPanel) PopUp);
		} else {
			ItemWithShadow = null;
		}
		
		setWidget(new Label("loading image"));
		
		
		//load offscreen first to preload
		Picture.getElement().setId("iconimage preload");
		RootPanel.get().add(Picture, -805, -800);
		
		
		
		// Discription.setText(ImageDiscription);
		this.setStylePrimaryName("pngfix");
		this.addStyleName("InventoryIcon");
		
		// Name = Imagelocation;
		this.setTitle(IconTitle);
		 loadedPic = false;
		Picture.setUrl("InventoryItems/" + Iconlocation + "/thumb_" + Iconlocation
				+ ".png");
		

		Log.info(("InventoryItems/" + Iconlocation + "/thumb_"
				+ Iconlocation + ".png"));

		//once loaded we copy into panel
		Picture.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				Picture.getElement().setId("iconimage");

				Log.info("________________Loading DATA Url from image of "+Picture.getUrl()+" which is attached:"+Picture.isAttached());
				
				DataURLofIcon =  SpiffyImageUtilitys.getDataURLFromImage(Picture,64,64,SpiffyImageUtilitys.SmallArrow);
				
				Log.info("________________Data URL SET Loaded");

				 loadedPic = true;
				
				setIconMode(currentMode);
				
			}
		});
		
		// this.add(Discription);
		// Add the items enlarger click listener if it has one.

		// this.addClickHandler(new ClickHandler(){
		//
		// public void onClick(ClickEvent event) {
		// //make sure this item is designed to popup on click
		// if (((isPopUpType)PopUp).POPUPONCLICK()==true){
		//
		// //and this isnt the end of a drag/drop operation
		// if (!(InventoryPanel.cancelClick)){
		// triggerPopup();
		// }
		//
		// InventoryPanel.cancelClick = true;
		// }
		//
		// }
		//
		// });
		Picture.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {

				// this cancels the click if there is a drag inbetween
				InventoryPanel.cancelClick = false;

			}

		});

		// assign handlers 
		sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
				| Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
				| Event.ONTOUCHSTART | Event.ONTOUCHEND | Event.ONTOUCHCANCEL|Event.ONTOUCHMOVE);

	}

	/** this allows us to detect clicks on the icon**/
	public void onBrowserEvent(Event event) {

		switch (DOM.eventGetType(event)) {

		//check the type of click (contextment type = rightclick)
		case Event.ONCONTEXTMENU:
			event.preventDefault();

			if (itemsActions != null) {
				CommandList localRightClickActions = itemsActions
						.getActionsForTrigger(TriggerType.MouseRightClickAction, null);
				
				processActionsFromThisIcon(localRightClickActions);
			}

			if (InventoryPanel.globalInventoryActions != null) {
				CommandList defaultRightClickActions = InventoryPanel.globalInventoryActions
						.getActionsForTrigger(TriggerType.MouseRightClickAction, null);
				processActionsFromThisIcon(defaultRightClickActions);
			}

			break;

		case Event.ONCLICK:
			
			iconclickedrecently=true;
			
			Boolean actions=false;
			
			if (itemsActions != null) {
				CommandList actionsToRunForMouseClick = itemsActions
						.getActionsForTrigger(TriggerType.MouseClickAction, null);
			//	Log.info("_________________Event.BUTTON_Click inv1");
				processActionsFromThisIcon(actionsToRunForMouseClick);
				actions=true;
			}

			if (InventoryPanel.globalInventoryActions != null) {
				CommandList defaultMouseClickActions = InventoryPanel.globalInventoryActions
						.getActionsForTrigger(TriggerType.MouseClickAction, null);
			//	Log.info("_________________Event.BUTTON_Click inv2 ");

				processActionsFromThisIcon(defaultMouseClickActions);
				actions=true;
			}
			
			if (!actions){
				//raise inventory panel to front
			//	MyApplication.allInventoryFrames.get(NativeInventoryPanel.Title).setZIndexTop();
				//
			}
			
			break;
		
		}

		super.onBrowserEvent(event);

	}
	/** change this between picture and icon mode
	 * CaptionedImage mode not yet supported, but should be easier with a verticalpanel  **/
	public void setIconMode(IconMode mode){
		currentMode = mode;
		
		if (mode==IconMode.Text)
		{
			super.setWidget(titlelab);
			super.setHeight("30px");
			
		} 
		if (mode==IconMode.Image)
		{
			//set if not loading
			if (loadedPic){
				super.setWidget(Picture);
				super.setHeight("100px");
			}
			
		} 
		if (mode==IconMode.CaptionedImage)
		{
			
		} 
	}

	private void processActionsFromThisIcon(CommandList actions) {
		if (!actions.isEmpty()) {

			int x = Event.getCurrentEvent().getClientX();
			int y = Event.getCurrentEvent().getClientY();

			InstructionProcessor.lastInventoryObjectClickedOn = this;
			InstructionProcessor.lastSceneObjectClickedOn = null;
			InstructionProcessor.lastTextObjectUpdated = null;
			
			InstructionProcessor.lastclicked_x = x+super.getAbsoluteLeft();
			InstructionProcessor.lastclicked_y = y+super.getAbsoluteTop();

			InstructionProcessor.lastclickedscreen_x = x;
			InstructionProcessor.lastclickedscreen_y = y;
		
			InstructionProcessor.processInstructions(actions, "rci_"
					+ this.Name,null);

		}
	}

	/** has this icon triggered a current popup? */
	public void setPopedUp(Boolean settothis) {
		popedup = settothis;

	}

	public void setFixedZdepth(int setDepth) {
		fixedxdepth = setDepth;

	}
	/** trigger its popup  */
	final public void triggerPopup() {
		
		 String type = ((isPopUpType)(PopUp)).POPUPTYPE();
			
		// check this icon even has a popup to trigger
		if ( (ItemWithShadow == null) || (type.equalsIgnoreCase("Dummy"))  ) {
			Log.info("not a popupable item (Dummy etc)");
			
			return;
		}
		
		Log.info("Popping up...|" + this.Name + "| type is "+type);
		  		

		if (popedup == false) {

			// disable icon
			popedup = true;

			// int posX = (int)Math.round((ScreenSizeX/2 - ((ScreenSizeX *
			// 0.8)/2)));

			// int posY = (int)Math.round((ScreenSizeY/2 - ((ScreenSizeY *
			// 0.78)/2)));

			// MyApplication.fadeback.setSize("100%", "100%");
			// RootPanel.get().add(MyApplication.fadeback,0, 0);

			// final PopUpWithShadow ItemWithShadow = new
			// PopUpWithShadow(iconroute,"30%","25%",Title,PopUp);

			if (fixedxdepth > -1) {
				ItemWithShadow.fixedZdepth(fixedxdepth);
			}
			ItemWithShadow.OpenDefault();

		} else {
			// if its already popped up, we close it.
			ItemWithShadow.CloseDefault();

		}
	}

	public void loadActionsFromActionList(ActionList actions) {
		itemsActions = actions;
	}

	public void loadActionsFromString(String actions) {
		if (actions.length() > 0) {
			//Log.info("actions are:" + actions);

			itemsActions = new ActionList(actions);
			// updateHandlersToMatchActions();

		}
	}

	public void userActionTriggeredOnObject(String userActionsName) {

		// we need to implement a similar inventory action system here that the
		// scene items have
		// for now we just have examine, which does the default action

		Log.info("triggering user action:" + userActionsName);

		// if (userActionsName.equalsIgnoreCase("examine")){
		// this.triggerPopup();
		// }

		// check for object specific actions
		if (itemsActions!=null){
		
			CommandList actions = itemsActions.getActionsForTrigger(
				TriggerType.UserActionUsed, userActionsName);

		Log.info("item actions found: \n" + actions.toString());
		// in future this function should support an arraylist
				InstructionProcessor.processInstructions(actions, "FROM_inventory"
						+ "_" + this.Name,null);

		}
		
	}

	public String getDataURL() {
		
		if (DataURLofIcon.length()>3){
			return DataURLofIcon;
		} 
		
		return null;
	}
	
	public boolean hasProperty(String testThis) {

		return objectsProperties.hasProperty(testThis);
	}
	
	//Note; No "add property" or "remove property" functions are supplied yet
	//because saving of property changes isnt supported on items.
	//in fact, other then having or not having an item, no details or changes to items are saved at all.
	//Consisderation has to be put into how best to handle saving of item details
	//Do we have states like sceneobjects? 
	//Do we even change inventory icons into a type of scene object? How much work will that bit? >???
	//We could either make them their own object type by just extending SceneObject
	//Or we could maybe make them just SceneObjectSprites?
	//Maybe extend sprites? (so they are basicly sprites with also a few other features, like having an associated popup maybe?)
	
	
	
	
	

}