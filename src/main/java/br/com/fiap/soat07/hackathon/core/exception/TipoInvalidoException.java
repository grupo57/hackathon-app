package br.com.fiap.soat07.hackathon.core.exception;

public class TipoInvalidoException extends StorageException {

    private final String nomeDoArquivo;

    public TipoInvalidoException(String nomeDoArquivo) {
        super("Tipo do arquivo não suportado");
        this.nomeDoArquivo = nomeDoArquivo;
    }

    public String getNomeDoArquivo() {
        return nomeDoArquivo;
    }
}
