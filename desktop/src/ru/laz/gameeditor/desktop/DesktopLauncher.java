package ru.laz.gameeditor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.laz.gameeditor.AEditor;
import ru.laz.gameeditor.ui.UI;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = UI.SCREENH;
		config.width = UI.SCREENW;
		new LwjglApplication(new AEditor(), config);
	}
}
