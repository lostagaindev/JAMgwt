package com.lostagain.JamGwt.InventoryObjectTypes;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.OptionalImplementations;
import com.lostagain.Jam.InstructionProcessing.CommandList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.InstructionProcessing.ActionSet.TriggerType;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.InventoryObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.SceneSpriteObjectState;
import com.lostagain.Jam.SceneObjects.Helpers.InventoryObjectHelpers;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.Jam.SceneObjects.Interfaces.hasUserActions;
import com.lostagain.JamGwt.TitledPopUpWithShadow;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;

import lostagain.nl.spiffyresources.client.SpiffyImageUtilitys;
import lostagain.nl.spiffyresources.client.spiffycore.Simple2DPoint;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;




/** 
 * This is literally one of the icons you see in a inventory.<br>
 * Like SceneObjects, they can have there own actions.<br>
 * They can also have associated InventoryObjectType popup objects 
 * (like tigs, movies,images whatever)<br>
 * and be displayed either as a picture or a bit of text.<br>
 * How they are displayed is controlled by the currentMode variable <br> 
 ***/
public class InventoryItem extends SceneSpriteObject implements IsInventoryItem,hasUserActions {

	public static Logger Log = Logger.getLogger("JAM.InventoryItem");


	private static final String INVENTORY_ICON_STYLENAME = "InventoryIcon";

	//shadows superclass one
	InventoryObjectState objectsCurrentState;

	/** If the Inventory item is a representation of a sceneobject, this is the objects name**/
	//TODO: this needs to be saved and loaded.
	//public SceneObject associatedSceneObject = null;

	/** If the icon is tied to a popup object, this will tell if you if its popped up or not.
	 *  ie. If the inventory item is an image, is the image open?*/
	Boolean popedup = false;

	/** Description of the item**/
	Label Discription = new Label("");

	/** Is the InventoryIcons image loaded yet? **/	
	Boolean loadedPic = false;


	/** 
	 * The DataURL of a 64x64 version of the image
	 * This will be generated when the url of this icon is set and the image first preloaded.
	 * This DataURL is typically used for an cursor item when the item is held 
	 * 
	 * FrameID, DataURL
	 ***/	
	private HashMap<String,String> DataURLofIcon = new HashMap<String,String>();


	/**
	 * Same as the above, but with a + on it as well to indicate item mixing
	 *  * FrameID, DataUR
	 **/
	private HashMap<String,String>  DataURLofIconWithPlus  = new HashMap<String,String>();


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
	public IsInventoryItemPopupContent PopUp; //Implementation


	int fixedxdepth = -1;
	/** simple stores this current icon when created so it can be refereed to easier in this code **/
	IsInventoryItem iconroute;
	final TitledPopUpWithShadow ItemWithShadow;




	


	//public enum IconMode {		
	//	Image,Text,CaptionedImage		
	//}

	IconMode currentMode = IconMode.Image; //defaults to image icons

	Label titlelab;



	private Runnable runwhenloaded;



	//NOTE: inventory items dont have any positioning! So these become no-op methods
	/**
	 * Does not do anything. InventoryItems dont have a position
	 */
	@Override
	protected void setPositionOnItsScene(int x, int y, int z, boolean restrictPositionToScreen) {
		//	super.setPositionOnItsScene(x, y, z, restrictPositionToScreen);
	}
	/**
	 * Does not do anything. InventoryItems dont have a position
	 */
	@Override
	public void setPosition(int x, int y, int z, boolean updateTouching) {
		//super.setPosition(x, y, z, updateTouching);
	}
	/**
	 * Does not do anything. InventoryItems dont have a position
	 */
	@Override
	public void setPosition(int x, int y, int z, boolean byPin, boolean ifOverLappingBringToFront,
			boolean updateTouching) {
		//super.setPosition(x, y, z, byPin, ifOverLappingBringToFront, updateTouching);
	}
	//--------------------------


	public Runnable getRunwhenloaded() {
		return runwhenloaded;
	}

	public void setRunwhenloaded(Runnable runwhenloaded) {
		this.runwhenloaded = runwhenloaded;
	}


