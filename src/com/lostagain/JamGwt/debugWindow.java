package com.lostagain.JamGwt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

@Deprecated
public class debugWindow extends PopupPanel{

	Label debugtext = new Label("debug window");
	
	public debugWindow(){
		
		this.add(debugtext);
		this.setSize("25%", "55%");
		this.setStylePrimaryName("standardframe");
		
		
		
	}
	public void addText(String text){		
		debugtext.setText(debugtext.getText()+"\n"+text);		
		
	}
	public void clearText(){
		debugtext.setText("");
	}
	public void setText(String text){
		debugtext.setText(text);
	}
}
