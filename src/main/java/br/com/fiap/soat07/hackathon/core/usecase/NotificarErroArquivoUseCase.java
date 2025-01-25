package br.com.fiap.soat07.hackathon.core.usecase;

import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.gateway.NotificacaoGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificarErroArquivoUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificarErroArquivoUseCase.class);
    private final NotificacaoGateway notificacaoGateway;

    public NotificarErroArquivoUseCase(NotificacaoGateway notificacaoGateway) {
        this.notificacaoGateway = notificacaoGateway;
    }

    public void execute(Usuario usuario, MetadadosDoArquivo metadados) {
        if (usuario == null)
            throw new IllegalArgumentException("Obrigatório definir o destinatário");
        if (metadados == null)
            throw new IllegalArgumentException("Obrigatório definir o arquivo");

        String mensagem = """
                Não foi possível processar o arquivo '%s'
                """.formatted(metadados.getNome());

        notificacaoGateway.enviar(usuario, mensagem);
    }

}
