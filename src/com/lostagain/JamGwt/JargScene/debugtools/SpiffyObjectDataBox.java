package com.lostagain.JamGwt.JargScene.debugtools;

import java.util.Iterator;
import java.util.logging.Logger;
import com.google.common.base.Optional;
import com.darkflame.client.semantic.SSSNode;
import com.darkflame.client.semantic.SSSProperty;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.CollisionMap.Polygon;
import com.lostagain.Jam.CollisionMap.PolygonCollisionMap;
import com.lostagain.Jam.CollisionMap.SceneCollisionMap;
import com.lostagain.Jam.Movements.MovementState;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectState.touchingMode;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.SceneSpriteObjectState;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneDivObject;
import com.lostagain.JamGwt.JargScene.SceneDialogObject;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;

import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyDataBox;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyEditRangeBox;

/** 
 * the super type of the object inspectors data box
 * all a objects common properties should go here. That is
 * objects that apply to all objects  **/ 

public class SpiffyObjectDataBox extends SpiffyDataBox {

	public static Logger Log = Logger.getLogger("JAM.SpiffyObjectDataBox");

	
	//the object and its state this box reflects
	protected SceneObjectVisual sourceObject;
	protected SceneObjectState sourceState;

	//x and y position label
	Label Xlab = new Label();
	Label Ylab = new Label();
	Label Zlab = new Label();

	//z-index level....which can be edited! 
	//uses spiffyeditrangebox to enable the editing
	SpiffyEditRangeBox zlab = new SpiffyEditRangeBox(0,20000,100,10, new Runnable(){
		@Override
		public void run() {
			//	sourceObject.setZIndex((int) zlab.getValue());	

		}
	});

	Button zDeduce = new Button ("(Deduce)");



	private boolean editingZindex=false;

	//box boxcssediter 
	TextBox BoxCSSEditer = new TextBox();

	//the panel the stores the z index range data
	HorizontalPanel zRangeStuff = new HorizontalPanel();
	//z index range data
	Label zRangeLower = new Label();
	Label zRangeUpper = new Label();
	Label zRangeStep = new Label();

	//is the zindex linked?
	Label zLinked = new Label();
	Label zLinkedStep = new Label();

	//is the object restricted to the screen
	Label RestrictToScreenLab = new Label();

	//the position of the pin
	Label PinLabX = new Label();
	Label PinLabY = new Label();
	Label PinLabZ = new Label();

	//the relative position data
	Label RelativeX = new Label();
	Label RelativeY = new Label();
	Label RelativeZ = new Label();

	Label RelativeToPoint = new Label(); //relative to a named point on the object
	Label RelativeObject = new Label();
	Label RelativeLinkType = new Label();

	//collision region mode	
	Label collisionModeLabel = new Label();
	Label collisionCmapLabel = new Label();
	Button resetPolygonCollisionCache = new Button("reset c_cache");
	DisclosurePanel collisionDetails = new DisclosurePanel("Collision Details");

	//friction details
	Label frictionDetails = new Label();

	//the objects url
	Label url = new Label();

	//control if its currently visible checkbox
	CheckBox currentlyVisible = new CheckBox();

	//opacity we just display
	Label currentOpacityLab = new Label();


	//Serialized data label
	DisclosurePanel sdata = new DisclosurePanel("sdata details");
	Label sdata_label = new Label();

	//Serialized property
	Label props = new Label();
	//Serialized variables
	Label Variables = new Label();
	//what objects touching this label
	Label touching = new Label();
	Button updateTouching = new Button("[auto update]");

	//property nodes stored in this panel
	VerticalPanel PropertyNodes = new VerticalPanel();

	//edit property
	HorizontalPanel addRemovePropertyPanel = new HorizontalPanel();
	TextBox propertyNameBox = new TextBox();
	Button addPropertyButton = new Button("+");
	Button removePropertyButton = new Button("-");

	//edit touching
	HorizontalPanel addRemovetouchingPanel = new HorizontalPanel();
	TextBox touchingNameBox = new TextBox();
	Button addtouchingButton = new Button("+");
	Button removetouchingButton = new Button("-");

