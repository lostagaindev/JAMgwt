package com.lostagain.JamGwt.JargScene.debugtools;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


import com.google.gwt.dev.util.Empty;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.InstructionProcessing.ActionSet;
import com.lostagain.Jam.InstructionProcessing.ActionSet.Trigger;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;
import com.lostagain.JamGwt.JargScene.SceneTextObject;
import com.lostagain.JamGwt.JargScene.SceneWidgetVisual;

import lostagain.nl.spiffyresources.client.IsSpiffyGenericLogBox;

/** for displaying/debugging data global to the scene and not any one specific object
 * **/
public class SceneDataBox extends VerticalPanel {

	public static Logger Log = Logger.getLogger("JAM.SceneDataBox");

	//contains all the types of data
	StackPanel dataContainer = new StackPanel();

	//scene controls
	VerticalPanel scenecontrols = new VerticalPanel();

	//scene state goes in here
	VerticalPanel scenestate = new VerticalPanel();

	//lists for various types of object go in these panels
	VerticalPanel allSceneObjects = new VerticalPanel();
	VerticalPanel allSpriteObjects = new VerticalPanel();
	VerticalPanel allTextObjects = new VerticalPanel();
	VerticalPanel allDialogueObjects = new VerticalPanel();
	VerticalPanel allDivObjects = new VerticalPanel();
	VerticalPanel allDynamiclyCreatedObjects = new VerticalPanel();
	VerticalPanel allObjectsWithClickWhileBehind = new VerticalPanel();

	//buttons to do stuff!
	Button generatedCode = new Button("Generate Scenes Jam Code (incomplete)");
	Button clearInventory = new Button("Clear Inventorys");
	Button resetScene = new Button("Reset Scene (does not reset inventorys)");

	Button saveStateTemp = new Button("Save State Snapeshot");
	Button loadStateTemp = new Button("Load State Snapeshot");

	//abortPanButton
	Button abortPanButton = new Button("Abort Current Pan");
	
	//lets the editor manually enter commands
	TextArea commandLineBox = new TextArea();
	Button   commandLineBoxRun = new Button("(run above commands)");
	//keep a cache of manual commands, as a script editor might want keep testing the same ones
	static ArrayList<String> manualCommandCache = new ArrayList<String>();
	ListBox commandCacheList = new ListBox();
	
	//manually fire this objects triggers 
	Label lab_FireThisObjectsTrigger = new Label("Fire a trigger of this object:");
	HorizontalPanel fireTriggerPan = new HorizontalPanel();
	ListBox triggerList = new ListBox();
	Button   fireTriggerRun = new Button("(Fire)");

	//manually fire this objects scenes triggers 
	Label lab_FireThisObjectsSceneTrigger = new Label("Fire A Trigger Of This Scene:");
	HorizontalPanel fireSceneTriggerPan = new HorizontalPanel();
	ListBox sceneTriggerList = new ListBox();
	Button   sceneFireTriggerRun = new Button("(Fire)");

	//manually fire global triggers 
	Label lab_GlobalTrigger = new Label("Fire Global Trigger:");
	HorizontalPanel fireGlobalTriggerPan = new HorizontalPanel();
	ListBox globalTriggerList = new ListBox();
	Button   globalFireTriggerRun = new Button("(Fire)");

	//the scene which this is the data off
	SceneWidgetVisual sourceScene;

	//logbox for the scene
	ScrollPanel scenesLogBox = new ScrollPanel();

	//flag to say if any scene data has been stored in a tempstate
	//if there is we enable a button later so the user can restore too it
	static Boolean aStateHasBeenSaved =false;

	//helper popup for typing script commands (created when needed)
	JAMScriptHelperPopup newHelperPop;

	private SceneObject sourceObject;


