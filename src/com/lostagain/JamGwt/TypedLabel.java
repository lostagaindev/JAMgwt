package com.lostagain.JamGwt;

import java.util.Iterator;
import java.util.logging.Logger;


import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.lostagain.Jam.TypedLabelCore;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.Jam.SceneObjects.SceneObjectType;
import com.lostagain.Jam.SceneObjects.Interfaces.IsSceneLabelObject;
import com.lostagain.Jam.SceneObjects.SceneObjectState.TrueFalseOrDefault;
import com.lostagain.JamGwt.JargScene.SceneLabelObject;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneWidgetVisual;
import lostagain.nl.spiffyresources.client.spiffycore.HasDeltaUpdate;

/** 
 * A widget that acts much like a Label widget, only it types out the text.
 * Like the bottom corner of a typical x-files episode.
 * Also lets things be dynamically inserted into the text 
 * **/
public class TypedLabel extends TypedLabelCore  {

	public static Logger Log = Logger.getLogger("JAM.TypedLabelCore");
	
	
	
	
	
	/**
	 * just a htmlpanel that knows its for a specific TypedLabel.
	 * This makes it possible to check if the parent widget in html is also a typed label 
	 * @author darkflame
	 */
	public static class HTMLPanelForTypedLabel extends HTMLPanel {
		public TypedLabel associatedTypedLabel;
		public HTMLPanelForTypedLabel(String html,TypedLabel associatedTypedLabel) {
			super(html);
			this.associatedTypedLabel=associatedTypedLabel;
		}
		
		/**
		 * purely to get access to orphan (in future might not be needed)
		 * @param child
		 */		
		public void orphanWidget(Widget child){
			super.orphan(child);
			return;
		}

		@Override
		protected WidgetCollection getChildren() {
			return super.getChildren();
		}
		
		
	}
	
	/**
	 * our htmlpanel
	 */
	public HTMLPanelForTypedLabel internalPanel;
	

	public HTMLPanelForTypedLabel getInternalWidget() {
		return internalPanel;
	}









	/** A widget that acts much like a Label widget, only it types out the text.
	 * Like the bottom corner of a typical xfiles episode.**/
	public TypedLabel(String StartingText) {
	//	super(StartingText);
		internalPanel = new HTMLPanelForTypedLabel(StartingText,this);
		
		// this.setText(StartingText);
		targetText = StartingText;
		i = 0;
		ObjectsToLoadIntoText.clear();

		//timer = new Timer() {
		//	public void run() {

		//		updateTyping();
		//	}

		//};

	}
	
	//	getElement().setInnerHTML(newText);
	@Override
	public void setTextInternalIMPL(String newText){
		this.internalPanel.getElement().setInnerHTML(newText);		
	}
	

	public void recheckInternalObjects(){
		Log.info("rechecking objects in dialogue");
		
		if (associatedObject!=null){
			associatedObject.ObjectsLog("Rechecking objects in dialogue. "+ObjectsToLoadIntoText.size()+" to load. Dialogue HTML is:"+internalPanel.getElement().getInnerHTML());
		}


		loadAnyNeededObjects();
	}


