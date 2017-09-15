package com.lostagain.JamGwt;

import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.lostagain.Jam.JAMcore;
import com.lostagain.JamGwt.JargScene.ServerOptions;

public class LoginBox extends TitledPopUpWithShadow {

	static Logger Log = Logger.getLogger("JAM.LoginBox");
	boolean AllowLogin = false;
	
	final Grid shadowgrid = new Grid(3,3);
	final SignUpBox signupbox = new SignUpBox();
	final ForgotPasswordBox forgotpasswordbox = new ForgotPasswordBox();
	 final VerticalPanel container = new VerticalPanel();
	 final Grid form_container = new Grid(4,3);
	 final FormPanel login_form = new FormPanel(); 
	 final Button login_button = new Button("Login"); 
     final TextBox username = new TextBox();
     final PasswordTextBox password = new PasswordTextBox();
     final CheckBox rememberme = new CheckBox();
     
     
     final Label lab_name = new Label(GamesInterfaceText.Login_Username);
     final Label lab_password = new Label(GamesInterfaceText.Login_Password);
     final Label lab_rememberme = new Label(GamesInterfaceText.Login_Rememberme);
     final Label lab_loginplease = new Label(GamesInterfaceText.Login_PleaseLogin);
     
     
     //error returns
     final HTML err_name = new HTML(" ");
     final HTML err_organisation = new HTML(" ");
     final HTML err_password = new HTML(" ");    
     String errorlist="";
     
    // final HTML signup = new HTML("Not registered? <a href=\"Login_System/Login%20System%20v.2.0/register.php\">Sign-Up!</a>");
     final Anchor signup = new Anchor(GamesInterfaceText.Login_SignUp,"#");
     final Anchor guestlogin = new Anchor(GamesInterfaceText.Login_GuestLogin,"#");
     final Anchor forgotpassword = new Anchor(GamesInterfaceText.Login_ForgotPassword,"#");
     final Anchor clearcookies = new Anchor(GamesInterfaceText.Login_ClearCookies,"#");
     
     LoginBox This = this;
     Timer recentercheck;
     
	 public LoginBox(){
		 
			super(null,"auto", "auto", "Login Box", new Label(
					"loading..."),true);
		
		 Log.info("setting up login box");
			
		 
		// DOM.setStyleAttribute(this.getElement(), "zIndex", "3000");
		 
		// this.setWidget(shadowgrid);
		 this.setCenterWidget(container);
		 
		 this.setAnimationEnabled(true);
		 
		 container.setWidth("100%");
		 container.setStyleName("loginback");
		 
		 login_form.setMethod(FormPanel.METHOD_POST); 
		 login_form.setEncoding(FormPanel.ENCODING_MULTIPART); 
		 
		 username.setName("user");
		 password.setName("pass");
		 
		 rememberme.setName("remember");
		 
		 this.setWidth("380px");
		 
		 form_container.setWidget(1,0,lab_password);
		 form_container.setWidget(0,0,lab_name);
		 form_container.setWidget(2,0,lab_rememberme);
		 form_container.setWidget(1,1,password);
		 form_container.setWidget(0,1,username); 
		 form_container.setWidget(1,2,err_password);
		 form_container.setWidget(0,2,err_name); 
		 form_container.setWidget(2,1,rememberme);
		 

		 Log.info("setting up login  box 2");
			
		 
		 Hidden h = new Hidden("sublogin", "1");
		 form_container.setWidget(3,0,h);
		 

		 
		 
		 
		 login_form.add(form_container);
         


		    
		 password.addKeyDownHandler(new KeyDownHandler() {
				public void onKeyDown(KeyDownEvent event) {

					int Key = event.getNativeKeyCode();
					if (Key == 13) {
						
						AllowLogin = true;
						Log.info("Allow login ="+AllowLogin);
						
						password.setText(JAM.RemoveCartReturns(password.getText())
								.trim());
						
						  login_form.setAction("Login_System/LoginUser.php");
			    		  login_form.submit();
						
					}

				}

			});
		 
		 login_button.addClickHandler(new ClickHandler(){
			

				public void onClick(ClickEvent event) {
					
					AllowLogin = true;
					
					//Date date = new Date();
					 
					 //int hour = cal.get(Calendar.HOUR_OF_DAY);
					 //int min = cal.get(Calendar.MINUTE);
					 //login_timestamp.setValue(""+date.getTime());
					
	    		  login_form.setAction("Login_System/LoginUser.php");
	    		  login_form.submit();
	    		  
	    		  
	    	  }
	      });
		 login_form.addSubmitCompleteHandler(new SubmitCompleteHandler(){	
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				//if the bottom was never pressed, dont login.
				// (this can happen under firefox if data is resent to login system)
				
				//Window.alert("login:"+event.getResults());
				Log.info("Login:"+event.getResults());
				
				
				if (AllowLogin){
					
				 err_name.setHTML("");
        		 err_password.setHTML("");
            	 //if not an error
            	 if (event.getResults().startsWith("ERROR_")==false){
            		 Log.info("Login, no error");
            		 JAMcore.Username = event.getResults();
            		 
            		 //clear history
            		 
            		 //login the user
            		 LoginUser();
            		 if (recentercheck!=null){
            			 recentercheck.cancel();
            		 }
            		 
            	 } else {
                //its an error
            		 Log.info("Login had error");
            		 
            		 errorlist = event.getResults().substring(event.getResults().indexOf("ERROR")+5);
            		 err_name.setHTML(errorlist.substring(errorlist.indexOf("_usererror=")+11, errorlist.indexOf("_",errorlist.indexOf("_usererror=")+11)));	            		 
            		 err_password.setHTML(errorlist.substring(errorlist.indexOf("_passworderror=")+15, errorlist.indexOf("_",errorlist.indexOf("_passworderror=")+15)));	            		 
            		
            		 
            		 
            	 }
			} else {
				//force a logout, as they shouldnt be logged in
				Window.alert("incorrect login session");
				
				GwtSaveGameManager.SEVER_OPTIONS.logoutUser(false);
				
			}
				
			}
		 });
		 
