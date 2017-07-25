package ru.laz.gameeditor.graph;


import java.util.HashMap;
import java.util.Map.Entry;

import ru.laz.gameeditor.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class PointOnEdge {
	
	private Vector2 position;
	private int parentEdge;
	private float edgePosition;
	private String parentPoly;
	private String childNode;
	
	public PointOnEdge(String poly, int parentEdge, float k) {
		this.parentEdge = parentEdge;
		this.position = new Vector2();
		this.edgePosition = k;
		this.parentPoly = poly;
	}
	
	public String getThisId() {
		String ret = null;
		
		for (Entry<String, PointOnEdge> entry : ((HashMap<String, PointOnEdge>)World.getWorld().getGraph().getPOEs()).entrySet()) {
			if (entry.getValue().equals(this)) {
				ret = entry.getKey();
				break;
			}
		}
		
		return ret;
	}
	
	
	public void setChildNode (String name) {
		this.childNode = name;
	}
	
	
	public String getChildNode () {
		return this.childNode;
	}
	
	
	public PointOnEdge(String poly, int parentEdge, float k, Node node) {
		this.parentEdge = parentEdge;
		this.position = new Vector2();
		this.edgePosition = k;
		this.parentPoly = poly;
	}

	
	public int getParentEdge() {
		return this.parentEdge;
	}
	
	public String getParentPolygon() {
		return this.parentPoly;
	}
	
	
	public float getEdgePosition() {
		return this.edgePosition;
	}
	
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public void setPosition(Vector2 pos) {
	this.position = pos;
	}
	
	
	public void setPointPosition() {
	
	int edge = this.parentEdge;
	float length = this.edgePosition;
		
	Vector2[] edVec = new Vector2[2];
	Vector2 tmp;

	
	edVec = World.getWorld().getGraph().getPolygons().get(this.parentPoly).getEdge(edge);
	tmp = new Vector2(edVec[1].x - edVec[0].x, edVec[1].y - edVec[0].y);
	
	tmp = tmp.scl(length);
	tmp.x = tmp.x + edVec[0].x;
	tmp.y = tmp.y + edVec[0].y;
	
	setPosition(tmp);
		
	}
}
