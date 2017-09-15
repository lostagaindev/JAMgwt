package com.lostagain.JamGwt.JargScene.debugtools;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.InstructionProcessing.ActionSet;
import com.lostagain.Jam.SceneObjects.SceneLabelObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneSpriteObjectState;
import com.lostagain.JamGwt.JargScene.SceneDialogObject;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;
import com.lostagain.JamGwt.JargScene.SceneTextObject;

/** will generate source code for specified object data.<br>
 * WIP only; needs to be completed and then compared and with actual JAM files <br>
 * to see if it matches<br>
 * 
 *  **/
public class ObjectSourceCode extends PopupPanel {

	TextArea sourceBox = new TextArea();
	VerticalPanel container = new VerticalPanel();
	
	public ObjectSourceCode(SceneObject sourceObject){
		
		//set up the style, size and contents (aka, the easy stuff)
		super.setSize("400px", "300px");
		sourceBox.setSize("400px", "300px");
		
		container.setSpacing(2);
		container.getElement().getStyle().setBorderWidth(2, Unit.PX);
		
		container.add(new Label("Objects Script Code:"));		
		container.add(sourceBox);
		container.add(new Label("NOTE: Actions and Parameters dont yet fully match what should be in the JAM file"));
		container.getElement().getStyle().setBackgroundColor("#FFF");
		
		super.add(container);
		
		//ensure when this popup is open the background is grayed out
		//and no other scene actions will be triggered
		super.setModal(true);
		super.setGlassEnabled(true);
		super.setAutoHideEnabled(true);
		
		//set popup panels z-index
		super.getElement().getStyle().setZIndex(99999);
		
		//does what it says!		
		String source = getSourceFromSceneObject(sourceObject);
		
		//with the source text we display it
		sourceBox.setText(source);
						
		
		
		
	}
	
	/** get the source from the scene object  data**/
	static  public String getSourceFromSceneObject(SceneObject sourceObject ){
		
		String source = "";
		
		//generic for all object types
		source = source +  "-Item: \n";
		source = source + sourceObject.getObjectsCurrentState().getParametersAsCode();
		
		
		//different based on type of object
		//(no longer needed, each type of state object should correctly override the getParametersAsCode() function
		//to return the correct set of data)
		/**
		if (sourceObject.getClass() == SceneSpriteObject.class) {

			SceneSpriteObjectState currentdata = ((SceneSpriteObject) sourceObject).objectsCurrentState;
		
			source = ObjectSourceCode.generateSourceFromSpritesData(currentdata);
						
		}
		if (sourceObject.getClass() == SceneTextObject.class || sourceObject.getClass() == SceneDialogObject.class) {

			SceneDialogObjectState currentdata = ((SceneDialogObject) sourceObject).objectsCurrentState;
			
			source = ObjectSourceCode.generateSourceFromTextsData(currentdata);
						
		}
		*/

		//attempt to add actions here
		if (sourceObject.objectsActions.size()>0){
			
			source = source +" (actions not fully implemented) \n";
				
			for (ActionSet actions : sourceObject.objectsActions) {
				
				source = source + "\n"+ actions.getCode()+"\n";
				
			}
			
			
			
		}
		
		
		return source;
	}
	
	private static String generateSourceFromTextsData(
			SceneLabelObjectState data) {
		
		String text = "";// "-Item: \n";
		
		//text = text + data.getParametersAsCode(); //gets the params as code (currently generic params only)
		/*
		String text = "-Item: \n";
		text=text+"  Name = "+data.ObjectsName+"\n";
		text=text+"  Title = "+data.Title+"\n";		
		text=text+"  DefaultURL = "+data.ObjectsCurrentURL +"\n";
		text=text+"  Located = "+data.X+","+data.Y +"\n";
		text=text+"  DefaultText = "+data.ObjectsCurrentText +"\n";;
		text=text+"  zIndex = "+data.zindex+"\n";
		*/
		return text;
	}

	/** 
-Item:
 Name = TheRoom_pc0.png
 Title = 'The computer'
 Frames = 2
 Located = 471,722
 Properties = Flammable
 zIndex = 751
  **/
	/**
	 * sprite states should handle generating source now
	 * @param data
	 * @return
	 */
	@Deprecated
	static public String generateSourceFromSpritesData(SceneSpriteObjectState data){
		
		String text = "";//
		
		//incomplete parameters 
		//this needs to be updated to make a string of all parameters EXACTLY
		//as they are in the source jam files
		//String text = "-Item: \n";
		
		//text = text + data.getParametersAsCode(); //gets the params as code (currently generic params only)
		
		/*
		text=text+"  Name = "+data.ObjectsName+"\n";
		text=text+"  FileName = "+data.ObjectsFileName+"\n";
		text=text+"  Title = "+data.Title+"\n";		
		text=text+"  Frames = "+data.currentNumberOfFrames +"\n";
		text=text+"  Located = "+data.X+","+data.Y +"\n"; //for example, this doesnt support relative stuff
		
		//the following should only be filled in if there is propertys
		if (data.objectsProperties.size()>0){
			text=text+"  Properties = "+data.objectsProperties.toString()+"\n";
		}
		//same for other optional data
		
		text=text+"  zIndex = "+data.zindex+"\n";
		*/
		
		//return the new object paramter string
		return text;
			
		
	}
	
	/** 
-Item:
 Type = TextBox
 Name = Text1
 Title = 'test box'
 Located = 1155,805
 Size = 300,100
 CSSname = dialogue_right
 DefaultText = "Hello! This is the default text! Yaaaaaaaaaaaaa!"
 DefaultURL = Dialog1.html
 zIndex = 10000 **/
public String generateSourceFromDialogueData(SceneLabelObjectState data){
		
		String text = "";
		
		//eek this ones worse, got nothing here yet
		//given theres a lot of common data, we should probably have a "generalSourceFromObject"
		//function to get the Type,Name,Title and located strings
		//then the other stuff seperately in the specific functions like this one
		 
		return text;
		
		
	}
	
}
