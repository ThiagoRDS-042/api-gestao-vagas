package br.com.thiago.gestao_vagas.modules.companies.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;
import br.com.thiago.gestao_vagas.modules.companies.repositories.CompanyRepository;
import br.com.thiago.gestao_vagas.modules.companies.repositories.JobRepository;
import br.com.thiago.gestao_vagas.modules.companies.useCases.exceptions.CompanyDoesNotExistsException;

@Service
public class CreateJobUseCase {

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private CompanyRepository companyRepository;

  public JobEntity execute(JobEntity jobEntity) {

    this.companyRepository.findById(jobEntity.getCompanyId()).orElseThrow(() -> {
      throw new CompanyDoesNotExistsException();
    });

    return this.jobRepository.save(jobEntity);
  }
}
