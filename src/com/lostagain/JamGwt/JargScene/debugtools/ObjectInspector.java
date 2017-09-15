package com.lostagain.JamGwt.JargScene.debugtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.base.Optional;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.CollisionMap.PolygonCollisionMap;
import com.lostagain.Jam.CollisionMap.SceneCollisionMap;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.InventoryObjectState;
import com.lostagain.Jam.SceneObjects.SceneDialogueObjectState;
import com.lostagain.Jam.SceneObjects.SceneDivObjectState;
import com.lostagain.Jam.SceneObjects.SceneInputObjectState;
import com.lostagain.Jam.SceneObjects.SceneLabelObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.SceneSpriteObjectState;
import com.lostagain.Jam.SceneObjects.SceneVectorObjectState;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneDialogueObject;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneDivObject;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneInputObject;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneLabelObject;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneObject;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneSpriteObject;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneVectorObject;
import com.lostagain.JamGwt.TitledPopUpWithShadow;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryItem;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryPanel;
import com.lostagain.JamGwt.JargScene.SceneDialogObject;
import com.lostagain.JamGwt.JargScene.SceneDivObject;
import com.lostagain.JamGwt.JargScene.SceneInputObject;
import com.lostagain.JamGwt.JargScene.SceneLabelObject;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;
import com.lostagain.JamGwt.JargScene.SceneTextObject;
import com.lostagain.JamGwt.JargScene.SceneVectorObject;

import com.lostagain.JamGwt.JargScene.SceneWidgetVisual;
import com.lostagain.JamGwt.JargScene.CollisionMap.CollisionDebugBox;


import lostagain.nl.spiffyresources.client.IsSpiffyGenericLogBox;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyWormhole;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyWormhole.IncomingMessageHandler;


import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyWormhole;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyWormhole.IncomingMessageHandler;

/**
 * Designed for game debugging, and to assist in construction, the object inspector<br>
 * has all the information for the current objects state, as well as the ability<br>
 * to save/restore that data.<br>
 * It also shows scene data, and scene reset options.<br>
 * The inspector is created for an object on request and once created auto-updates.<br>
 * Unless its created, however, it does not exist 
 * 
 * @author Tom  **/

public class ObjectInspector extends TitledPopUpWithShadow {

	private static final String OBJECT_LIST = "Object List";


	public static Logger Log = Logger.getLogger("JAM.ObjectInspector");
	


	//displays the objects Jamcode deduced from current data.
	//Experimental only, really doesn't work at the moment as its stupidly out of date
	final Button DisplayCodeBut = new Button("ShowCode");

	/** This button allows you to edit a temp cmap for the object,
	 * a new window pops up to show this data<br>
	 * Left click to add points, right click to finish editing <br>
	 * you could then copy the data it generates into a real file **/
	final Button EditCmapButton = new Button("Edit CMap");

	//triggers a clone?
	final Button Clone = new Button("Clone");

	/** a dropdown list of all the scenes objects, letting you pop up another inspector
	 * for that object by selecting one**/
	final ListBox allSceneObjects = new ListBox();

	//the data for the overall scene is displayed here
	SceneDataBox ScenesDataBox;

	//a scroll panel to put the scene databox into
	ScrollPanel ScenesGameBoxScroll = new ScrollPanel();

	//create the game data box here, which displays stuff global to the whole game
	static GameDataBox gameDataBox = null; 

	/** toggles the edit mode on/off, this allows you to move
	 * an object about by dragging or using the arrow keys to nudge it **/
	final ToggleButton EditModeToggle = new ToggleButton("(Edit off)",
			"(Edit on)");

	//The various types of boxs to put the current object data into
	SpiffyObjectDataBox currentataBox;
	
	SceneSpriteObjectDataBox currentSpriteDataBox;
	SceneDialogObjectDataBox currentDiaDataBox;
	SceneDivObjectDataBox currentDivDataBox;
	SceneVectorObjectDataBox currentVectorDataBox;
	SceneInputObjectDataBox currentInputDataBox;
	SceneLabelObjectDataBox currentLabelDataBox;
	InventoryIconObjectDataBox currentInventoryIconDataBox;
	
	//The various types of boxs to put the initial object data into. (ie, what the objects data was when it was first loaded)
	SpiffyObjectDataBox initialBox;
	
	SceneSpriteObjectDataBox initialSpriteDataBox;
	SceneDialogObjectDataBox initialDiaDataBox;
	SceneDivObjectDataBox initialDivDataBox;
	SceneVectorObjectDataBox initialVectorDataBox;
	SceneInputObjectDataBox initialInputDataBox;
	SceneLabelObjectDataBox initialLabelDataBox;
	InventoryIconObjectDataBox initialInventoryIconDataBox;
	
	//and the save data
	SpiffyObjectDataBox savedBox;
	
	SceneSpriteObjectDataBox savedSpriteDataBox;
	SceneDialogObjectDataBox savedDiaDataBox;
	SceneDivObjectDataBox savedDivDataBox;
	SceneVectorObjectDataBox savedVectorDataBox;
	SceneInputObjectDataBox savedInputDataBox;
	SceneLabelObjectDataBox savedLabelDataBox;
	InventoryIconObjectDataBox savedInventoryIconDataBox;
	

	/**the source object (ie, what you are inspecting right now **/
	SceneObjectVisual sourceobj;

	public SceneObjectVisual getSourceobj() {
		return sourceobj;
	}




	/** number of open inspector boxes like this **/
	public static int numOfInspectorsOpen = 0;

	/** the panel storing the controls at the top **/
	HorizontalPanel controlbar = new HorizontalPanel();



	//	VerticalPanel scenesObjectList = new VerticalPanel();

	//	Label scenesResetList = new Label();

	/**  flag that determains if the external debug handler has been set up yet
	 * The external handler is a JSNI (that is, native javascript interface) to
	 * a outside game editor like the iJammer. **/
	Boolean notSetupYet = true;

	/**method for comparing objects and putting them alphabetically.
	 * A comparator is just a way to compare things, mostly used so you can sort them in order.
	 *  **/
	static Comparator<IsSceneObject> alphabeticalObjects =
			new Comparator<IsSceneObject>(){
		@Override
		public int compare(IsSceneObject so1, IsSceneObject so2) {
			//get the names of both objects
			String name1 = so1.getObjectsCurrentState().ObjectsName;
			String name2 = so2.getObjectsCurrentState().ObjectsName;
			//magic compare to function. I dunno how it works. 
			//it seems to return a number - positive or negative depending
			//on if something is alphabetically in front or behind the other in
			//the alphabet.
			//we use it here to compare the names
			return  name1.compareToIgnoreCase(name2);				
		}

	};

	/**for editing the collision map
	 * this is the current handler for left and right clicks**/
	static private HandlerRegistration currentCmapHandler;
	/**for editing the collision map.
	 * the current path **/
	public String currentPath="";
	
	/**for editing the collision map - this is the current inspector whos objects cmap is being edited.
	 * **/
	static public ObjectInspector currentCmapBeingEdited= null;



