package applet.model;

public enum MessagesList {

	NO_BOL_FILES("Nenhum boleto a ser lido!"),
	AUTH_SUCCESS("Autentica��o realizada com sucesso!"),
	AUTH_FAILURE("Falha na Autentica��o!\nEntre em contato com a CerradoWeb"
		 	     + " pelo telefone (62)XXXX-XXXX ou pelo e-mail xxx@xxxx.xxx"),
	AUTH_PERIOD("A sua fatura se encontra pendente!\n Realize o pagamento at� "
			    + "o dia %s para evitar interrup��o do servi�o."),
	BOL_SUCCESS("%d boletos gerados com sucesso!"),
	BOL_FAILURE("%d boletos com falha!\n Verifique o arquivo errbol.log para maiores detalhes!");
			    	
	public String message;
	
	MessagesList( String message ){
		this.message = message;
	}
	
	
}
