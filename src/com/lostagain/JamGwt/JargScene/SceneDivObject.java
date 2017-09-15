package com.lostagain.JamGwt.JargScene;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneDivObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneDivObject;

/** 
 * A Div Object itself is an extremely simple object; A div with a css applied.
 * The idea is to use it for relatively static elements like backgrounds, boxes
 * or overlays.
 * The class is extended by many other types (Vector,Sprite,Input etc) in order to provide more functionality 
 ***/

public class SceneDivObject extends SceneObjectVisual implements IsSceneDivObject {

	public static Logger Log = Logger.getLogger("JAM.SceneDivObject");

	/**
	 * Stores the current object state. ALL object interactions should update
	 * this, or read from this.
	 **/
	private SceneDivObjectState objectsCurrentState;
	
	// ----------------------------------------------------------------------------


	// this object
	IsSceneDivObject thisObject = this;



	// ----------------------------------------------------------------------------
	/**
	 * The actual contents that this widget will be.
	 * By default it will be a simplepanel (which is a div) but subclass's can supply their own widget to put here, provided
	 * it is a normal GWT widget.
	 */
	Widget widgetContents; 


	/** Determines if the object is is moving **/
	//public boolean isMoving = false;



	/**
	 * Creates a new scene div object. <br><br>
	 * @param newobjectdata  - the DivState that determines stuff like basic position and size<br>
	 * @param actions - a list of actions that determines how this object will rect to stuff (can be null)<br>
	 * @param sceneItsOn - the scene this object is going to be on
	 */
	public SceneDivObject(SceneObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {
		this(new SceneDivObjectState(newobjectdata),actions,sceneItsOn,null,true);

	}

	public SceneDivObject(SceneDivObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {
		this((newobjectdata),actions,sceneItsOn,null,true);

	}

	//phase out
	//we should slowly change to supplying the actions directly rather then the whole objectsCurrentStateString
	//
	/*
	public SceneDivObject(SceneObjectState newobjectdata, String objectsCurrentStateString,
			SceneWidgetVisual sceneItsOn) {
		this(new SceneDivObjectState(newobjectdata),objectsCurrentStateString,sceneItsOn);

	}

	public SceneDivObject(SceneDivObjectState newobjectdata, String objectsCurrentStateString,
			SceneWidgetVisual sceneItsOn) {
		this(newobjectdata,  objectsCurrentStateString, sceneItsOn, null,true);
	}*/


	//temp constructor
	//we should slowly change to supplying the actions directly rather then the whole objectsCurrentStateString
	//
	/*
	protected SceneDivObject(SceneDivObjectState newobjectdata, String objectsCurrentStateString,
			SceneWidgetVisual sceneItsOn, Widget innerWidget, boolean finaliseLoad) {

		this( newobjectdata,splitActionsFromParametersAndActions(objectsCurrentStateString), sceneItsOn, innerWidget,  finaliseLoad);
	}
	 */
	/**<br>
	 * Creates a new scene div object. <br>
	 * For subclasses to implement, this constructor creates a base Div object for others to build from.<br>
	 * You need to supply a innerwidget and set finalize to false to do this.<br>
	 * <br>
	 * @param newobjectdata - the DivState that determines stuff like basic position and size<br>
	 * @param ActionList - a list of actions that determines how this object will rect to stuff (can be null)<br>
	 * @param sceneItsOn - the scene this object is going to be on
	 * @param innerWidget  - the actual contents of this widget. By default a div, but subclasses can provide alternative contents
	 * @param finaliseLoad - determines if we run the rest of the setup here, or set to false for a subtype of this to do it
	 **/
	protected SceneDivObject(
			SceneDivObjectState newobjectdata, 
			ActionList actions,
			SceneWidget sceneItsOn, 
			Widget innerWidget, 
			boolean finaliseLoad) {

		//by passing the data though to the constructor straight away
		//we allow it to handle the action and parameter settings itself.
		//It can separate of what it needs from the objectsCurrentStateString, and put it into the newly created SceneDivObjectState
		//
		super(newobjectdata,actions,sceneItsOn); //create and pass the SceneDivObjectState to the superclass constructor

		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
		//and also copys the super.objectsCurrentState to their own (type specific) objectsCurrentState object
	
		
		//	assignObjectTypeSpecificParameters();//(newobjectdata.ObjectsParamatersFromFile); //cant be done from super()			
		objectsCurrentState = (SceneDivObjectState) super.getObjectsCurrentState();

		Log.info("_________creating div object "+this.getName()+"________________");
		ObjectsLog.info("(creating div object)");






		//Lots of old code below which had to be done before the superclass handled it all;

		//objectsCurrentState = (SceneDivObjectState) super.objectsCurrentState;//then grab it back from the constructor (we know its a DivObjectState as we just made it on the above line so we can safely cast to it)


		//new method; we create and assign the state in one movement, saving a lot of redundancy with default values
		//	objectsCurrentState = new SceneDivObjectState(newobjectdata);
		//	super.objectsCurrentState = objectsCurrentState;



		// extract div specific data
		//		Log.info("extracting div data from:\r" + objectsCurrentStateString);
		//
		//		// split actions off if present
		//		// get scene parameters (anything before a line with : in it)
		//		int firstLocOfColon = objectsCurrentStateString.indexOf(':');
		//
		//		Log.info("LocOfColon recieved for div object:" + firstLocOfColon);
		//
		//		String Parameters = "";
		//		String allactions = "";
		//		if (firstLocOfColon == -1) {
		//			Parameters = objectsCurrentStateString;
		//		} else {
		//			int linebeforeColon = objectsCurrentStateString
		//					.lastIndexOf('\n', firstLocOfColon);
		//			Parameters = objectsCurrentStateString.substring(0, linebeforeColon);
		//			allactions = objectsCurrentStateString.substring(linebeforeColon);
		//
		//		}
		//
		//		assignObjectTypeSpecificParameters(Parameters);
		//		
		//		// add the actions
		//		if (allactions.length() > 0) {
		//			//Log.info("actions are:" + allactions);
		//
		//			objectsActions = new ActionList(allactions);
		//			updateHandlersToMatchActions();
		//
		//		}


		Log.info("done loading params");




		//assign the real widget if one was supplied
		if (innerWidget!=null){
			this.widgetContents = innerWidget;
		}


		//we used to assign scene actions here
		//(now in supertype)

		//if we are a div and not subtype we finalize
		if (finaliseLoad){
			
			
			// set defaults:
		//	setObjectsInitialState(objectsCurrentState.copy());


			Log.info("_________finalising div________________");
			
			
			// create object from data
			setupWidget(objectsCurrentState); 

			//startLoadingCmap(); //should move to supertype?

			ObjectsLog.logTimer("setup div took;");

			initialiseAndAddToDatabase();
		}


		//if (objectsCurrentState==null){
		//	Log.info("________objectsCurrentState is null____________");
		//}
		//if (this.getObjectsCurrentState()==null){
		//	Log.info("this.getObjectsCurrentState() is null____________");
		//}
		// add to the div object list
		//SceneObjectDatabase.all_div_objects.put(objectsCurrentState.ObjectsName.toLowerCase(), this);
		
		//SceneObjectDatabase.addDivObjectToDatabase(this);

		
	//	SceneObjectDatabase.addObjectToDatabase(this,SceneObjectType.Div);
		
	//	if (finaliseLoad){
	//		finaliseAndAddToDatabase();
	//	}
	}

	/**
	 * Assigns the div-specific parameters from the parameter string
	 * @param Parameters
	 **/
//	@Override
//	void assignObjectTypeSpecificParameters() { //String itemslines[]
//
//		Log.info(" ( assignObjectTypeSpecificParameters for div object )");
//		ObjectsLog.log("(assigning div ObjectTypeSpecificParameters)","green");
//
//		if (objectsCurrentState!=null){
//			ObjectsLog.info("(div parameters already set up)");
//			Log.info("(div parameters already set up)");
//			return;
//		}
//
//		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
//		//and also makes a reference to the super.objectsCurrentState to their own (type specific) objectsCurrentState object in order to do this
//		objectsCurrentState = (SceneDivObjectState) super.getObjectsCurrentState();
//
//		//if (itemslines==null){
//		//	Log.info("(no parameters to assign");			
//		//	return;
//
//		//}
//		//Now triggered from SceneObject
//		//objectsCurrentState.assignObjectTypeSpecificParameters(itemslines);
//
////		
////
////		// now use the pre-split lines to get parameter data
////		int currentlinenum = 0;
////		while (currentlinenum < itemslines.length) {
////
////			String currentline = itemslines[currentlinenum].trim();
////			currentlinenum++;
////			if ((currentline.length() < 2) || (currentline.startsWith("//"))) {
////				continue;
////			}
////
////			Log.info("processing line:"+currentline);
////
////
////			// split by =
////			String param = currentline.split("=")[0].trim();
////			String value = currentline.split("=")[1].trim();
////
////			// assign data
////			/*
////			if (param.equalsIgnoreCase("Name")) {
////
////				// ObjectsName = value;
////				objectsCurrentState.ObjectsName = value;
////				Log.info("Objects file name set to:"+ objectsCurrentState.ObjectsName);
////
////				//Dialogue objects if they use a folder, its just their object name
////				folderName = objectsCurrentState.ObjectsName;
////
////			}
////			 */
////
////			/** size can be set in the css or in the script
////			 * the script will override any css setting  **/
////			if (param.equalsIgnoreCase("Size")) {
////
////				String value1 = value.split(",")[0];
////				String value2 = value.split(",")[1];
////
////				value1 = SceneDivObject.ensureSizeIsRealCss(value1);
////				value2 = SceneDivObject.ensureSizeIsRealCss(value2);
////
////				objectsCurrentState.sizeX = value1;
////				objectsCurrentState.sizeY = value2;
////
////				Log.info("div Objects "+this.objectsCurrentState.ObjectsName+"size set to:" + objectsCurrentState.sizeX
////						+ " x " + objectsCurrentState.sizeY);
////
////			}
////
////			if (param.equalsIgnoreCase("RestrictToScreen")) {
////
////				// Note, for div type it defaults *not* to restricting to the screen
////				if (value.equalsIgnoreCase("true")) {
////					objectsCurrentState.restrictPositionToScreen = true;
////				} else {
////					objectsCurrentState.restrictPositionToScreen = false;
////				}
////
////			}
////			/*
////			if (param.equalsIgnoreCase("ActionOveride")) {
////
////				// Note, for dialogue it defaults too ignoring scene actions
////				if (value.equalsIgnoreCase("false")) {
////					ignoreSceneActions = false;
////				} else {
////					ignoreSceneActions = true;
////				}
////			}//
////
////		}
////
////
////		Log.info("__assigned div ObjectTypeSpecificParameters to object "+objectsCurrentState.ObjectsName+" ___");
//
//
//	}

	/*
	public SceneDivObject(SceneDivObjectState data, SceneWidgetVisual sceneItsOn) {
		super(null,null,sceneItsOn);

		super.objectsCurrentState = objectsCurrentState;

		initialObjectState =  data.copy();
		//associate this default with the parent types variable too
		//	super.getObjectsInitialState() = defaultObjectState;
		setObjectsInitialState(initialObjectState);

		Log.info("______starting to setup 1");


		setupWidget( sceneItsOn); //data,
	}
	 */
	protected void setupWidget(SceneDivObjectState data) {
		//SceneDivObjectState data,
		//super.setObjectsSceneVariable(sceneItsOne);
		//objectScene = sceneItsOne;

		//objectsCurrentState = data;

		//all the things common to all objects to setup are done in SceneObject
		super.setupWidget();

		ObjectsLog.log("Setting up div object","orange");

		//now we create the div-specific things		
		Log.info("___________________________________________________________________creating div object");

		//set style if one is specified
		if (objectsCurrentState.CurrentBoxCSS!=null && objectsCurrentState.CurrentBoxCSS.length()>0){		
			ObjectsLog.info("setting up CurrentBoxCSS to : "+objectsCurrentState.CurrentBoxCSS);
			setBoxCSS(objectsCurrentState.CurrentBoxCSS);			
		}


		if (objectsCurrentState.BackgroundString.length()>2)
		{
			super.getElement().getStyle().setProperty("background",objectsCurrentState.BackgroundString);			
		}

		// create animation timer
	//	createAnimationTimer();

		Log.info("starting setup div widget...");
		if (widgetContents == null){
			Log.info("(defaulting to div widget)...");
			widgetContents = new SimplePanel();
		}

		super.setWidget(widgetContents);

		//set size
		super.setSize(objectsCurrentState.sizeX, objectsCurrentState.sizeY);



		//startLoadingMovementAnimations();


	}


	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.tempIsDivObject#setBoxCSS(java.lang.String)
	 */
	@Override
	public void setBoxCSS(String currentProperty) {

		setStyleName(currentProperty);

		objectsCurrentState.CurrentBoxCSS = currentProperty;

		//update the objects inspector if there is one
		if (oipu != null) {
			oipu.update();
		}

	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.tempIsDivObject#addBoxCSS(java.lang.String)
	 */
	@Override
	public void addBoxCSS(String currentProperty) {

		addStyleName(currentProperty);

		objectsCurrentState.CurrentBoxCSS = this.getStyleName();

		//update the objects inspector if there is one
		updateDebugInfo();
		//if (oipu != null) {
		//	oipu.update();
		//}

	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.tempIsDivObject#removeBoxCSS(java.lang.String)
	 */
	@Override
	public void removeBoxCSS(String currentProperty) {

		removeStyleName(currentProperty);

		objectsCurrentState.CurrentBoxCSS  = this.getStyleName();

		//update the objects inspector if there is one
		updateDebugInfo();
		//if (oipu != null) {
		//	oipu.update();
		//}

	}



	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.tempIsDivObject#setCSS(java.lang.String)
	 */
	@Override
	public void setCSS(String primaryName){
		if (primaryName.length()>0){
			widgetContents.setStylePrimaryName(primaryName);
		}
	}

	/** fills the div with the specified widget.
	 * This only works if we are using the default Simple Panel implementation for this class,
	 * and not a subtype like Sprite or Input **/
	public void setDivsWidget(Object widget){
		if (widgetContents.getClass()==SimplePanel.class){			
			((SimplePanel)widgetContents).setWidget((Widget)widget); //bad cast really but necessary due to required interface
		}

	}


	// on attach run onLoad animation
	/*
	@Override
	public void onLoad() {
		super.onLoad();
		/*
		Log.info("__________onLoad actions:");
		//ok...deep breath....we trigger the OnFirstLoad actions when there is actions to run, and this hasnt already been loaded , and we are not loading the scene this object is in silently!

		if (actionsToRunOnFirstLoad != null && alreadyLoaded == false && !this.getScene().loadingsilently) {
			InstructionProcessor.lastDivObjectUpdated = this;

			InstructionProcessor.processInstructions(
					actionsToRunOnFirstLoad.CommandsInSet.getActions(), "fl_"
							+ this.objectsCurrentState.ObjectsName, this);
		}
		Log.info("__________onLoad actions ran");
		alreadyLoaded = true;

	}*/

	/**
	 * updates the object to match the specified object state data, any nulls in<br>
	 * the new data will result in the old data for those variables being kept<br>
	 * <br>
	 * NOTE this wont function correctly unless subclasses have overidden clear();<br>
	 * clear should reset the widget to default state they exist in before the code changes anything<br>
	 * eg, a inputbox is empty of text by default<br>
	 * a div would not have subdivs in it<br>
	 * a sprite would be a no-op<br>
	 * so would a vector<br>
	 * <br>
	 **/
	@Override
	public void updateState(SceneDivObjectState newState,
			final boolean runOnload,final boolean repositionObjectsRelativeToThis) {

		Log.info("setting objects data to newState requested");

		// remove contents, if any (should only be run for divs??)
		Log.info("clearing child widgets if we are a div and not a subclass...");
		// this.clear();
		if (widgetContents.getClass()==SimplePanel.class){			
			((SimplePanel)widgetContents).clear();;
		}		
		if (internalFocusPanel.getWidget()==null){
			Log.warning("(has no widget set)");			
		}

		//general update stuff
		super.updateGeneralObjectState(newState);

		ObjectsLog.log("Updating objects Div state - RunOnLoad="+runOnload+" AlreadyLoaded="+alreadyLoaded,"orange");
		Log.info("setting objects data to newState");

		//NOTE: This should be adapted for a general SceneObject style system. 
		//(does it need to be applied to the inner div?)
		if (newState.CurrentBoxCSS.length()>0){
			//widgetContents.setStylePrimaryName(newState.CurrentBoxCSS);
			getObjectsCurrentState().CurrentBoxCSS = newState.CurrentBoxCSS;
			super.setStylePrimaryName(newState.CurrentBoxCSS);
		}

		if (newState.BackgroundString.length()>2)
		{
			getObjectsCurrentState().BackgroundString = newState.BackgroundString;
			
			super.getElement().getStyle().setProperty("background",objectsCurrentState.BackgroundString);

		}
		
		if (newState.Title != null) {
			getObjectsCurrentState().Title = newState.Title;
			this.setTitle(getObjectsCurrentState().Title); 
		}
		
		if (runOnload) {

			Log.info("running objects onload again");
			alreadyLoaded = false; //has to be reset to false to run onload again
			onLoad();
		} else {

			Log.info("run onload no specified");
		}

		if (repositionObjectsRelativeToThis){
			Log.info("updateThingsPositionedRelativeToThis:");
			updateThingsPositionedRelativeToThis(true);
		}

		Log.info("done div update, or div phase if multipart update ");
		// --------
	}

	@Override
	public void resetObject() {
		resetObject(true,true);
	}

	@Override
	/**
	 * if reRunUpdateState and reOnLoad is false this method effectively just 	super.resetObject();
	 * 
	 * @param reRunUpdateState
	 * @param reOnLoad
	 */
	public void resetObject(boolean reRunUpdateState,boolean reOnLoad) {
		Log.info("resetting:"+this.objectsCurrentState.ObjectsName);
		if (reRunUpdateState){
			//updateState(initialObjectState, false,true);
			updateState(getInitialState(), false,true);
		}

		if (reOnLoad){
			alreadyLoaded = false;
			Log.info("running objects onload again");
			this.onLoad();
		}

		//This will run resetObject on its parent
		super.resetObject(false,false); //we are carefull to call this one, not just result() because reset() can cause a loop as it will call this method again
		

		// Log.info("running objects onload again");
		// run first commands again
		// onLoad();
		// --------
	}

	/**
	 * Note; subclasses should override this to provide their own specific implementations<br><br>
	 * @return the objectsCurrentState as a SceneDivObjectState<br>
	 */
	public SceneDivObjectState getObjectsCurrentState() {
		return (SceneDivObjectState)objectsCurrentState; //refers to the internal reference to objectscurrentstate
	}
	
	public SceneDivObjectState getInitialState() {
		return (SceneDivObjectState)initialObjectState; //directly refers to the objects initial state in superclass
	}
	public SceneDivObjectState getTempState() {
		return (SceneDivObjectState)tempObjectState; //directly refers to the objects temp state in the superclass
	}

	@Override
	public void saveTempState() {

		Log.info("saving div object");

		tempObjectState = objectsCurrentState.copy();


		Log.info("seralised to:" + tempObjectState.serialiseToString());

	}

	/**
	 * When setting visible any Div object, or object that inherits div,<br>
	 * there is a possibility that "restrict to screen" is checked. <br>
	 * this is because a object might have moved while invisible.<br>
	 * Restrict to screen means the object attempts to stay inside the screen boundarys - usefull for dialogue<br>
	 * <br>
	 * We assume when positioning that it should be positioned by pin. Please used setVisible(boolean,boolean) if it should<br>
	 * not be by pin<br>
	 */
	//@Override
	//public void setVisible(boolean status) {
	//	setVisible(status, true);
	//}
	//t
	/**
	 * When setting visible any Div object, or object that inherits div,<br>
	 * there is a possibility that "restrict to screen" is checked. <br>
	 * this is because a object might have moved while invisible.<br>
	 * Restrict to screen means the object attempts to stay inside the screen boundary's - useful for dialogue<br>
	 * <br>
	 * Because this means set visible can also, occasionally, reposition a object we provide a option to
	 * override its normal pin based positioning. If pin based positioning is false, its positioned by the top left
	 * This is required when loading.<br>
	 * <br>
	 * @param status<br>
	 * @param ByPin<br> - should it be positioned by pin or not? (normally true, but should be false when loading from a state)
	 */
	@Override
	public void setVisible(boolean status,boolean byPin) {
		Log.info("setting div visible"+status);
		
		if (objectsCurrentState.currentlyVisible==status){
			Log.info("already have visibility set to:"+status+" so we do nothing");
			return;
		}
		
		if (status) {
			//recheck position is safe if its not in a div and already attached
			if (this.objectsCurrentState.attachToHTMLDiv.length()<1 && this.isAttached()){
				if (this.objectsCurrentState.positionedRelativeToo!=null){
					//only run positioning to recheck it if restrict to screen is on
					if (this.getObjectsCurrentState().restrictPositionToScreen){
						Log.info("resetting relative position");
						this.updateRelativePosition(true);
					}
				} else {
					//only run positioning to recheck it if restrict to screen is on
					if (getObjectsCurrentState().restrictPositionToScreen){					
						Log.info("resetting position in order to re-check restrict to screen");
						thisobject.setPosition(objectsCurrentState.X, objectsCurrentState.Y, objectsCurrentState.Z, false, false,true); //cant think of a reason why bypin should ever be true here
						//we are positioning
					}
				}

			}

		}

		//objectsCurrentState.currentlyVisible = status; //redundant? the same thing is done in the super below (eventually)w
		super.setVisible(status, byPin);


	}
	@Override
	public void restoreTempState() {
		//this.updateState(tempObjectData, false,true);
		this.updateState(this.getTempState(), false,true);
	}

	/**
	 * sets the initial state to the supplied state, the state should match this objects type <br>
	 * as a untested cast is used<br>
	 * @param objectsInitialState
	 */
	@Override
	protected void setObjectsInitialState(SceneObjectState objectsInitialState) {		
		super.setObjectsInitialState(objectsInitialState);
		initialObjectState=(SceneDivObjectState) objectsInitialState;
	}

	/**
	 * Looks if the style class is set on the object <br>
	 * @param classToCheckFor
	 * @return
	 */
	public boolean hasClass(String classToCheckFor) {

		String allStyles = " "+this.getStyleName()+" "; //this ensures all styles are seperated by spaces

		return allStyles.contains(" "+classToCheckFor+" ");

	}


	@Override
	public void initCurrentState(boolean runOnload) {
		// TODO anything needed to make the visuals match the state of the object
	}


	

}
