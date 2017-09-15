package com.lostagain.JamGwt;

import java.util.Iterator;
import java.util.logging.Logger;

import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackError;
import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackRunnable;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.GamesInterfaceTextCore;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.SceneAndPageSet;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.JamGwt.JargScene.SceneWidgetVisual;

/** A tabpanel, designed for all the pages at the current location.
 * 
 *  "Pages" refers to either a scenepage or a htmlpage, which a game can contain any mix of. Both are stored in 
 *  a tabpanel, which may or may not be directly controllable by the user.
 * 
 * 
 * It should allow fast switching between them, so everything in the set should be kept loaded unless removePage is used
 * 
 * In the cuypers code, this set was pages in the current chapter.
 *  
 *  **/
public class GWTSceneAndPageSet  extends SceneAndPageSet implements IsWidget   {	
	public static Logger Log = Logger.getLogger("JAM.GWTSceneAndPageSet");

	/**
	 * the actual gwt widget which contains all the scenes and pages
	 */
	public DecoratedTabPanel visualContents = new DecoratedTabPanel();


	boolean tabsFunctionDisabled = false;


	final public SelectionHandler<Integer> OurTabSelectionHandeler = new TabSelectionHandeler();

	HandlerRegistration selectionHandeler;

	/** 
	 * A tabpanel, designed for all the pages at the current location.
	 * 
	 * In the cuypers code, this was pages in the current chapter.
	 * 
	 *  This extends SceneandPageSet, which controlls merely the visibility of pages in a set, as well as the sets contents.
	 *  This extended class though, lets it be handled by a tabpanel, which is nice for html based games as the user
	 *  can quickly switchbetween pages in a chapter
	 * **/
	public GWTSceneAndPageSet(String name) {	
		super(name);
		
		visualContents.setStylePrimaryName("standard_message_back");

		//add auto scroll;		
		visualContents.setStylePrimaryName("overflowscroll");		
		visualContents.setStylePrimaryName("standard_message");

		if (JAMcore.Client.indexOf("firefox")>-1){
			visualContents.getDeckPanel().setAnimationEnabled(false);
		} else {
			visualContents.getDeckPanel().setAnimationEnabled(false);
		}


		//this.addTabListener(OurTabListener);

		selectionHandeler = visualContents.addSelectionHandler(OurTabSelectionHandeler);


		visualContents.setSize("100%", "100%");


		//this.getDeckPanel().setSize("97%", "97%");
		visualContents.getDeckPanel().setSize("100%", "100%");

	}
	public void disableAllTabFunctions() {
		tabsFunctionDisabled = true;
	}
	public void removeHandlers(){
		selectionHandeler.removeHandler();
	}


	public final class TabSelectionHandeler implements SelectionHandler<Integer> {	

		public void onSelection(SelectionEvent<Integer> event) {			

			Log.info("tab selected");

			int tabIndex =  event.getSelectedItem();

			//clear the token
			// Log.info("clearing history token");
			History.newItem("-", false);

			// Let the user know what they just did.				
			if (tabsFunctionDisabled==false && disableTabEventsForStupidReasons==false)
			{				
				onPageSelected(tabIndex);				
			} 
		}		
	}


	/*
	public final class SetActiveAnswersFromTab implements TabListener {
		public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
		        // Let the user know what they just did.	

			  if (tagsFunctionDisabled==false)
			  {
				  MyApplication.chapter = MyApplication.locationpuzzlesActive.get(tabIndex);
			   //set location
			   Window.setTitle(MyApplication.Username+ LoadGamesText.MainGame_is_on_chapter + MyApplication.chapter);
			  // Feedback.setText("You goto the " + chapter+".");


			  } 
		  }

		public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		        // Let the user know what they just did.				        
		        return true;
		  }			
	}
	 */
	@Override	
	protected void removePageFromSetPhysically(int pageNum,String name) {
		//	super.removePageFromSet(RemoveLocation);

		this.visualContents.remove(pageNum);

		/*
		int TabCount = this.getPageCount();  //visualContents.getWidgetCount();
		int cindex =0;
		Log.info("remove-"+RemoveLocation);

		//remove tab
		 while(cindex<TabCount)
	        {
	        	if (JAMcore.currentOpenPages.get(cindex).indexOf(RemoveLocation)>-1) {	

	        		//we remove it from the tabs
	        		this.visualContents.remove(cindex);

	        		JAMcore.currentOpenPages.remove(cindex);
		    		// set chapter name to match
	        		JAMcore.locationpuzzlesActive.remove(cindex);


	        		return;
	        	}
	        	cindex++;
	        }
		 */
	}


