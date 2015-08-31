package applet.model.email;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class DestinatarioObject {
	
	private Address email;
	private List<String> boletos = new ArrayList<String>();
	
	public DestinatarioObject(){
		
	}

	public Address getEmail() {
		return email;
	}

	public void setEmail(String email) throws AddressException {
		this.email = new InternetAddress(email);
	}

	public List<String> getBoletos() {
		return boletos;
	}

	public void setBoletos(List<String> boletos) {
		this.boletos = boletos;
	}
		

}
