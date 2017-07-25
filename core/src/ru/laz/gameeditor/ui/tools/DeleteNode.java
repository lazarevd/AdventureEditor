package ru.laz.gameeditor.ui.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import ru.laz.gameeditor.graph.Edge;
import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.graph.PointOnEdge;
import ru.laz.gameeditor.graph.Polygon4;
import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.ui.UI.ToolDisplayStatus;
import ru.laz.gameeditor.ui.tools.Tool.ToolStatus;
import ru.laz.gameeditor.world.World;

public class DeleteNode implements Tool {

	
	private ToolStatus toolStat;
	private String node;
	
	
	public DeleteNode() {
		Gdx.app.log("DeleteNode:", "started");
	}
	
	
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
		UI.getUI().NODE = true;	
		UI.getUI().setToolDisplayStatus(ToolDisplayStatus.HOVERNODE);
	}

	@Override
	public void select() {
		Vector2 inputXY = new Vector2(UI.getCursor().x,UI.getCursor().y);
		
		if(setSelectedNode(inputXY)) {
			this.toolStat = ToolStatus.PROCESSING;
		}
		
	}

	@Override
	public void process() {	
		World.getWorld().getGraph().deleteNode(node);
		this.toolStat = ToolStatus.FINISHED;
	}

	@Override
	public void finish() {
		UI.getUI().setToolDisplayStatus(ToolDisplayStatus.NORMAL);
		
	}

	
	private boolean setSelectedNode(Vector2 inputXY)  {
		boolean added = false;
		
		Node retNode;

		if (Gdx.input.justTouched()) {

			retNode = Node.getNearestNode(inputXY);
			
			if(retNode.getDistance(inputXY.x, inputXY.y) < 10) {
			this.node = retNode.getThisId();
			added = true;
			}

		}
	
	//Gdx.app.log("selector", added + "");	
	return added;
	
	}
	

	
	
	
	
}