	/** creates a new inspector for the scene object **/
	public ObjectInspector(final SceneObjectVisual sourceObject) {


		//set up the title and size of this popup window
		//looks a bit of a mess, but its fairly simple 
		super(null, "450px", "500px", sourceObject.getObjectsCurrentState().ObjectsName + "("
				+ sourceObject.getObjectsCurrentState().ObjectsFileName + ")", new Label("loading..."), true);
			
		super.getElement().getStyle().setZIndex(200000); //ensure in front of everything, including overlays
		super.setMiniumZIndex(200000);//increase max to that value

		Log.info("Creating Inspector Inspector");

		//ensure we enable selection (default disabled for our popups)
		super.setContentsSelectable(true);


		//store the source object internally (allows us to look at its data from any method in this inspector
		sourceobj = sourceObject;

		//for connecting to the iJammer.
		//uses the Spiffywormhole technology! 
		if (notSetupYet){

			setupExternalDebuggerHandler();	
			notSetupYet=false;

		}

		// set up data object box

		//set up the tab bar at the top of the inspector box to put the various
		//bits of information into
		TabPanel tabbypanel = new TabPanel();
		tabbypanel.getTabBar().setStylePrimaryName("Inspector-TabBar");
		tabbypanel.setWidth("100%");
		Log.info("Preparing databox:");

		//Now we prepare the data in the box based on what type of object the source object is.
		//We need to do this differently for each, as different objects have different setup parameters and propertys in them

		// if this inspector was opened by clicking on sprite object class of object		
		if (sourceObject.getClass() == SceneSpriteObject.class) {

			//get get its current data
			SceneSpriteObjectState currentdata = ((IsSceneSpriteObject) sourceObject).getObjectsCurrentState();

			//and its initial data (that is, data when its first loaded)
			SceneSpriteObjectState intdata = ((IsSceneSpriteObject) sourceObject).getInitialState();

			//and its saved data,which is the snapshot of the object taken if a temp state was saved
			SceneSpriteObjectState saveddata = ((IsSceneSpriteObject) sourceObject).getTempState();

			//create a new Sprite data box, to do this we have to typecast that
			//we are supplying a sprite object as the sourceObject
			//Normally you should try not to type cast things, but as we know its
			//a sprite object from the IF statement above, its save to do here.
			//The typecast tells the code that the sourceObject is really a SceneSpriteObject,
			//which in turn lets the Spriteobject data box get sprite-specific data, rather
			//then just the generic SceneObject data
			currentSpriteDataBox = new SceneSpriteObjectDataBox(((SceneSpriteObject) sourceObject),
					currentdata);
			//turn off the edit mode initially
			currentSpriteDataBox.setEditMode(false);
			//turn of the ability to rest the data to a previous, as this is the current
			//state box and it would make no sense (note; we dont do this on the initial state box)
			currentSpriteDataBox.setResetEnable(false);

			//store the newly created databox in the variable currentataBox
			//currentataBox is simply currentdataBox with a typo
			//feel free to rename :)
			currentataBox = currentSpriteDataBox;

			//we now make the initial state box much like the above
			initialSpriteDataBox = new SceneSpriteObjectDataBox(((SceneSpriteObject) sourceObject),
					intdata);
			initialSpriteDataBox.setEditMode(false);

			//and also store it in a variable
			initialBox = initialSpriteDataBox;

			//if theres existing saved data, we have a box (and thus a tab) for that data too
			if (saveddata!=null){
				sourceObject.ObjectsLog("state added to inspector:seralised to:" + saveddata.serialiseToString(),"green");
				
				
				savedSpriteDataBox = new SceneSpriteObjectDataBox(((SceneSpriteObject) sourceObject),
						saveddata);
				
				
				savedSpriteDataBox.setEditMode(false);
				savedBox = savedSpriteDataBox;
			}

		}

		//Much like the above process we do much the same thing if its a text or div object instead of a sprite
		//----------------------------------------------------------------------------------

		//text object or dialogue object which have all the same data really so we display them with the same type of box
		if (sourceObject.getClass() == SceneTextObject.class
				|| sourceObject.getClass() == SceneDialogObject.class) {
			Log.info("preparing text or dialogue data box:");

			SceneDialogueObjectState currentdata = ((IsSceneDialogueObject) sourceObject).getObjectsCurrentState();
			SceneDialogueObjectState intdata     = ((IsSceneDialogueObject) sourceObject).getInitialState();
			SceneDialogueObjectState saveddata   = ((IsSceneDialogueObject) sourceObject).getTempState();

			currentDiaDataBox = new SceneDialogObjectDataBox(currentdata,
					((SceneDialogObject) sourceObject));

			currentDiaDataBox.setEditMode(false);
			currentDiaDataBox.setResetEnable(false);
			currentataBox = currentDiaDataBox;

			Log.info("preparing initual text or dialogue data box:");

			initialDiaDataBox = new SceneDialogObjectDataBox(intdata,
					((SceneDialogObject) sourceObject));
			initialDiaDataBox.setEditMode(false);
			initialBox = initialDiaDataBox;

			Log.info("preparing saved text or dialogue data box:");
			if (saveddata!=null){
				savedDiaDataBox = new SceneDialogObjectDataBox(saveddata,
						((SceneDialogObject) sourceObject));
				savedDiaDataBox.setEditMode(false);
				savedBox = savedDiaDataBox;
			}

		}

		//label
		if (sourceObject.getClass() == SceneLabelObject.class) {

			Log.info("preparing label data box:");


			SceneLabelObjectState currentdata = ((IsSceneLabelObject) sourceObject).getObjectsCurrentState();
			SceneLabelObjectState intdata     = ((IsSceneLabelObject) sourceObject).getInitialState();
			SceneLabelObjectState saveddata   = ((IsSceneLabelObject) sourceObject).getTempState();

			currentLabelDataBox = new SceneLabelObjectDataBox(currentdata,
					((SceneLabelObject) sourceObject));

			currentLabelDataBox.setEditMode(false);
			currentLabelDataBox.setResetEnable(false);
			currentataBox = currentLabelDataBox;

			Log.info("preparing initual text or dialogue data box:");

			initialLabelDataBox = new SceneLabelObjectDataBox(intdata,
					((SceneLabelObject) sourceObject));
			initialLabelDataBox.setEditMode(false);
			initialBox = initialLabelDataBox;

			Log.info("preparing saved text or dialogue data box:");
			if (saveddata!=null){
				savedLabelDataBox = new SceneLabelObjectDataBox(saveddata,
						((SceneLabelObject) sourceObject));
				savedLabelDataBox.setEditMode(false);
				savedBox = savedLabelDataBox;
			}

		}

		//div object
		if (sourceObject.getClass() == SceneDivObject.class) {

			SceneDivObjectState currentdata = ((IsSceneDivObject) sourceObject).getObjectsCurrentState();
			SceneDivObjectState intdata = ((IsSceneDivObject) sourceObject).getInitialState();
			SceneDivObjectState saveddata = ((IsSceneDivObject) sourceObject).getTempState();

			currentDivDataBox = new SceneDivObjectDataBox(currentdata,
					((SceneDivObject) sourceObject));
			currentDivDataBox.setEditMode(false);
			currentDivDataBox.setResetEnable(false);
			currentataBox = currentDivDataBox;

			initialDivDataBox = new SceneDivObjectDataBox(intdata,
					((SceneDivObject) sourceObject));
			initialDivDataBox.setEditMode(false);
			initialBox = initialDivDataBox;

			if (saveddata!=null){
				savedDivDataBox = new SceneDivObjectDataBox(saveddata,
						((SceneDivObject) sourceObject));
				savedDivDataBox.setEditMode(false);
				savedBox = savedDivDataBox;
			}

		}
		//Log.info("I sure could use a"+sourceObject.getClass().toString()+". Which is a vector, right?");
		if (sourceObject.getClass() == SceneVectorObject.class) {
			Log.info("Vector Inspector opening!!!");

			SceneVectorObjectState currentdata = ((IsSceneVectorObject) sourceObject).getObjectsCurrentState();
			Log.info(currentdata.objectsVectorString+" is a very very very very explicit string!");

			SceneVectorObjectState intdata = ((IsSceneVectorObject) sourceObject).getInitialState();
			Log.info(intdata.objectsVectorString+" is a very very very very very explicit string!");

			SceneVectorObjectState saveddata = ((IsSceneVectorObject) sourceObject).getTempState();
			//	Log.info(saveddata.ObjectsCurrentVectorString+" is a very very very very very very explicit string!");

			currentVectorDataBox = new SceneVectorObjectDataBox(currentdata,
					((SceneVectorObject) sourceObject));
			currentVectorDataBox.setEditMode(false);
			currentVectorDataBox.setResetEnable(false);
			currentataBox = currentVectorDataBox;

			Log.info("Creating initial box");

			initialVectorDataBox = new SceneVectorObjectDataBox(intdata,
					((SceneVectorObject) sourceObject));
			initialVectorDataBox.setEditMode(false);
			initialBox = initialVectorDataBox;

			if (saveddata!=null){
				savedVectorDataBox = new SceneVectorObjectDataBox(saveddata,
						((SceneVectorObject) sourceObject));
				savedVectorDataBox.setEditMode(false);
				savedBox = savedVectorDataBox;
			}

		}

		//input object
		if (sourceObject.getClass() == SceneInputObject.class) {

			SceneInputObjectState currentdata= ((IsSceneInputObject) sourceObject).getObjectsCurrentState();
			SceneInputObjectState intdata    = ((IsSceneInputObject) sourceObject).getInitialState();
			SceneInputObjectState saveddata  = ((IsSceneInputObject) sourceObject).getTempState();

			currentInputDataBox = new SceneInputObjectDataBox(currentdata,
					((SceneInputObject) sourceObject));
			currentInputDataBox.setEditMode(false);
			currentInputDataBox.setResetEnable(false);
			currentataBox = currentInputDataBox;

			initialInputDataBox = new SceneInputObjectDataBox(intdata,
					((SceneInputObject) sourceObject));
			initialInputDataBox.setEditMode(false);
			initialBox = initialInputDataBox;

			if (saveddata!=null){
				savedInputDataBox = new SceneInputObjectDataBox(saveddata,
						((SceneInputObject) sourceObject));
				savedInputDataBox.setEditMode(false);
				savedBox = savedInputDataBox;
			}

		}
		
		//inventory icon
		if (sourceObject.getClass() == InventoryItem.class) {
			Log.info("preparing InventoryIcon box:");

			//get get its current data
			InventoryObjectState currentdata = ((IsInventoryItem) sourceObject).getObjectsCurrentState();

			//and its initial data (that is, data when its first loaded)
			InventoryObjectState intdata     = ((IsInventoryItem) sourceObject).getInitialState();

			//and its saved data,which is the snapshot of the object taken if a temp state was saved
			InventoryObjectState saveddata   = ((IsInventoryItem) sourceObject).getTempState();
			
			if (currentdata==null){
				Log.info("current data is null!");
			}
			if (intdata==null){
				Log.info("inital data is null!");
			}
			if (saveddata==null){
				Log.info("save data is null!");
			}
			
			
			//create a new InventoryIcon data box, to do this we have to typecast that
			//we are supplying a InventoryIcon object as the sourceObject
			//Normally you should try not to type cast things, but as we know its
			//a InventoryIcon object from the IF statement above, its save to do here.
			//The typecast tells the code that the sourceObject is really a SceneInventoryIconObject,
			//which in turn lets the InventoryIconobject data box get InventoryIcon-specific data, rather
			//then just the generic SceneObject data
			currentInventoryIconDataBox = new InventoryIconObjectDataBox(currentdata,	((InventoryItem) sourceObject));
			if (saveddata==null){
				Log.info("Created main inventoryicon data box");
			}
			
			//turn off the edit mode initially
			currentInventoryIconDataBox.setEditMode(false);
			//turn of the ability to rest the data to a previous, as this is the current
			//state box and it would make no sense (note; we dont do this on the initial state box)
			currentInventoryIconDataBox.setResetEnable(false);

			//store the newly created databox in the variable currentataBox
			//currentataBox is simply currentdataBox with a typo
			//feel free to rename :)
			currentataBox = currentInventoryIconDataBox;

			//we now make the initial state box much like the above
			initialInventoryIconDataBox = new InventoryIconObjectDataBox(intdata,	((InventoryItem) sourceObject));
			initialInventoryIconDataBox.setEditMode(false);

			//and also store it in a variable
			initialBox = initialInventoryIconDataBox;
			Log.info("Preparing databox save:");

			//if theres existing saved data, we have a box (and thus a tab) for that data too
			if (saveddata!=null){
				savedInventoryIconDataBox = new InventoryIconObjectDataBox(saveddata,	((InventoryItem) sourceObject));
				savedInventoryIconDataBox.setEditMode(false);
				savedBox = savedInventoryIconDataBox;
			}

		}

		
		
		//----------
		//Now we have our databox for both current and initial data of  the specific object type
		//we make the rest of the inspector

		//set the background color and opacity of the top control bad
		controlbar.getElement().getStyle().setBackgroundColor("#a68bff");
		controlbar.getElement().getStyle().setOpacity(1);


		//we the zindex and padding of the box itself and the tab panel
		//super.getElement().getStyle().setZIndex(9999);
		tabbypanel.getElement().getStyle().setPadding(2, Unit.PX);

		//and some widths and heights for both current and initial data boxs
		currentataBox.setWidth("460px");
		initialBox.setWidth("460px");
		initialBox.setHeight("600px");
		currentataBox.setHeight("600px");

		//add them to the tab panel, given them the tab titles as the second parameter
		tabbypanel.add(currentataBox, " Current Data ");
		tabbypanel.add(initialBox, " Initial Data ");

		//if there was saved data, and thus a save box created, we add that to the tab panel as well
		if (savedBox!=null){
			tabbypanel.add(savedBox, " Save Data ");
		}

		//create a box for the objects log
		//(the objects log records some events that concern the object
		//which saves spamming the general console log too much)
		ScrollPanel ObjectsLogBox = new ScrollPanel();

		//add the objects log (which is controlled in the source object)
		//to the scroll panel created above
		ObjectsLogBox.add((IsSpiffyGenericLogBox)sourceObject.ObjectsLog);
		//set some relevant styles on the scrollpanel
		ObjectsLogBox.setHeight("700px");
		ObjectsLogBox.setWidth("460px");
		ObjectsLogBox.getElement().getStyle().setProperty("wordBreak", "break-all");

		//add the log scrollpanel to the tabbar
		tabbypanel.add(ObjectsLogBox," Log ");

		//create a scroll panel for the scenes data
		ScrollPanel ScenesDataBoxScroll = new ScrollPanel();

		//create a box for the scene data (as well as the scene data that fills it)
		//both of these are handled in the "SceneDataBox" class
		//we can trate it here just like a vertical panel thats been filled with the 
		//data we need. Because thats exactly what it is.
		ScenesDataBox = new SceneDataBox(sourceObject.getParentScene(),sourceObject);
		ScenesDataBoxScroll.add(ScenesDataBox);
		ScenesDataBoxScroll.setHeight("700px");
		//ScenesDataBoxScroll.setWidth("460px");

		//add the scenes data box too the tab panel as well
		tabbypanel.add(ScenesDataBoxScroll," SceneData ");

		//create gamedatabox if its not already
		if (gameDataBox==null){
			gameDataBox= new GameDataBox();

		}

		//add the box for the global game data
		//There's only one of these for the whole game, hence its a static object
		//its also just a vertical panel with stuff in.
		//However, as there's only one copy of this box, by adding it to the scroll panel
		//its removed from any other panels its in. Ie, from other ObjectInspectors that are open
		//to get around this, so its not so noticable, every time and inspector gets opened
		//we add the gamebox to that newly opened inspector.
		//we could have created a new gamebox each time, of course, but that seemed wastefull as
		//its always the same data

		ScenesGameBoxScroll.add(gameDataBox);


		ScenesGameBoxScroll.setHeight("700px");
		//ScenesGameBoxScroll.setWidth("460px");

		//add the global game data scroll panel to a new tab.
		tabbypanel.add(ScenesGameBoxScroll," GameData ");

		// set up control bar at the top of the inspector
		controlbar.setWidth("100%");
		controlbar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		controlbar.add(DisplayCodeBut); //add the display cod button for displaying fish. Or maybe not.
		controlbar.add(allSceneObjects); //add the dropdown list of scene objects
		controlbar.add(EditCmapButton); //add the edit cmap button
		controlbar.add(Clone); //add the clone button
		controlbar.add(EditModeToggle); //guess what button? 

		//set up the handlers for various buttons 
		Clone.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {

				Log.info("cloning object:");

				startCloneObjectDialogue(sourceObject);


			}

		});
		//This ones important! We hit the edit button to go into magic super happy special edit mode! Wheeeeee
		EditModeToggle.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				Log.info("setting edit mode:"
						+ Boolean.toString(event.getValue()));

				//turn edit mode on this inspectors object
				sourceobj.setEditModeOn(event.getValue());	

				//turn edit mode on in the current data box
				currentataBox.setEditMode(event.getValue());


			}	
		});

		//in theory this will in future display the script code needed to generate this
		//in its current state.
		//in practice the "ObjectSourceCode" object type needs a lot of work to get this done.
		//Its not a hard job, but its a fair bit of work to get everything accounted for
		DisplayCodeBut.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				ObjectSourceCode newdata = new ObjectSourceCode(sourceobj);

				newdata.center();

			}
		});

		//Triggers the Cmap editing function
		//usefull for getting SVG paths! :)
		EditCmapButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				Log.info("cmap editing requested");
				CollisionDebugBox.setSourceInspector(ObjectInspector.this);

				Log.info("opening log");
				//open collision log for debugging
				SceneCollisionMap.openLog();

				Log.info("starting editing cmap");
				//start cmap edit mode
				startEditingObjectsCmap();

			}
		});

		//populate the scene objects in the dropdown list (allSceneObjects)
		//this function also arranges them in alphabetical order
		populateAllSceneObjects();

		//select the first tab
		tabbypanel.selectTab(0);

		//create a panel called blah and put the controlbat at the top
		//and the tabpanel at the bottom
		VerticalPanel blah = new VerticalPanel();
		blah.add(controlbar);
		blah.add(tabbypanel);


		//more style things
		//maybe should be moved to the top rather then here as styles should be together
		//as much as we can
		super.getElement().getStyle().setBackgroundColor("#a68bff");
		super.getElement().getStyle().setOpacity(0.85);

		//finally add blah - which now contains the controll bars and tabs (which contain everything else)
		//add it to this ObjectInspectors center widget
		//(center widget is what we call the widget in the middle of a popup panel
		//we call it that because it might have a internal widget for, say, a close button at the top 
		//or even a minimise if thats ever needed)
		super.setCenterWidget(blah);

	}



	/** internal functions that should be run once the cmap has finished editing.
	 * This is mostly just to drop intercepting the left/right clicks. */
	protected void endEditingObjectsCmap() {

		//remove any existing handler
		if (currentCmapHandler!=null){
			currentCmapHandler.removeHandler();
		}
		((SceneWidgetVisual)sourceobj.getParentScene()).removeCmapClickHandler();
		currentCmapBeingEdited=null;
	}


	/** run this to stop editing the cmap */
	public void stopEditingObjectsCmap() {

		endEditingObjectsCmap();

		SceneCollisionMap.CollisionLogLog("-------|-------");
		
		//ensure it ends in z (cant have open paths)
		if (!currentPath.endsWith("z") && !currentPath.endsWith("Z")){
			currentPath=currentPath+" z";
		}
				
		SceneCollisionMap.CollisionLogLog(currentPath);
		SceneCollisionMap.CollisionLogLog("-----------|---");

		if (sourceobj.getParentScene().scenesCmap.isPresent()){
			sourceobj.getParentScene().scenesCmap.get().addToSketch(currentPath, "orange");
		} else {
			Log.severe("___Scene has no collision map set___");
		}


		sourceobj.cmap = Optional.of(new PolygonCollisionMap(currentPath,sourceobj));

		SpiffyWormhole.sendMessageToParent("debug", "file update data incoming");

		//send file a update file command to the iJammer
		String filename = sourceobj.getObjectsCurrentState().ObjectsFileName;
		//remove the ".png" from the filename and add the cmap extension
		filename = filename.substring(0,filename.lastIndexOf("."))+".vmap";

		String data = currentPath;

		SpiffyWormhole.sendMessageToParent("UpdateFile", "FileName = "+sourceobj.getObjectsCurrentState().ObjectsSceneName+"/"+filename+"\n"+data);

	}

	/** The setup stuff necessarily to start editing the objects cmap.
	 * Mostly its about handeling the clicks correctly. (that is, clicks will now add points
	 * to the cmap, not act as they normally do)
	 * Right click is set to end editing in Jam.java**/
	protected void startEditingObjectsCmap() {
		Log.severe("___startEditingObjectsCmap___");

		//start edit mode if not already on.
		currentCmapBeingEdited = this;

		//currentDataBox.setEditMode(true);
		
		

		//remove any existing handler
		if (currentCmapHandler!=null){
			currentCmapHandler.removeHandler();
		}

		currentPath="";

		//add handler if none already
		//clickhandler controls what happens when you click
		//the scene will create a special div layer ontop of itself to
		//recieve these clicks instead of the normal objects that would get it.
		//this click panel is at 150000 zindex. If you have stuff higher then that
		//in the scene, its doomed to fail and mess up and go wrong and maybe explode!
		currentCmapHandler = ((SceneWidgetVisual)sourceobj.getParentScene()).setCmapClickHandler(new ClickHandler() {			
			int lastx = -1;
			int lasty = -1;
			@Override
			public void onClick(ClickEvent event) {

				//	SceneCollisionMap.CollisionLog.log("click detected "+event.getX()+","+event.getClientY());

				//find object relative co-ordinates;
				int x=event.getRelativeX(sourceobj.getElement());				
				int y=event.getRelativeY(sourceobj.getElement());

				if (x==lastx && y==lasty){
					return; //ignore, as the user probably doubleclicked by mistake.
				} 
				lastx=x;
				lasty=y;
				
				//add to current path (example below of the type of string we are making)
				//M 1321,510 L 1487,626 L 1533,595 L 1369,482 z M 1262,507 L 1486,664 L 1589,595 L 1370,444 z
				if (currentPath==""){
					//start with a M which means "move too"
					currentPath=currentPath+" M "+x+","+y;						
				} else {				
					//if we arnt at the start of the path we use L which means "draw a line too"
					currentPath=currentPath+" L "+x+","+y;	
				}
				//also add this data to be displayed in the collision log to give the user some feedback
				SceneCollisionMap.CollisionLogLog("path= "+currentPath);



			}
		});



	}



	/** Uses the SpiffyWormhole class to communicate to an outside container frame.
	 * Basically, this is how JAM and iJAMMER talk to eachother. **/
	private void setupExternalDebuggerHandler() {

		//Sets up a handler which runs when iJammer sends a message to JAM		
		SpiffyWormhole.addMessageHandler(new IncomingMessageHandler() {			
			@Override
			public void run(String mode, String data) {

				//this function receives messages from THE OTHER END OF THE WORMHOLE!
				//Messages consist of a mode string and a data string

				//we run actions based on mode that was sent back

				//At the moment, we just support "goto" which goes to an objects scene
				//and then opens that objects inspector.
				if (mode.equalsIgnoreCase("goto")){

					//find the object to goto
					String objectName = data;

					if (data.contains(",")){

						String scenename = data.split(",",2)[0];
						objectName = data.split(",",2)[1];
						SpiffyWormhole.sendMessageToParent("debug", "told to goto object named '"+objectName+"' in scene "+scenename);

						InstructionProcessor.loadSceneIntoNewTab(scenename,scenename,true,true);

						//	SceneObject objectToJumpTo[] = SceneWidget.getSceneObjectByName(objectName,null);

						SceneObject objectToJumpTo = SceneObjectDatabase
								.getSingleSceneObjectNEW(objectName,null,true);

						objectToJumpTo.openObjectsInspector();
						//if (objectToJumpTo.length>0){
						//	objectToJumpTo[0].openObjectsInspector();
						//}

						return;
					}

					//give the parent a message using mode "debug" and a data string with the message
					SpiffyWormhole.sendMessageToParent("debug", "told to goto object named '"+objectName+"'");

					//ignore comment below getSceneObjectByName is depreciated
					///get the scene object to jump too
					//note this is an array as technical many objects could have the same name
					//(particularly with clones)
					//SceneObject objectToJumpTo[] = SceneWidget.getSceneObjectByName(objectName,null);

					//new method
					SceneObject objectToJumpTo =  SceneObjectDatabase
							.getSingleSceneObjectNEW(objectName,null,true);

					//if there was an object found the array will contain at least 1 object
					//so we send a message back either confirming it, or saying we couldnt find it
					if (objectToJumpTo!=null){
						SpiffyWormhole.sendMessageToParent("debug", "found object '"+objectToJumpTo.getObjectsCurrentState().ObjectsName+"'");

						//we then open the inspector on this object										
						objectToJumpTo.openObjectsInspector();
						//jump to the objects scene
						SpiffyWormhole.sendMessageToParent("debug", "switching to scene '"+objectToJumpTo.getParentScene().SceneFileName+"' silently");
						InstructionProcessor.bringSceneToFront(objectToJumpTo.getParentScene(),true);

					} else {
						//we couldnf find an object so we send a message back saying that
						SpiffyWormhole.sendMessageToParent("debug", "failed to get object");

					}





				}

			}
		});

		//a test function to show the messages from JAM to the iJammer is working
		//and the goto handler has been setup
		SpiffyWormhole.sendMessageToParent("debug", "goto handler has been setup.");

	}



	/**fills the scene data boxes with all the various types of objects.
	//usefull for checking the arrays are correctly registering objects**/
	private void populateAllSceneObjects() {

		allSceneObjects.clear();
		allSceneObjects.addItem(OBJECT_LIST);


		ArrayList<IsSceneObject> objects = new ArrayList<IsSceneObject>();	

		//Inventory's don't have scenes associations so we check its not a InventoryIcon before adding scene stuff
		if (sourceobj.getObjectsCurrentState().getPrimaryObjectType()!=SceneObjectType.InventoryObject){			

			//we also check the scene exists as if its in the inventory it wont have one
			if (sourceobj.getParentScene()!=null){
				
				Log.info("Getting scenes current objects");
				objects.addAll(sourceobj.getParentScene().getScenesData().getAllScenesCurrentObjects());

			}
			//	Log.info("addAll sprites and dialogues to list");
			/*
		objects.addAll(sourceobj.getScene().getScenesData().sceneSpriteObjects);
		objects.addAll(sourceobj.getScene().getScenesData().SceneDialogObjects);

	//	Log.info("addAll text and divs to list");
		objects.addAll(sourceobj.getScene().getScenesData().SceneTextObjects);
		objects.addAll(sourceobj.getScene().getScenesData().SceneDivObjects);

	//	Log.info("addAll input and vector objects to list");
		objects.addAll(sourceobj.getScene().getScenesData().SceneInputObjects);
		objects.addAll(sourceobj.getScene().getScenesData().SceneVectorObjects);

			 */
		}

		//	Log.info("addAll inventory");

		objects.addAll(InventoryPanel.getAllInventoryItems());
		//sort them into alphabetical order using the "alphabeticalObjects" comparator we defined at the top
		//the sort function uses the comparitor to work out the order we want the objects in
		//in this case, alphabetical by their object names
		Collections.sort(objects, alphabeticalObjects);


		//make an iterator from them now they are in order
		Log.info("adding to all sceneObject.objectsCurrentState");
		Iterator<IsSceneObject> sit = objects.iterator();

		//loop over them
		while (sit.hasNext()) {

			IsSceneObject sceneObject = sit.next();

			String itemName = sceneObject.getObjectsCurrentState().ObjectsName;
			String shortName = itemName;
			//shorten the name to fit if its too long
			if (itemName.length() > 30) {
				//get the first 30 letters
				shortName = itemName.substring(0, 30);
			}
			//add them to the list - we display the short name
			//but the list object is smart and can also reemember the full name
			//so if the name as shortened we can still find it from its full name
			allSceneObjects.addItem(shortName, itemName);

		}

		// add a selection handler
		Log.info("adding selection handlers");
		
		allSceneObjects.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				
				//get the item name from the selected items number in the drop down
				int itemNum = allSceneObjects.getSelectedIndex();
				
				//get its full name
				String objectName = allSceneObjects.getValue(itemNum);
				
				if (objectName.equalsIgnoreCase(OBJECT_LIST)){
					return;//do nothing
				}
				
				// popup a inspector for this object
				//Note; This will open every object that matchs the name
				Set<? extends SceneObject> objectstoopen = SceneObjectDatabase
						.getSceneObjectNEW(objectName,null,true);


				if (objectstoopen.size()==0){
					Log.info("no objects found called:"+objectName);

				}

				for (SceneObject sceneObject : objectstoopen) {
					sceneObject.openObjectsInspector();
				}


			}
		});

	}

	/** 
	 * functions that run when the inspector is opened.
	 * Specifically we trigger the default popup open functions (super.opendefault)
	 * and then add one to the number of open inspectors variable.
	 * Additionally we move the GameDataBox to this inspector*/
	@Override
	public void OpenDefault() {
		super.OpenDefault();
		numOfInspectorsOpen = numOfInspectorsOpen + 1;
		JAMcore.GameLogger.info("Object Inspectors now open ="+numOfInspectorsOpen);
		
		//move databox to the last opened
		ScenesGameBoxScroll.clear();
		ScenesGameBoxScroll.add(gameDataBox);
		
		this.update();

		//update the game data too
		GameDataBox.update();

		//enable scrolling on the window (in case the inspector goes off the bottom)
		//this adds overflow:auto to the html tag
		Window.enableScrolling(true);

	}

	/** functions that run when the inspector is closed.**/
	@Override
	public void CloseDefault() {
		super.CloseDefault(false);
		
		//turn edit modes off in case they are still on
		sourceobj.setEditModeOn(false);
		sourceobj.setSelectedCSS(false);
		EditModeToggle.setValue(false);

		//if the number of open inspectors are zero, we make the debug box invisible
		numOfInspectorsOpen = numOfInspectorsOpen - 1;

		JAMcore.GameLogger.info("Object Inspectors open ="+numOfInspectorsOpen);
		
		if (numOfInspectorsOpen <= 0) {
			numOfInspectorsOpen =0;

			//the debug box is the little labels that tell you current scene co-ordinates
			//and mouse position.
			//they are displayed on, and controlled by the current object scene
			if (sourceobj.getParentScene()!=null){
				((SceneWidgetVisual)sourceobj.getParentScene()).setDebugBoxVisible(false);
			}

			//we also redisable screen scrolling on the window
			Window.enableScrolling(false);


		}

	}

	/** Triggers an update to run on the inspectors displayed data.
	 * This should be triggered any time that data might have changed.
	 * Mostly used for movement, or animation frame changes **/
	public void update() {

		//dont bother if not open
		if (!this.isShowing()){
			return;
		}
		
		//triggered the right sort of update based on the class of object
		//this inspector is looking at
		if (sourceobj.getClass() == SceneSpriteObject.class) {

			((SceneSpriteObjectDataBox) currentataBox).update();

		}
		if (sourceobj.getClass() == InventoryItem.class) {

			((InventoryIconObjectDataBox) currentataBox).update();
			

		}
		if (sourceobj.getClass() == SceneDialogObject.class) {

			((SceneDialogObjectDataBox) currentataBox).update();

		}
		if (sourceobj.getClass() == SceneLabelObject.class) {

			((SceneLabelObjectDataBox) currentataBox).update();

		}

		if (sourceobj.getClass() == SceneTextObject.class) {

			((SceneDialogObjectDataBox) currentataBox).update();

		}

		if (sourceobj.getClass() == SceneDivObject.class) {

			((SceneDivObjectDataBox) currentataBox).update();

		}

		if (sourceobj.getClass() == SceneVectorObject.class) {

			((SceneVectorObjectDataBox) currentataBox).update();

		}


		if (sourceobj.getClass() == SceneInputObject.class) {

			((SceneInputObjectDataBox) currentataBox).update();

		}

		//general scene data update
		//we pass the objects scene in case the object moved between scenes
		
		ScenesDataBox.update(sourceobj,(SceneWidgetVisual)sourceobj.getParentScene());		
		


		//Populate the scene objects again
		populateAllSceneObjects();

		Log.info("finnished update");



	}



	/**
	 * To clone an object this is triggered
	 *Cloning is a fairly complex feature so we can debug it by trying it on any object
	 *with a inspector open
	 * 
	 * @param sourceObject - the object you want to clone **/

	private void startCloneObjectDialogue(final SceneObject sourceObject) {
		
		//get the objects name
		final SceneObject objectToClone = sourceObject;// .getObjectsCurrentState().ObjectsName;
				
		//the panel which will ask for the new clones name
		final PopupPanel askForName = new PopupPanel();
		//contents of the above panel
		VerticalPanel askForNameContents = new VerticalPanel();
		final TextBox nameBox = new TextBox();
		Button cancel = new Button("Cancel");

		//key down function, used to detect "enter" being hit on the new name
		nameBox.addKeyDownHandler(new KeyDownHandler() {					
			@Override
			public void onKeyDown(KeyDownEvent event) {

				//enter is KeyCode 13.
				//Its been that way since my Commadore Plus/4! 
				//I could also use KeyCodes.KEY_ENTER instead of writting 13, but thats less oldschool cool
				if (event.getNativeKeyCode()==13 ){

					//the new name they just typed
					String newname = nameBox.getText();

					//if the current object is positioned relatively, we display its position relatively too;
					if (sourceObject.getObjectsCurrentState().positionedRelativeToo != null) {
						//create object at slightly moved relative position
						SceneObject newObject = InstructionProcessor.triggerCloneObject(sourceObject, objectToClone, newname,sourceObject.getObjectsCurrentState().relX+50,sourceObject.getObjectsCurrentState().relY+50,sourceObject.getObjectsCurrentState().relZ+50);
						//open its inspector
						newObject.openObjectsInspector();
					} else {
						//else just set the new clones co-ordinates absolute
						SceneObject newObject = InstructionProcessor.triggerCloneObject(sourceObject, objectToClone, newname, sourceObject.getObjectsCurrentState().getX()+50, sourceObject.getObjectsCurrentState().getY()+50, sourceObject.getObjectsCurrentState().getZ()+50);
						//open its inspector
						newObject.openObjectsInspector();
					}


					//hide the popup panel
					askForName.hide();
				}
				//if they hit enter to cancel.
				//not so cool here, I used the Keycode key_escape as I couldnt remember the number for it
				if (event.getNativeKeyCode()==KeyCodes.KEY_ESCAPE){

					askForName.hide();

				}


			}
		});

		//also we have a cancel button
		cancel.addClickHandler(new ClickHandler() {					
			@Override
			public void onClick(ClickEvent event) {
				askForName.hide();
			}
		});


		//contents of popup
		askForNameContents.add(new Label("Please enter name of new object;"));
		askForNameContents.add(nameBox);	
		askForNameContents.add(cancel);

		askForNameContents.setSpacing(6);
		//add the contents to the popup
		askForName.add(askForNameContents);
		//set Glass & Modal Enabled (this fades the background and prevents anything else working till this 
		//popup goes away)
		askForName.setGlassEnabled(true);
		askForName.setModal(true);
		//also make sure it gets native events. So Nothing else but this popup works
		//(if we dont do this keyboard events might also trigger on the scene when you type)
		askForName.setPreviewingAllNativeEvents(true);

		//style!
		askForName.getElement().getStyle().setZIndex(999999999);
		askForName.getElement().getStyle().setBackgroundColor("white");
		//popup at center!
		askForName.center();
		//focus the namebox ready to type! because we are awesome like that!
		nameBox.setFocus(true);
	}




	//if any objectinspector is open, this returns true
	public static boolean debugIsOpen() {
		if (numOfInspectorsOpen>0){
			return true;
		}
		return false;
	}


}

