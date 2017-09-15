package com.lostagain.JamGwt;

import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.CurrentScenesVariables;
import com.lostagain.Jam.GameTextDatabase;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.TypedLabelCore;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryPanel;
import com.lostagain.JamGwt.JargScene.SceneDialogObject;
import com.lostagain.JamGwt.JargScene.SceneDivObject;
import com.lostagain.JamGwt.JargScene.SceneInputObject;
import com.lostagain.JamGwt.JargScene.SceneVectorObject;
import com.lostagain.JamGwt.JargScene.SceneWidgetVisual;
import com.lostagain.JamGwt.JargScene.ServerOptions;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isPopupTypeImplementation;
import com.lostagain.JamGwt.audio.GWTMusicBox;

import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyDataBox;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyListBox;



/**
 * The control panel lets you change the games various settings.
 * 
 * It also currently manages the clearing of game data, but this might be refractored somewhere else in future as its not strictly a game setting.
 * 
 * **/
public class ControllPanel extends SpiffyDataBox implements hasCloseDefault, hasOpenDefault, isPopupTypeImplementation  {

	static Logger Log = Logger.getLogger("JAM.ControllPanel");

	final ChangePasswordBox ChangePasswordBox = new ChangePasswordBox();
	final ResetGameBox ResetGameBox = new ResetGameBox();

	final Hyperlink changepassword = new Hyperlink(GamesInterfaceText.ControllPanel_ChangePassword,""); //$NON-NLS-1$ 
	final Hyperlink resetgame = new Hyperlink("Reset Game",""); //$NON-NLS-1$ //$NON-NLS-2$

	//music controller
	Label music_label = new Label(GamesInterfaceText.ControllPanel_CurrentMusic); 
	GWTMusicBox musicPlayer = new GWTMusicBox();

	HorizontalPanel SoundEffectsOnOff = new HorizontalPanel();
	Label sound_options = new Label(GamesInterfaceText.ControllPanel_SoundEffects); 

	RadioButton soundon = new RadioButton("sound_options", GamesInterfaceText.ControllPanel_on); //$NON-NLS-1$ 
	RadioButton soundoff = new RadioButton("sound_options", GamesInterfaceText.ControllPanel_off); //$NON-NLS-1$ 
	HorizontalPanel AnimationEffectsOnOff = new HorizontalPanel();

	Label animation_options = new Label(GamesInterfaceText.ControllPanel_AnimationEffects);	   
	RadioButton animationon = new RadioButton("animation_options", GamesInterfaceText.ControllPanel_on); //$NON-NLS-1$ 
	RadioButton animationoff = new RadioButton("animation_options", GamesInterfaceText.ControllPanel_off); //$NON-NLS-1$ 

	HorizontalPanel AutosaveOnOff = new HorizontalPanel();

	Label autosave_options = new Label("Autosave:");	   
	RadioButton autosaveon = new RadioButton("autosave_options", GamesInterfaceText.ControllPanel_on); //$NON-NLS-1$ 
	RadioButton autosaveoff = new RadioButton("autosave_options", GamesInterfaceText.ControllPanel_off); //$NON-NLS-1$ 


	//layout control's
	Label LayoutSizeOveride = new Label(GamesInterfaceText.ControllPanel_InterfaceSize);	 
	SpiffyListBox LayoutModes = new SpiffyListBox() ;

	//quality control's
	Label QualityOveride = new Label(GamesInterfaceText.ControllPanel_InterfaceSize);	 
	SpiffyListBox QualityModes = new SpiffyListBox() ;

	//language options
	Label language_options = new Label("Language:");	   
	SpiffyListBox languageModes = new SpiffyListBox() ;


	// panel layout
	topBar NewTopBar = new topBar(GamesInterfaceText.ControllPanel_ControllPanel,this); 
	//final HTML SaveData = new HTML(" "); //$NON-NLS-1$
	HorizontalPanel SaveLoadButtons = new HorizontalPanel();
	Button SaveGameData = new Button("Save To Link"); //$NON-NLS-1$

	Button LogOut = new Button(GamesInterfaceText.ControllPanel_ClickHereToLogOut);	 

	Button SaveOptions = new Button(GamesInterfaceText.ControllPanel_SaveOptions); 
	Button LoadOptions = new Button(GamesInterfaceText.ControllPanel_LoadOptions); 

	//explains the last clicked on setting
	Label explanationlabel = new Label("...");


