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
 * A Classe GeradorBoleto é responsável pela integração
 * do framework stella-caelum-boleto nos projetos MP.
 * <p>
 * A geração dos boletos depende de parâmetros obtidos
 * através da DOM ou de algum serviço, os valores devem
 * ser validados antes da chamada da classe.
 * [TODO] Criar uma tabela de ErrCode para validação in-class e 
 * mensagens de erro externas.
 * <p>a
 * Todos os valores aqui atribuídos são necessários de acordo com regulamentos 
 * da FEBABRAN, então caso qualquer informação esteja indisponível o boleto
 * [b]NÃO DEVE SER GERADO[/b], pois há o perigo do boleto não ser autenticado.
 * <p>
 * <h2>Utilização:</h2>
 * Deve se gerar o Beneficiário e o Pagador antes de se gerar o boleto!
 * Obs: A biblioteca suporta Method Chaining!
 * <p>
 * Há validações de ordem, caso a ordem não seja seguida não ocorrerá exceções,
 * porém o boleto não será gerado.
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
				.comInstrucoes("NÃO RECEBER APÓS 10 DIAS DE ATRASO!", "JUROS : 2,00 REAIS AO DIA", " ",
						"Senhor Caixa: Não receber! Boleto de Demonstração!")
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
				.comInstrucoes("NÃO RECEBER APÓS 10 DIAS DE ATRASO!", "JUROS : 2,00 REAIS AO DIA", " ",
						"Senhor Caixa: Não receber! Boleto de Demonstração!")
				.comLocaisDePagamento(this.banco.getLocalDePagamentoPadrao())
				.comEspecieDocumento(boletoObj.getTipoDocumento());

		return this;

	}

	/**
	 * Compila as informações em um stream do boleto
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
