package br.com.fiap.soat07.hackathon.core.exception;

public class UsuarioNaoAtivoException extends AutenticacaoException {
    private final String login;

    public UsuarioNaoAtivoException(String login) {
        super("Usuário não ativo");
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

}
