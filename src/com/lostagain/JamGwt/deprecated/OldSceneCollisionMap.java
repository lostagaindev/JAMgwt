package com.lostagain.JamGwt.deprecated;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;


import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.lostagain.Jam.Movements.MovementPath;
import com.lostagain.Jam.Movements.MovementWaypoint;
import com.lostagain.Jam.Movements.MovementWaypoint.MovementType;
import com.lostagain.Jam.SceneObjects.SceneObject;
import com.lostagain.JamGwt.JargScene.SceneObjectVisual;
import com.lostagain.JamGwt.JargScene.SceneWidgetVisual;

import lostagain.nl.spiffyresources.client.spiffycore.Simple2DPoint;

/**
 * Scene collision maps are very simple, but fast, arrays of box's. you can test
 * if a point is inside any box
 **/

/** no longer used, replaced by the com.darkflame.client.JargScene.CollisionMap.* set of classes
 * **/
@Deprecated //we now use vector maps...which turned out easier, weirdly.
public class OldSceneCollisionMap {

	static Logger Log = Logger.getLogger("JAM.OldSceneCollisionMap");

	OldCollisionMap scenesOwnMap;

	static Logger GreyLog = Logger.getLogger("scene cmap");

	HTML svgCalculatedPath = new HTML();
	HTML svgRefinedPath = new HTML();

	// preview widget - helps with debugging by visualising the map
	public AbsolutePanel previewRoot = new AbsolutePanel();

	// svg widget - helps with debugging by visualising the map
	public AbsolutePanel svgLayer = new AbsolutePanel();
	String PathfindingColor = "blue";

	SceneWidgetVisual sourcescene = null;

	private int SearchIterator = 0;

	private int collisionmargin = 20;

	private int maxIterations = 8;

	/**
	 * load collision box map in the form x,y,x2,y2 x,y,x2,y2 ..
	 * **/
	public OldSceneCollisionMap(String mapData, SceneWidgetVisual sourcescene) {

		this.sourcescene = sourcescene;

		scenesOwnMap = new OldCollisionMap(mapData, null);

		generatePreviewWidget();

		svgCalculatedPath = new HTML(
				"<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"400\" width=\"450\"><path id=\"lineBC\" d=\"M 0 0 l 300 300\" stroke=\"red\" stroke-width=\"3\" fill=\"none\" /></svg>");

		svgLayer.add(svgCalculatedPath);
		svgLayer.add(svgRefinedPath, 0, 0);

	}

	private void svgRootUpdate(String map) {

		GreyLog.info("updating svg layer");
		svgLayer.setSize("100%", "100%");
		
		svgLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		
		svgCalculatedPath.getElement().getStyle().setZIndex(99999);
		
		svgLayer.remove(svgCalculatedPath);

		svgCalculatedPath = new HTML(
				"<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"4000\" width=\"4000\"><path id=\"lineBC\" d=\""
						+ map
						+ "\" stroke=\""
						+ PathfindingColor
						+ "\" stroke-width=\"3\" fill=\"none\" /></svg>");

		svgLayer.add(svgCalculatedPath, 0, 0);

	}

	private void svgSmoothPathUpdate(String map) {

		GreyLog.info("updating svg smooth path");
		svgLayer.setSize("100%", "100%");
		svgLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		svgLayer.getElement().getStyle().setZIndex(99999);

		svgLayer.remove(svgRefinedPath);

		svgRefinedPath = new HTML(
				"<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"4000\" width=\"4000\"><path id=\"lineBC\" d=\""
						+ map
						+ "\" stroke=\""
						+ "green"
						+ "\" stroke-width=\"3\" fill=\"none\" /></svg>");

		svgLayer.add(svgRefinedPath, 0, 0);

	}

