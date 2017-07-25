package ru.laz.gameeditor.dataexchange;

import com.badlogic.gdx.utils.Array;

public class PolySource {
	public float[] vertices;
	
	public PolySource() {
		
	}
	
	public PolySource(float[] vertices) {
		this();
		this.vertices = vertices;
	}
}
