package br.com.fiap.soat07.hackathon.core.gateway;

import br.com.fiap.soat07.hackathon.core.domain.entity.FiltroDeArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.SituacaoDoArquivo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ArquivoGateway {

    Collection<MetadadosDoArquivo> find(FiltroDeArquivo filtro);

    Optional<MetadadosDoArquivo> get(UUID id);

    MetadadosDoArquivo incluir(MetadadosDoArquivo metadadosDoArquivo);

    MetadadosDoArquivo atualizaSituacao(MetadadosDoArquivo metadadosDoArquivo, SituacaoDoArquivo situacao);

    MetadadosDoArquivo defineQueArquivoFoiProcessado(MetadadosDoArquivo metadadosDoArquivo, LocalDateTime inicio, LocalDateTime termino);

    MetadadosDoArquivo defineQueArquivoFoiProcessadoComErro(MetadadosDoArquivo metadadosDoArquivo, LocalDateTime inicio, LocalDateTime termino, String mensagem);
}
