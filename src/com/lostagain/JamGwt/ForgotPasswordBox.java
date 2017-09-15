package com.lostagain.JamGwt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.JAMcore;
import com.lostagain.JamGwt.JargScene.ServerOptions;

public class ForgotPasswordBox  extends TitledPopUpWithShadow {
	
	
	final Grid shadowgrid = new Grid(3,3);
	
	 final VerticalPanel container = new VerticalPanel();
	 final Grid form_container = new Grid(7,3);
	 final FormPanel signup_form = new FormPanel(); 
	 final Button signup_button = new Button("Email Me New Pasword"); 
     final TextBox username = new TextBox();
    
     
     final Label lab_name = new Label("Username:");
     
     //error feedbacl
     final HTML err_name = new HTML("-");
     String errorlist="";
     
     //in case they just wanted to login
     final Hyperlink loginlink = new Hyperlink("<<Login","");
     
     
      public ForgotPasswordBox(){
		 

  		super(null,"400px", "auto", "Forgot Password Box:", new Label(
				"loading..."),true);
		// DOM.setStyleAttribute(this.getElement(), "zIndex", "4000");
    	  
		 this.setCenterWidget(container);
		 this.setAnimationEnabled(true);
		 
		 container.setWidth("100%");
		 container.setStyleName("ForgotPasswordBox");
		 
		 signup_form.setMethod(FormPanel.METHOD_POST); 
		 signup_form.setEncoding(FormPanel.ENCODING_MULTIPART); 
		 
		 username.setName("user");
		 
		 this.setWidth("400px");
		 
		 form_container.setWidget(0,0,lab_name);
		 form_container.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		 	 
		 form_container.setWidget(0,1,username); 
		
		 form_container.setWidget(0,2,err_name); 
		 
		 Hidden h = new Hidden("subforgot", "1");
		 form_container.setWidget(5,0,h);
		
		 signup_form.add(form_container);
		 form_container.setWidth("100%");
		 
		 container.add(signup_form);
		 container.add(signup_button);
		 

		    
		 signup_button.addClickHandler(new ClickHandler(){

				public void onClick(ClickEvent event) {
	    		  signup_form.setAction("Login_System/Forgot.php");
	    		  signup_form.submit();
	    		  
	    		  
	    	  }
	      });
		 
		 signup_form.addFormHandler(new FormHandler() { 
				public void onSubmit(FormSubmitEvent event){
					 err_name.setHTML("");   		
					
					 
				}
				
	             public void onSubmitComplete(FormSubmitCompleteEvent event) {
	            	
	            	 errorlist="";
	            	
	            	 //if an error
	            	 if (event.getResults().startsWith("ERROR")==true){
	            		            		 
	            		 errorlist = event.getResults().substring(event.getResults().indexOf("ERROR")+5);
	            		 err_name.setHTML(errorlist.substring(errorlist.indexOf("_usererror=")+11, errorlist.indexOf("_",errorlist.indexOf("_usererror=")+11)));	            		 
	            		
	            		
	            		 
	            	 } else {
	                //its not a form error
	            		 JAM.Feedback.setText(event.getResults());	            		
	            	 }
	            	 
	            	 if (event.getResults().startsWith("SUCCESS")==true){	
           		 
	            		 //hide this and bring login back
	            		 JAM.Feedback.setText("New Password Sent! Check your email account :)");
	            		 ServerOptions.user_login.signupbox.hide();
	            		 
	            	 }
	            	 
	            	 
	            	 
	             }
			 });
		
		 loginlink.addClickHandler(new ClickHandler(){

				public void onClick(ClickEvent event) {
				ServerOptions.user_login.forgotpasswordbox.hide();
			}

			 
		 });
		 container.add(loginlink);
		 container.setCellHorizontalAlignment(loginlink, HasHorizontalAlignment.ALIGN_LEFT);
		 container.setSpacing(4);
		container.setCellHorizontalAlignment(signup_button, HasHorizontalAlignment.ALIGN_CENTER);
		
/*
		 shadowgrid.setCellPadding(0);
		 shadowgrid.setCellSpacing(0);
		 shadowgrid.getCellFormatter().setStyleName(0, 0, "ShadowTopLeft");
			
		 shadowgrid.getCellFormatter().setWidth(0, 0, "8px");
		 shadowgrid.getCellFormatter().setHeight(0, 0, "26px");
			
		 shadowgrid.setWidget(0, 1, new Label("Forgot Password ?"));
			
			
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

}
