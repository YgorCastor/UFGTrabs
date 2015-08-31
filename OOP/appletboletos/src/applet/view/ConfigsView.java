package applet.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import java.awt.Label;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;

import applet.controller.AppletBoletosController;

public class ConfigsView extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfArqGer;
	private JTextField tfBolEm;
	private JTextField tfHistBol;
	private JTextField tfFalhaBol;
	private JTextField tfTimeGen;
	private JTextField tfServerIP;
	private JTextField tfSvPorta;
	private JTextField tfUserField;
	private JPasswordField tfPassField;
	private JTextField tfIPSmtp;
	private JTextField tfPortSmtp;
	private JTextField tfEmail;
	private JPasswordField tfMailPass;

	public static void initConfig() {

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

		try {
			ConfigsView dialog = new ConfigsView();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConfigsView() {

		setTitle("Configura\u00E7\u00F5es");
		setResizable(false);
		setBounds(100, 100, 710, 375);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 23, 684, 12);
		contentPanel.add(separator);

		Label confLabel = new Label("Diret\u00F3rios");
		confLabel.setEnabled(false);
		confLabel.setBounds(10, 0, 62, 22);
		contentPanel.add(confLabel);

		JLabel lblArqGer = new JLabel("Arquivos Geradores:");
		lblArqGer.setBounds(10, 36, 105, 14);
		contentPanel.add(lblArqGer);

		tfArqGer = new JTextField();
		tfArqGer.setBounds(115, 32, 248, 22);
		tfArqGer.setText(AppletBoletosController.appletBoleto.getBoletosGeneratorPath());
		contentPanel.add(tfArqGer);
		tfArqGer.setColumns(10);

		JButton btnArqGer = new JButton("...");
		btnArqGer.setBounds(361, 32, 25, 22);
		contentPanel.add(btnArqGer);

		JLabel lblBolEm = new JLabel("Boletos Emitidos:");
		lblBolEm.setBounds(10, 62, 94, 22);
		contentPanel.add(lblBolEm);

		tfBolEm = new JTextField();
		tfBolEm.setBounds(102, 62, 261, 20);
		tfBolEm.setText(AppletBoletosController.appletBoleto.getBoletosPDFPath());
		contentPanel.add(tfBolEm);
		tfBolEm.setColumns(10);

		JButton btnBolEm = new JButton("...");
		btnBolEm.setBounds(361, 62, 25, 22);
		contentPanel.add(btnBolEm);

		JLabel lblHistBol = new JLabel("Hist\u00F3rico de Boletos:");
		lblHistBol.setBounds(10, 92, 105, 14);
		contentPanel.add(lblHistBol);

		tfHistBol = new JTextField();
		tfHistBol.setBounds(112, 92, 251, 20);
		tfHistBol.setText(AppletBoletosController.appletBoleto.getBoletosGeradosPath());
		contentPanel.add(tfHistBol);
		tfHistBol.setColumns(10);

		JButton btnHistBol = new JButton("...");
		btnHistBol.setBounds(361, 92, 25, 22);
		contentPanel.add(btnHistBol);

		JLabel lblFalhaBol = new JLabel("Boletos com Falha:");
		lblFalhaBol.setBounds(10, 122, 96, 14);
		contentPanel.add(lblFalhaBol);

		tfFalhaBol = new JTextField();
		tfFalhaBol.setBounds(102, 122, 261, 20);
		tfFalhaBol.setText(AppletBoletosController.appletBoleto.getBoletosFalhaPath());
		contentPanel.add(tfFalhaBol);
		tfFalhaBol.setColumns(10);

		JButton btnFalhaBol = new JButton("...");
		btnFalhaBol.setBounds(361, 122, 25, 22);
		contentPanel.add(btnFalhaBol);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(462, 62, 219, 65);
		contentPanel.add(panel);
		panel.setLayout(null);

		JCheckBox chkBoxHistorico = new JCheckBox("Habilitar Hist\u00F3rico de Boletos Emitidos");
		chkBoxHistorico.setBounds(6, 7, 207, 23);
		chkBoxHistorico.setSelected(AppletBoletosController.appletBoleto.getGeneratorBkp());
		panel.add(chkBoxHistorico);

		JCheckBox chkBoxAutoGen = new JCheckBox("Gerar Boletos a cada");
		chkBoxAutoGen.setBounds(6, 33, 125, 23);
		chkBoxAutoGen.setSelected(AppletBoletosController.appletBoleto.getAutoGenerate());
		panel.add(chkBoxAutoGen);

		tfTimeGen = new JTextField();
		tfTimeGen.setHorizontalAlignment(SwingConstants.CENTER);
		tfTimeGen.setBounds(136, 34, 23, 20);
		tfTimeGen.setText(AppletBoletosController.appletBoleto.getGenerateTimer().toString());
		panel.add(tfTimeGen);
		tfTimeGen.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("minutos");
		lblNewLabel_4.setBounds(167, 37, 46, 14);
		panel.add(lblNewLabel_4);

		JLabel lblNewLabel_3 = new JLabel("Op\u00E7\u00F5es de Emiss\u00E3o");
		lblNewLabel_3.setEnabled(false);
		lblNewLabel_3.setBounds(462, 46, 105, 14);
		contentPanel.add(lblNewLabel_3);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 165, 684, 12);
		contentPanel.add(separator_1);

		JLabel lblNewLabel_5 = new JLabel("Conex\u00F5es");
		lblNewLabel_5.setEnabled(false);
		lblNewLabel_5.setBounds(10, 147, 62, 14);
		contentPanel.add(lblNewLabel_5);

		JLabel lblIpServidor = new JLabel("IP Servidor:");
		lblIpServidor.setBounds(10, 188, 62, 14);
		contentPanel.add(lblIpServidor);

		tfServerIP = new JTextField();
		tfServerIP.setBounds(70, 185, 86, 20);
		tfServerIP.setText(AppletBoletosController.appletBoleto.getServerIP());
		contentPanel.add(tfServerIP);
		tfServerIP.setColumns(10);

		JLabel lblPorta = new JLabel("Porta:");
		lblPorta.setBounds(166, 188, 46, 14);
		contentPanel.add(lblPorta);

		tfSvPorta = new JTextField();
		tfSvPorta.setBounds(198, 185, 33, 20);
		tfSvPorta.setText(AppletBoletosController.appletBoleto.getServerPort());
		contentPanel.add(tfSvPorta);
		tfSvPorta.setColumns(10);

		JLabel lblUsurio = new JLabel("Usu\u00E1rio:");
		lblUsurio.setBounds(10, 221, 46, 14);
		contentPanel.add(lblUsurio);

		tfUserField = new JTextField();
		tfUserField.setBounds(50, 218, 144, 20);
		tfUserField.setText(AppletBoletosController.appletBoleto.getAppletUsername());
		contentPanel.add(tfUserField);
		tfUserField.setColumns(10);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(10, 249, 46, 14);
		contentPanel.add(lblSenha);

		tfPassField = new JPasswordField();
		tfPassField.setBounds(48, 246, 144, 20);
		tfPassField.setText(AppletBoletosController.appletBoleto.getAppletPassword());
		contentPanel.add(tfPassField);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(309, 187, 385, 100);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_6 = new JLabel("SMTP:");
		lblNewLabel_6.setBounds(10, 11, 46, 14);
		panel_1.add(lblNewLabel_6);

		tfIPSmtp = new JTextField();
		tfIPSmtp.setBounds(41, 8, 138, 20);
		panel_1.add(tfIPSmtp);
		tfIPSmtp.setColumns(10);

		JLabel lblPorta_1 = new JLabel("Porta:");
		lblPorta_1.setBounds(189, 11, 46, 14);
		panel_1.add(lblPorta_1);

		tfPortSmtp = new JTextField();
		tfPortSmtp.setBounds(225, 8, 46, 20);
		panel_1.add(tfPortSmtp);
		tfPortSmtp.setColumns(10);

		ButtonGroup protGrp = new ButtonGroup();

		JRadioButton rdbtnTls = new JRadioButton("TLS");
		rdbtnTls.setBounds(285, 7, 46, 23);
		panel_1.add(rdbtnTls);

		JRadioButton rdbtnSsl = new JRadioButton("SSL");
		rdbtnSsl.setBounds(333, 7, 46, 23);
		panel_1.add(rdbtnSsl);

		protGrp.add(rdbtnSsl);
		protGrp.add(rdbtnTls);

		JLabel lblEmail = new JLabel("E-Mail:");
		lblEmail.setBounds(10, 36, 37, 14);
		panel_1.add(lblEmail);

		tfEmail = new JTextField();
		tfEmail.setBounds(41, 33, 153, 20);
		panel_1.add(tfEmail);
		tfEmail.setColumns(10);

		JLabel lblSenha_1 = new JLabel("Senha:");
		lblSenha_1.setBounds(199, 36, 46, 14);
		panel_1.add(lblSenha_1);

		tfMailPass = new JPasswordField();
		tfMailPass.setBounds(235, 33, 140, 20);
		panel_1.add(tfMailPass);

		JCheckBox chkBoxAutoMail = new JCheckBox("Habilitar envio autom\u00E1tico de Boletos via E-Mail");
		chkBoxAutoMail.setBounds(10, 70, 251, 23);
		panel_1.add(chkBoxAutoMail);

		JLabel lblOpesDeEmail = new JLabel("Op\u00E7\u00F5es de E-Mail");
		lblOpesDeEmail.setEnabled(false);
		lblOpesDeEmail.setBounds(309, 173, 94, 14);
		contentPanel.add(lblOpesDeEmail);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 302, 684, 12);
		contentPanel.add(separator_2);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		JLabel lblCerrado = new JLabel("CerradoWeb Solu\u00E7\u00F5es em Software");
		buttonPane.add(lblCerrado);
		lblCerrado.setEnabled(false);

		JButton okButton = new JButton("Salvar");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancelar");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		/* Eventos */
		
		/* Botões */
		okButton.addActionListener(btn -> {});
		cancelButton.addActionListener( btn -> this.dispose() );		

	}
	
	
	private void validateAndUpdateConfigs(){
		
		Map<String,Object> params = new HashMap<String,Object>();
				
		Arrays.asList( this.getContentPane().getComponents() )
		      .stream()
		      .filter( cmp ->  cmp instanceof JTextField  || cmp instanceof JPasswordField )
		      .forEach( comp -> {
		    	  
		    	 	    	  
		                         
		     }    
	    );
			
			
			
	    /*private JTextField tfArqGer;
		private JTextField tfBolEm;
		private JTextField tfHistBol;
		private JTextField tfFalhaBol;
		private JTextField tfTimeGen;
		private JTextField tfServerIP;
		private JTextField tfSvPorta;
		private JTextField tfUserField;
		private JPasswordField tfPassField;
		private JTextField tfIPSmtp;
		private JTextField tfPortSmtp;
		private JTextField tfEmail;
		private JPasswordField tfMailPass;*/
		
		
		
		
	}
	
}
