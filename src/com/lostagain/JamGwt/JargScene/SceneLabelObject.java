package com.lostagain.JamGwt.JargScene;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.GameManagementClass;
import com.lostagain.Jam.GameVariableManagement;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.InstructionProcessing.ActionSet.TriggerType;
import com.lostagain.Jam.InstructionProcessing.CommandList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneLabelObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneLabelObject;
import com.lostagain.Jam.SceneObjects.SceneLabelObjectState.cursorVisibleOptions;
import com.lostagain.Jam.SceneObjects.SceneObjectState.TrueFalseOrDefault;
import com.lostagain.JamGwt.TypedLabel;

/** 
 * A SceneLabelObject is a object designed for showing pieces of text 
 * Dialogue object will extend this to allow showing large amounts of text from files, as well as flicking
 * between different paragraphs of text 
 ***/

public class SceneLabelObject extends SceneDivObject implements IsSceneLabelObject { 

	public static Logger Log = Logger.getLogger("JAM.SceneLabelObject");



	/**
	 * stores the current default object state. ALL object interactions should update
	 * this, or read from this
	 **/
	private SceneLabelObjectState objectsCurrentState;

	/** The default state of an object is how its specified in the file.
	 * Or at the time it was first created. <br>
	 * **/
	//	public SceneDialogObjectState defaultObjectState;


	/** a temp state of the object.
	 * When fully implemented - which it almost is - we can use this for
	 * a fast save/restore function.
	 * So, for example, on entering a scene it saves, and when the user
	 * messs up badly, they can reset**/
	//	public SceneDialogObjectState tempObjectData;


	//bits to make this widget work

	/**the main animated label. The visual part of the object.**/
	public TypedLabel TextLabel;



	/**
	 * a reference to this object. This is basically used to refer
	 * to this objects functions from within annominious inner class's 
	 ***/
	SceneLabelObject thisObject = this;



	/** An object that appears to indicate there is more text to view 
	 * A simple setVisible is used to make this hide/appear depending on if 
	 * hasNextParagraph() is true **/
	SceneObject associatedNextParagraphIcon = null;


