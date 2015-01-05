package uk.zebcoding.battleships.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uk.zebcoding.battleships.Battleships;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 264;
		config.height = 548;
		config.resizable = false;
		new LwjglApplication(new Battleships(), config);
	}
}
