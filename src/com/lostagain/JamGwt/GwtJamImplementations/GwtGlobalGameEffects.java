package com.lostagain.JamGwt.GwtJamImplementations;

import java.util.HashMap;
import java.util.logging.Logger;


import com.google.gwt.dev.util.collect.Maps;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.lostagain.Jam.JAMTimerController;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.JamGlobalGameEffects;
import com.lostagain.Jam.RequiredImplementations;
import com.lostagain.Jam.InstructionProcessing.CommandList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;
import com.lostagain.JamGwt.JAM;
import com.lostagain.JamGwt.IconPacks.JamImages;

import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyAnimatedIcon.SpriteRenderMode;

/**
 * Triggers effects that change the look of the whole game screen
 * Effects might be a tempory, or persistent till they are turned off. 
 * 
 * @author darkflame *
 */
public class GwtGlobalGameEffects extends  JamGlobalGameEffects   {

	public static Logger Log = Logger.getLogger("JamGdx.GwtGlobalGameEffects");

	/**
	 * 
	 */
	public void turnHackedEffectOffImplementation_impl(int duration) {

		//int duration = Integer.parseInt(optionalExtraSettings);		
		ensureOverlayPresent();

		// make sure it isn't just resetting the background
		// this is a crude fix specific to the cuypers code. Remove in
		// later games

		if (JAM.CurrentBackground.endsWith("hackedback.gif")) {
			JAM.CurrentBackground = "GameTextures/CCmonMCdark.jpg";
		}

		//CommandList setBackground = new CommandList();

		//	JAM.EffectOverlay.ProcessThisAfter(" - SetBackgroundImage = " + JAM.CurrentBackground + " \n - SetClockMode = normal \n"); //$NON-NLS-1$

		CommandList setBackground = new CommandList(" - SetBackgroundImage = " + JAM.CurrentBackground + " \n - SetClockMode = normal \n");		
		JAM.EffectOverlay.ProcessThisAfter(setBackground); //$NON-NLS-1$



		JAM.EffectOverlay.Hacked_in(duration);
		JAM.EffectOverlay.Hacked_out(duration);
		JAM.Feedback.setDirection(Direction.LTR);

		((HTML) (JAM.messagehistory.MessageHistoryScroller
				.getWidget())).setDirection(Direction.LTR);

		// RootPanel.getBodyElement().setAttribute("background",
		// "");

		// gwt_clock.setModeNormal();
		// set feet hacked
		JAM.StatueFeat
		.setURL("GameIcons/" + JAMcore.iconsizestring
				+ "/" + "CCfeetb.png", 1);

		// make normal soldier

		if (JAMcore.iconsizestring.equals("SMALL")) {
			JAM.solider.setFrames(JamImages.SoldierSmall);
		} else if (JAMcore.iconsizestring.equals("MEDIUM")) {
			JAM.solider.setFrames(JamImages.SoldierMid);
		} else {
			JAM.solider.setFrames(JamImages.SoldierBig);
		}
		JAM.solider.mode = SpriteRenderMode.bundle;

		JAM.Blinking.cancel();
		JAM.Cyclonish.cancel();
	}

	private void ensureOverlayPresent() {
		if (JAM.EffectOverlay.isAttached() == false) {
			RequiredImplementations.PositionByCoOrdinates(JAM.EffectOverlay, 0, 0,0);

			
			JAM.EffectOverlay.setSize("100%", "100%");
		}
	}

	/**
	 * optionalExtraSettings is transition time in ms
	 */
	public void turnHackedEffectOnImplementation_impl(int duration) {

		/*
		int duration = 1000;
		if (optionalExtraSettings.isEmpty()){
			duration = Integer.parseInt(optionalExtraSettings);
		}
*/
		
		
		ensureOverlayPresent();

		// add second advert. Cuyperbix its archtechral-ishious!
		//JAM.EffectOverlay.ProcessThisAfter(" - SetBackgroundImage = GameTextures/hackedback.gif \n - SetClockMode = fast \n - PopUpAdvert = advert/Advert1.jpg \n - PopUpAdvert = advert/Advert2.jpg \n"); //$NON-NLS-1$
		CommandList setBackground = new CommandList(" - SetBackgroundImage = GameTextures/hackedback.gif \n - SetClockMode = fast \n - PopUpAdvert = advert/Advert1.jpg \n - PopUpAdvert = advert/Advert2.jpg \n"); //$NON-NLS-1$
		JAM.EffectOverlay.ProcessThisAfter(setBackground);

		JAM.EffectOverlay.Hacked_in(duration);
		JAM.EffectOverlay.Hacked_out(duration);
		JAM.Feedback.setDirection(Direction.RTL);

		((HTML) (JAM.messagehistory.MessageHistoryScroller
				.getWidget())).setDirection(Direction.RTL);

		JAM.StatueHead.setURL("GameIcons/"
				+ JAMcore.iconsizestring + "/" + "ladyclocklonger0.png",
				3);

		//JAM.Blinking = new Timer() {

		//	@Override
		//	public void run() {
		//		JAM.StatueHead.setPlayForwardThenBack();

		//	}

		//};

		JAM.Blinking = JAMTimerController.getNewTimerClass(new Runnable() {

			@Override
			public void run() {
				JAM.StatueHead.setPlayForwardThenBack();

			}
		});


		JAM.Blinking.scheduleRepeating(5000);
		// set feet hacked
		JAM.StatueFeat.setURL("GameIcons/"
				+ JAMcore.iconsizestring + "/" + "CCfeethacked0.png", 1);

		// make cylon soldier

		JAM.solider.setURL("GameIcons/"
				+ JAMcore.iconsizestring + "/" + "CCsoldier0.png", 5);
		JAM.solider.mode = SpriteRenderMode.bundle;

		//JAM.Cyclonish = new Timer() {

		//	@Override
		//	public void run() {
		//		JAM.solider.setPlayForwardThenBack();

		//	}

		//};

		JAM.Cyclonish = JAMTimerController.getNewTimerClass(new Runnable() {			
			@Override
			public void run() {
				JAM.solider.setPlayForwardThenBack();
			}
		});

		JAM.Cyclonish.scheduleRepeating(5000);
	}



