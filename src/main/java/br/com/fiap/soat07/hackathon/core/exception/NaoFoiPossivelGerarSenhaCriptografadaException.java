package br.com.fiap.soat07.hackathon.core.exception;

public class NaoFoiPossivelGerarSenhaCriptografadaException extends AutenticacaoException {

    public NaoFoiPossivelGerarSenhaCriptografadaException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }
}
