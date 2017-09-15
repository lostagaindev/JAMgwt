package com.lostagain.JamGwt.JargScene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jetty.util.log.Log;

import com.darkflame.client.InstructionProcessor;
import com.darkflame.client.SuperSimpleSemantics;
import com.darkflame.client.InventoryObjectTypes.InventoryIcon;
import com.darkflame.client.InventoryObjectTypes.InventoryPanel;
import com.lostagain.JamGwt.JargScene.SceneObjectState.SceneObjectType;
import com.lostagain.JamGwt.JargScene.Interfaces.IsSceneDivObject;
import com.darkflame.client.query.Query;
import com.darkflame.client.semantic.QueryEngine;
import com.darkflame.client.semantic.SSSNode;
import com.google.common.collect.HashMultimap;


//---------------------------------------------------------------------------------------------------------------------------------
//----These NEW functions should replace the old ones in SceneWidget eventually their NEW suffix should be removed-----------------
//---------------------------------------------------------------------------------------------------------------------------------

//NOTE: These new functions search GLOBAL over all the currently loaded scenes
//The old functions searched inconsistently across the current scene and sometimes global
//We do, however, allow a boolean flag to search current scene only.
//In future a script function might be introduced to limit the returned objects to the current scene.
//This might speed up very large games where lots of scenes are loaded at once
//----
//If trying to understand this code its best to follow over the route of a specific request, rather then 
//trying to just read it all and keep it all in your head at once.
//Say, a get text object request
//Start at say, getTextObjectNEW(String,SceneObject,boolean)
//Then follow over what it uses.

public class SceneObjectDatabase_old {

	/** Global hashmap of all known input objects (for search purposes) <br>
	 * <br>
	 * Remember all names are stored lowercase! use .toLowerCase() when searching **/
	public static HashMultimap<String, SceneInputObject> all_input_objects = HashMultimap
			.create();
	
	/** Global hashmap of all known vector objects (for search purposes) <br>
	 * <br>
	 * Remember all names are stored lowercase! use .toLowerCase() when searching **/
	public static HashMultimap<String, SceneVectorObject> all_vector_objects = HashMultimap
			.create();
	
	/** Global hashmap of all known div objects (for search purposes)<br>
	 * <br>
	 *  Remember all names are stored lowercase! use .toLowerCase() when searching **/
	public static HashMultimap<String, SceneDivObject> all_div_objects = HashMultimap
			.create();
	
	/** Global hashmap of all known text objects (for search purposes) <br>
	 * <br>
	 * Remember all names are stored lowercase! use .toLowerCase() when searching **/
	public static HashMultimap<String, SceneLabelObject> all_text_objects = HashMultimap
			.create(); //SceneDialogObject
	
	/** Global hashmap of all known label objects (for search purposes) <br>
	 * <br>
	 * Remember all names are stored lowercase! use .toLowerCase() when searching **/
	//public static HashMultimap<String, SceneLabelObject> all_label_objects = HashMultimap
	//		.create(); //nb; will eventually replace text above once SceneDialogObject extends SceneLabelObject
	
	
	
	/** Global hashmap of all known objects (for search purposes) <br>
	 * Remember all names are stored lowercase! use .toLowerCase() when searching**/
	public static HashMultimap<String, SceneSpriteObject> all_sprite_objects = HashMultimap
			.create();

	/**
	 * returns all the games objects by combining the lists of object types
	 * NOTE: don't search this combined list unless needed.
	 * If your looking just for a specific type of object, use the get function for that type
	 * @return
	 */
	public static Set<SceneObjectVisual> getAllGamesObjects(){

		Set<SceneObjectVisual> allObjects = new HashSet<SceneObjectVisual>();

		allObjects.addAll(SceneObjectDatabase_old.all_div_objects.values());
		allObjects.addAll(SceneObjectDatabase_old.all_input_objects.values());
		allObjects.addAll(SceneObjectDatabase_old.all_sprite_objects.values());
		allObjects.addAll(SceneObjectDatabase_old.all_text_objects.values());
		allObjects.addAll(SceneObjectDatabase_old.all_vector_objects.values());

		return allObjects;

	}
	
	/** clears all object databases global to the game.**/
	public static void clearAllObjectDataMaps(){

		all_div_objects.clear();
		all_input_objects.clear();
		all_sprite_objects.clear();
		all_text_objects.clear();
		all_vector_objects.clear();
		
		return;

	}
	/**
	 * Gets div objects that meet the requirements specified <br>
	 * <br>
	 * The basic process is<br><br>
	 * 1. Run generic variable checks on the search string, and return objects if found (ie, maybe its asking for "all objects of this type" or maybe its a "<:Property" search)<br>
	 * 2. Run div object variable checks (ie, we now look for variables in the search string that are only applicable to div objects. Like ....)<br>
	 * 3. If none of the above found we then simply look for any scene objects that match the name specified in the search string.<br>
	 *<br>
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return  a set of matching objects, or a zero length set if none found
	 */
	public static Set<? extends IsSceneDivObject> getDivObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		//ensure the search string is trimmed
		search = search.trim();

		SceneWidgetVisual.Log.info("Using the new div object search function to search for:"+search+" global:"+searchGlobal);

		Set<IsSceneDivObject> searchPool = new HashSet<IsSceneDivObject>();

