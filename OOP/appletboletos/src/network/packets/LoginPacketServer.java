package network.packets;

import java.io.Serializable;
/**
 *
 * @author Castor
 */
public class LoginPacketServer extends Packet implements Serializable {

	private Integer answer;
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param answer
	 * @param origin
	 * @param target
	 */
	public LoginPacketServer(Integer answer, String origin, String target) {
		super(origin, target);
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "LoginPacketServer{" + "answer=" + answer + '}';
	}

	public Integer getAnswer() {
		return answer;
	}

	public void setAnswer(Integer answer) {
		this.answer = answer;
	}

}
