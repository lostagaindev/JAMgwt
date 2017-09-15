package com.lostagain.JamGwt.GwtJamImplementations;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.Interfaces.hasInventoryButtonFunctionality;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.Sprites.InternalAnimations;

import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyAnimatedIcon;

/**
 * A version of SpiffyAnimatedIcon for the JAM engine.
 * Differences
 * - at the moment is the auto-selection of Flipbook mode if working locally.
 * - Support for runnable click handlers via hasInventoryButtonFunctionality
 * 
 * NOTE: FLIPMODE DOES NOT CORRECTLY DETECT WHEN LOADED
 * 
 * @author Tom
 * 
 */
public class GWTAnimatedIcon extends SpiffyAnimatedIcon implements hasInventoryButtonFunctionality {

	static Logger Log = Logger.getLogger("JAM.GWTAnimatedIcon");
	
	/**
	 * placeholder icon only. needs to be initialized with either a Abstract image prototype, or a file name, before anything will display
	 * DO NOT ADD TO A PANEL BEFORE running initialize
	 */
	public GWTAnimatedIcon() {
		super();
	}
	
	public GWTAnimatedIcon(AbstractImagePrototype[] SetFrames, String name) {
		super(SetFrames, name);
	}

	public GWTAnimatedIcon(AbstractImagePrototype[] SetFrames) {
		super(SetFrames);
	}

	public GWTAnimatedIcon(String FileZeroLocation, int NumOfFrames,
			boolean flipbook) {		
		super(FileZeroLocation, NumOfFrames, flipbook);
	}

	public GWTAnimatedIcon(String FileZeroLocation, int NumOfFrames) {
		super();
		intialise(FileZeroLocation, NumOfFrames);
	}

	public void intialise(String FileZeroLocation, int NumOfFrames) {
		if (NumOfFrames == 1){
			Log.info("static image mode used for sprite");
			mode = SpriteRenderMode.staticimage;
			staticImageSetup(FileZeroLocation);
			return;
		} else 	{		
			
			//manual setting
			String animatedSpriteMode = BasicGameInformationImp.staticGetAnimatedSpriteMode();
			if (animatedSpriteMode.equalsIgnoreCase("flipbook")){
				
				flipbookUrlBasedSetup(FileZeroLocation, NumOfFrames);	
				
			} else if (animatedSpriteMode.equalsIgnoreCase("standard")) {
				
				standardUrlBasedSetup(FileZeroLocation, NumOfFrames);
				
			} else {
				//assume default based on if the game is running on local mode or not
				if (BasicGameInformationImp.staticGetLocalFolderLocation().length()>3){
					
					flipbookUrlBasedSetup(FileZeroLocation, NumOfFrames);
					
				} else {
					
					standardUrlBasedSetup(FileZeroLocation, NumOfFrames);
					
				}		
			}
			
			
			
			
		}
	}

	public void intialise(AbstractImagePrototype[] SetFrames, String name) {
		bundledBasedSetup(SetFrames, name);
	}

	//extra things needed to ensure this can also be used for inventory buttons
	@Override
	public int getFrameTotal() {
		return getLastFrameNumber();
	}

	@Override
	public void removeFromInterface() {
		removeFromParent();		
	}

	//Handlers;
	@Override
	public void addClickRunnable(final Runnable onClick) {
		
		HandlerRegistration clickHandler = this.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				 onClick.run();
			}
		});
		
		
	}

	@Override
	public void addMouseOverRunnable(final Runnable onMouseOver) {
		final HandlerRegistration mouseOverHandler = this.addMouseOverHandler(new MouseOverHandler() {			
						@Override
			public void onMouseOver(MouseOverEvent event) {
							onMouseOver.run();
			}
		});
		
	}

	
	@Override
	public void addMouseOutRunnable(final Runnable onMouseOut) {
		final HandlerRegistration mouseoutHandler = this.addMouseOutHandler(new MouseOutHandler() {			
			@Override
				public void onMouseOut(MouseOutEvent event) {
				onMouseOut.run();
}
});
	}
	
	

	public void setInventoryIconTo(String location,	int iconsframes) {
		// - SetInventoryIcon =  magrietbag0.png,6

		
		// if its Margriet, we can use the preloaded ones;
		//Specific coding for the legacy cuypers code gwt game;
		if (location.startsWith("magrietbag")) {

			// update image sets
			JAM.BigInventoryImages    = InternalAnimations.BigMarInventoryImages;
			JAM.MediumInventoryImages = InternalAnimations.MediumMarInventoryImages;
			JAM.SmallInventoryImages  = InternalAnimations.SmallMarInventoryImages;

			switch (JAMcore.iconsizestring) {
			case "BIG":
				setFrames(JAM.BigInventoryImages);
				break;
			case "MEDIUM":
				setFrames(JAM.MediumInventoryImages);
				break;
			case "SMALL":
				setFrames(JAM.SmallInventoryImages);
				break;
			default:
				setFrames(JAM.BigInventoryImages);
				break;
			}

		} else {

			// else we load from the url
			if (location.length() > 5) {
				
				//int iconframes = Integer.parseInt(CurrentProperty
				//		.split(",")[1]); //$NON-NLS-1$
				
				setURL("GameIcons/"
						+ JAMcore.iconsizestring + "/" + location,
						iconsframes);
				
			//	JAM.DebugWindow.addText("-seticon-" + iconlocsii);
				Log.info("- setting inventory icon too:"+location);
				
			}
		}
	}

	public String getMode() {		
		return mode.name();
	}

	/**
	 * Disables the post animation commands for the next time they fire only
	 ***/
	public void disableNextPostAnimationCommands(boolean disable) {
		super.disableNextPostAnimationCommands(disable);
		
	}

	
	
	

}
