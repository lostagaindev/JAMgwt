package com.lostagain.JamGwt.JargScene.SceneObjects;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.SceneAndPageSet;
import com.lostagain.Jam.Factorys.JamMenuBar;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.Interfaces.IsBamfImage;
import com.lostagain.Jam.Interfaces.IsPopupPanel;
import com.lostagain.Jam.Interfaces.hasInventoryButtonFunctionality;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.Jam.Scene.SceneData;
import com.lostagain.Jam.Scene.SceneMenuWithPopUp;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.Scene.TextOptionFlowPanelCore;
import com.lostagain.Jam.SceneObjects.SceneDialogueObjectState;
import com.lostagain.Jam.SceneObjects.SceneDivObjectState;
import com.lostagain.Jam.SceneObjects.SceneInputObjectState;
import com.lostagain.Jam.SceneObjects.SceneLabelObjectState;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectFactory;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.SceneSpriteObjectState;
import com.lostagain.Jam.SceneObjects.SceneVectorObjectState;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem;
import com.lostagain.Jam.SceneObjects.Interfaces.IsInventoryItem.IconMode;
import com.lostagain.JamGwt.GWTSceneAndPageSet;
import com.lostagain.JamGwt.TitledPopUpWithShadow;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;
import com.lostagain.JamGwt.InventoryObjectTypes.InventoryPanel;
import com.lostagain.JamGwt.JargScene.GwtMenuBar;
import com.lostagain.JamGwt.JargScene.GwtTextOptionFlowPanel;
import com.lostagain.JamGwt.JargScene.SceneDialogObject;
import com.lostagain.JamGwt.JargScene.SceneDivObject;
import com.lostagain.JamGwt.JargScene.SceneInputObject;
import com.lostagain.JamGwt.JargScene.SceneLabelObject;
import com.lostagain.JamGwt.JargScene.SceneSpriteObject;
import com.lostagain.JamGwt.JargScene.SceneVectorObject;
import com.lostagain.JamGwt.JargScene.SceneWidgetVisual;
import com.lostagain.JamGwt.Sprites.InternalAnimations;

// GWT specific implementation of SceneObjectFactory
/**
 * To use;
 * run GWTSceneObjectFactory.setup() once sometime during the games initialization
 * 
 * @author darkflame
 *
 */
public class GWTSceneObjectFactory extends SceneObjectFactory {

	static Logger SOLog = Logger.getLogger("JAM.GWTSceneObjectFactory");
	
	/**
	 * must be run before anything else will work
	 */
	static public void setup(){
		setup(new GWTSceneObjectFactory());
	}
		

	/**
	 * clones a given object
	 * currently only supports spites
	 * easy to upgrade function to support more, but have to work out how to do it without 
	 * a lot of duplicate code
	 *
	//TODO: move as much as we can to core, then start supporting other types of clones
	//we can use createObjectFromExistingDataImplementation probably?
	@Override
	protected SceneObject returnCloneImplementation(SceneObject object, String clonesName) {
			
		SceneObject clone = null;
		SceneWidget objectScene = object.getParentScene(); 
	//	SceneObjectType objectTypeToClone = object.getObjectsCurrentState().getPrimaryObjectType();
		
		
		//first we do the global stuff needed for all sceneobject types
		//first copy our state 
		SceneObjectState newClonesState = object.getObjectsCurrentState().copy();			
		
		//change this new state where needed
		newClonesState.ObjectsName = clonesName;
		newClonesState.ObjectsSceneName = objectScene.SceneFileName;
		SOLog.info("new state scene set too:"+newClonesState.ObjectsSceneName);
		ActionList objectsActions = object.objectsActions;
		
		//Now get the object itself
		//createObjectFromExistingDataImplementation
		clone =	createObjectFromExistingDataImplementation(newClonesState,  objectsActions ,objectScene);
		
		/*
		if (objectTypeToClone == SceneObjectType.Sprite) {
			
			//get the object casted
			SceneSpriteObject spriteObjectToClone = (SceneSpriteObject) object;
			
			//debug stuff (temp)
			SOLog.info("cloning "+spriteObjectToClone.getName()+". Should be in focus panel="+spriteObjectToClone.shouldBeInFocusPanel());	
			
			//copy the object		
			//Note: we cast to ensure the correct constructor is triggered, as we dont want to lose information from the sprites state
			
			SceneSpriteObject spriteclone = new SceneSpriteObject( (SceneSpriteObjectState) newClonesState,  objectsActions ,objectScene);
			SOLog.info("cloned is "+spriteclone.getName()+". Should be in focus panel="+spriteclone.shouldBeInFocusPanel());	
			
			clone = spriteclone;
			
		} else {

			SOLog.severe("Cloning on non-sprite objects not yet supported");

		}
		
		if (objectTypeToClone == SceneObjectType.Div) {

			// not supported yet

			// make sure it knows where it comes from
			// clone.objectsData.clonedFrom = this;
			return null;
		}

		
		//
		
		
			//add it to the scene at a arbitrary point			
			//This function deals with pointEvents and shouldBeInFocusPanel() differences;
			objectScene.addObjectToScene(clone);
			//
			
			//now update its state
			clone.alreadyLoaded = false;

			//spriteclone.updateState(newSpriteState, true);
			clone.initCurrentState(true);		//just for sprites implemented? probably all object types need this?

			// add to scene
			SOLog.info("adding clone object to scenes lists:" + clone.getObjectsCurrentState().ObjectsName);

			//objectScene.getScenesData().sceneSpriteObjects.add((SceneSpriteObject) clone); 
			objectScene.getScenesData().addToScenesObjects(clone);

			// make sure it knows where it comes from
			clone.getObjectsCurrentState().clonedFrom = object;
			
		
		

		
		return clone;

		
		
		
		
		
	}*/


