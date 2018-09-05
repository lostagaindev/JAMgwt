package com.lostagain.JamGwt.JargScene;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.CurrentScenesVariables;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Movements.MovementWaypoint;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.AttachmentList;
import com.lostagain.Jam.SceneObjects.InventoryObjectState;
import com.lostagain.Jam.SceneObjects.SceneDialogueObjectState;
import com.lostagain.Jam.SceneObjects.SceneDivObjectState;
import com.lostagain.Jam.SceneObjects.SceneInputObjectState;
import com.lostagain.Jam.SceneObjects.SceneLabelObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.SceneSpriteObjectState;
import com.lostagain.Jam.SceneObjects.SceneVectorObjectState;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneDivObject;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneVectorObject;
import com.lostagain.Jam.SceneObjects.Interfaces.hasUserActions;
import com.lostagain.JamGwt.GWTJAMTimerController;
import com.lostagain.JamGwt.clickVisualiser;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryItem;
import com.lostagain.JamGwt.JargScene.debugtools.GameDataBox;
import com.lostagain.JamGwt.JargScene.debugtools.ObjectInspector;
import com.lostagain.JamGwt.audio.GwtAudioController;

import lostagain.nl.spiffyresources.interfaces.IsSpiffyGenericLogger;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyLogBox;

import java.util.HashSet;


/** <br>
 * A GWT implementation of SceneObject<br>
 * <br>
 * Normally this isn't used directly but extended into a <br>
 * another type of SceneObject like a SpriteObject, DialogueObject, DivObject etc.<br>
 * <br>
 * This constructor method is then called with super(  ) in order to set up the universal
 * functions/properties that need to be set on it. <br>
 * <br>
 * You should call assignObjectTypeSpecificParameters(ObjectsParamatersFromFile); straight after running this constructor
 * to assign the specific parameters for the subtype<br>
 * 
 * NOTE: We are still transitioning to this GWT-implementation (see old notes below)
 * Eventually this class will be the bare minimum to implement SceneObject in a GWT enviroment, with all extra html-specific functions going into SceneDivObject (ie, styleing)
 * 
 * **/

/**
 * SceneObject is the base type of all visual elements that can appear on a scene.<br>
 * Currently its a focus panel - effectively a div that can receive events.<br>
 * <br>
 * If the JAM is to be converted to support non-GWT visual components in future, <br>
 * SceneObject will have to be separated into two classes<br>
 * <br>
 * SceneObject - will control all the sceneobject data, its "state", its position but wont control its visuals in any way. This will be pure JAVA with no GWT elements at all.
 *<br>
 * SceneObjectVisual - will be the visual GWT reflection of the above. It will be initiate a  focus panel like it is right now, and control all the visual side. It will extend the above and override any functions that will effect things to trigger its own visual implementation.
 * <br>
 * Things that previously extended SceneObject would need to then extend SceneObjectVisual<br>
 * Once this separation is made we can then have other non-GWT visual stuff<br>
 *<br>
 * eg.<br>
 * SceneObject3DGL<br>
 * SceneObjectLibGDX<br>
 * or whatever<br>
 * These could be for non-browser games<br>
 * 


IMPORTANT STEP TO CHECK SOON (DONE!);
-----------------------------
All GWT JAM objects now extend SceneDivObject, which itself extends SceneObjectVisual
That way Div (and hence HTML) specific features can be put into SceneDivObject, and then a interface for those features can be made.
It makes sense to be structured this way as all objects are HTML objects, so they should all inherit their features.

Soon  direct references to SceneDivObject should be replaced by a isSceneDivObject interface


-----------
NOTE: Eventually only other GWT objects should know about SceneObjectVisual.
Almost everything should instead uses SceneObject

...



 * 
 * @author Tom
 *
 */
public abstract class SceneObjectVisual extends SceneObject implements hasUserActions{


	/** The SOLogs to the console. Subclass's (such as SceneSpriteObject) have their own log object simply called...Log **/
	public static Logger SOLog = Logger.getLogger("JAM.SceneObjectVisual");

	//Object Inspector - for debugging use only
	public ObjectInspector oipu = null;

	/**internal log which can be seen in the object inspector only **/
	public IsSpiffyGenericLogger ObjectsLog = SpiffyLogBox.createLogBox(JAMcore.DebugMode);



	boolean BEINGDRAGED = false;


	int dragStartX = 0, dragStartY = 0;
	// ----




	/** scene this object is on 
	 * NOTE: When SceneWidget is separated into GWT/Non-GWT specific bits, this variable can be moved to SceneObject**/
	public SceneWidgetVisual objectScene;

	/** In order to handle touchscreens we must constantly monitor the x/y touch position.
	 * This is because the getTouches array doesn't contain anything when the TouchEnd event is fired, 
	 * yet the game code needs to know where that TouchEnd happened **/
	static private Touch LastTouchMoveScreen = null;	


	SceneObjectVisual thisobject = this;








	//	enum FadeMode {
	//		None,FadeIn,FadeOut
	//	}
	//	FadeMode currentFade = FadeMode.None;



	//	Timer animationRunner; //animation now dealt with by the centralized JAMTimer system
	//AnimationHandle animationRunner; //new experimental one
	//AnimationCallback animationUpdateCode;
	//Date now;
	//Date updateTime;
	//float AnimationUpdateInterval = 80; //the min number of ms between frames. Beyond this the browser will decide when to update the screen now

	//-------------------------------------------------------------------------------------------------------------------------------------
	//Stuff below relates to wrapping the internal FocusPanel It lets code use SceneObjectVisual more or less like it extends FocusPanel
	//-------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------

	MyFocusPanel internalFocusPanel = new MyFocusPanel(this);




	//we need a custom focus panel class so we can use stuff like onLoad to trigger events on SceneObjects
	public class MyFocusPanel extends FocusPanel {
		SceneObjectVisual parentObject;

		public MyFocusPanel(SceneObjectVisual parentObject) {
			this.parentObject=parentObject;
		}		

		public SceneObject getParentSceneObject(){
			return parentObject;
		}

		/** Convince. Does a unsafe cast assuming the SceneObjectVisual this is linked too is a InventoryIcon. 
		 * This is here to avoid horrible double casting in InventoryPanel **/
		public InventoryItem getParentSceneObjectAsInventoryIcon(){
			return (InventoryItem) parentObject;
		}

		@Override
		public void onLoad() {
			super.onLoad();
			parentObject.onLoad();
		}		

		@Override
		public void onBrowserEvent(Event event) {
			super.onBrowserEvent(event);
			parentObject.onBrowserEvent(event);
		}


	}

	public com.google.gwt.user.client.Element getElement() {
		return internalFocusPanel.getElement();

	}
	public Widget getParent() {		
		return internalFocusPanel.getParent();

	}
	public Widget getWidget() {		
		return internalFocusPanel.getWidget();

	}
	public Widget getInternalGwtWidget() {		
		return internalFocusPanel;

	}
	public int getOffsetWidth() {
		return internalFocusPanel.getOffsetWidth();
	}

	public int getOffsetHeight() {
		return internalFocusPanel.getOffsetHeight();
	}
	public int getAbsoluteLeft() {
		return internalFocusPanel.getAbsoluteLeft();
	}

	public int getAbsoluteTop() {
		return internalFocusPanel.getAbsoluteTop();
	}

