package com.lostagain.JamGwt.JargScene.debugtools;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;

public class FrameControlls extends HorizontalPanel {

	//Helpers to preview frame forward/back
	Button playBack = new Button("(PBack)");
	Button firstFrame = new Button("|<");
	Button backFrame = new Button("<");

	//current frame
	Label cframe = new Label();

	Button nextFrame = new Button(">");
	Button lastFrame = new Button(">|");
	Button playForward = new Button("(PForward)");

	public FrameControlls(SceneSpriteObject objectToControl) {
		super();

		//animation frames-------
		playBack.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				objectToControl.setAnimationStatus("PlayBack");
				
			}
		});
		
		
		firstFrame.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				objectToControl.setAnimationStatus("gotoframe0");
			}
		});

		backFrame.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				objectToControl.setAnimationStatus("PrevFrame");
			}
		});
		nextFrame.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				objectToControl.setAnimationStatus("NextFrame");
			}
		});
		lastFrame.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				objectToControl.setAnimationStatus("gotoframe"+(objectToControl.getObjectsCurrentState().currentNumberOfFrames-1));
			}
		});
		playForward.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				objectToControl.setAnimationStatus("PlayForward");
				
			}
		});
		//-----------------------


		this.add(playBack);
		this.add(firstFrame);
		this.add(backFrame);
		this.add(cframe);
		this.add(nextFrame);
		this.add(lastFrame);
		this.add(playForward);
	}


	public void setText(String string) {
		cframe.setText(string);

	}

}
