package br.com.fiap.soat07.hackathon.infra.service;

import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.gateway.NotificacaoGateway;
import br.com.fiap.soat07.hackathon.core.usecase.NotificarErroArquivoUseCase;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {
    private final NotificarErroArquivoUseCase notificarErroArquivoUseCase;

    public NotificacaoService(NotificacaoGateway notificacaoGateway) {
        notificarErroArquivoUseCase = new NotificarErroArquivoUseCase(notificacaoGateway);
    }

    public void notificarErro(Usuario usuario, MetadadosDoArquivo metadados) {
        if (usuario == null)
            throw new IllegalArgumentException("Obrigatório definir o destinatário");
        if (metadados == null)
            throw new IllegalArgumentException("Obrigatório definir o arquivo");

        notificarErroArquivoUseCase.execute(usuario, metadados);
    }

}
