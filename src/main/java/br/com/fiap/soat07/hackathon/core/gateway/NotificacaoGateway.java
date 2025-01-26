package br.com.fiap.soat07.hackathon.core.gateway;

import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;

public interface NotificacaoGateway {

    void enviar(Usuario usuario, String mensagem);

}
