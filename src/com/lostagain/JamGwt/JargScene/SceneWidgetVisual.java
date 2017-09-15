package com.lostagain.JamGwt.JargScene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import com.google.common.base.Optional;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.CurrentScenesVariables;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.CollisionMap.SceneCollisionMap;
import com.lostagain.Jam.Scene.SceneData;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.JamGwt.GamesInterfaceText;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.clickVisualiser;
import com.lostagain.JamGwt.JAMtext.CommonText;
import com.lostagain.JamGwt.JAMtext.JamText;
import com.lostagain.JamGwt.JargScene.CollisionMap.CollisionDebugBox;
import com.lostagain.JamGwt.JargScene.CollisionMap.CollisionMapVisualiser;
import com.lostagain.JamGwt.JargScene.debugtools.ObjectInspector;
import com.lostagain.JamGwt.Sprites.InternalAnimations;


import lostagain.nl.spiffyresources.client.IsSpiffyGenericLogger;
import lostagain.nl.spiffyresources.client.spiffycore.Simple2DPoint;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyDragPanel;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyDragPanel.animationSpeedInterpretationMode;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyLogBox;


import lostagain.nl.spiffyresources.client.IsSpiffyGenericLogger;
import lostagain.nl.spiffyresources.client.spiffycore.Simple3DPoint;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyLogBox;

/**
 * A scene widget is designed to be a larger, scrollable, scene which can contain<br>
 * many interactable SceneObjects.<br>
 * Think of it as a typical location in a adventure game.<br>
 * <br>
 * Its created out of data in a scenedata object.<br> 
 * <br>
 * @author Thomas (mostly) 
 ***/
public class SceneWidgetVisual extends SceneWidget {

	/** The general console log **/
	public static Logger Log = Logger.getLogger("JAMCore.SceneWidgetVisual");

	/** 
	 * The log for scene changes (ie, not objects on the scene)
	 * This effects the log that appears in the ObjectInspector under the scene tag 
	 * **/
	public IsSpiffyGenericLogger ScenesLog  = SpiffyLogBox.createLogBox(JAMcore.DebugMode);

	/** This is the "core" of the scenewidget.<br>
	 *  Its essence. Its whole purpose. Its heart.<br>
	 *  <br>
	 *  Its where all the SceneObjects are placed, and interacted with.<br>
	 *  Its where the use can drag the scene about, if allowed.<br>
	 *  Its where the objects can be moved, if in editmode.<br>
	 *  ITs...<br>
	 *  The SpiffyDragPanel class!**/
	public SpiffyDragPanel dragPanelCore;
	

	// this widget 
	public SceneWidgetVisual thisWidget = this;



	/** scenes data (includes objects)
	 * <br><br>
	 * The scenes data contains everything needed to set up this scene<br>
	 * That includes amongst other things its background, its position, and its size.<br>
	 * Critically, it also contains all the objects of all types that this scene has on it <br>**/
	//private SceneData scenesData;// = new SceneData();


	/** a panel that appears ontop of the collision map if its visible
	 * and can receive click events.<br>
	 * This is used only for debugging purposes. <br>
	 * Normally when the collision map is visible, no clicks will be registered as the svg sketch is above everything else.
	 * This goes ontop of that sketch but is invisible, so it can sense clicks <br> **/
	public FocusPanel previewClickPanel = new FocusPanel();




	/**
	 * edit mode , allows spites to be moved about and disables all actions on
	 * them
	 **/	
	static boolean editMode    = false;	

	/**
	 * if we are currently editing the objects height (ie, shift key is held down)
	 */
	static boolean editInZAxis = false;
	static int editInZStartPos = 0;

	/** In order to handle touchscreens we must constantly monitor the x/y touch position.
	 * This is because the getTouches array doesn't contain anything when the TouchEnd event is fired, 
	 * yet the game code needs to know where that TouchEnd happened **/
	static private Touch LastTouchMoveScreen = null;



	/**
	 * A scene widget is designed to be a larger, scrollable, scene which can contain<br>
	 * many interactable SceneObjects.<br>
	 * Think of it as a typical location in a adventure game.<br>
	 * <br>
	 ** Its created from scene data object<br> 

	public SceneWidget(SceneData scenesdata) {

		all_scenes.put(scenesdata.SceneFolderName.toLowerCase(), this);
		Log.info("Loading scene: "+scenesdata);

		setUpScene(scenesdata);

	}**/


	///** gets a instance of the sceneDataVisual object **/
	//@Override
	//public SceneData getScenesData() {
	//	return scenesData;
	//}


	///** sets the SceneDataVisual object, as well as the internal superclass reference to it  **/
	//public void setScenesData(SceneData scenesData) {
	//	this.scenesData = scenesData;		
		//update the supertype to match. (these should always point to the same thing
	//	super.setScenesData(scenesData);

	//}





	/**
	 * A scene widget is designed to be a larger, scrollable, scene which can contain<br>
	 * many inter actable SceneObjects.<br>
	 * Think of it as a typical location in a adventure game.<br>
	 * <br>
	 ** Its created from scene from a ini file **/
	
	boolean firstLoadSceneWidgetVisual = true;
	
	public SceneWidgetVisual(final String SceneFileName) {
		super(SceneFileName);

		//add to list
		//all_scenes.put(SceneFileName.toLowerCase(), this);
		//Log.info("Loading scene: "+SceneFileName);

		dragPanelCore = new SpiffyDragPanel(SceneFileName,true);	//the true at the end turns on a experimental new way for the scene to scroll. Likely wont work when scene is dragable
		//	super.initWidget(sceneBackground);
		dragPanelCore.setWidth("100%"); //used to be super
		dragPanelCore.setHeight("100%");

		//	addToLoading(); //we start with one thing to load (The initial JAM file) We only subtract for this one again after the other items have started loading, else it thinks its finnished too soon

		ScenesLog("loading scene", "green");
		dragPanelCore.setLoading(true,GamesInterfaceText.Scene_GettingScenesData,InternalAnimations.getAnimation("loading"));
		dragPanelCore.setLoadingIconDefaultMessage(GamesInterfaceText.Scene_LoadingLoadingTime);

		//ensure other loading div is faded out as the scenebackground loading message should be clearly visible
		//Note, really we should be able to set this fading out straight after .setLoading(  above, but for some reason
		//theres a delay on the scenesloading background appearing, so we wait for the first file response to make sure
		//the overall fader doesn't fade out too soon
		JAM.fadeoutLoadaingDiv(); 


		// get universal data files if not yet loaded
		//as well as setup visualizers for logger classes
		if (firstLoadSceneWidgetVisual) {
			//TODO: all but the collision logger can be moved to the superclass
			Log.info(" loading global actions ");
			//InstructionProcessor.loadGlobalSceneActions();		
			SceneCollisionMap.setCollisionLogger(new CollisionDebugBox(this,null)); 
			//firstLoadOfAnyScene = false;
			firstLoadSceneWidgetVisual = false;
		}
		
		//
		//fetchSceneData(SceneFileName);


	}
	
	
	
	public void intialize(){

		Log.warning("initialising "+SceneFileName);
		setupBackgroundTouchHandlers();
		
		
		super.intialize();
		
		
		resizeScene(); //crude fix - drag for smaller then screen scenes messes up without this reset of size information
		
	}

	
	/** 
	 * The keep visible parameters ensures its position is kept within the screen limits, and in the case
	 * of dialogues not overlapping with an existing dialogue box.
	 * 
	 * NOTE: it does not ensure its visible from a z-index or visibility standpoint.  
	 * **/
	//TODO: consider % support.
	//That is, position screen relative by converting to pixel, then add the existing x/y/z to it
	//hmm...nope. Per-frame movement on scene scrolling is needlessly heavy. 
	//Screen relative stuff should be implemented some other way. (GDX is easy - glue to camera)
	//
	public void setObjectsPosition(SceneObjectVisual sceneobject, int x, int y,int z, boolean keepvisible) {

		//Log.info("_____moving object "+widget.objectsCurrentState.ObjectsName+": on " + this.SceneFileName);

		boolean updateRel = false;
		
		//if its a dialogue we should ensure its not colliding with other dialogues
		if (sceneobject.getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.DialogBox && keepvisible){			

			int safey = ((SceneObjectVisual)sceneobject).getSafeHeightAwayFromDialogues(x, y);		
			if (safey!=y){
				y=safey;
				//if y changed
				if(sceneobject.tempYOverride.isPresent() && y==sceneobject.tempYOverride.get()){

					sceneobject.ObjectsLog.info("DialogBoxs might be colliding! but height should already be safe ="+y);
				
				} else {

					sceneobject.ObjectsLog.info("DialogBoxs might be colliding! safe height now ="+y);
					sceneobject.tempYOverride = Optional.of(y); //we save the y here so things positioned relatively are correct
					//relative things might need updating
					updateRel = true;
				
				}
			} else {
				sceneobject.ObjectsLog.info("DialogBoxs doesnt need to move out the way y="+y);
				
				sceneobject.tempYOverride = Optional.absent();
			}
			
		} else {

			sceneobject.ObjectsLog.info("Object not set to move out the way. keepvisible="+keepvisible);
			sceneobject.tempYOverride = Optional.absent();
		}

		//FAKE Z location as we are a 2d implementation
		//displace y upwards by the z			
		y=y-z;
		
		dragPanelCore.setWidgetsPosition(sceneobject.getInternalGwtWidget(), x, y,keepvisible);

		 if (updateRel){
				sceneobject.updateThingsPositionedRelativeToThis(false);
		}
	}


