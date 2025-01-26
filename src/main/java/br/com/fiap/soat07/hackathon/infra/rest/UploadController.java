package br.com.fiap.soat07.hackathon.infra.rest;

import br.com.fiap.soat07.hackathon.core.Hash;
import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.TipoDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.exception.*;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Tag(name = "arquivo", description = "arquivo")
@RestController
@RequestMapping
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
    private final AutenticacaoService autenticacaoService;
    private final ArquivoService arquivoService;
    private final Storage storage;

    public UploadController(AutenticacaoService autenticacaoService,
                            ArquivoService arquivoService,
                            @Qualifier("s3") Storage storage
    ) {
        this.autenticacaoService = autenticacaoService;
        this.arquivoService = arquivoService;
        this.storage = storage;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload realizado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MetadadosDoArquivo.class)) }),
            @ApiResponse(responseCode = "400", description = "arquivo em formato inválido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "tamanho do arquivo inválido", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "erro ao enviar arquivo", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {

        Optional<Usuario> usuarioLogado = autenticacaoService.getAutenticado();
        if (usuarioLogado.isEmpty())
            return ResponseEntity.status(401).body("Permissão inválida");

        // Valida arquivo
        if (file == null || file.isEmpty())
            return ResponseEntity.badRequest().body("Arquivo vazio!");
        String nomeDoArquivo = file.getOriginalFilename();
        Optional<TipoDoArquivo> tipoDoArquivo = TipoDoArquivo.extrairTipo(file.getOriginalFilename());
        if (tipoDoArquivo.isEmpty())
            return ResponseEntity.internalServerError().body("Não há suporte para esse tipo de arquivo.");
        int tamanho = file.getBytes().length;
        String hash = Hash.calcular(file.getBytes());

        try {
            arquivoService.upload(usuarioLogado.get(), file.getOriginalFilename(), tipoDoArquivo.get(), tamanho, hash, file.getInputStream());
            return ResponseEntity.ok("Arquivo enviado com sucesso ("+ storage +"): " + nomeDoArquivo);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar o arquivo.");
        }
    }


}