	@Override
	public void loadAnyNeededObjects() {
		if (!internalPanel.isAttached()){
			Log.severe(" was arnt attached yet! therefor we cant add objects to ourself_");		
			return;
		}

		if (ObjectsToLoadIntoText.size() > 0) {

			if (associatedObject!=null){

				Log.info("loading objects into:"+associatedObject.getObjectsCurrentState().ObjectsName);

				Log.info("div is currently:"+internalPanel.getElement().getInnerHTML());

			} else {

				Log.info("loading objects into a text label");

			}


			//log all the objects that need loading into the text;
			Log.info("objects to load into text = "+ObjectsToLoadIntoText.toString());			
			if (associatedObject!=null){
				//also log to thhe sceneobject, if there is one
				associatedObject.ObjectsLog("objects to load into label = "+ObjectsToLoadIntoText.toString());
			}
			
			//loop over them one at a time
			Iterator<String> objectIt = ObjectsToLoadIntoText.iterator();
			
			while (objectIt.hasNext()) {
				String objectName = objectIt.next();
				
				
				Log.info("Starting to load label:" + objectName);

				//get the object from its name
				SceneObject newobjectToInsert = SceneObjectDatabase
						.getSingleSceneObjectNEW(objectName,null,true);

				//we need to use SceneObjectVisuals for now, as we need to use getElement to insert them
				//in future part of this might be possible to do just with SceneObjects
				SceneObjectVisual newobject = (SceneObjectVisual)newobjectToInsert;		
				
				//---
				if (newobjectToInsert == null) {
					//if the requested object cant be found, we moan about it
					Log.severe("Error; could not find object to put in text called: "+ objectName+" in ");
					Log.severe("If object is still loading, that object should itself go into text once loaded");

					if (associatedObject!=null){
						//if the requested object cant be found, we moan about it in the labals associated object log too
						associatedObject.ObjectsLog("Error; could not find object to put in text called: "+ objectName+" in ");
						associatedObject.ObjectsLog("If object is still loading, that object should itself go into text once loaded");
					}

				} else {
					//---
					Log.info("found object:"+newobject.getObjectsCurrentState().ObjectsName+" (type :"+newobject.getObjectsCurrentState().getPrimaryObjectType().toString()+")");
					Log.info("load status :"+newobject.getLoadStatusDebug()); //gives a detailed report of the objects load status
					//-----					
					try {
						
						//
						//assuming its a label being inserted we have to prepare some stuff
						//
						
						//if (newobject.getObjectsCurrentState().getPrimaryObjectType() == SceneObjectType.Label){						
						if (newobject.getObjectsCurrentState().isCompatibleWith(SceneObjectType.Label)){ //new; we now check for compatibility, potentially allowing label subclasses to work too
							SceneLabelObject newObjectAsLabel = newobject.getAsLabel(); //now we have checked compatibility this should be safe to assume its a label.
							
							//Separate function to prepare a label for insert into this one
							prepareLabelForInsertIntoThisLabel(newObjectAsLabel);

						}
						//
						//-----------------------------------------
						//
						
						
						//
						//Now we start the process of insertion, as safely as we can
						//
						String elementIDToInsertObjectAt = "Item_" + objectName; //were we will put it (div id)
						
						Log.info("inserting a object: "+newobject.getName()+" at "+elementIDToInsertObjectAt);
						
						//check that ID even exists on the html page
						if (Document.get().getElementById(elementIDToInsertObjectAt)==null){
							Log.severe("cant find element with ID :"+elementIDToInsertObjectAt+":");
							Log.severe("should be in :"+internalPanel.getElement().getInnerHTML());
						} else {
							Log.info("found Div with correct ID ("+elementIDToInsertObjectAt+") on html:");
						}
						//---------
						
						if (!newobject.isAttached()){
							Log.severe("Element:"+newobject.getObjectsCurrentState().ObjectsName+ " is not attached. Objects should be attached on scene before insert");
						}
						
						/*
						if ( (newobject.getElement().getParentElement())==null){
							Log.severe("__________Element:"+newobject.getObjectsCurrentState().ObjectsName+ " has no parent element");
						} else	if (!newobject.getParent().isAttached()){
							Log.info("has parent element:"+newobject.getElement().hasParentElement());	
							Log.severe("Element:"+newobject.getObjectsCurrentState().ObjectsName+ " parent is not attached");
						} else {
							Log.info("has parent element:"+newobject.getElement().hasParentElement());	
						}*/

						Log.info("attempting to remove from  parent");

						//ie currently crashs with the following for some reason;
						//newobject.removeFromParent();

						//Only needed on IE10
						//Note; the temp casting, this is bad but might not be worth the effort fixing if this whole section becomes unneeded
						boolean removedfromback = ((SceneWidgetVisual)newobject.getParentScene()).removeFromSceneBackground(newobject);
						Log.info("|was removed from scene back:"+removedfromback);					
						Log.info("...(done):");
						//if (removedfromback==false){
						if (newobject.getParent()!=null){
						//	Log.severe("_ Element:"+newobject.getObjectsCurrentState().ObjectsName+ " has parent widget of class:"+newobject.getParent().getClass().getName());

							if (newobject.getParent().getClass() == HTMLPanelForTypedLabel.class){ 
								Log.info("already on a typed label");

								HTMLPanelForTypedLabel testhtmlpanel = (HTMLPanelForTypedLabel) newobject.getParent();
								TypedLabel test = testhtmlpanel.associatedTypedLabel;
										
								if (test==this){
									Log.severe("________________already on this widget!");								
								} else {	
									Log.severe("________________removing from old loading list!"+test.ObjectsToLoadIntoText.remove(objectName));
								}

								Log.info("test html of it is:"+testhtmlpanel.getElement().getInnerHTML());

								Log.info("test getCurrentText:"+test.getCurrentText());

								Log.info("test typing state of it is:"+test.currentlyTyping+","+test.aboutToType);
								//if (test.runAfterTextSet!=null){
								//	Log.severe("________________runAfterTextSet is true");								
								//}
								Log.info("ObjectsToLoadIntoText of parent:"+test.ObjectsToLoadIntoText.size());
								Log.info("isAttached:"+test.isAttached());
								

								Log.info("new detach method starting:");

								Log.info(" ~~~~~~~~~~~~~~~~~~~~~ (objects contents before detach is:"+newobject.toString());

								newobject.ObjectsLog("preparing detach content length="+newobject.toString().length());


								try {
									internalPanel.orphanWidget(newobject.getInternalGwtWidget());

									Log.info("orphaned");
								} finally {
									// Physical detach.

									Log.info("Physical detach");

									Element elem =newobject.getElement();
									if ( DOM.getParent(elem)!=null){

										Log.info("Physical detach2");
										DOM.getParent(elem).removeChild(elem);
									}

									// Logical detach.
									Log.info(" Logical detach.");
									internalPanel.getChildren().remove(newobject.getInternalGwtWidget());

								}


								newobject.ObjectsLog("detached content length="+newobject.toString().length());

							} else {

								/*
									Log.info(" starting insane manual detact method because I cant work out why IE11 wont take the normal one :(");
									try {
										orphan(newobject);

										Log.info("orphaned");
									} finally {
										// Physical detach.

										Log.info("Physical detach");

										Element elem =newobject.getElement();
										if ( DOM.getParent(elem)!=null){

											Log.info("Physical detach2");
											DOM.getParent(elem).removeChild(elem);
										}

										// Logical detach.
										Log.info(" Logical detach.");
										getChildren().remove(newobject);

									}*/

							}

						}

						//}


						


						//Log.info("has parent 2:"+newobject.getElement().hasParentElement());

						//add it.
						//note, its important to set it typing above before display
						//else we will see a flicker as it re-types itself from being fully typed
						if (associatedObject!=null){
							associatedObject.ObjectsLog("adding newobject "+newobject.getObjectsCurrentState().ObjectsName+" into text:"+this.getElement().getInnerHTML());
						}
						Log.info("****************** contents before adding is:"+super.toString());	
						Log.info("****************** contents we are trying to add is:"+newobject.toString());	//new object lacks label contents
						Log.info("****************** contents we are trying to add is type:"+newobject.getObjectsCurrentState().getPrimaryObjectType());	

						if (newobject.getObjectsCurrentState().getPrimaryObjectType()==SceneObjectType.Label){
							SceneLabelObject newObjectAsLabel = newobject.getAsLabel(); //now we have checked compatibility this should be safe to assume its a label.
							
							Log.info("****************** current textobject is :"+newObjectAsLabel.TextLabel.toString());	 //label has contents
							Log.info("****************** current text is typing:"+newObjectAsLabel.TextLabel.currentlyTyping);
							Log.info("****************** current target text is:"+newObjectAsLabel.TextLabel.targetText);
						}

						Log.info("****************** contents we are trying to add is attached:"+newobject.isAttached());	

						if (internalPanel.getElementById(elementIDToInsertObjectAt)==null){
							Log.warning("attempting to insert to an ID that doesnt exist");
						}

						newobject.ObjectsLog.log("attaching into:"+associatedObject.getObjectsCurrentState().ObjectsName,"yellow");				
						newobject.ObjectsLog("current content length="+newobject.toString().length());

						//--------------
						internalPanel.add(newobject.getInternalGwtWidget(), elementIDToInsertObjectAt);
						
						if (associatedObject!=null){
							associatedObject.ObjectsLog("Added "+newobject.getObjectsCurrentState().ObjectsName+" into text:"+this.getElement().getInnerHTML());
						}
						//now its added, remove from list of stuff to load so its not accidentally loaded again later
						objectIt.remove();
						//------------------
						

						newobject.ObjectsLog("attached into:"+associatedObject.getObjectsCurrentState().ObjectsName);
						newobject.ObjectsLog("current content length="+newobject.toString().length());

						Log.info("****************** added to typed label");	
						Log.info("****************** contents added to typelabel was object:"+newobject.getObjectsCurrentState().ObjectsName+" with contents:"+newobject.toString()+"|");	
						Log.info("****************** contents now is:"+super.toString());	

						if (newobject.getObjectsCurrentState().getPrimaryObjectType()==SceneObjectType.Label){
							SceneLabelObject newObjectAsLabel = newobject.getAsLabel(); //now we have checked compatibility this should be safe to assume its a label.
							
							Log.info("****************** after adding textobject is :"+newObjectAsLabel.TextLabel.toString());	//textobject is ALWAYS CORRECT

							if (newobject.getWidget()==newObjectAsLabel.TextLabel.internalPanel){
								Log.info("****************** newobjects widget is correct label");
							} else {
								Log.info("****************** newobjects widget is NOT THE correct label");
							}

							if (newobject.getWidget()==null){
								Log.info("**************** newobject added did not have its TypedLabel widget!");

							} else {
								Log.info("*************** widget within dialog object is a:"+newobject.getWidget().getClass().getName());



							}



							Log.info("****************** after adding textobject is :"+newObjectAsLabel.TextLabel.toString());	

							Log.info("****************** after adding is typing:"+newObjectAsLabel.TextLabel.currentlyTyping);
							Log.info("****************** after adding text is:"+newObjectAsLabel.TextLabel.targetText);
						}


						//make sure it has a matching position by div
						newobject.getObjectsCurrentState().attachToHTMLDiv=elementIDToInsertObjectAt;

						//make sure its scene is updated (in case we are importing objects from a different scene
						//note; I think this should happen but currently causes problems elsewhere
						//When a object moves from one scene to another, such as a text import might, it causes issues when saving
						//This is because it needs its original state from its starting scene in the jam files AND the updated information of its new state
						// To do this it needs to load the original JAM state BEFORE the updated information from the state.

						//Which means;
						//1. File loads scene A 
						//2. creates textobject from scene A
						//3. File loads scene B
						//4. creates dialogue that contains text object from scene A
						//5. dialogue puts object in

						//However, if file B loads first (which is possible) then A must know where to put itself
						//This is done via the attach to HTMLDiv variable above
						//But, again, that will only be set correctly once its state has been set thus
						//1. File loads scene B 
						//2. creates dialogue that contains text object from scene A but it cant find it
						//3. File loads scene A
						//4. creates textobject
						//5. text object state is updated 
						//6. object now knows to go into B

						//Because that state information needs to be with "scene A" we must leave the object on its original scene
						//In future we might need a way to move objects without them "belonging" to that new scene for the save system?
						//Or have a scene dependency system

						//Note; Experimental scene dependancy system added
						//The following might now work to allow saving
						if (associatedObject!=null){

							newobject.ObjectsLog("moving object to match parent dialogues scene");	
							newobject.ObjectsLog("associatedObject.objectScene="+associatedObject.getParentScene().SceneFileName);	
							newobject.ObjectsLog("objectsCurrentState.ObjectsSceneName="+associatedObject.getObjectsCurrentState().ObjectsSceneName);	

							this.associatedObject.ObjectsLog("moving child objects scene to match this ones ("+this.associatedObject.getParentScene().SceneFileName+")");

							newobject.setObjectsScene(this.associatedObject.getParentScene());
						}




					} catch (Exception e) {
						Log.severe("Error; when filling "+objectName+" into:");
						Log.severe (this.getElement().getInnerHTML());


						Log.severe("Error was  :"+e.getLocalizedMessage());


					}

				}


			}

		}
		else 
		{
			Log.info ("no objects to load into typed label.");
		}

	}



	
	/**
	 * ONLY USE FOR DEBUGGING
	 * @return
	 */
	@Deprecated
	public Element getElement() {
		return internalPanel.getElement();
	}


	//private Element getElement() {
	//	return internalPanel.getElement();
	//}



	@Override
	public boolean isAttached() {
		return internalPanel.isAttached();
	}


	








	protected void prepareTypedLabelForInsert(IsSceneLabelObject newObjectAsLabel) {
		super.prepareTypedLabelForInsert(newObjectAsLabel);
		((SceneLabelObject)newObjectAsLabel).setCursorMode(this.cursorStyle);
		((SceneLabelObject)newObjectAsLabel).setCursorClass("inlinecursor");

	}

	@Override
	public void clear() {
		internalPanel.clear();
	}



	public void setDirection(Direction ltr) {

		// set direction
		Log.info("___setting direction to:" + ltr.toString());
		//.gete
		//super.getElement().setDir(ltr.toString());

		internalPanel.getElement().setDir(ltr.toString());

	}



	public void setCursorClass(String string) {

		cursorclass=string;

	}



}
