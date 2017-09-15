/**
 * FOR FUTURE USE<br>
 * <br>
 * These interfaces are intended to help split the JAM engine into two parts;<br>
 * <br>
 * JAMcore - pure Java code separated from all GUI specific things. This package will contain the Instruction processor, SceneObject and every non-visual thing needed to make those two work. 
 * JAMgwt - GWT specific widgets and implementation (imports JAMCore)<br>
 * <br>
 * Separating these two is a lot of work.
 * Part of the steps necessary is making SceneObject not depend on FocusPanel, and to make SceneDivObject implement focus panel itself (somehow)<br>
 * Then all the other sceneobject types extend SceneDivObject rather then SceneObject
 * (the only method SceneDivObject has others should not have is setDivsWidget(), so that should be hidden)
 * <br>
 * This probably means changes to SceneWidget as well, in order for it to place SceneDivObjects on the page, rather then SceneObjects (SceneWidget would become specific to GWT and probably need a similar split to SceneObject)<br> 
 * <br>
 * After All THAT is done 
 * Instruction processor will need to be changed not to implement any of the SceneObject types other then the basic "SceneObject"
 * Other types will need to be instead done via interfaces....and those will be defined here.
 * <br>
 * Basically this lets instruction processor know what's expected of, say, a SceneDivObject, without needing to know
 * any specific GWT code (which it can't if its pure Java). So instead it will know things that implement "isSceneDivObjects" are "things" that can be positioned,sized, and have styles applied to them.
 * Similarly it will understand anything that implements "isSceneSpriteObjects" has frames, and can set its playback state.<br>
 *<br>
 * This means JAMcore can understand what JAMgwts widgets can do (roughly) while still allowing non-GWT things to use it. Those non-gwt things can even make their own SceneDivObject replacements...provided they also 
 * have the same functions and implement "IsSceneDivObject"
 * <br>
 * phwee...its a lot of work, but thats the plan of attack.<br>
 * And this interface package is in preparation for it<br>
 * <br>
 *
 **/

//Structure concept
//IsSceneObject probably not needed - we extend SceneObject anyway with all object types, which will do the same job
//specific scene object types will have interfaces however, and implementations can mix and match them as needed.
package com.lostagain.JamGwt.JargScene.SceneObjects.Interfaces;