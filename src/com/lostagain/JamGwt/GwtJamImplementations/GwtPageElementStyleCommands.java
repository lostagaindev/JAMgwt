package com.lostagain.JamGwt.GwtJamImplementations;

import java.util.logging.Logger;


import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.lostagain.Jam.InstructionProcessing.HasPageElementStyleCommands;
import com.lostagain.JamGwt.GWTJAMTimerController;
import com.lostagain.JamGwt.JAM;

import lostagain.nl.spiffyresources.client.spiffycore.DeltaTimerController.DeltaRunnable;


/**
 * implementation of gwt specific commends (ie, html related stuff)
 * A lot of this can be made more tidy/efficiant
 * 
 * @author darkflame
 *
 */
public class GwtPageElementStyleCommands implements HasPageElementStyleCommands {

	static Logger Log = Logger.getLogger("JAM.GwtPageElementStyleCommands");
	
	
	@Override
	public boolean addCSSClassToDomElement(String Elementac, String Classac) {
		
		Element elementToSetac = DOM.getElementById(Elementac);

		if (elementToSetac != null) {
			RootPanel.get(Elementac).getElement().addClassName(Classac);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This will fade in, and eventually hide a preexisting div with the id you specify
	 * The original idea was this div will be used for a loading message while waiting for the rest of the game to load.
	 * Now it can be used for fading any div in and out, potentially used for a games perminate interface elements.
	 * 
	 * Warning: Do not trigger a fadein/out on a element currently fading in/out already. Be sure to use a timed action set
	 * to wait long enough for any existing fades to finnish
	 * 
	 * returns true if element was found to fade
	 * **/
	public boolean fadeInHTMLElement(final String elementID,int FadeInOverInMs) {
		
		
		final Element elementToFade = DOM.getElementById(elementID.trim());
		
		//ensure element exists
		if (elementToFade==null){
			return false;
		}
	
		//remove any style hidden
		elementToFade.getStyle().clearDisplay();
	
		Style style = elementToFade.getStyle();
	
		int startingOpacity=100;
	
		//if theres an existing opacity we start from that one
		if (style!=null)
		{
			if (style.getOpacity()!=null){
	
				//if its a valid number we use it here.
				try {
					startingOpacity = Math.round(Float.parseFloat(style.getOpacity())*100);  
				} catch (Exception e) {
					startingOpacity=100;
				}
	
			}
	
		}
	
		String runnablesID = "Fading_"+elementID;
		
		//ensure theres no existing runnable for this element to fade, if there is cancel it
		DeltaRunnable previousRunnable = GWTJAMTimerController.getNamedRunnable(runnablesID);
		if (previousRunnable!=null){
			JAM.GwtLog.info("canceled previous runnable");
			previousRunnable.cancel();
		}
	
		JAM.GwtLog.info("fade in overlay in with ID:"+elementID+" over "+FadeInOverInMs);
	
	
		//first we get the amount to increase each interval
		int totalIntervals = FadeInOverInMs / 100;
		int opacityStep = (100-startingOpacity) /  totalIntervals;
	
		//check step is at least 1
		if (opacityStep<1){
			opacityStep = 1;
		}
	
	
		//Ensure the element is on the changed div list 
		//This list is so the save functions know what element states need to save
		//if (!elementID.equalsIgnoreCase("loadingdiv")){
		//	ChangedHtmlPageElement.add(elementID); //we only add if not loadingdiv, as thats controlled seperately
		//}
		
	
		
		//new system
		//new delta based system
	
		final double finalstartingOpacity = startingOpacity;
		final double OpacityStepPerMS = 100.0/FadeInOverInMs;
		
		
		final DeltaRunnable deltaFadeUpdate = new DeltaRunnable(){
			private double backopacity = finalstartingOpacity;
			Style style = elementToFade.getStyle();
			@Override
			public void update(float delta) {
				//work out new opacity based on time since last update
				//opacity step x ms since last update = amount we change this time
				double changeAmount = OpacityStepPerMS * delta;
				
				
				backopacity = backopacity + changeAmount;
	
				if (backopacity > 100) {	
					style.setProperty("filter", "alpha(opacity=100)");
					style.setProperty("opacity", "1");
					this.cancel();			
					return;
				}
	
				//both for IE..for some reason
				style.setProperty("filter", "alpha(opacity=" + backopacity+ ")");
				style.setProperty("opacity", "" + (backopacity / 100));
	
			}
			
		};		
		//deltaFadeUpdate
		deltaFadeUpdate.setRunnablesName(runnablesID);
		
		GWTJAMTimerController.addObjectToUpdateOnFrame(deltaFadeUpdate);
		
		
	
		return true;
	}

	/**This will fade out, and eventually hide a preexisting div with the id you specify
	//The idea is this div will be used for a loading message while waiting for the rest of the game to load.**/
	public boolean fadeOutHTMLElement(final String elementID,int FadeOutOverInMs) {

		final Element elementToFade = DOM.getElementById(elementID.trim());
		//ensure element exists
		if (elementToFade==null){
				return false;
		}
			
		Style style = elementToFade.getStyle();

		int startingOpacity=100;

		//if theres an existing opacity we start from that one
		if (style!=null)
		{
			String opacityString = style.getOpacity();
			if (opacityString!=null){
				//if its a valid number we use it here.
				try {					
					startingOpacity = Math.round(Float.parseFloat(opacityString)*100);  					
				} catch (Exception e) {
					startingOpacity=100;
				}
			}

		}

		String runnablesID = "Fading_"+elementID;
		
		//ensure theres no existing runnable, if there is cancel it
		DeltaRunnable previousRunnable = GWTJAMTimerController.getNamedRunnable(runnablesID);
		if (previousRunnable!=null){
			Log.info("canceled previous runnable");
			previousRunnable.cancel();
		}

		Log.info("fading overlay with ID:"+elementID+" over "+FadeOutOverInMs);


		//first we get the amount to reduce each interval
		int totalIntervals = FadeOutOverInMs /100;
		int opacityStep = startingOpacity /  totalIntervals;

		// (num of seconds in ms / interval(ms)) = total # intervals		
		//opacity(100) / # intervals = opacity point per interval



		//Ensure the element is on the changed div list 
		//This list is so the save functions know what element states need to save		
		
		//Note; we exclude loading div from this list as the state of that shouldnt be saved/loaded
		//if (!elementID.equalsIgnoreCase("loadingdiv")){
		//	InstructionProcessor.ChangedHtmlPageElement.add(elementID);
		//}
		

		//check step is at least 1
		if (opacityStep<1){
			opacityStep = 1;
		}

		//final double finalOpacityStep = opacityStep;


		/*
		final Timer fadeout = new Timer() {
			private double backopacity = finalstartingOpacity;
			Style style = elementToFade.getStyle();
			@Override
			public void run() {

				//both for IE..for some reason
				style.setProperty("filter", "alpha(opacity=" + backopacity+ ")");
				style.setProperty("opacity", "" + (backopacity / 100));

				// ThisImage.getElement().setAttribute("style",
				// " filter: alpha(opacity="+opacity+"); opacity: "+(opacity/100)+";");
				backopacity = backopacity - finalOpacityStep;
				if (backopacity < finalOpacityStep) {		
					backopacity =0;
					style.setProperty("filter", "alpha(opacity=" + backopacity+ ")");
					style.setProperty("opacity", "" + (backopacity / 100));
					style.setDisplay(Display.NONE);
					this.cancel();					
				}

			}

		};
		*/

		final double finalstartingOpacity = startingOpacity;
		//new delta based system
		final double OpacityStepPerMS = 100.0/FadeOutOverInMs;		
		Log.info("OpacityStepPerMS:"+OpacityStepPerMS);
		
		final DeltaRunnable deltaFadeUpdate = new DeltaRunnable(){
			private double backopacity = finalstartingOpacity;
			Style style = elementToFade.getStyle();
			@Override
			public void update(float delta) {
				
				//work out new opacity based on time since last update
				//opacity step x ms since last update = amount we change this time
				double changeAmount = OpacityStepPerMS * delta;				
				
			//	Log.info("current opacity:"+backopacity+" changeing by:"+changeAmount);
				
				
				//subtrack from current value
				backopacity = backopacity - changeAmount;
				
				
				//update opacity
				//both for IE..for some reason
				style.setProperty("filter", "alpha(opacity=" + backopacity+ ")");
				style.setProperty("opacity", "" + (backopacity / 100));

				// ThisImage.getElement().setAttribute("style",
				// " filter: alpha(opacity="+opacity+"); opacity: "+(opacity/100)+";");
				//backopacity = backopacity - finalOpacityStep;
				
				if (backopacity < changeAmount) {		
					backopacity =0;
					style.setProperty("filter", "alpha(opacity=" + backopacity+ ")");
					style.setProperty("opacity", "" + (backopacity / 100));
					style.setDisplay(Display.NONE);
					
					//remove from updates (equilivant to cancel)
					super.cancel();
					
				}
				
			}
			
		};
		deltaFadeUpdate.setRunnablesName(runnablesID);
		GWTJAMTimerController.addObjectToUpdateOnFrame(deltaFadeUpdate);
		

		// (num of seconds in ms / interval(ms)) = total # intervals		
		//opacity(100) / # intervals = opacity point per interval

		// totalms / 


		//6 , 12, 18, 24, 30 , 36, 42	

		//Fader.scheduleRepeating(time / 6);

		//fadeout.scheduleRepeating(100);

		return true;
	}
	
	@Override
	public boolean hideelement(String IDnamehe) {
		
		Element elementToHidehe = DOM.getElementById(IDnamehe);

		if (elementToHidehe != null) {

			elementToHidehe.getStyle().setDisplay(Style.Display.NONE);

			return true;
			//Ensure the element is on the changed div list 
			//This list is so the save functions know what element states need to save
		//	ChangedHtmlPageElement.add(IDnamehe);
		//	Log.info("element with ID=" + IDnamehe+" set to "+elementToHidehe.getStyle().getDisplay());
			//RootPanel.get(IDnamehe).getElement().getStyle()
			//.setDisplay(Style.Display.NONE);
		};
		//else {
		//
		//	Log.info("hide element not found ID=" + IDnamehe);
		//	return false;
		//}

		// special processing for MyApplication.Feedback
		if (IDnamehe.equalsIgnoreCase("MyApplication.Feedback")) {
			// hide the controlls too
			JAM.MessageBackButton.setVisible(false);
			JAM.MessageForwardButton.setVisible(false);
			JAM.MessageHistoryButton.setVisible(false);
			return false;

		}

		return false;
	}

	
	@Override
	public boolean removeCSSClassFromDomElement(String elementID, String Classac) {
		
		Element elementToSetac = DOM.getElementById(elementID);

		if (elementToSetac != null) {
			RootPanel.get(elementID).getElement().removeClassName(Classac);
			return true;
		} else {
			return false;
		}
	}
	



	
	@Override
	public boolean setCSSClassOnDomElement(String elementsID, String classestoset) {
				
		Element elementToSetac = DOM.getElementById(elementsID);

		if (elementToSetac != null) {

			elementToSetac.setClassName(classestoset);				

			
			return true;
			
		} else {
			return false;
		}
		
	}

	@Override
	public boolean setStyleOnElement(String elementsID, String styletoset) {
		

		Element elementToSet = DOM.getElementById(elementsID);

		if (elementToSet==null){
			Log.info("Error: Cant find requested element in DOM");
			return false;
		}


		elementToSet.setAttribute("style", styletoset);
				
		return true;
	}

	@Override
	public boolean showelement(String elementsID) {

		Element elementToHide = DOM.getElementById(elementsID);

		if (elementToHide != null) {
			RootPanel.get(elementsID).getElement().getStyle().setDisplay(Style.Display.BLOCK);
			return true;
		}
		// special processing for MyApplication.Feedback
		if (elementsID.equalsIgnoreCase("MyApplication.Feedback")) {
			// hide the controlls too
			JAM.MessageBackButton.setVisible(true);
			JAM.MessageForwardButton.setVisible(true);
			JAM.MessageHistoryButton.setVisible(true);
			return false;
		}
		return false;
	}

	@Override
	public boolean testIfDivHasClass(String divWithThisIDToCheck, String classToLookFor) {
		Element divToCheck = DOM.getElementById(divWithThisIDToCheck);
		
		if (divToCheck==null){
						
			Log.info("No div found with ID  "+divWithThisIDToCheck+" ");								
			return false;
			
		}
		
		//get its style class's 
		String classes = " "+divToCheck.getClassName()+" "; //note we add spaces to make searching easier (else we get parts of words)
		Boolean hasClass = classes.contains(" "+classToLookFor+" ");
		return hasClass;
	}

	
	@Override
	public void setBackgroundClass(String currentProperty) {

		if (currentProperty.compareTo("none") == 0) { 				
			RootPanel.getBodyElement().setClassName("");
		} else {				
			RootPanel.getBodyElement().setClassName(currentProperty);
		}

	}
	
	
	
}


