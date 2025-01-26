package br.com.fiap.soat07.hackathon.core.domain.entity;

import java.util.Optional;

public enum SituacaoDoUsuario {
    AGUARDANDO_VALIDACAO,
    ATIVO,
    INATIVO;

    public boolean isValido() {
        return ATIVO.equals(this);
    }


    public static SituacaoDoUsuario of(String valor) {
        if (valor == null || valor.isEmpty())
            throw new IllegalArgumentException("Valor não preenchido");
        for (SituacaoDoUsuario s : SituacaoDoUsuario.values()) {
            if (s.name().equalsIgnoreCase(valor))
                return s;
        }
        return null;
    }

    public static Optional<SituacaoDoUsuario> parse(String valor) {
        if (valor == null || valor.isEmpty())
            Optional.empty();
        return Optional.ofNullable(of(valor));
    }

}
