package com.astrapay.rintis.service.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.astrapay.rintis.domain.Email;
import com.astrapay.rintis.service.EmailService;


import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("emailService")
@Transactional
public class EmailServiceImpl implements EmailService{
    private Properties props;
	private Session session;
    @Override
	public void sendMail(final String to, final String subject, final String content)
			throws Exception {
			new Thread() {
				@SuppressWarnings("deprecation")
				public void run() {
					try {
						final Email mail = new Email();
						mail.setEmailBody(content);
						mail.setEmailSubject(subject);
						mail.setEmailTo(to);
						sendEmailSample(mail);
					} catch (final MessagingException e) {
						e.printStackTrace();
					}
				}
			}.start();
    }
    
    // @Override
	public void sendEmailSample(Email email) throws MessagingException {
		this.office365Smtp(email);
		/* this.fifSmtp(org, email);
		this.sendEmailByCo(org, email); */
    }
    
    public void office365Smtp(final Email email) {
		final String username = "no-reply@fif.co.id"; 
		final String password = "pDLVh4%Xq9";

		props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.auth", "true");
		session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("no-reply@astrapay.com", "ASTRAPAY"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getEmailTo()));
			message.setSubject(email.getEmailSubject());
			message.setContent(email.getEmailBody(), "text/html; charset=utf-8");

			Transport.send(message);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}