	public void setWidget(Widget widget) {
		internalFocusPanel.setWidget(widget);
	}
	public void setHeight(String height){		
		internalFocusPanel.setHeight(height);		
	}
	public void setWidth(String width){		
		internalFocusPanel.setWidth(width);
	}
	public void setSize(String width,String height){		
		internalFocusPanel.setSize(width, height);		
	}
	public void clear(){		
		internalFocusPanel.clear();	
	}



	/**
	Overridden to defer the call to super.sinkEvents until the first time this widget is attached to the dom, as a performance enhancement. Subclasses wishing to customize sinkEvents can preserve this deferred sink behavior by putting their implementation behind a check of isOrWasAttached(): 

		 @Override
		 public void sinkEvents(int eventBitsToAdd) {
		   if (isOrWasAttached()) {
		     // customized sink code goes here 
		   } else {
		     super.sinkEvents(eventBitsToAdd);
		  }
		} 
		Overrides: sinkEvents(...) in UIObject
		Parameters:

		eventBitsToAdd a bitfield representing the set of events to be added to this element's event set

		//sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
	//		| Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
	//		| Event.ONTOUCHSTART | Event.ONTOUCHEND | Event.ONTOUCHCANCEL|Event.ONTOUCHMOVE);
	 **/
	public void sinkEvents(int eventBitsToAdd){
		internalFocusPanel.sinkEvents(eventBitsToAdd);
	}


	public void setVisibleInternalFocusPanel(boolean status) {
		internalFocusPanel.setVisible(status);

	}
	public void removeFromParent() {
		internalFocusPanel.removeFromParent();		
	}

	public void setTitle(String title){
		internalFocusPanel.setTitle(title);		
	}

	public void setStyleName(String stylename){
		internalFocusPanel.setStyleName(stylename);		
	}
	public void setStylePrimaryName(String stylename){
		internalFocusPanel.setStylePrimaryName(stylename);		
	}
	public void addStyleName(String stylename){
		internalFocusPanel.addStyleName(stylename);		
	}
	public void setStyleDependentName(String styleSuffix,boolean status){
		internalFocusPanel.setStyleDependentName(styleSuffix, status);	

	}
	public String getStyleName(){
		return internalFocusPanel.getStyleName();		
	}

	public  void removeStyleName(String stylename){
		internalFocusPanel.removeStyleName(stylename);
	}

	@Override
	public boolean isAttached() {
		return internalFocusPanel.isAttached();
	}

	//we need a hock to this function from SceneObject
	public boolean isVisible() {
		return internalFocusPanel.isVisible();		
	}

	//--------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	//----------------------------------------
	//----------------------
	///--------------
	///Now on with the non--focus panel wrapper related stuff;
	///-------------
	//----------------------
	//-----------------------------------------
	//-----------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------

	//This used to be onAttach, now its onload to correctly support variable zindexs

	//@Override	

	/**
	 *  As soon as the object is attached (and thus potentially visible) this function should run.<br>
	 *  
	 *  This function will tell the SceneObject its "physically loaded"
	 *  for some objects that are still loading images, like sprites, you should override this so it doesnt happen automatically.
	 *  And instead fire 
		setAsPhysicallyLoaded();	
		once both the attachment happens AND the initial image is loaded
	 **/
	public void onLoad() {

		String sceneName = "[no scene]";
		if (this.getParentScene()!=null){
			sceneName = this.getParentScene().SceneFileName;
		}
		
		this.ObjectsLog("--gwt onload (scene object attached to "+sceneName+")");
		//	super.onLoadFunctions(); //no longer used
		setAsPhysicallyLoaded();	
		

	}

	@Override
	protected void onFullyLoaded() {		
		this.getParentScene().stepLoadingTotalVisual(0.5f);		
	}
	
	
	/**
	 * 
	 * @param state
	 */
	public void setEditModeOn(boolean state) {


		if (currentEditedObject!=null){

			//can't edit inventory icons so we ignore
			if (currentEditedObject.getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.InventoryObject){
				return;
			}

		}

		if (state) {

			editMode = true;
			currentEditedObject = this;
			// activate on dragpanel
			((SceneWidgetVisual)currentEditedObject.getParentScene()).startEditingWidget(true, this);

		} else {
			editMode = false;

			if (objectScene!=null){
				objectScene.startEditingWidget(false, this);
			}

			currentEditedObject = null;

		}
	}

	
	//public SceneObjectVisual(SceneObjectState subclassStateObject,String rawParameterAndActionData,SceneWidget sceneItsOn) {
	//	this( subclassStateObject, splitActionsFromParametersAndActions(rawParameterAndActionData), sceneItsOn);
	//}


