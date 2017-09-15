package com.lostagain.JamGwt.InventoryObjectTypes;

import com.google.gwt.user.client.ui.PopupPanel;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

/**  a dummy object is simply one that does not popup and purely exists in the inventory for mixing and examineing **/
	  public class DummyPopUp  extends PopupPanel  implements hasCloseDefault, isInventoryItemImplementation,IsInventoryItemPopupContent
	  {
		// Type flag
		   static final String POPUPTYPE = "Dummy";
		  
		 //title 
		   static String TITLE = "";
		   
			 
			 
		  
		  public DummyPopUp(String Title)
		  {
			  TITLE = Title;
			//contains nothing at all!
			  
		  }
		  
		  public void CloseDefault(){		
			 
			 
			  
			  
		  }
		
		  public String POPUPTYPE() {
				return "Dummy";
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
			public void RecheckSize() {
				
			}

			@Override
			public String getState() {
				return null;
			}

			@Override
			public void loadState(String state) {
				//no state needed				
			}

			@Override
			public Object getVisualRepresentation() {
				return this.asWidget();
			}

			@Override
			public int getExpectedZIndex() {
				return 1;
			}

			@Override
			public void OpenDefault() {
				// TODO Auto-generated method stub
				
			}
	  }
