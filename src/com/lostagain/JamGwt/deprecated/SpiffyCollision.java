package com.lostagain.JamGwt.deprecated;


/** no longer used, replaced by the com.darkflame.client.JargScene.CollisionMap.* set of classes
 * **/
@Deprecated
class SpiffyCollision {

	public SpiffyCollision(int x, int y, SimpleCollisionBox collidingObject,
			CollisionSide side, int distance) {
		super();
		X = x;
		Y = y;
		this.collidingObject = collidingObject;
		this.side = side;
		this.distance = distance;
	}

	int X = -1; // x location of collision
	int Y = -1; // y location of collision
	SimpleCollisionBox collidingObject;
	int distance = 0;

	enum CollisionSide {
		Left, Right, Top, Bottom
	}

	CollisionSide side;

}