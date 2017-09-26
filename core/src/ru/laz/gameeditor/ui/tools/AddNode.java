package ru.laz.gameeditor.ui.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import ru.laz.gameeditor.graph.Graph;
import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.world.World;

public class AddNode implements Tool {
	
	private ToolStatus toolStat;
	private Vector2 xy;

	@Override
	public ToolStatus getStatus() {
		return this.toolStat;
	}

	@Override
	public void setStatus(ToolStatus toolStatus) {
		this.toolStat = toolStatus;
		
	}

	@Override
	public void prepare() {
		xy = null;
		toolStat = ToolStatus.SELECTING;
	}

	@Override
	public void select() {
		
		Vector2 txy = new Vector2(UI.getCursor().x,UI.getCursor().y);
		
		if (Gdx.input.justTouched()) {
			xy = txy;
			
		}
		
		
		if (xy != null) {

			this.toolStat = ToolStatus.PROCESSING;
		}
		
	}

	@Override
	public void process() {
		
		if(this.toolStat != ToolStatus.FINISHED && xy.x > 50) {
		Node node = new Node((int)xy.x, (int)xy.y, 1.0f);
		
		
		World.getWorld().getGraph().addNode(Graph.getNewNodeName(), node);
		
		
			prepare();
		}
		//this.toolStat = ToolStatus.FINISHED;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
