package ru.laz.gameeditor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;

import java.util.Map.Entry;

import ru.laz.gameeditor.dataexchange.GraphSource;
import ru.laz.gameeditor.graph.Edge;
import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.graph.PointOnEdge;
import ru.laz.gameeditor.graph.Polygon4;
import ru.laz.gameeditor.render.RenderShapes;
import ru.laz.gameeditor.render.RenderShapes.Colour;
import ru.laz.gameeditor.ui.tools.AddNode;
import ru.laz.gameeditor.ui.tools.AddPoly;
import ru.laz.gameeditor.ui.tools.ConnectNodes;
import ru.laz.gameeditor.ui.tools.ConnectPolygons;
import ru.laz.gameeditor.ui.tools.ConnectPolygons2;
import ru.laz.gameeditor.ui.tools.DeleteEdge;
import ru.laz.gameeditor.ui.tools.DeleteNode;
import ru.laz.gameeditor.ui.tools.MoveNodePoly;
import ru.laz.gameeditor.ui.tools.SetDistance;
import ru.laz.gameeditor.ui.tools.Tool;
import ru.laz.gameeditor.ui.tools.ToolBox;
import ru.laz.gameeditor.world.World;

public class UI {

	public final static int SCREENW = 1260;
	public final static int SCREENH = 540;
	
	
	private TextButtonStyle textButtonStyle;
	private BitmapFont font;
	private Skin skin;
	private TextureAtlas buttonAtlas;
	private Stage stage;
	private Table mainTable;
	private World world;
	RenderShapes renderShapes;
	private Tool curTool;
	private static UI ui; //singletone
	
	public boolean NODE, POLY = true;
	public boolean MOVEPOLY = false; //Режимы в которых можно двигать ноды, в некоторых инструментах полезны, в некоторых нет
	public boolean MOVENODE = false;

	public static boolean drawBack = true;
	
	private ToolDisplayStatus toolDisplayStatus = ToolDisplayStatus.NORMAL;
	
	public enum ToolDisplayStatus {HOVERNODEPOLYGON, HOVERPOLYGON, HOVERPOE, HOVERNODE, HOVEREDGE, NORMAL};

	TextField textDialogField = null;
	
