package com.lostagain.JamGwt;

import java.util.Iterator;
import java.util.logging.Logger;

//import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isPopupTypeImplementation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.IsPopupPanel;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;

public class TitledPopUpWithShadow extends PopupPanel implements IsPopupPanel,
TouchStartHandler,
TouchEndHandler,
MouseUpHandler,
TouchMoveHandler,
MouseMoveHandler,
MouseDownHandler, 
IsPopupContents 
{

	static Logger Log = Logger.getLogger("JAM.PopUpWithShadow");

	private   String BOTTOM_RIGHT_BACKSTYLE  = "ShadowBottomRight pngfix";
	private   String BOTTOM_MIDDLE_BACKSTYLE = "ShadowLower pngfix";
	private   String BOTTOM_LEFT_BACKSTYLE   = "ShadowCorner pngfix";
	private   String MIDDLE_LEFT_BACKSTYLE   = "ShadowLeft pngfix";
	private   String MIDDLE_RIGHT_BACKSTYLE = "ShadowRight pngfix";	
	private   String TOP_MIDDLE_BACKSTYLE = "DefaultTopBar";
	private   String TOP_LEFT_BACKSTYLE = "ShadowTopLeft pngfix";
	// 

	public boolean centered = true;



	VerticalPanel verticalSplit = new VerticalPanel();

	Grid Container = new Grid(3,3);

	//set at this zindex flag
	public boolean fixed_zindex = false;
	public int fixed_zindex_value = 5000;


	public Label caption = new Label("PopUp (Drag Me)");
	private boolean dragging;
	private int dragStartX, dragStartY;

	String sizeX = "200px";
	String sizeY = "100px";

	//isPopupTypeImplementation Contents;// = new Label("");

	IsPopupContents Contents;// = new Label("");

	// pretty colour base
	HTML overlay = new HTML("<div></div>");

	//topbar bits
	HorizontalPanel TopBar = new HorizontalPanel();

	//used to be interface icon
	GWTAnimatedIcon closeX = new GWTAnimatedIcon("GameIcons/cc2xbutton0.png",3);

	//source icon of popup
	IsInventoryItem SourceIcon; //used to be defined as a widget which was silly as it pointless required using .getInternalGwtWidget()

	private Runnable RunOnClose;

	//initial popup z level
	public static int MiniumZIndex = 20000;

	public static void setMiniumZIndex(int miniumZIndex) {
		MiniumZIndex = miniumZIndex;
	}

	/**NOTES:
	    Animation cant currently be used at the same time as box-shadow, due to the way GWT handles animations
		Apply   clip: auto; to the element to enable shadows but disable animations
		TODO: Work out how to have both at once....presumably by removing the automatic clipping applied once the animation that uses it is finnished?
	 **/
	//	 TODO: check that isPopUpType is always a widget. Enforce this somehow? 
	//(goes wrong when loading save)

	public TitledPopUpWithShadow(
			IsInventoryItem IconTrigger,
			String X,
			String Y,
			String title,
			IsPopupContents SetContents){

		Log.info("setting Widget");

		Contents = SetContents;//(isPopupTypeImplementation) SetContents;//we cast to get the needed asWidget functionality. GWT ONLY

		Log.info("setting SourceIcon");

		SourceIcon = IconTrigger; 

		sizeX = X;
		sizeY = Y;
		this.setSize(((Widget)Contents.getVisualRepresentation()).getOffsetWidth()+16+"px", sizeY);

		//DOM.setStyleAttribute(this.getElement(), "zIndex", "1050");
		super.getElement().getStyle().setZIndex(1050);


		this.setAnimationEnabled(true);
		//NOTES:
		//Animation cant currently be used at the same time as box-shadow, due to the way GWT handles animations

		setContentsSelectable(false);


		setupTitleBar(title,SetContents.DRAGABLE());
		setupContainer();


	}

	/** 
	 * 
	 * 
	 * @param X - sizeX (will be forced bigger) 
	 * @param Y - sizeY in css units**/

	public TitledPopUpWithShadow(
			IsInventoryItem IconTrigger,
			String X,
			String Y,
			String title,
			Widget SetContents,
			boolean draggable) {

		Log.info("setting Widget 2");

		//as we have been given a widget we need to make a really basic isPopUpType from it


		Contents = new DefaultIsPopUpType(SetContents,draggable);

		Log.info("setting SourceIcon 2");
		SourceIcon = IconTrigger;

		sizeX = X;
		sizeY = Y;

		this.setSize(((Widget)Contents.getVisualRepresentation()).asWidget().getOffsetWidth()+16+"px", sizeY);

		//DOM.setStyleAttribute(this.getElement(), "zIndex", "1050");
		super.getElement().getStyle().setZIndex(1050);


		this.setAnimationEnabled(true);
		super.setAnimationEnabled(true);
		super.setAnimationType(AnimationType.CENTER);
		setContentsSelectable(false);

		setupTitleBar(title,draggable);
		//set to Z ordering to high, as this should be ontop of everything!
		//	this.getElement().setAttribute("STYLE", "z-index: 100");


		setupContainer();

	}



	/** If you just supply the contents with nothing else we we make the most minimal implementation we can, 
	 * not using the normal contents structure nor having a popup */
	public TitledPopUpWithShadow(IsPopupContents setContents) {

		Contents = setContents;//new DefaultIsPopUpType(setContents,false);
		
		super.getElement().getStyle().setZIndex(1050);

		this.setAnimationEnabled(true);
		super.setAnimationEnabled(true);
		super.setAnimationType(AnimationType.CENTER);
		setContentsSelectable(false);

		super.setWidget((Widget) Contents.getVisualRepresentation());
		


	}

	/**
	 * adds and removes the unselectable css class.
	 * 
	 * @param b
	 */
	public void setContentsSelectable(boolean b) {
		if (b){
			super.removeStyleName("unselectable");
		} else {
			super.addStyleName("unselectable");
		}


	}

	private void setupContainer() {
		//set up shadows
		//set border shadows for title
		Container.setCellPadding(0);
		Container.setCellSpacing(0);
		Container.getCellFormatter().setStyleName(0, 0, TOP_LEFT_BACKSTYLE);

		Container.getCellFormatter().setWidth(0, 0, "8px");
		Container.getCellFormatter().setHeight(0, 0, "21px");

		Container.setWidget(0, 1, TopBar);

		Container.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);

		Container.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		Container.getCellFormatter().setStyleName(0, 1, TOP_MIDDLE_BACKSTYLE);
		Container.getCellFormatter().setHeight(0, 1, "21px");
		Container.getCellFormatter().setStyleName(0, 2, TOP_MIDDLE_BACKSTYLE);
		Container.getCellFormatter().setHeight(0, 2, "21px");

		Container.getCellFormatter().setWidth(1, 0, "8px");
		Container.getCellFormatter().setStyleName(1, 0, MIDDLE_LEFT_BACKSTYLE);

		//


		// load contents into HTML
		Widget contentsAsWidget = (Widget) Contents.getVisualRepresentation();
		Container.setWidget(1, 1, contentsAsWidget);
		//System.out.println("co");

		Container.getCellFormatter().setStyleName(1, 1, "Backstyle");
		//Container.getCellFormatter().setStyleName(1, 1, "popup_border");

		if (!(contentsAsWidget.getStylePrimaryName() == null))
		{		Container.getCellFormatter().setStyleName(1, 1, contentsAsWidget.getStylePrimaryName()); //why did this use to be 1,2?
		}
		Container.getCellFormatter().setStyleName(1, 2, MIDDLE_RIGHT_BACKSTYLE);

		Container.getCellFormatter().setWidth(2, 0, "8px");
		Container.getCellFormatter().setHeight(2, 0, "8px");
		Container.getCellFormatter().setStyleName(2, 0, BOTTOM_LEFT_BACKSTYLE);


		Container.getCellFormatter().setHeight(2, 1, "8px");
		Container.getCellFormatter().setStyleName(2, 1, BOTTOM_MIDDLE_BACKSTYLE);

		Container.getCellFormatter().setWidth(2, 2, "16px");
		Container.getCellFormatter().setHeight(2, 2, "8px");
		Container.getCellFormatter().setStyleName(2, 2, BOTTOM_RIGHT_BACKSTYLE);

		this.setWidget(Container);
	}

	private void setupTitleBar(String title,boolean draggable) {
		//this needs its own topbar set up	
		caption.setText(title);
		caption.setStylePrimaryName("unselectable");
		TopBar.setStylePrimaryName("unselectable");
		TopBar.add(caption);	
		TopBar.add(closeX);

		closeX.setSize("21px", "19px");
		TopBar.setCellHorizontalAlignment(closeX,HasHorizontalAlignment.ALIGN_CENTER);
		TopBar.setStylePrimaryName(TOP_MIDDLE_BACKSTYLE);
		TopBar.setWidth("100%");
		TopBar.setHeight("21px");
		TopBar.add(caption);

		TopBar.setCellWidth(caption, "95%");
		TopBar.add(closeX);
		TopBar.setCellHorizontalAlignment(caption, HasHorizontalAlignment.ALIGN_CENTER);
		TopBar.setCellHorizontalAlignment(closeX, HasHorizontalAlignment.ALIGN_RIGHT);

		//pass this to the close
		final Widget thisone = this;

		closeX.addMouseOverHandler(new MouseOverHandler(){
			public void onMouseOver(MouseOverEvent event) {
				closeX.setPlayForward();
			}

		});
		closeX.addMouseOutHandler(new MouseOutHandler(){
			public void onMouseOut(MouseOutEvent event) {

				closeX.setPlayBack();
			}

		});

		closeX.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				/*
			Widget Sender = (Widget)event.getSource();
			 //InventoryPanel parent = (InventoryPanel)(Sender.getParent().getParent());
			 //parent.CloseDefault();

			 //if the parent is the inventory panel, then we run its close function
			 System.out.print(Sender.getParent().getParent().getClass().getName());
			 String temp = Sender.getParent().getParent().getClass().getName();	

			 System.out.println(temp);		*/


				Log.info("closing popup");

				((hasCloseDefault)thisone).CloseDefault();

				RootPanel.get().remove(JAM.fadeback);


			}
		});
		// this.setText("- Drag Me -");
		//verticalSplit.add(TopBar);
		//this.add(verticalSplit);
		//overlay.setSize("400px", "400px");
		//overlay.setStyleName("overlay");
		//verticalSplit.add(IContents);

		//	DOM.appendChild(this.getElement(), caption.getElement());
		//adopt(caption);
		//caption.setStyleName("Caption");


		//set to dragable if draggable unless not dragable

		//if (((isPopUpType)Contents).DRAGABLE()){
		if (draggable){

			caption.addMouseMoveHandler(this);
			caption.addMouseUpHandler(this);
			caption.addMouseDownHandler(this);
			//handle touch events
			caption.addTouchStartHandler(this);
			caption.addTouchMoveHandler(this);
			caption.addTouchEndHandler(this);

		}

	}

	public void setCenterWidget(IsWidget widget){

		Container.setWidget(1, 1, widget);

		this.setSize( Container.getOffsetWidth()+16+"px", sizeY);

	}

	@Override
	public boolean onEventPreview(Event event) {
		// We need to preventDefault() on mouseDown events (outside of the
		// DialogBox content) to keep text from being selected when it
		// is dragged.
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			if (DOM.isOrHasChild(caption.getElement(), DOM
					.eventGetTarget(event))) {
				DOM.eventPreventDefault(event);
			}
		}

		if (DOM.eventGetType(event) == Event.ONTOUCHSTART) {
			if (DOM.isOrHasChild(caption.getElement(), DOM
					.eventGetTarget(event))) {
				DOM.eventPreventDefault(event);
			}
		}


		return super.onEventPreview(event);
	}



	public String POPUPTYPE() {
		return Contents.POPUPTYPE();
	}
	public boolean POPUPONCLICK() {
		return true;
	}
	public void RecheckSize() {

		//remove background, this popup specticaly dosnt use it
		RootPanel.get().remove(JAM.fadeback);

		//remove shadow frame, as it doesn't use that either
		RootPanel.get().remove(this.getParent());


		//we can asign this now as being attached
		//	 MyApplication.overlayPopUpsOpen.add(this);


		this.center();
		
	}

	
	//--- ensure CloseDefault is run even when auto closing;	
	@Override
	public void hide() {
		//as close also triggers hide, we use isShowing to ensure wwe dont run these two things constnatly in a loop
		if (super.isShowing()){			
			super.hide();
			CloseDefault(true);
		}
	}

	@Override
	public void hide(boolean autoClosed) {
		//as close also triggers hide, we use isShowing to ensure we dont run these two things constantly in a loop		
		if (super.isShowing()){
			super.hide(autoClosed);
			CloseDefault(true);
		}
	}
	//---

	public void CloseDefault() {
		CloseDefault(true);
	}
	public void CloseDefault(boolean runCloseDefaultChildren) {
		JAMcore.popupPanelCurrentlyOpen.remove(this);
		//RootPanel.get().remove(this);
		if (!(SourceIcon==null)){
			//((IsInventoryIcon)SourceIcon).setPopedUp(false);

			SourceIcon.setPopedUp(false);
		}

		super.hide(false); //should be before close default, as inner CloseDefault might foolishly retrigger this function and we dont want to get caught ina loop

		try {
			if ((Contents!=null) &&  runCloseDefaultChildren){
				Log.info("trying to run hasCloseDefault:");
				((hasCloseDefault)Contents).CloseDefault();		
			}
		} catch (Exception e) {

			// probably hasnt got a hasCloseDefault implementation
			//should test for this but dont know how
			//e.printStackTrace();
			Log.info("exception catched:"+e.getLocalizedMessage());
			Log.info("possibly has no CloseDefault implementation?");
			
		}

		
		if (RunOnClose!=null){
			Log.info("Running run on close");
			RunOnClose.run();
			RunOnClose=null;
		}

		//always open this when a panel is opened;
		//if nothing else is open close the box
		//  if (MyApplication.overlayPopUpsOpen.size()<1){
		//    MyApplication.closeallwindows.setAnimateClose();
		//    }
		//-
		//if nothing else is open close the box
		if (JAMcore.popupPanelCurrentlyOpen.size()<1){
			if (JAM.ClueReveal.isOpen()==false){
				//	Window.alert("closeing window close");
				JAM.closeallwindows.setPlayBack();
			}
		}
		//-


	}
	public void fixedZdepth (int setdepth)
	{
		fixed_zindex = true;
		fixed_zindex_value = setdepth;

		//DOM.setStyleAttribute(this.getElement(), "z-index", ""+(fixed_zindex_value));
		//DOM.setStyleAttribute(this.getElement(), "zIndex", ""+(fixed_zindex_value));
		super.getElement().getStyle().setZIndex(fixed_zindex_value);


	}

	public void fixedZdepthOff ()
	{
		fixed_zindex = false;


	}
	
	public void OpenDefault() {

		JAMcore.GameLogger.info("opening popup");
		Log.info("opening popup");

		int z=0;
		String tempz = " zdepth=first pop";
		JAMcore.z_depth_max=MiniumZIndex;
		//first we check over all the current open popups and their z-depth;
		Iterator<IsPopupPanel> popupIter = JAMcore.popupPanelCurrentlyOpen.iterator();
		
		JAM.Feedback.getInternalWidget().setTitle("a");
		if (JAMcore.popupPanelCurrentlyOpen.size()>0){
			
			while ( popupIter.hasNext() )
			{
				IsPopupPanel cur = popupIter.next();
				
				JAM.Feedback.getInternalWidget().setTitle("b");
				
				//in gwt we know its a widget of some sort
				if ( ((Widget)cur).getElement().getStyle().getProperty("z-index")==null){
					//z =  Integer.parseInt(cur.getElement().getStyle().getProperty("zIndex"));
				} else {
					z =  Integer.parseInt( ((Widget)cur).getElement().getStyle().getProperty("z-index"));
				}

				JAM.Feedback.getInternalWidget().setTitle("cz="+z);

				if (z>JAMcore.z_depth_max) {
					JAMcore.z_depth_max=z;
				}
				tempz = tempz +"\n z depth = "+z;

			}
			//	MyApplication.Feedback.setTitle("a max depth set to "+MyApplication.z_depth_max);

			//	} 
			//MyApplication.Feedback.setText("=-=");
			//	if ((Integer.parseInt(MyApplication.fadeback.getElement().getStyle().getProperty("z-index")))>MyApplication.z_depth_max)
			//	{
			//		MyApplication.Feedback.setText("b");
			//	MyApplication.z_depth_max=Integer.parseInt(MyApplication.fadeback.getElement().getStyle().getProperty("z-index"));
			//
			//		MyApplication.Feedback.setText("b max depth set to "+MyApplication.z_depth_max);


		} else {
			//we can reset the max
			JAMcore.z_depth_max=MiniumZIndex;
			//	MyApplication.Feedback.setText("c max depth set to "+MyApplication.z_depth_max);

		}


		//if the backfader is in front for some reason, we use thats zdepth instead
		if (centered)
		{
			this.center();
		} else {
			this.show();
		}

		try {
			Log.info("running popup open default");

			((hasOpenDefault) this.Contents).OpenDefault();
		} catch (Exception e) {
			//in case its not got a openDefault
			Log.info("not got hasOpenDefault");

		}



		//detect if its outside the top edge of the screen, and if so, align to the top.

		int this_x = this.getAbsoluteLeft();
		int this_height = this.getOffsetHeight();
		if (this_height>Window.getClientHeight()){
			this.setPopupPosition(this_x, 0);
		};





		//set this ones zdepth to max+2 (plus1 might be reserved for the fader)
		DOM.setStyleAttribute(this.getElement(), "z-index", ""+(JAMcore.z_depth_max+1));
		DOM.setStyleAttribute(this.getElement(), "zIndex", ""+(JAMcore.z_depth_max+1));
		JAMcore.z_depth_max=JAMcore.z_depth_max+1;


		JAMcore.popupPanelCurrentlyOpen.add(this);

	}


	public boolean DRAGABLE() {
		return false;
	}
	public boolean MAGNIFYABLE() {
		return false;
	}


	public void onMouseDown(MouseDownEvent event) {

		int x = event.getX();
		int y = event.getY();

		Log.info("onMouseDown");
		onMouseOrTouchStart(x, y);
		//DOM.setStyleAttribute(this.getElement(), "Zindex", ""+(MyApplication.z_depth_max+1));

	}

	private void onMouseOrTouchStart(int x, int y) {
		dragging = true;
		DOM.setCapture(caption.getElement());
		dragStartX = x;
		dragStartY = y;

		//set to top
		//set this ones zdepth to max+1

		if (fixed_zindex == false){        
			Log.info("setting ZIndexTop");
			setZIndexTop();
		}
	}

	@Override
	public void onTouchStart(TouchStartEvent event) {

		int x=event.getChangedTouches().get(0).getRelativeX(event.getRelativeElement());
		int y=event.getChangedTouches().get(0).getRelativeY(event.getRelativeElement());

		Log.info("onTouchStart "+x+","+y);

		onMouseOrTouchStart(x, y);
	}

	public void setZIndexTop() {
		DOM.setStyleAttribute(this.getElement(), "z-index", ""+(JAMcore.z_depth_max+1));
		DOM.setStyleAttribute(this.getElement(), "zIndex", ""+(JAMcore.z_depth_max+1));
		Log.info("setZIndexTop");
		JAM.Feedback.getInternalWidget().setTitle("z set too"+DOM.getStyleAttribute(this.getElement(),  "z-index"));

		JAMcore.z_depth_max=JAMcore.z_depth_max+1;
	}



	@Override
	public void onTouchMove(TouchMoveEvent event) {

		int x=event.getChangedTouches().get(0).getRelativeX(event.getRelativeElement());
		int y=event.getChangedTouches().get(0).getRelativeY(event.getRelativeElement());


		onMouseOrTouchMove(x, y);
	}


	public void onMouseMove(MouseMoveEvent event) {

		int x=event.getX();
		int y=event.getY();


		onMouseOrTouchMove(x, y);
	}

	private void onMouseOrTouchMove(int x, int y) {
		if (dragging) {
			int absX = x + getAbsoluteLeft();
			int absY = y + getAbsoluteTop();

			if ((absY - dragStartY)<0){
				setPopupPosition(absX - dragStartX,0);
			} else 
			{
				setPopupPosition(absX - dragStartX, absY - dragStartY);
			}
		}
	}




	public void onMouseUp(MouseUpEvent event) {
		dragging = false;
		DOM.releaseCapture(caption.getElement());
	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {

		Log.info("onTouchEnd");
		dragging = false;
		DOM.releaseCapture(caption.getElement());
	}


	public void clearBackgroundStyles() {

		Log.info("clearing all styles on popup");

		BOTTOM_RIGHT_BACKSTYLE = "";
		BOTTOM_MIDDLE_BACKSTYLE = "";
		BOTTOM_LEFT_BACKSTYLE = "";
		MIDDLE_LEFT_BACKSTYLE = "";
		MIDDLE_RIGHT_BACKSTYLE="";
		TOP_MIDDLE_BACKSTYLE = "";
		TOP_LEFT_BACKSTYLE = "";

		super.setStyleName("");

		setupContainer(); 



	}

	public void setTitlebBarText(String title){
		caption.setText(title);

	}

	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}

	@Override
	public void add(IsPopupContents popupMenuBar) {
		this.add((Widget)popupMenuBar.getVisualRepresentation()); //popup contents visual representation must be a widget in gwt implementations 

	}

	/**
	 * A clone of GWTs setPopupPositionAndShow, to allow the use of JamPositionCallback interface instead
	 * (and thus can be called from non-gwt code
	 */
	@Override
	public void setPopupPositionAndShow(final JamPositionCallback callback) {				
		this.setPopupPositionAndShow(new PositionCallback() {			
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				callback.setPosition(offsetWidth, offsetHeight);

			}
		});

	}

	@Override
	public void addRunOnClose(Runnable runnable) {
		this.RunOnClose = runnable;
	}

	@Override
	public void setCaptionText(String mainGame_Inventory) {
		caption.setText(mainGame_Inventory);
		
	}


}
