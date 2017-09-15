package com.lostagain.JamGwt.JargScene.debugtools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.common.collect.Table;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.AssArray;
import com.lostagain.Jam.CurrentScenesVariables;
import com.lostagain.Jam.GameStatisticDisplayer;
import com.lostagain.Jam.GameStatistics;
import com.lostagain.Jam.GameVariableManagement;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.JAMTimerController;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.ScoreControll;
import com.lostagain.Jam.CollisionMap.SceneCollisionMap;
import com.lostagain.Jam.Factorys.NamedActionSetTimer;
import com.lostagain.Jam.GameStatistics.DebugElement;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.Scene.TextOptionFlowPanelCore;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.Jam.SceneObjects.Interfaces.hasUserActions;
import com.lostagain.JamGwt.GWTJAMTimerController;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.TitledPopUpWithShadow;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;
import com.lostagain.JamGwt.JargScene.GwtTextOptionFlowPanel;
import com.lostagain.JamGwt.JargScene.GwtTextOptionFlowPanel.TextOption;
import com.lostagain.JamGwt.JargScene.SceneDialogObject;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;
import com.lostagain.JamGwt.JargScene.SceneTextObject;
import com.lostagain.JamGwt.JargScene.SceneVectorObject;
import com.lostagain.JamGwt.audio.GwtAudioController;
import com.lostagain.JamGwt.audio.Track;
import lostagain.nl.spiffyresources.client.IsSpiffyGenericLogBox;
import lostagain.nl.spiffyresources.client.SliderBar;
import lostagain.nl.spiffyresources.client.spiffycore.DeltaTimerController.DeltaRunnable;
import lostagain.nl.spiffyresources.client.spiffycore.HasDeltaUpdate;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyDataBox;
import lostagain.nl.spiffyresources.client.SliderBar;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyDataBox;
/** for displaying/debugging the games global data 
 * (sorry for spelling mistakes, was rushing the comments here a bit...
 * ...better rushed then absence...I hope)
 * 
 * @author Thomas Wrobel **/
public class GameDataBox extends VerticalPanel implements HasDeltaUpdate,GameStatisticDisplayer {
	
	public static Logger Log = Logger.getLogger("JAM.GameDataBox");

	//logbox for the scene
	ScrollPanel gameLogBox = new ScrollPanel();
	
	//the container we display most of the data in.
	//we use a stackpanel to only display some at a time
	static StackPanel dataContainer = new StackPanel();
	
	/**
	 * Experimental panel that lets you turn on/off console logs
	 */
	VerticalPanel loggerControls = new LoggerControl();
	
	
	// these ones explain themselves
	//static VerticalPanel allGamesSpriteObjects = new VerticalPanel();
	//static VerticalPanel allGamesTextObjects = new VerticalPanel();
	static HashMap<SceneObjectType,VerticalPanel> OverListBoxs = new HashMap<SceneObjectType,VerticalPanel>(); 
	
	

	//========
	static VerticalPanel allGlobalVariablesAndSettingsBox = new VerticalPanel();
	enum variableListMode {
		Alphabetical,LastUpdated,Unordered
	}
	static HorizontalPanel VariableSettingsBar = new HorizontalPanel();//filled in once at start then reused.
	static variableListMode VariableListCurrentMode = variableListMode.Alphabetical;
	static boolean DetachedVariableBox = false;
	static TitledPopUpWithShadow detachedVariableBox;
	//-----
	
	
		static SpiffyDataBox globalVariablesBox = new SpiffyDataBox();
	static SpiffyDataBox specialVariables = new SpiffyDataBox();
	
	/**
	 *  profiling is to work out what takes the longest time.
	 * Used to help optimism the engine
	 ***/
	static VerticalPanel global_info_and_profiling = new VerticalPanel();
	
	/**
	 * MusicStatus shows all tracks known as well as currently playing ones
	 */
	static VerticalPanel MusicStatus = new VerticalPanel();
	
	/**
	 * displays a list of currently active timers
	 */
	static SpiffyDataBox TimerStatus = new SpiffyDataBox();
	
	/**
	 * MusicStatus shows all tracks known as well as currently playing ones
	 */
	static VerticalPanel ObjectsUpdatingstatus = new VerticalPanel();
	
	/**
	 * TextOptionCacheDisplay displays the textoptions that have been displayed thus far.
	 * (these objects will be re-used if the same situation is triggered).
	 * 
	 */
	static VerticalPanel TextOptionCacheDisplay = new VerticalPanel();
	
	/**
	 * 	
	 * Sets autoupdating  on/off for the timer status box 
	 * 
	 */
	static CheckBox AutoUpdateActionSetTimerList = new CheckBox("Auto Update Timer List");	
	
	static TextBox ResumeSerailisedActionSet = new TextBox();
	

	VerticalPanel  TimerStatusAndMonitoring = new VerticalPanel();
	
	static Label currentPages= new Label();
	
	static Label profilingConditional = new Label();
	static Label profilingPropertyChecks = new Label();
	static Label profilingCollisions = new Label();
	
	//used for testing speeds between new and old database look up (not yet used)
	static Label profileingOldSceneObjectDatabase = new Label();
	static Label profileingNewSceneObjectDatabase = new Label();
	
	
	/** opens the log for the collision map data
	 * Used to help debug pathfinding. **/
	static Button DisCollisions = new Button("Collisions");
	
