package com.lostagain.JamGwt;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
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
/** popup form for changing password 
 * needs Login_System/EditAccount.php*/
public class ChangePasswordBox  extends TitledPopUpWithShadow {
	


	static Logger Log = Logger.getLogger("JAM.ChangePasswordBox");
	final Grid shadowgrid = new Grid(3,3);
	
	 final VerticalPanel container = new VerticalPanel();
	 final Grid form_container = new Grid(7,3);
	 final FormPanel changeDetailsForm = new FormPanel(); 
	 final Button changepassword_button = new Button(GamesInterfaceText.ControllPanel_ChangePassword); 
     final TextBox username = new TextBox();
    
     final PasswordTextBox curpassword = new PasswordTextBox();
     final PasswordTextBox newpassword = new PasswordTextBox();
     final TextBox email = new TextBox();
     
     final Label lab_curpassword = new Label("Current Password:");    
     final Label lab_newpassword = new Label("New Password:");
     final Label lab_email = new Label("Email:");
     
     //error feedback
     final HTML err_curpassword = new HTML("-");    
     final HTML err_newpassword = new HTML("-");
     final HTML err_email = new HTML("-");
     
     String errorlist="";
     
     //in case they just wanted to login
     final Anchor loginlink = new Anchor("<<Back","#");
     
     
     ChangePasswordBox thisChangePasswordBox = this;
     
      @SuppressWarnings("deprecation")
	public ChangePasswordBox(){
    	  
  		super(null,"400px", "auto", "Change Password Box:", new Label(
				"loading..."),true);
  		
  		Log.info("setting up ChangePasswordBox");		

		// DOM.setStyleAttribute(this.getElement(), "zIndex", "6000");
		 
		// this.setWidget(shadowgrid);
		 this.setCenterWidget(container);
		 
		 
		 this.setAnimationEnabled(true);
		 
		 container.setWidth("100%");
		 container.setStyleName("changepasswordbox");
		 
		 changeDetailsForm.setMethod(FormPanel.METHOD_POST); 
		 changeDetailsForm.setEncoding(FormPanel.ENCODING_MULTIPART); 
		 
		 username.setName("user");
		 curpassword.setName("curpass");
		 email.setName("email");
		 newpassword.setName("newpass");
		 
		 this.setWidth("400px");
		 
		form_container.setWidget(2,0,lab_curpassword);
		 form_container.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		 form_container.setWidget(3,0,lab_newpassword);
		 form_container.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		 form_container.setWidget(4,0,lab_email);
		 form_container.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			 
		 form_container.setWidget(2,1,curpassword);
		 form_container.setWidget(3,1,newpassword);
		 form_container.setWidget(4,1,email);
		
		 form_container.setWidget(2,2,err_curpassword);
		 form_container.setWidget(3,2,err_newpassword);
		 form_container.setWidget(4,2,err_email);
			
		 Hidden h = new Hidden("subedit", "1");
		 form_container.setWidget(5,0,h);
		
		 changeDetailsForm.add(form_container);
		 form_container.setWidth("100%");
		 
		 container.add(changeDetailsForm);
		 container.add(changepassword_button);
		 

		    
		 changepassword_button.addClickHandler(new ClickHandler(){

				public void onClick(ClickEvent event) {
	    		  changeDetailsForm.setAction("Login_System/EditAccount.php");
	    		  changeDetailsForm.submit();
	    		  
	    		  
	    	  }
	      });
	
		 changeDetailsForm.addFormHandler(new FormHandler() { 
				public void onSubmit(FormSubmitEvent event){
					// err_name.setHTML("");
            		 err_curpassword.setHTML("");
            		 err_newpassword.setHTML("");
             		
            		 err_email.setHTML("");   		
					
					 
				}
				
	             public void onSubmitComplete(FormSubmitCompleteEvent event) {
	            	
	            	 errorlist="";
	            	
	            	 //if an error
	            	 if (event.getResults().startsWith("ERROR")==true){
	            		            		 
	            		 errorlist = event.getResults().substring(event.getResults().indexOf("ERROR")+5);
	            		// err_name.setHTML(errorlist.substring(errorlist.indexOf("_usererror=")+11, errorlist.indexOf("_",errorlist.indexOf("_usererror=")+11)));	            		 
	            		 err_newpassword.setHTML(errorlist.substring(errorlist.indexOf("_newpassworderror=")+18, errorlist.indexOf("_",errorlist.indexOf("_newpassworderror=")+18)));	            		 
	            		 err_curpassword.setHTML(errorlist.substring(errorlist.indexOf("_curpassworderror=")+18, errorlist.indexOf("_",errorlist.indexOf("_curpassworderror=")+18)));	            		 
		            		
	            		 err_email.setHTML(errorlist.substring(errorlist.indexOf("_useremail=")+11, errorlist.indexOf("_",errorlist.indexOf("_useremail=")+11)));	            		 
	            		 
	            		
	            		 
	            	 } else {
	                //its not a form error
	            		 JAM.Feedback.setText(event.getResults());	            		
	            	 }
	            	 
	            	 if (event.getResults().startsWith("SUCCESS")==true){	
           		 
	            		 //hide this and bring login back
	            		 JAM.Feedback.setText("Password Changed");
	            		 ServerOptions.user_login.signupbox.hide();
	            		 
	            	 }
	            	 
	            	 
	            	 
	             }
			 });
		
		 
		 loginlink.addClickHandler(new ClickHandler(){

				public void onClick(ClickEvent event) {
					
					thisChangePasswordBox.hide();
				
				
			}
			 
		 });
		 
		 
		 container.add(loginlink);
		 container.setCellHorizontalAlignment(loginlink, HasHorizontalAlignment.ALIGN_LEFT);
		 container.setSpacing(4);
		container.setCellHorizontalAlignment(changepassword_button, HasHorizontalAlignment.ALIGN_CENTER);
		
/*
		 shadowgrid.setCellPadding(0);
		 shadowgrid.setCellSpacing(0);
		 shadowgrid.getCellFormatter().setStyleName(0, 0, "ShadowTopLeft");
			
		 shadowgrid.getCellFormatter().setWidth(0, 0, "8px");
		 shadowgrid.getCellFormatter().setHeight(0, 0, "26px");
			
		 shadowgrid.setWidget(0, 1, new Label("Change Password Form"));
			
			
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
	

     @Override
     public void OpenDefault(){
    	 super.OpenDefault();
    	 
    	 //when opening we disable the JAMs games keypress sensing 
    	 JAMcore.setIgnoreKeyPresses(true);
    	 
     }
     
     @Override
     public void CloseDefault(){
    	 super.CloseDefault();
    	 
    	 //when closing we enable the JAMs games keypress sensing 
    	 JAMcore.setIgnoreKeyPresses(false);
    	 
     }
     
    
		
      
    /** should be fired after a language change **/
  	public void refreshText() {
  		
  	   changepassword_button.setText(GamesInterfaceText.ControllPanel_ChangePassword);
  		
  	}

}
