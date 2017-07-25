package ru.laz.gameeditor.ui.tools;

import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import ru.laz.gameeditor.graph.Edge;
import ru.laz.gameeditor.graph.Graph;
import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.ui.UI.ToolDisplayStatus;
import ru.laz.gameeditor.ui.tools.Tool.ToolStatus;
import ru.laz.gameeditor.world.World;

public class ConnectNodes implements Tool {

	private ToolStatus toolStat;
	private Array<Node> nodes;
	
	@Override
	public ToolStatus getStatus() {
		return toolStat;
	}

	@Override
	public void setStatus(ToolStatus toolStatus) {
		this.toolStat = toolStatus;
		
	}

	@Override
	public void prepare() {
		toolStat = ToolStatus.SELECTING;
		
		if(nodes != null && nodes.size == 2) {//To add nodes continuously
		nodes.removeIndex(0);
		} else {
		nodes = new Array<Node>();
		}
		UI.getUI().NODE = true;	
		UI.getUI().setToolDisplayStatus(ToolDisplayStatus.HOVERNODE);
		
	}

	@Override
	public void select() {

		Vector2 inputXY = new Vector2(UI.getCursor().x,UI.getCursor().y);
		
		if(setSelectedNode(inputXY, 2)) {
			this.toolStat = ToolStatus.PROCESSING;
		}
	//Gdx.app.log("selected", "ok");
		
	}

	@Override
	public void process() {
		if(this.toolStat != ToolStatus.FINISHED) {
		Gdx.app.log("Tool Conn Node", "Process");
		Edge edge = new Edge(nodes.get(0).getThisId(), nodes.get(1).getThisId());
		World.getWorld().getGraph().addEdge(Graph.getNewEdgeName(), edge);
		Gdx.app.log("Added edge", edge.getThisId());
		World.getWorld().getGraph().refreshNeighbours();
		//this.toolStat = ToolStatus.FINISHED;
		this.prepare();
		}
	}

	@Override
	public void finish() {
		UI.getUI().setToolDisplayStatus(ToolDisplayStatus.NORMAL);	
		Gdx.app.log("Tool Conn Node", "Fin");
	}
	
	
	private boolean setSelectedNode(Vector2 inputXY, int count)  {
		boolean added = false;
		
		Node retNode;

		if (Gdx.input.justTouched()) {

			retNode = Node.getNearestNode(inputXY);
			
			if(nodes.size < count && retNode.getDistance(inputXY.x, inputXY.y) < 10 && !nodes.contains(retNode, true)) {
			nodes.add(retNode);
			} else if (retNode.getDistance(inputXY.x, inputXY.y) > 10) {
				this.toolStat = ToolStatus.FINISHED;
			}
			
			if (nodes.size >= count) {
				added = true;	
			}
			

		}
	
	//Gdx.app.log("selector", added + "");	
	return added;
	
	}
	
	


}
