/**
 * 
 */
package com.lostagain.JamGwt.GwtJamImplementations;

import java.util.Iterator;
import java.util.logging.Logger;

import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackError;
import com.darkflame.client.interfaces.SSSGenericFileManager.FileCallbackRunnable;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.GameVariableManagement;
import com.lostagain.Jam.InventoryPanelCore;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.PageLoadingData;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.SceneAndPageSet;
import com.lostagain.Jam.Interfaces.HasScreenManagement;
import com.lostagain.Jam.Interfaces.IsPopupPanel;
import com.lostagain.Jam.Interfaces.hasInventoryButtonFunctionality;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.JamGwt.GWTSceneAndPageSet;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.GwtSaveGameManager;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;

/**
 * Implementation of basic screen management systems for the JAM.
 * Basically lets it place objects on the screen directly rather then on a scene.
 * 
 * eg,
 * When the JAMCore needs to make a new inventory, it needs to place a button on the interface.
 * It will use the PositionByTag function we define here to do so.
 * 
 * For GWT all Objects will be Widgets of some sort, so its safe to cast to Widgets
 * 
 * This class also manages things like showing/hidding common gui elements like the gamecontrollpanel
 * 
 * @author darkflame
 *
 */
public class GwtScreenMangementImp implements  HasScreenManagement {

	static Logger Log = Logger.getLogger("JAM.GwtScreenMangementImp");


	@Override
	public void PositionByTag(SceneObject placeThis, String atThisTag) {
		
		SceneObjectVisual placeThisVisual = (SceneObjectVisual) placeThis;
		
		Element toaddto = DOM.getElementById(placeThisVisual.getObjectsCurrentState().attachToHTMLDiv);
		if (toaddto!=null)
			{


				//ObjectsLog.info("div current contents: "+toaddto.getInnerHTML());
				HTMLPanel container = HTMLPanel.wrap(toaddto);

				if (container.getWidgetCount()>0){
					placeThisVisual.ObjectsLog.info("elements already in container:"+container.getWidgetCount());
				}

				container.clear();
				container.add(placeThisVisual.getInternalGwtWidget());					
				placeThisVisual.setZIndex(placeThisVisual.getObjectsCurrentState().zindex);

				placeThisVisual.ObjectsLog.info("Positioned");

			} else {
				Log.severe("cant find element, so attaching at arbitary point");
				
				//Note; Hopefully a latter loaded tech box will pick it up for placement
				placeThisVisual.ObjectsLog.log("cant find html element with that id, so attaching at arbitary point","red");

				placeThisVisual.setPosition(placeThisVisual.getObjectsCurrentState().X,placeThisVisual.getObjectsCurrentState().Y,placeThisVisual.getObjectsCurrentState().Z ,false,false,true);


			}
		 
		
		
		//we used to just use;
		//RootPanel.get(atThisTag).add(placeThisVisual.getInternalGwtWidget()); 
		//but that wont give good errors like above
	}
	
	

	@Override
	public void PositionByTag(Object placeThis, String atThisTag) {
				
		RootPanel tagLoc = RootPanel.get(atThisTag);
				
				if (tagLoc!=null){				
					tagLoc.add((Widget) placeThis); 
				} else {
					Log.warning(" could not place object at tag:"+atThisTag+" as no element was found with that ID");
				}
				
		
	}
	

	@Override
	public boolean hasTag(String hasThisTag) {
		//tags in gwt are just div ids on the page
		RootPanel tagLoc = RootPanel.get(hasThisTag);
				
				if (tagLoc!=null){	
					return true;
				}
				 
		return false;
	}



	@Override
	public void PositionByCoOrdinates(Object placeThis, int x, int y, int z) {
		// No 3d support as yet for GWT
		//We thus ignore z
		//(we could always use it as a z-index value though?)
		RootPanel.get().add((Widget) placeThis, x, y); 
		
	}

	
	@Override
	public void setBackgroundImage(String imageLoc) {

		if (imageLoc.equalsIgnoreCase("none")) { 
			RootPanel.getBodyElement().getStyle().clearBackgroundImage();
			JAM.CurrentBackground = "none";

		} else {
		//	RootPanel.getBodyElement().setAttribute("background", imageLoc); 
			Log.info(" setting background image to:"+imageLoc+" ");
			
			RootPanel.getBodyElement().getStyle().setBackgroundImage("url(" + imageLoc + ")"); 

			JAM.CurrentBackground = imageLoc;

		}

	}

	@Override
	public void PopUpWasOpened(IsPopupPanel newInventoryFrame) {
		JAM.closeallwindows.setPlayForward();
	}
	
