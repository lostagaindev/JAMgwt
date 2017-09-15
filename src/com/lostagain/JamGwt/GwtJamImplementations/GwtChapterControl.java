package com.lostagain.JamGwt.GwtJamImplementations;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lostagain.Jam.GameManagementClass;
import com.lostagain.Jam.JAMcore;
import com.lostagain.Jam.SceneAndPageSet;
import com.lostagain.Jam.Interfaces.JamChapterControl;
import com.lostagain.JamGwt.GWTSceneAndPageSet;
import com.lostagain.JamGwt.JAM;

public class GwtChapterControl extends JamChapterControl implements IsWidget {

	public DecoratedTabPanel GamesChaptersPanel = new DecoratedTabPanel();

	public GwtChapterControl() {
		super();
		
		//set chapter panel style
		//(maybe move this into its gwt constructor?)
		GamesChaptersPanel.setStylePrimaryName("standard_message_back"); //$NON-NLS-1$
		// disable animation by default
		GamesChaptersPanel.setAnimationEnabled(false);

		Element bottompanel = (Element) GamesChaptersPanel.getElement().getFirstChild()
				.getChildNodes().getItem(1).getFirstChild().getFirstChild();
		
		//Log.info("adding overflow to bottom panel"
		//		+ bottompanel.getInnerHTML());
		bottompanel.getStyle().setProperty("overflow", "auto");
		bottompanel.getStyle().setProperty("overflow", "hidden");
		
	}
	
	//run seperately as it needs to wait for other sizes first
	//Probably this can be done better these days
	public void setupMoreStyleing(){
		GamesChaptersPanel.setSize("100%", "100%"); //$NON-NLS-1$ //$NON-NLS-2$

		GamesChaptersPanel.getDeckPanel().setSize("100%", JAM.Story_Text_Height + "px"); //$NON-NLS-1$ //$NON-NLS-2$
	
		GamesChaptersPanel.setStylePrimaryName("standard_message"); //$NON-NLS-1$

	}

	@Override
	public void setTabBarVisible(boolean b) {
		GamesChaptersPanel.getTabBar().setVisible(b);
	}

	@Override
	public Widget asWidget() {
		return GamesChaptersPanel;
	}

	@Override
	public void add(SceneAndPageSet gwtSceneAndPageSet, String name) {
		GamesChaptersPanel.add((GWTSceneAndPageSet)gwtSceneAndPageSet,name);
	}

	@Override
	public int getWidgetIndex(SceneAndPageSet gwtSceneAndPageSet) {
		return GamesChaptersPanel.getWidgetIndex((GWTSceneAndPageSet)gwtSceneAndPageSet);
	}

	@Override
	public void selectTab(int widgetIndex) {
		GamesChaptersPanel.selectTab(widgetIndex);
	}
	@Override
	public void setAnimationEnabled(boolean animationEffectsOn) {
		GamesChaptersPanel.setAnimationEnabled(animationEffectsOn);
		GamesChaptersPanel.getDeckPanel().setAnimationEnabled(animationEffectsOn);
	}

	@Override
	public void clear() {
		GamesChaptersPanel.clear();
	}

	//things just for gwts visual implementation
	public DeckPanel getDeckPanel() {
		return GamesChaptersPanel.getDeckPanel();
	}

	public void setSize(String width, String height) {
		GamesChaptersPanel.setSize(width, height);
		
	}

	public boolean isAttached() {
		return GamesChaptersPanel.isAttached();
	}

	@Override
	public void setStoryPageScrollBar(boolean b) {
		
		if (b){
			
			GamesChaptersPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
			GamesChaptersPanel.getDeckPanel().getElement().getStyle().setOverflow(Overflow.AUTO);

		} else {
			
			GamesChaptersPanel.getElement().getStyle().setOverflow(Overflow.HIDDEN);
			GamesChaptersPanel.getDeckPanel().getElement().getStyle().setOverflow(Overflow.HIDDEN);
		
			
		}
		
		
		
	}

	//lots of crude casting here
	//TODO:a fair bit of this can move to the supertype newchapter() function	
	public void newchapter_specificImplementation(String newChapterName) {
				
		// remove quotes
		newChapterName = newChapterName.substring(1,	(newChapterName.length() - 1));	
		JAM.GwtLog.info("Createing a new chapter called '" + newChapterName + "' \n"); 
		
		SceneAndPageSet NewChapter = new GWTSceneAndPageSet(newChapterName);
		GameManagementClass.AllSceneAndPageSets.add(NewChapter);
	
		// disable current tabs
		((GWTSceneAndPageSet)JAMcore.CurrentScenesAndPages).disableAllTabFunctions();
	
		JAMcore.currentOpenPages.clear();
		JAMcore.locationpuzzlesActive.clear();
	
		JAM.CurrentLocationTabsIndex  = GameManagementClass.AllSceneAndPageSets.indexOf(NewChapter);
		JAMcore.CurrentScenesAndPages = GameManagementClass.AllSceneAndPageSets.get(JAM.CurrentLocationTabsIndex);
		
		
		JAMcore.GamesChaptersPanel.add(JAMcore.CurrentScenesAndPages, newChapterName);
	
		JAM.ChapterList.addTab(newChapterName,	
				              JAMcore.GamesChaptersPanel.getWidgetIndex(((SceneAndPageSet)JAMcore.CurrentScenesAndPages))
				              );
		
		JAMcore.GamesChaptersPanel.selectTab(JAMcore.GamesChaptersPanel.getWidgetIndex(((SceneAndPageSet)JAMcore.CurrentScenesAndPages)));
	
	}

	
	
	
	
	
	
	
}
