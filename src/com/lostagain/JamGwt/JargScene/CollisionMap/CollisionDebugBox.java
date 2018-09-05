package com.lostagain.JamGwt.JargScene.CollisionMap;

import java.util.logging.Logger;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.IsCollisionLogger;
import com.lostagain.Jam.Movements.MovementPath;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.Jam.SceneObjects.SceneObjectDatabase;
import com.lostagain.JamGwt.TitledPopUpWithShadow;
import com.lostagain.JamGwt.JargScene.debugtools.ObjectInspector;

import lostagain.nl.spiffyresources.interfaces.IsSpiffyGenericLogBox;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyLogBox;



/**  a popup to help debug the games collisions and pathfinding **/
public class CollisionDebugBox extends TitledPopUpWithShadow implements IsCollisionLogger {

	static Logger Log = Logger.getLogger("JAM.CollisionDebugBox");
	//internally the collision log uses a SpiffyLogBox to do most of the work
	//this is the logbox it will use;
	private static IsSpiffyGenericLogBox CollisionLog = SpiffyLogBox.createLogBox(JAMcore.DebugMode);
	
	
	
	CollisionDebugBox thisbox=this;
	static ObjectInspector sourceInspector = null;
	 
	static public void setSourceInspector(ObjectInspector setsourceInspector) {
		sourceInspector = setsourceInspector;
	}

	public CollisionDebugBox(SceneWidget theCollisionsScene,ObjectInspector setSourceInspector) {
		
		super(null, "470px", "500px", "Collision Log", new Label(
				"loading..."), true);
		//set the main widget of this popup
		super.setCenterWidget(CollisionLog);
		
		this.sourceInspector = setSourceInspector;
		
		//set the collision scene this box refers too
		//this.collisionsScene = theCollisionsScene;
		
		//allow selections of this boxs text (disabled by default now)
		super.setContentsSelectable(true);
		
		//set the background style of the internal spiffy log box
		CollisionLog.setBackgroundColour("#FFF");
		
		//(the above is a crude cast atm, not strictly needed as its just to set the back color)
		
		//add a checkbox to determain if this has a forced high zindex value (usefull to keep it in front of
		//things if we wnat to keep the log in view at all times.
		CheckBox ontopBox = new CheckBox("on top");
		thisbox.getElement().getStyle().setZIndex(80000);
		
		thisbox.fixedZdepth(80000);
		
		ontopBox.setValue(true);
		//changes the zindex on click
		ontopBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()){
					thisbox.fixedZdepth(80000);
				}  else {
					thisbox.fixedZdepthOff();
				}
			}
		});
		
		//add a button to clear the current sketch map (which previews the pathfinding visualy)
		Button clearSketch = new Button("clear sketch");
		
		clearSketch.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				SceneWidget currentScene = 	SceneObjectDatabase.currentScene;
				
				if (currentScene.scenesCmap.isPresent()){
					currentScene.scenesCmap.get().clearSketch();
					currentScene.scenesCmap.get().clearCalculatedPath();
				}
				
			}
		});
		
		Button pasteNewCollisionMap = new Button("Manually Type Map");
		
		pasteNewCollisionMap.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				//display popup with text box
				//use text as new collision data

				if (sourceInspector !=null){	
					
					String newPath = Window.prompt("enter path", "M 0,0 L 0,10 L 10,10 Z");
						
								//currentObject		
					sourceInspector.currentPath=newPath;
					sourceInspector.stopEditingObjectsCmap();
				}
			}
		});

		//add these buttons (rather wastefull, we keep re-adding them as its a static and really should only be done once)
		CollisionLog.clearAddedControlls();
		CollisionLog.addControl(clearSketch);
		CollisionLog.addControl(ontopBox);
		CollisionLog.addControl(pasteNewCollisionMap);
		
		
	}
	
	//set pixel size on load 
	@Override
	public void onLoad(){
		CollisionLog.setPixelSize(470, 480);
	}
	
	//functions to log stuff in various ways
	public void log(String logThis,String color){
		CollisionLog.log(logThis, color);
		
	}
	public void log(String logThis){
		CollisionLog.log(logThis);
		
	}
	
	public void info(String logThis){
		CollisionLog.info(logThis);
		
	}

	public void error(String logThis){
		CollisionLog.error(logThis);
		
	}
/** logs the details of a movement path. 
 * Usefully, the path object in the log lets you toggle the path on/off to help debugging **/
	public void logPath(MovementPath np) {
		
		PathLogObject pathlog = new PathLogObject(np);
		
		
		CollisionLog.addWidgetToList(pathlog);
		
		
		
	}
	
	/** a horizontal panel that can be put into the log
	 * it represents a path, giving it a decent style to show its details
	 * and a toggle button to show/hide it **/
	class PathLogObject extends HorizontalPanel {
		
		/**
		 * a horizontal panel that can be put into the log
	 * it represents a path, giving it a decent style to show its details
	 * and a toggle button to show/hide it
		 * @param np - path to display in the log
		 */
		public PathLogObject(final MovementPath np) {
			
			//make a label from the paths data
			Label pathData = new Label(np.getAsSVGPath());
			
			//make the label red if the path broke (ie, it had errors and had to skip to the end)
			if (np.pathFindingBroke){
				pathData.getElement().getStyle().setColor("#FF0000");
			} else {
				pathData.getElement().getStyle().setColor("#AAAA00");
			}
			
			//set the font size
			pathData.getElement().getStyle().setFontSize(75, Unit.PCT);
			//set the tool tip to the name of the path
			pathData.setTitle(np.pathsName);
			//add this paths data label created above as the first element in this horizontal panel
			this.add(pathData);
			
			//create a checkbox
			CheckBox viewPath = new CheckBox();
			//if the checkbox is toggled show/hide the path
			viewPath.addValueChangeHandler(new ValueChangeHandler<Boolean>() {			
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {

					SceneWidget currentScene = 	SceneObjectDatabase.currentScene;
					//if the value is true then show the path
					if (event.getValue()){
					
						//CollisionLog.log("displaying:"+np.getAsSVGPath(),"#00DDFF");
						
						if (currentScene.scenesCmap.isPresent()){
						//	Log.info("adding debug path to cmap");
							
							currentScene.scenesCmap.get().addToSketch(np.getAsSVGPath(), "#00DDFF");
							currentScene.scenesCmap.get().showPath(true);
							
						}
						
					} else {
						//else remove the path
						if (currentScene.scenesCmap.isPresent()){
						//	Log.info("removing debug path to cmap");
							
							currentScene.scenesCmap.get().removeFromSketch(np.getAsSVGPath(), "#00DDFF");
						}
						//CollisionLog.log("removing:"+np.getAsSVGPath(),"#00DDFF");
					}
				}	
				
			});
			
			//add the checkbox created above
			this.add(viewPath);
		}
		
	}
}