	/** <br>
	 * A GWT implementation of SceneObject<br>
	 * <br>
	 * Normally this isn't used directly but extended into a <br>
	 * another type of SceneObject like a SpriteObject, DialogueObject, DivObject etc.<br>
	 * <br>
	 * This constructor method is then called with super(  ) in order to set up the universal
	 * functions/properties that need to be set on it. <br>
	 * <br>
	 * You should call assignObjectTypeSpecificParameters(ObjectsParamatersFromFile); straight after running this constructor
	 * to assign the specific parameters for the subtype<br>
	 * 
	 * **/
	public SceneObjectVisual(SceneObjectState subclassStateObject,ActionList actions,SceneWidget sceneItsOn) {		
		super(actions,subclassStateObject,sceneItsOn);

		//start the log
		ObjectsLog.log("started object creation..");
		ObjectsLog.settimer();

		//setObjectsSceneVariable(sceneItsOn); //redundancy?

		if (subclassStateObject==null){			
			SOLog.severe("ERROR:subclassStateObject not supplied to SceneObjectVisual constructor");						
			return;
		}

		//remove as might not be needed;
		//	setObjectsCurrentState(subclassStateObject); //currently a test for new construction system

		//all objects should adopted SceneInputsObjects method 
		//this lets the SceneObjectVisuals objectsCurrentState variable be correctly assigned right away
		//which will allow it to handle more of the parameter creation/assignment tasks
		//as well as setting the action variable.
		//Note; In future it might be better to change object creation almost completely
		//rather then creating the data first and then the object
		//Instead, just find out what object we are making, then create the object and its data together

		//SOLog.info("extracting input data from:\r" + rawParameterAndActionData); <-----
		/*
			// split actions off if present
			// get scene parameters (anything before a line with : in it)
			int firstLocOfColon = rawParameterAndActionData.indexOf(':');

			SOLog.info("LocOfColon recieved for input  object:" + firstLocOfColon);

			//String Parameters = "";
			String allactions = "";
			if (firstLocOfColon == -1) {
				//Parameters = rawParameterAndActionData;
			} else {
				int linebeforeColon = rawParameterAndActionData.lastIndexOf('\n', firstLocOfColon);
			//	Parameters = rawParameterAndActionData.substring(0, linebeforeColon);
				allactions = rawParameterAndActionData.substring(linebeforeColon);

			}


		//	assignObjectTypeSpecificParameters(ObjectsParamatersFromFile);
			//so it can deal with the actions as well

			// add the actions
			if (allactions.length() > 0) {
				//Log.info("actions are:" + allactions);

				objectsActions = new ActionList(allactions);
				updateHandlersToMatchActions();
				SOLog.info("actions loaded");

			}
		 */






		//operas method to prevent selection
		this.getElement().setAttribute("unselectable", "on");

		SOLog.info("SceneObjectVisual construction done");

	}
	/*
	public void setPosition(int x, int y) {

		if (objectsCurrentState.positionedRelativeToo == null) {

			setPosition(x, y, true, true);

		} else {

			objectsCurrentState.relX = x;
			objectsCurrentState.relY = y;

			updateRelativePosition();
		}

	}

	public void updateRelativePosition() {



		int ox = this.objectsCurrentState.relX;
		int oy = this.objectsCurrentState.relY;


		thisobject.setPosition(ox, oy, true, false);


	}
	 */
	//now in supertype
	/*
	public void setPosition(int x, int y, boolean byPin,
			boolean ifOverLappingBringToFront) {


		ObjectsLog.log("positioning:"+x+","+y+" bypin="+byPin+" pin is:"+objectsCurrentState.PinPointX+","+objectsCurrentState.PinPointY);

		//if its positioned by Div, we ignore all this.
		//position by div must be removed before any other movements are possible
		if (this.objectsCurrentState.attachToHTMLDiv.length()>1){

			ObjectsLog.log("cant position by location, as its set to a DIV","#FF0000" );

			return;

		}


		//GreySOLog.info("setting position:" + x + "," + y);

		if (byPin) {


			objectsCurrentState.X = x - objectsCurrentState.PinPointX;
			objectsCurrentState.Y = y - objectsCurrentState.PinPointY;

		} else {

			objectsCurrentState.X = x;
			objectsCurrentState.Y = y;

		}

		// if its positioned relatively	
		if (objectsCurrentState.positionedRelativeToo != null) {

			ObjectsLog.info("positioning relatively to:"+objectsCurrentState.positionedRelativeToo.folderName);

			// objectsData.relX = objectsData.X;
			// objectsData.relY = objectsData.Y;

			final String posTo = this.objectsCurrentState.positionedRelativeToPoint;
			int RX=0;
			int RY=0;
			if (posTo.length()==0){
				//get by the pin
				RX = objectsCurrentState.positionedRelativeToo.getX();
				RY = objectsCurrentState.positionedRelativeToo.getY();
			} else {
				SOLog.info("positioning to:"+posTo);

				//get by the attachment point
				MovementWaypoint glueToo = objectsCurrentState.positionedRelativeToo.getAttachmentPointsFor(posTo);

				if (glueToo!=null){

					RX = objectsCurrentState.positionedRelativeToo.getTopLeftX()+ glueToo.x;
					RY = objectsCurrentState.positionedRelativeToo.getTopLeftY()+ glueToo.y;

				} else {
					//we have to default to pin as there's no attachment of the specified name
					RX = objectsCurrentState.positionedRelativeToo.getX();
					RY = objectsCurrentState.positionedRelativeToo.getY();	
				}


			}

			ObjectsLog.info("positioning relatively to:"+objectsCurrentState.X+","+objectsCurrentState.Y+" + "+RX+","+RY);

			objectsCurrentState.X = objectsCurrentState.X + RX;
			objectsCurrentState.Y = objectsCurrentState.Y + RY;

		}
		if (ifOverLappingBringToFront) {

			//GreySOLog.info("setZIndexByPosition");
			setZIndexByPosition(objectsCurrentState.Y);
		}

		// GreySOLog.info("y="+y+",-"+objectsData.PinPointY);
		// GreySOLog.info("x="+x+",-"+objectsData.PinPointX);

		//	GreySOLog.info("setWidgetsPosition of: "+objectsData.ObjectsName);
		ObjectsLog.log(" x="+ objectsCurrentState.X+", y="+objectsCurrentState.Y+" restrict to screen="+objectsCurrentState.restrictPositionToScreen );

		setPositionOnItsScene( objectsCurrentState.X, objectsCurrentState.Y,objectsCurrentState.restrictPositionToScreen );

		if (oipu != null) {
			oipu.update();
		}

		//GreySOLog.info("updateThingsPositionedRelativeToThis:");
		updateThingsPositionedRelativeToThis();

	}

	 */
	//
	//
	//	/**
	//	 * Gets the co-ordinate of the specified attachment point at the current frame.
	//	 * NOTE: Objects which can have multiple frames, such as sprite animations, should override this
	//	 * to use  points.getPointsFor(atachmentPointName, currentFrame); instead 
	//	 * **/
	//	@Override
	//	public MovementWaypoint getAttachmentPointsFor(String atachmentPointName) {
	//
	//		AttachmentList points = this.attachmentPoints;
	//
	//		if (points!=null){
	//			int currentFrame =0;
	//			//if its a sprite, we check the frame! Else, we assume zero.
	//			if (this.getObjectsCurrentState().getPrimaryObjectType()==SceneObjectType.Sprite){
	//				//currentFrame = this.getAsSprite().SceneObjectIcon.getCurrentframe();	//faster?				
	//				currentFrame = this.getAsSprite().getObjectsCurrentState().currentFrame;				
	//				SOLog.info( "getting glu point for frame:"+currentFrame);
	//			}
	//			return points.getPointsFor(atachmentPointName, currentFrame);
	//
	//		} else {
	//			SOLog.info( "no attachment points found for :"+atachmentPointName+"(0)");
	//		}
	//
	//		return null;
	//	}




	/** gets the scene this object is on.
	 * eventually this should be moved to SceneObject once everything that uses it only needs  SceneWidget and not SceneWidgetVisual  **/
	//public SceneWidget getParentScene() {
	//	return objectScene;
	//}




