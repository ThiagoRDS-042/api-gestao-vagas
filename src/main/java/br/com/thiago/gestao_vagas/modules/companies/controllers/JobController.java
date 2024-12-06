package br.com.thiago.gestao_vagas.modules.companies.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiago.gestao_vagas.modules.companies.dto.CreateJobDTO;
import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;
import br.com.thiago.gestao_vagas.modules.companies.useCases.CreateJobUseCase;
import br.com.thiago.gestao_vagas.modules.companies.useCases.exceptions.CompanyDoesNotExistsException;
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
public class JobController {

  @Autowired
  private CreateJobUseCase createJobUseCase;

  @PostMapping("/jobs")
  @PreAuthorize("hasRole('COMPANY')")
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