	//reseting and saving controls
	final HorizontalPanel ResetControls = new HorizontalPanel();
	final Button ResetToThis     = new Button("Reset To This");
	final Button SaveThisState   = new Button("Save State");
	final CheckBox ResetChildren = new CheckBox("Reset Children");
	final CheckBox runOnLoad     = new CheckBox("Run onload");
	final Button testStateButton = new Button("Different From Current?");

	//fake height label

	//Label fakeHeight = new Label(); (not used - we have real heights!)

	//positioned relatively too stuff
	DisclosurePanel ThingsPositionedRelativeToThisDis = new DisclosurePanel("child Objects");	
	VerticalPanel ThingsPositionedRelativeToThis = new VerticalPanel();

	//movementstates
	DisclosurePanel movementStateDisclosure = new DisclosurePanel("movement data");

	SpiffyDataBox movementDatabox = new SpiffyDataBox();

	//action stuff
	DisclosurePanel actionsDisclosure = new DisclosurePanel("(Actions)");
	Label actionslab = new Label("");

	//edit the raw serial data
	TextArea editsdata = new TextArea();

	//labels for global object propertys
	Label PinLabl = new Label("Pin:");
	Label editsdatal = new Label("edit Sdata:");
	Label sdatal = new Label("Sdata:");
	Label propl = new Label("Propertys:");


	Label VariablesLab = new Label("ObjectsVariables:");
	Label Relativel = new Label("Rel:");

	Label locl = new Label("Loc:");
	Label inDiv = new Label("-");


	Label Urll = new Label("Url:");

	Label touchl = new Label("Touching:");

	Label typel = new Label("Type:");
	Label titlel = new Label("Title:");
	Label visiblel = new Label("Visible:");
	Label cssl = new Label("Css:");

	//universal html databox
	DisclosurePanel htmlToString = new DisclosurePanel("(htmlToString)");
	Label htmlToStringlab = new Label("htmltostringlab");



