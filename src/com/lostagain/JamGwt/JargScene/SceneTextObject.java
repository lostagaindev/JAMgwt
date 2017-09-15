package com.lostagain.JamGwt.JargScene;

import com.google.gwt.dom.client.Style.Display;
import com.lostagain.Jam.InstructionProcessing.ActionList;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObjectState;

/** Scene text object is a simplified dialogue object , perhaps wasteful to downgrade a object like this
 * but for now it adds features and simplies code
 * better would be to make dialogue a extension of this, not visa versa..
 * 
 * **/
//NOTE: Due to this inheriting dialogue object it means dialogue object shouldn't set its type (not that it should need to)
//if dialogue object does set its type rather then taking it from the newobjectdata, then we should set it back to label here (but lets avoide that)

//Replaced with SceneLabelObject
@Deprecated
public class SceneTextObject extends SceneDialogObject {

	public SceneTextObject(SceneObjectState newobjectdata, ActionList actions,
			SceneWidget sceneItsOne) {
		super(newobjectdata, actions, sceneItsOne);
		
		this.setSize("auto", "auto");
		this.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		
		super.TextLabel.getInternalWidget().setSize("auto", "auto");
		super.TextLabel.getInternalWidget().getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		
	}
//not ready yet
	/*
	public SceneTextObject(SceneDialogObjectState data, SceneWidgetVisual sceneItsOne) {
		
		super(data, sceneItsOne);
		this.setSize("auto", "auto");
		this.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		
		super.TextLabel.setSize("auto", "auto");
		super.TextLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		
	}
*/
	

	

}