	@Override
	public void setUpScene(SceneData scenedata) {
		dragPanelCore.setLoadingMessage(GamesInterfaceText.Scene_SettingUpScene);
		super.setUpScene(scenedata);
		
	}
	
	
	
	/** sets the scene widgets background size, movement limits and background image **/
	protected void setupBackground(SceneData scenedata) {
		dragPanelCore.setSize("100%", "100%");

		// internal size and limits taken from scene data

		Log.info("_____setting size:" + scenedata.InternalSizeX + ","				+ scenedata.InternalSizeY);

		dragPanelCore.setInternalSize(scenedata.InternalSizeX,	scenedata.InternalSizeY); //very important, wont work without as its got no way to know what its internal size should be.
		
		
/*
		//set movement limits to size by default	
		if (scenedata.MovementLimitsSX == -1 ){
			scenedata.MovementLimitsSX = 0;			
		}
		if (scenedata.MovementLimitsSY == -1 ){
			scenedata.MovementLimitsSY = 0;			
		}
		if (scenedata.MovementLimitsEX == -1 ){
			scenedata.MovementLimitsEX = scenedata.InternalSizeX;			
		}
		if (scenedata.MovementLimitsEY == -1 ){
			scenedata.MovementLimitsEY = scenedata.InternalSizeY;			
		}

		//
*/
		dragPanelCore.setMovementLimits(scenedata.MovementLimitsSX,
				scenedata.MovementLimitsSY, scenedata.MovementLimitsEX,
				scenedata.MovementLimitsEY);

		Log.info(" setting styles ");
		dragPanelCore.setBackgroundColour(scenedata.containerBackground);


		dragPanelCore.setDraggableBackground(currentState.currentBackground);

		dragPanelCore.setDraggableBackgroundRepeat(scenedata.currentBackgroundRepeat);

		// sceneBackground.YMOVEMENTDISABLED = true;

		Log.info(" setting mouse over and scene position ");
		dragPanelCore.setMouseOverEventsOnOverlays(false);
		
		//setting onscroll end trigger
		dragPanelCore.setRunAfterScroll(new Runnable() {			
			@Override
			public void run() {
				firePostScrollActions();
			}

		});
	}

	
	/**
	 * should set a specific load message, if your not using setLoadingMessages
	 * @param message
	 */

	@Override
	public void setLoadingMessage(String message){
		dragPanelCore.setLoadingMessage(message);

	};	
	
	/**
	 * This will fire when the scene starts loading its data.
	 * Use this to display some messages on screen, such as a sequenced or random selection of text hints.
	 * 
	 */

	@Override
	public void setLoadingMessages(){;
		String[] messages= JamText.getTextForCurrentLanaguage(CommonText.StringNames.loadingStrings).split("\n");	
		dragPanelCore.setLoadingMessages(messages); //gwts loading messages are tied directly to load progress
	};

	
	/**
	 * development use only. Override this to provide a method to force any loading screens to close even if not all objects are loaded.
	 * 
	 */
	@Override
	public void forceCloseLoading() {
		super.forceCloseLoading();
		
		if (super.Loading){
			ScenesLog("!!!WARNING LOADING SCREEN WAS FORCE CLOSED WHILE INCOMPLETE!!!", "RED");
		}

		if (dragPanelCore.loadingOverlayAttached()){
			dragPanelCore.setLoading(false, "",null);
		}
		
		
	}

	
	
	@Override
	public void setScroll_imp(boolean xMovement, boolean yMovement) {

	//	dragPanelCore.XMOVEMENTDISABLED = !xMovement;
	//	dragPanelCore.YMOVEMENTDISABLED = !yMovement;
		
		dragPanelCore.setScrollDisabled( !xMovement,!yMovement );
		
	}

	/** Experimental method, should not be used under most situations.
	 * Currently being used to ensure object is removed before being reattached 
	 * into a dialogue box (that is, added to a HTMLPanel which is itself on
	 * the scene) **/
	public boolean removeFromSceneBackground(SceneObjectVisual object){
		Log.info("removing from scene background:"+this.getScenesData().currentState.SceneName);


		return dragPanelCore.removeWidget(object.getInternalGwtWidget());
	}




	/**
	 * Physically attach a object to the scene.
	 * This should not be called directly, but rather use addObjectToScene(..) in the parent class of this (That is SceneWidget)
	 * (NOTE: Ideally more from this method should be moved to the superclass)
	 */
	protected void attachObjectToSceneIMPL(SceneObject attachThis, int x, int y, int z) {
		//we cast to a SceneObjectVisual
		//as we are a GWT implementation ALL SceneObjects should be sceneobject visual.
		//So this is safe.
		//That said, maybe SceneObjectVisual should be a interface that SceneWidget can use in its call to this? That way we dont need to cast?
		SceneObjectVisual objectToAttach = (SceneObjectVisual)attachThis;
		//-----------------------
		//----------
		//----
		//--
		/*
		objectToAttach.ObjectsLog("adding object to scene", "green");


		int x = objectToAttach.objectsCurrentState.X;
		int y = objectToAttach.objectsCurrentState.Y;

		SceneObject relativeObject = objectToAttach.objectsCurrentState.positionedRelativeToo;

		// if its positioned relative to something else get its absolute x/y values 
		if (relativeObject != null ) {

			objectToAttach.ObjectsLog("_____adding object "+objectToAttach.objectsCurrentState.ObjectsName+" relatively:");
			objectToAttach.ObjectsLog("_____relative object is attached "+relativeObject.isAttached());
			objectToAttach.ObjectsLog("_____relative test "+relativeObject.objectsCurrentState.X);
			objectToAttach.ObjectsLog("_____source object is at: "+relativeObject.getX()+","+relativeObject.getY());

			x = x + relativeObject.getX() - objectToAttach.objectsCurrentState.PinPointX;
			y = y + relativeObject.getY() - objectToAttach.objectsCurrentState.PinPointY;

		}*/
		//(the above can be moved to the superclass)



		//normal dom attach to div
		if (objectToAttach.getObjectsCurrentState().attachToHTMLDiv.length() > 2) {

			Log.info("_____adding object:"
					+ objectToAttach.getObjectsCurrentState().ObjectsName + " on "
					+ this.SceneFileName + " in div "
					+ objectToAttach.getObjectsCurrentState().attachToHTMLDiv);

			try {

				objectToAttach.ObjectsLog("added object to div with id=" + objectToAttach.getObjectsCurrentState().attachToHTMLDiv, "yellow");

				//	RootPanel.get(objectToAttach.getObjectsCurrentState().attachToHTMLDiv).add(objectToAttach.getInternalGwtWidget());


				RequiredImplementations.PositionByTag(objectToAttach.getInternalGwtWidget(),objectToAttach.getObjectsCurrentState().attachToHTMLDiv);

				//reset the visibility as adding it changes it
				//objectToAttach.setVisibleSecretly(objectToAttach.getObjectsCurrentState().currentlyVisible);
				//objectToAttach.setOpacityImplementation(objectToAttach.getObjectsCurrentState().currentOpacity);

				//Visibility will,unfortunately, also set opacity, so we need to save the opacity....
				double opac = objectToAttach.getObjectsCurrentState().currentOpacity;
				objectToAttach.setVisibleSecretly(objectToAttach.getObjectsCurrentState().currentlyVisible);
				objectToAttach.setOpacityImplementation(opac);//....................................................then restore it here (while setting the visual representation of it)
				objectToAttach.getObjectsCurrentState().currentOpacity=opac;
				
				

				objectToAttach.ObjectsLog("display after adding to div:"+objectToAttach.getElement().getStyle().getDisplay());

			} catch (Exception e) {

				objectToAttach.ObjectsLog("no such div ID currently in Dom", "yellow");
				objectToAttach.ObjectsLog("was looking for:"+objectToAttach.getObjectsCurrentState().attachToHTMLDiv, "yellow");
				objectToAttach.ObjectsLog("object might be for a text object not yet present on the page", "yellow");

				Log.info("no such div ID currently in Dom");
				Log.info("was looking for:"+objectToAttach.getObjectsCurrentState().attachToHTMLDiv);
				Log.info("object might be for a text object not yet present on the page");

			}

		} else {

			Log.info("_____adding object:"
					+ objectToAttach.getObjectsCurrentState().ObjectsName + " on "
					+ this.SceneFileName + " at " + x + "," + y+","+z);

			//FAKE HEIGHT:
			//we subtrack the z from the y
			//this is because we are a 2d implementation and z represents height
			int effectiveY = y - z;

			//-----------

			//pre-add it to get the size known 
			//sceneBackground.addWidget(sceneObject, -1000, -1000);

			boolean disablefocus = true;

			if (objectToAttach.getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.Input){
				disablefocus=false;
			}

			//if no actions we add in a way that cant get clicks (more efficient)
			//if (sceneObject.objectsActions.size()==0){
			if (!objectToAttach.shouldBeInFocusPanel()){
				objectToAttach.ObjectsLog("Adding "+objectToAttach.getName()+" to scene "+this.SceneFolderLocation+" as UNclickable object");
				dragPanelCore.addUnclickableWidget(objectToAttach.getInternalGwtWidget(), x, effectiveY,disablefocus,objectToAttach.getObjectsCurrentState().ignorePointerEvents);

			} else {		
				objectToAttach.ObjectsLog("Adding "+objectToAttach.getName()+" to scene "+this.SceneFolderLocation+" as clickable object");

				//ELSE WE USE THE NORMAL WAY
				dragPanelCore.addWidget(objectToAttach.getInternalGwtWidget(), x, effectiveY,disablefocus,objectToAttach.getObjectsCurrentState().ignorePointerEvents);
			}


			//reset the visibility as adding it changes it
			//Visibility will,unfortunately, also set opacity to 1 or 0, so we need to save the opacity....
			double opac = objectToAttach.getObjectsCurrentState().currentOpacity;		
			objectToAttach.setVisibleSecretly(objectToAttach.getObjectsCurrentState().currentlyVisible);
			objectToAttach.ObjectsLog("display now set to:"+objectToAttach.getElement().getStyle().getDisplay());
			objectToAttach.setOpacityImplementation(opac);//then use what we saved
			objectToAttach.getObjectsCurrentState().currentOpacity=opac;
			

			if (objectToAttach.getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.DialogBox && objectToAttach.getObjectsCurrentState().restrictPositionToScreen){

				Log.info("_____restricting to scene");

				effectiveY = ((SceneDialogObject)objectToAttach).getSafeHeightAwayFromDialogues(x, effectiveY);


			}

			//make sure if its a dialogue then its safely positioned
			//first add it so we get the size
			Log.info("_____adding object at "+x+","+effectiveY);


			//now set the position
			objectToAttach.ObjectsLog("setting position to "+x+","+effectiveY);
			dragPanelCore.setWidgetsPosition(objectToAttach.getInternalGwtWidget(), x, effectiveY, false);



			//set z-index if its variable
			if (objectToAttach.getObjectsCurrentState().variableZindex){
				objectToAttach.setZIndexByPosition(y); //NOTE: we use original Y, not the one with the fake height built in
			}

		}
	}

