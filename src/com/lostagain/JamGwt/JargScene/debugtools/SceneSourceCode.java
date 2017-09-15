package com.lostagain.JamGwt.JargScene.debugtools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.google.gwt.dom.client.Style.Unit;
//import com.google.gwt.gen2.logging.shared.Log;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneDialogueObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneSpriteObjectState;
import com.lostagain.JamGwt.JargScene.SceneDialogObject;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;
import com.lostagain.JamGwt.JargScene.SceneTextObject;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyWormhole;

import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyWormhole;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyWormhole.IncomingMessageHandler;


/** will generate source code for specified object data **/
public class SceneSourceCode extends PopupPanel {

	TextArea sourceBox = new TextArea();
	VerticalPanel container = new VerticalPanel();
	
	public SceneSourceCode(SceneWidget data){
		
		super.setSize("600px", "500px");
		sourceBox.setSize("600px", "500px");
		container.setSpacing(2);
		container.getElement().getStyle().setBorderWidth(2, Unit.PX);
		
		container.add(new Label("Scenes Script Code:"));		
		container.add(sourceBox);
		container.add(new Label("NOTE: This source wont be the same as the input source"));
		container.getElement().getStyle().setBackgroundColor("#FFF");
		
		super.add(container);
		super.setModal(true);
		super.setGlassEnabled(true);
		super.setAutoHideEnabled(true);
		super.getElement().getStyle().setZIndex(99999);
		
		sourceBox.setText(generateSourceFromData(data));
						
		
	}
	
	/** loops over all scene objects using the ObjectSourceCode class to generate a string 
	 * that should represent what you need in the jam to generate that objects current state.
	 * Note; This "getSourceFromSceneObject" function is currently not complete.
	 * it doesn't generate all current parameters, nor actions/triggers **/
	private String generateSourceFromData(SceneWidget sourcedata) {
		
		String text = "\n";
		
		//get all the scenesobjects (eventually this will just be sceneobject array, not visual, as SceneData will eventually return that)
		Set<SceneObject> scenesObjects = sourcedata.getScenesData().allScenesCurrentObjects();
		
		
		for (SceneObject so : scenesObjects) {
			
			
			
			String currentObjectString = ObjectSourceCode.getSourceFromSceneObject(so);
			
			text=text+currentObjectString+"\n"+"\n";
			
		}
		
		/*
		
		//loop for each item, adding its data
		Iterator<SceneSpriteObject> sos = sourcedata.getScenesData().sceneSpriteObjects.iterator();
		
		while (sos.hasNext()) {
			
			SceneObjectVisual sceneObject = (SceneObjectVisual) sos.next();
			
			String currentObjectString = ObjectSourceCode.getSourceFromSceneObject(sceneObject);
			
			text=text+currentObjectString+"\n"+"\n";
			
		}
		
		//now the text objects
		Iterator<SceneTextObject> sot = sourcedata.getScenesData().SceneTextObjects.iterator();
		
		while (sot.hasNext()) {
			
			SceneObjectVisual sceneObject = (SceneObjectVisual) sos.next();
			
			String currentObjectString = ObjectSourceCode.getSourceFromSceneObject(sceneObject);
			
			text=text+currentObjectString+"\n"+"\n";
			
		}
		*/
		
	
		
		//wormhole test
		try {
			SpiffyWormhole.sendFunctionToParent("sceneData", "la la la");	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return text;
	}



	
}
