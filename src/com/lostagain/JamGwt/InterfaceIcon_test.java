package com.lostagain.JamGwt;

import java.util.ArrayList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
/** shouldn't be needed anymore. AnimatedIcon should do this job **/
@Deprecated
public class InterfaceIcon_test extends Image {

	int frametotal = 1;
	int currentframe = 0;
	String animation_direction = "open";
	String basefilename ="";
	String originalfilename = "";
	
	String currentfilename ="";
	String filenameext = "png";
	boolean close_after_open = false;
	  final Timer timer;
	//image array
		int playuntill=100+frametotal;
	private boolean loop = false;
	
	ArrayList<Image> Frames = new ArrayList<Image>();

	//Image this_widget = this;
	
	// This is a special image that supports animations to be trigged when its rolled over.
	  public InterfaceIcon_test(String FileZeroLocation, int NumOfFrames){
		  
		  frametotal = NumOfFrames-1;
		  basefilename = FileZeroLocation.substring(0, (FileZeroLocation.indexOf(".")-1));
		  
		  originalfilename = FileZeroLocation.substring(FileZeroLocation.lastIndexOf("/")+1 ,  (FileZeroLocation.indexOf(".")));
		  
		  filenameext = FileZeroLocation.substring(FileZeroLocation.indexOf(".")+1);
		  
		// first we get all the images into the array
			System.out.print("frames-" + frametotal);
			for (int cp = 0; cp < (frametotal + 1); cp = cp + 1) {
				System.out.print("cp-" + cp);
				Image frame = new Image(
						(basefilename + "" + cp + "." + filenameext));
				Frames.add(frame);

			}
			
			
			//this_widget = Frames.get(0);
			//image.changeState(new UnclippedState(image, url));
			
		  currentfilename = FileZeroLocation;
		  //this.setWidth("100%");
		  
		  
		 
		  
		  //preload all images
		  
		  for (int cp=0;cp<frametotal; cp=cp+1){
			  
		  Image.prefetch((basefilename+""+cp+"."+filenameext));
		  
		  }
		  
		  final InterfaceIcon_test ThisIcon = this;
		  
		  timer = new Timer() { 				    
			

				@Override
				public void run() {
					 currentfilename= basefilename+""+currentframe+"."+filenameext;
					 ThisIcon.setUrl(currentfilename);

					  
						
					System.out.println("url="+basefilename+""+currentframe+"."+filenameext);
					if (animation_direction.compareTo("open")==0){
						currentframe=currentframe+1;				
						
					} else if (animation_direction.compareTo("close")==0){
						currentframe=currentframe-1;			
						
					} 
					
					//if out of range then stop, unless loop is set
					if (currentframe<0) {
						
						if (loop){
							currentframe=frametotal;
						}else {						
						currentframe=0;
						this.cancel();
						}
						
						
					}
					if (currentframe>playuntill) {
						currentframe=playuntill;
						playuntill=100+frametotal;
						this.cancel();
					}
					if (currentframe>frametotal) {
						currentframe=frametotal;
						
						if (close_after_open == true){
						animation_direction = "close";
						close_after_open = false;
						} else if (loop==true){
							currentframe=0;
						} 				
						else if (loop==false) {
						this.cancel();
						}
						
					}
					//Image.prefetch(basefilename+""+currentframe+"."+filenameext);
				}
		  };
	  }
	 
	  public void setURL(String FileZeroLocation, int NumOfFrames){
		  
		  this.setUrl(FileZeroLocation);
		  frametotal = NumOfFrames-1;
		  basefilename = FileZeroLocation.substring(0, (FileZeroLocation.indexOf(".")-1));
		  filenameext = FileZeroLocation.substring(FileZeroLocation.indexOf(".")+1);
		  originalfilename = FileZeroLocation.substring(FileZeroLocation.lastIndexOf("/")+1 ,  (FileZeroLocation.indexOf(".")));
		  
		  //preload all images
		  
		  for (int cp=0;cp<frametotal; cp=cp+1){
			  
		  Image.prefetch((basefilename+""+cp+"."+filenameext));
		  
		  }
		  
	  }
	  @Override
	  public String getUrl(){
		  return currentfilename;
	  }
	  
	  
	  public void setAnimateOpen(){
		  loop = false;
		  animation_direction = "open";
		  timer.scheduleRepeating(50);  
		  
		  
	  }
 public void setAnimateLoop(){
		  
		  animation_direction = "open";
		  loop  = true;
		  timer.scheduleRepeating(50);  
		  
		  
	  }
	  public void setAnimateClose(){
		  
		  animation_direction = "close";
		  loop = false;
		  timer.scheduleRepeating(50);
	  }
	  public void setAnimateOpenThenClose(){
		  loop = false;
		  animation_direction = "open";
		  close_after_open = true;
		  timer.scheduleRepeating(50);
		  
	  }
	  public void playUntill(int frame){		
		  loop = false;
	  playuntill = frame;
	  animation_direction = "open";
	  timer.scheduleRepeating(50);  
	  }
	  
	  public void nextFrameLoop(){
		  
		
			
		  currentframe=currentframe+1;
		  
		
		  if (currentframe>frametotal) {
				currentframe=0;
		  }
		  currentfilename= basefilename+""+currentframe+"."+filenameext;
		  this.setUrl(currentfilename);
		 // MyApplication.DebugWindow.addText("set frame to:"+currentfilename);
		  System.out.print(basefilename+""+currentframe+"."+filenameext);
	  }
	  public void prevFrameLoop(){
		  
			 
		  currentframe=currentframe-1;
		  if (currentframe<0) {
				currentframe=frametotal;
		  }
		  currentfilename= basefilename+""+currentframe+"."+filenameext;
		  this.setUrl(currentfilename);
		  System.out.print(basefilename+""+currentframe+"."+filenameext);
	  }
	  public void nextFrame(){
		  
			 
		  currentframe=currentframe+1;
		  if (currentframe>frametotal) {
				currentframe=frametotal;
		  }
		  currentfilename= basefilename+""+currentframe+"."+filenameext;
		  this.setUrl(currentfilename);
		  System.out.print(basefilename+""+currentframe+"."+filenameext);
	  }
	  public void firstFrame(){
		  
			 
		  currentframe=0;		  
		  currentfilename= basefilename+""+currentframe+"."+filenameext;
		  this.setUrl(currentfilename);
		  System.out.print(basefilename+""+currentframe+"."+filenameext);
	  }
}
