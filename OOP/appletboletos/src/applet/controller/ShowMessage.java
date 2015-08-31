package applet.controller;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import applet.view.AppletBoletosMain;

public class ShowMessage {

	public static void info(String msg) {

		Date data = new Date();
		String dataS = new SimpleDateFormat("H:mm").format(data);

		AppletBoletosMain.appendToPane(dataS + " [Informação] : " + msg + "\n", Color.BLACK);

	}
	
	public static void warning(String msg) {

		Date data = new Date();
		String dataS = new SimpleDateFormat("H:mm").format(data);

		AppletBoletosMain.appendToPane(dataS + " [Alerta] : " + msg + "\n" , Color.ORANGE);

	}
	
	public static void error(String msg) {

		Date data = new Date();
		String dataS = new SimpleDateFormat("H:mm").format(data);

		AppletBoletosMain.appendToPane(dataS + " [Erro] : " + msg + "\n", Color.RED);

	}
	
	public static void fatalError(String msg) {

		Date data = new Date();
		String dataS = new SimpleDateFormat("H:mm").format(data);

		AppletBoletosMain.appendToPane(dataS + " [Erro Fatal] : " + msg + "\n", Color.RED);

	}

}
