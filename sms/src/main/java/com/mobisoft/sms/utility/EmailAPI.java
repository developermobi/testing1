package com.mobisoft.sms.utility;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service("emailApi")
public class EmailAPI {
	
	@Autowired
	private MailSender mailSender;
	
	private static final String fromAddress = "pramod.v@mobisofttech.co.in";
	
	public void sendSimpleMail(Map<String,String> emailDetails){
		
		SimpleMailMessage mailMsg = new SimpleMailMessage();
	/*	mailMsg.setFrom(fromAddress);
		mailMsg.setTo(emailDetails.get("toAddress"));
		mailMsg.setReplyTo(fromAddress);
		mailMsg.setBcc("sano07info@gmail.com");
		mailMsg.setCc("sano009info@gmail.com");
		mailMsg.setSubject(emailDetails.get("subject"));
		mailMsg.setText(emailDetails.get("msgBody"));
		System.out.println(mailMsg.toString());	*/	
		
		System.out.println("in mail method call");
		mailMsg.setFrom(fromAddress);
		mailMsg.setTo("tushar.k@mobisofttech.co.in");
		mailMsg.setReplyTo(fromAddress);
		mailMsg.setBcc("sano07info@gmail.com");
		mailMsg.setCc("sano009info@gmail.com");
		mailMsg.setSubject("skdfhkla");
		mailMsg.setText("asjlkdlasdjlkadjlkas alsdjlaks");
		System.out.println(mailMsg.toString());
		
		System.out.println("in mail method call end");
		mailSender.send(mailMsg);
	}
	
}
