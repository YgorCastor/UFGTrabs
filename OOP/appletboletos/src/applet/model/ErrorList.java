package applet.model;

public enum ErrorList {	
	
	ARQUIVO_VAZIO("O Arquivo de geração de boletos está vazio!"),
	BOL_INVALID("O Boleto de ID %d é inválido!"),
	QTD_PARAMETROS_INVALIDA("Quantidade de parâmetros do boleto %d inválida!"),
	FALHA_CNV_STRING_BIGDECIMAL("Falha na conversão de valor!"),
	CPF_CNPJ_INVALIDO("CPF/CNPJ do %s Inválido"),
	FALHA_CONV_DATA("Falha na Conversão de Data"),
	UF_INVALIDA("Unidade Federativa Inválida"),
	CARTEIRA_INVALIDA("Carteira Inválida\n"),
	ACEITE_INVALIDO("Aceite inválido\n"),
	TDOC_INVALIDO("Tipo de Documento Inválido!"),
	AGENCIA_INVALIDA("Agência Inválida!\n O número deve possuir 4 dígitos sem o dígito verificador!"),
	COD_BEN_INVALIDO("Código do Beneficiário inválido!\n O Código deve possuir 6 dígitos!"),
	CONN_FAILURE("Falha na conexão!\nEntre em contato com a CerradoWeb"
		 	     + " pelo telefone (62)XXXX-XXXX ou pelo e-mail xxx@xxxx.xxx"),
    NEW_FILE_FAILURE("Falha na criação de arquivo! O boleto %s não foi criado!");
	
	
	public String msg;
	
	ErrorList( String valorErro ){
		
		this.msg = valorErro;
		
	}
	

}