	/**
	 * Creates a new object of the correct type from the data supplied
	 * 
	 * @param sceneObjectData - the scene object data of the correct subtype
	 * @param actions - the actions the object will have (can be null)
	 * @param sceneItBelongsTo - the scene the object belongs too
	 * @return a SceneObjectVisual as a SceneObject
	 *
	@Override
	protected SceneObject createObjectFromExistingDataImplementation(
			SceneObjectState sceneObjectData, 
			ActionList actions,
			SceneWidget sceneObjectBelongsTo) {
		
		
		//TODO: cant we just use createNewObjectImplementation and then update its state? move to core?
		
		SceneWidget sceneItBelongsTo = sceneObjectBelongsTo; 
		
		//the variable for the new object
		SceneObject newObject = null; 
		
		//specific creation varies based on type
		SceneObjectType primaryObjectType = sceneObjectData.getPrimaryObjectType();
		
		
		newObject = createNewObjectImplementation(actions,sceneObjectData,sceneObjectBelongsTo);

		newObject.updateState(sceneObjectData); //runs updateState(sceneObjectData,true,true) on subtype
		
		
		/*
		switch (primaryObjectType) {
		
		case Sprite:
			SceneSpriteObjectState spritedata = (SceneSpriteObjectState) sceneObjectData;
			SceneSpriteObject newSpriteObject = new SceneSpriteObject( spritedata,  actions , sceneItBelongsTo);
			//newSpriteObject.finaliseAndAddToDatabase();
			
			newSpriteObject.updateState(spritedata, true,true);
			newObject = newSpriteObject;
			break;
		case DialogBox:
			SceneDialogueObjectState diadata = (SceneDialogueObjectState) sceneObjectData;						
			SceneDialogObject newDiaObject = new SceneDialogObject( diadata,  actions , sceneItBelongsTo);
			//newDiaObject.finaliseAndAddToDatabase();
			
			newDiaObject.updateState(diadata, true,true);
			newObject = newDiaObject;
			//data = diadata;						
			break;
		case Div:
			SceneDivObjectState divdata = (SceneDivObjectState) sceneObjectData;						
			SceneDivObject newdivObject = new SceneDivObject( divdata,  actions , sceneItBelongsTo);
			//newdivObject.finaliseAndAddToDatabase();
			
			newdivObject.updateState(divdata, true,true);
			newObject = newdivObject;
			//data = divdata;		
			break;
		case Input:
			SceneInputObjectState inputdata = (SceneInputObjectState) sceneObjectData;						
			SceneInputObject newinputObject = new SceneInputObject( inputdata,  actions , sceneItBelongsTo);
			//newinputObject.finaliseAndAddToDatabase();
			
			newinputObject.updateState(inputdata, true,true);
			newObject = newinputObject;
			//data = inputdata;		
			break;
		case Label:
			SceneLabelObjectState labdata = (SceneLabelObjectState) sceneObjectData;						
			SceneLabelObject newlabObject = new SceneLabelObject( labdata,  actions , sceneItBelongsTo);
			//newlabObject.finaliseAndAddToDatabase();
			
			newlabObject.updateState(labdata, true,true);
			newObject = newlabObject;
			//data = labdata;		
			break;
		case Vector:
			SceneVectorObjectState vecdata = (SceneVectorObjectState) sceneObjectData;						
			SceneVectorObject newvecObject = new SceneVectorObject( vecdata,  actions , sceneItBelongsTo);
			//newvecObject.finaliseAndAddToDatabase();
			
			newvecObject.updateState(vecdata, true,true);
			newObject = newvecObject;
			//data = vecdata;		
			break;
		default:
			break;
		}
		
		return newObject;
	}
		*/
		

