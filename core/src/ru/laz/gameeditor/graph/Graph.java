package ru.laz.gameeditor.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.laz.gameeditor.dataexchange.EdgeSource;
import ru.laz.gameeditor.dataexchange.GraphSource;
import ru.laz.gameeditor.dataexchange.NodeSource;
import ru.laz.gameeditor.dataexchange.POESource;
import ru.laz.gameeditor.dataexchange.PolySource;
import ru.laz.gameeditor.graph.Node.NodeType;
import ru.laz.gameeditor.world.World;





import com.badlogic.gdx.Gdx;

public class Graph {//Use generic for key (id) of elements (node, pole etc.)
	
	
	
	//Node start, n1, n2, n3, n4, n5, n6, n7, n8, n9, finish;
	//Edge e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12;
	//Polygon4 poly1, poly2, poly3, poly4, poly5;
	
	
	
	public HashMap<String, Node> nodes;
	private HashMap<String, Edge> edges;
	private HashMap<String, Polygon4> polygons;
	private HashMap<String, PointOnEdge> poes;
	private GraphSource graphSource;
	
	public Graph() {
		nodes = new HashMap<String, Node>();
		edges = new HashMap<String, Edge>();
		polygons = new HashMap<String, Polygon4>();
		poes = new HashMap<String, PointOnEdge>();
		
		graphSource = new GraphSource();
		
		
		/*
		graphSource.addNode(200,200, "n1");		
		graphSource.addNode(400,250, "n2");
		
		graphSource.addEdge("n1", "n2", "e2");
		
		
		float[] polyFloat = {100,100,200,100,200,200,100,200};
		float[] polyFloat2 = {150,200,250,200,250,300,150,300};
		float[] polyFloat3 = {200,300,300,300,300,400,200,400};
		float[] polyFloat4 = {250,400,350,400,350,500,250,500};
		
		graphSource.addPoly(polyFloat, "poly1");
		graphSource.addPoly(polyFloat2, "poly2");
		graphSource.addPoly(polyFloat3, "poly3");
		graphSource.addPoly(polyFloat4, "poly4");
		
		
		loadGraph();
	*/
		
	}

	
	

	
	
	public GraphSource getGraphSource() {
		return this.graphSource;
	}
 	
	public void setGraphSource(GraphSource graphSource) {
		this.graphSource = graphSource;
	}
	
	
	
	public void saveGraph() {
		graphSource.getNodesSource().clear(); //clear export graph first
		graphSource.getEdgesSource().clear();
		graphSource.getPolysSource().clear();
		graphSource.getPOESource().clear();
		
		
		
		
		for (Entry<String,Node> nd : nodes.entrySet()) {
			if (!nd.getValue().isStart() && !nd.getValue().isFinish()) //dont export start/finish
				if(nd.getValue().nodeType == NodeType.STANDART) {
				graphSource.getNodesSource().put(nd.getKey(), new NodeSource(nd.getValue().getX(), nd.getValue().getY(), 1));//standart
				} else if(nd.getValue().nodeType == NodeType.POE) {
				graphSource.getNodesSource().put(nd.getKey(), new NodeSource(nd.getValue().getX(), nd.getValue().getY(), 2, nd.getValue().getListOfPOE()));//poe
				}	
		
		for (Entry<String,Edge> ed : edges.entrySet()) {
			Gdx.app.log("ed", ed.getKey());
			graphSource.getEdgesSource().put(ed.getKey(), new EdgeSource(ed.getValue().getNodes().get(0), ed.getValue().getNodes().get(1)));
		}
		
		for (Entry<String,Polygon4> pl : polygons.entrySet()) {
			
			PolySource polySrc = new PolySource(pl.getValue().getVertices());
			
			graphSource.getPolysSource().put(pl.getKey(), polySrc);	
		}
		
		for (Entry<String, PointOnEdge> poe : poes.entrySet()) {
			Gdx.app.log("poe", poe.getValue().getParentPolygon());
			graphSource.getPOESource().put(poe.getKey(), new POESource(poe.getValue().getParentEdge(), poe.getValue().getParentPolygon(), poe.getValue().getEdgePosition()));
		}
	}
	}
	
