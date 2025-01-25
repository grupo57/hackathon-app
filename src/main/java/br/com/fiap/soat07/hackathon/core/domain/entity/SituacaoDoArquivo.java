package br.com.fiap.soat07.hackathon.core.domain.entity;

import java.util.Optional;

public enum SituacaoDoArquivo {
    CARREGANDO,
    CARREGADO,
    EM_PROCESSAMENTO,
    PROCESSADO,
    BAIXADO,
    ERRO;


    public boolean isProcessado() {
        return PROCESSADO.equals(this) || BAIXADO.equals(this);
    }

    public static SituacaoDoArquivo of(String valor) {
        if (valor == null || valor.isEmpty())
            throw new IllegalArgumentException("Valor não preenchido");
        for (SituacaoDoArquivo s : SituacaoDoArquivo.values()) {
            if (s.name().equalsIgnoreCase(valor))
                return s;
        }
        return null;
    }

    public static Optional<SituacaoDoArquivo> parse(String valor) {
        if (valor == null || valor.isEmpty())
            Optional.empty();
        return Optional.ofNullable(of(valor));
    }

    public boolean isPossoDefinirComoProcessado() {
        return !(PROCESSADO.equals(this) || BAIXADO.equals(this) || ERRO.equals(this));
    }
    public boolean isPossoFazerDownload() {
        return (PROCESSADO.equals(this) || BAIXADO.equals(this));
    }
}