	public void triggerClearAllEffects_impl(CommandList instructionSet) {
		ensureOverlayPresent();
		Log.info(" clear triggered. Commands after this might not run though, as clear doesnt yet support commands run after...I think");
		
		
		JAM.EffectOverlay.Clear();
	}

	public void triggerHackedEffect_impl(double duration, final CommandList runAfter) {
		Log.info(" hacked triggered. Commands after this might not run though, as Hackedin/out doesnt yet support commands run after...I think");

		ensureOverlayPresent();
		JAM.EffectOverlay.ProcessThisAfter(runAfter); //newly added. maybe just run commands straight away?
		JAM.EffectOverlay.Hacked_in(duration);
		JAM.EffectOverlay.Hacked_out(duration);
		//	
	}

	public void triggerFadeInEffect_Impl(double duration, String backgroundColor, String backgroundImage,
			boolean centerImage, final CommandList RunThisAfter) {
		
		Log.info(" fadeout triggering: "+duration+","+backgroundColor+","+backgroundImage+":"+centerImage);

		ensureOverlayPresent();
		Log.info("instructions after fade in;" + RunThisAfter.getCode());
		
		// set instructions to trigger after fade is done
		//JAM.EffectOverlay.ProcessThisAfter(RunThisAfter); //old method
		JAM.EffectOverlay.FadeIn(duration,backgroundColor,backgroundImage,centerImage,RunThisAfter);
		
		// set instructions for after fadeout
		//int InstructionSetLoc = InstructionSet.indexOf("FadeIn", 0) + 6;
		//String afterFadeInstructions = InstructionSet.substring(
		//		InstructionSetLoc).trim();

		// now we cancel the rest because EffectOverlay will retrigger it
		// after it fades out
		//JAM.endInstructions = true;
		//
		Log.info("- Fadeing in -");
	}

	public void triggerFadeOutEffect_impl(double duration, String backgroundColor, String backgroundImage,
			boolean centerImage, final CommandList runTheseAfter) {
		
		ensureOverlayPresent();
		// set instructions to trigger after fade is done
		//JAM.EffectOverlay.ProcessThisAfter(runTheseAfter);	//old method 
		
		JAM.EffectOverlay.FadeOut(duration,backgroundColor,backgroundImage,centerImage,runTheseAfter);
		
		// now we cancel the rest because EffectOverlay will retrigger it
		// after it fades out
		//JAM.endInstructions = true;
		//
		Log.info("- Fadeing out -");
	}

	public void triggerFlashEffect_impl(double duration, String backgroundColor, final CommandList runThisAfter) {
		ensureOverlayPresent();
		JAM.EffectOverlay.ProcessThisAfter(runThisAfter);	
		JAM.EffectOverlay.Flash(duration,backgroundColor); //couild add image here?
		//JAM.EffectOverlay.Flash(duration); //we used to double flash
		Log.info("- Flash done -");
	}

	@Override
	public void setClockModeImplementation(String currentProperty) {

		if (currentProperty.compareTo("fast") == 0) { //$NON-NLS-1$
			JAM.gwt_clock.setModeFast();
		} else {
			JAM.gwt_clock.setModeNormal();
		}
	}
	
	
//
	HashMap<String,SimplePanel> overlayPanels = new HashMap<String,SimplePanel>();
	
	
	@Override
	public void turnScreenOverlayEffectOnImplementation_impl(String ID, String colour, String image, int durationms) {
		ensureOverlayPresent();
		
		//first see if there's a existing one to change
		if (overlayPanels.containsKey(ID)){

			SimplePanel overlayPanel = overlayPanels.get(ID);
			JAM.EffectOverlay.adjustOverlayPanel(overlayPanel, colour, image,durationms);
			
		} else {
		
				//if new
			SimplePanel overlayPanel = JAM.EffectOverlay.addOverlayPanel(colour, image,durationms);
			overlayPanels.put(ID, overlayPanel);
		
		}
	}

	@Override
	public void turnScreenOverlayEffectOffImplementation_impl(String ID,int duration) {
		
		
		SimplePanel overlayPanelToRemove = overlayPanels.get(ID);
		if (overlayPanelToRemove!=null){

		//	ensureOverlayPresent();
			JAM.EffectOverlay.removeOverlayPanel(overlayPanelToRemove,duration);
			overlayPanels.remove(ID);
		}
		
	}

	@Override
	public void triggerClearAllQueuedCommands_impl(final CommandList runTheseAfterClear) {
		
		JAM.EffectOverlay.ClearAllQueuedCommands();
		InstructionProcessor.processInstructions(runTheseAfterClear, "sfx_afterclearallqueuedcmds", null);
		
	}






}
