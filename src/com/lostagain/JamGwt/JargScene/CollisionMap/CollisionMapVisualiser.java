package com.lostagain.JamGwt.JargScene.CollisionMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.lostagain.Jam.CollisionMap.IsCollisionMapVisualiser;
import com.lostagain.Jam.CollisionMap.PolygonCollisionMap;
import com.lostagain.Jam.Movements.MovementPath;
import com.lostagain.Jam.Scene.SceneWidget;
import com.lostagain.Jam.SceneObjects.SceneObject;

public class CollisionMapVisualiser implements IsCollisionMapVisualiser {

	public static Logger Log = Logger.getLogger("JAM.CollisionMapVisualiser");
	
	/** the calculated path after pathfinding **/
	public HTML svgCalculatedPath = new HTML();
	
	/** used to display a svg of the refined path to help debug it **/
	public HTML svgRefinedPath = new HTML();

	/** preview widget - helps with debugging by visualizing the map**/
	public AbsolutePanel collisionMapPreviewWidget = new AbsolutePanel();

	/** svg preview layer, which display the scenes collision map **/
	public HTML svgPreview = new HTML();
	
	/** svg sketch layer, all the current paths are displayed on this  **/
	public HTML svgSketchPad = new HTML("");
	
	/** mains all the current path strings **/
	public HashSet<String> PathList = new HashSet<String>();
	
	/** panel we put the svgs on **/
	public AbsolutePanel svgPathVisualisation = new AbsolutePanel();

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#getSvgPathVisualisation()
	 */
	@Override
	public AbsolutePanel getSvgPathVisualisation() {
		return svgPathVisualisation;
	}

	/** pathfinding color **/
	String PathfindingColor = "blue";

	private SceneWidget sourceScene;