	//TODO: We need a factory method to make InventoryPanels
	//TODO: We need a factory method to make basic sprites (more or less just AnimatedIcons?)
	
	@Override
	protected SceneObject createNewObjectImplementation(ActionList actions, 
													    SceneObjectState newobjectdata,
													    SceneWidget sceneObjectBelongsTo) 
	{
		SceneObject newObject = null;
		SceneWidget scenesWidget =  sceneObjectBelongsTo; 
	
		
		SceneObjectType primaryObjectType = newobjectdata.getPrimaryObjectType();
		SOLog.info("Creating a "+primaryObjectType+" sceneobject");
		
		switch (primaryObjectType) {
		case Sprite:
			//	newObject =  new SceneSpriteObject(newobjectdata,currentItemData, scenesWidget);
			
			SceneSpriteObjectState spriteState = (SceneSpriteObjectState)newobjectdata;
			newObject =  new SceneSpriteObject(spriteState,actions, scenesWidget);
			
			//sceneSpriteObjects.add((SceneSpriteObject) newObject);
			//scenesCurrentObjects.add(newObject);
			//scenesOriginalObjects.add(newObject);
			//add to relative object if set
			//if (newobjectdata.positionedRelativeToo!=null){					
			//	newObject.ObjectsLog("assigning position relative to:"+newobjectdata.positionedRelativeToo.objectsCurrentState.ObjectsName);
			//	newobjectdata.positionedRelativeToo.relativeObjects.add(newObject);					
			//} else {
			//	newObject.ObjectsLog("no object to position relative to present yet or set");
			//	newObject.ObjectsLog("positionRelativeToOnceLoaded:"+newobjectdata.positionRelativeToOnceLoaded);
			//}
			break;
		case Label:
			SceneData.Log.info("setting up label...");
			//newObject =  new SceneTextObject(newobjectdata,currentItemData, scenesWidget);
			//newObject =  new SceneLabelObject(newobjectdata,currentItemData, scenesWidget);
			
			SceneLabelObjectState labelstate = (SceneLabelObjectState)newobjectdata;
			newObject =  new SceneLabelObject(labelstate,actions, scenesWidget);
			
			//--
			//scenesCurrentObjects.add(newObject);
			//scenesOriginalObjects.add(newObject);
			//SceneData.Log.info("label box set up...");
			//add to relative object if set
			//if (newobjectdata.positionedRelativeToo!=null){
			//	newobjectdata.positionedRelativeToo.relativeObjects.add(newObject);
			//}
			//SceneData.Log.info("label setup...");
			break;
		case DialogBox:
			//SceneData.Log.info("setting up dialogue box...");
			//restrict to screen by default if its a dialog
			newobjectdata.restrictPositionToScreen=true;
			//newObject =  new SceneDialogObject(newobjectdata,currentItemData, scenesWidget);	
			
			SceneDialogueObjectState dialogueState = (SceneDialogueObjectState)newobjectdata;
			newObject =  new SceneDialogObject(dialogueState,actions, scenesWidget);
			
			//--				
			//scenesCurrentObjects.add(newObject);
			//scenesOriginalObjects.add(newObject);
			//SceneData.Log.info("dialogue box set up...");
			//add to relative object if set
			//if (newobjectdata.positionedRelativeToo!=null){
			//	newobjectdata.positionedRelativeToo.relativeObjects.add(newObject);
			//}
			break;
		case Div:
			//SceneData.Log.info("setting up div box...");
			//dont restrict to screen by default if its a div
			newobjectdata.restrictPositionToScreen=false;
			//newObject =  new SceneDivObject(newobjectdata,currentItemData, scenesWidget);
			
			SceneDivObjectState divState = (SceneDivObjectState)newobjectdata;
			newObject =  new SceneDivObject(divState,actions, scenesWidget);
			//--				
		//	SceneDivObjects.add((SceneDivObject) newObject); 	
			//scenesCurrentObjects.add(newObject);
			//scenesOriginalObjects.add(newObject);
			//SceneData.Log.info("div box set up...");
			//add to relative object if set
			// (newobjectdata.positionedRelativeToo!=null){
			//	newobjectdata.positionedRelativeToo.relativeObjects.add(newObject);
			//}
			break;
		case Vector:
			//SceneData.Log.info("setting up vector box...");
			//dont restrict to screen by default if its a div
			newobjectdata.restrictPositionToScreen=false;
			//newObject =  new SceneVectorObject(newobjectdata,currentItemData, scenesWidget);
			
			SceneVectorObjectState vectorState = (SceneVectorObjectState)newobjectdata;
			newObject =  new SceneVectorObject(vectorState,actions, scenesWidget);
			
			//--				
			//SceneVectorObjects.add((SceneVectorObject) newObject); 		
			//scenesCurrentObjects.add(newObject);
			//scenesOriginalObjects.add(newObject);
			//SceneData.Log.info("vector box set up...");
			//add to relative object if set
			//if (newobjectdata.positionedRelativeToo!=null){
			//	newobjectdata.positionedRelativeToo.relativeObjects.add(newObject);
			//}
			break;
		case Input:
			//SceneData.Log.info("setting up input box...");
			//dont restrict to screen by default if its a div
			newobjectdata.restrictPositionToScreen=false;
			//	newObject =  new SceneInputObject(newobjectdata,currentItemData, scenesWidget);
			
			SceneInputObjectState inputState = (SceneInputObjectState)newobjectdata;
			newObject =  new SceneInputObject(inputState,actions, scenesWidget);
			
			//--				
		//	SceneInputObjects.add((SceneInputObject) newObject); 				
			//scenesCurrentObjects.add(newObject);
			//scenesOriginalObjects.add(newObject);
			//SceneData.Log.info("input box set up...");
			//add to relative object if set
			//if (newobjectdata.positionedRelativeToo!=null){
			//	newobjectdata.positionedRelativeToo.relativeObjects.add(newObject);
			//}
			break;
		case InventoryObject:
			SOLog.info("Creating a "+primaryObjectType+" sceneobject NOT SUPPORTED HERE");			
			break;
		case SceneObject:
			SOLog.info("Creating a default "+primaryObjectType+" sceneobject NOT SUPPORTED HERE");	
			break;
		default:
			break;
		}
		//newObject.finaliseAndAddToDatabase();
		
		return newObject;
	}


