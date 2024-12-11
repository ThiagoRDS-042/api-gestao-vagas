package br.com.thiago.gestao_vagas.modules.canditates.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiago.gestao_vagas.exceptions.InvalidCredentialsException;
import br.com.thiago.gestao_vagas.modules.canditates.dto.AuthCandidateRequestDTO;
import br.com.thiago.gestao_vagas.modules.canditates.dto.AuthCandidateResponseDTO;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.AuthCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/candidates")
@Tag(name = "Candidate", description = "Info of the candidate")
public class AuthCandidateController {

  @Autowired
  private AuthCandidateUseCase authCandidateUseCase;

  @PostMapping("/auth")
  @Operation(summary = "Authenticate candidate", description = "Authenticate candidate")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = AuthCandidateResponseDTO.class))

      }),
      @ApiResponse(responseCode = "401", description = "Invalid credentials."),
      @ApiResponse(responseCode = "500", description = "Internal server error.")
  })
  public ResponseEntity<Object> auth(@Valid @RequestBody AuthCandidateRequestDTO authCandidateRequestDTO) {
    try {
      var token = this.authCandidateUseCase.execute(authCandidateRequestDTO);

      return ResponseEntity.ok().body(token);
    } catch (InvalidCredentialsException exception) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseEntity.internalServerError().body("Internal server error.");
    }
  }

}
