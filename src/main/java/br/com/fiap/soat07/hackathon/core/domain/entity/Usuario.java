package br.com.fiap.soat07.hackathon.core.domain.entity;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.security.MessageDigest;

public class Usuario {
    public Long id;
    private String nome;
    private final String email;
    private final String salt;
    private final String senha;
    private SituacaoDoUsuario situacao;

    public Usuario(Long id, String nome, String email, String salt, String senha, SituacaoDoUsuario situacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.salt = salt;
        this.senha = senha;
        this.situacao = situacao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public SituacaoDoUsuario getSituacao() {
        return situacao;
    }

    public String getSalt() {
        return salt;
    }

    private String getSenhaCriptografada() {
        return senha;
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
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", situacao=" + situacao +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(getNome(), usuario.getNome())
                && Objects.equals(getEmail(), usuario.getEmail())
                && getSituacao() == usuario.getSituacao();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNome(), getEmail(), getSituacao());
    }
}
