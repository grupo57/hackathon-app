package br.com.fiap.soat07.hackathon.infra.rest.dto;

import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.TipoDoArquivo;

import java.util.Objects;
import java.util.UUID;

public class MetadadosDoArquivoDTO {
    private UUID id;
    private String nome;
    private int tamanho;
    private TipoDoArquivo tipo;
    private SituacaoDoArquivo situacao;
    private String url;

    public MetadadosDoArquivoDTO() {
        super();
    }
    public MetadadosDoArquivoDTO(UUID id, String nome, int tamanho, TipoDoArquivo tipo, SituacaoDoArquivo situacao, String url) {
        this();
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
        this.tipo = tipo;
        this.situacao = situacao;
        this.url = url;
    }


    public UUID getId() {
        return id;
    }

    public String getCodigo() {
        if (getId() == null)
            return getNome();
        return getId()+"."+getTipo().getSufixo();
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNomeZip() {
        return nome+"-frames.zip";
    }

    public int getTamanho() {
        return tamanho;
    }

    public String getUrl() {
        return url;
    }

    public TipoDoArquivo getTipo() {
        return tipo;
    }

    public SituacaoDoArquivo getSituacao() {
        return situacao;
    }
    public void setSituacao(SituacaoDoArquivo situacao) {
        this.situacao = situacao;
    }



    @Override
    public String toString() {
        return "MetadadosDoArquivoDTO{" +
                "id='" + getId() + '\'' +
                ", tipo='" + getTipo() + '\'' +
                ", situação='" + getSituacao() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MetadadosDoArquivoDTO that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