	private UI(Stage stg) {
		world = World.getWorld();
		renderShapes = new RenderShapes();
        stage = stg;
        renderShapes.setBackgroundSprite(new Texture(Gdx.files.internal("back.png")));
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.left().top();
        stage.addActor(mainTable);
        NODE = true;
        fillUI();
	}
	
	
	private void fillUI() {
		mainTable.setDebug(true);
		
		Button node_mode = createButton("Node");
		node_mode.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		        ToolBox.stopTool(curTool);
		    	if (NODE) {
		        	NODE = false;
		        } else {
		        	NODE = true;
		        }

		        toolDisplayStatus = ToolDisplayStatus.NORMAL;
		    }
		});
		
		Button poly_mode = createButton("Poly");
		poly_mode.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	ToolBox.stopTool(curTool);
		        if (POLY) {
		        	POLY = false;
		        } else {
		        	POLY = true;
		        }
		        toolDisplayStatus = ToolDisplayStatus.NORMAL;
		    }
		});
		
		Button move_button = createButton("Move");
		move_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	ToolBox.stopTool(curTool);
			        toolDisplayStatus = ToolDisplayStatus.HOVERNODEPOLYGON;
				if(curTool == null) {
					curTool = new MoveNodePoly();
					curTool.prepare();
				}


		    }
		});
		
		Button conn_poly_button = createButton("ConnPl1");
		conn_poly_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	POLY = true;
		    	ToolBox.stopTool(curTool);
		        curTool = new ConnectPolygons();
		        curTool.prepare();

		
		     
		    }
		});
		
		Button conn_poly2_button = createButton("ConnPl2");
		conn_poly2_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	POLY = true;
		    	ToolBox.stopTool(curTool);
		        curTool = new ConnectPolygons2();
		        curTool.prepare();
		
		     
		    }
		});
		
		
		Button del_node_button = createButton("DelNode");
		del_node_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
		        curTool = new DeleteNode();
		        curTool.prepare();
		    }
		});
		
		
		Button del_edge_button = createButton("DelEdge");
		del_edge_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
		        curTool = new DeleteEdge();
		        curTool.prepare();
		    }
		});
		
		
		Button con_node_button = createButton("ConnNod");
		con_node_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
			        curTool = new ConnectNodes();
			        curTool.prepare();
		
		    }
		});
		
		
		Button draw_back_button = createButton("DrawBack");
		draw_back_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	if (drawBack) {
		    		drawBack = false;
		        } else {
		        	drawBack = true;
		        }
		
		    }
		});
		

		Button add_node_button = createButton("AddNode");
		add_node_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
		        curTool = new AddNode();
		        curTool.prepare();
		    }
		});
		
		
		Button add_poly_button = createButton("AddPoly");
		add_poly_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
		        if(curTool == null) {
		        curTool = new AddPoly();
		        curTool.prepare();
		        } else {
		        	curTool = null;
		        }
		
	
		    }
		});
		
		Button save_graph_button = createButton("Save gr");
		save_graph_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	
		    Json json = new Json();
		    World.getWorld().getGraph().saveGraph();
		    String saveGraph = json.prettyPrint(World.getWorld().getGraph().getGraphSource());		    
		    FileHandle file = Gdx.files.local("source.graph");
		    file.writeString(saveGraph, false); 
		    Gdx.app.log("Graph written",saveGraph);
		    
		    }
		});
		
		Button load_graph_button = createButton("Load gr");
		load_graph_button.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	
		    	FileHandle file = Gdx.files.internal("source.graph");
		    	String loadGraph = file.readString();
		    	Gdx.app.log("Graph read",loadGraph);
		    	Json json = new Json();
		    	GraphSource graphSource = json.fromJson(GraphSource.class, loadGraph);
		    	World.getWorld().getGraph().setGraphSource(graphSource);
		    	

		    	World.getWorld().getGraph().loadGraph();
		    	
		    	for(Polygon4 pol : World.getWorld().getGraph().getPolygons().values()) {
		    	Gdx.app.log("Polys", pol.getThisId());	
		    	}
		    
		    }
		});


		Button set_far_button = createButton("Set far");
		set_far_button.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				NODE = true;
				ToolBox.stopTool(curTool);
				if(curTool == null) {
					curTool = new SetDistance();
					curTool.prepare();
				} else {
					curTool = null;
				}
				//TODO
			}
		});
		
	
		
		
        mainTable.add(node_mode).maxWidth(60);
        mainTable.row();
        mainTable.add(poly_mode).maxWidth(60);
        mainTable.row();
        mainTable.add(move_button).maxWidth(60);
        mainTable.row();
        mainTable.add(add_node_button).maxWidth(60);
        mainTable.row();
        mainTable.add(add_poly_button).maxWidth(60);
        mainTable.row();
        mainTable.add(conn_poly_button).maxWidth(60);
        mainTable.row();
        mainTable.add(conn_poly2_button).maxWidth(60);
        mainTable.row();
        mainTable.add(del_node_button).maxWidth(60);
        mainTable.row();
        mainTable.add(del_edge_button).maxWidth(60);
        mainTable.row();
        mainTable.add(con_node_button).maxWidth(60);
        mainTable.row();
        mainTable.add(draw_back_button).maxWidth(60);
		mainTable.row();
		mainTable.add(set_far_button).maxWidth(60);
        mainTable.row();
        mainTable.add(save_graph_button).maxWidth(60);
        mainTable.row();
        mainTable.add(load_graph_button).maxWidth(60);

        
	}
	
	public ToolDisplayStatus getToolDisplayStatus() {
		return this.toolDisplayStatus;
	}
	
	public void setToolDisplayStatus(ToolDisplayStatus displayMode) {
		this.toolDisplayStatus = displayMode;
	}	
	
	
	
	public Button createButton(String label) {
        font = new BitmapFont();
        skin = new Skin();
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        buttonAtlas = new TextureAtlas(Gdx.files.internal("ui/astar.pack"));
        skin.addRegions(buttonAtlas);
		textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("idle");
        textButtonStyle.down = skin.getDrawable("push");
		Button button = new TextButton(label, textButtonStyle);
		
		return button;
	}
	
	
	
	public static Vector2 getCursor() {
		return new Vector2(Gdx.input.getX(), SCREENH - Gdx.input.getY());
	}
	

	
	public void act() {
		if (ToolBox.processTool(curTool)) {//Если ToolBox вернул true, значит инструмент завершил работу и его можно удалить
			curTool = null;
			toolDisplayStatus = ToolDisplayStatus.NORMAL;
		}
		drawObjects();
		RenderShapes.drawPoint(UI.getCursor(), 3, Colour.WHITE);
	}
	
	



	public boolean showNodeDistanceDialog(Node node, final SetDistance setDistTool) {
		Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		textDialogField = new TextField("",skin);
		stage.addActor(textDialogField);
		textDialogField.setText(node.getCamDistance()+"");
		textDialogField.setTextFieldListener(new TextField.TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char ch) {
				setDistTool.setDialogBuffer(textField.getText());
				System.out.println("typed " + ch);
				if(ch == '\r') {setDistTool.setFinishEnter(true);}
			}
		});
	return true;
	}

	public void hideNodeDistanceDialog() {
		if(textDialogField != null) {
			stage.getActors().removeValue(textDialogField,true);
			textDialogField = null;
		}
	}
	
	
	private void drawObjects() {
		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderShapes.drawBackground();
		
		for (Entry<String, Edge> entry : world.getGraph().getEdges().entrySet()) {
			if (NODE == true){
			renderShapes.drawEdge(entry.getValue(), Colour.WHITE);
			}
		}

		for (Entry<String, Node> entry : world.getGraph().getNodes().entrySet()) {
			if (NODE == true){
			renderShapes.drawNode(entry.getValue(), Colour.YELLOW);
			}
		}
		
		
		for (Entry<String, PointOnEdge> entry : world.getGraph().getPOEs().entrySet()) {
			if (NODE == true || POLY == true) {
			renderShapes.drawPOE(entry.getValue());
			}
		}
		
		if (NODE == true){
		renderShapes.drawNode(world.getGraph().getStart(), Colour.RED);
		renderShapes.drawNode(world.getGraph().getFinish(), Colour.BLUE);
		}

		
		for (Entry<String, Polygon4> entry : world.getGraph().getPolygons().entrySet()) {
			if (POLY == true){
			renderShapes.drawPolygon(entry.getValue(), Colour.BLUE);
			}
		}
		
		
	}

	public static UI createUI(Stage stg) {
		if (ui ==null) {
			ui = new UI(stg);
		}
		return ui;
	}
	
	public static UI getUI() {
		return ui;
	}

}
