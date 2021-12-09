package br.com.zupproject.mailer.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zupproject.mailer.models.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {

	Optional<Email> findByIdMensagem(String idMensagem);
}
