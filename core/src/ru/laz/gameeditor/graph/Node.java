package ru.laz.gameeditor.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map.Entry;

import ru.laz.gameeditor.render.RenderShapes.Colour;
import ru.laz.gameeditor.world.World;

public class Node {
	
	private Array<String> poeList;
	protected NodeType nodeType;
	private transient boolean start, finish = false;
	private float x, y;
	private float renderScale = 0.0f;
	private transient String parentNode;
	private transient Array<String> neighbours;
	private transient int G, H; //G - from start cost, H - to finish heuristic cost.
	public transient Colour colour;
	
	
	enum NodeType {STANDART, POE};
	
	
	
	private Node(){
		this.neighbours = new Array<String>();
	}//empty constructor for serialization
	
	public Node(float x2, float y2) {
		nodeType = NodeType.STANDART;
		this.x = x2;
		this.y = y2;	
		this.colour = Colour.YELLOW;
		this.neighbours = new Array<String>();
		this.parentNode = null; 
		this.G = 0;
	}
	
	
	public Node(float x2, float y2, int sf) {
		this(x2, y2);
		
		switch (sf) {
		case 1: start = true;
		break;
		case 2: finish = true;
		
		}	
	}
	
	
	public Node(Array<String> poeList) {
		this(100,100);
		this.poeList = poeList;
		nodeType = NodeType.POE;
		setPOEPosition();
		for (String poe : poeList) {		
			
				try{
					World.getWorld().getGraph().getPOEs().get(poe).setChildNode(this.getThisId());
				} catch (NullPointerException nex) {
					Gdx.app.log("Can`t set child POE node", nex.toString());
				}	
		}
	}
	

	public boolean isStart() {
		return start;
	}
	
	public boolean isFinish() {
		return finish;
	}

	public String getThisId() {
		String ret = null;
		
		for (Entry<String, Node> entry : ((HashMap<String, Node>)World.getWorld().getGraph().getNodes()).entrySet()) {
			if (entry.getValue().equals(this)) {
				ret = entry.getKey();
				break;
			}
		}
		
		return ret;
	}
	
	
	public void clearLinked() {
		
		deleteFromNeighbours();
			
		for (String edg : getNeighbourEdges()) {
		World.getWorld().getGraph().getEdges().remove(edg);
		}
	
		deleteLinkedPOE();
		
	}
	
	

		
		
	
	public void deleteFromNeighbours () {
		for (Node nod : World.getWorld().getGraph().getNodes().values()) {
			nod.getNeighbours().removeValue(World.getWorld().getGraph().getNodeKey(this), true);
		}
	}
	
	
	public static Node getNearestNode(Vector2 inputXY) {
		
		float curDist, dist;
		Node curNode = null;
		
		try {

		
		Entry<String, Node> getNode = World.getWorld().getGraph().getNodes().entrySet().iterator().next();
		
		curNode = getNode.getValue();
		curDist = inputXY.dst(curNode.getX(), curNode.getY());
		dist = curDist;
		
		
		World.getWorld().getGraph().getNodes();
		
		for (Entry<String, Node> entry : World.getWorld().getGraph().getNodes().entrySet()) {
			curDist = inputXY.dst(entry.getValue().getX(), entry.getValue().getY());
			if (dist > curDist) {
				dist = curDist;
				curNode = entry.getValue();
			}
		}
		
		
		} 
		catch (java.util.NoSuchElementException nex) {
			
		}
		return curNode;
		
	}
	
	
	public Array<String> getNeighbourEdges() {
		
		Array<String> retEdge = new Array<String>();
		
		
		for(Entry<String, Edge> entry : World.getWorld().getGraph().getEdges().entrySet()) {
			if (getThisId().equals(entry.getValue().getNodes().get(0)) || getThisId().equals(entry.getValue().getNodes().get(1))) {
				
				if (!retEdge.contains(entry.getKey(), true)) retEdge.add(entry.getKey());
			}
		}
		
	return retEdge;

	}

	
	public boolean deleteNeighbour(String name) {
		boolean result = neighbours.removeValue(name, true);
		
		return result;
	}
	
	

	public String printNeighbours() {
		
		String neighbours = "";
		
		
		for (String node : getNeighbours()) {
			neighbours = neighbours + " " + node.toString();
		}
		
		return "[" + neighbours + "]";
	}
	

