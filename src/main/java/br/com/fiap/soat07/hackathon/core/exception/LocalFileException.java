package br.com.fiap.soat07.hackathon.core.exception;

public class LocalFileException extends StorageException {

    private final String filePath;

    public LocalFileException(String filePath, Throwable cause) {
        super("Erro ao criar o arquivo local", cause);
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

}
