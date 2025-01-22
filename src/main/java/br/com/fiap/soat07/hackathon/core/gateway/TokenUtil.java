package br.com.fiap.soat07.hackathon.core.gateway;

import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface TokenUtil {

    /**
     * Gera um token com base nos dados do usuário
     * @param usuario Usuario
     * @return token
     */
    String generateToken(Usuario usuario);

    /**
     * Extrai o email codificado no token.
     *
     * @param token Token.
     * @return email.
     */
    String extrairEmail(String token);

    /**
     * Valida o token, verificando o nome de usuário e a expiração.
     *
     * @param token   Token.
     * @param usuario Dados do usuário esperado.
     * @return true se o token for válido, caso contrário false.
     */
    boolean validateToken(String token, Usuario usuario);

    /**
     * Verifica se o token está expirado.
     *
     * @param token Token.
     * @return true se o token estiver expirado, caso contrário false.
     */
    boolean isTokenExpired(String token);

}
