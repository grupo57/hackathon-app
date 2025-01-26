package br.com.fiap.soat07.hackathon.infra.token.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    @Value("${spring.jwt.secret}")
    private String tokenSecret;

    @Value("${spring.jwt.issuer}")
    private String issuer;

    @Value("${spring.jwt.audience}")
    private String audience;

    @Value("${spring.jwt.expiration-time}")
    private long expirationTimeInSeconds;

    @Value("${spring.jwt.allowed-clock-skew}")
    private int allowedClockSkewInSeconds;


    public String getTokenSecret() {
        return tokenSecret;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAudience() {
        return audience;
    }

    public long getExpirationTimeInSeconds() {
        return expirationTimeInSeconds;
    }

    public int getAllowedClockSkewInSeconds() {
        return allowedClockSkewInSeconds;
    }
}
