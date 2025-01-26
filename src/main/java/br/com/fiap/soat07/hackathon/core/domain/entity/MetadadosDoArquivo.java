package br.com.fiap.soat07.hackathon.core.domain.entity;

import java.util.Objects;
import java.util.UUID;

public class MetadadosDoArquivo {
    private UUID id;
    private Long idUsuario;
    private String nome;
    private int tamanho;
    private TipoDoArquivo tipo;
    private SituacaoDoArquivo situacao;
    private String hash;

    public MetadadosDoArquivo() {
        super();
    }
    public MetadadosDoArquivo(UUID id, Long idUsuario, String nome, int tamanho, String hash, TipoDoArquivo tipo, SituacaoDoArquivo situacao) {
        this();
        this.id = id;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.tamanho = tamanho;
        this.hash = hash;
        this.tipo = tipo;
        this.situacao = situacao;
    }


    public static MetadadosDoArquivo mp4(String id, Long idUsuario, String nome, int tamanho, String hash, SituacaoDoArquivo situacao) {
        return new MetadadosDoArquivo(UUID.fromString(id), idUsuario, nome, tamanho, hash, TipoDoArquivo.VIDEO_MP4, situacao);
    }
    public static MetadadosDoArquivo zip(String id, Long idUsuario, String nome, int tamanho, String hash, SituacaoDoArquivo situacao) {
        return new MetadadosDoArquivo(UUID.fromString(id), idUsuario, nome, tamanho, hash, TipoDoArquivo.ZIP_IMAGENS, situacao);
    }
    public static MetadadosDoArquivo zip(MetadadosDoArquivo metadadosDoArquivo, int tamanho, String hash, SituacaoDoArquivo situacao) {
        return new MetadadosDoArquivo(metadadosDoArquivo.getId(), metadadosDoArquivo.getIdUsuario(),  metadadosDoArquivo.getNome(), tamanho, hash, TipoDoArquivo.ZIP_IMAGENS, situacao);
    }


    public UUID getId() {
        return id;
    }

    public String getCodigo() {
        if (getId() == null)
            return getNome();
        return getId()+"."+getTipo().getSufixo();
    }

    public Long getIdUsuario() {
        return idUsuario;
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

    public String getHash() {
        return hash;
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
        return "MetadadosDoArquivo{" +
                "id='" + getId() + '\'' +
                ", tipo='" + getTipo() + '\'' +
                ", situação='" + getSituacao() + '\'' +
                ", hash='" + getHash() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MetadadosDoArquivo that)) return false;
        return Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hash);
    }

}
