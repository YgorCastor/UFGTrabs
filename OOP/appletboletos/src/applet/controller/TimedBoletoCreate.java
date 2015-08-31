package applet.controller;

import java.awt.TrayIcon;
import java.util.TimerTask;

import applet.model.MessagesList;
import applet.view.CreateTrayIcon;

public class TimedBoletoCreate extends TimerTask {

	public void run() {

		if (AppletBoletosController.appletBoleto.getAuthenticated() == null
				|| !AppletBoletosController.appletBoleto.getAuthenticated()) {
			CreateTrayIcon.trayIcon.displayMessage("Autenticação", MessagesList.AUTH_FAILURE.message,
					TrayIcon.MessageType.WARNING);
			return;
		}

		int cnt = AppletBoletosController.gerarBoletos();

		if (cnt != 0)
			CreateTrayIcon.trayIcon.displayMessage("Info", cnt + " Boletos Processados!", TrayIcon.MessageType.INFO);
		else
			CreateTrayIcon.trayIcon
					.displayMessage("Info", MessagesList.NO_BOL_FILES.message, TrayIcon.MessageType.INFO);

	}

}