	private void generatePreviewWidget() {

//		previewRoot.setSize("100%", "100%");
//		previewRoot.getElement().getStyle().setOverflow(Overflow.VISIBLE);
//		previewRoot.getElement().getStyle().setZIndex(99999);
//
//		previewRoot.clear();
//
//		// loop over seens own map
//		Iterator<SimpleCollisionBox> bit = scenesOwnMap.iterator();
//		while (bit.hasNext()) {
//			SimpleCollisionBox cbox = (SimpleCollisionBox) bit.next();
//
//			// simple panel for each
//			SimplePanel newshader = new SimplePanel();
//			newshader.getElement().getStyle().setBackgroundColor("#A00");
//			newshader.getElement().getStyle().setOpacity(0.7);
//
//			newshader.setPixelSize(cbox.getBottomRightX() - cbox.getTopLeftX(),
//					cbox.getBottomRightY() - cbox.getTopLeftY());
//
//			previewRoot.add(newshader, cbox.getTopLeftX(), cbox.getTopLeftY());
//
//			// associate simple panel with cbox, helps debug pathfinding by
//			// highlighting box
//			cbox.visualHelper = newshader;
//
//		}
//
//		GreyLog.info("setting up cmap display for objects");
//
//		// now any objects on the scene which have maps
//
//		Iterator<SceneObject> soit = sourcescene.scenesData.scenesObjects
//				.iterator();
//		while (soit.hasNext()) {
//			SceneObject sceneObject = (SceneObject) soit.next();
//
//			if (sceneObject.cmap != null) {
//				Iterator<SimpleCollisionBox> box = sceneObject.cmap.iterator();
//
//				Log.info("setting up cmap display for "
//						+ sceneObject.objectsData.ObjectsName);
//
//				while (box.hasNext()) {
//					SimpleCollisionBox cbox = (SimpleCollisionBox) box.next();
//
//					// simple panel for each
//					SimplePanel newshader = new SimplePanel();
//					newshader.getElement().getStyle()
//							.setBackgroundColor("#A00");
//					newshader.getElement().getStyle().setOpacity(0.7);
//
//					newshader.setPixelSize(
//							cbox.getBottomRightX() - cbox.getTopLeftX(),
//							cbox.getBottomRightY() - cbox.getTopLeftY());
//
//					previewRoot.add(newshader, cbox.getTopLeftX(),
//							cbox.getTopLeftY());
//
//					Log.info("set up cmap display at " + cbox.getTopLeftX()
//							+ ", " + cbox.getTopLeftY() + " ");
//
//					cbox.visualHelper = newshader;
//
//				}
//
//			} else {
//
//				GreyLog.info("no cmap for this object:"
//						+ sceneObject.objectsData.ObjectsName);
//
//			}
//
//		}

	}

	public boolean isCmapVisible() {
		return previewRoot.isVisible();
	}

	public boolean isPathVisible() {
		return svgLayer.isVisible();
	}

	public void showCmap(boolean show) {

		generatePreviewWidget(); // update it
		previewRoot.setVisible(show);

	}

	public void showPath(boolean show) {
		svgLayer.setVisible(show);
	}

