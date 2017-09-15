package com.lostagain.JamGwt;

import com.google.gwt.user.client.Timer;
/**
public class GwtTimerFactory extends TimerObjectFactory {
	
//combined into the JAMTimerController, simply be giving that a getNewTimerClassImpl method
	
	public static void setup(){
		TimerObjectFactory.setup(new GwtTimerFactory());
		
	}
	
	@Override
	public IsTimerObject getNewTimerClassImpl(final Runnable triggerThis) {
					
		return new GWTTimer(){
			@Override
			public void run() {
				triggerThis.run();
			}
			
		};
	}
	
	
	abstract class GWTTimer extends Timer implements IsTimerObject {		
		
	}

	

}*/
import com.lostagain.Jam.Factorys.IsTimerObject;
import com.lostagain.Jam.Factorys.TimerObjectFactory;

