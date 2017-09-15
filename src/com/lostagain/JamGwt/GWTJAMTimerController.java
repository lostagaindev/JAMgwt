package com.lostagain.JamGwt;

import java.util.logging.Logger;

import lostagain.nl.spiffyresources.client.spiffycore.DeltaTimerController;
import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.animation.client.AnimationScheduler.AnimationHandle;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.lostagain.Jam.JAMTimerController;
import com.lostagain.Jam.Factorys.IsTimerObject;
import com.lostagain.Jam.JAMTimerController.IsIncrementalCommand;
import com.lostagain.JamGwt.JargScene.debugtools.GameDataBox;

/**
 * The global timer that eventually be used for all animation events in the game.
 * Currently not used much, except in music track fades.
 * 
 * The goal is to eventually have this class used to
 * a) Reduce the number of separate timers going
 * b) Make it easy to retractor into non-GWT versions of the JAM.
 * 
 * We have to consider two types of timer though;
 * - Timers for animating visual stuff, which should be done with "request animation frame" (see SceneObjectVisual AnimationScheduler )
 * - Timers that will go regardless of visibility. (ie, on a regular tick)
 * 
 * The second one we will only ever need one of, the first......no clue
 * 
 * NOTE: Regardless of the above we should never need to make a instance of this class. Just static methods applying to the whole game
 * **/
//TODO: extend SpiffyGWTDeltaTimer and reduce a lot of the code thats here
public class GWTJAMTimerController extends JAMTimerController {

	static Logger Log = Logger.getLogger("JAM.GWTJAMTimerController");
	
	/**
	 * the code that handles frame updates (can be prepared in advance, but it just fires updates on the loop
	 */
	private static AnimationCallback frameUpdateCode;
	
	/**
	 * holds the animation handler, letting the frame updates be canceled even before they fire.
	 */
	private static AnimationHandle animationRunner;


	/**
	 * Must be run before any timers or frame updates will work!
	 * Put this near the start of your code.
	 * 
	 * (Internally it makes a instance of GWTJAMTimer for JAMTimer. There should only ever be one, which is why its handled here
	 * and not in a new JAMTimer statement)
	 * @return 
	 * 
	 */
	public static void setup(){
		
		//create instance of ourselves
		JAMTimerController.setup(new GWTJAMTimerController()); //This gives a copy of GWTJAMTimer to DeltaTimerController superclass so it can call 
		//public startFromUpdates and stopFrameUpdates from static methods
		
		//also do the same for the GWTTimerFactory (inlined now
		//GwtTimerFactory.setup();
		
		//assign the frame update code
		frameUpdateCode = new AnimationFrameUpdateCallback();
		
	}
	
	//Here we override the start from updates function to trigger how GWT/webapps should be updated
	//That is, each time the browser thinks a animation from is available.
	@Override
	public void startFrameUpdates() {
		
		if (animationRunner==null){
			
			Log.warning("setting AnimationScheduler");		
			//first ensure any previous deltas values were cleared
			AnimationFrameUpdateCallback.lastUpdate=-1;
			
			animationRunner = AnimationScheduler.get().requestAnimationFrame(frameUpdateCode);
			Log.warning("AnimationScheduler now set");	
			
			
		}

		super.startFrameUpdates();
		//frameUpdatesRunning = true;
		
		

	}
	
	
	@Override
	protected void stopFrameUpdates() {
		if (animationRunner!=null){
			animationRunner.cancel();
			animationRunner=null;
			AnimationFrameUpdateCallback.lastUpdate=-1;//ensure any delta values were cleared
			
		}

		super.stopFrameUpdates();
		//frameUpdatesRunning = false;
	}






	/**
	 * may be able to slow or speed up updates with this
	 */
	static double expirementalDeltaMult = 1;
	
	
	
	



	public static double getExpirementalDeltaMult() {
		return expirementalDeltaMult;
	}

	public static void setExpirementalDeltaMult(double expirementalDeltaMult) {
		GWTJAMTimerController.expirementalDeltaMult = expirementalDeltaMult;
	}

	public void updateCurrentTimerDebugBoxImplm() {
		GameDataBox.updateCurrentTimers();
	}

	private static class AnimationFrameUpdateCallback implements AnimationCallback {
		static long lastUpdate = -1; //so we know the last update is inaccurate

