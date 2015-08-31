package network.packets;

import java.io.Serializable;

/**
 *
 * @author Castor
 */
public class LoginPacketClient extends Packet implements Serializable {

	private String password;
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param password
	 * @param origin
	 * @param target
	 */
	public LoginPacketClient(String password, String origin, String target) {
		super(origin, target);
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginPacketClient{" + "password=" + password + '}';
	}

	/**
	 *
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 *
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