		 /*
		 login_form.addFormHandler(new FormHandler() { 
				public void onSubmit(FormSubmitEvent event){
				}
				
	             public void onSubmitComplete(FormSubmitCompleteEvent event) {
	            	 
	             }
			 });
		*/
		 
		 signup.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
			
				signupbox.OpenDefault();
			}
			 
		 });
		 
		 forgotpassword.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					
					forgotpasswordbox.OpenDefault();
				}
				 
			 });
		 
		 
		 container.add(login_form);
		 HorizontalPanel UserAccountOptions = new HorizontalPanel();
		 UserAccountOptions.setWidth("100%");
		 UserAccountOptions.add(signup);
		 UserAccountOptions.setCellHorizontalAlignment(signup,HasHorizontalAlignment.ALIGN_LEFT);
		 UserAccountOptions.add(forgotpassword);
		 UserAccountOptions.setCellHorizontalAlignment(forgotpassword,HasHorizontalAlignment.ALIGN_RIGHT);
		 
		 container.add(UserAccountOptions);
		 
		 HorizontalPanel LoginAndClearCookes = new HorizontalPanel();
		 
		 LoginAndClearCookes.add(guestlogin);
		 LoginAndClearCookes.setCellHorizontalAlignment(guestlogin,HasHorizontalAlignment.ALIGN_LEFT);		 
			
		 LoginAndClearCookes.setWidth("100%");
		 LoginAndClearCookes.add(login_button);		 
		 LoginAndClearCookes.setCellHorizontalAlignment(login_button,HasHorizontalAlignment.ALIGN_RIGHT);		 
		 LoginAndClearCookes.add(clearcookies);
		 LoginAndClearCookes.setCellHorizontalAlignment(clearcookies,HasHorizontalAlignment.ALIGN_RIGHT);
		 LoginAndClearCookes.setCellWidth(clearcookies,"45%");	
		 container.add(LoginAndClearCookes);
		 
		 guestlogin.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
				
					//if not running the game already we load it
					if (!JAMcore.StartOfScriptProcessed)
					{
						//load data
						JAM.loadGameData("newuser",true);
						JAM.messagehistory.messageslist.setHTML("Welkom gastspeler. Als u uw spelgang wil opslaan, zult u zich moeten registreren.");
				
						//activate game if not already (the function checks this itself)
						JAMcore.maingameloop();
					}
				
				
				//fix size
				JAM.resizeStoryBox();
			
				JAMcore.Username = JAMcore.DefaultUsername; //$NON-NLS-1$
				JAMcore.Organisation = "pc";
				
				Log.info("hiding all login boxs");
				ServerOptions.user_login.CloseDefault();
				JAM.loadGamePopup.CloseDefault();
				JAM.saveGamePopup.contentPanel.CloseDefault();
				
				
				//triggerpreloader
				//MyApplication.preloadData();
				
				//fade it;
				/*
				final Timer fadeout = new Timer (){
					private double backopacity=100;

					@Override
					public void run() {
						System.out.print((backopacity/100)+"\n");
						
						JAM.DebugWindow.addText("--"+backopacity);
						
						Style loadingDivsStyle = DOM.getElementById("loadingdiv").getStyle();
						if (loadingDivsStyle==null){
							Log.severe("element with ID loadingdiv does not exist ");
							this.cancel();
							return;
						}
						loadingDivsStyle.setProperty("filter", "alpha(opacity="+backopacity+")");
						loadingDivsStyle.setProperty("opacity", ""+(backopacity/100));
						
						//ThisImage.getElement().setAttribute("style", " filter: alpha(opacity="+opacity+"); opacity: "+(opacity/100)+";");
						backopacity=backopacity-15;
						if (backopacity<15){
							
							//DOM.setStyleAttribute(DOM.getElementById("loadingdiv"),"visibility","hidden");
							//DOM.getElementById("loadingdiv").getStyle().setVisibility(Visibility.HIDDEN);
							loadingDivsStyle.setVisibility(Visibility.HIDDEN);
							
							Log.info("hiding box1");
							JAM.user_login.CloseDefault();
							
							this.cancel();
							
						}
						
						
						
					}
				
				};
				
				fadeout.scheduleRepeating(70);
				*/
				//RootPanel.get().remove(MyApplication.loginbackground);
				
				
			}
			 
		 });
		 
		 clearcookies.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
				
				
				Iterator<String> cookieit = Cookies.getCookieNames().iterator();
				String cookin = Cookies.getCookieNames().toArray(new String[0])[0];
				//Window.setTitle("removing cookies"+cookin);
				Cookies.setCookie(cookin, "", new Date(new Date().getTime()-1000*60*60*24), null, "/", false);
				Cookies.removeCookie(cookin);
				while (cookieit.hasNext()){
					//Window.setTitle("removing cookie:"+cookieit.next());
					Cookies.removeCookie(cookieit.next());
					
				}
				
			}
			 
		 });
		 
		 container.setSpacing(4);
		container.setCellHorizontalAlignment(login_button, HasHorizontalAlignment.ALIGN_CENTER);
		
