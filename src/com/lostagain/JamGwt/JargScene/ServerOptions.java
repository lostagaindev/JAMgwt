package com.lostagain.JamGwt.JargScene;

import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.GameManagementClass;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.ServerOptionsCore;
import com.lostagain.Jam.Interfaces.hasVisualRepresentation;
import com.lostagain.Jam.SaveMangement.HasServerStorageMethod;
import com.lostagain.JamGwt.ChangePasswordBox;
import com.lostagain.JamGwt.GamesInterfaceText;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.LoadSaveGamePopup;
import com.lostagain.JamGwt.LoginBox;
import com.lostagain.JamGwt.ResetGameBox;
import com.lostagain.JamGwt.GwtSaveGameManager;

/** manages options to save and load the game from the sever
 * as well as login and out, and reset the game **/
public class ServerOptions extends ServerOptionsCore implements HasServerStorageMethod  { // extends VerticalPanel

	VerticalPanel contentsPanel = new VerticalPanel();

	static Logger Log = Logger.getLogger("JAM.ServerOptions");
	
	//popup boxs
	final ChangePasswordBox ChangePasswordBox = new ChangePasswordBox();
	final ResetGameBox ResetGameBox = new ResetGameBox();
	
	//buttons! lots of buttons
	Button SaveGameDataToSever = new Button(GamesInterfaceText.ControllPanel_ClickHereToSaveYourProgress); 
	Button LoadGameDataFromSever = new Button(GamesInterfaceText.ControllPanel_LoadFromServer); 
	Button StartFromStartOfGame = new Button(GamesInterfaceText.MainGame_StartANewGame); 
	
	Button LogIn = new Button("");
	Button LogOut = new Button(GamesInterfaceText.ControllPanel_ClickHereToLogOut);	 
	
	//labels (really all the text should eventually be in GamesInterfaceText to allow language switching)w
	public Label ServerFeedback = new Label("");
	public Label LogedInStatus = new Label("Logged In As:");
	
	
	HorizontalPanel bottomoptions = new HorizontalPanel();
	
	final Anchor changepassword = new Anchor(GamesInterfaceText.ControllPanel_ChangePassword,"#changepassword");
	
	final Anchor  resetgame = new Anchor("Reset Game","#resetgame");

	public static final LoginBox user_login = new LoginBox();
	
