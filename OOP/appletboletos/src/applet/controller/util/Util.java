package applet.controller.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.text.MaskFormatter;

import applet.controller.ShowMessage;
import applet.model.boleto.BoletoObject;

public class Util {

	public static BigDecimal string2BigDecimal(String valor) throws ParseException {

		BigDecimal valorBig = BigDecimal.ZERO;
		Locale brasil = new Locale("pt", "BR");
		DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(brasil));
		df.setParseBigDecimal(true);

		valorBig = (BigDecimal) df.parse(valor);

		return valorBig;

	}

	public static Calendar string2Calendar(String data) {

		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		try {
			
			if( data.contains("/" ) )
				cal.setTime(sdf1.parse(data));
			else
				cal.setTime(sdf2.parse(data));
		
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return cal;
	}

	public static String formatCpfCnpj(String cpfCnpj) {

		if (cpfCnpj.length() == 9)
			return format("###.###.###-##", cpfCnpj);
		else if (cpfCnpj.length() == 14)
			return format("##.###.###/####-##", cpfCnpj);
		else
			return "";

	}

	private static String format(String pattern, Object value) {
		MaskFormatter mask;
		try {
			mask = new MaskFormatter(pattern);
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String dataFormatada(Calendar cal) {

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		return format1.format(cal.getTime());

	}

	public static class AppendableObjectOutputStream extends ObjectOutputStream {
		public AppendableObjectOutputStream(OutputStream out) throws IOException {
			super(out);
		}

		@Override
		protected void writeStreamHeader() throws IOException {
			reset();
		}
	}

	public static void writeToBinary(String filename, Object obj, boolean append) {

		File file = new File(filename);
		ObjectOutputStream out = null;

		try {
			if (!file.exists() || !append)

				out = new ObjectOutputStream(new FileOutputStream(filename));
			else
				out = new AppendableObjectOutputStream(new FileOutputStream(filename, append));

			out.writeObject(obj);
			out.flush();
		} catch (Exception e) {
			ShowMessage.error("Erro de Escrita Binária!");
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {
			  ShowMessage.error("Erro ao finalizar arquivo binário!");
			}
		}
	}

	public static List<BoletoObject> readFromBinaryFile(String filename) {

		File file = new File(filename);
		List<BoletoObject> list = new ArrayList<BoletoObject>();

		if (file.exists()) {
			ObjectInputStream ois = null;
			try {

				ois = new ObjectInputStream(new FileInputStream(filename));

				while (true) {
					list.add((BoletoObject) ois.readObject());
				}

			} catch (EOFException e) {

			} catch (Exception e) {
				ShowMessage.error("Erro de Leitura Binária!");
			} finally {

				try {
					if (ois != null) {
						ois.close();
						return list;
					}
				} catch (IOException e) {
					ShowMessage.error("Erro de Leitura Binária!");
				}

			}

		}

		return list;
	}
}
