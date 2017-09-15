package com.lostagain.JamGwt.audio;

import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.Sound.LoadState;
import com.allen_sauer.gwt.voices.client.SoundType;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;

import com.google.common.collect.Sets;
import com.lostagain.Jam.JAMTimerController;
import com.lostagain.Jam.audio.JamAudioController;
import com.lostagain.JamGwt.JargScene.debugtools.GameDataBox;

import lostagain.nl.spiffyresources.client.spiffycore.HasDeltaUpdate;

public class Track implements HasDeltaUpdate {

	
	public static Logger Log = Logger.getLogger("JAM.Track");
	Sound TracksSound;
	String name;
	/**
	 * represents the current maximum volume this will be played at
	 */
	int MaxVolume = 100;
	/**
	 * actually volume (this might be less then max if fading in/out)
	 */
	float CurrentVolume=MaxVolume;

	/**
	 * If fading in or out, this was the time the event started
	 * 
	 */
	long fadeStartTime = 0;
	
	/**
	 * Default fade is a slow 8000 ms
	 */
	final static float DefaultFadeLength = 8000f;
	/**
	 * the amount the fade changes per ms (currently fading over 8 seconds by default
	 */
	float stepPerMS = (100.0f/DefaultFadeLength); //(Max volume value/duration of fade in ms)

	enum AudioState {
		NoMusic,Stopped,Preparing,Loading,FadeingIn,Playing,FadeingOut
	}

	AudioState currentState = AudioState.NoMusic;

	/**
	 * music loops by default, sound effects dont
	 */
	JamAudioController.AudioType currentType = JamAudioController.AudioType.SoundEffect;
	private boolean fadeInRequested = false;



	/**
	 * list of currently fading in/out sounds (really only one or two things should be on this list at a time)
	 */
	static HashSet<Track> fadeingSounds = Sets.newHashSet();


	/**
	 * 
	 * @param tracksSound
	 * @param name
	 * @param maxVolume
	 * @param type
	 */

	public Track(Sound tracksSound,String name, int maxVolume,JamAudioController.AudioType type) {
		super();
		this.name=name;
		currentType = type;
		TracksSound = tracksSound;
		MaxVolume = maxVolume;
		setStateTo(AudioState.Stopped);
		TracksSound.addEventHandler(loopevent());   
	}

	public void setVolume(int currentMaxVolume) {
		MaxVolume = currentMaxVolume;

		if (currentState==AudioState.Playing){			
			TracksSound.setVolume(MaxVolume);
			CurrentVolume = MaxVolume;
		}

	}

	public String getUrl() {
		return TracksSound.getUrl();
	}


	public void stop(boolean FadeOut) {
		if (!FadeOut){
			TracksSound.stop();
			setStateTo(AudioState.Stopped);
		} else {			
			this.startFadeOut();			
		}
	}

	/**
	 * Track url, state, MaxVolume (good for debugging)
	 */
	public String toString(){
		return name+","+currentState.name()+",Vol="+CurrentVolume+"/"+MaxVolume;
	}


	public void play(boolean FadeIn) {
		
		//ensure we are not playing already
		if (currentState == AudioState.Playing || currentState == AudioState.FadeingIn ){			
			//Log.info("already playing track:"+name);
			return;
			
		}

		//set the starting volume depending if we are fading in or not
		if (!FadeIn){
			TracksSound.setVolume(MaxVolume); //set volume to max volume unless we are fading in
		} else {
			TracksSound.setVolume(0); //set volume to zero for fader start			
			fadeInRequested = true; //tell it a fade has been requested (Note; we cant set the state yet as the sound might not have been loaded! Fade should only start after loading)

		}
		
		//try to play, and store success of not in variable
		boolean Succeeded = TracksSound.play();
		
		//Log.info("played track Succeeded:"+Succeeded);
		if (Succeeded){
			setStateTo(AudioState.Playing);		
			if (fadeInRequested){
				startFadeIn(); //start the fade in if its loaded and ready (ie, playing succeeded)
			}
		} else {
			if (TracksSound.getLoadState() == LoadState.LOAD_STATE_SUPPORTED_NOT_READY) {
				setStateTo(AudioState.Loading);
			}
		}

		//Log.info("track state:"+currentState.toString()+" url:"+TracksSound.getUrl()+" type:"+TracksSound.getSoundType());

	}
	private void startFadeOut() {
		
		//ensure we are not on WEB_AUDIO, sadly fades dont work on web audio at the moment (problem in gwt-voices? its not me anyway)
				if (TracksSound.getSoundType()==SoundType.WEB_AUDIO){
					//no fade, full volume straight away
					setStateTo(AudioState.Stopped);
					TracksSound.setVolume(MaxVolume);
					CurrentVolume=MaxVolume;
					return;
				}
				
				setStateTo(AudioState.FadeingOut);
				JAMTimerController.addObjectToUpdateOnTick(this);
				fadeingSounds.add(this);
				
				
	}
	private void startFadeIn() {
		fadeInRequested=false; //as we have started the fadein its no longer requested. its the current state instead.
		
		//ensure we are not on WEB_AUDIO, sadly fades dont work on web audio at the moment (problem in gwt-voices? its not me anyway)
		if (TracksSound.getSoundType()==SoundType.WEB_AUDIO){
			//no fade, full volume straight away
			setStateTo(AudioState.Playing);
			TracksSound.setVolume(MaxVolume);
			CurrentVolume=MaxVolume;
			return;
		}
				

		setStateTo(AudioState.FadeingIn);
		TracksSound.setVolume(0); //set volume to zero for fader start
		CurrentVolume=0;
		fadeingSounds.add(this);//add it to the static list of fades to process

		//this is added to the JAMTimer in order to trigger regular updates
		JAMTimerController.addObjectToUpdateOnTick(this);


	}

