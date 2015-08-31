package applet.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JMenu;

import applet.controller.AppletBoletosController;
import applet.controller.ShowMessage;
import applet.controller.boleto.BoletoFactory;
import applet.controller.util.Util;
import applet.model.boleto.BoletoObject;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;

public class AppletBoletosMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static DefaultTableModel modelo = new DefaultTableModel();
	private static DefaultTableModel modeloErr = new DefaultTableModel();
	private static TableRowSorter<TableModel> sorter;

	private static JScrollPane painelHistorico = (JScrollPane) makeDataTableHistory();
	private JComponent painelFalha = makeDataTableErros();

	public static JTextPane consoleOutput = new JTextPane();

	public static AppletBoletosMain frame;
	private JTextField searchField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		UIManager.put("swing.boldMetal", Boolean.FALSE);

		EventQueue.invokeLater(() -> {
			try {
				frame = new AppletBoletosMain();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	public AppletBoletosMain() throws InterruptedException {

		// -- Inicializa Applet
		AppletBoletosController.inicializar();

		// --- Janela Principal
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 800, 540);
		// ---- end

		// --- Menu Bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuConf = new JMenu("Configura\u00E7\u00F5es");
		menuBar.add(menuConf);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		// --- end

		// --- Painéis
		JTabbedPane painelBoletos = new JTabbedPane(JTabbedPane.TOP);
		painelBoletos.setBounds(10, 21, 764, 242);
		painelBoletos.addTab("Histórico Sucesso", painelHistorico);
		painelBoletos.addTab("Histórico Falhas", painelFalha);
		contentPane.add(painelBoletos);
		// --- END

		// --- ConsoleOutput
		consoleOutput.setEditable(false);
		consoleOutput.setBounds(10, 312, 764, 124);
		consoleOutput.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(consoleOutput);
		// --- END

		// --- Botões
		JButton btnSair = new JButton("Finalizar Emissor");
		btnSair.setBounds(649, 447, 125, 23);
		contentPane.add(btnSair);

		JButton btnEmit = new JButton("Reemitir");
		btnEmit.setBounds(685, 278, 89, 23);
		contentPane.add(btnEmit);

		JButton btnLimparConsole = new JButton("Limpar Console");
		btnLimparConsole.setBounds(561, 278, 114, 23);
		contentPane.add(btnLimparConsole);

		JLabel lblPesquisar = new JLabel("Pesquisar:");
		lblPesquisar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPesquisar.setBounds(490, 5, 73, 23);
		contentPane.add(lblPesquisar);
		// -- End

		searchField = new JTextField();
		searchField.setBounds(561, 8, 213, 20);
		contentPane.add(searchField);
		searchField.setColumns(10);

		// --- Eventos
		btnSair.addActionListener(btn -> System.exit(0));
		btnEmit.addActionListener(btn -> reemitirBoletosSelecionados());
		btnLimparConsole.addActionListener(btn -> consoleOutput.setText(""));
		menuConf.addActionListener( men -> ConfigsView.initConfig() );

		/* Não adicionaram Functional Interface aqui, então sem lambda :( */
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = searchField.getText();

				if (text.trim().length() == 0) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = searchField.getText();

				if (text.trim().length() == 0) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

		});

		// --- Inicializadores
		initializeDataTables();

	}

	protected static JComponent makeDataTableHistory() {

		JTable jtable = new JTable(modelo);
		jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		modelo.addColumn("Nome Beneficiário");
		modelo.addColumn("CPF/CNPJ Beneficiário");
		modelo.addColumn("Cidade Beneficiário");
		modelo.addColumn("Logradouro Beneficiário");
		modelo.addColumn("UF Beneficiário");
		modelo.addColumn("Nome Pagador");
		modelo.addColumn("CPF/CNPJ Pagador");
		modelo.addColumn("Cidade Pagador");
		modelo.addColumn("Logradouro Pagador");
		modelo.addColumn("UF Pagador");
		modelo.addColumn("Tipo Carteira");
		modelo.addColumn("Nosso Número");
		modelo.addColumn("Número Documento");
		modelo.addColumn("Aceite");
		modelo.addColumn("Tipo Documento");
		modelo.addColumn("Agência");
		modelo.addColumn("Código do Beneficiário");
		modelo.addColumn("Valor");
		modelo.addColumn("Data Emissão");
		modelo.addColumn("Data Processamento");
		modelo.addColumn("Data Vencimento");

		modelo.setNumRows(0);

		jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(120);

		setSorter(new TableRowSorter<TableModel>(modelo));

		jtable.setRowSorter(getSorter());

		JScrollPane barraRolagem = new JScrollPane(jtable);
		return barraRolagem;

	}

	protected JComponent makeDataTableErros() {

		JTable jtable = new JTable(modeloErr);
		jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		modeloErr.addColumn("Número do Boleto");
		modeloErr.addColumn("Pagador do Boleto");
		modeloErr.addColumn("Falhas Detectadas");

		modeloErr.setNumRows(0);

		jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(190);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(600);

		JScrollPane barraRolagem = new JScrollPane(jtable);
		return barraRolagem;

	}

	public void initializeDataTables() {

		String bkpPath = AppletBoletosController.appletBoleto.getBoletosGeradosPath();
		String fullBkpPath = bkpPath + "\\boletosEmitidos.bin";
		String errPath = AppletBoletosController.appletBoleto.getBoletosFalhaPath();
		String fullErrPath = errPath + "\\errBol.bin";

		List<BoletoObject> historyList = new ArrayList<BoletoObject>();
		List<BoletoObject> errList = new ArrayList<BoletoObject>();

		historyList = Util.readFromBinaryFile(fullBkpPath);
		errList = Util.readFromBinaryFile(fullErrPath);

		insertIntoHistory(historyList);
		insertIntoErr(errList);
	}

	public static void insertIntoHistory(BoletoObject bol) {

		List<BoletoObject> historyList = new ArrayList<BoletoObject>();
		historyList.add(bol);
		insertIntoHistory(historyList);

	}

	public static void insertIntoHistory(List<BoletoObject> list) {

		if (list.isEmpty())
			return;

		list.stream().forEachOrdered(
				bol -> modelo.addRow(new Object[] { bol.getNomeBeneficiario(), bol.getCpfCnpjBeneficiario(),
						bol.getCidadeBeneficiario(), bol.getLogradouroBeneficiario(), bol.getUfBeneficiario(),
						bol.getNomePagador(), bol.getCpfCnpjPagador(), bol.getCidadePagador(),
						bol.getLogradouroPagador(), bol.getUfPagador(), bol.getCarteira(), bol.getNossoNumero(),
						bol.getNumeroDocumento(), bol.getAceite(), bol.getTipoDocumento(), bol.getAgencia(),
						bol.getCodigoBeneficiario(), bol.getValor(), Util.dataFormatada(bol.getDataDoDocumento()),
						Util.dataFormatada(bol.getDataDeProcessamento()), Util.dataFormatada(bol.getDataDeVencimento())

				}));

	}

	public static void insertIntoErr(BoletoObject bol) {

		List<BoletoObject> errList = new ArrayList<BoletoObject>();
		errList.add(bol);
		insertIntoErr(errList);

	}

	public static void insertIntoErr(List<BoletoObject> list) {

		if (list.isEmpty())
			return;

		list.stream().forEachOrdered(
				bol -> modeloErr.addRow(new Object[] { bol.getNumeroDocumento(), bol.getNomePagador(),
						bol.getErrosBoleto().toString() }));

	}

	public static void appendToPane(String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = consoleOutput.getDocument().getLength();

		try {
			consoleOutput.getDocument().insertString(len, msg, aset);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	protected static void reemitirBoletosSelecionados() {

		Integer[] rowids = { -1 };

		JTable viewport = (JTable) painelHistorico.getViewport().getView();

		if (viewport.getSelectedRowCount() == 0) {
			ShowMessage.info("Nenhum boleto selecionado!");
			return;
		}

		rowids = new Integer[viewport.getSelectedRows().length];

		for (int i = 0; i < viewport.getSelectedRows().length; i++) {
			rowids[i] = Integer.valueOf(viewport.getSelectedRows()[i]);
		}

		BoletoFactory.gerarPdfs(rows2list(rowids));

		ShowMessage.info("Boletos reemitidos com sucesso!");

	}

	protected static List<BoletoObject> rows2list(Integer[] rows) {

		List<BoletoObject> listSelected = new ArrayList<BoletoObject>();

		listSelected = Arrays.asList(rows).stream().map(row -> row2object(row)).collect(Collectors.toList());

		return listSelected;

	}

	protected static BoletoObject row2object(Integer rowid) {

		BoletoObject boletoObject = new BoletoObject();
		JTable viewport = (JTable) painelHistorico.getViewport().getView();

		boletoObject.setNomeBeneficiario((String) viewport.getValueAt(rowid, 0));
		boletoObject.setCpfCnpjBeneficiario((String) viewport.getValueAt(rowid, 1));
		boletoObject.setCidadeBeneficiario((String) viewport.getValueAt(rowid, 2));
		boletoObject.setLogradouroBeneficiario((String) viewport.getValueAt(rowid, 3));
		boletoObject.setUfBeneficiario((String) viewport.getValueAt(rowid, 4));
		boletoObject.setNomePagador((String) viewport.getValueAt(rowid, 5));
		boletoObject.setCpfCnpjPagador((String) viewport.getValueAt(rowid, 6));
		boletoObject.setCidadePagador((String) viewport.getValueAt(rowid, 7));
		boletoObject.setLogradouroPagador((String) viewport.getValueAt(rowid, 8));
		boletoObject.setUfPagador((String) viewport.getValueAt(rowid, 9));
		boletoObject.setCarteira((Integer) viewport.getValueAt(rowid, 10));
		boletoObject.setNossoNumero((Long) viewport.getValueAt(rowid, 11));
		boletoObject.setNumeroDocumento((Integer) viewport.getValueAt(rowid, 12));
		boletoObject.setAceite((String) viewport.getValueAt(rowid, 13));
		boletoObject.setTipoDocumento((String) viewport.getValueAt(rowid, 14));
		boletoObject.setAgencia((Integer) viewport.getValueAt(rowid, 15));
		boletoObject.setCodigoBeneficiario((Long) viewport.getValueAt(rowid, 16));
		boletoObject.setValor((BigDecimal) viewport.getValueAt(rowid, 17));
		boletoObject.setDataDoDocumento(Util.string2Calendar((String) viewport.getValueAt(rowid, 18)));
		boletoObject.setDataDeProcessamento(Util.string2Calendar((String) viewport.getValueAt(rowid, 19)));
		boletoObject.setDataDeVencimento(Util.string2Calendar((String) viewport.getValueAt(rowid, 20)));

		return boletoObject;

	}

	/**
	 * @return the sorter
	 */
	public static TableRowSorter<TableModel> getSorter() {
		return sorter;
	}

	/**
	 * @param sorter
	 *            the sorter to set
	 */
	public static void setSorter(TableRowSorter<TableModel> sorter) {
		AppletBoletosMain.sorter = sorter;
	}

}
