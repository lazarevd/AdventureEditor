package ru.laz.gameeditor;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.world.World;

public class AEditor extends ApplicationAdapter {


	
	Table tab;

	World world;
	Stage stage;
	UI ui;
	
	@Override
	public void create () {


		
		
		world = World.getWorld();
		world.getGraph().refreshNeighbours();
		stage = world.getStage();
		Gdx.input.setInputProcessor(stage);
		ui = UI.createUI(stage);
	}

	
	@Override
	public void render () {

		
		ui.act();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}
	
	

	
	
	
	
	

	
	
	
}
