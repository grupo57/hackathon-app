package br.com.fiap.soat07.hackathon.core.exception;

public class ArquivoNaoEncontradoException extends StorageException {

    private final String id;

    public ArquivoNaoEncontradoException(String id) {
        super("Arquivo não encontrado");
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
