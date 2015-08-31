package network.packets;

import java.io.Serializable;

/**
 *
 * @author Castor
 */
public class ConnectPacketServer extends Packet implements Serializable {

	private String servername;
	private static final long serialVersionUID = 3L;

	/**
	 *
	 * @param servername
	 * @param origin
	 * @param target
	 */
	public ConnectPacketServer(String servername, String origin, String target) {
		super(origin, target);
		this.servername = servername;
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

	@Override
	public String toString() {
		return "ConnectPacketServer{" + "servername=" + servername + '}';
	}

}
