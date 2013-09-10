package principal;

import java.awt.Component;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	
	private String servidorSmtp;
	private String portaServidor;
	private String emailFrom;
	private String senha;
	private boolean sucesso = false;
	
	public Email(String sv, String pt, String eF, String pass) {
		servidorSmtp = sv;
		portaServidor = pt;
		emailFrom = eF;
		senha = pass;
	}
 
	public void sendMail(String emailTo, String assunto, String texto) {
  
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", servidorSmtp);
		props.put("mail.smtp.port", portaServidor);

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailFrom, senha);
			}
		  });
 
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailFrom));
			message.setRecipients(Message.RecipientType.TO,
									InternetAddress.parse(emailTo));
			message.setSubject(assunto);
			message.setText(texto);
 
			Transport.send(message);
			sucesso = true;
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean getSucesso() {
		return sucesso;
	}
}