	//optionally set up a visualizer
	//we might be able to move this to the Visualizer class itself in future
	//have a generic setup function there that the SceneWidget class can call without knowing what it does
	//not sure how it would add itself to the scene though
	public void setupCmapVisualiser() {


		if (scenesCmap.isPresent()){
			Log.info("________________________________________________________setting up setCMapVisualiser:");
			scenesCmap.get().setCMapVisualiser(new CollisionMapVisualiser());
		}

		Log.info("scenes Vmap set");

		//if theres a collision map, and a visualizer is present but not attached then we attach it
		//(we could also add a check to only do this on debugmode as well)
		if ((scenesCmap.isPresent()) 	&& (scenesCmap.get().CMapVisualiser.isPresent())) {


			//cast to the absolutepanel displaying the preview
			AbsolutePanel previewElement = (AbsolutePanel) scenesCmap.get().getCMapPreviewWidget();

			if (!previewElement.isAttached()){

				dragPanelCore.addWidget((AbsolutePanel)scenesCmap.get().CMapVisualiser.get().getCollisionMapPreviewWidget(), 0, 0); //.previewRoot
				Log.info("overlay added");

				// add svg doddle layer
				AbsolutePanel svgPathVisualisation = (AbsolutePanel)scenesCmap.get().CMapVisualiser.get().getSvgPathVisualisation();

				dragPanelCore.addWidget(svgPathVisualisation, 0, 0);

				svgPathVisualisation.setPixelSize(thisWidget.getOffsetWidth(),thisWidget.getOffsetHeight());
				svgPathVisualisation.getElement().getStyle().setZIndex(50000);

				scenesCmap.get().showCmap(false);
				scenesCmap.get().showPath(false);

			}

		}

	}

	public int getOffsetWidth() {
		return dragPanelCore.getOffsetWidth();
	}

	public int getOffsetHeight() {
		return dragPanelCore.getOffsetHeight();
	}


	/**
	 * Scrolls the scene to the position at the pixel speed specified
	 * 
	 * @param centerX
	 * @param centerY
	 * @param overrideMOVEMENTDISABLED
	 * 
	 * @return the co-ordinates requested, corrected if it was needed
	 */		
	public Simple2DPoint scrollViewToPositionImplemenation(int centerX, int centerY,int duration, boolean overrideMOVEMENTDISABLED) {

		String msg = "scrolling view to:"+centerX+","+centerY+" over duration "+duration;
		Log.info(msg);
		this.ScenesLog(msg, "green");
		//
		
		// temp
		
		//probably want to move to duration based, not speed based. Script doesnt use speed based much
	//	sceneBackground.scrollViewToPos(centerX, centerY,overrideMOVEMENTDISABLED,speed,animationSpeedInterpretationMode.FixedSpeedModeOn); //add steps
		
		//we now work by duration not speed
		return dragPanelCore.scrollViewToPos(centerX, centerY,overrideMOVEMENTDISABLED,duration,animationSpeedInterpretationMode.DirectDurationMode); //add steps

		//	currentState.PosX = centerX; //we update straight away to the requested x/y, we dont yet autoupdate the scrolls position as it moves
		//	currentState.PosY = centerY;
	}


	/**if -1,-1 is requested this is taken to mean centralize the scene. Real co-ordinates can only ever be positive as they refer to where the screen center should go**/
	public Simple2DPoint setViewPosition_implementation(int centerX, int centerY, boolean overrideMOVEMENTDISABLED) {

		ScenesLog("setting view to:"+ centerX+","+ centerY + "," +overrideMOVEMENTDISABLED);
		Log.info("setting view to:"+centerX+","+centerY);

		//if -1,-1 is requested this is taken to mean centralize the scene. 
		//Real co-ordinates can only ever be positive as they refer to where the screen *center* should go, thus using -1,-1 to represent center is safe
	//	if (centerX==-1 && centerY==-1){
		//	sceneBackground.setViewToCenter(overrideMOVEMENTDISABLED);		
			
		//	centerX = scenesData.getCenterOfMovementLimitsX();
		//	centerY = scenesData.getCenterOfMovementLimitsY();
			
			//(if this works we can move it to the superclass rather then using the -1,-1 if statement above
		//	sceneBackground.setViewToPos(centerX, centerY,overrideMOVEMENTDISABLED);
	//	} else {
		return	dragPanelCore.setViewToPos(centerX, centerY,overrideMOVEMENTDISABLED);
		//}

			//isn't this done in the superclass? pointless to repeat here
	//	currentState.PosX = centerX;
	//	currentState.PosY = centerY;


	}
	public void setViewToCenter(boolean overrideDisabled) {
		ScenesLog("setting view to center");
		Log.info("setting view to center");

		currentState.PosX = -1;
		currentState.PosY = -1;
		// temp
		dragPanelCore.setViewToCenter(overrideDisabled);


	}
	static public void resizeAllScenes() {

		Iterator<SceneWidget> sit = all_scenes.values().iterator();

		while (sit.hasNext()) {

			SceneWidgetVisual sceneWidget = (SceneWidgetVisual)sit.next();

			if (sceneWidget.isAttached()) {

				sceneWidget.dragPanelCore.updateDragableSize();

			}
		}

	}

	public boolean isAttached() {
		return dragPanelCore.isAttached();
	}

	public void resizeScene() {

		dragPanelCore.updateDragableSize(); //TODO:uncomment

		// reposition
		if (currentState.PosX == -1 && currentState.PosY == -1) {
			Log.info("setting "+SceneFileName+" view to center");
			ScenesLog("setting "+SceneFileName+" view to center");

			dragPanelCore.setViewToCenter(true);

		} else {

			Log.info("setting view to  "+currentState.PosX +","+ currentState.PosY);
			ScenesLog("setting view after resize to  "+currentState.PosX +","+ currentState.PosY);

			dragPanelCore.setViewToPos(currentState.PosX, currentState.PosY,true);
			// good god, without the ',true', it won't properly position a scene, when movement's disabled
		}

	}





	/** save all the objects on this scene
	 * Currently redirects to saving the whole game.
	 * There is no saveSceneState function here anymore. 
	 * You can, however, load and save temp states for fast "restore" functions in the game **/

	@Deprecated
	public void saveSceneState() {

		/*
		SceneObjectState[] allSData = getAllSceneObjectData();

		Log.info("_____________________________________________________saving scene state itself");

		// save the scenes own settings
		tempState = new SceneStatus(
				currentState.SceneName,
				currentState.PosX,
				currentState.PosY,
				currentState.NumOfTimesPlayerHasBeenHere,
				currentState.currentBackground, currentState.DynamicOverlayCSS,
				currentState.StaticOverlayCSS);

		Log.info("___________________________________________________background="
				+ tempState.currentBackground);
		 */
		// popup a window with the save state string
	//	SaveGameManager.display(); //these params are redundant and can be removed as now all the scenes are saved with this function

		RequiredImplementations.saveManager.get().display();

		//SaveStateManager.display(this);

	}







