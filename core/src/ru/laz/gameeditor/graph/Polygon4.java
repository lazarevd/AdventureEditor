package ru.laz.gameeditor.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.laz.gameeditor.render.RenderShapes;
import ru.laz.gameeditor.render.RenderShapes.Colour;
import ru.laz.gameeditor.world.World;

public class Polygon4 extends Polygon {
	
	
	private transient Array<String> pointsOnEdges;
	public enum DrawStat {NORMAL, SELECTED, HIGHLIGHT};
	private float renderScale;
	public transient DrawStat drawStat;


	public Polygon4() {
		pointsOnEdges = new Array<String>();
		drawStat = DrawStat.NORMAL;
	}
	
	public Polygon4(float renderScale) {
		super();
	}
	
	
	
	public Polygon4(float[] vertices, float renderScale) {
		this(renderScale);
		this.setVertices(vertices);
	}
	
	
	
	
	@Override
	public void setVertices (float[] vertices) {
		
		if (vertices.length == 8) {
		super.setVertices(vertices);
		} else throw new IllegalArgumentException("polygons must contain only 4 points.");

	}


	public float getRenderScale() {
		return renderScale;
	}

	public void setRenderScale(float renderScale) {
		this.renderScale = renderScale;
	}
	
	
	public String getThisId() {
		String ret = null;
		
		for (Entry<String, Polygon4> entry : World.getWorld().getGraph().getPolygons().entrySet()) {
			if (entry.getValue().equals(this)) {
				ret = entry.getKey();
				break;
			}
		}
		
		return ret;
	}

	
	public String getNewPOEName() {
		String newName = "poe";
		
		newName += (getLastPOENameNumber() + 1);
		
	
		
		return newName;
	}
	
	
	private static int getLastPOENameNumber() {
		
		int curInt = 0;
		String regExp = "[0-9]+$";
		Pattern p = Pattern.compile(regExp);
		Matcher m;
		
			
			for (String poe : World.getWorld().getGraph().getPOEs().keySet()) {
				m = p.matcher((String)poe);
				
				if (m.find()) {
				
				if(Integer.parseInt(m.group()) > curInt) {
					curInt = Integer.parseInt(m.group());
				}
				}
				
				
			}
			
			
		return curInt;
	}
	
	
	
	
	public void addPointOnEdge(int edge, float k) {
		String newName = getNewPOEName();
		
		Gdx.app.log("addPointOnEdge", newName + " " + getThisId());
		World.getWorld().getGraph().getPOEs().put(newName, new PointOnEdge(getThisId(), edge, k));
		this.pointsOnEdges.add(newName);
	}
	
	
	public void addPointOnEdge(int edge, float k, String name) {
		World.getWorld().getGraph().getPOEs().put(name, new PointOnEdge(getThisId(), edge, k));
		this.pointsOnEdges.add(name);
	}
	
	public boolean removePointOnEdge (String name) {
		boolean ret = false;
		
		pointsOnEdges.removeValue(name, true);
		World.getWorld().getGraph().getPOEs().remove(name);	
		return ret;
	}
	
	public Array<String> getPointsOnEdge() {
		return this.pointsOnEdges;
	}
	
	
	public String addPointOnClosestEdge(float x, float y) {
		
		float k = 0;
		String poeId;
		
		int edge = getClosestEdge(new Vector2(x,y));
		Vector2 pxy = getClosestEdgePoint(new Vector2(x,y));
		
		
		Vector2 ab = new Vector2 (getEdge(edge)[1].x - getEdge(edge)[0].x, getEdge(edge)[1].y - getEdge(edge)[0].y);	
		Vector2 ap = new Vector2 (pxy.x - getEdge(edge)[0].x, pxy.y - getEdge(edge)[0].y);
		

		k = ap.len()/ab.len();
		poeId = getNewPOEName();
		
		
		World.getWorld().getGraph().getPOEs().put(poeId, new PointOnEdge(getThisId(), edge, k));
		return poeId;
	}
	
	
	
