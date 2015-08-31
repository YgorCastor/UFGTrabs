package applet.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import applet.controller.util.Util;
import applet.controller.util.Validators;
import applet.model.ErrorList;
import applet.model.boleto.BoletoObject;
import applet.model.boleto.CriadorBoleto;
import applet.model.boleto.Bancos.BoletoCaixa;

/*
 * @author Ygor Castor
 * @since 16/12/2014
 * @version 1.2
 * 
 * Classe de mapeamentos, ela é responsável por agrupar
 * os algoritmos que são aplicados em cada objeto através
 * de clausuras e funções lambda.
 * 
 */
public class Mappers {

	
	/*
	 * @author Ygor Castor
	 * @since 16/12/2014
	 * @version 1.2
	 * 
	 * Mapeia um arquivo gerador de boleto para um
	 * objeto.
	 * 
	 * @param boletoArquivo File Arquivo Gerador
	 * 
	 * @return BoletoObject Objeto Mapeado
	 * 
	 */
	public static BoletoObject boletoToObject(File boletoArquivo) {

		BoletoObject boletoObject = new BoletoObject();
		BufferedReader buffer = null;
		String linha = "";
		String splitBy = ";";

		try {

			buffer = new BufferedReader(new FileReader(boletoArquivo));
			linha = buffer.readLine();

			if ("".equals(linha)) {
				buffer.close();
				boletoObject.getErrosBoleto().add(ErrorList.ARQUIVO_VAZIO.msg);
				return boletoObject;
			}

			String[] dadosBoleto = linha.split(splitBy);

			if (dadosBoleto.length < 21 || dadosBoleto.length > 21) {
				buffer.close();
				boletoObject.setRawData(dadosBoleto);
				boletoObject.getErrosBoleto().add(ErrorList.QTD_PARAMETROS_INVALIDA.msg);
				return boletoObject;
			}
			
			boletoObject.setNomeBeneficiario(dadosBoleto[0].trim());
			boletoObject.setCpfCnpjBeneficiario(dadosBoleto[1].trim());
			boletoObject.setCidadeBeneficiario(dadosBoleto[2].trim());
			boletoObject.setLogradouroBeneficiario(dadosBoleto[3].trim());
			boletoObject.setUfBeneficiario(dadosBoleto[4].trim());
			boletoObject.setNomePagador(dadosBoleto[5].trim());
			boletoObject.setCpfCnpjPagador(dadosBoleto[6].trim());
			boletoObject.setCidadePagador(dadosBoleto[7].trim());
			boletoObject.setLogradouroPagador(dadosBoleto[8].trim());
			boletoObject.setUfPagador(dadosBoleto[9].trim());
			boletoObject.setCarteira(Validators.stringToInt(dadosBoleto[10].trim()));
			boletoObject.setNossoNumero(Long.parseLong(dadosBoleto[11].trim()));
			boletoObject.setNumeroDocumento(Integer.parseInt(dadosBoleto[12].trim()));
			boletoObject.setAceite(dadosBoleto[13].trim());
			boletoObject.setTipoDocumento(dadosBoleto[14].trim());
			boletoObject.setAgencia(Integer.parseInt(dadosBoleto[15].trim()));
			boletoObject.setCodigoBeneficiario(Long.parseLong(dadosBoleto[16].trim()));
			boletoObject.setValor(Util.string2BigDecimal(dadosBoleto[17].trim()));
			boletoObject.setDataDoDocumento(Util.string2Calendar(dadosBoleto[18].trim()));
			boletoObject.setDataDeProcessamento(Util.string2Calendar(dadosBoleto[19].trim()));
			boletoObject.setDataDeVencimento(Util.string2Calendar(dadosBoleto[20].trim()));
			boletoObject.setRawData(dadosBoleto);

			buffer.close();

		} catch (FileNotFoundException e) {			
			ShowMessage.error("Arquivo não encontrado!");
		} catch (IOException e) {
			ShowMessage.error("Erro de entrada e saída!");
		} catch (ParseException e) {
			ShowMessage.error("Erro de Parsing!");
		} catch (NumberFormatException e){
			ShowMessage.error("Erro na formatação numérica!");
		}

		return boletoObject;

	}

	/*
	 * @author Ygor Castor
	 * @since 18/12/2014
	 * @version 1.0
	 * 
	 * Compila e valida as informações do boleto
	 * 
	 * @param boleto BoletoObject Objeto do Boleto
	 * 
	 * @return CriadorBoleto Factory de Boletos
	 * 
	 */
	public static CriadorBoleto boletoObjectToPdf(BoletoObject boleto) {

		CriadorBoleto boletoFactory = new BoletoCaixa();

		try {

			return boletoFactory.comEmissor(boleto).comPagador(boleto).comDadosBoleto(boleto).compilaBoleto();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		return boletoFactory;

	}
	


}
