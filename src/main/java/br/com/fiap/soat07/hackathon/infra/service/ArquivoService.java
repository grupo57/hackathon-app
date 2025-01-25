package br.com.fiap.soat07.hackathon.infra.service;

import br.com.fiap.soat07.hackathon.core.domain.entity.*;
import br.com.fiap.soat07.hackathon.core.gateway.ArquivoGateway;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
import br.com.fiap.soat07.hackathon.core.usecase.DownloadArquivoUseCase;
import br.com.fiap.soat07.hackathon.core.usecase.ListarArquivosUseCase;
import br.com.fiap.soat07.hackathon.core.usecase.UploadArquivoUseCase;
import br.com.fiap.soat07.hackathon.infra.aws.SqsConfig;
import br.com.fiap.soat07.hackathon.infra.rest.dto.MetadadosDoArquivoDTO;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArquivoService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private SqsConfig sqsConfig;

    private final ArquivoGateway arquivoGateway;
    private final UploadArquivoUseCase uploadArquivoUseCase;
    private final DownloadArquivoUseCase downloadArquivoUseCase;
    private final ListarArquivosUseCase listarArquivosUseCase;
//    private String queueUrl = "https://sqs.us-east-1.amazonaws.com/744592382994/hackathon-video-processado";

    public ArquivoService(ArquivoGateway arquivoGateway, @Qualifier("s3") Storage storage) {
        this.arquivoGateway = arquivoGateway;
        this.uploadArquivoUseCase = new UploadArquivoUseCase(storage, arquivoGateway);
        this.downloadArquivoUseCase = new DownloadArquivoUseCase(storage, arquivoGateway);
        this.listarArquivosUseCase = new ListarArquivosUseCase(storage, arquivoGateway);
    }


    public Collection<MetadadosDoArquivoDTO> listar(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException("Obrigatório informar o usuário");
        if (!usuario.getSituacao().isValido())
            throw new IllegalArgumentException("Usuário não está validado");

        FiltroDeArquivo filtro = FiltroDeArquivo.of();
        filtro.usuario(usuario);
        return listarArquivosUseCase.execute(filtro);
    }

    @Transactional
    public void upload(Usuario usuario, String nome, TipoDoArquivo tipo, int tamanho, String hash, InputStream inputStream) {

        MetadadosDoArquivo metadados = new MetadadosDoArquivo(null, usuario.getId(), nome, tamanho, hash, tipo, SituacaoDoArquivo.CARREGANDO);
        metadados = arquivoGateway.incluir(metadados);
        uploadArquivoUseCase.execute(usuario, metadados, inputStream);
    }

    @SqsListener(value = "${cloud.aws.sqs.video-processado-queue-name}")
    public void videoProcessadoHandleMessage(@Payload Message message) {
        processarSucesso(message);
    }

    @SqsListener(value = "${cloud.aws.sqs.video-erro-queue-name}")
    public void videoErroHandleMessage(@Payload Message message) {
        processarSucesso(message);
    }


//    public void readMessages(String queue) {
//        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
//                .queueUrl(queue)
//                .maxNumberOfMessages(10)  // Número máximo de mensagens para receber por vez
//                .waitTimeSeconds(20)  // Tempo máximo de espera (long polling)
//                .build();
//
//        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
//        List<Message> messages = receiveMessageResponse.messages();
//
//        for (Message message : messages) {
//            System.out.println("Mensagem recebida: " + message.body());
//            processar(message);
//
//            // Após processar a mensagem, exclua-a para evitar que seja lida novamente
//            deleteMessage(queue, message);
//        }
//    }

    private void processarSucesso(Message message) {
        if (message == null)
            throw new IllegalArgumentException("Obrigatório informar o código");
        String body = message.body();
        String[] parts = body.split(";");
        if (parts.length != 3)
            throw new IllegalArgumentException("Mensagem em formato inválido");

        if (parts[0] == null || parts[0].lastIndexOf('.') == -1)
            throw new IllegalArgumentException("Chave informada está em formato inválido");
        String id = parts[0].substring(0, parts[0].lastIndexOf('.'));
        if (id == null)
            throw new IllegalArgumentException("Id está em formato inválido");

        processarSucesso(id, LocalDateTime.parse(parts[1], FORMATTER), LocalDateTime.parse(parts[2], FORMATTER));
    }
    private void processarSucesso(String id, LocalDateTime inicio, LocalDateTime termino) {
        if (id == null)
            throw new IllegalArgumentException("Obrigatório informar o código");

        Optional<MetadadosDoArquivo> metadados = arquivoGateway.get(UUID.fromString(id));
        if (metadados.isEmpty())
            throw new IllegalArgumentException("Arquivo não encontrado: "+id);

        arquivoGateway.defineQueArquivoFoiProcessado(metadados.get(), inicio, termino);
    }

    private void processarErro(Message message) {
        if (message == null)
            throw new IllegalArgumentException("Obrigatório informar o código");
        String body = message.body();
        String[] parts = body.split(";");
        if (parts.length != 2)
            throw new IllegalArgumentException("Mensagem em formato inválido");

        if (parts[0] == null || parts[0].lastIndexOf('.') == -1)
            throw new IllegalArgumentException("Chave informada está em formato inválido");
        String id = parts[0].substring(0, parts[0].lastIndexOf('.'));
        if (id == null)
            throw new IllegalArgumentException("Id está em formato inválido");

        processarErro(id, parts[1]);
    }
    private void processarErro(String id, String mensagem) {
        if (id == null)
            throw new IllegalArgumentException("Obrigatório informar o código");

        Optional<MetadadosDoArquivo> metadados = arquivoGateway.get(UUID.fromString(id));
        if (metadados.isEmpty())
            throw new IllegalArgumentException("Arquivo não encontrado: "+id);


        System.err.println("ERRO: "+metadados.get().getIdUsuario());

//        arquivoGateway.defineQueArquivoFoiProcessado(metadados.get(), inicio, termino);
    }

    private void deleteMessage(String queue, Message message) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queue)
                .receiptHandle(message.receiptHandle())
                .build();

        sqsClient.deleteMessage(deleteMessageRequest);
    }

    public InputStream download(MetadadosDoArquivo metadados) {
        if (metadados == null)
            throw new IllegalArgumentException("Obrigatório informar o código do arquivo");

        return downloadArquivoUseCase.execute(metadados);
    }

    public Optional<MetadadosDoArquivo> get(String id) {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("Obrigatório informar o código do arquivo");
        return arquivoGateway.get(UUID.fromString(id));
    }
}
