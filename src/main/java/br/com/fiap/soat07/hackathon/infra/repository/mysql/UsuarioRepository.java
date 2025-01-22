package br.com.fiap.soat07.hackathon.infra.repository.mysql;

import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoUsuario;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.exception.LoginInvalidoException;
import br.com.fiap.soat07.hackathon.core.exception.UsuarioNaoEncontradoException;
import br.com.fiap.soat07.hackathon.core.gateway.UsuarioGateway;
import br.com.fiap.soat07.hackathon.infra.repository.mysql.model.UsuarioModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class UsuarioRepository implements UsuarioGateway {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Usuario> find(final String email) {
        if (email == null || email.isEmpty())
            throw new LoginInvalidoException();
        String emailTratado = email.trim();

        String sql = """
            SELECT u.*
            FROM hackathon.usuario u
            WHERE u.email = :email
            """;
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        query.setParameter("email", emailTratado);
        try {
            return Stream.of(query.getSingleResult())
                    .map(i -> (Tuple)i)
                    .map(this::mapearParaDTO)
                    .findFirst();
        } catch (NoResultException e) {
            throw new UsuarioNaoEncontradoException(email);
        }
    }

    @Override
    public boolean validar(final UUID codigo) {
        if (codigo == null)
            throw new LoginInvalidoException();

        String sql = """
            SELECT u.*
            FROM hackathon.usuario u
            """;
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        try {
            UsuarioModel usuario = (UsuarioModel) query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            throw new UsuarioNaoEncontradoException(codigo.toString());
        }
    }

    private Usuario mapearParaDTO(Tuple tuple) {
        Long id = tuple.get("id", Long.class);
        String nome = tuple.get("nome", String.class);
        String email = tuple.get("email", String.class);
        String salt = tuple.get("salt", String.class);
        String senha = tuple.get("senha", String.class);
        SituacaoDoUsuario situacao = SituacaoDoUsuario.of(tuple.get("situacao", String.class));
        return new Usuario(id, nome, email, salt, senha, situacao);
    }

}