	/**
	 * replaced with getTextObjectNEW (that "NEW" may be removed after we know 100 % sure it works)
	 * @param name
	 * @return
	 */
	//	@Deprecated
	//	public static SceneDialogObject[] getTextObjectByName(String name) {
	//
	//		Log.info("getting-:" + name.toLowerCase());
	//
	//		SceneDialogObject[] objects;
	//
	//		if (name.equalsIgnoreCase("<LASTTEXTUPDATED>")) {
	//
	//			objects = new SceneDialogObject[] { InstructionProcessor.lastTextObjectUpdated };
	//
	//			return objects;
	//
	//			//redundant? this has already been checked if using "sceneobject by name"
	//		} else if (name.equalsIgnoreCase("<ALLOBJECTS>")) {
	//
	//			//Log.info("getting array lists");
	//
	//			//we combine by the text and the dialogue objects as they both count as "dialogue" types
	//			ArrayList<SceneDialogObject> scobjects = InstructionProcessor.currentScene.scenesData.SceneDialogObjects;
	//			ArrayList<SceneTextObject> textobjects = InstructionProcessor.currentScene.scenesData.SceneTextObjects;
	//			scobjects.addAll(textobjects);
	//
	//			//Log.info("got array list"
	//			//		+ InstructionProcessor.currentScene.SceneFileName);
	//
	//			//Log.info("got array list" + scobjects.size());
	//
	//			objects = scobjects.toArray(new SceneDialogObject[scobjects.size()]);
	//
	//			//Log.info("returning objects array list");
	//
	//			return objects;
	//
	//		} //else if (name.startsWith("<PROPERTY:")){
	//
	//		//	String propertyString = name.substring("<PROPERTY:".length(),name.length()-1);
	//
	//		//	Log.info("Getting all Text Objects with property:"+propertyString);
	//
	//		//we combine by the text and the dialogue objects as they both count as "dialogue" types
	//		//	ArrayList<SceneDialogObject> scobjects = InstructionProcessor.currentScene.scenesData.SceneDialogObjects;
	//		//	ArrayList<SceneTextObject> textobjects = InstructionProcessor.currentScene.scenesData.SceneTextObjects;
	//
	//		//	ArrayList<SceneObject> allObjectsWithText = new ArrayList<SceneObject>();
	//
	//
	//
	//		//	 return getObjectsWithProperty(propertyString,allObjectsWithText);
	//		//}
	//
	//		Log.info("returning text objects array list");
	//		Set<SceneDialogObject> resultingtextobjects = all_text_objects.get(name.toLowerCase());
	//
	//		objects = resultingtextobjects.toArray(
	//				new SceneDialogObject[resultingtextobjects.size()]);
	//
	//
	//
	//		return objects;
	//
	//	}
	//	/**
	//	 * Please use the new getVectorObjectNEW function
	//	 * 	  
	//	 * @param name
	//	 * @return
	//	 */
	//	@Deprecated
	//	public static SceneVectorObject[] getVectorObjectByName(String name) {
	//
	//		Log.info("getting-:" + name.toLowerCase());
	//
	//		SceneVectorObject[] objects;
	//
	//		if (name.equalsIgnoreCase("<LASTVECTORUPDATED>")) {
	//
	//			objects = new SceneVectorObject[] { InstructionProcessor.lastVectorObjectUpdated };
	//
	//			return objects;
	//
	//			//redundant? this has already been checked if using "sceneobject by name"
	//		} else if (name.equalsIgnoreCase("<ALLVECTOROBJECTS>")) {
	//
	//			Log.info("getting array lists");
	//
	//			ArrayList<SceneVectorObject> scobjects = InstructionProcessor.currentScene.scenesData.SceneVectorObjects;
	//
	//
	//			Log.info("got array list"
	//					+ InstructionProcessor.currentScene.SceneFileName);
	//
	//			Log.info("got array list" + scobjects.size());
	//
	//			objects = scobjects.toArray(new SceneVectorObject[0]);
	//
	//			Log.info("returning objects array list");
	//
	//			return objects;
	//
	//		}
	//
	//		Log.info("returning vector object from array list");
	//
	//
	//		Set<SceneVectorObject> matchingVectors = all_vector_objects.get(name.toLowerCase());
	//
	//
	//		if (matchingVectors==null){
	//			Log.info("no vector of that name found");
	//			Log.info("current known vector objects :"+all_vector_objects.size());
	//
	//			Log.info("current known vector objects are:"+all_vector_objects.keys().toString());
	//
	//		}
	//
	//		objects = matchingVectors.toArray(new SceneVectorObject[matchingVectors.size()]);
	//
	//
	//		return objects;
	//
	//	}
	//
	//	/**
	//	 * slowly and carefully replace with ""getSceneObjectNEW""
	//	 * @param name
	//	 * @param callingObject
	//	 * @return
	//	 */
	//	@Deprecated
	//	public static SceneObject[] getSceneObjectByName(String name, SceneObject callingObject) {
	//
	//		//These functions should be refractored so that 
	//		//variables are looked for first if they apply to any sceneobject type (ie, <LASTOBJECT> style stuff)
	//		//then sprites (including variables specific to sprites)
	//		//then text (including variables specific to text)
	//		//then divs (including ...you get the picture)
	//
	//		//test for generic types of objects
	//		if (name.equalsIgnoreCase("<LASTSCENEITEM>")) {
	//
	//			if (InstructionProcessor.lastSceneObjectClickedOn==null){
	//				Log.info("last scene item is null ");
	//				return null;
	//			}
	//
	//
	//			SceneObject[] objects = new SceneObject[] { InstructionProcessor.lastSceneObjectClickedOn };
	//			Log.info("last scene item found :"+InstructionProcessor.lastSceneObjectClickedOn.objectsCurrentState.ObjectsName);
	//
	//			return objects;
	//
	//		} else if (name.equalsIgnoreCase("<CHILDREN>")) {
	//			//return this objects children
	//			Log.info("returning objects <CHILDREN>:"+callingObject.relativeObjects.size());
	//			SceneObject[] children = callingObject.relativeObjects.toArray(new SceneObject[callingObject.relativeObjects.size()]);
	//			Log.info("number of <CHILDREN>="+children.length);
	//			return children;
	//
	//
	//
	//		} else if (name.startsWith("<PROPERTY:")) {
	//
	//			// <PROPERTY:Visible> 
	//			// <PROPERTY:Visible||active>		
	//			// <PROPERTY:"isVisible||active||(colour=green)">
	//
	//			String propertyString = name.substring("<PROPERTY:".length(),name.length()-1);
	//
	//			Log.info("Dont use this Getting all Objects with property:"+propertyString);
	//
	//			//we search for all the objects in the game with the specified property
	//			return null; //getObjectsWithProperty(propertyString,getAllGamesObjects());
	//
	//
	//		}
	//
	//
	//
	//
	//
	//		//AddClass = <PROPERTY:"isVisible||active||(colour=green)">,stuff
	//
	//		//test for specific types starting with sprites
	//
	//		SceneObject[] req = getSpriteObjectByName(name,callingObject,false);
	//
	//		if (req[0] == null) {
	//			Log.info("no sprite objects of name " + name
	//					+ " found, checking text...");
	//
	//			req = getTextObjectByName(name);
	//		}
	//		if (req[0] == null) {
	//			Log.info("no text objects of name " + name
	//					+ " found, checking divs...");
	//
	//			req = SceneWidget.getDivObjectByName(name);
	//		}
	//		if (req[0] == null) {
	//			Log.info("no divs objects of name " + name
	//					+ " found, checking vectors...");
	//
	//			req = getVectorObjectByName(name);
	//		}
	//
	//		if (req[0] == null) {
	//
	//			Log.info("no vector objects of name " + name
	//					+ " found. Ending checks");
	//
	//		}
	//
	//		if (req==null || req.length==0){			
	//
	//			Log.info("requested object not found :(");
	//
	//		}
	//		//Log.info("found:" + req.length + " matchs for "+name);
	//
	//		return req;
	//
	//	}
	//
	//	
	//
	//	/**
	//	 * This should slowly and carefully be replaced with getSpriteObjectNEW
	//	 * 
	//	 * @param name
	//	 * @param callingObject
	//	 * @return
	//	 */
	//	@Deprecated
	//	public static SceneSpriteObject[] getSpriteObjectByName(String name, SceneObject callingObject) {
	//		return getSpriteObjectByName(name, callingObject,true);
	//	}
	//	/**
	//	 * This should slowly and carefully be replaced with getSpriteObjectNEW
	//	 * 
	//	 * @param name
	//	 * @param callingObject
	//	 * @param used purely to log extra errors if no objects found for this name
	//	 * @return
	//	 */
	//	@Deprecated
	//	public static SceneSpriteObject[] getSpriteObjectByName(String name, SceneObject callingObject,boolean flagMissingerrors) {
	//
	//		Log.info("getting :" + name.toLowerCase());
	//
	//		SceneSpriteObject[] objects;
	//
	//		if (name.equalsIgnoreCase("<TOUCHER>")) {
	//
	//			objects = new SceneSpriteObject[] { InstructionProcessor.lastObjectThatTouchedAnother };
	//			Log.info("got touching object "
	//					+ InstructionProcessor.lastObjectThatTouchedAnother.ObjectsName);
	//
	//			return objects;
	//
	//		} else if (name.equalsIgnoreCase("<HELDITEM>")) {
	//
	//			//get inventory item
	//			InventoryIcon iitem = InventoryPanel.currentlyHeldItem; 
	//
	//			if (iitem==null){
	//				Log.info("no held item. Maybe the keepheld setting on the item is wrong?");
	//				return null;
	//			}
	//
	//
	//			if (iitem.associatedSceneObject!=null){
	//				String oname = iitem.associatedSceneObject;
	//				return getSpriteObjectByName(oname,null);
	//			} else {
	//				Log.info("associatedSceneObject is null");
	//				return null;
	//			}
	//
	//
	//		}  else if (name.equalsIgnoreCase("<LASTSPRITEITEM>")) {
	//
	//			if (InstructionProcessor.lastSpriteObjectClickedOn==null){
	//				Log.info("last scene item is null ");
	//				return null;
	//			}
	//
	//			objects = new SceneSpriteObject[] { InstructionProcessor.lastSpriteObjectClickedOn };
	//			Log.info("last scene item :"+objects.length);
	//
	//			return objects;
	//
	//		} else if (name.equalsIgnoreCase("<LASTINVENTORYITEM>")) {
	//
	//			if (InstructionProcessor.lastInventoryObjectClickedOn ==null){
	//				Log.info("last scene item is null ");
	//				return null;
	//			}
	//
	//			objects = new SceneSpriteObject[] { InstructionProcessor.lastInventoryObjectClickedOn };
	//			Log.info("last scene item :"+objects.length);
	//
	//			return objects;
	//
	//		} else if (name.equalsIgnoreCase("<LASTCLICKEDSCREATOR>")) {
	//			objects = new SceneSpriteObject[] { (SceneSpriteObject) InstructionProcessor.lastSpriteObjectClickedOn.objectsCurrentState.spawningObject };
	//
	//			return objects;
	//
	//		} else if (name.equalsIgnoreCase("<ALLOBJECTS>")) {
	//			Log.info("getting array list");
	//
	//			ArrayList<SceneSpriteObject> scobjects = InstructionProcessor.currentScene.scenesData.scenesSpriteObjects;
	//
	//			Log.info("got array list"
	//					+ InstructionProcessor.currentScene.SceneFileName);
	//
	//			Log.info("got array list" + scobjects.size());
	//
	//			objects = scobjects.toArray(new SceneSpriteObject[0]);
	//
	//			Log.info("returning objects array list");
	//
	//			return objects;
	//
	//		} 
	//
	//		Log.info("checking for sprite object named:"+name.toLowerCase()+": in list of size "+all_sprite_objects.size());
	//		Set<SceneSpriteObject> objectlist = all_sprite_objects.get(name.toLowerCase());
	//		objects = objectlist.toArray(new SceneSpriteObject[objectlist.size()]);
	//
	//		//error if none found
	//		if (flagMissingerrors && objectlist.size()==0){
	//
	//			Log.severe("OBJECT MISSING ERROR:"+name+" not found");
	//			for (SceneSpriteObject sceneSpriteObject : all_sprite_objects.values()) {				
	//				Log.info("List contained:"+sceneSpriteObject.ObjectsName);
	//			}
	//
	//			callingObject.ObjectsLog.log("OBJECT MISSING ERROR:"+name+" not found","RED");
	//			Window.alert("OBJECT MISSING ERROR:"+name+" not found, are you trying to trigger a command incompatible with the object type? (ie, a SetState on a TextObject?)");
	//
	//		}
	//
	//
	//		return objects;
	//
	//	}

