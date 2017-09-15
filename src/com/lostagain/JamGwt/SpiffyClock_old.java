package com.lostagain.JamGwt;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.SimplePanel;

/** a basic canvas clock */
public class SpiffyClock_old extends SimplePanel  {
	
//
//	static Logger Log = Logger.getLogger("SpiffyClock");
//	//canvas
//	Canvas canvas = Canvas.createIfSupported();
//	Context2d drawplane;
//	
//	
//	//--
//	int radius = 0;
//	int ClockSizeY = 0;
//	String Mode = "Normal";
//	
//	//current angles
//	int hour_angle = 0;
//	int minute_angle = 0;
//	int second_angle = 0;
//	
//	//timer
//	Timer minite;
//	String PrevMode;
//
///** creates a basic canvas clock of a given size */
//	public SpiffyClock(int sizeX, int sizeY){
//		
//		if (canvas==null){
//			Log.info("no canvas supported for clock");
//			return;
//		}		
//		
//		this.setWidget(canvas);
//		this.setSize(sizeX+"px", sizeY+"px");
//		canvas.setWidth((sizeX/2) + "px");
//		canvas.setCoordinateSpaceWidth(sizeX);
//		canvas.setHeight((sizeY/2) + "px");      
//		canvas.setCoordinateSpaceHeight(sizeY);
//				
//		//get the drawingplane in the canvas element		
//		Context2d drawplane = canvas.getContext2d();
//		
//		radius = sizeX/2;		
//		ClockSizeY = sizeY;
//		
//		//set up basic parameters
//	//	drawplane.set (sizeX+"px", sizeY+"px");
//		
//		//drawplane.setBackgroundColor(Canvas.TRANSPARENT);		
//		drawplane.beginPath();
//		drawplane.setStrokeStyle("Black");
//		
//		//draw outer circle		
//		//this.arc(radius,radius,radius,0,(float)Math.PI*2,true); // Outer circle
//		
//		drawplane.stroke();
//		update();
//		
//		//set timer
//		
//		minite = new Timer(){
//
//			@Override
//			public void run() {
//				update();
//			}
//			
//		};
//		
//		
//		minite.scheduleRepeating(1000);
//			
//	}
//	
//	public void setModeFast(){
//		Mode = "Fast";
//		minite.scheduleRepeating(50);
//	}
//	public void setModeNormal(){
//		Mode = "Normal";
//		minite.scheduleRepeating(1000);
//	}
//	public void SetModeRandom(int Duration){
//		
//		//ignore if already random
//		if (Mode.compareTo("Random")==0){
//			return;
//		}
//		
//		 PrevMode = Mode;
//		 JAM.DebugWindow.addText("set old mode to "+PrevMode);
//			
//		Mode = "Random";
//		minite.scheduleRepeating(50);
//		
//		//normal after duration
//		Timer stopafter = new Timer(){
//
//			@Override
//			public void run() {
//				
//				Mode = PrevMode;
//				JAM.DebugWindow.addText("set clock to "+Mode);
//				
//				if (Mode.compareTo("Fast")==0){
//					minite.scheduleRepeating(50);
//				} else {
//				minite.scheduleRepeating(1000);
//				}
//			}
//			
//		};
//		stopafter.schedule(Duration);
//		
//	}
//	public void setRadius(int newradius){
//		radius = newradius/2;
//		
//
//		//set up basic parameters
//		this.setSize(newradius+"px", newradius+"px");
//	}
//	
//	
//	
//	
//	public void update()
//	{
//		
//		
//		if (Mode.compareTo("Fast")==0){
//			
//			second_angle=second_angle+30;
//			if (second_angle>360){
//				second_angle = 0;
//				minute_angle = minute_angle +15;
//				if (minute_angle>360){
//					minute_angle = 0;
//					hour_angle = hour_angle +5;
//					if (hour_angle>360){
//						hour_angle = 0;
//						
//						
//					}
//				}
//			}
//			
//			 
//		} else if(Mode.compareTo("Random")==0) { 
//		
//			second_angle = (int) (Math.random()*360);
//			minute_angle = (int) (Math.random()*360);
//			hour_angle = (int) (Math.random()*360);	
//		
//		} else {
//			//get time
//			 Date rightNow = new Date();
//			 
//			 //get hour
//			 int Hour = rightNow.getHours();
//			 
//			 if (Hour>12) {
//				 Hour = Hour-12;
//			 }
//			
//			 //hour angle
//			 hour_angle = Math.round(((Hour*360)/12));
//				
//			 //get minute
//			 int minute = rightNow.getMinutes();
//			 
//				
//			 //second angle
//			 minute_angle = Math.round(((minute*360)/60));		 
//			 //draw hour hand
//			 
//			 int second = rightNow.getSeconds();		 
//				
//			 //second angle
//			 second_angle = Math.round(((second*360)/60));	
//			
//			
//			
//		}
//		 
//		 
//		 
//		 setAngles(hour_angle, minute_angle, second_angle);
//		
//		
//	}
//
//	private void setAngles(int hour_angle, int minute_angle, int second_angle) {
//		double hour_angle_rads = hour_angle*(Math.PI/180);
//
//		 double minute_angle_rads = minute_angle*(Math.PI/180);
//		 
//		 double second_angle_rads = second_angle*(Math.PI/180);
//		 
//		 
//		 double hour_x = Math.sin(hour_angle_rads)*(radius/2); 
//		 double hour_y = Math.cos(hour_angle_rads)*(radius/2); 
//
//		 double min_x = Math.sin(minute_angle_rads)*(radius-3); 
//		 double min_y = Math.cos(minute_angle_rads)*(radius-3); 
//		 
//
//		 double sec_x = Math.sin(second_angle_rads)*(radius-1); 
//		 double sec_y = Math.cos(second_angle_rads)*(radius-1); 
//		 
//		 this.clear();		
//			
//			//draw hands
//		 drawplane.beginPath();
//		//draw outer circle		
//			//this.arc(radius,radius,radius,0,(float)Math.PI*2,true); // Outer circle
//		
//			
//		 drawplane.moveTo(radius, radius);
//		 drawplane.setLineWidth(4);
//		 drawplane.lineTo((radius)+hour_x,(radius)-hour_y);
//			
//		 drawplane.moveTo(radius, radius);
//		 drawplane.setLineWidth(2);
//		 drawplane.lineTo((radius)+min_x,(radius)-min_y);
//			
//		 drawplane.moveTo(radius, radius);
//		 drawplane.setLineWidth(1);
//		 drawplane.lineTo((radius)+sec_x,(radius)-sec_y);
//			
//		 drawplane.stroke();
//	}
//
//	

}
