package br.com.fiap.soat07.hackathon.core.domain.entity;

import java.util.Optional;

public enum TipoDoArquivo {
    VIDEO_MP4   ("mp4"),
    VIDEO_MKV   ("mkv"),
    ZIP_IMAGENS ("zip");

    private final String prefixo;

    TipoDoArquivo(String prefixo) {
        this.prefixo = prefixo;
    }

    public String getSufixo() {
        return prefixo;
    }

    public boolean isVideo() {
        return VIDEO_MP4.equals(this) || VIDEO_MKV.equals(this);
    }

    public boolean isZip() {
        return ZIP_IMAGENS.equals(this);
    }

    public static Optional<TipoDoArquivo> extrairTipo(String nomeDoArquivo) {
        if (nomeDoArquivo == null || nomeDoArquivo.lastIndexOf('.') == -1)
            return Optional.empty();
        String tipo = nomeDoArquivo.substring(nomeDoArquivo.lastIndexOf('.') + 1);
        return Optional.ofNullable(sufixo(tipo));
    }

    public static TipoDoArquivo of(String valor) {
        if (valor == null || valor.isEmpty())
            throw new IllegalArgumentException("Valor não preenchido");
        for (TipoDoArquivo s : TipoDoArquivo.values()) {
            if (s.name().equalsIgnoreCase(valor))
                return s;
        }
        return null;
    }

    public static TipoDoArquivo sufixo(String valor) {
        if (valor == null || valor.isEmpty())
            throw new IllegalArgumentException("Valor não preenchido");
        for (TipoDoArquivo s : TipoDoArquivo.values()) {
            if (s.getSufixo().equalsIgnoreCase(valor))
                return s;
        }
        return null;
    }

    public static Optional<TipoDoArquivo> parse(String valor) {
        if (valor == null || valor.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(of(valor));
    }
}
