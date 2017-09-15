package com.lostagain.JamGwt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.FeedbackHistoryCore;
import com.lostagain.Jam.JAMcore;

/**
 * Maintains a history of messages - designed for the games feedback window
 * to let the user browser backwards if needed 
 ***/
public class FeedbackHistoryVisualiser extends FeedbackHistoryCore implements IsWidget {

	// extends VerticalPanel
	VerticalPanel visualContents = new VerticalPanel();

	//container scroller
	public ScrollPanel MessageHistoryScroller = new ScrollPanel();
	//internal HTML
	public HTML messageslist = new HTML("Message Log;");


	final static Image printicon = new Image("GameIcons/Printer.png");


	/**maintains a history of messages - designed for the games feedback window
	 * to let the user browser backwards if needed */
	public FeedbackHistoryVisualiser(){

		//tell our superclass we are the visual representation of the history
		FeedbackHistoryCore.setFeebackHistoryVisualiser(this);

		//setup
		MessageHistoryScroller.add(messageslist);
		visualContents.add(printicon);
		visualContents.setCellHorizontalAlignment(printicon,HasHorizontalAlignment.ALIGN_RIGHT);
		visualContents.add(MessageHistoryScroller);
		MessageHistoryScroller.setSize("100%", "281px");


		//add print function
		printicon.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {

				//openWindow("scripts/printthis.php"+"?text="+messageslist.getText());
				JAMcore.GameLogger.info("triggering print  window");
				//popupWindowWithString(" test string  123 ");

				// TODO change to filemanager based
				RequestBuilder printthis = new RequestBuilder(RequestBuilder.POST,"Login_System/UpdateHistoryTemp.php");
				try {
					printthis.sendRequest("MessageHistory=" + JAM.messagehistory.getHTML(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							FeedbackHistoryCore.AddNewMessage("Failed to Open new Print Window");
						}

						public void onResponseReceived(Request request,
								Response response) {


							Window.open("scripts/displayhistory.php", "_blank", "");

						}

					});

				}catch (RequestException ex) {
					System.out.println(" cant check login, assume new user");
				}
			}

		});

	}
	native void openWindow(String param) /*-{ 
    $wnd.open('scripts/printthis.php' + '?text=' +param, '_blank', null); 
    popup.getElementById('idShowText').innerHTML();
    return true;
}-*/;

	public String getText(){
		return messageslist.getText();
	}
	public String getHTML(){
		return messageslist.getHTML();
	}

	//	public void AddNewMessage(String newMessage){
	//
	//		messagehistory.add(newMessage);
	//		
	//		messageslist.setHTML( messageslist.getHTML()+"<br>"+newMessage );
	//		
	//		currentmessage=messagehistory.size();
	//
	//	}

	public  void AddNewMessage_notrecorded(String newMessage){

		//for adding a message to the box, but not the history array		
		messageslist.setHTML( messageslist.getHTML()+"<br>"+newMessage );

	}
	public void scrolltobottom(){
		JAMcore.GameLogger.info("scrolldown");
		MessageHistoryScroller.scrollToBottom();

	}

	public final native void popupWindowWithString(String name) /*-{
	    var popup = $wnd.open('scripts/printthis.php', '_blank', null);
		popup.document.getElementById('idShowText').innerHTML="someText";

	    return;
	  }-*/;

	@Override
	public Widget asWidget() {
		return visualContents;
	}


	@Override
	public void addToVisualRepresentationOfHistory(String addThis) {
		messageslist.setHTML( messageslist.getHTML()+"<br>"+addThis );


	}
}
