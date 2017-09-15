package com.lostagain.JamGwt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

/**
 * This is a special image that supports animations to be trigged when its
 * rolled over.
 * NOTE: USE AnimatedIcon.java instead *
 */
@Deprecated
public class SpiffyIcon extends Image {


	 static Logger Log = Logger.getLogger("SpiffyIcon");
	 
	int frametotal = 1;
	int currentframe = 0;
	String animation_direction = "open";
	String basefilename = "";
	String originalfilename = "";

	String currentfilename = "";
	String filenameext = "png";
	boolean close_after_open = false;
	final Timer timer;
	// image array
	int playuntill = 100 + frametotal;
	private boolean loop = false;

	//ArrayList<Image> Frames = new ArrayList<Image>();
	
	List<AbstractImagePrototype> Frames = new ArrayList<AbstractImagePrototype>();
	List<String> FrameURLS = new ArrayList<String>();
	
//	MouseHandler MouseListener;
//	ClickHandler ClickListener;	
	
	
    public Boolean BundleImageMode = false;
    
	final SpiffyIcon ThisIcon = this;
	/**
	 * This is a special image that supports animations to be trigged when its
	 * rolled over.
	 * NOTE: USE AnimatedIcon.java instead *
	 */
	@Deprecated
	public SpiffyIcon(AbstractImagePrototype[] SetFrames){
		super();
		
		BundleImageMode = true;
	    
		System.out.print("\n adding icons:" );
		

		frametotal = (SetFrames.length -1);
		
		for (int cp = 0; cp < (frametotal+1); cp = cp + 1) {
		Frames.add(SetFrames[cp]);
		//System.out.print("\n adding"+SetFrames[cp].getUrl() );
		
		}
		
		//this.setWidget(Frames.get(0));
		Frames.get(0).applyTo(this);
		
		//System.out.print("\n Current="+((Image)this.getWidget()).getUrl() );
		
		timer = new UpdateFrame(ThisIcon);
		
	}
	public SpiffyIcon(String FileZeroLocation, int NumOfFrames) {

		frametotal = NumOfFrames - 1;
		basefilename = FileZeroLocation.substring(0, (FileZeroLocation
				.indexOf(".") - 1));

	//	Log.info("basefilename filename = "+originalfilename);
		
		originalfilename = FileZeroLocation.substring(FileZeroLocation
				.lastIndexOf("/") + 1, (FileZeroLocation.indexOf(".")));

	//	Log.info("original filename = "+originalfilename);
		
		filenameext = FileZeroLocation
				.substring(FileZeroLocation.indexOf(".") + 1);

		// first we get all the images into the array
		//Log.info("frametotal = "+frametotal);
		
		for (int cp = 0; cp < (frametotal + 1); cp = cp + 1) {
			System.out.print("cp-" + cp);
			//Image frame = new Image(
				//	(basefilename + "" + cp + "." + filenameext));
			
			FrameURLS.add(basefilename + "" + cp + "." + filenameext);
			

		}

		
		// set this to the first frame
	//	Log.info("setUrl start=" +FrameURLS.get(currentframe));
		
		this.setUrl(FrameURLS.get(0));

		currentfilename = FileZeroLocation;
		// this.setWidth("100%");


		timer = new UpdateFrame(ThisIcon);
		

		//Log.info("created icon");
	}
	public void setFrames(AbstractImagePrototype[] SetFrames) {

		Frames.clear();
		frametotal = SetFrames.length-1;
		
		for (int cp = 0; cp < (frametotal+1); cp = cp + 1) {
		//AbstractImagePrototype frame = SetFrames[cp];
			
		Frames.add(SetFrames[cp]);
		
		//System.out.print("\n adding"+SetFrames[cp].getUrl() );		
		}
		
		Frames.get(0).applyTo(ThisIcon);
		
		//System.out.print("\n Current="+((Image)this.getWidget()).getUrl() );
		
		
	}

	public void setURL(String FileZeroLocation, int NumOfFrames) {

		BundleImageMode = false;
		
		frametotal = NumOfFrames - 1;
		basefilename = FileZeroLocation.substring(0, (FileZeroLocation
				.indexOf(".") - 1));
		filenameext = FileZeroLocation
				.substring(FileZeroLocation.indexOf(".") + 1);
		
		originalfilename = FileZeroLocation.substring(FileZeroLocation
				.lastIndexOf("/") + 1, (FileZeroLocation.indexOf(".")));
		
	
		// preload all images
		//remove existing ones;
		FrameURLS.clear();
		
		// first we get all the images into the array
		for (int cp = 0; cp < (frametotal+1); cp = cp + 1) {

			//Image frame = new Image((basefilename + "" + cp + "." + filenameext));
			
			
			
			
			FrameURLS.add((basefilename + "" + cp + "." + filenameext));
			
		}
		Log.info("setUrl==" +FrameURLS.get(currentframe));
		
		super.setUrl(FrameURLS.get(currentframe));
		
	}