	static VerticalPanel lastcommandstriggered = new VerticalPanel();
	
	Controls controls = new Controls();

	
	/** for displaying/debugging the games global data.
	 * Theres only one of these boxes global to the whole game ***/
	public GameDataBox() {
		
		super();
		
		//tell the statistic class to use this box to display/update when things change
		GameStatistics.setGameStatisticDisplayer(this);
		
		
		//add button to display the collision debug log
		this.add(DisCollisions);
		
		//add the main data container that holds everything else
		this.add(dataContainer);
		
		//make sure its clear (ie, to ensure we dont add the same stuff twice if this is reopened)
		dataContainer.clear();
		
		//add the global game controls (defined in a class below)
		dataContainer.add(controls,"Controls");
		
		gameLogBox.add((IsSpiffyGenericLogBox)JAMcore.GameLogger);
		JAMcore.GameLogger.info("log for game added to inspector");
		
		//set some relivant styles on the scrollpanel
		gameLogBox.setHeight("700px");
		gameLogBox.setWidth("460px");
		gameLogBox.getElement().getStyle().setProperty("wordBreak", "break-all");
		dataContainer.add(gameLogBox,"(games log)");
		
		//log level control
		dataContainer.add(loggerControls,"Log level controls");
		
		//create the variable settings controlls which appear under global game variables
		createGlobalVariableSettingsBar();
		
		//add the panels for games various sorts of objects
		//note; not all objects displayed here atm...divs are missing for one thing.
		//dataContainer.add(allGamesSpriteObjects,"all games sprites");
		//dataContainer.add(allGamesTextObjects,"all games text");
		dataContainer.add(allGlobalVariablesAndSettingsBox,"All global game variables");
		dataContainer.add(specialVariables,"All special variables");
				
		//add various profiling data to the profiling panel

		global_info_and_profiling.add(currentPages);
		global_info_and_profiling.add(new Label("---"));
		global_info_and_profiling.add(profilingConditional);
		global_info_and_profiling.add(profilingPropertyChecks);
		global_info_and_profiling.add(profilingCollisions);
		global_info_and_profiling.add(profileingOldSceneObjectDatabase);
		global_info_and_profiling.add(profileingNewSceneObjectDatabase);
		
		
		//add the prolfing panel now to the main data panel
		dataContainer.add(global_info_and_profiling,"Global infomation & Profiling");
		dataContainer.add(lastcommandstriggered,"last commands triggered");
		dataContainer.add(MusicStatus,"Music Status");
		
		TimerStatus.setWidth("100%");
		TimerStatus.setFirstColStyle("bold");
		
		
		AutoUpdateActionSetTimerList.setWidth("100%");
		AutoUpdateActionSetTimerList.addClickHandler(new ClickHandler() {			
			
			@Override
			public void onClick(ClickEvent event) {
				if (AutoUpdateActionSetTimerList.getValue()) {
					GWTJAMTimerController.addObjectToUpdateOnTick(GameDataBox.this);					
				} else {
					GWTJAMTimerController.removeObjectToUpdateOnTick(GameDataBox.this);	
					
				}
				
			}
		});
		
		
		ResumeSerailisedActionSet.addKeyPressHandler(new KeyPressHandler() {			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				
	            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {

					String seralisedState = ResumeSerailisedActionSet.getText();
					
					Log.info("ResumeSerailisedActionSet:"+seralisedState);
					NamedActionSetTimer.createAndResumeNamedActionState(seralisedState);
					
				}
				
				
				
			}
		});
		
		ResumeSerailisedActionSet.setTitle("(for resuming a seralised action set use this box and hit enter))");
		
		
		
		TimerStatusAndMonitoring.setWidth("100%");
		TimerStatusAndMonitoring.setHorizontalAlignment(ALIGN_LEFT);
		
		TimerStatusAndMonitoring.add(AutoUpdateActionSetTimerList);
		TimerStatusAndMonitoring.add(ResumeSerailisedActionSet);
		TimerStatusAndMonitoring.add(TimerStatus);

		dataContainer.add(TimerStatusAndMonitoring,"Timer Status");

		
		dataContainer.add(ObjectsUpdatingstatus,"Objects Updated Each Frame");
		
		dataContainer.add(TextOptionCacheDisplay,"All knowen text options");
		
		//set the size of this and its datacontainer panel to 100% as well as seeing its style
		//NOTE: You should try to set styles,class's and sizes together
		//This helps the browser have to update ethe screen less.
		this.setSize("100%", "100%");
		dataContainer.setSize("100%", "100%");
		dataContainer.setStyleName("debugStackPanel");
		
		dataContainer.getElement().getStyle().setOverflow(Overflow.HIDDEN);		
	//	allGamesSpriteObjects.getElement().getStyle().setOverflow(Overflow.HIDDEN);
	//	allGamesTextObjects.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		

		//add a click handler to the display collision button
		//When you click it...it opens the log!
		DisCollisions.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