//Below are the class's for the various types of inspector boxs.
//They all have a basic class "SpiffyObjectDataBox" which contains
//the data they all have in common.
//They then add their type specific data to that box.

//NB; The "SpiffyObjectDataBox" is internally a type of SpiffyDataBox.
//think of it like a 2 colume table for displaying data easily

/** a box designed to display all the data of a game sprite object **/
class SceneSpriteObjectDataBox extends SpiffyObjectDataBox {

	// if values are changed trigger this
	//might now be used but could be usefull
	private Runnable OnValueChangeRunnable;

	//if this object is cloned from something
	Label clonedFroml = new Label("");

	//current animation
	Label animation = new Label("");

	//manually controll the frames
	FrameControlls frameStuff;
	
	
	//labels for the another (I mean they are all labels, but this is purely the label
	//on the page that says what the thing on right of it is.
	//these dont chance as the data changes
	Label cfl = new Label("Frame:");
	Label Animationlab = new Label("Animation:");
	Label clonedFromlab = new Label("ClonedFrom:");

	//a disclosure panel thata opens to reveal all the objects attachment points
	//those are points we can pin other objects too
	//that is, relatively position them too an exact point
	DisclosurePanel attachmentList = new DisclosurePanel("(Atachment Point List)");
	Label attachmentListLabel = new Label();
    Button reloadAttachmentFile = new Button("reloadAttachmentFile (*.glu)");
    
