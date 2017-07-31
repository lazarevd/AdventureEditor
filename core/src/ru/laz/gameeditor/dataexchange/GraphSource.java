package ru.laz.gameeditor.dataexchange;


import java.util.HashMap;

import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.graph.Edge;
import ru.laz.gameeditor.graph.Polygon4;
import ru.laz.gameeditor.graph.PointOnEdge;


public class GraphSource {
	

	public HashMap<String, Node> nodes;
	public HashMap<String, PointOnEdge> poes;
	public HashMap<String, Edge> edges;
	public HashMap<String, Polygon4> polys;

	
	public GraphSource() {
		nodes = new HashMap<String, Node>();
		edges = new HashMap<String, Edge>();
		polys = new HashMap<String, Polygon4>();
		poes = new HashMap<String, PointOnEdge>();
	}
	
	
	public void initialize() {
		
		}
	
	public  HashMap<String, Node> getNodesSource() {
		return nodes;
	}
	
	public HashMap<String, PointOnEdge> getPOESource() {
		return poes;
	}
	
	public HashMap<String, Edge> getEdgesSource() {
		return edges;
	}
	
	public HashMap<String, Polygon4> getPolysSource() {
		return polys;
	}
	

	
}
