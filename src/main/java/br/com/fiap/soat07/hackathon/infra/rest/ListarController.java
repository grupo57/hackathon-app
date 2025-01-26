package br.com.fiap.soat07.hackathon.infra.rest;

import br.com.fiap.soat07.hackathon.core.domain.entity.MetadadosDoArquivo;
import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.gateway.Storage;
import br.com.fiap.soat07.hackathon.infra.rest.dto.MetadadosDoArquivoDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@Tag(name = "arquivo", description = "arquivo")
@RestController
@RequestMapping
public class ListarController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListarController.class);
    private final AutenticacaoService autenticacaoService;
    private final ArquivoService arquivoService;
    private final Storage sistemaDeArquivos;

    public ListarController(AutenticacaoService autenticacaoService,
                            ArquivoService arquivoService,
                            @Qualifier("s3") Storage storage
    ) {
        this.autenticacaoService = autenticacaoService;
        this.sistemaDeArquivos = storage;
        this.arquivoService = arquivoService;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de arquivos do usuário", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MetadadosDoArquivo.class)) })
    })
    @GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> listar() {

        Optional<Usuario> usuarioLogado = autenticacaoService.getAutenticado();
        if (usuarioLogado.isEmpty())
            return ResponseEntity.status(401).body("Permissão inválida");

        Collection<MetadadosDoArquivoDTO> arquivos = arquivoService.listar(usuarioLogado.get());
                ;
        if (!arquivos.isEmpty())
            return ResponseEntity.status(200).body(arquivos);
        return ResponseEntity.status(204).build();
    }

}
