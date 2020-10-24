package ru.mendel.apps.zeropatient.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.mendel.apps.zeropatient.ZeroPatientGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

		SaveCsvData saveCsvData = new SaveCsvData();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 700;
		config.height = 700;
		new LwjglApplication(new ZeroPatientGame(saveCsvData), config);
	}
}
