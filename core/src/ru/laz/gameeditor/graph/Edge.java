package ru.laz.gameeditor.graph;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map.Entry;

import ru.laz.gameeditor.world.World;

public class Edge {
	
	
	private Array<String> nodesToLink;

	private Edge() {}//empty constructor for serialization
	
	public Edge(String n1, String n2) {
		if (n1.equals(n2)) {
			Gdx.app.log("","Node1 must be != Node2");
			throw new IllegalArgumentException("Node1 must be != Node2");
		}
		nodesToLink = new Array<String>();
		nodesToLink.add(n1);
		nodesToLink.add(n2);
	}
	
	
	public Array<String> getNodes () {
		return nodesToLink;
	}

	
	public String getThisId() {
		String ret = null;
		
		for (Entry<String, Edge> entry : ((HashMap<String, Edge>)World.getWorld().getGraph().getEdges()).entrySet()) {
			if (entry.getValue().equals(this)) {
				ret = entry.getKey();
				break;
			}
		}
		
		return ret;
	}
	
	
	public boolean isPointOver(Vector2 xy) {
		
		Boolean res = false;
		

		
		Polygon4 square = new Polygon4(getEdgeSquare(), 0);

		res = square.isPointInside(xy);
		
		return res;
	}
	
	public float[] getEdgeSquare() {
		
		final int width = 10;
		
		
		float[] vertices = new float[8];
		
		//Получаем координаты грани
		Vector2 st = new Vector2 (World.getWorld().getGraph().nodes.get(nodesToLink.get(0)).getX(), World.getWorld().getGraph().nodes.get(nodesToLink.get(0)).getY());
		Vector2 fn = new Vector2 (World.getWorld().getGraph().nodes.get(nodesToLink.get(1)).getX(), World.getWorld().getGraph().nodes.get(nodesToLink.get(1)).getY());		
		//Результирующий вектор
		Vector2 edgeVector = MathGame.lineToVector(st, fn); //базовый вектор
		
		//Теперь нам нужно найти нормаль к вектору. Координаты вектора-нормали ищется по формулам:
		//x = (-y*y')/x', y = (-x*x')/y'. Тут x' и y' - координаты базового вектора
		//x или y выбираем любой, например y = 1.
		
		float normY;
		
		if (edgeVector.x >= 0)//Проверяем, что векто рв правой части графика - тогда Y положительный и наоборот
		normY = 1;
		else 
		normY = -1;
		
		float normX = -(normY * edgeVector.y)/edgeVector.x;//X компонента вектора нормали
		Vector2 resNormal1 = new Vector2(normX, normY); //Y компоненту мы уже определили
		
		
		resNormal1.nor(); //нормализуем вектор нормали
		
		resNormal1.x = resNormal1.x*width;// умножаем на ширину нашего прямоугольника
		resNormal1.y = resNormal1.y*width;
		
		
		Vector2 resNormal2 = new Vector2(resNormal1); //Назначаем оппозитный вектор к нормали, чтобы была вотрая сторона прямоугольника		
		resNormal2.x = -resNormal2.x;//отражаем оппозитный вектор
		resNormal2.y = -resNormal2.y;
		
		
		vertices[0] = resNormal1.x + st.x;
		vertices[1] = resNormal1.y + st.y;
		vertices[2] = resNormal2.x + st.x;
		vertices[3] = resNormal2.y + st.y;
		vertices[4] = resNormal2.x + fn.x;
		vertices[5] = resNormal2.y + fn.y;
		vertices[6] = resNormal1.x + fn.x;
		vertices[7] = resNormal1.y + fn.y;
		
		
		
		return vertices;
		
	}
	
}
