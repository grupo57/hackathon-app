package br.com.fiap.soat07.hackathon.core.usecase;

import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoUsuario;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.exception.LoginInvalidoException;
import br.com.fiap.soat07.hackathon.core.exception.SenhaInvalidaException;
import br.com.fiap.soat07.hackathon.core.exception.UsuarioNaoAtivoException;
import br.com.fiap.soat07.hackathon.core.exception.UsuarioNaoEncontradoException;
import br.com.fiap.soat07.hackathon.core.gateway.CriptografiaGateway;
import br.com.fiap.soat07.hackathon.core.gateway.UsuarioGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUseCase.class);
    private final CriptografiaGateway criptografiaGateway;
    private final UsuarioGateway usuarioGateway;

    public LoginUseCase(CriptografiaGateway criptografiaGateway, UsuarioGateway usuarioGateway) {
        this.criptografiaGateway = criptografiaGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario execute(String login, String senha) {
        if (login == null || login.isEmpty())
            throw new LoginInvalidoException();
        if (senha == null || senha.isEmpty())
            throw new SenhaInvalidaException(login);

        Usuario usuario = usuarioGateway.find(login).orElseThrow(() -> new UsuarioNaoEncontradoException(login));
        if (!SituacaoDoUsuario.ATIVO.equals(usuario.getSituacao()))
            throw new UsuarioNaoAtivoException(login);

        String senhaHash = criptografiaGateway.hash(usuario.getSalt(), senha.toCharArray());
        if (!usuario.verificaSenha(senhaHash))
            throw new SenhaInvalidaException(login);

        LOGGER.info("'{}' fez login", login);
        return usuario;
    }

}
