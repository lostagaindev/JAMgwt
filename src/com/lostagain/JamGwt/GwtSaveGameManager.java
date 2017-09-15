package com.lostagain.JamGwt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

//import org.dellroad.lzma.client.CompressionMode;
//import org.dellroad.lzma.client.LZMAByteArrayCompressor;
//import org.dellroad.lzma.client.LZMAByteArrayDecompressor;
//import org.dellroad.lzma.client.UTF8;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.GameManagementClass;
import com.lostagain.Jam.GameVariableManagement;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.OptionalImplementations;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.SceneAndPageSet;
import com.lostagain.Jam.ScoreControll;
import com.lostagain.Jam.GwtLegacySupport.secretsPanelCore.linkItem;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Interfaces.JamChapterControl;
import com.lostagain.Jam.Interfaces.hasVisualRepresentation;
import com.lostagain.Jam.SaveMangement.HasBrowserStorageMethod;
import com.lostagain.Jam.SaveMangement.HasCompressionSystem;
import com.lostagain.Jam.SaveMangement.JamSaveGameManager;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.audio.MusicBoxCore;
import com.lostagain.JamGwt.JargScene.ServerOptions;
import com.lostagain.JamGwt.Sprites.InternalAnimations;

import lostagain.nl.spiffyresources.client.SpiffyGWTLzma;
import lostagain.nl.spiffyresources.client.SpiffyGWTLzma.StringResult;
import lostagain.nl.spiffyresources.client.SpiffyGWTLzma.UpdateRunnable;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffySaveToLocalStorage;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffySaveToLocalStorage.RemoveSaveRequested;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffySaveToLocalStorage.savegamerequest;

/** 
 * Manages all saving of game data.
 * Game data can be saved in the following ways;
 * - To a sever (via php)
 * - To a browser html5 storage
 * - To a text string
 * - To a link 
 * 
 * All forms of saving use the same serialization method.
 * 
 * Additionally this class provides a visual widget for the user to select save options **/
