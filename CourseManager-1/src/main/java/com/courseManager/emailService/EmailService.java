package com.courseManager.emailService;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	public String sendEmain(String subject,String Message,String to) {
		//variable for gmail
		String host="smtp.gmail.com";
		String from="noreply.coursemanage@gmail.com";
		Properties property= System.getProperties();
		
		// setting imp information to properties object
		
		
		//host set
		property.put("mail.smtp.host", host);
		property.put("mail.smtp.port", "465");
		property.put("mail.smtp.ssl.enable", "true");
		property.put("mail.smtp.auth", "true");
		
		
		
		//Step:1 to get  the session object.....
		
		Session session= Session.getInstance(property, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from,"*&La*&1994*&2502");
			}
			
		});
		session.setDebug(true);
		
		
		// Step:2 compose the message........
		
		MimeMessage msg=new MimeMessage(session);
		
		try {
			// from email.....
			
			msg.setFrom(from);
			
			// add recipient.....
			
			msg.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
			
			
			//add subject to ....
			msg.setSubject(subject);
			
			//adding text to message.....
			msg.setContent(Message, "text/html");
			
			
			// sent message .......
			
			Transport.send(msg);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	return "";	
	}
}