	//a disclosure panel that opens to reveal the sprites image
	DisclosurePanel imageDetails = new DisclosurePanel("(Image Details)");
	//the above image the disclosure will contain
	Image imagePreview = new Image();

	//the object that this data came from
	SceneObject so;

	//the objects state
	SceneSpriteObjectState dat;

	public SceneSpriteObjectDataBox(final SceneSpriteObject sourceObject,	SceneSpriteObjectState obj) {
		this(sourceObject,obj,true);
	}

	public SceneSpriteObjectDataBox(final SceneSpriteObject sourceObject,	SceneSpriteObjectState obj,boolean runInitialUpdate) {
		
		//run the general SpiffyObjectDataBox setup with the objects and state supplied
		//this sets up all the things common to all objects, regardless of type
		super(sourceObject, obj);

		//store the source data and object
		so=sourceObject;
		dat = obj;

		//make sure animation state is updated if its been animating
		if (!sourceObject.SceneObjectIcon.isAnimating()) {
			sourceObject.getObjectsCurrentState().currentlyAnimationState = "";
		} else {
			sourceObject.getObjectsCurrentState().currentlyAnimationState = sourceObject.SceneObjectIcon
					.serialiseAnimationState();
		}

		//add the attachment list and put the label in it
		
		VerticalPanel attachmentPointPanel = new VerticalPanel();
		super.addrow("atachmentList:", attachmentList);
		attachmentList.add(attachmentPointPanel);
		attachmentPointPanel.add(reloadAttachmentFile);
		attachmentPointPanel.add(attachmentListLabel);		
		attachmentListLabel.getElement().getStyle().setWhiteSpace(WhiteSpace.PRE_WRAP);
		

		
		//add various other bits of data (I dont really have to comment this separately do I?)
		super.addrow(clonedFromlab, clonedFroml);
		
		
		frameStuff = new FrameControlls(sourceObject); //new HorizontalPanel();
		super.addrow(cfl, frameStuff);
		
		//current animation
		super.addrow(Animationlab, animation);
		
		//if url is dependent, change label
		if (!sourceObject.getObjectsCurrentState().DependantURLPrefix.isEmpty()){
			Urll.setText("Depedant URL:");
		} else {
			Urll.setText("URL:");				
		}
		
		//current image
		super.addrow(Urll, url, imageDetails);
		imageDetails.add(imagePreview);
		
		
		//current object variables
		super.addrow(VariablesLab, Variables);
		//the actions the object has assigned to it (that is, its triggers and commands it runs on certain conditions)
		super.addrow(new Label("Actions:"), actionsDisclosure);
		actionsDisclosure.add(actionslab);
		//propertys blah
		super.addrow(propl, props, PropertyNodes,addRemovePropertyPanel);

		//objects touching blah (set manually from scripts)
		super.addrow(touchl, touching, addRemovetouchingPanel);

		//all the data serialized as a string - this is used for saving
		super.addrow(sdatal, sdata);


		//update button that updates the serialized data
		Button updateObjectToThis = new Button("update");
		//to manually edit the serualised data (to help testing)
		super.addrow(editsdatal, editsdata, updateObjectToThis);
		editsdata.setWidth("100%");

		//updates the objects current state to reflect whats in the editsdata box
		updateObjectToThis.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				Log.info("<____________creating new state from edit data box");
				SceneSpriteObjectState newstate = new SceneSpriteObjectState(
						editsdata.getText());
				Log.info("<____________updating state");
				sourceObject.updateState(newstate, true,true);

			}
		});

		//update attachmens button (thiss should be moved elsewhere)
		reloadAttachmentFile.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				
				attachmentListLabel.setText("(reloading)");
				reloadAttachmentFile.setEnabled(false);
				
				Log.info("reloading attchment ponts requested");
				Log.info("first cllearing existing..");
				sourceObject.attachmentPoints=null;
				Log.info("requesting reload..");
				
			//	Log.info("Original filename:"+sourceObject.SceneObjectIcon.originalfilename);
				String basefilename = sourceObject.SceneObjectIcon.basefilename;
				Log.info("base filename:"+basefilename); //eg: base filename:Game Scenes/MainScene/Objects/meryll/meryll_climb_high
		
			//	String baseURL = SceneWidget.SceneFileRoot + dat.ObjectsSceneName+"/Objects/"+ dat.ObjectsName+"/"; //new url method
				String atachmentPointUrl =basefilename + ".glu"; //does this include zero?? seems too
				
				
				//refresh text after load and re-enable the reloadbutton
				//because retrieving the file isnt instant, we have to put this bit of code in a runnable
				//which the load function will trigger after loading
				Runnable runafterreload = new Runnable(){
					@Override
					public void run() {
						attachmentListLabel.setText(so.attachmentPoints.serialise());
						reloadAttachmentFile.setEnabled(true);			
					}
				};
			
				
				//Finally now we have the Url and what to do after, we set it loading
				sourceObject.loadAttachmentPoints(atachmentPointUrl, true,false,runafterreload);
				
			}
		});

		

		//update the data labels to match the objects current data
		if (runInitialUpdate){
			update();
		}
		
	}


	/** not currently used **/
	public void setOnValueChangeRunnable(Runnable onValueChange) {
		this.OnValueChangeRunnable = onValueChange;
	}

	/** update the inspectors sprite specific data **/
	public void update() {
		super.updateUniversalData();

		//if its position relative we update that data
		//this probably should be in updateUniversalData
		//as now any object can be positioned relative, it used to just be sprites.
		/*
		if (dat.positionedRelativeToo != null) {

			RelativeObject.setText(""
					+ dat.positionedRelativeToo.objectsCurrentState.ObjectsName+":"+dat.positionedRelativeToPoint);

		} else {

			RelativeObject.setText("");

		}
		 */

		//if its cloned from something display what its cloned from
		if (dat.clonedFrom != null) {
			clonedFroml.setText("-" + dat.clonedFrom.getObjectsCurrentState().ObjectsName);
		} else {
			clonedFroml.setText("-(no clone set)");
		}

		//fill in the actions
	//	actionslab.setText("" + sourceObject.objectsActions.getCode());
	//	actionslab.getElement().getStyle().setWhiteSpace(WhiteSpace.PRE);

		//set the url of the image
		url.setText("" + dat.ObjectsURL);
		//set the preview image
		imagePreview.setUrl(dat.ObjectsURL);

		//set the current frames text
		frameStuff.setText("" + dat.currentFrame+" out of "+dat.currentNumberOfFrames);

		//new addition, updates state each time
		SceneSpriteObject asSprite = (SceneSpriteObject)so; //we know its a sprite so we can safely cast. Storing the cast as a var saves us casting each time
		dat.currentlyAnimationState = asSprite.SceneObjectIcon.serialiseAnimationState();

		//display the animation state
		//used to help debug saving animations
		//the save saves the exact frame and type of animation (forward/backward/loop etc) all encoded into this string
		String aniinfo="(is ani:"+asSprite.SceneObjectIcon.isAnimating()+" at "+dat.currentFrameGap+"ms)";
		
		String postanimationactions = " rao:"+asSprite.SceneObjectIcon.animation.hasRunAfterOpen()+" rac:"+asSprite.SceneObjectIcon.animation.hasRunAfterClose();		
		if (asSprite.SceneObjectIcon.animation.isDisableNextPostAnimationCommands()){
			postanimationactions = postanimationactions+ " (nextDisabled)";
		}
		
		animation.setText("|" + dat.currentlyAnimationState+" " +aniinfo+" "+postanimationactions);

		//if there's attachment points we display them too
		//This also should be moved to a general object update function at some point
		//no reason it should just be for sprites!
		if (so.attachmentPoints!=null){
			attachmentListLabel.setText(so.attachmentPoints.serialise());
		}else {
			attachmentListLabel.setText("");
		}
		

		//objects properties (moved to general update)
//		
//		props.setText("" + dat.objectsProperties.toString());
//
//		//we also display these propertys in a disclosure panel
//		//so the user can browser their subclass's if they want
//		PropertyNodes.clear();
//
//		Iterator<SSSProperty> propit = dat.objectsProperties.iterator();
//
//		//loop over adding all the property node labels to the PropertyNodes vertical panel
//		while (propit.hasNext()) {
//
//			SSSProperty sssProp = propit.next();
//
//			// if its a class just add the node
//			if (sssProp.getPred() == SSSNode.SubClassOf) {
//
//				PropertyNodes.add(new NodeResultLabel(sssProp.getValue()));
//
//			} else {
//				// else we write the predicate then the node in a horizontal panel
//				HorizontalPanel predthennode = new HorizontalPanel();
//
//				predthennode.add(new NodeResultLabel(sssProp.getPred()));
//				predthennode.add(new Label(":"));
//				predthennode.add(new NodeResultLabel(sssProp.getValue()));
//
//				PropertyNodes.add(predthennode);
//
//			}
//		}

		// Log.info("setting touching data");


		// Log.info("variables setting");
		//set the variables
		//again, this should probably be global to all objects, not just spites
		Variables.setText(dat.ObjectRuntimeVariables.toString());

		Log.info("Data display updated");
	}

}

