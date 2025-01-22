package br.com.fiap.soat07.hackathon.core.exception;

public class UsuarioNaoEncontradoException extends AutenticacaoException {

    private final String login;

    public UsuarioNaoEncontradoException(String login) {
        super("Usuário não encontrado");
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

}