	static String location = getLocation();
	
	
	public ServerOptions(){

		Log.info("setting up ServerOptions");
		contentsPanel.setSize("100%", "100%");
				
		contentsPanel.setSpacing(8);
		contentsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		//set up style
		LogedInStatus.addStyleName("LoggedInTitle");
		
		
		
		//---
		Log.info("setting up Server handlers");
		
		LogIn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
			
				ServerOptions.user_login.OpenDefault();
			}
			 
		 });
		
		resetgame.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
			
				ResetGameBox.OpenDefault();
			}
			 
		 });
		
		StartFromStartOfGame.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				//Note; Clear data before starting the game, because anyone that does it the other way around is stupid
				GameManagementClass.clearAllGameData();
				JAM.startNewGame();

			}
			 
		 });
		
		
		changepassword.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				ChangePasswordBox.OpenDefault();
			}
			 
		 });
		
		
		LogOut.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				//if guest we just refresh
				if (JAMcore.Username.equalsIgnoreCase("guest")){
					Log.info("guest detected, so pointless");
					return;
				}
				
				
				logoutUser(true);
				
			}
			
		});

		SaveGameDataToSever.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				saveGameToServer();
				
				
				
			}
			
		});
		LoadGameDataFromSever.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				loadGame();
				
				
			}
			
		});
		Log.info("setting up interface for server buttons");		
		addButtons();
		
		Log.info("updating interface for server buttons");		
		updateStatus();
	}


	
	public void logoutUser(final Boolean reload) {
		if (getLocation().indexOf("#")>-1){ //$NON-NLS-1$
			location = getLocation().substring(0,getLocation().indexOf("#")); //$NON-NLS-1$
		} 
				
		JAM.Feedback.setText(GamesInterfaceText.ControllPanel_loggingout); 
		
		GwtSaveGameManager.SEVER_OPTIONS.LogedInStatus.setText("Logging oug");
		LoadSaveGamePopup.SEVER_OPTIONS.LogedInStatus.setText("Logging oug");
		
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,GWT.getHostPageBaseURL()+"Login_System/LogoutUser.php"); //$NON-NLS-1$
				
				try {

					JAM.Feedback.setText(GamesInterfaceText.ControllPanel_loggingout2); 
					
				      requestBuilder.sendRequest("", new RequestCallback() { //$NON-NLS-1$
				        @Override
						public void onError(Request request, Throwable exception) {
				        	System.out.println("encode url failed"); //$NON-NLS-1$
				        	JAM.Feedback.setText("error"+exception.getMessage()); //$NON-NLS-1$
				        }

				        @Override
						public void onResponseReceived(Request request, Response response) {
		                  
				        	String reponsetext = response.getText();
				        	
				        	if (reponsetext.equals("LoggedOutSuccess")||(reponsetext.equals("NotLoggedIn"))){ //$NON-NLS-1$
				        	
				        		
				        		GwtSaveGameManager.SEVER_OPTIONS.LogedInStatus.setText("Logged out");
				        		LoadSaveGamePopup.SEVER_OPTIONS.LogedInStatus.setText("Logged out");
				        	//	SaveData.setHTML(GamesInterfaceText.ControllPanel_LoggedOut);		
				        	
				        	//remove cookies to make double sure

							Iterator<String> cookieit = Cookies.getCookieNames().iterator();
							String cookin = Cookies.getCookieNames().toArray(new String[0])[0];
							Window.setTitle("removing cookies"+cookin);
							Cookies.setCookie(cookin, "", new Date(new Date().getTime()-1000*60*60*24), null, "/", false);
							Cookies.removeCookie(cookin);
							while (cookieit.hasNext()){
								
								String cookie = cookieit.next();
								Window.setTitle("removing cookie:"+cookie);
								
								Cookies.removeCookie(cookie);
								
							}
							// ----------------------------------
							Window.setTitle("removed cookies");
							
							Log.info("refreshing page;"+reload);
							setGameInterfaceLoggedOut();
							
							if (reload){
									reload();			        	
							}
							
				        	} else 
				        	{
				        		JAM.Feedback.setText("--"+response.getText()); //$NON-NLS-1$
				        		Log.info("--"+response.getText());

				        		GwtSaveGameManager.SEVER_OPTIONS.LogedInStatus.setText("Log out error");
				        		LoadSaveGamePopup.SEVER_OPTIONS.LogedInStatus.setText("Log out error");
				        		
				        	}
				        	
				        	
				        }
				      });
				    } catch (RequestException ex) {
				    	JAM.Feedback.setText("no response"+ex.getMessage()); //$NON-NLS-1$
				    }
		//------
	}
	
	protected void  reload() {
		
		Log.info("Logged out - reload page? or probably not needed with new system");
		
	}
	/****/
	protected void setGameInterfaceLoggedOut() {
		
		
		//set as logged out
		JAMcore.Username = JAMcore.DefaultUsername; 
		JAMcore.loggedIn = false;		
		
		//update interface
		GwtSaveGameManager.SEVER_OPTIONS.updateStatus();
		LoadSaveGamePopup.SEVER_OPTIONS.updateStatus();
		
	}



	private static native String getLocation() /*-{ 
		
	   return $wnd.location.href; 

	}-*/;
	
	
	
	public void loadGame() {
		
		loadSaveGameFromSever();
		
		//close any open save/load windows
		//JAM.saveGamePopup.CloseDefault();
		//JAM.loadGamePopup.CloseDefault();
		
	}

	/** user must be logged in, then this will get their savegame **/	
	public void loadSaveGameFromSever() {
		GwtSaveGameManager.SEVER_OPTIONS.ServerFeedback.setText("Loading...");
		LoadSaveGamePopup.SEVER_OPTIONS.ServerFeedback.setText("Loading...");
		Log.info("getting save game in");
		
		RequestBuilder requestBuilder2 = new RequestBuilder(RequestBuilder.GET,GWT.getHostPageBaseURL()+"Login_System/GetSaveGame.php");
		try {
		      requestBuilder2.sendRequest("", new RequestCallback() {
		        @Override
				public void onError(Request request, Throwable exception) {
		        	System.out.println("error in user gamedata loading::");
		        	JAM.Feedback.setText("error in user gamedata loading"+exception.getMessage());
		        }

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					
					String incomingdata = " "+response.getText();

					Log.info("got save game:"+incomingdata);
				//	MyApplication.Feedback.setTitle("loading game 1");
					
					if (incomingdata.indexOf("--<Sdata:HData>--")>-1){
					//	MyApplication.Feedback.setTitle("loading game 2");
						
					//load data
					String savedata = incomingdata.split("--<Sdata:HData>--")[0].trim();
					
					//save data might end in a unnessery "<br>:end" due to an old end-marker in use on the php side
					//this is outdated and can be removed on the php, but we detect and remove it here just in case its still being returned
					if (savedata.endsWith("<br>:end")){
						savedata = savedata.substring(0, savedata.length() - "<br>:end".length()); //remove the "<br>:end" bit at the end
					};
					
					
					//new method uses decompress and load;
					//update visual feedback of loading on all the server option boxs that might be open
					GwtSaveGameManager.SEVER_OPTIONS.LogedInStatus.setText("Loading "+JAMcore.Username+"s save game");
					LoadSaveGamePopup.SEVER_OPTIONS.LogedInStatus.setText("Loading "+JAMcore.Username+"s save game");
					
					//ControllPanel.clearAllGameData();
					GwtSaveGameManager.decompressAndLoad(savedata,true); //the decompressor removes the end marker itself (:end)

					//old method used JAM.loadGameData
					
					//JAM.loadGameData(savedata); //used to be URL.decode(()) before new save system

				//	MyApplication.Feedback.setTitle("loading game 3");
					
					//MyApplication.DebugWindow.setText("DECODED STUFF:"+URL.decode( (response.getText().split("--<Sdata:HData>--")[0])));
					
					JAM.messagehistory.messageslist.setHTML(incomingdata.split("--<Sdata:HData>--")[1]);
					
					
					//Temp experiment - all other load methods stop here, so why shouldnt we as well?
					if (true){
						return;
					}
					
					//activate game
					/*
					JAM.maingameloop();
					JAM.user_login.CloseDefault();
					//fix size
					JAM.resizeStoryBox();
					
					//enable scroll
					//Window.enableScrolling(true); (disabled since THorn Game - why did we do this?)	
					
					/*
					
					//DOM.setStyleAttribute(DOM.getElementById("tempback"),"visibility","hidden");
				*/
					//MyApplication.loginbackground.FadeOut(1500, 10);	
					
					//fade it;
					/*
					final Timer fadeout = new Timer (){
						private double backopacity=100;

						@Override
						public void run() {
							System.out.print((backopacity/100)+"\n");
							
							JAM.DebugWindow.addText("--"+backopacity);
							
							Style style = DOM.getElementById("loadingdiv").getStyle();
							if (style==null){
								Log.severe("element with ID loadingdiv does not exist ");
								this.cancel();
								return;
								
							}
							
							style.setProperty("filter", "alpha(opacity="+backopacity+")");
							style.setProperty("opacity", ""+(backopacity/100));
							
							//ThisImage.getElement().setAttribute("style", " filter: alpha(opacity="+opacity+"); opacity: "+(opacity/100)+";");
							backopacity=backopacity-15;
							if (backopacity<15){
								
								//DOM.setStyleAttribute(DOM.getElementById("loadingdiv"),"visibility","hidden");
								DOM.getElementById("loadingdiv").getStyle().setVisibility(Visibility.HIDDEN);
								
								this.cancel();
								
							}
							
							
							
						}
					
					};
					
					fadeout.scheduleRepeating(70);
					
					//RootPanel.get().remove(MyApplication.loginbackground);					
					JAM.Feedback.setTitle(".");
					
					//reset to bottom
					// DOM.setStyleAttribute(JAM.fadeback.getElement(), "zindex", "1000");
					
					 JAM.fadeback.getElement().getStyle().setZIndex(1000);
					 
					 */
					} else {
						JAM.Feedback.setText("error in user gamedata loading.");
					}
				}

		      });
		
		}catch (RequestException ex) {
			System.out.println("load game failed");
			JAM.Feedback.setText("error in user gamedata loading"+ex.getMessage());
		}
	}

	public void saveGameToServer() {
		saveGameToServerImpl( GwtSaveGameManager.current_save_string);//TODO: doesn't this need to be updated?!?
	}
	
	public void saveGameToServerImpl(String CompressedSaveGameData) {
		//update all server options text to reflect saving state
		  GwtSaveGameManager.SEVER_OPTIONS.ServerFeedback.setText("Saving...");
		LoadSaveGamePopup.SEVER_OPTIONS.ServerFeedback.setText("Saving...");
		
		//MyApplication.processInstructions(ProcessThis.trim());
		if (getLocation().indexOf("#")>-1){ //$NON-NLS-1$
			location = getLocation().substring(0,getLocation().indexOf("#")); //$NON-NLS-1$
		} 
		
		
		 // MyApplication.Feedback.setText(URL.encodeComponent(CreateSaveString()));
		//encrypt string		
		JAMcore.GameLogger.info(JAM.messagehistory.getHTML());
		
		//get the compressed string
		String compressedSaveGame = CompressedSaveGameData;
		
	//	String rawSaveGame = URL.encodeComponent(SaveGameManager.databox.getText());
		

		
		//ServerFeedback.setText("Saving...");
		
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,GWT.getHostPageBaseURL()+"Login_System/SaveGame.php"); //$NON-NLS-1$
				
				try {
				      requestBuilder.sendRequest("SaveData=" + compressedSaveGame+"&MessageHistory=" + URL.encodeQueryString(JAM.messagehistory.getHTML()), new RequestCallback() { //$NON-NLS-1$ //$NON-NLS-2$
				      
				    	@Override
						public void onError(Request request, Throwable exception) {
				        	System.out.println("encode url failed"); //$NON-NLS-1$
				        }

				        @Override
						public void onResponseReceived(Request request, Response response) {
		                  
				        	String responsetext = response.getText();
				        	Log.info("save response:"+responsetext);
				        	
				    		GwtSaveGameManager.SEVER_OPTIONS.ServerFeedback.setText("Saved Ok");
				    		LoadSaveGamePopup.SEVER_OPTIONS.ServerFeedback.setText("Saved Ok");
				    		
				        	//String saveString = "LoadGameData="+responsetext; //$NON-NLS-1$
				           //update widgets HTML field
				        //	SaveData.setHTML(GamesInterfaceText.ControllPanel_DataSaved); 
				        	
				        }
				      });
				    } catch (RequestException ex) {
				    	String responsetext = "can not connect to game controll file"; //$NON-NLS-1$
				    	//SaveData.setHTML(responsetext+"   "+ex.getMessage()); //$NON-NLS-1$
				    }
		//------
	}
	
	public void updateStatus() {

		LogedInStatus.setText("Logged In As:"+JAMcore.Username);
		
		//if guest disable stuff;
		if (JAMcore.Username.equals("guest") || JAMcore.Username.equalsIgnoreCase(JAMcore.DefaultUsername)){
			

			LogedInStatus.setText(GamesInterfaceText.MainGame_NotLoggedIn);
			
			//guests cant save or load!			
			changepassword.setVisible(false);
			resetgame.setVisible(false);	
			
			LoadGameDataFromSever.setEnabled(false);			
			SaveGameDataToSever.setEnabled(false);
			
			LogOut.setEnabled(false);
			LogIn.setEnabled(true);
			LogIn.setVisible(true);
			
		} else {
			
			LogedInStatus.setText(" "+JAMcore.Username+" Logged In  ");
			LogOut.setEnabled(true);
			
			LogIn.setEnabled(false);
			LogIn.setVisible(false);
			
			LoadGameDataFromSever.setEnabled(true);						
			SaveGameDataToSever.setEnabled(true);
			
			changepassword.setVisible(true);
			resetgame.setVisible(true);		
			
		}
		
		
		
	}
	

	private void addButtons() {
		
		contentsPanel.add(LogedInStatus);		
		contentsPanel.add(SaveGameDataToSever);	
		contentsPanel.add(LoadGameDataFromSever);
		contentsPanel.add(StartFromStartOfGame);
		
		contentsPanel.add(LogOut);
		contentsPanel.add(LogIn);
		contentsPanel.add(ServerFeedback);
		bottomoptions.setWidth("300px");
		bottomoptions.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomoptions.add(changepassword);	
		bottomoptions.add(resetgame);
		contentsPanel.add(bottomoptions);
		
	}


	

	/** should be fired after a language change **/
	public void refreshText() {
		
		SaveGameDataToSever.setText(GamesInterfaceText.ControllPanel_ClickHereToSaveYourProgress);
		changepassword.setText(GamesInterfaceText.ControllPanel_ChangePassword);
		LogOut.setText(GamesInterfaceText.ControllPanel_ClickHereToLogOut);	 
		LogIn.setText(GamesInterfaceText.Login_PleaseLogin);
		LoadGameDataFromSever.setText(GamesInterfaceText.ControllPanel_LoadFromServer); 
		
		StartFromStartOfGame.setText(GamesInterfaceText.MainGame_StartANewGame); 
		
		//refresh sub options in this panel
		ResetGameBox.refreshText();
		ChangePasswordBox.refreshText();
		
		
	}



	@Override
	public Object getVisualRepresentation() {
		return this.contentsPanel;
	}



	@Override
	public void openLoginBox() {
		ServerOptions.user_login.OpenDefault();
		ServerOptions.user_login.getElement().getStyle().setZIndex((JAMcore.z_depth_max + 1));
		
	}



	@Override
	public void closeLoginBox() {
		ServerOptions.user_login.CloseDefault(true);
	}
	
	
}
