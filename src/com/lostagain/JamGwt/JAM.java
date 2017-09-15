package com.lostagain.JamGwt;

import com.allen_sauer.gwt.voices.client.SoundType;
import com.google.common.base.Optional;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

import com.allen_sauer.gwt.voices.client.Sound;

import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackError;
import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackRunnable;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;


import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


//Note; Even though this should be replaced, its still needed for GWTClock
//Which is used in the cuypers code.
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.DOM;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


import com.lostagain.Jam.AssArray;
import com.lostagain.Jam.GameManagementClass;
import com.lostagain.Jam.GameTextDatabase;
import com.lostagain.Jam.GamesInterfaceTextCore;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.JAMTimerController;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.JAMcore.KeyState;
import com.lostagain.Jam.JamGlobalGameEffects;
import com.lostagain.Jam.OptionalImplementations;
import com.lostagain.Jam.PageLoadingData;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.SceneAndPageSet;
import com.lostagain.Jam.WebGameFunctions;
import com.lostagain.Jam.Factorys.IsTimerObject;
import com.lostagain.Jam.InstructionProcessing.CommandList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Interfaces.HasLoadMonitor;
import com.lostagain.Jam.Interfaces.IsPopupPanel;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.InventoryItems.InventoryItemFactory;
import com.lostagain.Jam.InventoryItems.TigItemCore;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.audio.MusicBoxCore;
import com.lostagain.JamGwt.clickVisualiser;
import com.lostagain.JamGwt.GwtJamImplementations.BasicGameInformationImp;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;
import com.lostagain.JamGwt.GwtJamImplementations.GWTImagePreloader;
import com.lostagain.JamGwt.GwtJamImplementations.GwtChapterControl;
import com.lostagain.JamGwt.GwtJamImplementations.GwtCuypersCodeSpecificFunctions;
import com.lostagain.JamGwt.GwtJamImplementations.GwtGlobalGameEffects;
import com.lostagain.JamGwt.GwtJamImplementations.GwtMouseCursorManagement;
import com.lostagain.JamGwt.GwtJamImplementations.GwtPageElementStyleCommands;
import com.lostagain.JamGwt.GwtJamImplementations.GwtScreenMangementImp;
import com.lostagain.JamGwt.IconPacks.JamImages;
import com.lostagain.JamGwt.InventoryObjectTypes.HeldItemBox;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryItem;
import com.lostagain.JamGwt.InventoryObjectTypes.ItemDropController;
import com.lostagain.JamGwt.InventoryObjectTypes.toggleImageGroupPopUp;
import com.lostagain.JamGwt.JargScene.GwtCoOrdinateSpaceConverter;
import com.lostagain.JamGwt.JargScene.SceneDialogObject;
import com.lostagain.JamGwt.JargScene.SceneDivObject;
import com.lostagain.JamGwt.JargScene.SceneInputObject;
import com.lostagain.JamGwt.JargScene.SceneLabelObject;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;
import com.lostagain.JamGwt.JargScene.SceneVectorObject;
import com.lostagain.JamGwt.JargScene.SceneWidgetVisual;
import com.lostagain.JamGwt.JargScene.ServerOptions;
import com.lostagain.JamGwt.JargScene.CollisionMap.CollisionMapVisualiser;
import com.lostagain.JamGwt.JargScene.SceneObjects.GWTSceneObjectFactory;
import com.lostagain.JamGwt.JargScene.SceneObjects.GWTSoundFactory;
import com.lostagain.JamGwt.JargScene.debugtools.GameDataBox;
import com.lostagain.JamGwt.JargScene.debugtools.LoggerControl;
import com.lostagain.JamGwt.JargScene.debugtools.ObjectInspector;
import com.lostagain.JamGwt.JargScene.debugtools.SceneDataBox;
import com.lostagain.JamGwt.JargScene.debugtools.SpiffyObjectDataBox;
import com.lostagain.JamGwt.Sprites.InternalAnimations;
import com.lostagain.JamGwt.audio.GWTMusicBox;
import com.lostagain.JamGwt.audio.GwtAudioController;
import com.lostagain.JamGwt.audio.Track;


import lostagain.nl.spiffyresources.client.spiffycore.SpiffyCalculator;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyAnimatedIcon;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyArrow;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyClock;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyDragPanel;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyFunctionsGWT;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyLogBox;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyPreloader;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyAnimatedIcon;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyArrow;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyClock;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyFunctionsGWT;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyLogBox;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyPreloader;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.Window;

/**
 * Entry point class..
 * This is the start of the JAM engine.
 * Like all GWT, Game evacuation starts in <code>onModuleLoad() method</code>.
 */
public class JAM implements EntryPoint, ValueChangeHandler<String>, NativePreviewHandler,HasLoadMonitor { //IsInterfaceVisualHandler
	/** The main log for this gwt implementation of the jam **/
	public static Logger GwtLog = Logger.getLogger("JAM");
	
	//Global game settings....................................	
	/** The home directory of the host page **/
	public static String homeurl = GWT.getHostPageBaseURL();


	
	

	/**
	 * This should be run asap.
	 * This gives the JamCore the needed variables and setup information to run
	 * See the BasicGameInformation class
	 * We also give it screen management and basic game functions at the same time, although these could be given later
	 * if we used individual set statements.
	 **/
	static Object variablenotused = RequiredImplementations.setBasicGameInformationImplemention(new BasicGameInformationImp());

	/**
	 * create our gwt save manager
	 */
	private static final GwtSaveGameManager SAVEMANAGER = new GwtSaveGameManager();
	
	/**
	 * very important
	 * setAllRequiredImplementations gives the jam core copies of all the various gwt specific implementations of essential classes
	 * Without this the core couldn't do much at all. It wouldn't know how to draw stuff to the screen , or access files etc
	 * 
	 */
	//TODO: add good descriptions to all the classes passed to the core here
	static Object variablenotused2 = RequiredImplementations.setAllRequiredImplementations(
			new GwtVisualHandlerImp(),
			new GwtScreenMangementImp(), 
			new BasicGameFunctionsImp(), 
			new GwtFileManagerImp(),
			SAVEMANAGER,
			new GWTSceneObjectFactory(),
			new GwtChapterControl(),
			new GwtCoOrdinateSpaceConverter(),
			new GwtScoreBoard() //back in the cuypers day, "GameIcons/ScoreBox.jpg" was specified for the boxs background. Its now done by css
			);
	
	
	
	
	
	/** This variable keeps track of the current message for the current loading stage. Not used after the first scene starts loading.
	 * This is thus only intended for the very first part of game engine loading   **/
	public static String currentLoadingText = "Loading game please wait a few seconds";


	final public static String gamesurllocation = getLocation();


			

	static boolean fixedSizeInterface = false; // set to false normally.

	/**
	 * This is the games list of semantic indexes. It shouldn't really be needed to
	 * refer to this directly except for debugging. Rest assured, however, it is
	 * used. Despite what eclipse might think. 
	 **/
//	private SSSIndex GamesSemanticIndexs;

	/** Images! All the built in images for the game are stored here **/
	static JamImages JargStaticImages = (JamImages) GWT	.create(JamImages.class);

	static HTML MainStoryText = new HTML();

	/**array of widget names to be added to story text when new text
	 * loads;**/
	static ArrayList<String> widgets_to_add_ids = new ArrayList<String>();

	// story text array
	static AssArray StoryTextStore = new AssArray();

	
	public static final TypedLabel Feedback = new TypedLabel(""); 



	static final VerticalPanel FeedbackContainer = new VerticalPanel();

	// Chapter list
	/**
	 * This is a optional list to switch between chapters thats at the right side of the screen
	 * most games dont have to have this
	 */
	public static VerticalTabs ChapterList = new VerticalTabs();

	// Preloader
	static SpiffyPreloader GamePreloader;

	final Label LocationLabel = new Label("Locations;"); 

	




	// private static boolean newplayer = false;

	// story current locations
	
	// list of dynamic pages
	/**
	 * Stores dynamic pages. Ones that should be reloaded from the cache each
	 * time they are displayed
	 **/
	public final static AssArray dynamicPages = new AssArray();
	
	public static int CurrentLocationTabsIndex = 0;



	// fader
	final static public fader fadeback = new fader();

	// login background
	final static public SpiffyImage loginbackground = new SpiffyImage();

	// decoration Icons
	final static public GWTAnimatedIcon StatueFeat = new GWTAnimatedIcon(JamImages.LadyFeatBig);
	final static public GWTAnimatedIcon StatueHead = new GWTAnimatedIcon(JamImages.LadyClockBig);
	final static public GWTAnimatedIcon solider    = new GWTAnimatedIcon(JamImages.SoldierBig);



	static AbstractImagePrototype BuyOpenButton = JamImages.BigBuyOpenButton;
	static AbstractImagePrototype BuyCloseButton = JamImages.BigBuyCloseButton;

	static Image BuyBackClosed = BuyCloseButton.createImage();
	static Image BuyBack = BuyOpenButton.createImage();

	static AbstractImagePrototype OpenClueBoxButton = JamImages.BigOpenClueBox;
	static Image OpenClueBox = OpenClueBoxButton.createImage();

	static AbstractImagePrototype CloseClueBoxButton = JamImages.BigOpenClueBox;
	static AbstractImagePrototype CloseClueBox = CloseClueBoxButton;



	// Backtexture
	static Image spacer;// = new Image("./GameIcons/BIG/TitleStrip.jpg");
	static Image TopStripBigGap;// = new Image("./GameIcons/BIG/TitleStrip.jpg");



	static GWTAnimatedIcon Cuyperstitle = new GWTAnimatedIcon(JamImages.BigCuypers2LogoImages);


	public final static SpiffyClock gwt_clock = new SpiffyClock(50, 50);

	// timers to animate icons above;
	public static IsTimerObject Blinking;
	public static IsTimerObject Cyclonish;


	// interface bits

	// set to defaults
	public static AbstractImagePrototype[] BigInventoryImages    = InternalAnimations.BigDefaultInventoryImages;;
	public static AbstractImagePrototype[] MediumInventoryImages = InternalAnimations.MediumDefaultInventoryImages;;
	public static AbstractImagePrototype[] SmallInventoryImages  = InternalAnimations.SmallDefaultInventoryImages;;

	static public GWTAnimatedIcon DefaultInventorysButton;// = new GWTAnimatedIcon(InternalAnimations.BigDefaultInventoryImages);
	

	final static public GWTAnimatedIcon ControllPanelButton = new GWTAnimatedIcon(
			InternalAnimations.BigControllPanelImages);

	final static public GWTAnimatedIcon ForumLinkButton = new GWTAnimatedIcon(
			InternalAnimations.BigForumImages);


	final static public GWTAnimatedIcon MessageHistoryButton = new GWTAnimatedIcon(
			JamImages.LogBoekImages);


	final static public GWTAnimatedIcon MessageBackButton = new GWTAnimatedIcon(JamImages.MessPrevImages);


	final static public GWTAnimatedIcon MessageForwardButton = new GWTAnimatedIcon(
			JamImages.MessNextImages);

	final static public GWTAnimatedIcon SecretsPopupPanelButton = new GWTAnimatedIcon(
			InternalAnimations.BigSecretsImages);

	// interface icons
	// final static public InterfaceIcon LoadingBar = new
	// InterfaceIcon("GameIcons/loading0.png",7);


	final static public GWTAnimatedIcon closeallwindows = new GWTAnimatedIcon(JamImages.CloseAll);

	final static ControllPanel ControlPanel = new ControllPanel();
	public final static TitledPopUpWithShadow ControllPanelShadows = new TitledPopUpWithShadow(
			null,
			"50%", "25%", GamesInterfaceText.MainGame_SaveOrLoadYourGame, ControlPanel); //$NON-NLS-1$ //$NON-NLS-2$ 

	public static LoadSaveGamePopup    loadGamePopup = null;
	public static GwtSaveGameManager   saveGamePopup = null; //note; These have to be made AFTER the debug mode has been set




	// inventory popups and other inventory things
	// -------------------------------------------

	/**
	 * A box that displays the current held item. Used if there is a div to attach it too on the page
	 * Clicking this item box will drop the current item
	 */
	public final static HeldItemBox heldItemBox = new HeldItemBox();


	






	//public final static HashMap<String, InventoryPanelCore> allInventorys = new HashMap<String, InventoryPanelCore>() {
	//	{
	//		put("Inventory Items", defaultInventory);
	//	}
	//};

	public static GwtNotebook PlayersNotepad;// = new Notebook(); //also creates its own icon
	
	static TitledPopUpWithShadow PlayersNotepadFrame;// = new PopUpWithShadow(
			//null,
			///"50%", "25%", GamesInterfaceText.MainGame_Charecter_Profiles, PlayersNotepad); //$NON-NLS-1$ 

	public final static GwtSecretsPanel SecretPanel = new GwtSecretsPanel();
	
	public final static TitledPopUpWithShadow SecretPanelFrame = new TitledPopUpWithShadow(null,
			"50%", "25%", GamesInterfaceText.MainGame_Secrets_Found, SecretPanel); //$NON-NLS-1$ 


	// clue(aka Joker Panel) panel
	final static CluePanel CluePopUp = new CluePanel();
	final static TitledPopUpWithShadow CluePanelFrame = new TitledPopUpWithShadow(null,
			"50%", "25%", "Clue Panel", CluePopUp.asWidget(),CluePopUp.DRAGABLE()); //$NON-NLS-1$ 

	// Message History
	public final static FeedbackHistoryVisualiser messagehistory = new FeedbackHistoryVisualiser();


	final static HorizontalPanel AnsBar = new HorizontalPanel();

	public static boolean ControllPanelOpen = false;

	public static boolean MessageHistoryOpen = false;
	public static boolean SecretsPanelOpen = false;

	// locations
	//TODO: all of these should be in core like inventory_url is handled
	public static final String textfetcher_url = homeurl + "text%20fetcher.php"; //$NON-NLS-1$
	public static final String emailer_url = homeurl+ "Login_System/Emailer.php"; //$NON-NLS-1$

	// audio location
	public static final String audioLocation_url = homeurl + "audio/"; //$NON-NLS-1$

	// item locations(use version in core)
	//public static final String inventory_url = homeurl + "InventoryItems/"; //$NON-NLS-1$

	// item locations
	static final String notebookpages_url = homeurl + "NotebookPages/"; //$NON-NLS-1$

	//sound type
	public static final String defaultSoundType = Sound.MIME_TYPE_AUDIO_OGG_VORBIS;





	// main music box (for music)
	public static final GWTMusicBox musicPlayer              = new GWTMusicBox();
	
	//for effects
	public static final SpecialEffectOverlays EffectOverlay = new SpecialEffectOverlays();

	// last clicked on X/Y/item
	public static int lastclicked_x = 0;

	public static int lastclicked_y = 0;

	public static Widget lastpopupmessage;
	public static SpiffyArrow messageArrow;


	// player options

	public static boolean SoundEffectOn = true;
	static boolean AnimationEffectsOn = true;
	// screen settings
	public static int Story_Text_Height = 0;
	public static int Story_Text_Width = 0;

	Timer ResizeOnLoad;

	private static int Game_Window_Width;
	private static int Game_Window_Height;

	// zoom fix toggle (this determines if +1 is added on the zoom image width,
	// to fix a loading bug)
	public static boolean zoomfixboolean = true;

	// All the games strings stored here;
	static GamesInterfaceTextCore GamesText = new GamesInterfaceText();

	// Score/Joker and Clue dropdown
	public static final VerticalPanel ScoreAndClueBox = new VerticalPanel();

	// Clue disclosure
	public static final DisclosurePanel ClueReveal = new DisclosurePanel("Clues:");

	// Score and Jocker Container
	public static final HorizontalPanel ScoreAndJokerContainer = new HorizontalPanel();

	// Joker Button
	public static final Image Joker = new Image(JargStaticImages.questionmark());

	
	

	

	// static //scorebox
	// Image ScoreBack = new Image("./GameTextures/TitleStrip_score.jpg");
	// Image BuyBack = new Image("./GameTextures/TitleStrip_buy.jpg");
	// Image ScoreBack =
	// JargStaticImages.BIGTitleStrip_Scoreback().createImage();

	public  static String CurrentBackground;
	//public  static boolean endInstructions = false;
	// calculator
	public static final SpiffyCalculator calc = new SpiffyCalculator();

	// clock silly bit
	public static final Image clockpopup = new Image(
			JargStaticImages.stopthat());

	// Title Bar
	// TitleBar Left
	public static final HorizontalPanel TitleBarLeft = new HorizontalPanel();
	public static final HorizontalPanel TitleBarRight = new HorizontalPanel();