	//override this purely to give a window title (no longer needed as a interfacwe was made in the core for it)
	@Override
	public void onPageSelected(int tabIndex) {
		super.onPageSelected(tabIndex);
		//set location title
	//	Window.setTitle(" - " + JAMcore.Username + GamesInterfaceTextCore.MainGame_is_on_chapter + JAMcore.usersCurrentLocation+" - ");
	

	}
	
	/**
	 * used as a really stupid work around.
	 * We want to run SelectTab() but not fire the events in OnSelect
	 * However the function that should do this blah.selecttab(0,false)
	 */
	boolean disableTabEventsForStupidReasons = false;
	/**
	 * Selects a page from this set.
	 * This means making it visible / to front
	 */
	public void selectPage_implementation(int PageNum,String name) {
		
		disableTabEventsForStupidReasons = true;
		visualContents.selectTab(PageNum,true); //unfortunately we cant set this to false as selectTab break completely (gwt bug)
		disableTabEventsForStupidReasons = false; //crude replacement to avoid bug
		
		//this.onPageSelected(PageNum);
		Log.info("selectPage implementation-"+name+","+PageNum);
		int sel = visualContents.getTabBar().getSelectedTab();
		Log.info("selectPage implementation-"+sel);

		
	}

	/**
	 * Selects a page from this set.
	 * This means making it visible / to front
	 *//*
	@Override
	public void selectPage(final String name) {
		super.selectPage(name);

		int PageNum = super.getPageNumber(name);

		if (PageNum!=-1){
			visualContents.selectTab(PageNum); 
		}



		Log.info("searching for:"+name);

		int TabCount = visualContents.getWidgetCount();

		Log.info("out of:"+TabCount);

		int cindex =0;

		//remove tab
		 while(cindex<TabCount)
	        {

			 Log.info("testing:"+JAMcore.currentOpenPages.get(cindex));

	        	if (JAMcore.currentOpenPages.get(cindex).equalsIgnoreCase(name)) {

	        		Log.info("selecting:"+cindex);
	        		visualContents.selectTab(cindex); //select that requested

	        		return;
	        	}
	        	cindex++;
	        }

	}*/

	/**
	 * Adds a newly created scene to this set<br>
	 * <br>
	 * location is the location associated with the scene (the player should will automatically go to this when its to front)<br>
	 * "make active" makes it the current active scene<br>
	 * and silent mode determines if OnSceneDebut SceneToFront and OnSceneLoad commands should be suppressed. <br>
	 * This is mostly for loading from a save state, where none of that stuff should be run. <br>
	 ***/
	@Override
	public void addNewSceneToSetPhysically(final SceneWidget scene, String name){


		SceneWidgetVisual sceneWidget =(SceneWidgetVisual) scene;


		//scene container (is this really needed? why not add sceneWidget.sceneBackground directly?)
		SimplePanel container = new SimplePanel(sceneWidget.dragPanelCore); 
		container.getElement().getStyle().setBackgroundColor("#000");		
		container.setSize("100%", "100%");		

		//TODO: can we do without the container ?	
		//attach to storyboxs in new tab at 100% 100% size		
		//	((GWTSceneAndPageSet)JAMcore.CurrentScenesAndPages).visualContents.add(container, name);		

		//	this.visualContents.add(container, name);		
		this.visualContents.add(sceneWidget.dragPanelCore, name);


		//we dont autoselect the tab anymore

		//MyApplication.CurrentLocationTabs.selectTab(MyApplication.CurrentLocationTabs.getWidgetIndex(container));
		//		if (makeactive){
		//			Log.info(" tab selected ");
		//			//InstructionProcessor.bringSceneToFront(scene);					
		//			SceneWidget.setActiveScene(scene,silentmode);
		//		}

	}

