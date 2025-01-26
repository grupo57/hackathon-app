package br.com.fiap.soat07.hackathon.core.exception;

public class StorageException extends BaseException{

    public StorageException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }

    public StorageException(String mensagem) {
        super(mensagem);
    }


}
