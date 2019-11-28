package jp.profield.chat.service.impl;

import java.util.Properties;

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

import jp.profield.chat.service.MailSendService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MailSendServiceImpl implements MailSendService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	final String userName = "profieldchat@gmail.com";
	final String password = "profield@123";
	
	
	@Override
	public void sendingInvitationToNewUser(String emailId,String subject, String messageContentOTP,String userFullName) {

		Multipart mp = new MimeMultipart();
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.smtp.host", "appscook.com");
		props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587"); //gmail default port
		/*props.put("mail.smtp.port", "465");*///Web mail SMTP-SSL port
		//props.put("mail.smtp.port", "26");// Web mail most commenly used SMTP port
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
		}
		});

		try {
		// Set the email attachment file
		    //   MimeBodyPart attachmentPart = new MimeBodyPart();
		         //  attachmentPart.setFileName(file.getName());
		     //  attachmentPart.attachFile(file);

		           // set message body part
			
			String htmlMsg;
			htmlMsg = "<br><p> Hi user,</p>";
			
			htmlMsg += "<p> " + userFullName + " with Profield Corporation has invited you to use the Profield Chat Application to collaborate with them. </p>";
			htmlMsg += "<p> Use the link below to set up your account and get started! </p>";
		    htmlMsg += "<p> https://www.hikari.in/profield-chat/register  </p>";
			htmlMsg += "<p>If you have any questions for " + userFullName + ",you can reply to this email and it will go right to them.  </p>";
			
			htmlMsg += "</br>";
			htmlMsg += "</br>";
			htmlMsg += "</br>";
			
			htmlMsg += "<p>Welcome onboard!</p></br><p>Profield Corporation</p> </br><p>3-5-11 Michi-machi, </p></br>";
			htmlMsg += "<p>Chuo-ku, </p></br><p>Osaka-shi 541-0045 </p></br><p>https://www.profield.jp/</p></br>";

		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(htmlMsg, "text/html");
		mp.addBodyPart(bodyPart);
		//mp.addBodyPart(attachmentPart);
		//set message
		Message message = new MimeMessage(session);
		message.setSubject(subject);
		message.setContent(mp);
		message.setFrom(new InternetAddress(userName));
		message.setRecipients(Message.RecipientType.TO,
		InternetAddress.parse(emailId));

		Transport.send(message);
		//log.info("Done..! Email was successfully sent on-"+student.getParent().getEmailId());
		//file.delete();
		} catch (MessagingException e) {
		throw new RuntimeException(e);
		}
	
	
		
	}


	@Override
	public void sendWelcomeMailToNewUser(String emailId, String userNameOfUser,
			String passwordOfUser, String userFullName) {
		
		log.info("Start Welcome message sending  to"+userFullName);
		
		Multipart mp = new MimeMultipart();
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.smtp.host", "appscook.com");
		props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587"); //gmail default port
		/*props.put("mail.smtp.port", "465");*///Web mail SMTP-SSL port
		//props.put("mail.smtp.port", "26");// Web mail most commenly used SMTP port
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
		}
		});

		try {
		// Set the email attachment file
		    //   MimeBodyPart attachmentPart = new MimeBodyPart();
		         //  attachmentPart.setFileName(file.getName());
		     //  attachmentPart.attachFile(file);

		           // set message body part
			
			String htmlMsg;
			htmlMsg = "<br><p> Hi "+userFullName+"</p>";
			
			htmlMsg += "<p>Thanks so much for joining into the Profield Chat Application!. </p>";
			htmlMsg += "<p>You can login to the chat application using the following URL: </p>";
		    htmlMsg += "<p> https://www.hikari.in/profield-chat/login  </p>";
			htmlMsg += "<p>Login user id : "+userNameOfUser+"  </p>";
			htmlMsg += "<p>Password : "+passwordOfUser+"  </p></br>";
			
			htmlMsg += "<p>Have an awesome day!</p></br>";
			htmlMsg += "</br>";
			htmlMsg += "</br>";
			
			htmlMsg += "<p>Profield Corporation</p> </br><p>3-5-11 Michi-machi, </p></br>";
			htmlMsg += "<p>Chuo-ku, </p></br><p>Osaka-shi 541-0045 </p></br><p>https://www.profield.jp/</p></br>";

		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(htmlMsg, "text/html");
		mp.addBodyPart(bodyPart);
		//mp.addBodyPart(attachmentPart);
		//set message
		Message message = new MimeMessage(session);
		message.setSubject("Welcome to Profield Chat!!!");
		message.setContent(mp);
		message.setFrom(new InternetAddress(userName));
		message.setRecipients(Message.RecipientType.TO,
		InternetAddress.parse(emailId));

		Transport.send(message);
		 log.info("Done..! Email was successfully sent on-"+userFullName);
		//file.delete();
		} catch (MessagingException e) {
			 log.info("Error in mailSending "+userFullName );
			 log.info (e.getMessage());
		throw new RuntimeException(e);
		}
	
		
	}


	@Override
	public void sendTokenForForgotPassword(String userFullName, String emailId,
			long token) {
		


		Multipart mp = new MimeMultipart();
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.smtp.host", "appscook.com");
		props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587"); //gmail default port
		/*props.put("mail.smtp.port", "465");*///Web mail SMTP-SSL port
		//props.put("mail.smtp.port", "26");// Web mail most commenly used SMTP port
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
		}
		});

		try {
		// Set the email attachment file
		    //   MimeBodyPart attachmentPart = new MimeBodyPart();
		         //  attachmentPart.setFileName(file.getName());
		     //  attachmentPart.attachFile(file);

		           // set message body part
			
			String htmlMsg;
			htmlMsg = "<br><p> Hi "+userFullName+"</p>";
			
			htmlMsg += "<p>You have requested to reset your password.  </p>";
			htmlMsg += "<p>The One Time Password (OTP) for resetting your password is : "+token+"</p></br>";
		    htmlMsg += "<p> Please ignore this email if you did not request a password change  </p>";
		
			
			htmlMsg += "<p>Have an awesome day!</p></br>";
			htmlMsg += "</br>";
			htmlMsg += "</br>";
			
			htmlMsg += "<p>Profield Corporation</p> </br><p>3-5-11 Michi-machi, </p></br>";
			htmlMsg += "<p>Chuo-ku, </p></br><p>Osaka-shi 541-0045 </p></br><p>https://www.profield.jp/</p></br>";

		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(htmlMsg, "text/html");
		mp.addBodyPart(bodyPart);
		//mp.addBodyPart(attachmentPart);
		//set message
		Message message = new MimeMessage(session);
		message.setSubject("Password Reset Requested");
		message.setContent(mp);
		message.setFrom(new InternetAddress(userName));
		message.setRecipients(Message.RecipientType.TO,
		InternetAddress.parse(emailId));

		Transport.send(message);
		//log.info("Done..! Email was successfully sent on-"+student.getParent().getEmailId());
		//file.delete();
		} catch (MessagingException e) {
		throw new RuntimeException(e);
		}
	
		
	
		
	}

}