	/** test if something is on the scenes background  panel
	 * 
	 * (note; will only detect widgets added in the traditional way, that are thus contained by a focus panel ) **/

	private boolean isObjectOnPanel(SceneObjectVisual testThis){


		return dragPanelCore.isOnPanel(testThis.getInternalGwtWidget());



	}

	/** gets an object in the scene by its name
	 * should be phased out - use the similiarly named NEW method instead with search global set to false **/
	//	public SceneObject getSpriteObjectInSceneByName(String name) {
	//
	//		Iterator<SceneSpriteObject> objectIT = scenesData.scenesSpriteObjects
	//				.iterator();
	//
	//		while (objectIT.hasNext()) {
	//
	//			SceneObject sceneObject = objectIT.next();
	//
	//			if (sceneObject.objectsCurrentState.ObjectsName.equalsIgnoreCase(name)) {
	//				return sceneObject;
	//			}
	//		}
	//		return null;
	//
	//	}

	public void setDynamicSceneOverlay(String CSS) {

		Log.info("adding dynamic overlay with css:" + CSS);

		dragPanelCore.setDynamicOverlayCSS(CSS);
	}

	public void setStaticSceneOverlay(String CSS) {

		Log.info("adding static overlay with css:" + CSS);

		dragPanelCore.setStaticOverlayCSS(CSS);
	}

	
	//override purely to give some loading feedback on the loading tool tip
	@Override
	protected void testForLoadComplete() {

		if (JAMcore.DebugMode){
			String title = "ObjectsPhysicallyLeftToLoad:"          + ObjectsPhysicallyLeftToLoad+"\n \n"+
						   "ObjectsLogicallyLeftToLoad:" + ObjectsLogicallyLeftToLoad +"\n \n";
		
			dragPanelCore.setLoadingTitleMessage(title);
		}
		
		super.testForLoadComplete();


	}
		

	
	
	@Override
	public void onSceneFullyLoaded() {
		super.onSceneFullyLoaded();
		//remove the overlay, as we are now ready for the player to see things!
		dragPanelCore.setLoading(false, "",InternalAnimations.getAnimation("loading"));
		
	}



	/**
	 * This should fire when all objects and data are loaded, and the objects are attached to the page
	 */
	@Override
	public void onObjectLoadFullyComplete() {
		super.onObjectLoadFullyComplete();
						
		//we can now setup the visualizer of the collision map (assuming there is one)
		setupCmapVisualiser(); 	
		
		// wait a brief time to give the browser time to catch up (didnt seem to help size issues)
		//Timer waitabit = new Timer() {
		//	@Override
		//	public void run() {
				// remove loading message

				// set foreground off loading
				//---
				/*
				ScenesLog("Random size test object 0v :"+allScenesCurrentObjects.get(0).getName()+":"+allScenesCurrentObjects.get(0).getObjectHeight()+","+allScenesCurrentObjects.get(0).getObjectWidth());
				ScenesLog("Random size test object 1i :"+allScenesCurrentObjects.get(1).getName()+":"+allScenesCurrentObjects.get(1).getObjectHeight()+","+allScenesCurrentObjects.get(1).getObjectWidth());
				ScenesLog("Random size test object 2i :"+allScenesCurrentObjects.get(2).getName()+":"+allScenesCurrentObjects.get(2).getObjectHeight()+","+allScenesCurrentObjects.get(2).getObjectWidth());
				ScenesLog("Random size test object 3v :"+allScenesCurrentObjects.get(3).getName()+":"+allScenesCurrentObjects.get(3).getObjectHeight()+","+allScenesCurrentObjects.get(3).getObjectWidth());
				ScenesLog("Random size test object 4i :"+allScenesCurrentObjects.get(4).getName()+":"+allScenesCurrentObjects.get(4).getObjectHeight()+","+allScenesCurrentObjects.get(4).getObjectWidth());
				 */
		
			//just moved to fully loaded (so we wait for states)_
			//	dragPanelCore.setLoading(false, "",InternalAnimations.getAnimation("loading"));
				
				
				/*
				ScenesLog("loading scene complete", "green");
				
				// check if it should be moved to the front, as long as this was the one we were waiting for
				if (JAMcore.triggerSelectCheck) {

					if (JAMcore.pageToSelect.startsWith(SceneFileName.toLowerCase())){

						Log.info("____________________________________________>> setting page too :"		+ JAMcore.pageToSelect + ":");

						InstructionProcessor.bringSceneToFront(JAMcore.pageToSelect,loadingsilently);

					}

				}

				//if we are already on this scene (and have been watching it load) we check for scene Debut commands
				if (SceneObjectDatabase.currentScene == thisWidget){
					Log.info("testing for scene debut after load");
					//testForSceneDebut(); //old
					if (!loadingsilently){
						onSceneMadeCurrent(); //new...test all scenes
					}
					//should we use 	newScene.loadingsilently?

				}*/

		//	}

		//};

		//waitabit.schedule(500);


		//recheck positions (I don't think this is needed, when loading at first shouldn't it know all the positions before the objects are placed?)
		//recheckRelativeandSpawnedObjects(); //newly added, used to be just for states but really it should check normal loads too in case the order is wrong in the file

/*
		//we now check relative and spawned objects as well as update touching data
		//this can only be done after everything is laded
		if (!StateToLoad){
			ScenesLog("recheckRelativeandSpawnedObjectsThenUpdateTouchingData","blue");
			recheckRelativeandSpawnedObjectsThenUpdateTouchingData(); //catchy name eh?
		} //we only check if there's no state to load as the state also runs this same check and we only want it run once

		//Note; we replaced the bottom commented out stuff with a method to check ALL scenes for states to load
		//not just this one.
		//This is because some scenes might have been waiting for this one.
		SceneWidget.checkAllScenesStillWaitingToLoadStates();
*/
		/*
		if (StateToLoad) {
			Log.info("_____________________________________________________________Loading states");
			ScenesLog("Loading state: "+sceneStateToLoad.seralise());


			loadSceneStateInternal(sceneStateToLoad, objectsCurrentStateToLoad);

			Log.info("rechecking all relatively positioned objects to make sure they are linked and correctly positioned");
			recheckRelativeandSpawnedObjects();



		} else {
			ScenesLog("Loaded scene and no state to load");

			Log.info("__(no state to load)__");
		}*/


	}
	
	int AdditionalLoading =0; //sprites images and preloaded images typically add to this total

	public boolean addToLoading(String LoadID) {
		boolean successfullyadded = super.addToLoading(LoadID);
		
		if (successfullyadded){
			AdditionalLoading++;
			updateLoadingVisualTotal();
			return true;
		} else {
			Log.info(LoadID+" was already set to load, so not increaseing total");		
			return false;
		}
	}



