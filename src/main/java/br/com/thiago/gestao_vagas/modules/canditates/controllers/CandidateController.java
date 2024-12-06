package br.com.thiago.gestao_vagas.modules.canditates.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.thiago.gestao_vagas.modules.canditates.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.CreateCandidateUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.ProfileCandidateUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateAlreadyExistsException;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateDoesNotExists;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

  @Autowired
  private CreateCandidateUseCase createCandidateUseCase;

  @Autowired
  private ProfileCandidateUseCase profileCandidateUseCase;

  @PostMapping("")
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

}