	/**
	 * draws a line between too points looking for collisions. If it finds none,
	 * it returns a two point array specifying the path. If, however, it finds a
	 * collision, it looks both above and below the box for a new path, calling
	 * itself again from the boxs corner.
	 * 
	 * Returns: - A path to the ex ey point or null for no path found
	 **/
	public MovementPath findPathBetweenAndAddToExistingPath(
			int ex, int ey, SceneObject movingObject, MovementPath np) {

		int sx = np.get(np.size()-1).pos.x;
		int sy = np.get(np.size()-1).pos.y;
		
		//if your already at the destination then stop
		if ((sx==ex)&&(sy==ey)){
			return np;
		}
		
		// get list of all collision boxs, both the scenes and all the objects
		// This can probably be done much better by not making a new object
		// here, or at least caching if we do
		OldCollisionMap combined = new OldCollisionMap();
		combined.addAll(scenesOwnMap);
		Iterator<SceneObject> soit = sourcescene.getScenesData().scenesOriginalObjects
				.iterator();
		
		while (soit.hasNext()) {
			SceneObject so = (SceneObject) soit.next();
			if (so.cmap != null) {
			//	combined.addAll(so.cmap);
			}
		}

		// find details of next collision:
		Iterator<SimpleCollisionBox> bit = combined.iterator();

		if (np.size() < 11) {

			PathfindingColor = "blue";
			SpiffyCollision NearestCollision = null;

			// get all collisions and find closest
			while (bit.hasNext()) {
				SimpleCollisionBox box = (SimpleCollisionBox) bit.next();

				// test if start is bad, else its invalid already

				if (box.isInside(sx, sy)) {

					np.pathFindingBroke = true;
					PathfindingColor = "red";

				}

				// Log.info("_________________calculating intersect point ");
				SpiffyCollision test = box.TestForCollisionWithLine(sx, sy, ex,
						ey);

				if (test != null) {

					// is it nearer then last one

					if ((NearestCollision == null)) {

						GreyLog.info("new nearest");
						NearestCollision = test;
					} else if (test.distance < NearestCollision.distance) {

						GreyLog.info("new nearest");
						NearestCollision = test;

					}
				}

			}

			if (NearestCollision != null) {
				SearchIterator = 0;

				int nx = 0, ny = 0;
				int nx2 = 0, ny2 = 0;
				
		//	np.add(new MovementWaypoint(NearestCollision.X,
		//				NearestCollision.Y, MovementType.LineTo));

				sx = NearestCollision.X;
				sy = NearestCollision.Y;

				MovementPath path1 = new MovementPath(np, "path");
				MovementPath path2 = new MovementPath(np, "path2");

				// Left collision goes to bottom or top left
				if (NearestCollision.side == SpiffyCollision.CollisionSide.Left) {
					

					path1.add(new MovementWaypoint(NearestCollision.X-10,
							NearestCollision.Y, MovementType.AbsoluteLineTo));
					
					addWaypointAtTopLeft(NearestCollision, path1,false);
					addWaypointAtTopRight(NearestCollision, path1,false);
					
					path1 = findPathBetweenAndAddToExistingPath( ex, ey,
							movingObject, path1);


					path2.add(new MovementWaypoint(NearestCollision.X-10,
							NearestCollision.Y, MovementType.AbsoluteLineTo));
					
					
					// bottomleft
					addWaypointAtBottomLeft(NearestCollision, path2,false);
					addWaypointAtBottomRight(NearestCollision, path2,false);
					
					// path2 = findPathBetweenAndAddToExistingPath(sx, sy, nx2,
					// ny2,
					// movingObject, path2);

					path2 = findPathBetweenAndAddToExistingPath(ex,
							ey, movingObject, path2);
				}

				// Right goes to top right
				if (NearestCollision.side == SpiffyCollision.CollisionSide.Right) {


					path1.add(new MovementWaypoint(NearestCollision.X+10,
							NearestCollision.Y, MovementType.AbsoluteLineTo));
					
					// top right
					addWaypointAtTopRight(NearestCollision, path1,false);
					addWaypointAtTopLeft(NearestCollision, path1,false);
					
					// path1 = findPathBetweenAndAddToExistingPath(sx, sy, nx,
					// ny,
					// movingObject, path1);

					
					path1 = findPathBetweenAndAddToExistingPath( ex, ey,
							movingObject, path1);



					path2.add(new MovementWaypoint(NearestCollision.X+10,
							NearestCollision.Y, MovementType.AbsoluteLineTo));
					
					// first we try left, then right, and take the shorter
					addWaypointAtBottomRight(NearestCollision, path2,false);
					addWaypointAtBottomLeft(NearestCollision, path2,false);
					// path2 = findPathBetweenAndAddToExistingPath(sx, sy, nx2,
					// ny2,
					// movingObject, path2);
					path2 = findPathBetweenAndAddToExistingPath( ex,
							ey, movingObject, path2);

				}

				// top goes to top left
				if (NearestCollision.side == SpiffyCollision.CollisionSide.Top) {
					

					path1.add(new MovementWaypoint(NearestCollision.X,
							NearestCollision.Y-10, MovementType.AbsoluteLineTo));
					
				
					addWaypointAtTopLeft(NearestCollision, path1,false);
					addWaypointAtBottomLeft(NearestCollision, path1,false);
										
					path1 = findPathBetweenAndAddToExistingPath(ex, ey,
							movingObject, path1);
					

					path2.add(new MovementWaypoint(NearestCollision.X,
							NearestCollision.Y-10, MovementType.AbsoluteLineTo));

					addWaypointAtTopRight(NearestCollision, path2,false);
					addWaypointAtBottomRight(NearestCollision, path2,false);
					
					// path2 = findPathBetweenAndAddToExistingPath(sx, sy, nx2,
					// ny2,
					// movingObject, path2);

					path2 = findPathBetweenAndAddToExistingPath( ex,
							ey, movingObject, path2);

				}

				// bottom goes to bottom right
				if (NearestCollision.side == SpiffyCollision.CollisionSide.Bottom) {
					

					
					path1.add(new MovementWaypoint(NearestCollision.X,
							NearestCollision.Y+10, MovementType.AbsoluteLineTo));
					
					addWaypointAtBottomLeft(NearestCollision, path1,false);
					addWaypointAtTopLeft(NearestCollision, path1,false);

					// path1 = findPathBetweenAndAddToExistingPath(sx, sy, nx,
					// ny,
					// movingObject, path1);

					path1 = findPathBetweenAndAddToExistingPath( ex, ey,
							movingObject, path1);
					
					
					path2.add(new MovementWaypoint(NearestCollision.X,
							NearestCollision.Y+10, MovementType.AbsoluteLineTo));
			
					addWaypointAtBottomRight(NearestCollision, path2,false);
					addWaypointAtTopRight(NearestCollision, path2,false);
					
					// path2 = findPathBetweenAndAddToExistingPath(sx, sy, nx2,
					// ny2,
					// movingObject, path2);

					path2 = findPathBetweenAndAddToExistingPath( ex,
							ey, movingObject, path2);

				}

				
				Log.info("picking path");
				
				if (path1.pathFindingBroke&&path2.pathFindingBroke){
					Log.info("both routes broke");
				}
				
				if (path1.pathFindingBroke
						|| (path1.PathLength > path2.PathLength && !path2.pathFindingBroke)) {

					np = path2;

				} else {

					np = path1;
				}
				
				
				GreyLog.info("_________________collision point_______________"
						+ NearestCollision.X + "," + NearestCollision.Y + 150);

			}

		} else {
			// toggle "gave up route finding flag"
			np.pathFindingBroke = true;
			PathfindingColor = "red";
		}

		if (!np.pathFindingBroke) {
			PathfindingColor = "blue";
		}
		// use nearest collision

		// Log.info("adding fin");
		np.add(new MovementWaypoint(ex, ey, MovementType.AbsoluteLineTo));

		// Log.info("path:" + np.getAsSVGPath() + "|" + np.size());

		this.svgRootUpdate(np.getAsSVGPath());

		return np;

	}

