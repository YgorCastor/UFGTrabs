package applet.model;

public enum ErrorList {	
	
	ARQUIVO_VAZIO("O Arquivo de gera��o de boletos est� vazio!"),
	BOL_INVALID("O Boleto de ID %d � inv�lido!"),
	QTD_PARAMETROS_INVALIDA("Quantidade de par�metros do boleto %d inv�lida!"),
	FALHA_CNV_STRING_BIGDECIMAL("Falha na convers�o de valor!"),
	CPF_CNPJ_INVALIDO("CPF/CNPJ do %s Inv�lido"),
	FALHA_CONV_DATA("Falha na Convers�o de Data"),
	UF_INVALIDA("Unidade Federativa Inv�lida"),
	CARTEIRA_INVALIDA("Carteira Inv�lida\n"),
	ACEITE_INVALIDO("Aceite inv�lido\n"),
	TDOC_INVALIDO("Tipo de Documento Inv�lido!"),
	AGENCIA_INVALIDA("Ag�ncia Inv�lida!\n O n�mero deve possuir 4 d�gitos sem o d�gito verificador!"),
	COD_BEN_INVALIDO("C�digo do Benefici�rio inv�lido!\n O C�digo deve possuir 6 d�gitos!"),
	CONN_FAILURE("Falha na conex�o!\nEntre em contato com a CerradoWeb"
		 	     + " pelo telefone (62)XXXX-XXXX ou pelo e-mail xxx@xxxx.xxx"),
    NEW_FILE_FAILURE("Falha na cria��o de arquivo! O boleto %s n�o foi criado!");
	
	
	public String msg;
	
	ErrorList( String valorErro ){
		
		this.msg = valorErro;
		
	}
	

}