	@Override
	public String getUrl() {
		return currentfilename;
	}

	public void setAnimateOpen() {
		loop = false;
		animation_direction = "open";
		timer.scheduleRepeating(50);

	}

	public void setAnimateLoop() {

		animation_direction = "open";
		loop = true;
		timer.scheduleRepeating(50);

	}

	public void setAnimateClose() {

		animation_direction = "close";
		loop = false;
		timer.scheduleRepeating(50);
	}

	public void setAnimateOpenThenClose() {
		loop = false;
		animation_direction = "open";
		close_after_open = true;
		timer.scheduleRepeating(85);

	}

	public void playUntill(int frame) {
		loop = false;
		playuntill = frame;
		animation_direction = "open";
		timer.scheduleRepeating(50);
	}

	public void nextFrameLoop() {

		currentframe = currentframe + 1;

		if (currentframe > frametotal) {
			currentframe = 0;
		}
		currentfilename = basefilename + "" + currentframe + "." + filenameext;
		if (BundleImageMode){
			Frames.get(currentframe).applyTo(this);
		} else {
			Log.info("setUrl =" +FrameURLS.get(currentframe));
			
		this.setUrl(FrameURLS.get(currentframe));
		}
		// MyApplication.DebugWindow.addText("set frame to:"+currentfilename);
		System.out.print(basefilename + "" + currentframe + "." + filenameext);
	}

	public void prevFrameLoop() {

		currentframe = currentframe - 1;
		if (currentframe < 0) {
			currentframe = frametotal;
		}
		currentfilename = basefilename + "" + currentframe + "." + filenameext;
		if (BundleImageMode){
			Frames.get(currentframe).applyTo(this);
		} else {
			Log.info("setUrl0=" +FrameURLS.get(currentframe));
			
		this.setUrl(FrameURLS.get(currentframe));
		}
		System.out.print(basefilename + "" + currentframe + "." + filenameext);
	}

	public void nextFrame() {

		currentframe = currentframe + 1;
		if (currentframe > frametotal) {
			currentframe = frametotal;
		}
		currentfilename = basefilename + "" + currentframe + "." + filenameext;
		if (BundleImageMode){
			Frames.get(currentframe).applyTo(this);
		} else {
			Log.info("setUrl1=" +FrameURLS.get(currentframe));
			
		this.setUrl(FrameURLS.get(currentframe));
		}
		System.out.print(basefilename + "" + currentframe + "." + filenameext);
	}

	public void firstFrame() {

		currentframe = 0;
		currentfilename = basefilename + "" + currentframe + "." + filenameext;

		if (BundleImageMode){
			Frames.get(currentframe).applyTo(this);
		} else {
			Log.info("setUrl2=" +FrameURLS.get(currentframe));
			
		this.setUrl(FrameURLS.get(currentframe));
		}
		
		System.out.print(basefilename + "" + currentframe + "." + filenameext);
	}

	
private final class UpdateFrame extends Timer {
	private final SpiffyIcon thisIcon;

	private UpdateFrame(SpiffyIcon thisIcon) {
		this.thisIcon = thisIcon;
	}

	@Override
	public void run() {
		// currentfilename=
		// basefilename+""+currentframe+"."+filenameext;
		// ThisIcon.setUrl(currentfilename);

		
		if (BundleImageMode){
			if (Frames.size()>currentframe){
			Frames.get(currentframe).applyTo(thisIcon);
			}
		} else {
		//	Log.info("getting url=" +FrameURLS.get(currentframe));
			
		thisIcon.setUrl(FrameURLS.get(currentframe));
		}
		
	
		if (animation_direction.compareTo("open") == 0) {
			currentframe = currentframe + 1;

		} else if (animation_direction.compareTo("close") == 0) {
			currentframe = currentframe - 1;

		}

		// if out of range then stop, unless loop is set
		if (currentframe < 0) {

			if (loop) {
				currentframe = frametotal;
			} else {
				currentframe = 0;
				this.cancel();
			}

		}
		if (currentframe > playuntill) {
			currentframe = playuntill;
			playuntill = 100 + frametotal;
			this.cancel();
		}
		if (currentframe > frametotal) {
			currentframe = frametotal;

			if (close_after_open == true) {
				animation_direction = "close";
				close_after_open = false;
			} else if (loop == true) {
				currentframe = 0;
			} else if (loop == false) {
				this.cancel();
			}

		}
		// Image.prefetch(basefilename+""+currentframe+"."+filenameext);
	}
}
}