public class GwtSaveGameManager extends JamSaveGameManager implements hasVisualRepresentation,
HasCompressionSystem,
HasBrowserStorageMethod { 

	public static Logger Log = Logger.getLogger("JAM.SaveGameManager");

	TitledPopUpWithShadow contentPanel;// = new TitledPopUpWithShadow();



	//save/load local manager (this saves to the browser)
	SpiffySaveToLocalStorage localStoreManager = new SpiffySaveToLocalStorage(SpiffySaveToLocalStorage.interactionmode.savemode,450, JAMcore.GameName);

	//This provides server user options, including saving to the webbserver
	public static ServerOptions SEVER_OPTIONS = new ServerOptions();

	DecoratedTabPanel saveoptions = new DecoratedTabPanel();

	/** for displaying the uncompressed raw save string (for debugging only)**/
	public static TextArea databox= new TextArea();


	/** for displaying the compressed encoded string (allowing player to save to clipboard/notepad etc**/
	static TextArea saveToTextBox = new TextArea();

	static VerticalPanel contents = new VerticalPanel();

	static HorizontalPanel linkAndUpdate = new HorizontalPanel();	
	static HorizontalPanel loadAndReset = new HorizontalPanel();

	static Label linkdoesntworkiniewarninglabel = new Label("Note; InternetExplorer is unable to load savegames from links due to it lacking support for urls this long. If you wish to save this way, please load your links on browsers that do like Opera,Firefox,Chrome or Safari");


	static Button updateBut = new Button("Update link from box");
	static Button emailLink = new Button("Email link to me");


	static Anchor compressedlink = new Anchor();


	static Button loadData = new Button("Load Box Data Into game");
	static CheckBox resetGameFirst = new CheckBox("reset first");


	static Button gsData = new Button("Save Data To Box");

	static Button rdData = new Button("Reset Data");

	//static SaveStateManager newpop = new SaveStateManager();



	public static String current_save_string = "";



	Label saveDataPastedBelow = new Label("This big string contains your savegame, you can copy and save this whereever you wish");

	private static native String getLocation() /*-{ 
	   return $wnd.location.href; 

	}-*/;

	/** manages the saving and loading of game states.
	 * The loadsavegamepopup uses functions from here to load the game, as well as any auto-loading from the url
	 *  **/
	public GwtSaveGameManager() {


		contentPanel = new TitledPopUpWithShadow(null,"400px", "auto", "Same Your Game;", new Label("loading..."),true){
			@Override
			public void CloseDefault(){
				super.CloseDefault();

				//when closing we enable the JAMs games keypress sensing 
				JAMcore.setIgnoreKeyPresses(false);

			}
		};

		//crude fix for size being wrong above when no server options are present
		contentPanel.getElement().getStyle().setProperty("maxWidth", "500px");

		Log.info("setting up save manager");

		//fill data in
		databox.setText("loading...");

		contents.clear();

		contents.setStylePrimaryName("loadsavegameboxinner");
		contents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contents.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);


		Log.info("setting up link and update");



		updateBut.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				updateLink();


			}

		});

		/** This sets what happens when we request to save to the browsers local storage **/
		localStoreManager.setSaveRequested(new savegamerequest() {			
			@Override
			public void save(String SaveOverThisKeyRequested) {

				SaveToLocalStorageRequested(SaveOverThisKeyRequested);

			}
		});

		localStoreManager.setRemoveSaveRequested(new RemoveSaveRequested() {

			@Override
			public void remove(String key) {

				Log.info("remove savegame:"+key);

				confirmSaveDelete(key);

			}
		});

		//what to do when the email button is pressed
		emailLink.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {


				Log.severe("sending email");
				askForEmailToSendLink();


			}
		});

		Log.info("adding to contents");


		//contents.add(saveOptionLAbel);

		//create the save option pages (one on each tab)

		//the page for the weblink
		VerticalPanel weblinkpanel = new VerticalPanel();

		linkAndUpdate.setSpacing(5);		
		linkAndUpdate.add(compressedlink);

		//add email option if not on local mode
		if (JAMcore.LocalFolderLocation.length()<3){
			linkAndUpdate.add(emailLink);		
		}

		if (JAMcore.DebugMode){
			linkAndUpdate.add(updateBut);
		}

		weblinkpanel.setHeight("200px");
		weblinkpanel.setWidth("100%");
		weblinkpanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		weblinkpanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		weblinkpanel.add(linkAndUpdate);


		weblinkpanel.add(linkdoesntworkiniewarninglabel);

		//the page for the server save settings

		VerticalPanel serverpanel = new VerticalPanel();
		serverpanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		serverpanel.setHeight("200px");
		serverpanel.setWidth("100%");
		serverpanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Log.info("creating SEVER_OPTIONS");
		//SEVER_OPTIONS = new ServerOptions();


		Log.info("adding SEVER_OPTIONS");		
		serverpanel.add((Widget) SEVER_OPTIONS.getVisualRepresentation());


		VerticalPanel saveToTextPage = new VerticalPanel();
		saveToTextPage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);		
		saveToTextPage.setHeight("200px");
		saveToTextPage.setWidth("100%");
		saveToTextPage.add(saveDataPastedBelow);
		saveToTextPage.setCellHeight(saveDataPastedBelow, "40px");
		saveDataPastedBelow.setSize("100%", "40px");
		saveToTextBox.setSize("100%", "100%");	

		saveToTextBox.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				saveToTextBox.selectAll();
			}
		});

		saveToTextBox.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {

				JAMcore.setIgnoreKeyPresses(true);

			}
		});

		saveToTextBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				JAMcore.setIgnoreKeyPresses(false);

			}
		});


		saveToTextPage.add(saveToTextBox);



		/*
		Button popupControlPanel = new Button("control panel");

		popupControlPanel.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("opening control panel");
				JAM.openControlPanel(); 
			}
		});




		serverpanel.add(popupControlPanel);
		 */

		Log.info("creating tabs and adding save options");

		saveoptions.getTabBar().setStylePrimaryName("Inspector-TabBar");

		//save option tabs. Note the order must be kept for the text change on them to work
		saveoptions.add(localStoreManager, " (...to This Browser) ");
		saveoptions.add(weblinkpanel, " (..as Web Link) ");

		if (JAMcore.HasServerSave){
			saveoptions.add(serverpanel, " (..on Server) ");
		}

		saveoptions.add(saveToTextPage, " (..as Text) ");

		saveoptions.selectTab(0);

		contents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		contents.add(saveoptions);	

		//contents.add(linkAndUpdate);

		Log.info("setting up loadAndReset");

		loadAndReset.add(loadData);

		Log.info("setting up loadAndReset and other buttons");
		loadAndReset.add(resetGameFirst);
		loadAndReset.add(gsData);
		loadAndReset.add(rdData);

		Log.info("adding loadAndReset because we are on debug mode");

		if (JAMcore.DebugMode){
			contents.add(databox);
			contents.add(loadAndReset);
		}


		//contents.add(localStoreManagerLabel);
		//contents.add(localStoreManager);

		Log.info("style stuff");
		contents.setHeight("100%");
		
		databox.setHeight("400px");
		databox.setWidth("500px");
		
		databox.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {

				JAMcore.setIgnoreKeyPresses(true);

			}
		});

		databox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				JAMcore.setIgnoreKeyPresses(false);

			}
		});

		Log.info("adding to widget in centert");

		contentPanel.setCenterWidget(contents);

	//	contentPanel.setWidth("500px");
		//newpop.setWidth("400px");

		//display		

		//String dataURLsafe = URL.encode(linkdata);

		//link.setHref("#LoadGameData="+dataURLsafe+"\n:end");
		//link.setText("(old link)");
		compressedlink.setText("(-- Game Saved As Link --)");
		compressedlink.setTarget("_blank");

		//fix the z-index as pretty much nothing should be higher then the save box
		contentPanel.getElement().getStyle().setZIndex(50000);
		//add click handlers
		gsData.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				String SaveString = CreateSaveString();

				//add the scene data at the end

				databox.setText(SaveString);

			}	



		});
		rdData.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				GameManagementClass.clearAllGameData();

			}	



		});

		loadData.addClickHandler(new ClickHandler() {			

			@Override
			public void onClick(ClickEvent event) {

				String commands = databox.getText()+"\n"; 

				Log.info("loading commands = "+commands);

				loadData.setText("processing commands:"+commands.length());

				JAM.loadGameData(commands,resetGameFirst.getValue());

				loadData.setText("done click to reload box");

				//disable load button		
				// deseraliseAndRestoreStateFromString(databox.getText());	

			}

		});

	}

	protected void confirmSaveDelete(final String SaveGameName) {

		Runnable onConfirm = new Runnable(){
			@Override
			public void run() {

				localStoreManager.removeSave(SaveGameName);

			}				
		};

		askForConfirm(SaveGameName,onConfirm,"Do you wish to delete ?");


	}

	public String getLoadableink(String SceneSaveString){

		//prepare initial functions (standard non-scene gama data, inventory items etc)

		//remove all newlines from savestring

		return "";

	}



	private void SaveToLocalStorageRequested(String saveNameRequested) {

		final String SaveGameName = saveNameRequested;

		Log.info("SaveToLocalStorageRequested with key:"+SaveGameName);

		if (SaveGameName==null || SaveGameName==""){
			//No name has been supplied so we should ask for one (the askForFile.. command will refire this set of commands with SaveGameName filled in) 
			String defaultname = JAMcore.usersCurrentLocation ;//""+ localStoreManager.getNumberOfSaves()+" - ("+JAM.location+")";

			askForFileNameToCreateSave(defaultname);

			return;

		} 

		//test if the key is an existing savegame name and if so trigger confirm dialogue
		if (localStoreManager.keyExists(saveNameRequested)){

			Log.info("key already exists");			
			Runnable onConfirm = new Runnable(){
				@Override
				public void run() {
					String SaveGaveData = current_save_string; //this string should be updated every time a save is generated. (so, same as the link)

					//localStoreManager.SaveData(SaveGameName, SaveGaveData, true);
					saveGameToBrowserImpl(SaveGameName, SaveGaveData, true);
					
					
					lastSaveName = SaveGameName;
				}				
			};

			askForConfirm(SaveGameName,onConfirm,"Do you wish to overwrite ?");

			return;
		}



		String SaveGaveData = current_save_string; //this string should be updated every time a save is generated. (so, same as the link)
		saveGameToBrowserImpl(SaveGameName, SaveGaveData, true);
		lastSaveName = SaveGameName;
		//	localStoreManager.SaveData(SaveGameName, SaveGaveData, true);
	}

	private void askForConfirm(final String SaveGameName,final Runnable onConfirm,final String message) {

		final PopupPanel askForConfirm = new PopupPanel();
		askForConfirm.addStyleName("SaveIconBack");


		Button cancel = new Button(GamesInterfaceText.MainGame_GeneralCancel);
		Button ok = new Button(GamesInterfaceText.MainGame_GeneralOk);

		//give the buttons a height (shouldn't be needed but something goes wrong on some systems where it becomes really small
		cancel.setHeight("50px");
		ok.setHeight("50px");

		ok.addClickHandler(new ClickHandler() {					
			@Override
			public void onClick(ClickEvent event) {
				onConfirm.run();
				askForConfirm.hide();

				//disable keypress's (remember to reenable when using this!)
				JAMcore.setIgnoreKeyPresses(false);

			}
		});

		cancel.addClickHandler(new ClickHandler() {					
			@Override
			public void onClick(ClickEvent event) {
				askForConfirm.hide();

				//disable keypress's (remember to reenable when using this!)
				JAMcore.setIgnoreKeyPresses(false);

			}
		});

		HorizontalPanel OkCancel = new HorizontalPanel();		
		OkCancel.setSpacing(5);
		OkCancel.add(ok);
		OkCancel.add(cancel);		

		VerticalPanel contents = new VerticalPanel();
		contents.setSpacing(5);

		contents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		contents.add(new Label(message));
		contents.add(OkCancel);

		askForConfirm.setPixelSize(300, 200);

		askForConfirm.add(contents);			

		askForConfirm.setGlassEnabled(true);
		askForConfirm.setModal(true);
		//also make sure it gets native events. So Nothing else but this popup works
		//(if we dont do this keyboard events might also trigger on the scene when you type)
		askForConfirm.setPreviewingAllNativeEvents(true);

		//style!
		askForConfirm.getElement().getStyle().setZIndex(999999999);




		askForConfirm.center();
	}


	private void askForEmailToSendLink() {

		//the panel which will ask for the new clones name
		final PopupPanel askForName = new PopupPanel();


		//contents of the above panel
		VerticalPanel askForNameContents = new VerticalPanel();
		final TextBox nameBox = new TextBox();
		nameBox.setText("");		

		Button cancel = new Button("Cancel");
		Button ok = new Button("Send Email");

		cancel.setHeight("50px");
		ok.setHeight("50px");
		//compressedlink.getHref()
		//the contents:

		String savecontents = compressedlink.getHref();

		//remove the end string (we need to re-add it after splitting, as it shouldnt be spread over multiple lines)
		savecontents = savecontents.substring(0, savecontents.length()-END_OF_GAMEDATA_MARKER.length());

		String url = savecontents.substring(0,savecontents.indexOf("#")+1);
		String data = savecontents.substring(savecontents.indexOf("#")+1);

		//encode it once
		data = URL.encodeQueryString(data);

		//Note; Emails are fussy. They the content cant have any lines over a certain length. My server was 550chars per line max.
		//But some are as low as 70!
		//In order to help deal with this, we split the data into newlines. (note; if done with the url you might get corruption issues with the spaces)
		ArrayList<String> databits = SpiffyGWTLzma.splitToBitsOfLength(data, 70); //we can make use of the "split to bits" function in the compressor for this

		//now we have an array for 70 character chunks, we reassemble them with newlines between! 
		data = "\n\r";
		for (String bit : databits) {
			data=data+bit+"\n\r";
		}
		//ok now that should have given us a data string thats divided into newlines of at most 70 chars
		data=data+END_OF_GAMEDATA_MARKER;

		Log.info("sending data:"+data);


		//but now we have to encode it for a url....twice! (as php will auto remove the first, but the email itself still needs it!)
		//PHP 5.5 no longer needs this (Earlier versions of PHP still do)
		savecontents = url+data;//URL.encodeQueryString(data);

		final String Link = "<a href=\""+savecontents+"\"> \r\n (Your Save Game Link) \r\n </a>";

		final String message = "<html>\r\n<head>\r\n(note; you may have to tell your email program to view the full message for this link to work)\r\n<title>Your SaveGame:</title>\r\n</head>\r\n<body>\r\n"+Link+"\r\n</body></html>\r\n";	


		//key down function, used to detect "enter" being hit on the new name
		nameBox.addKeyDownHandler(new KeyDownHandler() {					
			@Override
			public void onKeyDown(KeyDownEvent event) {

				//enter is KeyCode 13.
				//Its been that way since my Commodore Plus/4! 
				//I could also use KeyCodes.KEY_ENTER instead of writing 13, but thats less oldschool cool
				if (event.getNativeKeyCode()==13 ){
					String emailReq = nameBox.getText();
					if (emailReq.length()>3 && emailReq.contains("@")){

						JAM.sendEmail(emailReq,"Your Save Game Link","TheJamGameEngine", message,true);
						emailLink.setText(GamesInterfaceText.MainGame_EmailSent);

						askForName.hide();

						//disable keypress's (remember to reenable when using this!)
						JAMcore.setIgnoreKeyPresses(false);

					}
				}
				//if they hit enter to cancel.
				//not so cool here, I used the Keycode key_escape as I couldn't remember the number for it
				if (event.getNativeKeyCode()==KeyCodes.KEY_ESCAPE){

					askForName.hide();

					//disable keypress's (remember to reenable when using this!)
					JAMcore.setIgnoreKeyPresses(false);
				}


			}
		});
		ok.addClickHandler(new ClickHandler() {					
			@Override
			public void onClick(ClickEvent event) {

				String emailReq = nameBox.getText();
				if (emailReq.length()>3 && emailReq.contains("@")){ //minimum 2 letters

					JAM.sendEmail(emailReq,"Your Save Game Link","TheJamGameEngine", message,true);
					emailLink.setText(GamesInterfaceText.MainGame_EmailSent);
					askForName.hide();

					//disable keypress's (remember to reenable when using this!)
					JAMcore.setIgnoreKeyPresses(false);
				}
			}
		});

		//also we have a cancel button
		cancel.addClickHandler(new ClickHandler() {					
			@Override
			public void onClick(ClickEvent event) {
				askForName.hide();

				//disable keypress's (remember to reenable when using this!)
				JAMcore.setIgnoreKeyPresses(false);
			}
		});


		//contents of popup
		askForNameContents.addStyleName("SaveIconBack");
		askForNameContents.setSize("100%", "100%");

		askForNameContents.add(new Label("Please enter the email :"));
		askForNameContents.add(nameBox);	

		HorizontalPanel OkCancel = new HorizontalPanel();

		OkCancel.add(ok);
		OkCancel.add(cancel);
		askForNameContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		askForNameContents.add(OkCancel);


		askForNameContents.setSpacing(6);
		//add the contents to the popup
		askForName.add(askForNameContents);

		//set Glass & Modal Enabled (this fades the background and prevents anything else working till this 
		//popup goes away)
		askForName.setGlassEnabled(true);
		askForName.setModal(true);

		//also make sure it gets native events. So Nothing else but this popup works
		//(if we dont do this keyboard events might also trigger on the scene when you type)
		askForName.setPreviewingAllNativeEvents(true);

		//style!
		askForName.getElement().getStyle().setZIndex(999999999);
		askForName.getElement().getStyle().setBackgroundColor("white");

		//disable keypress's (remember to reenable when using this!)
		JAMcore.setIgnoreKeyPresses(true);


		//popup at center!
		askForName.center();
		//focus the namebox ready to type! because we are awesome like that!
		nameBox.setFocus(true);
	}


	private void askForFileNameToCreateSave(String defaultname) {

		//the panel which will ask for the new clones name
		final PopupPanel askForName = new PopupPanel();
		askForName.addStyleName("SaveIconBack");

		//contents of the above panel
		VerticalPanel askForNameContents = new VerticalPanel();
		final TextBox nameBox = new TextBox();
		nameBox.setText(defaultname);	
		
		Button cancel = new Button("Cancel");
		Button ok = new Button("Ok");

		//key down function, used to detect "enter" being hit on the new name
		nameBox.addKeyDownHandler(new KeyDownHandler() {					
			@Override
			public void onKeyDown(KeyDownEvent event) {

				//enter is KeyCode 13.
				//Its been that way since my Commadore Plus/4! 
				//I could also use KeyCodes.KEY_ENTER instead of writing 13, but thats less oldschool cool
				if (event.getNativeKeyCode()==13 ){
					String namerequested = nameBox.getText();
					if (namerequested.length()>2){
						SaveToLocalStorageRequested(namerequested);

						askForName.hide();

						//disable keypress's (remember to reenable when using this!)
						JAMcore.setIgnoreKeyPresses(false);
					}
				}
				//if they hit escape to cancel.
				//not so cool here, I used the Keycode key_escape as I couldnt remember the number for it
				if (event.getNativeKeyCode()==KeyCodes.KEY_ESCAPE){

					askForName.hide();

					//disable keypress's (remember to reenable when using this!)
					JAMcore.setIgnoreKeyPresses(false);
				}


			}
		});
		ok.addClickHandler(new ClickHandler() {					
			@Override
			public void onClick(ClickEvent event) {

				String namerequested = nameBox.getText();
				if (namerequested.length()>2){ //minimum 2 letters
					SaveToLocalStorageRequested(namerequested);

					askForName.hide();

					//disable keypress's (remember to reenable when using this!)
					JAMcore.setIgnoreKeyPresses(false);

				}
			}
		});

		//also we have a cancel button
		cancel.addClickHandler(new ClickHandler() {					
			@Override
			public void onClick(ClickEvent event) {
				askForName.hide();

				//disable keypress's (remember to reenable when using this!)
				JAMcore.setIgnoreKeyPresses(false);

			}
		});


		//contents of popup
		askForNameContents.add(new Label("Please enter name of new savegame;"));
		askForNameContents.add(nameBox);	

		HorizontalPanel OkCancel = new HorizontalPanel();

		OkCancel.add(ok);
		OkCancel.add(cancel);

		askForNameContents.add(OkCancel);


		askForNameContents.setSpacing(6);
		//add the contents to the popup
		askForName.add(askForNameContents);
		//set Glass & Modal Enabled (this fades the background and prevents anything else working till this 
		//popup goes away)
		askForName.setGlassEnabled(true);
		askForName.setModal(true);
		//also make sure it gets native events. So Nothing else but this popup works
		//(if we dont do this keyboard events might also trigger on the scene when you type)
		askForName.setPreviewingAllNativeEvents(true);

		//style!
		askForName.getElement().getStyle().setZIndex(999999999);
		askForName.getElement().getStyle().setBackgroundColor("white");

		//disable keypress's (remember to reenable when using this!)
		JAMcore.setIgnoreKeyPresses(true);

		//popup at center!
		askForName.center();
		//focus the namebox ready to type! because we are awesome like that!
		nameBox.setFocus(true);		
		nameBox.selectAll(); //select all
	}

	//SceneStatus tempState,			
	//SceneObjectState[] sceneobjectsCurrentState
	public void display() {

		//save is of the format:

		// Scene Load Event (all scenes that have been loaded should be here), we trigger loading first as this will need the most data transfered-best to get it started!
		// Load Scenes Data (for each of the above scenes)
		// NOTE; Currently only one scenes data is loaded

		// Then we load the rest of the Game Data

		//in future for multiple scenes we will need to have a separate command to put the correct scene to the front at the end


		//String LoadScene = "- LoadScene = "+InstructionProcessor.currentScene.scenesData.SceneFolderName+"\n";

		//always start with a newline 
		String finalSaveString =  CreateSaveString();


		//update display and link:	
		displayData(finalSaveString,finalSaveString);


	}

	public static void displayData(String displaydata) {

		displayData(displaydata, displaydata);

	}

	/* the display data should have newlines wherever you want, however the link data should be ready to load (no newlines in scene data) 
	 * EDIT: Newline limitation should be removed now!*/
	private static void displayData(String displaydata, String linkdata) {


		//newpop.fixedZdepth(20000);
		//fill data in
		databox.setText(displaydata);

		JAM.saveGamePopup.contentPanel.OpenDefault();



		updateLink();

	}

	protected static void updateLink() {

		//String dataURLsafe = History.encodeHistoryToken(databox.getText());		//url encode the data in the box
		//String href="#LoadGameData="+dataURLsafe+"\n:end";

		//link.setHref(href); //make a loadgamedata link from it
		//link.setText("(old link "+dataURLsafe.length()+")"); //add the length at the end just to confirm its updated for people


		compressedlink.setHref("needs update"); 
		compressedlink.setText(GamesInterfaceText.MainGame_UpdatingPleaseWait); 


		emailLink.setText(GamesInterfaceText.MainGame_EmailLinkToMe);
		compressStringToLinkAndSaveString(databox.getText());

		//	Log.info("dataURLsafe:"+href);



		//test the encoding to make sure its recodeable
		/*
		History.newItem(href, false);
		String gotfromurl = History.getToken();

		Log.info("from url:"+gotfromurl);

		try {
			String deccodetest = URL.decode(gotfromurl);
			Log.info("url decoded:"+deccodetest);
		} catch (Exception e) {
			Log.info("url decode failed");
			e.printStackTrace();
		}
	/*
		try {
			String deccodetest2 = History.encodeHistoryToken(href);
			Log.info("url decoded:"+deccodetest2);
		} catch (Exception e) {
			Log.info("decodeQueryString failed");
			e.printStackTrace();
		}
		 */

	}

	public static void compressStringToLinkAndSaveString(final String compressthis) {

		//Divide into chunks of length 
		//Separate each chunk with a COMPRESSION_LIMIT_SEPERATOR

		// Boolean compressing = true;

		//run when done 
		final StringResult runwhendone = new StringResult(){			
			@Override
			public void run(String result) {

				current_save_string=START_OF_GAMEDATA_MARKER+result+END_OF_GAMEDATA_MARKER;
				saveToTextBox.setText(current_save_string); //updates the box displaying the save string
				//auto select all
				saveToTextBox.selectAll();

				compressedlink.setText(GamesInterfaceText.MainGame_SaveStateAsCompressedLink);	

				// String cstring = prettyPrint(c.getCompressedData());
				Log.info("cstring="+"#"+START_OF_GAMEDATA_MARKER+result+"\n"+END_OF_GAMEDATA_MARKER);	                
				compressedlink.setHref("#"+START_OF_GAMEDATA_MARKER+result+"\n"+END_OF_GAMEDATA_MARKER);

			}

		};

		Log.info("compressing String");
		SpiffyGWTLzma.CompressString(compressthis, runwhendone, new UpdateRunnable() {
			@Override
			public void run(String updatetext) {
				compressedlink.setText(" ( "+GamesInterfaceText.MainGame_Compressing+" "+updatetext+") ");	    
			}
		});

		/*
        Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
            LZMAByteArrayCompressor c;
            @Override
            public boolean execute() {
                if (c == null) {
                    c = new LZMAByteArrayCompressor(UTF8.encode(compressthis), CompressionMode.MODE_1); //Shouldn't need more then MODE_1 unless it gets REALLY big
                    return true;
                }
                if (c.execute()) {
                   // int pcent = (int)(c.getProgress() * 100.0);
                   // LZMADemo.this.rightSizePanel.setWidget(new Label("Compressing... " + pcent + "%"));
                   // Log.info("pcent="+pcent);

                    return true;
                }
               // setRightData(c.getCompressedData());
                String cstring = prettyPrint(c.getCompressedData());
                runwhendone.run(cstring);

               // updateSizes(true);
                return false;
            }
        });*/

	}

	/****/
	//TODO: we might be able to move part of this to core after JAM.loadGameData is in the core
	//We might even be able to move it all in, depending how we want to handle debugging stuff like displaying the save string
	public void displayAndLoadDecompressedString(String data, boolean ClearGameFirst) {

		//if we are in debug mode we display it
		if (JAMcore.DebugMode){
			GwtSaveGameManager.displayData(data);
		}

		JAM.loadGameData(data,ClearGameFirst);


	}


	/*
   static private byte[] getDataToDecompress(String gamedata, boolean alert) {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        String s = gamedata;
        boolean gothi = false;
        int hinib = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (" \t\f\n\r".indexOf(ch) != -1)
                continue;
            int digit = Character.digit(ch, 16);
            if (digit == -1) {
                if (alert)
                    Log.severe("invalid compressed input: invalid hex character `" + ch + "'");
                return null;
            }
            if (gothi)
                b.write((hinib << 4) + digit);
            else
                hinib = digit;
            gothi = !gothi;
        }
        if (gothi) {
            if (alert)
            	  Log.severe("invalid compressed input: odd number of digits");
            return null;
        }
        return b.toByteArray();
    }
	 */


	public static void decompressAndLoad_old(String totalGameData,  final boolean ClearGameFirst) {

		//		//strip newlines
		//				totalGameData = totalGameData.replaceAll("\\r\\n|\\r|\\n", " ");
		//				Log.info("loading:"+totalGameData);
		//
		//				//remove the marker at the start
		//				if (totalGameData.startsWith(JamSaveGameManager.START_OF_GAMEDATA_MARKER)){
		//					Log.info("removing:"+JamSaveGameManager.START_OF_GAMEDATA_MARKER+" from save string");
		//
		//					totalGameData=totalGameData.substring(JamSaveGameManager.START_OF_GAMEDATA_MARKER.length()); //remove the start marker
		//				}
		//
		//				//ensure  marker exists
		//				if (!totalGameData.endsWith(JamSaveGameManager.END_OF_GAMEDATA_MARKER)){
		//					Log.severe("Save game data incomplete, no end marker found.");
		//					Log.severe("Last characters in save string was:   "+totalGameData.substring(totalGameData.length()-15,totalGameData.length()));			
		//					return;
		//				}
		//				
		//				
		//				//-----------------------------------------
		//		

		//run when done 
		final StringResult runwhendone = new StringResult(){			
			@Override
			public void run(String result) {

				compressedlink.setText(("loading... "));
				//displayAndLoadDecompressedString(result,ClearGameFirst);  
				RequiredImplementations.saveManager.get().displayAndLoadDecompressedString(result,ClearGameFirst);  

				GwtSaveGameManager.SEVER_OPTIONS.ServerFeedback.setText("Loaded OK");
				LoadSaveGamePopup.SEVER_OPTIONS.ServerFeedback.setText("Loaded OK");

			}

		};



		compressedlink.setText(("Decompressing... 0%"));

		SpiffyGWTLzma.DecompressString(totalGameData, runwhendone, compressedlink, new UpdateRunnable() {

			@Override
			public void run(String updatetext) {
				//update main user interface loading
				//as loading typically happens when first going to the game
				//JAM.currentLoadingText = "Loading savegame.."+updatetext;
				//JAM.advanceGameLoadingBar();
				if (OptionalImplementations.gamesLoadMonitor.isPresent()){		
					OptionalImplementations.gamesLoadMonitor.get().setGameLoadStatus("Loading savegame.."+updatetext);
					OptionalImplementations.gamesLoadMonitor.get().advanceGameLoadingBar();			
				}
			}
		});





		/*
		final byte[] data = getDataToDecompress(totalGameData,true);

		if (data==null){

			Log.severe("-----------------------------(decompression error in save string see above)");
			//we really should flag and alert here

		}

        // Decompress it
		 compressedlink.setText(("Decompressing... 0%"));



        Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
            LZMAByteArrayDecompressor d;
            @Override
            public boolean execute() {

                if (d == null) {
                    try {
                        d = new LZMAByteArrayDecompressor(data);
                    } catch (IOException e) {
                    	compressedlink.setText("Decompression failed: " + e.getMessage());
                        return false;
                    }
                    return true;
                }

                if (d.execute()) {
                    int pcent = (int)(d.getProgress() * 100.0);
                    compressedlink.setText("Decompressing... " + pcent + "%");
                    return true;
                }

                IOException ioe = d.getException();

                if (ioe != null) {
                	compressedlink.setText("Decompression failed: " + ioe.getMessage());
                    return false;
                }
                String text;
                try {
                    text = UTF8.decode(d.getUncompressedData());
                } catch (IllegalArgumentException e) {
                	compressedlink.setText("Decompression failed: " + e.getMessage());
                    return false;
                }

             //   displayAndLoadDecompressedString(text);
                runwhendone.run(text);


                return false;
            }
        });

		 */
		return;
	}


	//TODO: eventually refractor more to parent if possible , else just tidy up
	/**
	 * remember before storing to add START_OF_GAMEDATA_MARKER and the start and END_OF_GAMEDATA_MARKER at the end
	 * This function will not do that for you
	 */
	@Override
	public String CreateSaveString(){
		String saveString = super.CreateSaveString();


		//set icons - used for the CuypersCode2 only at this point
		//Basically these are interface elements that change over the course of the game
		//hair length
		if (JAM.StatueHead.originalfilename.length()>3){
			saveString = saveString + "\n- SetClockLadyIcon = " + JAM.StatueHead.originalfilename+"."+JAM.StatueHead.filenameext+","+(JAM.StatueHead.getLastFrameNumber()+1);
		}
		//soldier
		if (JAM.solider.originalfilename.length()>3){
			saveString = saveString + "\n- SetSoldierIcon = " + JAM.solider.originalfilename+"."+JAM.solider.filenameext+","+(JAM.solider.getLastFrameNumber()+1);
		}

		//bag
		//if the bag is a png;
		if (JAM.DefaultInventorysButton.originalfilename.length()>3){
			saveString = saveString + "\n- SetInventoryIcon = " + JAM.DefaultInventorysButton.originalfilename+"."+JAM.DefaultInventorysButton.filenameext+","+(JAM.DefaultInventorysButton.getLastFrameNumber()+1);
		} else if (JAM.BigInventoryImages.equals(InternalAnimations.BigMarInventoryImages)) {
			//its an internal specification - SetInventoryIcon =  
			saveString = saveString + "\n- SetInventoryIcon = magrietbag0.png,6";             	 

		}


		saveString = saveString + "\n";


		//we now we add all the changed div elements. That's actual html divs, not  div objects in a scene.
		//TODO: separate this out as a optional implementation using GWTPageElementSTyleCommands
		for (String divIDthatschanged : InstructionProcessor.ChangedHtmlPageElement) {

			Element divthatschanged = DOM.getElementById(divIDthatschanged);


			saveString = saveString+ "\n- SetClassOnElement = "+divthatschanged.getId()+","+divthatschanged.getClassName()+"\n";

			//and set the inline styles.
			//technically this might not be necessary if they havnt changed, but as they could have been set in the html
			//and REMOVED by the code, for now even a empty style should be saved and applied
			String inlinestyle = divthatschanged.getAttribute("style");// divthatschanged.getPropertyString("style");

			saveString = saveString+ "\n- SetStyleOnElement = "+divthatschanged.getId()+","+inlinestyle+"\n";




		}





		//Finally we add the current feedback message
		//we do this at the end to ensure nothing else can override it
		//and, if animated, the user will see it typed to draw the eye to it
	//	saveString = saveString + "\n- Message = "+JAM.Feedback.getCurrentText()+"\n";       

		
		
		//make version for display
		//String displayData = standardSaveStuff +"\n";//+LoadScene+sceneSaveString;

		//make version 
		String finalData = saveString+"\n";//+sceneSaveStrings;//+LoadScene+displayCurrentScene;



		return finalData+"\n";		
	}






	/*
	@Override
	public void CloseDefault(){
		contentPanel.CloseDefault();

		//when closing we enable the JAMs games keypress sensing 
		JAMcore.setIgnoreKeyPresses(false);

	}
	 */


	/** Should be fired after a language change.
	 * This function changes all text in this widget to match the current language-specific
	 * interface strings specified in GamesInterfaceText **/
	public void refreshText(){

		SEVER_OPTIONS.refreshText();

		linkdoesntworkiniewarninglabel.setText(GamesInterfaceText.ControllPanel_IECantDoLongLinks);

		contentPanel.setTitlebBarText(GamesInterfaceText.MainGame_SaveYourGame);		

		//save option tabs. Note the order must be kept for the text change on them to work
		saveoptions.getTabBar().setTabText(0, GamesInterfaceText.MainGame_ToThisBrowser );		
		saveoptions.getTabBar().setTabText(1, GamesInterfaceText.MainGame_AsAWebLink    );

		//Note the tab position index is different depending on is the server tab will be present
		if (JAMcore.HasServerSave){
			saveoptions.getTabBar().setTabText(2, GamesInterfaceText.MainGame_OnServer      );
			saveoptions.getTabBar().setTabText(3, GamesInterfaceText.MainGame_AsText        );
		} else {
			saveoptions.getTabBar().setTabText(2, GamesInterfaceText.MainGame_AsText        );
		}

		//update save explanation in local storage manager widget
		localStoreManager.setLoadInstructions(GamesInterfaceText.MainGame_NoteThisWillSaveYourGameOnThisPCAndBrowserOnly);

		//save state as compressed link message
		compressedlink.setText(GamesInterfaceText.MainGame_SaveStateAsCompressedLink);	

		//buttons!!! lovely buttons
		updateBut.setText(GamesInterfaceText.MainGame_UpdateLinkFromBox);
		emailLink.setText(GamesInterfaceText.MainGame_EmailLinkToMe);

		loadData.setText(GamesInterfaceText.MainGame_LoadBoxDataIntoGame);
		resetGameFirst.setText(GamesInterfaceText.MainGame_ResetFirst);

		gsData.setText(GamesInterfaceText.MainGame_SaveDataToBox );
		rdData.setText(GamesInterfaceText.MainGame_ResetData );

		//and the big long final string
		saveDataPastedBelow.setText(GamesInterfaceText.MainGame_ThisBigStringContainsYourSavegame );

	}

	@Override
	public Object getVisualRepresentation() {
		return contentPanel;
	}

	@Override
	public void compress(String data, final CompressionResult compressedresult) {
		SpiffyGWTLzma.CompressString(data, new StringResult() {			
			@Override
			public void run(String result) {
				compressedresult.gotResult(result);				
			}
		}, null);

	}

	@Override
	public void decompress(String compressedData,final CompressionResult resultCallback) {
		SpiffyGWTLzma.DecompressString(compressedData, new StringResult() {

			@Override
			public void run(String result) {
				resultCallback.gotResult(result);	
			}
		}, null, null);

	}

	@Override
	public void saveGameToBrowserImpl(String saveName, String savedata, boolean overWriteIfNameAlreadyPresent) {

		localStoreManager.SaveData(saveName, savedata, overWriteIfNameAlreadyPresent);

		//also ensure the load game popup updates to show this new save
		JAM.loadGamePopup.localStoreManager.repopulateOnNextOpen();

	}

	@Override
	public void loadingfeedback(String progresstext) {
		compressedlink.setText(progresstext);

	}

	@Override
	public void loadDataButtonState(boolean enable, String caption) {
		loadData.setEnabled(enable);
		loadData.setText(caption);
	}

	//Notes; Below is the old Save function for saving to a server
	//the new function is within ServerOptions
	/*
	public static void SaveGameToServer() {
		//MyApplication.processInstructions(ProcessThis.trim());
		if (getLocation().indexOf("#")>-1){ //$NON-NLS-1$
			JAM.gamesurllocation = getLocation().substring(0,getLocation().indexOf("#")); //$NON-NLS-1$
		} 

		 // MyApplication.Feedback.setText(URL.encodeComponent(CreateSaveString()));
		//encrypt string		
		JAM.DebugWindow.addText(JAM.messagehistory.getHTML());

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,GWT.getHostPageBaseURL()+"Login_System/SaveGame.php"); //$NON-NLS-1$

				try {
				      requestBuilder.sendRequest("SaveData=" + URL.encodeComponent(CreateSaveString())+"&MessageHistory=" + URL.encodeComponent(JAM.messagehistory.getHTML()), new RequestCallback() { //$NON-NLS-1$ //$NON-NLS-2$
				        public void onError(Request request, Throwable exception) {
				        	System.out.println("encode url failed"); //$NON-NLS-1$
				        }

				        public void onResponseReceived(Request request, Response response) {

				        	//String responsetext = response.getText();
				        	//String saveString = "LoadGameData="+responsetext; //$NON-NLS-1$
				           //update widgets HTML field
				        	SaveData.setHTML(GamesInterfaceText.ControllPanel_DataSaved); 

				        }
				      });
				    } catch (RequestException ex) {
				    	String responsetext = "can not connect to game controll file"; //$NON-NLS-1$
				    	SaveData.setHTML(responsetext+"   "+ex.getMessage()); //$NON-NLS-1$
				    }
		//------
	}


	public void refreshText() {
		SEVER_OPTIONS.refreshText();

	}*/

}
