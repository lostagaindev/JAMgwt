package com.lostagain.JamGwt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.JamGwt.JargScene.ServerOptions;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

public class CluePanel extends VerticalPanel implements hasCloseDefault, hasOpenDefault,IsPopupContents  {
	
	
	
	/*
	 *  The clue panel is the main container used to buy and sale clues in the game.
	 *  
	 *  It recieves data from a php script telling it what clues are avaliable on this chapter.
	 *  This returns the clues in a simple format listing the clue name and cost;
	 *  Clue,123|Clue,34|Tutorial,53|Key,64 etc.
	 * 
	 * Some clues have preresquists for buying them...specialy having bought other clues first.
	 * However, this detail is handeled by the PHP script itself.
	 * 
	 * Clues are controlled by clue.txt in the format
	 * 
	 * ClueName, Requires = ClueContents, Cost
	 * 
	 */
	
	
	Label instructions = new Label("Loading prices...");
	Label instructions2 = new Label("(You can check the clues you have bought at any time from the top right corner)");
	Label chapterheader = new Label(" Current Chapter: ");
	String currentCluesChapter = "";
	
	//ClueContainer
	VerticalPanel ClueContainer = new VerticalPanel();
	
	//Buttons
	HorizontalPanel buy_or_cancel = new HorizontalPanel();
	
	Button buy = new Button("buy");
	Button cancel = new Button("cancel");
	
	
	//this
	CluePanel thispanel = this;
	String clueData[];
	
	//
	int currentClueSelection = 0;
	
