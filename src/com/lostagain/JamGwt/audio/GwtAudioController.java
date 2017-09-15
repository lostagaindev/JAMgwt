package com.lostagain.JamGwt.audio;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.audio.JamAudioController;
import com.lostagain.Jam.audio.MusicBoxCore;
import com.lostagain.Jam.audio.JamAudioController.AudioType;
import com.lostagain.JamGwt.JAM;

/**
 * Eventually all the games audio should be controlled though here - music and sound effects alike
 * @author Tom
 *
 */
public class GwtAudioController extends JamAudioController {

	static Logger Log = Logger.getLogger("JAM.GwtAudioController");
	
//
//	private static final String DEFAULT_KEY_BEEP      = "CC2keybeep.mp3";
//	private static final String DEFAULT_SPACEKEY_BEEP = "CC2spacebeep.mp3";
//	
//	/** these are the generic sounds that play on dialogue boxs typing when no sound has been set **/
//	private static String KEY_BEEP = DEFAULT_KEY_BEEP;
//	/** these are the generic sounds that play on dialogue boxs typing when no sound has been set **/
//	private static String SPACEKEY_BEEP = DEFAULT_SPACEKEY_BEEP;	
//	/** these are the generic sounds that play on dialogue boxs typing when no sound has been set **/
//	private static String MESSAGE_KEY_BEEP = DEFAULT_KEY_BEEP;
//	/** these are the generic sounds that play on dialogue boxs typing when no sound has been set **/
//	private static String MESSAGE_SPACEKEY_BEEP = DEFAULT_SPACEKEY_BEEP;
//	
	
	
	/**
	 * A cache of all music to play or has been played.
	 * Note; Not the same as the visible track list as you can add tracks the user can't yet select
	 **/
	static HashMap<String,Track> MusicAndSoundCache = new HashMap<String,Track>();

	/** games sound controller **/
	static final SoundController musicController = new SoundController();

	// Sound music = musicController.createSound(Sound.MIME_TYPE_AUDIO_MPEG,MyApplication.audioLocation_url+"");

	// sound controller (for effects);
	public static final SoundController soundController = new SoundController();

	/** the last audio (any type) being played or faded in **/
	static Track lastTrackPlayed;
	
	/** the last music track played **/
	static Track lastMusicTrackPlayed;

	/**
	 * used to keep track of the currently playing sounds (NOT music)
	 * This lets us stop the sounds on demand separately to the music
	 * This might be removed in favor of just using MusicAndSound cache and checking type
	 */
	//public static ArrayList<Sound> CurrentPlayingSounds = new ArrayList<Sound>();


	/**
	 * ensures all music tracks are stopped
	 * @param trackTypeToStop - MUSIC to stop music, SOUNDEFFECT to stop effects, NULL to stop both
	 * @param ignoreThisOne - if you like you can specify a track not to stop 
	 * @param fadeOutOver - set the fade duration. -1 for unchanged.
	 */
	static public void stopAllTracks(boolean fadeOut,JamAudioController.AudioType trackTypeToStop, Track dontStopThisOne,int fadeOutOver){ //should add FadeOver option here

		//loop over and stop all known tracks;
		for (Track ctrack : MusicAndSoundCache.values()) {

			if (ctrack.currentType == trackTypeToStop || trackTypeToStop==null )
			{

				if (ctrack != dontStopThisOne){
					if (fadeOutOver!=-1){
						ctrack.setFadeTime(fadeOutOver);
						
					}
					ctrack.stop(fadeOut);
				}

			}
		}
	}
	/**
	 * All know tracks, both audio and music
	 * @return
	 */
	static public Collection<Track> getAllKnownTracks(){
		return MusicAndSoundCache.values();
	}

