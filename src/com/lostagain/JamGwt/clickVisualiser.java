package com.lostagain.JamGwt;
//import java.util.TimerTask;

//import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
//import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.lostagain.Jam.JAMTimerController;
import com.lostagain.Jam.Factorys.IsTimerObject;
import com.lostagain.JamGwt.GwtJamImplementations.GWTAnimatedIcon;

/** a simple 3-frame popup to visualize the mouse clicks  **/
public class clickVisualiser extends PopupPanel {
	
	
	static AbstractImagePrototype[] clickFeedbackImage = {
		AbstractImagePrototype.create(JAM.JargStaticImages.clickVisualiser0()),
		AbstractImagePrototype.create(JAM.JargStaticImages.clickVisualiser1()),
		AbstractImagePrototype.create(JAM.JargStaticImages.clickVisualiser2())
	};
	
	static public GWTAnimatedIcon clickVisualIcon = new GWTAnimatedIcon(clickFeedbackImage,"clickVisualiser");
	
	clickVisualiser pthis = this;
	IsTimerObject lastFrameTimer;

	/** A small little circle thing for visualizing clicks **/
	static clickVisualiser tinything = new clickVisualiser();

	//should ensure JAMTimerController is setup first
	//also move to feedback images own class for neatness.
	static IsTimerObject addClickFeedbackImage = JAMTimerController.getNewTimerClass(new Runnable(){
		@Override
		public void run() {			
			clickVisualiser.tinything.show();			
			//(it auto removes when it completes its animation)	
		}		
	});
	
	
	/** a simple 3-framepopup to visualize the mouse clicks  **/
	public clickVisualiser() {

		super();
		
		//set to first frame
		clickVisualIcon.firstFrame();
		
		//lastFrameTimer = new Timer(){
		//	@Override
		//	public void run() {
		//		pthis.hide();
		//	}					
		//};
		lastFrameTimer = JAMTimerController.getNewTimerClass(new Runnable() {			
			@Override
			public void run() {
				pthis.hide();
			}
		});
		
		
		this.setWidget(clickVisualIcon);		
		this.setStyleName("circleThingy");
		this.addStyleName("unselectable");		
		this.getElement().getStyle().setZIndex(999999);	
		
		
		clickVisualIcon.setFrameGap(70);
		clickVisualIcon.setCommandToRunAfterOpen(new Runnable() {			
			@Override
			public void run() {
				//hide after the last frame and a small delay
				lastFrameTimer.schedule(70);
			
		
				
			}
		});
	}
	
	@Override
	public void onLoad(){
		super.onLoad();
		//set to first frame
		clickVisualIcon.firstFrame();
		//start animation forward
		clickVisualIcon.setPlayForward();
		
		
	}

	public static void showClickVisauliserImplementation(int x, int y) {
	
		clickVisualiser.tinything.setPopupPosition(x-25, y-25);			
		//add timer to remove
		clickVisualiser.addClickFeedbackImage.schedule(20);
		
	}


}
