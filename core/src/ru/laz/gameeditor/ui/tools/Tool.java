package ru.laz.gameeditor.ui.tools;

import com.badlogic.gdx.math.Vector2;

public interface Tool {
	
	public enum ToolStatus {SELECTING, PROCESSING, FINISHED};
	
	public ToolStatus getStatus();
	
	public void setStatus(ToolStatus toolStatus);
	
	public void prepare();
	
	public void select();

	public void process();
	
	public void finish();
	
}