					SceneCollisionMap.openLog();

			}
		});

		
		//trigger the update function to fill in all the data above with the new data
		//(ie, all current objects and such) into their panels
		update();
		
		
	}

	
	/** fill in the data! yay! **/
	static public void update(){
		

		//update profile data
		profilingConditional.setText("Conditional TotalTime:"+GameStatistics.TotalConditionalCheckTime);
		profilingPropertyChecks.setText("Propertychecks TotalTime:"+GameStatistics.TotalPropertyCheckCheckTime);		
		profilingCollisions.setText("Collisions TotalTime:"+GameStatistics.TotalCollisionCheckTime);
		
		profileingOldSceneObjectDatabase.setText("Old Database Lookup-time:"+GameStatistics.TotalOldDatabaseLookupTime);
		profileingNewSceneObjectDatabase.setText("New Database Lookup-time:"+GameStatistics.TotalNewDatabaseLookupTime);
		
		currentPages.setText("Currently "+JAMcore.CurrentScenesAndPages.getPageCount()+" Open Pages:"+JAMcore.CurrentScenesAndPages.OpenPagesInSet);
		
		//
		//--
		

		//------------------
		//populate all sprite list
		// Log.info("updateing game data lists");
		// updateObjectList(allGamesSpriteObjects,SceneObjectDatabase.getAll_sprite_objects().values());
		// dataContainer.setStackText(1, "All "+SceneObjectDatabase.getAll_sprite_objects().size()+" games sprite objects");
		 
		//------------------
		//populate all text list
		// updateObjectList(allGamesTextObjects,SceneObjectDatabase.getAll_text_objects().values());
		 //dataContainer.setStackText(2, "All "+SceneObjectDatabase.getAll_text_objects().size()+" games text objects");
		
		 //new method for all objects
		 Table<SceneObjectType, String, HashSet<SceneObject>> objects = SceneObjectDatabase.getObjectDatabase();
		// Log.info("total objects"+objects.values().size());
		 
		 int i = 1; //position in stack of debug panels 
		 for (SceneObjectType type : objects.rowKeySet()) {
			 
			 Map<String, HashSet<SceneObject>> objectsOfType  = objects.row(type);
			 			 
			// for (HashSet<SceneObject> objectSets : objectsOfType.values()) {
			 		if (!objectsOfType.isEmpty()){
			 				 	
				 	Collection<HashSet<SceneObject>> allObjectsOfType =  objectsOfType.values();
				 	
				 	VerticalPanel ObjectList;			 
				 	//if container already exists we get it, else we create it
				 	if (OverListBoxs.containsKey(type)){

				 		ObjectList = OverListBoxs.get(type);
				 		int index=dataContainer.getWidgetIndex(ObjectList);
				 		dataContainer.setStackText(index, "All "+allObjectsOfType.size()+" games "+type.toString()+" objects");

				 	} else {

				 		ObjectList = new VerticalPanel();
				 		dataContainer.add(ObjectList,"All "+allObjectsOfType.size()+" games "+type.toString()+" objects");
						
				 	}
				 	
					
					//save the list for updating				
					OverListBoxs.put(type,ObjectList);
					
					//populate it
					updateObjectList(ObjectList,allObjectsOfType);
					
					//dataContainer.setStackText(2, "All "+SceneObjectDatabase.getAll_text_objects().size()+" games text objects");

					i++;
			 		}
					
			//}
				 
			
		}
		 

		 //populate all variables
		 updateGlobalVariablelist();
			 
		 //populate special game variables (done as a separate function purely for neatness)
		 refreshSpecialVariables();
		 
		 //update the music
		 updateMusic();
		 
		 //update list of objects being updated each frame
		 updateObjectsUpdateing();
		 
		 //update list of text options currently known
		 updateTextOptionCacheDisplay();
		 
		 //update any timers (ie, namedaction sets about to run)
		 updateCurrentTimers();
		 
		 
		 //add command stack
		 updateCommandList(lastcommandstriggered,GameStatistics.lastCommandsStack);
		 
		 


	}


	/**
	 * Updates the list of current active timers
	 * This might be expanded in future for more details if we wish to try to save/resume timed events
	 */
	public  static void updateCurrentTimers() {
		
		//ensure musicstatus container exists else we can't update it!
		if (TimerStatus!=null && TimerStatus.isAttached()){
			TimerStatus.removeAllRows();
			TimerStatus.add(new Label("Active NamedActionSet Timers:"));
		
			//add all the timers known
			for (String timerID : 	JAMTimerController.activeNamedActionSetTimers.keys()) {	
				
				Set<NamedActionSetTimer> timersCalledTheSame = JAMTimerController.activeNamedActionSetTimers.get(timerID);
				
				TimerStatus.add(new Label("timerID:"+timerID));
				
				
				//boolean isRunning = timerWasFor.isRunning();
				//Label timerDetails = new Label(timerWasFor+"-"+isRunning);
				//TimerStatus.addrow(timerWasFor.getRunnableObject()+"|"+timerWasFor.getRunnableName(),"--"+timerWasFor.getTimeRemaining()+" (from "+timerWasFor.getFuseLength()+")");
				
				for (NamedActionSetTimer namedActionSetTimer : timersCalledTheSame) {

					ActionSetTimerBar actionSetsBar = new ActionSetTimerBar(namedActionSetTimer);
					TimerStatus.add(actionSetsBar);
					
				}
				
				//
				//
					
			}
			
			TimerStatus.add(new Label("Things updating at regular tick:"));
			for (HasDeltaUpdate tickingThings : 	JAMTimerController.getObjectsCurrentlyUpdatedEachTick()) {	
				TimerStatus.add(new Label("Class:"+tickingThings.getClass())); //cant get much info from HasDelta
				
			}

			TimerStatus.add(new Label("Timer updating:"+JAMTimerController.isTimerRunning()+" ManualUpdate:"+JAMTimerController.isManualTickUpdate()));
			
			
		}
	}
	
	/** 
	 * a horizontal panel to start/stop action set timers
	 * 
	 * @author Tom	 *
	 */
	static class ActionSetTimerBar extends HorizontalPanel {
		NamedActionSetTimer timer;
		public ActionSetTimerBar(final NamedActionSetTimer timer){
			this.timer=timer;
			
			Label actionSetsObjectLabel = new Label(timer.getRunnableObject());
			Label actionSetsNameLabel = new Label(timer.getRunnableName());
			
			String createdWhileOnScene = timer.getSceneCurrentWhenCreated().SceneFileName;
			this.setTitle("This NamedActionSet was created while on scene:"+createdWhileOnScene+"\n"
					+ "Its ID is;"+timer.getThisRunnablesID()+"\n"
					+ "Its seralised state is;"+timer.seralise());
			
					
			Label seperator = new Label("___");
			Label timeRemaining = new Label(timer.getTimeRemaining()+" (From "+timer.getFuseLength()+")");
			
			final CheckBox playing = new CheckBox("Updating");
			playing.setValue(true);			
			playing.addClickHandler(new ClickHandler() {				
				@Override
				public void onClick(ClickEvent event) { 
					if (playing.getValue()){
						
						//JAMTimer.activeNamedActionSetTimers.put(timer.getRunnableObject()+"_"+timer.getRunnableName(), timer);
						//might cause concurrent runnable exceptions? Have to work out a way to safely to add the list
						timer.schedule((int)timer.getTimeRemaining());
						ActionSetTimerBar.this.getElement().getStyle().setBackgroundColor("rgb(90, 173, 90)");
						
					} else {
						//again, concurrent exceptions might happen due to adding/removing from inside list? maybe
						timer.cancel();
						ActionSetTimerBar.this.getElement().getStyle().setBackgroundColor("rgb(221, 88, 88)");
						
					}
					
				}
			});
			this.setWidth("100%");
			
			this.add(actionSetsObjectLabel);
			this.add(actionSetsNameLabel);
			this.add(seperator);
			this.add(timeRemaining);
			this.add(playing);
			
		}
	}
	

	public  static void updateMusic() {
		//ensure musicstatus container exists else we cant update it!
		if (MusicStatus!=null && MusicStatus.isAttached()){
			MusicStatus.clear();
			MusicStatus.add(new Label("Music Testing Data"));
		
			//add all the music tracks known
			for (Track trackName : GwtAudioController.getAllKnownTracks()) {
						
			MusicStatus.add(new Label(trackName.toString()));
			
			
			}
		}
	}

	public static void updateObjectsUpdateing() {
		//ensure container exists else we cant update it!
		if (ObjectsUpdatingstatus!=null && ObjectsUpdatingstatus.isAttached()){
			ObjectsUpdatingstatus.clear();
			ObjectsUpdatingstatus.add(new Label("Objects updating:"));
		
			//add all the updating objects  known
			for (HasDeltaUpdate object : GWTJAMTimerController.getObjectsCurrentlyUpdatedEachFrame()) {
						
				ObjectBeingUpdatedBar newBar = new ObjectBeingUpdatedBar(object);
				ObjectsUpdatingstatus.add(newBar);
				
				
			}
		}
	}
	
	//TextOptionCacheDisplay

	public static void updateTextOptionCacheDisplay() {
		//ensure container exists else we cant update it!
		if (TextOptionCacheDisplay!=null && TextOptionCacheDisplay.isAttached()){
			TextOptionCacheDisplay.clear();
			TextOptionCacheDisplay.add(new Label("Text Option Cache::"));
		
			HashMap<hasUserActions, HashMap<String, TextOption>> knownOptions = GwtTextOptionFlowPanel.GWTGlobalTextOptionCache;
			
			for (HashMap<String, TextOption> OptionSet : knownOptions.values()) {
				

				TextOptionCacheDisplay.add(new Label("----"));
				
				for (String key : OptionSet.keySet()) {
					
					TextOption value =  OptionSet.get(key);
					
					Label optiondislabel = new Label("::"+value.getUseractionname());
					optiondislabel.setStylePrimaryName(value.getStylePrimaryName());
					TextOptionCacheDisplay.add(optiondislabel);
					
					
				}
				
			}
			
			
		}
	}

	static class ObjectBeingUpdatedBar extends HorizontalPanel {
		HasDeltaUpdate object;
		public ObjectBeingUpdatedBar(final HasDeltaUpdate object){
			this.object=object;
			
			//first we work out the object type so we can figure out what to display
			if (object.getClass().equals(SceneObject.class) || object.getClass().equals(SceneSpriteObject.class)){
				//we can then safely typecast it
				SceneObject so = (SceneObject)object;					
				super.add(new Label(so.getObjectsCurrentState().ObjectsName));	
				
			} else if ( object.getClass().equals(DeltaRunnable.class) )
			{
				DeltaRunnable so = (DeltaRunnable)object;						
				super.add(new Label("-"+so.getRunnablesName()));
				
			}else if ( object.getClass().equals(GWTAnimatedIcon.class) )
			{
				GWTAnimatedIcon so = (GWTAnimatedIcon)object;						
				super.add(new Label(so.basefilename));
				
			} else {				
				
				super.add(new Label("unrecognised type:"+object.getClass().getName()));		
				
			}
			//default color
			ObjectBeingUpdatedBar.this.getElement().getStyle().setBackgroundColor("rgb(90, 173, 90)");
			
			//now add a button to control it
			final CheckBox playing = new CheckBox("Updating");
			playing.setValue(true);			
			playing.addClickHandler(new ClickHandler() {				
				@Override
				public void onClick(ClickEvent event) { 
					if (playing.getValue()){
						GWTJAMTimerController.addObjectToUpdateOnFrame(object);	
						ObjectBeingUpdatedBar.this.getElement().getStyle().setBackgroundColor("rgb(90, 173, 90)");
						
					} else {
						GWTJAMTimerController.removeObjectToUpdateOnFrame(object);
						ObjectBeingUpdatedBar.this.getElement().getStyle().setBackgroundColor("rgb(221, 88, 88)");
						
					}
					
				}
			});
			super.add(playing);			
			super.setCellHorizontalAlignment(playing, ALIGN_RIGHT);
			
			super.setWidth("100%");			
			
		}
		
		
		
	}
	
	private static void updateGlobalVariablelist() {
		 
		 //clear existing container for it all
		 allGlobalVariablesAndSettingsBox.clear();
		
		 //clear existing box containing the actual variables
		 globalVariablesBox.removeAllRows();
		 
		 //set first row of above bold
		 globalVariablesBox.setFirstColStyle("bold");
		 		 		 
		 //add settings bar (currently doesn't do anything)		
		 allGlobalVariablesAndSettingsBox.setHorizontalAlignment(ALIGN_CENTER);		 
		 allGlobalVariablesAndSettingsBox.add(VariableSettingsBar);
		 
		
		 //get all variable names
		 ArrayList<String> variableNames = GameVariableManagement.GameVariables.getAllUniqueNames();
		 
		 //apply the sort method based on what's currently selected
		 if (VariableListCurrentMode == variableListMode.Alphabetical){
			 Collections.sort(variableNames);
		 } else if (VariableListCurrentMode == variableListMode.LastUpdated) {
			
			 //in future we have order by last updated, which means maintaining a separate stack of variables updated
			
			 //first get the last updated ones (which is only known upto the last 20, if the GameVariables log has been left by default
			 
			 ArrayList<String> lastUpdated = GameVariableManagement.GameVariables.getLastUpdatedVariables();		 
			 
			 //reverse it all! (as the GameVariable list is the opersite way around as to what looks nice on screen)	
			 Collections.reverse(lastUpdated);
			 			 
			 
			 //Finally we add this remainder to last updated then use this as our variable names
			 variableNames = lastUpdated;
			 
			 //oh, and add a note reminding people how the mode works

			 globalVariablesBox.addrow("Last "+AssArray.lastVariablesUpdated.size()+" updated & their current values:");
			 
			 
		 } else {
			 //currently unsorted if not alphabetical (Default) 
		 }
		 
		 //loop over adding each as a new line
		 for (String varName : variableNames) {
			
			 //get data for name
			 String variableData = GameVariableManagement.GameVariables.GetItem(varName);
			 
			 //Label variableNameLabel = new Label(varName);
			 //Label variableDataLabel = new Label(variableData);
			 
			 globalVariablesBox.addrow(varName+":", variableData);
		 }
		 
		 //add repopulated globalVariablesBox back to main container if its not detached
		 if (!DetachedVariableBox){
			 allGlobalVariablesAndSettingsBox.add(globalVariablesBox);
		 }
		 
		 
		 
	//	 String variablesAsABigString = JAM.GameVariables.serialiseForSave("\n");
		// Label variableList = new Label(variablesAsABigString);
		// variableList.getElement().getStyle().setWhiteSpace(WhiteSpace.PRE);
		// allScriptVaribles.add(variableList);
		 
	}


	/**
	 * creates the variable settings bar (should only need to be run once at the start)
	 * @return
	 */
	private static HorizontalPanel createGlobalVariableSettingsBar() {
		 Label orderLabel = new Label("Order: ");
		 CheckBox autoUpdate = new CheckBox("autoupdate");
		 CheckBox detachedGlobalVars = new CheckBox("detached");
		 detachedGlobalVars.setTitle("doesnt work yet sorry");
		 
		 
		 final ListBox orderOptionList = new ListBox();		 
		 for (variableListMode mode : variableListMode.values()) {
			 orderOptionList.addItem(mode.name());		
			 if (mode==VariableListCurrentMode){
				 orderOptionList.setItemSelected(orderOptionList.getItemCount()-1, true);
			 }			 
		 } 
		 
		 VariableSettingsBar.add(orderLabel);
		 VariableSettingsBar.add(orderOptionList);
		 VariableSettingsBar.add(autoUpdate);
		 VariableSettingsBar.add(detachedGlobalVars);
		 
		 orderOptionList.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				VariableListCurrentMode = variableListMode.valueOf(orderOptionList.getSelectedItemText());	
				//refresh the list
				updateGlobalVariablelist();
			}
		});
		 
		 autoUpdate.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				
				boolean checked = event.getValue();
				if (checked){
					//we add a autoupdate handler to the games global variable list
					GameVariableManagement.GameVariables.setOnUpdateRun(new Runnable() {						
						@Override
						public void run() {
							//update the global variable list
							updateGlobalVariablelist();
						}
					});
				} else {
					//we remove any already set
					GameVariableManagement.GameVariables.setOnUpdateRun(null);
				}
					
			}
			 
		});
		 
		 //The game creator can optionally pop this variable box up to monitor it as they are debugging
		 detachedGlobalVars.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					
					if (event.getValue()){
						DetachedVariableBox = true;
						
						//close any existing box
						if (detachedVariableBox!=null){
							detachedVariableBox.CloseDefault();
						}
						
						//create new one (a little inefficient to make this every time, but its only for debugging so doesnt really matter)
						
						//ensure detached by creating a new popup containing the global variables
						detachedVariableBox = new TitledPopUpWithShadow(null,"300px","auto","GlobalVariables:",globalVariablesBox,true);
						globalVariablesBox.getElement().getStyle().setBackgroundColor("rgb(166, 139, 255)");
						globalVariablesBox.setSize("100%", "100%");						
						
						detachedVariableBox.setPopupPosition(Window.getClientWidth()-350, 50);
						//detachedVariableBox.center();
						detachedVariableBox.show();
						
					} else {
						DetachedVariableBox = false;
						//close any detached variable window and reattach here (which we can do just by refreshing)
						updateGlobalVariablelist();
						
					}
					
					
				}				
		 });
		 
		 
		
		return VariableSettingsBar;
	}


	
	public static void refreshSpecialVariables() {
		if (SceneObjectDatabase.currentScene==null){
			Log.info("no current scene, so not bothering to update inspectors gamedatabox ");
			return;
		}
		
		// Log.info("refreshing special game variables (last objects clicked on, etc)");
		 specialVariables.removeAllRows();
		 
	  //   specialVariables.clear();		 
	     specialVariables.setFirstColStyle("bold");
	     
	     specialVariables.addrow("<currentscore>:",""+ScoreControll.CurrentScore);
	     
	     
	     specialVariables.addrow("current scene:", SceneObjectDatabase.currentScene.SceneFileName);
	     
		 if (CurrentScenesVariables.lastSceneObjectUpdated!=null){
			 specialVariables.addrow("<LASTSCENEOBJECT> :", CurrentScenesVariables.lastSceneObjectUpdated.getName());
		 } else {
			 specialVariables.addrow("<LASTSCENEOBJECT> :", "null");
		 }
		 
		 if (CurrentScenesVariables.lastSceneObjectClicked!=null){
			 specialVariables.addrow("<LASTCLICKEDON> :", CurrentScenesVariables.lastSceneObjectClicked.getName());
		 } else {
			 specialVariables.addrow("<LASTCLICKEDON>  :", "null");
		 }
		 
		 //last clicked location (never null)
		 specialVariables.addrow("<LastClickedX>,<LastClickedY> :",  CurrentScenesVariables.lastclicked_x+","+ CurrentScenesVariables.lastclicked_y+","+ CurrentScenesVariables.lastclicked_z);
		 specialVariables.addrow("LastClickedScreen X,Y:",  CurrentScenesVariables.lastclickedscreen_x+","+ CurrentScenesVariables.lastclickedscreen_y);
		 //--
		
		 if (InventoryPanelCore.currentlyHeldItem!=null){
			 specialVariables.addrow("<HELDITEM> :", ((IsInventoryItem)InventoryPanelCore.currentlyHeldItem).getName());
		 } else {
			 specialVariables.addrow("<HELDITEM> :", "null");
		 }
		 specialVariables.addrow("---"); 
		 if (CurrentScenesVariables.lastSpriteObjectUpdated!=null){
			 specialVariables.addrow("<LASTSPRITEITEM> :", ((SceneObject)CurrentScenesVariables.lastSpriteObjectUpdated).getName());
		 } else {
			 specialVariables.addrow("<LASTSPRITEITEM> :", "null");
		 }
		 
		 if (CurrentScenesVariables.lastDivObjectUpdated !=null){
			 specialVariables.addrow("lastDivObjectUpdated:", ((SceneObject)CurrentScenesVariables.lastDivObjectUpdated).getName());
		 } else {
			 specialVariables.addrow("lastDivObjectUpdated:", "null");
		 }
		 
		 if (CurrentScenesVariables.lastInventoryObjectClickedOn!=null){
			 specialVariables.addrow("lastInventoryObjectClickedOn:", ((SceneObject)CurrentScenesVariables.lastInventoryObjectClickedOn).getObjectsCurrentState().ObjectsName);
		 } else {
			 specialVariables.addrow("lastInventoryObjectClickedOn:", "null");
				 
		 }
		 if (CurrentScenesVariables.lastTextObjectUpdated!=null){
			 specialVariables.addrow("lastTextObjectUpdated:", ((SceneObject)CurrentScenesVariables.lastTextObjectUpdated).getObjectsCurrentState().ObjectsName);
		 } else {
			 specialVariables.addrow("lastTextObjectUpdated:", "null");
		 }
		 if (CurrentScenesVariables.lastDialogueObjectUpdated!=null){
			 specialVariables.addrow("lastDialoueObjectUpdated:", ((SceneObject)CurrentScenesVariables.lastDialogueObjectUpdated).getObjectsCurrentState().ObjectsName);
		 } else {
			 specialVariables.addrow("lastDialoueObjectUpdated:", "null");
		 }
		 
		 if (CurrentScenesVariables.lastVectorObjectUpdated !=null){
			 //cast should be removable later
			 specialVariables.addrow("lastVectorObjectUpdated:",((SceneObject) CurrentScenesVariables.lastVectorObjectUpdated).getObjectsCurrentState().ObjectsName);
		 } else {
			 specialVariables.addrow("lastVectorObjectUpdated:", "null");
		 }
		 
		 
		 if (CurrentScenesVariables.lastObjectThatTouchedAnother !=null){
			 //cast should be removable later
			 specialVariables.addrow("<TOUCHER>:",CurrentScenesVariables.lastObjectThatTouchedAnother.getName());
		 } else {
			 specialVariables.addrow("<TOUCHER>:", "null");
		 }
	}

	@Override
	public void updateLastCommandList() {
		updateCommandList(lastcommandstriggered,GameStatistics.lastCommandsStack);

	}
	/** updates the displayed last command list **/
	private static void updateCommandList(VerticalPanel panel,			
			Queue<GameStatistics.DebugElement> lastCommandsStack) {
				
		
		panel.clear();
		
		
		for (GameStatistics.DebugElement debugevent : lastCommandsStack) {
			
			Label newlab = new Label(debugevent.debugstring);
			newlab.getElement().setAttribute("style", debugevent.debugstyle);
			newlab.setTitle(debugevent.debugtooltip);
			
			panel.add(newlab);
			
		}
		
	}
	

	
	/** takes a list of objects "collection" and puts it into the vertical panel suppled "updateThis"
	 * The objects can be any scene object type.
	 * So we use "extends SceneObject" to mean "anything that extends SceneObject"
	 * ie, SceneSpriteObject,SceneDialogObject etc**/
	private static void updateObjectList(VerticalPanel updateThis, Collection<HashSet<SceneObject>> collection) {
			
		
		Log.info("updateing scene object list.");
		//first clear the supplied panel
		updateThis.clear();

		//then get an interator from the supplied collection of objects
		Iterator<HashSet<SceneObject>> sit = collection.iterator();
					
		//then use the iterator to loop over the objects
		while (sit.hasNext()) {
			
			final HashSet<SceneObject> sceneObjects = sit.next();
			
			for (final SceneObject so : sceneObjects) {

				
				//get the objects name (all the names in the set should be the same anyway
				ObjectDataBar objectbar =  new ObjectDataBar(so);
				
				
				//add to the vertical panel
				updateThis.add(objectbar);
				
			}
			
		
			
		}
	}
	
	/** some global controls for the game, such as reset as such.
	 * Probably doesn't work as I don't update this too often.
	 * 
	 * its a separate class just for code neatness/separation, its purely a vertical panel with
	 * buttons**/
	class Controls extends VerticalPanel {
		
		/** displays if the delta timer is running **/
		Label lab_deltatimeInformation = new Label("...");
		
		public Controls() {			
			super();
			
			//Useful scene controls
			
			//create and add a game reset buttom
			Button reset = new Button("Reset Game");
			
			reset.addClickHandler(new ClickHandler() {
								
				@Override
				public void onClick(ClickEvent event) {
					
					// reset the whole game, we do this by resetting each scene and the inventory
					//ATM all this does is auto-enter "reset"
					//the real reset has to be handled by your game script
					InstructionProcessor.processInstructions(
							"- EnterAns = reset \n - Message = testing reset", "GameDebugBox", null); //$NON-NLS-1$
			
					
				
				}
			});
			
			//add the reset button to this vertical panel
			add(reset);
			
			//refresh lists whenever the button is hit yay!
			Button refreshLists = new Button("Refresh lists");
			
			refreshLists.addClickHandler(new ClickHandler() {
								
				@Override
				public void onClick(ClickEvent event) {

					//update delta time info:
							controls.lab_deltatimeInformation.setText("Frame updates Running:"+GWTJAMTimerController.isFrameUpdatesRunning()+" # of objs:"+
									GWTJAMTimerController.getObjectsCurrentlyUpdatedEachFrame().size());
							
							//--
							
					//trigger the refresh function....yay!
					GameDataBox.update();
					
				}
			});
			
			add(refreshLists);
			
			//clear commandlog
			Button clearcommands = new Button("Clear command history");
			
			clearcommands.addClickHandler(new ClickHandler() {
								
				@Override
				public void onClick(ClickEvent event) {
					
					GameStatistics.lastCommandsStack.clear();
					updateCommandList(lastcommandstriggered,GameStatistics.lastCommandsStack);
					
				}
			});
			
			add(clearcommands);
			
			CheckBox toggleLogs = new CheckBox("Enable Log Statements:");
			
			if (JAMcore.DebugMode && (!JAMcore.DebugSetting.equalsIgnoreCase("trueNoLog"))){
				//on by default if we are in debug mode and not "trueNoLog" debug mode
				toggleLogs.setValue(true);								
			} else {
				toggleLogs.setValue(false);	
			}
			
			toggleLogs.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					
					boolean enableLogs = event.getValue();
					
					JAM.setLoggingEnable(enableLogs);
															
					if (!enableLogs){
						
						for (SceneWidget scene : SceneWidget.all_scenes.values()) {

							SceneDataBox.clearScenesInspectors(scene);
							
						}
						
						
					}
					
					
						
				}
			});
						
			
			
			final ListBox filterBox = new ListBox();
			
			filterBox.addItem(Level.ALL.getName());
			filterBox.addItem(Level.FINEST.getName());
			filterBox.addItem(Level.INFO.getName());
			filterBox.addItem(Level.WARNING.getName());
			filterBox.addItem(Level.SEVERE.getName());
			
			filterBox.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					String logstring = filterBox.getSelectedItemText();
					Level selectedLogLevel = Level.parse(logstring);
					
					JAM.setLogLevel(selectedLogLevel);
					
					
				}
			});
			Label filterLogLabel = new Label("Filter log:");
			
			HorizontalPanel logOptions = new HorizontalPanel();
			logOptions.add(toggleLogs);		
			logOptions.add(filterLogLabel);
			logOptions.add(filterBox);
			
			
			add(logOptions);
			final CheckBox keepAllLogs = new CheckBox("Keep all Logs (overide stack size of "+GameStatistics.MAX_LAST_COMMAND_LIST_SIZE+")");				
			add(keepAllLogs);
			
			keepAllLogs.addValueChangeHandler(new ValueChangeHandler<Boolean>() {				
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (event.getValue()){
						GameStatistics.setKeepLogs(true);						
					} else {
						GameStatistics.setKeepLogs(false);						
					}
				}
			});
			
			
			
			final CheckBox AutoUpdate = new CheckBox("Auto Update OpenPage");				
			AutoUpdate.addClickHandler(new ClickHandler() {			
				
				@Override
				public void onClick(ClickEvent event) {
					if (AutoUpdate.getValue()) {
						GWTJAMTimerController.addObjectToUpdateOnTick(GameDataBox.this);					
					} else {
						GWTJAMTimerController.removeObjectToUpdateOnTick(GameDataBox.this);	
						
					}
					
				}
			});
			

			add(AutoUpdate);

			add(new Label("------"));
			
			
			final CheckBox refinePaths = new CheckBox("Refine Pathfinding:");		
			
			//set to existing value
			refinePaths.setValue(!SceneCollisionMap.isRefineDisabled());
			
			refinePaths.addClickHandler(new ClickHandler() {		
				@Override
				public void onClick(ClickEvent event) {
					if (refinePaths.getValue()) {
						SceneCollisionMap.setRefineDisabled(false);
						
					} else {
						SceneCollisionMap.setRefineDisabled(true);
						
					}
					
				}
			});
			

			add(refinePaths);
			
			//Experiments panel
			// current contents:   delta speed adjust
			//
			DisclosurePanel expirementalPanel = new DisclosurePanel("Delta Info/Expirements (may not work/break game)");
			
			
			VerticalPanel expirementlist = new VerticalPanel();
			
			
			Label lab_deltatime = new Label("multiply delta time:");
			
			SliderBar deltatimeadjustment = new SliderBar(0.0,2.0);
			deltatimeadjustment.setWidth("300px");
			deltatimeadjustment.setCurrentValue(1.0);
			deltatimeadjustment.setStepSize(0.1);
			deltatimeadjustment.setNumTicks(20);
			deltatimeadjustment.addValueChangeHandler(new ValueChangeHandler<Double>() {
				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					
					Double l = (event.getValue());
					
					GWTJAMTimerController.setExpirementalDeltaMult(l);
					
					lab_deltatime.setText("Multiply delta time by:"+l);
					
					
				}			
			});
			//
			

			expirementlist.add(lab_deltatimeInformation);
			expirementlist.add(lab_deltatime);
			expirementlist.add(deltatimeadjustment);
						
			expirementalPanel.add(expirementlist);
			add(expirementalPanel);
			
		}
		
	}
	
	
	/** the global game timer will update this box every so often, so we use this to update other stuff if 
	 * its visible.
	 * NOTE: some stuff will update anyway regardless. This is for things that dont or if you want more fine-grained updates then those give.
	 * (ie, by default it tells you what music is running, it wont update the volume controll however as it fades in and out - only when it stops completely.
	 * To see the volume change as it fades, we need to use this update system)**/
	@Override
	public void update(float delta) {
		
		
		
		//update timers if we are on that page
		if (dataContainer.getSelectedIndex()==dataContainer.getWidgetIndex(TimerStatusAndMonitoring)){
			updateCurrentTimers(); 
		}
		
		//update music if we are on that page
		if (dataContainer.getSelectedIndex()==dataContainer.getWidgetIndex(MusicStatus)){
			updateMusic();
		}
		
		//update objectsupdating if we are on that page
		if (dataContainer.getSelectedIndex()==dataContainer.getWidgetIndex(ObjectsUpdatingstatus)){
			updateObjectsUpdateing();
		}
		
	}



}
