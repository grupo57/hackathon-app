package br.com.fiap.soat07.hackathon.infra.rest;

import br.com.fiap.soat07.hackathon.core.Hash;
import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.TipoDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.exception.TipoInvalidoException;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
import br.com.fiap.soat07.hackathon.infra.repository.mysql.model.MetadadosDoArquivoModel;
import br.com.fiap.soat07.hackathon.infra.service.ArquivoService;
import br.com.fiap.soat07.hackathon.infra.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Tag(name = "arquivo", description = "arquivo")
@RestController
@RequestMapping
public class DownloadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
    private final AutenticacaoService autenticacaoService;
    private final ArquivoService arquivoService;
    private final Storage storage;

    public DownloadController(AutenticacaoService autenticacaoService,
                              ArquivoService arquivoService,
                              @Qualifier("s3") Storage storage
    ) {
        this.autenticacaoService = autenticacaoService;
        this.arquivoService = arquivoService;
        this.storage = storage;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Download realizado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MetadadosDoArquivo.class)) }),
            @ApiResponse(responseCode = "400", description = "Obrigatório informar o código do arquivo!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Você não possui permissão para acessar esse arquivo", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Arquivo ainda em processamento, não está disponível", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Usuário sem permissão para baixar esse arquivo", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro ao baixar arquivo", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> download(@PathVariable("id") String id) throws IOException {

        Optional<Usuario> usuarioLogado = autenticacaoService.getAutenticado();
        if (usuarioLogado.isEmpty())
            return ResponseEntity.status(401).body("Permissão inválida");

        // Valida arquivo
        if (id == null || id.isEmpty())
            return ResponseEntity.badRequest().body("Obrigatório informar o código do arquivo!");

        Optional<MetadadosDoArquivo> metadados = arquivoService.get(id);
        if (metadados.isEmpty())
            return ResponseEntity.badRequest().body("Arquivo não encontrado");
        if (!metadados.get().getIdUsuario().equals(usuarioLogado.get().getId()))
            return ResponseEntity.badRequest().body("Você não possui permissão para acessar esse arquivo");
        if (!metadados.get().getSituacao().isPossoFazerDownload())
            return ResponseEntity.badRequest().body("Arquivo ainda em processamento, não está disponível");

        InputStream is = arquivoService.download(metadados.get());
        InputStreamResource resource = new InputStreamResource(is);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadados.get().getNomeZip() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(resource);
    }

}