	private void addWaypointAtBottomRight(SpiffyCollision NearestCollision,
			MovementPath path2,boolean addSubWaypoints) {
		int nx2;
		int ny2;
		SearchIterator = 0;
		Simple2DPoint bottomright = this
				.getBottomMostRight(NearestCollision.collidingObject);
		
		if (SearchIterator > maxIterations) {
			path2.pathFindingBroke = true;
			Log.info("bottom right path broke");
		}

		nx2 = bottomright.x + collisionmargin;
		ny2 = bottomright.y + collisionmargin;

		path2.add(new MovementWaypoint(nx2, ny2,
				MovementType.AbsoluteLineTo));
		
	//	path2 = this.findPathBetweenAndAddToExistingPath(nx2, ny2, null, path2);
		
	}

	private void addWaypointAtTopRight(SpiffyCollision NearestCollision,
			MovementPath path1,boolean addSubWaypoints) {
		int nx;
		int ny;
		SearchIterator = 0;
		ArrayList<Simple2DPoint> topright = this
				.getTopMostRight(NearestCollision.collidingObject,null);
		
		
		if (SearchIterator > maxIterations) {
			path1.pathFindingBroke = true;
			Log.info("top right path broke");

		}
		

if (addSubWaypoints){
			
			for (Simple2DPoint currentCorner : topright) {
				nx = currentCorner.x - collisionmargin;
				ny = currentCorner.y - collisionmargin;
				path1.add(new MovementWaypoint(nx, ny, MovementType.AbsoluteLineTo));
			}
			
}else {
		nx = topright.get(topright.size()-1).x + collisionmargin;
		ny = topright.get(topright.size()-1).y - collisionmargin;

		

	//	path1 = this.findPathBetweenAndAddToExistingPath(nx, ny, null, path1);
		
		path1.add(new MovementWaypoint(nx, ny, MovementType.AbsoluteLineTo));
		
}
	}

