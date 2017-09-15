package com.lostagain.JamGwt.JargScene;

import java.util.logging.Logger;

import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackError;
import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackRunnable;
import com.lostagain.Jam.DialogueCollection;
import com.lostagain.Jam.GameVariableManagement;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneDialogueObjectState;
import com.lostagain.Jam.SceneObjects.SceneLabelObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.Helpers.DialogueObjectHelper;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneDialogueObject;
import com.lostagain.Jam.SceneObjects.SceneObjectState.TrueFalseOrDefault;
import com.lostagain.JamGwt.TypedLabel;

/** A DialogObject is a object designed for conversations, or displaying paragraphs of text **/

public class SceneDialogObject extends SceneLabelObject implements IsSceneDialogueObject { //SceneDivObject

	public static Logger Log = Logger.getLogger("JAM.SceneDialogObject");


	
	/**
	 * stores the current default object state. ALL object interactions should update
	 * this, or read from this
	 **/
	private SceneDialogueObjectState objectsCurrentState;


	//bits to make this widget work

	/**the main animated label. The visual part of the object.**/
	public TypedLabel TextLabel;


	/** The internal collection of paragraphsets used by this specific dialogue object **/
	DialogueCollection knownParagraphs;

	public DialogueCollection getKnownParagraphs() {
		return knownParagraphs;
	}

	public void setKnownParagraphs(DialogueCollection knownParagraphs) {
		this.knownParagraphs = knownParagraphs;
	}

	/**a reference to this object. This is basically used to refer
	 * to this objects functions from within annominious inner class's **/
	SceneDialogObject thisObject = this;


	/** An object that appears to indicate there is more text to view 
	 * A simple setVisible is used to make this hide/appear depending on if 
	 * hasNextParagraph() is true **/
	SceneObject associatedNextParagraphIcon = null;



	/**
	 * 
	 * @param newobjectdata
	 * @param objectsCurrentStateString
	 * @param sceneItsOn
	 */
	public SceneDialogObject(
			SceneObjectState newobjectdata, 
			ActionList actions,
			SceneWidget sceneItsOn) {
		this(new SceneDialogueObjectState(newobjectdata),actions,sceneItsOn); 		

	}

