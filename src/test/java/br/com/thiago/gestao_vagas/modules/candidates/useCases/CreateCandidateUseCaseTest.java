package br.com.thiago.gestao_vagas.modules.candidates.useCases;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import br.com.thiago.gestao_vagas.modules.canditates.entities.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.CreateCandidateUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateAlreadyExistsException;

@ExtendWith(MockitoExtension.class)
public class CreateCandidateUseCaseTest {

  @InjectMocks
  private CreateCandidateUseCase createCandidateUseCase;

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("Should not be able to create a new candidate with same email address")
  public void emailAddressAlreadyExists() {
    var candidate = CandidateEntity.builder()
        .email("email@example.com.br")
        .build();

    when(this.candidateRepository.findByUsernameOrEmail(null, candidate.getEmail()))
        .thenReturn(Optional.of(new CandidateEntity()));

    try {
      this.createCandidateUseCase.execute(candidate);
    } catch (Exception e) {
      Assert.isInstanceOf(CandidateAlreadyExistsException.class, e);
    }
  }

  @Test
  @DisplayName("Should not be able to create a new candidate with same username")
  public void usernameAlreadyExists() {
    var candidate = CandidateEntity.builder()
        .username("canidate")
        .build();

    when(this.candidateRepository.findByUsernameOrEmail(candidate.getUsername(), null))
        .thenReturn(Optional.of(new CandidateEntity()));

    try {
      this.createCandidateUseCase.execute(candidate);
    } catch (Exception e) {
      Assert.isInstanceOf(CandidateAlreadyExistsException.class, e);
    }
  }

  @Test
  @DisplayName("Should be able to create a new candidate")
  public void createNewCandidate() {
    var candidate = CandidateEntity.builder()
        .username("canidate")
        .email("candidate@example.com")
        .password("password")
        .name("candidate")
        .description("candidate")
        .build();

    when(this.candidateRepository.save(candidate))
        .thenReturn(candidate);

    var createdCandidate = this.createCandidateUseCase.execute(candidate);

    assertEquals(createdCandidate, candidate);
  }
}
