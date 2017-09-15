package com.lostagain.JamGwt.audio;

import java.util.logging.Logger;

import com.allen_sauer.gwt.voices.client.SoundType;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.OptionalImplementations;
import com.lostagain.Jam.audio.MusicBoxCore;

import lostagain.nl.spiffyresources.client.spiffygwt.SpiffyListBox;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * A horizontal page which lets the player control the games music.
 * Allows going left/right over the unlocked tracks
 * Designed for the cuypers code 
 ***/

public class GWTMusicBox extends MusicBoxCore implements IsWidget {

	HorizontalPanel visualRepresentation = new HorizontalPanel();
	
	public static Logger Log = Logger.getLogger("JAM.MusicBox");
	

	
	
	
	
	//static boolean fadeInRequested = false;
	
	
	
	/** music currently fading out **/
	//static Sound fadeingMusic; //Not used yet
	
	//purhapes extend current music into a track class? (this has a load listener by default, handles its own disposal and fades?)
	//static class MusicTrack extends Sound {
		
	//}
	

	//loop event
	static SoundHandler looper;

	//TempLabels
	final Label LeftPic = new Label("< ");
	final Label RightPic = new Label(" >");

	//Music Tack Title
	//static final Label CurrentMusicTrackLabel = new Label(" -NONE- ");
	public final SpiffyListBox CurrentMusicTrackLabel = new SpiffyListBox();
	
	
	

	
	//public static final ListBox test = new ListBox();

