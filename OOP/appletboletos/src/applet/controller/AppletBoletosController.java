package applet.controller;

import java.awt.TrayIcon;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import network.packets.ConnectPacketClient;
import applet.controller.boleto.BoletoFactory;
import applet.controller.email.AppletMail;
import applet.controller.tcpclient.SocketClient;
import applet.controller.util.Util;
import applet.model.ErrorList;
import applet.model.MessagesList;
import applet.model.Options;
import applet.view.AppletBoletosMain;
import applet.view.CreateTrayIcon;

public class AppletBoletosController {

	private static SocketClient socket;
	public static Options appletBoleto;
	private static Thread authenticateThread;
	private static TimedBoletoCreate tbc = new TimedBoletoCreate();
	private static Timer timer = new Timer();
	private static AppletMail appletMail;

	public static void inicializar() throws InterruptedException {

		inicializarOpcoes();
		CreateTrayIcon.startTray();
		inicializarAutenticacao();
		TimedBoleto(appletBoleto.getAutoGenerate());
		appletMail = new AppletMail();

	}

	public static void inicializarOpcoes() {

		appletBoleto = new Options();
		appletBoleto.openOptionsFile();

	}

	public static void inicializarAutenticacao() throws InterruptedException {

		try {
			socket = new SocketClient();
			authenticateThread = new Thread(socket);
			authenticateThread.start();
			socket.send(new ConnectPacketClient("CLIENT", "SERVER"));
		} catch (NoSuchAlgorithmException | IOException e) {
			CreateTrayIcon.trayIcon.displayMessage("Falha de Conexão", ErrorList.CONN_FAILURE.msg,
					TrayIcon.MessageType.ERROR);
			e.printStackTrace();
			Thread.sleep(6000);
			System.exit(1);
		}

		authenticateThread.join();

	}

	public static Integer gerarBoletos() {

		if (AppletBoletosController.appletBoleto.getAuthenticated() == null
				|| !AppletBoletosController.appletBoleto.getAuthenticated()) {
			System.out.println(MessagesList.AUTH_FAILURE.message);
			return 0;
		}

		BoletosTuple btupla = new BoletosTuple();
        int count = 0;
		
		try {

			btupla = BoletoFactory.processBoletos();
			BoletoFactory.gerarPdfs(btupla);
			BoletoFactory.errBol(btupla);
			
			count = btupla.getObjetosBoletos().size();
			

			if (appletBoleto.getGeneratorBkp() && !btupla.getArquivos().isEmpty() ) {

				String bkpPath = AppletBoletosController.appletBoleto.getBoletosGeradosPath();
				String fullPath = bkpPath + "\\boletosEmitidos" + ".bin";

				btupla.getObjetosBoletos().stream().filter(boleto -> boleto.getErrosBoleto().isEmpty())
						.forEach(boleto -> {

							AppletBoletosMain.insertIntoHistory(boleto);
							Util.writeToBinary(fullPath, boleto, true);

						});

			}			

			btupla.getArquivos().parallelStream().forEach(boleto -> {

				try {
					Files.deleteIfExists(Paths.get(boleto.getAbsolutePath()));
				} catch (IOException io) {
					io.printStackTrace();
				}

			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return count;
	
	}

	public static void TimedBoleto(Boolean param) {

		if (param){
			timer.scheduleAtFixedRate(tbc, 0, TimeUnit.MINUTES.toMillis(  appletBoleto.getGenerateTimer() ) );
		}else{
			timer.cancel();
		}

	}

	/**
	 * @return the appletMail
	 */
	public static AppletMail getAppletMail() {
		return appletMail;
	}

	/**
	 * @param appletMail the appletMail to set
	 */
	public static void setAppletMail(AppletMail appletMail) {
		AppletBoletosController.appletMail = appletMail;
	}



}