	/** All the various things to run when any external interaction happens
	 * This is like from the browser, mostly stuff like clicks, drags, keyboard actions. Anything the browser can receive.
	 * 
	 * **/
	//@Override
	public void onBrowserEvent(Event event) {
		//super.onBrowserEvent(event); //experiment

		//	SOLog.info("____________SceneObject:"+this.folderName+" onBrowserEvent fired");

		//if we are a inventory item, its important to call super and nothing else
		if (getObjectsCurrentState().getPrimaryObjectType()==SceneObjectType.InventoryObject){

			//	SOLog.info("nope, my mistake, was inventory event");	
			//REMOVED DUE TO THIS CLASS NOW HAVING ITS OWN FOCUS PANEL
			//super.onBrowserEvent(event);
			return;
		}

		// if editing, we basically ignore all actions. So all mouse clicks. So it only responds to actions on the editor.
		if (editMode) { //umm maybe when editing this should be before the prevent default, to ensure the parent dragpanel still gets events?		
			SOLog.info("____________SceneObject :"+this.getName()+" being edited and onBrowserEvent :"+event.getType()+"fired.(we thus do nothing, as no actions should run during editing");
			editingObjectEvents(event); 		// allowing dragging to move object (now handled by spiffydragpanel, this function really does nothing atm)
			return;
		}


	//	SOLog.info("____________SceneObject:"+this.getName()+" onBrowserEvent fired:"+event.getType()+". Preventing bubbling to parent");


		//-----------------------------------------------------------------------------------------------------------------------
		//we only stop propergating now for Click/UP/OUT/Cancel events if we wernt just dragging
		//
		//we do this because the parent element of this object - the dragpanel - will need to receive down/start/move in order to correctly handle dragging.
		//it may then, itself, cancel the event even trigger a setCapture, depending on whats going on.
		if ( (!objectScene.wasJustDragged()) && (
				DOM.eventGetType(event) == Event.ONCLICK    ||
				DOM.eventGetType(event)	== Event.ONMOUSEUP  ||
				DOM.eventGetType(event)	== Event.ONTOUCHEND ||
				DOM.eventGetType(event)	== Event.ONTOUCHCANCEL 	)			
				)
		{ 
			event.cancelBubble(true);// This will stop the event from being propagated
			event.stopPropagation();// This will stop the event from being propagated
			DOM.eventCancelBubble(DOM.eventGetCurrentEvent(), true);// This will stop the event from being propagated
		}
		//-------------------------------------------------------------------------------------------------------------------------


		//prevents all default actions when the debug is not open. Mostly for right-mouse clicks.
		//when debug is open we allow right clicks so people can access the chrome inspector/firebug (etc)
		if (!ObjectInspector.debugIsOpen()){
			event.preventDefault();
			DOM.eventPreventDefault(event);
		}



		/**
		 * 
		 * So I should remember this, but I probably don't so here:
		 * With the switch statement, all events (so clicks and stuff) are sent through and it tumbles down until it
		 * Recognizes the event triggered. Then it runs the commands and exists the list.
		 * 
		 * So don't forget the break, otherwise it will just keep on tumbling. That would be dumb.
		 * 
		 * Also, this is pretty clever because in the previous lines we removed all default browser behavior.
		 * Below, we specify what we really want it to do instead.
		 * 
		 */

		switch (DOM.eventGetType(event)) {

		case Event.ONCLICK:			
			// Tom specified a separate command here because really, onclick and ontouch *should* do the same thing. 
			onMouseOrTouchClick(event);
			break;

		case Event.ONTOUCHMOVE:
			LastTouchMoveScreen = event.getTouches().get(0);	
			break;

		case Event.ONTOUCHSTART:			
			SOLog.info("____________Event.ONTOUCHSTART");
			LastTouchMoveScreen = event.getTouches().get(0);	//remember location as we might need it
			objectScene.clearJustDraggedFlag();			
			break;

		case Event.ONMOUSEDOWN:

			SOLog.info("____________Event.ONMOUSEDOWN on "+this.getName()+" + set capture ...does background click fire now? it shouldn't");
			objectScene.clearJustDraggedFlag();

			break;

		case Event.ONTOUCHEND:


			SOLog.info("_________Event.ONTOUCHEND ");

			onMouseOrTouchClick(event);

			break;
		case Event.ONMOUSEUP:

			SOLog.info("____________Event.ONMOUSEUP");

			//-------------------------
			//DOM.releaseCapture(this.getElement());//TEMP TEST //what if we never get the mouse up? (ie, mouse moves off - add to mouse out as well?)
			//-------------------
			SOLog.info("_____________________________MouseUp detected on object "+this.getName()+"...does background click fire now? it shouldn't!");



			//this shouldn't run if the mouse up was caused by the end of a drag
			//if (objectScene.sceneBackground.wasJustDragged()){
			//	return;
			//}		
			/*
			if (DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
				SOLog.info("_________Event.BUTTON_LEFT ");
				if (actionsToRunForMouseClick != null) {
					SOLog.info("_________actionsToRunForMouseClick "
							+ actionsToRunForMouseClick);
				}
			}

			if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT) {
				SOLog.info("____________Event.BUTTON_RIGHT");

			}*/
			break;
		case Event.ONDBLCLICK:
			SOLog.info("________________Event.ONDBLCLICK");
			triggerDoubleClickOnObject();

			break;
		case Event.ONCONTEXTMENU:
			triggerContextClickOnObject();
			break;
		case Event.ONMOUSEOUT:
			// SOLog.info("_________________Event.ONMOUSEOUT");
			// run object actions
			if (actionsToRunForMouseOut != null) {

				wasLastObjectUpdated();

				updateLastClickedLocation();

				InstructionProcessor.processInstructions(
						actionsToRunForMouseOut.CommandsInSet.getActions(), "mou_"
								+ getObjectsCurrentState().ObjectsName, this);

			}
			break;
		case Event.ONMOUSEOVER:
			// SOLog.info("_________________Event.ONMOUSEOVER");
			// run object actions
			if (actionsToRunForMouseOver != null) {

				wasLastObjectUpdated();

				updateLastClickedLocation();

				InstructionProcessor.processInstructions(
						actionsToRunForMouseOver.CommandsInSet.getActions(), "mov_"
								+ getObjectsCurrentState().ObjectsName, this);

			} else {
				// SOLog.info("_________________no actions found for mouse over");
			}
			break;
		default:
			break; // Do nothing
		}// end switch
	}


	private void onMouseOrTouchClick(Event event) {

		SOLog.info("_________________Event.BUTTON_Click ");

		if (objectScene.wasJustDragged()){
			return;
		}		

		// if debug on and control key down
		if (event.getCtrlKey()) {

			// JAM.Quality.equalsIgnoreCase("debug")
			if (JAMcore.DebugMode){
				openObjectsInspector();
			}
			return;
		}

		triggerClickOnObject(true);


		//give click feedback. clicker is screen relative, not scene relative. It also appears above anything else
		if ((Event.getCurrentEvent().getTypeInt() & Event.TOUCHEVENTS)!=0){

			//touch events use the last known move or touch down
			//this is because touchend doesnt have known co-ordinates
			clickVisualiser.showClickVisauliserImplementation(LastTouchMoveScreen.getClientX(), LastTouchMoveScreen.getClientY());


		} else {
			//use the events x/y
			clickVisualiser.showClickVisauliserImplementation(event.getClientX(), event.getClientY());
		}


	}


	/** creates the animation timer object, that controls the objects
	 * movement across the scene

	 *		NOT LONGER used; we use delta time now!*/
	/*
	protected void createAnimationTimer() {

		/*
		animationRunner = new Timer() {

			boolean telePortFlag;


			//keep track of pixel rounding errors
			double remainderX = 0;
			double remainderY = 0;

			// double curveTime = 0;
			// double curveTimeStep = 0.01;

			@Override
			public void run() {

				movementFrameUpdate(this,remainderX,remainderY,telePortFlag);

			}
		};
	 */

	//Template for new animation method;
	//Note; No framerate in itself, just calls to redraw when the time difference is beyond the framerate
	//animationRunner = AnimationScheduler.get().requestAnimationFrame(

	//now = new Date();
	//	updateTime = new Date();
	/*
		animationUpdateCode = new AnimationCallback() {
			double lasttime = System.currentTimeMillis();
			@Override
			public void execute(double timestamp) {

				//	SOLog.info("****** AnimationCallBack Triggered! asdf");
				if (!isMoving()) {
					return;					
				}

			//	now = new Date();

				double delta = timestamp-lasttime;
				lasttime = timestamp;

				//new delta based update
				movementFrameUpdate(delta);

				/*
				if (now.getTime() - updateTime.getTime() > AnimationUpdateInterval) {
					//updateGeneration();
					movementFrameUpdate(this,AnimationUpdateInterval );
					updateTime = now;
				} */
	/*

				if (isMoving()){
					AnimationScheduler.get().requestAnimationFrame(this,getElementOnPanel());
				}

			}
		};



	}
	 */






	public void openObjectsInspector() {
		// create a inspector popup


		SOLog.info("testing for debug");
		if (oipu == null) {

			oipu = new ObjectInspector(this);

		}

		SOLog.info("updating debug");
	//	oipu.update();
		oipu.OpenDefault();

		// highlight object
		this.setSelectedCSS(true);

		// activate dragpanels debug box if we have a scene
		//(InventoryIcon types that extend SceneSpriteObject dont have a scene)
		if (objectScene!=null){
			objectScene.setDebugBoxVisible(true);
		}
	}



