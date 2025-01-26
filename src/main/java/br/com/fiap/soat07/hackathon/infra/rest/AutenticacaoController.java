package br.com.fiap.soat07.hackathon.infra.rest;

import br.com.fiap.soat07.hackathon.core.domain.entity.Usuario;
import br.com.fiap.soat07.hackathon.core.exception.*;
import br.com.fiap.soat07.hackathon.infra.rest.dto.LoginDTO;
import br.com.fiap.soat07.hackathon.infra.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "autenticacao", description = "autenticação")
@RestController
@RequestMapping
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)) }),
            @ApiResponse(responseCode = "400", description = "login/senha não informados", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "login/senha inválidos", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "erro ao validar usuário", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> login(@RequestBody final LoginDTO loginDTO) {
        if (loginDTO == null)
            return ResponseEntity.status(400).body("login/senha não informados");

        try {
            Usuario usuario = autenticacaoService.login(loginDTO.getLogin(), loginDTO.getSenha());
            String token = autenticacaoService.gerarToken(usuario);
            return ResponseEntity.status(200).body(token);
        } catch (LoginInvalidoException | SenhaInvalidaException e) {
            return ResponseEntity.status(400).body("login/senha não informados");
        } catch (UsuarioNaoAtivoException e) {
            return ResponseEntity.status(401).body("usuário não ativo");
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.status(401).body("login/senha inválidos");
        } catch (NaoFoiPossivelGerarSenhaCriptografadaException e) {
            return ResponseEntity.status(500).body("erro ao validar usuário");
        }
    }

}
