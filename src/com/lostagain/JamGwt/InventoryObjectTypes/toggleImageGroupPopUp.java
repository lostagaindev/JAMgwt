package com.lostagain.JamGwt.InventoryObjectTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import com.google.common.primitives.Ints;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.Jam.InventoryItems.TigItemCore;
import com.lostagain.Jam.SceneObjects.SceneObjectState;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.TypedLabel;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;


import lostagain.nl.spiffyresources.client.spiffycore.SpiffyFunctions;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyArrow;

import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyArrow;


//TODO: Move as much as possible into TigItemCore 
//This probably means making TigItem not be a composite, but rather contain a abstract or interface
//for its implementation
public class toggleImageGroupPopUp extends TigItemCore implements
hasCloseDefault, hasOpenDefault, isInventoryItemImplementation {

	static Logger Log = Logger.getLogger("Jam.toggleImageGroupPopUp");
	VerticalPanel panelcontents = new VerticalPanel();
	
    boolean setup=false;
	String stateToLoad = "";
	
	// screen size
	int ScreenSizeX = Window.getClientWidth();
	int ScreenSizeY = Window.getClientHeight();
	int Original_picY = 0;
	int Original_picX = 0;

	// pic size
	int picX = 0;
	int picY = 0;
	int posX = 0;
	int posY = 0;

	// feedback
	public final TypedLabel TiGFeedback = new TypedLabel(""); //$NON-NLS-1$

	// solution flag
	Boolean solutionpresent = true;

	// Tig message present flag
	Boolean messageboxpresent = false;

	String solution_script = "";
	// Description
	HTML ImageDiscription = new HTML("");

	// button
	final Button submitans = new Button("~ Check ~");

	final Timer timer;
	
	// image
	Image Background = new Image("");
	public AbsolutePanel imagegroupframe = new AbsolutePanel();

	toggleImageGroupPopUp thisframe = this;

	// timer for repeating actions
	Timer AnimateRepeat; // used to do a bounce-loop for now;

	// Item array

	final ArrayList<TigItem> TIGitems = new ArrayList<TigItem>();

	public boolean Magnifable = false;

	String sourceurl = "";
	String sourcesizeX = "";
	String sourcesizeY = "";

	public toggleImageGroupPopUp(final String ItemName,final String nDiscription, String sizeX, String sizeY,boolean PngMode) {
		super(ItemName);
		
		panelcontents.setStylePrimaryName("unselectable");


		sourcesizeX = sizeX;
		sourcesizeY = sizeY;

		// make sure this is clear;
		TIGitems.clear();

		// First we load the backdrop
		String BackURL;
		if (PngMode) {
			BackURL = "InventoryItems/" + ItemName + "/" + ItemName + ".png";
		} else {
			BackURL = "InventoryItems/" + ItemName + "/" + ItemName + ".jpg";
		}

		sourceurl = BackURL;

		Background.setUrl(BackURL);

		panelcontents.setBorderWidth(0);
		panelcontents.setSpacing(0);		
		panelcontents.add(imagegroupframe);

		imagegroupframe.add(Background, 0, 0);
		panelcontents.add(TiGFeedback.getInternalWidget());

		TiGFeedback.getInternalWidget().setWidth("100%"); //$NON-NLS-1$	
		TiGFeedback.getInternalWidget().setStyleName("Feedback");
		TiGFeedback.getInternalWidget().setHeight("50px");
		TiGFeedback.getInternalWidget().setVisible(false);

		// Description
		JAMcore.GameLogger.info("tig loading");

		// MyApplication.DebugWindow.info("loading image.."+BackURL);

		timer = new Timer() {
			@Override
			public void run() {
				// We check the size here, and fix if not correct. (for IE, as
				// IE dosnt work with LoadListener due to the cache)

				if (Background.getOffsetWidth() > 35) {
					Original_picY = Background.getHeight();
					Original_picX = Background.getWidth();

					imagegroupframe.setSize(Original_picX + "px", Original_picY
							+ "px");

					if (solutionpresent == true) {
						imagegroupframe.add(submitans,
								(imagegroupframe.getOffsetWidth() / 2) - 50,
								imagegroupframe.getOffsetHeight() - 30);
					}

					if (messageboxpresent) {
						TiGFeedback.getInternalWidget().setVisible(true);
					}

					JAMcore.GameLogger.info("image load b"
							+ Original_picY + " " + Original_picX);

					// add descriptions
					if (nDiscription != "") {

						JAMcore.GameLogger.info("discription set"
								+ nDiscription);

						ImageDiscription.setText(nDiscription);
						ImageDiscription.setWidth("100%");

						ImageDiscription.setStylePrimaryName("unselectable");
						// imagegroupframe.add(ImageDiscription,5,Original_picY);
						// imagegroupframe.setSize(Original_picX+"px",
						// Original_picY+30+"px");

						thisframe.panelcontents.insert(ImageDiscription, 0);
						ImageDiscription.setStyleName("notepadback");
						ImageDiscription.getElement().getParentElement().setClassName("picturePopUp");

					}

					reSizePopUp();
					this.cancel();
				}

			}

		};
		Background.setStylePrimaryName("unselectable");
		Background.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				System.out.print("\n /n <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<image failed "
						+ Original_picX + "-" + Original_picY);


			}
		});

		Background.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {

				Widget sender = (Widget) event.getSource();

				Original_picY = ((Image) sender).getHeight();
				Original_picX = ((Image) sender).getWidth();

				// Original_picX = sourcesizeX;
				// Original_picY = sourcesizeY;

				// imagegroupframe.setSize(Original_picX+"px",
				// Original_picY+"px");
				imagegroupframe.setSize(sourcesizeX, sourcesizeY);

				// we finaly add the tester button
				if (solutionpresent == true) {
					imagegroupframe.add(submitans,
							(imagegroupframe.getOffsetWidth() / 2) - 50,
							imagegroupframe.getOffsetHeight() - 30);
				}
				if (messageboxpresent) {
					TiGFeedback.getInternalWidget().setVisible(true);
				}

				JAMcore.GameLogger.info("image load a");

				// add descriptions
				if (nDiscription != "") {

					JAMcore.GameLogger.info("discription set"+ nDiscription);

					ImageDiscription.setText(nDiscription);
					ImageDiscription.setWidth("100%");

					// imagegroupframe.add(ImageDiscription,5,Original_picY);
					// imagegroupframe.setSize(Original_picX+"px",					
					// Original_picY+30+"px");
					thisframe.panelcontents.insert(ImageDiscription, 0);

					Log.info("setting class on parent");	
					ImageDiscription.getElement().getParentElement().setClassName("picturePopUp");
					ImageDiscription.setStyleName("notepadback");

				}

				reSizePopUp();
				timer.cancel();

			}


		});
		// -----------------------

		timer.scheduleRepeating(1000);

		// Then we get the TIS's item ini
		String ControllIni = GWT.getHostPageBaseURL() + "InventoryItems/"
				+ ItemName + "/" + ItemName + "_items.ini";

		RequestBuilder requestBuilder2 = new RequestBuilder(RequestBuilder.GET,
				ControllIni);
		try {
			requestBuilder2.sendRequest("", new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					System.out.println("error in user logged in test");
				}

				public void onResponseReceived(Request request,
						Response response) {

					System.out.println("\n\n" + response.getText());

					String item_list = response.getText();

					//parse for language specific extensions
					//Anything with __LAN will be replaced with the current language extension
					item_list = JAMcore.parseForLanguageSpecificExtension(item_list);
					//also text IDs for CSV file based text
					item_list = JAMcore.parseForTextIDs(item_list);

				//	JAM.DebugWindow.info("Item Data:" + item_list);
					Log.info("Item Data:" + item_list);
					
					// check for no items
					if (!item_list.contains("-Item:")) {
						Log.info("NO ITEMS IN TIG FILE FOUND");
						JAMcore.itemsLeftToLoad--;
						Log.info("reduceing items left to load to:"	+ JAMcore.itemsLeftToLoad);
						JAM.testForInstructionsToRun();
					}

					// then we loop over adding the items

					int starthere = 0;
					int loc = item_list.indexOf("-Item:", starthere) + 6;

					String itemset = "";
					if ((item_list.indexOf("-Item:", loc + 6)) > -1) {
						itemset = item_list.substring(loc,
								item_list.indexOf("-Item:", loc));
					} else {
						itemset = item_list.substring(loc, item_list.length());
					}
					System.out.println("set:" + itemset);

					while (loc > -1) {

						System.out.println("\n\n adding next item to TIG");

						// get type (if any set, image by default, else its a label)
						String type = "";
						if (itemset.contains("Type =")){					
						 type = itemset.substring(
								itemset.indexOf("Type =", 0) + 7,
								itemset.indexOf("\n",
										itemset.indexOf("Type =", 0) + 7)).trim();
						}
						
						//default text is, surprisingly only relevant to text objects
						String DefaultText = "";
						if (itemset.contains("DefaultText")){
						 DefaultText = itemset.substring(
								itemset.indexOf("DefaultText =", 0) + 14,
								itemset.indexOf("\n",
										itemset.indexOf("DefaultText =", 0) + 14)).trim();
						}
						
						// get the pictures filename
						String name = itemset.substring(
								itemset.indexOf("Name =", 0) + 7,
								itemset.indexOf("\n",
										itemset.indexOf("Name =", 0) + 7));

						//cssclasss
						String cssclasss = "";
						if (itemset.contains("CSSname")){
								cssclasss = itemset.substring(
								itemset.indexOf("CSSname =", 0) + 9,
								itemset.indexOf("\n", itemset.indexOf("CSSname =", 0) + 9));
						}
						
						
						// get the pictures title
						String title = "";
						if (itemset.contains("Title =")){
						 title = itemset.substring(
								itemset.indexOf("Title =", 0) + 9,
								itemset.indexOf("'",
										itemset.indexOf("Title =", 0) + 9));
						}
						
						// get the id name, if there is one
						String IconIDName = "";
						if (itemset.contains("IconID")){
							IconIDName = itemset.substring(
									itemset.indexOf("IconID =", 0) + 8,
									itemset.indexOf("\n",itemset.indexOf("IconID =", 0) + 8));
						}

						// get the states number
						int States = Integer.parseInt((itemset.substring(
								itemset.indexOf("States =", 0) + 8,
								itemset.indexOf("\n",
										itemset.indexOf("States =", 0) + 8)))
										.trim());
						
						// get the location to put it
						int ItemLocX = Integer.parseInt((itemset.substring(
								itemset.indexOf("Located =", 0) + 9,
								itemset.indexOf(",",
										itemset.indexOf("Located =", 0) + 9)))
										.trim());

						int ItemLocY = Integer.parseInt((itemset.substring(
								itemset.indexOf(",",
										itemset.indexOf("Located =", 0) + 9) + 1,
										itemset.indexOf("\n",
												itemset.indexOf("Located =", 0) + 9)))
												.trim());

						//if its hidden by default
						String Visibility = "";
						if (itemset.contains("Visibility")){
						 Visibility = itemset.substring(
								itemset.indexOf("Visibility =", 0) + 12,
								itemset.indexOf("\n",
										itemset.indexOf("Visibility =", 0) + 12)).trim();
						}
						

						// NOTE: Currently it can only have actions or rollover
						// actions, not both <<outdated?
						final boolean isActions = itemset.contains("Actions:");

						String ActionsString = "";
						if (isActions){
							ActionsString = itemset.substring(
								itemset.indexOf("Actions:", 0) + 8,
								((itemset.indexOf("Mouse", 0)) > 0 ? (itemset
										.indexOf("Mouse", 0)) : (itemset
												.length())));
						}
						final String Actions = ActionsString;
						
						final boolean isMouseoverActions = itemset
								.contains("MouseoverActions:");
						
						String mouseoverActionsString = "";
						if (isMouseoverActions){							
							mouseoverActionsString = itemset.substring(
								itemset.indexOf("MouseoverActions:", 0) + 17,
								((itemset.indexOf(
										"Mouse",
										itemset.indexOf("MouseoverActions:", 0) + 17)) > 0 ? (itemset
												.indexOf("Mouse", itemset.indexOf(
														"MouseoverActions:", 0) + 17))
														: (itemset.length())));
						}
						final String mouseoverActions = mouseoverActionsString;
						
						final boolean isMouseoutActions = itemset
								.contains("MouseoutActions:");

						String mouseoutActionsString = "";
						if (isMouseoutActions){
						 mouseoutActionsString = itemset.substring(
								itemset.indexOf("MouseoutActions:", 0) + 16,
								((itemset.indexOf(
										"Mouse",
										itemset.indexOf("MouseoutActions:", 0) + 16)) > 0 ? (itemset
												.indexOf("Mouse", itemset.indexOf(
														"MouseoutActions:", 0) + 16))
														: (itemset.length())));
						}
						final String mouseoutActions = mouseoutActionsString;
						// MyApplication.DebugWindow.setText("Icon:"+IconIDName+" Adding actions: "+IconActions+" :");

						// =========
						
						Log.info(" cssclasss="+cssclasss);
						Log.info(" type=" + type);
						Log.info(" defaulttext=" + DefaultText);
						Log.info(" name=" + name);
						Log.info(" title=" + title);
						Log.info(" States=" + States);
						Log.info(" ItemLocX=" + ItemLocX);
						Log.info(" ItemLocY=" + ItemLocY);
						Log.info(" Visibility=" + Visibility);
						 
						Log.info(" RunWhenClicked=" + Actions);

						starthere = starthere + itemset.length() + 6;
						loc = item_list.indexOf("-Item:", starthere);
						// new item set
						if ((item_list.indexOf("-Item:", loc + 6)) > -1) {
							itemset = item_list.substring(loc + 6,
									item_list.indexOf("-Item:", loc + 6));
						} else {
							itemset = item_list.substring(loc + 6,
									item_list.length());
						}

						//JAM.DebugWindow.info(" start here ="		+ starthere + ";");
						Log.info(" loc =" + loc + ";");
						Log.info(" itemset =" + itemset + ";--------------------\n ");

						//the path to the icon object
						//if the name has a path we use it
						//else we assume its a normal item in the inventory directory
						String path ="";
						if (name.contains("/")){
							path = name;				

						} else {
							path ="InventoryItems/" + ItemName + "/items/" + name;
						}

						// Now we create a new icon object if we are making a sprite
						TigItem tempItem = null;	
						if (type.equalsIgnoreCase("text")) {
							//process the text for languges
							DefaultText = JAMcore.parseForLanguageSpecificExtension(DefaultText);
							
							//make a label
							InlineHTML  newLabel = new InlineHTML (DefaultText);
							
							if (!cssclasss.isEmpty()){
								newLabel.setStylePrimaryName(cssclasss);
							}

							tempItem = new TigItem( newLabel );							
						} else {
							tempItem = new TigItem( new GWTAnimatedIcon(path,States) );
						}
						final TigItem newItem = tempItem;


						newItem.setTitle(title);
						newItem.uniqueName = IconIDName.trim();
						JAMcore.GameLogger.info(newItem.uniqueName
								+ " set:");

						// and add it where specified
						imagegroupframe.add(newItem, ItemLocX, ItemLocY);

						//if its invisible by default set visibility to false
						if (Visibility.equalsIgnoreCase("false")){
							newItem.setVisible(false);

						}

						// add to item set
						TIGitems.add(newItem); //all items get added to the list in the order they are in the file
						
						// Add the interaction.
						// Note; In future it would be nice to add a required
						// section here too,
						// allowing specific requirements to be forfilled before
						// an action is accepted

						// if it has mouseovers, add them
						if (isMouseoverActions) {
							newItem.addMouseOverHandler(new MouseOverHandler() {

								public void onMouseOver(MouseOverEvent event) {
									if (disabled){
										return;
									}

									Widget sender = newItem;//(Widget) event.getSource();
									// run actions
									// update last clicked variables so that
									// popups can be positioned correctly if
									// used
									JAM.lastclicked_x = imagegroupframe
											.getWidgetLeft(sender)
											+ (sender.getOffsetWidth() / 2);
									JAM.lastclicked_y = imagegroupframe
											.getWidgetTop(sender)
											+ (sender.getOffsetHeight() / 2);
									TigItemCore.lastclickedTig = thisframe;

									// process the instructions
									JAMcore.GameLogger.info(" processing from image click ");

									InstructionProcessor.processInstructions(
											mouseoverActions, "TIG:" + CoreName
											+ ","
											+ newItem.getBaseFileName(),
											null);

								}

							});
						}
						// if it has mouseout, add them
						if (isMouseoutActions) {
							newItem.addMouseOutHandler(new MouseOutHandler() {

								public void onMouseOut(MouseOutEvent event) {
									if (disabled){
										return;
									}
									Widget sender = newItem;//(Widget) event.getSource();
									// run actions
									// update last clicked variables so that
									// popups can be positioned correctly if
									// used
									JAM.lastclicked_x = imagegroupframe
											.getWidgetLeft(sender)
											+ (sender.getOffsetWidth() / 2);
									JAM.lastclicked_y = imagegroupframe
											.getWidgetTop(sender)
											+ (sender.getOffsetHeight() / 2);
									TigItemCore.lastclickedTig = thisframe;

									// process the instructions
									JAMcore.GameLogger.info(" processing from image click ");

									InstructionProcessor.processInstructions(
											mouseoutActions, "TIG:" + CoreName
											+ ","
											+ newItem.getBaseFileName(),
											null);

								}

							});
						}
						// add its click-and-switch-ability

						newItem.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								if (disabled){
									return;
								}
								Log.info("item clickedon");
								TigItem sender = newItem;//(AnimatedIcon) event.getSource();

								Log.info("item clicked on..");

								// default actions only if there's no other
								// commands for itself.
								if (((Actions.indexOf(((TigItem) sender).uniqueName)) > 0)
										&& (isActions)) {


									Log.info("actions detected:"
											+ (((TigItem) sender).uniqueName)
											+ " : " + isActions);

								} else {

									Log.info("no actions");
									JAMcore.GameLogger.info(isActions
											+ "no actions:" + Actions);
									
									// don't run if actions set to none
									Log.info("running default actions");

									if ((!(Actions.trim().equals("none")))) {
										((TigItem) sender).nextFrameLoop();
									}
								}

								Log.info("run actions");
								// run actions
								if ((!(Actions.trim().equals("none")))
										&& (Actions.length() > 4)) {

									String justActions = Actions;
									// check for condictionals
									// if (Actions.startsWith("(")){
									// String Conditionals =
									// mouseoverActions.split("\\(.*\\)", 2)[0];
									// justActions =
									// mouseoverActions.split("\\(.*\\)", 2)[1];

									// Log.info("conditionals="+Conditionals);
									// Log.info("actions="+Actions);

									// }
									//

									// update last clicked variables so that
									// popups can be positioned correctly if
									// used
									JAM.lastclicked_x = imagegroupframe
											.getWidgetLeft(sender)
											+ (sender.getOffsetWidth() / 2);
									JAM.lastclicked_y = imagegroupframe
											.getWidgetTop(sender)
											+ (sender.getOffsetHeight() / 2);
									TigItemCore.lastclickedTig = thisframe;

									// process the instructions
									JAMcore.GameLogger.info(" processing from image click ");

									InstructionProcessor.processInstructions(
											justActions, "TIG:" + CoreName
											+ ","
											+ newItem.getBaseFileName(),null);

								}

							}

						});

					}

					// check for solution
					if (item_list.indexOf("- CheckTIG") > -1) {
						solutionpresent = false;
						submitans.removeFromParent();
					}

					if (item_list.indexOf("- TIGMessage") > -1) {
						messageboxpresent = true;
						TiGFeedback.getInternalWidget().setVisible(true);
					}
					
					setup=true;
					if (!stateToLoad.isEmpty()){						
						Log.info("now loading state");
						loadState(stateToLoad);
						stateToLoad="";
												
					}
					
					// reduce the loading list
					JAMcore.itemsLeftToLoad--;
					Log.info("tig:"+CoreName+"loaded reduceing items left to load to:"
							+ JAMcore.itemsLeftToLoad);
					
					JAM.testForInstructionsToRun();

				}

			});

		} catch (RequestException ex) {
			System.out.println(" cant item ini");
		}

		// Then we get the TIS's item ini
		String SolutionsIni = GWT.getHostPageBaseURL() + "InventoryItems/"
				+ ItemName + "/" + ItemName + "_solutions.ini";

		RequestBuilder requestSolutions = new RequestBuilder(
				RequestBuilder.GET, SolutionsIni);
		try {
			requestSolutions.sendRequest("", new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Log.info("error in user logged in test");
					solutionpresent = false;
					submitans.removeFromParent();
				}

				public void onResponseReceived(Request request,
						Response response) {

					solution_script = response.getText();

					Log.info("solutions found" + solution_script);

					//parse for language specific extensions
					//Anything with __LAN will be replaced with the current language extension
					solution_script = JAMcore.parseForLanguageSpecificExtension(solution_script);


					if (solution_script.toLowerCase().startsWith("none")) {
						solutionpresent = false;
						submitans.removeFromParent();
					} else {

						submitans.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {

								testCombination();

							}

						});

						if (solution_script.indexOf("- TIGMessage") > -1) {
							messageboxpresent = true;
							TiGFeedback.getInternalWidget().setVisible(true);
						}

					}
				}

			});

		} catch (RequestException ex) {
			JAMcore.GameLogger.info(" cant item ini");
			solutionpresent = false;
			submitans.removeFromParent();
		}

	}

	protected void reSizePopUp() {

		Background.setWidth(Original_picX + "px");
		Background.setHeight(Original_picY + "px");

		Background.getParent().setWidth(Original_picX + "px");
		Background.getParent().setHeight(Original_picY + "px");

		/*
		 * // screen size ScreenSizeX = Window.getClientWidth(); ScreenSizeY =
		 * Window.getClientHeight();
		 * 
		 * // image loaded picY = Original_picY; picX = Original_picX;
		 * System.out.print("\n /n <<<<<<<<<<<<<<<<<<<<<<<image loaded []" +
		 * picX + "-" + picY);
		 * 
		 * // cancel timer if (Original_picX > 5) { timer.cancel(); }
		 * 
		 * // Resize the picture if its over 80% of the screen size
		 * 
		 * if (picX > (ScreenSizeX * 0.8)) {
		 * 
		 * Background.setHeight(picY * ((ScreenSizeX * 0.8) / picX) + "px");
		 * 
		 * System.out.println("ratio=" + picX + ">" + (ScreenSizeX * 0.8)); picX
		 * = (int) Math.round(ScreenSizeX * 0.8); Background.setWidth(picX +
		 * "px");
		 * 
		 * } else if (Original_picX > 5) {
		 * 
		 * Background.setWidth(Original_picX + "px");
		 * Background.setHeight(Original_picY + "px"); } // then match the frame
		 * to fit
		 * 
		 * ImageDiscription.setHeight("100%"); int NewFrameWidth =
		 * (Background.getOffsetWidth() + 0);
		 * Background.getParent().setWidth(NewFrameWidth + "px");
		 * 
		 * int NewFrameHeight = getOffsetHeight();
		 * 
		 * // and the shadow container //
		 * bigPicture.getParent().getParent().setWidth(20+NewFrameWidth+"px");
		 * 
		 * // re center
		 * 
		 * // test it isnt bigger then the screen. if (NewFrameHeight >
		 * ScreenSizeY) { posY = ((ScreenSizeY / 2) - (NewFrameHeight / 2)) +
		 * ((NewFrameHeight - ScreenSizeY) / 2);
		 * 
		 * } else { posY = (ScreenSizeY / 2) - (NewFrameHeight / 2); }
		 * 
		 * // -- ((hasCloseDefault) (Background.getParent())).CloseDefault();
		 * ((hasOpenDefault) (Background.getParent())).OpenDefault();
		 * 
		 * // RootPanel.get().remove(bigPicture.getParent().getParent()); //
		 * RootPanel.get().add(bigPicture.getParent().getParent(), //
		 * (ScreenSizeX/2)-(NewFrameWidth/2),posY );
		 * 
		 * // adjust parents topbar size //
		 * ((PopUpWithShadow)(this.getParent()))
		 * .TopBar.setWidth((this.getOffsetWidth()-80)+"px");
		 * 
		 * // adjust fader size //
		 * MyApplication.fadeback.setSize("100%",900+"px");
		 * System.out.println("Screen Height=" +
		 * RootPanel.getBodyElement().getOffsetHeight());
		 */
	}

	public void RecheckSize() {

		// reSizePopUp();

	}
	
	//Name is currently checked in the following order;
	//a)if it does not have .png at the end we check for IDs with this name
	//b)if none found, or it does not have png at the end, we then check over the base filenames manually for a match
	//
	//in future we might have a numbered ID system to make this cleaner? [0] etc
	public void setItemState(String itemnamesearch, String state) {

		if (itemnamesearch.isEmpty()){
			Log.info("no item name supplied");
			return;
		}
		
		if (itemnamesearch.startsWith("[")){
			
			//strip []
			itemnamesearch = itemnamesearch.replace("[", "").replace("]", "");
			
			//get the number
			int itemNum = Integer.parseInt(itemnamesearch);			
			setItem(TIGitems.get(itemNum), state);
			
		}

		// we assume its an ID name
		String itemname = itemnamesearch;
		
		// if item name is a png;
		if (!itemnamesearch.toLowerCase().endsWith(".png")) {

			JAMcore.GameLogger.info("\n setting item-" + itemname);
			JAMcore.GameLogger.info("\n checking-" + TIGitems.size()
					+ "-");

			Iterator<TigItem> itemit = TIGitems.iterator();

			while (itemit.hasNext()) {
				TigItem testhis = itemit.next();

				JAMcore.GameLogger.info("\n compare to-"
						+ testhis.getBaseFileName());

				if (testhis.uniqueName.equalsIgnoreCase(itemname.trim())) {
					// MyApplication.DebugWindow.info("\n setting -"+testhis.basefilename);
					setItem(testhis, state);
					return;
					}
				//if no id is found we continue searching based on the filename
			
			};
			
		} else {

			//if the name contained .png remove the .png and the number at the end to get the base url
			itemname = itemnamesearch.substring(0,
					itemnamesearch.length() - 5);
		}
		
		
			// search for this
			//String itemname = itemnamesearch.substring(0,
			//		itemnamesearch.length() - 5);


			// Log.info("\n setting item-"+itemname);
			// Log.info("\n checking-"+TIGitems.size()+"-");

			// search for item matching itemname
			Iterator<TigItem> itemit = TIGitems.iterator();


			while (itemit.hasNext()) {
				TigItem testhis = itemit.next();

				JAMcore.GameLogger.info("\n compare to-"+ testhis.getBaseFileName());

				//Log.info("testing item:"+itemname.trim()+":");
				//Log.info("against item:"+testhis.getBaseFileName()+":");
				
				if (testhis.getBaseFileName().endsWith(itemname.trim())) {
					// MyApplication.DebugWindow.info("\n setting -"+testhis.basefilename);
					setItem(testhis, state);

					// break;
				}
			
		}

	}

	public void setItem(final TigItem item, String state) {
		state=state.trim();

		// set Items based on state		
		Log.info("\n setting :" + item.getBaseFileName() + " to:" + state+"");

		//if the state is "hide" or "show" then any TigItem can take it
		if (state.equalsIgnoreCase("show")){
			item.setVisible(true);
			return;
		}
		if (state.equalsIgnoreCase("hide")){
			item.setVisible(false);
			return;
		}

		//else its just for AnimatedIcons
		GWTAnimatedIcon icontochange = item.iconIfAny;


		if (icontochange==null) {
			Log.info("Attempting to set something that wasn't a icon, returning");
			return;			
		}


		// cancel any timers
		if (AnimateRepeat != null) {
			AnimateRepeat.cancel();
		}


		//if its a animation state directly it will contain _ or be just a number
		if(state.contains("_")	|| (Ints.tryParse(state)!=null)) {
			Log.info("Load animation state:"+state);
			icontochange.loadSerialisedAnimationState(state);
			return;
		}


		if (state.compareTo("PlayForward") == 0) {
			icontochange.setPlayForward();
		} else if (state.compareTo("PlayBounceLoop") == 0) {

			// should be;
			// Log.info("play bounce loop set");
			icontochange.setPlayForwardThenBackLoop();
			// But this doesnt work (no error, just novisible change

			/*
			 * //workaround below for now (-sigh-) AnimateRepeat = new Timer(){
			 * 
			 * @Override public void run() {
			 * Log.info("triggering bouncy thing");
			 * 
			 * icontochange.setAnimateOpenThenClose(); }
			 * 
			 * };
			 * 
			 * AnimateRepeat.scheduleRepeating(1000);
			 */

		} else if (state.compareTo("PlayLoop") == 0) {
			icontochange.setPlayLoop();
		} else if (state.compareTo("PlayBounce") == 0) {
			icontochange.setPlayForwardThenBack();
		} else if (state.compareTo("PlayBack") == 0) {
			icontochange.setPlayBack();
		} else if (state.startsWith("GotoFrame")) {

			String targetFrameStr = state.substring(9);
			int targetFrame = Integer.parseInt(targetFrameStr);
			Log.info("Setting frame to:" + targetFrame);

			icontochange.gotoFrame(targetFrame);

		} else if (state.compareTo("NextFrameLoop") == 0) {
			icontochange.nextFrameLoop();
		} else if (state.compareTo("PrevFrameLoop") == 0) {
			icontochange.prevFrameLoop();
		} else if (state.compareTo("PlayForwardXframes") == 0) {
			// 4 needs to be changed to variable number
			icontochange.playForwardXframes(4);
		}
	}

	public void CloseDefault() {

	}

	public void OpenDefault() {

	}

	public String POPUPTYPE() {
		return "TIG";
	}

	public boolean POPUPONCLICK() {
		return true;
	}

	public void testCombination() {

		Log.info("test Combination");

		// First we get the states of all current items in the absolute panel.
		String allcurrenttstates = "";

		Iterator<TigItem> TIGitems_it = TIGitems.iterator();


		while (TIGitems_it.hasNext()) {

			TigItem currentItem = TIGitems_it.next();

			//ignore labels
			if (currentItem.labelIfAny!=null){
				continue;
			}			

			String currentstate = currentItem.getUrl();	

			// we just want the filename
			currentstate = currentstate.substring(currentstate.lastIndexOf("/") + 1).trim();

			allcurrenttstates = allcurrenttstates + "~~" + currentstate + "~~";

		}
		Log.info(allcurrenttstates);

		// Now we test them against the reponse's

		// loop for each ans line;
		int starthere = 0;
		int cpos = 0;
		int nextansstart = 0;
		int linestarthere = 0;
		String currentline = "";
		String state = "";
		boolean correct_ans = false;

		checkansloop: while (solution_script.indexOf("ans:", starthere) > -1) {

			currentline = "";

			// loop for each ans in line;
			cpos = solution_script.indexOf("ans:", starthere) + 4;
			currentline = solution_script.substring(cpos,
					solution_script.indexOf("\n", cpos)).trim();
			// System.out.print("\n-"+currentline);

			// now with this line, I do loop to see if all answers lie...
			linestarthere = 0;
			correct_ans = true;
			linecheck: do {

				// get image state
				nextansstart = currentline.indexOf("+", linestarthere);
				if (nextansstart == -1) {
					nextansstart = currentline.length();
				}
				state = currentline.substring(linestarthere, nextansstart)
						.trim();

				// check for an =, which indicates a specific icon is specified
				if (state.contains("=")) {

					// split around =
					String requiredNameID = state.split("=")[0].trim();
					String requiredState = state.split("=")[1].trim();

					JAMcore.GameLogger.info("searching for "
							+ requiredNameID + " set to " + requiredState);

					// look up item that corisponds to left side
					// search for item matching itemname
					Iterator<TigItem> itemit = TIGitems.iterator();

					String currentItemsState = "";
					while (itemit.hasNext()) {
						TigItem currentItem = itemit.next();

						JAMcore.GameLogger.info("<br> testing: "
								+ currentItem.uniqueName + ": ");

						if (currentItem.uniqueName
								.equalsIgnoreCase(requiredNameID)) {
							currentItemsState = currentItem.getUrl();

							// we just want the filename
							currentItemsState = currentItemsState.substring(
									currentItemsState.lastIndexOf("/") + 1)
									.trim();

							JAMcore.GameLogger.info(" found: "
									+ currentItemsState + ":");
							break;
						}
					}

					// see if its currently set to the right state
					if (!(currentItemsState.trim().equals(requiredState))) {
						// if its not then we skip this loop
						correct_ans = false;
						System.out.print("wrong");
						break linecheck;

					}

				} else {

					// else we default to just checking something is at that
					// state
					// we check if the state is in the allcurrenttstates
					Log.info("\n checking :" + state + "  in  "
							+ allcurrenttstates + "\n");
					if (allcurrenttstates.indexOf("~" + state + "~") == -1) {
						// if its not then we skip this loop
						correct_ans = false;
						Log.info("wrong");
						break linecheck;
					}

				}
				linestarthere = nextansstart + 1;

			} while (currentline.indexOf("+", linestarthere - 1) > -1);

			if (correct_ans == true) {

				Log.info("CORRRRECTTT!!! processing script from "+cpos);

				// we then get the ans strings and process them...just like
				// other answers!
				int endans = solution_script.indexOf("ans:", cpos);
				if (endans == -1) {
					endans = solution_script.length();
				}

				//Log.info("endans ="+endans);


				String instructionset = solution_script.substring(
						solution_script.indexOf("\n", cpos) + 1, endans).trim();

				//	Log.info("indexOf(\n, cpos) + 1 ="+solution_script.indexOf("\n", cpos) + 1);

				Log.info(" Processing:" + instructionset);

				InstructionProcessor.processInstructions(instructionset, "TIG:"	+ CoreName + ",CorrectAns", null);

				break checkansloop;

			}

			starthere = cpos + 1;

		}

		// if no correct

	}


	public boolean DRAGABLE() {
		return true;
	}

	public boolean MAGNIFYABLE() {
		return Magnifable;
	}

	public String getSourceURL() {
		return sourceurl;
	}

	public int sourcesizeX() {
		//safely remove any CSS first
				String removedAnyCss = SpiffyFunctions.StripCSSfromNumber(sourcesizeX);	
			

		return Integer.parseInt(removedAnyCss);
	}

	public int sourcesizeY() {
		//safely remove any CSS first
				String removedAnyCss = SpiffyFunctions.StripCSSfromNumber(sourcesizeY);	
			

		return Integer.parseInt(removedAnyCss);
	}
	//states look like this atm;
	//_#_Select_the_details_for_Coin_A_(Click_on_the_red_text_to_change_the_values)=Select the details for Coin A (Click on the red text to change the values)_#_Coin_A's_Weight:=Coin A's Weight:_#_Coin_A's_Volume:=Coin A's Volume:_#_Coin_A's_Density:=Coin A's Density:_#_How_much_did_the_water_move?=How much did the water move?_#_When_calculating_the_density_remember_to_work_out_the_maths_in_the_brackets_first!=When calculating the density remember to work out the maths in the brackets first!_#_InventoryItems/coinadetails/items/arrowToDisplacement=_#_InventoryItems/coinadetails/items/d=_#_InventoryItems/coinadetails/items/g=_#_InventoryItems/coinadetails/items/divided=_#_InventoryItems/coinadetails/items/equals=_#_InventoryItems/coinadetails/items/leftBracket=_#_InventoryItems/coinadetails/items/times=_#_InventoryItems/coinadetails/items/rightBracket=_#_InventoryItems/coinadetails/items/dis=_#_InventoryItems/coinadetails/items/volperdis=_#_InventoryItems/coinadetails/items/correct=_#_InventoryItems/coinadetails/items/ans=_#_InventoryItems/coinadetails/items/check__en=

	public String getState(){
		Log.info("getting tig state");
		String deliminator = SceneObjectState.deliminator; //we use the same deliminator as sceneobjects
		String serialisedTig = "";
		//loop over each item getting its state
		for (TigItem item : TIGitems) {


			String itemsSettings = item.getState(); //we should exclude if objects don't have a ID or filename (ie, most text)
			serialisedTig=serialisedTig+deliminator+itemsSettings;

		}
		return serialisedTig;		
	}

	public void loadState(String state){
		
		if (!setup){		   
			Log.info("tig not setup yet waiting for load");
			stateToLoad = state;
			return;
		}
		
		Log.info("loading tig state");
		
		String deliminator = SceneObjectState.deliminator; //we use the same deliminator as sceneobjects

		//split
		String itemData[] = state.split(deliminator);

		for (String itemString : itemData) {
			if (itemString.isEmpty()){
				continue;
			}

			String itemName  = itemString.split("=", 2)[0];
			String itemState = itemString.split("=", 2)[1];

			Log.info("setting tig item:"+itemName+" to "+itemState);

			setItemState(itemName, itemState);


		}

		return;		
	}

	//A simple class for TiGItems this is only really used to let us have either images or text objects on tigs
	class TigItem extends Composite {
		String uniqueName;

		GWTAnimatedIcon iconIfAny = null;
		InlineHTML  labelIfAny = null;
		FocusPanel focusWrapper = null;

		Widget handlerHock;


		TigItem(InlineHTML  contents){	
			labelIfAny=contents;
			handlerHock = contents;
			focusWrapper = new FocusPanel(contents);
			initWidget(focusWrapper);


			super.setStylePrimaryName("unselectable");



		}


		TigItem(GWTAnimatedIcon contents){	
			handlerHock = contents;
			iconIfAny = contents; //NOTE unsafe typically as AnimationContents might not have all the mouse handlers
			initWidget(contents);			

			super.setStylePrimaryName("unselectable");

		}




		/**
		 * moves to the next frame if its a sprite object
		 */
		public void nextFrameLoop() {
			if (iconIfAny!=null){
				iconIfAny.nextFrameLoop();				
			}
		}

		/**
		 * gets the url if its a image 
		 * @return
		 */
		public String getUrl() {
			if (iconIfAny!=null){
				return iconIfAny.getUrl();

			}
			return null;
		}

		/**
		 * gets a string representing this icons identifier and state
		 * Animation states look like;
		 * _0_0_0_0_0_112_
		 * Text states are just the text contents
		 * @return
		 */

		public String getState() {
			//labels with no Ids have no state (as theres no way to change them later)			
			if (labelIfAny!=null && (uniqueName==null||uniqueName.isEmpty())){
				return "";
			}

			String identifier = "["+TIGitems.indexOf(this)+"]";

			if ( uniqueName!=null && !uniqueName.isEmpty()){
				identifier = this.uniqueName;
			}

			if (iconIfAny!=null){
				String state = iconIfAny.serialiseAnimationState();
				if (state.isEmpty()){
					return "";//empty state means we dont even return this items name, as its on the default state anyway it never needs to change
				}
				return identifier+"="+state;				
			}

			if (labelIfAny!=null){
				return identifier+"="+labelIfAny.getHTML();
			}

			return null;
		}

		public void restoreState(String state) {
			if (iconIfAny!=null){
				iconIfAny.loadSerialisedAnimationState(state);
				return; 

			}
			if (labelIfAny!=null){
				labelIfAny.setHTML(state);
				
				return;
			}
			return;
		}


		public HandlerRegistration addMouseOutHandler(MouseOutHandler mouseOutHandler) {

			if (iconIfAny!=null){
				return iconIfAny.addMouseOutHandler(mouseOutHandler);				
			}
			if (labelIfAny!=null){
				return labelIfAny.addMouseOutHandler(mouseOutHandler);	
			}
			return null;
		}


		public HandlerRegistration addMouseOverHandler(MouseOverHandler mouseOverHandler) {

			if (iconIfAny!=null){
				return iconIfAny.addMouseOverHandler(mouseOverHandler);				
			}
			if (labelIfAny!=null){
				return labelIfAny.addMouseOverHandler(mouseOverHandler);	
			}
			return null;
		}


		public HandlerRegistration addClickHandler(ClickHandler clickHandler) {

			if (iconIfAny!=null){
				return iconIfAny.addClickHandler(clickHandler);				
			}
			if (labelIfAny!=null){
				return labelIfAny.addClickHandler(clickHandler);	
			}
			return null;
		}




		/**
		 * returns the base filename of sprites, or the string contents of TEXT items (only without spaces)
		 * @return
		 */
		public String getBaseFileName(){

			if (iconIfAny!=null){
				return iconIfAny.basefilename;
			}
			if (labelIfAny!=null){
				return labelIfAny.getText().replace(" ","_");
			}
			return "BAD TYPE ERROR IN TIG ITEM";
		}


	}

	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}

	@Override
	public Widget asWidget() {
		return panelcontents;
	}

	//Seems to add a popup message to the last clicked tigs
	public void triggerPopUpMessage(String CurrentProperty) {
		
		
		if (CurrentProperty.indexOf(",") > -1) {
			int totalmessages = CurrentProperty.split("\",\"").length;
			int selectthis = (int) (Math.random() * totalmessages);
			// note - to increase speed a precompiled regex can be used
			// here.
			CurrentProperty = CurrentProperty.split("(\",\")|(\" , \")")[selectthis]; 
		}
	
		// position based on freespace
		int lastclicked_x = JAM.lastclicked_x;
		int lastclicked_y = JAM.lastclicked_y;
	
		CurrentProperty = CurrentProperty.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
		//JAM.DebugWindow.info(lastclicked_x
		//		+ "  " + JAM.lastclickedTig.getWidgetCount()); //$NON-NLS-1$
	
		Label messagepop = new Label(CurrentProperty.trim());
	
		if (!(JAM.messageArrow == null)) {
			JAM.messageArrow.removeFromParent();
		}
	
		JAM.messageArrow = new SpiffyArrow(30, 30);
	
		// style controlls size
		//messagepop.setSize("100px", "50px"); //$NON-NLS-1$ //$NON-NLS-2$
		messagepop.setStylePrimaryName("mesagePopUps"); //$NON-NLS-1$
	
		// check for too far right
		if (lastclicked_x > (((Widget)TigItemCore.lastclickedTig.getVisualRepresentation()).getOffsetWidth() - 200)) {
			JAM.messageArrow.SetArrowTopRight();
			TigItemCore.lastclickedTig.add(
					JAM.messageArrow, lastclicked_x - 30,
					lastclicked_y);
			TigItemCore.lastclickedTig.add(messagepop,
					lastclicked_x - 30 - 107, lastclicked_y + 29);
	
		} else {
			TigItemCore.lastclickedTig.add(
					JAM.messageArrow, lastclicked_x, lastclicked_y);
			TigItemCore.lastclickedTig.add(messagepop,
					lastclicked_x + 30, lastclicked_y + 29);
	
		}
	
		if (!(JAM.lastpopupmessage == null)) {
			JAM.lastpopupmessage.removeFromParent();
		}
		JAM.lastpopupmessage = messagepop;
	}

	@Override
	public int getAbsoluteTopOfImage() {
		return imagegroupframe.getAbsoluteTop();
	}

	@Override
	public int getAbsoluteLeftOfImage() {
		return imagegroupframe.getAbsoluteLeft();
	}

	@Override
	public void add(Object addThis, int left, int top) {
		imagegroupframe.add((Widget) addThis, left, top);
		
	}

	@Override
	public void setFeedbackText(String text) {
		TiGFeedback.setText(text);
	}

	@Override
	public int getExpectedZIndex() {
		return -1;
	}
}
