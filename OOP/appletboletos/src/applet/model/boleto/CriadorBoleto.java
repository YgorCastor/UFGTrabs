package applet.model.boleto;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Endereco;
import br.com.caelum.stella.boleto.Pagador;
import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;

/*
 * <h1>Classe GeradorBoleto</h1>
 * A Classe GeradorBoleto � respons�vel pela integra��o
 * do framework stella-caelum-boleto nos projetos MP.
 * <p>
 * A gera��o dos boletos depende de par�metros obtidos
 * atrav�s da DOM ou de algum servi�o, os valores devem
 * ser validados antes da chamada da classe.
 * [TODO] Criar uma tabela de ErrCode para valida��o in-class e 
 * mensagens de erro externas.
 * <p>a
 * Todos os valores aqui atribu�dos s�o necess�rios de acordo com regulamentos 
 * da FEBABRAN, ent�o caso qualquer informa��o esteja indispon�vel o boleto
 * [b]N�O DEVE SER GERADO[/b], pois h� o perigo do boleto n�o ser autenticado.
 * <p>
 * <h2>Utiliza��o:</h2>
 * Deve se gerar o Benefici�rio e o Pagador antes de se gerar o boleto!
 * Obs: A biblioteca suporta Method Chaining!
 * <p>
 * H� valida��es de ordem, caso a ordem n�o seja seguida n�o ocorrer� exce��es,
 * por�m o boleto n�o ser� gerado.
 * 
 * @author Ygor Castor
 * @version 1.1
 * @since 24/10/2014
 * 
 */
public abstract class CriadorBoleto {

	protected Boleto boleto;
	protected Banco banco;
	protected Pagador pagador;
	protected Beneficiario beneficiario;
	protected GeradorDeBoleto boletoCompilado;

	protected Calendar dataAtual = Calendar.getInstance();

	public CriadorBoleto comEmissor(String nomeBeneficiario, String cpfCnpj, String carteira, String agencia,
			String codBeneficiario, String nossoNumero, Endereco endereco) {

		this.beneficiario = Beneficiario.novoBeneficiario().comNomeBeneficiario(nomeBeneficiario).comCpfCnpj(cpfCnpj)
				.comCarteira(carteira).comNossoNumero(nossoNumero).comEndereco(endereco);

		return this;

	}

	public CriadorBoleto comEmissor(BoletoObject boleto) {

		this.beneficiario = Beneficiario
				.novoBeneficiario()
				.comNomeBeneficiario(boleto.getNomeBeneficiario())
				.comCpfCnpj(boleto.getCpfCnpjBeneficiario())
				.comCarteira(boleto.getCarteira().toString())
				.comNossoNumero(boleto.getNossoNumero().toString())
				.comEndereco(
						Endereco.novoEndereco().comCidade(boleto.getCidadeBeneficiario())
								.comLogradouro(boleto.getLogradouroBeneficiario()).comUf(boleto.getUfPagador())

				);

		return this;

	}

	public CriadorBoleto comPagador(String nomePagador, String cpfCnpj, Endereco endereco) {

		this.pagador = Pagador.novoPagador()
				     .comNome(nomePagador)
				     .comCpfCnpj(cpfCnpj)
				     .comEndereco(endereco);

		return this;

	}

	public CriadorBoleto comPagador(BoletoObject boleto) {

		this.pagador = Pagador
				.novoPagador()
				.comNome(boleto.getNomePagador())
				.comCpfCnpj(boleto.getCpfCnpjPagador())
				.comEndereco(
						Endereco.novoEndereco().comCidade(boleto.getCidadePagador())
								.comLogradouro(boleto.getLogradouroPagador()).comUf(boleto.getUfPagador())

				);

		return this;

	}

	public CriadorBoleto comDadosBoleto(String numeroDocumento, BigDecimal valor, Boolean aceite, String tipoDocumento,
			Calendar dataDocumento, Calendar dataProcessamento, Calendar dataVencimento) {

		this.boleto = Boleto
				.novoBoleto()
				.comNumeroDoDocumento(numeroDocumento)
				.comValorBoleto(valor)
				.comAceite(aceite)
				.comDatas(
						Datas.novasDatas().comDocumento(dataDocumento).comProcessamento(dataProcessamento)
								.comVencimento(dataVencimento))
				.comBanco(getBanco())
				.comBeneficiario(this.beneficiario)
				.comPagador(this.pagador)
				.comInstrucoes("N�O RECEBER AP�S 10 DIAS DE ATRASO!", "JUROS : 2,00 REAIS AO DIA", " ",
						"Senhor Caixa: N�o receber! Boleto de Demonstra��o!")
				.comLocaisDePagamento(this.banco.getLocalDePagamentoPadrao()).comEspecieDocumento(tipoDocumento);

		return this;

	}

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
				.comInstrucoes("N�O RECEBER AP�S 10 DIAS DE ATRASO!", "JUROS : 2,00 REAIS AO DIA", " ",
						"Senhor Caixa: N�o receber! Boleto de Demonstra��o!")
				.comLocaisDePagamento(this.banco.getLocalDePagamentoPadrao())
				.comEspecieDocumento(boletoObj.getTipoDocumento());

		return this;

	}

	/**
	 * Compila as informa��es em um stream do boleto
	 * 
	 * @return this Method Chaining
	 */
	public CriadorBoleto compilaBoleto() throws FileNotFoundException {
	
		InputStream templateBoleto = getClass().getResourceAsStream("jasper/boleto/boleto.jasper");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("MENSAGEM_BANCO", new String(banco.getMensagemPadrao()));
		GeradorDeBoleto gerador = new GeradorDeBoleto(templateBoleto, parametros, boleto);
		boletoCompilado = gerador;

		return this;

	}
	
	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public Banco getBanco() {
		return banco;
	}

	public Pagador getPagador() {
		return pagador;
	}

	public void setPagador(Pagador pagador) {
		this.pagador = pagador;
	}

	public Beneficiario getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}

	public GeradorDeBoleto getGerador() {
		return boletoCompilado;
	}

	public void setGerador(GeradorDeBoleto gerador) {
		this.boletoCompilado = gerador;
	}

}
