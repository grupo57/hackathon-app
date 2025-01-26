package br.com.fiap.soat07.hackathon.core.gateway;

import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;

import java.util.Collection;
import java.util.Optional;

public interface ProcessadorGateway {

    /**
     * Listagem de Arquivos do usuário
     *
     * @return {@link MetadadosDoArquivo}
     */
    Collection<MetadadosDoArquivo> find(Usuario usuario);

    /**
     * Get by id
     *
     * @param id {@link Long}
     * @return {@link MetadadosDoArquivo}
     */
    Optional<MetadadosDoArquivo> findById(Long id);

}