	public void updateState(InventoryObjectState newstate,final boolean runOnload,final boolean repositionObjectsRelativeToThis) {

		//NOTE: inventory items are a special case where we run the specific data update before the general stuff
		//This is because SpriteObjectState has to potentially run "onLoad" itself - as it might need to wait for image changes
		Log.info("Updating inventory icon status");
		ObjectsLog("Updating inventory icon state ","orange");


		this.getObjectsCurrentState().associatedSceneObject = newstate.associatedSceneObject;
		this.getObjectsCurrentState().associatedSceneObjectWhenNeeded = newstate.associatedSceneObjectWhenNeeded;
		ObjectsLog("Associated object : "+newstate.associatedSceneObjectWhenNeeded);

		super.updateState(newstate, runOnload, repositionObjectsRelativeToThis);

		//Normally this class should handle the following, but the super has too due to the above reason
		/*
		if (runOnload && !urlchanged) {

			ObjectsLog.info("running objects onload again");
			alreadyLoaded = false; //has to be reset to false to run onload again
			onLoad();

		} else {

			ObjectsLog.info("run onload none specified");
		}

		if (repositionObjectsRelativeToThis){
			updateThingsPositionedRelativeToThis(true);
		}

		// update debug boxs
		if (oipu != null) {
			oipu.update();
		}*/

	}

	//InventoryObjectState
	public InventoryItem(
			final IsInventoryItemPopupContent ItemPopUp, 
			final String title,
			InventoryPanel sourceInventory, 
			SceneObjectState newobjectdata,
			String objectsCurrentStateasstring) {

		this (ItemPopUp,title,sourceInventory,new InventoryObjectState(newobjectdata),objectsCurrentStateasstring);
	}
	
