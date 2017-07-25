package ru.laz.gameeditor.ui.tools;

import ru.laz.gameeditor.graph.Graph;
import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.graph.PointOnEdge;
import ru.laz.gameeditor.graph.Polygon4;
import ru.laz.gameeditor.render.RenderShapes;
import ru.laz.gameeditor.render.RenderShapes.Colour;
import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.ui.UI.ToolDisplayStatus;
import ru.laz.gameeditor.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ConnectPolygons2 implements Tool {
	
	private Array<Polygon4> selectedPolygons ; //Array of selected items. Using Libgdx array
	private Array<Vector2> selectedCoords ; //Array of selected items coordinates. Using Libgdx array\
	private ToolStatus toolStat;
	
	

	
	
	public ConnectPolygons2() {
		
        selectedPolygons = new Array<Polygon4>();
        selectedPolygons.ordered = true;
        selectedCoords = new Array<Vector2>();
        selectedCoords.ordered = true;
	}
	
	public void prepare() {
		Gdx.app.log("Tool", "Prepare");
		clearSelected();
		toolStat = ToolStatus.SELECTING;
		UI.getUI().setToolDisplayStatus(ToolDisplayStatus.HOVERPOLYGON);

		
	}
	
	@Override
	public void select() {
		
		Vector2 inputXY = new Vector2(UI.getCursor().x,UI.getCursor().y);
		
		for (Polygon4 poly : World.getWorld().getGraph().getPolygons().values()) {
		//Gdx.app.log("input", inputXY.x + ":" + inputXY.y);
		Vector2 pxy = poly.getClosestEdgePoint(inputXY);//!!! Почему-то не рисует 22.01.15
		//Gdx.app.log("pxy", inputXY.x + ":" + inputXY.y);
		RenderShapes.drawPoint(pxy, 10, Colour.BLUE);
		}
		
		//Gdx.app.log("Tool", "Select");
		if (setSelectedPolygons(inputXY, 2)) {
		this.toolStat = ToolStatus.PROCESSING;
		}
		
	}


	@Override
	public void process() {
		Gdx.app.log("Tool", "Process");
		/*
		for (Vector2 xy : selectedCoords) {
			Gdx.app.log("xy", xy.toString());
		}
		for (Polygon4 xy : selectedPolygons) {
			Gdx.app.log("xy", xy.toString());
		}
		*/
		if(this.toolStat != ToolStatus.FINISHED) {
		
			String n1Poe, n2Poe;
			
			n1Poe = selectedPolygons.get(0).addPointOnClosestEdge(selectedCoords.get(0).x, selectedCoords.get(0).y);//На основании ранее отселекченных координат добавляем
			n2Poe = selectedPolygons.get(1).addPointOnClosestEdge(selectedCoords.get(1).x, selectedCoords.get(1).y);//точки на ребра (poe) на отселекченные полигоны. сразу добавлем их в список poe
		
		Array<String> n1Poes = new Array<String>();
		Array<String> n2Poes = new Array<String>();
		
		n1Poes.add(n1Poe); //Make array with 2 same POEs
		n1Poes.add(n1Poe);		
		n2Poes.add(n2Poe);
		n2Poes.add(n2Poe);
		
		World.getWorld().getGraph().addNode(Graph.getNewNodeName(), new Node(n1Poes));
		World.getWorld().getGraph().addNode(Graph.getNewNodeName(), new Node(n2Poes));
		
		this.toolStat = ToolStatus.FINISHED;
		}
	}
	
	
	@Override
	public void finish() {
		Gdx.app.log("Tool Connect poly", "Fin");
		UI.getUI().setToolDisplayStatus(ToolDisplayStatus.NORMAL);
		
	}
	
	
	


	
	
	public void clearSelected() {
		
		selectedPolygons.clear();
		selectedCoords.clear();
		
		
	}
	
	public boolean setSelectedPolygons(Vector2 xy, int count) { //Returns true if count of nodes in select list ok.
		
		boolean added = false;
		Polygon4 curPoly = null;
		
		if (Gdx.input.justTouched()) {
		
        for (Polygon4 poly : World.getWorld().getGraph().getPolygons().values()) {
        

			if (selectedPolygons.size < count && poly.isPointInside(xy) && !selectedPolygons.contains(curPoly, false)) {
			selectedPolygons.add(poly);
			selectedCoords.add(xy);
			}
			 if (selectedPolygons.size >= count) {
				//System.out.println("selectedPolygons.size " + selectedPolygons.size);
				added = true;
			}
		
		}
		
        }
		
		
		if (selectedCoords.size == 2) {
		RenderShapes.drawLine(selectedCoords.get(0), selectedCoords.get(1), Colour.WHITE);
		}
		
		
		return added;
	}


	@Override
	public ToolStatus getStatus() {
		return this.toolStat;
	}


	@Override
	public void setStatus(ToolStatus toolStatus) {
		this.toolStat = toolStatus;
		
	}






}