	public Vector2 getVertexXY(int i) { //Returns vertex. Vertex not an object, but
									//we return only x and y components of the float array of vertexes.
									//This made only for compatibility to libgdx polygon
		Vector2 vertexXY = new Vector2();
		
			vertexXY.x = this.getVertices()[getVertexId(i)[0]];
			vertexXY.y = this.getVertices()[getVertexId(i)[1]];
		
		return vertexXY;
		
	}
	
	
	public void setVertexXY (int vertex, float x, float y) {
		
		float[] vertices = this.getVertices();
		
		vertices[getVertexId(vertex)[0]] = x;
		vertices[getVertexId(vertex)[1]] = y;
		
		this.setVertices(vertices);
		
	}
	
	
	public int[] getVertexId(int i) {
		
		int[] ids = new int[2];
		
		switch (i) {
		case 1:
			ids[0] = 0;
			ids[1] = 1;
			break;
		case 2:
			ids[0] = 2;
			ids[1] = 3;
			break;
		case 3:
			ids[0] = 4;
			ids[1] = 5;
			break;
		case 4:
			ids[0] = 6;
			ids[1] = 7;
			break;
		}
		
		
		return ids;
		
	}
	
	
	
	
	public Vector2[] getEdge(int j) {
		Vector2[] edge = new Vector2[2];
		edge[0] = new Vector2(0,0);
		edge[1] = new Vector2(0,0);//Have to initialize
		
		
		switch (j) {
		case 1:
			edge[0].x = this.getVertices()[0];
			edge[0].y = this.getVertices()[1];
			edge[1].x = this.getVertices()[2];
			edge[1].y = this.getVertices()[3];
			break;
		case 2:
			edge[0].x = this.getVertices()[2];
			edge[0].y = this.getVertices()[3];
			edge[1].x = this.getVertices()[4];
			edge[1].y = this.getVertices()[5];
			break;
		case 3:
			edge[0].x = this.getVertices()[4];
			edge[0].y = this.getVertices()[5];
			edge[1].x = this.getVertices()[6];
			edge[1].y = this.getVertices()[7];
			break;
		case 4:
			edge[0].x = this.getVertices()[6];
			edge[0].y = this.getVertices()[7];
			edge[1].x = this.getVertices()[0];
			edge[1].y = this.getVertices()[1];
			break;
		
		}
		
		return edge;
		
	}
	
	
	public Vector2 getEdgeNormal(int edgeId) {//возвращает вектор нормали
		
		Vector2[] edge = getEdge(edgeId); 
		
		if (edgeId == 1) {
			RenderShapes.drawLine(edge[0], edge[1], Colour.RED);
		}
		
		Vector2 AB = new Vector2(edge[1].x-edge[0].x, edge[1].y-edge[0].y);
		
		Vector2 normal = new Vector2(AB.y, -AB.x); //Поворачиваем на 90 град по часовой стрелке
		
		normal.nor(); 
		
		return normal;
	}
	
	public void drawNormal(int edgeId) {
		
		Vector2[] edge = getEdge(edgeId); 
		
		Vector2 normal = getEdgeNormal(edgeId);
		
		normal.x = normal.x * 20 + edge[0].x;
		normal.y = normal.y * 20 + edge[0].y;
		
		if(edgeId == 1) {
		RenderShapes.drawLine(edge[0], normal, Colour.RED);	
		} else {
		
		RenderShapes.drawLine(edge[0], normal, Colour.GREEN);
		}
		
	}
	
	
	public float getDistanceToVertex(int vertex, float x, float y) {
		
		Vector2 vertexXY = new Vector2();
		
		vertexXY = getVertexXY(vertex);
		
		float dist;
		
		Vector2 vec1 = new Vector2(vertexXY.x,vertexXY.y);
		dist = vec1.dst(x, y);	
		return dist;
	}
	
	

	
	
	//for test
	
