package com.lostagain.JamGwt;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.JamAnswerBox;

public class GwtAnswerBox extends JamAnswerBox {

	static Logger Log = Logger.getLogger("JAM.GwtAnswerBox");
	public final TextBox widgetContents = new TextBox();

	public GwtAnswerBox() {
		super();
		
		//we add a handler to run a answer check when enter is hit
		widgetContents.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {

				int Key = event.getNativeKeyCode();
				
				if (Key == 13) {
					GwtAnswerBox.this.setText(JAM.RemoveCartReturns(	GwtAnswerBox.this.getText())
							.trim());

					// if over one letters scan if its correct answer
					if (	GwtAnswerBox.this.getText().length() > 1) {
						Log.info("answer given from keydown");
						JAMcore.AnswerGiven(	GwtAnswerBox.this.getText());
					}

				}

			}

		});

	}

	@Override
	public void setEnabled(boolean b) {
		widgetContents.setEnabled(b);
	}

	@Override
	public String getText() {
		return widgetContents.getText();
	}

	@Override
	public void setText(String text) {
		widgetContents.setText(text);;
	}

	@Override
	public void setFocus(boolean b) {
		widgetContents.setFocus(b);
	}

	@Override
	public Object getVisualRepresentation() {
		return widgetContents;
	}
	
	

	
}