//inventory icons are very similar so we can just extend sprite
class InventoryIconObjectDataBox extends SceneSpriteObjectDataBox {
	
	Label associatedObjectlab  = new Label("AsasociatedObject:");
	Label associatedObjectData = new Label("(none)");

	
	public InventoryIconObjectDataBox(InventoryObjectState objstate, InventoryItem sourceObject) {		
		super(sourceObject,objstate,false); //false ensures we don't run the update from the superclass (as this will break as we havnt finnished making our labels yet)

		//TODO: make proper insert function in spiffydatabox
	//	int newindex = super.insertRow(2);
		//super.setWidget(newindex, 0, associatedObjectlab);
		//super.setWidget(newindex, 1, associatedObjectData);
		
		super.insertrow(2,associatedObjectlab,associatedObjectData);
		
		
		this.update();
		
		
	}
	
    @Override
	public void update() {
		super.update();
		
		InventoryObjectState iconState = (InventoryObjectState) dat;
		
		if (iconState.getAssociatedSceneObject()!=null){
			
			associatedObjectData.setText( iconState.getAssociatedSceneObject().getName() );
			
		} else if (!iconState.associatedSceneObjectWhenNeeded.isEmpty()){			
			
			associatedObjectData.setText(".."+iconState.associatedSceneObjectWhenNeeded );	
			
		} else {
			
			associatedObjectData.setText("(none)");
			
		}
		
		
	}


}

