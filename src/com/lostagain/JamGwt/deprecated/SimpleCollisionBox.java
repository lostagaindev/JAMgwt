package com.lostagain.JamGwt.deprecated;

import java.util.logging.Logger;


import com.google.gwt.user.client.ui.SimplePanel;
import com.lostagain.Jam.SceneObjects.SceneObject;

import lostagain.nl.spiffyresources.client.spiffycore.Simple2DPoint;


/** no longer used, replaced by the com.darkflame.client.JargScene.CollisionMap.* set of classes
 * **/
@Deprecated
class SimpleCollisionBox {

	static Logger Log = Logger.getLogger("JAM.SimpleCollisionBox");
	int itlx = 0;
	int itly = 0;
	int ibrx = 0;
	int ibry = 0;
	SceneObject AssociatedObject = null; // if this box is associated with a
											// scene object
	
	SimplePanel visualHelper=null;
	


	public SimpleCollisionBox(int tlx, int tly, int brx, int bry) {

		this.itlx = tlx;
		this.itly = tly;
		this.ibrx = brx;
		this.ibry = bry;

	}

	
	public SpiffyCollision TestForCollisionWithLine(int ax, int ay, int bx,
			int by) {

		// test each side for collision
		SpiffyCollision top = null;
		SpiffyCollision bottom = null;
		SpiffyCollision left = null;
		SpiffyCollision right = null;

		// stupid method to get the minimum
		int disToTop = 1000000;
		int disToLeft = 1000000;
		int disToBottom = 1000000;
		int disToRight = 1000000;

		int tlx = itlx;
		int tly = itly;
		int brx = ibrx;
		int bry = ibry;

		// update based on object
		if (AssociatedObject != null) {

			tlx = itlx + AssociatedObject.getObjectsCurrentState().X;
			tly = itly + AssociatedObject.getObjectsCurrentState().Y;
			brx = ibrx + AssociatedObject.getObjectsCurrentState().X;
			bry = ibry + AssociatedObject.getObjectsCurrentState().Y;

		}

		// top line
		Simple2DPoint start = new Simple2DPoint(tlx, tly);
		Simple2DPoint end = new Simple2DPoint(brx, tly);
		Simple2DPoint toptest = lineIntersect(ax, ay, bx, by, start.x, start.y,
				end.x, end.y);
		if (toptest != null) {

			// work out distance
			disToTop = (int) Math.hypot(Math.abs(toptest.x - ax),
					Math.abs(toptest.y - ay));

			top = new SpiffyCollision(toptest.x, toptest.y, this,
					SpiffyCollision.CollisionSide.Top, disToTop);

			Log.info("disToTop = " + disToTop);

		}

		// left line
		Simple2DPoint lstart = new Simple2DPoint(tlx, tly);
		Simple2DPoint lend = new Simple2DPoint(tlx, bry);
		Simple2DPoint lefttest = lineIntersect(ax, ay, bx, by, lstart.x,
				lstart.y, lend.x, lend.y);
		if (lefttest != null) {

			// work out distance
			disToLeft = (int) Math.hypot(Math.abs(lefttest.x - ax),
					Math.abs(lefttest.y - ay));

			left = new SpiffyCollision(lefttest.x, lefttest.y, this,
					SpiffyCollision.CollisionSide.Left, disToLeft);

			Log.info("disToLeft = " + disToLeft);
		}

		// bottom line
		Simple2DPoint bstart = new Simple2DPoint(tlx, bry);
		Simple2DPoint bend = new Simple2DPoint(brx, bry);
		Simple2DPoint bottomtest = lineIntersect(ax, ay, bx, by, bstart.x,
				bstart.y, bend.x, bend.y);
		if (bottomtest != null) {

			// work out distance
			disToBottom = (int) Math.hypot(Math.abs(bottomtest.x - ax),
					Math.abs(bottomtest.y - ay));
			Log.info("disToBottom = " + disToBottom);

			bottom = new SpiffyCollision(bottomtest.x, bottomtest.y, this,
					SpiffyCollision.CollisionSide.Bottom, disToBottom);
		}
		// right line
		Simple2DPoint rstart = new Simple2DPoint(brx, tly);
		Simple2DPoint rend = new Simple2DPoint(brx, bry);
		Simple2DPoint righttest = lineIntersect(ax, ay, bx, by, rstart.x,
				rstart.y, rend.x, rend.y);
		if (righttest != null) {

			// work out distance
			disToRight = (int) Math.hypot(Math.abs(righttest.x - ax),
					Math.abs(righttest.y - ay));

			right = new SpiffyCollision(righttest.x, righttest.y, this,
					SpiffyCollision.CollisionSide.Right, disToRight);

			Log.info("disToRight = " + disToRight);
		}

		// return nearest only
		int nearest = Math.min(disToRight,
				Math.min(disToLeft, Math.min(disToTop, disToBottom)));

		// Log.info("nearest=" + nearest);

		if (nearest > 99000) {
			return null;
		}

		// Log.info("finding shortest=" + nearest);
		if (nearest == disToRight) {
			return right;
		}
		if (nearest == disToBottom) {
			return bottom;
		}
		if (nearest == disToLeft) {
			return left;
		}
		if (nearest == disToTop) {
			return top;
		}

		return null;

	}

