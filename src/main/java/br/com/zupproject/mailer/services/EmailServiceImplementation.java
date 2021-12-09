package br.com.zupproject.mailer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.zupproject.mailer.models.Email;

@Service
public class EmailServiceImplementation  {

	@Autowired
	private JavaMailSender sender;
	
	public void sendEmail(Email email) {
		
		SimpleMailMessage mensagem = new SimpleMailMessage();
		mensagem.setFrom(email.getRemetente());
		mensagem.setTo(email.getDestinatario());
		mensagem.setSubject(email.getAssunto());
		mensagem.setText(email.getMensagem());
		
		sender.send(mensagem);
	}
}
