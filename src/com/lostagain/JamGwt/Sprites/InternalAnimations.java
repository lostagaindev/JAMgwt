package com.lostagain.JamGwt.Sprites;

import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.lostagain.JamGwt.IconPacks.JamImages;

/**
 * Stores all the games internal animations
 * Change what this extends to change what internal animations it will compile into the game.
 * 
 * Current animation sets
 * NoirInternalAnimations - fire,crumble
 * ThornInternalAnimations - watersplash (wip)
 * MeryllInternalAnimations - fire,crumble
 * LandOfEdgeAndHueAnimations - goal_animation,drawattention etc
 * 
 * @author Tom
 **/
public class InternalAnimations extends MeryllInternalAnimations {

	static public AbstractImagePrototype[] getAnimation (String name){
		//all internal names are small
		name = name.toLowerCase().trim();

		//first look for game specific animations
		AbstractImagePrototype[] gameSpecificAnimation = allGameSpecificAnimations.get(name);
		//return one if found
		if (gameSpecificAnimation!=null){
			return gameSpecificAnimation;
		}
		//else we look for animations that all games have
		AbstractImagePrototype[] genericAnimations = allGenericAnimations.get(name);


		return genericAnimations;

	}

	static public Set<String> getAnimationNames(){
		return allGameSpecificAnimations.keySet();
	}




	///
	///Generic Animations
	/// (all gwt jam games have these)
	///Declared into a a array at the bottom that associates the animations with string indentifiers

	//----------------------------------


	// Inventory Images for the open/close animation 
	// at various sizes 
	// (If Cuypers code this is Piere's bag)
	public static final AbstractImagePrototype[] BigDefaultInventoryImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGrugzakje0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGrugzakje1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGrugzakje2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGrugzakje3()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGrugzakje4()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGrugzakje5())
	};
	// The class this extends contains the actual images used.
	// What is below is all the arrays used to make the animations from these images.
	// eg, 6 images are put into "BigDefaultInventoryImages", then the main code
	//can simply use a SpiffyIcon with this array to have the animation.	
	public static final AbstractImagePrototype[] MediumDefaultInventoryImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDrugzakje0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDrugzakje1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDrugzakje2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDrugzakje3()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDrugzakje4()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDrugzakje5())
	};
	public static final AbstractImagePrototype[] SmallDefaultInventoryImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLrugzakje0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLrugzakje1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLrugzakje2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLrugzakje3()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLrugzakje4()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLrugzakje5())
	};
	// Inventory Images (If cuypers code this is Margrs bag, else not used)
	public static final AbstractImagePrototype[] BigMarInventoryImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGmagrietbag0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGmagrietbag1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGmagrietbag2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGmagrietbag3()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGmagrietbag4()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGmagrietbag5()) };

	public static final AbstractImagePrototype[] MediumMarInventoryImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDmagrietbag0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDmagrietbag1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDmagrietbag2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDmagrietbag3()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDmagrietbag4()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDmagrietbag5()) };

	public static final AbstractImagePrototype[] SmallMarInventoryImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLmagrietbag0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLmagrietbag1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLmagrietbag2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLmagrietbag3()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLmagrietbag4()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLmagrietbag5()) };

	// Notepad images
	public static final AbstractImagePrototype[] BigNotepadImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGcc2notebook0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGcc2notebook1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGcc2notebook2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGcc2notebook3()) };

	public static final AbstractImagePrototype[] MediumNotepadImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMcc2notebook0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMcc2notebook1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMcc2notebook2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMcc2notebook3())
	};

	public static final AbstractImagePrototype[] SmallNotepadImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLcc2notebook0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLcc2notebook1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLcc2notebook2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLcc2notebook3()) };

	// ControllPanel images
	public static final AbstractImagePrototype[] BigControllPanelImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGCCtype0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGCCtype1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGCCtype2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGCCtype3()) };

	public static final AbstractImagePrototype[] MediumControllPanelImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMCCtype0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMCCtype1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMCCtype2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMCCtype3()) };

	public static final AbstractImagePrototype[] SmallControllPanelImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLCCtype0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLCCtype1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLCCtype2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLCCtype3()) };

















	final static ImmutableMap<String, AbstractImagePrototype[]> allGenericAnimations  = ImmutableMap.<String, AbstractImagePrototype[]>builder()
			//interface animations at various sizes (should be lower case)
			.put("bigdefaultinventorybutton",BigDefaultInventoryImages)
			.put("mediumdefaultinventorybutton",MediumDefaultInventoryImages)
			.put("smalldefaultinventorybutton",SmallDefaultInventoryImages)
			//--
			.put("bigmarinventorybutton",BigMarInventoryImages)
			.put("mediummarinventorybutton",MediumMarInventoryImages)
			.put("smallmarinventorybutton",SmallMarInventoryImages)
			.build();				
	//--
	// Forum images
	public static final AbstractImagePrototype[] BigForumImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGCC2forum0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGCC2forum1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGCC2forum2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGCC2forum3()) 				
	};
	public static final AbstractImagePrototype[] MediumForumImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMCC2forum0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMCC2forum1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMCC2forum2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMCC2forum3()) 				
	};
	public static final AbstractImagePrototype[] SmallForumImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLCC2forum0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLCC2forum1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLCC2forum2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLCC2forum3()) 
	};
	// Secrets
	public static final AbstractImagePrototype[] BigSecretsImages = {
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGeasteregg0()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGeasteregg1()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGeasteregg2()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGeasteregg3()),
			AbstractImagePrototype.create(JamImages.JargStaticImages.BIGeasteregg4()) };
	
	public static final AbstractImagePrototype[] MediumSecretsImages = {
	AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMeasteregg0()),
	AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMeasteregg1()),
	AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMeasteregg2()),
	AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMeasteregg3()),
	AbstractImagePrototype.create(JamImages.JargStaticImages.MEDIUMeasteregg4()) };
	
	public static final AbstractImagePrototype[] SmallSecretsImages = {
	AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLeasteregg0()),
	AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLeasteregg1()),
	AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLeasteregg2()),
	AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLeasteregg3()),
	AbstractImagePrototype.create(JamImages.JargStaticImages.SMALLeasteregg4()) };

}
