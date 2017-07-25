package ru.laz.gameeditor.dataexchange;

public  class POESource {
	
	public int parentEdge;
	public String parentPoly;
	public float position;
	
	
	public POESource() {
		
	}

	public POESource(int parentEdge, String parentPoly, float position) {
		this.parentEdge = parentEdge;
		this.parentPoly = parentPoly;
		this.position = position;
	}
	
	
	
}
