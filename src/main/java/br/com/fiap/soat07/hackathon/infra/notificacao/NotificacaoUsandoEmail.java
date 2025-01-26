package br.com.fiap.soat07.hackathon.infra.notificacao;

import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.gateway.NotificacaoGateway;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoUsandoEmail implements NotificacaoGateway {


    @Override
    public void enviar(Usuario usuario, String mensagem) {
        System.err.println("email enviado para "+usuario.getEmail()+": "+mensagem);
    }
}
