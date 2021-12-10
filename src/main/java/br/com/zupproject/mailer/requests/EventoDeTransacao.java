package br.com.zupproject.mailer.requests;

public class EventoDeTransacao {

	private String mensagem;
	private String assunto;
	private String remetente;
	private String destinatario;
	private String idMensagem;

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
