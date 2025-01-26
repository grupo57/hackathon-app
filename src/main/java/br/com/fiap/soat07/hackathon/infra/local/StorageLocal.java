package br.com.fiap.soat07.hackathon.infra.local;

import br.com.fiap.soat07.hackathon.core.exception.SistemaDeArquivosException;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component("local")
public class StorageLocal implements Storage {
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageLocal.class);

	private final String root;

	StorageLocal(@Value("${spring.arquivo.local.root}") String root) {
		this.root = root;
	}

    @Override
    public void adicionar(String diretorio, String chave, InputStream arquivo) {
		if (chave == null || chave.isEmpty())
			throw new SistemaDeArquivosException("Obrigatório informar a chave");

		// Verifica se o diretório existe
		Path caminhoAbsolutoDoDiretorioRoot = Paths.get(root, diretorio).toAbsolutePath();
		if (!Files.exists(caminhoAbsolutoDoDiretorioRoot)) {
			try {
				Files.createDirectories(caminhoAbsolutoDoDiretorioRoot);
				LOGGER.debug("Diretório criado: " + caminhoAbsolutoDoDiretorioRoot);
			} catch (IOException e) {
				throw new SistemaDeArquivosException("Não foi possível criar o diretório de upload.", e);
			}
		}

		Path caminho = Path.of(root, diretorio, chave);
        try {
        	Files.copy(arquivo, caminho.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
			throw new SistemaDeArquivosException("Não foi possível copiar o conteúdo do arquivo para o path de destino: "+caminho.toAbsolutePath(), e);
		}
    }


    @Override
    public InputStream recuperar(String diretorio, String chave) {
		if (chave == null || chave.isEmpty())
			throw new SistemaDeArquivosException("Obrigatório informar a chave");

		Path caminho = Path.of(root, diretorio, chave);
		try {
			return Files.newInputStream(caminho);
		} catch (IOException e) {
			throw new SistemaDeArquivosException("Não foi possível recuperar o arquivo: "+caminho.toAbsolutePath(), e);
		}
    }


    @Override
    public void excluir(String bucketName, String diretorio, String chave) {
		if (chave == null || chave.isEmpty())
			throw new SistemaDeArquivosException("Obrigatório informar a chave");

		Path caminho = Path.of(root, diretorio, chave);
		try {
			Files.deleteIfExists(caminho);
		} catch (IOException e) {
			throw new SistemaDeArquivosException("Não foi possível excluir o arquivo: "+caminho.toAbsolutePath(), e);
		}
    }

	@Override
	public String getUrl(String chave, int duracaoEmMinutos) {
		return "";
	}

	@Override
	public String toString() {
		return "SistemaDeArquivosLocal";
	}
}