package br.com.fiap.soat07.hackathon.core.usecase;

import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.gateway.ArquivoGateway;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

public class UploadArquivoUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadArquivoUseCase.class);
    private final Storage storage;
    private final ArquivoGateway arquivoGateway;

    public UploadArquivoUseCase(Storage storage, ArquivoGateway arquivoGateway) {
        this.storage = storage;
        this.arquivoGateway = arquivoGateway;
    }

    @Transactional
    public MetadadosDoArquivo execute(Usuario usuario, MetadadosDoArquivo metadadosDoArquivo, InputStream inputStream) {
        String chave = metadadosDoArquivo.getId()+"."+metadadosDoArquivo.getTipo().getSufixo();

        // Salva o arquivo
        LOGGER.info(chave+" carregando...");
        storage.adicionar(chave, inputStream);

        // ATUALIZA SITUACAO
        LOGGER.info(chave+" carregado");
        arquivoGateway.atualizaSituacao(metadadosDoArquivo, SituacaoDoArquivo.CARREGADO);

        return metadadosDoArquivo;
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    protected Arquivo criarArquivo(String nome, TipoDoArquivo tipo, byte[] conteudo) {
//        String hash = ArquivoHelper.calculaHashDoArquivo(conteudo);
//        Arquivo arquivo = new Arquivo(null, nome, conteudo.length, hash, tipo, SituacaoDoArquivo.CARREGANDO);
//        arquivo.setSituacao(SituacaoDoArquivo.CARREGANDO);
//        storageGateway.salvar(arquivo);
//        return arquivo;
//    }
//
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void atualizaSituacaoComNovaTransacao(MetadadosDoArquivo metadadosDoArquivo, SituacaoDoArquivo situacao) {
        arquivoGateway.atualizaSituacao(metadadosDoArquivo, SituacaoDoArquivo.CARREGADO);
    }


}
