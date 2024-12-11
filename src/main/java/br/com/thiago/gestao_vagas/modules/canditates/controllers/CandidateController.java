package br.com.thiago.gestao_vagas.modules.canditates.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiago.gestao_vagas.modules.canditates.dto.ProfileCandidateResponseDTO;
import br.com.thiago.gestao_vagas.modules.canditates.entities.ApplyJobEntity;
import br.com.thiago.gestao_vagas.modules.canditates.entities.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.ApplyJobCandidateUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.CreateCandidateUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.ListAllJobsByFilterUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.ProfileCandidateUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateAlreadyExistsException;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateDoesNotExists;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.JobDoesNotExistsException;
import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/candidates")
@Tag(name = "Candidate", description = "Info of the candidate")
public class CandidateController {

  @Autowired
  private CreateCandidateUseCase createCandidateUseCase;

  @Autowired
  private ProfileCandidateUseCase profileCandidateUseCase;

  @Autowired
  private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

  @Autowired
  private ApplyJobCandidateUseCase applyJobCandidateUse;

  @PostMapping("")
  @Operation(summary = "Create of the candidate", description = "Create of the candidate")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = CandidateEntity.class))
      }),
      @ApiResponse(responseCode = "409", description = "Candidate already exists."),
      @ApiResponse(responseCode = "500", description = "Internal server error.")
  })
  public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
    try {
      var candidate = this.createCandidateUseCase.execute(candidateEntity);

      return ResponseEntity.ok().body(candidate);
    } catch (CandidateAlreadyExistsException exception) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseEntity.internalServerError().body("Internal server error.");
    }

  }

  @GetMapping("/profile")
  @PreAuthorize("hasRole('CANDIDATE')")
  @Operation(summary = "Profile of the candidate", description = "Profile of the candidate")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = ProfileCandidateResponseDTO.class)), }),
      @ApiResponse(responseCode = "404", description = "Candidate does not exists."),
      @ApiResponse(responseCode = "500", description = "Internal server error.")
  })
  @SecurityRequirement(name = "jwt_auth")
  public ResponseEntity<Object> profile(HttpServletRequest request) {
    var candidateId = request.getAttribute("candidate_id");

    try {

      var profileCandidate = this.profileCandidateUseCase.execute(UUID.fromString(candidateId.toString()));

      return ResponseEntity.status(HttpStatus.OK).body(profileCandidate);
    } catch (CandidateDoesNotExists exception) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseEntity.internalServerError().body("Internal server error.");
    }
  }

  @GetMapping("/jobs")
  @PreAuthorize("hasRole('CANDIDATE')")
  @Operation(summary = "List of the jobs enables for the candidate", description = "List of the jobs")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(array = @ArraySchema(schema = @Schema(implementation = JobEntity.class)))
      })
  })
  @SecurityRequirement(name = "jwt_auth")
  public List<JobEntity> listAllJobsByFilter(@RequestParam String filter) {
    return this.listAllJobsByFilterUseCase.execute(filter);

  }

  @PostMapping("/jobs/{jobId}/apply")
  @PreAuthorize("hasRole('CANDIDATE')")
  @Operation(summary = "Apply the job", description = "Apply the job")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = ApplyJobEntity.class))
      }),
      @ApiResponse(responseCode = "404", description = "Candidate does not exists."),
      @ApiResponse(responseCode = "404", description = "Job does not exists."),
      @ApiResponse(responseCode = "500", description = "Internal server error.")
  })
  @SecurityRequirement(name = "jwt_auth")
  public ResponseEntity<Object> applyJob(@PathVariable UUID jobId, HttpServletRequest request) {
    var candidateId = request.getAttribute("candidate_id");

    try {
      var result = this.applyJobCandidateUse.execute(UUID.fromString(candidateId.toString()), jobId);

      return ResponseEntity.ok().body(result);
    } catch (CandidateDoesNotExists | JobDoesNotExistsException exception) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseEntity.internalServerError().body("Internal server error.");
    }

  }
}
