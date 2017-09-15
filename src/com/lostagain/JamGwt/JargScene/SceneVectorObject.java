package com.lostagain.JamGwt.JargScene;

import java.util.logging.Logger;

import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackError;
import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackRunnable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.SceneVectorObjectState;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneVectorObject;
import com.lostagain.JamGwt.GwtFileManagerImp;

/** A Vector object allows the creation and placement of interactable polygons **/

public class SceneVectorObject extends SceneDivObject implements IsSceneVectorObject { //SceneObjectVisual

	
	public static Logger Log = Logger.getLogger("SceneVectorObject");

	
	/**
	 * stores the current object state. ALL object interactions should update
	 * this, or read from this
	 * 
	 * Note; It shadows objectsCurrentState in the superclass
	 * This means while "objectsCurrentState" points to the same data, when its accessed
	 * from SceneInputObject it will have any extra fields specific to InputObjects	 * 
	 **/
	private SceneVectorObjectState objectsCurrentState; 
	// ----------------------------------------------------------------------------
	//public SceneVectorObjectState defaultObjectState;
	//public SceneVectorObjectState tempObjectData;
	
	// this object
	SceneVectorObject thisObject = this;
	
	
	//default data:
	//private String ObjectsName; // default name only. Its just blank Ditto for all the fields below
	
	// title
	//private String Title = ""; //

	// location (default/starting location)
	//private int defaultX;//
	//private int defaultY;//
	
	//default size (none specified)
	//int defaultSizeX=-1;
	//int defaultSizeY=-1;
	

	// location (default pin location)
	//int PinPointX=0;//
	//int PinPointY=0;//
	
	
	//private int ZIndex; //
	

	
	/** default movement state **/
	//private movementState dmove = new movementState();
	

	// css name
	//private String css = "";


	// ----------------------------------------------------------------------------
	
	//vector contents - the actual object as displayed on the screen.
	//unlike other types, this is really simple; just an empty div with lines
	//FocusPanel vector = new FocusPanel();
	SimplePanel vector; //no need for focus as the super class sceneobjectvisual handles the click handleing
	
	
	
	///** Determines if the object is is moving **/
	//public boolean isMoving = false;


	// UNDO DATA STORAGE
	