	public SceneLabelObject(SceneObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {

		this(new SceneLabelObjectState(newobjectdata), actions, sceneItsOn,  new TypedLabel("").getInternalWidget(),  true);
		
		Log.info("-_-_-oxoxo   created label from generic object state");
		
	}


	public SceneLabelObject(SceneLabelObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOn) {

		this(newobjectdata, actions, sceneItsOn,  new TypedLabel("").getInternalWidget(),  true);

		Log.info("-_-_-oxoxo   created label from specific label object state");
	}

	/**<br>
	 * Creates a new label object.<br>
	 * This constructor method is designed to be used directly only from subclasses based on Label.<br>
	 * For directly making label one of the above methods should be used instead.<br>
	 * <br>
	 * @param newobjectdata -the new objects SceneLabelObjectState<br>
	 * @param actions - any actions it has, if any<br>
	 * @param sceneItsOn - the scene this object is on<br>
	 * @param innerWidget - must be or extend TypedLabel <br>
	 * @param finaliseLoad - if you are calling this from a subclass this should be set to false and the subclass should finalize itself<br>
	 * 
	 */
	protected SceneLabelObject(
			SceneLabelObjectState newobjectdata, 
			ActionList actions,
			SceneWidget sceneItsOn, 
			Widget innerWidget, 
			boolean finaliseLoad) {

		//by passing the data though to the constructor straight away
		//we allow it to handle the action and parameter settings itself.
		//It can separate of what it needs from the objectsCurrentStateString, and put it into the newly created SceneDialogObjectState
		//
		//In order to process parameters specific to SceneDialogObject, however, it triggers assignObjectTypeSpecificParameterscommand
		//which this class implements. It has to do this because SceneObjectVisual knows nothing about this class itself! 
		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
		//and also copys the super.objectsCurrentState to their own (type specific) objectsCurrentState object
		//	super(new SceneDialogObjectState(newobjectdata),objectsCurrentStateString,sceneItsOn);

		super(newobjectdata,actions,sceneItsOn, innerWidget,false ); 		

		TextLabel = ((TypedLabel.HTMLPanelForTypedLabel) super.widgetContents).associatedTypedLabel; //assign contents as we know it will match what we just passed to the super constructor


		//assignObjectTypeSpecificParameters();//newobjectdata.ObjectsParamatersFromFile); //Can't be done from super()
		objectsCurrentState = (SceneLabelObjectState) super.getObjectsCurrentState();


		if (objectsCurrentState==null){
			Log.warning("objectsCurrentState is null");
		}

		objectsCurrentState.zindex = newobjectdata.zindex; //redundant?why do we have it here and in currentObjectState?





		Log.warning("_________creating label object "+objectsCurrentState.ObjectsName+"________________");

		ObjectsLog.info("(creating label object)");

		ObjectsLog.info("currentObjectState visibility:"+objectsCurrentState.currentlyVisible);

		//defaultObjectState = objectsCurrentState.copy();
		//associate this default with the parent types variable too
		//	super.getObjectsInitialState() = defaultObjectState;
		if (finaliseLoad){



			//	setObjectsInitialState(objectsCurrentState.copy());



			//	ObjectsLog.info("objects initial state has visibility:"+getInitialState().currentlyVisible);
			//initialObjectState.setObjectsPrimaryType(objectsCurrentState.getCurrentPrimaryType());

			// Log.info("_________serialisation test________________");
			// String serialised = defaultObjectData.serialiseToString();
			// defaultObjectData.deserialise(Serialized);
			// Log.info("_________serialisation test done________________");
			// Log.info("_________________________Objects url set to:"
			// + currentObjectState.ObjectsURL);

			// create object from data
			setupWidget(objectsCurrentState);

			//	startLoadingCmap();
			initialiseAndAddToDatabase();
		}




		//	Log.info("_________adding label object to object list: "
		//		+ objectsCurrentState.ObjectsName.toLowerCase());

		// add to object list
		//SceneObjectDatabase.all_text_objects.put(
		//		objectsCurrentState.ObjectsName.toLowerCase(), this);


		//SceneObjectDatabase.addTextObjectToDatabase(this);

		//SceneObjectDatabase.addObjectToDatabase(this,SceneObjectType.Label);

		//if (finaliseLoad){
		//	finaliseAndAddToDatabase();
		//}


		ObjectsLog.logTimer("setup label took;");

	}
	/*
	public SceneDialogObject(SceneDialogObjectState data, SceneWidgetVisual sceneItsOn) {

		super(null,null,sceneItsOn);

		super.objectsCurrentState = objectsCurrentState;

		//make a copy of the the state for the default
	//	defaultObjectState = data.copy();
		//ensure the parent type also is using this default state
		//super.getObjectsInitialState() = defaultObjectState;
		setObjectsInitialState(data.copy());


		defaultObjectState.setObjectsPrimaryType(objectsCurrentState.getCurrentPrimaryType());

		setupWidget(data, sceneItsOn);
	}

	 */

	/** Sets the style of the cursor when text is typed**/
	public void setCursorMode(cursorVisibleOptions cursorStyle) {

		TextLabel.setBlink(cursorStyle);
	}

	protected void setupWidget(SceneLabelObjectState state) { //		final SceneWidgetVisual sceneItsOne

		//objectScene = sceneItsOne;
		//super.setObjectsSceneVariable(sceneItsOne);
		objectsCurrentState = state;

		//all the common things to setup are done in SceneObject
		super.setupWidget(objectsCurrentState);

		Log.info("___________________________________________________________________creating label object with text:"+objectsCurrentState.ObjectsCurrentText);
		ObjectsLog.log("Setting up label","orange");

		//	TextLabel = new TypedLabel(data.ObjectsCurrentText);

		//TextLabel.setText(data.ObjectsCurrentText,data.TypedText); 

		if (objectsCurrentState.cursorVisible == cursorVisibleOptions.DEFAULT){
			TextLabel.setBlink(cursorVisibleOptions.WHENTYPING);	
		} else {
			TextLabel.setBlink(objectsCurrentState.cursorVisible);	
		}

		if (!objectsCurrentState.Custom_Key_Beep.isEmpty()){			
			TextLabel.setCUSTOM_KEY_BEEP(objectsCurrentState.Custom_Key_Beep);
		} else {
			Log.info("__________(no custom sound)");
		}

		if (!objectsCurrentState.Custom_Space_Beep.isEmpty()){
			TextLabel.setCUSTOM_SPACEKEY_BEEP(objectsCurrentState.Custom_Space_Beep);
		} else {
			Log.info("__________(no custom sound)");
		}

		TextLabel.setAssociatedObject(this);		


		TextLabel.setRunBeforeTextSet(new Runnable(){
			@Override
			public void run() {			
				runBeforeTextSet();
			}
		});


		TextLabel.setRunAfterTextSet(new Runnable(){
			@Override
			public void run() {
				runAfterTextSet();
			}
		});


		// start loading text if there's a url
		//if (objectsCurrentState.ObjectsCurrentURL != null) {

		//	Log.info("loading text from url");

		//	loadTextFromURL(objectsCurrentState.ObjectsCurrentURL);
		//}
		//
		// set styles
		TextLabel.getInternalWidget().setTitle(state.Title);


		//size should already be in css units
		TextLabel.getInternalWidget().setSize(state.sizeX, state.sizeY);


		Log.info("setting style name to:"+state.CSSname);
		//TextLabel.setStyleName(data.CSSname);
		setTextCSS(state.CSSname);

		//AbsolutePanel internalFrame = new AbsolutePanel();
		//internalFrame.add(TextLabel);

		//int npi_x = data.sizeX-associatedNextParagraphIcon.getOffsetWidth()-10;
		//int npi_y = data.sizeY-associatedNextParagraphIcon.getOffsetHeight()-10;

		//internalFrame.add(associatedNextParagraphIcon,npi_x,npi_y);



		// create animation timer
		//createAnimationTimer();

		
		
		
		Log.info("Text state..typed="+state.TypedText+" text="+state.ObjectsCurrentText);
		
		if (state.ObjectsCurrentText==null){
			Log.warning("text was null on setup, this is probably a mistake  ,setting to zero length string");
			state.ObjectsCurrentText="";
		}


		//
		//Note: we dont set the text physically on the screen untill logical and physical loading is complete.
		//
		//This is because the text could require other objects on the screen (to embed), or even trigger other code while its typeing ((inline commands)
		//"text" can be far more then just static labels in the Jam engine.
		//
	

	
		super.setWidget(TextLabel.getInternalWidget());		

	//	startLoadingMovementAnimations();

	}

	/**
	 * This code runs before any typing ends
	 **/
	protected void runAfterTextSet() {

		Log.info("Triggering any actions for textset.");
		CommandList actionsForTrigger = this.getObjectsActions().getActionsForTrigger(TriggerType.OnTextEnd, null);
		if (actionsForTrigger!=null){

			Log.info("(actions found)");
			InstructionProcessor.processInstructions(actionsForTrigger, "ontextend_"					
					+ this.objectsCurrentState.ObjectsName, this);
		}



	}


	/**
	 * This code runs when any typing starts
	 **/
	protected void runBeforeTextSet() {

		Log.info("Triggering any actions for before textset.");

		CommandList actionsForTrigger = this.getObjectsActions().getActionsForTrigger(TriggerType.OnTextStart, null);
		if (actionsForTrigger!=null){

			Log.info("(actions found)");
			InstructionProcessor.processInstructions(actionsForTrigger, "ontextstart_"					
					+ this.objectsCurrentState.ObjectsName, this);
		}

	}



	/** load a new paragraph text from a file 
	public void loadTextFromURL(String url) {
		// JAM.Quality.equalsIgnoreCase("debug")
		if (JAM.DebugMode){
			TextLabel.setTextNow("(loading text from file:"+url+")",true); //the true disables the sound effect
			ObjectsLog.log("Text Set To;(loading text from file:"+url+")","green");
		} else {
			TextLabel.setTextNow("...",true); //the true disables the sound effect
			ObjectsLog.log("Text Set To;...","green");	
		}

		//set what to do when we get the text data retrieved
		FileCallbackRunnable onResponse = new FileCallbackRunnable(){

			@Override
			public void run(String responseData, int responseCode) {

				// check response is not an error
				if (responseCode == 404) {
					Log.info("________no text file recieved (404):\n");

					return;
				}
				//

				String text = responseData;

				//make sure all file names are language specific				
				text = JAM.parseForLanguageSpecificExtension(text);

				// swap TextIds for text
				text = JAM.parseForTextIDs(text);



				Log.info("splitting too paragraphs:" + text);
				// split into paragraphs
				knownParagraphs = new DialogueCollection(text);

				Log.info("setting current number of paragraphs:");
				Log.info("for "	+ objectsCurrentState.currentparagraphName);

				objectsCurrentState.currentNumberOfParagraphs = knownParagraphs
						.getNumberOfParagraphs(objectsCurrentState.currentparagraphName);

				Log.info("num is "+objectsCurrentState.currentNumberOfParagraphs );

				//is it correct to start from p0?
				objectsCurrentState.currentParagraphPage =0;

				if (objectsCurrentState.currentparagraphName.length()>1){

					//we log the current name and paranumber
					//note; a paranumber is not, like Ghostpi.
					Log.info("setting newly loaded file to paragraph "+objectsCurrentState.currentparagraphName +" with paranumber "+objectsCurrentState.currentParagraphPage);

					ObjectsLog.log("setting newly loaded file to paragraph "+objectsCurrentState.currentparagraphName +" with paranumber "+objectsCurrentState.currentParagraphPage,"blue");

					//set the text to the curenttly set  paragraph
					thisObject.setText(""
							+ knownParagraphs.getText(objectsCurrentState.currentparagraphName, objectsCurrentState.currentParagraphPage));

				} else {
					Log.info("setting newly loaded file to default paragraph:");

					ObjectsLog.log("setting newly loaded file to default paragraph:","blue");

					thisObject.setText(""
							+ knownParagraphs.getText("default", 0));
				}
			}

		};

		//what to do if theres an error
		FileCallbackError onError = new FileCallbackError(){

			@Override
			public void run(String errorData, Throwable exception) {

				thisObject.setText(exception.getLocalizedMessage());
			}

		};

		//using the above, try to get the text!
		FileManager.GetTextFile(SceneWidget.SceneFileRoot
				+ this.getScene().SceneFileName + "/Text/" + url,
				onResponse,
				onError,
				false);



	}**/

	public void setText(String text) {

		//use the default setting to determine if its typed or not
		if (objectsCurrentState.TypedText==TrueFalseOrDefault.TRUE){
			setText(text,true);
		} else {
			setText(text,false); //default to false
		}

	}

	/** 
	 * Sets the current text to a specific string, either straight away, or after being typed.
	 * 
	 * If this object has not yet been attached, the text will be saved and set onload()
	 ***/
	public void setText(String text, boolean Typed) {

		//process for any variables in the text (we might want to provide a option not to run this function)
		text = GameVariableManagement.replaceGameVariablesWithValues(text,this);

		objectsCurrentState.ObjectsCurrentText = text;

		//if (!isAttached()){
		
		//but what if we arnt ready?  everything needs to be both logically loaded and physically too.
		//This is because settext might want to insert elements from the scene, and those need to be attached elsewhere first
		if (   !this.getParentScene().isAllFilesLogicallyLoaded()
			|| !this.getParentScene().isAllFilesPhysicalLoaded() ){
			
			
			String errormsg = getName()+ " was not ready to settext. Both logical and physical loading have to be finnished for a settext to gaurenty to work.\n"
									   + "logical(all files needed on scene):"+ getParentScene().isAllFilesLogicallyLoaded() +" physical (is attached);"+ getParentScene().isAllFilesPhysicalLoaded()+"\n"
									   + "dont panic, however, as the text is remembered and set once loading is fully complete.";
			
			//
			Log.info(errormsg);	
			ObjectsLog(errormsg);			
			///
			wasLastObjectUpdated();
			return;
		} else {
		//	Log.info("waiting to set label object text: "+this.getLoadStatusDebug()); //temp	log
		}
		//-----------------

		boolean disablesound = !(   this.isVisible() 
				                 && this.isAttached()
				                 && this.getParentScene()==SceneObjectDatabase.currentScene); //disable sound should be false unless its invisible or not attached or its scene isnt current
		
		
		if (Typed){
			TextLabel.setText(objectsCurrentState.ObjectsCurrentText ,disablesound);
			ObjectsLog.log("Text Set To;"+text+" sound disabled:"+disablesound+" currently attached:"+isAttached(),"green");
		} else {
			boolean textset = TextLabel.setTextNow(objectsCurrentState.ObjectsCurrentText,disablesound);
			
			if (textset){
				ObjectsLog.log("Text instantly Set To;"+text+" sound disabled:"+disablesound+" currently attached:"+isAttached(),"green");
			}
			
		}

		wasLastObjectUpdated();
		//updateNextIcon();
	}


	
	@Override
	public void onLogicalLoadCompleteForAllObjectsInScene() {
		super.onLogicalLoadCompleteForAllObjectsInScene();		
		
		ObjectsLog("Objects on scene ALL logically loaded At This Point","Orange");
		//we wait for both logical and physical loading because  because setText can potentially trigger anything
		//including needing movement,collision,or glu files. (this is because we have triggers for ontextstart and end)
		//if (super.isLogicalyLoaded() && super.isPhysicallyLoaded()){
		//	setTextForFirstTime();
		//}
	}

	@Override
	public void onLoad() {
		super.onLoad(); //will set 	setAsPhysicallyLoaded();	
		ObjectsLog("Object Attached At This Point","Orange");
		
		//we wait for both logical and physical loading because  because setText can potentially trigger anything
		//including needing movement,collision,or glu files. (this is because we have triggers for ontextstart and end)	
		//if (super.isLogicalyLoaded() && super.isPhysicallyLoaded()){
		//	setTextForFirstTime();
		//}
		
	}


	@Override
	public void onFullyLoadedCompleteForAllObjectsInScene() {
		super.onFullyLoadedCompleteForAllObjectsInScene();
		setTextForFirstTime();
	}



	@Override
	public void initCurrentState(boolean runOnload) {
		super.initCurrentState(runOnload);
		ObjectsLog.log("oxoxo initialising state");
		Log.info("oxoxo initialising state");
		
		//We run this to sync the text.
		//This is not run by default during setup, as it should only fire when all the objects in the scene are both logically and physically loaded.
		//(that is, when onFullyLoadedCompleteForAllObjectsInScene is triggered). This is because the text might require other objects.
		setTextForFirstTime();

	}


	private void setTextForFirstTime() {
		//We set the text here, rather then in setup.
		//By setting it only after its loaded it prevents redundant changes that might happen during loading
		//(for example, the default text set, then overwritten by file loaded text)
		//It also allows all sceneobjects to be know, thus its cleaner for the attachments to work within text, if theres any	
		String objectsCurrentText = this.getObjectsCurrentState().ObjectsCurrentText;
		ObjectsLog.log("Setting text(for first time) to;"+objectsCurrentText+" Typed:"+this.getObjectsCurrentState().TypedText,"green");		
		if (this.getObjectsCurrentState().TypedText == TrueFalseOrDefault.TRUE){
			setText(objectsCurrentText,true);
		} else {
			setText(objectsCurrentText,false); //Labels default to false			
		}
		//--
	}
	



	/** sets the name of the current paragraph.
	 *  If "TriggerNow" is set true, the paragraph will be displayed straight away on its first page 
	public void setParagraphName(String name,boolean TriggerNow) {

		ObjectsLog.log("Setting paragraph to:"+name+" triggernow = "+TriggerNow,"green");

		objectsCurrentState.currentParagraphPage = 0;
		Log.info("paragraph name was "+objectsCurrentState.currentparagraphName+" is now set too "+name);

		objectsCurrentState.currentparagraphName = name;

		//update number of paragraphs if paragraphs are loaded
		//if they arnt loaded we assume its 1 page (this will auto fix to 
		//the correct number when loadeding is finnished)
		if (knownParagraphs!=null){
			objectsCurrentState.currentNumberOfParagraphs = knownParagraphs.getNumberOfParagraphs(objectsCurrentState.currentparagraphName);
		} else {
			objectsCurrentState.currentNumberOfParagraphs = 1;
		}


		Log.info("This new paragraphset has "+objectsCurrentState.currentNumberOfParagraphs+" paragraphs ");

		if (TriggerNow){
			this.setText(""+ knownParagraphs.getText(
					objectsCurrentState.currentparagraphName,
					objectsCurrentState.currentParagraphPage));
		} else {
			Log.info("New paragraphname set for next NextParagraph(), unless a new setURL happens first");

			objectsCurrentState.currentParagraphPage = -1;

			ObjectsLog.info("Paragraph set to:"+objectsCurrentState.currentparagraphName+":"+
					objectsCurrentState.currentParagraphPage+", but not set to appear yet");
		}

		Log.info("paragraph number is set to "+objectsCurrentState.currentParagraphPage);		


		InstructionProcessor.lastTextObjectUpdated = this;

	}**/

	public void setTextCSS(String name) {

		TextLabel.getInternalWidget().setStyleName(name);

		objectsCurrentState.CSSname = TextLabel.getInternalWidget().getStyleName();

	}

	public void addTextCSS(String name) {

		TextLabel.getInternalWidget().addStyleName(name);

		objectsCurrentState.CSSname = TextLabel.getInternalWidget().getStyleName();


	}

	public void removeTextCSS(String name) {

		TextLabel.getInternalWidget().removeStyleName(name);

		objectsCurrentState.CSSname = TextLabel.getInternalWidget().getStyleName();

	}

	/**
	 * Reloads the text data into this dialogue from objectsCurrentState.ObjectsCurrentURL
	 * CURRENTLY UNTESTED (designed to be hocked up to the object inspector with a reload button)

	public void reloadTextFromFile(){
		this.loadTextFromURL(objectsCurrentState.ObjectsCurrentURL);
	}
	 **/
	/*
	public void setURL(String URL) {

		Log.info("setting url on "+objectsCurrentState.ObjectsName+" to "+URL);

		// get url from name
		objectsCurrentState.ObjectsCurrentURL = URL;

		if (objectsCurrentState.ObjectsCurrentURL != null) {
			loadTextFromURL(objectsCurrentState.ObjectsCurrentURL);
		}
		Log.info("Objects url set to:"
				+ objectsCurrentState.ObjectsCurrentURL);

		// update text

		// SceneObjectIcon.setURL(currentObjectState.ObjectsURL,
		// currentObjectState.currentNumberOfFrames);
		InstructionProcessor.lastTextObjectUpdated = this;
	}

	 */

	// Note; as all object types have similar onloads we could move it all into SceneObjects onLoad instead (just after its existing stuff)
	// (removingthe redundant super.onLoad as theres one already there)
	//To do this though we would needsome way to set the correct "last__ObjectUpdated" variable, as that changes based on type
	/*
	@Override
	public void onLoad() {
			//None of this should be needed - its handled in supertype
		Log.info("__________onLoad actions (pre-super):"+alreadyLoaded);
		super.onLoad();

		Log.info("__________onLoad actions:"+alreadyLoaded);
		//ok...deep breath....we trigger the OnFirstLoad actions when there is actions to run, and this hasnt already been loaded , and we are not loading the scene this object is in silently!
		if (actionsToRunOnFirstLoad != null && alreadyLoaded == false && !this.getScene().loadingsilently) {

			InstructionProcessor.lastTextObjectUpdated = this;

			ObjectsLog.info("_running onload :"+actionsToRunOnFirstLoad.CommandsInSet.size()+ " actions");
			ObjectsLog.info("_running actions :"+actionsToRunOnFirstLoad.CommandsInSet.toString());


			InstructionProcessor.processInstructions(
					actionsToRunOnFirstLoad.CommandsInSet.getActions(), "fl_"					
							+ this.objectsCurrentState.ObjectsName, this);

			Log.info("__________onLoad actions ran");


			ObjectsLog.info("after firing onload actions length="+toString().length());
		} else {

			Log.info("__________onLoad actions not ran");
			ObjectsLog.info("__________onLoad actions not ran");
		}


		alreadyLoaded = true;

	}
	 */
	/**
	 * updates the object to match the specified object state data, any nulls in
	 * the new data will result in the old data for those variables being kept
	 **/
	public void updateState(final SceneLabelObjectState newState,final boolean runOnload,final boolean repositionObjectsRelativeToThis) {

		if (newState == null){
			ObjectsLog.error("ERROR: State requested is null");
		}

		//	ObjectsLog.info("setting objects data to newState. RunOnLoad="+runOnload);
		//	ObjectsLog.info("setting objects data to newState. AlreadyLoaded="+alreadyLoaded);

		//	Log.info("	" + newState.serialiseToString() + "|__");

		// SceneObjectIcon.setAnimateClose();


		//general update stuff
		//updateState(newState);
		super.updateState(newState,false,false);

		ObjectsLog.log("Updating Label state - RunOnLoad="+runOnload+" AlreadyLoaded="+alreadyLoaded,"blue");
		Log.info("setting object:"+newState.ObjectsName+" data to newState");

		// reset values


		//set program name and number
		//	//NEWly added; Why wasn't this here before? Sept 2014 I added this - but we been using paragraphs for aggeeee....hmm
		//	if (objectsCurrentState.currentparagraphName.length()>2){
		//		objectsCurrentState.currentParagraphPage = newState.currentParagraphPage;
		//		objectsCurrentState.currentparagraphName = newState.currentparagraphName;
		//	}

		//set the text if any
		boolean textChanged = false;
		if (newState.ObjectsCurrentText != null) {

			if (!newState.ObjectsCurrentText.equals(objectsCurrentState.ObjectsCurrentText)){

				objectsCurrentState.ObjectsCurrentText=newState.ObjectsCurrentText;
				
				
				Log.info("text changing to:"+objectsCurrentState.ObjectsCurrentText);
				ObjectsLog.log("Text now inst Seting To:"+objectsCurrentState.ObjectsCurrentText,"green");

				//Note; we only set text if loading is ready for it, else we wait for it to be triggered after loading
				//(as ObjectsCurrentText has been set this should happen automatically) 
				if (   !this.getParentScene().isAllFilesLogicallyLoaded()
					|| !this.getParentScene().isAllFilesPhysicalLoaded() ){
						
						
						String errormsg = getName()+ " was not ready to settext from state. Both logical and physical loading have to be finnished for a settext to gaurenty to work.\n"
												   + "logical(all files needed on scene):"+ getParentScene().isAllFilesLogicallyLoaded() +" physical (is attached);"+ getParentScene().isAllFilesPhysicalLoaded()+"\n"
												   + "dont panic, however, as the text is remembered and set once loading is fully complete.";
						
						//
						Log.info(errormsg);	
						ObjectsLog(errormsg);	
						
				} else {
					TextLabel.setTextNow(objectsCurrentState.ObjectsCurrentText);
				}
				
				
				textChanged=true;



			} else {
				Log.info("text not changed (still "+newState.ObjectsCurrentText+")");
			}
		}

		//
		//ObjectsLog.log("paragraph name:"+currentObjectState.currentparagraphName
		//		+" data="+currentObjectState.currentParagraphPage,"green");

		//set url if changed
		/*
		if (newState.ObjectsCurrentURL != null) {

			//if the url has changed, or the text has been changed, we load the url
			//this is because the url should always override the default text.
			//so if the default text is set to "loading..." as its initial state, we load the url text
			//to replace that.
			if ((!objectsCurrentState.ObjectsCurrentURL
					.equals(newState.ObjectsCurrentURL))||textChanged) {


				Log.info("loading new text data from url[tp]");
				this.loadTextFromURL(objectsCurrentState.ObjectsCurrentURL);

			} else {
				Log.info("url not changed");
			}

		}
		 */


		this.ObjectsLog.info("setting paragraph css too:"+newState.CSSname);
		Log.info("setting css too:"+newState.CSSname);

		//change the css (if its blank then the CSS should be blank! no null value here needed
		this.setTextCSS(newState.CSSname);


		/* The following is now done in updateStateInternal:
		 * 
		if (newState.ObjectsName != null) {
			currentObjectState.ObjectsName = newState.ObjectsName;
		}

		//put it on the right scene and attach if needed
		if (newState.ObjectsSceneName!=null){


			currentObjectState.ObjectsSceneName = newState.ObjectsSceneName;

			Log.info("adding object to scene:"+currentObjectState.ObjectsSceneName);
			SceneWidget newScene = SceneWidget.getSceneByName(currentObjectState.ObjectsSceneName);
			this.setObjectsScene(newScene);


		}


		if (newState.Title != null) {
			currentObjectState.Title = newState.Title;
		}

		if (newState.X != -1) {
			currentObjectState.X = newState.X;

		}

		if (newState.Y != -1) {
			currentObjectState.Y = newState.Y;
		}

		if (newState.zindex != -1) {
			currentObjectState.zindex = newState.zindex;
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
		 */

		///if (newState.currentNumberOfParagraphs != -1) {
		//	objectsCurrentState.currentNumberOfParagraphs = newState.currentNumberOfParagraphs;
		//}


		/* The following is now done in updateStateInternal:
		 * 
		 Log.info("set objects position to "+currentObjectState.X+","+currentObjectState.Y);
		 Log.info("set div= "+objectsCurrentState.attachToDiv);

		// position
		// set position if not by div 
			if ( (this.objectsCurrentState.attachToDiv.length()<1)&&(this.objectsCurrentState.positionedRelativeToo==null)){

				ObjectsLog.info("set objects position ");

				super.setPosition(currentObjectState.X,
					currentObjectState.Y,false,false); //when state loading,the co-ordinates are set to the top right, not by the pin

				super.setZIndex(currentObjectState.zindex);

			}  else if (this.objectsCurrentState.positionedRelativeToo!=null){
				Log.info("set objects position relatively|"+this.objectsCurrentState.positionedRelativeToo.objectsCurrentState.ObjectsName);


				ObjectsLog.info("set objects position relatively|"+this.objectsCurrentState.positionedRelativeToo.objectsCurrentState.ObjectsName);

				objectsCurrentState.positionedRelativeToo.relativeObjects.add(this);

				super.setPosition(currentObjectState.relX ,
						currentObjectState.relY ,false,false); //when state loading,the co-ordinates are set to the top right, not by the pin

				super.setZIndex(currentObjectState.zindex);
			}

		//thisObject.getScene().setWidgetsPosition(thisObject,
		//		currentObjectState.X, currentObjectState.Y,currentObjectState.restrictPositionToScreen);


		 Log.info("setting title and visibility");

		// set its visibility
		this.setVisible(newState.currentlyVisible);

		TextLabel.setTitle(currentObjectState.Title);
		 */


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

		Log.info("..");
		//things might need to be reattached internally, so we run that again:
		TextLabel.recheckInternalObjects();

		// Log.info("clearing state");
		// previousStates.clear();

		// Log.info("Objects propertysChanged");

		// Log.info("Objects propertysChanged done");

		// Log.info("running objects onload again");
		// run first commands again
		// onLoad();

		// if (movementsLoaded){
		// start animations if its a default
		// MovementPath DefaultMovementPath = objectsMovements
		// .getMovement("default");

		// if (DefaultMovementPath != null) {
		// thisObject.playMovement(DefaultMovementPath, 5000, 100);
		// }
		// }
		// --------
	}

	public void resetObject() {
		resetObject(true,true);
	}

	@Override
	public void resetObject(boolean runUpdateState,boolean runOnLoad) {



		ObjectsLog.log("Resetting object:","#FF0000");
	//	ObjectsLog.log("Movement in initial state is :"+initialObjectState.moveState.get().currentmovementpath,"#FF0000");
		
		if (runUpdateState){
			updateState(getInitialState(), false,true);
		}
		if (runOnLoad){
			alreadyLoaded = false; //we reset here as to update it might have been reattached and thus the onload fired twice
			Log.info("running objects onload again..");
			this.onLoad();
		}

		super.resetObject(false,false);

	}

	/*
	@Override
	public void saveTempState() {

		Log.info("saving text object");

		tempObjectData =objectsCurrentState.copy();
		tempObjectData.setObjectsPrimaryType(objectsCurrentState.getCurrentPrimaryType());

		Log.info("seralised to:" + tempObjectData.serialiseToString());

	}
	 */

	@Override
	public void setVisible(boolean status) {

		// if made visible rerun paragraph
		if (status) {

			//recheck position is safe (as it might be restricted to the screen) if its not in a div
			if (this.objectsCurrentState.attachToHTMLDiv.length()<1 && this.isAttached()){

				if (this.objectsCurrentState.positionedRelativeToo!=null){
					this.updateRelativePosition(true);
				} else {

					Log.info("resetting position");
					thisobject.setPosition(objectsCurrentState.X, objectsCurrentState.Y,objectsCurrentState.Z, true, false,true);

				}
			}
			/*
			if (objectsCurrentState.TypedText == TrueFalseOrDefault.TRUE){

				ObjectsLog.info("typed text made visible, reseting paragraph");

				//ensure we are on at least the zero page (-1 might be used to mean "not set yet" so that a "next paragraph" would have
				//correctly gone to the first [0] page rather then skipping to the second. As this is not a nextparagraph function though we should check to
				//ensure its at least at zero, as -1 wont display anything)
				if (objectsCurrentState.currentParagraphPage==-1){
					objectsCurrentState.currentParagraphPage=0;
				}

				this.setParagraph(objectsCurrentState.currentParagraphPage);
			}*/


		}

		/*
		if (!status && associatedNextParagraphIcon!=null){
			ObjectsLog.info("setting dia visible secretly:"+status);
			associatedNextParagraphIcon.setVisibleSecretly(status);
		}
		 */

		//	objectsCurrentState.currentlyVisible = status;
		super.setVisible(status);


	}



	/*

	@Override
	public void restoreTempState() {

		ObjectsLog.log("Restoring temp state:","#FF0000");

		this.updateState(tempObjectData, false);
		// update debug boxs
		if (oipu != null) {

			oipu.update();
		}
	}
	 */


	/**
	 * sets the initial state to the supplied state, the state should match this objects type 
	 * as a untested cast is used
	 * @param objectsInitialState
	 */
	@Override
	protected void setObjectsInitialState(SceneObjectState objectsInitialState) {		
		super.setObjectsInitialState(objectsInitialState);
		initialObjectState=(SceneLabelObjectState) objectsInitialState;
	}



	/**
	 * @return the objectsCurrentState
	 */
	@Override
	public SceneLabelObjectState getObjectsCurrentState() {
		return (SceneLabelObjectState)objectsCurrentState;
	}
	public SceneLabelObjectState getInitialState() {
		return (SceneLabelObjectState)initialObjectState;
	}
	public SceneLabelObjectState getTempState() {
		return (SceneLabelObjectState)tempObjectState;
	}


	/** returns the current paragraph name.
	 * Remember both the name and the number determine the current bit of text
	 * Eg
	 * "Reply1 would be name "reply" number "1"
	 * "Reply2 would be name "reply" number "2"
	 *
	public String getParagraphName() {

		return objectsCurrentState.currentparagraphName;
	}*/

	/** returns the currently displayed paragraph number.
	 * Think of this as the current "page" of text being displayed within
	 * a current named reply 
	public int getParagraphNumber() {

		return objectsCurrentState.currentParagraphPage;
	}
	 **/
	/** gets the currently displayed text **/
	public String getCurrentText() {

		return objectsCurrentState.ObjectsCurrentText;
	}

	public void setCursorClass(String string) {
		TextLabel.setCursorClass(string);

	}

	//
	//	/** assign the label specific parameters from the given string split by newlines **/
	//	@Override
	//	void assignObjectTypeSpecificParameters(){//String[] itemslines) {
	//
	//		//ensure not already setup
	//		if (objectsCurrentState!=null){
	//			Log.info("(parameters for label "+objectsCurrentState.ObjectsName+" already set up)");
	//			return;
	//		}
	//		//super needs to be run before 	objectsCurrentState can be assigned
	//		super.assignObjectTypeSpecificParameters();//itemslines); 
	//
	//		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
	//		//and also makes a reference to the super.objectsCurrentState to their own (type specific) objectsCurrentState object in order to do this
	//		objectsCurrentState = (SceneLabelObjectState) super.getObjectsCurrentState();
	//
	//		//Log.info("(assigning assignObjectTypeSpecificParameters for label object "+objectsCurrentState.ObjectsName+" )");
	//	//	ObjectsLog.log("(assigning label ObjectTypeSpecificParameters)","green");
	//
	//
	//		//if (itemslines==null){
	//		//	Log.info("(no label parameters to assign for:"+objectsCurrentState.ObjectsName);
	//		//	return;
	//		//}
	//		
	//		//Now triggered from sceneobject;		
	//	//	objectsCurrentState.assignObjectTypeSpecificParameters(itemslines); //THIS RUNS DIALOGUE STATE UPDATE NOT LABEL 
	//		//We either need seperately named commands that dont override eachother
	//		//or we need one to call the other and them both triggered elsewhere
	//		//Temp; named it "LType" for now, in future rearrange as above specified
	//
	//		Log.info("L Objects ObjectsCurrentText set to:"
	//				+ objectsCurrentState.ObjectsCurrentText); //undefimed
	//		
	//		
	////
	////		//String itemslines[] = Parameters.split("\n");
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
	////
	////			
	////			//note we have cleared these settings above only to reset them here
	////			//because dialogue objects treat these parameters differently
	////			//Normally CSSname is assumed to be BoxCSS, but with text objects it refers to the inner css of the text
	////			//and BoxCSS only the containers CSS
	////			//Other object types only have container CSS, thus both parameters effect that instead.
	////			if (param.equalsIgnoreCase("BoxCSS")) {
	////				objectsCurrentState.CurrentBoxCSS = value;				
	////				Log.info("Objects CurrentBoxCSS set to:"+ objectsCurrentState.CurrentBoxCSS);
	////			}
	////
	////			if (param.equalsIgnoreCase("CSSname")) {
	////				objectsCurrentState.CSSname = value;
	////				Log.info("Objects CSSname set to:"+ objectsCurrentState.CSSname);
	////			}
	////
	////
	////			if (param.equalsIgnoreCase("CursorVisible")) {
	////
	////				objectsCurrentState.cursorVisible = cursorVisibleOptions.valueOf(value.toUpperCase());  //value;
	////				Log.info("_______________________________________Objects visibility set to:"+ objectsCurrentState.cursorVisible);
	////			}
	////			if (param.equalsIgnoreCase("TypeText")) {
	////
	////				//objectsCurrentState.TypedText = Boolean.parseBoolean(value);
	////
	////				objectsCurrentState.TypedText = TrueFalseOrDefault.valueOf(value.toUpperCase());
	////
	////				Log.info(objectsCurrentState.ObjectsName+"_____________Objects TypedText set to:"
	////						+ objectsCurrentState.TypedText);
	////			}
	////
	////			if (param.equalsIgnoreCase("KeyBeep")) {
	////
	////				objectsCurrentState.Custom_Key_Beep = value;
	////
	////				Log.info(objectsCurrentState.ObjectsName+"_____________Objects Custom_Key_Beep set to:"+ objectsCurrentState.Custom_Key_Beep);
	////			}
	////
	////			if (param.equalsIgnoreCase("SpaceKeyBeep")) {
	////
	////				objectsCurrentState.Custom_Space_Beep = value;
	////
	////				Log.info(objectsCurrentState.ObjectsName+"_____________Objects SpaceKeyBeep set to:"+ objectsCurrentState.Custom_Space_Beep);
	////			}
	////
	////
	////			if (param.equalsIgnoreCase("DefaultText")) {
	////
	////				objectsCurrentState.ObjectsCurrentText = value;
	////
	////				Log.info("Objects ObjectsCurrentText set to:"
	////						+ objectsCurrentState.ObjectsCurrentText);
	////			}
	////			
	////		}
	//		Log.info("done loading label specific params");
	//
	//	}




}
