package network.packets;

import java.io.Serializable;

public class AuthenticatePacketClient extends Packet implements Serializable {

	private int id;
	private int method;
	private static final long serialVersionUID = -955616938587000681L;

	public AuthenticatePacketClient(String origin, String target, int id, int method) {
		super(origin, target);
		this.id = id;
		this.method = method;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

}
