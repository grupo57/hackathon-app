package br.com.fiap.soat07.hackathon.core.usecase;

import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.exception.SistemaDeArquivosException;
import br.com.fiap.soat07.hackathon.core.gateway.ArquivoGateway;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.util.UUID;

public class DownloadArquivoUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadArquivoUseCase.class);
    private final Storage sistemaDeArquivos;
    private final ArquivoGateway arquivoGateway;

    public DownloadArquivoUseCase(@Qualifier("s3") Storage storage, ArquivoGateway arquivoGateway) {
        this.sistemaDeArquivos = storage;
        this.arquivoGateway = arquivoGateway;
    }

    public InputStream execute(MetadadosDoArquivo metadados) {
        String chave = metadados.getId().toString()+"."+metadados.getTipo().getSufixo()+"-frames.zip";
        return sistemaDeArquivos.recuperar(chave);
    }

}