	public void loadGraph() {
		
		
		edges.clear();
		nodes.clear();
		polygons.clear();
		poes.clear();
		
		
		for (Entry<String,NodeSource> ns : graphSource.nodes.entrySet()) {
			if (ns.getValue().nodeType == 1)//Standart nodes
			addNode(ns.getKey(), new Node(ns.getValue().x, ns.getValue().y));
		}
		
		for (Entry<String,EdgeSource> ed : graphSource.edges.entrySet()) {
			addEdge(ed.getKey(), new Edge(ed.getValue().node1, ed.getValue().node2));
		}
			
		for (Entry<String,PolySource> ps : graphSource.polys.entrySet()) {
			Polygon4 polygon = new Polygon4(ps.getValue().vertices);	
				addPolygon(ps.getKey(), polygon);		
		}
		
		for(Entry<String, POESource> poeSrcId : graphSource.poes.entrySet()) { //Делать всегда отдельным циклом каждый список класса
			polygons.get(poeSrcId.getValue().parentPoly).addPointOnEdge(poeSrcId.getValue().parentEdge, poeSrcId.getValue().position, poeSrcId.getKey());
		Gdx.app.log("Adding poe", poeSrcId.getKey());
		}		
				
		for (Entry<String,NodeSource> ns : graphSource.nodes.entrySet()) {
			if (ns.getValue().nodeType == 2)//POE nodes
				addNode(ns.getKey(), new Node(ns.getValue().getPOEs()));
			}		
			
		
		
		}
		
		
		
		/*
		for (NodePOESource nps : graphSource.getNodesPOESource()) {
			addNode();
			pointsOnEdges.add(selectedPolygons.get(0).addPointOnClosestEdge(selectedCoords.get(0).x, selectedCoords.get(0).y));//На основании ранее отселекченных координат добавляем
			pointsOnEdges.add(selectedPolygons.get(1).addPointOnClosestEdge(selectedCoords.get(1).x, selectedCoords.get(1).y));//точки на ребра (poe) на отселекченные полигоны. сразу добавлем их в список poe
			World.getWorld().getGraph().addNode(new NodeConnector(pointsOnEdges, true));

		}
		
	*/	
	
		
		

	
	
	
	public PointOnEdge getPOEById(String name) {	
		return this.poes.get(name);
	}
	
	public HashMap<String, PointOnEdge> getPOEs() {
		return this.poes;
	}
	

	public HashMap<String, Node> getNodes() {
		return this.nodes;
	}
	
	
	public String getNodeKey(Node node) {
		
		for(Entry<String, Node> entry : nodes.entrySet()) {
			if (node.equals(entry.getValue())) {
				return entry.getKey();
			}
		}	
		return null;
	}
	
	public HashMap<String, Polygon4> getPolygons() {
		
		return this.polygons;
	}
	
	
	public String getEdgeKey(Edge edge) {
		
		for (Entry<String, Edge> entry : edges.entrySet()) {
			
			if (entry.equals(edge)) {
				return entry.getKey();
			}
		}
		
		
		return null;
	}
	
	
	public HashMap<String, Edge> getEdges() {
		return this.edges;
	}
	
	public void addNode(String name, Node node) {		
			
		this.nodes.put(name, node);

	}
	
	

	public void addEdge(String name, Edge edge) {		
		
		
		
		if (edge != null) {
			for(Entry<String, Edge> entry : World.getWorld().getGraph().edges.entrySet()) {
				try {
					String sn1 = edge.getNodes().get(0);
					String sn2 = edge.getNodes().get(1);
					String dn1 = entry.getValue().getNodes().get(0);
					String dn2 = entry.getValue().getNodes().get(1);
				if((sn1.equals(dn1) && sn2.equals(dn2)) || (sn2.equals(dn1) && sn1.equals(dn2))) {
					return;//Check each edge. Is there edge with same nodes. If is - then return;
				}
					
				} catch (NullPointerException nex) {
					Gdx.app.log("error adding edge", nex.getLocalizedMessage());
				}
			}		
 		this.edges.put(name, edge);	
		}
	
	}
	
