package applet.model;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * @author Ygor Castor
 * @since 18/12/2014
 * @version 1.2
 * 
 * Classe manipuladora do arquivo de configuração
 * 
 */
public class Options {
	
	private JSONObject optionsFile;	
	private String serverIP;
	private String serverPort;
	private String appletUsername;
	private String appletPassword;
	
	private String boletosGeneratorPath;
	private String boletosPDFPath;
	private String boletosFalhaPath;
	private String boletosGeradosPath;
	
	private Boolean authenticated;	
	private Boolean autoGenerate;
	private Boolean generatorBkp;
	private Integer generateTimer;
	
	private Boolean sendMail;
	private String ipSmtp;
	private Integer mailPort;
	private String email;
	private String emailPassword;
	
	
	public Options() {
				
		
	}
	
	/*
	 * @author Ygor Castor
	 * @since 18/12/2014
	 * @version 1.0.3
	 * 
	 * Abre o arquivo de configuração e realiza o parsing das opções.
	 */
	@SuppressWarnings("unchecked")
	public void openOptionsFile(){
		
		JSONParser parser = new JSONParser();		
				
		try {
			
			Object file = parser.parse(new FileReader(getClass().getResource("/conf/options.json").getPath().replaceAll("%20", " ")));			
			optionsFile = (JSONObject)file;
									
			this.serverIP = (String)optionsFile.getOrDefault("serverip", "127.0.0.1");
			this.serverPort = (String)optionsFile.getOrDefault("serverport", 2000 );
			this.boletosGeneratorPath = (String)optionsFile.getOrDefault("generatorFilesPath", "C:\\Boletos\\generatorFiles");
			this.boletosPDFPath = (String)optionsFile.getOrDefault("pdfPath", "C:\\Boletos\\boletosGerados");
			this.boletosFalhaPath = (String)optionsFile.getOrDefault("failedBoletos", "C:\\Boletos\\boletosFalha" );
			this.boletosGeradosPath = (String)optionsFile.getOrDefault("boletosGerados", "C:\\Boletos\\boletosAntigos");
			this.appletUsername = (String)optionsFile.get("username");
			this.appletPassword = (String)optionsFile.get("password");
			this.autoGenerate = (Boolean)optionsFile.getOrDefault("autoGenerate", Boolean.TRUE );
			this.generatorBkp = (Boolean)optionsFile.getOrDefault("generatorBkp", Boolean.TRUE );
			this.generateTimer = (Integer)optionsFile.getOrDefault("generatorTimer", Integer.parseInt("15") );
			this.sendMail = ((Boolean) optionsFile.getOrDefault("sendMail", Boolean.FALSE) );
			this.ipSmtp = (String)optionsFile.get("ipsmtp");
			this.mailPort = Integer.parseInt((String)optionsFile.get("mailport"));
			this.email = (String)optionsFile.get("email");
			this.emailPassword = (String)optionsFile.get("emailPass");
			
		
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}		
		
	}
	
	public void updateOptionsFile(){
		
			
		
		
	}

	public JSONObject getOptionsFile() {
		return optionsFile;
	}

	public void setOptionsFile(JSONObject optionsFile) {
		this.optionsFile = optionsFile;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getAppletUsername() {
		return appletUsername;
	}

	public void setAppletUsername(String appletUsername) {
		this.appletUsername = appletUsername;
	}

	public String getAppletPassword() {
		return appletPassword;
	}

	public void setAppletPassword(String appletPassword) {
		this.appletPassword = appletPassword;
	}

	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getBoletosGeneratorPath() {
		return boletosGeneratorPath;
	}

	public void setBoletosGeneratorPath(String boletosGeneratorPath) {
		this.boletosGeneratorPath = boletosGeneratorPath;
	}

	public String getBoletosPDFPath() {
		return boletosPDFPath;
	}

	public void setBoletosPDFPath(String boletosPDFPath) {
		this.boletosPDFPath = boletosPDFPath;
	}

	public String getBoletosFalhaPath() {
		return boletosFalhaPath;
	}

	public void setBoletosFalhaPath(String boletosFalhaPath) {
		this.boletosFalhaPath = boletosFalhaPath;
	}

	public String getBoletosGeradosPath() {
		return boletosGeradosPath;
	}

	public void setBoletosGeradosPath(String boletosGeradosPath) {
		this.boletosGeradosPath = boletosGeradosPath;
	}

	/**
	 * @return the autoGenerate
	 */
	public Boolean getAutoGenerate() {
		return autoGenerate;
	}

	/**
	 * @param autoGenerate the autoGenerate to set
	 */
	public void setAutoGenerate(Boolean autoGenerate) {
		this.autoGenerate = autoGenerate;
	}

	/**
	 * @return the generatorBkp
	 */
	public Boolean getGeneratorBkp() {
		return generatorBkp;
	}

	/**
	 * @param generatorBkp the generatorBkp to set
	 */
	public void setGeneratorBkp(Boolean generatorBkp) {
		this.generatorBkp = generatorBkp;
	}

	/**
	 * @return the generateTimer
	 */
	public Integer getGenerateTimer() {
		return generateTimer;
	}

	/**
	 * @param generateTimer the generateTimer to set
	 */
	public void setGenerateTimer(Integer generateTimer) {
		this.generateTimer = generateTimer;
	}

	public String getIpSmtp() {
		return ipSmtp;
	}

	public void setIpSmtp(String ipSmtp) {
		this.ipSmtp = ipSmtp;
	}

	public Integer getMailPort() {
		return mailPort;
	}

	public void setMailPort(Integer mailPort) {
		this.mailPort = mailPort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	/**
	 * @return the sendMail
	 */
	public Boolean getSendMail() {
		return sendMail;
	}

	/**
	 * @param sendMail the sendMail to set
	 */
	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}
	
	

}