	public ControllPanel()  {

		this.setSize("300px", "60px"); //$NON-NLS-1$ //$NON-NLS-2$

		this.setStylePrimaryName("notepadback"); //$NON-NLS-1$

		//this.add(SaveData);

		//	this.setCellHorizontalAlignment(SaveData, HasHorizontalAlignment.ALIGN_CENTER);

		//this.setSpacing(8);




		this.addrow(SaveOptions,LoadOptions);

		//	SaveLoadButtons.add(SaveOptions);
		//	SaveLoadButtons.add(LoadOptions);

		//	this.add(LogOut);

		SoundEffectsOnOff.add(sound_options);
		SoundEffectsOnOff.add(soundon);
		soundon.setValue(true);
		SoundEffectsOnOff.add(soundoff);

		soundon.addClickHandler(updateUserOptions());
		soundoff.addClickHandler(updateUserOptions());


		this.addrow(sound_options,soundon,soundoff);

		AnimationEffectsOnOff.add(animation_options);
		AnimationEffectsOnOff.add(animationon);
		AnimationEffectsOnOff.add(animationoff);

		animationoff.addClickHandler(animationSettingChanged());
		animationon.addClickHandler(animationSettingChanged());
		animationon.setValue(true);

		this.addrow(music_label);
		this.addrow(musicPlayer.asWidget());

		this.addrow(animation_options,animationon,animationoff);

		if (!JAMcore.DisableAutoSave){
			AutosaveOnOff.add(autosave_options);
			AutosaveOnOff.add(autosaveon);
			AutosaveOnOff.add(autosaveoff);

			this.addrow(autosave_options,autosaveon,autosaveoff);
		}
		autosaveon.addClickHandler(updateUserOptions());
		autosaveoff.setValue(true);
		autosaveoff.addClickHandler(updateUserOptions());

		//LayoutMods
		LayoutModes.addItem(GamesInterfaceText.ControllPanel_Default); 
		LayoutModes.addItem(GamesInterfaceText.ControllPanel_Small); 
		LayoutModes.addItem(GamesInterfaceText.ControllPanel_Medium); 
		LayoutModes.addItem(GamesInterfaceText.ControllPanel_Big); 

		LayoutModes.setDefaultStyle("OptionListBox");
		LayoutModes.setHoverStyle("OpenListBoxHoverStyle");

		//all spiffyboxs we give a width too so they look neater
		//if text is changed too much this will need to be increased
		LayoutModes.setWidth("100px");
		QualityModes.setWidth("100px");
		languageModes.setWidth("100px");



		//this.setHorizontalAlignment(ALIGN_CENTER);		
		super.getColumnFormatter().setWidth(0, "150px");

		//	HorizontalPanel sizeoptions = new HorizontalPanel();

		//	sizeoptions.add(LayoutSizeOveride);
		//	sizeoptions.add(LayoutModes);

		LayoutModes.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {
				JAMcore.InterfaceSize = LayoutModes.getItemText(LayoutModes.getSelectedIndex());
				JAMcore.GameLogger.info(JAMcore.InterfaceSize);

				JAM.setInterfaceIconSize();

				//Manually overide javascript layout stylesheets
				if  (JAMcore.InterfaceSize.compareTo(GamesInterfaceText.ControllPanel_Small)==0) { 
					changecss("smallscreen"); 
				}
				if  (JAMcore.InterfaceSize.compareTo(GamesInterfaceText.ControllPanel_Medium)==0) { 
					changecss("midscreen"); 
				}
				if  (JAMcore.InterfaceSize.compareTo(GamesInterfaceText.ControllPanel_Big)==0) { 
					changecss("default"); 
				}

				explanationlabel.setText("Interface size set to "+JAMcore.InterfaceSize.toString()+". ");



				//new
				JAM.resizeEverything();

			}

		});

		if (!JAM.fixedSizeInterface){
			this.addrow(LayoutSizeOveride,LayoutModes);
		}

		//HorizontalPanel qualityoptions = new HorizontalPanel();

		//qualityoptions.add(QualityOveride);
		//qualityoptions.add(QualityModes);

		//add all the quality levels to the list (loop over from 0-10 listing them, adding words for the best and worst to make it clear
		int QualityLevel = 0;
		for (QualityLevel = 0; QualityLevel <= 10; QualityLevel++) {

			if (QualityLevel == 0){
				QualityModes.addItem("0 - (worst)", ""+QualityLevel);			
			} else if (QualityLevel ==10 ){
				QualityModes.addItem("10 - (best)", ""+QualityLevel);
			} else {
				QualityModes.addItem(""+QualityLevel, ""+QualityLevel);
			}

			if (JAMcore.Quality.equalsIgnoreCase(""+QualityLevel)){
				QualityModes.setSelectedIndex(QualityLevel);

			}


		}


		QualityModes.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {

				String  selectedValue = QualityModes.getValue(QualityModes.getSelectedIndex());
				JAMcore.Quality = selectedValue;

				explanationlabel.setText("Quality set to "+selectedValue+". This will apply to the next scenes loaded. ");

			}

		});


		this.addrow(QualityOveride,QualityModes);

		for (String id : GameTextDatabase.getLanIDs()) {

			languageModes.addItem(id,id);

			if (id.equalsIgnoreCase(JAMcore.LanguageExtension)){

				languageModes.setSelectedIndex(languageModes.getItemCount()-1);

			}

		}

		languageModes.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event) {

				String  selectedValue = languageModes.getItemText(languageModes.getSelectedIndex());

				JAMcore.LanguageExtension  = selectedValue;
				JAM.GamesText.loadGamesText();

				explanationlabel.setText("Lan now set to "+selectedValue+". This will apply to the next scenes loaded. ");


			}

		});
		this.addrow(language_options,languageModes);


		this.addrow(explanationlabel);




		//HorizontalPanel Bottomsplitter = new HorizontalPanel();
		//Bottomsplitter.setWidth("100%");
		//	Bottomsplitter.add(changepassword);
		//Bottomsplitter.add(resetgame);
		//Bottomsplitter.setCellHorizontalAlignment(changepassword, HasHorizontalAlignment.ALIGN_LEFT);		
		//Bottomsplitter.setCellHorizontalAlignment(resetgame, HasHorizontalAlignment.ALIGN_RIGHT);

		//this.add(Bottomsplitter);
		/*
		resetgame.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {

				ResetGameBox.OpenDefault();
			}

		 });
		changepassword.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {

				ChangePasswordBox.OpenDefault();
			}

		 });
		 */
		//this.setCellHorizontalAlignment(LogOut, HasHorizontalAlignment.ALIGN_CENTER);

		SaveLoadButtons.setSpacing(5);

		//this.setCellHorizontalAlignment(SaveLoadButtons,
		//		HasHorizontalAlignment.ALIGN_CENTER);


		/*

		LogOut.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {

				//if guest we just refresh
				if (JAM.Username.equalsIgnoreCase("guest")){
					Log.info("guest detected, thus user refresh");
					reload();
				}

				SaveGameManager.SEVER_OPTIONS.logoutUser(true);




			}

		});*/

		SaveOptions.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {

				//ServerOptions.saveGame();
				//JAM.saveGamePopup.center();
				//SaveGameManager.display(); 
				RequiredImplementations.saveManager.get().display();
				

			}

		});
		LoadOptions.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {	

				//LoadData(SaveData.getText());
				JAM.loadGamePopup.center();
			}

		});




	}
	private ClickHandler animationSettingChanged() {
		return new ClickHandler(){
			public void onClick(ClickEvent event) {

				explanationlabel.setText("Animation Turn set to "+animationon.getValue()+". This will effect stuff like Text typeing. ");
				updateUserOptions();
			}
		};

	};

	public static native void changecss(String description) /*-{

	     var i, a;
   for(i=0; (a = $doc.getElementsByTagName("link")[i]); i++){
	   if(a.getAttribute("title") == description){a.disabled = false;}
           else if((a.getAttribute("title") == "midscreen") || (a.getAttribute("title") == "smallscreen")){a.disabled = true;}	

   }
	}-*/;



	private ClickHandler updateUserOptions() {
		return new ClickHandler(){

			public void onClick(ClickEvent event) {

				//update options				
				JAM.SoundEffectOn = soundon.getValue();
				JAM.AnimationEffectsOn = animationon.getValue();
				TypedLabelCore.setGloballyDisableTypeing(!JAM.AnimationEffectsOn); //disable the typing effect if animations are not on

				JAMcore.autoserversaveon = autosaveon.getValue();

				//set animation on/off
				JAMcore.GamesChaptersPanel.setAnimationEnabled(JAM.AnimationEffectsOn);
				//JAM.GamesChaptersPanel.getDeckPanel().setAnimationEnabled(JAM.AnimationEffectsOn); (controlled together now)
				JAM.ControllPanelShadows.setAnimationEnabled(JAM.AnimationEffectsOn);

				((TitledPopUpWithShadow)JAMcore.PlayersInventoryFrame).setAnimationEnabled(JAM.AnimationEffectsOn);	
				JAM.PlayersNotepadFrame.setAnimationEnabled(JAM.AnimationEffectsOn);	

				JAM.loadGamePopup.setAnimationEnabled(JAM.AnimationEffectsOn);
				JAM.saveGamePopup.contentPanel.setAnimationEnabled(JAM.AnimationEffectsOn);

				ServerOptions.user_login.setAnimationEnabled(JAM.AnimationEffectsOn);
			}

		};
	}

	public void CloseDefault() {
		//-- we empty the textbox
		//SaveData.setText(""); //$NON-NLS-1$


		//--
		JAM.ControllPanelOpen = false;
		JAM.ControllPanelButton.setPlayBack();

		RootPanel.get().remove(this.getParent());

	}


	

	//	
	//	public void LoadData(String ProcessThis){
	//		
	//		resetGame();
	//		
	//		
	//		String location = getLocation();
	//		//MyApplication.processInstructions(ProcessThis.trim());
	//		if (getLocation().indexOf("#")>-1){ //$NON-NLS-1$
	//		location = getLocation().substring(getLocation().indexOf("#")+1); //$NON-NLS-1$
	//		} 
	//		Window.open(location+"#"+ProcessThis.trim(),"_self", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	//	}


	public void ShowDefault() {

		this.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

		int ScreenSizeX = Window.getClientWidth();
		int ScreenSizeY = Window.getClientHeight();

		RootPanel.get().remove(JAM.fadeback);
		RootPanel.get().add(this.getParent(), (int)(ScreenSizeX*0.25),  (int)(ScreenSizeY*0.40));
	}
	private native void reload() /*-{ 

	    $wnd.location.reload(); 
	    //used to be this

	    //$wnd.location = $wnd.location.href;

	   }-*/;
	public void OpenDefault() {
		//if guest disable stuff;
		if (JAMcore.Username.equals("guest")){


			changepassword.setVisible(false);
			resetgame.setVisible(false);

			SaveLoadButtons.setVisible(false);

		}


	}
	public boolean DRAGABLE() {
		return true;
	}
	public boolean POPUPONCLICK() {
		return false;
	}
	public String POPUPTYPE() {
		return null;
	}
	public void RecheckSize() {

	}
	public boolean MAGNIFYABLE() {
		return false;
	}





	public void updateText() {

		changepassword
		.setText(GamesInterfaceText.ControllPanel_ChangePassword);
		sound_options
		.setText(GamesInterfaceText.ControllPanel_SoundEffects);
		soundon
		.setText(GamesInterfaceText.ControllPanel_on);
		soundoff
		.setText(GamesInterfaceText.ControllPanel_off);
		animation_options
		.setText(GamesInterfaceText.ControllPanel_AnimationEffects);
		animationon
		.setText(GamesInterfaceText.ControllPanel_on);
		animationoff
		.setText(GamesInterfaceText.ControllPanel_off);
		autosaveon
		.setText(GamesInterfaceText.ControllPanel_on);
		autosaveoff
		.setText(GamesInterfaceText.ControllPanel_off);

		//music
		music_label.setText(GamesInterfaceText.ControllPanel_CurrentMusic);

		LayoutSizeOveride
		.setText(GamesInterfaceText.ControllPanel_InterfaceSize);
		NewTopBar.Title
		.setText(GamesInterfaceText.ControllPanel_ControllPanel);
		animationoff
		.setText(GamesInterfaceText.ControllPanel_off);
		SaveOptions
		.setText(GamesInterfaceText.ControllPanel_SaveOptions);
		LoadOptions
		.setText(GamesInterfaceText.ControllPanel_LoadOptions);
		LogOut
		.setText(GamesInterfaceText.ControllPanel_ClickHereToLogOut);

		LayoutModes.clear();
		LayoutModes
		.addItem(GamesInterfaceText.ControllPanel_Default);
		LayoutModes
		.addItem(GamesInterfaceText.ControllPanel_Small);
		LayoutModes
		.addItem(GamesInterfaceText.ControllPanel_Medium);
		LayoutModes
		.addItem(GamesInterfaceText.ControllPanel_Big);

		// update reset box text
		ResetGameBox.lab_warning
		.setText(GamesInterfaceText.GameReset_Warning);
		ResetGameBox.resetgamebutton
		.setText(GamesInterfaceText.GameReset_Button);
		// ResetGameBox.
		// .setText(MyApplication.GamesText.GameReset_Warning);
		// update change password
		ChangePasswordBox.changepassword_button
		.setText(GamesInterfaceText.ControllPanel_ChangePassword);


		QualityOveride.setText(GamesInterfaceText.ControllPanel_QualitySetting);



	}
	public void updateLans() {

		languageModes.clear();

		for (String id : GameTextDatabase.getLanIDs()) {			
			languageModes.addItem(id);

			if (id.equalsIgnoreCase(JAMcore.LanguageExtension)){

				languageModes.setSelectedIndex(languageModes.getItemCount()-1);

			}

		}
	}
	
	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}
}
