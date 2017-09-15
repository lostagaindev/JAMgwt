package com.lostagain.JamGwt;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.SceneAndPageSet;

public class VerticalTabs extends SimplePanel {
	
	
	public final VerticalPanel VTabPanel = new VerticalPanel();		
	String[] TAB_ROW_STYLES = {"tabTop", "tabMiddle"};		
	//array we keep of panel ints in list
	public final int[] PanelsToTriggerList = new int[100];

	
	
	public VerticalTabs()
	{
		this.setStylePrimaryName("overflowscroll");
		
		VTabPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		VTabPanel.setSpacing(3);
		
		VTabPanel.setWidth("100%");
		this.add(VTabPanel);
		
	}
	public void addTab(String Title, final int PanelToTrigger)
	{
					//DecoratedTabs title = new DecoratedTabs(Title);
		Label title = new Label(Title);
					 DecoratorPanel decPanel = new DecoratorPanel();
					 decPanel.setWidget(title);
					 
	  		 
					 
		VTabPanel.add(decPanel);
		PanelsToTriggerList[VTabPanel.getWidgetIndex(decPanel)] = PanelToTrigger;
		
		//bold by default
		Iterator<Widget> ChapterList = VTabPanel.iterator();		
		while (ChapterList.hasNext()){
			((DecoratorPanel)ChapterList.next()).getWidget().removeStyleName("bold");
			//MyApplication.DebugWindow.setText("setting tabs to normal");
		}
		
		//set this to bold
		title.addStyleName("bold");
		                    
	    final SceneAndPageSet TempCurrentLocationTabs = JAMcore.CurrentScenesAndPages;
		//if clicked on trigger panel
		title.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				
				Widget Sender = (Widget) event.getSource();
				
				JAMcore.GamesChaptersPanel.selectTab(PanelToTrigger);
								
								
				//set rest to normal
				Iterator<Widget> ChapterList = VTabPanel.iterator();
				
				while (ChapterList.hasNext()){
					((DecoratorPanel)ChapterList.next()).getWidget().removeStyleName("bold");
					//MyApplication.DebugWindow.setText("setting tabs to normal");
				}
				
				//set this to bold
				((Label)Sender).addStyleName("bold");
				
				
				/*
				//unselect rest
				VerticalPanel ParentPanel = (VerticalPanel) Sender.getParent().getParent().getParent();
				Iterator iter = ParentPanel.iterator();
				while ( iter.hasNext() ){
					Widget key = (Widget) iter.next();
					key.setStyleName("chapterlist_tab");
				}
				//.setStyleName("chapterlist_tab");
				
				//select this one
				Sender.getParent().getParent().setStyleName("chapterlist_tab_selected");
				*/
				if (TempCurrentLocationTabs.equals(JAMcore.CurrentScenesAndPages)) {
					JAMcore.AnswerBox.get().setEnabled(true);
				} else {
					
					JAMcore.AnswerBox.get().setEnabled(false);
					
				}
				
			}
		});			
		
	}
	public class DecoratedTabs extends SimplePanel
	{
		Label ThisTabsLabel = new Label("");
		Grid EdgeGrid = new Grid(1,3);
		
		public DecoratedTabs(String LabelTitle){
			ThisTabsLabel.setText(LabelTitle);
			ThisTabsLabel.setWidth("100%");
			ThisTabsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			
			//EdgeGrid.setBorderWidth(1);
			EdgeGrid.setSize("100%", "100%");
			
			EdgeGrid.setCellPadding(0);
			EdgeGrid.setCellSpacing(0);
			EdgeGrid.setWidget(0, 1, ThisTabsLabel);
			this.setStyleName("chapterlist_tab");
			//EdgeGrid.getCellFormatter().setStylePrimaryName(0, 0, "chapterlist_tab");		
			//EdgeGrid.getCellFormatter().setStylePrimaryName(0, 1, "chapterlist_tab");	
			//EdgeGrid.getCellFormatter().setHeight(0, 0, "3px");
			//EdgeGrid.getCellFormatter().setHeight(2, 0, "3px");
			
			
			this.add(EdgeGrid);
			
			
		}
	}
	
}