	/**
	 * cache a bit of audio for instant use later
	 * @param trackname
	 * @param asMusic
	 */
	 public void cacheAudio(String trackname,boolean asMusic){

	//	//String trackname= Track.split("\\.")[0];
		Log.info("cacheing track:"+trackname);

		Sound newsound =  musicController.createSound(JAM.defaultSoundType,JAM.audioLocation_url+trackname);
		Log.info("cacheing track in cache under this name:"+trackname);

		JamAudioController.AudioType audioType =  JamAudioController.AudioType.Music;
		if (asMusic){
			audioType = JamAudioController.AudioType.Music;
		} else {

			audioType = JamAudioController.AudioType.SoundEffect;
		}
		Track newMusic = new Track(newsound,trackname,MusicBoxCore.currentMaxVolume,audioType);

		MusicAndSoundCache.put(trackname,newMusic);


	}

	/**
	 * default to music type and no fade change
	 * 
	 * @param ThisTrackName
	 * @param targetVolume
	 * @param fadeInAndOut
	 */
	public void playMusicTrack(String ThisTrackName,int targetVolume,boolean fadeInAndOut){    	
		playAudioTrack(ThisTrackName,targetVolume,fadeInAndOut,JamAudioController.AudioType.Music,-1);
	}

	/**
	 * default to music type  and no fade change
	 * 
	 * @param ThisTrackName
	 * @param targetVolume
	 * @param fadeInAndOut
	 */
	public void playSound(String ThisTrackName,int targetVolume,boolean fadeInAndOut){ 

		if (JAM.SoundEffectOn){
			playAudioTrack(ThisTrackName,targetVolume,fadeInAndOut,JamAudioController.AudioType.SoundEffect,-1);
			
		}

	}

	/**
	 * Plays a audiotrack
	 * tracks are just what we call sound clips, they can be music(which loops) or soundeffects(that don't)
	 * If the sound isnt yet known it will be created and added to the internal cache.
	 * 
	 * This function will not, however, add a music track to the playlist box. That has to be done explicitly
	 * 
	 * 
	 * @param ThisTrackName
	 * @param targetVolume
	 * @param fadeInAndOut
	 * @param audiotype - music loops and gets added to the playlist, sound effects don't
	 * @param FadeOver - set how this track should fade if fade is turned on (-1 for leave fade as default)
	 */
	public void playAudioTrack_implementation(String ThisTrackName,int targetVolume,boolean fadeInAndOut, JamAudioController.AudioType audiotype, int FadeOver){    	

		Log.fine("requested to play music-"   +ThisTrackName+" at volume "+targetVolume);
		MusicBoxCore.currentMaxVolume=targetVolume;

		//if audiotype is sound effect, we check if sound effects are turned on, if not we return
		if (JamAudioController.AudioType.SoundEffect == audiotype && !JAM.SoundEffectOn){
			return;
		}

		if (ThisTrackName==null || ThisTrackName.isEmpty() || ThisTrackName.equalsIgnoreCase(MusicBoxCore.NO_MUSIC))
		{

			GWTMusicBox.setAllMusicTrackPlayingLabelTo(0);
			stopAllTracks(fadeInAndOut,null,null,FadeOver); //FadeOver?

		} else  {
			//	music.stop();
		//	Log.info("play track-"+ThisTrackName);
			//	music.removeEventHandler(looper);

			//primaryMusicState = MusicState.Preparing;
			lastTrackPlayed = MusicAndSoundCache.get(ThisTrackName);  

			if (lastTrackPlayed!=null){

		//		Log.info("playing "+ThisTrackName+" from cache:"); 							

			} else {    	

				Sound newMusicSource = musicController.createSound(JAM.defaultSoundType,JAM.audioLocation_url+ThisTrackName);

				lastTrackPlayed = new Track(newMusicSource,ThisTrackName,MusicBoxCore.currentMaxVolume,audiotype);

				Log.info("storing track in cache under this name:"+ThisTrackName);
				MusicAndSoundCache.put(""+ThisTrackName,GwtAudioController.lastTrackPlayed);

			}


			//if we are playing music we stop other tracks and set the music playlist label to this one
			if (audiotype==JamAudioController.AudioType.Music){

				//we stop other tracks if we are playing music
				stopAllTracks(fadeInAndOut,JamAudioController.AudioType.Music ,GwtAudioController.lastTrackPlayed,FadeOver); //we stop all tracks except the new one (as it might already be playing)
				//FadeOver? on above function too!
				
				//we now set the label on all music boxs in the game    		
				GWTMusicBox.setCurrentTrackTo(ThisTrackName);
				lastMusicTrackPlayed = lastTrackPlayed;
			}
			
		//	Log.info("Playing track "+ThisTrackName+" at volume "+MusicBoxCore.currentMaxVolume);
			

			lastTrackPlayed.setVolume(MusicBoxCore.currentMaxVolume);	
			
			if (FadeOver!=-1){
				lastTrackPlayed.setFadeTime(FadeOver);
			}
			lastTrackPlayed.play(fadeInAndOut);

		}

	}
	
	
	