	public void clearObjectsInspector() {

		if (oipu!=null){
			oipu.CloseDefault();
			oipu=null; //oipu stands for object inspector pop up
		}

	}


	private void editingObjectEvents(Event event) {
		//GreySOLog.info("_________________editing object");
		//SOLog.info("_________________event type: " + event.getTypeInt());

		switch (DOM.eventGetType(event)) {

		case Event.ONCLICK:

			SOLog.info("editing setup...");

			//
			// // flag drag! (hope for no lag!)
			// SOLog.info("mousedown on panel...");
			// // remove any existing panels being edited
			// if (currentEditedObject != null) {
			// currentEditedObject.setEditModeOn(false);
			// this.setEditModeOn(true);
			// }
			// SOLog.info("start editing scene object ...");
			// // store current location relative to object
			//
			// int x = Event.getCurrentEvent().getClientX();
			// int y = Event.getCurrentEvent().getClientY();
			//
			// dragStartX = x - this.getElement().getAbsoluteLeft()
			// + this.getElement().getScrollLeft()
			// + this.getElement().getOwnerDocument().getScrollLeft();
			// //+ thisobject.objectsData.X;
			//
			// dragStartY = y - this.getElement().getAbsoluteTop()
			// + this.getElement().getScrollTop()
			// + this.getElement().getOwnerDocument().getScrollTop();
			// //+ thisobject.objectsData.Y;
			//
			// DOM.setCapture(this.getElement());
			// SOLog.info("capture set...");
			// BEINGDRAGED = true;

			break;

		case Event.ONMOUSEDOWN:
			SOLog.info("_________________Event down while editing object");

			break;

		case Event.ONMOUSEOUT:

			// stop dragging
			// BEINGDRAGED = false;
			// DOM.releaseCapture(this.getElement());

			break;
		case Event.ONMOUSEMOVE:
			SOLog.info("_________________Event mouse move while editing object");
			//do nothing, the spiffydragpanel handles this)


			// // if dragging then move this object, displaced by clickpoint
			// if (BEINGDRAGED) {
			//
			// // SOLog.info("being dragged...");
			// // get co-ordinates relative to parent frame (kinda messy)
			//
			// int cx = event.getClientX();
			// int cy = event.getClientY();
			//
			// cx = cx - this.getElement().getAbsoluteLeft()
			// + this.getElement().getScrollLeft()
			// + this.getElement().getOwnerDocument().getScrollLeft()
			// + thisobject.objectsData.X ;
			//
			// cy = cy - this.getElement().getAbsoluteTop()
			// + this.getElement().getScrollTop()
			// + this.getElement().getOwnerDocument().getScrollTop()
			// + thisobject.objectsData.Y;
			//
			// this.setPosition(cx-20, cy-20);
			//
			// // update data
			//
			// }

			break;
		case Event.ONMOUSEUP:
			SOLog.info("_________________Event mouse upwhile editing object");
			//do nothing, the spiffydragpanel handles this)

			// stop dragging
			// BEINGDRAGED = false;

			// DOM.releaseCapture(this.getElement());
			break;
		default:
			//	GreySOLog.info("__________(no action)_________");

			break; // Do nothing
		}// end switch

	}

	public void setSelectedCSS(boolean b) {

		if (b) {


			this.getElement().getStyle().setOutlineColor("#FF0000");			
			this.getElement().getStyle().setOutlineWidth(2, Unit.PX);
			this.getElement().getStyle().setOutlineStyle(OutlineStyle.DOTTED);


		} else {

			this.getElement().getStyle().setOutlineWidth(0, Unit.PX);
			this.getElement().getStyle().setOutlineStyle(OutlineStyle.NONE);

		}

	}




	//These lines are currently holding up some moves of functions from SceneObjectVisual to SceneObject
	public void updateLastClickedLocation() {

		//only update if there's a current event
		if (Event.getCurrentEvent()==null){
			return;
		}

		//Note for touchend events the values of X/Y will be zero
		//This is because your figures have already left the screen and technically not touching
		//To get around this, we detect this event type here, and use values obtained from the last
		//TouchMove or TouchStart

		//Note we are using Bit comparison logic here
		//that lets us check for any touch event at the same time
		//Information here:
		//http://stackoverflow.com/questions/16166895/declaring-and-checking-comparing-bitmask-enums-in-objective-c

		int x=0;
		int y=0;

		if ((Event.getCurrentEvent().getTypeInt() & Event.TOUCHEVENTS)!=0){

			SOLog.info(" WAS A TOUCH EVENT:");

			x = LastTouchMoveScreen.getClientX();
			y = LastTouchMoveScreen.getClientY();

		} else {

			x = Event.getCurrentEvent().getClientX();
			y = Event.getCurrentEvent().getClientY();

		}

		SOLog.info("click at screen:"+x+","+y);


		//might be able to simplify the below by just using the current scenes position?
		//(but then the scene might be in a frame or bordered)

		CurrentScenesVariables.lastclicked_x = x
				- this.getElement().getAbsoluteLeft()
				+ this.getElement().getScrollLeft()
				+ this.getElement().getOwnerDocument().getScrollLeft()
				+ thisobject.getTopLeftBaseX(); //not working when relative


		//	SOLog.info("thisobject.objectsData.X = "+thisobject.getTopLeftX());


		CurrentScenesVariables.lastclicked_y = y
				- this.getElement().getAbsoluteTop()
				+ this.getElement().getScrollTop()
				+ this.getElement().getOwnerDocument().getScrollTop()
				+ thisobject.getTopLeftBaseY();


		CurrentScenesVariables.lastclicked_z = thisobject.getTopLeftBaseZ();

		CurrentScenesVariables.lastclickedscreen_x = x;
		CurrentScenesVariables.lastclickedscreen_y = y;
	}


	//t

	//@Override
	//when relative objects in made from SceneObjects this can be moved into the superclass SceneObject rather then here
	/*
	/**
	 *  We override visibility to correctly sync the opacity, as our SceneObjectVisual objects have opacity
	 *  yet SceneObjects do not (at least not yet).
	 *  When we do this can be combined with the supertype method (ready)
	 * @param status

	@Override
	public void setVisibleSecretly(boolean status) {
		ObjectsLog.info("(sov) Setting "+this.objectsCurrentState.ObjectsName+" visible secretly:"+status);
		if (status) {
			Opacity = 100;
			//thisobject.getElement().getStyle().setOpacity(100);
			setOpacityImplementation(100/100);

		} else {
			Opacity = 0;
			//thisobject.getElement().getStyle().setOpacity(0);
			setOpacityImplementation(0);
		}
		super.setVisibleSecretly(status);
		ObjectsLog.info("display is now set to:"+getElement().getStyle().getDisplay());
	}
	 */

	/**
	 * We override to allow fade updates to work.
	 * And to not remove ourselves from updating if movement is over if fades are still going
	 *
	@Override
	public void update(float delta)  //can be combined with suipertype
	{	
		//if we arnt moving and not fadeing we remove from the updates
		if (!isMoving() && currentFade==FadeMode.None) {
			JAMTimer.removeObjectToUpdateOnFrame(this);			
			return;					
		}

		//if moving we update the movement
		if (isMoving()){
			movementFrameUpdate(delta);
		}

		//if fading we update the fade
		if (currentFade!=FadeMode.None){
			updateFade(delta);
		}

	}
	 */



