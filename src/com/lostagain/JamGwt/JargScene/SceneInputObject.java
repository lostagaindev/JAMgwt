package com.lostagain.JamGwt.JargScene;

import java.util.HashSet;
import java.util.logging.Logger;

import com.darkflame.client.semantic.SSSProperty;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextArea;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneInputObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneInputObject;

/** 
 * A input Object is an extremely simple object; A text area 
 * It will auto-update its "current text" variable as its contents change.
 * 
 * Aside from that it will just have standard DIV object functions too.
 * **/

public class SceneInputObject extends SceneDivObject implements IsSceneInputObject {
	public static Logger Log = Logger.getLogger("JAM.SceneInputObject");
	
	

	/**
	 * stores the current object state. ALL object interactions should update
	 * this, or read from this
	 * 
	 * Note; It shadows objectsCurrentState in the superclass
	 * This means while "objectsCurrentState" points to the same data, when its accessed
	 * from SceneInputObject it will have any extra fields specific to InputObjects	 * 
	 **/
	private SceneInputObjectState objectsCurrentState;

	// ----------------------------------------------------------------------------
//	public SceneInputObjectState defaultObjectState; 
//	public SceneInputObjectState tempObjectData;

	// this object
	IsSceneInputObject thisObject = this;

	//default data:
	//	private String ObjectsName; // default name only. Its just blank Ditto for all the fields below
	//	
	//	// title
	//	private String Title = ""; //
	//
	//	// location (default/starting location)
	//	private int defaultX;//
	//	private int defaultY;//

	//default size (none specified)
	//int defaultSizeX=-1;
	//int defaultSizeY=-1;


	// location (default pin location)
	//int PinPointX=0;//
	//int PinPointY=0;//

	//	
	//	private int ZIndex; //
	//	
	//
	//	
	//	/** default movement state **/
	//	private movementState dmove = new movementState();
	//	
	//	private PropertySet objectsProperties = new PropertySet(); //defaults only, real data is set in currentObjectState
	//	private VariableSet objectsRuntimeVariables = new VariableSet();

	// css name
	//private String css = "";


	// ----------------------------------------------------------------------------

	TextArea inputBox;

	/**
	 * The name of the variable that stores the text in this inputobject
	 * That is this.objectsRuntimeVariables will contain a variable called "currentinput" which constantly updates
	 * to what's typed in this box
	 */
	private static final String CURRENTINPUT = "currentinput";




	/** Determines if the object is is moving **/
	//public boolean isMoving = false;


	// UNDO DATA STORAGE

