package br.com.fiap.soat07.hackathon.core.gateway;

import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {

    /**
     * Recupera o usuário validado associado a um email
     * @param email email validado do usuário
     */
    Optional<Usuario> find(String email);

    /**
     * Valida um usuário
     * @param codigo UUID para validar o usuário
     */
    boolean validar(UUID codigo);

    /**
     * Recupera o usuário
     * @param id chave primária
     */
    Optional<Usuario> get(Long id);
}
