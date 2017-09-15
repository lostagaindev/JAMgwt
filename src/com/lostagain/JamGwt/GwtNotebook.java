package com.lostagain.JamGwt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.PlayersNotebookCore;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isPopupTypeImplementation;
import com.lostagain.JamGwt.Sprites.InternalAnimations;

public class GwtNotebook extends PlayersNotebookCore implements hasCloseDefault, hasOpenDefault,isPopupTypeImplementation {

	AbsolutePanel visualContents = new AbsolutePanel();
	
	//background image
	HTML notebookpage = new HTML();
	
	//main stack panel
	StackPanel ProfilePages = new StackPanel();

	/**
	 * This shadows the copy in the NotepadCore to provide the GWT specific functions
	 */
	final public GWTAnimatedIcon NotepadButton;
	

	public void CloseDefault(){		
		
		  NotepadOpen = false;
		  NotepadButton.setPlayBack();
		  		 
	  }
	
	public GwtNotebook(){
		super();

		//assign the core button
		NotepadButton = new GWTAnimatedIcon(InternalAnimations.BigNotepadImages);
		super.NotepadButton = NotepadButton;
		
		//add its handlers;
				NotepadButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						// RootPanel.get().add(MyApplication.fadeback,0, 0);
						JAM.PlayersNotepadFrame.OpenDefault();

					}
				});
				NotepadButton.addMouseDownHandler(new MouseDownHandler() {
					@Override
					public void onMouseDown(MouseDownEvent event) {

						NotepadOpen = true;

						// NotepadButton.setUrl("GameIcons/Notebook_open.png");
						// toggle inventory open
						// NotepadOpen = true;
						// always open this when a panel is opened;
						JAM.closeallwindows.setPlayForward();
						// --

					}

				});

				NotepadButton.addMouseOverHandler(new MouseOverHandler() {
					@Override
					public void onMouseOver(MouseOverEvent event) {
						NotepadButton.setPlayForward();
					}
				});
				NotepadButton.addMouseOutHandler(new MouseOutHandler() {
					@Override
					public void onMouseOut(MouseOutEvent event) {
						if (NotepadOpen == false) {
							NotepadButton.setPlayBack();
						}
					}
				});
				//--------------
		
		
		
		//make topbar
		//  topBar NewTopBar = new topBar("Notebook",this);	  	
		  
		  //add title
		//  this.add(NewTopBar,0,0);
		 // NewTopBar.setWidth("100%");
		//this.add(notebookBackground, 0, 30);
		visualContents.setStylePrimaryName("notepadback");
		  //ProfilePages.setStylePrimaryName("DefaultTopBar");
		  
		visualContents.setWidth( (Window.getClientWidth()/2)+"px");
		visualContents.setHeight( (Window.getClientHeight()*0.7)+"px");
		  
		  //add html back
		  
			ScrollPanel MainContainer = new ScrollPanel();
			MainContainer.setHeight( (Window.getClientHeight()*0.69)+"px");
			MainContainer.setWidth("100%");
			MainContainer.add(ProfilePages);
			visualContents.add(MainContainer,0,5);
		  
		  
		  //this.add(new Label("Charecter Profils"),0,5);
		  ProfilePages.setWidth("100%");
		  
		  
		  //add first page
		  //ProfilePages.add(notebookpage, "Charecter Profiles"); 
		  
		 
	}

	
	//TODO: use file manager, a lot of this can be moved to the core and instead we have it referee to function
	//here to add already retrieved data to the pad
	public void AddPage(final String PageName, final String PageTitle){
			
		     
	    
	RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				 JAM.notebookpages_url+PageName);	
	
				 requestBuilder.setHeader("Content-Type", "text/plain;charset=utf-8");
				 
				try {
				      requestBuilder.sendRequest("FileURL=" + JAM.notebookpages_url, new RequestCallback() {
				        public void onError(Request request, Throwable exception) {
				        	System.out.println("http failed while getting:"+JAM.notebookpages_url+PageName);
				        }

				        public void onResponseReceived(Request request, Response response) {
				        	
				        	
				        	JAMcore.GameLogger.info(response.getHeadersAsString());
				        	
				        	String newPage = response.getText();
				        	
				        	int headerLoc = newPage.toLowerCase().indexOf("</head>");
				        	//crop the header if its there
				        	
				        	newPage = newPage.substring(headerLoc+7);
				        	
				        	//ScrollPanel PageContainer = new ScrollPanel();
				    		HTML newnotebookpage = new HTML(newPage);
				    		
				    		newnotebookpage.setHeight("100%");
				    		//PageContainer.setHeight((Window.getClientHeight()*0.5)+"px");
				    		
				    		//PageContainer.add(newnotebookpage);
				    	    ProfilePages.add(newnotebookpage,PageTitle); 	
				    	  //PageContainer.setHeight( "100%");
				    	   // newnotebookpage.setStyleName("overflowscroll");
				    	  //  Style notebookstyle = newnotebookpage.getElement().getStyle();
				    	    //notebookstyle.setProperty("overflow", "scroll");
				    	    
				    	    LoadedPages.add(PageName);
				        }
				      });
				    } catch (RequestException ex) {
				    	System.out.println("can not connect to game controll file for notes");
				    }
	    
	}
	
	
	
	public void ShowDefault(){
		
		visualContents.setSize("55%", "70%");

		//screen size
		int ScreenSizeX = Window.getClientWidth();
		int ScreenSizeY = Window.getClientHeight();
		
		 
		// app size
		int NootbookSizeX = (int)Math.round(Window.getClientWidth()*0.5);
		int NootbookSizeY = (int)Math.round(Window.getClientHeight()*0.7);
		
		  int posX = ScreenSizeX/2 - NootbookSizeX/2;
		  int posY = ScreenSizeY/2 - NootbookSizeY/2;
		
		 
		RootPanel.get().add(this,posX, posY);
	}

	public void OpenDefault() {
		
	}

	public boolean DRAGABLE() {
		return true;
	}

	public boolean POPUPONCLICK() {
		return false;
	}

	public String POPUPTYPE() {
		return null;
	}

	public void RecheckSize() {
		
	}
	public boolean MAGNIFYABLE() {
		return false;
	}
	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}

	@Override
	public Widget asWidget() {
		return visualContents;
	}

	@Override
	public void clearPagesVisualImplementation() {
		JAM.PlayersNotepad.LoadedPages.clear();
		
	}
}
