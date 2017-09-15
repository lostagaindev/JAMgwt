package com.lostagain.JamGwt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.lostagain.Jam.JamAnswerButton;

public class GWTAnswerButton extends Button implements JamAnswerButton {

	public GWTAnswerButton(String mainGame_Submit) {
		super(mainGame_Submit);
	}



	@Override
	public Object getVisualRepresentation() {
		return this;
	}


	
	@Override
	public void addOnClickRunnable(final Runnable runthis) {
		
		super.addClickHandler(new ClickHandler() {	
			public void onClick(ClickEvent event) {
				
					runthis.run();
				
			}
		});
		
		
		
	}

}