		//first get the relevant objects to search
		if (searchGlobal){
			searchPool.addAll(SceneObjectDatabase_old.all_div_objects.values());
		} else {
			//searchPool.addAll(InstructionProcessor.currentScene.getScenesData().SceneDivObjects);		
			Set<IsSceneDivObject> divobjects = InstructionProcessor.currentScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Div);
			searchPool.addAll(divobjects);
		}	
		
		//if the search pool is empty we return a empty set
		if (searchPool.size()==0){
			SceneWidgetVisual.Log.info("No objects in search list so returning a zero length set");

			return searchPool;
		}
		//first we look if there's a < in the search string, as that identifies a variable.
		//if none is found we just do a novariable check here without bothering to look for variables
		if (search.contains("<"))
		{
			SceneWidgetVisual.Log.info("Num of objects to search:"+searchPool.size());

			Set<IsSceneDivObject> results = SceneObjectDatabase_old.getObjectsFromSpecificVariable(search, callingObject, searchPool); //This function will check for any functions like "<ALLOBJECTS>" or property specific searchs

			//if results are found we just return them
			if (results != null && results.size()>0){

				SceneWidgetVisual.Log.info("specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		

			SceneWidgetVisual.Log.info("No specific variable checks found for:"+search);

			//if not we check for vector object specific things
			//These checks will only ever return a text object type.
			results = SceneObjectDatabase_old.getSceneDivObjectFromDivSpecificVariable(search);

			//if results are found we just return them
			if (results != null && results.size()>0){
				SceneWidgetVisual.Log.info("vector specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		
			//as no variables were found we now do a general check for name and return that


		}
		//finally if none of the fancy pants variables are used we look for just a object with this name
		if (searchGlobal){
			//if we are on a global search we can take a nifty shortcut here
			//This is because the global game variables storing the object types are hasmaps with the names of the object used as the key
			//this is much quicker then looping over which is done within getSceneObjectByName
			return SceneObjectDatabase_old.all_div_objects.get(search.toLowerCase());

		} else {
			return SceneObjectDatabase_old.getSceneObjectVisualsByName_novariablecheck(search,searchPool);
		}



	}
	/**
	 * Gets a single Input object that matches the requirements of the search string.
	 * NOTE: as the search string can specify a whole bunch of objects, this will get just one of them arbitrarily.
	 * It can't be guaranteed to be the same one each time, AND it can't be guaranteed to be random either.
	 * So dont use this function unless you are sure there's only one object that meets the requirements.
	 * 
	 * use public static getDivObjectNEW to get a set of scenedivobjects instead
	 * 
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return null if none found that matchs the search requirement
	 */
	public static SceneInputObject getSingleInputObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		Set<? extends SceneInputObject> allMatchingObjects = getInputObjectNEW( search, callingObject,  searchGlobal); 

		if (allMatchingObjects.size()>1){

			SceneWidgetVisual.Log.info("found multiple possibilitys for:"+search+" global:"+searchGlobal+". But only one scenedivobject requested, so picking one arbitarily...");

		} else if (allMatchingObjects.size()==0){

			return null;			

		}

		return allMatchingObjects.iterator().next();
	}
	/**
	 * Gets a single vector object that matches the requirements of the search string.
	 * NOTE: as the search string can specify a whole bunch of objects, this will get just one of them arbitrarily.
	 * It can't be guaranteed to be the same one each time, AND it can't be guaranteed to be random either.
	 * So dont use this function unless you are sure there's only one object that meets the requirements.
	 * 
	 * use public static getDivObjectNEW to get a set of scenedivobjects instead
	 * 
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return null if none found that matchs the search requirement
	 */
	public static SceneVectorObject getSingleVectorObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		Set<? extends SceneVectorObject> allMatchingObjects = getVectorObjectNEW( search, callingObject,  searchGlobal); 

		if (allMatchingObjects.size()>1){

			SceneWidgetVisual.Log.info("found multiple possibilitys for:"+search+" global:"+searchGlobal+". But only one scenedivobject requested, so picking one arbitarily...");

		} else if (allMatchingObjects.size()==0){

			return null;			

		}

		return allMatchingObjects.iterator().next();
	}
	
	
	/**
	 * Gets a single dialogue object that matches the requirements of the search string.
	 * NOTE: as the search string can specify a whole bunch of objects, this will get just one of them arbitrarily.
	 * It can't be guaranteed to be the same one each time, AND it can't be guaranteed to be random either.
	 * So dont use this function unless you are sure there's only one object that meets the requirements.
	 * 
	 * use public static getDivObjectNEW to get a set of scenedivobjects instead
	 * 
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return null if none found that matchs the search requirement
	 */
	public static SceneLabelObject getSingleTextBoxObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		Set<? extends SceneLabelObject> allMatchingObjects = getTextObjectNEW( search, callingObject,  searchGlobal); 

		if (allMatchingObjects.size()>1){

			SceneWidgetVisual.Log.info("found multiple possibilitys for:"+search+" global:"+searchGlobal+". But only one scenedivobject requested, so picking one arbitarily...");

		} else if (allMatchingObjects.size()==0){

			return null;			

		}

		return allMatchingObjects.iterator().next();
	}
	/**
	 * Gets a single object that matchs the requirements of the search string.
	 * NOTE: as the search string can specify a whole bunch of objects, this will get just one of them arbitarily.
	 * It cant be garentied to be the same one each time, AND it cant be garentied to be random either.
	 * So dont use this function unless you are sure theres only one object that meets the requirements.
	 * 
	 * use public static getDivObjectNEW to get a set of scenedivobjects instead
	 * 
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return null if none found that matchs the search requirement
	 */
	public static IsSceneDivObject getSingleDivObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		Set<? extends IsSceneDivObject> allMatchingObjects = getDivObjectNEW( search, callingObject,  searchGlobal); 

		if (allMatchingObjects.size()>1){

			SceneWidgetVisual.Log.info("found multiple possibilitys for:"+search+" global:"+searchGlobal+". But only one scenedivobject requested, so picking one arbitarily...");

		} else if (allMatchingObjects.size()==0){

			return null;			

		}


		return allMatchingObjects.iterator().next();
	}
	
	/**
	 * Gets a single sprite object that matchs the requirements of the search string.
	 * NOTE: as the search string can specify a whole bunch of objects, this will get just one of them arbitrarily.
	 * It cant be garentied to be the same one each time, AND it cant be guaranteed to be random either.
	 * So dont use this function unless you are sure theres only one object that meets the requirements.
	 * 
	 * use public static getDivObjectNEW to get a set of scenedivobjects instead
	 * 
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return null if none found that matchs the search requirement
	 */
	public static SceneObject getSingleSpriteObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		Set<? extends SceneSpriteObject> allMatchingObjects = getSpriteObjectNEW( search, callingObject,  searchGlobal); 

		if (allMatchingObjects.size()>1){

			SceneWidgetVisual.Log.info("found multiple possibilitys for:"+search+" global:"+searchGlobal+". But only one scenedivobject requested, so picking one arbitarily...");

		} else if (allMatchingObjects.size()==0){

			return null;			

		}

		return allMatchingObjects.iterator().next();
	}

	/**
	 * See getSceneObjectVisualNEW. Currently an experiment that returns a extension of SceneObject instead
	 * use this instead of getSceneObjectVisualNEW as much as possible
	 * 
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return
	 */
	public static Set<? extends SceneObject> getSceneObjectNEW(String search,SceneObjectVisual callingObject, boolean searchGlobal) 
	{
		return  getSceneObjectVisualNEW( search, callingObject,  searchGlobal);  
	}
	
	/**
	 * Gets a scene object based on the specified search string.
	 * the search string is either the name of a object, or a variable that can refer to one or more objects.
	 * 
	 * We return a set of objects that match the search
	 * 
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * 
	 * @return a set of matching objects, or a zero length set if none found
	 */
 //eventually we will probably need to output SceneObject not SceneObjectVisual
	public static Set<? extends SceneObjectVisual> getSceneObjectVisualNEW(String search,SceneObjectVisual callingObject, boolean searchGlobal) 
	{
		//ensure the search string is trimmed
		search = search.trim();

		SceneWidgetVisual.Log.info("Using the new SceneObject search function to search for:"+search+" global:"+searchGlobal);

		Set<SceneObjectVisual> searchPool = new HashSet<SceneObjectVisual>();

		//First get the relevant objects to search
		if (searchGlobal){
			searchPool.addAll(getAllGamesObjects());
		} else {
			
			//Set<SceneDialogObject> dobjects = InstructionProcessor.currentScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.DialogBox);
			//searchPool.addAll(dobjects);
			//Set<SceneLabelObject> lobjects = InstructionProcessor.currentScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Label);
			//searchPool.addAll(lobjects);
						
			ArrayList<SceneObjectVisual> objects = InstructionProcessor.currentScene.getScenesData().getAllScenesCurrentObjects();
			searchPool.addAll(objects);
			
		}	

		SceneWidgetVisual.Log.info("Num of scene objects to search:"+searchPool.size());
		
		if (searchPool.size()==0){
			SceneWidgetVisual.Log.info("No objects in search list so returning a zero length set");

			return searchPool;
		}

		//first we look if there's a < in the search string, as that identifies a variable.
		//if none is found we just do a novariable check here without bothering to look for variables
		if (!search.contains("<"))
			
		{
			//as no variables were found we now do a general check for name and return that
			return SceneObjectDatabase_old.getSceneObjectVisualsByName_novariablecheck(search,searchPool);
		}


		//First we check for things that return specifically SceneObject
		//The reason this function isn't together with the ones below (like <LastSCNENEITEM> and <CHILDREN>) is that unlike
		//those variable checks, these checks can be reused for elsewhere in the code (ie, when we are purely getting textobjects, or purely getting sprite objects)
		//in our case we are getting any of them so we can also use it.
		Set<SceneObjectVisual> results = SceneObjectDatabase_old.getObjectsFromSpecificVariable(search, callingObject, searchPool); //This function will check for any functions like "<ALLOBJECTS>" or property specific searchs

		//if results are found we just return them
		if (results != null && results.size()>0){

			SceneWidgetVisual.Log.info("specific variable check found results for:"+search+" global:"+searchGlobal);
			return results;
		}		


		SceneWidgetVisual.Log.info("No specific variable checks found for:"+search);

		//check variables that return SceneObject types, as this these variables can return SceneObject Or any of its subtypes, 
		//and we dont care which for this function.
		if (search.equalsIgnoreCase("<LASTSCENEITEM>")) {
			//return the last object clicked on
			if (InstructionProcessor.lastSceneObjectClickedOn==null){
				SceneWidgetVisual.Log.info("LASTSCENEITEM was asked for, but its not set (null) so we can only return null ");
				return null;
			}
			Set<SceneObjectVisual> resultobjects = new HashSet<SceneObjectVisual>();
			resultobjects.add(InstructionProcessor.lastSceneObjectClickedOn);
			SceneWidgetVisual.Log.info("last scene item found :"+InstructionProcessor.lastSceneObjectClickedOn.objectsCurrentState.ObjectsName);

			return resultobjects;

		} else if (search.equalsIgnoreCase("<CHILDREN>")) {
			
			//return this objects children (ie, things positioned relative to it)
			SceneWidgetVisual.Log.info("returning objects <CHILDREN>:"+callingObject.relativeObjects.size());
			Set<SceneObjectVisual> resultobjects = new HashSet<SceneObjectVisual>();			
			resultobjects.addAll(callingObject.relativeObjects); 
			SceneWidgetVisual.Log.info("number of <CHILDREN>="+resultobjects.size());
			
			return resultobjects;

		} else //The toucher is the last object that touched another
		//This is not some advanced collision detection thing, but a manually set flag that can happen during a scripter created path
		//This is because 2D games, with faked perspective, have objects "touching" on the screen all the time, but when they are supposed to be 
		//"really" behind and in front of things, toucher should not be set. For this reason its controlled manually
		//See Instruction "addobjecttouching"
		//Note; this is a spriteobject specific variable right now, but at some point refractoring it to be a generic sceneobject would be better
		//as theres nothing really sprite-specific the idea of a toucher needs to work (unlike, say, frame-changing)
		//If that change is ever made, this check should be moved to general sceneobject variable checks
		if (search.equalsIgnoreCase("<TOUCHER>")) {
			
			Set<SceneObjectVisual> resultobjects = new HashSet<SceneObjectVisual>();
			resultobjects.add(InstructionProcessor.lastObjectThatTouchedAnother);
			
								
			SceneWidgetVisual.Log.info("got touching object "
					+ InstructionProcessor.lastObjectThatTouchedAnother.objectsCurrentState.ObjectsName);

			return resultobjects;

		} 


		//if none of the above, we now check for variables that will return specific sceneobjecttypes (like text, or sprite)
		//If we were in a function for getting something specific, we would only check one of these things
		//but as we have been asked to look for any sceneobjects that meet the search requirements, we need to check them all.


		//first sprites, as these are almost certainly the most common to check
		//the following functions only return sprite objects


		//Note: ? extends means "any object of type SceneObject or its subtypes"
		//We have to do this because even though;
		//SceneDialogueObject object extends SceneObject
		//Set<SceneDialogObjecte> does not extend Set<SceneObject)
		//This seems odd but if it did strange things would happen 
		//See here; http://stackoverflow.com/questions/5082044/most-efficient-way-to-cast-listsubclass-to-listbaseclass
		//Because of this we use the ? extends syntext below. The restriction is Sets (or Lists) made this way are read-only. add() or addAll() used on the list will cause a error.
		Set<SceneSpriteObject> spriteresults = SceneObjectDatabase_old.getSceneSpriteObjectFromSpriteSpecificVariable(search);

		//see if there was any text results found and return them if so
		if (spriteresults != null && spriteresults.size()>0){
			SceneWidgetVisual.Log.info("text variable check found results for:"+search+" global:"+searchGlobal);
			return spriteresults;
		}	

		//then we check variables that return text objects in the same way
		Set<SceneLabelObject> textresults = SceneObjectDatabase_old.getSceneTextObjectFromTextSpecificVariable(search);

		//see if there was any text results found and return them if so
		if (textresults != null && textresults.size()>0){
			SceneWidgetVisual.Log.info("text variable check found results for:"+search+" global:"+searchGlobal);
			return textresults;
		}	

		//Then vector specific variables
		Set<SceneVectorObject> vectorresults = SceneObjectDatabase_old.getSceneVectorObjectFromVectorSpecificVariable(search);

		//see if there was any text results found and return them if so
		if (vectorresults != null && vectorresults.size()>0){
			SceneWidgetVisual.Log.info("vector variable check found results for:"+search+" global:"+searchGlobal);
			return vectorresults;
		}	

		//Then div specific variables
		Set<SceneDivObject> divresults = SceneObjectDatabase_old.getSceneDivObjectFromDivSpecificVariable(search);

		//see if there was any div results found and return them if so
		if (divresults != null && divresults.size()>0){
			SceneWidgetVisual.Log.info("div  variable check found results for:"+search+" global:"+searchGlobal);
			return divresults;
		}	

		//Then input specific variables
		Set<SceneInputObject> inputresults = SceneObjectDatabase_old.getSceneInputObjectFromInputSpecificVariable(search);

		//see if there was any div results found and return them if so
		if (inputresults != null && inputresults.size()>0){
			SceneWidgetVisual.Log.info("input  variable check found results for:"+search+" global:"+searchGlobal);
			return inputresults;
		}	

		//as no variables were found we now do a general check for name and return that
		return SceneObjectDatabase_old.getSceneObjectVisualsByName_novariablecheck(search,searchPool);

	}

	/**
	 * Gets a single object that matches the requirements of the search string.
	 * NOTE: as the search string can specify a whole bunch of objects, this will get just one of them arbitrarily.
	 * It cant be guaranteed to be the same one each time, AND it cant be guaranteed to be random either.
	 * So don't use this function unless you are sure theres only one object that meets the requirements.
	 * 
	 * use public static Set<? extends SceneObject> getSceneObjectNEW to get a set of sceneobjects instead
	 * 
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return null if no object found that matches the search requirement
	 */
	public static SceneObjectVisual getSingleSceneObjectVisualNEW(String search,SceneObjectVisual callingObject, boolean searchGlobal) 
	{
		Set<? extends SceneObjectVisual> allMatchingObjects = getSceneObjectVisualNEW( search, callingObject,  searchGlobal); 

		if (allMatchingObjects==null){
			SceneWidgetVisual.Log.info("null returned from getSceneObjectVisualNEW, likely no objects found");
			return null;

		}
		
		if (allMatchingObjects.size()>1){

			SceneWidgetVisual.Log.info("found multiple possibilitys for:"+search+" global:"+searchGlobal+". But only one sceneobject requested, so picking one arbitarily...");

		} else if (allMatchingObjects.size()==0){
			
			SceneWidgetVisual.Log.info("no objects found returning null...");

			return null;			

		}



		return allMatchingObjects.iterator().next();
	}

	/**
	 * Gets spites objects that meet the requirements specified <br>
	 * <br>
	 * The basic process is<br><br>
	 * 1. Run generic variable checks on the search string, and return objects if found (ie, maybe its asking for "all objects of this type" or maybe its a "<:Property" search)<br>
	 * 2. Run Sprite object variable checks (ie, we now look for variables in the search string that are only applicable to sprite objects. Like <HELDITEM>)<br>
	 * 3. If none of the above found we then simply look for any scene objects that match the name specified in the search string.<br>
	 *<br>
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return  a set of matching objects, or a zero length set if none found
	 */
	public static Set<SceneSpriteObject> getSpriteObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		//ensure the search string is trimmed
		search = search.trim();

		SceneWidgetVisual.Log.info("Using the new sprite object search function to search for:"+search+" global:"+searchGlobal);

		Set<SceneSpriteObject> searchPool = new HashSet<SceneSpriteObject>();

		//first get the relivant objects to search
		if (searchGlobal){
			searchPool.addAll(SceneObjectDatabase_old.all_sprite_objects.values());
		} else {
		//	searchPool.addAll(InstructionProcessor.currentScene.getScenesData().sceneSpriteObjects);
			Set<SceneSpriteObject> scenesSprites = InstructionProcessor.currentScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Sprite);
			searchPool.addAll(scenesSprites);
			
		}	

		SceneWidgetVisual.Log.info("Num of objects to search:"+searchPool.size());
		//if the search pool is empty we return a empty set
		if (searchPool.size()==0){
			SceneWidgetVisual.Log.info("No objects in search list so returning a zero length set");

			return searchPool;
		}
		//NOTE: as an optimisation we  check for the presence of a variable before doing variable searchs
		//first we look if there's a < in the search string, as that identifies a variable.
		//if none is found we just do a novariable check here without bothering to look for variables
		if (search.contains("<"))
		{
			Set<SceneSpriteObject> results = SceneObjectDatabase_old.getObjectsFromSpecificVariable(search, callingObject, searchPool); //This function will check for any functions like "<ALLOBJECTS>" or property specific searchs

			//if results are found we just return them
			if (results != null && results.size()>0){
				SceneWidgetVisual.Log.info("specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		

			SceneWidgetVisual.Log.info("No specific variable checks found for:"+search);

			//if not we check for sprite object specific things
			//These checks will only ever return a text object type.
			results = SceneObjectDatabase_old.getSceneSpriteObjectFromSpriteSpecificVariable(search);

			//if results are found we just return them
			if (results != null && results.size()>0){
				SceneWidgetVisual.Log.info("sprite specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		
		}
		//finally if none of the fancy pants variables are used we look for just a object with this name
		if (searchGlobal){
			//if we are on a global search we can take a nifty shortcut here
			//This is because the global game variables storing the object types are hasmaps with the names of the object used as the key
			//this is much quicker then looping over
			return SceneObjectDatabase_old.all_sprite_objects.get(search.toLowerCase());

		} else {

			return SceneObjectDatabase_old.getSceneObjectVisualsByName_novariablecheck(search,searchPool);
		}


	}

	/**
	 * returns a set of sprite objects from a sprite specific variable
	 * 
	 * @param searchVariable
	 * @return  a set of matching objects, or a zero length set if none found
	 */
	public static Set<SceneSpriteObject> getSceneSpriteObjectFromSpriteSpecificVariable(String searchVariable){

		Set<SceneSpriteObject> results = new HashSet<SceneSpriteObject>();

		
		
		//get inventory item that is being held, if any (inventory items are sprite objects!)
		if (searchVariable.equalsIgnoreCase("<HELDITEM>")) {

			//first get the held item from the inventorypanel class
			InventoryIcon iitem = InventoryPanel.currentlyHeldItem; 
			//check is its null
			if (iitem==null){
				SceneWidgetVisual.Log.info("no held item. Maybe the keepheld setting on the item is wrong?");
				return results;
			}
			//then check if theres a associated scene object that should be used instead. (ie, the inventory icon is just a visual reflection of this,
			//so this one should be interacted with instead). Associatedobjects are typically needed when the inventory icon looks different to it in the scene
			if (iitem.associatedSceneObject!=null){
				SceneWidgetVisual.Log.info("an associated sprite object is set for this inventory item, so we return that instead");
				String oname = iitem.associatedSceneObject;
				return SceneObjectDatabase_old.all_sprite_objects.get(oname); //note this is always global anyway, as the associated object shouldnt change on the wimm of the searcher
			} else {
				SceneWidgetVisual.Log.info("associatedSceneObject is null so we return the iventory item directly");
				results.add(iitem);
				return results;
			}


		}  
		//here we check if we have been asked to return the last sprite object clicked on
		else if (searchVariable.equalsIgnoreCase("<LASTSPRITEITEM>")) {

			if (InstructionProcessor.lastSpriteObjectClickedOn==null){
				SceneWidgetVisual.Log.info("LASTSPRITEITEM was asked for, but its not set (null) so we can only return null ");
				return results;
			}

			results.add( InstructionProcessor.lastSpriteObjectClickedOn );
			SceneWidgetVisual.Log.info("last scene item clicked :"+ InstructionProcessor.lastSpriteObjectClickedOn.objectsCurrentState.ObjectsName);

			return results;

		} 
		//here we check is we have been asked to return the last inventory item clicked on
		else if (searchVariable.equalsIgnoreCase("<LASTINVENTORYITEM>")) {

			if (InstructionProcessor.lastInventoryObjectClickedOn ==null){
				SceneWidgetVisual.Log.info("LASTINVENTORYITEM was asked for, but its not set (null) so we can only return null ");
					return results;
			}

			results.add( InstructionProcessor.lastInventoryObjectClickedOn);
			SceneWidgetVisual.Log.info("last inventory item clicked :"+ InstructionProcessor.lastInventoryObjectClickedOn.objectsCurrentState.ObjectsName);

			return results;

		}
		//if this is dynamically created object, 
		//the object that created it is here
		//Note; This is not the same as the object it might be cloned from 
		//eg. A clone of fire is spawned from the object that caused it (ie, paper) , but is still a clone of a fire sprite.
		//the spawning object is usefull to know at times. For example, if a bullit hits something you might want to know which gun it came from
		else if (searchVariable.equalsIgnoreCase("<LASTCLICKEDSCREATOR>")) {
			results.add( (SceneSpriteObject) InstructionProcessor.lastSpriteObjectClickedOn.objectsCurrentState.spawningObject );
			return results;
		}

		//if we need more sprite specific variables they should be inserted as more else ifs here

		//no variables found so we return  a empty set
		return results;

	}

	/**
	 * This will search over all the games objects and return any with the specifies property
	 * 
	 * You can also search for multiple properties by separation with a || for OR
	 * ie
	 * 'Visible||Active' would return any object with the property visible or active
	 * 
	 * You can even do full semantic query searches by using quotes
	 * '"((Colour=Green)||(Colour=Red))&&(Fruit)"'
	 * Would search for either red or green fruit.
	 * 
	 * NOTE: when doing semantic searches it will returns "things which are" not the words themselves.
	 * "Apple"
	 * would return a object with the property "Granny Smith"
	 * But not one with the property "Apple"
	 * 
	 * @param propertyString
	 * @return
	 */
	public static Set<?extends SceneObjectVisual> getObjectsWithPropertyNEW(String propertyString,Set<?extends SceneObjectVisual>  searchThese) {


		//if we start with a quote then we are a semantic query and we deal with that separately
		if (propertyString.startsWith("\"")){
			String query = propertyString.substring(0,propertyString.length()-1);
			SceneWidgetVisual.Log.info("Getting all Objects with semantic query:"+query+"(slower then other GetObject functions)");
		
			//trim the quotes from the request
			propertyString = propertyString.substring(1, propertyString.length()-1);
			
			
			//first we get the set of properties the query results in
			HashSet<SSSNode> queryResults = getAllPropertysMatchingSemanticQuery(propertyString);
			
			//Then we get all the nodes with these properties
			//This method will be improved in future to search nodes directly and not via strings
			SSSNode[] array = queryResults.toArray(new SSSNode[queryResults.size()]); 
			
			Set<SceneObjectVisual> results = getObjectsWithThesePropertys(searchThese,array);
			
			//then we look at the things we have been allowed to search within, and keep only those also in the results.
			//the "intersection" of the two lists in other words
			searchThese.retainAll(results);
			
			//then we return the newly shortened list, which is not the things that match the query which we are allowed to search within.
			return searchThese;
		}
		
	

		//split propertyString by ||
		String properties[] = propertyString.split("\\|\\|");

		SceneWidgetVisual.Log.info("looking for "+properties.length+" properties.");

		SceneWidgetVisual.Log.info("first property is "+properties[0]);
		
		//We use an set to start as we don't know how many objects we need in the end, but we dont want duplicates
		Set<SceneObjectVisual> results = getObjectsWithThesePropertys(searchThese,
				properties);


		SceneWidgetVisual.Log.info("found "+results.size()+" objects with any of those properties.");
		return results;
	}

	/**
	 * Gets a set of objects that have particular properties
	 * 
	 * @param searchThese - the Strings representing the properties.
	 * @param properties
	 * @return
	 */
	public static Set<SceneObjectVisual> getObjectsWithThesePropertys(
			Set<? extends SceneObjectVisual> searchThese, String[] properties) {
		
		Set<SceneObjectVisual> results = new HashSet<SceneObjectVisual>();
		
		//loop over all the objects we have been asked to search
		for (SceneObjectVisual sceneObject : searchThese) {

			//check if they have any of the properties asked for
			for (String property : properties) {

				//if they do add it to the results list
				if (sceneObject.hasProperty(property)){				
					results.add(sceneObject);
				}

			}
		}
		return results;
	}
	
	/**
	 * Gets a set of objects that have particular properties
	 * 
	 * @param searchThese - the SSSNodes representing the properties.
	 * @param properties
	 * @return
	 */
	public static Set<SceneObjectVisual> getObjectsWithThesePropertys(
			Set<? extends SceneObjectVisual> searchThese, SSSNode[] properties) {
		
		Set<SceneObjectVisual> results = new HashSet<SceneObjectVisual>();
		
		//loop over all the objects we have been asked to search
		for (SceneObjectVisual sceneObject : searchThese) {

			//check if they have any of the properties asked for
			for (SSSNode property : properties) {

				//if they do add it to the results list
				if (sceneObject.hasProperty(property)){				
					results.add(sceneObject);
				}

			}
		}
		return results;
	}
	/**
	 * runs a semantic query and returns the objects that match the resulting properties in a HashSet
	 * 
	 * @param propertyString
	 * @return
	 */
	private static HashSet<SSSNode> getAllPropertysMatchingSemanticQuery(
			String propertyString) {
		
		
		//refresh the semantic cache (NOTE: SHOULD NOT BE DONE HERE. SHOULD BE DONE AFTER LOADING OBJECTS)
		//SSSNode.refreshAllCaches();
		
		SceneWidgetVisual.Log.info("creating semantic query from:"+propertyString);
		Query semanticQuery = Query.createQuerySafely(propertyString);
		
		//SceneWidget.Log.info("NOT Processing Query:"+semanticQuery.getAsString()+" as this function isn't ready to use yet");

		//Note; The SemanticEnegine doesn't know about SceneObjects or Properties directly.
		//Instead it knows of SSSNodes, which are the semantic representation of sceneobject.
		//So first we ask it nicely to give us all the Nodes that match now.
		ArrayList<SSSNode> resultNodes = QueryEngine.processQueryNOW(semanticQuery, false);
		
		HashSet<SSSNode> resultPropertys = new HashSet<SSSNode>(resultNodes);
		
		 /*
		for (SSSNode sssNode : resultNodes) {
			
			SceneWidget.Log.info("Matching property found:"+sssNode);
			//add its label to the set of propertys to look for 		
			
			resultPropertys.add(sssNode.getPURI());
			
		}
		*/
		
		
		return resultPropertys;
	}

	/**
	 * Gets input objects that meet the requirements specified <br>
	 * <br>
	 * The basic process is<br><br>
	 * 1. Run generic variable checks on the search string, and return objects if found (ie, maybe its asking for "all objects of this type" or maybe its a "<:Property" search)<br>
	 * 2. Run input object variable checks (ie, we now look for variables in the search string that are only applicable to input objects. Like <LASTINPUTUPDATED>)<br>
	 * 3. If none of the above found we then simply look for any scene objects that match the name specified in the search string.<br>
	 *<br>
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return a set of matching objects, or a zero length set if none found
	 */
	public static Set<SceneInputObject> getInputObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		//ensure the search string is trimmed
		search = search.trim();

		SceneWidgetVisual.Log.info("Using the new input object search function to search for:"+search+" global:"+searchGlobal);

		Set<SceneInputObject> searchPool = new HashSet<SceneInputObject>();

		//first get the relevant objects to search
		if (searchGlobal){
			
			searchPool.addAll(SceneObjectDatabase_old.all_input_objects.values());
			
		} else {
			//searchPool.addAll(InstructionProcessor.currentScene.getScenesData().SceneInputObjects);		
			
			Set<SceneInputObject> iobjects = InstructionProcessor.currentScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Input);
			searchPool.addAll(iobjects);
		}	

		SceneWidgetVisual.Log.info("Num of objects to search:"+searchPool.size());
		//if the search pool is empty we return a empty set
		if (searchPool.size()==0){
			SceneWidgetVisual.Log.info("No objects in search list so returning a zero length set");

			return searchPool;
		}
		
		
		//first we look if there's a < in the search string, as that identifies a variable.
		//if none is found we just do a novariable check here without bothering to look for variables
		if (search.contains("<"))
		{
			Set<SceneInputObject> results = SceneObjectDatabase_old.getObjectsFromSpecificVariable(search, callingObject, searchPool); //This function will check for any functions like "<ALLOBJECTS>" or property specific searchs

			//if results are found we just return them
			if (results != null && results.size()>0){

				SceneWidgetVisual.Log.info("specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		

			SceneWidgetVisual.Log.info("No specific variable checks found for:"+search);

			//if not we check for vector object specific things
			//These checks will only ever return a text object type.
			results = SceneObjectDatabase_old.getSceneInputObjectFromInputSpecificVariable(search);

			//if results are found we just return them
			if (results != null && results.size()>0){
				SceneWidgetVisual.Log.info("vector specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		

		}
		//finally if none of the fancy pants variables are used we look for just a object with this name
		if (searchGlobal){
			//if we are on a global search we can take a nifty shortcut here
			//This is because the global game variables storing the object types are hasmaps with the names of the object used as the key
			//this is much quicker then looping over which is done within getSceneObjectByName
			return SceneObjectDatabase_old.all_input_objects.get(search.toLowerCase());

		} else {
			return SceneObjectDatabase_old.getSceneObjectVisualsByName_novariablecheck(search,searchPool);
		}



	}

	/** check for variables that only apply to Divs, and return their results if found **/
	public static Set<IsSceneDivObject> getSceneDivObjectFromDivSpecificVariable(String searchVariable){

		SceneWidgetVisual.Log.info("getting-:" + searchVariable.toLowerCase());
		Set<IsSceneDivObject> results = new HashSet<IsSceneDivObject>();


		if (searchVariable.equalsIgnoreCase("<LASTDIVUPDATED>")) {

			results.add(InstructionProcessor.lastDivObjectUpdated);

			return results;

		} else if (searchVariable.equalsIgnoreCase("<ALLDIVOBJECTS>")) {

			SceneWidgetVisual.Log.info("returning all div objects");
			results.addAll( SceneObjectDatabase_old.all_div_objects.values());

			return results;
		}

		return null;

	}

	/** check for variables that only apply to input, and return their results if found **/
	public static Set<SceneInputObject> getSceneInputObjectFromInputSpecificVariable(String searchVariable){

		SceneWidgetVisual.Log.info("getting-:" + searchVariable.toLowerCase());
		Set<SceneInputObject> results = new HashSet<SceneInputObject>();


		if (searchVariable.equalsIgnoreCase("<LASTINPUTUPDATED>")) {

			results.add(InstructionProcessor.lastInputObjectUpdated);

			return results;

		} else if (searchVariable.equalsIgnoreCase("<ALLINPUTOBJECTS>")) {

			SceneWidgetVisual.Log.info("returning all input objects");
			results.addAll( SceneObjectDatabase_old.all_input_objects.values() );

			return results;
		}

		return null;

	}

	public static Set<SceneVectorObject> getSceneVectorObjectFromVectorSpecificVariable(String searchVariable){

		Set<SceneVectorObject> results = new HashSet<SceneVectorObject>();


		if (searchVariable.equalsIgnoreCase("<LASTVECTORUPDATED>")) {

			results.add( InstructionProcessor.lastVectorObjectUpdated);

			return results;

			//somewhat redundant as a global function <ALLOBJECTS> can do similiar
		} else if (searchVariable.equalsIgnoreCase("<ALLVECTOROBJECTS>")) {

			SceneWidgetVisual.Log.info("getting all vector objects");

			results.addAll(SceneObjectDatabase_old.all_vector_objects.values());


			return results;

		}

		return null;

	}

	/**
	 * Gets dialogue objects that meet the requirements specified <br>
	 * <br>
	 * The basic process is:<br><br>
	 * 1. Run generic variable checks on the search string, and return objects if found (ie, maybe its asking for "all objects of this type" or maybe its a "<:Property" search)<br>
	 * 2. Run dialogue object variable checks (ie, we now look for variables in the search string that are only applicable to dialogue objects. Like <LASTTEXTUPDATED><br>
	 * 3. If none of the above found we then simply look for any scene objects that match the name specified in the search string.<br>
	 *<br>
	 * @param search<br>
	 * @param callingObject<br>
	 * @param searchGlobal<br>
	 * @return a set of matching objects, or a zero length set if none found<br>
	 * 
	 * NOTE: currently searchs both text and dialogue objects, as all dialogue objects are also text objects
	 */
	public static Set<SceneLabelObject> getTextObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{				
		//ensure the search string is trimmed
		search = search.trim();

		SceneWidgetVisual.Log.info("Using the new text object search function to search for:"+search+" global:"+searchGlobal);

		Set<SceneLabelObject> searchPool = new HashSet<SceneLabelObject>();

		//first get the relivant objects to search
		if (searchGlobal){
			searchPool.addAll(SceneObjectDatabase_old.all_text_objects.values());
		} else {
			Set<SceneDialogObject> dobjects = InstructionProcessor.currentScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.DialogBox);
			searchPool.addAll(dobjects);
			Set<SceneLabelObject> lobjects = InstructionProcessor.currentScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Label);
			searchPool.addAll(lobjects);
			//searchPool.addAll(InstructionProcessor.currentScene.getScenesData().SceneDialogObjects);
		//	searchPool.addAll(InstructionProcessor.currentScene.getScenesData().SceneTextObjects);			
		}	

		SceneWidgetVisual.Log.info("Num of objects to search:"+searchPool.size());
		
		//if the search pool is empty we return a empty set
		if (searchPool.size()==0){
			SceneWidgetVisual.Log.info("No objects in search list so returning a zero length set");

			return searchPool;
		}
		
		//first we look if there's a < in the search string, as that identifies a variable.
		//if none is found we just do a novariable check here without bothering to look for variables
		if (search.contains("<"))
		{
			Set<SceneLabelObject> results = SceneObjectDatabase_old.getObjectsFromSpecificVariable(search, callingObject, searchPool); //This function will check for any functions like "<ALLOBJECTS>" or property specific searchs

			//if results are found we just return them
			if (results != null && results.size()>0){

				SceneWidgetVisual.Log.info("specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		

			SceneWidgetVisual.Log.info("No specific variable checks found for:"+search);

			//if not we check for text object specific things
			//These checks will only ever return a text object type.
			results = SceneObjectDatabase_old.getSceneTextObjectFromTextSpecificVariable(search);

			//if results are found we just return them
			if (results != null && results.size()>0){

				SceneWidgetVisual.Log.info("text specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		

		}
		//finally if none of the fancy pants variables are used we look for just a object with this name
		if (searchGlobal){


			SceneWidgetVisual.Log.info("Searching globally for:"+search);

			//if we are on a global search we can take a nifty shortcut here
			//This is because the global game variables storing the object types are hasmaps with the names of the object used as the key
			//this is much quicker then looping over
			return SceneObjectDatabase_old.all_text_objects.get(search.toLowerCase());

		} else {

			return SceneObjectDatabase_old.getSceneObjectVisualsByName_novariablecheck(search,searchPool);
		}


	}

	public static Set<SceneLabelObject> getSceneTextObjectFromTextSpecificVariable(String searchVariable){

		Set<SceneLabelObject> results = new HashSet<SceneLabelObject>();

		//Note; this isnt clicked on, but the one that last changed.
		if (searchVariable.equalsIgnoreCase("<LASTTEXTUPDATED>")) {
			results.add(InstructionProcessor.lastTextObjectUpdated );
			return results;
		}

		//We look for last scene item (really object, this is badly named)
		//This has to be checked here and only used if its a text object.
		if (searchVariable.equalsIgnoreCase("<LASTSCENEITEM>")) {

			if (InstructionProcessor.lastSceneObjectClickedOn!=null && InstructionProcessor.lastSceneObjectClickedOn.isTextObject()){
				results.add(InstructionProcessor.lastSceneObjectClickedOn.getAsDialog()); //Note casts to to Dialogue. If SceneText and SceneDialogue were more cleanly seperate objects this will cause issues for text objects cast. In future we should check type and cast to the correct one only
				return results;

			} else {

				SceneWidgetVisual.Log.info("last sceneobject clicked on was not a text object");

			}


		}

		return null;
	}

	/**
	 * Gets vector objects that meet the requirements specified <br>
	 * <br>
	 * The basic process is<br><br>
	 * 1. Run generic variable checks on the search string, and return objects if found (ie, maybe its asking for "all objects of this type" or maybe its a "<:Property" search)<br>
	 * 2. Run vector object variable checks (ie, we now look for variables in the search string that are only applicable to vector objects. Like <HELDITEM>)<br>
	 * 3. If none of the above found we then simply look for any scene objects that match the name specified in the search string.<br>
	 *<br>
	 * @param search
	 * @param callingObject
	 * @param searchGlobal
	 * @return a set of matching objects, or a zero length set if none found
	 */
	public static Set<? extends SceneVectorObject> getVectorObjectNEW(String search,SceneObject callingObject, boolean searchGlobal) 
	{
		//ensure the search string is trimmed
		search = search.trim();

		SceneWidgetVisual.Log.info("Using the new vector object search function to search for:"+search+" global:"+searchGlobal);

		Set<SceneVectorObject> searchPool = new HashSet<SceneVectorObject>();

		//first get the relivant objects to search
		if (searchGlobal){
			searchPool.addAll(SceneObjectDatabase_old.all_vector_objects.values());
		} else {
			//searchPool.addAll(InstructionProcessor.currentScene.getScenesData().SceneVectorObjects);		
			
			Set<SceneVectorObject> lobjects = InstructionProcessor.currentScene.getScenesData().getScenesCurrentObjectsOfType(SceneObjectType.Vector);
			searchPool.addAll(lobjects);
		}	

		SceneWidgetVisual.Log.info("Num of objects to search:"+searchPool.size());
		
		//if the search pool is empty we return a empty set
		if (searchPool.size()==0){
			SceneWidgetVisual.Log.info("No objects in search list so returning a zero length set");

			return searchPool;
		}
		
		//first we look if there's a < in the search string, as that identifies a variable.
		//if none is found we just do a novariable check here without bothering to look for variables
		if (search.contains("<"))
		{
			Set<SceneVectorObject> results = SceneObjectDatabase_old.getObjectsFromSpecificVariable(search, callingObject, searchPool); //This function will check for any functions like "<ALLOBJECTS>" or property specific searchs

			//if results are found we just return them
			if (results != null && results.size()>0){

				SceneWidgetVisual.Log.info("specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		

			SceneWidgetVisual.Log.info("No specific variable checks found for:"+search);

			//if not we check for vector object specific things
			//These checks will only ever return a text object type.
			results = getSceneVectorObjectFromVectorSpecificVariable(search);

			//if results are found we just return them
			if (results != null && results.size()>0){
				SceneWidgetVisual.Log.info("vector specific variable check found results for:"+search+" global:"+searchGlobal);
				return results;
			}		
		}

		//finally if none of the fancy pants variables are used we look for just a object with this name
		if (searchGlobal){
			//if we are on a global search we can take a nifty shortcut here
			//This is because the global game variables storing the object types are hasmaps with the names of the object used as the key
			//this is much quicker then looping over which is done within getSceneObjectByName
			return SceneObjectDatabase_old.all_vector_objects.get(search.toLowerCase());

		} else {
			return SceneObjectDatabase_old.getSceneObjectVisualsByName_novariablecheck(search,searchPool);
		}


	}

	/**
	 * This function will only return things that will be of the type requested.<br>
	 * That is, of the type "T" in the supplied set of objects.<br>
	 * <br>
	 * So really this function will only look within searchWithinThese for stuff, as there the only things genertied to be of the type asked
	 * <LASTSCENEITEM> for example wont be checked for.<br> As that can be any type of scene object it might not be of the type we are looking for,
	 * and thus it shouldnt return it. (All TextObjects are SceneObjects, but not all SceneObjects are text objects!)<br>
	 * <br>
	 * This function has to ONLY return objects of the type asked for, and it does that with "T extends SceneObject" as the return type<br>
	 * and (the same T) repeated elsewhere for the types to always match.<br>
	 * <br>
	 * Generics can be confusing.<br>
	 * <br>
	 * <br>
	 * @param name
	 * @param callingObject
	 * @param searchWithinTheseObjects
	 * @return
	 */
	public static <T extends SceneObjectVisual> Set<T> getObjectsFromSpecificVariable (String name, SceneObject callingObject, Set<T> searchWithinTheseObjects) {
	
		//if the search pool is empty we return a empty set
				if (searchWithinTheseObjects.size()==0){
					SceneWidgetVisual.Log.info("No objects in search list so returning a zero length set");

					return searchWithinTheseObjects;
				}
				
		if (name.equalsIgnoreCase("<ALLOBJECTS>")) {			
			//just return all the objects in searchWithinTheseObjects!
			return searchWithinTheseObjects;	

		} else if (name.startsWith("<PROPERTY:")) {

			String propertyString = name.substring("<PROPERTY:".length(),name.length()-1);
			SceneWidgetVisual.Log.info("Getting all Objects with property:"+propertyString+" new method");

			//we search for all the objects in the game with the specified property
			//Note;  as its only searching within objects of type T, then it should be safe to cast to type T on the return
			//The JAVA doesnt know this though so it thinks its safe
			return (Set<T>) getObjectsWithPropertyNEW(propertyString,searchWithinTheseObjects);

		} 
		
		
		

		return null;

	}

	/**
	 * This function looks over the given objects and returns those with a matching name
	 * 
	 * Note the fancy pants use of <? extends SceneObject> as both the return type and of the type of objects we are searching.
	 * This means we can take any sceneobject type as the search pool, and return any type too
	 * See some answers here for the formating of this "T extends" buisness;
	 * 
	 * http://stackoverflow.com/questions/13116175/how-to-make-method-parameter-type-arraylistobject-take-different-object-types
	 * 
	 * Basicaly we are saying "T can be any SceneObject type" then saying "we return a arraylist of type T"
	 * then for the searchWithin parameter we declate it also as the same type "T"
	 * Notice we continue to use T further down in the code to - it means the same SceneObject type all the way
	 * 
	 * @param name
	 * @param searchWithinTheseObjects
	 * @return a set of matching objects, or a zero length set if none found
	 */
	public static <T extends SceneObject> Set<T> getSceneObjectVisualsByName_novariablecheck(String name, Set<T> searchWithinTheseObjects) 
	{				
		Set<T> matchs = new HashSet<T>();
		//finnally we search for just matching names in all supplied objects
		for (T sceneobject : searchWithinTheseObjects) {

			if (sceneobject.objectsCurrentState.ObjectsName.equalsIgnoreCase(name)){
				matchs.add(sceneobject);
			}

		}				
		SceneWidgetVisual.Log.info("Found:"+matchs.size());

		return matchs;		
	}

/**
 * Returns a sceneobject of the type specified by ofType
 * Internally just uses the getSingle___ObjectNEW methods, see them for a referance on other parameters
 * 
 * @param search
 * @param callingObject
 * @param searchGlobal
 * @param ofType
 * @return
 */
	public static SceneObject getSingleObjectOfType(String search,SceneObject callingObject, boolean searchGlobal, SceneObjectType ofType)
	{
		SceneObject result = null;
		//pick a object based on type
		switch (ofType) {
		
		case Sprite:
			result = getSingleSpriteObjectNEW(search, callingObject, searchGlobal);
			break;
			
		case Label:
			result = getSingleTextBoxObjectNEW(search, callingObject, searchGlobal);
			break;
			
		case DialogBox:
			result = getSingleTextBoxObjectNEW(search, callingObject, searchGlobal);
			break;
			
		case Div:
			//currently uses a cast to go from "IsDivObject" to "SceneObject". ALL game objects should extend sceneobject so this should
			//always be safe. However the interface "IsDivObject" doesn't know that it will only be applied to SceneObjects
			//debating 
			result = (SceneObject)getSingleDivObjectNEW(search, callingObject, searchGlobal);
			break;
			
		case Input:
			result = getSingleInputObjectNEW(search, callingObject, searchGlobal);
			break;
		
		case Vector:
			result = getSingleVectorObjectNEW(search, callingObject, searchGlobal);
			break;
			
		default:
			break;
		
		}
		
		
		return result;		
		
	}

public static void removeObjectFromAllLists(String objectsName) {

	//names are always stored lowercase, so we convert to ensure a name supplied is also lowercase
	objectsName = objectsName.toLowerCase();
	
	//remove from all known object types	
	all_sprite_objects.removeAll(objectsName);
	all_text_objects.removeAll(objectsName);
	all_div_objects.removeAll(objectsName);
	all_input_objects.removeAll(objectsName);
	all_vector_objects.removeAll(objectsName);
	
}

}