	/** the super type of the object inspectors data box
	 * all a objects common properties should go here. That is
	 * objects that apply to all objects  **/ 
	public SpiffyObjectDataBox (final SceneObjectVisual sObject, final SceneObjectState objstate){


		//set the object we are geting the data from and its state
		this.sourceObject = sObject;
		this.sourceState = objstate;

		//set the fake height
		//fakeHeight.setText(" "+sourceObject.getObjectsCurrentState().Z);
		zlab.addBlurHandler(new BlurHandler() {		
			@Override
			public void onBlur(BlurEvent event) {
				sourceObject.setZIndex((int) zlab.getValue());	
				editingZindex = false;
			}
		});
		zlab.addFocusHandler(new FocusHandler() {	
			@Override
			public void onFocus(FocusEvent event) {	
				editingZindex = true;
			}
		});

		zDeduce.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {

				SceneWidget parentScene = sourceObject.getParentScene();	

				if (parentScene!=null){
					Log.info("Deduceing zindex");					
					parentScene.updateZIndexBasedOnPerspective(sourceObject); 
				}


			}
		});


		//set the disclosure containing the list of things positioned relative to this
		ThingsPositionedRelativeToThisDis.add(ThingsPositionedRelativeToThis);
		ThingsPositionedRelativeToThisDis.setOpen(false);

		//---styles---
		locl.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		sdata_label.setWidth("350px");
		sdata_label.getElement().getStyle().setFontSize(80, Unit.PCT);
		sdata_label.getElement().getStyle().setProperty("wordWrap", "break-Word");
		sdata.add(sdata_label);


		props.setWidth("350px");
		props.getElement().getStyle().setFontSize(80, Unit.PCT);

		url.getElement().getStyle().setFontSize(80, Unit.PCT);

		touching.setWidth("350px");
		touching.getElement().getStyle().setFontSize(80, Unit.PCT);		
		//----
		addRemovePropertyPanel.add(propertyNameBox);
		addRemovePropertyPanel.add(addPropertyButton);
		addRemovePropertyPanel.add(removePropertyButton);

		//manually add/remove property
		propertyNameBox.addFocusHandler(new FocusHandler() {			
			@Override
			public void onFocus(FocusEvent event) {
				JAMcore.setIgnoreKeyPresses(true);
			}
		});
		propertyNameBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				JAMcore.setIgnoreKeyPresses(false);
			}
		});


		addPropertyButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {

				sourceObject.addProperty(propertyNameBox.getText(),true,true);	

			}
		});

		removePropertyButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				sourceObject.removeProperty(propertyNameBox.getText(),true,true);	

			}
		});		


		//----------------------
		addRemovetouchingPanel.add(touchingNameBox);
		addRemovetouchingPanel.add(addtouchingButton);
		addRemovetouchingPanel.add(removetouchingButton);
		addRemovetouchingPanel.add(updateTouching);

		touchingNameBox.addFocusHandler(new FocusHandler() {			
			@Override
			public void onFocus(FocusEvent event) {
				JAMcore.setIgnoreKeyPresses(true);
			}
		});
		touchingNameBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				JAMcore.setIgnoreKeyPresses(false);
			}
		});





		//manually add/remove touchings
		addtouchingButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				SceneObject sceneObject = SceneObjectDatabase.getSingleSceneObjectNEW(touchingNameBox.getText().trim(), null, true);

				sourceObject.addTouchingProperty(sceneObject,sObject);	
				sourceObject.updateDebugInfo();

			}
		});

		removetouchingButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SceneObject sceneObject = SceneObjectDatabase.getSingleSceneObjectNEW(touchingNameBox.getText().trim(), null, true);

				sourceObject.removeTouchingProperty(sceneObject,sObject);	
				sourceObject.updateDebugInfo();

			}
		});	



		updateTouching.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				sourceObject.updateTouchingAutomatically();

			}
		});


		//Tests if the state has changed from the current state
		testStateButton.getElement().getStyle().setFontSize(70, Unit.PCT);		//shrink it a bit to fit better

		testStateButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				SceneObjectState thisState    = objstate;
				SceneObjectState currentState = sObject.getObjectsCurrentState();				
				//compare
				boolean statesMatch = thisState.sameStateAs(currentState);

				if (statesMatch){
					Window.alert("States match!");
				} else {
					Window.alert("States dont match!");
				}

			}
		});


		//set the handler for the reset button
		//when clicked it loads the state
		ResetToThis.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				Log.info("loading state of "+sourceObject.getObjectsCurrentState().ObjectsName+" to box");
				sourceObject.ObjectsLog("REQUESTED save state to load:"+objstate.serialiseToString());
				
				
				//loads the state in this box
				boolean runOnLoadBoolean = runOnLoad.getValue();
				//	if (runOnLoadBoolean){
				//		sourceObject.alreadyLoaded = false;
				//	}

				sourceObject.loadState(objstate, runOnLoadBoolean);

				//if flagged reset the children
				if (ResetChildren.getValue()){
					if (sourceObject.relativeObjects!=null){

						//reset the children too
						for (SceneObject relObj : sourceObject.relativeObjects) {

							Log.info("restting state of "+relObj.getObjectsCurrentState().ObjectsName+" to initial");
							((SceneObjectVisual)relObj).resetToDefaultState();

						}
					}
				}

			}
		});

		//save this state button
		SaveThisState.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				Log.info("saving state of "+sourceObject.getObjectsCurrentState().ObjectsName+" ");
				sourceObject.saveTempState();

				//close it
				sourceObject.oipu.CloseDefault();
				sourceObject.oipu = null;
				//reopen it again after setting it to null (this ensures its recreated
				//with the "savedata" tab)
				sourceObject.openObjectsInspector();


			}
		});

		//set up visibility toggle
		//if the checkbox is clicked it now makes the box appear/disapere
		currentlyVisible.addValueChangeHandler(new  ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Log.info("setting visibility to "+event.getValue());
				sourceObject.setVisible(event.getValue());

			}
		});

		//setup boxcss editing letting you change the css from the inspector
		BoxCSSEditer.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				Log.info("setting boxcss to "+event.getValue());
				//sourceObject.setBoxCSS(event.getValue());

				if (sourceObject.getObjectsCurrentState().isCompatibleWith(SceneObjectType.Div)){

					((IsSceneDivObject)sourceObject).setBoxCSS(event.getValue());

				}


			}
		});

		//add the various reset and save controls
		ResetControls.add(SaveThisState);
		ResetControls.add(ResetToThis);
		ResetControls.add(ResetChildren);
		ResetControls.add(runOnLoad);
		ResetControls.add(testStateButton);

		//add the z index range stuff (for dynamic zindex based on vertical position)
		zRangeStuff.add(zRangeLower);
		zRangeStuff.add(zRangeUpper);
		zRangeStuff.add(zRangeStep);

		//style the first column bold
		super.setFirstColStyle("bold");
		//put the reset button
		super.addrow(new Label("Reset"), ResetControls);


		//the labels and data specifying the type of object this is
		//and what scene it is on
		HorizontalPanel sceneAndType =new HorizontalPanel();
		Label sceneAndTypeLab = new Label("Scene and Type:");
		sceneAndTypeLab.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		sceneAndType.setSpacing(4);
		sceneAndType.add(sceneAndTypeLab);

		if (sourceObject.getParentScene()!=null) {
			sceneAndType.add(new Label(sourceObject.getParentScene().SceneFileName +":"));
		} else {
			sceneAndType.add(new Label(SceneObjectState.OBJECT_HAS_NO_SCENE_STRING+":"));

		}

		sceneAndType.add(new Label(objstate.ObjectsName));
		sceneAndType.add(new Label(" ("+objstate.getPrimaryObjectType().toString()+")"));

		//add the horizontal panel created above
		super.addrow(sceneAndType);

		//add the objects capabilities. (this is a list of objectypes this object can support GameCommands from) 

		super.addrow("Capabilities:",objstate.getCapabilitiesAsString());
		super.getRowFormatter().getElement(super.getRowCount()-1).getStyle().setFontSize(65, Unit.PCT);


		//  font-size: 65%;

		//	//add the title, visible other information
		super.addrow("Title:", "" + objstate.Title);

		HorizontalPanel visAndOpacity = new HorizontalPanel();
		visAndOpacity.add(currentlyVisible);
		visAndOpacity.add(currentOpacityLab);		
		super.addrow(new Label("Visible:"),visAndOpacity);

		super.addrow("IgnorePointerEvents:",""+objstate.ignorePointerEvents);

		HorizontalPanel BoxCSSAndBackground = new HorizontalPanel();
		//	BoxCSSAndBackground.add(new Label("BoxCss:"));
		BoxCSSAndBackground.add(BoxCSSEditer);//add the css editor box here, which both displays its css and lets you change it! wheee!
		BoxCSSAndBackground.add(new Label("Background:"+objstate.BackgroundString));//somewhat unused I think these days, as we fully control css rather then just the background property
		super.addrow("BoxCss:",BoxCSSAndBackground);
		//	super.addrow("BoxCss:", BoxCSSEditer); //add the css editor box here, which both displays its css and lets you change it! wheee!
		//super.addrow("Background:","" +obj.BackgroundString);//somewhat unused I think these days, as we fully control css rather then just the background property


		/**
		 * contains the absolute location, the pin, and any relative positioning as well as labels
		 */
		// Loc:  Pin:  Rel:
		// Xloc  Xloc  object
		// Yloc  Yloc  Xrel
		// -     -     yrel

		//In order to allow selections to work vertically down columns we make 3 vertical panels
		VerticalPanel locationPanel = new VerticalPanel();
		VerticalPanel pinPanel = new VerticalPanel();
		VerticalPanel relativePanel = new VerticalPanel();
		//fill them
		locationPanel.add(locl);
		locationPanel.add(inDiv);
		locationPanel.add(Xlab);
		locationPanel.add(Ylab);
		locationPanel.add(Zlab);

		//--
		pinPanel.add(PinLabl);
		pinPanel.add(PinLabX);
		pinPanel.add(PinLabY);
		pinPanel.add(PinLabZ);
		//--
		relativePanel.add(Relativel);
		relativePanel.add(RelativeObject);
		relativePanel.add(RelativeX);
		relativePanel.add(RelativeY);
		relativePanel.add(RelativeZ);

		//add handler so the user can quickly jump to the relative objects inspector
		RelativeObject.addClickHandler(new ClickHandler() {				
			@Override
			public void onClick(ClickEvent event) {
				if ( sourceState.positionedRelativeToo!=null){
					sourceState.positionedRelativeToo.openObjectsInspector();	
				}
			}
		});
		//just a visual highlight whenrolled over;
		RelativeObject.addMouseOverHandler(new MouseOverHandler() {			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				RelativeObject.getElement().getStyle().setColor("BLUE");
			}
		});

		RelativeObject.addMouseOutHandler(new MouseOutHandler() {			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				RelativeObject.getElement().getStyle().clearColor();
			}
		});
		//--


		//and put them in a horizontal panel
		HorizontalPanel positionParameters = new HorizontalPanel();
		positionParameters.add(locationPanel);
		positionParameters.add(pinPanel);
		positionParameters.add(relativePanel);


		locl.setStylePrimaryName("bold");
		PinLabl.setStylePrimaryName("bold");
		Relativel.setStylePrimaryName("bold");

		/*
		Grid positionParameters = new Grid(4,3);
		positionParameters.setWidget(0, 0, locl); 
		positionParameters.setWidget(1, 0, inDiv);
		positionParameters.setWidget(2, 0, Xlab);
		positionParameters.setWidget(3, 0, Ylab);
		PinLabl.setStylePrimaryName("bold");
		Relativel.setStylePrimaryName("bold");

		positionParameters.setWidget(0, 1, PinLabl);
		positionParameters.setWidget(1, 1, PinLabX);
		positionParameters.setWidget(2, 1, PinLabY);

		positionParameters.setWidget(0, 2, Relativel);
		positionParameters.setWidget(1, 2, RelativeObject);
		positionParameters.setWidget(2, 2, RelativeX);
		positionParameters.setWidget(3, 2, RelativeY);*/
		positionParameters.setWidth("400px");

		/*
		if (sourceObject.objectsCurrentState.attachToHTMLDiv.length()<2){
			super.addrow(locl, Xlab, Ylab); //x and y position of this object	if its not positioned by html
		} else {
			super.addrow(locl,":"+sourceObject.objectsCurrentState.attachToHTMLDiv); 
		}

		super.addrow(PinLabl, PinLabX, PinLabY); //the objects pin
		super.addrow(Relativel, RelativeObject, RelativeX, RelativeY,ThingsPositionedRelativeToThisDis);
		 */
		super.addrow(positionParameters);

		//super.addrow("FakeHeight:",fakeHeight);
		super.addrow("Child Objects:",ThingsPositionedRelativeToThisDis);

		HorizontalPanel zindexDetails = new HorizontalPanel();
		zindexDetails.add(zlab);
		zindexDetails.add(zRangeStuff);		
		zindexDetails.add(zDeduce);;
		super.addrow(new Label("Z-index:"), zindexDetails);

		HorizontalPanel linkedDetails = new HorizontalPanel();
		linkedDetails.add(zLinked);
		linkedDetails.add(zLinkedStep);		
		super.addrow(new Label("Z-linked:"),linkedDetails );		
		super.addrow(new Label("RestrictToScreen:"), RestrictToScreenLab); //if its restricted to screen (mostly used for dialogue divs)

		//does it have a GLU file specified? Or a movement INI?
		//(GLU files store attachment points, even in animations!)
		HorizontalPanel relatedFiles = new HorizontalPanel();
		//relatedFiles.add( new Label("relatedfiles:") );
		relatedFiles.add( new Label("attach(*.Glu):"+objstate.hasAttachmentPointFile)  );
		relatedFiles.add( new Label("____move(*.ini):"+objstate.hasMovementFile));
		super.addrow("relatedfiles:",relatedFiles);


		/*
		super.addrow(new Label("hasGlu/Ini"), 
				     new Label("attach(Glu):"+obj.hasAttachmentPointFile), 
				     new Label("move(ini):"+obj.hasMovementFile));

		 */

		Button resetPolygonCollisionCache = new Button("reset c_cache");
		resetPolygonCollisionCache.setTitle("resets the collision map for the polygons associated with this object");
		resetPolygonCollisionCache.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {

				Polygon.removeFromAllcaches(sourceObject.cmap.get());

			}
		});

		//current movement information
		VerticalPanel collisionDetailsPanel = new VerticalPanel();

		collisionDetailsPanel.add(collisionModeLabel);
		collisionDetailsPanel.add(collisionCmapLabel);
		collisionDetailsPanel.add(resetPolygonCollisionCache);

		Button previewCompoundMap = new Button("Preview CompoundMap");
		previewCompoundMap.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				//get map
				Optional<PolygonCollisionMap> map = sourceObject.getCompoundCmap();

				if (!map.isPresent()){
					sourceObject.ObjectsLog("previewing compound map....no map to preview");
					return;
				}		

				sourceObject.ObjectsLog("previewing compound map....");
				sourceObject.ObjectsLog("compound map elements:"+map.get().size());

				String compoundMapString = map.get().getPath();


				//sourceObject.ObjectsLog("previewing compound map:\n"+compoundMapString);

				//display it using the svg sketch layer

				SceneCollisionMap scenesmap = sourceObject.getParentScene().scenesCmap.get();


				scenesmap.clearSketch();
				scenesmap.addToSketch(compoundMapString, "pink",false);


				//(hide other things first?)
			}
		});

		collisionDetailsPanel.add(previewCompoundMap);
		Button clearCompoundMap = new Button("Clear CompoundMap");
		clearCompoundMap.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				sourceObject.ObjectsLog("clearing compound map....");
				sourceObject.invalidateCompoundCmap();

			}
		});

		collisionDetailsPanel.add(clearCompoundMap);

		collisionDetails.add(collisionDetailsPanel);

		super.addrow(new Label("Collision :"), collisionDetails); // 
		super.addrow(new Label("Friction:"), frictionDetails); // 
		

		HorizontalPanel movementDetails = new HorizontalPanel();
		movementDetails.add(movementStateDisclosure);
		
		Button stopMovements = new Button("Stop Movements");
		stopMovements.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				sourceObject.ObjectsLog("Stopping due to ObjectInspectorRequest","RED");
				sourceObject.StopCurrentMovement();

			}
		});
		movementDetails.add(stopMovements);
		
		super.addrow(new Label("Movement state:"), movementDetails);

		//clear and add movement data
		movementStateDisclosure.clear();
		movementStateDisclosure.add(movementDatabox);
		movementDatabox.setFirstColStyle("bold");

		super.addrow(new Label("HTML (tostring):"), htmlToString);
		//set htmltostring contents
		htmlToString.add(htmlToStringlab);


		//update the universal object data
		updateUniversalData();

	}

	/**
	 *  update the objects universal data.
	 * that is data thats common to all object types
	 *  **/
	public void updateUniversalData(){

		//Log.info("running update for: "+sourceState.ObjectsName);

		String onStepData="(none)";

		if (sourceObject.actionsToRunOnStep!=null){
			onStepData = ""+sourceObject.actionsToRunOnStep.size()+"";
			//intervals of each set
			for(SceneObject.onStepInfo info : sourceObject.actionsToRunOnStep.values()){
				onStepData=onStepData+" "+info.interval;	
			}
		}

		String tooltpDataOnActions = "("+sourceObject.objectsActions.size()+" actions) onstep:"+onStepData;
		actionsDisclosure.setTitle(tooltpDataOnActions);
		actionslab.setText("" + sourceObject.objectsActions.getCode());
		actionslab.getElement().getStyle().setWhiteSpace(WhiteSpace.PRE);

		//location information displays different depending on if we are in a htmldiv or not
		if (sourceObject.getObjectsCurrentState().attachToHTMLDiv.length()<2){			
			inDiv.setText("-");
			Xlab.setText("" + sourceState.getX());			
			Ylab.setText("" + sourceState.getY());			
			Zlab.setText("" + sourceState.getZ());
			
			//y might be temp higher on dialogues
			if (sourceObject.tempYOverride.isPresent()){
				Ylab.setText("" + sourceState.getY()+"("+sourceObject.tempYOverride.get()+")");		
			}


		} else {

			inDiv.setText(sourceObject.getObjectsCurrentState().attachToHTMLDiv);
			Xlab.setText("-");
			Ylab.setText("-");
			Zlab.setText("-");

		}
		/*
		if (sourceObject.objectsCurrentState.attachToHTMLDiv.length()<2){

			int rownumber = super.getRowContaining(locl);			
			//Log.info("found widget at:"+rownumber+" replacing loc info");

			super.editrow(rownumber,locl, Xlab, Ylab); //x and y position of this object	if its not positioned by html
		} else {

			int rownumber = super.getRowContaining(locl);			
			//	Log.info("found widget at:"+rownumber+" replacing with div info");

			super.editrow(rownumber,locl,":"+sourceObject.objectsCurrentState.attachToHTMLDiv);

		}*/


		//Log.info("running update.");
		if (!editingZindex){
			zlab.setValue((double) sourceState.zindex);
		}

		//if theres a variable z-index then display its data
		if (sourceState.variableZindex) {

			zRangeLower.setText(":"+sourceState.lowerZindex);
			zRangeUpper.setText("-"+sourceState.upperZindex);
			zRangeStep.setText(" / "+sourceState.stepZindex);

		} else {

			zRangeLower.setText("");
			zRangeUpper.setText("");
			zRangeStep.setText("");

		}


		if (sourceState.positionedRelativeToo != null) {

			RelativeObject.setText("-"
					+ sourceState.positionedRelativeToo.getObjectsCurrentState().ObjectsName+":"+sourceState.positionedRelativeToPoint);



		} else {

			RelativeObject.setText("-");


		}

		//is this visible?
		currentlyVisible.setValue(sourceState.currentlyVisible);

		//and its opacity
		currentOpacityLab.setText("  Opacity(0-1):"+sourceState.currentOpacity);


		//set its css name
		BoxCSSEditer.setText(sourceState.CurrentBoxCSS);

		//set its linked zindex state		
		zLinked.setText("linkedzindex:"+sourceState.linkedZindex);
		zLinkedStep.setText(""+sourceState.linkedZindexDifference);

		//is it restricted to screen?
		RestrictToScreenLab.setText(""+sourceState.restrictPositionToScreen);

		//relative position
		RelativeX.setText(" " + sourceState.relX+"("+sourceState.positionedRelativeLinkType.isXlinked()+")");
		RelativeY.setText(" " + sourceState.relY+"("+sourceState.positionedRelativeLinkType.isYlinked()+")");
		RelativeZ.setText(" " + sourceState.relZ+"("+sourceState.positionedRelativeLinkType.isZlinked()+")");

		//RelativeToPoint.setText(" -" + );

		//update the pin position text
		PinLabX.setText(" " + sourceState.CurrentPinPoint.x+"("+sourceState.DefaultPinPoint.x+")");
		PinLabY.setText(" " + sourceState.CurrentPinPoint.y+"("+sourceState.DefaultPinPoint.y+")");
		PinLabZ.setText(" " + sourceState.CurrentPinPoint.z+"("+sourceState.DefaultPinPoint.z+")");
		//--

		//
		collisionModeLabel.setText(sourceObject.getObjectsCurrentState().boundaryType.toString()+"_");
		collisionCmapLabel.setText(" cmap:"+sourceObject.cmap.isPresent()+" HasCompoundMap:"+sourceObject.hasCompoundMap());

		//friction details
		frictionDetails.setText(""+sourceState.objectsFrictionalResistance);
		//

		//update the movement state data
		if (sourceState.moveState.isPresent()){
			MovementState ms = sourceState.moveState.get();

			//clear and re-add all the movement data
			movementDatabox.removeAllRows();

			movementDatabox.addrow("Currentmode:",ms.getCurrentMode().name());	

			movementDatabox.addrow("currentpath:",ms.getCurrentPathName());

			movementDatabox.addrow("currentwaypoint:",""+ms.getMovement_currentWaypoint());
			movementDatabox.addrow("current x/y/z:", ms.get_current_position().toString());
			movementDatabox.addrow("vel x/y/z:",ms.get_current_velocity().toString());
			movementDatabox.addrow("acc x/y/z:",ms.get_current_acceleration().toString());


			//	movementDatabox.addrow("isMoving:",""+ms.isMoving());

			movementDatabox.addrow("current sx/sy/sz:",  ms.get_SC().toString());	  //start x/y of current movement curve	
			movementDatabox.addrow("current cx/cy/cz:",  ms.get_CC().toString());   //(curve x/y I think - not used when movement is a line)
			movementDatabox.addrow("current dx/dy/dz:",  ms.get_current_destination().toString());   //destination x/y (see the timer that manages curve movement to understand all these variables)

			movementDatabox.addrow("speed:",""+ms.get_speed());

			//movementDatabox.addrow("is on curve?:",""+ms.is_onCurve());
			movementDatabox.addrow("curve time:"  ,""+ms.get_curveTime());
			movementDatabox.addrow("curve step:"  ,""+ms.get_curveTimeStep());

		} else {
			movementDatabox.removeAllRows();
			movementDatabox.addrow("(No Movestate)");	
		}

		//-----------------------

		//	Log.info("updating ThingsPositionedRelativeToThis");

		//updating ThingsPositionedRelativeToThis if there's one
		if (sourceObject.relativeObjects!=null){

			ThingsPositionedRelativeToThis.clear();


			for (final SceneObject relObj : sourceObject.relativeObjects) {

				Label rName = new Label(relObj.getObjectsCurrentState().ObjectsName);
				rName.addClickHandler(new ClickHandler() {						
					@Override
					public void onClick(ClickEvent event) {
						relObj.openObjectsInspector();
					}
				});

				ThingsPositionedRelativeToThis.add(rName);

			}


		}		



		props.setText("" + sourceState.objectsProperties.toString());

		//we also display these propertys in a disclosure panel
		//so the user can browser their subclass's if they want
		PropertyNodes.clear();

		Iterator<SSSProperty> propit = sourceState.objectsProperties.iterator();

		//loop over adding all the property node labels to the PropertyNodes vertical panel
		while (propit.hasNext()) {

			SSSProperty sssProp = propit.next();

			// if its a class just add the node
			if (sssProp.getPred() == SSSNode.SubClassOf) {

				PropertyNodes.add(new NodeResultLabel(sssProp.getValue()));

			} else {
				// else we write the predicate then the node in a horizontal panel
				HorizontalPanel predthennode = new HorizontalPanel();

				predthennode.add(new NodeResultLabel(sssProp.getPred()));
				predthennode.add(new Label(":"));
				predthennode.add(new NodeResultLabel(sssProp.getValue()));

				PropertyNodes.add(predthennode);

			}
		}


		//update the touching data
		//touching data is usefull for when you have animated objects on a path
		//and you want to flag when they are touching other objects in the scene.
		String touchingOnManualOrAutomatic = "(Auto Mode)";
		if (sourceState.ObjectsTouchingUpdateMode == touchingMode.Manual){
			touchingOnManualOrAutomatic = "(Manual Mode)";
		}

		if (sourceState.touching != null) {

			touching.setText("" + sourceState.touching.getNames().toString()+" "+touchingOnManualOrAutomatic);
		} else {

			touching.setText("[]"+" "+touchingOnManualOrAutomatic);

		}


		//-------------

		//update the serialized sdata
		sdata_label.setText("" + sourceState.serialiseToString());
		sdata_label.setTitle(""+sourceState.serilisationHelperString());

		//update html tostringdata
		htmlToStringlab.setText(sourceObject.getInternalGwtWidget().toString());

	}

	/** sets or ends the edit mode **/
	public void setEditMode(boolean state) {

		if (state) {

			zlab.setEnable(true);
			editsdata.setEnabled(true);

		} else {

			zlab.setEnable(false);
			editsdata.setEnabled(false);

		}

	}

	/**enables the reset button**/
	public void setResetEnable(Boolean state){

		ResetToThis.setEnabled(state);
		ResetChildren.setEnabled(state);

	}
}
