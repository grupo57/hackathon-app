package br.com.fiap.soat07.hackathon.infra.service;

import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.gateway.CriptografiaGateway;
import br.com.fiap.soat07.hackathon.core.gateway.TokenUtil;
import br.com.fiap.soat07.hackathon.core.gateway.UsuarioGateway;
import br.com.fiap.soat07.hackathon.core.usecase.LoginUseCase;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AutenticacaoService {

    private final TokenUtil tokenUtil;
    private final LoginUseCase loginUseCase;
    private final UsuarioGateway usuarioGateway;

    public AutenticacaoService(TokenUtil tokenUtil, CriptografiaGateway criptografiaGateway, UsuarioGateway usuarioGateway) {
        this.tokenUtil = tokenUtil;
        this.loginUseCase = new LoginUseCase(criptografiaGateway, usuarioGateway);
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario login(String login, String senha) {
        return loginUseCase.execute(login, senha);
    }

    public String gerarToken(String login, String senha) {
        Usuario usuario = loginUseCase.execute(login, senha);
        return tokenUtil.generateToken(usuario);
    }

    public String gerarToken(Usuario usuario) {
        return tokenUtil.generateToken(usuario);
    }

    public Optional<Usuario> getAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return Optional.ofNullable((Usuario) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public Optional<Usuario> getUsuario(String token) {
        String email = tokenUtil.extrairEmail(token);
        return usuarioGateway.find(email);
    }

}
