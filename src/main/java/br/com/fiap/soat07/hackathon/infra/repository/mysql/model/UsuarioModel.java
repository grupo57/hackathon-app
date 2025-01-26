package br.com.fiap.soat07.hackathon.infra.repository.mysql.model;

import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoUsuario;
import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

@Entity
@Table(name = "usuario", schema = "hackathon")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "salt", length = 36)
    private String salt;

    @Column(name = "senha", length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", length = 20)
    private SituacaoDoUsuario situacao;

    public UsuarioModel() {
        this.situacao = SituacaoDoUsuario.AGUARDANDO_VALIDACAO;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSenhaCriptografada() {
        return senha;
    }
    public void setSenhaCriptografada(String senha) {
        this.senha = senha;
    }

    public SituacaoDoUsuario getSituacao() {
        return situacao;
    }
    public void setSituacao(SituacaoDoUsuario situacao) {
        this.situacao = situacao;
    }


    public boolean verificaSenha(String senha) {
        if (senha == null || senha.isEmpty())
            return false;
        if (getSenhaCriptografada() == null || getSenhaCriptografada().isEmpty())
            return false;

        // Converte as strings em arrays de bytes
        byte[] senhaBytes = senha.getBytes(StandardCharsets.UTF_8);
        byte[] senhaCriptografadaBytes = getSenhaCriptografada().getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(senhaBytes, senhaCriptografadaBytes);
    }


    @Override
    public String toString() {
        return "UsuarioModel{" +
                "situacao=" + situacao +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UsuarioModel that)) return false;
        return Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEmail());
    }

}
