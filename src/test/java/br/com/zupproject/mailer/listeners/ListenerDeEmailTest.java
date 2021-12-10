package br.com.zupproject.mailer.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sun.mail.util.MailConnectException;

import br.com.zupproject.mailer.models.Email;
import br.com.zupproject.mailer.repositories.EmailRepository;
import br.com.zupproject.mailer.requests.EventoDeTransacao;

@SpringBootTest
@Transactional
class ListenerDeEmailTest {

	private EventoDeTransacao eventoDeTransacao;
	private EventoDeTransacao novoEventoDeTransacao;
	private String uuidEventoSalvo = UUID.randomUUID().toString();
	private String uuidNovoEvento = UUID.randomUUID().toString();

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private ListenerDeEmail listener;

	@BeforeEach
	void setUp() {
		eventoDeTransacao = Mockito.mock(EventoDeTransacao.class);
		Mockito.when(eventoDeTransacao.getAssunto()).thenReturn("Sucesso ao pagar boleto");
		Mockito.when(eventoDeTransacao.getDestinatario()).thenReturn("joaodostestes@email.com");
		Mockito.when(eventoDeTransacao.getIdMensagem()).thenReturn(uuidEventoSalvo);
		Mockito.when(eventoDeTransacao.getMensagem()).thenReturn("O boleto de numero 12345 foi pago com sucesso.");
		Mockito.when(eventoDeTransacao.getRemetente()).thenReturn("naoresponda@emailot9.com");

		novoEventoDeTransacao = Mockito.mock(EventoDeTransacao.class);
		Mockito.when(novoEventoDeTransacao.getAssunto()).thenReturn("Sucesso ao pagar boleto");
		Mockito.when(novoEventoDeTransacao.getDestinatario()).thenReturn("joaodostestes@email.com");
		Mockito.when(novoEventoDeTransacao.getIdMensagem()).thenReturn(uuidNovoEvento);
		Mockito.when(novoEventoDeTransacao.getMensagem()).thenReturn("O boleto de numero 54321 foi pago com sucesso.");
		Mockito.when(novoEventoDeTransacao.getRemetente()).thenReturn("naoresponda@emailot9.com");

		Email email = Email.montaEmail(eventoDeTransacao);

		emailRepository.save(email);
	}

	@Test
	void deveGravarUmNovoEmailNoBancoDeDadosAoReceberUmaMensagem() {

		listener.ouvir(novoEventoDeTransacao);

		Optional<Email> possivelEmail = emailRepository.findByIdMensagem(uuidNovoEvento);
		if (possivelEmail.isPresent()) {
			assertEquals(novoEventoDeTransacao.getIdMensagem(), possivelEmail.get().getIdMensagem());
		} else {
			fail("Não foi encontrado no banco o email");
		}
	}

	@Test
	void naoDeveGravarUmEmailNoBancoDeDadosAoReceberUmaMensagemDuplicada() {

		EventoDeTransacao eventoRepetido = Mockito.mock(EventoDeTransacao.class);
		Mockito.when(eventoRepetido.getAssunto()).thenReturn("Sucesso ao pagar boleto");
		Mockito.when(eventoRepetido.getDestinatario()).thenReturn("joaodostestes@email.com");
		Mockito.when(eventoRepetido.getIdMensagem()).thenReturn(uuidEventoSalvo);
		Mockito.when(eventoRepetido.getMensagem()).thenReturn("Mensagem distinta da primeira, mas o uuid é o mesmo");
		Mockito.when(eventoRepetido.getRemetente()).thenReturn("naoresponda@emailot9.com");

		listener.ouvir(eventoRepetido);

		Optional<Email> possivelEmail = emailRepository.findByIdMensagem(uuidEventoSalvo);
		if (possivelEmail.isPresent()) {
			assertNotEquals(eventoRepetido.getMensagem(), possivelEmail.get().getMensagem());
		} else {
			fail("Não foi encontrado no banco o id");
		}
	}
	
	@Test
	void deveCapturarUmaExcecaoENaoSalvarOEmailNoBancoDeDados() {
		
		Mockito.when(Email.montaEmail(novoEventoDeTransacao)).thenThrow(IllegalStateException.class);

		listener.ouvir(novoEventoDeTransacao);
		
		Optional<Email> possivelEmail = emailRepository.findByIdMensagem(uuidNovoEvento);
		if (possivelEmail.isEmpty()) {
			assertTrue(true);
		} else {
			System.out.print(possivelEmail.get().getIdMensagem());
			fail("Não foi encontrado no banco o id");
		}
	}
}