	public SceneDataBox(final SceneWidget objectsSourceScene,SceneObject sourceobject) {
		super();
		//ensure this box is clear
		this.clear();

		Log.info("adding SceneDataBox;");
		
		//set the source scene
		this.sourceScene=(SceneWidgetVisual) objectsSourceScene;
		this.sourceObject=sourceobject;
		//set this databox to be filled with the datacontainer stack panel
		this.add(dataContainer);

		
		//clear that panel
		dataContainer.clear();

		//first add scene controls and state
		dataContainer.add(scenecontrols,"scenecontrols");
		dataContainer.add(scenestate,"scene state");

		//button to manually trigger updating the lists (ie, scene objects, scene state etc)
		Button refreshLists = new Button("Refresh inspector data");
		refreshLists.getElement().getStyle().setFontSize(115, Unit.PCT);
		refreshLists.getElement().getStyle().setMarginBottom(5, Unit.PX);


		refreshLists.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				update();

			}
		});

		//add this new refresh lists button
		scenecontrols.add(refreshLists);
		scenecontrols.setCellHorizontalAlignment(refreshLists, HasAlignment.ALIGN_CENTER);

		
		abortPanButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				
				sourceScene.abortCurrentPan();

				update();
			}
		});
		
		//add all the refresh lists into the disclosure panels under the correct headings
		//dataContainer.add(allSceneObjects,"all objects");
		//dataContainer.add(allSpriteObjects,"all sprites");
		//dataContainer.add(allTextObjects,"all text");
		//dataContainer.add(allDialogueObjects,"all dialogue");
		//dataContainer.add(allDivObjects,"all divs");

		//dataContainer.add(allDynamiclyCreatedObjects,"all dynamicly created stuff");

		//add the objects log (which is controlled in the source object)
		//to the scroll panel created above
		if (sourceScene!=null){

			scenesLogBox.add((IsSpiffyGenericLogBox)sourceScene.ScenesLog);
			sourceScene.ScenesLog.log("log for "+sourceScene.SceneFileName+" added to inspector");

			//set some relivant styles on the scrollpanel
			scenesLogBox.setHeight("700px");
			scenesLogBox.setWidth("460px");
			scenesLogBox.getElement().getStyle().setProperty("wordBreak", "break-all");
			dataContainer.add(scenesLogBox,"(scenes log)");

		}

		//set the sizes of stuff to fill their parent containers
		this.setSize("100%", "100%");
		dataContainer.setSize("100%", "100%");
		dataContainer.setStyleName("debugStackPanel");


		Log.info("setting styles..");
		
		//make sure the overflows arnt visible
		//cant quite remember while this is needed.
		//but its just CSS-y stuff
		dataContainer.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		allSceneObjects.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		allSpriteObjects.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		allTextObjects.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		allDialogueObjects.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		allDivObjects.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		allDynamiclyCreatedObjects.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		//add scene control buttons
		scenecontrols.add(generatedCode);

		scenecontrols.add(clearInventory);
		scenecontrols.add(resetScene);

		//add load and save state bar		
		HorizontalPanel loadAndSaveState = new HorizontalPanel();
		loadAndSaveState.setWidth("100%");
		loadAndSaveState.add(saveStateTemp);
		loadAndSaveState.add(loadStateTemp);


		scenecontrols.add(loadAndSaveState);
		//disable temp state button by default
		loadStateTemp.setEnabled(false);

		Log.info("setting buttons..");
		
		clearInventory.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				for (InventoryPanelCore inventory : JAMcore.allInventorys.values()) {
					inventory.ClearInventory();
				}

			}
		});

		//reset scene button
		resetScene.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {

				//start the scene reset
				sourceScene.resetScene();

				//update lists after reset
				update();

			}
		});

		//looad a previously stored temp state for this scene
		loadStateTemp.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {

				//update from the internal save string
				sourceScene.loadSceneTempState();
				//update lists
				update();

			}
		});

		//save a temp state for this scene
		saveStateTemp.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {

				//update the internal save string
				sourceScene.saveSceneState();


				//remember we have a state saved
				aStateHasBeenSaved= true;

				//enable load button for it
				loadStateTemp.setEnabled(true);

				//clear all object inspectors that are open
				//not sure why this is done to be honest
				//I think we needed to force a refresh somewhere
				//and this was easier
				clearScenesInspectors(sourceScene);


			}


		});

		commandLineBox.setText("(run game commands here)");	
		commandLineBox.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {			
				if (commandLineBox.getText().equalsIgnoreCase("(run game commands here)")){
					commandLineBox.setText("");

				}
			}
		});

		commandLineBox.addFocusHandler( new FocusHandler() {			
			@Override
			public void onFocus(FocusEvent event) {				
				//disable key events
				JAMcore.setIgnoreKeyPresses(true);

				//if (newHelperPop!=null ){
				//	newHelperPop.setCurrentEditBox(commandLineBox);
				//}

			}
		});

		commandLineBox.addBlurHandler(new BlurHandler() {			
			@Override
			public void onBlur(BlurEvent event) {
				JAMcore.setIgnoreKeyPresses(false);

			}
		});


		commandLineBox.addKeyPressHandler(new KeyPressHandler() {			
			@Override
			public void onKeyPress(KeyPressEvent event) {

				if (event.getNativeEvent().getKeyCode()==45){//45 keycode for -

					if ( newHelperPop==null){ 

						Log.info("newHelperPop is null,creating");

						newHelperPop = new JAMScriptHelperPopup();

						newHelperPop.setCurrentEditBox(commandLineBox);
						//also send that first keystroke
						newHelperPop.manuallySendKeyPress(event);
						newHelperPop.showRelativeTo(commandLineBox);


					} else if (!newHelperPop.isShowing( )){

						Log.info("newHelperPop is showing");
						//also send that first keystroke
						newHelperPop.resetHelp(); //ensure any existing help is cleared as we are starting a new word

						newHelperPop.setCurrentEditBox(commandLineBox); //should already be set but make sure
						newHelperPop.manuallySendKeyPress(event);						
						newHelperPop.showRelativeTo(commandLineBoxRun);


					}

				}

				//shift enter
				if (    event.isShiftKeyDown() 
						&& event.getNativeEvent().getKeyCode()==KeyCodes.KEY_ENTER){
					//same as pressingthe button
					commandLineBoxRun.click();
				}

			}
		});

		commandLineBox.getElement().getStyle().setMarginTop(5, Unit.PX);		
		commandLineBox.setWidth("100%");
		commandLineBox.setHeight("35px");


		scenecontrols.add(commandLineBox);
		//	 commandLineBoxRun 
		commandLineBoxRun.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				
				String instructions = commandLineBox.getText().trim();

				//get the object this box belongs too atm					
				InstructionProcessor.processInstructions(instructions, "FromEditorsBox", sourceObject);
				
				//save commands if different?
				
				for (String existingCommands : manualCommandCache) {
					if (existingCommands.equals(instructions)){
						return; //it already there so dont add it
					}					
				}
				manualCommandCache.add(instructions); //add to cache
				commandCacheList.addItem(instructions.substring(0, 40));
				
			}
		});
		commandCacheList.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				int num=commandCacheList.getSelectedIndex();
				commandLineBox.setText(manualCommandCache.get(num));
				
			};
		});
		

		Log.info("setting manual commands..");
		
		//populate with existing manual commands if any
		for (String existingCommands : manualCommandCache) {
			commandCacheList.addItem(existingCommands.substring(0, 40));			
			
		}
		
		//commandCacheList
		HorizontalPanel runAndCache = new HorizontalPanel();		
		runAndCache.add(commandLineBoxRun);
		runAndCache.add(commandCacheList);
		
		scenecontrols.add(runAndCache);		
		scenecontrols.setWidth("100%");
		scenecontrols.getElement().getStyle().setPadding(10, Unit.PX);


		//Manual Triggering:

		//manually run a trigger of this object
		scenecontrols.add(lab_FireThisObjectsTrigger);
		scenecontrols.add(fireTriggerPan);
		fireTriggerPan.add(triggerList);
		fireTriggerPan.add(fireTriggerRun);

		//populate trigger list		

		Log.info("populating trigger lists..");
		
		if (sourceObject.objectsActions!=null && !sourceObject.objectsActions.isEmpty()){
			
			for (ActionSet actionSet : sourceObject.objectsActions) {			
				ArrayList<Trigger> triggers = actionSet.getTriggers();
				
				if (triggers.isEmpty()){
					Log.info("trigger list empty somehow..");
					Log.info("commands.:"+actionSet.CommandsInSet.getCode());
					continue;
				}
				
				//normally theres just one trigger per actionset, we have support for more in future though
				triggerList.addItem(triggers.get(0).toString());						
			}
			
		}

		fireTriggerRun.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				//get requested trigger
				int index=triggerList.getSelectedIndex();
				ActionSet actionSet = sourceObject.objectsActions.get(index);
				InstructionProcessor.processInstructions(actionSet.getActionsArray(), "FromEditorsBox", sourceObject);
			}
		});
		//--
		//manually run a scene trigger from this object
		if (sourceScene!=null){

			Log.info("adding scene triggers;");

			scenecontrols.add(lab_FireThisObjectsSceneTrigger);
			scenecontrols.add(fireSceneTriggerPan);
			fireSceneTriggerPan.add(sceneTriggerList);
			fireSceneTriggerPan.add(sceneFireTriggerRun);

			//populate trigger list		
			if (sourceScene.sceneActions!=null && !sourceScene.sceneActions.isEmpty()){
				for (ActionSet actionSet : sourceScene.sceneActions) {			
					ArrayList<Trigger> triggers = actionSet.getTriggers();
					if (triggers.size()==0){
						Log.info("empty scene triggers;"+actionSet.toString());
						continue;
					}
					
					//normally there's just one trigger per actionset, we have support for more in future though
					sceneTriggerList.addItem(triggers.get(0).toString());						
				}
			}

			sceneFireTriggerRun.addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					//get requested trigger
					int index=sceneTriggerList.getSelectedIndex();
					ActionSet actionSet = sourceScene.sceneActions.get(index);
					InstructionProcessor.processInstructions(actionSet.getActionsArray(), "FromEditorsBox", sourceObject);
				}
			});

		}
		//--
		//manually run a global trigger
		scenecontrols.add(lab_GlobalTrigger);
		scenecontrols.add(fireGlobalTriggerPan );
		fireGlobalTriggerPan.add(globalTriggerList );
		fireGlobalTriggerPan.add(globalFireTriggerRun );

		Log.info("adding global triggers;");
		
		//populate trigger list		
		if (InstructionProcessor.globalActions!=null && !InstructionProcessor.globalActions.isEmpty()){
			for (ActionSet actionSet : InstructionProcessor.globalActions) {			
				ArrayList<Trigger> triggers = actionSet.getTriggers();
				if (triggers.size()==0){
					Log.info("empty global triggers;"+actionSet.toString());
					continue;
				}
			
				//normally theres just one trigger per actionset, we have support for more in future though
				globalTriggerList.addItem(triggers.get(0).toString());						
			}
		}

		globalFireTriggerRun.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				//get requested trigger
				int index=globalTriggerList.getSelectedIndex();
				ActionSet actionSet = InstructionProcessor.globalActions.get(index);
				InstructionProcessor.processInstructions(actionSet.getActionsArray(), "FromEditorsBox", sourceObject);
			}
		});
		//--				
		//---------------------
		//------------
		//-------
		//---		

		//this will be used to attempt to recreate the complete scene code in the same format as a jam file
		//it loops over each object making their code.
		//atm object code string generation is incomplete however
		generatedCode.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				generateCodeForAllSceneObjects();

			}
		});

		//enable the load button if theres a state saved
		if (aStateHasBeenSaved){
			//enable load
			loadStateTemp.setEnabled(true);
		}


		Log.info("updating scene data..");
		//update the data lists etc
		update();


	}

	/**this will be used to attempt to recreate the complete scene code in the same format as a jam file
	 **it loops over each object making their code.
	 **atm object code string generation is incomplete however**/

	protected void generateCodeForAllSceneObjects() {

		SceneSourceCode newdata = new SceneSourceCode(sourceScene);

		newdata.center();

	}
	public void update(){
		update(null,null);
	}
	public void update(SceneObject sourceObject, SceneWidgetVisual newSceneIfChanged){
		this.sourceObject = sourceObject; //used purely so we know where to send commands to if using the built in command running console

		Log.info("updating scene data in inspector");

		if (sourceScene==null){
			Log.info("no source scene, probably is inventory item");
			return;
		}

		//update source scene in case object moved between scenes
		if (newSceneIfChanged!=null && newSceneIfChanged!=sourceScene){

			sourceScene=newSceneIfChanged;

			//update scenes logbox
			scenesLogBox.clear();
			scenesLogBox.add((IsSpiffyGenericLogBox)sourceScene.ScenesLog);
			sourceScene.ScenesLog.log("log for "+sourceScene.SceneFileName+" added to inspector");
		}

		dataContainer.setStackText(0, "Controls ");


		// clear things already in the panel (we dont want to keep adding the same stuff!)
		scenestate.clear();
		Log.info("updating scene data in inspector...");

		//add some basic scene state information into the scenestate vertical panel
		if (sourceScene!=null){ //we might not have a scene
			scenestate.add(abortPanButton);
			scenestate.add(new Label("Object belongs to Scene Name:"+sourceScene.SceneFileName));
			scenestate.add(new Label("Source Location:"+sourceScene.getPosX()+","+sourceScene.getPosY()));
		}

		Log.info("updating scene data 2");
		SceneWidget currentScene = SceneObjectDatabase.currentScene;

		if (currentScene!=null  && !currentScene.Loading){ //quick check - its theoretically possible we are updating while a scene is loading 

			Log.info("updating scene data 3");

			scenestate.add(new Label("------------------------------"));
			scenestate.add(new Label("Current Scene:"));			
			scenestate.add(new Label("Scene Name:"+currentScene.getScenesData().SceneFolderName));
			scenestate.add(new Label("Scene Movement Limits:"+currentScene.getScenesData().getMovementLimitsAsString()));
			scenestate.add(new Label("Scene Background:"+currentScene.getScenesData().currentState.currentBackground));
			scenestate.add(new Label("Current Container Background:"+currentScene.getScenesData().containerBackground));
			scenestate.add(new Label("Scene Friction:"+currentScene.getDefaultSceneFriction()));			
			scenestate.add(new Label("Scene PhysicsBias:"+currentScene.getScenesData().physicsBias));

			
			//state

			scenestate.add(new Label("------------------------------"));
			scenestate.add(new Label("Scene status:"+currentScene.getSceneStatus().seralise()));


		}


		//	scenestate.add(new Label("Source Serialised State:"+sourceScene.currentState.seralise()));

		//below is some simple test of the new functions to get objects of certain types on the scene
		/*
		int NumOfSprites = sourceScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Sprite).size();
		int NumOfLabels =  sourceScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Label).size();
		int NumOfDialogue =sourceScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.DialogBox).size();
		int NumOfInputs =  sourceScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Input).size();
		int NumOfDivs =    sourceScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Div).size();

		scenestate.add(new Label("Test of SceneObject Quanitys: sprites:"+NumOfSprites+" labels:"+NumOfLabels+" Dia:"+NumOfDialogue+" Input:"+NumOfInputs+" Div:"+NumOfDivs));

		 */


		//we now delete everything under this point as it will be replaced
		while (dataContainer.getWidgetCount()>3){
			dataContainer.remove(3);
		};


		dataContainer.add(allSceneObjects,"all objects");
		updateObjectList(allSceneObjects,sourceScene.getScenesData().scenesOriginalObjects);
		dataContainer.setStackText(dataContainer.getWidgetCount()-1, "All "+sourceScene.getScenesData().scenesOriginalObjects.size()+" original scene objects");

		//new method to populate list of scene objects;

		//first we get ALL the scene objects
		Set<SceneObject> objects = sourceScene.getScenesData().allScenesCurrentObjects();


		//then we define a hashmap of verticalpanels with object type as the key
		HashMap<SceneObjectType,VerticalPanel> objectPanels = new HashMap<SceneObjectType,VerticalPanel>();	
		//(this will become the vertical panels in each of the stack slots, we use object type as the key to make it easy
		//to find the right panel to put the objects into)

		// now we loop over each object
		for (SceneObject so : objects) {

			//make a label to add to a panel
			//Label newlabel = new Label(so.getObjectsCurrentState().ObjectsName);
			ObjectDataBar objectLabel = new ObjectDataBar(so);

			//get the objects type so we know where to add it too
			SceneObjectType sotype = so.getObjectsCurrentState().getPrimaryObjectType();

			//so we have a vertical panel for this object type already?
			if (objectPanels.containsKey(sotype)){

				//find panel to add it too
				VerticalPanel panel = objectPanels.get(sotype); 

				//and add it
				panel.add(objectLabel);

				//update the panels title to reflect the count
				int panelsIndex = dataContainer.getWidgetIndex(panel);
				int total = panel.getWidgetCount();
				dataContainer.setStackText(panelsIndex,sotype.name()+" objects ("+total+")");

			} else {

				//create a panel and add it to it;
				VerticalPanel newpanel = new VerticalPanel();
				newpanel.add(objectLabel);
				objectPanels.put(sotype, newpanel);

				//add the panel to the stack too;
				dataContainer.add(newpanel,sotype.name()+" object");					

			}

		}



		//dynamic objects dealt with separately;
		dataContainer.add(allDynamiclyCreatedObjects,"all dynamicly created stuff");

		//	 Log.info("adding dynamic objects from:"+sourceScene.SceneFileName);

		//and objects that were dynamically created
		updateObjectList(allDynamiclyCreatedObjects,sourceScene.AllDynamicObjects);


		int totalTabsInStackSoFar = dataContainer.getWidgetCount();
		dataContainer.setStackText(totalTabsInStackSoFar-1, "All "+sourceScene.AllDynamicObjects.size()+" scenes dynamic objects");


		dataContainer.add(allObjectsWithClickWhileBehind,"all objects with OnClickWhileBehind");		
		updateObjectList(allObjectsWithClickWhileBehind,sourceScene.getScenesData().scenesObjectsThatSupportClicksWhileBehind);
		totalTabsInStackSoFar = dataContainer.getWidgetCount();
		dataContainer.setStackText(totalTabsInStackSoFar-1, "All "+sourceScene.getScenesData().scenesObjectsThatSupportClicksWhileBehind.size()+" OnClickWhileBehind objects");







		/**
		//------------------
		//populate all sprite list
		 //we make a new copy so we can sort it without changing the original lists order
		 ArrayList<SceneObjectVisual> allspriteobjects = new ArrayList<SceneObjectVisual>();

		 Log.info("adding sprite objects from:"+sourceScene.SceneFileName);
		 allspriteobjects.addAll(sourceScene.getScenesData().sceneSpriteObjects);

		 //sort into alphabetical order using the alphabetical comparitor we defined in the ObjectInspector class
		 Collections.sort(allspriteobjects, ObjectInspector.alphabeticalObjects);

		 updateObjectList(allSpriteObjects,allspriteobjects);
		 dataContainer.setStackText(3, "All "+sourceScene.getScenesData().sceneSpriteObjects.size()+" scenes sprite objects");

			//------------------
			//populate all text list
		//we make a new copy so we can sort it without changing the original lists order
		 ArrayList<SceneObjectVisual> alltextobjects = new ArrayList<SceneObjectVisual>();	

		 Log.info("adding text objects from:"+sourceScene.SceneFileName);
		 alltextobjects.addAll(sourceScene.getScenesData().SceneTextObjects);

		 //sort into alphabetical order using the alphabetical comparitor we defined in the ObjectInspector class
		 Collections.sort(alltextobjects, ObjectInspector.alphabeticalObjects);

			 updateObjectList(allTextObjects,alltextobjects);
			 dataContainer.setStackText(4, "All "+sourceScene.getScenesData().SceneTextObjects.size()+" scenes text objects");


				//------------------
				//populate all dialogue list
			//we make a new copy so we can sort it without changing the original lists order
			 ArrayList<SceneObjectVisual> alldialogueobjects = new ArrayList<SceneObjectVisual>();	

			 Log.info("adding  dialogue objects from:"+sourceScene.SceneFileName);
			 alldialogueobjects.addAll(sourceScene.getScenesData().SceneDialogObjects);

			 //sort into alphabetical order using the alphabetical comparitor we defined in the ObjectInspector class
			 Collections.sort(alldialogueobjects, ObjectInspector.alphabeticalObjects);

			 updateObjectList(allDialogueObjects,alldialogueobjects);
			dataContainer.setStackText(5, "All "+sourceScene.getScenesData().SceneDialogObjects.size()+" scenes dialogue objects");

			//populate remainder (unsorted atm)
			 Log.info("adding div objects from:"+sourceScene.SceneFileName);
				//populate all div list
			 updateObjectList(allDivObjects,sourceScene.getScenesData().SceneDivObjects);
			 dataContainer.setStackText(6, "All "+sourceScene.getScenesData().SceneDivObjects.size()+" scenes div objects");


		 **/





	}




	/** 
	 * closes all the inspectors and removes their data ready to be recreated freshly when next opened. 
	 * Normally when closed/reopened they are just hidden/shown rather then remade.
	 * By seeing an objects object inspector to null, it ensures it needs to be remade
	 * @param sourceScene2 
	 ***/
	static void clearScenesInspectors(SceneWidget sw) {

		for (SceneObject so : sw.getScenesData().scenesOriginalObjects) {

			so.clearObjectsInspector();					

		}

	}



	/** fills a vertical panel with data from a list of objects **/
	private void updateObjectList(VerticalPanel updateThis, Collection<? extends SceneObject> scenesOriginalObjects) {



		//	Log.info("updateing scene object list");

		//clear the supplied panel of anything already on it
		updateThis.clear();

		//get the iterator for the supplied object list
		Iterator<? extends SceneObject> sit = scenesOriginalObjects.iterator();





		//	Log.info("list has:"+withThis.size());
		//updateThis.add(new Label("total objects = "+withThis.size()));

		//loop over panel adding each objects name to it
		while (sit.hasNext()) {

			SceneObject sceneObject = sit.next();

			//object shouldnt be null, but we check here anyway in case something went wrong
			if (sceneObject==null){

				Log.severe("ERROR:SCENE OBJECT IN LIST IS NULL");
				throw new NullPointerException(); 

			}
			//get the name
			//String itemName = sceneObject.getObjectsCurrentState().ObjectsName;
			//make a label from it to add to the vertical panel
			//Label ObjectNameLabel = new Label(itemName);

			//We now use a objectdata bar, which allows clickin to open the objects inspector
			ObjectDataBar objectLabel = new ObjectDataBar(sceneObject);


			updateThis.add(objectLabel);

		}


	}






}
