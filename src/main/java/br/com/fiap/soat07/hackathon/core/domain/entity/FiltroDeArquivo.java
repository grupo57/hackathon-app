package br.com.fiap.soat07.hackathon.core.domain.entity;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;

public class FiltroDeArquivo {
    private Usuario usuario;
    private Collection<SituacaoDoArquivo> situacoes;
    private String nome;

    private FiltroDeArquivo() {
        this.situacoes = EnumSet.of(SituacaoDoArquivo.PROCESSADO);
    }

    public static FiltroDeArquivo of() {
        return new FiltroDeArquivo();
    }

    public FiltroDeArquivo usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public FiltroDeArquivo situacao(SituacaoDoArquivo s, SituacaoDoArquivo... ss) {
        if (s != null)
            this.situacoes.add(s);
        if (ss != null && ss.length > 0)
            for (SituacaoDoArquivo situacaoDoArquivo : ss) {
                this.situacoes.add(situacaoDoArquivo);
            }
        return this;
    }

    public FiltroDeArquivo nome(String nome) {
        this.nome = nome;
        return this;
    }

    public Optional<Usuario> getUsuario() {
        return Optional.ofNullable(usuario);
    }

    public Collection<SituacaoDoArquivo> getSituacoes() {
        return situacoes;
    }

    public Optional<String> getNome() {
        return Optional.ofNullable(nome);
    }
}
