package com.github.break27.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.break27.TodoGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
                System.setProperty("file.encoding", "UTF-8");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TodoGame(), config);
	}
}
