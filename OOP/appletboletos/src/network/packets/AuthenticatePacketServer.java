package network.packets;

import java.io.Serializable;
import java.util.Calendar;

public class AuthenticatePacketServer extends Packet implements Serializable {

	/**
	 * 
	 */
	private int answer;
	private Calendar dataVencimento;
	private static final long serialVersionUID = -7693857740453022726L;

	public AuthenticatePacketServer(String origin, String target, int answer, Calendar dataVencimento) {
		super(origin, target);
		this.answer = answer;
		this.dataVencimento = dataVencimento;

	}

	public int getAswer() {
		return answer;
	}

	public void setAnswer(int aswer) {
		this.answer = aswer;
	}

	public Calendar getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Calendar dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

}
