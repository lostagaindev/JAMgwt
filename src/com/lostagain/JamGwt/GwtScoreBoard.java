package com.lostagain.JamGwt;


import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.ScoreControll;

/** displays a score, counting up/down to match what it should me */
public class GwtScoreBoard extends ScoreControll implements IsWidget {
	
	HorizontalPanel visualRepresentation = new HorizontalPanel();
	
	Label ScoreLabel = new Label();
	
	//TODO; invisible when score is zero?
	
	public GwtScoreBoard(){
		super();

		//hide by default
		setVisible(false);
		
		visualRepresentation.setSize("100%", "100%");
		//add the background
		
		ScoreLabel.setStylePrimaryName("ScoreLabel");
				
		//and the score
		visualRepresentation.add(ScoreLabel);
		visualRepresentation.setCellHorizontalAlignment(ScoreLabel, HasHorizontalAlignment.ALIGN_CENTER);
		visualRepresentation.setSpacing(1);
		
		
		//should not update, as theres no need till a score has been set or added, and it would wastefully start a timer
		//UpdateScoreVisuals();
		
		updateVisualDisplayTo(CurrentScore); //we do however set the current score, as theres a chance it might have been altered before this widget was created
		//(unlikely though)
	}
	
	
	
	public void updateVisualDisplayTo(double  num) {
		
		ScoreLabel.setText(Long.toString((long)num));
	}
	
	@Override
	public Widget asWidget() {
		return visualRepresentation;
	}

	@Override
	public Object getVisualRepresentation() {
		return visualRepresentation;
	}



	@Override
	public void setVisible(boolean show) {
		visualRepresentation.setVisible(show);
	}

	
	
	
	
}
