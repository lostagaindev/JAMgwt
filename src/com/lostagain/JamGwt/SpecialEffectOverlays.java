package com.lostagain.JamGwt;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.lostagain.Jam.JAMTimerController;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.InstructionProcessing.CommandLine;
import com.lostagain.Jam.InstructionProcessing.CommandList;
import com.lostagain.Jam.InstructionProcessing.InstructionProcessor;

import lostagain.nl.spiffyresources.client.spiffycore.HasDeltaUpdate;

import com.lostagain.Jam.InstructionProcessing.CommandLine.GameCommands;

/**
 * 
 * Class to manage full screen special effects, mostly done as overlays with animations
 * TODO: tidy up. Ensure flash effect looks nice
 * 
 * @author darkflame
 *
 */
public class SpecialEffectOverlays extends AbsolutePanel implements HasDeltaUpdate {

	static Logger Log = Logger.getLogger("Jam.SpecialEffectOverlays");

	/**
	 * currently active effects (can be many at once)
	 **/
	ArrayList<SimplePanel> overlayPanels = new 	ArrayList<SimplePanel>();

	/**
	 * Panel controlls fadein/out - can only be one fade at a time.
	 * Its also always attached
	 */
	SimplePanel fadePanel = new SimplePanel();


	/**
	 * 0 = transparent
	 * 1 = fully visible
	 */
	//double currentOpacity = 1.0; //phase out


	/**
	 * The root overlay should by backgroundless, 100% opacity and only used directly for the hacking effect.
	 * Other things, like fullscreen overlays should be SimplePanels attached to it (which can thus fade in/out independantly)
	 * 
	 */
	SpecialEffectOverlays overlay = this;

	Style fadePanelStyle;
	/**
	 * is a special effect currently playing?
	 */
	boolean inuse = false;

	//final Timer faderin; 
	//final Timer faderout;

	//final Timer hacked_in;
	//final Timer hacked_out;

	int wx=0;
	int wy=0;
	int number_of_lines = 0;
	//int fade_out_step = 10;

	double fadestepperms = 1.0;

	final static ArrayList<Label> leterarray = new ArrayList<Label>();

	//for after processing
	//(can be either a runnable or a jamcommandlist)
	static class postProcess {
		CommandList commands = null;
		Runnable runnable=null;
		
		public postProcess(CommandList newProcessThisAfter) {
			commands=newProcessThisAfter;
		}
		public postProcess(Runnable newProcessThisAfter) {
			runnable=newProcessThisAfter;
		}
		public boolean isJamProcess() {
			if (commands!=null && !commands.isEmpty()){
				return true;
			}
			return false;
		}
		public boolean isRunnable() {
			if (runnable!=null){
				return true;
			}
			return false;
		}
	}
	
	/**
	 * List of things pending to process after the current effect.
	 * (can be other effects)
	 */
	ArrayList<postProcess> PostProcessList = new ArrayList<postProcess>();



	enum screenAnimations {
		hacked_in(true),
		hacked_out(true),

		fade_in,
		fade_out;

		boolean deltaBased=false;
		screenAnimations(){			
		}
		screenAnimations(boolean deltaBased){
			this.deltaBased=deltaBased;
		}

	}
	
	HashSet<screenAnimations> currentlyPlayingAnimations = new 	HashSet<screenAnimations>();
	/**
	 * A temp set of animations that are finnished and should be removed from the list
	 */
	HashSet<screenAnimations> finnishedAnimations = new HashSet<screenAnimations>();

	private String currentOverlayBackColour;



