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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
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
import ru.laz.gameeditor.ui.tools.Tool;
import ru.laz.gameeditor.ui.tools.ToolBox;
import ru.laz.gameeditor.world.World;

public class UI {
	 
	public final static int SCREENW = 1440;
	public final static int SCREENH = 480;
	
	
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
	public boolean MOVEPOLY = false;
	public static boolean drawBack = false;
	
	private ToolDisplayStatus toolDisplayStatus = ToolDisplayStatus.NORMAL;
	
	public enum ToolDisplayStatus {HOVERPOLYGON, HOVERPOE, HOVERNODE, HOVEREDGE, NORMAL};
	
	
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
		
		Button but1 = createButton("Node");
		but1.addListener(new ChangeListener() {
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
		
		Button but2 = createButton("Poly");
		but2.addListener(new ChangeListener() {
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
		
		Button but13 = createButton("MovePoly");
		but13.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	ToolBox.stopTool(curTool);
		        if (MOVEPOLY) {
		        	MOVEPOLY = false;
			        toolDisplayStatus = ToolDisplayStatus.NORMAL;
		        } else {
		        	MOVEPOLY = true;
			        toolDisplayStatus = ToolDisplayStatus.HOVERPOLYGON;
		        }

		    }
		});
		
		Button but3 = createButton("ConnPl1");
		but3.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	POLY = true;
		    	ToolBox.stopTool(curTool);
		        if(curTool == null) {
		        curTool = new ConnectPolygons();
		        curTool.prepare();
		        } else {
		        	curTool = null;
		        }
		
		     
		    }
		});
		
		Button but10 = createButton("ConnPl2");
		but10.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	POLY = true;
		    	ToolBox.stopTool(curTool);
		        if(curTool == null) {
		        curTool = new ConnectPolygons2();
		        curTool.prepare();
		        } else {
		        	curTool = null;
		        }
		
		     
		    }
		});
		
		
		Button but4 = createButton("DelNode");
		but4.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
		        if(curTool == null) {
		        curTool = new DeleteNode();
		        curTool.prepare();
		        } else {
		        	curTool = null;
		        }
		
	
		    }
		});
		
		
		Button but12 = createButton("DelEdge");
		but12.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
		        if(curTool == null) {
		        curTool = new DeleteEdge();
		        curTool.prepare();
		        } else {
		        	curTool = null;
		        }
		
	
		    }
		});
		
		
		Button but5 = createButton("ConnNod");
		but5.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
		    	if(curTool == null) {
			        curTool = new ConnectNodes();
			        curTool.prepare();
			        } else {
			        	curTool = null;
			        }
		
		    }
		});
		
		
		Button but6 = createButton("DrawBack");
		but6.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	if (drawBack) {
		    		drawBack = false;
		        } else {
		        	drawBack = true;
		        }
		
		    }
		});
		

		Button but7 = createButton("AddNode");
		but7.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	NODE = true;
		    	ToolBox.stopTool(curTool);
		        if(curTool == null) {
		        curTool = new AddNode();
		        curTool.prepare();
		        } else {
		        	curTool = null;
		        }
		
	
		    }
		});
		
		
		Button but11 = createButton("AddPoly");
		but11.addListener(new ChangeListener() {
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
		
		Button but8 = createButton("Save gr");
		but8.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	
		    Json json = new Json();
		    World.getWorld().getGraph().saveGraph();
		    String saveGraph = json.prettyPrint(World.getWorld().getGraph().getGraphSource());		    
		    FileHandle file = Gdx.files.local("source.graph");
		    file.writeString(saveGraph, false); 
		    Gdx.app.log("Graph written",saveGraph);
		    
		    }
		});
		
		Button but9 = createButton("Load gr");
		but9.addListener(new ChangeListener() {
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
		
		
	
		
		
        mainTable.add(but1).maxWidth(60);
        mainTable.row();
        mainTable.add(but2).maxWidth(60);
        mainTable.row();
        mainTable.add(but13).maxWidth(60);
        mainTable.row();
        mainTable.add(but7).maxWidth(60);
        mainTable.row();
        mainTable.add(but11).maxWidth(60);
        mainTable.row();
        mainTable.add(but3).maxWidth(60);
        mainTable.row();
        mainTable.add(but10).maxWidth(60);
        mainTable.row();
        mainTable.add(but4).maxWidth(60);
        mainTable.row();
        mainTable.add(but12).maxWidth(60);
        mainTable.row();
        mainTable.add(but5).maxWidth(60);
        mainTable.row();
        mainTable.add(but6).maxWidth(60);
        mainTable.row();
        mainTable.add(but8).maxWidth(60);
        mainTable.row();
        mainTable.add(but9).maxWidth(60);

        
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
		if (ToolBox.processTool(curTool)) {//Если ToolBox вернул true, значит инструмент завершил работу и его можну удалить
			curTool.finish();
			curTool = null;
		}
		drawObjects();
		//TODO Test cursor position
		RenderShapes.drawPoint(UI.getCursor(), 3, Colour.WHITE);
		movePolygonVertex();
		moveNode();
		
	}
	
	
	public void movePolygonVertex() {

		Vector2 touchPos = new Vector2();
	      touchPos.set(Gdx.input.getX(), Gdx.input.getY());
	      if(Gdx.input.isTouched() && MOVEPOLY) {  
	      
	      if (POLY == true)  {
	    	  	  Array<Polygon4> polys = new Array<Polygon4>();
	    	  	  
	    	  	  for (Entry<String, Polygon4> entry : world.getGraph().getPolygons().entrySet()) {
	    	  		  polys.add(entry.getValue());
	    	  	  }  	  
	    	  	  Polygon4 curPolygon, movePolygon;
				  int moveVertex;  //IDs of vertex in poly
			      int curVertex;
			      float curDistance;
			      float distance;
			      float nodDistance;
			           
			      if (polys.size > 0 ) {
			      
			      curPolygon = polys.get(polys.size-1);// Test only
			      movePolygon = curPolygon;
			      moveVertex = 1;
			      nodDistance = curPolygon.getDistanceToVertex(1, UI.getCursor().x, UI.getCursor().y);
			      
			      curDistance = 0;
			      for (Polygon4 pol : polys) {//Loop polys
				      
			    	  curVertex = 1;
				      curDistance = pol.getDistanceToVertex(1, UI.getCursor().x, UI.getCursor().y);

			    	  
			    	  for(int i = 1; i <= 4; i++) {
			    		  distance = pol.getDistanceToVertex(i, UI.getCursor().x, UI.getCursor().y);
			    		  if(distance < curDistance) {
		    			  curVertex = i;
			    			  curDistance = distance;
			    		  }				    		    
			    	  }
	    		  if(curDistance < nodDistance) {
		    			  curPolygon = pol;
			    		  nodDistance = curDistance;
			    		  moveVertex = curVertex;

			    		  }
	    		  movePolygon = curPolygon;
			      }
	     
			      if (movePolygon.getDistanceToVertex(moveVertex, UI.getCursor().x, UI.getCursor().y) < 20) {
			    	  movePolygon.setVertexXY(moveVertex,UI.getCursor().x, UI.getCursor().y);
					  RenderShapes.drawPoint(UI.getCursor(), 5, Colour.RED);
			      }
}
 
	      }
            }
		
	}
	
	
	

	
	
	public void moveNode() {
		 if(Gdx.input.isTouched()) {
			   
		      Vector2 touchPos = new Vector2();
		      touchPos.set(Gdx.input.getX(), Gdx.input.getY());
		   
		      if (NODE == true)    {
		   Node moveNode;
		   Array<Node> nodes = new Array<Node>();
				   
			for (Entry<String,Node> entry : world.getGraph().getNodes().entrySet()) {
				nodes.add(entry.getValue());
			}
		   
		   
		   
		   
		   
		   if (nodes.size > 0) {
		   
		      moveNode = nodes.get(nodes.size-1);
		      
		      for (Node nod : nodes) {
		    	  if (nod.getDistance(touchPos.x, UI.getCursor().y) < moveNode.getDistance(UI.getCursor().x, UI.getCursor().y)) {
		    		  moveNode = nod;
		    	  }
		      }
		      
		      			      if (moveNode.getDistance(UI.getCursor().x, UI.getCursor().y) < 20) {
		    	  
		    	  moveNode.setX(UI.getCursor().x);
		    	  moveNode.setY(UI.getCursor().y);
		      }
		      
		      }
		      
		      }
		      
		      

		      
		      
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
