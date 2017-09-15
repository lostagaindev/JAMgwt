package com.lostagain.JamGwt.deprecated;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import com.lostagain.Jam.SceneObjects.SceneObject;

//An listarray of collision boxs 

/** no longer used, replaced by the com.darkflame.client.JargScene.CollisionMap.* set of classes
 * **/
@Deprecated
public class OldCollisionMap extends ArrayList<SimpleCollisionBox> {



	/**
	 * still no clue what this is for...eclipse just likes them
	 */
	private static final long serialVersionUID = 5209938317787947426L;
	
	
	static Logger Log = Logger.getLogger("cmap");
	
	public OldCollisionMap(String mapData,SceneObject so){

		// divide by newlines
				String lines[] = mapData.trim().split("\r");

				for (int i = 0; i < lines.length; i++) {

					String currentBox = lines[i].trim();

					int tx = Integer.parseInt(currentBox.split(",")[0]);
					int ty = Integer.parseInt(currentBox.split(",")[1]);
					int bx = Integer.parseInt(currentBox.split(",")[2]);
					int by = Integer.parseInt(currentBox.split(",")[3]);

					SimpleCollisionBox newbox = new SimpleCollisionBox(tx, ty, bx, by);

					if (so!=null){
						newbox.AssociatedObject=so;

						Log.info("added collision map:" + tx + "," + ty + " to " + bx + ","
								+ by+"     on       "+so.getObjectsCurrentState().ObjectsName);
						
					}
					
					this.add(newbox);

					Log.info("added collision map:" + tx + "," + ty + " to " + bx + ","
							+ by);

				}
		
	}

	public OldCollisionMap() {
		
	}
	
	
	
}