	public boolean isPointInside(Vector2 xy) {
		boolean proj = false;
		
		int closestEdge = getClosestEdge(xy);
		
		Vector2 ab = MathGame.lineToVector(getEdge(closestEdge));//вектор грани
		Vector2 ap = MathGame.lineToVector(getEdge(closestEdge)[0], xy);//вектор от начала грани до точки

		/*Проверка пренадлежности делается с помощью вычисления векторного произведения 
		 *между вектором образованым ближайшей гранью (ab) и вектором между курсором и началом ближайшей грани (ap)
		 *если произведение меньше 0, то точка принадлежит полигону.
		 *Для полигонов с острыми углами еще нужна проверка на длину. ap не должно быть > ab.  
		 *
		 *
		 *Checking by using cross product between vector creating by nearest edge (ab) and 
		 *vector specified by start point of ab and xy coordinates. Negative value of cross means pont
		 *is inside poly. Length check is for non-square polys.
		 */
		
		float cross = ap.crs(ab);
		
		if(cross < 0 && ab.len() >= ap.len()) { //cross - векторное умножение - проверка тупой угол или острый
			proj = true;
		} else {
			proj = false;	
		}

		return proj;
	}
	
	
	public int getCornerByEdges(int edge1, int edge2) {
		int ret = 0;
		//переделать. сделать просто проверку на 1 и на size - особые случае, далее i, i+1
		if ((edge1 == 1 && edge2 == 2) || (edge1 == 2 && edge2 == 1)) ret = 2; 
		else if ((edge1 == 2 && edge2 == 3) || (edge1 == 3 && edge2 == 2)) ret = 3;
		else if ((edge1 == 3 && edge2 == 4) || (edge1 == 4 && edge2 == 3)) ret = 4;
		else if ((edge1 == 4 && edge2 == 1) || (edge1 == 1 && edge2 == 4)) ret = 1;
		return ret;
	}
	
	public int[] getEdgesByCorner(int corner) {
		int[] ret = new int[2];
		
		switch(corner) {
		case 1: ret[0] = 4;
				ret[1] = 1;
				break;
				
		case 2: ret[0] = 1;
				ret[1] = 2;
				break;
				
		case 3: ret[0] = 2;
				ret[1] = 3;
				break;
				
		case 4: ret[0] = 3;
				ret[1] = 4;
				break;
		
		}
		
		
		//Gdx.app.log("edge by corn", ret[0] + ": " + ret[1]);
		return ret;
	}
	
	
	
	public Vector2 getVectorProjection(Vector2 a, Vector2 b, Vector2 p) {
		
		Vector2 AP = new Vector2(p.x - a.x, p.y - a.y);//Определяем векторы и переносим их в начало координат
		Vector2 AB = new Vector2(b.x - a.x, b.y - a.y);//AB - вектор на который проецируем точку. AP - вектор откуда проецируем
		float scalarProj = AP.dot(AB)/AB.len();	//Скалаярная проекция
		
		Vector2 norm = new Vector2(AB);
		norm.nor();	//Нормализуем AB
		
		Vector2 vec = new Vector2(scalarProj*norm.x, scalarProj*norm.y); //Векторная проекция. Vector projection.
		return vec; 
	
	}

	
	
	public float getDistanceToLine(Vector2 a, Vector2 b, Vector2 p) {
		float dist = 0;
		
		
		
		return dist;
	}
	
	
	
