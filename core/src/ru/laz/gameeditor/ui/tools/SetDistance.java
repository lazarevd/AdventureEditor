package ru.laz.gameeditor.ui.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import ru.laz.gameeditor.graph.Node;
import ru.laz.gameeditor.graph.Polygon4;
import ru.laz.gameeditor.ui.UI;
import ru.laz.gameeditor.ui.UI.ToolDisplayStatus;

/**
 * Created by Dmitry Lazarev on 07.08.2017.
 */

public class SetDistance implements Tool  {

    private ToolStatus toolStat;
    private Node workNode;
    private Polygon4 workPoly;

    private String dialogBuffer = "";
    private boolean finishEnter;


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
        Gdx.app.log("Prepare ", dialogBuffer);
        toolStat = ToolStatus.SELECTING;
        UI.getUI().NODE = true;
        UI.getUI().setToolDisplayStatus(ToolDisplayStatus.HOVERNODE);
    }

    @Override
    public void select() {
        UI.getUI().hideNodeDistanceDialog();
        Gdx.app.log("Select ", dialogBuffer);
        Vector2 inputXY = new Vector2(UI.getCursor().x,UI.getCursor().y);
        if(setSelectedNode(inputXY)) {
            UI.getUI().showNodeDistanceDialog(workNode, this);
            toolStat = ToolStatus.PROCESSING;
            UI.getUI().setToolDisplayStatus(ToolDisplayStatus.NORMAL);
            finishEnter = false;
        }
    }

    @Override
    public void process() {
        if(workNode != null) {
            Gdx.app.log("Process ", dialogBuffer);
            if(finishEnter) {
                float dist = 0;
                try {
                    dist = Float.parseFloat(dialogBuffer);
                } catch (NumberFormatException nex) {
                    toolStat = ToolStatus.FINISHED;
                } catch (NullPointerException npe ) {
                    toolStat = ToolStatus.FINISHED;
                }
                workNode.setRenderScale(dist);
                prepare();
            }
        } else {
            toolStat = ToolStatus.FINISHED;
        }
    }

    @Override
    public void finish() {
        Gdx.app.log("Finish ", "SetDist");
        UI.getUI().setToolDisplayStatus(ToolDisplayStatus.NORMAL);
        UI.getUI().hideNodeDistanceDialog();
    }


    private boolean setSelectedNode(Vector2 inputXY)  {
        boolean added = false;
        Node retNode = null;
        if (Gdx.input.justTouched()) {
            retNode = Node.getNearestNode(inputXY);
            if(retNode.getDistance(inputXY.x, inputXY.y) < 10) {
                workNode = retNode;
                added = true;
            }
        }
        Gdx.app.log("SetDistance tool add: ", workNode + " " + toolStat);
        return added;
    }

    public void setDialogBuffer(String dialogBuffer) {
        this.dialogBuffer = dialogBuffer;
    }

    public void setFinishEnter(boolean finishEnter) {
        this.finishEnter = finishEnter;
    }
}