	/**
	 * Updates the volume state of all tracks fading in/out
	 * @param currenTime
	 *
	static public void updateAllTracks(float delta){
	//	Log.info("updateing tracks:"+delta);
		
		Iterator<Track> iterator = fadeingSounds.iterator();
		while (iterator.hasNext()) {
			Track track = iterator.next();
			
			updateThisTracksFade(delta, iterator, track);		
		}
		
		

	}*/

	public static void updateThisTracksFade(float delta, Track track) {

		track.updateFade(delta);
		
		if (   track.currentState!=AudioState.FadeingIn 
			&& track.currentState!=AudioState.FadeingOut){
			
			//iterator.remove(); //removes this track from the fadeing sounds list
			
			fadeingSounds.remove(track);
			
			//also remove fromJAMTimer which fired this update
			JAMTimerController.removeObjectToUpdateOnTick(track);
			return;

		}
		
	}

	/**
	 * All state changing goes via here. This is purely to update a inspector if open so we can have realtime feedback on whats
	 * playing, whats fading, etc
	 * 
	 * @param state
	 */
	private void setStateTo(AudioState state){

		currentState=state;
		GameDataBox.updateMusic();
	}
	/**
	 * 
	 * @param delta - time since last update in ms
	 */
	private void updateFade(float delta){
	
		//work out how much to fade in/out by which is just step x delta
		float fadeChangeAmount = stepPerMS * delta; 
	//	Log.info("fade change amount:"+fadeChangeAmount+" cur="+CurrentVolume+" MaxVolume="+MaxVolume+" currentState="+currentState);  

		

		switch (currentState) {
		case FadeingIn:
			CurrentVolume = CurrentVolume + fadeChangeAmount;
			//Log.info("CurrentVolume now:"+CurrentVolume);
			if (CurrentVolume>MaxVolume){
				CurrentVolume=MaxVolume;
				setStateTo(AudioState.Playing);
			}
			TracksSound.setVolume((int) CurrentVolume);
			break;
		case FadeingOut:
			CurrentVolume = (CurrentVolume - fadeChangeAmount);
			//Log.info("CurrentVolume now::"+CurrentVolume);
			if (CurrentVolume<1){
				CurrentVolume=0;
				setStateTo(AudioState.Stopped);
				
			}
			TracksSound.setVolume((int) CurrentVolume);
			break;
		default:
			break;
		}

		//Log.info("new volume:"+CurrentVolume);

	}

	private SoundHandler loopevent() {

		return new SoundHandler(){

			public void onPlaybackComplete(PlaybackCompleteEvent event) {

				if (currentType==JamAudioController.AudioType.Music && (currentState == AudioState.Playing || 
													currentState == AudioState.FadeingIn || 
													currentState == AudioState.FadeingOut )){
					//System.out.print("\n looping music-");
					TracksSound.play();
				} else {
					//if we arnt supposed to loop we just set the state as stopped
					currentState = AudioState.Stopped;
				}


			}

			public void onSoundLoadStateChange(
					SoundLoadStateChangeEvent event) {
				// if its only just loaded check if we should be playing and then play
				if (event.getLoadState() == LoadState.LOAD_STATE_SUPPORTED_AND_READY) {
					if (TracksSound == event.getSource()){


						//if we arnt fading set volume and play
						if (!fadeInRequested){

						//	Log.info("now loaded, so playing it;");
							TracksSound.setVolume(MaxVolume);
							TracksSound.play();
							setStateTo(AudioState.Playing);
							
						} else {

						//	Log.info("now loaded, so fading  it in;");
							startFadeIn();
						}




					} else {

						Log.severe("new music loaded but objects dont match"); //should never happen

					}

				} else {
					Log.info("Music load state is:"+event.getLoadState());
				}

			}

		};
	}

	@Override
	public void update(float delta) {
	//	updateAllTracks(delta);
		
		updateThisTracksFade(delta,this);
		
	}

	/**
	 * Sets the duration of the next fade (either in or out) 
	 * as well as the default for fades after that.
	 * 
	 * @param fadeDurationMS
	 */
	public void setFadeTime(float fadeDurationMS){
		stepPerMS = (100.0f/fadeDurationMS); //volume % / fade duration = vol change per ms
		
		//note; wont be accurate if we arnt fadeing from or two 100%
	}





}
