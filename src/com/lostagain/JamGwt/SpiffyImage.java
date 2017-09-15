package com.lostagain.JamGwt;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;

//image class with face and opacity settings.
//needs improvements and to be moved into SpiffyResources
public class SpiffyImage extends Image {
double opacity=100;
final Image ThisImage = this;

public void setOpacity(int NewOpacity){
	if (NewOpacity<100 &&(NewOpacity>-1)){
	opacity = NewOpacity;
	} else {
		System.out.print("Opacity out of range error");
	}
	Style style = ThisImage.getElement().getStyle();
	style.setProperty("filter", "alpha(opacity="+opacity+")");
	style.setProperty("opacity", ""+(opacity/100));
	
}
	public void FadeOut(int Over, int StartIn){
		
	
		final Timer fader = new Timer (){
			public void run() {
				System.out.print((opacity/100)+"\n");
				
				Style style = ThisImage.getElement().getStyle();
				style.setProperty("filter", "alpha(opacity="+opacity+")");
				style.setProperty("opacity", ""+(opacity/100));
				
				//ThisImage.getElement().setAttribute("style", " filter: alpha(opacity="+opacity+"); opacity: "+(opacity/100)+";");
				opacity=opacity-15;
				if (opacity<15){
					ThisImage.removeFromParent();
					this.cancel();
					
				}
				
				
				
			}
			
		};
		
		final int Delay = Over/7;
		
		Timer startin = new Timer (){
			public void run() {
				fader.scheduleRepeating(Delay);
			}
		};
		
		startin.schedule(StartIn);
		
	}
}
