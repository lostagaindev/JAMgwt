package com.lostagain.JamGwt.JargScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
//import com.lostagain.Jam.Scene.TextOption;
import com.lostagain.Jam.Scene.TextOptionFlowPanelCore;
import com.lostagain.Jam.SceneObjects.Interfaces.hasUserActions;

//TODO: Somehow make this a generic class with a GWT implementation
/** 
 * A text option flow panel is a flow panel filled with
 * clickable bits of text's that trigger UserActionUsed on the
 * spawning object.
 * This is mostly used for the selection of dialogue options in
 * umm...dialogue.
 * As there can be many,many options we can also divide them into tabs * 
 ***/

public class GwtTextOptionFlowPanel extends TextOptionFlowPanelCore   {

	/**
	 * GWTGlobalTextOptionCache creates a cache PER SceneObject of the related textObjects. 
	 * This allows us to remember which textoptions have been selected before.
	 * WARNING: This is a GWT SPECIFIC implementation. For other implementations, you will need to write it's own version.
	 **/
public 	static HashMap<hasUserActions, HashMap<String,TextOption>> GWTGlobalTextOptionCache  =   new HashMap<hasUserActions, HashMap<String, TextOption>>();

	static Logger Log = Logger.getLogger("JAM.TextOptions");
	TabPanel visualRepresenation = new TabPanel(); //extends TabPanel 

	GwtTextOptionFlowPanel thisPanel = this; 


	/** 
	 * Options can be put into tab panels - letting us have more space,
	 * as well as option category's
	 **/


	/** all of this TextOptionFlowPanels tab contents are stored here
	 * Basically named flow panels**/
	HashMap<String,FlowPanel> allTabPanels = new HashMap<String,FlowPanel>(); 


	/** 
	 * Create a new TextOptionFlowPanel
	 * 
	 * @param OptionList - a list of useraction options
	 * @param sourceobject - the object any selected action will be sent too
	 * @param FlowPanelName - the name of this flowpanel we are creating
	 * @param tabname - name of the tab
	 **/
	public GwtTextOptionFlowPanel(ArrayList<String> OptionList, hasUserActions sourceobject,String FlowPanelName, String tabname){

		visualRepresenation.addStyleName("questionpanel");

		//remember the source object (where the useractionused commands will be sent too)				
		toRememberSource = sourceobject;

		//add the tab panel to the div



		//create a default tab
		FlowPanel defaultPanel = createAndAddNewTab(tabname);


		//get all items
		addOptions(OptionList, sourceobject,tabname);
		globalTextOptionsList.put(FlowPanelName,this);

		visualRepresenation.selectTab(0);


	}


	/** creates a new tab in this panel **/
	private FlowPanel createAndAddNewTab(String tabname) {

		//create a new flowpanel
		FlowPanel newflowpanel = new FlowPanel();

		//add it to the tabpanel
		visualRepresenation.add(newflowpanel,tabname);

		allTabPanels.put(tabname, newflowpanel);

		//return it for convince
		return newflowpanel;

	}


	/** 
	 * Adds options to a specified tab.
	 * 
	 * @param OptionList - list of option strings to add
	 * @param sourceobject - where the useractionused events will be applied too
	 * @param tabName - name of the tab these options will be put on. 
	 **/
	public void addOptions(ArrayList<String> OptionList, hasUserActions sourceobject, String tabName) {

		if (sourceobject == null) {
			sourceobject = toRememberSource;
		}

		//if tabnam is null, we default to the default tabName 
		if (tabName == null){
			tabName = "Ask about";
		}

		//get the tab to add it too.
		FlowPanel panelToAddTo = allTabPanels.get(tabName);

		//if none found with that name,create it
		if (panelToAddTo==null){
			panelToAddTo = createAndAddNewTab(tabName);
		}
		int optionnumber = panelToAddTo.getWidgetCount();
		//add the options
		for (String optionname : OptionList) {

			optionnumber++;
			TextOption newOption;

			if (GWTGlobalTextOptionCache.containsKey(sourceobject)){
				if (GWTGlobalTextOptionCache.get(sourceobject).containsKey(optionname)){

					newOption = GWTGlobalTextOptionCache.get(sourceobject).get(optionname);


				} else {

					newOption = new TextOption(optionname,sourceobject,optionnumber );
				}}
			else {

				newOption = new TextOption(optionname,sourceobject,optionnumber );
			}

			panelToAddTo.add(newOption);
		}
	}

	/**currently does nothing, but will be fired when an option is picked.
	 * If we ever want animations for picking an action to go on its TextOptionFlowPanel
	 * it should go here **/

	public void optionPicked() {
		//exit

	}

	
	/** runs the actions on the source object when one is picked**/
	public class TextOption extends SimplePanel {

		hasUserActions sourceobject;
		String useractionname;
		public String getUseractionname() { //only for debugging
			return useractionname;
		}

		public Label getOptionsLabel() {//only for debugging
			return optionsLabel;
		}

		Label optionsLabel; 

		/** creates a new textoption, with the actionname to run and the object it will effect by clicking on it**/
		public TextOption(String actionName, hasUserActions object,int NumberOption) {

			this.setStylePrimaryName("textOptionDefaultStyle");
			sourceobject = object;
			useractionname = actionName;

			//creates a label and a click handler
			optionsLabel = new Label(NumberOption+". "+ useractionname);

			optionsLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					//trigger the action
					execute();
				}
			});

			//sets the label as this panels widget
			this.setWidget(optionsLabel);


			// We check if the textoption already exists, if so, we add a textoption to this cache.
			//When it doesn't, we first create a new hashmap to associate the Text of the Option with the new TextOption. 
			if (GWTGlobalTextOptionCache.containsKey(object)) {
				GWTGlobalTextOptionCache.get(object).put(actionName, this);
				Log.info("This textoption was already in the cache. It's a"+object.getClass());
				
				
			} else { 
				HashMap<String,TextOption> newassociatedtextoptions = new HashMap<String,TextOption>();
				newassociatedtextoptions.put(actionName, this);
				GWTGlobalTextOptionCache.put(object, newassociatedtextoptions);
				Log.info("Adding new textoption to cache. It's a"+object.getClass());
			}
			

		}

		/** if this option is selected this is run**/
		public void execute() {
			this.setStyleName("usedtextoption");
			
			//sent a optionPicked command to the panel this was on
			thisPanel.optionPicked();			

			if (sourceobject==null){ //should be impossible
				Log.severe("object was null on text option");				
			}
			//send the command picked to the object it is set to fire (this is the important bit!)
			sourceobject.userActionTriggeredOnObject(useractionname);

		}
	}

	@Override
	public Object getVisualRepresentation() {
		return visualRepresenation;
	}

	public void setSize(String width, String height) {		
		visualRepresenation.setSize(width, height);
	};










}