/*
		 shadowgrid.setCellPadding(0);
		 shadowgrid.setCellSpacing(0);
		// shadowgrid.getCellFormatter().setStyleName(0, 0, "ShadowTopLeft");
			
		 shadowgrid.getCellFormatter().setWidth(0, 0, "8px");
		 shadowgrid.getCellFormatter().setHeight(0, 0, "26px");
			
		 shadowgrid.setWidget(0, 1, lab_loginplease);
			
			
		 shadowgrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		 shadowgrid.getCellFormatter().setStyleName(0, 1, "loginback");
			shadowgrid.getCellFormatter().setHeight(0, 1, "26px");
			shadowgrid.getCellFormatter().setStyleName(0, 2, "loginback");
			shadowgrid.getCellFormatter().setHeight(0, 2, "26px");
					
			shadowgrid.getCellFormatter().setWidth(1, 0, "8px");
			//shadowgrid.getCellFormatter().setStyleName(1, 0, "ShadowLeft");
			
			// load review into HTML
			shadowgrid.setWidget(1, 1, container);
			System.out.println("co");

			shadowgrid.getCellFormatter().setStyleName(1, 1, "Backstyle");

			shadowgrid.getCellFormatter().setStyleName(1, 2, "loginback");

			shadowgrid.getCellFormatter().setWidth(2, 0, "8px");
			shadowgrid.getCellFormatter().setHeight(2, 0, "8px");
			//shadowgrid.getCellFormatter().setStyleName(2, 0, "ShadowCorner");
			

			shadowgrid.getCellFormatter().setHeight(2, 1, "8px");
			//shadowgrid.getCellFormatter().setStyleName(2, 1, "ShadowLower");
			
			shadowgrid.getCellFormatter().setWidth(2, 2, "16px");
			shadowgrid.getCellFormatter().setHeight(2, 2, "8px");
			//shadowgrid.getCellFormatter().setStyleName(2, 2, "ShadowBottomRight");
			
			//set timer to recenter
			recentercheck = new Timer(){

				@Override
				public void run() {
					if (This.isAttached()){
					This.center();
					}
				}
				
			};
			
			recentercheck.schedule(3500);
			
		 */
	 }
	 
	 
	/** this should run when a successful login is detected **/
	public void LoginUser(){
		
		
		Log.info("LoginUser processor");
		
		 if (recentercheck!=null){
			 recentercheck.cancel();
		 }
		 
		JAMcore.loggedIn = true;
		
		//update the game control for their username (note; this is wastefull, fix it to just update the user specific stuff later)		
		JAM.SwapUserSpecificWords();
		
		//update server boxes
		GwtSaveGameManager.SEVER_OPTIONS.updateStatus();		
		LoadSaveGamePopup.SEVER_OPTIONS.updateStatus();
		
		//close this window
		CloseDefault();
		
		//open server options if load or save isn't already open
		//AND no history string is loading from the url
		if (!JAM.loadGamePopup.isShowing() && !JAM.saveGamePopup.contentPanel.isShowing()){			
			if (JAMcore.StartFromURL.length()<2){
				JAM.loadGamePopup.OpenOnServerOptions();
			}
		}
		
		
		
		
		
		
	}

}