	/**
	 * 
	 * @param newobjectdata
	 * @param objectsCurrentStateString
	 * @param sceneItsOn
	 */
	public SceneDialogObject(
			SceneDialogueObjectState newobjectdata, 
			ActionList actions,
			SceneWidget sceneItsOn) {

		//by passing the data though to the constructor straight away
		//we allow it to handle the action and parameter settings itself.
		//It can separate of what it needs from the objectsCurrentStateString, and put it into the newly created SceneDialogObjectState
		//
		//In order to process parameters specific to SceneDialogObject, however, it triggers assignObjectTypeSpecificParameterscommand
		//which this class implements. It has to do this because SceneObjectVisual knows nothing about this class itself! 
		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
		//and also copys the super.objectsCurrentState to their own (type specific) objectsCurrentState object
		//	super(new SceneDialogObjectState(newobjectdata),objectsCurrentStateString,sceneItsOn);

		super(newobjectdata,actions,sceneItsOn, new TypedLabel("").getInternalWidget() ,false ); 		

	//	TextLabel = (TypedLabel) super.widgetContents; //assign contents as we know it will match what we just passed to tyhe super constructor
		TextLabel = ((TypedLabel.HTMLPanelForTypedLabel) super.widgetContents).associatedTypedLabel; //assign contents as we know it will match what we just passed to the super constructor

		if (TextLabel==null){
			Log.warning("TextLabel is null");
		}


		//	assignObjectTypeSpecificParameters();//newobjectdata.ObjectsParamatersFromFile); //Can't be done from super()
		objectsCurrentState = (SceneDialogueObjectState) super.getObjectsCurrentState();

		if (objectsCurrentState==null){
			Log.warning("objectsCurrentState is null");
		}

		ObjectsLog.info("(creating dialogue object)");

		//set up any global SceneObjectSettings before doing the specific stuff
		//super();

		//copy all the specified data into the variable settings
		//super.objectsCurrentState = currentObjectState;

		//ObjectsLog.info("newobjectdata has visibility:"+newobjectdata.currentlyVisible);

		//update the generic data common to all objects to match whats supplied
		//this command replaces the need for all the commented out stuff below
		//only Dialogue object specific parameters need to be dealt with in the SceneDialogue file now, reducing redundancy.
		//currentObjectState.setGenericObjectData(newobjectdata);


		//ObjectsLog.info("currentObjectState has visibility:"+currentObjectState.currentlyVisible);



		objectsCurrentState.zindex = newobjectdata.zindex; //redundant?why do we have it here and in currentObjectState?

		// we used to ignore scene actions by default on dialogue objects
		//ignoreSceneActions = true;

		// extract dialog specific data from the string parameters
		/*
		Log.info("extracting text data from:\r" + objectsCurrentStateString);

		// split actions off if present
		// get scene parameters (anything before a line with : in it)
		int firstLocOfColon = objectsCurrentStateString.indexOf(':');

		Log.info("LocOfColon recieved for text object:" + firstLocOfColon);

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

		// now split again by new lines to get parameter data
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

		Log.info("_________creating default label data object________________");
		ObjectsLog.info("objects currentObjectState state has visibility:"+objectsCurrentState.currentlyVisible);

		//defaultObjectState = objectsCurrentState.copy();
		//associate this default with the parent types variable too
		//	super.getObjectsInitialState() = defaultObjectState;


		//	setObjectsInitialState(objectsCurrentState.copy());

		ObjectsLog.info("objects initial state has visibility:"+getInitialState().currentlyVisible);
		//	initialObjectState.setObjectsPrimaryType(objectsCurrentState.getPrimaryObjectType());



		// Log.info("_________serialisation test________________");
		// String serialized = defaultObjectData.serialiseToString();
		// defaultObjectData.deserialise(Serialized);
		// Log.info("_________serialisation test done________________");
		// Log.info("_________________________Objects url set to:"
		// + currentObjectState.ObjectsURL);

		// create object from data

		// add to object list
		//SceneObjectDatabase.all_text_objects.put(
		//		objectsCurrentState.ObjectsName.toLowerCase(), this);


		//SceneObjectDatabase.addObjectToDatabase(this,SceneObjectType.DialogBox);
		boolean initialize = true; //theres no subtypes of dialogbox object yet
		if (initialize){
			setupWidget(objectsCurrentState);

			ObjectsLog.logTimer("setup initialize took;");			

			initialiseAndAddToDatabase();
		}

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

	private void setupWidget(SceneDialogueObjectState data) {		//,		final SceneWidgetVisual sceneItsOne
		//set up label related functions (css, core text etc)
		super.setupWidget(data); //sceneItsOne;

		Log.info("___________________________________________________________________creating dialogue object");
		ObjectsLog.log("Setting up dialogue object","orange");

		//TextLabel.setAssociatedObject(this);	//no need to run as label does this

		//		Override the one set by the super in order to  trigger  	updateNextIcon();
		/*
		TextLabel.setRunAfterTextSet(new Runnable(){
			@Override
			public void run() {
				updateNextIcon();
				//fire OnTypingEnd check?

			}
		});


		 */

		// start loading text if there's a url
		if (objectsCurrentState.ObjectsCurrentURL != null) {
			Log.info("loading text from url");
			loadTextFromURL(objectsCurrentState.ObjectsCurrentURL);
		}

		ObjectsLog.log("created dialogue object","green");

	}


	/** sets the style of the cursor when text is typed
	public void setCursorMode(cursorVisibleOptions cursorStyle) {

		TextLabel.setBlink(cursorStyle);
	}**/

