package br.com.thiago.gestao_vagas.modules.companies.useCases;

import static org.junit.Assert.assertEquals;
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

import br.com.thiago.gestao_vagas.modules.companies.entities.CompanyEntity;
import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;
import br.com.thiago.gestao_vagas.modules.companies.repositories.CompanyRepository;
import br.com.thiago.gestao_vagas.modules.companies.repositories.JobRepository;
import br.com.thiago.gestao_vagas.modules.companies.useCases.exceptions.CompanyDoesNotExistsException;

@ExtendWith(MockitoExtension.class)
public class CreateJobUseCaseTest {
  @InjectMocks
  private CreateJobUseCase createJobUseCase;

  @Mock
  private CompanyRepository companyRepository;

  @Mock
  private JobRepository jobRepository;

  @Test
  @DisplayName("Should not be able to create a new job with company does not exists")
  public void companyDoesNotExists() {

    var job = JobEntity.builder()
        .benefits("Job")
        .companyId(UUID.randomUUID())
        .description("Job description")
        .level("all jobs")
        .build();

    try {
      this.createJobUseCase.execute(job);
    } catch (Exception e) {
      Assert.isInstanceOf(CompanyDoesNotExistsException.class, e);
    }

  }

  @Test
  @DisplayName("Should be able to create a new job ")
  public void createNewJob() {
    var company = CompanyEntity.builder()
        .description("company")
        .email("company@example.com.br")
        .name("company")
        .password("Company@123")
        .website("http://example.com")
        .build();

    var job = JobEntity.builder()
        .benefits("Job")
        .companyId(company.getId())
        .description("Job description")
        .level("all jobs")
        .build();

    when(this.companyRepository.findById(company.getId()))
        .thenReturn(Optional.of(company));
    when(this.jobRepository.save(job))
        .thenReturn(job);

    var jobCreated = this.createJobUseCase.execute(job);

    assertEquals(job, jobCreated);
    Assertions.assertThat(jobCreated).hasFieldOrProperty("id");
  }
}
