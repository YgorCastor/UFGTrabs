package applet.controller.tcpclient;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import applet.controller.AppletBoletosController;
import applet.model.keys.SSLKeys;
import network.packets.*;

/**
 * @author Ygor Castor
 * @since 16/12/2014
 * @version 1.1
 * 
 * Socket para comunicação com o servidor de autenticação
 */
public class SocketClient implements HandshakeCompletedListener, Runnable {

	private String port;
	private String serverIP;
	private SSLSocket socket;
	private String servername;
	private ObjectInputStream In;
	private ObjectOutputStream Out;
	private Boolean release;

	/**
	 *
	 * @param lw
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public SocketClient() throws IOException, NoSuchAlgorithmException {
		
		setRelease(false);

		SSLKeys key = new SSLKeys();

		this.serverIP = AppletBoletosController.appletBoleto.getServerIP();
		this.port = AppletBoletosController.appletBoleto.getServerPort();

		System.setProperty("javax.net.ssl.trustStore", key.getPrivatekey().getPath().replaceAll("%20", " "));
		System.setProperty("javax.net.ssl.trustStorePassword", key.getPassword());

		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		socket = (SSLSocket) factory.createSocket(InetAddress.getByName(serverIP), Integer.parseInt(port));
		socket.setSoTimeout(15000);
		socket.setEnabledProtocols(new String[] { "SSLv3" });
		((SSLSocket) socket).addHandshakeCompletedListener(this);

		Out = new ObjectOutputStream(socket.getOutputStream());
		Out.flush();
		In = new ObjectInputStream(socket.getInputStream());

	}

	@Override
	public void run() {
		while (!getRelease()) {
			try {

				Object pkt = In.readObject();

				//System.out.println("Incoming: " + pkt);

				if (pkt instanceof LoginPacketServer) {

					LoginPacketServer lp = (LoginPacketServer) pkt;

					if (lp.getAnswer() == 0) {

						System.out.println("[Servidor] : Autenticação realizada com sucesso!");
						AppletBoletosController.appletBoleto.setAuthenticated(true);
                        send(new SignoutPacketClient("CLIENT","SERVER") );
                        closeSocket();
                        
					} else if (lp.getAnswer() == 1) {

						System.out.println("[Servidor][Aviso] : A Sua assinatura está vencida!");
						System.out.println("[Servidor] : Autenticação realizada com sucesso!");
						AppletBoletosController.appletBoleto.setAuthenticated(true);
						send(new SignoutPacketClient("CLIENT","SERVER") );
						closeSocket();

					} else if (lp.getAnswer() == 2) {
						System.out.println("[Servidor][Aviso] : A Sua assinatura está vencida e já se passou o período de aviso!");
						System.out.println("[Servidor][Aviso] : Autenticação não realizada!");
						AppletBoletosController.appletBoleto.setAuthenticated(false);
						send(new SignoutPacketClient("CLIENT","SERVER") );
						closeSocket();

					}

				} else if (pkt instanceof ConnectPacketServer) {

					ConnectPacketServer sp = (ConnectPacketServer) pkt;
					setServername(sp.getServername());
					System.out.println("[Servidor] : O Servidor " + sp.getServername() + " respondeu com sucesso!\n");
                    send(new LoginPacketClient("blablabla", "CLIENTE", "SERVIDOR"));
				} 

			} catch (Exception ex) {
				setRelease(true);
				ex.printStackTrace();

			}
		}
	}

	/**
	 *
	 * @param pkt
	 */
	public void send(Packet pkt) {
		try {
			Out.writeObject(pkt);
			Out.flush();
			//System.out.println("Outgoing : " + pkt.toString());
		} catch (IOException ex) {
			System.out.println("Exception SocketClient send()");
		}
	}

	/**
	 *
	 * @param event
	 */
	@Override
	public void handshakeCompleted(HandshakeCompletedEvent event) {

		System.out.println("Handshake Completado\n");

		try {
			System.out.println("Suíte de Cífras: " + event.getCipherSuite() + "\n");

			SSLSession session = event.getSession();
			System.out.println("Protocolos: " + session.getProtocol() + "\n");
			System.out.println("Host: " + session.getPeerHost() + "\n");

			java.security.cert.Certificate[] certs = event.getPeerCertificates();
			for (int i = 0; i < certs.length; i++) {
				if (!(certs[i] instanceof java.security.cert.X509Certificate))
					continue;
				java.security.cert.X509Certificate cert = (java.security.cert.X509Certificate) certs[i];
				System.out.println("Cert #" + i + ": " + cert.getSubjectDN().getName() + "\n");
			}
		} catch (Exception e) {
			System.err.println("handshakeCompleted: " + e);
		}
	}
	
	private void closeSocket() throws IOException{
		
		setRelease(true);
		getSocket().close();
		
	}

	/**
	 *
	 * @return
	 */
	public String getPort() {
		return port;
	}

	/**
	 *
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 *
	 * @return
	 */
	public String getServerIP() {
		return serverIP;
	}

	/**
	 *
	 * @param serverIP
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 *
	 * @return
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 *
	 * @param socket
	 */
	public void setSocket(Socket socket) {
		this.socket = (SSLSocket) socket;
	}

	/**
	 *
	 * @return
	 */
	public String getServername() {
		return servername;
	}

	/**
	 *
	 * @param servername
	 */
	public void setServername(String servername) {
		this.servername = servername;
	}

	/**
	 * @return the release
	 */
	public Boolean getRelease() {
		return release;
	}

	/**
	 * @param release the release to set
	 */
	public void setRelease(Boolean release) {
		this.release = release;
	}
}
