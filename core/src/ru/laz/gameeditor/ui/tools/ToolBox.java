package ru.laz.gameeditor.ui.tools;

import ru.laz.gameeditor.ui.tools.Tool.ToolStatus;

//Manages any tool behavior using necessary field "toolStatus". Works in main cycle in UI.

public class ToolBox {
	
	public static boolean processTool(Tool tool) { //returns true if finished
		
		boolean result = false;
		
		if(tool != null) {
			
			if(tool.getStatus() == ToolStatus.SELECTING) {
				tool.select();
			}
			
			else if(tool.getStatus() == ToolStatus.PROCESSING) {
				tool.process();
			}
			
			else if(tool.getStatus() == ToolStatus.FINISHED) {
				result = true;
			}
}
		return result;
		
	}
	
	
	public static void stopTool(Tool tool) {
		if(tool != null) {
		tool.setStatus(ToolStatus.FINISHED);
		}
	}
	
	

}