	private void addWaypointAtBottomLeft(SpiffyCollision NearestCollision,
			MovementPath path2,boolean addSubWaypoints) {
		int nx2;
		int ny2;
		SearchIterator = 0;
		Simple2DPoint bottomleft = this
				.getBottomMostLeft(NearestCollision.collidingObject);
		
		if (SearchIterator > maxIterations) {
			path2.pathFindingBroke = true;
			Log.info("bottom left path broke");
		}
		
if (addSubWaypoints){
			
			//for (SimplePoint currentCorner : bottomleft) {
		//	
		//	}
			
}else {
		
		nx2 = bottomleft.x - collisionmargin;
		ny2 = bottomleft.y + collisionmargin;

	//	path2 = this.findPathBetweenAndAddToExistingPath(nx2, ny2, null, path2);
		
		path2.add(new MovementWaypoint(nx2, ny2,
				MovementType.AbsoluteLineTo));
		
}
	}

	private void addWaypointAtTopLeft(SpiffyCollision NearestCollision,
			MovementPath path1,boolean addSubWaypoints) {
		int nx;
		int ny;
		SearchIterator = 0;
		// top left
		ArrayList<Simple2DPoint> topleft = this
				.getTopMostLeftRoute(NearestCollision.collidingObject,null);
		
		if (SearchIterator > maxIterations) {
			path1.pathFindingBroke = true;
			Log.info("top left path broke");
		}
		
		if (addSubWaypoints){
			
			for (Simple2DPoint currentCorner : topleft) {
				
				nx = currentCorner.x - collisionmargin;
				ny = currentCorner.y - collisionmargin;
				path1.add(new MovementWaypoint(nx, ny, MovementType.AbsoluteLineTo));
				
			}
			
			
		} else {
		
		nx = topleft.get(topleft.size()-1).x - collisionmargin;
		ny = topleft.get(topleft.size()-1).y - collisionmargin;
		path1.add(new MovementWaypoint(nx, ny, MovementType.AbsoluteLineTo));
		
		}
		//path1 = this.findPathBetweenAndAddToExistingPath(nx, ny, null, path1);
		
		
	}

	public MovementPath refinePath(MovementPath p) {

		int nodestart = 0;

		Log.info("refining...");

		// try to remove 2nd node

		while (nodestart < (p.size() - 2)) {
			// start is first node
			int sx = p.get(nodestart + 0).pos.x;
			int sy = p.get(nodestart + 0).pos.y;
			// end is third
			int ex = p.get(nodestart + 2).pos.x;
			int ey = p.get(nodestart + 2).pos.y;
			// test
			SpiffyCollision c = findFirstCollision(sx, sy, ex, ey, null);
			if (c == null) {
				p.remove(nodestart + 1);

			} else {
				nodestart = nodestart + 1;
			}
		}

	//	svgSmoothPathUpdate(p.getAsSVGPath());

		return p;
	}

