package ru.laz.gameeditor.render;

import java.util.HashMap;

import ru.laz.gameeditor.graph.Edge;
import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.graph.PointOnEdge;
import ru.laz.gameeditor.graph.Polygon4;
import ru.laz.gameeditor.graph.Polygon4.DrawStat;
import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.ui.UI.ToolDisplayStatus;
import ru.laz.gameeditor.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class RenderShapes {
	
	static ShapeRenderer shapeRenderer;
	static boolean edgeOn, polyOn, nodeOn;
    static SpriteBatch spriteBatch;
    static BitmapFont font;
    CharSequence str = "default name";
	Sprite backgroungSprite;

    

	
	
	
	public enum Colour {YELLOW, BLUE, RED, WHITE, GREEN};
	
	
	public RenderShapes() {
		shapeRenderer = new ShapeRenderer();
	    spriteBatch = new SpriteBatch();
	    font = new BitmapFont();
	    backgroungSprite = new Sprite();
	}
	
	public static SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
	
	public void setBackgroundSprite(Texture tex) {
		this.backgroungSprite = new Sprite(tex);
		this.backgroungSprite.setSize(2048, 512);
	}
	
	public static void drawPoint(Vector2 xy, int r, Colour colour) {
		//Gdx.app.log("draw", "s");
		shapeRenderer.begin(ShapeType.Filled);
		switch (colour) {
		case YELLOW:
		shapeRenderer.setColor(1, 1, 0, 1);
		break;
		case BLUE:
		shapeRenderer.setColor(0, 0, 1, 1);
		break;
		case RED:
		shapeRenderer.setColor(1, 0, 0, 1);	
		break;
		case GREEN:
		shapeRenderer.setColor(0, 1, 0, 1);	
		break;
		default:
		shapeRenderer.setColor(1, 1, 1, 1);		
		}
		
		
		
		shapeRenderer.circle(xy.x, xy.y, r);
		shapeRenderer.end();
	}
	
	public static void drawLine(Vector2 xy1, Vector2 xy2, Colour colour) {
		shapeRenderer.begin(ShapeType.Line);
		switch (colour) {
		case YELLOW:
		shapeRenderer.setColor(1, 1, 0, 1);
		break;
		case BLUE:
		shapeRenderer.setColor(0, 0, 1, 1);
		break;
		case RED:
		shapeRenderer.setColor(1, 0, 0, 1);	
		break;
		case GREEN:
		shapeRenderer.setColor(0, 1, 0, 1);	
		break;
		default:
		shapeRenderer.setColor(1, 1, 1, 1);		
		}
		
		
		
		shapeRenderer.line(xy1.x, xy1.y, xy2.x, xy2.y);
		shapeRenderer.end();
	}
	
	
	public static void drawLine(Vector2 xy1, Vector2 xy2, String label, Colour colour) {
		shapeRenderer.begin(ShapeType.Line);
		switch (colour) {
		case YELLOW:
		shapeRenderer.setColor(1, 1, 0, 1);
		break;
		case BLUE:
		shapeRenderer.setColor(0, 0, 1, 1);
		break;
		case RED:
		shapeRenderer.setColor(1, 0, 0, 1);	
		break;
		case GREEN:
		shapeRenderer.setColor(0, 1, 0, 1);	
		break;
		default:
		shapeRenderer.setColor(1, 1, 1, 1);		
		}
		
		
		
		shapeRenderer.line(xy1.x, xy1.y, xy2.x, xy2.y);
		shapeRenderer.end();
		spriteBatch.begin();
		font.draw(spriteBatch, label, xy2.x, xy2.y - 10);
		spriteBatch.end();
		
	}
	
	public static void drawLine(int x1, int y1, int x2, int y2,  Colour colour) {
		shapeRenderer.begin(ShapeType.Line);
		switch (colour) {
		case YELLOW:
		shapeRenderer.setColor(1, 1, 0, 1);
		break;
		case BLUE:
		shapeRenderer.setColor(0, 0, 1, 1);
		break;
		case RED:
		shapeRenderer.setColor(1, 0, 0, 1);	
		break;
		case GREEN:
		shapeRenderer.setColor(0, 1, 0, 1);	
		break;
		default:
		shapeRenderer.setColor(1, 1, 1, 1);		
		}
		
		
		
		shapeRenderer.line(x1, y1, x2, y2);
		shapeRenderer.end();
	}
	

	public void drawNode(Node node, Colour colour) {
		
		if (node != null) {
		
		node.updateStatus();
		
		
		if(UI.getUI().getToolDisplayStatus() == ToolDisplayStatus.HOVERNODE && node.getDistance(UI.getCursor().x, UI.getCursor().y) < 10) {
		shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1);	
		} else {
		

		switch (colour) {
		case YELLOW:
		shapeRenderer.setColor(1, 1, 0, 1);
		break;
		case BLUE:
		shapeRenderer.setColor(0, 0, 1, 1);
		break;
		case RED:
		shapeRenderer.setColor(1, 0, 0, 1);	
		break;
		default:
		shapeRenderer.setColor(1, 1, 1, 1);		
		}
		}
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.circle(node.getX(), node.getY(), 10);
		shapeRenderer.end();
		
		
	    spriteBatch.begin();
	    try {
	    font.draw(spriteBatch, node.getThisId().toString(), node.getX(), node.getY() - 10);
	    } catch (NullPointerException nex) {//Catch exception when node has no name.
	    font.draw(spriteBatch, "???", node.getX(), node.getY() - 10);	
	    }
	    //font.draw(spriteBatch, node.getX() + "; " +node.getY(), node.getX() + 10, node.getY());
	    
	    /*
	    if (node.getListOfPOE() != null) {// Print parent POEs
			font.draw(spriteBatch, node.getListOfPOE().get(0), node.getX()-15, node.getY() + 35);
			font.draw(spriteBatch, node.getListOfPOE().get(1), node.getX()-15, node.getY() + 20);
	    }
	    */
	    
	    if (node.isStart() == true) {
	   // font.draw(spriteBatch, "Angle to fin: " + Float.toString(node.getAngle(World.getWorld().getGraph().finish)), 20, 20);	
	   // font.draw(spriteBatch, "Angle to n1: " + Float.toString(node.getAngle(World.getWorld().getGraph().n1)) + " ; " + Float.toString(node.getAngle(World.getWorld().getGraph().finish) - node.getAngle(World.getWorld().getGraph().n1)), 20, 35);
	   // font.draw(spriteBatch, "Angle to n2: " + Float.toString(node.getAngle(World.getWorld().getGraph().n2)) + " ; " + Float.toString(node.getAngle(World.getWorld().getGraph().finish) - node.getAngle(World.getWorld().getGraph().n2)), 20, 50);
	    }
	    
	    spriteBatch.end();
		
		}
	}
	
	public void drawEdge(Edge edge, Colour colour) {
		
		if (edge != null) {
			
		
		
		switch (colour) {
		case YELLOW:
		shapeRenderer.setColor(1, 1, 0, 1);
		break;
		case BLUE:
		shapeRenderer.setColor(0, 0, 1, 1);
		break;
		case RED:
		shapeRenderer.setColor(1, 0, 0, 1);	
		break;
		case WHITE:
		shapeRenderer.setColor(1, 1, 1, 1);
		break;
		default:
		shapeRenderer.setColor(1, 1, 1, 1);		
		}
		
		if(UI.getUI().getToolDisplayStatus() == ToolDisplayStatus.HOVEREDGE && edge.isPointOver(UI.getCursor()) == true) {
			shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1);}
			
		try {
			shapeRenderer.begin(ShapeType.Line);
		float x, y, x2, y2;
		
		x = World.getWorld().getGraph().getNodes().get(edge.getNodes().get(0)).getX();
		y = World.getWorld().getGraph().getNodes().get(edge.getNodes().get(0)).getY();
		x2 = World.getWorld().getGraph().getNodes().get(edge.getNodes().get(1)).getX();
		y2 = World.getWorld().getGraph().getNodes().get(edge.getNodes().get(1)).getY();

		
		shapeRenderer.line(x, y, x2, y2);
		
		/*
		spriteBatch.begin();
		font.draw(spriteBatch, edge.getThisId().toString(), x + 10, y + 10);
		spriteBatch.end();
		*/
		
		} 
		catch (NullPointerException nex) {
			Gdx.app.log("No node to draw", nex.toString());
		}
		//Gdx.app.log("Edge: ", edge.getNodes()[0].getX() + " " + edge.getNodes()[0].getY() + "; " + edge.getNodes()[1].getX() + " " + edge.getNodes()[1].getY());
		shapeRenderer.end();
		}
		
		//debug
		/*
		Vector2 p1, p2, p3, p4;
		
		p1 = new Vector2(edge.getEdgeSquare()[0], edge.getEdgeSquare()[1]);
		p2 = new Vector2(edge.getEdgeSquare()[2], edge.getEdgeSquare()[3]);
		p3 = new Vector2(edge.getEdgeSquare()[4], edge.getEdgeSquare()[5]);
		p4 = new Vector2(edge.getEdgeSquare()[6], edge.getEdgeSquare()[7]);
		
		drawPoint(p1, 3, Colour.BLUE);
		drawPoint(p2, 3, Colour.RED);
		drawPoint(p3, 3, Colour.GREEN);
		drawPoint(p4, 3, Colour.YELLOW);
		
		drawLine(p1, p2, Colour.BLUE);
		drawLine(p2, p3, Colour.RED);
		drawLine(p3, p4, Colour.GREEN);
		drawLine(p4, p1, Colour.YELLOW);
		*/
		
	}
	
	public void drawPolygon(Polygon4 polygon, Colour colour) {
		
		float[] vertices = polygon.getVertices();

		if (UI.getUI().getToolDisplayStatus() == ToolDisplayStatus.HOVERPOLYGON && polygon.isPointInside(new Vector2(UI.getCursor().x, UI.getCursor().y))) {
			
		shapeRenderer.setColor(1, 0.3f, 0.3f, 1);	
			
		} else {
		
		if (polygon.drawStat == DrawStat.NORMAL) {
		switch (colour) {
		case YELLOW:
		shapeRenderer.setColor(1, 1, 0, 1);
		break;
		case BLUE:
		shapeRenderer.setColor(0, 0, 1, 1);
		break;
		case RED:
		shapeRenderer.setColor(1, 0, 0, 1);	
		break;
		case WHITE:
		shapeRenderer.setColor(1, 1, 1, 1);
		break;
		default:
		shapeRenderer.setColor(1, 1, 1, 1);		
		}
		} else if (polygon.drawStat == DrawStat.SELECTED) {
		shapeRenderer.setColor(0.5f, 0.5f, 1, 1);	
		}
		
		}
		
		shapeRenderer.begin(ShapeType.Line);//This draw lines.
		
		shapeRenderer.polygon(vertices);
		

		
		shapeRenderer.end();
		
		/*
		for(int i = 1; i <=4; i ++) {		
			polygon.drawNormal(i);	
		}
		
		*/
		
		shapeRenderer.begin(ShapeType.Filled); //And this draw vertices.
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.circle(vertices[0], vertices[1], 5);
		shapeRenderer.circle(vertices[2], vertices[3], 5);
		shapeRenderer.circle(vertices[4], vertices[5], 5);
		shapeRenderer.circle(vertices[6], vertices[7], 5);
		  
		shapeRenderer.end();

	}
	
	public void drawPOE(PointOnEdge poe) {
		poe.setPointPosition();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		if (UI.getUI().getToolDisplayStatus() == ToolDisplayStatus.HOVERPOE && poe.getPosition().dst(UI.getCursor().x, UI.getCursor().y) < 20) {
		shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1);}
		shapeRenderer.circle(poe.getPosition().x,poe.getPosition().y , 5);
		shapeRenderer.end();
		spriteBatch.begin();
		//font.draw(spriteBatch, poe.getThisId() + ", p: " + poe.getParentPolygon() + ", k: " + poe.getEdgePosition() + ", pe: " + poe.getParentEdge(), poe.getPosition().x, poe.getPosition().y - 10);
		spriteBatch.end();
		}
	 
	
	
	
	public void drawBackground() {
		if (UI.drawBack) {
		spriteBatch.begin();
		backgroungSprite.draw(getSpriteBatch());
		spriteBatch.end();
		}
	}
	
}
