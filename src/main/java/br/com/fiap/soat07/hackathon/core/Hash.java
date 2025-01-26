package br.com.fiap.soat07.hackathon.core;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public final class Hash {
	private static final String HASH_ALGORITMO = "SHA-256";
	
	private Hash() {
	}
	
    
    
	public static String calcular(InputStream inputStream) {
        MessageDigest digest = getMessageDigest();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Lê o InputStream em blocos e atualiza o MessageDigest com os dados
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Obtém o resultado do hash como um array de bytes
        byte[] hashBytes = digest.digest();

        // Converte os bytes do hash para uma string hexadecimal
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITMO);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return digest;
    }

    public static String calcular(byte[] data) {
        MessageDigest digest = getMessageDigest();

        // Atualiza o MessageDigest com os dados do array
        byte[] hashBytes = digest.digest(data);
        
        // Converte os bytes do hash para uma string hexadecimal
        return Base64.getEncoder().encodeToString(hashBytes);
    }	

}
