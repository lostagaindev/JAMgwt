package com.lostagain.JamGwt.JargScene;

import java.util.logging.Logger;

import org.eclipse.jetty.util.log.Log;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.lostagain.Jam.Factorys.JamMenuBar;
import com.lostagain.Jam.Scene.SceneMenuWithPopUp;
import com.lostagain.Jam.SceneObjects.Interfaces.hasUserActions;

//
//we simply use a version of menu bar for now //extends MenuBar
public class GwtMenuBar extends JamMenuBar  {

	static Logger Log = Logger.getLogger("JAM.GwtMenuBar");
	
	MenuBar visualRepresentation = new MenuBar(true);
	
	
	public GwtMenuBar(SceneMenuWithPopUp parentbox ) {
		super( parentbox);
		visualRepresentation.addStyleName("unselectable");
		
	}
	@Override
	public Object getVisualRepresentation() {
		return visualRepresentation; //important bit so our popup can take it
	}
	
	/** 
	 * @param name
	 * @param sourceObject - 
	 **/
	public void addMenuItem_impl(String name, final runActions runOnMenuClick) {
		
		MenuItem menuItem = new MenuItem(name, true, new Command(){
			@Override
			public void execute() {
				runOnMenuClick.execute();
			}			
		});
		

		menuItem.addStyleName("unselectable");
		menuItem.addStyleName("popup-item");
		
		visualRepresentation.addItem(menuItem);
		
	}
	
	public void setVisible(boolean b) {
		visualRepresentation.setVisible(b);
	}
	
	


}