	private PolygonCollisionMap scenesOwnMap;
	
	
	
	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#setupVisualiser(com.darkflame.client.JargScene.SceneWidget, com.darkflame.client.JargScene.CollisionMap.PolygonCollisionMap)
	 */
	@Override
	public void setupVisualiser(SceneWidget sourceScene, PolygonCollisionMap scenesOwnMap) {
		
		this.sourceScene  = sourceScene;
		this.scenesOwnMap = scenesOwnMap;
		
		svgSketchPad.getElement().setId("svgSketchpad"); //TODO: just remove this line, as only there to help find the svg in the dom
		
		
		svgPathVisualisation.add(svgSketchPad, 0, 0);
		svgPathVisualisation.add(svgCalculatedPath, 0, 0);

		svgPathVisualisation.setSize("100%", "100%");
		svgPathVisualisation.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		svgPathVisualisation.getElement().getStyle().setZIndex(50005);

		svgPathVisualisation.getElement().getStyle().setProperty("pointerEvents", "none");
		svgPathVisualisation.clear();
	}
	
	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#generatePreviewWidget()
	 */
	@Override
	public void generatePreviewWidget() {
		
		
		Set<SceneObject> scenesObjects = sourceScene.getScenesData().allScenesCurrentObjects();// .scenesOriginalObjects;
		
		collisionMapPreviewWidget.setSize("100%", "100%");		
		collisionMapPreviewWidget.getElement().getStyle().setProperty("pointerEvents", "none");		
		collisionMapPreviewWidget.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		collisionMapPreviewWidget.getElement().getStyle().setZIndex(50000);	
		collisionMapPreviewWidget.clear();
		
		String AllPaths = scenesOwnMap.getPath(sourceScene.getNormalScaleingForReflections());

		if (scenesObjects.isEmpty()){
			Log.info("no objects yet so no cmap generated");
		}
		Log.info("cmap generated for scene:"+AllPaths);
		
		// loop over all scenes objects adding their SVGs to the panel if they are visible
		//Iterator<SceneObject> sit = sourcescene.getScenesData().scenesOriginalObjects.iterator();		
		Iterator<? extends SceneObject> sit = scenesObjects.iterator();	
		
		while (sit.hasNext()) {
			SceneObject sceneObject = (SceneObject) sit.next();

			if (!sceneObject.isVisible()){
				continue;
			}
			
			AllPaths = AllPaths + sceneObject.getCollisionPath();
			
			/*
			if (sceneObject.cmap.isPresent()) {
				//get each objects path and add it to the overall AllPath
				//which contains both the scenes collision map and all its objects maps
				//giving us all the things that should be avoided
				AllPaths = AllPaths + sceneObject.cmap.get().getPath();
			}*/
			
		}

		//generate the svg string from AllPaths so we can see it all!
		svgPreview.setHTML("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"40000\" width=\"40000\">"
						+ AllPaths + "</svg>");

		collisionMapPreviewWidget.add(svgPreview);

	}

	
	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#getCollisionMapPreviewWidget()
	 */
	@Override
	public AbsolutePanel getCollisionMapPreviewWidget() {
		return collisionMapPreviewWidget;
	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#clearSketch()
	 */
	@Override
	public void clearSketch() {
		PathList.clear();
		svgSketchPad.setHTML("");
	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#clearCalculatedPath()
	 */
	@Override
	public void clearCalculatedPath() {
		svgCalculatedPath.setHTML("");
	}
	

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#addToSketch(java.lang.String, java.lang.String)
	 */
	@Override
	public void addToSketch(String sketchSVGPath, String color,boolean rawPath) {
		String newPath ="";

		if (rawPath){
			 newPath = "<path id=\"lineBC\" d=\"" + sketchSVGPath
				+ "\" stroke=\"" + "" + color + ""
				+ "\" stroke-width=\"3\" fill=\"none\" />";
		} else {
			 newPath =  sketchSVGPath;
			
		}
		//add to path list	
		PathList.add(newPath);

		Log.info("Sketchpad added:" + newPath);
		//tell the sketch to redraw
	    redrawSketch();

	}
	
	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#removeFromSketch(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeFromSketch(String sketchSVGPath, String color) {

		//ensure this matches the "addToSketch" formula else it wont be able tp
		//remove what you asked for correctly
		String newPath = "<path id=\"lineBC\" d=\"" + sketchSVGPath
				+ "\" stroke=\"" + "" + color + ""
				+ "\" stroke-width=\"3\" fill=\"none\" />";
		
		
		Boolean success = PathList.remove(newPath);
		
		if (!success){
			Log.info("no match found to remove");		
		}
		
	    redrawSketch();

	}
	
	/** redraws the sketch layer and everything on it **/
	private void redrawSketch() {
		svgPathVisualisation.remove(svgSketchPad);
				
		
		//create the all path string
		String allPaths="";
		
		for (String iterable_element : PathList) {
			allPaths=allPaths+iterable_element;
		}
		
		//use the all path string in a svg object
		svgSketchPad.setHTML("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"40000\" width=\"40000\">"
						+ allPaths + "</svg>");

		svgSketchPad.setSize("100%", "100%");

		//add this html div it to the layer displaying it.
		svgPathVisualisation.add(svgSketchPad,0,0);
	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#isCmapVisible()
	 */
	@Override
	public boolean isCmapVisible() {
		return collisionMapPreviewWidget.isVisible();
	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#isPathVisible()
	 */
	@Override
	public boolean isPathVisible() {
		return svgPathVisualisation.isVisible();
	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#showCmap(boolean)
	 */
	@Override
	public void showCmap(boolean show) {

		generatePreviewWidget(); // update it
		collisionMapPreviewWidget.setVisible(show);

	}

	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#showPath(boolean)
	 */
	@Override
	public void showPath(boolean show) {
		
		svgPathVisualisation.setVisible(show);
	}

	
	
	/** draws a green line to represent the refined path **/
	public void svgSmoothPathUpdate(String map) {

		Log.info("updating svg smooth path");
		
		svgPathVisualisation.setSize("100%", "100%");
		svgPathVisualisation.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		svgPathVisualisation.getElement().getStyle().setZIndex(50005); //ensure above cmaps

		//remove the existing refined path displayed
		svgPathVisualisation.remove(svgRefinedPath);

		//create a new one from the supplied svg data 
		svgRefinedPath = new HTML(
				"<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"40000\" width=\"40000\"><path id=\"lineBC\" d=\""
						+ map
						+ "\" stroke=\""
						+ "green"
						+ "\" stroke-width=\"3\" fill=\"none\" /></svg>");

		svgPathVisualisation.add(svgRefinedPath, 0, 0);

	}
	
	/* (non-Javadoc)
	 * @see com.darkflame.client.JargScene.CollisionMap.isCollisionMapVisualiser#updatePath(com.darkflame.Jam.MovementPath, com.darkflame.client.JargScene.CollisionMap.PolygonCollisionMap)
	 */
	@Override
	public  void updatePath(MovementPath np,PolygonCollisionMap lastCollision) {

		String Path = "<path id=\"lineBC\" d=\"" + np.getAsSVGPath()
				+ "\" stroke=\"" + PathfindingColor
				+ "\" stroke-width=\"3\" fill=\"none\" />";

		svgPathVisualisation.clear();

		// add a edge we hit last
		if (lastCollision != null) {
			// Log.info("_______________________lastCollision = "+lastCollision.associatedObject.objectsCurrentState.ObjectsName);
			Path = Path + lastCollision.getCurrentHighlightedSide();
		} else {
			Log.info("_______________________no last collision set");
		}

		svgCalculatedPath
				.setHTML("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"40000\" width=\"40000\">"
						+ Path + "</svg>");

		
		svgPathVisualisation.add(svgSketchPad, 0, 0);
		svgPathVisualisation.add(svgCalculatedPath, 0, 0);
	}

	
}