	/** returns the top left most point in a set of boxs overlapping **/
	public ArrayList<Simple2DPoint> getTopMostRight(SimpleCollisionBox starthere, ArrayList<Simple2DPoint>  pathsofar) {
		
		//pathsofar
				if (pathsofar==null){			
					pathsofar = new ArrayList<Simple2DPoint>();			
				}
				
		// highlight test box
		if (starthere.visualHelper!=null){
		starthere.visualHelper.getElement().getStyle()
				.setBackgroundColor("#0F0");
		}
		// test if the top of this box overlaps with anything.
		// (this could be cached in future)
		Log.info("getting top right point");
		SearchIterator++;
		// SpiffyCollision testTopLineOfSquare = this.findFirstCollision(
		// starthere.getBottomRightX(), starthere.getTopLeftY(),
		// starthere.getTopLeftX(), starthere.getTopLeftY(), starthere);
		// test top right
		SimpleCollisionBox testtr = this.isHorizontalLineColliding(starthere.getTopLeftX(),
				starthere.getBottomRightX(), starthere.getTopLeftY());

		int targetX = starthere.getBottomRightX();
		int targetY = starthere.getTopLeftY();

		// test for OB
		if (testForOutOfBounds(targetX, targetY)) {
			SearchIterator = maxIterations + 1;
		}

		if (testtr == null || SearchIterator > maxIterations) {

			pathsofar.add(new Simple2DPoint(starthere.getBottomRightX(),
					starthere.getTopLeftY()));
			
			return pathsofar;

		} else {

			// unhighlight test box
			starthere.visualHelper.getElement().getStyle()
					.setBackgroundColor("#F00");
			// we start again using the new square
			pathsofar.add(new Simple2DPoint(starthere.getBottomRightX(),
					starthere.getTopLeftY()));			
			
			return getTopMostRight(testtr,pathsofar);

			// return new
			// SimplePoint(testTopLineOfSquare.collidingObject.getBottomRightX(),
			// testTopLineOfSquare.collidingObject.getTopLeftY());
		}

	}

	/** returns the top left most point and the path to get there in a set of boxs overlapping **/
	
	public ArrayList<Simple2DPoint> getTopMostLeftRoute(SimpleCollisionBox starthere, ArrayList<Simple2DPoint> pathsofar) {
		// highlight test box
		
		//pathsofar
		if (pathsofar==null){			
			pathsofar = new ArrayList<Simple2DPoint>();			
		}
		
		
		if (starthere.visualHelper!=null){
		starthere.visualHelper.getElement().getStyle()
				.setBackgroundColor("#0F0");
		}
		// test if the top of this box overlaps with anything.
		// (this could be cached in future)
		Log.info("getting top left point");
		SearchIterator++;
		// SpiffyCollision testTopLineOfSquare = this
		// .findFirstCollision(starthere.getTopLeftX(),
		// starthere.getTopLeftY(), starthere.getBottomRightX(),
		// starthere.getTopLeftY(), starthere);

		// test top left
		int targetX = starthere.getTopLeftX();
		int targetY = starthere.getTopLeftY();

		// test for OB
		if (testForOutOfBounds(targetX, targetY)) {
			SearchIterator = maxIterations + 1;
		}

		SimpleCollisionBox testtl = this.isHorizontalLineColliding(
				starthere.getTopLeftX(),starthere.getBottomRightX(), starthere.getTopLeftY());

		if (testtl == null || SearchIterator > maxIterations) {
			
			pathsofar.add(new Simple2DPoint(starthere.getTopLeftX(),
					starthere.getTopLeftY()));
			
			return pathsofar;

		} else {

			// we start again using the new square

			// unhighlight test box
			starthere.visualHelper.getElement().getStyle()
					.setBackgroundColor("#F00");
			
			pathsofar.add(new Simple2DPoint(starthere.getTopLeftX(),
					starthere.getTopLeftY()));
			

			return getTopMostLeftRoute(testtl,pathsofar);

			// return new
			// SimplePoint(testTopLineOfSquare.collidingObject.getTopLeftX(),
			// testTopLineOfSquare.collidingObject.getTopLeftY());

		}

	}