	public static Simple2DPoint lineIntersect(Simple2DPoint start, Simple2DPoint end,
			Simple2DPoint start2, Simple2DPoint end2) {

		return lineIntersect(start.x, start.y, end.x, end.y, start2.x,
				start2.y, end2.x, end2.y);

	}

	/** returns the intersection between two lines or null **/
	public static Simple2DPoint lineIntersect(double x1, double y1, double x2,
			double y2, double x3, double y3, double x4, double y4) {

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (denom == 0.0) { // Lines are parallel.
			return null;
		}
		double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
		double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
		if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
			// Get the intersection point.
			return new Simple2DPoint((int) (x1 + ua * (x2 - x1)), (int) (y1 + ua
					* (y2 - y1)));
		}

		return null;
	}

	
	public int getTopLeftX() {
		int tlx = itlx;

		// update based on object
		if (AssociatedObject != null) {

			tlx = itlx + AssociatedObject.getObjectsCurrentState().X;

		}

		return tlx;
	}

	public int getTopLeftY() {
		int tly = itly;

		// update based on object
		if (AssociatedObject != null) {

			tly = itly + AssociatedObject.getObjectsCurrentState().Y;

		}

		return tly;
	}

	public int getBottomRightX() {
		int brx = ibrx;

		// update based on object
		if (AssociatedObject != null) {

			brx = ibrx + AssociatedObject.getObjectsCurrentState().X;

		}

		return brx;
	}

	public int getBottomRightY() {
		int bry = ibry;

		// update based on object
		if (AssociatedObject != null) {

			bry = ibry + AssociatedObject.getObjectsCurrentState().Y;

		}

		return bry;
	}

	// nb: co-ordinates measured down, not up
	public boolean Yoverlap(int lowestY, int highestY) {

		int tlx = itlx;
		int tly = itly;
		int brx = ibrx;
		int bry = ibry;

		// update based on object
		if (AssociatedObject != null) {

			tlx = itlx + AssociatedObject.getObjectsCurrentState().X;
			tly = itly + AssociatedObject.getObjectsCurrentState().Y;
			brx = ibrx + AssociatedObject.getObjectsCurrentState().X;
			bry = ibry + AssociatedObject.getObjectsCurrentState().Y;

		}
		// overlap region is smaller then collision region
		if ((highestY < bry) && (highestY > tly)) {
			return true;
		}
		if ((lowestY < bry) && (lowestY > tly)) {
			return true;
		}
		// other way around (ie, overlapping object is bigger then this one
		if ((lowestY > bry) && (highestY < tly)) {
			return true;
		}

		return false;

	}

	public boolean Xoverlap(int lowestX, int highestX) {

		int tlx = itlx;
		int tly = itly;
		int brx = ibrx;
		int bry = ibry;

		// update based on object
		if (AssociatedObject != null) {

			tlx = itlx + AssociatedObject.getObjectsCurrentState().X;
			tly = itly + AssociatedObject.getObjectsCurrentState().Y;
			brx = ibrx + AssociatedObject.getObjectsCurrentState().X;
			bry = ibry + AssociatedObject.getObjectsCurrentState().Y;

		}
		// overlap region is smaller then collision region
		if ((highestX > tlx) && (highestX < brx)) {
			return true;
		}
		if ((lowestX > tlx) && (lowestX < brx)) {
			return true;
		}
		// other way around (ie, overlapping object is bigger then this one
		if ((lowestX < tlx) && (highestX > brx)) {
			return true;
		}

		return false;

	}

	/** checks if point is inside this box **/
	public boolean isInside(int x, int y) {

		int tlx = itlx;
		int tly = itly;
		int brx = ibrx;
		int bry = ibry;

		// update based on object
		if (AssociatedObject != null) {

			tlx = itlx + AssociatedObject.getObjectsCurrentState().X;
			tly = itly + AssociatedObject.getObjectsCurrentState().Y;
			brx = ibrx + AssociatedObject.getObjectsCurrentState().X;
			bry = ibry + AssociatedObject.getObjectsCurrentState().Y;

		}
		// Log.info("testing x=" + x + " > " + tlx + " &<" + brx);
		// Log.info("testing y=" + y + " > " + tly + " &<" + bry);

		if ((x > tlx) && (x < brx)) {

			if ((y > tly) && (y < bry)) {

				return true;

			}

		}

		return false;
	}

}