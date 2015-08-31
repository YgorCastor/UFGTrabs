package applet.model.email;

public enum MailTemplates {

	MAIL_TEST("EMAIL DE TESTE","TESTANDO ENVIO DE EMAIL");	
	
	public String message;
	public String header;
	
	MailTemplates( String header , String message ){
		this.message = message;
		this.header = header;
	}
	

}
