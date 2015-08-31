package applet.model.boleto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import applet.controller.util.Util;
import applet.model.ErrorList;

/*
 * @author Ygor Castor
 * @since 16/12/2014
 * @version 1.0.2
 * 
 * Objeto que representa um boleto a ser gerado
 * 
 */
public class BoletoObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> errosBoleto = new ArrayList<String>();
	private String[] rawData;
	private String nomeBeneficiario;
	private String cpfCnpjBeneficiario;
	private String cidadeBeneficiario;
	private String logradouroBeneficiario;
	private String ufBeneficiario;
	private String nomePagador;
	private String cpfCnpjPagador;
	private String cidadePagador;
	private String logradouroPagador;
	private String ufPagador;
	private Integer carteira;
	private Long nossoNumero;
	private Integer numeroDocumento;
	private String aceite;
	private String tipoDocumento;
	private Integer agencia;
	private Long codigoBeneficiario;
	private BigDecimal valor;
	private Calendar dataDeProcessamento;
	private Calendar dataDoDocumento;
	private Calendar dataDeVencimento;

	public BoletoObject() {

	}

	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}

	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}

	public String getCpfCnpjBeneficiario() {
		return cpfCnpjBeneficiario;
	}

	public void setCpfCnpjBeneficiario(String cpfCnpjBeneficiario) {
	
		String temp = Util.formatCpfCnpj(cpfCnpjBeneficiario.replaceAll("[^\\d]", ""));

		if (temp.equals(""))
			errosBoleto.add(ErrorList.CPF_CNPJ_INVALIDO.msg);

		this.cpfCnpjBeneficiario = temp;
	}

	public String getCidadeBeneficiario() {
		return cidadeBeneficiario;
	}

	public void setCidadeBeneficiario(String cidadeBeneficiario) {
		this.cidadeBeneficiario = cidadeBeneficiario;
	}

	public String getLogradouroBeneficiario() {
		return logradouroBeneficiario;
	}

	public void setLogradouroBeneficiario(String logradouroBeneficiario) {
		this.logradouroBeneficiario = logradouroBeneficiario;
	}

	public String getUfBeneficiario() {
		return ufBeneficiario;
	}

	public void setUfBeneficiario(String ufBeneficiario) {
		this.ufBeneficiario = ufBeneficiario;
	}

	public String getNomePagador() {
		return nomePagador;
	}

	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}

	public String getCpfCnpjPagador() {
		return cpfCnpjPagador;
	}

	public void setCpfCnpjPagador(String cpfCnpjPagador) {	;
		
		String temp = Util.formatCpfCnpj(cpfCnpjPagador.replaceAll("[^\\d]", ""));

		if (temp.equals(""))
			errosBoleto.add(ErrorList.CPF_CNPJ_INVALIDO.msg);

		this.cpfCnpjPagador = temp;
	}

	public String getCidadePagador() {
		return cidadePagador;
	}

	public void setCidadePagador(String cidadePagador) {
		this.cidadePagador = cidadePagador;
	}

	public String getLogradouroPagador() {
		return logradouroPagador;
	}

	public void setLogradouroPagador(String logradouroPagador) {
		this.logradouroPagador = logradouroPagador;
	}

	public String getUfPagador() {
		return ufPagador;
	}

	public void setUfPagador(String ufPagador) {
		this.ufPagador = ufPagador;
	}

	public Integer getCarteira() {
		return carteira;
	}

	public void setCarteira(Integer carteira) {
		
		if( carteira != -1 && ( carteira == 14 || carteira == 04 )  ){
			this.carteira = carteira;						
		}else{
			this.carteira = carteira;
		    errosBoleto.add(ErrorList.CARTEIRA_INVALIDA.msg);
		}
		
	}

	public Long getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(Long nossoNumero) {
				
		this.nossoNumero = nossoNumero;
	}

	public Integer getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(Integer numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getAceite() {
		return aceite;
	}

	public void setAceite(String aceite) {
		this.aceite = aceite;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Integer getAgencia() {
		return agencia;
	}

	public void setAgencia(Integer agencia) {
		this.agencia = agencia;
	}

	public Long getCodigoBeneficiario() {
		return codigoBeneficiario;
	}

	public void setCodigoBeneficiario(Long codigoBeneficiario) {		
		if( codigoBeneficiario.toString().length() < 6 || codigoBeneficiario.toString().length() > 6 )
			errosBoleto.add(ErrorList.COD_BEN_INVALIDO.msg);		
		
		this.codigoBeneficiario = codigoBeneficiario;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Calendar getDataDeProcessamento() {
		return dataDeProcessamento;
	}

	public void setDataDeProcessamento(Calendar dataDeProcessamento) {
		this.dataDeProcessamento = dataDeProcessamento;
	}

	public Calendar getDataDoDocumento() {
		return dataDoDocumento;
	}

	public void setDataDoDocumento(Calendar dataDoDocumento) {
		this.dataDoDocumento = dataDoDocumento;
	}

	public Calendar getDataDeVencimento() {
		return dataDeVencimento;
	}

	public void setDataDeVencimento(Calendar dataDeVencimento) {
		this.dataDeVencimento = dataDeVencimento;
	}

	public String[] getRawData() {
		return rawData;
	}

	public void setRawData(String[] rawData) {
		this.rawData = rawData;
	}

	public List<String> getErrosBoleto() {
		return errosBoleto;
	}

	public void setErrosBoleto(List<String> errosBoleto) {
		this.errosBoleto = errosBoleto;
	}

}
