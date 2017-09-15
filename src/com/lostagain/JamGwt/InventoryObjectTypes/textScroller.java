package com.lostagain.JamGwt.InventoryObjectTypes;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

public class textScroller extends SimplePanel  implements hasCloseDefault,isInventoryItemImplementation,
hasOpenDefault, IsPopupContents {

	int ypos=0;

	AbsolutePanel Container = new AbsolutePanel();
	HTML contents = new HTML ("Laaaa, la la Laaaaa, laaaaa la la laaaaaa....LAAAA LA <br> la laaaa <br> la laaaa <br>la laaaa <br>la laaaa <br>la laaaa <br>la laaaa <br>la laaaa <br> la laaaa <br> la laaaa");
	
	Timer Scroller;
	
	SimplePanel scrollpop = this;
	
	public textScroller(String URL){
		
		//get the contents from file
		
		RequestBuilder requestBuilder2 = new RequestBuilder(RequestBuilder.GET,
				URL); //$NON-NLS-1$
try {
	requestBuilder2.sendRequest("", new RequestCallback() { //$NON-NLS-1$
				public void onError(Request request, Throwable exception) {
					System.out.println("error getting scroll text"); //$NON-NLS-1$
				}

				public void onResponseReceived(Request request,
						Response response) {
					

					
					// process for names etc
					String newscrollbit = JAMcore.SwapCustomWords(response.getText());
					
					
					//add as html
					contents.setHTML(newscrollbit);
					// add buffer at end
					contents.setHTML("<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>"+contents.getHTML()+"<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>");
					

					scrollpop.setWidth("400px");
					scrollpop.setHeight("400px");
					Container.add(contents);
					contents.setStylePrimaryName("Scroll");
					contents.setWidth("100%");
					Container.setSize("100%", "100%");
					scrollpop.add(Container);
					
					ypos = Container.getOffsetHeight();
					
					//timer to scroll it
					Scroller= new Timer(){

						@Override
						public void run() {
							ypos=ypos-1;
							Container.setWidgetPosition(contents, 0, ypos);
							if (ypos<-(contents.getOffsetHeight()-Container.getOffsetHeight() )){
								ypos=0;
							}
						}
						
					};
					
				}
				
	});
}catch (RequestException ex) {
	System.out.println(" cant find scroll text"); //$NON-NLS-1$
}
		
	}

	public void CloseDefault() {
		
		Scroller.cancel();
	}

	public boolean MAGNIFYABLE() {
		return false;
	}

	public String getSourceURL() {
		return null;
	}

	public int sourcesizeX() {
		return 0;
	}

	public int sourcesizeY() {
		return 0;
	}

	public void OpenDefault() {
		Scroller.scheduleRepeating(50);
		
	}
	public boolean DRAGABLE() {
		return true;
	}
	public boolean POPUPONCLICK() {
		return true;
	}

	public String POPUPTYPE() {
		return "TEXTSCROLL";
	}

	public void RecheckSize() {
		
	}

	@Override
	public String getState() {
		return null;
	}

	@Override
	public void loadState(String state) {
		
	}
	
	@Override
	public Object getVisualRepresentation() {
		return this.asWidget();
	}

	@Override
	public int getExpectedZIndex() {
		return -1;
	}
}
