package com.lostagain.JamGwt.IconPacks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;


/** Images for the game engine, simply extend a group of images you want to use **/
/** Currently has following iconsets possible: 
 *   
 * ThornIcons  
 * CuypersImages (note: resource names outdated, remove cc2 from  BIGrugzakje0 etc) 
 * MemDayImages   
 * mwaCIcons   
 * 
 * **/
public interface JamImages extends mwaCIcons {
		
	static JamImages JargStaticImages = (JamImages) GWT.create(JamImages.class);
	
	
	    // close all windows button
		static AbstractImagePrototype[] CloseAll = {
				AbstractImagePrototype.create(JargStaticImages.closeall0()),
				AbstractImagePrototype.create(JargStaticImages.closeall1()),
				AbstractImagePrototype.create(JargStaticImages.closeall2()) };
		
		// Logo (only used for cuypers at the moment)
		static AbstractImagePrototype[] BigCuypers2LogoImages = {
				AbstractImagePrototype.create(JargStaticImages
						.BIGTitleStripRight0()),
				AbstractImagePrototype.create(JargStaticImages
						.BIGTitleStripRight1()),
				AbstractImagePrototype.create(JargStaticImages
						.BIGTitleStripRight2()) };

		static AbstractImagePrototype[] MediumCuypers2LogoImages = {
				AbstractImagePrototype.create(JargStaticImages
						.MEDIUMTitleStripRight0()),
				AbstractImagePrototype.create(JargStaticImages
						.MEDIUMTitleStripRight1()),
				AbstractImagePrototype.create(JargStaticImages
						.MEDIUMTitleStripRight2()) };

		static AbstractImagePrototype[] SmallCuypers2LogoImages = {
				AbstractImagePrototype.create(JargStaticImages
						.SMALLTitleStripRight0()),
				AbstractImagePrototype.create(JargStaticImages
						.SMALLTitleStripRight1()),
				AbstractImagePrototype.create(JargStaticImages
						.SMALLTitleStripRight2()) };
		
		// -------------- (end of animated images)		
		//--------------------------------------------------------------------------------------------
		
		//Now the non-animated images;
		
		// decoration images
		static AbstractImagePrototype[] LadyClockSmall = { AbstractImagePrototype
				.create(JargStaticImages.StatueHeadSmall()) };
		static AbstractImagePrototype[] LadyClockMid = { AbstractImagePrototype
				.create(JargStaticImages.StatueHeadMid()) };
		static AbstractImagePrototype[] LadyClockBig = { AbstractImagePrototype
				.create(JargStaticImages.StatueHeadBig()) };

		static AbstractImagePrototype[] LadyFeatSmall = { AbstractImagePrototype
				.create(JargStaticImages.StatueFeatSmall()) };
		static AbstractImagePrototype[] LadyFeatMid = { AbstractImagePrototype
				.create(JargStaticImages.StatueFeatMid()) };
		static AbstractImagePrototype[] LadyFeatBig = { AbstractImagePrototype
				.create(JargStaticImages.StatueFeatBig()) };

		static AbstractImagePrototype[] SoldierSmall = { AbstractImagePrototype
				.create(JargStaticImages.SoldierSmall()) };
		static AbstractImagePrototype[] SoldierMid = { AbstractImagePrototype
				.create(JargStaticImages.SoldierMid()) };
		static AbstractImagePrototype[] SoldierBig = { AbstractImagePrototype
				.create(JargStaticImages.SoldierBig()) };
		

		// Top Strip, containing the title, score, but etc (left to right)

		// Leftmost Bit
		final static AbstractImagePrototype BigLeftBit = AbstractImagePrototype
				.create(JargStaticImages.BIGTitleStripleftbit());
		final static AbstractImagePrototype MediumLeftBit = AbstractImagePrototype
				.create(JargStaticImages.MEDIUMTitleStripleftbit());
		final static AbstractImagePrototype SmallLeftBit = AbstractImagePrototype
				.create(JargStaticImages.SMALLTitleStripleftbit());
		final static Image LeftBit = BigLeftBit.createImage();

		// OpenClueBox
		final static AbstractImagePrototype BigOpenClueBox = AbstractImagePrototype
				.create(JargStaticImages.BIGTitleStripleftOpen());
		final static AbstractImagePrototype MediumOpenClueBox = AbstractImagePrototype
				.create(JargStaticImages.MEDIUMTitleStripleftOpen());
		final static AbstractImagePrototype SmallOpenClueBox = AbstractImagePrototype
				.create(JargStaticImages.SMALLTitleStripleftOpen());
		

		// CloseClueBox
		final static AbstractImagePrototype BigCloseClueBox = AbstractImagePrototype
				.create(JargStaticImages.BIGTitleStripleftClose());
		final static AbstractImagePrototype MediumCloseClueBox = AbstractImagePrototype
				.create(JargStaticImages.MEDIUMTitleStripleftClose());
		final static AbstractImagePrototype SmallCloseClueBox = AbstractImagePrototype
				.create(JargStaticImages.SMALLTitleStripleftClose());
		

		// Buy bit
		final static AbstractImagePrototype BigBuyOpenButton = AbstractImagePrototype
				.create(JargStaticImages.BIGTitleStrip_buy());
		final static AbstractImagePrototype MediumBuyOpenButton = AbstractImagePrototype
				.create(JargStaticImages.MEDIUMTitleStrip_buy());
		final static AbstractImagePrototype SmallBuyOpenButton = AbstractImagePrototype
				.create(JargStaticImages.SMALLTitleStrip_buy());
		// final static Image LeftBuyButtonOpen = BigOpenButton.createImage();
	

		// Buy bit closed
		final static AbstractImagePrototype BigBuyCloseButton = AbstractImagePrototype
				.create(JargStaticImages.BIGTitleStrip_buyclose());
		final static AbstractImagePrototype MediumBuyCloseButton = AbstractImagePrototype
				.create(JargStaticImages.MEDIUMTitleStrip_buyclose());
		final static AbstractImagePrototype SmallBuyCloseButton = AbstractImagePrototype
				.create(JargStaticImages.SMALLTitleStrip_buyclose());
		
		// final static Image LeftBuyButtonOpen = BigOpenButton.createImage();
		

		static AbstractImagePrototype[] LogBoekImages = {
				AbstractImagePrototype.create(JargStaticImages.logboek0()),
				AbstractImagePrototype.create(JargStaticImages.logboek1()) };
		

		static AbstractImagePrototype[] MessPrevImages = {
				AbstractImagePrototype.create(JargStaticImages.previous0()),
				AbstractImagePrototype.create(JargStaticImages.previous1()) };
		

		static AbstractImagePrototype[] MessNextImages = {
				AbstractImagePrototype.create(JargStaticImages.next0()),
				AbstractImagePrototype.create(JargStaticImages.next1()) };
		
		
		// ScoreBack bit
		final static AbstractImagePrototype BigScoreBack = AbstractImagePrototype
				.create(JargStaticImages.BIGTitleStrip_Scoreback());
		final static AbstractImagePrototype MediumScoreBack = AbstractImagePrototype
				.create(JargStaticImages.MEDIUMTitleStrip_Scoreback());
		final static AbstractImagePrototype SmallScoreBack = AbstractImagePrototype
				.create(JargStaticImages.SMALLTitleStrip_Scoreback());
		static Image ScoreBack = BigScoreBack.createImage();
		
		
		
}
		
		