	/**
	 * 
	 * @param ItemPopUp
	 * @param title
	 * @param sourceInventory
	 * @param newobjectdata
	 * @param objectsCurrentStateasstring
	 */
	public InventoryItem(
			final IsInventoryItemPopupContent ItemPopUp, 
			final String title,
			InventoryPanel sourceInventory, 
			InventoryObjectState newobjectdata,
			String objectsCurrentStateasstring) {

		//super(new InventoryObjectState(newobjectdata), objectsCurrentStateasstring, null);
		super( newobjectdata, splitActionsFromParametersAndActions(objectsCurrentStateasstring),null, new GWTAnimatedIcon() , false);

		//if (newobjectdata.getCurrentPrimaryType() == SceneObjectType.InventoryIcon){			
		//assignObjectTypeSpecificParameters();//newobjectdata.ObjectsParamatersFromFile); //? not sure I think the supertype will trigger our type anyway?
		//}
		objectsCurrentState = (InventoryObjectState) super.getObjectsCurrentState(); //sets this icons state object

		NativeInventoryPanel = sourceInventory;

		PopUp =  ItemPopUp;
		//Title = IconTitle;
		iconroute = this;



		//setup needs to run before the below (in future should be part of setup)

		// create object from data
		setupWidget(objectsCurrentState.ObjectsURL,
				objectsCurrentState.currentNumberOfFrames,
				null);


		titlelab=new Label(objectsCurrentState.Title);
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
		SceneObjectIcon.setPixelSize(100, 100); //used to be picture

		if (ItemPopUp != null) {

			ItemWithShadow = new TitledPopUpWithShadow(iconroute, "30%", "25%",
					objectsCurrentState.Title, PopUp);





		} else {
			ItemWithShadow = null;
		}

		//InventoryItems might be not attached to the page yet
		//This means normal image load handlers wont fire
		//Because of this, we check for it here, and load offscreen if they are not.
		//The onImageLoaded function then sets the widget if needed

		//set widget to loading if there isn't one already
		if (this.getWidget()==null || !SceneObjectIcon.isAttached() ){ //there might have not even be a widget set yet, in which case we use a temp label here
			setWidget(new Label("loading image")); //first we check if its already all set up by the time it gets to here - loading might have finished! yet this would override that widget

			//load offscreen first to preload
			SceneObjectIcon.getElement().setId("iconimage preload");
			RootPanel.get().add(SceneObjectIcon, -805, -800);

			// Name = Imagelocation;
			//this.setTitle(IconTitle);
			loadedPic = false;
		} else {
			SOLog.info("_inventory image already attached and set so firing imageLoaded. attached:"+SceneObjectIcon.isAttached());			
			imageloaded(); //ensure post load done ?
		}

		// Discription.setText(ImageDiscription);

		this.setStylePrimaryName(INVENTORY_ICON_STYLENAME);
		this.addStyleName("pngfix");



		//Picture.setUrl("InventoryItems/" + Iconlocation + "/thumb_" + Iconlocation
		//		+ ".png");


		//Log.info(("InventoryItems/" + Iconlocation + "/thumb_"
		//		+ Iconlocation + ".png"));

		//once loaded we copy into panel
		//not handled in the onLoad section which the parent SceneObjectClass deals with.
		/*
		Picture.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {


			}
		});
		 */

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
		SceneObjectIcon.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {

				// this cancels the click if there is a drag inbetween
				InventoryPanel.cancelClick = false;

			}

		});

		boolean finaliseLoad=true; //there are no subtypes of inventoryicon, so this can always be true
		if (finaliseLoad){

			// create object from data
			//	setupWidget(objectsCurrentState.ObjectsURL,
			//			objectsCurrentState.currentNumberOfFrames, null);

			Log.info("_________initialising inventoryicon and adding to database:"
					+ objectsCurrentState.ObjectsName.toLowerCase() + ":");

			initialiseAndAddToDatabase();

		}



		// assign handlers 
		sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
				| Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
				| Event.ONTOUCHSTART | Event.ONTOUCHEND | Event.ONTOUCHCANCEL|Event.ONTOUCHMOVE);

	}

	//static protected void imageloaded(SceneSpriteObjectState objectsCurrentState2, SceneWidget sceneItsOne) {
	//
	//	}


	protected void setupWidget(String imageZeroURL, int numOfFrames,
			final SceneWidget sceneItsOne) {		
		super.setupWidget(imageZeroURL, numOfFrames, sceneItsOne);

		ObjectsLog.log("Setting up InventoyIcon..","orange");

	}
	/**
	 * Sets the inner css to reflect the fact its being dragged
	 * @param status
	 */
	public void setCssAsDragging(boolean status){

		Log.info("________________Setting Css As Dragging:"+status);

		if (status){
			//remove normal style
			removeStyleName(INVENTORY_ICON_STYLENAME);
			//Add special styles
			SceneObjectIcon.addStyleName(INVENTORY_ICON_STYLENAME+"-dragging");
			this.addStyleName(INVENTORY_ICON_STYLENAME+"-dragging");

		} else {
			//put normal style back
			setStylePrimaryName(INVENTORY_ICON_STYLENAME);
			//Remove special styles
			SceneObjectIcon.removeStyleName(INVENTORY_ICON_STYLENAME+"-dragging");
			this.removeStyleName(INVENTORY_ICON_STYLENAME+"-dragging");

		}
	}

	@Override
	public void imageloaded(){

		if (this.getWidget()==null || this.getWidget()!=SceneObjectIcon){
			setWidget(SceneObjectIcon); //in case we were preloading offscreen or had some sort of loading message, we ensure its replaced here with the real image
		}

		Log.info("________________inventory icon loaded");

		SceneObjectIcon.getElement().setId("iconimage"); //used to be setting the picture
		SceneObjectIcon.setStylePrimaryName("InventoryIconInner");

		/** frame number for icon
		 * If the icon contains multiple frames, this is the frame number it will use for its image and cursor **/

		ObjectsLog("ImageBaseFilename:"+SceneObjectIcon.basefilename);
		
		ObjectsLog("setting up dataurld");

		for (int j = 0; j <  getObjectsCurrentState().currentNumberOfFrames; j++) {			
			int FrameNumberForIcon = j;
			iconiseImageAtFrame(FrameNumberForIcon);
		} //iconise all frames in advance


		//Log.info("________________Data URL SET Loaded "+DataURLofIcon.length());

		loadedPic = true;

		setIconMode(currentMode);

		if (runwhenloaded!=null){

			Runnable temp = runwhenloaded; //save it to a temp var
			Log.info("runwhendone clearing");
			runwhenloaded=null; //clear the run when done before running it. We do this rather then clearing after to ensure a crash in the running cant stop it being cleared 
			Log.info("runwhendone runing");	
			temp.run(); //run it						

		} else {
			Log.info("no runwhendone");	
		}



	}




	@Override
	public void setURLPhysically(boolean newAnimation) {
		super.setURLPhysically(newAnimation);

		for (int j = 0; j <  getObjectsCurrentState().currentNumberOfFrames; j++) {			
			int FrameNumberForIcon =j;
			iconiseImageAtFrame(FrameNumberForIcon);
		} 
	}


	private void iconiseImageAtFrame(int FrameNumberForIcon) {



		//associate with frame and url?
		final String FrameID = SceneObjectIcon.basefilename+FrameNumberForIcon;
		ObjectsLog("Creating dataurl for ID: "+FrameID+"");

		if (DataURLofIcon.containsKey(FrameID)){
			ObjectsLog("Already have dataurl for:  "+FrameID+"");			
			return;
		}



		//
		// TODO: for single frame currently the url is wrong, it misses the last letter.
		// This is probably due to inventoryitems not having 0's at the end for single frames.
		//
		final Image imagetoiconnise = SceneObjectIcon.getImageAtFrame(FrameNumberForIcon);

		ObjectsLog("originalFileNameHadNoZerol is: "+SceneObjectIcon.originalFileNameHadNoZero);
		ObjectsLog("imagetoiconnise url is: "+imagetoiconnise.getUrl()+"");
		
		//if image is ready then we set the dataurl, else we add a loadhandler and wait
		if (imagetoiconnise.getHeight()>5){
			
			Log.info("cacheing dataurl imediately");				
			cacheDataURLFromThisLoadedImage(FrameID, imagetoiconnise);	
			
		} else {
			ObjectsLog("waiting for image to  load for frameid: "+FrameID+"");
			Log.info("waiting for image load in order to set dataurl");
			
			imagetoiconnise.addLoadHandler(new LoadHandler() {			
				@Override
				public void onLoad(LoadEvent event) {
					
					Log.info("cacheing dataurl after load");	
					ObjectsLog("cacheing dataurl after load: "+FrameID+"");
					
					cacheDataURLFromThisLoadedImage(FrameID, imagetoiconnise);
					
				}
			});
			
			imagetoiconnise.addErrorHandler(new ErrorHandler() {
				
				@Override
				public void onError(ErrorEvent event) {
					
					Log.severe("cacheing dataurl after load ERROR, using default arrow instead");	
					ObjectsLog("cacheing dataurl after load ERROR: "+FrameID+"");
					
					String DataURLofIconString         =  SpiffyImageUtilitys.getSmallArrowDataURL();		
					String DataURLofIconWithPlusString =  SpiffyImageUtilitys.getSmallArrowWithPlusDataURL();

					ObjectsLog("DataURL content: "+imagetoiconnise.getHeight()+": "+imagetoiconnise.getUrl() );
					ObjectsLog("DataURL content: "+imagetoiconnise.getUrl() +": "+DataURLofIconString);

					DataURLofIcon.put(FrameID,DataURLofIconString);
					DataURLofIconWithPlus.put(FrameID, DataURLofIconWithPlusString);
				}
			});
			
		}
		
		
		
	}



	/** this allows us to detect clicks on the icon , probably should override**/

	public void onBrowserEvent(Event event) {

		Log.info("on browser event from inventory icon");


		switch (DOM.eventGetType(event)) {


		//check the type of click (contextment type = rightclick)
		case Event.ONCONTEXTMENU:
		{
			event.preventDefault();

			updateLastClickedLocation(); //could we just use ? 
		//	int x = Event.getCurrentEvent().getClientX();
			//int y = Event.getCurrentEvent().getClientY();
	
			InventoryObjectHelpers.onInventoryItemRightClick_HELPER(this);

			break;
		}
		case Event.ONCLICK:
		{
			//int x = Event.getCurrentEvent().getClientX();
		//	int y = Event.getCurrentEvent().getClientY();
			
			updateLastClickedLocation(); //could we just use this? 
	
			InventoryObjectHelpers.onInventoryItemClick_HELPER(this);


			break;
		}
			//inventory items now support rollovers!
			//
		case Event.ONMOUSEOUT:			
			//If we are currently holding a item we should chance the mouse cursor back to reflect it not being over this
			if (InventoryPanelCore.isItemBeingHeldOrDragged()){

				SOLog.info("_________________ONMOUSEOUT when holding");
				IsInventoryItem itemBeingHeldOrDragged = InventoryPanelCore.getCurrentItemBeingHeldOrDragged();
				OptionalImplementations.setMouseCursorTo(itemBeingHeldOrDragged);
			}			
			//----------
			// SOLog.info("_________________Event.ONMOUSEOUT");
			// run object actions
			if (actionsToRunForMouseOut != null) {
				wasLastObjectUpdated();
				updateLastClickedLocation();
				InstructionProcessor.processInstructions(actionsToRunForMouseOut.getActionsArray().getActions(), "mou_"	+ objectsCurrentState.ObjectsName, this);
			}

			break;
		case Event.ONMOUSEOVER:

			//if we are currently holding a item we should chance the mouse cursor
			if (InventoryPanelCore.isItemBeingHeldOrDragged()){
				SOLog.info("_________________ONMOUSEOVER when holding");
				IsInventoryItem itemBeingHeldOrDragged = InventoryPanelCore.getCurrentItemBeingHeldOrDragged();
				OptionalImplementations.setMouseCursorToHoldingOver(itemBeingHeldOrDragged,this);
			}

			// SOLog.info("_________________Event.ONMOUSEOVER");
			// run object actions
			if (actionsToRunForMouseOver != null) {

				wasLastObjectUpdated();

				updateLastClickedLocation();

				InstructionProcessor.processInstructions(
						actionsToRunForMouseOver.getActionsArray().getActions(), "mov_"
								+ objectsCurrentState.ObjectsName, this);

			} else {
				// SOLog.info("_________________no actions found for mouse over");
			}
			break;

		}

		//probably have to disable some specific internals of this?
		//which we have. artm, the super does nothing for inventoyitems
		super.onBrowserEvent(event);

	}

	
	/** change this between picture and icon mode
	 * CaptionedImage mode not yet supported, but should be easier with a vertical panel  **/
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

				super.setWidget(SceneObjectIcon);

				super.setHeight("100px");
			}

		} 
		if (mode==IconMode.CaptionedImage)
		{

		} 
	}
	public void fireOnItemAddedToInventoryCommands() {
		int x=0,y=0;
		if (Event.getCurrentEvent()!=null){
			 x = Event.getCurrentEvent().getClientX();
			 y = Event.getCurrentEvent().getClientY();
		}
		
		if (objectsActions != null) {
			
			CommandList localOnItemAddedActions = objectsActions
					.getActionsForTrigger(TriggerType.OnItemAdded, null);

			InventoryObjectHelpers.processActionsFromThisInventoryItem(this,localOnItemAddedActions,x,y);
			
		}

		if (InventoryPanelCore.globalInventoryActions != null) {
			
			CommandList globalOnItemAddedActions = InventoryPanelCore.globalInventoryActions
					.getActionsForTrigger(TriggerType.OnItemAdded, null);
			
			if (globalOnItemAddedActions.isEmpty()){
				ObjectsLog("no OnItemAdded global inventory actions found");
			}
			
			InventoryObjectHelpers.processActionsFromThisInventoryItem(this,globalOnItemAddedActions,x,y);	
			
		} else {
			ObjectsLog("no global inventory actions");
			
		}

		return;
	}


	@Override
	public boolean isPopedUp() {
		return popedup;
	}

	@Override
	/** has this icon triggered a current popup? */
	public void setPopedUp(Boolean settothis) {

		popedup = settothis;

		if (popedup){

			//fire event for item opening
			if (objectsActions != null) {

				Log.info("firing inventory icon open actions");	
				CommandList actionsToRunWhenOpened = objectsActions
						.getActionsForTrigger(TriggerType.OnItemOpen, null);
				int x = Event.getCurrentEvent().getClientX();
				int y = Event.getCurrentEvent().getClientY();
		
				InventoryObjectHelpers.processActionsFromThisInventoryItem(this,actionsToRunWhenOpened,x,y);
			}


		} else {

			//fire event item closing
			if (objectsActions != null) {				

				Log.info("firing inventory icon close actions");	
				CommandList actionsToRunWhenClose = objectsActions
						.getActionsForTrigger(TriggerType.OnItemClose, null);
				int x = Event.getCurrentEvent().getClientX();
				int y = Event.getCurrentEvent().getClientY();
		
				InventoryObjectHelpers.processActionsFromThisInventoryItem(this,actionsToRunWhenClose,x,y);
			}

		}


	}
	public void setFixedZdepth(int setDepth) {
		fixedxdepth = setDepth;

	}


	/** trigger its popup  */
	final public void triggerPopup() {

		String type = ((IsPopupContents)(PopUp)).POPUPTYPE();

		// check this icon even has a popup to trigger
		if ( (ItemWithShadow == null) || (type.equalsIgnoreCase("Dummy"))  ) {
			Log.info("not a popupable item (Dummy etc)");

			return;
		}

		Log.info("Popping up...|" + this.objectsCurrentState.ObjectsName + "| type is "+type);


		if (popedup == false) {

			// disable icon
			//popedup = true;
			setPopedUp(true);

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


	@Override
	public void userActionTriggeredOnObject(String userActionsName) {

		// we need to implement a similar inventory action system here that the
		// scene items have

		Log.info("inv icon:"+this.getObjectsCurrentState().ObjectsName+" triggering user action:" + userActionsName);


		// check for object specific actions
		if (objectsActions!=null){

			CommandList actions = objectsActions.getActionsForTrigger(
					TriggerType.UserActionUsed, userActionsName);

			Log.info("item actions found: \n" + actions.toString()+"out of "+objectsActions.size());
			// in future this function should support an arraylist
			InstructionProcessor.processInstructions(actions, "FROM_inventory"
					+ "_" + this.objectsCurrentState.ObjectsName,this);

		}

	}

	public String getDataURL() {

		String FrameID = SceneObjectIcon.basefilename+this.getObjectsCurrentState().currentFrame;
		Log.info("gettingdataurl for "+FrameID);	
		String dataurl = DataURLofIcon.get(FrameID);

		if (dataurl!=null){
			Log.info("dataurl retrieved:"+dataurl);	
			return dataurl;
		}  else {
			Log.info("no dataurl retrieved");	

		}

		return null;
	}

	public String getDataURLWithPlus() {
		String FrameID = SceneObjectIcon.basefilename+this.getObjectsCurrentState().currentFrame;
		Log.info("gettingdataurl+ for "+FrameID);	

		if (DataURLofIconWithPlus.get(FrameID)!=null){
			return DataURLofIconWithPlus.get(FrameID);
		}  else {
			Log.info("no dataurl retrieved");	


		}
		return null;
	}

	public Image getImage() {

		return SceneObjectIcon.getCurrentImage();
	}


	//	@Override
	//	protected void assignObjectTypeSpecificParameters() { //String itemslines[]
	//		if (objectsCurrentState!=null){
	//			Log.info("(inventory parameters already set up)");
	//			return;
	//		}
	//			
	//			
	//			super.assignObjectTypeSpecificParameters(); //itemslines
	//		
	//		
	//		//no need to do this anymore, as the constructor will
	//		//call this function for each layer of the class hyrachy
	//		//we could still, however, put in some sort of check to ensure its only loaded once?
	//		
	//		//note; parent parameters would already be assigned by this point as that happens by the constructors super(..) call
	//		objectsCurrentState = (InventoryObjectState) super.getObjectsCurrentState(); //sets this icons state object
	//		
	//		//inventory specific parameters should be assigned below
	//		//Log.info("__assigning inventoryicon ObjectTypeSpecificParameters to icom___");
	//		//(none to set!)
	//		//Log.info("__assigned inventoryicon  ObjectTypeSpecificParameters to icon___");
	//		
	//	}
	////
	//	

	//positions are meaningless for inventory icons
	public Simple2DPoint getUpperRight() {
		return null;
	}

	public Simple2DPoint getUpperLeft() {
		return null;
	}

	public Simple2DPoint getLowerRight() {
		return null;
	}

	public Simple2DPoint getLowerLeft() {
		return null;
	}

	public Simple2DPoint getLowerLeftDisplacement() {
		return null;
	}

	public void setKeepHeldMode(KeepHeldMode mode) {
		keepheld = mode;
	}

	@Override
	public KeepHeldMode getKeepHeldMode() {
		return keepheld;
	}

	@Override
	public IsInventoryItemPopupContent getPopup() {
		return this.PopUp;
	}

	//	@Override
	//	public isInventoryItemImplementation getPopupImpl() {
	//		return this.PopUp;
	//	}


	@Override
	public SceneObject getAssociatedSceneObject() {
		return this.getObjectsCurrentState().getAssociatedSceneObject();

	}

	/**
	 * sets the initial state to the supplied state, the state should match this objects type 
	 * as a untested cast is used
	 * @param objectsInitialState
	 */
	//@Override
	protected void setObjectsInitialState(SceneObjectState objectsInitialState) {		
		super.setObjectsInitialState(objectsInitialState);
		initialObjectState=(InventoryObjectState) objectsInitialState;
	} //TODO: this surely shouldn't be needed? theres only one initial object state in SceneObject and they all refer to that anyway?
	//Does everything, including inspector, work fine without this? 





	@Override
	public InventoryObjectState getObjectsCurrentState() {
		return (InventoryObjectState) objectsCurrentState;
	}

	@Override
	public InventoryObjectState getInitialState() {
		return (InventoryObjectState) initialObjectState;
	}

	@Override
	public InventoryObjectState getTempState() {
		return (InventoryObjectState) tempObjectState;
	}

	@Override	
	public InventoryPanelCore getNativeInventoryPanel() {	
		return this.NativeInventoryPanel;
	}

	@Override
	public void setPopupsBackgroundToTransparent() {
		ItemWithShadow.clearBackgroundStyles();

	}
	private void cacheDataURLFromThisLoadedImage(final String FrameID, final Image imagetoiconnise) {
		
		Log.info("________________SceneObjectIcon height        ="+imagetoiconnise.getOffsetHeight()+"x"+imagetoiconnise.getOffsetWidth());

		Log.info("________________Loading DATA Url from image of "+imagetoiconnise.getUrl()+" which is attached:"+imagetoiconnise.isAttached());

		String DataURLofIconString         =  SpiffyImageUtilitys.getDataURLFromImage(imagetoiconnise,64,64,SpiffyImageUtilitys.SmallArrow);		
		String DataURLofIconWithPlusString =  SpiffyImageUtilitys.getDataURLFromImage(imagetoiconnise,64,64,SpiffyImageUtilitys.SmallArrowWithPlus);

		ObjectsLog("DataURL content: "+imagetoiconnise.getHeight()+": "+imagetoiconnise.getUrl() );
		ObjectsLog("DataURL content: "+imagetoiconnise.getUrl() +": "+DataURLofIconString);

		DataURLofIcon.put(FrameID,DataURLofIconString);
		DataURLofIconWithPlus.put(FrameID, DataURLofIconWithPlusString);

		//clear up image 
		if (imagetoiconnise.isAttached() && imagetoiconnise.getElement().getId().equals("_TEMP_IMAGE_")){
			imagetoiconnise.removeFromParent();
		}
		
	}
	@Override
	public void triggerPickedUp(boolean b) {
		// no need to do anything
		
	}
	@Override
	public void triggerPutDown() {
		// TODO Auto-generated method stub
		
	}




}