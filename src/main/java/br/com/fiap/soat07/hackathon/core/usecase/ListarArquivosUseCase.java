package br.com.fiap.soat07.hackathon.core.usecase;

import br.com.fiap.soat07.hackathon.core.domain.entity.FiltroDeArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.TipoDoArquivo;
import br.com.fiap.soat07.hackathon.core.gateway.ArquivoGateway;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
import br.com.fiap.soat07.hackathon.infra.rest.dto.MetadadosDoArquivoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.UUID;

public class ListarArquivosUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListarArquivosUseCase.class);
    private final Storage storage;
    private final ArquivoGateway arquivoGateway;

    public ListarArquivosUseCase(Storage storage, ArquivoGateway arquivoGateway) {
        this.storage = storage;
        this.arquivoGateway = arquivoGateway;
    }

    public Collection<MetadadosDoArquivoDTO> execute(FiltroDeArquivo filtro) {
        if (filtro == null)
            throw new IllegalArgumentException("Obrigatório informar o filtro");
        if (filtro.getUsuario().isEmpty())
            throw new IllegalArgumentException("Obrigatório informar o usuário");

        return arquivoGateway.find(filtro).stream()
                .map(i -> {
                    String chave = i.getId().toString()+"."+i.getTipo().getSufixo()+"-frames.zip";
                    String url = i.getSituacao().isPossoFazerDownload() ? storage.getUrl(chave, 60) : "";
                    MetadadosDoArquivoDTO dto = new MetadadosDoArquivoDTO(
                        i.getId(),
                        i.getNome(),
                        i.getTamanho(),
                        i.getTipo(),
                        i.getSituacao(),
                        url);
                    return dto;
                })
                .toList();
    }

}