	@Override
	public void CloseAllOpenPopUps() {
		
		JAM.closeallwindows.setPlayForward();
		JAM.closeAllWindows();
		
	}

	
	
	
	@Override
	public void closeControlPanel() {
		JAM.ControllPanelShadows.CloseDefault();
	}

	@Override
	public boolean controllPanelIsOpen() {
		return JAM.ControllPanelShadows.isShowing();
	}
	
	
	@Override
	public void openControlPanel() {
	
		// always open this when a panel is opened;
		JAM.closeallwindows.setPlayForward();
		// --
	
		/*
		RootPanel.get().add(JAM.fadeback, 0, 0);
		// ControlPanel.ShowDefault();
		DOM.setStyleAttribute(fadeback.getElement(),
				"zIndex", "" + (JAM.z_depth_max + 1)); //$NON-NLS-1$ //$NON-NLS-2$
		DOM.setStyleAttribute(fadeback.getElement(),
				"z-index", "" + (JAM.z_depth_max + 1)); //$NON-NLS-1$ //$NON-NLS-2$
		z_depth_max = z_depth_max + 1;
	
		ControllPanelShadows.OpenDefault();
	
		ControllPanelShadows.getElement().getStyle()
		.setProperty("zIndex", "2000"); //$NON-NLS-1$ //$NON-NLS-2$
		ControllPanelShadows.getElement().getStyle()
		.setProperty("z-index", "2000"); //$NON-NLS-1$ //$NON-NLS-2$
	
		// make sure its on top
		ControllPanelShadows.fixedZdepth(2000);
		 */
	
		JAM.ControllPanelShadows.OpenDefault();
	
	}

	@Override
	public void setControlPanelPosition(int x, int y){
	
		
		JAM.ControllPanelShadows.setPopupPosition(x,y);

		JAM.ControllPanelShadows.centered = false;

	}

	@Override
	public void toggleLoadGamePopUp() {
		if (!JAM.loadGamePopup.isShowing()){

			
			JAM.loadGamePopup.OpenDefault();

		} else {
			JAM.loadGamePopup.CloseDefault();

		}
	}

