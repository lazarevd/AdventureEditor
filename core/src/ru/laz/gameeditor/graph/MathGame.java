package ru.laz.gameeditor.graph;

import com.badlogic.gdx.math.Vector2;

public class MathGame {


	public static Vector2 lineToVector(Vector2[] line) {
		Vector2 res = new Vector2(line[1].x-line[0].x, line[1].y - line[0].y);
		
		return res;
	}
	
	public static Vector2 lineToVector(Vector2 xy0, Vector2 xy1) {
		Vector2 res = new Vector2(xy1.x-xy0.x, xy1.y - xy0.y);
		
		return res;
	}
	
}