	public void updateLoadingVisualTotal() {
		//update the loading total, which is ObjectsPhysicallyLeftToLoad   +   item data still processing
		
		//int totalLoadUnits = ObjectsPhysicallyLeftToLoad.size()+ this.getScenesData().getTotalKnowenItems();
		
		
		int totalLoadUnits = AdditionalLoading+this.getScenesData().getTotalKnowenItems(); //Note; can be higher then item total, as sometimes theres extra images to load too
		
		setLoadingTotal(totalLoadUnits);
	}
	
	/**
	 * total representational/visual units of loading for this scene
	 */
	public void setLoadingTotal(int totalLoadUnits) {

		if (totalLoadUnits<getScenesData().getTotalKnowenItems()){
			totalLoadUnits = getScenesData().getTotalKnowenItems(); //never go under the total number of items!
		}
		Log.info("setting loading total to:"+totalLoadUnits+" on "+this.SceneFolderLocation);
		
		dragPanelCore.setLoadingTotal(totalLoadUnits);
	}
	
	/**
	 * Used purely to track loading progress for use bars/pie-charts/other visualizations for the enduser.
	 * This should not be used for any real functional things.
	 *  
	 * we use a float so we can represent partial progress of object loading
	 */
	float curentLoadProgress  = 0.0f; 
	
	public void stepLoadingTotalVisual(float steps){
		curentLoadProgress=curentLoadProgress+steps;
		//Log.severe("stepping loading forward");
		//dragPanelCore.stepLoading();// .setLoadingTotal(sub);
		setLoadingTotalProgress(curentLoadProgress);
	}

	/**
	 * sets the total visual progress reflection of  loading
	 */
	public void setLoadingTotalProgress(float loadedSofar) {	
		
		int loadingRoundedUp = (int) Math.ceil(loadedSofar);
		
		
		
		
		dragPanelCore.setLoadingProgress(loadingRoundedUp);
		
	}
	

	//we used to advance the visual loading here, but now its handled separately
	public void advancePhysicalLoading(String LoadIDToRemove) {
		super.advancePhysicalLoading(LoadIDToRemove); 

		if (Loading) {

		//	ObjectsLeftTooLoad = ObjectsLeftTooLoad - 1;

		//	Log.info("_____________________________________________________"+this.SceneFileName+"_  Left to load="+ ObjectsLeftTooLoad);

			//dragPanelCore.stepLoading(); //removed as expiremwnt (onfullyloaded now fires this for all objects)

	//		testForLoadComplete();

		} else {
		//	Log.info("not currently loading");
		}
	}

	public void setBackgroundUrlImplementation(String url) {
		//currentState.currentBackground = this.SceneFolderLocation + "/" + url; (supertype deals with this)
		dragPanelCore.setDraggableBackground(currentState.currentBackground);
	}

	public void setDebugBoxVisible(boolean b) {

		dragPanelCore.DisplayDebugBar(b);

	}

	/**
	 * Edits just the specified widget
	 ***/
	public void startEditingWidget(boolean b, final SceneObjectVisual EditThis) {

		editMode = b;

		dragPanelCore.EditMode(b, EditThis.getInternalGwtWidget());

		if (EditThis != null) {
			
			dragPanelCore.setOnFinishedEditingWidget(new Runnable() {
				@Override
				public void run() {

					// make sure the position is updated correctly
					// EditThis.setPosition(EditThis.getAbsoluteLeft(),
					// EditThis.getAbsoluteTop());
					
					
					if (!editInZAxis){
						
						Log.info("Setting position to:"
								+ EditThis.getAbsoluteLeft() + ","
								+ EditThis.getAbsoluteTop() + ","
								+ EditThis.getZ());

						int newX2d = EditThis.getAbsoluteLeft()
								- dragPanelCore
								.getCurrentPanelAbsoluteX();

						int newY2d = EditThis.getAbsoluteTop()
								- dragPanelCore
								.getCurrentPanelAbsoluteY();

						int fakez = EditThis.getTopLeftBaseZ(); //total fake height

						/**
						 * Note; we add the fakez back onto the 2d position.
						 * This is because the position your dragging it relative to the screen already has had its verticle position subtracked (ie "moved up" as z is zero at the top of the screen)
						 * We thus add that z position back to get the real 3d y position 
						 * (note; this only works as this is a 2d fake of 3d games - either isometrix, or 3/4. It wouldnt work with anything with real perspective forshortening)
						 */
						EditThis.updatePositionData(newX2d, newY2d  + fakez, fakez); //as we are dragging in screen space we need to add the z to give the new real y

						Log.info("position data updated");
						
						//if its normally positioned relatively we should also recalculate the relative position
						//we do this by looking at the new position, and comparing it to what its being positioned against.
						if ( EditThis.getObjectsCurrentState().positionedRelativeToo!=null){
							
							//pin of relative object (what if we are positioning by a specific glu point?
							//TODO: check for positioning relative to glupoint?
							
							int PivRelX = newX2d  + EditThis.getObjectsCurrentState().CurrentPinPoint.x;
							int PivRelY = (newY2d+fakez) + EditThis.getObjectsCurrentState().CurrentPinPoint.y;
							int PivRelZ = fakez + EditThis.getObjectsCurrentState().CurrentPinPoint.z;

							EditThis.ObjectsLog("positioning relative too:"+PivRelX+","+PivRelY+","+PivRelZ);
							
							//Difference between that pin and the object we are movings pin							
							int NewRX = PivRelX - EditThis.getObjectsCurrentState().positionedRelativeToo.getX();
					    	int NewRY = PivRelY - EditThis.getObjectsCurrentState().positionedRelativeToo.getY();							
							int NewRZ = PivRelZ - EditThis.getObjectsCurrentState().positionedRelativeToo.getZ();

							EditThis.ObjectsLog(" relative at:"+NewRX+","+NewRY+","+NewRZ);
							
						//	int NewRX = newX2d           - EditThis.getObjectsCurrentState().positionedRelativeToo.getX();
						//	int NewRY = (newY2d + fakez) - EditThis.getObjectsCurrentState().positionedRelativeToo.getY();							
						//	int NewRZ = fakez - EditThis.getObjectsCurrentState().positionedRelativeToo.getZ();

							Log.info("updating relative position variables");
							EditThis.updateRealtivePositionData(NewRX,NewRY,NewRZ); //not sure if this is right
						}


					} else {

						//(difference we moved in y since turning editheight on?)
						
						//dragStartY - 
						
						int oldy2d = editInZStartPos; 
						
						int newY2d = EditThis.getAbsoluteTop()
								- dragPanelCore
								.getCurrentPanelAbsoluteY();
						
						int displacement = (newY2d-oldy2d);
						
						Log.info("Displacement was:"+displacement);						
						
						if ( EditThis.getObjectsCurrentState().positionedRelativeToo!=null){
							//relative
							Log.info("changing relative height to::"+(newY2d-oldy2d));
							//absolute (we only change z so we copy the original values back in)
							int rx =  EditThis.getObjectsCurrentState().relX;
							int ry =  EditThis.getObjectsCurrentState().relY;
							int rz =  EditThis.getObjectsCurrentState().relZ - displacement;
							
							
							EditThis.updateRealtivePositionData(rx,ry,rz); //not sure if this is right
							
						} else {

							Log.info("changing height to::");
							
							//absolute (we only change z so we copy the original values back in)
							int x = EditThis.getObjectsCurrentState().X;
							int y = EditThis.getObjectsCurrentState().Y;
							int z = EditThis.getObjectsCurrentState().Z - displacement ;

							Log.info("changing height to::"+z);
							
							EditThis.updatePositionData(x,y, z); //as we are dragging in screen space we need to add the z to give the new real y

						}

						//shift might still be held down, so this needs updating
						
						editInZStartPos = newY2d;
						
						
						
						
						

					}

					Log.info("updateThingsPositionedRelativeToThis:");
					//lastly we move any objects that position themselves relative to this
					//note; this seems a bit slow and needs some testing to find out why
					//relatively positioned objects seem to be moved at a decent framerate when not
					//dragging - why does this slow it?
					EditThis.updateThingsPositionedRelativeToThis(true);
					
					//refresh cmap if theres one
					if (scenesCmap.isPresent()){
						if (scenesCmap.get().CMapVisualiser.isPresent()){
							scenesCmap.get().CMapVisualiser.get().generatePreviewWidget();
						}
					}

				}

			});
		}
	}