	/** stores the current and all previous animation states for this object **/
	// Stack<String> previousStates = new Stack<String>();
	public SceneInputObject(SceneObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {
		this(new SceneInputObjectState(newobjectdata),actions,sceneItsOn ); 		
		
	}

	/**
	 * SceneInput object.
	 * 
	 * Inheritance;
	 * SceneObject<<SceneObjectVisual<<SceneDivObject<<SceneInputObject
	 * 
	 * We are putting DivObject inbetween all object types as ALL gwt widgets are Div/html elements and thus they should all get those functions
	 * Additionally, Div might be renamed to be more explicit	 * 
	 * 
	 * @param newobjectdata
	 * @param objectsCurrentStateString
	 * @param sceneItsOne
	 */
	public SceneInputObject(SceneInputObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {

		//by passing the data though to the constructor straight away
		//we allow it to handle the action and parameter settings itself.
		//It can separate of what it needs from the objectsCurrentStateString, and put it into the newly created SceneInputObjectState	
		super((newobjectdata),actions,sceneItsOn, new TextArea() ,false ); 		
		inputBox = (TextArea) super.widgetContents; //assign contents as we know it will match what we just passed to tyhe super constructor
		
		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
		//and also copys the super.objectsCurrentState to their own (type specific) objectsCurrentState object
	//	assignObjectTypeSpecificParameters();//newobjectdata.ObjectsParamatersFromFile); //cant be run from superconstructor apparently due to the subclass not being made yet
		objectsCurrentState = (SceneInputObjectState) super.getObjectsCurrentState();

		// save initial state
		Log.info("_________creating input data object________________");

	//	objectsDefaultState = objectsCurrentState.copy();

		
		
		//associate this initial state with the parent types variable too
		//super.getObjectsInitialState() = objectsDefaultState;
	//	setObjectsInitialState(objectsCurrentState.copy());
		
		
		
		
		
		// create html object from data
		setupWidget(objectsCurrentState);

		//startLoadingCmap();

		Log.info("_________adding input object to object list: "+ objectsCurrentState.ObjectsName.toLowerCase());


		// add to object list
		//SceneObjectDatabase.all_input_objects.put(
		//		objectsCurrentState.ObjectsName.toLowerCase(), this);
		
	//	SceneObjectDatabase.addInputObjectToDatabase(this);

		
	//	SceneObjectDatabase.addObjectToDatabase(this,SceneObjectType.Input);
		
		boolean finalise = true; //theres no subtypes of input object yet
		if (finalise){
			initialiseAndAddToDatabase();
		}
		
	}


	//Below now used (not sure why its here? Cloning maybe? old method?)
	/*
	public SceneInputObject(SceneInputObjectState data, SceneWidgetVisual sceneItsOne) {

		super.objectsCurrentState = objectsCurrentState;

		objectsInitialState =  data.copy();
		objectsCurrentState =  data;


		//associate this default with the parent types variable too
		super.objectsInitialState = objectsInitialState;


		setupWidget(sceneItsOne);
	}*/


	/**
	 * Runs the specific setup for a Input object using information from its currentState object.
	 * 
	 * @param data
	 * @param sceneItsOne
	 */
	protected void setupWidget(SceneInputObjectState data) {//,final SceneWidgetVisual sceneItsOne
		super.setupWidget(data);
		
		//should run super setups first?	
				
		//super.setObjectsSceneVariable(sceneItsOne);

		//objectsCurrentState = data;

		Log.info("__________________________________________________setting up input object");

		//set style if one is specified
		if (objectsCurrentState.CurrentBoxCSS.length()>0){	

			super.setStylePrimaryName(objectsCurrentState.CurrentBoxCSS);


		}

		if (objectsCurrentState.BackgroundString.length()>2)
		{
			super.getElement().getStyle().setProperty("background",objectsCurrentState.BackgroundString);

		}

		// create animation timer
		//createAnimationTimer(); (no longer needs to be setup

		Log.info("starting to setup input widget...");

		super.setWidget(inputBox);

		//set size
		super.setSize(objectsCurrentState.sizeX, objectsCurrentState.sizeY);

		inputBox.setSize("100%", "100%");
		inputBox.getElement().getStyle().setProperty("maxWidth", 100, Unit.PCT);
		inputBox.getElement().getStyle().setProperty("maxHeight", 100, Unit.PCT);

		//only 1 line for now
		inputBox.setVisibleLines(1);


		//We ignore key press's in the game when the person is typing in this box
		inputBox.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				JAMcore.setIgnoreKeyPresses(true);


			}
		});

		inputBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				JAMcore.setIgnoreKeyPresses(false);

			}
		});
		//---------------------------------

		//When the typing changes we update its variable

		Log.info("Maxcharacters setting to:"+objectsCurrentState.maxcharacters);
		inputBox.addKeyUpHandler( new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				String text = inputBox.getText();

				if (text.length()> objectsCurrentState.maxcharacters){
					text = text.substring(0, objectsCurrentState.maxcharacters);
					inputBox.setText(text);					 
				}

				objectsCurrentState.ObjectRuntimeVariables.put(CURRENTINPUT, text);

				//if there's an inspector open we update
				if (oipu!=null){
					oipu.update();
				}
			}
		});

		


	//	startLoadingMovementAnimations();

		// assign generic handlers
		//probably should be refractoed into SceneObject (that is, this ones superclass)
