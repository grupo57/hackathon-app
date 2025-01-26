package br.com.fiap.soat07.hackathon.core.gateway;

public interface CriptografiaGateway {

    String gerarSalt();

    String hash(String salt, char[] senha);

}
