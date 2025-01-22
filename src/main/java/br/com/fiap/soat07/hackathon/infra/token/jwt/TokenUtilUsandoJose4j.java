package br.com.fiap.soat07.hackathon.infra.token.jwt;

import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.gateway.TokenUtil;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Component
public class TokenUtilUsandoJose4j implements TokenUtil {

    @Autowired
    private TokenConfig tokenConfig;


    private static String generate256BitKey(String secret) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] encodedHash = digest.digest(secret.getBytes());
        return Base64.getEncoder().encodeToString(encodedHash); // Codifica a chave em Base64
    }



    @Override
    public String generateToken(Usuario usuario) {
        if (tokenConfig.getTokenSecret() == null)
            throw new IllegalArgumentException("Obrigatório definir a chave do jwt");

        if (tokenConfig.getTokenSecret().length() < 32) {
            System.err.println(generate256BitKey(tokenConfig.getTokenSecret()));
            throw new IllegalArgumentException("A chave deve ter pelo menos 32 caracteres");
        }

        JsonWebSignature jws = new JsonWebSignature();

        // Calcula o tempo de expiração
        long now = Instant.now().getEpochSecond();
        long exp = now + tokenConfig.getExpirationTimeInSeconds();

        // Configura o payload com os campos iss e aud
        jws.setPayload("{\"sub\":\"" + usuario.getEmail() + "\", " +
                "\"iss\":\"" + tokenConfig.getIssuer() + "\", " +
                "\"aud\":\"" + tokenConfig.getAudience() + "\", " +
                "\"iat\":" + now + ", " +
                "\"exp\":" + exp + "}"
                );

        // Define o algoritmo e a chave secreta
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(new HmacKey(tokenConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8)));

        // Gera o token
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String extrairEmail(String token) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireSubject() // Exige o campo "sub" no token
                .setExpectedIssuer(tokenConfig.getIssuer()) // Valida o campo "iss"
                .setExpectedAudience(tokenConfig.getAudience()) // Valida o campo "aud"
                .setRequireExpirationTime() // Exige que o token tenha o campo "exp"
                .setAllowedClockSkewInSeconds(30) // Permite 30 segundos de tolerância
                .setVerificationKey(new HmacKey(tokenConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8)))
                .build();

        try {
            // Processa o token e obtém as claims
            var claims = jwtConsumer.processToClaims(token);

            // Extrai o valor do campo "sub"
            return claims.getSubject();
        } catch (InvalidJwtException e) {
            throw new RuntimeException("Token inválido: " + e.getMessage(), e);
        } catch (MalformedClaimException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateToken(String token, Usuario usuario) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireSubject() // Exige o campo "sub" no token
                .setExpectedIssuer(tokenConfig.getIssuer()) // Valida o campo "iss"
                .setExpectedAudience(tokenConfig.getAudience()) // Valida o campo "aud"
                .setRequireExpirationTime() // Exige que o token tenha o campo "exp"
                .setAllowedClockSkewInSeconds(tokenConfig.getAllowedClockSkewInSeconds()) // Permite 30 segundos de tolerância
                .setVerificationKey(new HmacKey(tokenConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8)))
                .build();

        try {
            // Processa o token e valida suas reivindicações
            jwtConsumer.processToClaims(token);
            return true;
        } catch (InvalidJwtException e) {
            return false;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }
}
