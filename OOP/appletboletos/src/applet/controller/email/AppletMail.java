package applet.controller.email;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import applet.controller.AppletBoletosController;
import applet.model.email.DestinatarioObject;
import applet.model.email.MailTemplates;

public class AppletMail {

	private String sender;

	/* Infos Servidor */
	private Properties props = new Properties();
	private Session session;

	public AppletMail() {

		initProperties();
		initSession();

	}

	protected void initProperties() {

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", AppletBoletosController.appletBoleto.getIpSmtp());
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", AppletBoletosController.appletBoleto.getIpSmtp());

	}

	protected void initSession() {

		session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(AppletBoletosController.appletBoleto.getEmail(),
						AppletBoletosController.appletBoleto.getEmailPassword());
			}
		});

		// session.setDebug(true);
		setSender(AppletBoletosController.appletBoleto.getEmail());

	}


	public void sendMail( List<DestinatarioObject> destinatarios , MailTemplates template ) {

		try {

			MimeMessage message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();

			BodyPart messageBody = new MimeBodyPart();

			message.setFrom(new InternetAddress(getSender()));
	
			message.setSubject(MimeUtility.encodeText(template.header, "utf-8", "B"));

			for (DestinatarioObject str : destinatarios) {
				
				message.addRecipient(Message.RecipientType.TO, str.getEmail() );
				
				if(!str.getBoletos().isEmpty()){
					
					messageBody.setText(template.message);
					multipart.addBodyPart(messageBody);
					
					str.getBoletos().parallelStream().filter( path -> !path.equals("") )
					                                 .forEach( mail -> addAttachment(multipart,mail) );
					
					message.setContent(multipart);
					
				} else {
					message.setText(template.message);
				}
				
				Transport.send(message);

			}

		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}


	private static void addAttachment(Multipart multipart, String filename) {
		DataSource source = new FileDataSource(filename);
		BodyPart messageBodyPart = new MimeBodyPart();
		
		try {
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

}
