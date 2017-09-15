package com.lostagain.JamGwt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.Interfaces.hasCloseDefault;

public class topBar extends HorizontalPanel {
	  Label Title = new Label("Inventory");
	  HorizontalPanel TopBar = new HorizontalPanel();
	  Image closeX = new Image("GameIcons/cc2xbutton.png");
	
	public topBar(String Name, final Widget Container){
		//set name label
		Title.setText(Name);
		
		//make topbar
		  setStylePrimaryName("DefaultTopBar");
		 
		  setWidth("100%");
		  setHeight("20px");
		  add(Title);
		  setCellWidth(Title, "95%");
		  add(closeX);
		  setCellHorizontalAlignment(Title, HasHorizontalAlignment.ALIGN_CENTER);
		  setCellHorizontalAlignment(closeX, HasHorizontalAlignment.ALIGN_RIGHT);
		  closeX.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
				 //InventoryPanel parent = (InventoryPanel)(Sender.getParent().getParent());
				 //parent.CloseDefault();
				 
					Widget Sender = (Widget) event.getSource();
					
				 //if the parent is the inventory panel, then we run its close function
				 System.out.print(Sender.getParent().getParent().getClass().getName());
				 String temp = Sender.getParent().getParent().getClass().getName();	
				
				 System.out.println(temp);
				 
				
					 ((hasCloseDefault)Container).CloseDefault();
					 			 
				//RootPanel.get().remove((Sender.getParent().getParent()));
				
				 
			 }
		  });
		
	}

	
	
}