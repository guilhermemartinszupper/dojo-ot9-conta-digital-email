package br.com.zupproject.mailer.listeners;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.zupproject.mailer.models.Email;
import br.com.zupproject.mailer.repositories.EmailRepository;
import br.com.zupproject.mailer.requests.EventoDeTransacao;
import br.com.zupproject.mailer.services.EmailServiceImplementation;

@Component
public class ListenerDeEmail {

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private EmailServiceImplementation mailer;

	@KafkaListener(topics = "${spring.kafka.topic.transacao}")
	public void ouvir(EventoDeTransacao evento) {

		final Logger logger = LoggerFactory.getLogger(ListenerDeEmail.class);

		try {
			logger.info("Recebido uma mensagem do kafka para o evento de id " + evento.getIdMensagem());

			Email email = Email.montaEmail(evento);

			Optional<Email> possivelEmail = emailRepository.findByIdMensagem(email.getIdMensagem());
			if (possivelEmail.isPresent()) {
				logger.warn("A mensagem recebida já foi processada anteriormente.");
				return;
			}

			emailRepository.save(email);

			mailer.sendEmail(email);

		} catch (Exception e) {
			logger.error("Falha ao receber a mensagem do evento " + evento.getIdMensagem());
			logger.error("Erro recebido: " + e.getMessage());
		}
	}
}