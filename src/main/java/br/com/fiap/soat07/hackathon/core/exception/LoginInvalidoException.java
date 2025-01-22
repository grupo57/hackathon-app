package br.com.fiap.soat07.hackathon.core.exception;

public class LoginInvalidoException extends AutenticacaoException {

    public LoginInvalidoException() {
        super("Login não informado");
    }
}
