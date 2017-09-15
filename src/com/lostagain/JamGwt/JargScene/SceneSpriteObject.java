package com.lostagain.JamGwt.JargScene;

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.SceneSpriteObjectState;
import com.lostagain.Jam.SceneObjects.Helpers.SpriteObjectHelpers;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneSpriteObject;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;
import com.lostagain.JamGwt.Sprites.InternalAnimations;

/** Any interactive sprite object in a scene 
 *  Sprite objects are based around a single AnimatedIcon
 *  which can have its url set to change its animation.
 *  (ie, changing the direction of a character to play the correct walkcycle)
 *  Sprites also can keep track of what they are touching, and if something new touches them.
 *  This is done manually as in a 2D scene, the overlap of sprites on the page doesn't necessarily mean they are "really" touching from a 3d perspective
 *  **/
public class SceneSpriteObject extends SceneDivObject implements IsSceneSpriteObject   { //extends SceneObjectVisual

	public static Logger Log = Logger.getLogger("JAM.SceneSpriteObject");
	
	private class SpriteImageErrorHandler implements ErrorHandler {
		@Override
		public void onError(ErrorEvent event) {

			Log.info("__subtracting "+thisObject.initialObjectState.ObjectsName+" loading because file errored:"+SceneObjectIcon.getUrl());

			ObjectsLog("error ="+getPhysicalObjectWidth()+","+getPhysicalObjectHeight(),"green");
			ObjectsLog("error ="+getOffsetWidth()+","+getOffsetHeight(),"green");
			//imageloaded(objectsCurrentState,sceneItsOne);
			imageloaded();

		}
	}

	private class SpriteFirstImageLoadedHandler implements LoadHandler {
		@Override
		public void onLoad(LoadEvent event) {
			Log.info("__subtracting from loading::");

			//temp test, don't need this here yet
			Scheduler.get().scheduleDeferred(new ScheduledCommand(){
				@Override
				public void execute() {
					
				//	ObjectsLog("Size          ="+getObjectWidth()+","+getObjectHeight(),"green");            //0,0 
				//	ObjectsLog("is Attached   ="+SceneObjectIcon.isAttached(),"green");                      //true
				//	ObjectsLog("is Attached   ="+SceneObjectIcon.imageContents.isAttached(),"green");        //true												
					ObjectsLog("offset 1       ="+SceneObjectIcon.getOffsetWidth()+","+SceneObjectIcon.getOffsetHeight(),"green");			 //0,0	
					ObjectsLog("offset 2       ="+getOffsetWidth()+","+getOffsetHeight(),"green");			 //0,0	
					
					//ObjectsLog("so element:      "+getInternalGwtWidget().toString() , "blue");			 //0,0	
					ObjectsLog("SceneObjectIcon element:      "+SceneObjectIcon.toString() , "blue");			 //0,0	
					
					ObjectsLog("image itself  ="+SceneObjectIcon.imageContents.getWidth()      +","+SceneObjectIcon.imageContents.getHeight(),      "green");		 // works		
					
					//remove handler so this wont fire on subsequent loads
					ONLOADHANDLE.removeHandler();

					SpriteNewURLImageLoadedHandler ONNEWURLLOADED =  new SpriteNewURLImageLoadedHandler(getPhysicalObjectWidth(),getPhysicalObjectHeight(),SceneSpriteObject.this);
					ONNEWURLLOADEDHANDLE = SceneObjectIcon.addLoadHandler(ONNEWURLLOADED);
					
					Log.info("__subtracting from loading::"+SceneObjectIcon.getUrl());

					//imageloaded(objectsCurrentState,sceneItsOne);
					imageloaded();						
				}					
			});
			

			

		}
	}

	/**
	 * This handler is for when a new url is set.
	 * This lets us check if the image size has changed, as this will potentially mean rechecking the zindex based on position
	 * (as the baseline would have shifted)
	 * 
	 * @author darkflame	 
	 */
	static private class SpriteNewURLImageLoadedHandler implements LoadHandler {
		int currentImageX = 0;
		int currentImageY = 0;
		private SceneSpriteObject sprite;		
		
		public SpriteNewURLImageLoadedHandler(int objectWidth, int objectHeight, SceneSpriteObject sprite) {
			this.currentImageX = objectWidth;			
			this.currentImageY = objectHeight;
			this.sprite=sprite; //storeing the sprite lets this whole class be static, which is likely more efficient
		}

		@Override
		public void onLoad(LoadEvent event) {
			int newX =  sprite.getOffsetWidth();
			int newY = sprite.getPhysicalObjectHeight();
			
			if (newX!=currentImageX ||
				newY!=currentImageY){
				//
				sprite.ObjectsLog(" _ImageURL change resulted in size change: old size was: "+currentImageX+","+currentImageY+" _new size is: "+newX+","+newY);
				//run the sizechecks, this function will ensure anything that is size-dependant is rechecked
				sprite.objectSizeChanged();
				
				
			}
			
			currentImageX = newX;
			currentImageY = newY;
			
			
			
		
		}
		
	}
	
	


	/**
	 * stores the current object state as a sprite state.
	 * This shadows the same object in SceneDivObject and also SceneObject
	 *  ALL object interactions should update this, or read from this.
	 **/
	public SceneSpriteObjectState objectsCurrentState;

	// ----------------------------------------------------------------------------
	//public SceneSpriteObjectState defaultObjectState;
	//
	//public SceneSpriteObjectState tempObjectData;


	// scene objects main image widget
	public GWTAnimatedIcon SceneObjectIcon;

	//
	Command clearAnimationState;

	// dynamic values (can change)

	// this object
	SceneSpriteObject thisObject = this;





	/** This will store the actions that happens when the sprite loads for the first time*/
	private HandlerRegistration ONLOADHANDLE;

	/** This will store the actions that happens when the sprite loads for the first time with an error (ie, the url isn't found) */
	private HandlerRegistration ONERRORHANDLE;
	
	/** This will store the actions that happens when the sprite loads a new image from a url after the first time */
	private HandlerRegistration ONNEWURLLOADEDHANDLE;
	
	
	


	/** Determines if the object is is moving **/

	// UNDO DATA STORAGE

	/** stores the current and all previous animation states for this object **/
	// Stack<String> previousStates = new Stack<String>();
	/** This constructor is designed for cloning objects, as it lets you supply a new name and scene for the object
	 * thats different to whats in newobjectdata 
	 * 
	 * this constructor currently wont work correctly as some fields arnt copied.
	 * However;
	 * SceneSpriteObject(SceneObjectState newobjectdata, String objectsCurrentState,
			SceneWidget sceneItsOn does work

			Cloning will be readdressed once the normal construction system has changed to the new more efficient system
	 **/
	/*
	@Deprecated
	public SceneSpriteObject(SceneSpriteObjectState newobjectdata,
			SceneWidgetVisual scene, String newname, ActionList actions,
			PropertySet props,
			VariableSet ObjectRuntimeVariables, 
			boolean runOnLoad) {

		// generic scene object setup (we could copy some of the things common to all objects over in this super
		// by giving it SceneSpriteObjectState, but using SceneObjectState so it only deals with things common to all objects)
		super(null,actions,scene); //broken now

		objectsCurrentState = new SceneSpriteObjectState(newobjectdata);

		//currentObjectState = newobjectdata;
		super.getObjectsCurrentState() = objectsCurrentState;

		//generic object data setup

		objectsCurrentState.setGenericObjectData(newobjectdata); //should be jus currentObjectState = newobjectdata; 

		//clone and sprite specific overrides 
		objectsCurrentState.ObjectsName = newname;
		objectsCurrentState.ObjectsSceneName = scene.SceneFileName;

		Log.info("scene set too:"+scene.SceneFileName);

		//currentObjectState.ObjectsFileName = newobjectdata.ObjectsFileName;

		Log.info("ObjectsURL set too:"+newobjectdata.ObjectsURL);
		ObjectsLog.info("ObjectsURL setting too:"+newobjectdata.ObjectsURL);


		//before setting the url we should ensure its stripped down to original filename
		//ie :Game Scenes/beforechurch/Objects/archflag/archflag13.png
		//should become :Game Scenes/beforechurch/Objects/archflag/archflag0.png
		newobjectdata.ObjectsURL = AnimatedIcon.getFirstFrameURL(newobjectdata.ObjectsURL); //the newobjectdata is used latter



		objectsCurrentState.ObjectsURL = newobjectdata.ObjectsURL;


		ObjectsLog.info("ObjectsURL set too:"+objectsCurrentState.ObjectsURL );

		objectsCurrentState.currentNumberOfFrames = newobjectdata.currentNumberOfFrames;

		//	currentObjectState.restrictPositionToScreen = newobjectdata.restrictPositionToScreen;
		//currentObjectState.hasAttachmentPointFile = newobjectdata.hasAttachmentPointFile;
		//currentObjectState.hasMovementFile = newobjectdata.hasMovementFile;
		//currentObjectState.CurrentBoxCSS = newobjectdata.CurrentBoxCSS;

		//clone specific stuff
		objectsCurrentState.clonedFrom = newobjectdata.clonedFrom;
		objectsCurrentState.clonedFromOnceLoaded = newobjectdata.clonedFromOnceLoaded;//not sure if this should be here

		objectsCurrentState.spawningObject = newobjectdata.spawningObject;
		objectsCurrentState.spawningObjectFromOnceLoaded = newobjectdata.spawningObjectFromOnceLoaded; //not sure if should be here

		//defaultObjectState = objectsCurrentState;

		//associate this default with the parent types variable too
		//super.getObjectsInitialState() = defaultObjectState;
		setObjectsInitialState(objectsCurrentState.copy());

		Log.info("adding propertys");

		if (props != null) {
			objectsCurrentState.objectsProperties = props;
		}

		if (ObjectRuntimeVariables!=null){
			objectsCurrentState.ObjectRuntimeVariables = ObjectRuntimeVariables;
		}
		Log.info("adding actions");

		if (actions != null) {
			objectsActions = actions;
			updateHandlersToMatchActions();

		}

		Log.info("updateState new object");

		setupWidget(objectsCurrentState.ObjectsURL,
				objectsCurrentState.currentNumberOfFrames, scene);

		Log.info("adding object "+this.initialObjectState.ObjectsName+" to scene "+scene.SceneFileLocation+" at artibitary location (has to be on the panel so it can be moved and set correctly)");
		scene.addWidget(this.getInternalGwtWidget(), -1000, -1000);



		Log.info("updating new object to specified state");


		alreadyLoaded = !runOnLoad;

		this.updateState(newobjectdata, true,true);
		objectsCurrentState.ObjectsName = newname; //has to be reset as the state we are changing too will alter the name back again to the source object!

		Log.info("adding to list"+objectsCurrentState.ObjectsName.toLowerCase());

		SceneObjectDatabase.all_sprite_objects.put(
				objectsCurrentState.ObjectsName.toLowerCase(), this);
	}
	 */
	/**
	 * auto-creates as SceneSpriteObjectState from the newobjectdata and then creates a SceneSpriteObject from it
	 * @param newobjectdata
	 * @param objectsCurrentStateString
	 * @param sceneItsOn
	 */
	//public SceneSpriteObject(SceneObjectState newobjectdata, String objectsCurrentStateString,
	//		SceneWidget sceneItsOn) {