	public GWTMusicBox(){
		
		GwtAudioController.musicController.setPreferredSoundTypes(SoundType.HTML5,SoundType.WEB_AUDIO,  SoundType.FLASH); //In future shouldn't be needed 
		//NOTE: For some reason volume changing only works on HTML5 mode, not web audio
		
		//set event
	//	looper =  loopevent();
		// add widgets



		visualRepresentation.add(LeftPic);
		visualRepresentation.setCellVerticalAlignment(LeftPic, HasVerticalAlignment.ALIGN_BOTTOM );
		LeftPic.getElement().getStyle().setProperty("color", "FFFFFF");

		visualRepresentation.add(CurrentMusicTrackLabel);
		//SpiffyListBox testTest = new SpiffyListBox();
		//testTest.addItem("blah","blah");
		//this.add(testTest);

		CurrentMusicTrackLabel.setSize("100%","100%");
		CurrentMusicTrackLabel.ScrollContainer.setHeight("290px");

		CurrentMusicTrackLabel.setVisibleItemCount(1);
		//CurrentMusicTrackLabel.DropDownContainer.setAnimationEnabled(true);

		CurrentMusicTrackLabel.CurrentSelectedLab.setStyleName("MusicBoxDropDownLabel");		
		CurrentMusicTrackLabel.DropDownContainer.removeStyleName("SpiffyTextBox");
		CurrentMusicTrackLabel.ListContainer.setStyleName("MusicBoxDropDownPanel");
		CurrentMusicTrackLabel.hoverStyleName = "MusicBoxDropDownHover";

		//	CurrentMusicTrackLabel.getElement().getStyle().setProperty("zIndex", "99990");
		//Window.alert("spiffy text box added");
		//CurrentMusicTrackLabel.addItem("test");
		//CurrentMusicTrackLabel.addItem("test2");
		//	CurrentMusicTrackLabel.addItem("test3");
		//Window.alert("test items added");

		visualRepresentation.setCellHorizontalAlignment(CurrentMusicTrackLabel, HasHorizontalAlignment.ALIGN_CENTER);
		visualRepresentation.setCellVerticalAlignment(CurrentMusicTrackLabel, HasVerticalAlignment.ALIGN_BOTTOM );
		/* the following fails in IE:
		CurrentMusicTrackLabel.setWidth("100%");

		CurrentMusicTrackLabel.setHeight("100%");
		 */
		visualRepresentation.add(RightPic);
		visualRepresentation.setCellVerticalAlignment(RightPic, HasVerticalAlignment.ALIGN_BOTTOM );
		visualRepresentation.setCellHorizontalAlignment(RightPic, HasHorizontalAlignment.ALIGN_RIGHT);
		RightPic.getElement().getStyle().setProperty("color", "FFFFFF");

		//set width
		visualRepresentation.setSize("100%","100%");

		musicTracks.clear();
		//CurrentMusicTrackLabel.clear();
		//set first track to blank

	//	JAM.DebugWindow.info("/n starting music box-");

		//add no music label
		musicTracks.add(NO_MUSIC);
		CurrentMusicTrackLabel.addItem(NO_MUSIC,NO_MUSIC);	
		
		CurrentMusicTrackLabel.addChangeHandler(new ChangeHandler(){
			
			public void onChange(ChangeEvent event) {


				currentTrack=CurrentMusicTrackLabel.getSelectedIndex();
				CurrentMusicTrackLabel.setFocus(false);
				JAMcore.GameLogger.info("\n changing track to -"+currentTrack);
				System.out.print("\n changing track to -"+currentTrack);
				
				if (JAMcore.AnswerBox.isPresent()){					
					JAMcore.AnswerBox.get().setFocus(true);
				}
				//play			
				playtrack(currentTrack,currentMaxVolume);
				hasBeenSetByUser = true;
			}


		});

		//make buttons work
		LeftPic.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				prevTrack();
				hasBeenSetByUser = true;
			}

		});
		RightPic.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				nextTrack();
				System.out.print("bnl");
				hasBeenSetByUser = true;
			}


		});

		
		//ensure we are populated with all currently known tracks
		for (String track : musicTracks) {
		//	GwtAudioController.addMusicTrack(track,false,100,-1);			
			OptionalImplementations.addMusicTrack(track,false,100,-1);//	
			
		}

		
		//add this box to the list of all music boxs
		allMusicBoxs.add(this);
		
		

	}

	static void addMusicTrackToLabel(String label,String url) {
		
		Log.info("adding music track label to "+allMusicBoxs.size()+" music boxs");
		
		for (MusicBoxCore musicBoxinstance : allMusicBoxs) {    
			
			((GWTMusicBox)musicBoxinstance).CurrentMusicTrackLabel.addItem(label,musicTracks.get(musicTracks.size()-1).split("\\.")[0]);	
			
		}
		
		
	}

	
	public void setMusicBoxVisualForItemSelected(int ThisTrackNum) {
		//cast to our specific type
		GWTMusicBox musicBoxinstance = this;
		
		Log.info("Music Box Labels = "+musicBoxinstance.CurrentMusicTrackLabel.FieldNames.toString());			
		
		musicBoxinstance.CurrentMusicTrackLabel.setItemSelected(ThisTrackNum, true);	
		
		//if its track 0 hide the previous track  arrow
		if (ThisTrackNum==0){
			musicBoxinstance.LeftPic.setVisible(false);
		} else {
			musicBoxinstance.LeftPic.setVisible(true);
		}
		
		//if its the last tracj hide the next track arrow
		if (ThisTrackNum==(musicTracks.size()-1)){
			musicBoxinstance.RightPic.setVisible(false);
		} else {
			musicBoxinstance.RightPic.setVisible(true);
		}
	}
	/**
	 * 
	 * sets the currently playing label on all music boxs in the game as well as the "currentTrack" variable
	 * @param ThisTrackName
	 */
	static void setCurrentTrackTo(String ThisTrackName) {
		
		int trackNumber = musicTracks.indexOf(ThisTrackName);
		currentTrack = trackNumber;
		Log.info(ThisTrackName+" is track number = "+trackNumber+" tracklist is:"+musicTracks.toString());	
		setAllMusicTrackPlayingLabelTo(trackNumber);
		
		
	}


	@Override
	public Widget asWidget() {
		return visualRepresentation;
	}

	public void setSize(String width, String height) {
		visualRepresentation.setSize(width, height);
		
	}
	

}