	private void setupWidget_old(SceneDialogueObjectState data,
			final SceneWidget sceneItsOne) {

		//objectScene = sceneItsOne;
		//super.setObjectsSceneVariable(sceneItsOne);
		objectsCurrentState = data;

		//all the common things to setup are done in SceneObject
		super.setupWidget();

		Log.info("___________________________________________________________________creating text object");
		ObjectsLog.log("creating text label","green");
		//	TextLabel = new TypedLabel(data.ObjectsCurrentText);

		//TextLabel.setText(data.ObjectsCurrentText,data.TypedText); 


		TextLabel.setBlink(objectsCurrentState.cursorVisible);	

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

		TextLabel.setRunAfterTextSet(new Runnable(){

			@Override
			public void run() {
				updateNextIcon();

			}

		});

		// start loading text if there's a url
		if (objectsCurrentState.ObjectsCurrentURL != null) {

			Log.info("loading text from url");

			loadTextFromURL(objectsCurrentState.ObjectsCurrentURL);
		}
		//
		// set styles
		TextLabel.getInternalWidget().setTitle(data.Title);


		//size should already be in css units
		TextLabel.getInternalWidget().setSize(data.sizeX, data.sizeY);


		Log.info("setting style name to:");
		//TextLabel.setStyleName(data.CSSname);
		setTextCSS(data.CSSname);

		//AbsolutePanel internalFrame = new AbsolutePanel();
		//internalFrame.add(TextLabel);

		//int npi_x = data.sizeX-associatedNextParagraphIcon.getOffsetWidth()-10;
		//int npi_y = data.sizeY-associatedNextParagraphIcon.getOffsetHeight()-10;

		//internalFrame.add(associatedNextParagraphIcon,npi_x,npi_y);



		// create animation timer
		//	createAnimationTimer();

		Log.info("setting text and widget on dialogue widget...typed="+data.TypedText);

		ObjectsLog.log("Text Set To;"+data.ObjectsCurrentText+" "+data.TypedText,"green");
		//set the text
		if (data.TypedText == TrueFalseOrDefault.FALSE ){
			TextLabel.setTextNow(data.ObjectsCurrentText); 
		} else {			
			TextLabel.setText(data.ObjectsCurrentText); //true or default gives typed text
		}


		super.setWidget(TextLabel.getInternalWidget());		

	//	startLoadingMovementAnimations();

		// assign generic handlers
		//probably should be refractoed into SceneObject (that is, this ones superclass)

		//sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
		//		| Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
		//		| Event.ONTOUCHSTART | Event.ONTOUCHEND | Event.ONTOUCHCANCEL|Event.ONTOUCHMOVE);

	}

	public void loadTextFromURL(String url) {
		loadTextFromURL( url, true);
	}

	/** Load a new paragraph text from a file **/
	public void loadTextFromURL(String url,boolean useCache) {

		// JAM.Quality.equalsIgnoreCase("debug")
		if (JAMcore.DebugMode){
			//	TextLabel.setTextNow("(loading text from file:"+url+")",true); //the true disables the sound effect
			setText("(loading text from file:"+url+")",false); //the true disables the sound effect

			ObjectsLog.log("Text Set To;(loading text from file:"+url+")","green");
		} else {
			//	TextLabel.setTextNow("...",true); //the true disables the sound effect
			this.setText("...", false);

			ObjectsLog.log("Text Set To;...","green");	
		}

		DialogueObjectHelper.setDialogueURL_Helper(url, this, useCache);

		/*

		//TODO: find a method to do most of the below in the core;

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
				text = JAMcore.parseForLanguageSpecificExtension(text);

				// swap TextIds for text
				text = JAMcore.parseForTextIDs(text);



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

				//if (!isAttached()){
				//	Log.severe(getName()+ " was not attached yet! yet setText is triggered for url text...");	
				//}

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
		//	FileManager.GetTextFile(SceneWidget.SceneFileRoot
		//			+ this.getScene().SceneFileName + "/Text/" + url,
		//			onResponse,
		//			onError,
		//		false,
		//		useCache);

		RequiredImplementations.getFileManager().getText(SceneWidget.SceneFileRoot
				+ this.getParentScene().SceneFileName + "/Text/" + url,
				onResponse,
				onError,
				false,
				useCache);
		 */

	}

	/*
	@Override
	public void setText(String text) {

		//use the default setting to determine if its typed or not
		if (objectsCurrentState.TypedText==TrueFalseOrDefault.TRUE){
			setText(text,true);
		} else {
			setText(text,false);
		}
		//setText(text,objectsCurrentState.TypedText);

	}
*/
	
