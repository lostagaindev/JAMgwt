package com.lostagain.JamGwt;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.hasTopBar;
import com.lostagain.Jam.Interfaces.PopupTypes.IsPopupContents;
import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

/** an image popup with a magnified section. This requires external javascript to work called tjpzoom **/
public class ImageZoomPopUp extends VerticalPanel implements hasCloseDefault,
		hasTopBar, hasOpenDefault, isInventoryItemImplementation {

	static Logger Log = Logger.getLogger("JAM.imagezoomPopUp");
	
	VerticalPanel container = this;
	
	// Description
	HTML Discription = new HTML("");
	String ImageDiscription = "";
	
	// main image
	SpiffyImage bigPicture = new SpiffyImage();
	
	// screen size
	int ScreenSizeX = Window.getClientWidth();
	int ScreenSizeY = Window.getClientHeight();
	
	// pic size
	int picX = 0;
	int picY = 0;
	int posX = 0;
	int posY = 0;

	//requested size
	int rsizeX = 0;
	int rsizeY = 0;
	
	// topBar NewTopBar = new topBar("",this);

	int Original_picY = 0;
	int Original_picX = 0;

	String Imagelocation;
	String BigImagelocation="";

	Timer imagetester;
		
	
	public ImageZoomPopUp(String ImagelocationSet, String NewImageDiscription, int rsizex, int rsizey) {
		
		rsizeX = rsizex;
		rsizeY = rsizey;
		
		ImageDiscription = NewImageDiscription;

		Imagelocation = ImagelocationSet;
		
		

		// set standard image size
		this.setWidth((rsizeX) + "px");
		this.setHeight((rsizeY+30) + "px");
		
		//set overflow to hide
		this.setStyleName("overflowhide");
		
		//potential bigger image location set
		String biggerlocation_before = Imagelocation.substring(0,(Imagelocation.lastIndexOf("/")+1));
		String biggerlocation_after = Imagelocation.substring(1+(Imagelocation.lastIndexOf("/")));
		final String biggerlocation = biggerlocation_before + "highres_"+biggerlocation_after;
		
		Log.info("requesting image"+biggerlocation);
		
		//used to be "HEAD" rather then RequestBuilder.GET
		RequestBuilder biggerimagetester = new RequestBuilder(RequestBuilder.GET, biggerlocation) {
			
		};

			try {
				biggerimagetester.sendRequest("", new RequestCallback() {
			        public void onError(Request request, Throwable exception) {
			        	Log.info("no bigger image" + exception.getMessage());
			        }

			        public void onResponseReceived(Request request, Response response) {
			        	
			        	
			        	System.out.println("bigger image go..." + response.getStatusText());
			        	
			        	if ((response.getStatusText().compareTo("Not Found")==0)){
			        		Log.info("bigger image not found");
			        		BigImagelocation ="";
			        		//loadImage();
			        	} else {
			        		//we set the new location.
			        		
			        		BigImagelocation = "','"+biggerlocation;
			        		//Image.prefetch(Imagelocation);
			        		
			        		//loadImage();
			        	}
			        	
			        	//crude fix to force reload in zoom java...need to replace with unique number each time.
			        	JAM.zoomfixboolean = !(JAM.zoomfixboolean);
			        	if (JAM.zoomfixboolean){
			        	rsizeX=rsizeX+1;
			        	rsizeY=rsizeY+1;
			        	}
			        	//HTML zoomcode = new HTML("<div style=\"float:left\" onmouseover=\"zoom_on(event,"+rsizeX+","+rsizeY+",'"+"http://www.google.co.uk/intl/en_uk/images/logo.gif"+"');\" onmousemove=\"zoom_move(event);\" onmouseout=\"zoom_off();\"><img src=\""+"http://www.google.co.uk/intl/en_uk/images/logo.gif"+"\" alt=\"tjpzoom picture title\" style=\"padding:0;margin:0;border:0\" /></div><div style=\"clear:both;\"></div>");
			        	HTML zoomcode = new HTML("<div style=\"float:left\" onmouseover=\"zoom_on(event,"+rsizeX+","+rsizeY+",'"+Imagelocation+BigImagelocation+"');\" onmousemove=\"zoom_move(event);\" onmouseout=\"zoom_off();\"><img src=\""+Imagelocation+"\" alt=\"tjpzoom picture title\" style=\"padding:0;margin:0;border:0\" /></div><div style=\"clear:both;\"></div>");
			        	
			        	Log.info("Created Zoom Code:"+zoomcode);
			        	
			        	container.insert(zoomcode,0);
						//container.remove(zoomcode);
						//container.insert(zoomcode2,0);
					
						if (ImageDiscription != "") {
							container.setHeight((rsizeY+20) + "px");
							Discription.setHTML(ImageDiscription);
							} else {
							container.setHeight((rsizeY) + "px");
							}
			        	
			        }
			      });
			    } catch (RequestException ex) {
			    	System.out.println("no bigger image");
			    }
			
			
			
		
		
		
		
		
		
		
		
	


		if (ImageDiscription != "") {
			Discription.setHTML(" Loading....");
			this.add(Discription);
			Discription.setHeight("100%");
			Discription.getElement().getParentElement().setClassName(
					"picturePopUp");
		}
		
		//bigPicture.getElement().getParentElement().setClassName("popup_border");

		this.setCellHorizontalAlignment(Discription,
				HasHorizontalAlignment.ALIGN_CENTER);

	}

	public void loadImage() {
		

    	Log.info("running load image:");
    	
		bigPicture.setUrl(Imagelocation);
		
		container.add(bigPicture);
		bigPicture.setStyleName("overflowhide");
		
		
		
		
		
		bigPicture.addErrorHandler(new ErrorHandler() {			
			@Override
			public void onError(ErrorEvent event) {
				imagetester.cancel();
			}
		});
		
		bigPicture.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {			

				JAMcore.GameLogger.info("\n 3width is"+bigPicture.getWidth());

				JAMcore.GameLogger.info("\n size to is"+rsizeX);
				
				imagetester.cancel();
				// replace image with HTML now
				HTML zoomcode = new HTML("<div style=\"float:left\" onmouseover=\"zoom_on(event,"+rsizeX+","+rsizeY+",'"+Imagelocation+BigImagelocation+"');\" onmousemove=\"zoom_move(event);\" onmouseout=\"zoom_off();\"><img src=\""+Imagelocation+"\" alt=\"tjpzoom picture title\" style=\"padding:0;margin:0;border:0\" /></div><div style=\"clear:both;\"></div>");
				
				
				container.insert(zoomcode,0);
				bigPicture.removeFromParent();
				
				container.setWidth((rsizeX) + "px");
				if (ImageDiscription != "") {
				container.setHeight((rsizeY+20) + "px");
				Discription.setHTML(ImageDiscription);
				} else {
				container.setHeight((rsizeY) + "px");
				}
				
				//reccenter

			     //((PopUpWithShadow)container.getParent().getParent()).setPopupPosition((ScreenSizeX-rsizeX)/2, (ScreenSizeY-rsizeY)/2);
			     
			     
			}
		
		
		});
		
		// use this timer for IE/check its loaded already.
		imagetester = new Timer() {
			@Override
			public void run() {
				// We check the size here, and fix if not correct. (for IE, as
				// IE dosnt work with LoadListener due to the cache)
				JAMcore.GameLogger.info("\n width is-"+bigPicture.getWidth());
				if (bigPicture.getWidth() > 15) {
					JAMcore.GameLogger.info("\n shrunk");
					imagetester.cancel();
					// replace image with HTML now
					HTML zoomcode = new HTML("<div style=\"float:left\" onmouseover=\"zoom_on(event,"+rsizeX+","+rsizeY+",'"+Imagelocation+BigImagelocation+"');\" onmousemove=\"zoom_move(event);\" onmouseout=\"zoom_off();\"><img src=\""+Imagelocation+"\" alt=\"tjpzoom picture title\" style=\"padding:0;margin:0;border:0\" /></div><div style=\"clear:both;\"></div>");
					container.insert(zoomcode,0);
					bigPicture.removeFromParent();
					container.setWidth((rsizeX) + "px");
					if (ImageDiscription != "") {
					container.setHeight((rsizeY+20) + "px");
					Discription.setHTML(ImageDiscription);
					} else {
					container.setHeight((rsizeY) + "px");
					}
					
					

					
					
					//reccenter

				     ((TitledPopUpWithShadow)container.getParent().getParent()).setPopupPosition((ScreenSizeX-rsizeX)/2, (ScreenSizeY-rsizeY)/2);
				     
				}

			}

		};

    	Log.info("running imagetester scheduleRepeating:");
		imagetester.scheduleRepeating(1000);
		

		
		
		
		
	}

	protected void reSizePopUp() {
		/*
		// screen size
		ScreenSizeX = Window.getClientWidth();
		ScreenSizeY = Window.getClientHeight();

		// image loaded
		picY = Original_picY;
		picX = Original_picX;
		System.out.print("\n /n <<<<<<<<<<<<<<<<<<<<<<<image loaded []" + picX
				+ "-" + picY);

		

		// Resize the picture if its over 80% of the screen size

		if (picX > (ScreenSizeX * 0.8)) {

			bigPicture.setHeight(picY * ((ScreenSizeX * 0.8) / picX) + "px");

			System.out.println("ratio=" + picX + ">" + (ScreenSizeX * 0.8));
			picX = (int) Math.round(ScreenSizeX * 0.8);
			bigPicture.setWidth(picX + "px");

		} else if (Original_picX > 5) {

			bigPicture.setWidth(Original_picX + "px");
			bigPicture.setHeight(Original_picY + "px");
		}
		
		
		
		// then match the frame to fit

		Discription.setHeight("100%");
		int NewFrameWidth = (bigPicture.getOffsetWidth() + 0);
		bigPicture.getParent().setWidth(NewFrameWidth + "px");
		this.setCellHeight(bigPicture, bigPicture.getHeight() + "px");
		int NewFrameHeight = getOffsetHeight();

		// and the shadow container
		// bigPicture.getParent().getParent().setWidth(20+NewFrameWidth+"px");

		// re center

		// test it isnt bigger then the screen.
		if (NewFrameHeight > ScreenSizeY) {
			posY = ((ScreenSizeY / 2) - (NewFrameHeight / 2))
					+ ((NewFrameHeight - ScreenSizeY) / 2);

		} else {
			posY = (ScreenSizeY / 2) - (NewFrameHeight / 2);
		}

		// --
		((hasCloseDefault) (bigPicture.getParent())).CloseDefault();
		((hasOpenDefault) (bigPicture.getParent())).OpenDefault();

		//this.OpenDefault();
		
		//fix zdepth again
		((PopUpWithShadow)container.getParent().getParent()).fixedZdepth(1800);
		
		
		// RootPanel.get().remove(bigPicture.getParent().getParent());
		// RootPanel.get().add(bigPicture.getParent().getParent(),
		// (ScreenSizeX/2)-(NewFrameWidth/2),posY );

		// adjust parents topbar size
		// ((PopUpWithShadow)(this.getParent())).TopBar.setWidth((this.getOffsetWidth()-80)+"px");

		// adjust fader size
		// MyApplication.fadeback.setSize("100%",900+"px");
		System.out.println("Screen Height="
				+ RootPanel.getBodyElement().getOffsetHeight());
		

		//set_zoomer(NewFrameWidth,NewFrameHeight);
		 * 
		 */
	}

	public void RecheckSize() {
/*
		if (ImageDiscription != "") {
			this
					.setCellHeight(bigPicture, (bigPicture.getHeight() + 10)
							+ "px");
		}

		reSizePopUp();
*/
	}

	public void HideTopBar() {
		// this.NewTopBar.setVisible(false);

	}

	public void ShowTopBar() {
		// this.NewTopBar.setVisible(true);

	}

	public void CloseDefault() {

	}

	public String POPUPTYPE() {
		return "MAGNIFYINGGLASS";
	}
	public boolean POPUPONCLICK() {
		return false;
	}
	public boolean DRAGABLE() {
		return false;
	}
	public void OpenDefault() {
		// has to have grey back, and be set to fixed zdepth
		 RootPanel.get().add(JAM.fadeback,0, 0);
       //ControlPanel.ShowDefault();
     DOM.setStyleAttribute(JAM.fadeback.getElement(), "zIndex", ""+(JAMcore.z_depth_max+1));
     DOM.setStyleAttribute(JAM.fadeback.getElement(), "z-index", ""+(JAMcore.z_depth_max+1));
     JAMcore.z_depth_max = JAMcore.z_depth_max+1;
    /*
     if (bigPicture.getWidth() > 15) {

			MyApplication.DebugWindow.addText("\n 2width is"+bigPicture.getWidth());
			//imagetester.cancel();
			// replace image with HTML now
			HTML zoomcode = new HTML("<div style=\"float:left\" onmouseover=\"zoom_on(event,"+rsizeX+","+rsizeY+",'"+Imagelocation+"');\" onmousemove=\"zoom_move(event);\" onmouseout=\"zoom_off();\"><img src=\""+Imagelocation+"\" alt=\"tjpzoom picture title\" style=\"padding:0;margin:0;border:0\" /></div><div style=\"clear:both;\"></div>");
			container.insert(zoomcode,0);
			bigPicture.removeFromParent();
			container.setWidth((rsizeX) + "px");
			if (ImageDiscription != "") {
			container.setHeight((rsizeY+20) + "px");
			} else {
			container.setHeight((rsizeY) + "px");
			}
			
			//reccenter

		    // ((PopUpWithShadow)container.getParent().getParent()).setPopupPosition((ScreenSizeX-rsizeX)/2, (ScreenSizeY-rsizeY)/2);
		     
		}
     */
	}

	
	//---
	//------
	//Not all of the following needs to be implemented, but needs to be here to maintain compatibility with
	//other popup item types
	//please check the implementations elsewhere for discriptions of what they are for, in case
	//I forget to add them to the interface
	public boolean MAGNIFYABLE() {
		return false;
	}

	@Override
	public String getSourceURL() {
		return null;
	}

	@Override
	public int sourcesizeX() {
		return 0;
	}

	@Override
	public int sourcesizeY() {
		return 0;
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