/*
		sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
				| Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
				| Event.ONTOUCHSTART | Event.ONTOUCHEND | Event.ONTOUCHCANCEL|Event.ONTOUCHMOVE);
*/
	}

	/** 
	 * This sets the CSS style.
	 * To explain this further; This sets the css style 
	 *
	public void setCSS(String primaryName){
		if (primaryName.length()>0){
			inputBox.setStylePrimaryName(primaryName);
		}
	}
 **/

	// on attach run onLoad event
	/*
	@Override
	public void onLoad() {
		super.onLoad();
		Log.info("__________onLoad actions:");
		//ok...deep breath....we trigger the OnFirstLoad actions when there is actions to run, and this hasnt already been loaded , and we are not loading the scene this object is in silently!
/*
		if (actionsToRunOnFirstLoad != null && alreadyLoaded == false && !this.getScene().loadingsilently) {
			InstructionProcessor.lastInputObjectUpdated = this;

			InstructionProcessor.processInstructions(
					actionsToRunOnFirstLoad.CommandsInSet.getActions(), "fl_"
							+ this.objectsCurrentState.ObjectsName, this);
		}
		Log.info("__________onLoad actions ran");
		alreadyLoaded = true;
	}*/

	//Override any other clear functions because those will remove the textarea from its container
	//This is because this object, like all SceneObjects that ultimately extend SceneObjectVisual, is really a focus panel 
	//running clear on the focus panel would remove the input box from it
	//Instead we just want to clear the text from the inputBox which is within that focus panel!
	
	/**
	 * clears the text contents
	 */
	@Override
	public void clear(){
		inputBox.setText("");
	}

	/**
	 * updates the object to match the specified object state data, any nulls in
	 * the new data will result in the old data for those variables being kept.
	 * 
	 * NOTE: we copy the values into the current state object. We don't reassign the object.
	 * This is so the newState passed in remains unchanged regardless of what happens.
	 *
	 **/	
	//with a default as true
	//Specifically do this to SceneDivObject.updateState, so it can be called from SceneInput.updateState object once Input inherits Div.
	public void updateState(final SceneInputObjectState newState,final boolean runOnload,final boolean repositionObjectsRelativeToThis) {
		
		Log.info("setting input objects	 data to newState");
		Log.info("__|" + newState.serialiseToString() + "|__");

		//general update stuff (this updates all the universal data to update)
		super.updateState(newState,false,false);

		// remove contents, if any
		this.clear();			
		
		//Below should  be updates specific to InputObject
		//TODO: Once Input inherits div, we should fire DIVs update function to handle all generic + div specific stuff
		//Before getting to get input specific stuff
		//We do, however, still handle the onLoad as that has to be done last.

		//NOTE: This should be adapted for a general SceneObject style system. 
		//(does it need to be applied to the inner div?)
		if (newState.CurrentBoxCSS.length()>0){
			inputBox.setStylePrimaryName(newState.CurrentBoxCSS);
		}

		if (newState.BackgroundString.length()>2)
		{
			super.getElement().getStyle().setProperty("background",objectsCurrentState.BackgroundString);

		}

		
		//INPUT SPECIFIC State settings;
		objectsCurrentState.ReadOnly=newState.ReadOnly;
		
		//set if its read only or not
		inputBox.setReadOnly(objectsCurrentState.ReadOnly);
		
		
		//We can restore the objects text from its objectvariable "currenttext" parameter
		//This saves us needing another variable in the state
		String currentInputValue = objectsCurrentState.ObjectRuntimeVariables.getVariable(CURRENTINPUT);
		if (currentInputValue!=null){
			inputBox.setText(objectsCurrentState.ObjectRuntimeVariables.getVariable(CURRENTINPUT));
		}
		//-------------------------------------
		
		
		

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
			this.onLoad(); //TODO: I think we need to make this optional like the super statements do
		}

		//The super here will refer to "SceneDivObjects.reset()" rather then SceneObjectVisual.reset()
		//This will cause problems as we dont want reset running updateState and onload again!
		//To solve this resetobject will need to take a boolean to determine if it does those actions or not
		super.resetObject(false,false);

	}
	
	
	
	