	public SceneVectorObject(SceneObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {
		this(new SceneVectorObjectState(newobjectdata),actions,sceneItsOn); 		
		
	}
	
	/** stores the current and all previous animation states for this object **/
	// Stack<String> previousStates = new Stack<String>();

	public SceneVectorObject(SceneVectorObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {
		
		
		//by passing the data though to the constructor straight away
				//we allow it to handle the action and parameter settings itself.
				//It can separate of what it needs from the objectsCurrentStateString, and put it into the newly created SceneVectorObjectState
				//
				//In order to process parameters specific to SceneInputObject, however, it triggers assignObjectTypeSpecificParameterscommand
				//which this class implements. It has to do this because SceneObjectVisual knows nothing about this class itself! 
				//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
				//and also copys the super.objectsCurrentState to their own (type specific) objectsCurrentState object
	//	super(new SceneVectorObjectState(newobjectdata),objectsCurrentStateString,sceneItsOn);
		
		super((newobjectdata),actions,sceneItsOn, new SimplePanel(),false ); 		
		
		vector = (SimplePanel) super.widgetContents; //(when we change the supertype to div, use this line to get the correct widget assignment
		
		//assignObjectTypeSpecificParameters();//newobjectdata.ObjectsParamatersFromFile); //cant be done from super()
		objectsCurrentState = (SceneVectorObjectState) super.getObjectsCurrentState(); //make the local type specific objectsCurrentState referance the superclasses
		
		//objectsCurrentState =  new SceneVectorObjectState(newobjectdata);
		//super.objectsCurrentState = objectsCurrentState;
		

		//update the generic data common to all objects to match whats supplied
		//this command replaces the need for all the commented out stuff below
		//only Vector object specific parameters need to be dealt with in the SceneDialogue file now, reducing redundancy.
		//currentObjectState.setGenericObjectData(newobjectdata);
		
		
		//currentObjectState.Title = newobjectdata.Title;
		//currentObjectState.CurrentBoxCSS = newobjectdata.CurrentBoxCSS;
	//	currentObjectState.BackgroundString= newobjectdata.BackgroundString;
	//	currentObjectState.positionedRelativeToo = newobjectdata.positionedRelativeToo;
	//	currentObjectState.positionedRelativeToPoint = newobjectdata.positionedRelativeToPoint;
	//	currentObjectState.PropagateVisibility = newobjectdata.PropagateVisibility;
	//	currentObjectState.ObjectsSceneName = newobjectdata.ObjectsSceneName;
		
	//	currentObjectState.relX = newobjectdata.relX;
	//	currentObjectState.relY = newobjectdata.relY;		
		

	//	currentObjectState.attachToHTMLDiv = newobjectdata.attachToHTMLDiv;
		
	//	currentObjectState.X = newobjectdata.X;
	//	currentObjectState.Y = newobjectdata.Y;
	//	currentObjectState.restrictPositionToScreen = newobjectdata.restrictPositionToScreen;
	//	currentObjectState.hasAttachmentPointFile = newobjectdata.hasAttachmentPointFile;
	//	currentObjectState.hasMovementFile = newobjectdata.hasMovementFile;
	
		
		
	//	currentObjectState.currentType = newobjectdata.currentType;
	//	currentObjectState.zindex = newobjectdata.zindex;
		
		objectsCurrentState.zindex  = newobjectdata.zindex; //why duplicate of whats in currentObjectState?
		
	//	currentObjectState.stepZindex = newobjectdata.stepZindex;
	//	currentObjectState.upperZindex = newobjectdata.upperZindex;
	//	currentObjectState.lowerZindex = newobjectdata.lowerZindex;
		
	//	currentObjectState.linkedZindex = newobjectdata.linkedZindex;
	//	currentObjectState.linkedZindexDifference = newobjectdata.linkedZindexDifference;
		

	//	currentObjectState.moveState = newobjectdata.moveState;
		//currentObjectState.objectsProperties = newobjectdata.objectsProperties;

		//currentObjectState.moveState.movement_currentX = currentObjectState.X;
		//currentObjectState.moveState.movement_currentY = currentObjectState.Y;
		// ignore scene actions by default
		//ignoreSceneActions = true;
/*
		// extract data
		Log.info("extracting vector data from:\r" + objectsCurrentStateString);

		// split actions off if present
		// get scene parameters (anything before a line with : in it)
		int firstLocOfColon = objectsCurrentStateString.indexOf(':');

		Log.info("LocOfColon recieved for vector object:" + firstLocOfColon);

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
		
		assignObjectTypeSpecificParameters(Parameters);
		
		// add the actions
		if (allactions.length() > 0) {
			//Log.info("actions are:" + allactions);

			objectsActions = new ActionList(allactions);
			updateHandlersToMatchActions();

		}
*/
		// set defaults:
		/**
		 * DefaultObjectsFileName = ObjectsFileName; DefaultObjectURL =
		 * ObjectsURL; DefaultObjectName = ObjectsName; DefaultTitle = Title;
		 * DefaultX = X; DefaultY = Y; DefaultZIndex = ZIndex;
		 * DefaultobjectsProperties = (HashSet<String>)
		 * objectsProperties.clone(); Defaulttouching = (HashSet<String>)
		 * touching.clone();; defaultcurrentNumberOfFrames =
		 * currentNumberOfFrames;
		 **/

		Log.info("_________creating initial vector data object________________");
				
		//defaultObjectState =objectsCurrentState.copy();
				
		//associate this default with the parent types variable too
	//	super.getObjectsInitialState() = defaultObjectState;
		
		
	//	setObjectsInitialState(objectsCurrentState.copy());
		
		
		
		
		// Log.info("_________serialisation test________________");
		// String serialised = defaultObjectData.serialiseToString();
		// defaultObjectData.deserialise(serialised);
		// Log.info("_________serialisation test done________________");
		// Log.info("_________________________Objects url set to:"
		// + currentObjectState.ObjectsURL);

		// create object from data
		setupWidget(objectsCurrentState);//sceneItsOn
	//	startLoadingCmap();

		//Log.info("_________adding vector object to object list: "+ objectsCurrentState.ObjectsName.toLowerCase());

		ObjectsLog.logTimer("setup vector took;");
		
		// add to object list
	//	SceneObjectDatabase.all_vector_objects.put(objectsCurrentState.ObjectsName.toLowerCase(), this);

	//	SceneObjectDatabase.addObjectToDatabase(this,SceneObjectType.Vector);
		boolean finalise = true; //theres no subtypes of vector object yet
		if (finalise){
			initialiseAndAddToDatabase();
		}
	
		//SceneObjectDatabase.addVectorObjectToDatabase(this);
	}

/*
	public SceneVectorObject(SceneVectorObjectState data, SceneWidgetVisual sceneItsOn) {
		super(null,null,sceneItsOn);
		
		super.objectsCurrentState = objectsCurrentState;

	//	defaultObjectState =  data.copy();
		//associate this default with the parent types variable too
		//super.getObjectsInitialState() = defaultObjectState;
		setObjectsInitialState(data.copy());
		
		
		setupWidget(data, sceneItsOn);
	}
*/
	
	private void setupWidget(SceneVectorObjectState data) {
		super.setupWidget(data);
		//
		//objectScene = sceneItsOne;
		//super.setObjectsSceneVariable(sceneItsOne);
		objectsCurrentState = data;


		//all the common things to setup are done in SceneObject
		//super.setupWidget();
		
		Log.info("___________________________________________________________________creating vector object");
	
		//set style if one is specified
	//	if (currentObjectState.CurrentBoxCSS.length()>0){
			
		//	super.setStylePrimaryName(currentObjectState.CurrentBoxCSS);
			
			
		//}
		
		if (objectsCurrentState.BackgroundString.length()>2)
		{
			super.getElement().getStyle().setProperty("background",objectsCurrentState.BackgroundString);
			
		}

		if (objectsCurrentState.objectsVectorSourceURL != null && objectsCurrentState.objectsVectorSourceURL.length()>3 ) {			
			Log.info("loading vectors from url");			
			loadVectorFromURL(objectsCurrentState.objectsVectorSourceURL);
		} else {
			setVectorString(objectsCurrentState.objectsVectorString);
			Log.info("loading vectors from string instead");
			Log.info("the Vector String is:"+objectsCurrentState.objectsVectorString);
		}
		
		// create animation timer
	//	createAnimationTimer();
		
		Log.info("starting to setup vector widget...");
		
		//super.setWidget(vector); //redundant? super does this
		
		//set size
		super.setSize(objectsCurrentState.sizeX, objectsCurrentState.sizeY);
		
		
		
	//	startLoadingMovementAnimations();

		// assign generic handlers
		//probably should be refractoed into SceneObject (that is, this ones superclass)
		
		//sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
		//		| Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
		//		| Event.ONTOUCHSTART | Event.ONTOUCHEND | Event.ONTOUCHCANCEL|Event.ONTOUCHMOVE);

	}

	private void loadVectorFromURL(String objectsCurrentURL) {
		
		// JAM.Quality.equalsIgnoreCase("debug")
		if (JAMcore.DebugMode){
			
			ObjectsLog.log("Text Set To;(loading text from file:"+objectsCurrentURL+")","green");
		} else {
			
			ObjectsLog.log("Text Set To;...","green");	
		}
		
		//set what to do when we get the text data retrieved
		FileCallbackRunnable onResponse = new FileCallbackRunnable(){

			@Override
			public void run(String responseData, int responseCode) {

				String vstring = responseData;
						

				// check response is not an error
				if (responseCode >= 400 || responseCode == 204) {
					Log.info("________no text file recieved ("+responseCode+"):\n");

					return;
				}
				
				thisObject.setVectorString(vstring);

			
			}
			
		};
		
		//what to do if theres an error
		FileCallbackError onError = new FileCallbackError(){

			@Override
			public void run(String errorData, Throwable exception) {

				thisObject.setVectorString(exception.getLocalizedMessage());
			}
			
		};
		
	
		//text directory? is that right?
		RequiredImplementations.getFileManager().getText(SceneWidget.SceneFileRoot
				+ getParentScene().SceneFileName + "/Text/" + objectsCurrentURL,  
				true,
				onResponse,
				onError,
				false);

		
	}


	protected void setVectorString(String vstring) {
		if (vstring.startsWith("<")){
			getElement().setInnerHTML("<svg width=\"100%\" height=\"100%\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">"+vstring+"</svg>");
		} else {
			getElement().setInnerHTML("<svg width=\"100%\" height=\"100%\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">"+"<path d=\""+vstring+"\"/>"+"</svg>");		
		}
		
	}

	public void setNewVectorString(String vstring) {
		
		setVectorString(vstring);
		objectsCurrentState.objectsVectorString = vstring;
	}
	
/*
	/** This sets the CSS style.
	 * To explain this further; This sets the css style 
	public void setCSS(String primaryName){
		if (primaryName.length()>0){
			vector.setStylePrimaryName(primaryName);
		}
	}
	
*/
	//WHY THE HELL WOULD WE WANT THE FOLLOWING? Commented out because it was stupid
	/** fills the vector with the specified widget 
	public void setVectorsWidget(Widget widget){
		vector.setWidget(widget);
	}
	**/
	
	// on attach run onLoad animation
	/*
	@Override
	public void onLoad() {
		super.onLoad();
		Log.info("__________onLoad actions:");
		/*
		//ok...deep breath....we trigger the OnFirstLoad actions when there is actions to run, and this hasnt already been loaded , and we are not loading the scene this object is in silently!
		if (actionsToRunOnFirstLoad != null && alreadyLoaded == false && !this.getScene().loadingsilently) {
			InstructionProcessor.lastVectorObjectUpdated = this;

			InstructionProcessor.processInstructions(
					actionsToRunOnFirstLoad.CommandsInSet.getActions(), "fl_"
							+ this.objectsCurrentState.ObjectsName, this);
		}
		Log.info("__________onLoad actions ran");
		alreadyLoaded = true;
	}*/

	/**
	 * updates the object to match the specified object state data, any nulls in
	 * the new data will result in the old data for those variables being kept
	 **/
	public void updateState(final SceneVectorObjectState newState,
			final boolean runOnload,final boolean repositionObjectsRelativeToThis) {

		Log.info("setting vector objects data to newState");

		Log.info("__|" + newState.serialiseToString() + "|__");
		
		//general update stuff (this updates all the universal data to update)
		super.updateState(newState,false,false);

		// remove contents, if any
		//Log.info("running clear on vector but probably shouldnt");

		//this.clear();				//hu? hows this not break things


		//general update stuff
		//updateState(newState);
		
		
		/* ~~~~~(moved to updateStateInternal)
		*/
		
		//NOTE: This should be adapted for a general SceneObject style system. 
		//(does it need to be applied to the inner vector?)
		if (newState.CurrentBoxCSS.length()>0){
			
			vector.setStylePrimaryName(newState.CurrentBoxCSS);
		}
		
		if (newState.BackgroundString.length()>2)
		{
			super.getElement().getStyle().setProperty("background",objectsCurrentState.BackgroundString);
			
		}
		//---
		
		if (newState.objectsVectorSourceURL != null && newState.objectsVectorSourceURL.length()>3 ) {			
		//	Log.info("loading vectors from url");			
			loadVectorFromURL(newState.objectsVectorSourceURL);
		} else {
			setVectorString(newState.objectsVectorString);
			//Log.info("loading vectors from string instead");
		//	Log.info("the Vector String is:"+newState.objectsVectorString);
		}
		
		
		
		
		if (runOnload) {

			Log.info("running objects onload again");
			alreadyLoaded = false; //has to be reset to false to run onload again
			onLoad();
		} else {

			Log.info("run onload no specified");
		}
		
		
		Log.info("updateThingsPositionedRelativeToThis:");
		if (repositionObjectsRelativeToThis){
			updateThingsPositionedRelativeToThis(true);
		}
		// --------
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


		Log.info("resetting:"+this.objectsCurrentState.ObjectsName);
				
		if (reRunUpdateState){
			updateState(getInitialState(), false,true);
		}
		
		if (reOnLoad){
			alreadyLoaded = false;
			Log.info("running objects onload again");
			this.onLoad();
		}
		//The super here will refer to "SceneDivObjects.reset()" rather then SceneObjectVisual.reset()
		//This will cause problems as we dont want reset running updateState and onload again!
		//To solve this resetobject will need to take a boolean to determine if it does those actions or not
		super.resetObject(false,false);


	}
/*
	@Override
	public void saveTempState() {

		Log.info("saving vector object");

		tempObjectData = objectsCurrentState.copy();
				
		
		 Log.info("seralised to:" + tempObjectData.serialiseToString());

	}

	@Override
	public void setVisible(boolean status) {


		Log.info("setting vector visible");
		
		if (status) {
			
			//recheck position is safe if its not in a html div and already attached
			if (this.objectsCurrentState.attachToHTMLDiv.length()<1 && this.isAttached()){
			
			if (this.objectsCurrentState.positionedRelativeToo!=null){
				Log.info("resetting relative position");
				this.updateRelativePosition();
			} else {

				Log.info("resetting position");
				thisobject.setPosition(objectsCurrentState.X, objectsCurrentState.Y, true, false);
				
			}
			
			}
			
			
			
			
			
		}
		
		objectsCurrentState.currentlyVisible = status;
		super.setVisible(status);


	}
	@Override
	public void restoreTempState() {

		this.updateState(tempObjectData, false);

	}
	*/
	/**
	 * sets the intial state to the supplied state, the state should match this objects type 
	 * as a untested cast is used
	 * @param objectsInitialState
	 */
	@Override
	protected void setObjectsInitialState(SceneObjectState objectsInitialState) {		
		super.setObjectsInitialState(objectsInitialState);
		initialObjectState=(SceneVectorObjectState) objectsInitialState;
	}


	
	private boolean paramsSetup = false;
	
	/**
	 * @return the objectsCurrentState as a SceneVectorObjectState
	 */
	@Override
	public SceneVectorObjectState getObjectsCurrentState() {
		return (SceneVectorObjectState)objectsCurrentState;
	}
	
	public SceneVectorObjectState getInitialState() {
		return (SceneVectorObjectState)initialObjectState;
	}	
	
	public SceneVectorObjectState getTempState() {
		return (SceneVectorObjectState)tempObjectState;
	}

	@Override
	public String getCurrentVectorString() {
		return 	objectsCurrentState.objectsVectorString;
	}
	
	
	
//	@Override
//	void assignObjectTypeSpecificParameters() { //String itemslines[]
//		if (objectsCurrentState!=null){
//			Log.info("(vector parameters already set up)");
//			return;
//		}
//		super.assignObjectTypeSpecificParameters();  //itemslines
//		//call this function for each layer of the class hyrachy
//		//we could still, however, put in some sort of check to ensure its only loaded once?
//		Log.info("(assignObjectTypeSpecificParameters for vector object");
//		ObjectsLog.info("__assigning sprite ObjectTypeSpecificParameters to object___"+paramsSetup);
//		
//	
//		
//		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
//		//and also makes a reference to the super.objectsCurrentState to their own (type specific) objectsCurrentState object in order to do this
//		objectsCurrentState = (SceneVectorObjectState) super.getObjectsCurrentState(); //make the local type specific objectsCurrentState referance the superclasses
//		
//		//if (itemslines==null){
//		//	Log.info("(no parameters to assign");
//		//	return;
//		//	
//	//	}
//		
//		//Now triggered from sceneobject
//		//objectsCurrentState.assignObjectTypeSpecificParameters(itemslines);
//
//		
////		
////		// now split again by new lines to get parameter data
////				//String itemslines[] = Parameters.split("\n");
////				int currentlinenum = 0;
////				while (currentlinenum < itemslines.length) {
////
////					String currentline = itemslines[currentlinenum].trim();
////					currentlinenum++;
////					if ((currentline.length() < 2) || (currentline.startsWith("//"))) {
////						continue;
////					}
////
////					Log.info("Processing line:"+currentline);
////					
////					
////					//Split by =
////					String param = currentline.split("=",2)[0].trim();
////					String value = currentline.split("=",2)[1].trim();
////								
////					if (param.equalsIgnoreCase("DefaultURL")) {
////
////						objectsCurrentState.objectsVectorSourceURL = value;
////
////						Log.info("Objects ObjectsCurrentURL set to:"
////								+ objectsCurrentState.objectsVectorSourceURL);
////					}
////
////					if (param.equalsIgnoreCase("VectorString")) { 
////						
////						objectsCurrentState.objectsVectorString = value;
////
////						Log.info("Objects ObjectsCurrentText set to:"
////								+ objectsCurrentState.objectsVectorString);
////					}
////				
////
////				}
//				Log.info("done loading vector specific params");
//				paramsSetup=true;
//	}

}