	/**
	 * We override to provide a more accurate version if attached to the Dom.
	 * Shouldn't really be needed but just makes sure really.	 * 
	 * 
	@Override
	public int getTopLeftX() {
		//if its attached we simply check the document
		if (this.isAttached()){

			//get the element attached to the panel
			//if there's no actions this will be the widget itself
			//if there is its the PARENT because its put into a focus panel first
			Element elmentToCheck = this.getElementOnPanel();

			//we could check just the offsetLeft property, but that will return zero if the scene is not to the fron already.
			//still, possible future optimization?

			String leftCSS = elmentToCheck.getStyle().getLeft();

			leftCSS=leftCSS.substring(0, leftCSS.length()-2); //removes the "px" from the end			
			//	SOLog.info(objectsData.ObjectsName+ " is attached at "+leftCSS);
			int left;
			try {

				left = Integer.parseInt(leftCSS);

			} catch (NumberFormatException e) {				

				SOLog.info(getObjectsCurrentState().ObjectsName+ " not got css, returning default x");
				return getObjectsCurrentState().X;
			}

			return left; 

		} else {
			SOLog.info(getObjectsCurrentState().ObjectsName+ " not attached, returning default x which is "+getObjectsCurrentState().X+" (assuming its not relative)");
		}

		return super.getTopLeftX();


	}
	 **/

	/**
	 * We override to provide a more accurate version if attached to the Dom.
	 * Shouldn't really be needed but just makes sure really.	 * 
	 * 
	@Override
	public int getTopLeftY() {
		//if its attached we simply check the document
		if (this.isAttached()){

			//get the element attached to the panel
			//if theres no actions this will be the widget itself
			//if there is its the PARENT because its put into a focus panel first
			Element elmentToCheck = this.getElementOnPanel();
			//if (this.objectsActions.size()>0){
			//	 elmentToCheck = this.getElement().getParentElement();
			//} 

			String topCSS = elmentToCheck.getStyle().getTop();
			topCSS=topCSS.substring(0, topCSS.length()-2); //removes the "px" from the end

			//SOLog.info(objectsData.ObjectsName+ " is attached at "+topCSS);
			int top;
			try {
				top = Integer.parseInt(topCSS);
			} catch (NumberFormatException e) {				
				SOLog.info(getObjectsCurrentState().ObjectsName+ " not got css, returning default y");
				return getObjectsCurrentState().Y;
			}
			return top; 

		}

		return super.getTopLeftY(); 


	}
	 **/


	/**
	 * Convenience method to return this object as a spritesceneobject, if thats
	 * its type else returns null (in future exception error)
	 * Try to avoid using these 
	 * 
	 **/
	public SceneSpriteObject getAsSprite() {
		if (getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.Sprite) {
			return (SceneSpriteObject) this;
		} else {
			SOLog.info("attemting a sprite only function on a non sprite object ");
			return null;
		}

	}

	public SceneDialogObject getAsDialog() {
		if (getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.DialogBox) {
			return (SceneDialogObject) this;
		} else {
			SOLog.info("attemting a Dialog only function on a non Dialog object ");
			return null;
		}

	}

	public SceneLabelObject getAsLabel() {
		if (getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.Label) {
			return (SceneLabelObject) this;
		} else {
			SOLog.info("attemting a text only function on a non text object ");
			return null;
		}

	}

	public IsSceneDivObject getAsDiv() {
		if (getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.Div) {
			return (IsSceneDivObject) this;
		} else {
			SOLog.info("attemting a div only function on a non div object ");
			return null;
		}


	}

	public IsSceneVectorObject getAsVector() {
		if (getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.Vector) {
			return (IsSceneVectorObject) this;
		} else {
			SOLog.info("attemting a vector only function on a non vector object ");
			return null;
		}


	}


	/**
	 * remember to run 
	 * updateRelativelyZIndexedObjects(newzindex);
		 after calling this 
	 * @param newzindex
	 */
	public void setZIndexImplementation(int newzindex) {



		if (newzindex != -1) {
			// only set a zindex if not -1 
			getObjectsCurrentState().zindex = newzindex;

			SOLog.info("_____________________________the zindex set too..."	+ getObjectsCurrentState().zindex);


			if (thisobject.isAttached()){
				SOLog.info("____"+thisobject.getObjectsCurrentState().ObjectsName+"___is attached");
			} else {
				SOLog.info("____"+thisobject.getObjectsCurrentState().ObjectsName+"___is not attached");
			}

			Element elementOnPanel = thisobject.getElementOnPanel();

			// set zindex on the element (note: dont set it on a elephant thats
			// just silly)
			//if (elementOnPanel==null){
			//	SOLog.info("____thisobject is null");
			//	return;

			//}

			if (elementOnPanel==null){
				SOLog.info("_____________________________element not found");
				return;
			}

			SOLog.info("~~~~~Just before setting style");
			//thisobject.getElement().getParentElement().getStyle().setZIndex(objectsCurrentState.zindex);
			elementOnPanel.getStyle().setZIndex(getObjectsCurrentState().zindex);

		} else {

			Element elementOnPanel = thisobject.getElementOnPanel();
			//thisobject.getElement().getParentElement().getStyle().setProperty("zIndex", "auto");
			elementOnPanel.getStyle().setProperty("zIndex", "auto");
		}


	}

	/**
	 * Returns either this.getelement or this.getElement.getPArentElement depending on if this scene object
	 * is within a focuspanel or not
	 * (sceneobjects are put into focus panels currently if they have any mouse actions)
	 * 
	 * In effect this returns the element which is directly within the spiffydragpanel used for the scenewidget.
	 * @return
	 */
	public Element getElementOnPanel(){
		
		//if we have no scene we return the element itself 
		if (this.getParentScene()==null){
			return getElement();
		}
		
		//if we are on a scene...		
		//we now ask the dragpanel if we are in a parent container instead of relaying on our own test
		//This is because, frankly, the dragpanel knows best, as its in control of if we are given a container panel or not.
		//As long as we are a focus panel ourselves, these days it shouldn't bother putting us in a additional one, but its best to be sure
		//-----------
		
		boolean inContainer = ((SceneWidgetVisual)this.getParentScene()).isWidgetInContainer(this.getInternalGwtWidget());
		
		
		if (inContainer){
			//SOLog.info("______________________in focus panel so returning parent:");
			return getElement().getParentElement();
		} else {
			//SOLog.info("______________________not in focus panel:");
			return getElement();
		}

		//old:
/*
		if (shouldBeInFocusPanel()){
			//SOLog.info("______________________in focus panel so returning parent:");
			return getElement().getParentElement();
		} else {
			//SOLog.info("______________________not in focus panel:");
			return getElement();
		}*/


	}
	/**
	 * returns true if this should be put into a focus panel.
	 * currently based on if it has any actions or not
	 * NOTE: The dragpanel wont put this in a focus panel anymore because we already are one.
	 * This is more efficiant.
	 * However, we still tell it if its needed or not, as if its not needed, we can also forget about remembering handlers
	 * 
	 * @return
	 */
	public boolean shouldBeInFocusPanel(){
		
		//we used to use objectsActions, but now even with objectsActions we should not need to be in a focus panel
		//This is because we ARE a focus panel, and the dragpanel is now inteligent enough to know that.
		
		if (objectsActions.size()>0 || getObjectsCurrentState().forceReceiveActions){
			return true;
		} else {
			return false;
		}
		
		
		
		
	}

