package com.lostagain.JamGwt.InventoryObjectTypes;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

	  public class ConceptPopUp  extends PopupPanel  implements hasCloseDefault, isInventoryItemImplementation,IsInventoryItemPopupContent
	  {
		// Type flag
		   static final String POPUPTYPE = "Concept";
		  
		   //verticlan panel
		   VerticalPanel panel = new VerticalPanel();
		   
			//Discription
		    Label Discription = new Label("");
			//main image
			Image Picture = new Image();
			//screen size
			int ScreenSizeX = Window.getClientWidth();
			int ScreenSizeY = Window.getClientHeight();
			// pic size
			 int picX =0;
			 int picY =0;
			 
			 
		  
		  public ConceptPopUp(String ImageDiscription, String Title)
		  {
			  Discription.setText(ImageDiscription);
			//  topBar NewTopBar = new topBar(Title, this);	 	
			  //set style
			  this.setStyleName("DefaultTopBar");
			  
			  //add title
			 // panel.add(NewTopBar);
			 
			  panel.add(Discription);
			  Discription.setWidth("250px");
			  
			  panel.setCellHorizontalAlignment(Discription, HasHorizontalAlignment.ALIGN_CENTER);
			  panel.setStylePrimaryName("picturePopUp");		
			  this.add(panel);
			  
			  
		  }
		  
		  public void CloseDefault(){		
			 
			 
			  
			  
		  }
		  public void RecheckSize(){
			  RootPanel.get().remove(JAM.fadeback);
			  
			 // this.setWidth("400px");
			  //this.center();
			  //this.center();
			  
		  }
		  public String POPUPTYPE() {
				return "Concept";
			}
		  
			public boolean POPUPONCLICK() {
				return true;
			}
			public boolean MAGNIFYABLE() {
				return false;
			}
			public boolean DRAGABLE() {
				return true;
			}

			public String getSourceURL() {
				return null;
			}

			public int sourcesizeX() {
				return 0;
			}

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
