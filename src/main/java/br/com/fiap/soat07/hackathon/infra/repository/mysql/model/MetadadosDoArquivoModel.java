package br.com.fiap.soat07.hackathon.infra.repository.mysql.model;

import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.TipoDoArquivo;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "arquivo_metadados", schema = "hackathon")
public class MetadadosDoArquivoModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id", columnDefinition = "CHAR(36)", length = 36, unique = true, updatable = false, nullable = false)
    private String id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "tamanho")
    private int tamanho;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 36, nullable = false)
    private TipoDoArquivo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", length = 20, nullable = false)
    private SituacaoDoArquivo situacao;

    @Column(name = "conteudo_hash", length = 128, nullable = false)
    private String hash;

    @Column(name = "data_processamento_inicio")
    private LocalDateTime dataProcessamentoInicio;

    @Column(name = "data_processamento_termino")
    private LocalDateTime dataProcessamentoTermino;

    public MetadadosDoArquivoModel() {}
    public MetadadosDoArquivoModel(UUID id, Long idUsuario,  String nome, TipoDoArquivo tipo, int tamanho, String hash, SituacaoDoArquivo situacao) {
        this.id = (id == null ? null : id.toString());
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.hash = hash;
        this.situacao = situacao;
    }


    public UUID getId() {
        return UUID.fromString(id);
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

    public int getTamanhoEmBytes() {
        return tamanho;
    }

    public TipoDoArquivo getTipo() {
        return tipo;
    }
    public void setTipo(TipoDoArquivo tipo) {
        this.tipo = tipo;
    }

    public SituacaoDoArquivo getSituacao() {
        return situacao;
    }
    public void setSituacao(SituacaoDoArquivo situacao) {
        this.situacao = situacao;
    }

    public String getHash() {
        return hash;
    }

    public LocalDateTime getDataProcessamentoInicio() {
        return dataProcessamentoInicio;
    }
    public void setDataProcessamentoInicio(LocalDateTime dataProcessamentoInicio) {
        this.dataProcessamentoInicio = dataProcessamentoInicio;
    }

    public LocalDateTime getDataProcessamentoTermino() {
        return dataProcessamentoTermino;
    }
    public void setDataProcessamentoTermino(LocalDateTime dataProcessamentoTermino) {
        this.dataProcessamentoTermino = dataProcessamentoTermino;
    }

    @Override
    public String toString() {
        return "ArquivoModel{" +
                "id=" + getId() +
                ", usuario=" + getIdUsuario() +
                ", situacao=" + getSituacao() +
                ", nome='" + getNome() + '\'' +
                ", tipo='" + getTipo() + '\'' +
                ", hash='" + getHash() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MetadadosDoArquivoModel that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
