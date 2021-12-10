package br.com.zupproject.mailer.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.zupproject.mailer.requests.EventoDeTransacao;

@Entity
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String mensagem;
	private String assunto;
	private String remetente;
	@Column(nullable = false)
	private String destinatario;
	@Column(nullable = false, unique = true)
	private String idMensagem;

	private LocalDateTime horaEnvio = LocalDateTime.now();

	@Deprecated
	public Email() {
	}

	private Email(EventoDeTransacao evento) {
		this.mensagem = evento.getMensagem();
		this.assunto = evento.getAssunto();
		this.remetente = evento.getRemetente();
		this.destinatario = evento.getDestinatario();
		this.idMensagem = evento.getIdMensagem();
	}

	public static Email montaEmail(EventoDeTransacao evento) {
		return new Email(evento);
	}

	public String getMensagem() {
		return mensagem;
	}

	public String getAssunto() {
		return assunto;
	}

	public String getRemetente() {
		return remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public String getIdMensagem() {
		return idMensagem;
	}
}