	@Override
	/** sets the current text to a specific string, either straight away, or after being typed **/
	public void setText(String text, boolean Typed) {
		super.setText(text, Typed); //we do the exact same as our parent label type....
		updateNextIcon(); //only we also update our next icon
		
		/*
		//process for any variables in the text (we might want to provide a option not to run this function)
		text = GameVariableManagement.replaceGameVariablesWithValues(text,this);

		objectsCurrentState.ObjectsCurrentText = text;

		if (!isAttached()){
			Log.info(getName()+ " was not attached yet! yet setText is triggered on dialogue...");	
			ObjectsLog(getName()+ " was not yet attached yet when settext triggered. Text remembered and will be set when attached");			
			return;
		}



		boolean disablesound = !(this.isVisible() && this.isAttached()); //disable sound should be false unless its invisible or not attached
		if (Typed){
			
			TextLabel.setText(text,disablesound);
			ObjectsLog.log("Text Set To;"+text+" sound disabled:"+disablesound,"green");
			
		} else {
			TextLabel.setTextNow(text,disablesound);
			ObjectsLog.log("Text instantly Set To;"+text+" sound disabled:"+disablesound,"green");
		}



		//	SceneObjectDatabase.lastTextObjectUpdated = this;

		wasLastObjectUpdated();
		updateNextIcon();*/
	}





	public void setParagraph(int num) {

		ObjectsLog.info("setting Paragraph to "+num);
		DialogueObjectHelper.setParagraph_Helper(num, this);

		/*
		if (num <= (objectsCurrentState.currentNumberOfParagraphs - 1)) {
			objectsCurrentState.currentParagraphPage = num;

			ObjectsLog.log("(setting text after set paragraph requested)","blue");

			this.setText(""
					+ knownParagraphs.getText(
							objectsCurrentState.currentparagraphName,
							objectsCurrentState.currentParagraphPage));

		}

		this.wasLastObjectUpdated();

		if (oipu != null) {
			oipu.update();
		}
		 */	
	}

	public void nextParagraph() {

		//SceneObjectDatabase.lastTextObjectUpdated = this;


		//if typing finish current one
		if (TextLabel.isTyping()){

			Log.info("dialogue currently typing so we skip to the end");

			TextLabel.setTextNow(""
					+ knownParagraphs.getText(
							objectsCurrentState.currentparagraphName,
							objectsCurrentState.currentParagraphPage));

			ObjectsLog.log("Text Set To:","green");
			ObjectsLog.log("paragraph name:"+objectsCurrentState.currentparagraphName
					+" data="+objectsCurrentState.currentParagraphPage,"green");

			wasLastObjectUpdated();

			return;
		} else {
			//if not typing goto next paragraph if there is one
			DialogueObjectHelper.nextParagraph_Helper(this);

			/*
			//if not typing goto next paragraph if there is one
			//
			//(trying to goto)   <= (where we can goto) 
			// eg, (currentparagraph + 1) <= (total - 1)
			//(4+1) <= (5-1) 
			if ((objectsCurrentState.currentParagraphPage+1) <= (objectsCurrentState.currentNumberOfParagraphs - 1)) {
				//+1 added
				Log.info("currentParagraph was at:"+objectsCurrentState.currentParagraphPage);

				objectsCurrentState.currentParagraphPage++;

				Log.info("currentParagraph is  at:"+objectsCurrentState.currentParagraphPage);

				ObjectsLog.log("(setting text after next paragraph requested)","blue");
				this.setText(""
						+ knownParagraphs.getText(
								objectsCurrentState.currentparagraphName,
								objectsCurrentState.currentParagraphPage));

				return;
			} else {

				ObjectsLog.log("Requested paragraph out of range:"+(objectsCurrentState.currentParagraphPage+1)+" requested out of "+objectsCurrentState.currentNumberOfParagraphs,"blue");
				Log.info("Requested paragraph out of range:"+(objectsCurrentState.currentParagraphPage+1)+" requested out of "+objectsCurrentState.currentNumberOfParagraphs);
				Log.info("Paragraph name is  :"+objectsCurrentState.currentparagraphName);
			}*/

		}

	}

	public void previousParagraph() {

		DialogueObjectHelper.previousParagraph_Helper(this);


		/*
		if (objectsCurrentState.currentParagraphPage > 0) {
			objectsCurrentState.currentParagraphPage--;

			ObjectsLog.log("(setting text after previous paragraph requested)","blue");
			this.setText(""
					+ knownParagraphs.getText(
							objectsCurrentState.currentparagraphName,
							objectsCurrentState.currentParagraphPage));
		}
		//SceneObjectDatabase.lastTextObjectUpdated = this;

		this.wasLastObjectUpdated();*/
	}

