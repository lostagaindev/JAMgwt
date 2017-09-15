package com.lostagain.JamGwt.InventoryObjectTypes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

public class overlayPopUp extends PopupPanel implements MouseListener, IsInventoryItemPopupContent,hasCloseDefault,isInventoryItemImplementation {

	VerticalPanel verticalSplit = new VerticalPanel();

	//private topBar caption = new topBar("Overlay");

	private Label caption = new Label("Overlay (Drag Me)");
	private boolean dragging;
	private int dragStartX, dragStartY;

	// pretty colour base
	HTML overlay = new HTML("<div></div>");
	
	//topbar bits
	 HorizontalPanel TopBar = new HorizontalPanel();
	 Label closeX = new Label("X");

	public overlayPopUp() {

		//this needs its own topbar set up
		/*
		TopBar.add(caption);
		TopBar.add(closeX);
		TopBar.setStylePrimaryName("DefaultTopBar");
		TopBar.setWidth("100%");
		TopBar.setHeight("20px");
		TopBar.add(caption);
		TopBar.setCellWidth(caption, "95%");
		TopBar.add(closeX);
		TopBar.setCellHorizontalAlignment(caption, HasHorizontalAlignment.ALIGN_CENTER);
		TopBar.setCellHorizontalAlignment(closeX, HasHorizontalAlignment.ALIGN_RIGHT);
		closeX.addClickListener(new ClickListener(){
			 public void onClick(Widget Sender){
				 //InventoryPanel parent = (InventoryPanel)(Sender.getParent().getParent());
				 //parent.CloseDefault();
				 
				 //if the parent is the inventory panel, then we run its close function
				 System.out.print(Sender.getParent().getParent().getClass().getName());
				 String temp = Sender.getParent().getParent().getClass().getName();	
				
				 System.out.println(temp);
				 
				
					 ((hasCloseDefault)Sender.getParent().getParent().getParent()).CloseDefault();
					 			 
				//RootPanel.get().remove((Sender.getParent().getParent()));
				
				 
			 }
		  });
		*/
		
		// this.setText("- Drag Me -");
	//	verticalSplit.add(TopBar);
		this.add(verticalSplit);
		overlay.setSize("400px", "400px");
		overlay.setStyleName("overlay");
		verticalSplit.add(overlay);

	//	DOM.appendChild(this.getElement(), caption.getElement());
		//adopt(caption);
		caption.setStyleName("Caption");
		caption.addMouseListener(this);
		
		//set to Z ordering to high, as this should be ontop of everything!
	//	this.getElement().setAttribute("STYLE", "z-index: 100");

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

		return super.onEventPreview(event);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		dragging = true;
		DOM.setCapture(caption.getElement());
		dragStartX = x;
		dragStartY = y;
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (dragging) {
			int absX = x + getAbsoluteLeft();
			int absY = y + getAbsoluteTop();
			setPopupPosition(absX - dragStartX, absY - dragStartY);
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		dragging = false;
		DOM.releaseCapture(caption.getElement());
	}

	public String POPUPTYPE() {
		return "Overlay";
	}
	public boolean POPUPONCLICK() {
		return true;
	}
	public void RecheckSize() {
		
		//remove background, this popup specticaly dosnt use it
		 RootPanel.get().remove(JAM.fadeback);
		 
		 //remove shadow frame, as it dosnt use that either
		 RootPanel.get().remove(this.getParent());
		 
		 
		 //we can asign this now as being attached
		// MyApplication.overlayPopUpsOpen.add(this);
		 
		 
		 this.center();
	}

	public void CloseDefault() {
		
	}

	public boolean DRAGABLE() {
		return false;
	}

	public boolean MAGNIFYABLE() {
		return false;
	}

	@Override
	public String getSourceURL() {
		return null;
	}

	@Override
	public int sourcesizeX() {
		return 0;
	}

	@Override
	public int sourcesizeY() {
		return 0;
	}

	@Override
	public String getState() {
		return null;
	}

	@Override
	public void loadState(String state) {
		
	}
	
	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}

	@Override
	public int getExpectedZIndex() {
		return -1;
	}

	@Override
	public void OpenDefault() {
		// TODO Auto-generated method stub
		
	}

}
