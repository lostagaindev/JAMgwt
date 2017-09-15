package com.lostagain.JamGwt.JargScene;

import com.lostagain.Jam.Movements.SimpleVector3;
import com.lostagain.Jam.Scene.CoordinateSpaceConvertor;

public class GwtCoOrdinateSpaceConverter extends CoordinateSpaceConvertor {

	@Override
	public SimpleVector3 convert_impl(SimpleVector3 incomingCordinates, CordinateInterpretation mode) {
		
		
		switch(mode)
		{
		case Gdx3d:
			return incomingCordinates; //currently not used, and unlikely to have a gwt implementation anyway
		case Web2D:
			return incomingCordinates; //no conversion needed
			//todo; WebIsometrix
			//the visual represention means that Y effects the position on the monitor half as much as X does.
			//That is, going west 10 units and then north 10 units results in a position on the screen 10 pixels left and 5 up.
			//z is handled the same as web2d
		default:
			break;
		
		}
		
		return null;
	}

}