	/** We update the associated "nextparagraph" object if one exists.
	 * Typically this would be an arrow or a > that shows when there
	 * is more text to display**/
	public void updateNextIcon(){

		Log.info("setting updateNextIcon");

		//ensure we are even using a associatedNextParagraph object
		if (this.objectsCurrentState.NextParagraphObject.length()<2){
			return;
		}

		//ensure there is an icon set, if not get it
		if (associatedNextParagraphIcon==null){

			//SceneObject nextobject = SceneWidget.getSceneObjectByName(this.currentObjectState.NextParagraphObject,this)[0];


			SceneObject nextobject = SceneObjectDatabase
					.getSingleSceneObjectNEW(this.objectsCurrentState.NextParagraphObject,this,true);

			if (nextobject!=null){			

				associatedNextParagraphIcon = nextobject;

			} else {

				Log.info("Error: Cant find nextparagraphobject for icon, specified as:"+this.objectsCurrentState.NextParagraphObject);
				return;
			}
		}



		Log.info("setting next icon currently typing:"+TextLabel.isTyping());

		if (hasNextParagraph() && 
				(!TextLabel.isTyping()) && 
				!TextLabel.isWaitingToType() && 
				this.isVisible())
		{
			//also make sure this dialogue is visible!
			//we don't want the associated icon visible all on its own
			associatedNextParagraphIcon.setVisibleSecretly(true);			

		} else {
			Log.info("setting next icon to invisible");

			associatedNextParagraphIcon.setVisibleSecretly(false);
		}
	}

	public boolean hasNextParagraph(){

		//we check <(num-1)
		//currentParagraphPage counts from zero
		//so if we are on paragraph page 4 thats actually the 5th paragraph
		//0,1,2,3,4
		//So if the total is 5 we are already on the last one
		if (objectsCurrentState.currentParagraphPage < (objectsCurrentState.currentNumberOfParagraphs)-1) {
			return true;
		}		

		return false;
	}


	/** sets the name of the current paragraph.
	 *  If "TriggerNow" is set true, the paragraph will be displayed straight away on its first page **/
	public void setParagraphName(String name,boolean TriggerNow) {
		DialogueObjectHelper.setParagraphName_Helper(name,TriggerNow,this);

		/*
		SceneDialogueObjectState dialogue_objects_current_state = objectsCurrentState;

		ObjectsLog.log("Setting paragraph to:"+name+" triggernow = "+TriggerNow,"green");


		dialogue_objects_current_state.currentParagraphPage = 0;
		Log.info("paragraph name was "+dialogue_objects_current_state.currentparagraphName+" is now set too "+name);

		dialogue_objects_current_state.currentparagraphName = name;

		//update number of paragraphs if paragraphs are loaded
		//if they arnt loaded we assume its 1 page (this will auto fix to 
		//the correct number when loadeding is finished)
		if (knownParagraphs!=null){
			dialogue_objects_current_state.currentNumberOfParagraphs = knownParagraphs.getNumberOfParagraphs(dialogue_objects_current_state.currentparagraphName);
		} else {
			dialogue_objects_current_state.currentNumberOfParagraphs = 1;
		}


		Log.info("This new paragraphset has "+dialogue_objects_current_state.currentNumberOfParagraphs+" paragraphs ");

		if (TriggerNow){
			this.setText(""+ getKnownParagraphs().getText(
					dialogue_objects_current_state.currentparagraphName,
					dialogue_objects_current_state.currentParagraphPage));
		} else {
			Log.info("New paragraphname set for next NextParagraph(), unless a new setURL happens first");

			dialogue_objects_current_state.currentParagraphPage = -1;

			ObjectsLog.info("Paragraph set to:"+dialogue_objects_current_state.currentparagraphName+":"+
					dialogue_objects_current_state.currentParagraphPage+", but not set to appear yet");
		}

		Log.info("paragraph number is set to "+dialogue_objects_current_state.currentParagraphPage);		



		wasLastObjectUpdated();*/
	}
	/*
	public void setParagraphCSS(String name) {

		TextLabel.setStyleName(name);

		objectsCurrentState.CSSname = TextLabel.getStyleName();

	}

	public void addParagraphCSS(String name) {

		TextLabel.addStyleName(name);

		objectsCurrentState.CSSname = TextLabel.getStyleName();


	}

	public void removeParagraphCSS(String name) {

		TextLabel.removeStyleName(name);

		objectsCurrentState.CSSname = TextLabel.getStyleName();

	}*/