	@Override
	public void onModuleLoad() {

		GwtLog.info("game loading...");

		
		//set correct debug mode based on DebugSetting string taken from the Debug param in the html
		if (JAMcore.DebugSetting.equalsIgnoreCase("true"))
		{
			JAMcore.DebugMode = true;	
			JAM.setLoggingEnable(true);		
		}		
		if (JAMcore.DebugSetting.equalsIgnoreCase("trueNoLog"))
		{
			JAMcore.DebugMode = true;	
			JAM.setLoggingEnable(false);		
		}		
		if (JAMcore.DebugSetting.equalsIgnoreCase("false"))
		{
			JAMcore.DebugMode = false;	
			JAM.setLoggingEnable(false);		
		}		
		if (JAMcore.DebugSetting.equalsIgnoreCase("falseWithLogs"))
		{
			JAMcore.DebugMode = false;	
			JAM.setLoggingEnable(true);		
		}
		
		if (!JAMcore.DebugMode){
			JAMcore.GameLogger  = SpiffyLogBox.createLogBox(false); //remove the box
		}
		

		//set game loading implementation first so we can display loading progress
		OptionalImplementations.SetHasLoadMonitor(this);
		if (OptionalImplementations.gamesLoadMonitor.isPresent()){		
			//advance loadingdiv, if theres one			
			OptionalImplementations.gamesLoadMonitor.get().advanceGameLoadingBar();			
		}
		


		
		//All uses of the JAM engine will need equilivents of GWT functions set up for
		//there own implementations.
		//The following are GWT specific implementations;

		//setup game time controller
		//This will tell the game to update each frame
		GWTJAMTimerController.setup(); //games timer system wont work till this is done

		
		GWTJAMTimerController.setMaxDelta(500);
		
		
		
		
		//setup the game object creator
	//	GWTSceneObjectFactory.setup();
		
		
		InventoryItemFactory.setup(new GWTInventoryItemFactory() );
		
		 
		//create initial pageset
		//can we do this automatically somewhere in core?
		JAMcore.standardSetup();

		DefaultInventorysButton =  (GWTAnimatedIcon) JAMcore.DefaultInventorysButton;
				//(GWTAnimatedIcon) 
				
		//maybe the following 4 things could be added to the to the factory methods
		//and thus auto-made/assigned by the core rather then here?
		
		
		// JAMcore.PlayersScore = new SpiffyScoreBoard("GameIcons/ScoreBox.jpg"); //TODO: 
		 //A tag can be used for platform agnostic positioning from core
		 

		 //create answer box
		 GwtAnswerBox AnsBox =  new GwtAnswerBox();			
		 JAMcore.AnswerBox = Optional.of( AnsBox );
		 
		 GWTAnswerButton enteransgwt =  new GWTAnswerButton(GamesInterfaceText.MainGame_Submit);
		 JAMcore.EnterAns  = Optional.of( enteransgwt );
		 ///--
		 
		 //And the clue box
		 JAMcore.playersClues = new GwtClueBox();
		 
		 //notepad and its popup
		 PlayersNotepad = new GwtNotebook(); //also creates its own icon
		 
		 //give a reference of this notepad to the Core
		 JAMcore.PlayersNotepad = JAM.PlayersNotepad;
		 
		 PlayersNotepadFrame = new TitledPopUpWithShadow(
					null,
					"50%", "25%", GamesInterfaceText.MainGame_Charecter_Profiles, PlayersNotepad); //$NON-NLS-1$ 

		 
		//setup the sound object creator
		GWTSoundFactory.setup();
		
		//  file manager and save manager (both of these maybe can go into the combo set at the top?)
		//	RequiredImplementations.SetFileManager(new GwtFileManagerImp());
		//	RequiredImplementations.setSaveManager(new SaveGameManager());
		
		//setup image preloader
		OptionalImplementations.SetImagePreloadFunctions(new GWTImagePreloader());
		
		
		//setup the general sound manager
		OptionalImplementations.SetAudioControllerFunctions(new GwtAudioController());
				


		//Give the core the functions needed for adding stuff to the screen either by co-ordinate or ID
		//It cant do this itself - the core is strictly non visual - so it needs to be given these functions
	//	RequiredImplementations.setScreenManagementImplementation(new GwtScreenMangementImp());
		
		//set the instructionprocessor to use the GWTimplementation for html element changing
		OptionalImplementations.setPageStyleCommandImplemention(new GwtPageElementStyleCommands());
		
		
		
		//cuypers specific functions
		OptionalImplementations.SetCuypersCodeSpecificFunctions(new GwtCuypersCodeSpecificFunctions());
		
		//Secrets Panel (also just used in cuypers, but in theory could be used elsewhere)
		OptionalImplementations.SetJamSecretsPanel(new GwtSecretsPanel());
		
		
		JamGlobalGameEffects.setJamGlobalGameEffectsFunctions(new GwtGlobalGameEffects());		

		OptionalImplementations.SetWebGameFunctionality(new WebGameFunctions() {			
			@Override
			public void openWebpage(String url, String name, String features) {
				Window.open(url, name, features); //only two functions for now, no need for its own class so we do it inline
			}

			@Override
			public void sendEmailTemplate(String templatename) {
				JAM.sendEmailTemplate(templatename);

			}
		});
		OptionalImplementations.SetMouseVisualImplementation(new GwtMouseCursorManagement());
		OptionalImplementations.SetCompressionSystemFunctions(SAVEMANAGER); //we can just use our savemanager
		OptionalImplementations.setBrowserStorageImplementation(SAVEMANAGER);//we can just use our savemanager
		//----------------------------------------------
		//--------------------------------------------------------

		//Set sound preference
		// Prefer Web Audio, then try Flash, and then HTML5
		GwtAudioController.soundController.setPreferredSoundTypes(SoundType.HTML5,SoundType.WEB_AUDIO,  SoundType.FLASH); //In future shouldn't be needed

		//-------------------------------------------------------------		
		//Create save and load popups
		//These are set up after the debug mode is picked, as theres extra options for developers if debug is turned on
		saveGamePopup = new GwtSaveGameManager();
		loadGamePopup = new LoadSaveGamePopup();
		//-------------------------------------------------------------

		// preload the images necessary for login
		Image.prefetch("GameTextures/loginbox_back.jpg");

		//set the mouse to the games style
		OptionalImplementations.setMouseCursorToDefault();


		// assign inventory images to default
		BigInventoryImages = InternalAnimations.BigDefaultInventoryImages;
		MediumInventoryImages = InternalAnimations.MediumDefaultInventoryImages;
		SmallInventoryImages = InternalAnimations.SmallDefaultInventoryImages;

		// set client identification string
		JAMcore.Client = SpiffyFunctionsGWT.getUserAgent();

		// then we load the interface text (not currently set up for automatic language support)
		GamesText.loadGamesText();

		//then we load the main text database
		GameTextDatabase.loadDatabase("GameText.csv");


		// Set loading text
		MainStoryText.setText(GamesInterfaceText.MainGame_Loading);
		Window.setTitle(GamesInterfaceText.MainGame_WindowTitle_Loading);

		// Store screen settings
		Story_Text_Height  = RootPanel.get("bigtextbox").getOffsetHeight() - 2; 
		Story_Text_Width   = RootPanel.get("bigtextbox").getOffsetWidth()  - 2; 

		Game_Window_Width  = RequiredImplementations.getCurrentGameStageWidth();
		Game_Window_Height = RequiredImplementations.getCurrentGameStageHeight(); 
		
		
		
		PlayersNotepad.NotepadButton.setStyleName("IconPositionsNotebook"); //$NON-NLS-1$
		ControllPanelButton.setStyleName("IconPositionsControllPanel"); //$NON-NLS-1$

		// test login
		test_for_logged_in_user();

		// disable answere box button
		JAMcore.AnswerBox.get().setEnabled(false);
		//detect if we are loading for a existing save game
		if (History.getToken().length() > 2) {

			//The history token function should decode automatically
			//String  decoded = History.getToken();
			//But thanks to a bug in Firefox we need to do it this way;

			String decoded =  Window.Location.getHref().split("#",2)[1];			
			decoded = URL.decodeQueryString(decoded);

			//get 2.7 should fix this so we can just use getToken()


			//String decoded = URL.decodeComponent(History.getToken());
			GwtLog.info("decoding History token:"+decoded);
			//decoded= URL.decode(decoded);
			//String decoded =  URL.decodeQueryString(History.getToken());

			//detect if we are loading save game data straight into the game
			//this causes the start functions in the main game control not to run
			if (decoded.startsWith("LoadGameData")){
				GwtLog.info("Starting game with existing save data, therefor bypassing game start commands");
				JAMcore.StartFromURL = decoded;

			}
		}

	
		
		// First we load the main control script
		GwtLog.info("loading the main game control");
		JAMcore.startGameFromControllFile("Game Controll Script/Main_Game_Controll.txt",true);


		//advance loadingdiv, if theres one
		//advanceGameLoadingBar();
		if (OptionalImplementations.gamesLoadMonitor.isPresent()){		
			OptionalImplementations.gamesLoadMonitor.get().advanceGameLoadingBar();			
		}

		GwtLog.info("continueing to set up game...");

		// add history listener
		// This listens for changes in the URL after the # and then acts on them
		// This is mostly used as a way for HTML inner pages to send messages to the code
		// But it could also be used for auto-login events, or other start-of-game stats.

		History.addValueChangeHandler(JAM.this);

		// History.fireCurrentHistoryState();
		//we update the status if its NOT a save game command
		//if it is a saved game command this is delayed till the main game script is loaded
		if (History.getToken().length() > 2 & JAMcore.StartFromURL.length()<2) {
			String decoded = URL.decodeComponent(History.getToken());

			JAMcore.updateGameState(decoded);
		}
		History.newItem("", false);

		GwtLog.info("continueing to set up game.......");
		// Set Label style

/*		//set chapter panel style
		//(maybe move this into its gwt constructor?)
		GamesChaptersPanel.setStylePrimaryName("standard_message_back"); //$NON-NLS-1$
		// disable animation by default
		GamesChaptersPanel.setAnimationEnabled(false);

		Element bottompanel = (Element) GamesChaptersPanel.getElement().getFirstChild()
				.getChildNodes().getItem(1).getFirstChild().getFirstChild();
		
		//Log.info("adding overflow to bottom panel"
		//		+ bottompanel.getInnerHTML());
		bottompanel.getStyle().setProperty("overflow", "auto");
		bottompanel.getStyle().setProperty("overflow", "hidden");*/

		// .overflow-x:auto;

		JAMcore.AnswerBox.get().setEnabled(true);

		// while waiting for that we load the text boxs into the interface;

		// set chapter list

		ChapterList.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

		ChapterList.addStyleName("standard_message_back"); //$NON-NLS-1$

		//	Feedback.setWidth("100%"); //$NON-NLS-1$
		// Feedback.setHeight("18px");

		Feedback.getInternalWidget().setStyleName("FeedbackText"); //$NON-NLS-1$
		Feedback.getInternalWidget().getElement().setId("feedbacktext");

		// MainStoryText.setReadOnly(true);
		// MainStoryText.setStyleName("standard_message");
		//System.out.print("________height=" + StoryTabs.getOffsetHeight()); //$NON-NLS-1$

		// Story tabs
		/*GamesChaptersPanel.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

		GamesChaptersPanel.getDeckPanel().setSize("100%", Story_Text_Height + "px"); //$NON-NLS-1$ //$NON-NLS-2$
*/
		
		((GwtChapterControl)JAMcore.GamesChaptersPanel).setupMoreStyleing();
		
		//	GreyLog.info("adding resize handler");

		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				resizeEverything();

			}

		});

		ResizeOnLoad = new Timer() {
			@Override
			public void run() {
				// We check the size here, and fix if not correct. (for IE, as
				// IE dosnt work with LoadListener due to the cache)

				// resizeStoryBox();

				// set icon size
				// Game_Window_Width = Window.getClientWidth();
				// setInterfaceIconSize();
				// -------------

				GwtLog.info("resize after load triggered");
				resizeEverything();

			}

		};

		//GreyLog.info("adding resize on load timer");
		ResizeOnLoad.schedule(1000);

		/*
		GamesChaptersPanel.setStylePrimaryName("standard_message"); //$NON-NLS-1$
		 */
		
		
		
		// remove welcome from being active

		// CurrentLocationTabs
		// .removeTabListener(CurrentLocationTabs.OurTabListener);
		((GWTSceneAndPageSet)JAMcore.CurrentScenesAndPages).removeHandlers();

		JAMcore.currentOpenPages.clear();
		JAMcore.locationpuzzlesActive.clear();