	public SpecialEffectOverlays() {

		this.setSize("100%", "100%");
		this.setStylePrimaryName("effectoverlay");
		this.getElement().setId("effectoverlay");

		// default to black style
		Style overlayStyle = overlay.getElement().getStyle();
		//	overlayStyle.setProperty("filter", "alpha(opacity=" + (currentOpacity*100) + ")");
		//overlayStyle.setProperty("opacity", "" + (currentOpacity));
		overlayStyle.setProperty("zIndex", "53000");
		overlayStyle.setProperty("pointer-events", "none");	
		overlayStyle.setOpacity(1.0);

		//setup fade panel
		fadePanel.setSize("100%", "100%");
		fadePanelStyle =fadePanel.getElement().getStyle();
		fadePanelStyle.setProperty("pointer-events", "none");	
		fadePanel.getElement().setId("fadeID");	

		super.add(fadePanel);

		/*
		faderout = new Timer() {
			@Override
			public void run() {
				System.out.print((opacity / 100) + "\n");
				inuse = true;
				Style style = overlay.getElement().getStyle();
				style.setProperty("filter", "alpha(opacity=" + opacity + ")");
				style.setProperty("opacity", "" + (opacity / 100));

				// ThisImage.getElement().setAttribute("style", " filter:
				// alpha(opacity="+opacity+"); opacity: "+(opacity/100)+";");
				opacity = opacity + fade_out_step;
				if (opacity > 100) {
					//overlay.removeFromParent();
					inuse = false;
					this.cancel();
					fade_out_step=10;
					Log.info("post fade instructions = "+PostProcessList.get(0));

					//if theres after processing we run it
					if (PostProcessList.get(0).length()>2){
						Log.info("processing post fade instructions:"+ProcessThisAfter);

						JAMcore.processInstructions(PostProcessList.get(0),"postFadeInstructions",null);//<<NEEDS UNIQUE ID FOR THIS
						//then clear it
						PostProcessList.remove(0);
						Log.info("cleared fade instructions");

					}

				}

			}

		};
		faderin = new Timer() {
			@Override
			public void run() {
				System.out.print((opacity / 100) + "\n");
				inuse = true;
				Style style = overlay.getElement().getStyle();
				style.setProperty("filter", "alpha(opacity=" + opacity + ")");
				style.setProperty("opacity", "" + (opacity / 100));

				// ThisImage.getElement().setAttribute("style", " filter:
				// alpha(opacity="+opacity+"); opacity: "+(opacity/100)+";");
				opacity = opacity - fade_out_step;
				if (opacity < 0) {
					//we removed the line below because we manually trigger the clear command with SpecialEffect = Clear. This is because we want to control ourselves when the user can operate the website.
					//overlay.removeFromParent();
					inuse = false;
					this.cancel();
					fade_out_step=10;
					Log.info("post fade instructions = "+PostProcessList.get(0));

					//if theres after processing we run it
					if (PostProcessList.get(0).length()>2){
						Log.info("processing post fade instructions:"+PostProcessList.get(0));
						JAMcore.processInstructions(PostProcessList.get(0),"postFadeInstructions",null); //<<NEEDS UNIQUE ID FOR THIS
						//then clear it
						PostProcessList.remove(0);
						Log.info("cleared fade instructions");

					}


				}

			}

		};
		hacked_in = new Timer() {
			@Override
			public void run() {
				//draw random letter somewhere
				wx = (int)Math.round(Math.random()*overlay.getOffsetWidth()  )-110;
				//int y = (int)Math.round(Math.random()*overlay.getOffsetHeight()  );				
				wy=wy+2;

				JAMcore.GameLogger.info(" x= "+wx+" y= "+wy+"=");

				Label newword1 = new Label(randomword());
				Label newword2 = new Label(randomword());

				leterarray.add(newword1);
				leterarray.add(newword2);

				overlay.add(newword1, wx, wy);
				overlay.add(newword2, wx, wy+8);
				//
				number_of_letters=number_of_letters+1;

				if (wy>overlay.getOffsetHeight()){

					//if theres after processing we run it
					if (ProcessThisAfter.length()>2){
						JAMcore.processInstructions(ProcessThisAfter,"hackedin",null);
						//then clear it
						ProcessThisAfter="";
					}


					inuse = false;
					this.cancel();
				}
			}

		};
		hacked_out = new Timer() {
			@Override
			public void run() {
				//draw random letter somewhere
				JAMcore.GameLogger.info(" deleteing= "+number_of_letters);

				leterarray.get(number_of_letters).removeFromParent();
				leterarray.get(number_of_letters).setVisible(false);

				if (leterarray.get(number_of_letters).isAttached()==false){
				number_of_letters=number_of_letters+1;
				}

				if (number_of_letters>=leterarray.size()){
					inuse = false;
					leterarray.clear();
					overlay.clear();
					overlay.removeFromParent();

					this.cancel();
				}
			}

		};*/
	}

	/**
	 * 0-100
	 * @param NewOpacity
	 */
	/*
	public void SetOpacity(double NewOpacity){
		if ((NewOpacity<101) &&(NewOpacity>-1)){
			currentOpacity= NewOpacity/100.0;;
			Style style = overlay.getElement().getStyle();
			style.setProperty("filter", "alpha(opacity=" + (currentOpacity*100) + ")");
			style.setProperty("opacity", "" + (currentOpacity));

		}
	}*/

	//public void SetOpacity(double NewOpacity){
	public void SetOpacity_internal(double NewOpacity){


		//Log.info("FadeIn fadestepperms=.. rgba(0,0,0,"+NewOpacity+")");

		fadePanelStyle.setBackgroundColor("rgba(0,0,0,"+NewOpacity+")");


	}

	public void startCSSFadeOnOverlay(final Style overlayStyle, final double targetOpacity, double duration ){
		startCSSFadeOnOverlay(overlayStyle,targetOpacity,duration,null);

	}


	public void startCSSFadeOnOverlayWithPostActions(final Style overlayStyle, final double targetOpacity, double duration,final CommandList postActions ){

		Runnable runAfterRunnable = null;

		if (!postActions.isEmpty()){			
			runAfterRunnable = new Runnable(){
				@Override
				public void run() {
					Log.info("Processing post FadeIn in instructions:"+postActions.getCode());
					InstructionProcessor.processInstructions(postActions,"postFadeInstructions",null); //<<NEEDS UNIQUE ID FOR THIS

					Log.info("Proccessed post fadein post instructions. #sets now="+PostProcessList.size());	

				}			
			};

		} 

		startCSSFadeOnOverlay(overlayStyle,targetOpacity,duration,runAfterRunnable);

	}