	public Vector2 getProjectionPointOnLine(Vector2 a, Vector2 b, Vector2 p) {
		
		/*Для нахождения ближайшей точки P на прямой AB надо найти ее векторную проекцию
		 * вектора AP на вектор AB. Формула расчета: ((AP, AB)/|AB|)/(AB/|AB|),
		 * где (AP, AB) - скалярное произведение, |AB| - длина вектора, AB/|AB| - нормализованный
		 * вектор на который делается проекция. При этом (AP, AB)/|AB| - скалярная проекция, 
		 * а AB/|AB| - нормализованный вектор AB.
		 * 
		 * Затем проекцию умножаем на единичный вектор, образованный от AB.
		 * 
		 * 
		 * To find closest point use projection. Full formula ((AP, AB)/|AB|)/(AB/|AB|). Where
		 * (AP, AB) dot product, (AP, AB)/|AB| - scalar projection, AB/|AB| - normalized AB.
		 * First find scalar proj then multiply it with normilized vector. 
		 */
		
		
		Vector2 fin;
		//Vector2 AP = new Vector2(p.x - a.x, p.y - a.y);//Определяем векторы и переносим их в начало координат
		Vector2 AB = new Vector2(b.x - a.x, b.y - a.y);//AB - вектор на который проецируем точку. AP - вектор откуда проецируем
		Vector2 proj = getVectorProjection(a, b, p);
		
	
		if (proj.len() >= AB.len() && proj.dot(AB) > 0) {//Ограничение по максимуму длины
			proj.set(AB);			//Проверяем, что угол между векторами острый (скалярное произв > 0). Это важное условие, иначе точка скачет от одного края к другому при увеличении дистанции						
		}
	
		if (proj.dot(AB) <= 0) {
			proj.x= 0;
			proj.y= 0;
		}
		
		
		
		//Gdx.app.log("scalarProj", scalarProj + "");
		fin = new Vector2(proj.x + a.x, proj.y + a.y);
		
	
		
		int offset = 3;		
		//RenderShapes.drawLine(new Vector2(a.x + offset, a.y + offset), new Vector2(AB.x + a.x + offset, AB.y + a.y + offset), Colour.RED);
		//RenderShapes.drawLine(new Vector2(a.x + offset, a.y + offset), new Vector2(AP.x + a.x + offset, AP.y + a.y + offset), Colour.GREEN);
		//RenderShapes.drawPoint(new Vector2(AB.x + a.x + offset, AB.y + a.y + offset), 7, Colour.RED);
		//RenderShapes.drawPoint(new Vector2(AP.x + a.x + offset, AP.y + a.y + offset), 7, Colour.GREEN);
		//RenderShapes.drawLine(new Vector2(fin.x, fin.y), new Vector2(AP.x + a.x, AP.y + a.y), Colour.WHITE);
		RenderShapes.drawPoint(new Vector2(fin.x + offset, fin.y + offset), 2, Colour.WHITE);
		
		
		//Итоговый вектор стоится путем умножения числа скалярной проекции на нормализованный
		//вектор AB. В завершение переносим векторы в первоначальное положение.
		
		
		
		//RenderShapes.drawPoint(fin, 9, Colour.BLUE);
		return fin;
		
	}


	
	public boolean hasPointOnLine(Vector2 a, Vector2 b, Vector2 p) {
		
		/*Для нахождения ближайшей точки P на прямой AB надо найти ее векторную проекцию
		 * Строится так же как и getProjectionPointOnLine, но в случае если точка за пределами проекции - возвращает Null
		 */
		
		
		boolean fin;
		//Vector2 AP = new Vector2(p.x - a.x, p.y - a.y);//Определяем векторы и переносим их в начало координат
		Vector2 AB = new Vector2(b.x - a.x, b.y - a.y);//AB - вектор на который проецируем точку. AP - вектор откуда проецируем
		Vector2 proj = getVectorProjection(a, b, p);
		
	
		if ((proj.len() >= AB.len() && proj.dot(AB) > 0) || (proj.dot(AB) <= 0)) {//Ограничение по максимуму длины
			fin = false;			//Проверяем, что угол между векторами острый (скалярное произв > 0). Это важное условие, иначе точка скачет от одного края к другому при увеличении дистанции						
		} else 
		{
		fin = true;
		}
		return fin;
		
	}
	
	

