package br.com.thiago.gestao_vagas.modules.candidates.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.thiago.gestao_vagas.modules.canditates.useCases.ListAllJobsByFilterUseCase;
import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;
import br.com.thiago.gestao_vagas.modules.companies.repositories.JobRepository;

@ExtendWith(MockitoExtension.class)
public class ListAllJobsByFilterUseCaseTest {

  @InjectMocks
  private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

  @Mock
  private JobRepository jobRepository;

  @Test
  @DisplayName("Should be able to list all jobs by filter")
  public void listAllJobsByFilter() {

    var job = JobEntity.builder()
        .benefits("Job")
        .companyId(UUID.randomUUID())
        .description("Job description")
        .level("all jobs")
        .build();

    String filter = "test";

    when(this.jobRepository.findByDescriptionContainingIgnoreCase(filter))
        .thenReturn(Arrays.asList(job));

    var jobs = this.listAllJobsByFilterUseCase.execute(filter);

    assertEquals(jobs.size(), 1);
    Assertions.assertThat(jobs).contains(job);
  }
}
