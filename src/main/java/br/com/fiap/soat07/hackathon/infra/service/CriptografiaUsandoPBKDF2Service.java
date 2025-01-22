package br.com.fiap.soat07.hackathon.infra.service;

import br.com.fiap.soat07.hackathon.core.exception.NaoFoiPossivelGerarSenhaCriptografadaException;
import br.com.fiap.soat07.hackathon.core.gateway.CriptografiaGateway;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class CriptografiaUsandoPBKDF2Service implements CriptografiaGateway {
    private static final int SALT_LENGTH = 16; // Tamanho do salt em bytes
    private static final int KEY_LENGTH = 256; // Tamanho do hash em bits
    // Número de iterações recomendado: 100.000 ou mais
    private static final int ITERATIONS = 100_000;

    private final int saltLength;
    private final int keyLength;
    private final int iterations;

    public CriptografiaUsandoPBKDF2Service(int saltLength, int keyLength, int iterations) {
        this.saltLength = saltLength;
        this.keyLength = keyLength;
        this.iterations = iterations;
    }
    public CriptografiaUsandoPBKDF2Service() {
        this.saltLength = SALT_LENGTH;
        this.keyLength = KEY_LENGTH;
        this.iterations = ITERATIONS;
    }

    public int getSaltLength() {
        return saltLength;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public int getIterations() {
        return iterations;
    }

    @Override
    public String gerarSalt() {
        // Gerar salt aleatório
        byte[] salt = new byte[getSaltLength()];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    @Override
    public String hash(String salt, char[] senha) {
        // Usar PBKDF2 com HMAC-SHA-256
        try {
            // Convert salt de Base64 para byte array
            byte[] saltBytes = Base64.getDecoder().decode(salt);

            // Criar KeySpec com a senha, salt, e número de iterações
            KeySpec spec = new PBEKeySpec(senha, saltBytes, getIterations(), getKeyLength());
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // Retornar hash como Base64
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new NaoFoiPossivelGerarSenhaCriptografadaException("Erro ao criar o hash da senha", e);
        }
    }

    @Override
    public String toString() {
        return "CriptografiaUsandoPBKDF2Service{" +
                "saltLength=" + saltLength +
                ", keyLength=" + keyLength +
                ", iterations=" + iterations +
                '}';
    }

    public static void main(String[] args) {
        String salt = "vGrmIur/vHuhVP8kH+hRqQ==";
        String senha = "123";

        CriptografiaUsandoPBKDF2Service service = new CriptografiaUsandoPBKDF2Service();
        System.err.println(service.hash(salt, senha.toCharArray()));
    }


}