	/** in case we need to listen for events ontop of the cmap 
	 * this should be used for editing only.
	 * It places a div focus panel at zindex 150000 ontop of the scene in order to work
	 * **/
	public HandlerRegistration setCmapClickHandler(ClickHandler clickHandler) {

		//if the click detection layer isnt there, then add it
		if ((scenesCmap.isPresent()) && (!previewClickPanel.isAttached())) {	


			Log.info("cmap editing requested - adding previewClickPanel");
			
			dragPanelCore.addWidget(previewClickPanel, 0, 0);
			previewClickPanel.setSize("100%", "100%");

			//	previewClickPanel.getElement().getParentElement().getStyle().setWidth(value, unit)

			previewClickPanel.getElement().getStyle().setOverflow(Overflow.VISIBLE);

			previewClickPanel.setPixelSize(thisWidget.getScenesData().InternalSizeX,
					thisWidget.getScenesData().InternalSizeY);

			//apply zindex to previewClickPanel or the widget that wrapped it if the dragpanel decided to do that.
			if (dragPanelCore.isWidgetInContainer(previewClickPanel)){			
				previewClickPanel.getElement().getParentElement().getStyle().setZIndex(150000);
			} else {
				previewClickPanel.getElement().getStyle().setZIndex(150000);
				
			}
			//--
			
			//add handlers to stop propergation of events
			previewClickPanel.addMouseDownHandler(new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {

					Log.info("preventing default mouse down");

					event.stopPropagation();
					event.preventDefault();


				}
			});
			previewClickPanel.addMouseUpHandler(new MouseUpHandler() {
				@Override
				public void onMouseUp(MouseUpEvent event) {

					Log.info("preventing default mouse up");

					event.stopPropagation();
					event.preventDefault();


				}
			});
		}

