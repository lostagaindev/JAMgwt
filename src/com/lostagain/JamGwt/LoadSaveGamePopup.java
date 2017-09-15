package com.lostagain.JamGwt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.lostagain.Jam.GameManagementClass;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.SaveMangement.JamSaveGameManager;
import com.lostagain.JamGwt.JargScene.ServerOptions;

import lostagain.nl.spiffyresources.client.spiffygwt.SpiffySaveToLocalStorage;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffySaveToLocalStorage.loadGameRequest;


/** lets the user load a saved game from a selection of sources **/
public class LoadSaveGamePopup  extends TitledPopUpWithShadow  {

	VerticalPanel contents = new VerticalPanel();
	
	
	//save/load local manager
	SpiffySaveToLocalStorage localStoreManager = new SpiffySaveToLocalStorage(SpiffySaveToLocalStorage.interactionmode.loadmode,400, JAMcore.GameName);
	
	//load from text options
	Button loadDataPastedBelow = new Button("Load Data Pasted Below:");
	TextArea loadDataTextbox   = new TextArea();
	
	//purely for debug mode, this will reset the variables without unloading stuff
	//WIP
	 final CheckBox expirementalReset = new CheckBox("Expiremental Reset"); //temp, we will use this to try to test game resetting rather then game clearing and reloading
	//---------    
	
	int width = 500;
	int height = 200;
	
	Label loadingFeedback = new Label("---");
	
	DecoratedTabPanel loadoptions = new DecoratedTabPanel();
	public static ServerOptions SEVER_OPTIONS;
	
	public LoadSaveGamePopup() {
		super(null,"500px", "500px", "Load Saved Game:", new Label(
				"loading..."),true);
	
		Log.info("setting up load savegame popup contents");
		
		loadoptions.setSize("100%", "100%");		
		loadoptions.getTabBar().setStylePrimaryName("Inspector-TabBar");
		loadoptions.add(localStoreManager, " (...From this Browser) ");
		
		SEVER_OPTIONS = new ServerOptions();
		
		if (JAMcore.HasServerSave){
			loadoptions.add((IsWidget) SEVER_OPTIONS.getVisualRepresentation(), " (..From Server) ");
		}
		
		VerticalPanel LoadFromTextPage= new VerticalPanel();
		
		LoadFromTextPage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);	

		LoadFromTextPage.add(loadDataPastedBelow);
		loadDataTextbox.setSize("100%", "200px");		
		LoadFromTextPage.add(loadDataTextbox);
		
		loadoptions.add(LoadFromTextPage, " (..From Text) ");
		
		loadoptions.selectTab(0);
		contents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contents.add(loadoptions);
		 contents.add(loadingFeedback);
		 
		 if (JAMcore.DebugMode){
			contents.add(expirementalReset);
		 }
		 
		 
		//contents.getElement().getStyle().setBackgroundColor("white");
		contents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		Log.info("adding center widget");
		
		
		super.setCenterWidget(contents);
		super.setPixelSize(width, height);
		
		contents.setWidth("100%");
		contents.setHeight("100%");
		contents.setStylePrimaryName("loadsavegameboxinner");
		
		this.getElement().getStyle().setZIndex(50000);
		