	public int getClosestEdge(Vector2 xy) {
		//Реально гемор. 
		/*Процедура возвращает номер ближайшей ВНЕШНЕЙ грани.
		 * В данном случае у нас 2 варианта: 
		 * 1. Либо курсор находится рядом с гранью, и от курсора можно опустить перпендикуляр на ближайшую грань.
		 * 2. Либо курсор находится на углу.
		 * 
		 * в 1 случае при измерении расстояний 
		 */
		
		

		

		
		
		HashMap<Integer,Float> distances = new HashMap<Integer,Float>();
				
		for(int i = 1; i <=4; i++) {//заполняем расстояния
			/*
			RenderShapes.drawLine(getProjectionPointOnLine(getEdge(1)[0], getEdge(1)[1], xy), xy, Colour.RED);
			RenderShapes.drawLine(getProjectionPointOnLine(getEdge(2)[0], getEdge(2)[1], xy), xy, Colour.BLUE);
			RenderShapes.drawLine(getProjectionPointOnLine(getEdge(3)[0], getEdge(3)[1], xy), xy, Colour.GREEN);
			RenderShapes.drawLine(getProjectionPointOnLine(getEdge(4)[0], getEdge(4)[1], xy), xy, Colour.YELLOW);
		*/
			
		float theDistance = xy.dst(getProjectionPointOnLine(getEdge(i)[0], getEdge(i)[1], xy));
		distances.put(i, theDistance);//Расстояние до прекции на линию
		//Gdx.app.log("dist", "i: " + i + ", dist " + theDistance +", cross: " + normals[i-1].crs(lookVector) +", dot: " + normals[i-1].dot(lookVector));
		//Gdx.app.log("dist", "i: " + i + ", dist " + theDistance);
		//RenderShapes.drawLine(getEdge(i)[0], xy, Colour.WHITE);
		}

		Array<Integer> duplicateLength = new Array<Integer>();	//Находим одинаковые расстояния в карте расстояний до вертексов. Если таковые есть, значит курсор у угла, но еще надо проверить, что это не равноудаленые точки на противоположных гранях
		for(Entry<Integer, Float> entry : distances.entrySet()) {
			for(Entry<Integer, Float> entry2 : distances.entrySet()) {//Используем 2 вложенных цикла, чтобы сравнить каждую величину
				if(entry.getValue().equals(entry2.getValue()) && !entry.getKey().equals(entry2.getKey())) {//дополнительно вводим проверку на то, что не делаем сравнение элемента с самим собой
					duplicateLength.add(entry.getKey());
				}
			}
		}

		boolean isInCorner = false;			//Выясняем какой у нас случай
											

			if(duplicateLength.size == 2) {
				if(getCornerByEdges(duplicateLength.get(0), duplicateLength.get(1)) != 0) //Если есть 2 одинково удаленных вертекса, то еще проверяем - угол ли это.
				isInCorner = true;
		}
		
		//Gdx.app.log("is corner", isInCorner + "");
		
		
		int edge = 1;
		if(isInCorner == false) {//1 случай (рядом с гранью)

			float minDist = distances.get(1);
			
			for (int i = 1; i <= 4; i++) {
				if(distances.get(i) < minDist) {
					edge = i;
					minDist = distances.get(i);
				}
				Vector2 xyN = MathGame.lineToVector(getEdge(i)[0], xy);
	 			xyN.nor();
			//Gdx.app.log("edge cross", "i: " + i + "cross: " + MathGame.lineToVector(getEdge(i)).nor().dot(xyN) + ", min dist: " + minDist);

			
			}
			
		} else {//2 случай - на углу
			
			HashMap<Integer, Float> dot = new HashMap<Integer, Float>();//Массив скалярных перемножений вектора курсора и нормалей

			
			//Определяем угол по записям в массиве дублей расстояний duplicateLength
			//На самом деле просто упорядочиваем набор граней
			
			int[] edges = getEdgesByCorner(getCornerByEdges(duplicateLength.get(0), duplicateLength.get(1)));
			
			int edge1 = edges[0];
			int edge2 = edges[1];
			
			//Gdx.app.log("ordered edges", edges[0] + ": " + edges[1]);
			 
			Vector2 xyN = MathGame.lineToVector(getEdge(edge2)[0], xy);
 			xyN.nor();
			dot.put(edge1, getEdgeNormal(edge1).dot(xyN));
			dot.put(edge2, getEdgeNormal(edge2).dot(xyN));

 			
 			
			if(dot.get(edge1) > dot.get(edge2)) {
				edge = edge1;
			} else {
				edge = edge2;
			}
			
			//Gdx.app.log("dots", dot.toString());

		//Gdx.app.log("dupl leng", duplicateLength.toString() + ", dots: " + dot.toString());
		
		}
		
		
		
		
		
		
		/*

		
		lookVector = MathGame.lineToVector(getEdge(i)[0], xy);
		lookVector.nor();
			
		normals[i-1] = getEdgeNormal(i);
		
		
		*/
		
		Gdx.app.log("Fin edge", edge+"");

		return edge;
	}
	
	
	public float getEdgeCursorRatio(Vector2 xy, int edge) {
		
		float curRatio;
		Vector2 xyN = MathGame.lineToVector(getEdge(edge)[0], xy);
		xyN.nor();
		curRatio = getEdgeNormal(edge).dot(xyN);
		
		/*
		int curEdge = 1;
		for (int i = 1; i <= 4; i++) {
		if(dot.get(curEdge) > dot.get(i)) 
			curEdge = i;
		}
		*/
	
		return curRatio;
}
		
	
	
	public Vector2 getClosestEdgePoint(Vector2 xy) {
	
		int edge = getClosestEdge(xy);
		
		return getProjectionPointOnLine(getEdge(edge)[0], getEdge(edge)[1] , xy);
	}

	

}
