package com.lostagain.JamGwt;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JamClueControll;

public class GwtClueBox extends JamClueControll implements IsWidget {

	ScrollPanel visualContents = new ScrollPanel();
	
	/** 
	 * Clue box contains the clues, but also a function used to return an imploded of what clues are already got.
	 * The format of this array is just;
	 * 
	 *  ClueName,Contents|ClueName2,Contents|ClueName3 etc etc
	 *  
	 *  The contents are also sent, because people are free to use the same clue names more then once, and if thats the case it needs to chec
	 *  the clue hasnt already been bought.
	 * 
	 **/
	
	
	HTML contents = new HTML();
	
	Grid ContainerGrid = new Grid(3,3);
	
	String display = "";
	String currentchapscanning="";
	public GwtClueBox(){
	
		visualContents.setSize("100%", "100%");
	ContainerGrid.setSize("100%", "100%");
	
	ContainerGrid.setWidget(1,1,contents);
	ContainerGrid.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
	
	ContainerGrid.setCellPadding(0);
	ContainerGrid.setCellSpacing(0);
	
	ContainerGrid.getCellFormatter().setWidth(0, 2, "26px");
	ContainerGrid.getCellFormatter().setWidth(0, 0, "11px");
	ContainerGrid.getCellFormatter().setHeight(0, 0, "41px");
	
	ContainerGrid.getCellFormatter().setWidth(1, 0, "11px");
	
	ContainerGrid.getCellFormatter().setHeight(2, 0, "9px");
	
	visualContents.setStylePrimaryName("clues");
	ContainerGrid.getCellFormatter().setStylePrimaryName(0, 0, "CluesTopLeft");
	ContainerGrid.getCellFormatter().setStylePrimaryName(0, 1, "CluesTopMid");
	ContainerGrid.getCellFormatter().setStylePrimaryName(0, 2, "CluesTopRight");
	
	ContainerGrid.getCellFormatter().setStylePrimaryName(1, 0, "CluesMidLeft");
	ContainerGrid.getCellFormatter().setStylePrimaryName(1, 2, "CluesMidRight");
	
	ContainerGrid.getCellFormatter().setStylePrimaryName(2, 0, "CluesBottomLeft");
	ContainerGrid.getCellFormatter().setStylePrimaryName(2, 1, "CluesBottomMid");
	ContainerGrid.getCellFormatter().setStylePrimaryName(2, 2, "CluesBottomRight");
	
	ContainerGrid.getCellFormatter().setStylePrimaryName(1, 1, "CluesBack");
	
	
	//ContainerGrid.getCellFormatter().setStylePrimaryName(1, 0, "CluesBottom");
	//ContainerGrid.getCellFormatter().setStylePrimaryName(1, 1, "CluesBottomRight");
	
	visualContents.setWidget(ContainerGrid);
	contents.setHTML("This is a box for clues");	
	
	}
	
	/**
	 * 
	 */
	@Override
	public void AddClueImplementation (String Clue,String ClueName,String ForChapter){
		
		//add to ass array, if not there already
		//if (!Clues.GetItem(ForChapter).equals(Clue)){
		//Clues.AddItem(Clue, ForChapter);
		//}
		System.out.print( "\n-----------------------------");
		if (!Clues.TestForMatch(ForChapter, Clue)){
		Clues.AddItem(ClueName+": "+Clue, ForChapter);
		}
		
		
		ArrayList<String> ClueChapters = Clues.getAllUniqueNames();
		display = "";
		
		for (Iterator<String>ClueChaptersit = ClueChapters.iterator(); ClueChaptersit.hasNext(); ) {
			
			String currentChapter= ClueChaptersit.next(); 
			display = display +"</br><b>"+currentChapter+"</b>;";
			
			//loop for each clue in chapter
		//	int i=0;
	     	ArrayList<String> CluesForChapter = Clues.GetAllItems(currentChapter);
			for (Iterator<String>CluesForChapterit = CluesForChapter.iterator(); CluesForChapterit.hasNext(); ) {
					
				String cluetoadd = CluesForChapterit.next();
			display = display +"<br>-"+cluetoadd;	
	
			}
			
			
		}
		
		
		contents.setHTML(display);
		
		
		
	}
	@Override
	public Widget asWidget() {
		return visualContents;
	}

	public void setSize(String w, String h) {
		visualContents.setSize(w, h);	
		}

	public void clear() {
		visualContents.clear();
		//shouldnt Clues (assarray) be cleared as well?
	}
}