	public CluePanel(){
		
		this.setSize("350px", "280px");
		this.setStylePrimaryName("notepadback");
		this.add(instructions);
		this.setCellHorizontalAlignment(instructions, HasHorizontalAlignment.ALIGN_CENTER);
		this.add(chapterheader);
		chapterheader.setText("Clues for chapter: "+JAMcore.usersCurrentLocation+":");
		this.setCellHorizontalAlignment(chapterheader, HasHorizontalAlignment.ALIGN_CENTER);
		
		this.add(ClueContainer);
		ClueContainer.setWidth("100%");
		ClueContainer.setSpacing(6);
		ClueContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.setCellHorizontalAlignment(ClueContainer, HasHorizontalAlignment.ALIGN_CENTER);
		
		this.setSpacing(5);
		
		this.add(instructions2);
		this.setCellHorizontalAlignment(instructions2, HasHorizontalAlignment.ALIGN_CENTER);
		
		buy_or_cancel.add(buy);
		buy_or_cancel.add(cancel);
		buy_or_cancel.setSpacing(4);
		this.add(buy_or_cancel);
		this.setCellHorizontalAlignment(buy_or_cancel, HasHorizontalAlignment.ALIGN_CENTER);
		
		//add button listeners
		cancel.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				
				JAM.CluePanelFrame.CloseDefault();
			}
		});
		
		buy.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				//Check something is selected
				if (currentClueSelection==0){
					return;
				} else {
					currentCluesChapter = JAMcore.usersCurrentLocation;
					
					
					RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,
							// "text/messages_dutch.properties");
									"clue fetcher2.php");

							try {
								requestBuilder.sendRequest("ClueChapter="+JAMcore.usersCurrentLocation+"&FetchClue="+currentClueSelection+"&ExistingClues="+JAMcore.playersClues.ClueArrayAsString(), new RequestCallback() {

									public void onError(Request request,
											Throwable exception) {
									
									}

									public void onResponseReceived(Request request,
											Response response) {
										String responsetext=response.getText();
																				
										
										//Confirm clue is recieved
										if (responsetext.startsWith("#ClueReturn:")==true){
											String newclue = responsetext.substring(12);
											
											if (newclue.startsWith("(Already Got This Clue)")){
												return;								
											}
											if (newclue.startsWith("(Already Got This Answer)")){
												return;								
											}											
											if (newclue.startsWith("(You havnt got the requirements)")){
												return;
											}
											//get score
											int cost = Integer.parseInt(clueData[currentClueSelection-1].trim().split(",")[1].trim());
											
											//get clue name
											String cluename = clueData[currentClueSelection-1].trim().split(",")[0].trim();
											
											//Subtract score
											JAMcore.PlayersScore.AddScore(-cost);
															
																					
											//Add to clue list
											JAMcore.playersClues.AddClue(newclue.trim(),clueData[currentClueSelection-1].trim().split(",")[0], currentCluesChapter.trim());
											//MyApplication.DebugWindow.setText(MyApplication.playersClues.Clues)
											
											//add to the HistoryPanel
											
											// if tutorial 
										//	if (){
											InstructionProcessor.processInstructions(" - Message = \" \""+cluename+"\" "+GamesInterfaceText.CluePanel_purchasedclue+" "+currentCluesChapter.trim()+" \"  \n ", "ClueGotMessage",null);
										//	} else {
										//		MyApplication.processInstructions(" - Message = \" Clue Purchased for "+currentCluesChapter.trim()+" \"  \n ", "ClueGotMessage");												
										//	}
											//Save the game
											JAMcore.GameLogger.info("SAVEING GAME....");
										//	ServerOptions.saveGameToServer();
											GwtSaveGameManager.SEVER_OPTIONS.saveGameToServer();
											
											//update clue box to show its bought
											//MyApplication.DebugWindow.setText("Changing clue container");
											
												ClueContainer.remove(currentClueSelection-1);
												
												ClueContainer.insert(new Label(" "+clueData[currentClueSelection-1].trim().split(",")[0]+" (already bought)"), currentClueSelection-1);
										
												
											//reset currentClueSelection
												currentClueSelection=0;
											
												//update buy list
												updateCluesToBuy();
											
											//close this box
												thispanel.CloseDefault();
												
											//open the cluebox
												JAM.ClueReveal.setOpen(true);
												//open the close box
												JAM.closeallwindows.setPlayForward();
												
												
										}
										
										//if clue is present already
									
									} 
							});
				} catch (RequestException ex) {
				
					
				}
					
					
					
				}
				
				
				
			}
			
		});
	}

	public void CloseDefault() {
		
		
		// Close the panel
		JAM.CluePanelFrame.hide();
		JAMcore.popupPanelCurrentlyOpen.remove(JAM.CluePanelFrame);
		
		JAM.BuyOpenButton.applyTo(JAM.BuyBack);	
		   
	
		
		
	    
	}

	public void OpenDefault() {
		// When opened we check if the chapter is changed, and update clue if nesscery
		   JAMcore.GameLogger.info("opening cluepanel");
		   
		if (!(JAMcore.usersCurrentLocation.equalsIgnoreCase(currentCluesChapter))){

			
			chapterheader.setText(" "+JAMcore.usersCurrentLocation);
			
				//get all clues cost for chapter
			
			//MyApplication.chapter
			updateCluesToBuy();
		
		currentCluesChapter = JAMcore.usersCurrentLocation;
		
		
			
		}
		
	}

	public void updateCluesToBuy() {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,
				// "text/messages_dutch.properties");
						"clue fetcher2.php");

				try {
					requestBuilder.sendRequest("ClueChapter="+JAMcore.usersCurrentLocation+"&FetchClue=&ExistingClues="+JAMcore.playersClues.ClueArrayAsString()+"<br>", new RequestCallback() {

						public void onError(Request request,
								Throwable exception) {
						
						}

						public void onResponseReceived(Request request,
								Response response) {
							String responsetext=response.getText();
							//Clear existing clues
							ClueContainer.clear();
							
							if (responsetext.equals("chapter not found")){
								ClueContainer.add(new Label("No Clues For This Chapter"));
							}
							
							//split into array
							clueData = responsetext.split("\\|");
							
							//loop for each element of array and add to panel
							
							int i=0;
							while (i<clueData.length){
								
								if ( clueData[i].trim().contains("(already bought)") ){
									ClueContainer.add(new Label( clueData[i].trim()));
									
								} else if( !(clueData[i].trim().contains(","))){
									
									//do nothing
									
								}
								else {
									
									
									ClueForSale newclue;
									
//										//detect solution
//										if ( clueCosts[i].trim().startsWith("ANS:")){
//											
//											clueCosts[i] = clueCosts[i].trim().substring(4);
//											int cost = Integer.parseInt(clueCosts[i].trim());
//											
//											newclue  = new ClueForSale(i+1,cost,true);
//											
//										} else {

									
									//extract name
									
									String Cluename =  clueData[i].trim().split(",")[0];
									String ClueCost =  clueData[i].trim().split(",")[1];
									
									
										int cost = Integer.parseInt(ClueCost.trim());
										
										newclue  = new ClueForSale(i+1,cost,Cluename);
										
									//}
									
									ClueContainer.add(newclue);
									ClueContainer.setCellHorizontalAlignment(newclue, HasHorizontalAlignment.ALIGN_CENTER);
									
								}
								i++;
							}
							
							
							//instructions.setText("When buying a clue you gain a hint for the current puzzle, but perminately reduces your score by the amount it costs, and the game will automaticaly save. \n Which clue would you like to buy?");
							instructions.setText(GamesInterfaceText.CluePanel_instructions);
							
							//MyApplication.DebugWindow.setText(MyApplication.playersClues.ClueArrayAsString());
							
						
						} 
				});
} catch (RequestException ex) {

		
}
	}

	public class ClueForSale extends HorizontalPanel
	{
		public boolean Solution_Mode = false;
		
		public ClueForSale(final int num, final int cost, final String ClueName){
		
	    
	    	this.add(new Label( ClueName+" costs    -  "+cost+" points. -"));
	    
		final RadioButton selectClue = new RadioButton("SelectClue", "");
		
		//add selection detection
		selectClue.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (selectClue.getValue()){
				currentClueSelection = num;
				}
			}
			
		});
		
		this.add(selectClue);
		
		
	    }
	}
	
	public boolean DRAGABLE() {
		
		return false;
	}

	public boolean POPUPONCLICK() {
	
		return false;
	}

	public String POPUPTYPE() {
		
		return "CluePanel";
	}

	public void RecheckSize() {
		
		
	}

	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}

}