	/**
	 * Reloads the text data into this dialogue from objectsCurrentState.ObjectsCurrentURL
	 * CURRENTLY UNTESTED (designed to be hocked up to the object inspector with a reload button)
	 **/
	public void reloadTextFromFile(){
		this.loadTextFromURL(objectsCurrentState.ObjectsCurrentURL,false);
	}

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
		//	SceneObjectDatabase.lastTextObjectUpdated = this;

		wasLastObjectUpdated();
	}



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
	public void updateState(final SceneDialogueObjectState newState,final boolean runOnload,final boolean repositionObjectsRelativeToThis) {

		if (newState == null){
			ObjectsLog.error("ERROR: State requested is null");
		}

		//Log.info("	" + newState.serialiseToString() + "|__");

		// SceneObjectIcon.setAnimateClose();

		//we need to know if the updateState on our superclass will change the text
		//(this is to determain if we need to reload the url or not, as that overrides the text change if a url is set)
		boolean textChanged = false;
		if (newState.ObjectsCurrentText != null) {
			if (!newState.ObjectsCurrentText.equals(objectsCurrentState.ObjectsCurrentText)){
				textChanged=true;
			}
		}


		//general update stuff
		//updateState(newState);
		super.updateState(newState,false,false);

		Log.info("setting object:"+newState.ObjectsName+" data to newState");
		ObjectsLog.log("Updating Diaglogue state - RunOnLoad="+runOnload+" AlreadyLoaded="+alreadyLoaded,"blue");

		// reset values


		//set program name and number
		//NEWly added; Why wasn't this here before? Sept 2014 I added this - but we been using paragraphs for aggeeee....hmm
		if (objectsCurrentState.currentparagraphName.length()>2){
			objectsCurrentState.currentParagraphPage = newState.currentParagraphPage;
			objectsCurrentState.currentparagraphName = newState.currentparagraphName;
		}


		//note; label does this stuff so we dont have too
		/*
		if (newState.ObjectsCurrentText != null) {

			if (!newState.ObjectsCurrentText.equals(objectsCurrentState.ObjectsCurrentText)){

				objectsCurrentState.ObjectsCurrentText=newState.ObjectsCurrentText;
				Log.info("text changing to:"+objectsCurrentState.ObjectsCurrentText);
				ObjectsLog.log("Text now instantly Seting To:"+objectsCurrentState.ObjectsCurrentText,"green");

				TextLabel.setTextNow(objectsCurrentState.ObjectsCurrentText);
				textChanged=true;



			} else {
				Log.info("text not changed (still "+newState.ObjectsCurrentText+")");
			}
		}*/

		//
		//ObjectsLog.log("paragraph name:"+currentObjectState.currentparagraphName
		//		+" data="+currentObjectState.currentParagraphPage,"green");

		//set url if changed
		if (newState.ObjectsCurrentURL != null) {
			ObjectsLog.log("new state specified url:"+newState.ObjectsCurrentURL,"blue");
			
			//If the url has changed, or the text has been changed, we load the url
			//this is because the url should always override the default text.
			//so if the default text is set to "loading..." as its initial state, we load the url text
			//to replace that.
			if ((!objectsCurrentState.ObjectsCurrentURL.equals(newState.ObjectsCurrentURL)) || textChanged) {
				
				objectsCurrentState.ObjectsCurrentURL = newState.ObjectsCurrentURL; //new, was oddly absent before
				
						
				ObjectsLog.log("loading new text data from url:"+objectsCurrentState.ObjectsCurrentURL,"blue");

				Log.info("loading new text data from url[tp]");
				this.loadTextFromURL(objectsCurrentState.ObjectsCurrentURL);

			} else {
				Log.info("url not changed");
			}

		}



		///		this.ObjectsLog.info("setting paragraph css too:"+newState.CSSname);
		//		Log.info("setting css too:"+newState.CSSname);

		//change the css (if its blank then the CSS should be blank! no null value here needed
		//	this.setParagraphCSS(newState.CSSname);


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

		if (newState.currentNumberOfParagraphs != -1) {
			objectsCurrentState.currentNumberOfParagraphs = newState.currentNumberOfParagraphs;
		}


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


		Log.info("updateThingsPositionedRelativeToThis:");
		if (repositionObjectsRelativeToThis){
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
		//ObjectsLog.log("Movement in initial state is :"+initialObjectState.moveState.get().currentmovementpath,"#FF0000");

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

			//recheck position is safe if its not in a div
			//TODO: the supertype does this bit so we shouldnt really bother ourselves?
			if (this.objectsCurrentState.attachToHTMLDiv.length()<1 && this.isAttached()){

				if (this.objectsCurrentState.positionedRelativeToo!=null){
					this.updateRelativePosition(true);
				} else {

					Log.info("resetting position");
					thisobject.setPosition(objectsCurrentState.X, objectsCurrentState.Y, objectsCurrentState.Z ,true, false,true);

				}
			}
			//--------------

			if (objectsCurrentState.TypedText == TrueFalseOrDefault.TRUE){

				ObjectsLog.info("typed text made visible, reseting paragraph");

				//ensure we are on at least the zero page (-1 might be used to mean "not set yet" so that a "next paragraph" would have
				//correctly gone to the first [0] page rather then skipping to the second. As this is not a nextparagraph function though we should check to
				//ensure its at least at zero, as -1 wont display anything)
				if (objectsCurrentState.currentParagraphPage==-1){
					objectsCurrentState.currentParagraphPage=0;
				}

				this.setParagraph(objectsCurrentState.currentParagraphPage);
			}


		}


		if (!status && associatedNextParagraphIcon!=null){
			ObjectsLog.info("setting associatedNextParagraphIcon visible secretly:"+status);
			associatedNextParagraphIcon.setVisibleSecretly(status);
		}


		//		objectsCurrentState.currentlyVisible = status;
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


	@Override
	protected void runAfterTextSet() {
		super.runAfterTextSet();
		updateNextIcon(); //we also trigger this on dialogues		
	}

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
	public SceneDialogueObjectState getObjectsCurrentState() {
		return (SceneDialogueObjectState)objectsCurrentState;
	}
	public SceneDialogueObjectState getInitialState() {
		return (SceneDialogueObjectState)initialObjectState;
	}
	public SceneDialogueObjectState getTempState() {
		return (SceneDialogueObjectState)tempObjectState;
	}


	/** returns the current paragraph name.
	 * Remember both the name and the number determine the current bit of text
	 * Eg
	 * "Reply1 would be name "reply" number "1"
	 * "Reply2 would be name "reply" number "2"
	 **/
	public String getParagraphName() {

		return objectsCurrentState.currentparagraphName;
	}

	/** returns the currently displayed paragraph number.
	 * Think of this as the current "page" of text being displayed within
	 * a current named reply **/
	public int getParagraphNumber() {

		return objectsCurrentState.currentParagraphPage;
	}

	/**
	/** gets the currently displayed text 
	public String getCurrentText() {

		return objectsCurrentState.ObjectsCurrentText;
	}

	public void setCursorClass(String string) {
		TextLabel.setCursorClass(string);

	}
	 **/

	/** assign the dialogue specific parameters from the given string split by newlines **/
	//	@Override
	//	void assignObjectTypeSpecificParameters() {//String[] itemslines) {
	//		
	//		//ensure not already setup
	//		if (objectsCurrentState!=null){
	//			Log.info("(parameters for dialogue "+objectsCurrentState.ObjectsName+" already set up)");
	//			return;
	//		}
	//		//super needs to be run before 	objectsCurrentState can be assigned
	//		super.assignObjectTypeSpecificParameters();//itemslines); 
	//		
	//		//The assignObjectTypeSpecificParameterscommand function, which all sceneobjectvisuals must implement, assigns their own specific parameters
	//		//and also makes a reference to the super.objectsCurrentState to their own (type specific) objectsCurrentState object in order to do this
	//		objectsCurrentState = (SceneDialogObjectState) super.getObjectsCurrentState();
	//			
	//		//Log.info("(assigning assignObjectTypeSpecificParameters for dialog object "+objectsCurrentState.ObjectsName+" )");
	//		//ObjectsLog.log("(assigning dialog ObjectTypeSpecificParameters)","green");
	//				
	//		
	//		//if (itemslines==null){
	//		//	Log.info("(no parameters to assign for "+objectsCurrentState.ObjectsName);
	//		//	return;
	//			
	//		//}
	//		
	//		//objectsCurrentState.assignObjectTypeSpecificParameters(itemslines); 
	//		
	//	//	Log.info("D Objects ObjectsCurrentText set to:"
	//	//			+ objectsCurrentState.ObjectsCurrentText); //undefined here
	//		
	//		
	////		
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
	////			// assign data
	////			/*
	////			if (param.equalsIgnoreCase("Name")) {
	////
	////				// ObjectsName = value;
	////				objectsCurrentState.ObjectsName = value;
	////
	////				Log.info("Objects file name set to:"
	////						+ objectsCurrentState.ObjectsName);
	////
	////				//Dialogue objects if they use a folder, its just their object name
	////				folderName = objectsCurrentState.ObjectsName;
	////
	////			}
	////
	////			if (param.equalsIgnoreCase("Title")) {
	////
	////				currentObjectState.Title = value;
	////				Log.info("Objects Title set to:" + currentObjectState.Title);
	////
	////			}
	////			 */
	/////*
	////			if (param.equalsIgnoreCase("Size")) {
	////
	////				String value1 = value.split(",")[0];
	////				String value2 = value.split(",")[1];
	////
	////				value1 = SceneDivObject.ensureSizeIsRealCss(value1);
	////				value2 = SceneDivObject.ensureSizeIsRealCss(value2);
	////
	////
	////				objectsCurrentState.sizeX = value1; //Integer.parseInt(value1); //we used to use ints now we support all css sizes
	////				objectsCurrentState.sizeY = value2;//Integer.parseInt(value2);
	////
	////				Log.info("Objects size set to:" + objectsCurrentState.sizeX
	////						+ "x" + objectsCurrentState.sizeY);
	////
	////			}
	////			if (param.equalsIgnoreCase("RestrictToScreen")) {
	////
	////				// Note, for dialogue type it defaults too restricting to the screen
	////				if (value.equalsIgnoreCase("false")) {
	////					objectsCurrentState.restrictPositionToScreen = false;
	////				} else {
	////					objectsCurrentState.restrictPositionToScreen = true;
	////				}
	////
	////			}
	////
	////
	////*/
	////			
	////			//note we have cleared these settings above only to reset them here
	////			//because dialogue objects treat these parameters differently
	////			//Normally CSSname is assumed to be BoxCSS, but with text objects it refers to the inner css of the text
	////			//and BoxCSS only the containers CSS
	////			//Other object types only have container CSS, thus both parameters effect that instead.
	////			if (param.equalsIgnoreCase("BoxCSS")) {
	////
	////				objectsCurrentState.CurrentBoxCSS = value;				
	////				Log.info("Objects CurrentBoxCSS set to:"+ objectsCurrentState.CurrentBoxCSS);
	////			}
	////
	////			if (param.equalsIgnoreCase("CSSname")) {
	////
	////				objectsCurrentState.CSSname = value;
	////
	////				Log.info("Objects CSSname set to:"+ objectsCurrentState.CSSname);
	////			}
	////
	////		/*
	////
	////			if (param.equalsIgnoreCase("CursorVisible")) {
	////
	////				objectsCurrentState.cursorVisible = cursorVisibleOptions.valueOf(value.toUpperCase());  //value;
	////
	////				Log.info("_______________________________________Objects visibility set to:"
	////						+ objectsCurrentState.cursorVisible);
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
	////*/
	////			if (param.equalsIgnoreCase("DefaultURL")) {
	////
	////				objectsCurrentState.ObjectsCurrentURL = value;
	////
	////				Log.info("Objects ObjectsCurrentURL set to:"
	////						+ objectsCurrentState.ObjectsCurrentURL);
	////			}
	/////*
	////			if (param.equalsIgnoreCase("DefaultText")) {
	////
	////				objectsCurrentState.ObjectsCurrentText = value;
	////
	////				Log.info("Objects ObjectsCurrentText set to:"
	////						+ objectsCurrentState.ObjectsCurrentText);
	////			}
	////*/
	////			if (param.equalsIgnoreCase("NextParagraphObject")) {
	////
	////				objectsCurrentState.NextParagraphObject = value;
	////
	////				Log.info("Objects NextParagraphObject set to:"
	////						+ objectsCurrentState.NextParagraphObject);
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
	////			}*/
	////
	////		}
	//		Log.info("done loading dialog specific params");
	//
	//	}

}
