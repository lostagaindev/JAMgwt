package com.lostagain.JamGwt.GwtJamImplementations;


import com.lostagain.Jam.Interfaces.HasImagePreloading;
import lostagain.nl.spiffyresources.client.spiffycore.RunnableWithString;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyPreloader;
import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyPreloader;


/**
 * simple class that lets the JAM preload images in advance.
 * We basically pass-though to the SpiffyResources GWT preloader for this
 * 
 * @author darkflame
 *
 **/
public class GWTImagePreloader implements HasImagePreloading {

	
	
	@Override
	public boolean addImageToLoading(String url,RunnableWithString runwhendone) {
		
		 return SpiffyPreloader.addToLoading(url, runwhendone);
			

	}

	@Override
	public void startPreloading() {
		SpiffyPreloader.preloadList();
	}

}