	@Override
	public hasInventoryButtonFunctionality createNewInventoryButtonImplementation(String location, int aniLen) {
		
		
		final hasInventoryButtonFunctionality NewInventoryButton;
		
		if (location.startsWith("<")){
			//if the location starts with < then its a internal animation
			String internalname = location.substring(1,	location.length() - 1);
			SOLog.info("__creating inventory button from internal name:" + location);

			//get correct animation from internal name
			AbstractImagePrototype[] internalAnimation = InternalAnimations.getAnimation(internalname);
			NewInventoryButton = new GWTAnimatedIcon(internalAnimation);


			return NewInventoryButton;
		} else {
			NewInventoryButton = new GWTAnimatedIcon(location, aniLen);

			return NewInventoryButton;
		}
		
		
		
	}


	@Override
	public IsBamfImage createNewBamfObjectImplementation(String location) {
		IsBamfImage newBamfObject = new BamfImage(location);
		
		return newBamfObject;
	}


	@Override
	protected InventoryPanelCore createInventoryPanelImplementation(String Name, IconMode Mode) {
		return new InventoryPanel(Name, Mode);
	}


	@Override
	protected IsPopupPanel createTitledPopUpImplementation(
			IsInventoryItem IconTrigger, 
			String X, 
			String Y, 
			String title,
			IsPopupContents SetContents) {
		
		return new TitledPopUpWithShadow(
			     IconTrigger,
			     X,
			     Y,
				 title,
				 SetContents);
	}


	
	public SceneWidget makeNewSceneImplementation(String currentProperty) {
		
		//TODO:
		//probably need finalize for scene as well?
		//only place objects when we are sure the scene itself is ready?
		SceneWidget scene = new SceneWidgetVisual(currentProperty);
		//scene.finalize(); //do this after attaching object to page
		//Thats because finalize creates the objects on the scene and some objects need dom references
		//err...by finalize I meant initialize
		return scene;
	}


	@Override
	protected SceneAndPageSet createSceneAndPageSetImplementation(String chapterName) {
		return new GWTSceneAndPageSet(chapterName);
	}
	@Override
	protected IsPopupPanel createBasicPopUpImplementation(IsPopupContents SetContents) {
		return new TitledPopUpWithShadow(SetContents);
	}


	@Override
	public JamMenuBar createJamMenuBarImplementation(SceneMenuWithPopUp parentbox) {
		return  new GwtMenuBar(parentbox);
	}


	@Override
	public TextOptionFlowPanelCore createTextOptionFlowPanelImplementation(ArrayList<String> options,
			SceneObject callingObject, String divName, String tabName) {
		return new GwtTextOptionFlowPanel(options,callingObject,divName,tabName);
	}
	
		
}
