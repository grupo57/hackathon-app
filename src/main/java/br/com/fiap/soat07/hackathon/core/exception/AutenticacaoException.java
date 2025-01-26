package br.com.fiap.soat07.hackathon.core.exception;

public class AutenticacaoException extends BaseException{

    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }

    public AutenticacaoException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }
}
