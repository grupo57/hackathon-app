package br.com.fiap.soat07.hackathon.core.exception;

public class SistemaDeArquivosException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SistemaDeArquivosException(String mensagem, Throwable cause) {
		super(mensagem, cause);
	}

	public SistemaDeArquivosException(String mensagem) {
		super(mensagem);
	}

}