	/** returns the bottom left most point in a set of boxs overlapping **/
	public Simple2DPoint getBottomMostLeft(SimpleCollisionBox starthere) {

		// highlight test box
		if (starthere.visualHelper!=null){
		starthere.visualHelper.getElement().getStyle()
				.setBackgroundColor("#0F0");
		}
		// test if the top of this box overlaps with anything.
		// (this could be cached in future)
		Log.info("getting bottom left point");
		SearchIterator++;
		// SpiffyCollision testBottomLineOfSquare = this.findFirstCollision(
		// starthere.getTopLeftX(), starthere.getBottomRightY(),
		// starthere.getBottomRightX(), starthere.getBottomRightY(),
		// starthere);

		// get bottom left
		int targetX = starthere.getTopLeftX();
		int targetY = starthere.getBottomRightY();

		// test for OB
		if (testForOutOfBounds(targetX, targetY)) {
			SearchIterator = maxIterations + 1;
		}

		SimpleCollisionBox testbl = this.isHorizontalLineColliding(
				starthere.getTopLeftX(),starthere.getBottomRightX(), starthere.getBottomRightY());

		if (testbl == null || SearchIterator > maxIterations) {

			return new Simple2DPoint(starthere.getTopLeftX(),
					starthere.getBottomRightY());

		} else {

			// unhighlight test box
			starthere.visualHelper.getElement().getStyle()
					.setBackgroundColor("#F00");
			// we start again using the new square

			return getBottomMostLeft(testbl);
			// return new
			// SimplePoint(testBottomLineOfSquare.collidingObject.getTopLeftX(),
			// testBottomLineOfSquare.collidingObject.getBottomRightY());

		}

	}

	/** returns the bottom left most point in a set of boxs overlapping **/
	public Simple2DPoint getBottomMostRight(SimpleCollisionBox starthere) {

		// test if the top of this box overlaps with anything.
		// (this could be cached in future)
		Log.info("getting bottom right point");
		SearchIterator++;
		if (starthere.visualHelper!=null){
		// highlight test box
		starthere.visualHelper.getElement().getStyle()
				.setBackgroundColor("#0F0");
		}
		int targetX = starthere.getBottomRightX();
		int targetY = starthere.getBottomRightY();

		// test for OB
		if (testForOutOfBounds(targetX, targetY)) {
			SearchIterator = maxIterations + 1;
		}

		// test bottom right
		SimpleCollisionBox testbr = this.isHorizontalLineColliding(
				starthere.getTopLeftX(),starthere.getBottomRightX(), starthere.getBottomRightY());

		if (testbr == null || SearchIterator > maxIterations) {

			return new Simple2DPoint(starthere.getBottomRightX(),
					starthere.getBottomRightY());

		} else {

			// we start again using the new square

			// unhighlight test box
			starthere.visualHelper.getElement().getStyle()
					.setBackgroundColor("#F00");

			return getBottomMostRight(testbr);
			// return new
			// SimplePoint(testBottomLineOfSquare.collidingObject.getBottomRightX(),
			// testBottomLineOfSquare.collidingObject.getBottomRightY());

		}

	}

	private boolean testForOutOfBounds(int targetX, int targetY) {
		if (targetX < 0) {
			return true;
		}
		if (targetY < 0) {
			return true;
		}
		if (targetX > sourcescene.getOffsetWidth()) {
			return true;
		}
		if (targetY > sourcescene.getOffsetHeight()) {
			return true;
		}

		Log.info("___________________________________________________________________out of bounds!");

		return false;
	}

