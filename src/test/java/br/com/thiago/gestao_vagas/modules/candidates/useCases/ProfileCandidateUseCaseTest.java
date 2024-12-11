package br.com.thiago.gestao_vagas.modules.candidates.useCases;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import br.com.thiago.gestao_vagas.modules.canditates.entities.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.ProfileCandidateUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateDoesNotExists;

@ExtendWith(MockitoExtension.class)
public class ProfileCandidateUseCaseTest {

  @InjectMocks
  private ProfileCandidateUseCase profileCandidateUseCase;

  @Mock
  private CandidateRepository candidateRepository;

  @Test
  @DisplayName("Should not be able to get profile with candidate does not exists")
  public void candidateDoesNotExists() {
    try {
      this.profileCandidateUseCase.execute(UUID.randomUUID());
    } catch (Exception e) {
      Assert.isInstanceOf(CandidateDoesNotExists.class, e);
    }

  }

  @Test
  @DisplayName("Should be able to get a candidate profile")
  public void candidateProfile() {
    var candidate = CandidateEntity.builder()
        .username("canidate")
        .email("candidate@example.com")
        .password("password")
        .name("candidate")
        .description("candidate")
        .build();

    when(this.candidateRepository.findById(candidate.getId())).thenReturn(Optional.of(candidate));

    var profile = this.profileCandidateUseCase.execute(candidate.getId());

    assertAll(
        () -> assertEquals(candidate.getEmail(), profile.getEmail()),
        () -> assertEquals(candidate.getDescription(), profile.getDescription()),
        () -> assertEquals(candidate.getName(), profile.getName()),
        () -> assertEquals(candidate.getUsername(), profile.getUsername()));
  }
}