	public void addNeighbour(String name) {
		//Gdx.app.log("Fill", "start");
		
		if (neighbours.size > 0){
		for (String nod : neighbours) {
			if (nod.equals(name)) {
				return;
					}
				}
		}
		neighbours.add(name);
		
	}
	
	
	
	public void setNeighbours(Array<String> names) {
	this.neighbours = names;	
	}

	/*
	public float getCost(Node node) { //Get Heuristic cost. We need this when we chose next node to move.
		
		
		
		
		
		class CustomComparator implements Comparator<Node> {
		    @Override
		    public int compare(Node o1, Node o2) {
		        return o1.getCost(node).compareTo(o2.getCost(node));
		    }

		}
		
		Vector2 vec;
		
		for (Node nod : getNeighbours())
			
			
	}
	

	*/
	public Array<String> getNeighbours() {
	return this.neighbours;	
	}	
	
	

	
	
	public void setParent(String parent) {
		this.parentNode = parent;
	}

	public String getParent() {
		return this.parentNode;
	}	
	
	public NodeType getNodeType() {
		return this.nodeType;
	}
	
	
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void setX(float x2) {
		this.x = x2;
	}
	
	public void setY(float y2) {
		this.y = y2;

	}
	
	

	
	public int getCost() {
		return this.G + this.H;
	}
	
	
	public void setG(int g) {
		this.G = g;
	}
	
	public void setH(int h) {
		this.H = h;
	}
	
	public int getG() {
		return this.G;
	}
	
	public int getH() {
		return this.H;
	}
	
	public float getDistance(Node node) {
		float dist;
		
		Vector2 vec1 = new Vector2(this.x,this.y);
		dist = vec1.dst(node.getX(), node.getY());	
		return dist;
	}
	
	public float getDistance(float x, float y) {
		float dist;
		
		Vector2 vec1 = new Vector2(this.x,this.y);
		dist = vec1.dst(x, y);	
		return dist;
	}	
	
	
	
	public float getAngle(Node node) {
		float deltaX, deltaY;
		float angle;
		
		deltaX = node.getX() - this.x;
		deltaY = node.getY() - this.y;

		angle = (float)Math.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
		
		return angle;
	}
	
	
	public int countHCost() {
		
		//float angle; 
		float distance;
		int H;
		
		//angle = getAngle(World.getWorld().getGraph().finish);
		distance = Math.abs(getDistance(World.getWorld().getGraph().getFinish()));
		
		H = (int) distance;
		
		
		return H;
	}
	
	
	public void updateStatus() {
		
		if (this.getNodeType() == NodeType.POE)
		setPOEPosition();
	
	}


	
	//Methods for POE type
	public Array<String> getListOfPOE () {
		return this.poeList;
	}
	
	
	public void setPOEPosition() {
		PointOnEdge poe1 = World.getWorld().getGraph().getPOEById(poeList.get(0));
		PointOnEdge poe2 = World.getWorld().getGraph().getPOEById(poeList.get(1));
				
		try {
		Vector2 pos = getMiddleOfLine(poe1.getPosition().x, poe1.getPosition().y, poe2.getPosition().x, poe2.getPosition().y );
		this.setX((int)pos.x);
		this.setY((int)pos.y);		
		}
		catch (NullPointerException nex) {
			Gdx.app.log("POE", poeList.get(0) + " : " + poe1 + "|"+ poeList.get(1) + " : " + poe2);
			Gdx.app.log("setPOEPosition() - no poe! ", nex.toString());
		}
	}
	
	
	public Vector2 getMiddleOfLine(float x1, float y1, float x2, float y2) {
		
		float x = (x1 + x2)/2;
		float y = (y1 + y2)/2;
		
		Vector2 ret = new Vector2(x,y);
		
		return ret;
}
	

	public void deleteLinkedPOE() {
		
			if (poeList != null ) {
				for (String poe : poeList) {
					Gdx.app.log("Deleting POE", poe);
				World.getWorld().getGraph().getPOEs().remove(poe);
				}
				poeList.clear();
			}
		
	}



	public void setRenderScale(float distance) {
		this.renderScale = distance;
	}

	public float getRenderScale() {
		return renderScale;
	}
	
	
	
	

	
}
