package br.com.fiap.soat07.hackathon.core.exception;

public class ErroAoRecuperarArquivoException extends StorageException {

    private final String id;

    public ErroAoRecuperarArquivoException(String id) {
        super("Arquivo não encontrado");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