		Log.info("cmap editing requested - adding click handler");
		return previewClickPanel.addClickHandler(clickHandler);



	}

	public void removeCmapClickHandler() {
		if ((scenesCmap.isPresent()) && (previewClickPanel.isAttached())) {	

			dragPanelCore.removeWidget(previewClickPanel);

		}

	}

	/** 
	 * shakes the camera screen a bit. For fun! 
	 * 
	 * @param duration - duration in ms
	 * @param distance - number of pixels +/- in both dimensions
	 */
	public void shakeView(int duration, int distance)
	{
		ScenesLog("shaking the screen for:"+duration+" over distance:"+distance, "Pink");		
		dragPanelCore.shakeFor(duration, distance);

	}

	public boolean wasJustDragged() {
		return dragPanelCore.wasJustDragged();
	}

	/**
	 * should be run when a key is pressed down
	 * This is because we detect some keystrokes for editing
	 * @param keycode 
	 */
	public void testForKeyDown(int keycode){

		if (editMode){
			if (keycode == KeyCodes.KEY_SHIFT){
				setAsEditingHeight(true);
				
			}			
		}			
	}
	
	public void testForKeyUp(int keycode){
		
		if (editMode){
			if (keycode == KeyCodes.KEY_SHIFT){
				setAsEditingHeight(false);				
			}	
			//ensure any nudge events correctly on key release
			dragPanelCore.testForNudgeKeyUp(keycode); 

			
		}			
	}
	public void setAsEditingHeight(boolean state){
		if (state){
			
			if (!editInZAxis){
				editInZAxis = true; //
				Log.info("(edit height mode on)");
				
				editInZStartPos = dragPanelCore.dragOnlyThis.getAbsoluteTop()- dragPanelCore.getCurrentPanelAbsoluteY();
			}
			//also restrict visual movement on background to just vertical?
			dragPanelCore.setEditingDragRestriction(SpiffyDragPanel.DragRestriction.Vert);
			
			
		} else {

			Log.info("(edit height mode off)");
			if (editInZAxis){
				editInZAxis = false; 
				editInZStartPos = 0;
			}
			//unrestrict backgrounds movement
			dragPanelCore.setEditingDragRestriction(SpiffyDragPanel.DragRestriction.None);
			
		}
	}
	
	public void testForKeyPress(int keycode){//NativePreviewEvent event) {
		//nudge keys are ones that move the edited objects left/right/up/down in screen space
		dragPanelCore.testForNudgeKeyPressed(keycode); 

		
	}

	public int getCurrentPanelAbsoluteX() {
		return dragPanelCore.getCurrentPanelAbsoluteX();
	}
	public int getCurrentPanelAbsoluteY() {
		return dragPanelCore.getCurrentPanelAbsoluteY();
	}
	//	/**
	//	 * This will search over all the games objects and return any with the specifies property
	//	 * 
	//	 * You can also search for multiple propertys by seperation with a || for OR
	//	 * ie
	//	 * 'Visible||Active' would return any object with the property visible or active
	//	 * 
	//	 * You can even do full semantic query searchs by using quotes
	//	 * '"((Colour=Green)||(Colour=Red))&&(Fruit)"'
	//	 * Would search for either red or green fruit.
	//	 * 
	//	 * NOTE: when doing semantic searchs it will returns "things which are" not the words themselves.
	//	 * "Apple"
	//	 * would return a object with the property "Granny Smith"
	//	 * But not one with the property "Apple"
	//	 * 
	//	 * @param propertyString
	//	 * @return
	//	 */
	//	public static SceneObject[] getObjectsWithProperty(String propertyString,ArrayList<SceneObject> searchThese) {
	//	
	//	
	//		//if we start with a quote then we are a semantic query and we deal with that seperately
	//		if (propertyString.startsWith("\"")){
	//			String query = propertyString.substring(0,propertyString.length()-1);
	//			Log.info("Getting all Objects with semantic query:"+query);
	//			//getAllObjectsWithPropertysMatchingSemanticQuery();
	//			//return
	//		}
	//		//We use an arraylist to start as we dont know how many objects we need in the end
	//		ArrayList<SceneObject> results = new ArrayList<SceneObject>();
	//	
	//		//split propertyString by ||
	//		String properties[] = propertyString.split("||");
	//	
	//		Log.info("looking for "+properties+" properties.");
	//	
	//		//loop over all the objects we have been asked to search
	//		for (SceneObject sceneObject : searchThese) {
	//	
	//			//check if they have any of the properties asked for
	//			for (String property : properties) {
	//				//if they do add it to the results list
	//				if (sceneObject.hasProperty(property)){				
	//					results.add(sceneObject);
	//				}
	//	
	//			}
	//		}
	//	
	//	
	//		//results need to be converted to an array
	//		SceneObject[] resultsArray = results.toArray(new SceneObject[results.size()]);
	//	
	//		Log.info("found "+resultsArray.length+" objects with property:"+propertyString);
	//	
	//		return resultsArray;
	//	}

	/** Adds a widget to the position x,y in the drag panel the widget is first added to a container, however, to ensure clicks and drags are handled correctly You can specify if focus is disabled or not. By default its true, which means images 
	 * wont be selected and textboxs wont work 
	 * 
	 * @param internalGwtWidget
	 * @param x
	 * @param y
	 */
	public void addWidget(Widget internalGwtWidget, int x, int y) {		
		dragPanelCore.addWidget(internalGwtWidget, x, y);		
	}

	/** 
	 * Adds a widget to the position x,y in the drag panel the widget is first added to a container, however, to ensure clicks and drags are handled correctly You can specify if focus is disabled or not. By default its true, which means images 
	 * wont be selected and textboxs wont work 
	 * 
	 * @param internalGwtWidget
	 * @param x
	 * @param y
	 * @param disableFocus (stops it being selectable by the browser)
       @param TransparentToClicks - adds "pointer-events:none" to the style so clicks pass though (note; will prevent editing!)
	 */
	public void addWidget(Widget internalGwtWidget, int x, int y,boolean disableFocus,boolean TransparentToClicks) {		
		dragPanelCore.addWidget(internalGwtWidget, x, y,disableFocus, TransparentToClicks);		
	}

	public void clearJustDraggedFlag() {
		dragPanelCore.clearJustDraggedFlag();		
	}

	public void removeWidget(Widget internalGwtWidget) {
		dragPanelCore.removeWidget(internalGwtWidget);

	}

	private boolean isOnPanel(Widget GwtWidget) {		
		return dragPanelCore.isOnPanel(GwtWidget);
	}


	public Widget getParent() {
		return dragPanelCore.getParent();
	}




	/*
	/** 
	 * The keep visible parameters ensures its position is kept within the screen limits, and in the case
	 * of dialogues not overlapping with an existing dialogue box.
	 * NOTE: it does not ensure its visible from a z-index or visibility standpoint. 
	 * 
	 * @param object - the sceneobject to place (has to be a SceneObjectVisual of some sort, as it will be cast to that in order to get its GWT widget)
	 * @param x - the x position! 
	 * @param y - the y position!
	 * @param keepvisible (see above)
	 * 
	 *  	
	@Override
	public void setObjectsPosition(SceneObject object, int x, int y,boolean keepvisible) {

		setObjectsPosition((SceneObject)object,  x,  y, keepvisible);

	}
*/

	/** at the moment requires an unchecked cast, not sure how to get around it **/
	@Override
	public boolean isObjectInScene(SceneObject object) {
		return this.isObjectOnPanel((SceneObjectVisual)object);
	}



	@Override
	public void ScenesLog(String logthis) {
		ScenesLog.log(logthis);	
	}

	@Override
	public void ScenesLog(String logthis,String colour) {
		ScenesLog.log(logthis,colour);	
	}


	/**
	 * Sets up the handlers for background actions on the scene.
	 * This can get kinda complex. Sorry.
	 * The most important part is handling the mouseup events, which might trigger Background click actions.
	 * These background click actions should only fire if no item is interacted with.
	 * <br>
	 * We also have to handle some stuff for touch events - specifically to remember where the last touchstart or touchmove happened, so any TouchEnd events (the equilivent of MouseUp)
	 * knows where the last clicks location was. We need to do this as TouchEnd, unlike MouseUp, has no idea where the cursor is in the event information itself. 
	 */
	private void setupBackgroundTouchHandlers() {
		
	
		
		
		
		//----------------------------------------------------------------------------------------------------
		//New method. We apply the the background event actions to a special background widget in the panel. This widget is at the lowest z-index, right behind everything else
		//Its specially for the purpose of adding default interactions if nothing else was hit.
		//Applying it to the panels "real background" made event capturing/propergation too complex and messy.
		//-----------------------------------------------
		
		dragPanelCore.getBackgroundWidget().addTouchEndHandler(new TouchEndHandler() {
			@Override
			public void onTouchEnd(TouchEndEvent event) {

				boolean currentDragPanelEventCameFromItem = dragPanelCore.currentDragPanelEventCameFromItem();
				Log.info("_____________________________touchEnd detected on background"+currentDragPanelEventCameFromItem);
				
				if (editMode) {
					return;
				}
				
				//leftclickortouch shouldn't run if the scene is being dragged, nor the click visualizer
				if (dragPanelCore.wasJustDragged()){
					return;
				}			
				
				//we ONLY fire these events if it was not from a item (ie, it really was on a background)
				if (currentDragPanelEventCameFromItem){
					return;//do nothing return if not from item
				}
				
				//note; we cant get the mouse position from the event as theres no figures on the screen thus
				//the getTouchs is empty - this behaviors different from them mouseup action
				//therefor we get the x / y from the last ones changed or touched
				
				int screenx = LastTouchMoveScreen.getClientX();
				int screeny = LastTouchMoveScreen.getClientY();

				Log.info("__________________________________________________LastTouch up screen = "+screenx+","+screeny);
				
				int rx = LastTouchMoveScreen.getRelativeX(dragPanelCore.dragableContents
						.getElement());
				int ry = LastTouchMoveScreen.getRelativeY(dragPanelCore.dragableContents
						.getElement());
				
				updateLastClickedLocation(rx, ry, screenx, screeny);
				
				//int rx = LastTouchMoveScreen.getRelativeX(sceneBackground.dragableContents.getElement());
				//int ry = LastTouchMoveScreen.getRelativeY(sceneBackground.dragableContents.getElement());
				Log.info("______________________________________________________________LastTouch up scene = "+rx+","+ry);

						
				
				clickVisualiser.showClickVisauliserImplementation(screenx,screeny);

				leftclickortouch();

			}
		});
		dragPanelCore.getBackgroundWidget().addTouchMoveHandler(new TouchMoveHandler() {		
			@Override
			public void onTouchMove(TouchMoveEvent event) {

				boolean currentDragPanelEventCameFromItem = dragPanelCore.currentDragPanelEventCameFromItem();
				Log.info("_____________________________onTouchMove detected on background. Current event came from item;"+currentDragPanelEventCameFromItem);
			
				LastTouchMoveScreen = event.getTouches().get(0);		

			}
		});

		dragPanelCore.getBackgroundWidget().addTouchStartHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {

				boolean currentDragPanelEventCameFromItem = dragPanelCore.currentDragPanelEventCameFromItem();
				Log.info("_____________________________onTouchStart detected on background. Current event came from item;"+currentDragPanelEventCameFromItem);
			
				if (!ObjectInspector.debugIsOpen()){
					event.preventDefault();
				}

				LastTouchMoveScreen = event.getTouches().get(0);
				
				//we ONLY fire these events if it was not from a item (ie, it really was on a background)
				if (currentDragPanelEventCameFromItem){
					return;//do nothing return if not from item
				}
			}
		});
		
		dragPanelCore.getBackgroundWidget().addTouchCancelHandler(new TouchCancelHandler() {			
			@Override
			public void onTouchCancel(TouchCancelEvent event) {
				// nothing?
			}
		});
		
		dragPanelCore.getBackgroundWidget().addMouseDownHandler(new MouseDownHandler() {					
			@Override
			public void onMouseDown(MouseDownEvent event) {

				boolean currentDragPanelEventCameFromItem = dragPanelCore.currentDragPanelEventCameFromItem();
				Log.info("_____________________________onMouseDown detected on background. Current event came from item;"+currentDragPanelEventCameFromItem);
			
				//prevent a right click if the debug is not open
				if (!ObjectInspector.debugIsOpen()){
					event.preventDefault();
				}
				
				//we ONLY fire these events if it was not from a item (ie, it really was on a background)
				if (currentDragPanelEventCameFromItem){
					return;//do nothing return if not from item
				}
			}
		});

		dragPanelCore.getBackgroundWidget().addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {

				boolean currentDragPanelEventCameFromItem = dragPanelCore.currentDragPanelEventCameFromItem();
				Log.info("_____________________________MouseUp detected on background. Current event came from item;"+currentDragPanelEventCameFromItem);
				
				//prevent a right click if the debug is not open
				if (!ObjectInspector.debugIsOpen()){
					event.preventDefault();
				}
				
				//we ONLY fire these events if it was not from a item (ie, it really was on a background)
				if (currentDragPanelEventCameFromItem){
					return;//do nothing return if not from item
				}
							
				if (editMode) {
					return;
				}

				//this shouldn't run if the scene is being dragged.
				if (dragPanelCore.wasJustDragged()){
					return;
				}

				//give click feedback
				int screenx = event.getClientX();
				int screeny = event.getClientY();
				
				//
				clickVisualiser.showClickVisauliserImplementation(screenx,screeny);
				
				//
				int rx = event.getRelativeX(dragPanelCore.dragableContents
						.getElement());
				int ry = event.getRelativeY(dragPanelCore.dragableContents
						.getElement());
				
				updateLastClickedLocation(rx, ry, screenx, screeny);

				//Deal with background actions (both left and right);
				//
				if (event.getNativeButton()==NativeEvent.BUTTON_LEFT){
					leftclickortouch();
				};				
				if (event.getNativeButton()==NativeEvent.BUTTON_RIGHT){
					rightclickortouch();
				};
				//------

			}
		});
		

		//TODO: doubleclick support on background?
	}


	@Override
	public void setLoadingBackground() {
		dragPanelCore.setLoadingBackground(LoadingBackground); //Doesn't seem to be used, LoadingBackground is never set to anything
	}


	
	
	

	@Override
	public ArrayList<SceneObject> getObjectsMouseIsOver(Collection<SceneObject> checkTheseObjects) {
		
		int cx = CurrentScenesVariables.lastclicked_x;
		int cy = CurrentScenesVariables.lastclicked_y;
		int cz = CurrentScenesVariables.lastclicked_z;
		
		
		int effective2DY = cy-cz; //we need to get the position of the click relative to the 2d corner of the scene in browser-space
		//as cy reflects only the position of the click on the lowest point of the scene, we subtract cz which reflects the hieght of the click
		//TODO: the above will be unneeded if "testIfWithinObject" is upgraded to support true 3d positioning
		
		ArrayList<SceneObject> mouseIsOver = new ArrayList<SceneObject>();
		
		//look over supplied objects and find which the mouse hits, then return that
		for (SceneObject so : checkTheseObjects) {
			
			//ignore invisible ones
			if (!so.isVisible()){
				continue;
			}
			
			Log.info("testing if "+cx+","+effective2DY+" is over "+so.getName());
			boolean isWithin  = so.testIfMouseWouldHit(cx, effective2DY);
			if (isWithin){
				Log.info("(Which it was)");
				mouseIsOver.add(so);
			}
			
			
		}
		//-------
		
		return mouseIsOver;
	}



	public boolean isWidgetInContainer(Widget widget) {		
		return this.dragPanelCore.isWidgetInContainer(widget);
	}



	@Override
	public int getScenePanelSizeX() {
		return this.dragPanelCore.getContainerSizeX();
	}

	@Override
	public int getScenePanelSizeY() {
		return this.dragPanelCore.getContainerSizeY();
	}



	
	public void abortCurrentPan() {
	
		this.dragPanelCore.abortCurrentPan();
		
	}


	

	//	@Deprecated
	//	public static SceneDivObject[] getDivObjectByName(String name) {
	//	
	//		Log.info("getting-:" + name.toLowerCase());
	//	
	//		SceneDivObject[] objects;
	//	
	//		if (name.equalsIgnoreCase("<LASTDIVUPDATED>")) {
	//	
	//			objects = new SceneDivObject[] { InstructionProcessor.lastDivObjectUpdated };
	//	
	//			return objects;
	//	
	//			//redundant? this has already been checked if using "sceneobject by name"
	//		} else if (name.equalsIgnoreCase("<ALLDIVOBJECTS>")) {
	//	
	//			Log.info("getting array lists");
	//	
	//			ArrayList<SceneDivObject> scobjects = InstructionProcessor.currentScene.scenesData.SceneDivObjects;
	//	
	//	
	//			Log.info("got array list"
	//					+ InstructionProcessor.currentScene.SceneFileName);
	//	
	//			Log.info("got array list" + scobjects.size());
	//	
	//			objects = scobjects.toArray(new SceneDivObject[0]);
	//	
	//			Log.info("returning objects array list");
	//	
	//			return objects;
	//	
	//		}
	//	
	//		Log.info("returning div object from array list");
	//	
	//	
	//		Set<SceneDivObject> matchingDivs = all_div_objects.get(name.toLowerCase());
	//	
	//	
	//		if (matchingDivs==null){
	//			Log.info("no div of that name found");
	//			Log.info("current knowen divs :"+all_div_objects.size());
	//	
	//			Log.info("current knowen divs are:"+all_div_objects.keys().toString());
	//	
	//		}
	//	
	//		objects = matchingDivs.toArray(new SceneDivObject[matchingDivs.size()]);
	//	
	//	
	//		return objects;
	//	
	//	}

}
