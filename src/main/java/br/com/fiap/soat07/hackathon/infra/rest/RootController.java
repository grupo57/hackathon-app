package br.com.fiap.soat07.hackathon.infra.rest;

import br.com.fiap.soat07.hackathon.infra.rest.dto.Grupo57DTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "root", description = "root")
@RestController
@RequestMapping
public class RootController {

    public RootController() {
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sistema online", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo57DTO.class)) }),
    })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> root() {
        return ResponseEntity.status(200).body(new Grupo57DTO());
    }

}
