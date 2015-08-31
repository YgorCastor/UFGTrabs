package applet.model;

public enum MessagesList {

	NO_BOL_FILES("Nenhum boleto a ser lido!"),
	AUTH_SUCCESS("Autenticação realizada com sucesso!"),
	AUTH_FAILURE("Falha na Autenticação!\nEntre em contato com a CerradoWeb"
		 	     + " pelo telefone (62)XXXX-XXXX ou pelo e-mail xxx@xxxx.xxx"),
	AUTH_PERIOD("A sua fatura se encontra pendente!\n Realize o pagamento até "
			    + "o dia %s para evitar interrupção do serviço."),
	BOL_SUCCESS("%d boletos gerados com sucesso!"),
	BOL_FAILURE("%d boletos com falha!\n Verifique o arquivo errbol.log para maiores detalhes!");
			    	
	public String message;
	
	MessagesList( String message ){
		this.message = message;
	}
	
	
}
