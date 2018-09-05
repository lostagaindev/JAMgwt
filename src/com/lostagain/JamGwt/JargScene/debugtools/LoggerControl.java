package com.lostagain.JamGwt.JargScene.debugtools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.CollisionMap.PolySide;
import com.lostagain.Jam.CollisionMap.PolygonCollisionMap;
import com.lostagain.Jam.CollisionMap.SceneCollisionMap;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Movements.MovementPath;
import com.lostagain.Jam.Movements.SpiffyPath;
import com.lostagain.JamGwt.JAM;

public class LoggerControl extends VerticalPanel {
	public static Logger Log = Logger.getLogger("JAMCore.LoggerControl");

	static ArrayList<LogsControl> controls = new ArrayList<LogsControl>();
	
	public LoggerControl() {
		super();
		
		//controls to effect all of them
		HorizontalPanel allPan = new HorizontalPanel();
		allPan.add(new Label("Change All:"));
		
		
		logLevelChangeAll changeAllDropDown = new logLevelChangeAll(controls);
		allPan.add(changeAllDropDown);
		
		//presets
		PresetButton movementset = new PresetButton(" Movement",MovementPath.Log,SceneCollisionMap.Log,PolySide.Log,PolygonCollisionMap.GreyLog,SpiffyPath.Log);
		allPan.add(new Label("Presets:"));
		allPan.add(movementset);
		PresetButton cloneset = new PresetButton(" CloneOver",PolySide.Log,SpiffyPath.Log,InstructionProcessor.Log,PolySide.Log);
		allPan.add(cloneset);
		
		
		//manual settings for everything:
		super.add(allPan);
		super.add(new Label("-----------"));	
		
		
		
		
		//fill with known loggers
		for (Logger logger : JAMcore.ALL_LOGGERS) {
			
			LogsControl control = new LogsControl(logger);
						
			super.add(control);
			controls.add(control);
		}
		
	}

	/**
	 * final ListBox filterBox = new ListBox();
			
	 * @author darkflame
	 *
	 */
	
	class LogsControl extends HorizontalPanel {
		logLevelListBox dropdown;
		public LogsControl(Logger logger) {
			//label
			Label name = new Label(logger.getName());
			super.add(name);
			//dropdown settings
			dropdown = new logLevelListBox(logger);
			super.add(dropdown);
			super.setCellHorizontalAlignment(dropdown, ALIGN_RIGHT);
			
			super.setWidth("460px");
			
			
		}

		public void setLevel(Level selectedLogLevel) {
			dropdown.setToLevel(selectedLogLevel,true);
			
			
		}
		
	}
	
	class logLevelListBox extends ListBox {
		Logger controllsLog;
		public logLevelListBox(final Logger logToSet) {
			super();
			controllsLog = logToSet;
			
			addItem(Level.ALL.getName());
			addItem(Level.FINEST.getName());
			addItem(Level.INFO.getName());
			addItem(Level.WARNING.getName());
			addItem(Level.SEVERE.getName());
			
			addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					String logstring = getSelectedItemText();
					Level selectedLogLevel = Level.parse(logstring);
					
					logToSet.setLevel(selectedLogLevel);
					
				}
			});
			
			//set to current level
			Level lev = logToSet.getLevel();
			this.setToLevel(lev,false);
			
		}
		
		
		/**
		 * 
		 * @param lev
		 * @param fireLogChange - false = just visually set dropdown true=change Logger as well
		 */
		public void setToLevel(Level lev, boolean fireLogChange ){
			int index=0;
			
			if (lev==Level.ALL){
				index=0;
			} else if (lev==Level.FINEST) {
				index=1;	
			}  else if (lev==Level.INFO) {
				index=2;
			} else if (lev==Level.WARNING) {
				index=3;
			}  else if (lev==Level.SEVERE) {
				index=4;
			} 
			
			super.setItemSelected(index, true);
			if (fireLogChange){
				controllsLog.setLevel(lev);
			}
			
		}
		
	}
	

	class logLevelChangeAll extends ListBox {

		
		public logLevelChangeAll(final ArrayList<LogsControl> controls) {
		
			addItem(Level.ALL.getName());
			addItem(Level.FINEST.getName());
			addItem(Level.INFO.getName());
			addItem(Level.WARNING.getName());
			addItem(Level.SEVERE.getName());
			
			addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					String logstring = getSelectedItemText();
					Level selectedLogLevel = Level.parse(logstring);
					
				//	for (Logger logger : aLL_LOGGERS) {
				//		logger.setLevel(selectedLogLevel);									
				//	}
					
					for (LogsControl control : controls)
					{
						control.setLevel(selectedLogLevel);
						
					}
					
				}
			});
			
		}
		
	}
	
	class PresetButton extends Button {
		private Logger[] loggersToRemainOn;

		
		/**
		 * turns all to SEVERE except those specified
		 * @param name
		 * @param loggersToRemainOn
		 */
		public PresetButton(String name,Logger... loggersToRemainOn){
			super(name);
			
			this.loggersToRemainOn=loggersToRemainOn;
		
			super.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					clicked();
				}
			});
			
		}

		public void clicked() {
			Log.info("turning all "+controls.size()+" controlls to SEVERE except "+loggersToRemainOn.length);
			
			for (LogsControl control : controls)
			{
				boolean turntoinfo = false;
				
				for (Logger logger : loggersToRemainOn) {
					
					if (logger==control.dropdown.controllsLog) {					
						turntoinfo=true;
						break;
					}					
				}
				
				if (turntoinfo){
					control.setLevel(Level.INFO);						
				} else {
					control.setLevel(Level.SEVERE);	
				}
				
			}
		}
	}
	
}