	@Override
	public int getZindex() {

		int zin = getObjectsCurrentState().zindex; 

		//get the right element to test (that is, this objects outer div, the one immediately a child of the dragpanel)
		Element ElementOnPanel = getElementOnPanel();


		//if (this.getElement().getParentElement()==null){
		if (ElementOnPanel==null){
			SOLog.info("no parent element error ");
			return zin;
		}

		//Element ElementOnPanel = this.getElement().getParentElement(); //we used to get the element this way, now we use the getElementOnPanel() above


		//SOLog.info("Got parent E");
		Style ElementStyle = ElementOnPanel.getStyle();
		//SOLog.info("Got parent Style");
		String ZindexNonTrim = ElementStyle.getZIndex();
		//SOLog.info("Got parent Zindex "+ZindexNonTrim);

		//SOLog.info(""+ZindexNonTrim.getClass().getName());

		//String blar = "30".trim();

		//SOLog.info("Well gosh darnit! "+blar);
		String zindexstring = (""+ZindexNonTrim).trim(); //ie hackjob

		//SOLog.info("~~~~~zomg no crashyness?");
		if ((zindexstring != "") && (zindexstring!="auto")) {

			zin = Integer.parseInt(zindexstring);

		} else {

			SOLog.info("no zindex set on " + this.getObjectsCurrentState().ObjectsName);

		}

		return zin;
	}



	public int getOriginalX() {
		//int ox = -1;

		return initialObjectState.X;

		/*
		// this is crude, the data really needs refractoring so we can use
		// so.ObjectData and not need this 
		if (this.getAsSprite() != null) {
			ox = this.getAsSprite().initialObjectState.X;
			return ox;
		}

		if (this.getAsDialog() != null) {
			ox = this.getAsDialog().initialObjectState.X;
			return ox;
		}
		if (this.getAsVector() != null) {
			ox = this.getAsVector().initialObjectState.X;
			return ox;
		}
		if (this.getAsDiv() != null) {
			ox = this.getAsDiv().initialObjectState.X;
			return ox;
		}

		 */

		//return ox;
	}

	public int getOriginalY() {
		return initialObjectState.Y;
		/*
		int oy = -1;

		// this is crude, the data really needs refractoring so we can use
		// so.ObjectData and not need this if
		if (this.getAsSprite() != null) {
			oy = this.getAsSprite().initialObjectState.Y;
			return oy;
		}
		if (this.getAsDialog() != null) {
			oy = this.getAsDialog().initialObjectState.Y;
			return oy;
		}
		if (this.getAsDiv() != null) {
			oy = this.getAsDiv().initialObjectState.Y;
			return oy;
		}
		if (this.getAsVector() != null) {
			oy = this.getAsVector().initialObjectState.Y;
			return oy;
		}
		return oy;*/
	}


	//need implementation to give the true size of the object
	//from its internal gwt widget
	public int getPhysicalObjectWidth(){
		return this.getOffsetWidth();
	}

	public int getPhysicalObjectHeight(){
		return this.getOffsetHeight();
	}







	//public void stopMovements() {

	// stop any active movements
	///	if (this.objectsCurrentState.getCurrentType()==SceneObjectType.Sprite){
	//		((SceneSpriteObject) this).StopCurrentMovement();
	//	}

	//}

	public void resetToDefaultState() {

		this.resetObject();

		//		alreadyLoaded=false;
		//		
		//		if (objectsData.currentType==SceneObjectType.Sprite){
		//			
		//		((SceneSpriteObject) this).updateState(((SceneSpriteObject) this).defaultObjectState,true);
		//		
		//		} 
		//		
		//		if (objectsData.currentType==SceneObjectType.DialogBox){
		//			
		//		((SceneDialogObject) this).updateState(((SceneDialogObject) this).defaultObjectState,true);
		//			
		//		} 
		//		


	}

	/** Loads the specified state into the object <br>
	 * Internally this detects which object type this is, and runs the update method on that specific type.
	 * @param runOnload - determines if the OnLoad: actions are triggered for this object again
	 * @param newState - will crash if the state isnt of the correct type for this object **/

	public void loadState(final SceneObjectState newState,	final boolean runOnload) {

		//check what type of object this is, then run the special update state for that type.
		//yeah, some messy typecasting here on both for object itself, as well as the "newState" data.
		SceneObjectType primaryObjectType = getObjectsCurrentState().getPrimaryObjectType();

		switch (primaryObjectType) {
		case Sprite:
			((SceneSpriteObject) this).updateState(((SceneSpriteObjectState) newState),runOnload,true);
			break;
		case Label:
			((SceneLabelObject) this).updateState(((SceneLabelObjectState) newState),runOnload,true);
			break;
		case DialogBox:
			((SceneDialogObject) this).updateState(((SceneDialogueObjectState) newState),runOnload,true);
			break;
		case Div:
			((SceneDivObject) this).updateState(((SceneDivObjectState) newState),runOnload,true);
			break;
		case Vector:
			((SceneVectorObject) this).updateState(((SceneVectorObjectState) newState),runOnload,true);
			break;
		case Input:	
			((SceneInputObject) this).updateState(((SceneInputObjectState) newState),runOnload,true);
			break;
		case InventoryObject:
			((InventoryItem) this).updateState(((InventoryObjectState) newState),runOnload,true);			
			break;
		default:
			break;
		} 
		//refresh the object inspector if its open
		updateDebugInfo();

	}

	/** things needed to be setup on all SceneObject types (the sub types will call these commands with super.setupWidget()**/
	public void setupWidget() {

		ObjectsLog.log("Setting up SceneObjectVisual..","orange");

		//set style if one is specified
		//	if (objectsCurrentState.CurrentBoxCSS!=null && objectsCurrentState.CurrentBoxCSS.length()>0){		
		//		ObjectsLog.info("setting up CurrentBoxCSS to : "+objectsCurrentState.CurrentBoxCSS);
		//		setBoxCSS(objectsCurrentState.CurrentBoxCSS);			
		//	}

		ObjectsLog.info("setting currently visibile:"+getObjectsCurrentState().currentlyVisible);	
		//Visibility will,unfortunately, also set opacity, so we need to save the opacity....
		double opac = getObjectsCurrentState().currentOpacity;
		setVisibleSecretly(getObjectsCurrentState().currentlyVisible);
		
		if (getObjectsCurrentState().currentlyVisible){
			setOpacityImplementation(opac);//....................................................then restore it here (while setting the visual representation of it)
			getObjectsCurrentState().currentOpacity=opac;
			ObjectsLog.info("opacity set to:"+opac);	
		}
		
		
		//set the title
		setTitle(getObjectsCurrentState().Title);

		//if set to be invisible to clicks we apply the pointer events none css
		//should be done in spiffydrag panel as a general function	
		//if (objectsCurrentState.ignorePointerEvents){
		//	this.getElement().getParentElement().getStyle().setProperty("pointerEvents", "none");

		//}


		sinkEvents(Event.ONMOUSEUP | Event.ONDBLCLICK | Event.ONCONTEXTMENU
				| Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
				| Event.ONTOUCHSTART | Event.ONTOUCHEND | Event.ONTOUCHCANCEL|Event.ONTOUCHMOVE);

	}