//I wont comment as detailed here, use the SpriteObjectDataBox as a guide as many things are the same
class SceneLabelObjectDataBox extends SpiffyObjectDataBox {

	Label css = new Label();
	Label typedTextSetting = new Label(); 
	// if values are changed trigger this
	private Runnable OnValueChangeRunnable;

	//dialogue specific variable
	Label clonedFroml = new Label("");
	//Label currentParagraph = new Label("");
	//Label currentNextParagraphObject= new Label("");

	Label typedTextStylelabel = new Label("TypedText&Cursor:");
	Label cssl = new Label("CSSname");

	//Label currentParagraphlab = new Label("CurrentParagraph:");
	Label clonedFromlab = new Label("ClonedFrom:");

	DisclosurePanel textDetails = new DisclosurePanel("(Text Details)");

	DisclosurePanel TypedLabelToString = new DisclosurePanel("( TypedLabel html)");
	Label TypedLabelToStringlab = new Label("typed label to string");

	Label currentText = new Label();

	SceneLabelObjectState dat;


	public SceneLabelObjectDataBox(SceneLabelObjectState state,final SceneLabelObject sourceObject) {
		super(sourceObject, state);
		dat = state;

		update();

		//add the data labels and label labels
		super.addrow(typedTextStylelabel, typedTextSetting);
		super.addrow(cssl, css);
		super.addrow(clonedFromlab, clonedFroml);
		//super.addrow("Cur Paragraph:", currentParagraph);
		//super.addrow("Next icon object:", currentNextParagraphObject);

		HorizontalPanel urlAndIJammerButton = new HorizontalPanel();
		urlAndIJammerButton.add(url);


		super.addrow(Urll, urlAndIJammerButton, textDetails);

		textDetails.add(currentText);
		super.addrow(VariablesLab, Variables);
		super.addrow(propl, props, PropertyNodes,addRemovePropertyPanel);

		//objects touching blah (set manually from scripts)
		super.addrow(touchl, touching, addRemovetouchingPanel);

		super.addrow(new Label("Actions:"), actionsDisclosure);
		actionsDisclosure.add(actionslab);

		//for debugging the contents of the internal TypedLabel
		super.addrow(new Label("TypedLabel HTML (tostring):"), TypedLabelToString);
		TypedLabelToString.add(TypedLabelToStringlab);

		super.addrow(sdatal, sdata);

	}


