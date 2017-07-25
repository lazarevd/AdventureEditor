package ru.laz.gameeditor.world;


import com.badlogic.gdx.scenes.scene2d.Stage;

import ru.laz.gameeditor.graph.Graph;

public class World {
	
	private static World instant; //Singletone pattern
	
	private Graph graph;
	private Stage stage;

	
	private World() {
		
	graph = new Graph();
	stage = new Stage();
	}
	
	public static World getWorld() {
		if (instant == null) {
			instant = new World();
		}
		return instant;
	}
	
	
	public Graph getGraph() {
		return graph;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	
}