	/**
	 *     opacity: 1;
    -webkit-transition: opacity 3.55s ease-in-out;
	 */
	//TODO: make work on any overlay
	public void startCSSFadeOnOverlay(final Style overlayStyle, final double targetOpacity, double duration, final Runnable runAfterCSSAnimation){

		double secounds = duration/1000.0;

		Log.info("StartCSSFadeOnOverlay = "+overlayStyle.getOpacity()+" >>> "+targetOpacity+"  over "+duration);


		overlayStyle.setProperty("-webkit-transition", " "+secounds+"s ease-in-out");
		overlayStyle.setProperty("-moz-transition", " "+secounds+"s ease-in-out");
		overlayStyle.setProperty("-ms-transition", " "+secounds+"s ease-in-out");
		overlayStyle.setProperty("-o-transition", " "+secounds+"s ease-in-out");
		overlayStyle.setProperty("-transition", " "+secounds+"s ease-in-out");

		//brief wait to ensure new transition time is set
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {			
			@Override
			public void execute() {
				overlayStyle.setOpacity(targetOpacity);					
			}
		});

		//as we cant detect the end ourselves (TransitionEnd events not fully supported), we set a timer purely to flag the end.
		final Timer TransitionEndTimer = new Timer() {
			@Override
			public void run() {
				inuse = false;
				//overlay.getElement().getStyle().clearBackgroundColor();
				//Log.info("post fadein instructions. Sets in queue = "+PostProcessList.size());
				//if theres after processing we run it			
				//then clear it
			//	testIfThereIsCommandsToResumeAfterEffect();  //old method, not used for fades anymore
				
				
				
				//if theres after processing we run it		
				if (runAfterCSSAnimation!=null){
					runAfterCSSAnimation.run();
				}
			}
		};
		TransitionEndTimer.schedule((int) (duration+30)); //70 is arbitrary just to give some breathing room for the event processing




	}



	/**
	 * 
	 */
	public void Clear(){

		final Timer WaitTillFree = new Timer() {
			@Override
			public void run() {
				if (inuse == false) {
					inuse = false;

					//set back to normal
					/*
					currentOpacity = 1.0;
					overlay.getElement().getStyle().setProperty("backgroundColor", "black");
					overlayStyle.clearBackgroundColor();
					overlayStyle.clearBackgroundImage();

					overlay.removeFromParent();
					 */
					ClearNOW();
					this.cancel();
				} else {
					// wait more
				}

			}

		};
		WaitTillFree.scheduleRepeating(500);
	}

	public void ClearNOW(){

		ResetNOW();

		for (SimplePanel panel : overlayPanels) {
			this.removeOverlayPanel(panel,0);

		}

		//only remove if no other overlays present

		overlay.removeFromParent();

	}


	public void ResetNOW(){
		inuse = false;
		//	currentOpacity = 1.0;

		//overlay.getElement().getStyle().setProperty("backgroundColor", "black");		
		//	overlayStyle.clearBackgroundColor();
		//overlayStyle.clearBackgroundImage();

		fadePanelStyle.clearBackgroundColor();
		fadePanelStyle.clearBackgroundImage();
		fadePanelStyle.setOpacity(0.0);


	}


	/** Fades it in and out again quickly with a lightBlue colour **/
	public void Flash(final double Over, String backgroundColour){

		int Half = (int)Math.ceil(Over/2);
		if (Half<10){
			Half=10;
		}
		//fade_out_step=25; PostProcessList

		//ArrayList<CommandList> CurrentPostProcessList = PostProcessList;

		//	overlay.getElement().getStyle().setProperty("backgroundColor", "lightBlue");
		Log.info("- Flashing in and out at -"+(Half)+" duration each");


		//add the fadein command to the start of the commandlist
		CommandList commandList = new CommandList();
		if (backgroundColour.isEmpty()){			 
			backgroundColour="\"200,200,255\"";
		}

		commandList.add(new CommandLine(GameCommands.specialeffect, "fadein,"+Half+","+backgroundColour));		
		PostProcessList.add(0,new postProcess(commandList) );

		Log.info("- PostProcessList now -"+PostProcessList.size());
		Log.info("- PostProcessList now -"+PostProcessList.get(0).commands.getCode());


		FadeOut(Half,backgroundColour,"",false,null);

		//fade_out_step=25;
		//FadeIn(Half,"170,170,255",""); //the fadein is now run auto by the above





		//overlay.getElement().getStyle().setProperty("backgroundColor", "black");

	}


	public void FadeIn(final double Over) {
		FadeIn(Over,"0,0,0","",false,null);
	}

	public void FadeIn(final double OverInMs, final String colourSpecified,final String imageURL,final boolean centerImage, final CommandList runAfter) {

		final String colour = colourSpecified.replaceAll("\"", ""); //remove any quotes from colourSpecified 

		JAM.GwtLog.info("- fadein requested: = "+OverInMs+","+colour+","+imageURL);
		// wait for effect overlay to not be in use;

		final Timer WaitTillFree = new Timer() {
			@Override
			public void run() {


				if ((inuse == false) && (JAMcore.NumberOfHTMLsLeftToLoad == 0)) {


					//	fadestepperms = 1.0/OverInMs;
					//	Log.info("FadeIn fadestepperms=.."+fadestepperms);
					inuse = true;

					JAM.GwtLog.info("- fadein state: = "+OverInMs+","+colour+","+imageURL);

					if (!colour.isEmpty()) {
						Log.info("Color setting to:"+colour);
						setStyleBackgroundAsColour(fadePanelStyle,colour);
					} else if (imageURL.isEmpty()) {
						//default color if no image
						setStyleBackgroundAsColour(fadePanelStyle,"0,0,0");

					}

					if (!imageURL.isEmpty()){
						Log.info("Image setting to:"+imageURL);
						setStyleBackgroundAsImage(fadePanelStyle,imageURL,centerImage); //true by default? should this be a option?
					} 

					startCSSFadeOnOverlayWithPostActions(fadePanelStyle,0.0,OverInMs,runAfter);

					//faderin.scheduleRepeating(Over / 10); //old timing based on fixed steps
					//	StartScreenAnimation(screenAnimations.fade_in);
					this.cancel();
				} else {
					// wait more
					Log.info("PagesToLoad:"+JAMcore.NumberOfHTMLsLeftToLoad);					
					Log.info("not ready yet so we can wait..");
				}

			}

		};


		//we no longer wait for other fades to finish
		WaitTillFree.run();

		/*
		Log.info("PagesToLoad:"+JAMcore.NumberOfHTMLsLeftToLoad);

		if ((inuse == true) || (JAMcore.NumberOfHTMLsLeftToLoad!=0)){
			Log.info("________________________________________________FadeIn not ready yet so we wait..");
			WaitTillFree.scheduleRepeating(500); 
			//should change this to a proper system that gets notified to run the next bit when done the current one
			//Really this shouldnt be needed as each fadein stops other command anyway till its finished
		} else {
			//inuse = true;
			//opacity = 100;
			//faderin.scheduleRepeating(Over / 10);
			WaitTillFree.run();
		}

		 */

	}

	public void FadeOut(final double Over) {
		FadeOut(Over,"0,0,0","",false,null);
	}

	/**
	 * 
	 * @param Over
	 * @param colour  comma separated RGB
	 */
	public void FadeOut(final double OverInMs,String colourSpecified,final String imageURL,final boolean centerImage, final CommandList runAfter) {

		final String colour = colourSpecified.replaceAll("\"", ""); //remove any quotes from colourSpecified 

		JAM.GwtLog.info("- fadeout requested: = "+OverInMs+","+colour+","+imageURL+" cenetered:"+centerImage);

		final Timer WaitTillFree2 = new Timer() {
			@Override
			public void run() {
				if ((inuse == false)&& (JAMcore.NumberOfHTMLsLeftToLoad==0)) {

					inuse = true;
					//	currentOpacity = 0.0;
					//currentOverlayBackColour = colour;

					//fadestepperms = 1.0/OverInMs;
					//Log.info("FadeOut fadestepperms=.."+fadestepperms);

					//what we are fadeing to/from
					//String colourString = "rgba("+currentOverlayBackColour+","+1.0+")";					
					fadePanelStyle.setOpacity(0.0);
					//overlayStyle.setBackgroundColor(colourString);

					JAM.GwtLog.info("- fadeout state: = "+OverInMs+","+colour+","+imageURL);

					if (!colour.isEmpty()) {
						Log.info("Color setting to:"+colour);
						setStyleBackgroundAsColour(fadePanelStyle,colour);						
					} else if (imageURL.isEmpty()) {
						//default colour if no image
						setStyleBackgroundAsColour(fadePanelStyle,"0,0,0");

					}
					if (!imageURL.isEmpty()){
						Log.info("Image setting to:"+imageURL+" centered:"+centerImage);
						setStyleBackgroundAsImage(fadePanelStyle,imageURL,centerImage); //true by default? should this be a option?
					} else {
						fadePanelStyle.clearBackgroundImage();
					}


					startCSSFadeOnOverlayWithPostActions(fadePanelStyle,1.0,OverInMs,runAfter);

					///	StartScreenAnimation(screenAnimations.fade_out);
					this.cancel();
				} else {
					// wait more
					Log.info("PagesToLoad:"+JAMcore.NumberOfHTMLsLeftToLoad);					
					Log.info("not ready yet so we can wait..");
				}

			}

		};

		Log.info("PagesToLoad:"+JAMcore.NumberOfHTMLsLeftToLoad);

		//we no longer wait for other fades to finnish
		WaitTillFree2.run();

		/*
		if ((inuse == true) || (JAMcore.NumberOfHTMLsLeftToLoad!=0)){
			Log.info("________________________________________________Fadeout not ready yet so we wait..");
			WaitTillFree2.scheduleRepeating(500);	
			//should change this to a proper system that gets notified to run the next bit when done the current one
			//Really this shouldnt be needed as each fadein stops other command anyway till its finished

		} else {
			WaitTillFree2.run();
		}
		 */	

	}

	//public void Hacked_in(){
	////	Hacked_in(2);
	//}

	//public void Hacked_out(){
	//	Hacked_out(2);
	//}

	double iterationsperms = 1;
	int totalHackedIterations = 0;

	/**
	 * 15 is a reasonable animation update interval to have
	 * 
	 * @param Speed
	 */
	public void Hacked_in(final double duration ){



		final Runnable WaitTillFree3 = new Runnable() {
			@Override
			public void run() {
				if (inuse == false) {
					inuse = true;
					wy=0;
					wx=0;
					//	currentOpacity=1.0;
					number_of_lines=0;

					overlay.addStyleName("hackerstyle");

					/*
					Style style = overlay.getElement().getStyle();
					style.setProperty("filter", "alpha(opacity=" +( currentOpacity * 100.0)+ ")");
					style.setProperty("opacity", "" + (currentOpacity));	
					style.setProperty("pointer-events", "none");		*/


					prepareHackedEffect(duration);
					//dont use the hacked_in timer, just add to the currentlyPlayingAnimations instead
					//		hacked_in.scheduleRepeating(animation_update_interval);				
					StartScreenAnimation(screenAnimations.hacked_in);

					//this.cancel();
				} else {
					// wait more
				}

			}

		};




		if (inuse == true){
	//		WaitTillFree3.scheduleRepeating(500);
			ProcessThisAfter(WaitTillFree3);
			
		} else {
			WaitTillFree3.run();
			/*
			inuse = true;
			wy=0;
			wx=0;
			opacity=100;
			number_of_words=0;
			Style style = overlay.getElement().getStyle();
			style.setProperty("filter", "alpha(opacity=" + opacity + ")");
			style.setProperty("opacity", "" + (opacity / 100));		
			overlay.setStylePrimaryName("hackerstyle");

			prepareHackedEffect(speed);
			//dont use the hacked_in timer, just add to the currentlyPlayingAnimations instead
			//		hacked_in.scheduleRepeating(animation_update_interval);				
			StartScreenAnimation(screenAnimations.hacked_in);*/

		}
	}	

	protected void prepareHackedEffect(double duration) {

		int totalHackedIterations =0;
		leterarray.clear();

		//pre-prepare words
		for (int y = 0; y <= overlay.getOffsetHeight(); y=y+2) {
			totalHackedIterations++;

			Label newword1 = new Label(randomword());
			Label newword2 = new Label(randomword());

			leterarray.add(newword1);
			leterarray.add(newword2);

			wy=wy+2;
			//position random letter somewhere
			wx = (int)Math.round(Math.random()*overlay.getOffsetWidth()  )-110;

			//Label newword1 = leterarray.get(number_of_words);
			//Label newword2 = leterarray.get(number_of_words+1);
			newword1.setVisible(false);
			newword2.setVisible(false);

			overlay.add(newword1, wx, wy);
			overlay.add(newword2, wx, wy+8);


		}


		//Few extra for good measure
		Label newword1 = new Label(randomword());
		Label newword2 = new Label(randomword());
		leterarray.add(newword1);
		leterarray.add(newword2);
		//---

		iterationsperms = totalHackedIterations/duration;

		Log.info("totalHackedIterations:"+totalHackedIterations+"   lines:"+leterarray.size());
		Log.info("Iterations per ms:     "+iterationsperms);



		//	if (wy>overlay.getOffsetHeight()){
		//				
		//}


	}

	public void Hacked_out(final double duration){


		//final Timer WaitTillFree4 = new Timer() {
		final Runnable WaitTillFree4 = new Runnable() {			
			@Override
			public void run() {
				if (inuse == false) {
					inuse = true;
					wy=0;
					wx=0;
					//	currentOpacity=1.0;
					number_of_lines=0;
					/*
					Style style = overlay.getElement().getStyle();
					style.setProperty("filter", "alpha(opacity=" + (currentOpacity*100.0) + ")");
					style.setProperty("opacity", "" + (currentOpacity));						
					style.setProperty("pointer-events", "none");		
					 */
					overlay.addStyleName("hackerstyle");
					//needs transparent back
					overlay.getElement().getStyle().clearBackgroundColor();
					overlay.getElement().getStyle().clearBackgroundImage();


					//hacked_out.scheduleRepeating(animation_update_interval);
					StartScreenAnimation(screenAnimations.hacked_out);
				//	this.cancel();
				} else {
					// wait more
					//ERROR, should not get here
					Log.severe("Error; tried to start hacked out while inuse still true");
				}

			}

		};
		if (inuse == true){
			//WaitTillFree4.scheduleRepeating(500);
			//No longer timer, now we add to the post process list
			ProcessThisAfter(WaitTillFree4);
			
		} else {
			WaitTillFree4.run();
		}



	}


	//Special function for JARG	
	public void ProcessThisAfter(CommandList NewProcessThisAfter){
		//if its not empty, add it
		if (!NewProcessThisAfter.isEmpty()){
			//set up an arraylist of process's to do after, and add this new one on the end of the queue
			//ProcessThisAfter=NewProcessThisAfter;

			//(likewise, remove another when they are run and remove all when clearing the effect)
			PostProcessList.add(new postProcess(NewProcessThisAfter));

			Log.info("post fade instructions now has             = "+PostProcessList.size()+" sets of stuff after a jam command list was added");
			Log.info("post fade instructions newly added set was = "+PostProcessList.get(PostProcessList.size()-1).commands.getCode());
		}
	}
	
	public void ProcessThisAfter(Runnable NewProcessThisAfter){
		
			PostProcessList.add(new postProcess(NewProcessThisAfter));


			Log.info("post fade instructions now has             = "+PostProcessList.size()+" sets of stuff after a runnable was added");
	
	}


	public String randomword(){
		String word = "1010101";

		int rw = (int)(Math.random()*18);
		switch (rw) {
		case 1:  word = "4423"; break;
		case 2:  word = "101010"; break;
		case 3:  word = "4815162342"; break;
		case 4:  word = "The Matrix Has You"; break;
		case 5:  word = "Would You like To Play a Game?"; break;
		case 6:  word = "]SYSTEM CRASH: \n ]Restart Y/N?: \n YES"; break;
		case 7:  word = "42"; break;
		case 8:  word = "Good morning Dave."; break;
		case 9:  word = "12:50, press Return."; break;
		case 10: word = "THERE IS AS YET INSUFFICIENT DATA FOR A MEANINGFUL ANSWER"; break;
		case 11: word = "\"Yes, now there is a God.\"";break;          
		case 12: word = "\"Insufficient data at this point. But whatever it was, he did it at a) each step along life's highway and b) not in a shy way..\"";break;
		case 13: word = "On the other side of the screen, it all looks so easy";break;
		case 14: word = "End of line";break;
		case 15: word = "By Your Command";break;
		case 16: word = "++?????++ Out of Cheese Error. Redo From Start.";break;
		case 17: word = "Thank you for helping us help you help us all.";break;
		case 18: word = "Error. Grasshopper disassembled... Re-assemble!.";break;
		default: word = "\"Where do you want to go today?\"";break;

		}

		return word;
	}

	@Override
	public void update(float delta) {
		
		Iterator<screenAnimations> iterator = currentlyPlayingAnimations.iterator(); 
		
		while (iterator.hasNext()) 
		{
			screenAnimations animation = iterator.next();
			
			switch (animation){
			case hacked_in:
				//	Log.info(" Updating hacked in");
				updateHackedIn(delta,animation); //note; we pass the iterator though so we can safely remove ourselves from the list when done
				break;
			case hacked_out:
				//	Log.info(" updateing hacked out");
				updateHackedOut(delta,animation);//note; we pass the iterator though so we can safely remove ourselves from the list when done
				break;
				//case fade_in:
				//	Log.info(" updateing fade in");
				//	updateFadeInFromBlack(delta);
				//	break;
				//case fade_out:
				//	Log.info(" updateing fade out");
				//	updateFadeToBlack(delta);
				//	break;
			default:
				break;			
			}
		}
		
		//now we finnish any animations flagged to finnish
		//(we dont remove them directly from currentlyPlaying, in the loop above as that will cause a concurrent modifcation error.
		//So instead we wait till the loop is finnished then remove them here)
		Iterator<screenAnimations> finiterator = finnishedAnimations.iterator();
		while (finiterator.hasNext()) 
		{
			screenAnimations animation = finiterator.next();
			StopScreenAnimation(animation);				
			//and remove from the finnished animation it too. (its safe to use the iterators remove command)
			finiterator.remove();
		}
		
		
	}



	/**
	 * 
	 * @param delta - ms since last frame
	 *
	private void updateFadeToBlack(float delta) {

		double fade_out_step = fadestepperms * delta;
		currentOpacity = currentOpacity + fade_out_step;
		//Log.info("currentOpacity="+currentOpacity+" ("+fade_out_step+")");

	//	overlayStyle.setProperty("filter", "alpha(opacity=" + currentOpacity + ")");
	//	overlayStyle.setProperty("opacity", "" + (currentOpacity / 100));
	//	overlayStyle.setOpacity(currentOpacity);
		SetOpacity_internal(currentOpacity);

		if (currentOpacity > 1.0) {
		//	fade_out_step=10;
			inuse = false;
			StopScreenAnimation(screenAnimations.fade_out);

			Log.info("Testing for post fade instructions = "+PostProcessList.size());		
			testIfThereIsCommandsToResumeAfterEffect();
			/*
			//if theres after processing we run it
			if (PostProcessList.size()>0){
				CommandList postProcess = PostProcessList.remove(0);

				if (!postProcess.isEmpty()){	
					Log.info("Processing post fade to black instructions:"+postProcess.getCode());
					JAMcore.processInstructions(postProcess,"postFadeInstructions",null);//<<NEEDS UNIQUE ID FOR THIS			
					Log.info("proccessed post fade instructions #sets now="+PostProcessList.size());
				} 

			}*/ 
	/*

		}


	}*/

	/**
	 * 
	 * @param delta - ms since last frame
	 *
	private void updateFadeInFromBlack(float delta) {

		double fade_in_step = fadestepperms * delta;

		currentOpacity = currentOpacity - fade_in_step;

	//	Log.info("currentOpacity="+currentOpacity+" ("+fade_in_step+")");

		//overlayStyle.setProperty("filter", "alpha(opacity=" + currentOpacity + ")");
		//overlayStyle.setProperty("opacity", "" + (currentOpacity / 100));

		//overlayStyle.setOpacity(currentOpacity);
		SetOpacity_internal(currentOpacity);

		if (currentOpacity < 0) {
			//we removed the line below because we manually trigger the clear command with SpecialEffect = Clear. This is because we want to control ourselves when the user can operate the website.
			//overlay.removeFromParent();
			inuse = false;
			StopScreenAnimation(screenAnimations.fade_in);
			overlay.getElement().getStyle().clearBackgroundColor();
			//	fade_out_step=10;
			Log.info("post fadein instructions. Sets in queue = "+PostProcessList.size());

			//if theres after processing we run it			
			//then clear it
			testIfThereIsCommandsToResumeAfterEffect();
		}
	}
	 */

	private void testIfThereIsCommandsToResumeAfterEffect() {
		if (PostProcessList.size()>0){
			Log.info("processing post found");
			
			postProcess postProcess = PostProcessList.remove(0);
			
			if (postProcess.isJamProcess()){							
				
				Log.info("processing post FadeIn in instructions:"+postProcess.commands.getCode());
				InstructionProcessor.processInstructions(postProcess.commands,"postFadeInstructions",null); //<<NEEDS UNIQUE ID FOR THIS

				Log.info("proccessed post fadein post instructions. #sets now="+PostProcessList.size());	

				//and finally ensure color is reset
				//	overlay.getElement().getStyle().setProperty("backgroundColor", "black");	
			} 
			
			if (postProcess.isRunnable()){							
				
				Log.info("processing post FadeIn in instructions (was runnable)");
				postProcess.runnable.run();
				Log.info("proccessed post fadein post instructions (was runnable). #sets now="+PostProcessList.size());	

			} 


		}
	}

	/**
	 * 
	 * @param delta - ms since last frame
	 * 
	 * @param animation - the current hacked animation
	 */
	private void updateHackedOut(float delta, screenAnimations animation) {
		//int wordsSetsToDo =    (int) Math.floor(((delta)*(linesPerHundredms/100.0))/2.0);

		int wordsSetsToDo =    (int) Math.ceil(iterationsperms * delta);

		Log.info("HackedOut: updates This delta="+wordsSetsToDo);

		//JAMcore.GameLogger.info(" deleteing= "+number_of_words+","+wordsSetsToDo);
		for (int i = 0; i < wordsSetsToDo; i=i+2) {

			if (number_of_lines+1>=leterarray.size()){
				inuse = false;
				//StopScreenAnimation(screenAnimations.hacked_out);		
				finnishedAnimations.add(animation);
				
				clearUpHackingEffect();
				testIfThereIsCommandsToResumeAfterEffect();

				return;
			}


			//leterarray.get(number_of_lines).removeFromParent();
			leterarray.get(number_of_lines).setVisible(false);
			leterarray.get(number_of_lines+1).setVisible(false);

			number_of_lines=number_of_lines+2;

			//if (leterarray.get(number_of_lines).isAttached()==false){

			//}

		}


	}
	private void clearUpHackingEffect() {
		Log.info(" cleaning up hack effect");

		for (Label lab : leterarray) {
			lab.removeFromParent();			
		}

		leterarray.clear();


		overlay.removeStyleName("hackerstyle");

	}
	/**
	 * 
	 * @param delta - ms since last frame
	 * @param animation 
	 * 
	 */
	private void updateHackedIn(float delta, screenAnimations animation) {
		

		

		//based on speed and delta we determine how many to add
		//speed is words per 100ms?
		//int wordsSetsToDo = (int) Math.floor((delta*(linesPerHundredms/100.0))/2.0);

		int wordsSetsToDo =    (int) Math.ceil(iterationsperms * delta);
		//	JAMcore.GameLogger.info("wordsSetsToDo= "+wordsSetsToDo);

		Log.info("Hacked In: Updates This delta="+wordsSetsToDo  +"  total lines so far:"+number_of_lines);

		//	Iterator<Label> letterArrayIt = leterarray.iterator();

		for (int i = 0; i < wordsSetsToDo; i++) {

			//int y = (int)Math.round(Math.random()*overlay.getOffsetHeight()  );				

			/*
			Label newword1 = new Label(randomword());
			Label newword2 = new Label(randomword());

			leterarray.add(newword1);
			leterarray.add(newword2);
			 */
			if ((    (number_of_lines+1)>(leterarray.size()-1)))
				//		||   (wy>overlay.getOffsetHeight()))
			{
				/*
				//if there's after processing we run it
				if (ProcessThisAfter!=null){
					JAMcore.processInstructions(ProcessThisAfter,"hackedin",null);
					//then clear it
					ProcessThisAfter=null; //"";
				}			
				 */

				inuse = false;
				//Stop hackedIn effect;
			//	StopScreenAnimation(screenAnimations.hacked_in);
				finnishedAnimations.add(animation);
				
				testIfThereIsCommandsToResumeAfterEffect();
				return;
			}


			leterarray.get(number_of_lines).setVisible(true);
			leterarray.get(number_of_lines+1).setVisible(true);


			//
			number_of_lines=number_of_lines+2;



		}
	}

	private void StartScreenAnimation(screenAnimations effecttype) {
		if (currentlyPlayingAnimations.isEmpty()){ //if nothing animating already then start timer
			JAMTimerController.addObjectToUpdateOnFrame(this);	 
		}
		currentlyPlayingAnimations.add(effecttype);				
	}

	private void StopScreenAnimation(screenAnimations effecttype) {
		currentlyPlayingAnimations.remove(effecttype);
		if (currentlyPlayingAnimations.isEmpty()){
			JAMTimerController.removeObjectToUpdateOnFrame(this);
		}

	}

	//rename, no longer just for fades
	private void setStyleBackgroundAsColour(Style overlayStyle,final String colour) {

		currentOverlayBackColour = colour;		

		String rgba[] = colour.split(",");
		String colourString = "";

		if (rgba.length == 3){
			colourString = "rgba("+currentOverlayBackColour+","+1.0+")";					
		} else if (rgba.length == 4){
			colourString = "rgba("+currentOverlayBackColour+")";
		} else {
			Log.warning("invalid color specified:"+colourString);
		}


		overlayStyle.setBackgroundColor(colourString);	


		Log.info("color set to:"+colourString);
	}



	private void setStyleBackgroundAsImage(Style overlayStyle, final String imageURL,boolean centered) {

		if (imageURL.toLowerCase().contentEquals("none")){
			overlayStyle.clearBackgroundImage();
			overlayStyle.setProperty("background-position" , "initial");
			overlayStyle.setProperty("background-repeat"   , "initial");
			overlayStyle.setProperty("background-size"     , "100% 100%");
			return;
		}

		//currentOverlayBackColour = color;					
		if (centered){			

			overlayStyle.setProperty("background-position", "center center");
			overlayStyle.setProperty("background-repeat"  , "no-repeat");		
			overlayStyle.setProperty("background-size"    , "initial");

		} else {

			overlayStyle.setProperty("background-position" , "initial");
			overlayStyle.setProperty("background-repeat"   , "initial");
			overlayStyle.setProperty("background-size"     , "100% 100%");

		}
		overlayStyle.setBackgroundImage("url(\""+imageURL+"\")");

		Log.info("image background set to:"+overlayStyle.getBackgroundImage()+" centered:"+centered);


	}


	public SimplePanel addOverlayPanel(String colour,String imageURL, double fadeOnDuration){

		SimplePanel newpanel = createOverlayPanel();

		Style newPanelsStyle = setPanelsStyle(newpanel, colour, imageURL); 

		newPanelsStyle.setOpacity(0.0);

		overlayPanels.add(newpanel);
		super.add(newpanel, 0, 0);

		Log.warning("new overlay added ");

		//set it to fade
		this.startCSSFadeOnOverlay(newPanelsStyle, 1.0, fadeOnDuration,null);


		return newpanel;
	}

	private Style setPanelsStyle(SimplePanel panel, String colour, String imageURL) {
		Style newPanelsStyle = panel.getElement().getStyle();

		if (!colour.isEmpty()) {
			Log.info("Color setting to:"+colour);
			setStyleBackgroundAsColour(newPanelsStyle,colour);
		}		

		if (!imageURL.isEmpty()){
			Log.info("Image setting to:"+imageURL);
			setStyleBackgroundAsImage(newPanelsStyle,imageURL,false);
		}
		return newPanelsStyle;
	}

	private SimplePanel createOverlayPanel() {
		SimplePanel newpanel = new SimplePanel();
		newpanel.setSize("100%", "100%");
		newpanel.getElement().setId("overlaypanel"+overlayPanels.size());
		return newpanel;
	}

	public void removeOverlayPanel(final SimplePanel panel,int fadeOffDuration){
		//set our own runnable to remove after fade
		Runnable removeStuff = new Runnable(){
			@Override
			public void run() {
				panel.removeFromParent();
				overlayPanels.remove(panel);
			}				
		};

		if (fadeOffDuration==0){			
			removeStuff.run();
		} else {	
			//set it to off
			startCSSFadeOnOverlay(panel.getElement().getStyle(), 0.0, fadeOffDuration,removeStuff);

		}



	}



	/**
	 * Clears queued commands set to fire after each effect is finished.
	 * This should not be used when a FLASH might fire, as that needs the queued commands to trigger the flashs fadein
	 * (as really its a fadeout+fadein)
	 */
	public void ClearAllQueuedCommands() {
		PostProcessList.clear();

	}


	/**
	 * duration not implemented, assumed zero
	 * (in future we should set the transition time in css, then change the style)
	 * 
	 * @param overlayPanel
	 * @param colour
	 * @param imageURL
	 * @param durationms
	 */
	public void adjustOverlayPanel(SimplePanel overlayPanel, String colour, String imageURL, int durationms) {
		Style style = setPanelsStyle(overlayPanel, colour, imageURL); 

		if (durationms!=0.0){
			startCSSFadeOnOverlay(style, 1.0, durationms,null);
		}

	}
}
