package applet.model.keys;

import java.net.URL;

public class SSLKeys {

	private URL privatekey;
	private String password;

	public SSLKeys() {

		this.privatekey = getClass().getResource("/conf/clientkeystore.jks");
		this.password = "123456";

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public URL getPrivatekey() {
		return privatekey;
	}

	public void setPrivatekey(URL privatekey) {
		this.privatekey = privatekey;
	}

}
