package br.com.thiago.gestao_vagas.modules.candidates.useCases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import br.com.thiago.gestao_vagas.modules.canditates.entities.ApplyJobEntity;
import br.com.thiago.gestao_vagas.modules.canditates.entities.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.ApplyJobRepository;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.ApplyJobCandidateUseCase;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateDoesNotExists;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.JobDoesNotExistsException;
import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;
import br.com.thiago.gestao_vagas.modules.companies.repositories.JobRepository;

@ExtendWith(MockitoExtension.class)
public class ApplyJobCandidateUseCaseTest {

  @InjectMocks
  private ApplyJobCandidateUseCase applyJobCandidateUseCase;

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private JobRepository jobRepository;

  @Mock
  private ApplyJobRepository applyJobRepository;

  @Test
  @DisplayName("Should not be able to apply job with candidate doesn't exist")
  public void candidateDoesNotExists() {
    try {
      this.applyJobCandidateUseCase.execute(null, null);
    } catch (Exception e) {
      Assert.isInstanceOf(CandidateDoesNotExists.class, e);
    }

  }

  @Test
  @DisplayName("Should not be able to apply job with job doesn't exist")
  public void jobDoesNotExists() {
    var candidateId = UUID.randomUUID();
    var candidate = new CandidateEntity();
    candidate.setId(candidateId);

    when(this.candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

    try {
      this.applyJobCandidateUseCase.execute(candidateId, null);
    } catch (Exception e) {
      Assert.isInstanceOf(JobDoesNotExistsException.class, e);
    }

  }

  @Test
  @DisplayName("Should be able to create a new apply job")
  public void createNewApplyJon() {
    var candidateId = UUID.randomUUID();
    var candidate = new CandidateEntity();

    var jobId = UUID.randomUUID();
    var job = new JobEntity();

    var applyJob = ApplyJobEntity.builder()
        .candidateId(candidateId)
        .jobId(jobId)
        .build();

    var applyJobCreated = ApplyJobEntity.builder()
        .id(UUID.randomUUID())
        .build();

    when(this.candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

    when(this.jobRepository.findById(jobId)).thenReturn(Optional.of(job));

    when(this.applyJobRepository.save(applyJob)).thenReturn(applyJobCreated);

    var result = this.applyJobCandidateUseCase.execute(candidateId, jobId);

    Assertions.assertThat(result).hasFieldOrProperty("id");
    assertNotNull(result.getId());
  }

}