	//	this(new SceneSpriteObjectState(newobjectdata),  objectsCurrentStateString,
	//			sceneItsOn);
	//}

	public SceneSpriteObject(SceneObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {

		this(new SceneSpriteObjectState(newobjectdata),  actions,
				sceneItsOn);
	}


	//temp constructor
	//this is here only till we change over to only using actionlists supplied in advance
	//rather then needing to split it off
	//public SceneSpriteObject(SceneSpriteObjectState newobjectdata, String objectsCurrentStateString,
	//		SceneWidget sceneItsOn) {

	//	this( newobjectdata, splitActionsFromParametersAndActions( objectsCurrentStateString),sceneItsOn, new GWTAnimatedIcon() , true);
	//}

	/**
	 * assumes we are making a sprite, not a subclasss of SceneSpriteObject
	 * @param newobjectdata
	 * @param actions
	 * @param sceneItsOn
	 */
	public SceneSpriteObject(SceneSpriteObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {

		this( newobjectdata, actions,sceneItsOn, new GWTAnimatedIcon() , true);
	}



	
	/**
	 * Makes a new SceneSpriteObject from the supplied SceneSpriteObjectState.
	 * 
	 * @param newobjectdata - state that defines this object
	 * @param actions - actionlist that defines how this object acts
	 * @param sceneItsOn - the scene this object will be created on
	 * @param innerWidget - the widget that defines this objects GWT implementation. Normally a GWTAnimatedIcon, but can be something else if your making a subclass of scenespriteobjects
	 * @param initialise - is this a real object to use directly, or merely the parent class of a subtype?
	 */
	protected SceneSpriteObject(
			SceneSpriteObjectState newobjectdata, 
			ActionList actions,
			SceneWidget sceneItsOn,
			Widget innerWidget, 
			boolean initialise) {

		//super(newobjectdata,objectsCurrentStateString,sceneItsOn);

		//by passing the data though to the constructor straight away
		//we allow it to handle the action and parameter settings itself.
		//It can separate of what it needs from the objectsCurrentStateString, and put it into the newly created SceneInputObjectState	
		super( newobjectdata,actions,sceneItsOn, innerWidget ,false ); 
		objectsCurrentState = (SceneSpriteObjectState) super.getObjectsCurrentState();

		Log.info("_________created initial  sprite data object "+this.getName()+"________________");

		SceneObjectIcon = (GWTAnimatedIcon) super.widgetContents; //assign contents as we know it will match what we just passed to tyhe super constructor

		//if (newobjectdata.getCurrentPrimaryType() == SceneObjectType.Sprite){
		//we only assign the objects specific types if we are creating that objects type
		//subtypes will call this with their super() method from their own assignObjectTypeSpecific function, so we dont
		//want to call this again here for them

		//assignObjectTypeSpecificParameters();//newobjectdata.ObjectsParamatersFromFile); 
		//}

		ObjectsLog.info("(creating sprite object)");

		// generic scene object setup
		//super();

		//objectsCurrentState = new SceneSpriteObjectState(newobjectdata);
		//transfer a copy of the data

		// transfer association so the superclass is using the samedata as this
		//super.objectsCurrentState = objectsCurrentState;

		//update the generic data common to all objects to match what's supplied
		//currentObjectState.setGenericObjectData(newobjectdata);


		//if (objectsCurrentStateString.length()<3){
		//	Log.info("no data specified, loading purely from state or defaults");
		//} else {
		//	Log.info("loading settings from string");
		// extract data
		//if (objectsCurrentState.getCurrentType() == SceneObjectType.InventoryIcon){
		//	setupSettingsFromString(newobjectdata, objectsCurrentStateString); // "InventoryItems"
		//} else {
		//	setupSettingsFromString(newobjectdata, objectsCurrentStateString); //, sceneItsOn.SceneFolderLocation
		//}
		//}
		// set defaults:

		//defaultObjectState = objectsCurrentState.copy();
		//associate this default with the parent types variable too
		//super.getObjectsInitialState() = defaultObjectState;
		
		if (initialise){
			
		//	setObjectsInitialState(objectsCurrentState.copy());	
			
			//shouldnt be needed
			//initialObjectState.setObjectsPrimaryType(objectsCurrentState.getPrimaryObjectType());

		
			
			//----
			Log.info("_________setting up sprite:");
			

			// create object from data
			setupWidget(objectsCurrentState.ObjectsURL,
					objectsCurrentState.currentNumberOfFrames, sceneItsOn);

			Log.info("_________initialising sprite and adding to database:"
					+ objectsCurrentState.ObjectsName.toLowerCase() + ":");
			

			initialiseAndAddToDatabase();

		}

		// add to object list
		//SceneObjectDatabase.all_sprite_objects.put(
		//		objectsCurrentState.ObjectsName.toLowerCase(), this);

		//	SceneObjectDatabase.addSpriteObjectToDatabase(this);

		//SceneObjectDatabase.addObjectToDatabase(this,SceneObjectType.Sprite);
		
		//boolean finalise = true; //theres no subtypes of input object yet
	//	if (finaliseLoad){
	//		finaliseAndAddToDatabase();
	//	}
	

		// clears animation state after commands
		// clearAnimationState = new Command() {
		// @Override
		// public void execute() {
		// //clear animation
		// currentObjectState.currentlyAnimationState ="";
		//
		// / }
		// };
		// SceneObjectIcon.setCommandToRunAfterOpen(clearAnimationState);
		// SceneObjectIcon.setCommandToRunAfterClose(clearAnimationState);

		//if we are not a inventory item, we try to load a cmap if theres any
		if (objectsCurrentState.getPrimaryObjectType() == SceneObjectType.InventoryObject){

		} else {
			//	startLoadingCmap();
		}

		// setup post animation actions		
		setupPostAnimationActions(objectsCurrentState.ObjectsFileName);

		Log.info("____sprite setup done");
		ObjectsLog.logTimer("setup sprite took;");
	}

	/*
	//old
	private void setupSettingsFromString(SceneObjectState newobjectdata,
			String objectsCurrentStateString) {


		//if (!tsourceFolder.equals(sourceFolder)){
		//	Log.severe(tsourceFolder+" does not match "+sourceFolder);
		//}

		//use the folder worked out internally (letting as in future simplify the parameters if this works 100% after extensive testing)
		//sourceFolder = tsourceFolder;



		Log.info("extracting data from:\r" + objectsCurrentStateString);
		//ignoreSceneActions = false;
		//currentObjectState.zindex = newobjectdata.zindex;
		objectsCurrentState.zindex = newobjectdata.zindex;
		// split actions off if present
		// get scene parameters (anything before a line with : in it)
		int firstLocOfColon = objectsCurrentStateString.indexOf(':');

		Log.info("LocOfColon recieved for object:" + firstLocOfColon);

		String Parameters = "";
		String allactions = "";
		if (firstLocOfColon == -1) {
			Parameters = objectsCurrentStateString;
		} else {
			int linebeforeColon = objectsCurrentStateString
					.lastIndexOf('\n', firstLocOfColon);
			Parameters = objectsCurrentStateString.substring(0, linebeforeColon);
			allactions = objectsCurrentStateString.substring(linebeforeColon);

		}
		// add the actions
		if (allactions.length() > 0) {
			Log.info("actions are:" + allactions);

			objectsActions = new ActionList(allactions);
			updateHandlersToMatchActions();

		}

		// now split again by new lines to get parameter data
		assignObjectTypeSpecificParameters(newobjectdata.ObjectsParamatersFromFile);

	}
	 */
	/*
@Deprecated
	public SceneSpriteObject(String imageZeroURL, SceneObject newobjectproto,
			SceneWidgetVisual sceneItsOn) {

		super(null,"",sceneItsOn);//broken now

		//defaultObjectState =objectsCurrentState.copy();
		//associate this default with the parent types variable too
		//super.getObjectsInitialState() = defaultObjectState;
		setObjectsInitialState(objectsCurrentState.copy());

		// for testing only
		// defaultObjectData.serialiseToString();
		// String serialised = defaultObjectData.serialiseToString();
		// defaultObjectData.deserialise(Serialized);

		// --
		setupWidget(imageZeroURL, objectsCurrentState.currentNumberOfFrames,
				sceneItsOn);

		// add to object list
		SceneObjectDatabase.all_sprite_objects.put(
				objectsCurrentState.ObjectsName.toLowerCase(), this);

		// setup post animation actions
		setupPostAnimationActions(objectsCurrentState.ObjectsURL);
	}*/

	/*
	private static AnimatedIcon getAnimatedSpriteFromData(final SceneSpriteObjectState newobjectdata, final SceneWidgetVisual sceneItsOne) {



		String imageZeroURL = newobjectdata.ObjectsURL;
		int numOfFrames = newobjectdata.currentNumberOfFrames;

		Log.info("___________________________________________________________________creating animated icon for superclass "
				+ imageZeroURL + "|");
		imageZeroURL = imageZeroURL.trim();

		final AnimatedIcon newicon;


		if (imageZeroURL.startsWith("<")) {

			String internalname = imageZeroURL.substring(1,
					imageZeroURL.length() - 1);
			Log.info("__creating sprite from internal name:" + internalname);


			//get correct animation from internal name
			AbstractImagePrototype[] internalAnimation = InternalAnimations.getAnimation(internalname);

			if (internalAnimation==null){

				Log.severe("No Internal Animation of that name found....did you compile the code with the right internal spritesets?");
				Log.severe("Knowen Sprites;"+InternalAnimations.getAnimationNames().toString());
			}


			//set frame total to match
			newobjectdata.currentNumberOfFrames = internalAnimation.length;

			//set the animation
			newicon = new AnimatedIcon(internalAnimation
					,"<"+internalname+">"); //internal name always has "<>" added to it to separate it from real file names





		} else {
			Log.info("__creating sprite from :" + imageZeroURL+" frames="+numOfFrames);

			newicon = new AnimatedIcon(imageZeroURL, numOfFrames);


			//only add to the loading if its not a inventory item
			//in future if needbe, we can have a similiar load monitering in the inventory
			if (newobjectdata.getCurrentPrimaryType() == SceneObjectType.InventoryIcon){

			} else {
				Log.info("__added to loading::" + imageZeroURL);
				sceneItsOne.addToLoading();
			}
		}


		// add load end handler		
		HandlerRegistration ONLOADHANDLE = null;		
		ONLOADHANDLE =  newicon.addLoadHandler(new myLoadHandler(ONLOADHANDLE,newobjectdata,sceneItsOne));

		final HandlerRegistration ONERRORHANDLE = newicon.addErrorHandler(new ErrorHandler() {				

			@Override
			public void onError(ErrorEvent event) {

				Log.info("__subtracting "+newobjectdata.ObjectsName+" loading because file errored:"+newicon.getUrl());

				imageloaded(newobjectdata,sceneItsOne);

			}
		});

		return newicon;
	}


	static class myLoadHandler implements LoadHandler {
		HandlerRegistration myRegistration = null;
		private SceneSpriteObjectState newobjectdata;
		private SceneWidgetVisual sceneItsOne;		

		public myLoadHandler(HandlerRegistration myRegistration, SceneSpriteObjectState newobjectdata, SceneWidgetVisual sceneItsOne) {
			super();
			this.myRegistration= myRegistration;		
			this.newobjectdata = newobjectdata;
			this.sceneItsOne   = sceneItsOne;
		}

		@Override
		public void onLoad(LoadEvent event) {

			Log.info("__subtracting from loading::"+newobjectdata.ObjectsURL);

			//remove handler so this wont fire on subsequent loads
			if (myRegistration!=null){
				myRegistration.removeHandler();
			}

			imageloaded(newobjectdata,sceneItsOne);
		}


	}*/

	
	protected void setupWidget(String imageZeroURL, int numOfFrames,
			final SceneWidget sceneItsOne) {

		ObjectsLog.log("Setting up Sprite..","orange");
		ObjectsLog.log("Url:"+imageZeroURL+"("+numOfFrames+" frames)");
		
		Log.info("___________________________________________________________________creating animated icon "+ imageZeroURL + "|");

		imageZeroURL = imageZeroURL.trim();
		
		Log.info("__SETTING LOAD HANDLERS :" + imageZeroURL+" frames="+numOfFrames);

		// add load end handler .imageContents.add
		ONLOADHANDLE  = SceneObjectIcon.addLoadHandler(new SpriteFirstImageLoadedHandler());			
		ONERRORHANDLE = SceneObjectIcon.addErrorHandler(new SpriteImageErrorHandler());
		

		if (imageZeroURL.startsWith("<")) {

			String internalname = imageZeroURL.substring(1,	imageZeroURL.length() - 1);
			Log.info("__creating sprite from internal name:" + internalname);

			//get correct animation from internal name
			AbstractImagePrototype[] internalAnimation = InternalAnimations.getAnimation(internalname);

			if (internalAnimation==null){

				Log.severe("No Internal Animation of that name found....did you compile the code with the right internal spritesets?");

				Log.severe("Knowen Sprites;"+InternalAnimations.getAnimationNames().toString());
			}


			//set frame total to match
			objectsCurrentState.currentNumberOfFrames = internalAnimation.length;

			//set the animation
			//SceneObjectIcon = new AnimatedIcon(internalAnimation
			//		,"<"+internalname+">"); //internal name always has "<>" added to it to separate it from real file names

			SceneObjectIcon.intialise(internalAnimation	,"<"+internalname+">");




		} else {

			Log.info("__creating sprite from :" + imageZeroURL+" frames="+numOfFrames);

			//SceneObjectIcon = new AnimatedIcon(imageZeroURL, numOfFrames);
			SceneObjectIcon.intialise(imageZeroURL, numOfFrames);

			if (objectsCurrentState.hasAttachmentPointFile){
				
				//get first/default attachment point for frame
				String atachmentPointUrl = imageZeroURL.subSequence(0, imageZeroURL.lastIndexOf(".")-1)+".glu";
				getAttachmentPointMovements(atachmentPointUrl,true);
			}

			//only add to the loading if its not a inventory item
			//in future if needbe, we can have a similar load monitoring in the inventory
			if (objectsCurrentState.getPrimaryObjectType() == SceneObjectType.InventoryObject){

			} else {
				Log.info("__added to loading::" + imageZeroURL);
				sceneItsOne.addToLoading(this.getName()); //imageZeroURL
			}

		}

	//	Log.info("__SETTING LOAD HANDLERS :" + imageZeroURL+" frames="+numOfFrames);

		// add load end handler
	//	ONLOADHANDLE  = SceneObjectIcon.imageContents.addLoadHandler(new SpriteImageLoadedHandler());			
	//	ONERRORHANDLE = SceneObjectIcon.addErrorHandler(new SpriteImageErrorHandler());

		
		//all the common things to setup are done in 
		Log.info("______done setup for sprite:");
		super.setupWidget(objectsCurrentState);		
		//NOTE: Super can not be done before the sprite is set above
		//This is because its the supertype that actually adds it to the page and it needs to exist to do that!

		this.setTitle(objectsCurrentState.Title);





		/**
		animationRunner = new Timer() {

			boolean telePortFlag;

			boolean withinY = false;
			boolean withinX = false;


			//keep track of pixel rounding errors
			double remainderX = 0;
			double remainderY = 0;

			// double curveTime = 0;
			// double curveTimeStep = 0.01;

			@Override
			public void run() {

				isMoving = true;

				// if at or within just under one step of next waypoint, then we
				// test the next waypoint
				//	 Log.info("_____testing if we are at waypoint: desX = "
				// + currentObjectState.movement_DX + " cx =  " + currentObjectState.movement_currentX);

				// Log.info("_____testing if we are at waypoint: desY = "
				// + currentObjectState.movement_DY + " cy =  " + currentObjectState.movement_currentY);

				if (!currentObjectState.moveState.movement_onCurve) {


					if (Math.abs(currentObjectState.moveState.movement_currentX
							- currentObjectState.moveState.movement_DX) < (Math
									.abs(currentObjectState.moveState.movement_StepX) + 1)) {

						withinX = true;
						currentObjectState.moveState.movement_currentX = currentObjectState.moveState.movement_DX;
						currentObjectState.moveState.movement_StepX = 0;

					} else {
						withinX = false;
					};



					if (Math.abs(currentObjectState.moveState.movement_currentY
							- currentObjectState.moveState.movement_DY) < (Math
									.abs(currentObjectState.moveState.movement_StepY) + 1)) {

						withinY = true;
						currentObjectState.moveState.movement_currentY = currentObjectState.moveState.movement_DY;
						currentObjectState.moveState.movement_StepY = 0;

					} else {
						withinY = false;
					}
					;
				} else {

					withinX = false;
					withinY = false;

					if (currentObjectState.moveState.movement_curveTime + currentObjectState.moveState.movement_curveTimeStep > 1) {
						withinX = true;
						withinY = true;

					}

				}

				if ((withinX) && (withinY)) {

					ObjectsLog.info("at waypoint"+ currentObjectState.moveState.movement_currentWaypoint);

					currentObjectState.moveState.movement_currentWaypoint = currentObjectState.moveState.movement_currentWaypoint + 1;


					// snap to current waypoint reached for consistency in
					// movement
					currentObjectState.moveState.movement_currentX = currentObjectState.moveState.movement_DX;
					currentObjectState.moveState.movement_currentY = currentObjectState.moveState.movement_DY;

					// remove flags
					currentObjectState.moveState.movement_onCurve = false;
					currentObjectState.moveState.movement_SX = -1;
					currentObjectState.moveState.movement_SY = -1;


					// Log.info("currentPath size test:"+currentPath.getAsSVGPath());


					if (currentObjectState.moveState.movement_currentWaypoint < currentPath
							.size()) {

						ObjectsLog.info("updating waypoint"+currentObjectState.moveState.movement_currentWaypoint);

						MovementWaypoint cp = currentPath
								.get(currentObjectState.moveState.movement_currentWaypoint);

						MovementType currenttype = cp.type;




						if (skipFirstJump == true) {

							currenttype = MovementType.LineTo;

							skipFirstJump = false;

						}

						//if the new waypoint is at the current location, then we force a teleport to 
						//save calculation power
						if (cp.x==currentObjectState.moveState.movement_currentX){							
							if (cp.y==currentObjectState.moveState.movement_currentY){
								Log.info("already at destination");
								currenttype=MovementType.Move;
							}

						}

						if (currenttype == MovementType.LineTo) {


							currentObjectState.moveState.movement_DX = cp.x;
							currentObjectState.moveState.movement_DY = cp.y;

							//if relative add the last location on
							if (cp.isRelative()){
								Log.info("relative line to");
								currentObjectState.moveState.movement_DX = cp.x+	currentObjectState.moveState.movement_currentX;
								currentObjectState.moveState.movement_DY = cp.y+	currentObjectState.moveState.movement_currentY;
							}


							ObjectsLog.info("next waypoint is a line going to "
									+ currentObjectState.moveState.movement_DX + ","
									+ currentObjectState.moveState.movement_DY);

							ObjectsLog.info("from "
									+ currentObjectState.moveState.movement_currentX + ","
									+ currentObjectState.moveState.movement_currentY);

							// update direction
							updateObjectsDirection(
									currentObjectState.moveState.movement_DX,
									currentObjectState.moveState.movement_DY);

							// we ensure a minimum speed by rounding up
							ObjectsLog.info("movement_currentX "
									+ currentObjectState.moveState.movement_currentX + " ");

							ObjectsLog.info("movement_currentY "
									+ currentObjectState.moveState.movement_currentY + " ");

							int Xdistance = ((currentObjectState.moveState.movement_DX - currentObjectState.moveState.movement_currentX));
							int Ydistance = ((currentObjectState.moveState.movement_DY - currentObjectState.moveState.movement_currentY));

							//note; not a error, this is meant to be the total x/y magnitudes, not the hypo
							int TotalLength = Math.abs((Ydistance))+ Math.abs((Xdistance));

							// work out the vector x/y between 0 and 1, and
							// then multiply by the stepsize



							ObjectsLog.info("TotalLength "
									+ TotalLength + " ");

							// work out ratios
							Double Xratio = ((double)Xdistance / (double) TotalLength);
							Double Yratio = ((double)Ydistance / (double) TotalLength);


							//		Log.info("Xratio "
							//				+ Xratio + " ");

							//		Log.info("Yratio "
							//				+ Yratio + " ");

							double StepX =(Xratio
		 * currentObjectState.moveState.movement_speed); 


							currentObjectState.moveState.movement_StepX  = StepX;


							// 10
							// can
							// be
							// replaced
							// by the speed in
							// pixels a cycle
							double StepY = (Yratio
		 * currentObjectState.moveState.movement_speed);

							currentObjectState.moveState.movement_StepY = StepY;


							ObjectsLog.info(" step X="+currentObjectState.moveState.movement_StepX+" step Y="+currentObjectState.moveState.movement_StepY);

							// ensure minimum movement
							if (currentObjectState.moveState.movement_StepY == 0
									&& currentObjectState.moveState.movement_DY < 0) {
								currentObjectState.moveState.movement_StepY = -1;
							}
							if (currentObjectState.moveState.movement_StepY == 0
									&& currentObjectState.moveState.movement_DY > 0) {
								currentObjectState.moveState.movement_StepY = 1;
							}
							if (currentObjectState.moveState.movement_StepX == 0
									&& currentObjectState.moveState.movement_DX < 0) {
								currentObjectState.moveState.movement_StepX = -1;
							}
							if (currentObjectState.moveState.movement_StepX == 0
									&& currentObjectState.moveState.movement_DX > 0) {
								currentObjectState.moveState.movement_StepX = 1;
							}

							// Log.info("so we are moving by..."+movement_StepX+","+movement_StepY);

							// movement_currentX = movement_currentX +
							// movement_StepX;
							// movement_currentY = movement_currentY +
							// movement_StepY;

							telePortFlag = false;

						} else if (currenttype == MovementType.Move) {

							currentObjectState.moveState.movement_DX = cp.x;
							currentObjectState.moveState.movement_DY = cp.y;

							if (cp.isRelative()){

								ObjectsLog.info("relative move");
								currentObjectState.moveState.movement_DX = cp.x+	thisObject.getX();
								currentObjectState.moveState.movement_DY = cp.y+	thisObject.getY(); //currentObjectState.moveState.movement_currentY;

								//set starting loc to x/y of pin
							}

							ObjectsLog.info("next waypoint is to move to"+
									currentObjectState.moveState.movement_DX+" "+currentObjectState.moveState.movement_DY);

							// teleport there

							currentObjectState.moveState.movement_currentX = currentObjectState.moveState.movement_DX;
							currentObjectState.moveState.movement_currentY = currentObjectState.moveState.movement_DY;

							telePortFlag = true;

							// movement_StepX = (cp.x - movement_currentX);
							// movement_StepY = (cp.y - movement_currentY);

							// Log.info("so we are moving by..."+movement_StepX+","+movement_StepY);

						} else if (currenttype == MovementType.CurveToo) {


							// sets up the params needed for curved movement
							// updates

							currentObjectState.moveState.movement_DX = cp.x;
							currentObjectState.moveState.movement_DY = cp.y;

							currentObjectState.moveState.movement_CX = cp.midpoint_x;
							currentObjectState.moveState.movement_CY = cp.midpoint_y;

							currentObjectState.moveState.movement_SX = currentObjectState.moveState.movement_currentX;
							currentObjectState.moveState.movement_SY = currentObjectState.moveState.movement_currentY;

							//if relative add the last location on
							if (cp.isRelative()){

								currentObjectState.moveState.movement_DX = cp.x+	currentObjectState.moveState.movement_currentX;
								currentObjectState.moveState.movement_DY = cp.y+	currentObjectState.moveState.movement_currentY;

								currentObjectState.moveState.movement_CX = cp.midpoint_x+	currentObjectState.moveState.movement_currentX;
								currentObjectState.moveState.movement_CY = cp.midpoint_y+	currentObjectState.moveState.movement_currentY;

								Log.info("relative curve to "+currentObjectState.moveState.movement_DX +","+currentObjectState.moveState.movement_DY);
								Log.info("via "+currentObjectState.moveState.movement_CX +","+currentObjectState.moveState.movement_CY);

							}

							currentObjectState.moveState.movement_onCurve = true;

							ObjectsLog.info("curve data x: "
									+ currentObjectState.moveState.movement_SX + " "
									+ currentObjectState.moveState.movement_CX + " "
									+ currentObjectState.moveState.movement_DX);

							ObjectsLog.info("curve data y: "
									+ currentObjectState.moveState.movement_SY + " "
									+ currentObjectState.moveState.movement_CY + " "
									+ currentObjectState.moveState.movement_DY);

							// temp, need to be corrected:
							currentObjectState.moveState.movement_curveTime = 0;
							currentObjectState.moveState.movement_curveTimeStep = 0.01 * currentObjectState.moveState.movement_speed;

							ObjectsLog.info(" time step=" + currentObjectState.moveState.movement_curveTimeStep
									+ " ");

							if (currentObjectState.moveState.movement_curveTimeStep < 0.01) {
								ObjectsLog.info(currentObjectState.moveState.movement_curveTimeStep
										+ " is under minimum speed");
								currentObjectState.moveState.movement_curveTimeStep = 0.01;
							}

							currentObjectState.moveState.movement_StepY = 10;
							currentObjectState.moveState.movement_StepX = 10;

						} else if (currenttype == MovementType.LoopToStart) {

							// if there's a "z" then loop
							// next destination is the first again
							// Log.info("looping to start");

							currentObjectState.moveState.movement_currentWaypoint = -1;
							telePortFlag = true;

						} else if (currenttype == MovementType.Command) {

							Log.info("running command :" + cp.Command);

							InstructionProcessor
							.processInstructions(
									cp.Command,
									"movec_"
											+ thisObject.currentObjectState.ObjectsName,
											thisObject);

						} else {
							// etc for other types
							// Log.info("unknown type "+cp.type.toString());

							telePortFlag = false;
						}

					} else {
						// if at end, stop
						Log.info("end of movement path");
						thisObject.setPosition(
								currentObjectState.moveState.movement_currentX,
								currentObjectState.moveState.movement_currentY);

						this.cancel();
						isMoving = false;

						//path specific commands
						if (currentPath.postAnimationCommands!=null){
							currentPath.postAnimationCommands.execute();
						}


						//generic movement end commands
						triggerMovementEndCommands();


						return;
					}
				}
				;

				if (!telePortFlag && !currentObjectState.moveState.movement_onCurve) {
					// if its not a teleport waypoint continue movement as we
					// were before
					if (currentObjectState.moveState.movement_currentX != currentObjectState.moveState.movement_DX) {

						double newX =remainderX+ (currentObjectState.moveState.movement_currentX
								+ currentObjectState.moveState.movement_StepX);
						currentObjectState.moveState.movement_currentX = (int) newX;

						remainderX = newX-currentObjectState.moveState.movement_currentX;

					}
					if (currentObjectState.moveState.movement_currentY != currentObjectState.moveState.movement_DY) {

						double newY =remainderY+  (currentObjectState.moveState.movement_currentY
								+ currentObjectState.moveState.movement_StepY);

						currentObjectState.moveState.movement_currentY = (int)newY;
						remainderY = newY-currentObjectState.moveState.movement_currentY;
					}

					// movememt step trigger
					advanceOnStepActions(currentObjectState.moveState.movement_StepX,
							currentObjectState.moveState.movement_StepY);

				}

				if (currentObjectState.moveState.movement_onCurve) {

					int newX = (int) (Math.pow(1 - currentObjectState.moveState.movement_curveTime, 2)
		 * currentObjectState.moveState.movement_SX + 2
		 * (1 - currentObjectState.moveState.movement_curveTime) * currentObjectState.moveState.movement_curveTime
		 * currentObjectState.moveState.movement_CX + Math.pow(
									currentObjectState.moveState.movement_curveTime, 2)
		 * currentObjectState.moveState.movement_DX);

					int newY = (int) (Math.pow(1 - currentObjectState.moveState.movement_curveTime, 2)
		 * currentObjectState.moveState.movement_SY + 2
		 * (1 - currentObjectState.moveState.movement_curveTime) * currentObjectState.moveState.movement_curveTime
		 * currentObjectState.moveState.movement_CY + Math.pow(
									currentObjectState.moveState.movement_curveTime, 2)
		 * currentObjectState.moveState.movement_DY);

					currentObjectState.moveState.movement_curveTime = currentObjectState.moveState.movement_curveTime
							+ currentObjectState.moveState.movement_curveTimeStep;

					// Movement step trigger
					// not tested!
					advanceOnStepActions(
							Math.abs(currentObjectState.moveState.movement_currentX
									- newX),
									Math.abs(currentObjectState.moveState.movement_currentY
											- newY));

					currentObjectState.moveState.movement_currentX = newX;
					currentObjectState.moveState.movement_currentY = newY;

				}

				// thisObject.objectScene.setWidgetsPosition(
				// thisObject,
				// currentObjectState.moveState.movement_currentX,
				// currentObjectState.moveState.movement_currentY);

				// Log.info("setting position of"+thisObject.ObjectsName);

				thisObject.setPosition(currentObjectState.moveState.movement_currentX,
						currentObjectState.moveState.movement_currentY);
				//
				// Log.info("current object position:"+movement_currentX+" "+movement_currentY);

			}
		};

		 **/

		// create animation timer
		//	createAnimationTimer();

		Log.info("starting to setup scenespriteobject widget (then animations and cmap)...");


		//		super.setWidget(SceneObjectIcon);//redundant (see super call about to widgetsetup, as that contains setWidget())

	//	startLoadingMovementAnimations();

		// assign generic handlers
		//probably should be refractoed into SceneObject (that is, this ones superclass)

		//sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
		//		| Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
		//		| Event.ONTOUCHSTART | Event.ONTOUCHEND | Event.ONTOUCHCANCEL|Event.ONTOUCHMOVE );

	}




	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.IsSceneSpriteObject#setURL(java.lang.String, int)
	 */
	//@Override
	/**
	 * When this method is called the visual representation of the sprite should be set to  
	 * objectsCurrentState.ObjectsURL
	 * with
	 * objectsCurrentState.currentNumberOfFrames
	 * frames
	 */
	public void setURLPhysically(boolean newAnimation) {
		
		if (newAnimation){
		// update name
		//SceneObjectIcon.firstFrame();
		SceneObjectIcon.setCurrentframeVariable(0);
		SceneObjectIcon.setURL(objectsCurrentState.ObjectsURL,
				objectsCurrentState.currentNumberOfFrames);

		} else {
			SceneObjectIcon.setURLExactly(objectsCurrentState.ObjectsURL);

		}

		//update the animation end actions ,if theres any
		Log.info("end setting objectsCurrentState.ObjectsFileName = "+objectsCurrentState.ObjectsFileName);
		updateAnimationEndActionsHELPER(this,objectsCurrentState.ObjectsFileName,SceneObjectIcon.animation);
		
		// check for commands to run after
		//	setupPostAnimationActions(URL); //maybe remove this
		if (oipu != null) {
			oipu.update();
		}
	}
	
	
	/**
	 * Adds the OnAnimationEnd instructions to the end of the animation of a SceneObjectIcon (which is like a generic graphics icon).
	 * 
	 * An example is the opening of a inventory and the poppingup of the actual inventory at the end of the animation.
	 * @param URL
	 */

	public void setupPostAnimationActions(String URL) {

		//At the end of the animation, it checks if there are any actions at all to trigger.

		if (objectsActions!=null){			
			//update the animation end actions ,if theres any
			updateAnimationEndActionsHELPER(this,URL,SceneObjectIcon.animation);
			
		}


		SceneObjectIcon.setCommandToRunAfterFrameChange( new Runnable() {
			@Override
			public void run() {
			
				//runs all the necessary core frame change functions
				SpriteObjectHelpers.runOnFrameChangeSpriteHelper(SceneSpriteObject.this);

				
				//we also update the gwt sppecific debug box's if there's any
				if (oipu != null) {
					//Log.info("updating after frame change");
					objectsCurrentState.currentlyAnimationState = SceneObjectIcon.serialiseAnimationState();//this only needs to be done when saving state

					//Log.info("currentObjectState.ObjectsURL ="+currentObjectState.ObjectsURL);
					oipu.update();
				}

			}
		});
	}

	
	



	/*@Override
	public void userActionTriggeredOnObject(String actionname) {

		Log.info("action triggered:" + actionname);

		// check for global and scene actions;
		testAndRunGlobalActions(TriggerType.ActionUsed, actionname);

		// check for object specific actions
		if (objectsActions!=null){

		ArrayList<String> actions = objectsActions.getActionsForTrigger(
				TriggerType.ActionUsed, actionname);
		Log.info("actions found: \n" + actions.toString());
		if (actions.size() > 0) {

			// in future this function should support an arraylist
			InstructionProcessor.processInstructions(actions, "FROM_"
					+ objectScene.SceneFileName + "_"
					+ this.currentObjectState.ObjectsFileName, this);

			// unhold any items being held
			MyApplication.unholdItem();

			// run its default actions
		} 
		}

		// if actions are found run them:
		else if (InventoryPanel.itemCurrentlyBeingHeld) {

			Log.info("itemCurrentlyBeingHeld - looking for default action "
					+ actionname + " \n");

			if (InventoryPanel.currentlyHeldItem.itemsActions!=null){
			// run its default actions
			ArrayList<String> defaultActions = InventoryPanel.currentlyHeldItem.itemsActions
					.getActionsForTrigger(TriggerType.DefaultActionFor,
							actionname);

			InstructionProcessor.processInstructions(defaultActions, "FROM_"
					+ objectScene.SceneFileName + "_"
					+ InventoryPanel.currentlyHeldItem.Name, this);

			MyApplication.unholdItem();
			}
		}

	}*/
	/*
	private void testAndRunGlobalActions(TriggerType trigger, String param) {

		// test for global actions
		SceneWidget.testForGlobalActions(trigger, param, this);

		// set correct scene
		InstructionProcessor.currentScene = objectScene;

		// test for scene actions
		objectScene.testForSceneActions(trigger, param, this);

		return;

	}
	 */
	/*
	 * public void updateHandlersToMatchActions() {
	 * 
	 * // loop over actions Iterator<ActionSet> actionsIt =
	 * objectsActions.iterator(); Log.info("updateHandlersToMatchActions");
	 * 
	 * while (actionsIt.hasNext()) {
	 * 
	 * final ActionSet actionSet = actionsIt.next();
	 * 
	 * // loop for each trigger in action set ArrayList<Trigger> triggers =
	 * actionSet.getTriggers(); Iterator<Trigger> triggerIT =
	 * triggers.iterator(); Log.info("updateing handlers for actionset");
	 * 
	 * while (triggerIT.hasNext()) {
	 * 
	 * ActionSet.Trigger trigger = triggerIT.next();
	 * 
	 * Log.info("updateing trigger:" + trigger.triggertype.toString());
	 * 
	 * if (trigger.triggertype == TriggerType.MouseOver) {
	 * actionsToRunForMouseOver = actionSet; Log.info("mouse over set1" +
	 * actionSet.Actions.toString()); }
	 * 
	 * if (trigger.triggertype == TriggerType.MouseOut) {
	 * actionsToRunForMouseOut = actionSet; }
	 * 
	 * if (trigger.triggertype == TriggerType.Click) { actionsToRunForMouseClick
	 * = actionSet; Log.info("action set added with contents " +
	 * actionSet.Actions.toString()); } if (trigger.triggertype ==
	 * TriggerType.RightClick) { actionsToRunForMouseRightClick = actionSet; }
	 * 
	 * if (trigger.triggertype == TriggerType.OnTouchingChange) {
	 * actionsToRunForWhenATouchingObjectChanges = actionSet; } if
	 * (trigger.triggertype == TriggerType.OnFirstLoad) {
	 * 
	 * Log.info("__________actionsToRunOnFirstLoad");
	 * Log.info("__________actionsToRunOnFirstLoad = :" +
	 * actionSet.Actions.toString());
	 * 
	 * actionsToRunOnFirstLoad = actionSet; } }
	 * 
	 * }
	 * 
	 * }
	 */



	// public void itemDragedOnto() {

	// get items actions

	// create menu
	// we parse this object and the scene its in as a parameter.

	// display menu for actions

	// if action triggered, first the scene is tested for a trigger, then
	// this object

	// }

	// on attach run onLoad animation
	//Remember, this will trigger when setting a object onto a different scene (as its detached and reattached in order to do that)
	/*
	@Override
	public void onLoad() {
		super.onLoad();
		//Log.info("__________" + this.currentObjectState.ObjectsFileName + " onLoad actions:");
		//ok...deep breath....we trigger the OnFirstLoad actions when there is actions to run, and this hasnt already been loaded , and we are not loading the scene this object is in silently!
/*
		if (actionsToRunOnFirstLoad != null && alreadyLoaded == false && !this.getScene().loadingsilently) {
			InstructionProcessor.lastSpriteObjectClickedOn = this;
			InstructionProcessor.processInstructions(
					actionsToRunOnFirstLoad.CommandsInSet.getActions(), "fl_"
							+ this.objectsCurrentState.ObjectsFileName, this);

			//Log.info("__________onLoad actions ran for "+ this.currentObjectState.ObjectsFileName);
		} else {
			//Log.info("_________onload not ran: alreadyLoaded=" +alreadyLoaded);
		}


		alreadyLoaded = true;
	}*/


	//NOTE: Should we override setvisible ? Restrict to screen will now apply to all objects if we dont
	//Restrict to screen currently seems to always position by pin regardless of if it should or not?
	//(We dont position by pin when loading states, for example)
	//If setVisible runs from a state its likely to thus mess things up. (at least if the setvisible happens after the position).
	//

	/**
	 * updates the object to match the specified object state data, any nulls in
	 * the new data will result in the old data for those variables being kept
	 **/
	public void updateState(final SceneSpriteObjectState newState,
			final boolean runOnload,final boolean repositionObjectsRelativeToThis) {


		Log.info("updating state to state with frame:" + newState.currentFrame);
		ObjectsLog("Updating spriteobject state ","orange");
		

		// update current frame so it knows to rewind/ff to target frame
		// correctly
		// Assuming we arnt merely loading the current state in for the first time, in which case newstate = state
		if (objectsCurrentState!=newState) {
			objectsCurrentState.currentFrame = SceneObjectIcon.getCurrentframe();
		} else {

			//objectsCurrentState and newState are the same object
			//This is likely because we are loading for the first time, and not changing from one state to another
			//because of that we dont have to worry about setting objectsCurrentState, and can instead just ensure the item 
			//is set up to match
			ObjectsLog.info("setting up for first time");			
			initCurrentState(runOnload);
			return;
		}


		//ObjectsLog.info("__|" + newState.serialiseToString() + "|__");

		//Log.info("newState.zindex-" + newState.zindex);

		// set states

		SceneObjectIcon.pauseAnimation();

		ObjectsLog.info("clearing run after open/close"); //we have to clear the open/close animations when updating the state so we can manually trigger animations if needed
		SceneObjectIcon.clearRunthisAfterClose();
		SceneObjectIcon.clearRunthisAfterOpen();

		// SceneObjectIcon.setAnimateClose();
		boolean urlchanged = false;

		ObjectsLog.info("newState.ObjectsURL-" + newState.ObjectsURL);
		ObjectsLog.info("newState.currentFrame-" + newState.currentFrame);

		Log.info("setting sprite objects	 data to newState");
		//	Log.info("__|" + newState.serialiseToString() + "|__");

		//general update stuff (this updates all the universal data to update)
		super.updateState(newState,false,false);


		//general update stuff
		//updateState(newState);



		//set scene	(now in updateStateInternal)
		/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		SceneWidget sw=	SceneWidget.getSceneByName(newState.ObjectsSceneName);	
		if (sw==null){

			ObjectsLog.info("scene name not recognised:" + newState.ObjectsSceneName);
		}

		setObjectsScene(sw);
		 */


		// load ObjectsURL
		if (newState.ObjectsURL != null) {


			if (!objectsCurrentState.ObjectsURL.equals(newState.ObjectsURL) || !objectsCurrentState.ObjectsFileName.equals(newState.ObjectsFileName) ) {
				urlchanged = true;

				if (newState.ObjectsFileName.startsWith("<")) {

					objectsCurrentState.ObjectsURL = newState.ObjectsFileName;

					// if name is blank
					if (newState.ObjectsName.length() < 3) {
						objectsCurrentState.ObjectsName = newState.ObjectsFileName
								.trim();
					}
					ObjectsLog.info("______________________currentObjectState.ObjectsName:"
							+ objectsCurrentState.ObjectsName + ":");

				} else {

					objectsCurrentState.ObjectsURL = newState.ObjectsURL;
				}

				ObjectsLog.info("ObjectsURL now set from:" + newState.ObjectsURL);
				ObjectsLog.info("ObjectsURL now set to:"   + objectsCurrentState.ObjectsURL);
				ObjectsLog.info("anima is:" + SceneObjectIcon.basefilename);

			} else {
				ObjectsLog.info("url not changed");

				ObjectsLog.info("internally still:"+SceneObjectIcon.basefilename);


			}

		}


		if (newState.currentlyAnimationState.length() > 1) {

			objectsCurrentState.currentlyAnimationState = newState.currentlyAnimationState;
			ObjectsLog.info("new animation state | "+objectsCurrentState.currentlyAnimationState);
		} else {
			ObjectsLog.info(" animation state still | "+objectsCurrentState.currentlyAnimationState);
		}
		
		//update the framerate
		objectsCurrentState.currentFrameGap = newState.currentFrameGap;		
		SceneObjectIcon.setFrameGap(newState.currentFrameGap);
		

		// if the url is already the same, then animate to the correct frame
		// either forward or backward

		if (!urlchanged) {
			
			if (newState.currentFrame!=objectsCurrentState.currentFrame){
				ObjectsLog.info("Animating to frame:"+newState.currentFrame);
				animateToFrame(newState.currentFrame,true); //true means dont run animatioend actions this one time
			} else {
				ObjectsLog.info("Already at correct frame:"+newState.currentFrame);				
			}
			
		} else {
			ObjectsLog.info("url changed");
		}


		//moved from below (before staticimage was supported the time delay meant this didnt cause an error
		//now it needs to be up here else the wrong filename will be set)
		//what about num of frames though?
		if (newState.ObjectsFileName != null) {
			objectsCurrentState.ObjectsFileName = newState.ObjectsFileName;
		}


		// if the url is not the same, we animate backwards before changing it,
		// under the assumption it
		// Didn't spontaneously change

		// if its not already on the first frame, run this after closing

		if (urlchanged) {

			//added this
			objectsCurrentState.currentNumberOfFrames = newState.currentNumberOfFrames;

			Runnable	ChangeURLAfterAnimating = new Runnable() {

				@Override
				public void run() {

					ObjectsLog.log("ObjectsURL set after snimation 2:"
							+ objectsCurrentState.ObjectsURL+" ("+objectsCurrentState.currentNumberOfFrames+")","#00FF00");

					// SceneObjectIcon.setURL(defaultObjectData.ObjectsURL,
					// defaultObjectData.currentNumberOfFrames);

					thisObject.setSpritesURL(objectsCurrentState,objectsCurrentState.ObjectsFileName,
							objectsCurrentState.currentNumberOfFrames);

					ObjectsLog.info("clearing run this after close");

					SceneObjectIcon.clearRunthisAfterClose();

					//re add the normal run after close functions

					ObjectsLog("Setting up post animation actions after state change","green");
					setupPostAnimationActions(objectsCurrentState.ObjectsFileName);

					// SceneObjectIcon.currentframe =
					// currentObjectState.currentFrame;

					// SceneObjectIcon.gotoFrame(currentObjectState.currentFrame);

					if (newState.currentlyAnimationState.length() > 1) {

						ObjectsLog.info("___________loading Serialised Animation State");
						animateToFrame(newState.currentFrame,false);
						SceneObjectIcon.loadSerialisedAnimationState(newState.currentlyAnimationState);
						// setAnimationState(currentObjectState.currentlyAnimationState);
						SceneObjectIcon.resumeAnimation();

						ObjectsLog.info("loaded  Serialised Animation State");

					} else {
						//animate to current frame (should only use this if we arnt animating atm, else we jump to frame)

						ObjectsLog.info("Animating to frame:"+newState.currentFrame+" fromframe"+SceneObjectIcon.getCurrentframe());

						animateToFrame(newState.currentFrame,true); //true disables the endcommands from firing


					}

					if (runOnload) {

						ObjectsLog.info("running objects onload again.");
						alreadyLoaded = false; //has to be reset to false to run onload again
						onLoad();
					} else {

						ObjectsLog.info("run onload not specified");
					}

				}

			};

			if (SceneObjectIcon.isOnStaticImageMode()){
				ObjectsLog.info("ObjectsURL set straight away as its currently just a static image:" + objectsCurrentState.ObjectsURL);
				//we run the changes straight away if its just a still image as playback would have no effect
				ChangeURLAfterAnimating.run();
			} else {
				ObjectsLog.info("ObjectsURL set after animation close:" + objectsCurrentState.ObjectsURL);
				//if the SceneObjectsIcon is a real animation we animate back before the commands change its url (merely fora visual effect)
				SceneObjectIcon.setCommandToRunAfterClose(ChangeURLAfterAnimating);
				SceneObjectIcon.setPlayBack();
			}


		} else {
			// thisObject.setURL(newState.ObjectsFileName,
			// newState.currentNumberOfFrames);

			// SceneObjectIcon.currentframe = currentObjectState.currentFrame;
			// SceneObjectIcon.gotoFrame(currentObjectState.currentFrame);
			ObjectsLog.info("testing for animation in state");

			if (newState.currentlyAnimationState.length() > 1) {
				
				ObjectsLog.info("restoring animation from state");

				SceneObjectIcon.loadSerialisedAnimationState(objectsCurrentState.currentlyAnimationState);
				SceneObjectIcon.resumeAnimation();
				// setAnimationState(currentObjectState.currentlyAnimationState);
			}

			//re add the normal run after close functions
			setupPostAnimationActions(objectsCurrentState.ObjectsFileName);

			//	setupPostAnimationActions(currentObjectState.ObjectsFileName); (not needed I think)

		}




		/*~~~~~~~~~~~~~~~~~~~~~(moved to updateStateInternal)
		if (newState.ObjectsName != null) {
			currentObjectState.ObjectsName = newState.ObjectsName;
		}

		if (newState.Title != null) {
			currentObjectState.Title = newState.Title;
		}


		if (newState.X != -1) {
			currentObjectState.X = newState.X;
			currentObjectState.moveState.movement_currentX = currentObjectState.X;

		}

		if (newState.Y != -1) {
			currentObjectState.Y = newState.Y;
			currentObjectState.moveState.movement_currentY = currentObjectState.Y;
		}~~~~~~~~~~~~~~~~~~~~~~~~
		 */

		/* ~~~~~(moved to updateStateInternal)
		if (newState.positionedRelativeToo!=null){
			currentObjectState.positionedRelativeToo = newState.positionedRelativeToo;

		} else {
			currentObjectState.positionRelativeToOnceLoaded = newState.positionRelativeToOnceLoaded;
		}

		if (newState.relX != 0) {
			currentObjectState.relX = newState.relX;

		}

		if (newState.relY != 0) {
			currentObjectState.relY = newState.relY;
		}


		if (newState.zindex != -1) {

			currentObjectState.zindex = newState.zindex;
			ObjectsLog.info("currentObjectState.zindex=" + currentObjectState.zindex);

		}
		//set up variable zindex
		currentObjectState.variableZindex=newState.variableZindex;

		if (newState.lowerZindex!=-1){
			currentObjectState.lowerZindex=newState.lowerZindex;
		}
		if (newState.upperZindex!=-1){
			currentObjectState.upperZindex=newState.upperZindex;
		}
		if (newState.stepZindex!=-1){
			currentObjectState.stepZindex=newState.stepZindex;
		}

		if (newState.moveState.movement_currentWaypoint != -1) {
			currentObjectState.moveState.movement_currentWaypoint = newState.moveState.movement_currentWaypoint; // current
			// number
			// of
			// waypoint
		} 
		 **/

		// THIS BIT DOESNT WORK - movements dont resume if currently playing
		// curves
		/* ~~~~~(moved to updateStateInternal)
		ObjectsLog.info("setting objects data to newState d");

		if (newState.moveState.movement_SX != -1) {
			currentObjectState.moveState.movement_SX = newState.moveState.movement_SX; // destination

		}
		if (newState.moveState.movement_SY != -1) {
			currentObjectState.moveState.movement_SY = newState.moveState.movement_SY;
		}

		if (newState.moveState.movement_CX != -1) {
			currentObjectState.moveState.movement_CX = newState.moveState.movement_CX; // destination

		}
		if (newState.moveState.movement_CY != -1) {
			currentObjectState.moveState.movement_CY = newState.moveState.movement_CY;
		}

		if (newState.moveState.movement_DX != -1) {
			currentObjectState.moveState.movement_DX = newState.moveState.movement_DX; // destination
			// X
		}
		if (newState.moveState.movement_DY != -1) {
			currentObjectState.moveState.movement_DY = newState.moveState.movement_DY;
		}

		if (newState.moveState.movement_StepX != -1) {
			currentObjectState.moveState.movement_StepX = newState.moveState.movement_StepX;
		}
		if (newState.moveState.movement_StepY != -1) {
			currentObjectState.moveState.movement_StepY = newState.moveState.movement_StepY;
		}

		if (newState.moveState.movement_currentX != -1) {
			currentObjectState.moveState.movement_currentX = newState.moveState.movement_currentX;
		}

		if (newState.moveState.movement_currentY != -1) {
			currentObjectState.moveState.movement_currentY = newState.moveState.movement_currentY;
		}

		if (newState.moveState.movement_speed != -1) {
			currentObjectState.moveState.movement_speed = newState.moveState.movement_speed; // pixels
			// per
			// cycle
		}
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		 * 
		 */
		/* ~~~~~(moved to updateStateInternal)
		ObjectsLog.info("setting objects movement");
		if (movementsLoaded) {
			if ((newState.currentmovement != "" )&& (!newState.currentmovement.startsWith("_internal_"))) {

				currentPath = objectsMovements.getMovement(newState.currentmovement);

				if (currentPath==null){
					Window.alert("_cant find:"+newState.currentmovement);
				}
				currentObjectState.currentmovement = newState.currentmovement;

				// if oncurve
				currentObjectState.moveState.movement_onCurve = false;
				currentObjectState.moveState.movement_curveTime = 0;
				currentObjectState.moveState.movement_curveTimeStep = 0.01;

				if (currentObjectState.moveState.movement_SX != -1) {

					currentObjectState.moveState.movement_onCurve = true;

				}

				// run it
				ObjectsLog.info("_____________________________________________________animationRunner is a go!");

				animationRunner.scheduleRepeating(100);

			}
		}
		ObjectsLog.info("setting objects data to newState e");

		if ((newState.objectsProperties!=null)&&(newState.objectsProperties!=null)) {

			ObjectsLog.info("Setting objects Properties. Num of new props = "+newState.objectsProperties.size());

			currentObjectState.objectsProperties = new PropertySet(
					newState.objectsProperties.clone());

		}
		 */

		ObjectsLog.info("setting objects data to newState f");




		if (newState.currentNumberOfFrames != -1) {
			objectsCurrentState.currentNumberOfFrames = newState.currentNumberOfFrames;
		}

		// Log.info("setobjects states to new state");

		// position
		// thisObject.objectScene.setWidgetsPosition(thisObject,
		// currentObjectState.moveState.movement_currentX,
		// currentObjectState.moveState.movement_currentY);
		if (objectsCurrentState.moveState.isPresent()){
			ObjectsLog.info("setting position  to "    +objectsCurrentState.moveState.get().movement_current_pos.toString());
		}


		// set position if not by div:
		/* ~~~~~(moved to updateStateInternal)
		if ( (this.objectsCurrentState.attachToDiv.length()<1)&&(this.objectsCurrentState.positionedRelativeToo==null)){

			ObjectsLog.info("setting position non-relative");

			super.setPosition(currentObjectState.X,
					currentObjectState.Y,false,false); //when state loading,the co-ordinates are set to the top right, not by the pin

			super.setZIndex(currentObjectState.zindex);

			ObjectsLog.info("set position too:"+currentObjectState.X+","+currentObjectState.Y);

		}  else if (this.objectsCurrentState.positionedRelativeToo!=null){

			ObjectsLog.info("setting position RelativeToo: "+objectsCurrentState.positionedRelativeToo.objectsCurrentState.ObjectsName);
			ObjectsLog.info("which is at: "+objectsCurrentState.positionedRelativeToo.objectsCurrentState.X+","+objectsCurrentState.positionedRelativeToo.objectsCurrentState.Y);

			objectsCurrentState.positionedRelativeToo.relativeObjects.add(this);

			super.updateRelativePosition();

		//	super.setPosition(currentObjectState.relX ,
			//		currentObjectState.relY ,false,false); //when state loading,the co-ordinates are set to the top right, not by the pin

			super.setZIndex(currentObjectState.zindex);

			ObjectsLog.info("set position too:"+currentObjectState.X+","+currentObjectState.Y);

		}*/



		/* ~~~~~(moved to updateStateInternal)
		ObjectsLog.info("currentObjectState.zindex is " + currentObjectState.zindex);


		ObjectsLog.info("setting setVisible:"+newState.currentlyVisible);
		// set its visibility
		this.setVisible(newState.currentlyVisible);
		ObjectsLog.info("Objects title setting to:" + currentObjectState.Title);

		this.setTitle(currentObjectState.Title);

		// Log.info("clearing state");
		// previousStates.clear();

		ObjectsLog.info("Objects propertysChanged");

		super.propertysChanged();
		 **/
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
		ObjectsLog.info("(after updatestate) Animation running "+SceneObjectIcon.isAnimating());

		// update debug boxs
		if (oipu != null) {
			oipu.update();
		}

	}


	/**
	 * Makes the internal sprite reflect the current state.
	 * Normally only needed when loading, as once loaded the sprite and objectstate should be in sync anyway 
	 * and any changes to the state should be done with updatestate which ensures they are kept sync.
	 * 
	 * @param runOnload
	 */
	public void initCurrentState(final boolean runOnload) {
		//NOTE THE FOLLOWING IS THUS A TEMP EXPIREMENT.
		//We need a neater way to do this as the code is a duplicate of later code atm
		super.updateState(objectsCurrentState,false,false); //could this update be instead a initilise on the superclass?

		if (objectsCurrentState.currentlyAnimationState.length() > 1) {
			SceneObjectIcon.loadSerialisedAnimationState(objectsCurrentState.currentlyAnimationState);
			SceneObjectIcon.resumeAnimation();
		} else {
			SceneObjectIcon.gotoFrame(objectsCurrentState.currentFrame);
		}

		if (runOnload) {
			ObjectsLog.info("running objects onload again");
			alreadyLoaded = false; //has to be reset to false to run onload again
			onLoad();
		}

		updateThingsPositionedRelativeToThis(true);

		ObjectsLog.info("Animation running:"+SceneObjectIcon.isAnimating());

		// update debug boxs
		if (oipu != null) {
			oipu.update();
		}
	}

	@Override
	public void clear() {
		//No op. Clear exists here purely to prevent any clearing of the sprite from its focus panel
		//Unlike div objects which have their children cleared, or input objects which have their text removed
		//Sprites dont have anything to "clear"  
	}

	private void animateToFrame(int newframe,boolean disablePostAnimationActionsForThisCommand) {
		
		if (disablePostAnimationActionsForThisCommand){

			ObjectsLog("disableing animation end commands for next firing.");
			//Disables the post animation commands for the next time they fire only
			SceneObjectIcon.disableNextPostAnimationCommands(true);
			
		}
		
		if (objectsCurrentState.currentFrame < newframe) {

			Log.info("playing forward until frame:" + newframe);
			SceneObjectIcon.playUntill(newframe);
			objectsCurrentState.currentFrame = newframe;

		} else if (objectsCurrentState.currentFrame > newframe) {

			// play back until
			Log.info("playing backward untill frame:" + newframe);
			SceneObjectIcon.playBackUntill(newframe);
			objectsCurrentState.currentFrame = newframe;
		} else {
			Log.info("current frame number the same");
			
			ObjectsLog("enableing animation end commands for next firing.");			
			SceneObjectIcon.disableNextPostAnimationCommands(false); //ensure reenabled if we make no change
			
		}

		// update debug boxs
		if (oipu != null) {

			oipu.update();
		}
	}

	@Override
	public void resetObject() {
		resetObject(true,true);
	}

	/**
	 * if reRunUpdateState and reOnLoad is false this method effectively just 	super.resetObject();
	 * 
	 * @param reRunUpdateState
	 * @param reOnLoad
	 */
	public void resetObject(boolean reRunUpdateState,boolean reOnLoad) {

		ObjectsLog.log("Resetting object:","#FF0000");
		//Log.info("________Resetting object:"+this.initialObjectState.ObjectsName+"_____________");
		Log.info("resetting:"+this.objectsCurrentState.ObjectsName);

		if (reRunUpdateState){
			updateState(getInitialState(), false,true);
		}

		ObjectsLog.info("trying to run objects onload again");

		if (reOnLoad){
			alreadyLoaded = false; //we reset here as to update it might have been reattached and thus the onload fired twice
			this.onLoad();
		}
		// run first commands again
		// onLoad();


		//The super here will refer to "SceneDivObjects.reset()" rather then SceneObjectVisual.reset()
		//This will cause problems as we dont want reset running updateState and onload again!
		//To solve this resetobject will need to take a boolean to determine if it does those actions or not

		super.resetObject(false,false);

	}



	@Override
	public void saveTempState() {

		//Spites require objectsCurrentState to be updated before any saving of info
		objectsCurrentState.currentFrame = SceneObjectIcon.getCurrentframe();
		ObjectsLog.info("saving sprite object data");
		objectsCurrentState.currentlyAnimationState = SceneObjectIcon.serialiseAnimationState();
		objectsCurrentState.currentFrame = SceneObjectIcon.getCurrentframe();

		super.saveTempState();
		tempObjectState.setObjectsPrimaryType(objectsCurrentState.getPrimaryObjectType());
		Log.info("seralised to:" + tempObjectState.serialiseToString());
		
		super.ObjectsLog("new temp state made:seralised to:" + tempObjectState.serialiseToString(),"green");
		
		
	}	

	/*
	//@Override
	public void saveTempState_old() {

		// update frames:
		objectsCurrentState.currentFrame = SceneObjectIcon.currentframe;

		////if (!SceneObjectIcon.isAnimating()) {
		//	currentObjectState.currentlyAnimationState = "";
		//} else {
		//	currentObjectState.currentlyAnimationState = SceneObjectIcon
		//			.serialiseAnimationState();
		//}

		ObjectsLog.info("saving object");

		objectsCurrentState.currentlyAnimationState = SceneObjectIcon.serialiseAnimationState();
		objectsCurrentState.currentFrame = SceneObjectIcon.currentframe;

		ObjectsLog.info("current animation  = "
				+ objectsCurrentState.currentlyAnimationState);

		ObjectsLog.info("currentObjectState.currentFrame="
				+ objectsCurrentState.currentFrame);

		ObjectsLog.info("currentObjectState.currentNumberOfFrames="
				+ objectsCurrentState.currentNumberOfFrames);

		ObjectsLog.info("currentPath.pathsName=" + objectsCurrentState.moveState.currentmovement);

		//not upto date, does not include Relative object, and clone object data
		tempObjectData = objectsCurrentState.copy();


		tempObjectData.setObjectsPrimaryType(objectsCurrentState.getCurrentPrimaryType());

		Log.info("seralised to:" + tempObjectData.serialiseToString());

		return;
	}
	 */


	/*
	@Override
	public void restoreTempState() {

		this.updateState(tempObjectData, false);
		// update debug boxs
		if (oipu != null) {

			oipu.update();
		}
	}*/

	/**
	 * sets the initial state to the supplied state, the state should match this objects type 
	 * as a untested cast is used
	 * @param objectsInitialState
	 */
	//@Override
	protected void setObjectsInitialState(SceneObjectState objectsInitialState) {		
		super.setObjectsInitialState(objectsInitialState);
		initialObjectState=(SceneSpriteObjectState) objectsInitialState;
	}


	/**
	 * @return the objectsCurrentState
	 */
	@Override
	public SceneSpriteObjectState getObjectsCurrentState() {		
		return (SceneSpriteObjectState)objectsCurrentState;
	}

	public SceneSpriteObjectState getInitialState() {
		return (SceneSpriteObjectState)initialObjectState;
	}

	public SceneSpriteObjectState getTempState() {
		return (SceneSpriteObjectState)tempObjectState;
	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.IsSceneSpriteObject#setFrameGap(int)
	 */
	//@Override
	public void setFrameGap(int gap) {
		objectsCurrentState.currentFrameGap = gap;
		SceneObjectIcon.setFrameGap(gap);
	}


	/**
	 * sets the animation state
	 */
	//TODO: change this t more enum based, and make it refer to HasFrameControll functions on this class, 
	//rather then the icon directly.
	//Then work out somewhere to put this so non-gwt implementations don't need to implement this function
	public void setAnimationStatus(String state) {
		SpriteObjectHelpers.setAnimationsStatusHelper(state, this, SceneObjectIcon.animation);
		
		
		objectsCurrentState.ObjectsURL   = SceneObjectIcon.getUrl();
		
		ObjectsLog("Setting animation status too: "+state+" url is:"+objectsCurrentState.ObjectsURL);
		
		
		// update debug boxs
		if (oipu != null) {
			oipu.update();
		}
		// add to undo stack
		// previousStates.push(state);
	}




	protected void imageloaded() {
		
		this.ObjectsLog("Image loaded","green");

		visualsLoaded = true;
		
		//only advance the loading  forward if its not a inventory item
		//in future if need be, we can have a similar load monitoring in the inventory
		//this method would then be override by InventoryIcon child class of this
	//	if (objectsCurrentState.getPrimaryObjectType() == SceneObjectType.InventoryObject){

		//} else {
							
				//if attached we also set as physically loaded
				testIfPhysicallyLoaded();
								
			
			
		//}
	}


	private void testIfPhysicallyLoaded() {
		if (attached && visualsLoaded){
			super.setAsPhysicallyLoaded();				
			//only advance loading if we even have a scene
			//some objects, like inventory items, dont!
			if (this.getParentScene()!=null){
				getParentScene().advancePhysicalLoading(this.getName()); 	//and tell the parent scene

				getParentScene().stepLoadingTotalVisual(1); //advancePhysicalLoading no longer does this itself

			}
		}
	}
	
	
	//both are needed to count as physically loaded
	private boolean attached = false;
	private boolean visualsLoaded = false;
	
	@Override
	public void onLoad() {
		//super.onLoad(); //prevents setAsPhysicallyLoaded running by default. We only want that for when our visual loads
		attached = true;

		this.ObjectsLog("--gwt onload (sprite attached)");
		
		
		testIfPhysicallyLoaded();
	}
	
	

	/**
	 * assigns the sprite specific parameters
	 * hmm...a lot of this really should be moved into the state objects somehow
	 */
//	@Override
//	protected void assignObjectTypeSpecificParameters() { //String itemslines[]
//
//		if (objectsCurrentState!=null){
//			ObjectsLog.info("(sprite parameters already set up)");
//			Log.info("(sprite parameters already set up)");
//			return;
//		}	
//
//		super.assignObjectTypeSpecificParameters(); //itemslines
//
//		ObjectsLog.log("(assigning sprite ObjectTypeSpecificParameters)","green");
//		Log.info("__assigning sprite ObjectTypeSpecificParameters to object___");
//
//
//		//As this is the only remaining real line of importance, we could move this to the constructor and get rid of this whole function?
//		//(as long as we do the same in all classes so the ones in the super get called as well)
//		objectsCurrentState = (SceneSpriteObjectState) super.getObjectsCurrentState();
//
//		//	Log.info("objectsCurrentState.name="+objectsCurrentState.ObjectsName);
//
//		//	if (itemslines==null){
//		//	Log.info("(no parameters to assign");
//		//		return;			
//		//	}
//
//		//assign folder name from ObjectName (really this should be handled within assignObjectTypeSpecificParameters)
//		//folderName = objectsCurrentState.ObjectsFileName.split("0\\.")[0]; //current crashes if ObjectsFileName is null
//		//Log.info("foldername = "+folderName+" name="+objectsCurrentState.ObjectsName);
//
//		//the object current state can handle the rest on its own
//
//		//should be just triggered from sceneobject
//		//(note folderName needs to be correct before that will work)
//		//objectsCurrentState.assignSpriteObjectTypeSpecificParameters(itemslines);
//
//		//String itemslines[] = Parameters.split("\n");
//
//
//		///below removed for testing above assignObjectTypeSpecificParameters which should do the same job		
//		//		int currentlinenum = 0;
//		//
//		//		// assign data not yet assigned in super
//		//		while (currentlinenum < itemslines.length) {
//		//
//		//			String currentline = itemslines[currentlinenum].trim();
//		//			currentlinenum++;
//		//
//		//			if (currentline.equals("")){				
//		//				continue;
//		//			}
//		//			if ((currentline.length() < 3) || (currentline.startsWith("//"))) {
//		//				continue;
//		//			}
//		//
//		//			// split by =
//		//			String param = currentline.split("=")[0].trim();
//		//			String value = currentline.split("=")[1].trim();
//		//
//		//			
//		//			if (param.equalsIgnoreCase("Name")) {
//		//
//		//				objectsCurrentState.ObjectsName = value;
//		//				objectsCurrentState.ObjectsFileName = value;
//		//				Log.info("Objects file name set by default to:"
//		//						+ objectsCurrentState.ObjectsFileName);
//		//
//		//			}
//		//			
//		//			if (param.equalsIgnoreCase("FileName")) {
//		//
//		//				objectsCurrentState.ObjectsFileName = value;
//		//				Log.info("Objects file name specificly set to:"
//		//						+ objectsCurrentState.ObjectsFileName);
//		//
//		//			}
//		//
//		//			if (param.equalsIgnoreCase("Frames")) {
//		//
//		//				objectsCurrentState.currentNumberOfFrames = Integer
//		//						.parseInt(value);
//		//				Log.info("Objects Frame count set to:" + value);
//		//
//		//			}
//		//
//		//		}
//		//		
//		//		//------------------------------------------------------------------------
//		//		//----------------------------------------------------
//		//		//----------------------------------
//		//		String sourceFolder = "";
//		//		//we will in future get the golder from the name so we dont have to pass it in
//		//		//the following is a temp check they will always match
//		//		if (objectsCurrentState.getPrimaryObjectType() == SceneObjectType.InventoryIcon){
//		//			sourceFolder = "InventoryItems";
//		//		} else {
//		//			sourceFolder = "Game Scenes/"+objectsCurrentState.ObjectsSceneName    ;//newobjectdata.ObjectsSceneName;						
//		//		}
//		//
//		//
//		//		//Sprites need some extra work to get the settings right
//		//		//This is because the filename has to be treated differently due to it containing frame numbers
//		//		//or even being a internal animation
//		//
//		//		// strip extension from name
//		//		if (objectsCurrentState.ObjectsName.contains(".")) {
//		//
//		//			objectsCurrentState.ObjectsName = objectsCurrentState.ObjectsName
//		//					.substring(0,
//		//							(objectsCurrentState.ObjectsName.indexOf(".")));
//		//
//		//			//if theres a 0 at the end remove that too 
//		//			if (objectsCurrentState.ObjectsName.endsWith("0")){
//		//				objectsCurrentState.ObjectsName=objectsCurrentState.ObjectsName.substring(0, objectsCurrentState.ObjectsName.length()-1);
//		//			}
//		//
//		//
//		//		}
//		//
//		//		if (objectsCurrentState.ObjectsFileName.startsWith("<")) {
//		//
//		//			objectsCurrentState.ObjectsURL = objectsCurrentState.ObjectsFileName;
//		//
//		//			// if name is blank
//		//			if (objectsCurrentState.ObjectsName.length() < 3) {
//		//				objectsCurrentState.ObjectsName = objectsCurrentState.ObjectsFileName
//		//						.trim();
//		//			}
//		//			Log.info("______________________currentObjectState.ObjectsName:"
//		//					+ objectsCurrentState.ObjectsName + ":");
//		//
//		//		} else {
//		//
//		//			folderName = objectsCurrentState.ObjectsFileName.split("0\\.")[0];
//		//
//		//			ObjectsLog.info("foldername = "+folderName+" name="+objectsCurrentState.ObjectsName);
//		//
//		//			//different source folder for inventory items
//		//
//		//			if (objectsCurrentState.getPrimaryObjectType()==SceneObjectType.InventoryIcon){
//		//
//		//				Log.info("setting up location for inventory icon ");
//		//
//		//				objectsCurrentState.ObjectsURL = sourceFolder
//		//						+ "/" + objectsCurrentState.ObjectsName+ "/"
//		//						+ objectsCurrentState.ObjectsFileName;
//		//				//Note; For inventory items the folder name is the same as the objectname
//		//				//This isnt always true of sceneitems, which the folder name is always 
//		//				//the same as the filename, but can be different from its objectname (ie, when many objects refer to the same image file)
//		//				//in future it might be worth changing inventory icons to the same system
//		//				//by having their "folderName" correctly set earlier.
//		//
//		//			} else {
//		//
//		//				ObjectsLog.info("setting up location for sprite ");
//		//
//		//				if (!(folderName.contains("\\") || folderName.contains("/"))){
//		//					ObjectsLog.info("using default path  ");
//		//					objectsCurrentState.ObjectsURL = sourceFolder
//		//							+ "/Objects/" + folderName+ "/"
//		//							+ objectsCurrentState.ObjectsFileName;
//		//				} else {
//		//					ObjectsLog.info("setting up location for sprite with full path ");
//		//					objectsCurrentState.ObjectsURL = objectsCurrentState.ObjectsFileName;
//		//
//		//				}
//		//			}
//		//		}
//		//		
//		//		
//		//
//		//		Log.info("__assigned sprite ObjectTypeSpecificParameters to object "+objectsCurrentState.ObjectsName+" ___");
//		//		
//
//
//
	
//
//
//	}

	
	
	
	@Override
	public int getPhysicalObjectWidth(){
		//TODO: This is a temp override while we are working out why the normal getOffsets dont work durign loading
		//See: stackoverflow.com/questions/35904452/image-attached-and-loaded-yet-getoffsetwidth-height-still-zero
		
		return SceneObjectIcon.imageContents.getWidth();
	}
	@Override
	public int getPhysicalObjectHeight(){
		//TODO: This is a temp override while we are working out why the normal getOffsets dont work durign loading
		//See: stackoverflow.com/questions/35904452/image-attached-and-loaded-yet-getoffsetwidth-height-still-zero
		
		return SceneObjectIcon.imageContents.getHeight();
	}
	//---------------------------------------------------------------------
	
	@Override
	public String getSerialisedAnimationState() {

		if (!SceneObjectIcon.isAnimating()) {

			getObjectsCurrentState().currentlyAnimationState = "";

		} else {
			getObjectsCurrentState().currentlyAnimationState = SceneObjectIcon.serialiseAnimationState();
		}

		return getObjectsCurrentState().currentlyAnimationState;
	}

	@Override
	public int getCurrentFrame() {
		//update current frame to ensure it matches the icon
		getObjectsCurrentState().currentFrame = SceneObjectIcon.getCurrentframe();
		return 	getObjectsCurrentState().currentFrame;
	}

	@Override
	public String getCurrentURL() {
		//update current url to ensure it matches the physical sprite
		getObjectsCurrentState().ObjectsURL = SceneObjectIcon.getUrl();
		return 	getObjectsCurrentState().ObjectsURL;
	}


}
