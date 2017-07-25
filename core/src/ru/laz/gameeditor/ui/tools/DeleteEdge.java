package ru.laz.gameeditor.ui.tools;

import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import ru.laz.gameeditor.graph.Edge;
import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.ui.UI.ToolDisplayStatus;
import ru.laz.gameeditor.ui.tools.Tool.ToolStatus;
import ru.laz.gameeditor.world.World;

public class DeleteEdge implements Tool {

	
	private ToolStatus toolStat;
	private String edge;
	
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
		UI.getUI().setToolDisplayStatus(ToolDisplayStatus.HOVEREDGE);
	}

	@Override
	public void select() {

		Vector2 inputXY = new Vector2(UI.getCursor().x,UI.getCursor().y);	
		if(setSelectedEdge(inputXY)) {
			this.toolStat = ToolStatus.PROCESSING;
		}
		
	
	}

	@Override
	public void process() {
		Gdx.app.log("process del edge", edge);
		World.getWorld().getGraph().deleteEdge(edge);
		this.toolStat = ToolStatus.FINISHED;
	}

	@Override
	public void finish() {
		UI.getUI().setToolDisplayStatus(ToolDisplayStatus.NORMAL);
		
	}

	
	private boolean setSelectedEdge(Vector2 inputXY)  {
		boolean added = false;
	
				
				if (Gdx.input.justTouched()) {
					edge = getHoverEdge(inputXY);
					if (edge != "zz")
						added = true;
			
				}
				return added;
	}
			
			
		
	

	
	private String getHoverEdge(Vector2 inputXY) {
		String curEdge = "zz";
		
		
		for (Entry<String,Edge> entry : World.getWorld().getGraph().getEdges().entrySet()) {
			
			if(entry.getValue().isPointOver(inputXY)) 
			{
			curEdge = entry.getKey();
			Gdx.app.log("hover egde", curEdge);
			}
		

	}
		return curEdge;	
	}
	
}
