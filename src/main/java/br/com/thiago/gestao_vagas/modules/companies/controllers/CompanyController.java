package br.com.thiago.gestao_vagas.modules.companies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiago.gestao_vagas.modules.companies.entities.CompanyEntity;
import br.com.thiago.gestao_vagas.modules.companies.useCases.CreateCompanyUseCase;
import br.com.thiago.gestao_vagas.modules.companies.useCases.exceptions.CompanyAlreadExistsException;
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
public class CompanyController {

  @Autowired
  private CreateCompanyUseCase createCompanyUseCase;

  @PostMapping("")
  @Operation(summary = "Create of the company", description = "Create of the company")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = CompanyEntity.class))
      }),
      @ApiResponse(responseCode = "409", description = "Company already exists."),
      @ApiResponse(responseCode = "500", description = "Internal server error.")
  })
  public ResponseEntity<Object> create(@Valid @RequestBody CompanyEntity companyEntity) {
    try {
      var company = this.createCompanyUseCase.execute(companyEntity);

      return ResponseEntity.ok().body(company);
    } catch (CompanyAlreadExistsException exception) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseEntity.internalServerError().body("Internal server error.");
    }

  }

}
