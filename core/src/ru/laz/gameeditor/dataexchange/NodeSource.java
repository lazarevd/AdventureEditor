package ru.laz.gameeditor.dataexchange;

import java.util.Map.Entry;

import com.badlogic.gdx.utils.Array;


	public class NodeSource {
		public float x, y;
		public int nodeType;// 1 - standart, 2 - poe
		public Array<String> poes;
		
		
		public NodeSource() {
		}
		
		public NodeSource(float x, float y, int type) {
			this.x = x;
			this.y = y;
			this.nodeType = type;
		}
		
		public NodeSource(float x, float y, int type, Array<String> poes) {
			this(x, y, type);
			this.poes = poes;
		}
		
		public Array<String> getPOEs() {
			return this.poes;
		}
		
	}
