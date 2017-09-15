package com.lostagain.JamGwt;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.JAMcore;
import com.lostagain.JamGwt.JargScene.ServerOptions;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class ResetGameBox  extends TitledPopUpWithShadow {

	static Logger Log = Logger.getLogger("JAM.ResetGameBox");
	
	final Grid shadowgrid = new Grid(3,3);
	
	 final VerticalPanel container = new VerticalPanel();
	 final Grid form_container = new Grid(4,3);
	 final FormPanel resetgame_form = new FormPanel(); 
	 final Button resetgamebutton = new Button(GamesInterfaceText.GameReset_Button); 
     final TextBox username = new TextBox();
    
     final PasswordTextBox curpassword = new PasswordTextBox();
  
     
     final Label lab_curpassword = new Label("Reenter Password:");    
     final Label lab_warning = new Label(GamesInterfaceText.GameReset_Warning);    
     
     
     //error feedback
     final HTML err_curpassword = new HTML("-");    
    
     
     String errorlist="";
     
     //in case they just wanted to login
     final Anchor loginlink = new Anchor("<<Back","#");
     
     ResetGameBox thisResetGameBox = this;
     
      public ResetGameBox(){

    		super(null,"400px", "auto", "Change Password Box:", new Label(
  				"loading..."),true);

    		Log.info("setting up ResetGameBox");	
    		
    		
		// DOM.setStyleAttribute(this.getElement(), "zIndex", "6000");
		// this.setWidget(shadowgrid);
		 this.setCenterWidget(container);
		 
		 this.setAnimationEnabled(true);
		 
		 container.setWidth("100%");
		 container.setStyleName("resetgamebox");
		 
		 resetgame_form.setMethod(FormPanel.METHOD_POST); 
		 resetgame_form.setEncoding(FormPanel.ENCODING_MULTIPART); 
		 
		 username.setName("user");
		 curpassword.setName("curpass");
		 
		 this.setWidth("400px");
		 
		form_container.setWidget(2,0,lab_curpassword);
		 form_container.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			 
		 form_container.setWidget(2,1,curpassword);
		
		 form_container.setWidget(2,2,err_curpassword);
			
		 Hidden h = new Hidden("subedit", "1");
		 form_container.setWidget(3,0,h);
		
		 resetgame_form.add(form_container);
		 form_container.setWidth("100%");
		 form_container.setHeight("110px");
		 
		 container.add(resetgame_form);
		 container.add(lab_warning);
		 container.add(resetgamebutton);
		 

		    
		 resetgamebutton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
	    		  resetgame_form.setAction("Login_System/ResetAccount.php");
	    		  resetgame_form.submit();
	    		  
	    		  
	    	  }
	      });
		 resetgame_form.addSubmitHandler(new SubmitHandler(){
			public void onSubmit(SubmitEvent event) {
				// err_name.setHTML("");
       		 err_curpassword.setHTML("");
			}
			 
		 });
		 resetgame_form.addSubmitCompleteHandler(new SubmitCompleteHandler(){
				
				public void onSubmitComplete(SubmitCompleteEvent event) {
					 errorlist="";
		            	
	            	 //if an error
	            	 if (event.getResults().startsWith("ERROR")==true){
	            		            		 
	            		 errorlist = event.getResults().substring(event.getResults().indexOf("ERROR")+5);
	            		// err_name.setHTML(errorlist.substring(errorlist.indexOf("_usererror=")+11, errorlist.indexOf("_",errorlist.indexOf("_usererror=")+11)));	            		 
	            		 err_curpassword.setHTML(errorlist.substring(errorlist.indexOf("_curpassworderror=")+18, errorlist.indexOf("_",errorlist.indexOf("_curpassworderror=")+18)));	            		 
		            		
	            		 
	            		
	            		 
	            	 } else {
	                //its not a form error
	            		 JAM.Feedback.setText(event.getResults());	            		
	            	 }
	            	 
	            	 if (event.getResults().startsWith("SUCCESS")==true){	
           		 
	            		 //hide this and bring login back
	            		 JAM.Feedback.setText("Game Reset");
	            		 ServerOptions.user_login.signupbox.hide();
	            		 reload();
	            	 }
	            	 
	            	 
	            	 
				}
				 
			 });
		
		
		 loginlink.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					thisResetGameBox.hide();
			}
			 
		 });
		 container.add(loginlink);
		 container.setCellHorizontalAlignment(loginlink, HasHorizontalAlignment.ALIGN_LEFT);
		 container.setSpacing(4);
		container.setCellHorizontalAlignment(resetgamebutton, HasHorizontalAlignment.ALIGN_CENTER);
		/*

		 shadowgrid.setCellPadding(0);
		 shadowgrid.setCellSpacing(0);
		 shadowgrid.getCellFormatter().setStyleName(0, 0, "ShadowTopLeft");
			
		 shadowgrid.getCellFormatter().setWidth(0, 0, "8px");
		 shadowgrid.getCellFormatter().setHeight(0, 0, "26px");
			
		 shadowgrid.setWidget(0, 1, new Label("Reset Your Game"));
			
			
		 shadowgrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		 shadowgrid.getCellFormatter().setStyleName(0, 1, "notepadback");
			shadowgrid.getCellFormatter().setHeight(0, 1, "26px");
			shadowgrid.getCellFormatter().setStyleName(0, 2, "notepadback");
			shadowgrid.getCellFormatter().setHeight(0, 2, "26px");
					
			shadowgrid.getCellFormatter().setWidth(1, 0, "8px");
			shadowgrid.getCellFormatter().setStyleName(1, 0, "ShadowLeft");
			
			// load review into HTML
			shadowgrid.setWidget(1, 1, container);
			System.out.println("co");

			shadowgrid.getCellFormatter().setStyleName(1, 1, "Backstyle");

			shadowgrid.getCellFormatter().setStyleName(1, 2, "notepadback");

			shadowgrid.getCellFormatter().setWidth(2, 0, "8px");
			shadowgrid.getCellFormatter().setHeight(2, 0, "8px");
			shadowgrid.getCellFormatter().setStyleName(2, 0, "ShadowCorner");
			

			shadowgrid.getCellFormatter().setHeight(2, 1, "8px");
			shadowgrid.getCellFormatter().setStyleName(2, 1, "ShadowLower");
			
			shadowgrid.getCellFormatter().setWidth(2, 2, "16px");
			shadowgrid.getCellFormatter().setHeight(2, 2, "8px");
			shadowgrid.getCellFormatter().setStyleName(2, 2, "ShadowBottomRight");
			*/
		 
	 }
      private native void reload() /*-{ 
	    $wnd.location.reload(); 
	   }-*/;
      

  	/** should be fired after a language change **/
	public void refreshText() {
		
		 resetgamebutton.setText(GamesInterfaceText.GameReset_Button); 
	     lab_warning.setText(GamesInterfaceText.GameReset_Warning);   
	     
		
	}
	
    @Override
    public void OpenDefault(){
   	 super.OpenDefault();
   	 
   	 //when opening we disable the JAMs games keypress sensing 
   	 JAMcore.setIgnoreKeyPresses(true);
   	 
    }
    
    @Override
    public void CloseDefault(boolean runClosedefaultOnChildren){
   	 super.CloseDefault(runClosedefaultOnChildren); //TODO:probably should be false to not run this twice
   	 
   	 //when closing we enable the JAMs games keypress sensing 
   	 JAMcore.setIgnoreKeyPresses(false);
   	 
    }

}