	/**
	 * Assigns all the parameters specific to whatever this objects type is
	 * All the subtypes have to implement this.
	 * It will be called by SceneObjectVisual automatically after the generate universal object parameters are assigned
	 * 
	 * Classes when implementing this should,after calling any needed super, as there first thing assign their state object as the same as this SceneObjectVisuals;
	 * objectsCurrentState = (SceneInputObjectState) super.objectsCurrentState; 
	 * This ensures the subclass keeps a reference to the specific type of state it is, while the superclass points to the same
	 * data but only  knows it as a general SceneObjectState.
	 * 
	 * 
	 * TODO: This should be removed, as assigning is now done in SceneObject
	 * 
	 * @param Parameters - string of parameters containing the objects settings
	 */
	//abstract void assignObjectTypeSpecificParameters(String Parameters[]);
	//abstract void assignObjectTypeSpecificParameters();


	@Override
	/** This can be overridden to allow custom UI for displaying the log in different implementations **/
	public void ObjectsLog(String contents,LogLevel level){

		switch (level) {
		case Apoclypse:
			ObjectsLog.log(contents,"RED"); //in future this SpiffyLogBox should be upgraded to make it flash
			break;
		case Error:
			ObjectsLog.error(contents);	
			break;
		case MildPanic:
			ObjectsLog.log(contents,"ORANGE");
			break;
		case Warning:
			ObjectsLog.log(contents,"ORANGE");
			break;
		case Info:
			ObjectsLog.info(contents);
			break;
		case Spam:
			ObjectsLog.log(contents,"GREY");
			break;		
		default:
			break;

		}
	}

	@Override
	/** This can be overridden to allow custom UI for displaying the log in different implementations **/
	public void ObjectsLog(String contents,String color){
		ObjectsLog.log(contents,color);
	}
	
	@Override
	/** This can be overridden to allow custom UI for displaying the log in different implementations **/
	public void ObjectsLog(double contents){
		ObjectsLog.log(contents);
	}

	/** 
	 * As soon as a objects scene is known it should be set here.
	 * DO NOT get this mixed up with setObjectsScene, which does everything needed to move a object from one scene to another.
	 * This one just sets the sceneObject variable, and should only be used as part of the other method, and when loading
	 *  
	 *  @param- scene - must be a SceneWidgetVisual
	 **/
	//(remember scene can be none)
	@Override
	public void setObjectsSceneVariable(SceneWidget scene){

		//SOLog.info("sov setting object to new scene:"+scene.SceneFileName);
		objectScene = (SceneWidgetVisual)scene; //temp cast
		super.setObjectsSceneVariable(scene);   //need to keep there in sync
	}


	//Following are requirement implementations from superclass
	/**
	 * Called from the animation system. Should not be called separately as then the positional variables and the real position
	 * will be out of sync.	 
	 */
	@Override
	protected void setPositionOnItsScene(
			int x, int y,int z,
			boolean restrictPositionToScreen) 
	{				

		objectScene.setObjectsPosition(this,x, y,z,restrictPositionToScreen);

		updateDebugInfo();

	}




	/** Gets a height thats safe - that is, avoiding dialogue boxes that are open.**/
	public int getSafeHeightAwayFromDialogues(int x, int y) {
		//check for collision

		//get all the dialogues in this scene
		Set<SceneDialogObject> dobjects = this.getParentScene().getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.DialogBox);
		Iterator<SceneDialogObject> dit = dobjects.iterator();

		int txmax = x+this.getPhysicalObjectWidth();
		int tymax = y+this.getPhysicalObjectHeight();
		//Log.info("txmax:"+"_"+txmax+",tymax"+tymax);

		super.ObjectsLog("testing :"+dobjects.size()+" dialogues for collisions");
		
		
		while (dit.hasNext()) {

			SceneDialogObject sceneDialogObject = dit.next();

			if (sceneDialogObject==this){
				continue;
			}

			//only if visible
			if (sceneDialogObject.isVisible()){



				super.ObjectsLog("testing :"+sceneDialogObject.getName()+"  for collisions");
				int maxx = sceneDialogObject.getTopLeftBaseX()+sceneDialogObject.getPhysicalObjectWidth();
				int maxy = sceneDialogObject.getTopLeftBaseY()+sceneDialogObject.getPhysicalObjectHeight();


				//	Log.info("dialog testing this:"+"_"+x+","+y+"->"+txmax+","+tymax);
				//	Log.info("testing against:"+sceneDialogObject.getTopLeftX()+","+sceneDialogObject.getTopLeftY()+"-"+maxx+","+maxy);

				//testing top of box
				if ((x<maxx)&&(txmax>sceneDialogObject.getTopLeftBaseX())){
					//Log.info("dialog hitting x");
					if ((y<maxy)&&(tymax>sceneDialogObject.getTopLeftBaseY())){

						//we are inside so we should move out the way
						//	Log.info("dialog hitting x/y");
						super.ObjectsLog("dialog hitting:"+sceneDialogObject.getName());
						
						//if bottom is under then we go under
						if (tymax>maxy){

							y=maxy+25;

						} else {

							y = sceneDialogObject.getTopLeftBaseY()-this.getPhysicalObjectHeight()-25;
						}




					}


				}
				//testing bottom of box
				//if ((x<maxx)&&(txmax>sceneDialogObject.getTopLeftX())){

				//if ((tymax<maxy)&&(by>sceneDialogObject.getTopLeftY())){

				//we are inside so we should move out the way
				//	Log.info("bottom dialog hitting");

				//we go to the bottom by default
				//	y=maxy+10;


				//	}


				//}

			}


		}
		return y;
	}




	//updates the debugger when info has changed 
	@Override
	public void updateDebugInfo() {
		if (oipu != null) {
			oipu.update();
		}

	}


	@Override 
	protected void setVisibleImplementation(boolean status) {
		setVisibleInternalFocusPanel(status);
	}

	@Override 	
	protected void setOpacityImplementation(double opacityAsDouble){
		thisobject.getElement().getStyle().setOpacity(opacityAsDouble);
	}


	@Override
	public void removeThisObjectFromItsSceneImplementation() {
		// remove from scenes draggable background if it was not placed by DIV
		if (objectScene.isObjectInScene(this)){

			objectScene.removeWidget(this.getInternalGwtWidget());

		} else {

			SOLog.info("removing this object from parent: ");
			this.removeFromParent();

		}
	}




	@Override
	public void updateDebugGlobalVariableInfo(){
		GameDataBox.refreshSpecialVariables();

	}
	
	/**
	 * Purely for debugging loading, we can add more information
	 * specific to gwt
	 */
	@Override
	public String getLoadStatusDebug() {
		String loadStatusDebug = super.getLoadStatusDebug();
		
		loadStatusDebug = loadStatusDebug + "\n"
				+ " isAttached:"+this.isAttached()+"\n";
		
		if (this.getParentScene()!=null){
			
			loadStatusDebug = loadStatusDebug + "\n"
					+ " parentScene:"+getParentScene().SceneFileName+"\n"
					+ " onDragPanel:"+((SceneWidgetVisual)getParentScene()).dragPanelCore.isOnPanel(this.getInternalGwtWidget())+"\n"				
					+ " inFocusContainer?:"+((SceneWidgetVisual)getParentScene()).isWidgetInContainer(this.getInternalGwtWidget())+"\n";
						
		} else {

			loadStatusDebug = loadStatusDebug + "\n"
					+ " parentScene: not in scene";			
		}
		
		
		return loadStatusDebug;
	}
	
}
