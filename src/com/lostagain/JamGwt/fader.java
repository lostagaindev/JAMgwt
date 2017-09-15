package com.lostagain.JamGwt;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;

//probably can be a simple panel instead.
public class fader extends AbsolutePanel{

	//simple fullscreen div used to fade the back.
	public fader (){
		this.setStyleName("fader");

        DOM.setStyleAttribute(this.getElement(), "zIndex", "1000");
     //   System.out.println("blah");
      //  System.out.println(this.getElement().getStyle().getProperty("zIndex"));
        
	    DOM.setStyleAttribute(this.getElement(), "width", "100%");
        DOM.setStyleAttribute(this.getElement(), "height", "100%");
        
	}
	
}