	public void setOnValueChangeRunnable(Runnable onValueChange) {
		this.OnValueChangeRunnable = onValueChange;
	}

	public void update() {
		super.updateUniversalData();


		Log.info("running update..");

		css.setText(dat.CSSname);
		typedTextSetting.setText(""+dat.TypedText+","+dat.cursorVisible);
		Log.info("running update...");

		if (dat.clonedFrom != null) {
			clonedFroml.setText("-" + dat.clonedFrom.getObjectsCurrentState().ObjectsName);
		} else {
			clonedFroml.setText("-(no clone set)");
		}
		Log.info("running update....");

	//	actionslab.setText("" + sourceObject.objectsActions.getCode());
	//	actionslab.getElement().getStyle().setWhiteSpace(WhiteSpace.PRE);

		Log.info("running update............");

		currentText.setText(dat.ObjectsCurrentText);

		editsdata.setText("" + dat.serialiseToString());


		Log.info("variables setting");
		Variables.setText(dat.ObjectRuntimeVariables.toString());

		/*
		PropertyNodes.clear();

		Iterator<SSSProperty> propit = dat.objectsProperties.iterator();

		while (propit.hasNext()) {

			SSSProperty sssProp = propit.next();

			// if its a class just add the node
			if (sssProp.getPred() == SSSNode.SubClassOf) {

				PropertyNodes.add(new NodeResultLabel(sssProp.getValue()));

			} else {
				// else we write the predicate then the node
				HorizontalPanel predthennode = new HorizontalPanel();

				predthennode.add(new NodeResultLabel(sssProp.getPred()));
				predthennode.add(new Label(":"));
				predthennode.add(new NodeResultLabel(sssProp.getValue()));

				PropertyNodes.add(predthennode);

			}
		}
*/
		
		//update TypedLabel contents too
		if (sourceObject.getObjectsCurrentState().getPrimaryObjectType()==SceneObjectType.DialogBox ){

			TypedLabelToStringlab.setText(sourceObject.getAsDialog().TextLabel.toString());

		} else if (sourceObject.getObjectsCurrentState().getPrimaryObjectType()==SceneObjectType.Label){

			TypedLabelToStringlab.setText(sourceObject.getAsLabel().TextLabel.toString());

		} else {
			TypedLabelToStringlab.setText("does not have typed label");

		}

		Log.info("data display updated");
	}

}


//I wont comment as detailed here, use the SpriteObjectDataBox as a guide as many things are the same
class SceneDialogObjectDataBox extends SpiffyObjectDataBox {

	Label css = new Label();
	Label typedTextSetting = new Label(); 
	// if values are changed trigger this
	private Runnable OnValueChangeRunnable;

	//dialogue specific variable
	Label clonedFroml = new Label("");
	Label currentParagraph = new Label("");
	Label currentNextParagraphObject= new Label("");

	Label typedTextStylelabel = new Label("TypedText&Cursor:");
	Label cssl = new Label("CSSname");

	//Label currentParagraphlab = new Label("CurrentParagraph:");
	Label clonedFromlab = new Label("ClonedFrom:");

	DisclosurePanel textDetails = new DisclosurePanel("(Text Details)");

	DisclosurePanel TypedLabelToString = new DisclosurePanel("( TypedLabel html)");
	Label TypedLabelToStringlab = new Label("typed label to string");

	Label currentText = new Label();

	SceneDialogueObjectState dat;


	public SceneDialogObjectDataBox(SceneDialogueObjectState state,final SceneDialogObject sourceObject) {
		super(sourceObject, state);
		dat = state;

		update();

		//add the data labels and label labels
		super.addrow(typedTextStylelabel, typedTextSetting);
		super.addrow(cssl, css);
		super.addrow(clonedFromlab, clonedFroml);
		super.addrow("Cur Paragraph:", currentParagraph);
		super.addrow("Next icon object:", currentNextParagraphObject);

		HorizontalPanel urlAndIJammerButton = new HorizontalPanel();
		urlAndIJammerButton.add(url);

		Button refresh       = new Button("(refresh)");
		Button editInIJammer = new Button("(EditInIJammer)");

		//handler for to tell the  iJammer to edit this dialogue
		editInIJammer.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				//could also send the paragraph number to edit?
				SpiffyWormhole.sendFunctionToParent("editDialogue", url.getText()+","+dat.currentparagraphName);	
			}
		});

		//to refresh the contents
		refresh.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				sourceObject.reloadTextFromFile();
			}
		});

		urlAndIJammerButton.add(refresh);
		urlAndIJammerButton.add(editInIJammer);


		super.addrow(Urll, urlAndIJammerButton, textDetails);

		textDetails.add(currentText);
		super.addrow(VariablesLab, Variables);
		super.addrow(propl, props, PropertyNodes,addRemovePropertyPanel);

		//objects touching blah (set manually from scripts)
		super.addrow(touchl, touching, addRemovetouchingPanel);

		super.addrow(new Label("Actions:"), actionsDisclosure);
		actionsDisclosure.add(actionslab);

		//for debugging the contents of the internal TypedLabel
		super.addrow(new Label("TypedLabel HTML (tostring):"), TypedLabelToString);
		TypedLabelToString.add(TypedLabelToStringlab);


		sdatal.setWidth("400px");
		super.addrow(sdatal, sdata);


	}


	public void setOnValueChangeRunnable(Runnable onValueChange) {
		this.OnValueChangeRunnable = onValueChange;
	}

	public void update() {

		super.updateUniversalData();


		Log.info("running update..");

		css.setText(dat.CSSname);
		typedTextSetting.setText(""+dat.TypedText+","+dat.cursorVisible);
		Log.info("running update...");

		if (dat.clonedFrom != null) {
			clonedFroml.setText("-" + dat.clonedFrom.getObjectsCurrentState().ObjectsName);
		} else {
			clonedFroml.setText("-(no clone set)");
		}
		Log.info("running update....");

		Log.info("running update............");
		url.setText("" + dat.ObjectsCurrentURL);

		currentText.setText(dat.ObjectsCurrentText);

		editsdata.setText("" + dat.serialiseToString());

		//update its current paragraph
		currentParagraph.setText(dat.currentparagraphName+":"+dat.currentParagraphPage+" ("+dat.currentNumberOfParagraphs+" in total)");
		currentNextParagraphObject.setText(":"+dat.NextParagraphObject);


		Log.info("variables setting");
		Variables.setText(dat.ObjectRuntimeVariables.toString());

		/*
		PropertyNodes.clear();

		Iterator<SSSProperty> propit = dat.objectsProperties.iterator();

		while (propit.hasNext()) {

			SSSProperty sssProp = propit.next();

			// if its a class just add the node
			if (sssProp.getPred() == SSSNode.SubClassOf) {

				PropertyNodes.add(new NodeResultLabel(sssProp.getValue()));

			} else {
				// else we write the predicate then the node
				HorizontalPanel predthennode = new HorizontalPanel();

				//predthennode.add(new Label(sssProp.getPred().getPLabel()));
				
				predthennode.add(new NodeResultLabel(sssProp.getPred()));
				
				predthennode.add(new Label(":"));
				predthennode.add(new NodeResultLabel(sssProp.getValue()));

				PropertyNodes.add(predthennode);

			}
		}
*/
		
		//update TypedLabel contents too
		if (sourceObject.getObjectsCurrentState().getPrimaryObjectType()==SceneObjectType.DialogBox ){

			TypedLabelToStringlab.setText(sourceObject.getAsDialog().TextLabel.toString());

		} else if (sourceObject.getObjectsCurrentState().getPrimaryObjectType()==SceneObjectType.Label){

			TypedLabelToStringlab.setText(sourceObject.getAsLabel().TextLabel.toString());

		} else {
			TypedLabelToStringlab.setText("does not have typed label");

		}

		Log.info("data display updated");
	}

}

