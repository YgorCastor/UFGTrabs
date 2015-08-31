package applet.model.boleto.Bancos;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import applet.model.boleto.BoletoObject;
import applet.model.boleto.CriadorBoleto;
import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Endereco;
import br.com.caelum.stella.boleto.bancos.Caixa;
import br.com.caelum.stella.boleto.bancos.gerador.GeradorDeDigito;
import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;

public class BoletoCaixa extends CriadorBoleto {

	private Banco banco = new Caixa();
	private Beneficiario beneficiario;

	GeradorDeDigito dvGenerator = banco.getGeradorDeDigito();
	StringBuilder nossoNumeroDV = new StringBuilder();
	StringBuilder codBeneficiarioDV = new StringBuilder();

	@Override
	public CriadorBoleto comEmissor(String nomeBeneficiario, String cpfCnpj, String carteira, String agencia,
			String codBeneficiario, String nossoNumero, Endereco endereco) {

		this.beneficiario = Beneficiario.novoBeneficiario().comNomeBeneficiario(nomeBeneficiario).comCpfCnpj(cpfCnpj)
				.comCarteira(carteira).comNossoNumero(nossoNumero).comEndereco(endereco).comAgencia(agencia)
				.comCodigoBeneficiario(codBeneficiario);

		nossoNumeroDV.append(dvGenerator.geraDigitoMod11AceitandoRestoZero(banco
				.getNossoNumeroFormatado(this.beneficiario)));

		codBeneficiarioDV.append(dvGenerator.geraDigitoMod11AceitandoRestoZero(banco
				.getCodigoBeneficiarioFormatado(this.beneficiario)));

		this.beneficiario.comDigitoNossoNumero(nossoNumeroDV.toString()).comDigitoCodigoBeneficiario(
				codBeneficiarioDV.toString());

		return this;

	}

	@Override
	public CriadorBoleto comEmissor(BoletoObject boleto) {

		this.beneficiario = Beneficiario
				.novoBeneficiario()
				.comNomeBeneficiario(boleto.getNomeBeneficiario())
				.comCpfCnpj(boleto.getCpfCnpjBeneficiario())
				.comCarteira(boleto.getCarteira().toString())
				.comNossoNumero(boleto.getNossoNumero().toString())
				.comAgencia(boleto.getAgencia().toString())
				.comCodigoBeneficiario(boleto.getCodigoBeneficiario().toString())
				.comEndereco(
						Endereco.novoEndereco().comCidade(boleto.getCidadeBeneficiario())
								.comLogradouro(boleto.getLogradouroBeneficiario()).comUf(boleto.getUfPagador())

				);

		nossoNumeroDV.append(dvGenerator.geraDigitoMod11AceitandoRestoZero(banco
				.getNossoNumeroFormatado(this.beneficiario)));

		codBeneficiarioDV.append(dvGenerator.geraDigitoMod11AceitandoRestoZero(banco
				.getCodigoBeneficiarioFormatado(this.beneficiario)));

		this.beneficiario.comDigitoNossoNumero(nossoNumeroDV.toString()).comDigitoCodigoBeneficiario(
				codBeneficiarioDV.toString());

		return this;

	}
	
	@Override
	public CriadorBoleto comDadosBoleto(BoletoObject boletoObj) {

		this.boleto = Boleto
				.novoBoleto()
				.comNumeroDoDocumento(boletoObj.getNumeroDocumento().toString())
				.comValorBoleto(boletoObj.getValor()) 
				.comAceite( ( boletoObj.getAceite().equals("A") ) ? true : false )
				.comDatas(
						Datas.novasDatas().comDocumento(boletoObj.getDataDoDocumento())
						                  .comProcessamento(boletoObj.getDataDeProcessamento())
								          .comVencimento(boletoObj.getDataDeVencimento()))
				.comBanco(getBanco())
				.comBeneficiario(this.beneficiario)
				.comPagador(this.pagador)
				.comInstrucoes("NÃO RECEBER APÓS 10 DIAS DE ATRASO!", "JUROS : 2,00 REAIS AO DIA", " ",
						"Senhor Caixa: Não receber! Boleto de Demonstração!")
				.comLocaisDePagamento(this.banco.getLocalDePagamentoPadrao())
				.comEspecieDocumento(boletoObj.getTipoDocumento());

		return this;

	}

	@Override
	public CriadorBoleto compilaBoleto() throws FileNotFoundException {

		InputStream templateBoleto = getClass().getResourceAsStream("../../jasper/boleto/boleto-caixa.jasper");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("MENSAGEM_BANCO", new String(banco.getMensagemPadrao()));
		GeradorDeBoleto gerador = new GeradorDeBoleto(templateBoleto, parametros, boleto);
		boletoCompilado = gerador;

		return this;

	}

	public static CriadorBoleto novoBoleto() {

		return new BoletoCaixa();

	}

	public Banco getBanco() {
		return banco;
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

}