/*
		GamesChaptersPanel.getTabBar().setVisible(false);
		*/
		JAMcore.GamesChaptersPanel.setTabBarVisible(false);
		
		//CurrentLocationTabs.setHeight("100%"); //$NON-NLS-1$
		((GWTSceneAndPageSet)JAMcore.CurrentScenesAndPages).visualContents.setWidth("100%"); 
		((GWTSceneAndPageSet)JAMcore.CurrentScenesAndPages).visualContents.setHeight("auto");

		MainStoryText.setSize("100%", "100%"); 

		
		
		// set inventory button style				
		DefaultInventorysButton.setStyleName("IconPositionsInventory"); 
		
		// add a global event preview to detect mouse and key press's
		// This is used to detect and act on game-global interface events.
		// eg. canceling holding an item if a right click
		// mouse button is pressed
		Event.addNativePreviewHandler(this);

		//--
		//----
		//Note; a lot of what follows, but not all, is cuypers specific stuff.
		//We will attempt to main compatibility, as it shouldnt be much bother and we might want re-releases of the CupersCode2 one day.
		//-----
		//--
		
		// add forum link
		ForumLinkButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				// loadforum
				Window.open("http://forum.res-nova.nl/fora/", "_blank", "");
			}
		});

		ForumLinkButton.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				ForumLinkButton.setPlayForward();
			}
		});
		ForumLinkButton.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				ForumLinkButton.setPlayBack();

			}

		});

		MessageForwardButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String mess = FeedbackHistoryVisualiser.getnextmessage();

				// remove the tags
				mess = mess.replaceAll("\\<.*?>", "").trim();

				Feedback.setTextNow(" " + mess);
			}

		});
		MessageForwardButton.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				MessageForwardButton.setPlayForward();
			}

		});
		MessageForwardButton.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				MessageForwardButton.setPlayBack();
			}

		});

		MessageBackButton.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {

				MessageBackButton.setPlayForward();
			}
		});
		MessageBackButton.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {

				MessageBackButton.setPlayBack();
			}

		});

		MessageBackButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String mess = FeedbackHistoryVisualiser.getprevmessage();

				// remove the tags
				mess = mess.replaceAll("\\<.*?>", "").trim();

				Feedback.setTextNow(" " + mess);

			}

		});

		// MessageHistory popup
		MessageHistoryButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (MessageHistoryOpen == false) {
					messagehistory.asWidget().setHeight("300px"); //$NON-NLS-1$
					messagehistory.asWidget().setWidth(((FeedbackContainer.getParent()
							.getOffsetWidth())) + "px"); //$NON-NLS-1$
					messagehistory.asWidget().setStyleName("FeedbackOpen"); //$NON-NLS-1$
					messagehistory.scrolltobottom();

					// loadpanel
					RootPanel.get()
					.add(messagehistory,
							DOM.getElementById("feedback")
							.getAbsoluteLeft(),
							DOM.getElementById("feedback")
							.getAbsoluteTop() - 300);
					MessageHistoryButton.setPlayForward();
					MessageHistoryOpen = true;

				} else {
					RootPanel.get().remove(messagehistory);
					MessageHistoryOpen = false;
					MessageHistoryButton.setPlayBack();
				}

			}
		});

		// Add Notepad

		/*//could move this button stuff to the notepad class itself
		PlayersNotepad.NotepadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// RootPanel.get().add(MyApplication.fadeback,0, 0);
				PlayersNotepadFrame.OpenDefault();

			}
		});
		PlayersNotepad.NotepadButton.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {

				PlayersNotepad.NotepadOpen = true;

				// NotepadButton.setUrl("GameIcons/Notebook_open.png");
				// toggle inventory open
				// NotepadOpen = true;
				// always open this when a panel is opened;
				closeallwindows.setPlayForward();
				// --

			}

		});

		PlayersNotepad.NotepadButton.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				PlayersNotepad.NotepadButton.setPlayForward();
			}
		});
		PlayersNotepad.NotepadButton.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (PlayersNotepad.NotepadOpen == false) {
					PlayersNotepad.NotepadButton.setPlayBack();
				}
			}
		});
*/
		ControllPanelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			//	GwtScreenMangement.openControlPanel();
				RequiredImplementations.screenManager.get().openControlPanel();
				

			}
		});

		ControllPanelButton.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				ControllPanelOpen = true;
			}

		});

		ControllPanelButton.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				ControllPanelButton.setPlayForward();
			}

		});

		ControllPanelButton.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (ControllPanelOpen == false) {
					ControllPanelButton.setPlayBack();
				}
			}

		});

		// invisible by default
		SecretsPopupPanelButton.setVisible(false);
		SecretsPopupPanelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SecretPanelFrame.OpenDefault();

				// always open this when a panel is opened;
				// closeallwindows.setAnimateOpen();
			}

		});
		SecretsPopupPanelButton.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {

				SecretsPanelOpen = true;

			}
		});

		SecretsPopupPanelButton.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (SecretsPanelOpen == false) {
					SecretsPopupPanelButton.setPlayBack();
				}
			}
		});
		SecretsPopupPanelButton.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				SecretsPopupPanelButton.setPlayLoop();
			}
		});

		// close all button
		closeallwindows.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				closeAllWindows();
			}

		});

		/*
		 * //setup Joker Panel Joker.addClickListener(new ClickListener(){
		 * 
		 * public void onClick(Widget sender) {
		 * 
		 * CluePanelFrame.OpenDefault();
		 * 
		 * }
		 * 
		 * });
		 */
		
		

		//ID titlecontainer triggers cuypers mode score placement
		if (Document.get().getElementById("titlecontainer") != null) {
			JAMcore.ScoreBoxVisible_CuypersMode = true;
			GwtLog.warning("The HTML contains a div with id 'titlecontainer'. This means we place the score and clues in a cuypers like arrangement ");
			
		} else {
			// we dont use cuypers style placement
			JAMcore.ScoreBoxVisible_CuypersMode = false;

		}
		//---------------------------
		
		
		if (JAMcore.ScoreBoxVisible_CuypersMode){
		ScoreAndJokerContainer.add((GwtScoreBoard)JAMcore.PlayersScore);
		ScoreAndJokerContainer.setCellVerticalAlignment((GwtScoreBoard)JAMcore.PlayersScore,
				HasVerticalAlignment.ALIGN_MIDDLE);
		
		// ScoreAndJokerContainer.add(Joker);
		// ScoreAndJokerContainer.setCellVerticalAlignment(Joker,
		// HasVerticalAlignment.ALIGN_MIDDLE);

		ScoreAndJokerContainer.setSize("100px", "28px");
		
		// ScoreAndJokerContainer.setStylePrimaryName("clueBox");
		// PlayersScore.SetScore(2500);
		ScoreAndClueBox.setHeight("40px");
		ScoreAndClueBox.add(ScoreAndJokerContainer);
		}
		// ScoreAndClueBox.setStylePrimaryName("ScoreBox");
		ClueReveal.setHeight("20px");
		ClueReveal.setAnimationEnabled(true);
		// ClueReveal.getHeaderTextAccessor().setText("Clues");
		// clear the header
		ClueReveal.setHeader(new Label(""));
		ClueReveal.setContent(((GwtClueBox)JAMcore.playersClues).asWidget());

		// ClueReveal.setStylePrimaryName("clueBox");
		ClueReveal.getHeader().getElement().getStyle().setProperty("color", "white");


		RootPanel.get().add(closeallwindows, RequiredImplementations.getCurrentGameStageWidth() - 55, 0);
		
		
		
		closeallwindows.getElement().getStyle().setZIndex(1805);				
		closeallwindows.getElement().setId("closeallwindowsbox");


		// decorations
		try {
			RootPanel.get("Statuefeet").add(StatueFeat); 
			RootPanel.get("clocklady").add(StatueHead);
			RootPanel.get("clocklady").add(gwt_clock, 35, 40);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		StatueFeat.setTitle("the game is a");


		gwt_clock.addClickListener(new ClickListener() {
			int clickcount = 0;

			@Override
			public void onClick(Widget sender) {
				gwt_clock.SetModeRandom(3000);
				clickcount++;
				System.out.print(clickcount);

				if (clickcount > 5) {

					// add score
					JAMcore.processInstructions(
							" \n - AddScore = 5 \n ", "ClockClickedALot", null); 

					RootPanel.get().add(clockpopup,
							RootPanel.get("clocklady").getAbsoluteLeft() + 35,
							RootPanel.get("clocklady").getAbsoluteTop() - 30);
					// put it on top
					DOM.setIntStyleAttribute(clockpopup.getElement(), "zIndex",
							5000);

					// timer to make it vanish
					Timer vanishimage = new Timer() {

						@Override
						public void run() {
							clockpopup.removeFromParent();
						}

					};
					vanishimage.schedule(3000);
				} else {
					JAMcore.processInstructions(
							" \n - AddScore = 1 \n ", "Clock" + clickcount, null); //$NON-NLS-1$

				}

			}

		});

		// gwt_clock.setRadius((int)(StatueHead.getOffsetWidth()/2.2));

		//setupTitleBar();

		// we try each icon out separately, because some are optional for some
		// games

			RequiredImplementations.PositionByTag(DefaultInventorysButton, "Inventory");			
			DefaultInventorysButton.setWidth("100%"); 
			

			//No longer needed; 
		//	RequiredImplementations.PositionByTag(heldItemBox, "itempicked");	
			// it will position itself when its set to display a item as held
			//(this is because its position might change based on what inventory the item came from 
			

		GwtLog.info("adding other interface elments");

		//Position by tag used instead of RootPanel.get (which itself will check for element presence so the try/catch isn't needed)
		RequiredImplementations.PositionByTag(solider, "soldier");				
		solider.setStyleName("solider");
		solider.getElement().getStyle().setZIndex(0);
	
		RequiredImplementations.PositionByTag(PlayersNotepad.NotepadButton, "Notepad");				
		PlayersNotepad.NotepadButton.setWidth("100%"); //$NON-NLS-1$
		
		RequiredImplementations.PositionByTag(ControllPanelButton, "ControllPanel");
		ControllPanelButton.setWidth("100%"); 			
		
		RequiredImplementations.PositionByTag(ChapterList, "chapterlist");
	//	RootPanel.get("musicControll").add(musicPlayer); 
	
		//TODO: place scorebox by tag too?
		//(if not on cupersmode?)
		
		
		
		
		try {
		//	RootPanel.get("soldier").add(solider); 
			//DOM.setStyleAttribute(solider.getElement(), "zIndex", "0");
			
		//	RootPanel.get("Notepad").add(PlayersNotepad.NotepadButton);
			
			RequiredImplementations.PositionByTag(musicPlayer, "musicControll");
			musicPlayer.setSize("90%", "100%");

			// reposition music box to float above the rest			
			int MpSizeX = DOM.getElementById("musicControll").getOffsetWidth() - 60;
			int MpSizeY = musicPlayer.CurrentMusicTrackLabel.getOffsetHeight();
			int MpX = DOM.getElementById("musicControll").getAbsoluteLeft() + 20;
			int MpY = DOM.getElementById("musicControll").getAbsoluteTop()
					+ DOM.getElementById("musicControll").getOffsetHeight();

			musicPlayer.CurrentMusicTrackLabel.setPixelSize(MpSizeX, MpSizeY);
			musicPlayer.CurrentMusicTrackLabel.ListContainer.setWidth(MpSizeX + "px");
			musicPlayer.CurrentMusicTrackLabel.getElement().getStyle().setProperty("zIndex", "500");
			
			int musicControlWidth = DOM.getElementById("musicControll").getOffsetWidth();
			
			//RootPanel.get().add(musicPlayer.CurrentMusicTrackLabel, MpX,MpY - 23);
			RequiredImplementations.PositionByCoOrdinates(musicPlayer.CurrentMusicTrackLabel, MpX,MpY - 23,0);
			
			musicPlayer.setSize(musicControlWidth + "px", "100%");


			RequiredImplementations.PositionByTag(SecretsPopupPanelButton, "Secretlink");
			RequiredImplementations.PositionByTag(ForumLinkButton, "ForumLink");
			
			
		} catch (Exception e) {
			GwtLog.info("elements missing from page, so not added");
		}

		RequiredImplementations.PositionByTag(((GwtChapterControl)JAMcore.GamesChaptersPanel).asWidget(), "bigtextbox");
		
		
		ClueReveal.getElement().getStyle().setZIndex(1000);
		
		
		
		JAMcore.playersClues.setSize(
				(DOM.getElementById("bigtextbox").getOffsetWidth() - 14)  + "px", 
				(DOM.getElementById("bigtextbox").getOffsetHeight() - 21) + "px"
				);

		// RootPanel.get("answerbox").add(AnswerBox);
		// RootPanel.get("answerbox").add(EnterAns);

		// we have to do this to get around the text going off the bottom of the
		// frame (in FF and Chrome)

		//FeedbackContainer.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$
		FeedbackContainer.setWidth("100%");
		FeedbackContainer.setStylePrimaryName("Feedback");
		FeedbackContainer.getElement().setId("feedbackinner");


		ScrollPanel FeedbackScroller = new ScrollPanel();
		FeedbackContainer.add(FeedbackScroller);

		// FeedbackScroller.setSize("100%", "70px"); //used to be 70px;

		FeedbackScroller.getElement().setId("feedbackscroller");
		FeedbackScroller.setStyleName("FeedbackScrollerStyle"); // used for
		// height
		// setting
		// (default
		// 70px!)


		FeedbackScroller.getElement().getStyle().setPaddingTop(3, Unit.PX);

		// FeedbackScroller.add(new Label
		// ("This is a test label for testing \n the feedback overflow \n settings for when the text is too big. Scrollbars should appearonly when needed, and the box should keep a steady size at all times. \n sorry for the inconvience this has caused, normal messages will return shortly......................................................... - DF "));

		VerticalPanel FeedbackToBottom = new VerticalPanel();
		FeedbackToBottom.setSize("100%", "100%");
		FeedbackToBottom.add(Feedback.getInternalWidget());
		FeedbackToBottom.setCellVerticalAlignment(Feedback.getInternalWidget(),
				HasVerticalAlignment.ALIGN_BOTTOM);

		FeedbackScroller.add(FeedbackToBottom);

		FeedbackContainer.setCellVerticalAlignment(Feedback.getInternalWidget(),
				HasVerticalAlignment.ALIGN_BOTTOM);

		GwtLog.info("adding feedback container");

		RootPanel.get("feedback").add(FeedbackContainer); //$NON-NLS-1$
		//	GreyLog.info("added feedback container:"
		//		+ FeedbackContainer.getOffsetHeight());

		// feedback history flip thingy

		MessageForwardButton.setSize("19px", "19px");
		MessageForwardButton.getElement().addClassName("logarrowforward");
		MessageForwardButton.getElement().setId("logarrowforward");

		Style MHBStyle2 = MessageForwardButton.getElement().getStyle();
		MHBStyle2.setProperty("zIndex", "250"); //$NON-NLS-1$ //$NON-NLS-2$

		MessageBackButton.setSize("19px", "19px");
		MessageBackButton.getElement().addClassName("logarrowback");
		MessageBackButton.getElement().setId("logarrowback");

		Style MHBStyle3 = MessageBackButton.getElement().getStyle();
		MHBStyle3.setProperty("zIndex", "250"); //$NON-NLS-1$ //$NON-NLS-2$

		MessageHistoryButton.setSize("16px", "16px"); //$NON-NLS-1$ //$NON-NLS-2$
		MessageHistoryButton.getElement().addClassName("logarrowhistory");
		MessageHistoryButton.getElement().setId("logarrowhistory");

		Style MHBStyle = MessageHistoryButton.getElement().getStyle();
		MHBStyle.setProperty("zIndex", "250"); //$NON-NLS-1$ //$NON-NLS-2$

		GwtLog.info("adding MessageHistoryButton at "+ DOM.getElementById("feedback").getAbsoluteTop());
		
		
		positionHistoryButtons();

		// AnsBar.setWidth("100%");
		AnsBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		AnsBar.setHeight("100%"); 		
		AnsBar.add((Widget) JAMcore.AnswerBox.get().getVisualRepresentation()); //we know its awidget as we are a gwt implementation of the jam
		
		// AnsBar.setBorderWidth(2);
		AnsBar.add((Widget) JAMcore.EnterAns.get().getVisualRepresentation());//we know its awidget as we are a gwt implementation of the jam
		

		//ansbox
		RequiredImplementations.PositionByTag(AnsBar, "ansbar");
		
		// titles, if used (cuypers only?
		if (RequiredImplementations.hasTag("titlecontainer")){
			setupTitleBar();
			RequiredImplementations.PositionByTag(TitleBarLeft, "titlecontainer");
		}
		
		// Things that have to be set after other things are added;
		RootPanel.get().add(ClueReveal, 
							JamImages.ScoreBack.getAbsoluteLeft(),
							JamImages.ScoreBack.getAbsoluteTop() 
						 +  JamImages.ScoreBack.getOffsetHeight() - 5);

		
		((GwtChapterControl)JAMcore.GamesChaptersPanel).setSize("100%", "100%"); //$NON-NLS-1$ 

		// StoryTabs.setWidth(""+(StoryTabs.getParent().getOffsetWidth()-16)+"px");
		// StoryTabs.setHeight((StoryTabs.getParent().getOffsetHeight()-15)+"px");

		//System.out
		//.print("get offset-" + GamesChaptersPanel.getParent().getOffsetWidth()); //$NON-NLS-1$	
		((GwtChapterControl)JAMcore.GamesChaptersPanel).getDeckPanel().setSize("100%", Story_Text_Height + "px"); //$NON-NLS-1$ //$NON-NLS-2$

		//advance loadingdiv, if theres one
		//advanceGameLoadingBar();
		if (OptionalImplementations.gamesLoadMonitor.isPresent()){		
			OptionalImplementations.gamesLoadMonitor.get().advanceGameLoadingBar();			
		}

	}

	public void setupTitleBar() {
		TitleBarLeft
		.getElement()
		.getStyle()
		.setProperty("backgroundImage",
				"url(../GameIcons/BIG/TitleStrip.jpg)");
		TitleBarLeft.setWidth("100%");
		TitleBarLeft.setHeight("55px");
		TitleBarLeft.setStylePrimaryName("TitleBarLeft");
		TitleBarLeft.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		// final Image LeftBit = new
		// Image("./GameTextures/TitleStrip_leftbit.jpg");

		JamImages.LeftBit.setHeight("55px");
		TitleBarLeft.add(JamImages.LeftBit);
		TitleBarLeft.setCellWidth(JamImages.LeftBit, "25px");

		OpenClueBox.setHeight("55px");

		TitleBarLeft.add(OpenClueBox);
		TitleBarLeft.setCellWidth(OpenClueBox, "24px");

		OpenClueBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClueReveal.setOpen(!ClueReveal.isOpen());
				if (!ClueReveal.isOpen()) {

					// OpenClueBox.setUrl("./GameTextures/TitleStrip_leftOpen.jpg");
					OpenClueBoxButton.applyTo(OpenClueBox);

					// open close all box
					if (JAMcore.popupPanelCurrentlyOpen.size() < 1) {
						if (CluePopUp.isAttached() == false) {
							closeallwindows.setPlayBack();
						}
					}

				} else {
					// OpenClueBox.setUrl("./GameTextures/TitleStrip_leftClose.png");
					CloseClueBoxButton.applyTo(OpenClueBox);

					closeallwindows.setPlayForward();
				}
			}

		});

		TitleBarLeft.add(JamImages.ScoreBack);
		JamImages.ScoreBack.setWidth("170px");
		JamImages.ScoreBack.setHeight("55px");
		TitleBarLeft.setCellWidth(JamImages.ScoreBack, "170px");
		TitleBarLeft
		.getElement()
		.getStyle()
		.setProperty("backgroundImage",
				"url(./GameIcons/BIG/TitleStrip.jpg)");

		BuyBack.setHeight("55px");
		TitleBarLeft.add(BuyBack);
		BuyBack.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				JAMcore.GameLogger.info("opening clue panel");

				// change button appearance
				// TitleStrip
				BuyCloseButton.applyTo(BuyBack);

				CluePanelFrame.OpenDefault();
				closeallwindows.setPlayForward();

			}

		});
		TitleBarLeft.setCellWidth(BuyBack, "26px");
		// spacer for the end

		//Image spacer = JargStaticImages.BIGTitleBackground().createImage();

		spacer =   new Image("./GameIcons/BIG/TitleStrip.jpg");
		spacer.setUrl("./GameIcons/BIG/TitleStrip.jpg");
		spacer.setWidth("100%");
		spacer.setHeight("55px");
		TitleBarLeft.add(spacer);
		
		//------
		//--------------
		//------

		try {
			RootPanel.get("ScoreBox").add(TitleBarRight);
		} catch (Exception e1) {
			GwtLog.info("no scorebox set");

		}

		// TitleBarRight.setStylePrimaryName("TitleBarRight");

		TitleBarRight.setWidth("100%");
		TitleBarRight.setHeight("56px");

		if (JAMcore.ScoreBoxVisible_CuypersMode) {
			RootPanel.get().add(
					ScoreAndClueBox,
					JamImages.ScoreBack.getAbsoluteLeft(),
					JamImages.ScoreBack.getAbsoluteTop() + (JamImages.ScoreBack.getHeight() / 2)
					- 15);
			DOM.setStyleAttribute(ScoreAndClueBox.getElement(), "zIndex", "110");
		}
		// Image TopStrip = new Image("./GameTextures/TitleStrip.jpg");

		// Image TopStrip = JargStaticImages.BIGTitleBackground().createImage();
		TopStripBigGap =   new Image("./GameIcons/BIG/TitleStrip.jpg");
		TopStripBigGap.setUrl("./GameIcons/BIG/TitleStrip.jpg");
		TopStripBigGap.setWidth("100%");
		TopStripBigGap.setHeight("55px");

		TitleBarRight.add(TopStripBigGap);
		TitleBarRight
		.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		// Image Cuyperstitle= new Image("./GameTextures/TitleStrip_right.png");

		// Cuyperstitle = new AnimatedIcon(BigCuypers2LogoImages);

		Cuyperstitle.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Cuyperstitle.setPlayLoop();

				// add score
				JAMcore.processInstructions(
						" \n - AddScore = 5 \n ", "SpunTheWheel", null); //$NON-NLS-1$

				Timer stopSpining = new Timer() {
					@Override
					public void run() {
						Cuyperstitle.setPlayBack();
					}
				};
				stopSpining.schedule(3000);
			}
		});

		// JargStaticImages.BIGTitleStripRight0().createImage();
		Cuyperstitle.setWidth("177px");
		Cuyperstitle.setHeight("55px");
		TitleBarRight.add(Cuyperstitle);
		TitleBarRight.setCellWidth(Cuyperstitle, "1px");

	}

	/**
	 * Sets the logging level
	 * @param selectedLogLevel
	 */
	public static void setLogLevel(Level selectedLogLevel) {
		GwtLog.setLevel(selectedLogLevel);
		JAMcore.Log.setLevel(selectedLogLevel);
		
		//and the spiffy drag panel
		Logger.getLogger("SpiffyGWT").setLevel(selectedLogLevel);		
		
		//and SpiffyCore stuff
		Logger.getLogger("SpiffyCore").setLevel(selectedLogLevel);
		
	}


	/** Determines if the games logs are shown or not **/
	//public static void setLoggingFilter(final String startsWith) {

	/*
		Log.setFilter(new Filter() {			
			@Override
			public boolean isLoggable(LogRecord record) {
				if (record.getMessage().startsWith(startsWith)){
					return true;
				}
				return false;
			}
		});*/ //GWT doesnt support this



	//}




	/** Determines if the games logs are shown or not **/
	public static void setLoggingEnable(boolean b) {

		JAMcore.setLoggingEnable(b); //turn the log on/off in the core

		//turn the log on/off for gwt specific classes

		if (b){

			GwtLog.info("setting log on");
			GwtLog.setLevel(Level.INFO);

			//manually turn some off here

			Logger.getLogger("JAM.SceneDataBox").setLevel(Level.SEVERE);
			
			ObjectInspector.Log.setLevel(Level.SEVERE);
			SpiffyObjectDataBox.Log.setLevel(Level.SEVERE);
			
			//Logger.getLogger("JAM.InstructionProcessor").setLevel(Level.OFF);

			//Disable some of the loggers for inherited projects and other class's we don't need to debug right not
			//Logger.getLogger("JAM").setLevel(Level.OFF);




			//Logger.getLogger("ParagraphCollection").setLevel(Level.OFF);

		//	Logger.getLogger("SpiffyGWT.SpiffyDragPanel").setLevel(Level.OFF);//disable drag panels log

			/*
			Logger.getLogger("SpiffyDragPanel").setLevel(Level.OFF);//disable drag panels log

			//internal loggers
			//com.darkflame.client.JargScene.PropertySet.Log.setLevel(Level.OFF);
			//com.darkflame.client.JargScene.SceneObjectData.Log.setLevel(Level.OFF);

			Logger.getLogger("PropertySet").setLevel(Level.OFF);//disable log for PropertySet class
			Logger.getLogger("LocationTabSet").setLevel(Level.OFF);//disable log for LocationTabSet class
			Logger.getLogger("MovementPath").setLevel(Level.OFF);//disable log for MovementPath class
			Logger.getLogger("ActionSet").setLevel(Level.OFF); //disable log for ActionSet class
			Logger.getLogger("ActionList").setLevel(Level.OFF); //disable log for ActionSet class

			Logger.getLogger("JAM.AnimatedIcon").setLevel(Level.OFF); //disable log for Animated Icon class
			Logger.getLogger("JAM.GameTextDatabase").setLevel(Level.OFF); //disable for the text database
			Logger.getLogger("JAM.TypedLabel").setLevel(Level.OFF);
			Logger.getLogger("JAM.SceneWidget").setLevel(Level.OFF);
			Logger.getLogger("ParagraphCollection").setLevel(Level.OFF);
			Logger.getLogger("JAM.SceneDialogObject").setLevel(Level.OFF);
			Logger.getLogger("JAM.SceneObject").setLevel(Level.OFF);
			Logger.getLogger("JAM.PopUpWithShadow").setLevel(Level.OFF);

			Logger.getLogger("JAM.SaveStateManager").setLevel(Level.OFF);	


			Logger.getLogger("SceneDivObjectData").setLevel(Level.OFF);
			Logger.getLogger("SceneObjectData").setLevel(Level.OFF);
			Logger.getLogger("SceneDialogObjectData").setLevel(Level.OFF);
			Logger.getLogger("SceneVectorObjectData").setLevel(Level.OFF);

			Logger.getLogger("SceneVectorObject").setLevel(Level.OFF);
			Logger.getLogger("JAM.ConditionalLine").setLevel(Level.OFF);

			Logger.getLogger("JAM.ChangePasswordBox").setLevel(Level.OFF);
			Logger.getLogger("JAM.GamesAnswerStore").setLevel(Level.OFF);
			Logger.getLogger("JAM.InstructionProcessor").setLevel(Level.OFF);

			Logger.getLogger("JamMain").setLevel(Level.OFF);

			Logger.getLogger("JAM.SceneWidget").setLevel(Level.OFF);

			Logger.getLogger("JAM.LoginBox").setLevel(Level.OFF);
			Logger.getLogger("Jam.AssArray").setLevel(Level.OFF);
			Logger.getLogger("JAM.TypedLabel").setLevel(Level.OFF);*////

			//disabled due to logspam;
			//Logger.getLogger("JAMCore.InstructionProcessor").setLevel(Level.SEVERE);
			Logger.getLogger("JAMCore.SceneObjectState").setLevel(Level.SEVERE);
			SceneDataBox.Log.setLevel(Level.SEVERE);
			GameDataBox.Log.setLevel(Level.SEVERE);
			
			//
			//add some logs to cores list

			JAMcore.ALL_LOGGERS.add(SceneObjectVisual.SOLog);
			JAMcore.ALL_LOGGERS.add(SceneDialogObject.Log);
			JAMcore.ALL_LOGGERS.add(SceneLabelObject.Log);
			JAMcore.ALL_LOGGERS.add(SceneDivObject.Log);
			JAMcore.ALL_LOGGERS.add(SceneVectorObject.Log);
			JAMcore.ALL_LOGGERS.add(SceneInputObject.Log);			
			JAMcore.ALL_LOGGERS.add(SceneSpriteObject.Log);
			
			JAMcore.ALL_LOGGERS.add(SpiffyDragPanel.Log);
			
			JAMcore.ALL_LOGGERS.add(SceneWidgetVisual.Log);
			JAMcore.ALL_LOGGERS.add(ItemDropController.Log);

			JAMcore.ALL_LOGGERS.add(InventoryItem.Log);

			JAMcore.ALL_LOGGERS.add(SpiffyAnimatedIcon.Log);
			JAMcore.ALL_LOGGERS.add(CollisionMapVisualiser.Log);
			//timer stuff
			JAMcore.ALL_LOGGERS.add(GWTJAMTimerController.Log);
			JAMcore.ALL_LOGGERS.add(JAMTimerController.Log);
			//--
			//
			JAMcore.ALL_LOGGERS.add(ObjectInspector.Log);
			JAMcore.ALL_LOGGERS.add(LoggerControl.Log);
			
			//sound
			JAMcore.ALL_LOGGERS.add(Track.Log);
						
			//
			//
		} else {

			GwtLog.info("setting log off");
			GwtLog.setLevel(Level.OFF);

		}


	}

	/** Advances the text loading in the div with id "loadingdiv" 
	 * This is only intended as a very fast pre-title loading bar.
	 * The majority of loading should be during the title screen, or shown with
	 * a proper scene loading widget (which displays automatically if a scene is shown while its still getting its data) */
	public void advanceGameLoadingBar() {


		GwtLog.info("advanceLoadingDiv");
		Element loadingElement = DOM.getElementById("loadingdiv");
		if (loadingElement!=null){
			currentLoadingText=currentLoadingText+".";
			loadingElement.setInnerText(currentLoadingText);
		}

		GwtLog.info("advanceLoadingDiv:"+currentLoadingText);
	}
	
	public void setGameLoadStatus(String url) {		
		currentLoadingText=url;		
	}

	private static void positionHistoryButtons() {
		if (!fixedSizeInterface) {
			RootPanel.get().add(
					MessageHistoryButton,
					DOM.getElementById("feedback").getAbsoluteLeft()
					+ FeedbackContainer.getOffsetWidth() - 20,
					DOM.getElementById("feedback").getAbsoluteTop());

			RootPanel.get().add(MessageForwardButton,
					MessageHistoryButton.getAbsoluteLeft() - 20,
					MessageHistoryButton.getAbsoluteTop());

			RootPanel.get().add(MessageBackButton,
					MessageHistoryButton.getAbsoluteLeft() - 40,
					MessageHistoryButton.getAbsoluteTop());

		} else {

			GwtLog.info("adding feedback to container");

			RootPanel.get("feedback").add(FeedbackContainer); //$NON-NLS-1$

			GwtLog.info("setting fixed history buttons positions");

			// we assume the page deals with positions

			try {
				RootPanel.get("historyButtonContainerID").add(
						MessageHistoryButton);
				RootPanel.get("historyButtonContainerID")
				.add(MessageBackButton);
				RootPanel.get("historyButtonContainerID").add(
						MessageForwardButton);
			} catch (Exception e) {
				GwtLog.info("no history IDs on page");

			}

		}
	}

	public static void setInterfaceIconSize() {

		GwtLog.info("setting interface size");

		GwtLog.info("setting interface Game_Window_Height = "+Game_Window_Height);

		JAMcore.iconsizestring = "BIG";

		if (JAMcore.InterfaceSize.compareTo(GamesInterfaceText.ControllPanel_Default) == 0) { 
			if (Game_Window_Width < 1050 || Game_Window_Height < 990) { //we used to not check the height at the moment these are both looking for the same sizes
				JAMcore.iconsizestring = "MEDIUM";                               //ideally we should balance this well for widescreen tablets etc
			}
			if (Game_Window_Width < 800 || Game_Window_Height < 800) {
				JAMcore.iconsizestring = "SMALL";
			}
		} else {
			if (JAMcore.InterfaceSize.compareTo(GamesInterfaceText.ControllPanel_Small) == 0) { 
				JAMcore.iconsizestring = "SMALL";
			}
			if (JAMcore.InterfaceSize.compareTo(GamesInterfaceText.ControllPanel_Medium) == 0) { 
				JAMcore.iconsizestring = "MEDIUM";
			}
			if (JAMcore.InterfaceSize.compareTo(GamesInterfaceText.ControllPanel_Big) == 0) { 
				JAMcore.iconsizestring = "BIG";
			}
		}

		GwtLog.info("setting interface size 2 :" + JAMcore.iconsizestring);

		// InventoryButton.setURL(
		// "GameIcons/" + iconsizestring + "/cc2rugzakje0.png", 6);

		// InventoryButton.setURL(
		// "GameIcons/" + iconsizestring +
		// "/"+InventoryButton.originalfilename+".png", 6);

		if (JAMcore.iconsizestring.equals("BIG")) {

			if (StatueHead.isOnBundleImageMode()) {
				StatueHead.setFrames(JamImages.LadyClockBig);
			} else {

				// Window.alert("GameIcons/" + iconsizestring + "/" +
				// StatueHead.originalfilename+".png set");

				StatueHead.setURL("GameIcons/" + JAMcore.iconsizestring + "/"
						+ StatueHead.originalfilename + ".png",
						StatueHead.getLastFrameNumber() + 1);

				// Window.alert("url set too"+StatueHead.getUrl());
			}
			StatueFeat.setFrames(JamImages.LadyFeatBig);

			if (solider.isOnBundleImageMode()) {
				solider.setFrames(JamImages.SoldierBig);
			} else {
				solider.setURL("GameIcons/" + JAMcore.iconsizestring + "/"
						+ solider.originalfilename + ".png",
						solider.getLastFrameNumber() + 1);

			}
			// topbar (not always used)

			if (RequiredImplementations.hasTag("titlecontainer")){
			
			JamImages.BigLeftBit.applyTo(JamImages.LeftBit);
			JamImages.BigBuyOpenButton.applyTo(BuyBack);
			JamImages.BigScoreBack.applyTo(JamImages.ScoreBack);
			JamImages.BigOpenClueBox.applyTo(OpenClueBox);

			JamImages.LeftBit.setHeight("55px");
			BuyBack.setHeight("55px");
			OpenClueBox.setHeight("55px");
			JamImages.ScoreBack.setHeight("55px");
			JamImages.ScoreBack.setWidth("170px");
			TitleBarRight.setHeight("56px");

			// if theres a scorebox, set it.
			if (DOM.getElementById("ScoreBox") != null) {
				DOM.getElementById("ScoreBox").getStyle()
				.setProperty("height", "55px");
			}

			BuyCloseButton = JamImages.BigBuyCloseButton;
			BuyOpenButton = JamImages.BigBuyOpenButton;
			OpenClueBoxButton = JamImages.BigOpenClueBox;
			CloseClueBoxButton = JamImages.BigCloseClueBox;

			TitleBarLeft.setHeight("55px");
			TitleBarLeft
			.getElement()
			.getStyle()
			.setProperty("backgroundImage",
					"url(./GameIcons/BIG/TitleStrip.jpg)");

			TopStripBigGap.setUrl("./GameIcons/BIG/TitleStrip.jpg");
			TopStripBigGap.setWidth("100%");
			TopStripBigGap.setHeight("55px");
			//spacer =   new Image("./GameIcons/BIG/TitleStrip.jpg");
			spacer.setUrl("./GameIcons/BIG/TitleStrip.jpg");
			spacer.setHeight("55px");

			// title bar set
			Cuyperstitle.setFrames(JamImages.BigCuypers2LogoImages);
			
			}
			
			DefaultInventorysButton.setFrames(BigInventoryImages);

			PlayersNotepad.NotepadButton.setFrames(InternalAnimations.BigNotepadImages);
			ControllPanelButton.setFrames(InternalAnimations.BigControllPanelImages);
			ForumLinkButton.setFrames(InternalAnimations.BigForumImages);
			SecretsPopupPanelButton.setFrames(InternalAnimations.BigSecretsImages);

		} else if (JAMcore.iconsizestring.equals("MEDIUM")) {

			if (StatueHead.isOnBundleImageMode()) {
				StatueHead.setFrames(JamImages.LadyClockMid);
			} else {

				StatueHead.setURL("GameIcons/" + JAMcore.iconsizestring + "/"
						+ StatueHead.originalfilename + ".png",
						StatueHead.getLastFrameNumber() + 1);

			}
			StatueFeat.setFrames(JamImages.LadyFeatMid);

			if (solider.isOnBundleImageMode()) {
				solider.setFrames(JamImages.SoldierMid);
			} else {
				solider.setURL("GameIcons/" + JAMcore.iconsizestring + "/"
						+ solider.originalfilename + ".png",
						solider.getLastFrameNumber() + 1);

			}
			// topbar
			JamImages.MediumLeftBit.applyTo(JamImages.LeftBit);
			JamImages.MediumBuyOpenButton.applyTo(BuyBack);
			JamImages.MediumOpenClueBox.applyTo(OpenClueBox);

			JamImages.LeftBit.setHeight("47px");
			BuyBack.setHeight("47px");
			OpenClueBox.setHeight("47px");
			JamImages.ScoreBack.setHeight("47px");
			JamImages.ScoreBack.setWidth("140px");
			TitleBarRight.setHeight("47px");
			// if theres a scorebox, set it.
			if (DOM.getElementById("ScoreBox") != null) {
				DOM.getElementById("ScoreBox").getStyle()
				.setProperty("height", "47px");
				DOM.getElementById("ScoreBox").setAttribute("Style",
						"height:47px");
			}
			BuyCloseButton = JamImages.MediumBuyCloseButton;
			BuyOpenButton = JamImages.MediumBuyOpenButton;
			OpenClueBoxButton = JamImages.MediumOpenClueBox;
			CloseClueBoxButton = JamImages.MediumCloseClueBox;

			JamImages.MediumScoreBack.applyTo(JamImages.ScoreBack);

			TopStripBigGap.setUrl("./GameIcons/MEDIUM/TitleStrip.jpg");
			TopStripBigGap.setWidth("100%");
			TopStripBigGap.setHeight("47px");
			spacer.setUrl("./GameIcons/MEDIUM/TitleStrip.jpg");
			spacer.setHeight("47px");
			TitleBarLeft.setHeight("47px");
			TitleBarLeft
			.getElement()
			.getStyle()
			.setProperty("backgroundImage",
					"url(./GameIcons/MEDIUM/TitleStrip.jpg)");

			// title bar set
			Cuyperstitle.setFrames(JamImages.MediumCuypers2LogoImages);

			DefaultInventorysButton.setFrames(MediumInventoryImages);
			PlayersNotepad.NotepadButton.setFrames(InternalAnimations.MediumNotepadImages);
			ControllPanelButton.setFrames(InternalAnimations.MediumControllPanelImages);
			ForumLinkButton.setFrames(InternalAnimations.MediumForumImages);
			SecretsPopupPanelButton.setFrames(InternalAnimations.MediumSecretsImages);

		} else if (JAMcore.iconsizestring.equals("SMALL")) {

			if (StatueHead.isOnBundleImageMode()) {
				StatueHead.setFrames(JamImages.LadyClockSmall);
			} else {

				// Window.alert("GameIcons/" + iconsizestring + "/" +
				// StatueHead.originalfilename+".png set");

				StatueHead.setURL("GameIcons/" + JAMcore.iconsizestring + "/"
						+ StatueHead.originalfilename + ".png",
						StatueHead.getLastFrameNumber() + 1);

				// Window.alert("url set too"+StatueHead.getUrl());
			}

			StatueFeat.setFrames(JamImages.LadyFeatSmall);

			if (solider.isOnBundleImageMode()) {
				solider.setFrames(JamImages.SoldierSmall);
			} else {
				solider.setURL("GameIcons/" + JAMcore.iconsizestring + "/"
						+ solider.originalfilename + ".png",
						solider.getLastFrameNumber() + 1);

			}
			// topbar
			JamImages.SmallLeftBit.applyTo(JamImages.LeftBit);
			JamImages.SmallBuyOpenButton.applyTo(BuyBack);
			JamImages.SmallScoreBack.applyTo(JamImages.ScoreBack);
			JamImages.SmallOpenClueBox.applyTo(OpenClueBox);
			BuyCloseButton = JamImages.SmallBuyCloseButton;
			BuyOpenButton = JamImages.SmallBuyOpenButton;
			OpenClueBoxButton = JamImages.SmallOpenClueBox;
			CloseClueBoxButton = JamImages.SmallCloseClueBox;

			JamImages.LeftBit.setHeight("40px");
			BuyBack.setHeight("40px");
			OpenClueBox.setHeight("40px");
			JamImages.ScoreBack.setHeight("40px");
			TitleBarRight.setHeight("40px");
			JamImages.ScoreBack.setWidth("120px");

			// if theres a scorebox, set it.
			if (DOM.getElementById("ScoreBox") != null) {
				DOM.getElementById("ScoreBox").getStyle()
				.setProperty("height", "40px");
			}

			TopStripBigGap.setUrl("./GameIcons/SMALL/TitleStrip.jpg");
			spacer.setUrl("./GameIcons/SMALL/TitleStrip.jpg");
			TopStripBigGap.setWidth("100%");
			TopStripBigGap.setHeight("40px");
			spacer.setHeight("40px");
			TitleBarLeft.setHeight("40px");
			TitleBarLeft
			.getElement()
			.getStyle()
			.setProperty("backgroundImage",
					"url(./GameIcons/SMALL/TitleStrip.jpg)");

			// title bar set
			Cuyperstitle.setFrames(JamImages.SmallCuypers2LogoImages);

			DefaultInventorysButton.setFrames(SmallInventoryImages);
			PlayersNotepad.NotepadButton.setFrames(InternalAnimations.SmallNotepadImages);
			ControllPanelButton.setFrames(InternalAnimations.SmallControllPanelImages);
			ForumLinkButton.setFrames(InternalAnimations.SmallForumImages);
			SecretsPopupPanelButton.setFrames(InternalAnimations.SmallSecretsImages);

		} else {

			if (StatueHead.isOnBundleImageMode()) {
				StatueHead.setFrames(JamImages.LadyClockBig);
			} else {

				// Window.alert("GameIcons/" + iconsizestring + "/" +
				// StatueHead.originalfilename+".png set");

				StatueHead.setURL("GameIcons/" + JAMcore.iconsizestring + "/"
						+ StatueHead.originalfilename + ".png",
						StatueHead.getLastFrameNumber() + 1);

				// Window.alert("url set too"+StatueHead.getUrl());
			}

			StatueFeat.setFrames(JamImages.LadyFeatBig);

			if (solider.isOnBundleImageMode()) {
				solider.setFrames(JamImages.SoldierBig);
			} else {
				solider.setURL("GameIcons/" + JAMcore.iconsizestring + "/"
						+ solider.originalfilename + ".png",
						solider.getLastFrameNumber() + 1);

			}
			// topbar
			JamImages.BigLeftBit.applyTo(JamImages.LeftBit);
			JamImages.BigBuyOpenButton.applyTo(BuyBack);
			JamImages.BigScoreBack.applyTo(JamImages.ScoreBack);
			JamImages.BigOpenClueBox.applyTo(OpenClueBox);

			JamImages.LeftBit.setHeight("55px");
			BuyBack.setHeight("55px");
			OpenClueBox.setHeight("55px");
			JamImages.ScoreBack.setHeight("55px");
			JamImages.ScoreBack.setWidth("170px");
			// if theres a scorebox, set it.
			if (DOM.getElementById("ScoreBox") != null) {
				DOM.getElementById("ScoreBox").getStyle()
				.setProperty("height", "55px");
			}
			BuyCloseButton = JamImages.BigBuyCloseButton;
			BuyOpenButton = JamImages.BigBuyOpenButton;
			OpenClueBoxButton = JamImages.BigOpenClueBox;
			CloseClueBoxButton = JamImages.BigCloseClueBox;

			TitleBarLeft.setHeight("55px");
			TitleBarLeft
			.getElement()
			.getStyle()
			.setProperty("backgroundImage",
					"url(./GameIcons/BIG/TitleStrip.jpg)");

			TopStripBigGap.setUrl("./GameIcons/BIG/TitleStrip.jpg");
			TopStripBigGap.setWidth("100%");
			TopStripBigGap.setHeight("55px");
			spacer.setUrl("./GameIcons/BIG/TitleStrip.jpg");
			spacer.setHeight("55px");

			// title bar set
			Cuyperstitle.setFrames(JamImages.BigCuypers2LogoImages);

			DefaultInventorysButton.setFrames(BigInventoryImages);

			PlayersNotepad.NotepadButton.setFrames(InternalAnimations.BigNotepadImages);
			ControllPanelButton.setFrames(InternalAnimations.BigControllPanelImages);
			ForumLinkButton.setFrames(InternalAnimations.BigForumImages);
			SecretsPopupPanelButton.setFrames(InternalAnimations.BigSecretsImages);
		}

		GwtLog.info("setting interface size 3 :");

		// NotepadButton.setURL(
		//		"GameIcons/" + iconsizestring + "/cc2notebook0.png", 4); //$NON-NLS-1$ //$NON-NLS-2$
		// ControllPanelButton.setURL(
		//			"GameIcons/" + iconsizestring + "/CCtype0.png", 4); //$NON-NLS-1$ //$NON-NLS-2$
		// ForumLinkButton.setURL(
		//			"GameIcons/" + iconsizestring + "/CC2forum0.png", 4); //$NON-NLS-1$ //$NON-NLS-2$
		// SecretsPopupPanelButton.setURL(
		//			"GameIcons/" + iconsizestring + "/easteregg0.png", 5); //$NON-NLS-1$ //$NON-NLS-2$
		// StatueHead.setURL(
		//		"GameIcons/" + iconsizestring + "/CCladyclock0.png", 3); //$NON-NLS-1$ //$NON-NLS-2$
		// StatueFeat
		// .setURL(
		//					"GameIcons/" + iconsizestring + "/" + StatueFeat.originalfilename + ".png", 1); //$NON-NLS-1$ //$NON-NLS-2$
		//	solider.setURL("GameIcons/" + iconsizestring + "/CCsoldier0.png", 5); //$NON-NLS-1$ //$NON-NLS-2$
		// fix for IE because ie is dumb
		StatueHead.getElement().getStyle().setProperty("zIndex", "50"); //$NON-NLS-1$ //$NON-NLS-2$

		// set screen size

		if (JAMcore.iconsizestring.compareTo("SMALL") == 0) {
			ControllPanel.changecss("smallscreen"); //$NON-NLS-1$
			// Window.alert("screen size set a");
		}
		if (JAMcore.iconsizestring.compareTo("MEDIUM") == 0) {
			ControllPanel.changecss("midscreen"); //$NON-NLS-1$
			// Window.alert("screen size set b ");
		}
		if (JAMcore.iconsizestring.compareTo("BIG") == 0) {
			ControllPanel.changecss("default"); //$NON-NLS-1$
			// Window.alert("screen size set c");
		}

		// Window.alert("screen size set 1 "+iconsizestring);

	}

	//not sure if this is still used, as general file handling can now be done locally.
	//(that is, bi-passing the php)
	/*
	public void startgamefromlocalcontrollfile(final String fileurl) {
		
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				fileurl);

		try {
			requestBuilder.sendRequest("", new RequestCallback() { //$NON-NLS-1$
				@Override
				public void onError(Request request, Throwable exception) {
					System.out.println("http failed"); //$NON-NLS-1$
				}

				@Override
				public void onResponseReceived(Request request,
						Response response) {

					// catch error
					if (response.getText().length() < 10) {
						System.out.println("controll file not recieved;" + response.getText()); 
						MainStoryText.setText("controll file not recieved;" + response.getText()); 
						return;
					}

					JAMcore.controllscript = response.getText();

					JAMcore.controllscript=JAMcore.parseForLanguageSpecificExtension(JAMcore.controllscript);
					JAMcore.controllscript=JAMcore.parseForTextIDs(JAMcore.controllscript);

					// crop till start;
					// "Game Controll Starts Here:"

					int StartIndex = JAMcore.controllscript
							.indexOf("Game Controll Starts Here:"); 
					JAMcore.controllscript = JAMcore.controllscript.substring(
							StartIndex, JAMcore.controllscript.length());

					// swap custom words
					JAMcore.controllscript = JAMcore.SwapCustomWords(JAMcore.controllscript,false); //false as we don't want to burn in dynamic values

					Log.info("setting up answers");

					// set up answers
					gamesAnswerStore = new GamesAnswerStore(
							JAMcore.controllscript);

					// once loaded we start the main game loop
					maingameloop();
				}
			});
		} catch (RequestException ex) {
			System.out.println("can not connect to game controll file"); //$NON-NLS-1$
		}

	}*/

	


	
	
	
		