//....and the input object box
class SceneInputObjectDataBox extends SpiffyObjectDataBox {


	// if values are changed trigger this
	private Runnable OnValueChangeRunnable;
	SceneInputObjectState dat;

	//control if its currently read only
	CheckBox currentlyReadOnly = new CheckBox();

	public SceneInputObjectDataBox(SceneInputObjectState obj,
			final SceneInputObject sourceObject) {

		super(sourceObject, obj);
		dat = obj;

		update();


		currentlyReadOnly.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sourceObject.setReadOnly(currentlyReadOnly.getValue());

			}
		});

		super.addrow("MaxCharacters", ""+sourceObject.getObjectsCurrentState().maxcharacters);
		super.addrow("Read only", currentlyReadOnly);
		super.addrow(VariablesLab, Variables);
		super.addrow(propl, props, PropertyNodes,addRemovePropertyPanel);


		//objects touching blah (set manually from scripts)
		super.addrow(touchl, touching, addRemovetouchingPanel);

		super.addrow(new Label("Actions:"), actionsDisclosure);
		actionsDisclosure.add(actionslab);

		super.addrow(sdatal, sdata);
	}



	public void setOnValueChangeRunnable(Runnable onValueChange) {
		this.OnValueChangeRunnable = onValueChange;
	}

	public void update() {

		super.updateUniversalData();


		Log.info("running update..");


	//	actionslab.setText("" + sourceObject.objectsActions.getCode());
		//actionslab.getElement().getStyle().setWhiteSpace(WhiteSpace.PRE);

		Log.info("running update............");

		editsdata.setText("" + dat.serialiseToString());

		Log.info("variables setting");
		Variables.setText(dat.ObjectRuntimeVariables.toString());

		/*
		PropertyNodes.clear();

		Iterator<SSSProperty> propit = dat.objectsProperties.iterator();

		while (propit.hasNext()) {

			SSSProperty sssProp = propit.next();

			// if its a class just add the node
			if (sssProp.getPred() == SSSNode.SubClassOf) {

				PropertyNodes.add(new NodeResultLabel(sssProp.getValue()));

			} else {
				// else we write the predicate then the node
				HorizontalPanel predthennode = new HorizontalPanel();

				predthennode.add(new NodeResultLabel(sssProp.getPred()));
				predthennode.add(new Label(":"));
				predthennode.add(new NodeResultLabel(sssProp.getValue()));

				PropertyNodes.add(predthennode);

			}
		}*/

		currentlyReadOnly.setValue(dat.ReadOnly);



		Log.info("data display updated");
	}




}
//....and the div object box
class SceneDivObjectDataBox extends SpiffyObjectDataBox {


	// if values are changed trigger this
	private Runnable OnValueChangeRunnable;
	SceneDivObjectState dat;

	public SceneDivObjectDataBox(SceneDivObjectState obj,
			SceneDivObject sourceObject) {

		super(sourceObject, obj);
		dat = obj;

		update();

		super.addrow(VariablesLab, Variables);
		super.addrow(propl, props, PropertyNodes,addRemovePropertyPanel);

		//objects touching blah (set manually from scripts)
		super.addrow(touchl, touching, addRemovetouchingPanel);


		super.addrow(new Label("Actions:"), actionsDisclosure);
		actionsDisclosure.add(actionslab);

		super.addrow(sdatal, sdata);
	}



	public void setOnValueChangeRunnable(Runnable onValueChange) {
		this.OnValueChangeRunnable = onValueChange;
	}

	public void update() {
		//first we ensure we are attached, else why should we waste our precious cycles? Not to mention the needless logspam
		if (!this.isAttached()) {
			return;
		}
		super.updateUniversalData();


		Log.info("running update..");

				
		actionslab.setText("" + sourceObject.objectsActions.getCode());
		actionslab.getElement().getStyle().setWhiteSpace(WhiteSpace.PRE);

		Log.info("running update............");

		editsdata.setText("" + dat.serialiseToString());

		Log.info("variables setting");
		Variables.setText(dat.ObjectRuntimeVariables.toString());

		/*
		PropertyNodes.clear();

		Iterator<SSSProperty> propit = dat.objectsProperties.iterator();

		while (propit.hasNext()) {

			SSSProperty sssProp = propit.next();

			// if its a class just add the node
			if (sssProp.getPred() == SSSNode.SubClassOf) {

				PropertyNodes.add(new NodeResultLabel(sssProp.getValue()));

			} else {
				// else we write the predicate then the node
				HorizontalPanel predthennode = new HorizontalPanel();

				predthennode.add(new NodeResultLabel(sssProp.getPred()));
				predthennode.add(new Label(":"));
				predthennode.add(new NodeResultLabel(sssProp.getValue()));

				PropertyNodes.add(predthennode);

			}
		}*/

		Log.info("data display updated");
	}




}
// but also a vector boxxxxx
class SceneVectorObjectDataBox extends SpiffyObjectDataBox {


	// if values are changed trigger this
	private Runnable OnValueChangeRunnable;
	SceneVectorObjectState dat;
	
	Label PathLabel = new Label();

	public SceneVectorObjectDataBox(SceneVectorObjectState obj,
			SceneVectorObject sourceObject) {

		super(sourceObject, obj);
		dat = obj;

		update();

		super.addrow("Path:", PathLabel);
		super.addrow(VariablesLab, Variables);
		super.addrow(propl, props, PropertyNodes,addRemovePropertyPanel);


		//objects touching blah (set manually from scripts)
		super.addrow(touchl, touching, addRemovetouchingPanel);

		super.addrow(new Label("Actions:"), actionsDisclosure);
		actionsDisclosure.add(actionslab);

		super.addrow(sdatal, sdata);
	}



	public void setOnValueChangeRunnable(Runnable onValueChange) {
		this.OnValueChangeRunnable = onValueChange;
	}

	public void update() {
		/**updates the data universal to all object types */
		super.updateUniversalData();

		//Now we updat the data specific to this type (Note; Some of this stuff might also be universal and we havnt moved it over yet...like actions)
		Log.info("running update..");
		

		PathLabel.setText(sourceObject.getAsVector().getCurrentVectorString());

	//	actionslab.setText("" + sourceObject.objectsActions.getCode());
	//	actionslab.getElement().getStyle().setWhiteSpace(WhiteSpace.PRE);

		Log.info("running update............");

		editsdata.setText("" + dat.serialiseToString());

		Log.info("variables setting");
		Variables.setText(dat.ObjectRuntimeVariables.toString());

		/*
		PropertyNodes.clear();

		Iterator<SSSProperty> propit = dat.objectsProperties.iterator();

		while (propit.hasNext()) {

			SSSProperty sssProp = propit.next();

			// if its a class just add the node
			if (sssProp.getPred() == SSSNode.SubClassOf) {

				PropertyNodes.add(new NodeResultLabel(sssProp.getValue()));

			} else {
				// else we write the predicate then the node
				HorizontalPanel predthennode = new HorizontalPanel();

				predthennode.add(new NodeResultLabel(sssProp.getPred()));
				predthennode.add(new Label(":"));
				predthennode.add(new NodeResultLabel(sssProp.getValue()));

				PropertyNodes.add(predthennode);

			}
		}*/

		Log.info("data display updated");
	}




}