	public void addPolygon(String name, Polygon4 polygon) {
		if (polygon != null) {
			this.polygons.put(name, polygon);
		}
	}
	
	
	public void deleteNode(String name) {
		Node procNode = nodes.get(name);	
		procNode.clearLinked();	
		nodes.remove(name);
		refreshNeighbours();
	}
	
	
	public void deleteEdge(String name) {
		edges.remove(name);	
		refreshNeighbours();
	}
	
	
	
	
public void refreshNeighbours() {
	
		fillNeighbours();
	
}




public void fillNeighbours() {
	
	for (Entry<String, Node> entry : nodes.entrySet()) {
		
	
	for (Edge edg : edges.values()) {
		if (entry.getKey().equals(edg.getNodes().get(0))) {//if first node exist in edge, then second is neigbout
		entry.getValue().addNeighbour(edg.getNodes().get(1));
		} else if (entry.getKey().equals(edg.getNodes().get(1))) {//  and vice versa 
		entry.getValue().addNeighbour(edg.getNodes().get(0));		
		}
	}
	
	}


}


public static String getNewNodeName() {
	String newName = null;
	
	newName = "node" + (getLastNodeNameNumber() + 1);
	
	
	return newName;
}


public static int getLastNodeNameNumber() {
	
	int curInt = 0;
	String regExp = "[0-9]+$";
	Pattern p = Pattern.compile(regExp);
	Matcher m;
	
	
	
	for (String nam : World.getWorld().getGraph().getNodes().keySet()) {
		m = p.matcher((String)nam);
		//Gdx.app.log("node numb", curInt +" name: " + nod.getName() );
		
		if (m.find()) {
		
		if(Integer.parseInt(m.group()) > curInt) {
			curInt = Integer.parseInt(m.group());
		}
		}
	}

	return curInt;
}


public static String getNewEdgeName() {
	String newName = null;
	
	newName = "edge" + (getLastEdgeNameNumber() + 1);
	
	
	return newName;
}

public static int getLastEdgeNameNumber() {
	
	int curInt = 0;
	String regExp = "[0-9]+$";
	Pattern p = Pattern.compile(regExp);
	Matcher m;
	
	
	
	for (String nam : World.getWorld().getGraph().getEdges().keySet()) {
		m = p.matcher((String)nam);
		//Gdx.app.log("node numb", curInt +" name: " + nod.getName() );
		
		if (m.find()) {
		
		if(Integer.parseInt(m.group()) > curInt) {
			curInt = Integer.parseInt(m.group());
		}
		}
	}

	return curInt;
}








public static String getNewPolyName() {
	String newName = null;
	
	newName = "poly" + (getLastPolyNameNumber() + 1);
	
	
	return newName;
}

public static int getLastPolyNameNumber() {
	
	int curInt = 0;
	String regExp = "[0-9]+$";
	Pattern p = Pattern.compile(regExp);
	Matcher m;
	
	
	
	for (String nam : World.getWorld().getGraph().getPolygons().keySet()) {
		m = p.matcher((String)nam);
		//Gdx.app.log("node numb", curInt +" name: " + nod.getName() );
		
		if (m.find()) {
		
		if(Integer.parseInt(m.group()) > curInt) {
			curInt = Integer.parseInt(m.group());
		}
		}
	}

	return curInt;
}




