//	/** This will trigger the games script once its loaded
//	 *  (Determined by its length being greater then 5)
//	 *  - This probably should be tested manually rather then repeated like this
//	 *   **/
//	private static void start_game_script_when_ready_old() {
//		
//	
//
//		
//		
//		JAMcore.StartOfScriptProcessed = true; //should be true only when all the prerequisites are loaded
//
//		GwtLog.info("Start_of_game_script triggered");
//
//	///	currentLoadingText = "start of game script triggered";
//			
//		if (OptionalImplementations.gamesLoadMonitor.isPresent())
//		{				
//			OptionalImplementations.gamesLoadMonitor.get().setGameLoadStatus("start of game script triggered");
//			OptionalImplementations.gamesLoadMonitor.get().advanceGameLoadingBar();			
//		}
//		
//
//
//		// wait to make sure controll script is loaded
//		// Set loading text
//		MainStoryText.setText(GamesInterfaceText.MainGame_StoryText_Loading);
//		//Feedback.setText(GamesInterfaceText.MainGame_LoadingNewGame);
//		RequiredImplementations.interfaceVisualElements.get().setCurrentFeedbackText(GamesInterfaceText.MainGame_LoadingNewGame);
//		
//		Timer temptimer = new Timer() {
//
//			@Override
//			public void run() {
//				
//				if (OptionalImplementations.gamesLoadMonitor.isPresent()){					
//					OptionalImplementations.gamesLoadMonitor.get().advanceGameLoadingBar();					
//				}
//
//				//TODO: we need to scrap this dumb timer method and instead make this
//				//all into a proper "start_game_script_if_ready" function, which checks the prerequisites
//				//and if they are not all there does nothing.
//				//Meanwhile each prerequisite fires this statement on completion
//				
//				//check game prerequisites before continuing
//				if (JAMcore.controllscript.length() > 5 && 
//					GameTextDatabase.isLoaded()) {
//
//					this.cancel();
//
//					// Set loading text
//					
//					MainStoryText.setText(GamesInterfaceText.MainGame_StoryText_Loading + "...");
//					
//					//Feedback.setText(GamesInterfaceText.MainGame_LoadingNewGame
//					//		+ "...",true); //the true disables the sound effect);
//					
//					RequiredImplementations.interfaceVisualElements.get().setCurrentFeedbackText(GamesInterfaceText.MainGame_LoadingNewGame + "...",true);
//					
//					
//					// grab start commands
//					int FirstBitsStart = JAMcore.controllscript.indexOf("Start:", 0) + 6; //$NON-NLS-1$
//					int FirstBitsEnd   = JAMcore.controllscript.indexOf("ans=", 0); //$NON-NLS-1$
//					// System.out.print("/n ans next pos="+FirstBitsStart);
//					//JAMcore.GameLogger.info("ans next pos=" + FirstBitsStart); //$NON-NLS-1$
//					// Isolate instructions to process
//					final String instructionset = JAMcore.controllscript.substring(FirstBitsStart, FirstBitsEnd);
//
//					GwtLog.info("Startup instruction set : " + instructionset + " \n -instruction set- \n"); //$NON-NLS-1$ //$NON-NLS-2$
//					JAMcore.GameLogger.log("Startup instruction set : ","green"); //$NON-NLS-1$ //$NON-NLS-2$
//					JAMcore.GameLogger.info(instructionset); 
//					
//					// if theres no preloaded instructions, then we process the
//					// first lot
//
//					if (StartFromURL.length()<2) {
//						// Window.alert("processing starting instructions"+instructionset);
//						JAMcore.processInstructions(instructionset, "Start", null);
//
//					} else {
//						//we run the start from url data rather then the start of game script 
//						JAMcore.updateGameState(StartFromURL);
//						//clear the variable to ensure it cant be run again
//						StartFromURL="";
//					}
//
//					// Window.alert("triggering main game loop");
//					JAMcore.maingameloop();
//				} else {
//					GwtLog.info("waiting for controllscript to load. controllscript length ="+JAMcore.controllscript.length());
//				}
//			}
//
//		};
//
//		// Window.alert(" setting timer for starting controll script ");
//		temptimer.scheduleRepeating(30);
//		// add history triggers
//
//	}

	/** A special game function to send email's to the player. 
	 * This relays on both the textFetcher php (to get the email templates)
	 * and "emailer.php" which handles the email's.
	 * Used in the CupersCode2 **/

	public static void sendEmailTemplate(String currentProperty) {

		// First we grab the email's contents
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,
				textfetcher_url);

		try {
			requestBuilder
			.sendRequest(
					"FileURL=Game%20Emails/" + currentProperty, new RequestCallback() { //$NON-NLS-1$

						@Override
						public void onError(Request request,
								Throwable exception) {

						}

						@Override
						public void onResponseReceived(Request request,
								Response response) {

							// we have the email
							// we now divide up the lines
							String emailtext = response.getText();

							String Title = emailtext.substring(
									emailtext.indexOf("Subject:") + 8, emailtext.indexOf("\n", emailtext.indexOf("Subject:") + 8)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							String From = emailtext.substring(
									emailtext.indexOf("From:") + 5, emailtext.indexOf("\n", emailtext.indexOf("From:") + 5)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							String Content = emailtext
									.substring(emailtext
											.indexOf("Message:") + 8); //$NON-NLS-1$


							sendEmail(null,Title, From, Content,false);

						}
					});
		} catch (RequestException ex) {
			// blah blah
			// Feedback.setText("email data missing");

		}

	}

//TODO: shouldn't this be in GWTSceneAndPageSet itself?
	/*
	static public void removePage(final String RemoveLocation) {

		int TabCount = JAMcore.CurrentScenesAndPages.getPageCount();
		int cindex = 0;
		Log.info("removeing-" + RemoveLocation); //$NON-NLS-1$

		for (Iterator<String> it = JAMcore.CurrentScenesAndPages.OpenPagesInSet
				.iterator(); it.hasNext();) {

			String currentItem = it.next();

			if (currentItem.compareTo(RemoveLocation) == 0) {
				JAMcore.CurrentScenesAndPages.OpenPagesInSet.remove(currentItem);
			}

		}

		// remove tab
		while (cindex < TabCount) {
			if (JAMcore.currentOpenPages.get(cindex).indexOf(RemoveLocation) > -1) {

				// we remove it from the tabs
				((GWTSceneAndPageSet)JAMcore.CurrentScenesAndPages).visualContents.remove(cindex);

				JAMcore.currentOpenPages.remove(cindex);
				// set chapter name to match
				JAMcore.locationpuzzlesActive.remove(cindex);

				return;
			}
			cindex++;
		}

	}*/

	static public void runPagesCommands(HTML storyText, String NewMessageURL) {
		JAMcore.storyPageLoading = true;
		// need to divide this into two sections; "run once" and "run everytime"

		// find "run once" commands and process ---------

		// get start of commands;
		// these commands are in the format;
		// <!-- run:Message="blahblah";AddItem=blah; -->

		String Text = storyText.getHTML();
		GwtLog.info("checking for html commands");

		int startfrom = 0;

		// loop for all runonce commands
		while (Text.indexOf("runonce:", startfrom) > -1) {

			int Start = Text.indexOf("runonce:", startfrom) + 8;
			int End = Text.indexOf("-->", Start);

			String commands = "";
			if (Start > 8) {
				commands = " - "
						+ Text.substring(Start, End).replaceAll(";", "\n - ");
				if (commands.endsWith(" - ")) {
					commands = commands.substring(0, commands.length() - 3);
				}

				GwtLog.info("run once commands =" + commands + "." + Start
						+ "|" + End);

				JAMcore.processInstructionsWhenReady(commands, "fromStoryText"
						+ NewMessageURL);

			}

			startfrom = End + 2;

		}
		// find "run everytime" commands store and process ---------

		startfrom = 0;

		String runcommands = "";

		GwtLog.info("looping for all run commands on page");
		// loop for all runcommands
		while (Text.indexOf("run:", startfrom) > -1) {

			int Start = Text.indexOf("run:", startfrom) + 4;
			int End = Text.indexOf("-->", Start);

			if (Start > 4) {
				String newcommands = " - "
						+ Text.substring(Start, End).replaceAll(";", "\n - ");
				if (newcommands.endsWith(" - ")) {
					newcommands = newcommands.substring(0,
							newcommands.length() - 3);
				}
				GwtLog.info("found command, adding it to list");
				runcommands = runcommands + "\n" + newcommands;
			}
			startfrom = End + 2;
		}

		if (runcommands.length() > 1) {

			// Log.info("run everytime commands ="+commands+"."+Start+"|"+End);
			GwtLog.info("storing commands in ~"
					+ NewMessageURL.split("\\.")[0] + "~");
			GwtLog.info("commands are ~" + runcommands);

			SceneAndPageSet.PageCommandStore
			.AddItem(runcommands, NewMessageURL.split("\\.")[0]);

			GwtLog.info("commands stored check:"
					+ SceneAndPageSet.PageCommandStore.GetItem(NewMessageURL.split("\\.")[0]));

			// run it
			JAMcore.processInstructionsWhenReady(runcommands, "fromStoryText"
					+ NewMessageURL);

		}

		JAMcore.storyPageLoading = false;

		GwtLog.info("removing from loading queue 2");
		PageLoadingData.PageLoadingQueue.remove(0);
		JAMcore.CheckLoadingQueue();
	}



	public static void testForInstructionsToRun() {

		GwtLog.info("testing for instructions to run now that the tigs are loaded");
		// make sure loading is finnished
		if (JAMcore.itemsLeftToLoad == 0) {

			GwtLog.info("loading done, now loop over commands:"+JAMcore.CommandsToRunAfterLoadingArray.size()+" in total");
			int i = 0;
			while (i < JAMcore.CommandsToRunAfterLoadingArray.size()) {

				CommandList commands = JAMcore.CommandsToRunAfterLoadingArray.get(i);
				String id       = JAMcore.CommandsToRunAfterLoadingArrayIDs.get(i);

				GwtLog.info("running instructions that were stored "+ commands);

				InstructionProcessor.processInstructions(commands, id, null);


				i++;

			}

			// clear then
			GwtLog.info("loading done clearing list");

			JAMcore.CommandsToRunAfterLoadingArray.clear();
			JAMcore.CommandsToRunAfterLoadingArrayIDs.clear();

		}

	}


	/** Removes all the (non-html) newlines from a string **/
	static public String RemoveCartReturns(String input_string) {
		input_string = input_string.replaceAll("\n", " "); 
		input_string = input_string.replaceAll("\r", " "); 
		input_string = input_string.replaceAll("\r\n", " ");
		return input_string;
	}

	/**
	 * This inserts the divs that get replaced by inventory items Allowing
	 * images, tigs and other items to be embedded straight into the page :)
	 * 
	 * @param newMessageURL
	 *            - the url of the text to insert (used as a unique ID)
	 * @param input_text
	 *            - the html of the text
	 **/
	static public void insertItemDivsIntoText(HTML input_text,
			final String newMessageURL) {

		JAMcore.BusyAddingDivs = true;
		String input_string = input_text.getHTML();

		// String input_string = input_text.getText();
		int starthere = 0;
		// array of widget names
		// ArrayList<String> widgets_to_add_ids = new ArrayList<String>();
		// widgets_to_add_ids.clear();

		// int BookShelfNum = 0; // change to global later

		// Window.alert("searching for <item= in :\n"+input_string);

		while (input_string.indexOf("<!-- item=\"", starthere) > 0) { //$NON-NLS-1$

			// Window.alert("looking for item match");

			// starthere = starthere + 1;
			int bookshelfloc = input_string.indexOf("<!-- item=\""); //$NON-NLS-1$
			if (bookshelfloc == -1) {
				// Window.alert("no matchs found");
				return;
			}
			// Window.alert("found at:"+bookshelfloc);

			// "\>
			int itemnameendloc = input_string.indexOf("\"", bookshelfloc + 11); //$NON-NLS-1$
			int bookshelfendloc = input_string.indexOf("-->", bookshelfloc); //$NON-NLS-1$
			// Window.alert("ends at:"+bookshelfendloc);

			String stringbefore = input_string.substring(0, bookshelfloc);
			String itemName = input_string.substring(bookshelfloc + 11,
					itemnameendloc);

			String stringafter = input_string.substring(bookshelfendloc + 3);

			// input_string =
			// stringbefore+"<a class=\"bookshelf\" href=\""+stringurl+"\"
			// target=\"_blank\"><img border=\"0\" src=\"blank.gif\"
			// width=\"30\" height=\"29\"></a>"
			// +stringafter;

			// ((InventoryIcon)currentItem.getDropTarget())

			// Window.alert("getting item "+itemName);

			// String testName = InventoryPanel.itemList.get(0).itemName;

			// add the item.
			// Window.alert("trying to add item");
			try {

				// Window.alert("add item:"+itemName);
				// processInstructions("- AddItem = "+itemName,"addItemFromStoryText");

				if (InventoryPanelCore.defaultInventory.inventoryContainsItem(itemName) == false) {
					GwtLog.info("adding item:" + itemName);
					InventoryPanelCore.defaultInventory.AddItem(itemName);

				}

			} catch (Exception e) {

				Window.alert("cant add item");
			}

			// Window.alert("getting item 2"+itemName);

			// Window.alert(itemName);

			// String popupElement =
			// ((InventoryIcon)InventoryPanel.itemList.get(0).getDropTarget()).PopUp.getElement().getString();
			// String popupElement =
			// ((InventoryIcon)InventoryPanel.itemList.get(0).getDropTarget()).ItemWithShadow.getElement().getString();

			// note:location swapped for message url

			input_string = stringbefore
					+ "<div ID=\"" + newMessageURL + "_item_" + itemName + "\"></div>" + stringafter; //$NON-NLS-1$ 						

			// find item named that

			widgets_to_add_ids.add(newMessageURL + "_item_" + itemName);

			starthere = bookshelfendloc + 2;

		}
		// Window.alert("divs added?:"+input_string);

		input_text.setHTML(input_string);

		// now the text is updated, we attach the items, but we first have to
		// wait for any new ones to load;
		Timer newTestTimer = new Timer() {

			@Override
			public void run() {
				// Log.info("checking for inline items");

				// check all IDs have loaded
				int i = 0;
				GwtLog.info("adding widgets for " + newMessageURL);

				while (i < widgets_to_add_ids.size()) {
					String itemToAddsName = widgets_to_add_ids.get(i);
					IsPopupContents ItemToAdd = InventoryPanelCore.defaultInventory
							.getItemPopUp(itemToAddsName.replaceFirst(
									newMessageURL + "_item_", ""));

					if ((itemToAddsName.startsWith(newMessageURL) && (ItemToAdd == null))) {
						GwtLog.info("no item found under that name, but could still be loading: "
								+ itemToAddsName.replaceFirst(newMessageURL
										+ "_item_", ""));
						return;

					} else if (ItemToAdd == null) {
						GwtLog.info("no item found under that name, probably not loading for this page: "
								+ itemToAddsName.replaceFirst(newMessageURL
										+ "_item_", ""));
						// Window.alert("no item found under that name : "+itemToAddsName);
						i++;
						continue;
					}

					i++;

					// if its a tig set it to the last clicked
					if (((IsPopupContents) ItemToAdd).POPUPTYPE().equals("TIG")) {

						GwtLog.info("TIG on page set");

						TigItemCore.lastclickedTig = (toggleImageGroupPopUp) ItemToAdd;
					}

				}
				// if all are ok we update
				// Log.info(i+" items found");
				GwtLog.info("starting to add items to page:" + newMessageURL);

				updateWidgetsInStoryText(newMessageURL);

				this.cancel();
				JAMcore.storyPageLoading = false;
				GwtLog.info("removing from loading queue 3");
				PageLoadingData.PageLoadingQueue.remove(0);
				JAMcore.CheckLoadingQueue();
			}

		};

		// in future we should tie this to the loading of the text itself.
		newTestTimer.scheduleRepeating(50);

		return;
	}

	private static void updateWidgetsInStoryText(final String newMessageURL) {

		int i = 0;
		// ArrayList<String> removeList = new ArrayList<String>(); //<Verycrude>

		while (i < widgets_to_add_ids.size()) {

			GwtLog.info("adding items to page:" + newMessageURL);

			String itemToAddsName = widgets_to_add_ids.get(i);
			String rawName = itemToAddsName.replaceFirst(newMessageURL
					+ "_item_", "");
			IsPopupContents ItemToAdd = InventoryPanelCore.defaultInventory.getItemPopUp(rawName);
			if (ItemToAdd == null) {
				GwtLog.info("no item found under that name : " + rawName);
				GwtLog.info("skipping item and going onto next" + i);
				i++;
				continue;
			}

			RootPanel.get(itemToAddsName).add((Widget) ItemToAdd);
			GwtLog.info("Adding to removelist: " + itemToAddsName);
			// removeList.add(itemToAddsName);

			// add swap item function to page instructions
			String CurrentPageCommands = SceneAndPageSet.PageCommandStore.GetItem(newMessageURL);
			GwtLog.info("adding replacediv to current page commands ("
					+ CurrentPageCommands + ")");

			CurrentPageCommands = CurrentPageCommands + "\n - ReplaceDiv = "
					+ itemToAddsName + "," + rawName;

			// if its a tig set it to the last clicked
			if (((IsPopupContents) ItemToAdd).POPUPTYPE().equals("TIG")) {

				GwtLog.info("TIG on page set 2");

				TigItemCore.lastclickedTig = (toggleImageGroupPopUp) ItemToAdd;
			}

			GwtLog.info("storing commands for:" + newMessageURL);
			GwtLog.info("commands =" + CurrentPageCommands);

			SceneAndPageSet.PageCommandStore
			.AddItemUniquely(CurrentPageCommands, newMessageURL);

			GwtLog.info("commands stored check"
					+ SceneAndPageSet.PageCommandStore.GetItem(newMessageURL));

			// remove the item (this is quite crude, having to add it to the
			// inventory and remove it again, probably should have an internal
			// store
			// Window.setTitle("removing item from store:"+itemToAddsName.replaceFirst(location+"_item_",
			// ""));
			GwtLog.severe("item being removed; " + itemToAddsName
					+ "|rawname=" + rawName);

			InventoryPanelCore.defaultInventory.RemoveItem(rawName);

			i++;
		}

		GwtLog.info("widgets_to_add_ids size" + widgets_to_add_ids.size());

		widgets_to_add_ids.clear();

		// Log.info("clearing widgets loaded. There is "+removeList.size()+" things to remove");

		// widgets_to_add_ids.clear();
		// int r=removeList.size();
		// while(r>=0){

		// Log.info(r+" removing:"+removeList.get(r));
		// widgets_to_add_ids.remove(removeList.get(r));

		// r--;
		// }
		// Log.info("widgets_to_add_ids size"+widgets_to_add_ids.size());
		// removeList.clear();

		JAMcore.BusyAddingDivs = false;
		testForInstructionsToRun();

	}

	/** swaps user specific words in the script file **/
	static public void SwapUserSpecificWords() {

		if (JAMcore.loggedIn) {
			JAMcore.controllscript = JAMcore.controllscript.replaceAll("<USERNAME>", JAMcore.Username); 
			JAMcore.controllscript = JAMcore.controllscript.replaceAll(
					"<ORGANISATION>", JAMcore.Organisation); 
		}

	}

	public void test_for_logged_in_user() {

		//if theres no server support we skip the request 
		if (!JAMcore.HasServerSave && !JAMcore.RequiredLogin) {
			GwtLog.info("No login system present");
			actionsToRunWhenNoLoginIsPresent("No login system found here and html said not to look. organisation:internal~");
			preloadData();
			return;
		}
		//

		//currentLoadingText = "testing for logged in user";		
		if (OptionalImplementations.gamesLoadMonitor.isPresent()){			
			OptionalImplementations.gamesLoadMonitor.get().setGameLoadStatus("testing for logged in user");			
		}
		

		FileCallbackRunnable onResponse = new FileCallbackRunnable(){

			@Override
			public void run(String responseData, int responseCode) {
				GwtLog.info("recieved response, processing logging...");

				String responsetext= responseData;

				if (responsetext.equalsIgnoreCase("NO")) { 
					// display login box
					GwtLog.info("No login data found or access denighed");

					// RootPanel.get().add(
					// MyApplication.loginbackground, 0, 0);

					//	DOM.setStyleAttribute(fadeback.getElement(),
					//			"z-Index", "" + (z_depth_max + 1)); //$NON-NLS-1$ //$NON-NLS-2$
					if (JAMcore.RequiredLogin){
						fadeback.getElement().getStyle().setZIndex((JAMcore.z_depth_max + 1));

						JAMcore.z_depth_max = JAMcore.z_depth_max + 1;

						if (OptionalImplementations.ServerStorageImplementation.isPresent()){
							OptionalImplementations.ServerStorageImplementation.get().openLoginBox();
							
						//	ServerOptions.user_login.OpenDefault();
							//ServerOptions.user_login.getElement().getStyle().setZIndex((JAMcore.z_depth_max + 1));
							
							JAMcore.z_depth_max = JAMcore.z_depth_max + 1;
							
						} else {
							GwtLog.severe("Login required but no sever storage implementation found");
						}
						
												

						// set user as not logged in
						JAMcore.Username = "not logged in"; //$NON-NLS-1$
						JAMcore.loggedIn = false;
					} else {
						actionsToRunWhenNoLoginIsPresent(responseData);
					}



				} else if (responsetext.startsWith("NOLOGIN") ||responsetext.startsWith("Access denied") ) {

					actionsToRunWhenNoLoginIsPresent(responseData);

				} else {

					GwtLog.info("recieved response :"+responsetext);

					JAMcore.Username = responsetext
							.substring(responseData.indexOf(
											"username:") + 9, responseData.indexOf("~", responseData.indexOf("username:") + 9)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					JAMcore.Organisation = responsetext
							.substring(responseData.indexOf(
											"organisation:") + 13, responseData.indexOf("~", responseData.indexOf("organisation:") + 13)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					// Client = response
					// .getText()
					// .substring(
					// response.getText().indexOf(
					//								"client:") + 7, response.getText().indexOf("~", response.getText().indexOf("client:") + 7)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					// we set the username, and log him in.

					GwtLog.info("Username :"+JAMcore.Username+" Organisation:"+JAMcore.Organisation);


					Window.setTitle(GamesInterfaceText.MainGame_Welcome_Back
							+ JAMcore.Username);

					Window.setStatus(JAMcore.Client);

					JAMcore.GameLogger.info("client-" + JAMcore.Client);

					GwtLog.info("starting logged in user processors (including getting the save game from server)");
					ServerOptions.user_login.LoginUser();


					GwtLog.info("Logged in");

					JAMcore.start_game_script_when_ready();


					// enable scrollbars
					//Window.enableScrolling(true);(disabled since THorn Game - why did we do this?)	

					/*
					 * DebugWindow.info("Setting text-"+Client);
					 * 
					 * if (Client.indexOf("Firefox") > -1) {
					 * //$NON-NLS-1$
					 * StoryTabs.getDeckPanel().setAnimationEnabled
					 * (false);
					 * StoryTabs.setAnimationEnabled(false);
					 * 
					 * } else {
					 * StoryTabs.getDeckPanel().setAnimationEnabled
					 * (true);
					 * 
					 * }
					 */
				}

				// now we know is the user is logged in or not, we
				// start the preloader;
				// // then the preloader
				preloadData();
			}
		};

		//what to do if theres an error
		FileCallbackError onError = new FileCallbackError(){

			@Override
			public void run(String errorData, Throwable exception) {
				// if local mode is on, then assume there is no login system at all,
				// else assume guest

				if (JAMcore.LocalFolderLocation.length()>3) {

					actionsToRunWhenNoLoginIsPresent("organisation:pclocal~");
				} else {

					Window.alert("no login system found, assumeing a guest user");

					System.out.println(" cant check login, assume guest"); //$NON-NLS-1$

					// load data
					JAM.loadGameData(" newuser");
					JAM.messagehistory.messageslist
					.setHTML("(No login script found, assumeing guest user");
					// activate game
					JAMcore.maingameloop();
					ServerOptions.user_login.hide();
					// fix size
					JAM.resizeStoryBox();

					JAMcore.Username = "guest"; //$NON-NLS-1$
					JAMcore.Organisation = "pc";
				}

			}

		};
		

		RequiredImplementations.getFileManager().getText(homeurl + "Login_System/TestIfUserIsLoggedIn.php",
				onResponse,
				onError,
				false);

		/*
		
		//RequestBuilder requestBuilder2 = new RequestBuilder(RequestBuilder.GET,
		//		homeurl + "Login_System/TestIfUserIsLoggedIn.php");

		try {
			requestBuilder2.sendRequest("", new RequestCallback() { //$NON-NLS-1$
				@Override
				public void onError(Request request, Throwable exception) {
					System.out.println("error in user logged in test"); //$NON-NLS-1$
				}

				@Override
				public void onResponseReceived(Request request,
						Response response) {

					Log.info("recieved response, processing logging...");

					String responsetext= response.getText();

					if (responsetext.equalsIgnoreCase("NO")) { 
						// display login box
						Log.info("No login data found or access denighed");

						// RootPanel.get().add(
						// MyApplication.loginbackground, 0, 0);

						//	DOM.setStyleAttribute(fadeback.getElement(),
						//			"z-Index", "" + (z_depth_max + 1)); //$NON-NLS-1$ //$NON-NLS-2$
						if (JAMcore.RequiredLogin){
							fadeback.getElement().getStyle().setZIndex((JAMcore.z_depth_max + 1));

							JAMcore.z_depth_max = JAMcore.z_depth_max + 1;

							user_login.OpenDefault();

							user_login.getElement().getStyle().setZIndex((JAMcore.z_depth_max + 1));
							//DOM.setStyleAttribute(user_login.getElement(),
							//		"z-Index", "" + (z_depth_max + 1)); //$NON-NLS-1$ //$NON-NLS-2$

							JAMcore.z_depth_max = JAMcore.z_depth_max + 1;

							// set user as not logged in
							JAMcore.Username = "not logged in"; //$NON-NLS-1$
							JAMcore.loggedIn = false;
						} else {
							actionsToRunWhenNoLoginIsPresent(response
									.getText());
						}



					} else if (responsetext.startsWith("NOLOGIN") ||responsetext.startsWith("Access denied") ) {

						actionsToRunWhenNoLoginIsPresent(response
								.getText());

					} else {

						Log.info("recieved response :"+responsetext);

						JAMcore.Username = responsetext
								.substring(
										response.getText().indexOf(
												"username:") + 9, response.getText().indexOf("~", response.getText().indexOf("username:") + 9)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						JAMcore.Organisation = responsetext
								.substring(
										response.getText().indexOf(
												"organisation:") + 13, response.getText().indexOf("~", response.getText().indexOf("organisation:") + 13)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						// Client = response
						// .getText()
						// .substring(
						// response.getText().indexOf(
						//								"client:") + 7, response.getText().indexOf("~", response.getText().indexOf("client:") + 7)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						// we set the username, and log him in.

						Log.info("Username :"+JAMcore.Username+" Organisation:"+JAMcore.Organisation);


						Window.setTitle(GamesInterfaceText.MainGame_Welcome_Back
								+ JAMcore.Username);

						Window.setStatus(JAMcore.Client);

						JAMcore.GameLogger.info("client-" + JAMcore.Client);

						Log.info("starting logged in user processors (including getting the save game from server)");
						user_login.LoginUser();


						Log.info("Logged in");

						start_of_game_script();


						// enable scrollbars
						//Window.enableScrolling(true);(disabled since THorn Game - why did we do this?)	

						/*
						 * DebugWindow.info("Setting text-"+Client);
						 * 
						 * if (Client.indexOf("Firefox") > -1) {
						 * //$NON-NLS-1$
						 * StoryTabs.getDeckPanel().setAnimationEnabled
						 * (false);
						 * StoryTabs.setAnimationEnabled(false);
						 * 
						 * } else {
						 * StoryTabs.getDeckPanel().setAnimationEnabled
						 * (true);
						 * 
						 * }
						 *
					}

					// now we know is the user is logged in or not, we
					// start the preloader;
					// // then the preloader
					preloadData();
				}

			});

		} catch (RequestException ex) {

			// if local mode is on, then assume there is no login system at all,
			// else assume guest

			if (JAMcore.LocalFolderLocation.length()>3) {

				actionsToRunWhenNoLoginIsPresent("organisation:pclocal~");
			} else {

				Window.alert("no login system found, assumeing a guest user");

				System.out.println(" cant check login, assume guest"); //$NON-NLS-1$

				// load data
				JAM.loadGameData(" newuser");
				JAM.messagehistory.messageslist
				.setHTML("(No login script found, assumeing guest user");
				// activate game
				JAM.maingameloop();
				JAM.user_login.hide();
				// fix size
				JAM.resizeStoryBox();

				JAMcore.Username = "guest"; //$NON-NLS-1$
				JAMcore.Organisation = "pc";
			}
		}
*/
	}

	private void actionsToRunWhenNoLoginIsPresent(String response) {
		JAMcore.Organisation = response
				.substring(
						response.indexOf("organisation:") + 13, response.indexOf("~", response.indexOf("organisation:") + 13)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		if (DOM.getElementById("loadingdiv") != null) {

			JAM.fadeOutHTMLElement("loadingdiv");

		}



		Window.setTitle(GamesInterfaceText.MainGame_Welcome_Back
				+ " (nologin system found");

	//	Window.setStatus(JAMcore.Client);

		JAMcore.GameLogger.info("Current Game Client=" + JAMcore.Client);
		JAMcore.GameLogger.info("Server Response text=" + response);

		JAMcore.loggedIn = false;

		// Feedback.setText("response
		// text="+response.getText());
		// now we wait for the controll script to load
		// before starting
		//if (controllscript.length() > 5) {
		JAMcore.start_game_script_when_ready();
		//} else {

		//the following timer is probably not needed.
		//this timer is meant to ensure the controllscript is loaded.
		//but we do the same thing within start_of_game_script, so why wait here as well?
		//Timer startgamedelay = new Timer() {

		//	@Override
		//	public void run() {

		//	if (controllscript.length() > 5) {
		//		start_of_game_script();
		//		this.cancel();
		//	}
		//}

		//};

		//startgamedelay.scheduleRepeating(100);

		//}
	}


	//static Timer addClickFeedbackImageOld = new Timer (){
	//	@Override
	//	public void run() {			
	//		tinything.show();			
	//		//(it auto removes when it completes its animation)	
	//	}		
	//};

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {


		String PostString = event.getValue();
		JAMcore.updateGameState(PostString);

		//make sure the history token is set back to a neutral thing so we can detect future changes
		History.newItem("--");

	}


	/** Closes all the open windows **/
	public static void closeAllWindows() {

		closeallwindows.setPlayBack();

	
		int size = JAMcore.popupPanelCurrentlyOpen.size();
		//int closei = size - 1;
		
		
		//note; we make a copy of the currently open list because the close function of eachpopup will remove themselves from the original list -
		//and you shouldnt modify lists your looping over.
		for (IsPopupPanel closethis : new ArrayList<IsPopupPanel>(JAMcore.popupPanelCurrentlyOpen)) { 
			JAMcore.GameLogger.info("closing "); 
			JAMcore.GameLogger.info("-" + closethis.getClass() + "-");
			closethis.CloseDefault();
		}

		// close cluebox
		ClueReveal.setOpen(false);
		// OpenClueBox.setUrl("./GameTextures/TitleStrip_leftOpen.jpg");
		// JargStaticImages.BIGTitleStripleftOpen().applyTo(OpenClueBox);
		OpenClueBoxButton.applyTo(OpenClueBox);

		// remove fade
		RootPanel.get().remove(fadeback);

		// make sure main ones are closed too all main panels.
		// SecretPanelFrame.CloseDefault();
		// PlayersNotepadFrame.CloseDefault();
		// PlayersInventoryFrame.CloseDefault();
	}

	static public void resizeEverything() {

		// unfix width of frame;
		// DOM.getElementById("gwtlink_borderframe").setAttribute("style", "");

		// wait a moment

		// Timer resizeStuffNOW = new Timer(){

		// @Override
		// public void run() {

		//Game_Window_Width = Window.getClientWidth();
		//Game_Window_Height = Window.getClientHeight();
		
		Game_Window_Width  = RequiredImplementations.getCurrentGameStageWidth();
		Game_Window_Height = RequiredImplementations.getCurrentGameStageHeight();
		
	

		// get width of border
		// int Game_Frame_Width =
		// DOM.getElementById("gwtlink_borderframe").getOffsetWidth();


		try {
			setInterfaceIconSize();
		} catch (Exception e) {
			GwtLog.info("set interface size failed");
			GwtLog.info("exception " + e.getLocalizedMessage());

			e.printStackTrace();
		}

		// resize message history if open
		if (MessageHistoryOpen == true) {
			RootPanel.get().remove(messagehistory);
			MessageHistoryOpen = false;
			MessageHistoryButton.setPlayBack();
		}

		GwtLog.info("resizeing story box");

		resizeStoryBox();

		// resize inventorys if open
		// if (InventoryOpen == true) {
		// defaultInventory.OpenDefault();
		// }

		InventoryPanelCore.ResizeAllInventoryPanels();

		GwtLog.info("resizeing scenes");
		SceneWidgetVisual.resizeAllScenes();

		// reposition cluebox
		// RootPanel.get().add(ScoreAndClueBox,
		// Window.getClientWidth() - 229, 7);

		DOM.setStyleAttribute(ScoreAndClueBox.getElement(), "zIndex", "110");

		// RootPanel.get().add(ClueReveal,
		// DOM.getElementById("bigtextbox").getAbsoluteLeft()+20,
		// DOM.getElementById("bigtextbox").getAbsoluteTop() -8);
		RootPanel.get().add(ClueReveal, JamImages.ScoreBack.getAbsoluteLeft(),
				JamImages.ScoreBack.getAbsoluteTop() + JamImages.ScoreBack.getOffsetHeight() - 5);

		DOM.setStyleAttribute(ClueReveal.getElement(), "zIndex", "1000");
		JAMcore.playersClues
		.setSize(
				(DOM.getElementById("bigtextbox").getOffsetWidth() - 14)
				+ "px", (DOM.getElementById("bigtextbox")
						.getOffsetHeight() - 21) + "px");

		// ClueReveal.setWidth(ScoreAndJokerContainer.getOffsetWidth()
		// + "px");

		if (Document.get().getElementById("musicControll") != null) {
			RootPanel.get("musicControll").add(musicPlayer); //$NON-NLS-1$
		}
		musicPlayer.setSize("90%", "100%");

		// fix width of frame so its not a %
		// this makes scrolling neater
		// DOM.getElementById("gwtlink_borderframe").getStyle().setPropertyPx("width",Game_Frame_Width);
		// }

		// };

		// resizeStuffNOW.schedule(4000);
	}

	public static void preloadData() {
		GwtLog.info("trigger preload");

		//set what to do when we get the text data retrieved
		FileCallbackRunnable onResponse = new FileCallbackRunnable(){

			@Override
			public void run(String responseData, int responseCode) {

				String preloadingdata = responseData.trim();
				preloadingdata = JAMcore.parseForLanguageSpecificExtension(preloadingdata);

				GwtLog.info("preload list recieved:" + preloadingdata);

				// split up to array
				List<String> LoadingList = Arrays.asList(preloadingdata.split("\r?\n|\r"));

				GwtLog.info("number of things to preload=:"
						+ LoadingList.size());

				Iterator<String> it = LoadingList.iterator();

				// loop over
				while (it.hasNext()) {
					String preloadthis = it.next().trim();

					//if length is less then 5 it cant be a real file url (a.png)
					if (preloadthis.length()<5){
						continue;						
					}
					// Log.info("adding to loading list"+preloadthis);

					SpiffyPreloader.addToLoading(preloadthis);


				}

				SpiffyPreloader.preloadList();
			}
		};

		//what to do if theres an error
		FileCallbackError onError = new FileCallbackError(){

			@Override
			public void run(String errorData, Throwable exception) {
				JAMcore.GameLogger.info("\n preloader failed exception" + exception.getLocalizedMessage()); //$NON-NLS-1$

			}

		};


		//using the above, try to get the text!
		//

		RequiredImplementations.getFileManager().getText("Preloadlist.txt",true,
				onResponse,
				onError,
				false);


		/*
		//doesnt yet support multi-language cacheing
		FileManager.GetTextSecurelyOLDMETHOD("Preloadlist.txt", new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {

				String preloadingdata = response.getText().trim();
				preloadingdata = JAM.parseForLanguageSpecificExtension(preloadingdata);

				Log.info("preload list recieved:" + preloadingdata);

				// split up to array
				List<String> LoadingList = Arrays.asList(preloadingdata
						.split("/\r\n|\r|\n/"));

				Log.info("number of things to preload=:"
						+ LoadingList.size());

				Iterator<String> it = LoadingList.iterator();

				// loop over
				while (it.hasNext()) {
					String preloadthis = it.next().trim();
					// Log.info("adding to loading list"+preloadthis);
					Preloader.addToLoading(preloadthis);
				}

				Preloader.preloadList();

			}

			@Override
			public void onError(Request request, Throwable exception) {
				JAM.DebugWindow
						.setText("\n preloader failed exception" + exception.getLocalizedMessage()); //$NON-NLS-1$

			}

		}, null, false);*/


	}
//
//	public static void toggleControlPanel(){
//		
//		if (ControllPanelShadows.isShowing()){
//			ControllPanelShadows.CloseDefault();
//		} else {
//			GwtScreenMangement.openControlPanel();
//		}
//	}
	
	/** Should be triggered if the browser window resizes
	 * This resizes the main story box, and positions other interface items correctly **/
	static public void resizeStoryBox() {

		//we put the close window button in its correct place, only if its set to be visible
		if (!closeallwindows.getElement().getStyle().getDisplay().equalsIgnoreCase("none")){


			if (closeallwindows.isAttached()){
				GwtLog.info("close all windows display is attached and display is"+closeallwindows.getElement().getStyle().getDisplay());
				GwtLog.info("ID is currently="+closeallwindows.getElement().getId());

				RootPanel.get().setWidgetPosition(closeallwindows, Window.getClientWidth() - 55, 0);
			} else {			
				GwtLog.info("____________adding closeallwindows to page");
				RootPanel.get().add(closeallwindows, Window.getClientWidth() - 55, 0);

				//NOTE THE ID IS NOT BEING SET RIGHT...WHY??!?

				//after attachment

				Scheduler.get().scheduleDeferred(new ScheduledCommand() {					
					@Override
					public void execute() {
						GwtLog.info("___________ setting zindex for closeallwindows ");

						closeallwindows.getElement().getStyle().setZIndex(1805);				
						closeallwindows.getElement().setId("closeallwindowsbox");
						if (RootPanel.get("closeallwindowsbox")==null){
							GwtLog.info("close window id not found");
						}
						GwtLog.info("close all windows has just been attached "+closeallwindows.getElement().getStyle().getDisplay());

					};
				});

			}
		}
		// set animation for all except firefox3 (has some glitch)
		// Feedback.setTitle(Client);
		// Log.info("Resizing for-"+Client);

		if (JAMcore.Client.toLowerCase().indexOf("firefox") > -1) { //$NON-NLS-1$
			((GwtChapterControl)JAMcore.GamesChaptersPanel).getDeckPanel().setAnimationEnabled(false);
			((GwtChapterControl)JAMcore.GamesChaptersPanel).setAnimationEnabled(false);

		} else {
			((GwtChapterControl)JAMcore.GamesChaptersPanel).getDeckPanel().setAnimationEnabled(true);

			// note;at some point this should be a proper options system
			((GwtChapterControl)JAMcore.GamesChaptersPanel).getDeckPanel().setAnimationEnabled(false);
			((GwtChapterControl)JAMcore.GamesChaptersPanel).setAnimationEnabled(false);
		}

		GwtLog.info("removing buttons");

		// tempory remove these buttons, as they are being moved, it looks nicer
		// for them to disasper first
		RootPanel.get().remove(MessageHistoryButton);
		RootPanel.get().remove(MessageBackButton);
		RootPanel.get().remove(MessageForwardButton);

		GwtLog.info("creating timer");

		Timer Blah = new Timer() {
			@Override
			public void run() {

				// Store screen settings
				Story_Text_Height = RootPanel.get("bigtextbox")
						.getOffsetHeight() - 25; // used to be -2

				Story_Text_Width = RootPanel.get("bigtextbox").getOffsetWidth() - 5; //$NON-NLS-1$

				//ToDo: Work out why this statement is re-running constantly for the iJammer
				//The bigtextbox offset height and width seems to be zero for some reason
				//*shrug*
				//maybe to do with container frames? html standards modes? aliens?

				//	GreyLog.info("__________________setting size to-"
				//			+ Story_Text_Height + "," + Story_Text_Width);

				// if width not set, run this again later
				if (Story_Text_Width < 10) {
					this.cancel();
					this.schedule(1200);
					return;
				}

				JAMcore.GameLogger.info("" + Story_Text_Height); //$NON-NLS-1$

				RootPanel.get("bigtextbox").add(((GwtChapterControl)JAMcore.GamesChaptersPanel)); //$NON-NLS-1$
				// fix text window to right size
				// StoryTabs.setWidth(Story_Text_Width+"px");

				((GwtChapterControl)JAMcore.GamesChaptersPanel).getDeckPanel().setSize(
						Story_Text_Width + "px", Story_Text_Height + "px"); //$NON-NLS-1$ //$NON-NLS-2$

				((GwtChapterControl)JAMcore.GamesChaptersPanel).getDeckPanel().setWidth("100%");
				((GwtChapterControl)JAMcore.GamesChaptersPanel).getDeckPanel().setHeight("100%");

				// fix score position
				if (JAMcore.ScoreBoxVisible_CuypersMode) {
					RootPanel.get().add(
							ScoreAndClueBox,
							JamImages.ScoreBack.getAbsoluteLeft(),
							JamImages.ScoreBack.getAbsoluteTop()
							+ (JamImages.ScoreBack.getHeight() / 2) - 15);
				}

				// reposition message history
				JAMcore.GameLogger.info("resizing");

				positionHistoryButtons();
				
				JAMcore.GameLogger.info("..done message history bits :"
						+ DOM.getElementById("feedback").getAbsoluteTop()
						+ " :" + FeedbackContainer.getAbsoluteTop() + ":");

				// reposition clock relative to statue if statue is there, if
				// not we wait for it to appear

				// if clocklady is present we position the clock
				if (Document.get().getElementById("clocklady") != null) {

					if (StatueHead.getWidth() > 33) {
						
						// Window.alert("drawing clock"+StatueHead.getWidth());
						RootPanel.get().remove(gwt_clock);
						RootPanel.get("clocklady").add(gwt_clock, (int) (StatueHead.getOffsetWidth() * 0.315), (int) (StatueHead.getOffsetWidth() * 0.585)); //$NON-NLS-1$

						gwt_clock.setRadius((int) (StatueHead.getOffsetWidth() / 2.2));
						
					} else {

						Timer displayClockAfterStatueLoaded = new Timer() {

							@Override
							public void run() {
								// Window.alert("testing statue:"+StatueHead.getWidth());

								if (StatueHead.getWidth() > 33) {
									// Window.alert("drawing clock");
									RootPanel.get().remove(gwt_clock);
									RootPanel
									.get("clocklady").add(gwt_clock, (int) (StatueHead.getOffsetWidth() * 0.315), (int) (StatueHead.getOffsetWidth() * 0.585)); //$NON-NLS-1$

									gwt_clock.setRadius((int) (StatueHead
											.getOffsetWidth() / 2.2));
									this.cancel();

								}
							}

						};

						displayClockAfterStatueLoaded.scheduleRepeating(1000);

					}

				}
				// reposition music box to float above the rest if music box is
				// present
				if (Document.get().getElementById("musicControll") != null) {

					int MpSizeX = DOM.getElementById("musicControll")
							.getOffsetWidth() - 60;
					int MpSizeY = musicPlayer.CurrentMusicTrackLabel
							.getOffsetHeight();
					int MpX = DOM.getElementById("musicControll")
							.getAbsoluteLeft() + 20;
					int MpY = DOM.getElementById("musicControll")
							.getAbsoluteTop()
							+ DOM.getElementById("musicControll")
							.getOffsetHeight();
					musicPlayer.CurrentMusicTrackLabel.setPixelSize(MpSizeX,
							MpSizeY);
					musicPlayer.CurrentMusicTrackLabel.ListContainer
					.setWidth(MpSizeX + "px");
					musicPlayer.CurrentMusicTrackLabel.getElement().getStyle()
					.setProperty("zIndex", "500");
					RootPanel.get().add(musicPlayer.CurrentMusicTrackLabel,
							MpX, MpY - 23);
					musicPlayer.setSize(DOM.getElementById("musicControll")
							.getOffsetWidth() + "px", "100%");

				}

			}
		};

		GwtLog.info("deattach the Storytext stuff");

		// deattach the Storytext stuff
		if (((GwtChapterControl)JAMcore.GamesChaptersPanel).isAttached()){
			RootPanel.get("bigtextbox").remove(((GwtChapterControl)JAMcore.GamesChaptersPanel)); //$NON-NLS-1$
		} else {
			GwtLog.warning("StoryTabs was not attached when attempting to remove...hu?");

		}
		// wait a mo for the browser to catch up (if its IE that is, Opera works
		// just dandy)
		if (JAMcore.Client.indexOf("msie") > -1) { //$NON-NLS-1$

			// Window.alert("triggering IE resize in 0.9 seconds");
			Blah.schedule(900);
			// Blah.run();

			// firefox probably dosnt need it
		} else if (JAMcore.Client.indexOf("firefox2") > -1) { //$NON-NLS-1$
			Blah.schedule(900);
		} else if (JAMcore.Client.indexOf("WebKit") > -1) { //$NON-NLS-1$
			Blah.schedule(900);
		} else {
			Blah.run();
		}

	}

	/** Loads data into the game, after resetting it first **/
	public static void loadGameData(String GameData) {
		loadGameData(GameData,true);
	}

	/** Loads data into the game, reset first is optional **/
	public static void loadGameData(String GameData, boolean resetGameFirst) {

		// remove first space if theres one
		if (GameData.startsWith(" ")){
			GameData = GameData.substring(1);
		}

		// Feedback.setText("new user-"+GameData);
		// MyApplication.Feedback.setTitle("loading game 2.5");
		if ((GameData.compareTo("newuser") == 0) || (GameData.length() < 4)) {

			// MyApplication.Feedback.setTitle("loading game 2.6");
			startNewGame();

		} else {

			// make sure login is off
			//ServerOptions.user_login.hide(); //TODO: should be part of optional server save implementation?
			if (OptionalImplementations.ServerStorageImplementation.isPresent()){
				OptionalImplementations.ServerStorageImplementation.get().closeLoginBox();							
			} 
			// 

			// double check
			if (GameData.length() < 4) {
				return;
			}

			//Why do we have this ? :?
			//It will mess up needed <br>s in html loading stuff :-/
			//String responsetext = GameData.replaceAll("<br />", "\n"); //$NON-NLS-1$ 
			//responsetext = responsetext.replaceAll("<br>", "\n"); //$NON-NLS-1$
			String responsetext = GameData;
			//	responsetext = responsetext.substring(0,
			//		responsetext.indexOf(":end")); //$NON-NLS-1$


			// Window.alert(responsetext);
			// Feedback.setText(responsetext);
			// update widgets HTML field
			//	System.out.println("\n Instructions-:" + GameData); //$NON-NLS-1$

			if (resetGameFirst){
				GwtLog.info("resetting game data");
				GameManagementClass.clearAllGameData();
			}
			GwtLog.info("loading game data length:"+responsetext.length());
			GwtLog.info("loading game data:"+responsetext);			

			JAMcore.processInstructions(responsetext, "Loading", null);

			// start music
			//
			// MusicBox.CurrentMusicTrackLabel
			// .setItemSelected(musicPlayer.CurrentMusicTrackLabel
			// .getItemCount() - 1, true);
			// musicPlayer.playtrack(MusicBox.musicTracks.size() - 1);
			// }
			// });
			// } catch (RequestException ex) {
			// String responsetext = "can not process save file"; //$NON-NLS-1$
			// System.out.print(responsetext + " " + ex.getMessage());
			// //$NON-NLS-1$
			// }

		}

	}

	/** gets the games href **/
	private static native String getLocation() /*-{ 
	   return $wnd.location.href; 
	}-*/;

	/** should be fired the first time a user starts a new game **/
	public static void startNewGame() {

		
		musicPlayer.CurrentMusicTrackLabel.setItemSelected(
				musicPlayer.CurrentMusicTrackLabel.getItemCount() - 1,
				true);
		
		GWTMusicBox.playtrack(MusicBoxCore.musicTracks.size() - 1,100);

		//close open windows to do with loading/saving
		//ServerOptions.user_login.CloseDefault();
		if (OptionalImplementations.ServerStorageImplementation.isPresent()){
			OptionalImplementations.ServerStorageImplementation.get().closeLoginBox();
			
		};
		
		JAM.loadGamePopup.CloseDefault();		
		JAM.saveGamePopup.contentPanel.CloseDefault();


		//start the game from the start of the script file
		JAMcore.GameLogger.info("start game script");
		JAMcore.start_game_script_when_ready();

	}

	public static void fadeOutHTMLElement(final String elementID) {
		if (OptionalImplementations.PageStyleCommandImplemention.isPresent()){
			OptionalImplementations.PageStyleCommandImplemention.get().fadeOutHTMLElement(elementID,1670); //1670 Isn't anything special, its just backward compatible with our old system which subtracted 6 each time from the opacity. Why 6? Well, why not.

		}

	}

	public static void createAndAddNewPage(final String NewMessageURL,
			final SceneAndPageSet addToThisSet, String responseString) {

		HTML StoryText = new HTML(JAMcore.SwapCustomWords(responseString)
				+ "");
		
		GWTSceneAndPageSet TempCurrentLocationTabs = (GWTSceneAndPageSet) addToThisSet; //we know its GWTSceneAndPageSet
		
		// we used to have a scroller here, but now it might not be needed, so
		// we replace it with a simple panel
		// ScrollPanel StoryScroller = new ScrollPanel();
		SimplePanel StoryScroller = new SimplePanel();
		// set relative
		DOM.setStyleAttribute(StoryScroller.getElement(), "position","relative");

		StoryScroller.add(StoryText);

		GwtLog.info("setting StoryText styles");

		// StoryScroller.add(new HTML("|^^^^^^^^^^^^^^^^^^^^^^^^"));

		StoryText.getElement().getStyle().setPropertyPx("padding", 0);
		StoryText.getElement().getStyle().setPropertyPx("margin", 0);

		System.out.println(StoryText);
		String randomstyle = "backgroundtexture" + ((int) ((Math.random() * 3) + 1)); //$NON-NLS-1$

		StoryText.addStyleName("maintextsettings");
		// if firefox, set the back of the scroller
		// instead
		if (JAMcore.Client.toLowerCase().contains("firefox")
				|| JAMcore.Client.toLowerCase().contains("chrome")) {
			StoryScroller.addStyleName(randomstyle);
			// MyApplication.DebugWindow.info("firefox
			// found");

		} else {
			StoryText.setStylePrimaryName(randomstyle);
			JAMcore.GameLogger.info("client found=" + JAMcore.Client);

		}

		StoryText.addStyleName("pngfix");

		///StoryScroller.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

		GwtLog.info("setting StoryText size");
		StoryScroller.setWidth("100%");
		StoryScroller.setHeight("auto");

		DOM.setStyleAttribute(StoryScroller.getElement(), "zIndex", "150");

		//StoryText.setHeight("100%"); //$NON-NLS-1$ //$NON-NLS-2$
		StoryText.setWidth("100%");
		StoryText.setHeight("auto");

		// StoryText.addStyleName("overflowscroll");

		// TempCurrentLocationTabs.addStyleName("overflowscroll");

		// TempCurrentLocationTabs
		//	.setHeight(Story_Text_Height + "px"); //$NON-NLS-1$

		GwtLog.info("setting TempCurrentLocationTabs size");

		GwtLog.info("setting TempCurrentLocationTabs size:" + Story_Text_Width);

		TempCurrentLocationTabs.visualContents.setWidth((Story_Text_Width - 2) + "px"); //$NON-NLS-1$

		// TempCurrentLocationTabs.setSize(
		//			"100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

		TempCurrentLocationTabs.visualContents.setWidth("100%");

		GwtLog.info("setTempCurrentLocationTabs size");

		// TempCurrentLocationTabs.setHeight(TempCurrentLocationTabs.getOffsetHeight()+"px");

		Label Tab = new Label();
		Tab.setText(NewMessageURL.substring(0, NewMessageURL.length() - 5));

		// if current location is the active one,
		// then we add to the lists;

		if (TempCurrentLocationTabs.equals(JAMcore.CurrentScenesAndPages)) {
			// add opened chapter name to list
			JAMcore.currentOpenPages.add(NewMessageURL);

			// set chapter name to match
			GwtLog.info(" adding location " + JAMcore.usersCurrentLocation + " to page "
					+ NewMessageURL);
			JAMcore.locationpuzzlesActive.add(JAMcore.usersCurrentLocation);

			if (JAMcore.AnswerBox.isPresent()){
			JAMcore.AnswerBox.get() .setEnabled(true);
			JAMcore.AnswerBox.get().setFocus(true);
			JAMcore.AnswerBox.get().setText(""); 
			}
			
		}

		GwtLog.info("adding story scroller and selecting tab");

		//remove the file extension
		String tabText = (NewMessageURL.substring(0, NewMessageURL.length() - 5));
		//remove any language specific parts of the filename
		tabText = JAMcore.removeLanguageSpecificExtension(tabText);

		TempCurrentLocationTabs.visualContents.add(StoryScroller,tabText);

		TempCurrentLocationTabs.visualContents.selectTab(TempCurrentLocationTabs.visualContents.getWidgetIndex(StoryScroller));

		// now we add items if theres any.
		// Window.alert("trying to swap text over"+responseString);

		GwtLog.info("testing page " + NewMessageURL + " for items or commands");

		boolean nothingElseToDo = true;

		if (responseString.contains("<!-- item=\"")) {
			GwtLog.info("adding divs");
			insertItemDivsIntoText(StoryText,
					NewMessageURL.substring(0, NewMessageURL.length() - 5));
			nothingElseToDo = false;

		}
		if (responseString.contains("<!-- run")) {
			GwtLog.info("running commands for page...");
			runPagesCommands(StoryText, NewMessageURL);
			nothingElseToDo = false;
		}

		// System.out.print("\n -----------------number of temp tabs = "+TempCurrentLocationTabs.getWidgetCount());
		// new line;
		// CurrentLocationTabs = TempCurrentLocationTabs;
		// ---
		// System.out.print("\n -----------------number of current tabs = "+CurrentLocationTabs.getWidgetCount());

		if (JAMcore.Client.contains("firefox")) {
			StoryText
			.setSize(
					Story_Text_Width - 30 - 15 + "px", Story_Text_Height - 350 + "px"); //$NON-NLS-1$ //$NON-NLS-2$ removed - 55 

		}

		JAMcore.NumberOfHTMLsLeftToLoad--; // used to be inlined in the ifs below

		// check if selection is nesscery
		if (JAMcore.triggerSelectCheck) {

			GwtLog.info("/n setting page too :" + JAMcore.pageToSelect + ":");
			GWTSceneAndPageSet.selectPageIfLoadedOrSceneStraightAway(JAMcore.pageToSelect);

			// update pending list
			// NumberOfHTMLsLeftToLoad--;

		} else {

			// update pending list
			// NumberOfHTMLsLeftToLoad--;
		}

		if (nothingElseToDo) {
			JAMcore.storyPageLoading = false;
			GwtLog.info("removing from loading queue 4");
			PageLoadingData.PageLoadingQueue.remove(0);
			JAMcore.CheckLoadingQueue();
		}
	}

	/** Sends an email to the current user if they are registered.
	 * Requires a working emailer.php to receive the request and tell the server to send the email  **/
	public static void sendEmail(String To, String Title, String From, String Content, boolean AsHTML){

		//if the To field is left blank we assume the current logged in user (if logged in to the sever - WE really should put a check for that!)
		String ToField="";
		if (To!=null){
			ToField = "To="+To+"&";
		} else {
			ToField="";
		}

		String HeadersField="";
		if (AsHTML){

			String HTMLHEADERS  = "MIME-Version: 1.0" + "\r\n";
			HTMLHEADERS = HTMLHEADERS+ "Content-type: text/html; charset=iso-8859-1" + "\r\n";

			HeadersField = "&Headers="+HTMLHEADERS;
		} else {
			HeadersField="";
		}



		RequestBuilder emailer = new RequestBuilder(
				RequestBuilder.POST, emailer_url);

		try {
			emailer.sendRequest(ToField+
					"Title=" + Title + "&From=" + From + "&Content=" + Content+HeadersField, new RequestCallback() { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				@Override
				public void onError(
						Request request,
						Throwable exception) {

				}

				@Override
				public void onResponseReceived(
						Request request,
						Response response) {
					// Feedback.setText(response.getText());

				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	/** Controls all the global game triggers. ie, mouse actions or keyboard actions that should
	 * trigger before any other reaction to them**/
	@Override
	public void onPreviewNativeEvent(NativePreviewEvent event) {

		//gets and stores the current event in the variable "e"
		//ie, "what just happened"
		NativeEvent e = event.getNativeEvent();

		//Based on what just happened we do stuff!
		int keyCode = e.getKeyCode();
		switch (Event.getTypeInt(e.getType())) {

		case Event.ONMOUSEDOWN:

			//global over the whole game, we detect any right mouse click
			if (e.getButton() == NativeEvent.BUTTON_RIGHT) {

				GwtLog.info("right click native event");

				//right mouse clicks unhold any held item.
				InventoryPanelCore.unholdItem();

				//only prevent a right click if the debug is not open
				if (!ObjectInspector.debugIsOpen()){
					//prevent the browsers normal right click events
					e.preventDefault();
				} else {
					//else we cancel some debugging specific events
					//if editing
					if (ObjectInspector.currentCmapBeingEdited!=null){
						ObjectInspector.currentCmapBeingEdited.stopEditingObjectsCmap();
					}		
				}

			}


			break;

		case Event.ONMOUSEUP:
			//hide the mouse click indictator
			clickVisualiser.tinything.hide();

		case Event.ONKEYUP:

			// remove key from keys currently held down list
			JAMcore.HeldKeys.remove(keyCode);

			if (JAMcore.DebugMode){
				if (ObjectInspector.debugIsOpen()){
					((SceneWidgetVisual)SceneObjectDatabase.currentScene).testForKeyUp(keyCode);					
				}	
			}

			break;

		case Event.ONMOUSEMOVE:

			//	if (CustomMouseImage!=null){						
			//CustomMouseImage.						
			//	RootPanel.get().setWidgetPosition(CustomMouseImage, e.getClientX()+4, e.getClientY()+4);

			//	}


			break;


		case Event.ONKEYDOWN:
			
			if (JAMcore.DebugMode){
				if (ObjectInspector.debugIsOpen()){
					//Log.info("keydown when inspector is open");					
					((SceneWidgetVisual)SceneObjectDatabase.currentScene).testForKeyDown(keyCode);					
				}	 else {
				//	Log.info("keydown when inspector is closed"+ObjectInspector.numOfInspectorsOpen);					
				}
			}
			
			//if key actions are currently being ignored then we do nothing
			if (JAMcore.IgnoreKeyPresses){
				break;
			}
			
			// add key to keys currently held down 
			JAMcore.HeldKeys.put(keyCode,KeyState.NewlyDown);

			if (keyCode == 46 && JAMcore.DebugMode) {

				//InstructionProcessor.currentScene.scenesCmap.showCmap(!InstructionProcessor.currentScene.scenesCmap.isCmapVisible());
				SceneObjectDatabase.currentScene.toggleCmap();

			}
			if (keyCode == 48 && JAMcore.DebugMode) {

				//InstructionProcessor.currentScene.scenesCmap.showPath(!InstructionProcessor.currentScene.scenesCmap.isPathVisible());
				SceneObjectDatabase.currentScene.togglePath();

			}
			
			if (keyCode == KeyCodes.KEY_SHIFT){

				GwtLog.info("KEY_SHIFT down:");
			}

			if (keyCode==KeyCodes.KEY_ESCAPE){

				GwtLog.info("Escape Detected:");


				if (ObjectInspector.currentCmapBeingEdited!=null){
					ObjectInspector.currentCmapBeingEdited.stopEditingObjectsCmap();
				}		

			}

			if (keyCode==KeyCodes.KEY_I && e.getShiftKey() && JAMcore.DebugMode){

				GwtLog.info("For inspector open:");

				//crude method to grab the first object we know about (Crude because it gets ALL objects on a scene first
				//in order to get just one of them)
				SceneObject openinspectorOn = SceneObjectDatabase.currentScene.getScenesData().allScenesCurrentObjects().iterator().next();

				if (openinspectorOn!=null){
					openinspectorOn.openObjectsInspector();
				} else{
					GwtLog.info("NO OBJECTS IN CURRENT SCENE SO CANT OPEN INSPECTOR :( sad code error :( ");
				}
				
				//we also force close the loading screen if it was stuck
				 SceneObjectDatabase.currentScene.forceCloseLoading();

			}

			//we only fire this on keys that wont fire OnKeyPress
			//Note; IE10 and earlier will fire OnKeyPress anyway, meaning these events fire twice
			//if (KeyCodes.isArrowKey(keyCode) || keyCode == KeyCodes.KEY_ESCAPE  ){
				GwtLog.info("key down detected:"+keyCode);
			//	checkForEventsForHeldKeys();					
			//}
				
			triggerActionsForKey(keyCode); //test for actions on the newly down key
			
			//
			break;
		case Event.ONKEYPRESS:

			//if key actions are currently being ignored then we do nothing
			if (JAMcore.IgnoreKeyPresses){
				break;
			}

			//for all the keys held we fire actions. As this KeyPress event gets fired by browsers continuously, this means the events repeat
			//Note: This method doesn't work for arrow keys in some browsers
			GwtLog.info("key press detected");
			checkForEventsForHeldKeys(); 
			

			break;

		}



	}

	private void checkForEventsForHeldKeys() {
		
		Iterator<Integer> kit = JAMcore.HeldKeys.keySet().iterator();
		
		while (kit.hasNext()) {

			int keycode = kit.next();
			KeyState state = JAMcore.HeldKeys.get(keycode);

			GwtLog.info("key press detected:" + keycode+" ("+state+")");

			
			//of newlydown we don't fire, but instead change to currently held (so its fired the next time)/
			//This ensures we don't fire twice for both keydown and keypress
			if (state==KeyState.NewlyDown){
				JAMcore.HeldKeys.put(keycode, KeyState.CurrentlyDown);
				continue;
			}
			
			triggerActionsForKey(keycode);

		}
	}

	private void triggerActionsForKey(int keycode) {
		//test only  for keyboard nudge commands when editing
		//used to be current active scene
		if (SceneObjectDatabase.currentScene!=null){

			//if editing
			if (ObjectInspector.debugIsOpen()){

				//cast for now to get its unique nudge function
				((SceneWidgetVisual)SceneObjectDatabase.currentScene).testForKeyPress(keycode);
				
				if (keycode==KeyCodes.KEY_SHIFT){
					GwtLog.info("Key shift pressed:");
				}
				
				if (keycode==KeyCodes.KEY_ESCAPE){
					GwtLog.info("Escape Detected::");

					if (ObjectInspector.currentCmapBeingEdited!=null){
						ObjectInspector.currentCmapBeingEdited.stopEditingObjectsCmap();
					}			

				}


			} else {

				//else test for normal interaction							
				JAMcore.testForActionsToTriggerOnKeyPress(keycode);
				

			}
		}
	}

	/** fades out, then removes, the loading div used to black out the screen while loading **/
	public static void fadeoutLoadaingDiv() {
		if (DOM.getElementById("loadingdiv") != null) {
			JAM.fadeOutHTMLElement("loadingdiv");
		}

	}

}