	/**another huge mess, blah
	//we will need at some point to divide this into visual/non-visual elements
	//so that non-gwt games can also load pages correctly**/
	//TODO: major tidying up, refractoring etc
	//Separate out visual non-visual
	//let parent class do non-visual stuff
	//maybe even not put this in screen management? Doesn't exactly fit
	public void setNewPage(final PageLoadingData pageLoadData) {
	
		final String NewMessageURL = pageLoadData.getPageURL();
	
		final GWTSceneAndPageSet useThisLocation = (GWTSceneAndPageSet) pageLoadData.getChapterToPlaceItOn(); //we know its a GWTSceneAndPageSet
	
		JAMcore.storyPageLoading = true;
	
		JAMcore.GameLogger.log("____________________________ Loading Page: "	+ NewMessageURL);
	
		if (useThisLocation == null) {
			JAMcore.GameLogger.log("__________________________ location is null?!","yellow");
		}
	
		JAMcore.GameLogger.log("__________________________ location has "	+ useThisLocation.getPageCount() + " widgets");
	
		// NOTE: The following has been changed from "CurrentLocationTabs" to
		// "useThisLocation"
		// ---
		// First we check if its on the page list already.
		// ==
	
		int TabCount = useThisLocation.getPageCount();
		int cindex = 0;
		JAMcore.GameLogger.log(" index" + cindex + " pagesInThisChapter" + TabCount); //$NON-NLS-1$ //$NON-NLS-2$
	
		// first we test if its a page that needs updateing
		boolean dynamicPage = JAM.dynamicPages.hasItem(NewMessageURL);
		
		// its also counts as dynamic if its a php page (dynamic pages should always refresh and not cache)
		if ((!dynamicPage) && (!NewMessageURL.endsWith(".php"))) {
	
			// test if the page is already open, but only if this is loading for
			// the currently active chapter
			if (useThisLocation == JAMcore.CurrentScenesAndPages) {
				while (cindex < TabCount) {
					if (JAMcore.currentOpenPages.get(cindex).indexOf(NewMessageURL) > -1) {
	
						JAMcore.GameLogger.log("match. page Already Open."); //$NON-NLS-1$
	
						// we set it to front and return
						useThisLocation.visualContents.selectTab(cindex);
						JAMcore.storyPageLoading = false;
	
						JAMcore.GameLogger.log("removing from loading queue 1");
						PageLoadingData.PageLoadingQueue.remove(0);
						JAMcore.CheckLoadingQueue();
						return;
					}
					cindex++;
				}
			}
		} else {
	
			JAMcore.GameLogger.log("dynamic page detected so we dont just flick it to the front;");
			// we do, however, remove its existing copy
			// CurrentLocationTabs.removePage(NewMessageURL);
			// openPages.remove(index);
	
			JAMcore.CurrentScenesAndPages.removePageFromSet(NewMessageURL); //used to use  JAM.removePage(NewMessageURL);  ?
			// and its commands
			JAMcore.GameLogger.log("we erase and reload completely; Removing current page commands"
					+ NewMessageURL.split("\\.")[0]);
			SceneAndPageSet.PageCommandStore.RemoveItem(NewMessageURL.split("\\.")[0]);
	
		}
	
		// if it dosnt;
		JAMcore.GameLogger.log("setting page " + NewMessageURL); //$NON-NLS-1$
		useThisLocation.OpenPagesInSet.add(NewMessageURL.replace(".html", "")); //$NON-NLS-1$ //$NON-NLS-2$
	
		// place the current location in a temp
		final SceneAndPageSet TempCurrentLocationTabs = useThisLocation;
	
		// disable answer box
		if (JAMcore.AnswerBox.isPresent()){
			JAMcore.AnswerBox.get().setEnabled(false);
		}
		
		// turned off while offline
	
		// make builder, add variables if its a php page
	
		// update pending list
		JAMcore.NumberOfHTMLsLeftToLoad++;
	
		// In future this should only be done for NON dynamic pages, dynamic
		// ones are already loaded
		// if they are on the dynamicPages list, and the assarray already
		// contains the unprocessed data
		// ready for refreshing
	
		if (dynamicPage) {
	
			JAMcore.GameLogger.log("getting dynamic page from storage ("
					+ NewMessageURL.trim() + ")");
	
			String responseString = JAM.dynamicPages.GetItem(NewMessageURL.trim());
	
			JAMcore.GameLogger.log("message found:" + responseString);
	
			JAM.createAndAddNewPage(NewMessageURL, TempCurrentLocationTabs,
					responseString);
	
		} else {
	
			// prepare the url data
			String URLDATA = "Game Message Text/" + NewMessageURL.trim();
			// if being sent to a php file, we add all the variables
	
			boolean POSTneeded = false;
	
			if (NewMessageURL.endsWith(".php")) {
	
				JAMcore.GameLogger.log("php request detected");
	
				// make variable array
				String urlvars = GameVariableManagement.GameVariables.serialise();
	
				URLDATA = URLDATA + "&" + urlvars;
				JAMcore.GameLogger.log("loading:" + URLDATA);
	
				POSTneeded = true;
			}
	
			// RequestBuilder requestBuilder = new
			// RequestBuilder(RequestBuilder.POST,
			// textfetcher_url);
	
			// requestBuilder.sendRequest("", new RequestCallback() {
			//requestBuilder.sendRequest(URLDATA, new RequestCallback() { //$NON-NLS-1$
	
			//set what to do when we get the text data retrieved
	
			FileCallbackRunnable onResponse = new FileCallbackRunnable(){
				@Override
				public void run(String responseData, int responseCode) {
	
					// instertItemsIntoText
	
					String responseString = responseData;
	
					// Window.alert("response string = "+responseString);
	
					// if the page is dynamic, detect and add it
					JAMcore.GameLogger.log("testing for dynamic page added");
					if (responseString.contains("<V:")) {
						JAMcore.GameLogger.log("dynamic page detected");
						JAM.dynamicPages.AddItemUniquely(responseString,
								NewMessageURL.trim());
						JAMcore.GameLogger.log("following dynamic page added:"
								+ JAM.dynamicPages.GetItem(NewMessageURL.trim()));
	
					}
					JAMcore.GameLogger.log("creating new page","blue");
	
					JAM.createAndAddNewPage(NewMessageURL, TempCurrentLocationTabs,
							responseString);
				}
	
			};
	
	
			//what to do if theres an error
			FileCallbackError onError = new FileCallbackError(){
	
				@Override
				public void run(String errorData, Throwable exception) {
					System.out.println("http failed"); //$NON-NLS-1$
					
					if (JAMcore.AnswerBox.isPresent()){
						JAMcore.AnswerBox.get().setEnabled(true);	
						JAMcore.AnswerBox.get().setText(""); 
					}
					
					JAMcore.storyPageLoading = false;
					JAMcore.GameLogger.log("removing from loading queue 1");
					PageLoadingData.PageLoadingQueue.remove(0);
	
				}
	
			};
	
	
			RequiredImplementations.getFileManager().getText(URLDATA,true,
					onResponse,
					onError,
					POSTneeded);
	
	
	
		}
	}

	@Override
	public void openSavePanel() {
		
	//	SaveGameManager.display(); 
		RequiredImplementations.saveManager.get().display();
		
	}



	@Override
	public String getBackgroundImage() {
		
		return  RootPanel.getBodyElement().getStyle().getBackgroundImage();// .getAttribute("background");
	}



}