package applet.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import applet.model.boleto.BoletoObject;

/*
 * @author Ygor Castor
 * @since 17/12/2014
 * @version 1.0
 * 
 * Define uma tupla para retornar os objetos
 * relativos aos boletos gerados para tratamento 
 * posterior.
 */
public class BoletosTuple {
	
	private List<BoletoObject> objetosBoletos;
	private List<File> arquivos;
	
	
	public BoletosTuple( List<BoletoObject> boletosObj  ){
		
		 this.objetosBoletos = boletosObj;
		
	}
	
	public BoletosTuple(){
		
		this.objetosBoletos = new ArrayList<BoletoObject>();
		this.arquivos = new ArrayList<File>();
		
	}	

	public List<BoletoObject> getObjetosBoletos() {
		return objetosBoletos;
	}
	
	public void setObjetosBoletos(List<BoletoObject> objetosBoletos) {
		this.objetosBoletos = objetosBoletos;
	}

	public List<File> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<File> arquivos) {
		this.arquivos = arquivos;
	}
   
	

}