		@Override
		public void execute(double timestamp) {

			if (!isFrameUpdatesRunning()) {
				lastUpdate = -1; //so we know the last update is inaccurate
				return;					
			}	

			//work out the time since the last update
			long currentTime = System.currentTimeMillis();
			double currentDelta; //was long
			
			if (lastUpdate!=-1){
				currentDelta =  currentTime - lastUpdate;
				
				currentDelta = currentDelta *expirementalDeltaMult;
				
				//we cap the delta to 1000 (ie, never more then 1 seconds worth or movement or transitions will happen at once)
			//	if (currentDelta>1000){
				//	Log.info("_______________WARNING Delta was :"+currentDelta+" which is a tad high. capping to "+maxDelta);					
				//	currentDelta=1000;
			//	}
				
				//(cap now delta with by superclass)
				
				
				lastUpdate = currentTime;
			} else {
				currentDelta =  50; //default delta
				lastUpdate = currentTime;
		
			}
			//Log.info("delta="+currentDelta+" (currenttime="+currentTime+" - lastUpdate"+lastUpdate+")");
							
			
			//updateAllFrameObjects(currentDelta);  
			try {
				updateFrame(currentDelta);  //we now run it though this now so it deals with min/max caps itself
			} catch (Exception e) {
				
				Log.severe("--"+e.getLocalizedMessage());				
				Log.severe("---------------------------------------------");
				Log.severe("-------------");
				Log.severe("--");
				Log.severe("GWT animation updates canceled due to previous error");				
				return;
				//e.printStackTrace();
			}
			

			//reschedule the update
			if (isFrameUpdatesRunning()){
				AnimationScheduler.get().requestAnimationFrame(this);
			}

		}
	}


	   /**
	    * Will run a command repeatingly for as long as it returns true.
	    * Use this for cpu-intensive stuff where you dont want the browser to hang
	    * 
	    * @param cmd
	    */
	   @Override
	   public void scheduleIncremental_impl(IsIncrementalCommand cmd){

		   Scheduler.get().scheduleIncremental(new RepeatingCommand() {			
			@Override
			public boolean execute() {
				return cmd.run();
			}
		});
		   
	   }
	   
	   
	   /**
	    * These commands would run after giving the browser time to update.
	    * 
	    * @param cmd
	    */
	   @Override
	   public void scheduleDefered_impl(Runnable cmd){
		   
		   Scheduler.get().scheduleDeferred(new ScheduledCommand() {			
			@Override
			public void execute() {
				cmd.run();
			}
		});
		   
	   }
	
	
	
	
	
	/**
	 * Sets up a special specific task that will be run on its own timer, and not part of the main timer loops
	 * This should only be used if you want to run something after a specific amount of time, not on a regular interval.
	 * 
	 * This will not do much more then use Javas timer to fire the event atm, 
	 */
	//static public void setDedicatedTimerToGo(Runnable fireThisEvent, long AfterThisManyMS){
	//Store time till fireing
	//Store start time (this lets us check how long is remaining

	//}

	//-------------------------------
	//The following is for generic timer creation
	//This lets the JAM Core create timers without a specific implementation
	//In GWT we use just GWT Timer class, extending it purely to tell JAM it implements everything needed for a timer
	abstract class GWTTimer extends Timer implements IsTimerObject {

		//This whole override can be removed, it was just to check it was firing
		
		@Override
		public void cancel() {
			
			Log.info("canceling timer itself check");
			Log.info("hashcode "+this.hashCode());
			
			super.cancel();
			
		}		
		
	}
	
	//this method then gives a copy of the above when JAMCore needs one
	@Override
	public IsTimerObject getNewTimerClassImpl(final Runnable triggerThis) {
					
		return new GWTTimer(){
			@Override
			public void run() {
				triggerThis.run();
			}
			
		};
	}
	
	

	//---------
	//-------------------
	//--------------------------
	//Fixed timer implementation (used mostly for sound fades atm)
	//This timer is for scheduling a bunch of things to update at a fixed rate. (ie, locked framerate)
	//--

	static Timer tickTimer;
	//all timing subject to change this is at the moment just a temp implementation to get the interfacing right
		static long lastUpdateTime = 0;

	@Override
	protected void startFixedTimerIMPL() {

		if (tickTimer==null){
			tickTimer = new Timer() {
				@Override
				public void run() {

					//work out the time since the last update
					long currentTime = System.currentTimeMillis();
					long currentDelta =  currentTime - lastUpdateTime;
					lastUpdateTime = currentTime;


					DeltaTimerController.updateAllTickObjects(currentDelta);		
				}
			};
		}

		tickTimer.scheduleRepeating(FRAMEPERIOD);
		lastUpdateTime= System.currentTimeMillis();
		TimerRunning = true;
	}

	@Override
	protected void stopFixedTimerIMPL() {

		if (tickTimer!=null){
			tickTimer.cancel();
		}
	}




}



