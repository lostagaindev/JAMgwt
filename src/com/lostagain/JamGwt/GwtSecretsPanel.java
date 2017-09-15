package com.lostagain.JamGwt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.GwtLegacySupport.secretsPanelCore;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isPopupTypeImplementation;



public class GwtSecretsPanel extends secretsPanelCore implements hasCloseDefault,hasOpenDefault,isPopupTypeImplementation {
	public static Logger Log = Logger.getLogger("JAM");

	AbsolutePanel secretsPanelWidth = new AbsolutePanel();
	//--

	final static ArrayList<Hyperlink> secretitems = new ArrayList<Hyperlink>();
	int numofitems = 0;
	int angle_between_each = 0;
	double baseangle = 0;

	int centerx = 100;
	int centery = 100;
	int radius = 50;

	Timer update;

	public GwtSecretsPanel(){

		secretsPanelWidth.setSize("571px", "617px");
		secretsPanelWidth.setStylePrimaryName("secretsback");

		update = new Timer(){

			@Override
			public void run() {
				baseangle=baseangle+1;
				if (baseangle>360){
					baseangle=0;
				}
				updatePositions(baseangle);

			}

		};

		//update.scheduleRepeating(1000);

	}


	public void addLinkItem_impl(final String Name, 
						    final String Link){
		
		//first we make sure it doesn't already exist in the secret set
		//	Iterator<Hyperlink> it = secretitems.iterator(); 
		boolean addable = true;
		/*
		while (it.hasNext()){			
		 if (it.next().getText().compareTo(Name)==0){
			 addable = false;
			 break;

		 }
		}		*/
		//

		if (addable == true){
			numofitems=numofitems+1;
			Hyperlink newsecret = new Hyperlink(Name, "");		
			newsecret.setTitle(Link);
			newsecret.addClickHandler(gotoURL(Link));
			
			newsecret.setStylePrimaryName("button1");


			secretitems.add(newsecret);
			secretsPanelWidth.add(newsecret);

			angle_between_each=360/numofitems;
		}
		
	}


	public ClickHandler gotoURL(final String Link) {
		return new ClickHandler(){
			public void onClick(ClickEvent event) {
				Window.open(Link, "_blank","");
			}

		};
	}
	public void updatePositions(double from_this_angle){

		//center point
		centerx = (secretsPanelWidth.getOffsetWidth()/2)-15;
		centery = (secretsPanelWidth.getOffsetHeight()/2);	
		radius = (secretsPanelWidth.getOffsetWidth()/2)-35;

		Iterator<Hyperlink> it = secretitems.iterator(); 

		while (it.hasNext()){

			Widget item = it.next();

			//angle displacement from base
			int displace = (secretitems.indexOf(item))*angle_between_each;

			//work out x/y
			int newxpos = (int)Math.round(centerx + (Math.sin(  Math.toRadians(from_this_angle+displace) )*radius));
			int newypos = (int)Math.round(centery + (Math.cos(  Math.toRadians(from_this_angle+displace) )*radius));


			//get item
			secretsPanelWidth.setWidgetPosition(item, newxpos,newypos); 
		}



	}


	public void CloseDefault() {
		// we stop spinning
		update.cancel();

		JAM.SecretsPopupPanelButton.setPlayBack();
		JAM.SecretsPanelOpen = false;

	}


	public void OpenDefault() {
		// We start to spin...
		update.scheduleRepeating(45);
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
		return secretsPanelWidth.asWidget();
	}


	public void clear() {
		secretsPanelWidth.clear();
	}


	public Iterator<Widget> iterator() {
		return secretsPanelWidth.iterator();
	}
}
