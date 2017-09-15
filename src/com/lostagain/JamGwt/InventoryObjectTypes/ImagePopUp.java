package com.lostagain.JamGwt.InventoryObjectTypes;

import net.sphene.gwt.widgets.slider.Slider;

import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.hasCloseDefault;
import com.lostagain.Jam.Interfaces.hasOpenDefault;
import com.lostagain.Jam.Interfaces.hasTopBar;
import com.lostagain.Jam.Interfaces.PopupTypes.IsImagePopUpContents;
import com.lostagain.Jam.Interfaces.PopupTypes.IsInventoryItemPopupContent;
import com.lostagain.JamGwt.SpiffyImage;
//import com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces.isInventoryItemImplementation;

//TODO: make a IsImagePop interface for this, that inherits a few of these other interfaces as well as its own functions
public class ImagePopUp extends VerticalPanel implements
		hasCloseDefault,
		hasTopBar, 
		hasOpenDefault, //isInventoryItemImplementation,
		IsImagePopUpContents {
	
	final Slider slider = new Slider();

	//Current specs
	int currentOpacity = 50;

	//Requested size
	public int rsizeX = 0;	
	public int rsizeY = 0;
	
	
	//Description
	HTML Discription = new HTML("");
	
	private String ImageDiscription = "";
	
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

	// topBar NewTopBar = new topBar("",this);

	final Timer timer;
	int Original_picY = 0;
	int Original_picX = 0;

	private String imageSourceLocation;
	


	

	public ImagePopUp(String ImagelocationSet, String NewImageDiscription,int rsize_x,int rsize_y) {
		
		//Save requested sizes
		rsizeX=rsize_x;
		rsizeY=rsize_y;	
		//--
		
		super.setStylePrimaryName("unselectable");
		
		
		setImageDiscription(NewImageDiscription);

		System.out.print("\n /n <<< added listener-" + bigPicture.getHeight()
				+ "-");
		imageSourceLocation = ImagelocationSet;
		bigPicture.setUrl(imageSourceLocation);
		bigPicture.getElement().setAttribute("draggable", "false"); //ensure the image itself it cant be dragged using the browsers silly drag-image function
		
		slider.setValue(99);
		slider.setWidth("80%");
		if (slider.getOffsetWidth()>350){
			slider.setWidth("350px");
		}
		
		timer = new Timer() {
			@Override
			public void run() {
				// We check the size here, and fix if not correct. (for IE, as
				// IE dosnt work with LoadListener due to the cache)

				if (bigPicture.getWidth() > 5) {
					Original_picY = bigPicture.getOffsetHeight();
					Original_picX = bigPicture.getOffsetWidth();
					

					reSizePopUp();
					
					this.cancel();
				}

			}

		};

		timer.scheduleRepeating(1000);
		
		bigPicture.addErrorHandler(new ErrorHandler(){

			public void onError(ErrorEvent event) {
				System.out
				.print("\n /n <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<image failed "
						+ Original_picX + "-" + Original_picY);

			}
			
		});
		bigPicture.addLoadHandler(new LoadHandler() {


			public void onLoad(LoadEvent event) {
				
				Widget sender = (Widget)event.getSource();
				
				Original_picY = ((Image) sender).getOffsetHeight();
				Original_picX = ((Image) sender).getOffsetWidth();
				System.out.print("\n --Image loaded"
						+ ((Image) sender).getUrl());
				System.out
						.print("\n /n <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<image loaded "
								+ Original_picX + "-" + Original_picY);

				timer.cancel();


				//slider.setWidth(((int)Math.round(Original_picX*0.8))+"px");
				JAMcore.GameLogger.info(":"+Original_picX+":");
				
				// Sender.getParent().setSize(picX+"px",picY+"px"+100);
			}
		});
		// -----------------------

		ScreenSizeX = Window.getClientWidth();
		ScreenSizeY = Window.getClientHeight();
		// set standard image size
		this.setWidth((ScreenSizeX * 0.8) + "px");
		this.add(slider);
		if (slider.getOffsetWidth()>350){
			slider.setWidth("350px");
		}
		this.setCellHorizontalAlignment(slider, HasHorizontalAlignment.ALIGN_CENTER);


		slider.addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {

				currentOpacity = (int) slider.getValue();

				bigPicture.setOpacity(currentOpacity);

			}

		});

		this.add(bigPicture);

		// Discription.setHeight("100px");

		if (NewImageDiscription != "") {
			Discription.setHTML(NewImageDiscription);
			this.add(Discription);
			Discription.getElement().getParentElement().setClassName(
					"picturePopUp");
		}
		// slider.getElement().getParentElement().setAttribute("class",
		// "picturePopUp");
		slider.getElement().getParentElement().setClassName("picturePopUp");

		bigPicture.getElement().getParentElement().setClassName("popup_border");

		this.setCellHorizontalAlignment(Discription,
				HasHorizontalAlignment.ALIGN_CENTER);

	}

	protected void reSizePopUp() {
		// screen size
		ScreenSizeX = Window.getClientWidth();
		ScreenSizeY = Window.getClientHeight();

		// image loaded
		picY = Original_picY;
		picX = Original_picX;
		System.out.print("\n /n <<<<<<<<<<<<<<<<<<<<<<<image loaded []" + picX
				+ "-" + picY);

		// cancel timer
		if (Original_picX > 5) {
			timer.cancel();
		}

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

		// RootPanel.get().remove(bigPicture.getParent().getParent());
		// RootPanel.get().add(bigPicture.getParent().getParent(),
		// (ScreenSizeX/2)-(NewFrameWidth/2),posY );

		// adjust parents topbar size
		// ((PopUpWithShadow)(this.getParent())).TopBar.setWidth((this.getOffsetWidth()-80)+"px");

		// adjust fader size
		// MyApplication.fadeback.setSize("100%",900+"px");
		

		//slider.setWidth(((int)Math.round(this.getOffsetWidth()*0.8))+"px");
		JAMcore.GameLogger.info(":"+bigPicture.getOffsetWidth()+"-:");
		
		
		System.out.println("Screen Height="
				+ RootPanel.getBodyElement().getOffsetHeight());
	}

	public void RecheckSize() {

		if (getImageDiscription() != "") {
			this
					.setCellHeight(bigPicture, (bigPicture.getHeight() + 10)
							+ "px");
		}

		reSizePopUp();

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
		return "PICTURE";
	}
	public boolean POPUPONCLICK() {
		return true;
	}
	public void OpenDefault() {
	
		
	}

	public boolean DRAGABLE() {
		return true;
	}

	public boolean MAGNIFYABLE() {
		return true;
	}

	public String getSourceURL() {
		return imageSourceLocation;
	}

	public int sourcesizeX() {
		return rsizeX;
	}

	public int sourcesizeY() {
		return rsizeY;
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

	/**
	 * @return the imageDiscription
	 */
	public String getImageDiscription() {
		return ImageDiscription;
	}

	/**
	 * @param imageDiscription the imageDiscription to set
	 */
	public void setImageDiscription(String imageDiscription) {
		ImageDiscription = imageDiscription;
	}

	@Override
	public int getExpectedZIndex() {
		return -1;
	}
}
