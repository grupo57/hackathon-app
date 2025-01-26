package br.com.fiap.soat07.hackathon.core.exception;

public class SenhaInvalidaException extends AutenticacaoException {
    private final String login;

    public SenhaInvalidaException(String login) {
        super("Senha não informada");
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
