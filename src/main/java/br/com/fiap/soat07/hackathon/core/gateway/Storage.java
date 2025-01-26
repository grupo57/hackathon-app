package br.com.fiap.soat07.hackathon.core.gateway;

import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.exception.SistemaDeArquivosException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface Storage {

    /**
     * Adiciona um arquivo no sistema de arquivos.
     *
     * @param diretorio diretório
     * @param chave chave do arquivo será armazenado
     * @param arquivo o InputStream do arquivo a ser adicionado
     * @throws SistemaDeArquivosException se houver falha ao adicionar o arquivo
     */
    void adicionar(String diretorio, String chave, InputStream arquivo) throws SistemaDeArquivosException;

    default void adicionar(String chave, InputStream arquivo) {
        adicionar( "", chave, arquivo);
    }


    /**
     * Recupera um arquivo do sistema de arquivos.
     *
     * @param diretorio diretório
     * @param chave a chave do arquivo a ser recuperado
     * @return um InputStream do arquivo recuperado
     * @throws SistemaDeArquivosException se houver falha ao recuperar o arquivo
     */
    InputStream recuperar(String diretorio, String chave) throws SistemaDeArquivosException;
    
    default byte[] toByteArray(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            
            // Lê os dados do InputStream e escreve no ByteArrayOutputStream
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            
            // Retorna o array de bytes com os dados lidos
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
			throw new SistemaDeArquivosException("Não foi possível converter o stream", e);
		}    	
    }

    default InputStream recuperar(String chave) {
        return recuperar( "", chave);
    }


    /**
     * Exclui um arquivo do sistema de arquivos.
     *
     * @param bucketName nome do bucket
     * @param diretorio diretório
     * @param chave a chave do arquivo a ser excluído
     * @throws SistemaDeArquivosException se houver falha ao excluir o arquivo
     */
    void excluir(String bucketName, String diretorio, String chave);

    String getUrl(String chave, int duracaoEmMinutos);
}