	public Node getStart() {

		Node ret = null;
		
		for (Node nod : nodes.values()) {
			if (nod.isStart()) {
				ret = nod;
			}
		}
		
		return ret;
	}
	
	
	public String getStartId() {
		
		String ret = null;
		
		ret = getNodeKey(getStart());
		
		return ret;
	}
	
	
	public Node getFinish() {
		Node ret = null;
		
		for (Node nod : nodes.values()) {
			if (nod.isFinish()) {
				ret = nod;
			}
		}
		
		return ret;
	}
	
	public String getFinishId() {
		String ret = null;
		
		ret = getNodeKey(getFinish());
		
		return ret;
	}
	
	public Node getByName(String name) {
		
		
		return nodes.get(name);
	}

	
public ArrayList<String> AStarSearch() {
	
	fillNeighbours();
	
	
		for (Node nd : nodes.values()) {		//Assign default parents (nodes itself for begining);
			nd.setParent(getNodeKey(nd));
		}
		
		Gdx.app.log("Start AStar", "\n");
		
		ArrayList<String> openList = new ArrayList<String>();
		ArrayList<String> closedList = new ArrayList<String>();
		ArrayList<String> finalPath = new ArrayList<String>();
		String curNode;
		
		
		openList.add(getStartId()); //This will be start
		
		
		while(openList.size() > 0 && !closedList.contains(getFinishId())) {
			

			curNode = openList.get(openList.size()-1); // Get last element (FIFO queue)
			
			
			for (String openNode : openList) {//Find node with lowest price
				
				if (nodes.get(curNode).getCost() > nodes.get(openNode).getCost()) {
					curNode = openNode;
				}
				//Gdx.app.log("Min price", curNode.getName());
			}
			
			
			
			closedList.add(curNode);
			openList.remove(curNode);
			
			Gdx.app.log("Current node", curNode + "; Parent node: " + nodes.get(curNode).getParent() + "; G: " + nodes.get(curNode).getG() + "\n");
			Gdx.app.log("Neigbours list", nodes.get(curNode).printNeighbours());
			Gdx.app.log("Open list", printNodes(openList));
			Gdx.app.log("Closed list", printNodes(closedList) + "\n");
			
			
			for (String nam : nodes.get(curNode).getNeighbours()) {

				if (!closedList.contains(nam)) {
				
				nodes.get(nam).setParent(curNode);
				nodes.get(nam).setG(nodes.get(nodes.get(curNode).getParent()).getG() + 10); //G - from start cost, //Big construction. HashMaps..
				nodes.get(nam).setH(nodes.get(nam).countHCost()); //H - to finish heuristic cost. 
				//Gdx.app.log("Neigbours stats", nod.getName() + ", Dist: " + Float.toString(nod.getDistance(World.getWorld().getGraph().finish)) + ", Rad: " + Float.toString(curNode.getAngle(World.getWorld().getGraph().finish)));
				//Gdx.app.log("Neigbours costs", nod.getName() + ", G: " + Float.toString(nod.getG()) + ", H: " + Float.toString(nod.getH()) + ", Cost: " + nod.getCost() + "\n\n");

				if (!openList.contains(nam)){		
				openList.add(nam);

				}
				}
				

				
				
				
			}
			


		}
		
		
		
		
		
		String curentNod = getFinishId();
		
		while (curentNod != getStartId()) { //add cycle count to debug
			
			if  (finalPath.size() < 100)  {
			
			finalPath.add(curentNod);
			curentNod = nodes.get(curentNod).getParent();
			Gdx.app.log("curNode", curentNod);
			//Gdx.app.log("Build fin path", curentNod.getName() + " " + curentNod.getParent().getName());
			}
		}
		finalPath.add(getStartId());
		
		Gdx.app.log("Final nodes", printNodes(finalPath));	
		Gdx.app.log("Final size", Integer.toString(finalPath.size()) + "\n");
		
		return finalPath;
	}
	


public String printNodes(ArrayList<String> nodeList) {
	
	String neighbours = "";
	
	
	for (String node : nodeList) {
		neighbours = neighbours + " " + node.toString();
	}
	
	return "[" + neighbours + "]";
}


}