	public void playtrack_implementation(String ThisTrackName){    
		playMusicTrack(ThisTrackName,100,true);
	}


	//Eventually replace this with just the normal cache list?
	public void stopAllSoundEffects_implementation() {

		stopAllTracks(false, JamAudioController.AudioType.SoundEffect, null,-1);

		/*
		Iterator<Sound> it = CurrentPlayingSounds.iterator();

		while (it.hasNext()) {

			Sound currentSoundToStop = it.next();
			currentSoundToStop.stop();
		}*/

	}





	public void setCurrentMusicVolume(int Vol){

	//	Log.info("setCurrentMusicVolume to "+Vol);

		MusicBoxCore.currentMaxVolume=Vol;
				
		lastMusicTrackPlayed.setVolume(MusicBoxCore.currentMaxVolume);
	}


	@Override
	public void addMusicTrack_implementation(String Track, boolean autoPlay, int Volume,int fadeOver){	

		if (MusicBoxCore.musicTracks.contains(Track)){			
			Log.info("track:"+Track+" already in music tracklist");
			//we still play it though
			playAudioTrack(Track,Volume,true,JamAudioController.AudioType.Music,fadeOver);		
			
			return;
		}
		Log.info("adding track:"+Track);

		MusicBoxCore.musicTracks.add(Track);		

		String trackname = MusicBoxCore.musicTracks.get(MusicBoxCore.musicTracks.size()-1).split("\\.")[0]; //crop filename
		if (trackname.contains("\\")){
			trackname=trackname.substring(trackname.indexOf("\\")+1);	
		}
		if (trackname.contains("/")){
			trackname=trackname.substring(trackname.indexOf("/")+1);	
		}

		String trackurl  = MusicBoxCore.musicTracks.get(MusicBoxCore.musicTracks.size()-1);

		GWTMusicBox.addMusicTrackToLabel(trackname,trackurl);

		//if its not set to none play it


		//we auto play if requested AND;
		//a) the current track is not set to NONE
		//or the user has never event selected a track yet
		Log.info("currentTrack="+MusicBoxCore.currentTrack+" : "+GWTMusicBox.musicHasBeenSelectedOnce()+":"+autoPlay);
		if ((MusicBoxCore.currentTrack>0 || !GWTMusicBox.musicHasBeenSelectedOnce() ) && autoPlay){ //|| (!this.isOrWasAttached())

			MusicBoxCore.currentTrack = MusicBoxCore.musicTracks.size()-1;

			//CurrentMusicTrackLabel.setItemSelected(CurrentMusicTrackLabel.getItemCount()-1,true);

			//setMusicTrackPlayingLabelTo(CurrentMusicTrackLabel.getItemCount()-1);

			JAMcore.GameLogger.info(":"+MusicBoxCore.musicTracks.get(MusicBoxCore.currentTrack));

			//currentTrack=CurrentMusicTrackLabel.getSelectedIndex();
			//	CurrentMusicTrackLabel.setFocus(false);

			//play	
		//	Log.info("Playing:"+MusicBoxCore.currentTrack);

			GWTMusicBox.playtrack(MusicBoxCore.currentTrack,Volume,fadeOver);
		}

	}
	public static String getLastTrackName() {		
		return lastMusicTrackPlayed.name;
	}

}