/*
	@Override
	public void saveTempState() {
		super.saveTempState();
		tempObjectData = (SceneInputObjectState) super.tempObjectData;
		
		//Log.info("saving input object state");
		//tempObjectData = objectsCurrentState.copy();
		//Log.info("Serialized to:" + tempObjectData.serialiseToString());

	}*/
	
/*
	@Override
	public void setVisible(boolean status) {
		Log.info("setting input visible");

		if (status) {

			//recheck position is safe if its not in a div
			if (this.objectsCurrentState.attachToHTMLDiv.length()<1 && this.isAttached()){

				if (this.objectsCurrentState.positionedRelativeToo!=null){
					this.updateRelativePosition();
				} else {
					thisobject.setPosition(objectsCurrentState.X, objectsCurrentState.Y, true, false);

				}
			}





		}

		objectsCurrentState.currentlyVisible = status;
		super.setVisible(status);

	}
	*/
	/*
	@Override	
	public void restoreTempState() {
		this.updateState(tempObjectData, false,true);
	}

	*/
	
	
	/**
	 * The parameters specific to SceneInputObjects get processed here from the supplied parameter string.
	 * This should be triggered straight after (super) in the constructor
	 */
//	@Override
//	void assignObjectTypeSpecificParameters() { //String itemslines[]
//		
//		//ensure not already setup
//		if (objectsCurrentState!=null){
//			Log.info("(parameters for input already set up)");
//			return;
//		}
//		
//		//super needs to be run before 	objectsCurrentState can be assigned
//		super.assignObjectTypeSpecificParameters(); //itemslines
//		objectsCurrentState = (SceneInputObjectState) super.getObjectsCurrentState();
//
//		//Log.info("(assigning assignObjectTypeSpecificParameters for input object)");
//		//ObjectsLog.info("(assigning input ObjectTypeSpecificParameters to object)");
//			
//		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
//		//and also makes a reference to the super.objectsCurrentState to their own (type specific) objectsCurrentState object in order to do this
//		//objectsCurrentState = (SceneInputObjectState) super.objectsCurrentState; //make the local type specific objectsCurrentState referance the superclasses
//
//		//if (itemslines==null){
//		//	Log.info("(no parameters to assign");
//		//	return;			
//		//}
//		
//		//Now triggered from sceneobject;
//	//	objectsCurrentState.assignObjectTypeSpecificParameters(itemslines); 
//		
//		//But we cant as maxcharacters is currently not on the state object
//		
//		//loop over the itemline array
////		int currentlinenum = 0;
////		while (currentlinenum < itemslines.length) {
////			//trim current line
////			String currentline = itemslines[currentlinenum].trim();
////			currentlinenum++;
////			//skip if comment or empty
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
////			
////			if (param.equalsIgnoreCase("MaxCharacters")) {
////
////				objectsCurrentState.maxcharacters = Integer.parseInt(value);
////				Log.info("maxcharacters set to:"+objectsCurrentState.maxcharacters);
////
////			}
////
////
////		}
//		Log.info("done loading input specific params");
//		
//	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.IsSceneInputObject#setReadOnly(java.lang.Boolean)
	 */
	@Override
	public void setReadOnly(Boolean value) {
		
		//update the state to reflect this change
		this.objectsCurrentState.ReadOnly=value;
		//update the box
		this.inputBox.setReadOnly(value);
		
	}


	/**
	 * sets the initial state to the supplied state, the state should match this objects type 
	 * as a untested cast is used
	 * @param objectsInitialState
	 */
	@Override
	protected void setObjectsInitialState(SceneObjectState objectsInitialState) {		
		super.setObjectsInitialState(objectsInitialState);
		initialObjectState=(SceneInputObjectState) objectsInitialState;
	}

	
	/**
	 * @return the objectsCurrentState
	 */
	@Override
	public SceneInputObjectState getObjectsCurrentState() {
		return (SceneInputObjectState)objectsCurrentState;
	}
	public SceneInputObjectState getInitialState() {
		return (SceneInputObjectState)initialObjectState;
	}

	public SceneInputObjectState getTempState() {
		return (SceneInputObjectState)tempObjectState;
	}

}
