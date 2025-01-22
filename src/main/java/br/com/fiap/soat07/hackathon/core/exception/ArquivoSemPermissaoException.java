package br.com.fiap.soat07.hackathon.core.exception;

public class ArquivoSemPermissaoException extends StorageException {

    private final String id;

    public ArquivoSemPermissaoException(String id) {
        super("Sem permissão para acessar esse arquivo");
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
