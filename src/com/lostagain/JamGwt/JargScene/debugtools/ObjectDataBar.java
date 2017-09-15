package com.lostagain.JamGwt.JargScene.debugtools;

import java.awt.Color;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObject;

/**
 * this is a simple bar with the objects name and scene on it
 * 
 * @author darkflame
 *
 */
class ObjectDataBar extends HorizontalPanel {
	Label itemNameLabel;
	Label itemsScenelabel;
	public ObjectDataBar(final SceneObject so){
		String itemName   = so.getObjectsCurrentState().ObjectsName;
		SceneWidget scene = so.getParentScene();			
		String itemsScene = "(no scene specified)"; //todo: make better sceneobject data bar
		if (scene!=null) {
			itemsScene = scene.SceneFileName;
		}
		
		itemNameLabel  = new Label(itemName);
		itemNameLabel.setTitle(so.getObjectsCurrentState().getCapabilitiesAsString());
		itemsScenelabel = new Label(itemsScene);
		
		itemNameLabel.addMouseOverHandler(new MouseOverHandler() {			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				itemNameLabel.getElement().getStyle().setColor("BLUE");
			}
		});
		
		itemNameLabel.addMouseOutHandler(new MouseOutHandler() {			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				itemNameLabel.getElement().getStyle().clearColor();
			}
		});
		
		
		this.add(itemNameLabel);
		this.setHorizontalAlignment(ALIGN_RIGHT);
		this.add(itemsScenelabel);
		this.setWidth("100%");
		
		//let people open it by clicking
		itemNameLabel.addClickHandler(new ClickHandler() {				
			@Override
			public void onClick(ClickEvent event) {
				so.openObjectsInspector();
				
			}
		});
		
		itemsScenelabel.getElement().getStyle().setColor("rgb(163, 216, 189)");
		
		
		
	}
}