	//TODO: Refractor some of this into the core once answerbox can be refereed to there	
	//To do this;
	//b) make new function for the actual "attach to the webpage" stuff
	@Override
	public void addNewHTMLPagePhysical(final String NewMessageURL) {
		//	super.addNewHTMLPage(NewMessageURL);

		/*
		// First we check if its on the page list already.
		// ==				
		int TabCount = this.visualContents.getWidgetCount();
		int cindex =0;

		//check if its already added, if so select it
	        while(cindex<TabCount)
	        {
	        	if (JAMcore.currentOpenPages.get(cindex).indexOf(NewMessageURL)>-1) {
	        		System.out.println("/n match. Already Open.");
	        		//we set it to front and return
	        		this.visualContents.selectTab(cindex);


	        		return;
	        	}


	        	cindex++; 
	        }

		 */

		//place the current location in a temp
		final GWTSceneAndPageSet TempCurrentLocationTabs = this;

		//disable answer box
		if (JAMcore.AnswerBox.isPresent()){
			JAMcore.AnswerBox.get().setEnabled(false);
		}


		FileCallbackRunnable onSuccess = new FileCallbackRunnable(){

			@Override
			public void run(String responseData, int responseCode) {
				HTML StoryText = new HTML(responseData);
				StoryText.setHTML("t");
				StoryText.setStylePrimaryName("overflowscroll");

				//System.out.println(StoryText);
				//StoryText.setSize("100%", "100%");

				Label Tab = new Label();
				Tab.setText(NewMessageURL.substring(0, NewMessageURL.length()-5));

				// if current location is the active one, then we add to the lists;

				if (TempCurrentLocationTabs.equals(this)) {
					// add opened chapter name to list
					JAMcore.currentOpenPages.add(NewMessageURL);
					// set chapter name to match
					JAMcore.locationpuzzlesActive.add(JAMcore.usersCurrentLocation);
					if (JAMcore.AnswerBox.isPresent()){
						JAMcore.AnswerBox.get().setEnabled(true);
						JAMcore.AnswerBox.get().setFocus(true);
						JAMcore.AnswerBox.get().setText("");
					}
				}

				TempCurrentLocationTabs.visualContents.add(StoryText,(NewMessageURL.substring(0, NewMessageURL.length()-5)));		
				int widgetIndex = TempCurrentLocationTabs.visualContents.getWidgetIndex(StoryText);
				
			//	TempCurrentLocationTabs.selectPage(widgetIndex);
				TempCurrentLocationTabs.visualContents.selectTab(widgetIndex,false);
				TempCurrentLocationTabs.onPageSelected(widgetIndex);
				
			}

		};

		FileCallbackError onError = new FileCallbackError(){

			@Override
			public void run(String errorData, Throwable exception) {

				Log.info("http retrieve failed");
				if (JAMcore.AnswerBox.isPresent()){
					JAMcore.AnswerBox.get().setEnabled(true);	    	    
					JAMcore.AnswerBox.get().setText("");
				}
			}

		};


		RequiredImplementations.getFileManager().getText("Game Message Text/" + NewMessageURL.trim(),true,onSuccess,onError , false);
	}
	
	
	@Override
	public Widget asWidget() {
		return visualContents;
	}


	@Override
	public void setstoryboxbackgroundclass(String CurrentProperty) {
		// Element bottom panel =
		// (Element)MyApplication.StoryTabs.getDeckPanel().getElement().getFirstChild().getFirstChild().getFirstChild().getChildNodes().getItem(1);
		Log.info("setting storybox background style to "
				+ CurrentProperty);
		// test bits
		// CurrentLocationTabs.getElement().setClassName("_level1_test");
		// CurrentLocationTabs.getElement().getFirstChildElement().setClassName("_level2_test");
		Element bottompanel = (Element) (((GWTSceneAndPageSet)JAMcore.CurrentScenesAndPages).visualContents
				.getElement().getFirstChildElement().getChildNodes()
				.getItem(1));

		if (bottompanel == null) {
			Log.info("null element, cant set style");
		}

		if (CurrentProperty.compareTo("none") == 0) { //$NON-NLS-1$
			// MyApplication.StoryTabs.getDeckPanel().setStyleName("");
			bottompanel.setClassName("_none_");

		} else {
			Log.info("setting deck style.");
			// MyApplication.StoryTabs.getDeckPanel().setStyleName("blah1"+CurrentProperty);

			// Log.info("nodes="+bottompanel.getInnerHTML());
			bottompanel.setClassName(CurrentProperty);

		}
	}
	@Override
	public void clearVisuals() {
		visualContents.clear();
	}

}
