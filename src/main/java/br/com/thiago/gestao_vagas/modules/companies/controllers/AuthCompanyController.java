package br.com.thiago.gestao_vagas.modules.companies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiago.gestao_vagas.exceptions.InvalidCredentialsException;
import br.com.thiago.gestao_vagas.modules.companies.dto.AuthCompanyDTO;
import br.com.thiago.gestao_vagas.modules.companies.dto.AuthCompanyResponseDTO;
import br.com.thiago.gestao_vagas.modules.companies.useCases.AuthCompanyUseCase;
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
@RequestMapping("/companies")
@Tag(name = "Company", description = "Info of the company")
public class AuthCompanyController {

  @Autowired
  private AuthCompanyUseCase authCompanyUseCase;

  @PostMapping("/auth")
  @Operation(summary = "Autenticate company", description = "Autenticate company")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = AuthCompanyResponseDTO.class))
      }),
      @ApiResponse(responseCode = "401", description = "Invalid credentials."),
      @ApiResponse(responseCode = "500", description = "Internal server error.")
  })
  public ResponseEntity<Object> auth(@Valid @RequestBody AuthCompanyDTO authCompanyDTO) {
    try {
      var token = this.authCompanyUseCase.execute(authCompanyDTO);

      return ResponseEntity.ok().body(token);
    } catch (InvalidCredentialsException exception) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseEntity.internalServerError().body("Internal server error.");
    }

  }

}
