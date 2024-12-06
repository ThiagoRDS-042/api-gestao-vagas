package br.com.thiago.gestao_vagas.modules.companies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiago.gestao_vagas.modules.companies.dto.CreateJobDTO;
import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;
import br.com.thiago.gestao_vagas.modules.companies.useCases.CreateJobUseCase;
import br.com.thiago.gestao_vagas.modules.companies.useCases.exceptions.CompanyDoesNotExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/companies")
@Tag(name = "Job", description = "Info of the jobs")
public class JobController {

  @Autowired
  private CreateJobUseCase createJobUseCase;

  @PostMapping("/jobs")
  @PreAuthorize("hasRole('COMPANY')")
  @Operation(summary = "Create of the jobs enables for the candidates", description = "Create of the jobs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = JobEntity.class))
      }),
      @ApiResponse(responseCode = "404", description = "Company does not exists."),
      @ApiResponse(responseCode = "500", description = "Internal server error.")
  })
  @SecurityRequirement(name = "jwt_auth")
  public ResponseEntity<Object> create(@Valid @RequestBody CreateJobDTO createJobDTO, HttpServletRequest request) {

    try {
      var companyId = request.getAttribute("company_id");

      var jobEntity = JobEntity.builder()
          .benefits(createJobDTO.getBenefits())
          .level(createJobDTO.getLevel())
          .description(createJobDTO.getDescription())
          .companyId(UUID.fromString(companyId.toString()))
          .build();

      var job = this.createJobUseCase.execute(jobEntity);

      return ResponseEntity.ok().body(job);
    } catch (CompanyDoesNotExistsException exception) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseEntity.internalServerError().body("Internal server error.");
    }

  }

}
