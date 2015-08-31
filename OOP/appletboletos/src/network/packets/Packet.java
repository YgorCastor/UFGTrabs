package network.packets;

import java.io.Serializable;

/**
 *
 * @author Castor
 */
public abstract class Packet implements Serializable {

	private String origin, target;
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param origin
	 * @param target
	 */
	public Packet(String origin, String target) {
		this.origin = origin;
		this.target = target;
	}

	@Override
	public String toString() {
		return "Packet = {" + "origin=" + origin + ", target = " + target + " }";
	}

	/**
	 *
	 * @return
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 *
	 * @param origin
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 *
	 * @return
	 */
	public String getTarget() {
		return target;
	}

	/**
	 *
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

}
