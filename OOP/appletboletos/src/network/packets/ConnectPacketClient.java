package network.packets;

import java.io.Serializable;

/**
 *
 * @author Castor
 */
public class ConnectPacketClient extends Packet implements Serializable {

	private static final long serialVersionUID = 2L;

	/**
	 *
	 * @param origin
	 * @param target
	 */
	public ConnectPacketClient(String origin, String target) {
		super(origin, target);
	}

}
