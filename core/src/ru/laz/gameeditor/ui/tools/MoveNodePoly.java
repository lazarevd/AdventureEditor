package ru.laz.gameeditor.ui.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Map;

import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.graph.Polygon4;
import ru.laz.gameeditor.render.RenderShapes;
import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.world.World;

/**
 * Created by Dmitry Lazarev on 17.09.2017.
 */

public class MoveNodePoly implements Tool {

    private ToolStatus toolStat;


    @Override
    public ToolStatus getStatus() {
        return toolStat;
    }

    @Override
    public void setStatus(ToolStatus toolStatus) {
        toolStat = toolStatus;
    }

    @Override
    public void prepare() {
        toolStat = ToolStatus.PROCESSING;
    }

    @Override
    public void select() {
//Пропускаем, двигаем по одному все
    }

    @Override
    public void process() {

        if (UI.getUI().NODE == true && UI.getUI().POLY ==true) {
            movePolygonVertex();
            moveNode();
        } else if (UI.getUI().NODE == true) {
            moveNode();
        } else if (UI.getUI().POLY ==true) {
            movePolygonVertex();
        }

    }

    @Override
    public void finish() {
        toolStat = ToolStatus.FINISHED;
        UI.getUI().setToolDisplayStatus(UI.ToolDisplayStatus.NORMAL);
    }




    public void movePolygonVertex() {

        Vector2 touchPos = new Vector2();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        if(Gdx.input.isTouched()) {

            if (UI.getUI().POLY == true)  {
                Array<Polygon4> polys = new Array<Polygon4>();

                for (Map.Entry<String, Polygon4> entry : World.getWorld().getGraph().getPolygons().entrySet()) {
                    polys.add(entry.getValue());
                }
                Polygon4 curPolygon, movePolygon;
                int moveVertex;  //IDs of vertex in poly
                int curVertex;
                float curDistance;
                float distance;
                float nodDistance;

                if (polys.size > 0 ) {

                    curPolygon = polys.get(polys.size-1);// Test only
                    movePolygon = curPolygon;
                    moveVertex = 1;
                    nodDistance = curPolygon.getDistanceToVertex(1, UI.getCursor().x, UI.getCursor().y);

                    curDistance = 0;
                    for (Polygon4 pol : polys) {//Loop polys

                        curVertex = 1;
                        curDistance = pol.getDistanceToVertex(1, UI.getCursor().x, UI.getCursor().y);


                        for(int i = 1; i <= 4; i++) {
                            distance = pol.getDistanceToVertex(i, UI.getCursor().x, UI.getCursor().y);
                            if(distance < curDistance) {
                                curVertex = i;
                                curDistance = distance;
                            }
                        }
                        if(curDistance < nodDistance) {
                            curPolygon = pol;
                            nodDistance = curDistance;
                            moveVertex = curVertex;

                        }
                        movePolygon = curPolygon;
                    }

                    if (movePolygon.getDistanceToVertex(moveVertex, UI.getCursor().x, UI.getCursor().y) < 20) {
                        movePolygon.setVertexXY(moveVertex,UI.getCursor().x, UI.getCursor().y);
                        RenderShapes.drawPoint(UI.getCursor(), 5, RenderShapes.Colour.RED);
                    }
                }

            }
        }

    }



    public void moveNode() {
        if(Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            if (UI.getUI().NODE == true)    {
                Node moveNode;
                Array<Node> nodes = new Array<Node>();

                for (Map.Entry<String,Node> entry : World.getWorld().getGraph().getNodes().entrySet()) {
                    nodes.add(entry.getValue());
                }

                if (nodes.size > 0) {
                    moveNode = nodes.get(nodes.size-1);
                    for (Node nod : nodes) {
                        if (nod.getDistance(touchPos.x, UI.getCursor().y) < moveNode.getDistance(UI.getCursor().x, UI.getCursor().y)) {
                            moveNode = nod;
                        }
                    }
                    if (moveNode.getDistance(UI.getCursor().x, UI.getCursor().y) < 20) {
                        moveNode.setX(UI.getCursor().x);
                        moveNode.setY(UI.getCursor().y);
                    }

                }

            }





        }
    }


}
