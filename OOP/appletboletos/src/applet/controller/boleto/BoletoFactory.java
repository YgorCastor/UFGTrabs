package applet.controller.boleto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import applet.controller.AppletBoletosController;
import applet.controller.BoletosTuple;
import applet.controller.Mappers;
import applet.controller.ShowMessage;
import applet.controller.util.Util;
import applet.model.ErrorList;
import applet.model.MessagesList;
import applet.model.boleto.BoletoObject;
import applet.view.AppletBoletosMain;

/*
 * @author Ygor Castor
 * @since 19/12/2014
 * @version 1.3
 * 
 * Clásse fábrica de Boletos, implementa
 * a lógica de geração
 */
public class BoletoFactory {

	/*
	 * @author Ygor Castor
	 * 
	 * @since 19/12/2014
	 * 
	 * @version 1.3
	 * 
	 * Percorre a pasta com os arquivos geradores de boleto, os processa e os
	 * agrupam em uma lista
	 * 
	 * @return BoletosTuple Tupla com os Boletos gerados e ponteiros para os
	 * arquivos
	 */
	public static BoletosTuple processBoletos(String path) throws IOException {

		BoletosTuple bTupla = new BoletosTuple();

		List<File> filesInFolder = Files.walk(Paths.get(path)).filter(Files::isRegularFile).map(Path::toFile)
				.collect(Collectors.toList());

		if (filesInFolder.isEmpty()) {
			ShowMessage.info(MessagesList.NO_BOL_FILES.message);
			return bTupla;
		}

		List<BoletoObject> dados = filesInFolder.stream().map(boleto -> Mappers.boletoToObject(boleto))
				.collect(Collectors.toList());

		if (dados.isEmpty()) {
			ShowMessage.info(MessagesList.NO_BOL_FILES.message);
			return bTupla;
		}

		bTupla.setObjetosBoletos(dados);
		bTupla.setArquivos(filesInFolder);

		return bTupla;

	}

	public static BoletosTuple processBoletos() throws IOException {
		return processBoletos(AppletBoletosController.appletBoleto.getBoletosGeneratorPath());
	}

	/*
	 * @author Ygor Castor
	 * 
	 * @since 19/12/2014
	 * 
	 * @version 1.3
	 * 
	 * Percorre os objetos gerando os pdfs dos boletos válidos
	 */
	public static void gerarPdfs(BoletosTuple tupla) {

		byte[] comparable = new byte[] { 0 };

		if (tupla.getObjetosBoletos().isEmpty())
			return;

		tupla.getObjetosBoletos()
				.parallelStream()
				.filter(boleto -> boleto.getErrosBoleto().isEmpty())
				.map(boleto -> Mappers.boletoObjectToPdf(boleto))
				.filter(vByte -> !Arrays.equals(vByte.getGerador().geraPDF(), comparable))
				.forEach(
						p -> {

							String boletosPath = AppletBoletosController.appletBoleto.getBoletosPDFPath();

							String fullPath = boletosPath + "\\" + p.getPagador().getNome() + "_"
									+ Util.dataFormatada(p.getBoleto().getDatas().getProcessamento())
									+ p.getBoleto().getNumeroDoDocumento() + ".pdf";

							p.getGerador().geraPDF(fullPath);

						});

	}

	public static void gerarPdfs(List<BoletoObject> lista) {

		BoletosTuple tupla = new BoletosTuple();

		tupla.setObjetosBoletos(lista);
		gerarPdfs(tupla);

	}

	public static void errBol(BoletosTuple tupla) {

		if (tupla.getArquivos().isEmpty())
			return;

		tupla.getObjetosBoletos().stream().filter(boleto -> !boleto.getErrosBoleto().isEmpty()).forEach(boleto -> {

			String errPath = AppletBoletosController.appletBoleto.getBoletosFalhaPath();
			String fullPath = errPath + "\\errBol.bin";

			Util.writeToBinary(fullPath, boleto, true);

			AppletBoletosMain.insertIntoErr(boleto);

			ShowMessage.warning(String.format(ErrorList.BOL_INVALID.msg, boleto.getNumeroDocumento()));

		});

	}

}