	public SpiffyCollision findFirstCollision(int x1, int y1, int x2, int y2,
			SimpleCollisionBox ignorethis) {
		// get list of all collision boxs, both the scenes and all the objects
		// This can probably be done much better by not making a new object
		// here, or at least caching if we do
		OldCollisionMap combined = new OldCollisionMap();
		combined.addAll(scenesOwnMap);
		Iterator<SceneObject> soit = sourcescene.getScenesData().scenesOriginalObjects
				.iterator();
		while (soit.hasNext()) {
			SceneObject so = (SceneObject) soit.next();
			if (so.cmap != null) {
			//	combined.addAll(so.cmap);
			}
		}

		if (ignorethis != null) {
			Log.info("ignoring a box");

			boolean state = combined.remove(ignorethis);
			if (state) {
				Log.info("ignoring a box success:");
			}
		}

		Iterator<SimpleCollisionBox> bit = combined.iterator();

		SpiffyCollision NearestCollision = null;

		// get all collisions and find closest
		while (bit.hasNext()) {

			SimpleCollisionBox box = (SimpleCollisionBox) bit.next();

			// / Log.info("testing box at: " + box.getTopLeftX() + ","
			// + box.getTopLeftY());
			// Log.info("against line: " + x1 + "," + y1 + "->" + x2 + "," +
			// y2);

			SpiffyCollision test = box.TestForCollisionWithLine(x1, y1, x2, y2);

			if (test != null) {

				// is it nearer then last one

				if ((NearestCollision == null)) {

					GreyLog.info("new nearest");
					NearestCollision = test;
				} else if (test.distance < NearestCollision.distance) {

					GreyLog.info("new nearest");
					NearestCollision = test;

				}
			}

		}

		return NearestCollision;
	}

	public boolean isBoxColliding(int tlx, int tly, int brx, int bry) {

		GreyLog.info("testing box collision:" + tlx + "," + tly + "-" + brx
				+ "," + bry);

		OldCollisionMap combined = new OldCollisionMap();
		combined.addAll(scenesOwnMap);
		Iterator<SceneObject> soit = sourcescene.getScenesData().scenesOriginalObjects
				.iterator();
		while (soit.hasNext()) {
			SceneObject so = (SceneObject) soit.next();
			if (so.cmap != null) {
			//	combined.addAll(so.cmap);
			}
		}

		boolean xoverlap = false, yoverlap = false;
		Iterator<SimpleCollisionBox> bit = combined.iterator();
		while (bit.hasNext()) {
			xoverlap = false;
			yoverlap = false;
			SimpleCollisionBox box = (SimpleCollisionBox) bit.next();

			if (box.Xoverlap(brx, tlx)) {

				GreyLog.info("overlap in x");
				xoverlap = true;
			}
			if (box.Yoverlap(tly, bry)) {

				GreyLog.info("overlap in y");
				yoverlap = true;

			}
			if ((xoverlap) && (yoverlap)) {
				return true;
			}

		}

		return false;

	}
	
	
	
	public SimpleCollisionBox isHorizontalLineColliding(int sx,int ex, int y) {
		OldCollisionMap combined = new OldCollisionMap();
		combined.addAll(scenesOwnMap);

		Iterator<SceneObject> soit = sourcescene.getScenesData().scenesOriginalObjects
				.iterator();
		while (soit.hasNext()) {
			SceneObject so = (SceneObject) soit.next();
			if (so.cmap != null) {
				//combined.addAll(so.cmap);
			}
		}
		
		

		Iterator<SimpleCollisionBox> bit = combined.iterator();
		while (bit.hasNext()) {

			SimpleCollisionBox box = (SimpleCollisionBox) bit.next();
			
			int minY= box.getTopLeftY();
			int maxY= box.getBottomRightY();
			int minX = box.getTopLeftX();
			int maxX = box.getBottomRightX();
			
			if ((y>minY)&&(y<maxY)){
				
				if (box.Xoverlap(sx, ex)){
					
					return box;
					
				}
				
				
			}
			
			
			
			
			
		}
		
		return null;
		
		
	}

	
	public SimpleCollisionBox isPointColliding(int x, int y) {

		Log.info("testing collision at" + x + "," + y);

		OldCollisionMap combined = new OldCollisionMap();
		combined.addAll(scenesOwnMap);

		Iterator<SceneObject> soit = sourcescene.getScenesData().scenesOriginalObjects
				.iterator();
		while (soit.hasNext()) {
			SceneObject so = (SceneObject) soit.next();
			if (so.cmap != null) {
			//	combined.addAll(so.cmap);
			}
		}

		Iterator<SimpleCollisionBox> bit = combined.iterator();
		while (bit.hasNext()) {

			SimpleCollisionBox box = (SimpleCollisionBox) bit.next();

			if (box.isInside(x, y)) {

				Log.info("found collision");
				return box;
			}

		}

		return null;

	}

}