		loadDataPastedBelow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String dataToLoad = loadDataTextbox.getText().trim(); //trim it!!!!
				dataToLoad=dataToLoad.substring(JamSaveGameManager.START_OF_GAMEDATA_MARKER.length()); //remove the start marker
				
				
				Log.info("loading data:"+dataToLoad);				
				GwtSaveGameManager.decompressAndLoad(dataToLoad,true);
				
			}
		});
		
		
		//setup what to do when a savegame is clicked
		localStoreManager.setLoadRequested(new loadGameRequest() {			
			@Override
			public void load(String incommingLoadData, String saveGameName) {

				Log.info("restting game:");
				if (expirementalReset.getValue()){
					
					loadingFeedback.setText("expirmental reset running");
										
					GameManagementClass.expirementalReset(); //not hocked up fully yet
					
					
				} else {
					//for now we clear all data
					loadingFeedback.setText("clearing all game data");
					
					GameManagementClass.clearAllGameData();
					

					Log.info("All data cleared, now loading save game.");

				}
				
				loadingFeedback.setText("loading...");
				

				//auto close window?
				JAM.loadGamePopup.CloseDefault(true);

				Log.info("_____");
				Log.info("_________");				
				Log.info("Loading save data:"+incommingLoadData);
				Log.info("_________");
				Log.info("____");
				
				//note; we dont clear the game below as we clear it above in either the normal or experimental ways
				GwtSaveGameManager.decompressAndLoad(incommingLoadData,false); //the decompressor removes the end marker itself (:end)
												
				loadingFeedback.setText("");				
				JamSaveGameManager.lastSaveName = saveGameName; //we remember what we loaded last
				

				//set this game as the last loaded or saved visually
				//(first checking if theres a autosaved version present and if so to highlight that one)
				if (JAM.saveGamePopup.localStoreManager.keyExists(saveGameName+JamSaveGameManager.AUTOSAVEPOSTFIX)){
					JAM.saveGamePopup.localStoreManager.setLastSaveGameOutline(saveGameName+JamSaveGameManager.AUTOSAVEPOSTFIX);
					
				} else {
					JAM.saveGamePopup.localStoreManager.setLastSaveGameOutline(saveGameName);
				}
				
			//	JAM.saveGamePopup.localStoreManager.repopulateOnNextOpen();
				
			}
		});
		
	}
	
	@Override
	public void show(){
		super.show();

	//	Log.info("popping up load game box");
		localStoreManager.populateCurrentStoredData(); //Might be redundant
		
		resize();
	}
	
	/** should be fired after a language change **/
	public void refreshText(){
		
		//sever option text
		SEVER_OPTIONS.refreshText();
		
		setTitlebBarText(GamesInterfaceText.MainGame_LoadSavedGame);
		
		//tabbar text	
		loadoptions.getTabBar().setTabText(0, GamesInterfaceText.MainGame_FromThisBrowser);
		//Note; If there is no saving/loading to server supported then the FromText menu option is earlier and the other option not present at all
		if (JAMcore.HasServerSave){
			loadoptions.getTabBar().setTabText(1, GamesInterfaceText.MainGame_FromServer);
			loadoptions.getTabBar().setTabText(2, GamesInterfaceText.MainGame_FromText);
		} else {
			loadoptions.getTabBar().setTabText(1, GamesInterfaceText.MainGame_FromText);
		}
		
		//set text for manual save string entry
		loadDataPastedBelow.setText(GamesInterfaceText.MainGame_LoadDataPastedBelow);
		
		//set the local browser store widget to use the correct load instructions text
		localStoreManager.setLoadInstructions(GamesInterfaceText.MainGame_ClickBelowToLoadYourGame);
		
	}
	
	public void resize(){
		
			int ScreenSizeX = Window.getClientWidth();
			int ScreenSizeY = Window.getClientHeight();
			
			// app size
			int InventorySizeX = (100 * (ScreenSizeX / 150));

			int saveIconSize = 100;
			
			int NumOfSaves = localStoreManager.getNumberOfSaves();
			int InventorySizeY = saveIconSize + (Math.round(NumOfSaves / (InventorySizeX / saveIconSize))) * saveIconSize;
										
		
			int RInventorySizeX = (int) ((InventorySizeX + (0.5 * saveIconSize)) / saveIconSize)
					* saveIconSize;
			int RInventorySizeY = 20
					+ (int) ((InventorySizeY + (0.5 * saveIconSize)) / saveIconSize)
					* saveIconSize;

			setSize(RInventorySizeX + "px", RInventorySizeY + "px");
			
	}

	
	public void OpenOnServerOptions() {
		OpenDefault();
		
		//set page		
		if (JAMcore.HasServerSave){
			loadoptions.selectTab(1);
		} else {
			//if no server we default to the local save page
			loadoptions.selectTab(0);
		}
		
		
	}
	
	@Override
	public void OpenDefault() {
		super.OpenDefault();
		JAMcore.setIgnoreKeyPresses(true);
		
	}
	
	@Override
	public void CloseDefault(boolean runClosedefaultOnChildren){
		super.CloseDefault(runClosedefaultOnChildren); //TODO: probably should be false
		JAMcore.setIgnoreKeyPresses(false);
		
	}
	
	

}
