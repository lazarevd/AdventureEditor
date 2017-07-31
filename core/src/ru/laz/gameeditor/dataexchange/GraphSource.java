package ru.laz.gameeditor.dataexchange;


import java.util.HashMap;

import ru.laz.gameeditor.graph.Node;


public class GraphSource {
	

	public HashMap<String, Node> nodes;
	public HashMap<String, POESource> poes;
	public HashMap<String, EdgeSource> edges;
	public HashMap<String, PolySource> polys;

	
	public GraphSource() {
		nodes = new HashMap<String, Node>();
		edges = new HashMap<String, EdgeSource>();
		polys = new HashMap<String, PolySource>();
		poes = new HashMap<String, POESource>();
	}
	
	
	public void initialize() {
		
		
	}
	
	public  HashMap<String, Node> getNodesSource() {
		return nodes;
	}
	
	public HashMap<String, POESource> getPOESource() {
		return poes;
	}
	
	public HashMap<String, EdgeSource> getEdgesSource() {
		return edges;
	}
	
	public HashMap<String, PolySource> getPolysSource() {
		return polys;
	}
	
	

	
	
	public void addNode(float x, float y, String name) {
		nodes.put(name, new Node(x,y,1));
	}
	
	

	

	
	public void addEdge(String node1, String node2, String name) {
		edges.put(name, new EdgeSource(node1, node2));
	}
	
	

	

	public void addPoly(float[] vertices, String name) {
		polys.put(name, new PolySource(vertices));
	}
	
	
	public void addPOE(int parentEdge, String parentPoly, float position, String name) {
		
		poes.put(name, new POESource(parentEdge, parentPoly, position));
	}
	
	
	
	
}
