package br.com.thiago.gestao_vagas.modules.canditates.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thiago.gestao_vagas.modules.canditates.entities.ApplyJobEntity;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.ApplyJobRepository;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateDoesNotExists;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.JobDoesNotExistsException;
import br.com.thiago.gestao_vagas.modules.companies.repositories.JobRepository;

@Service
public class ApplyJobCandidateUseCase {

  @Autowired
  private CandidateRepository candidateRepository;

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private ApplyJobRepository applyJobRepository;

  public ApplyJobEntity execute(UUID candidateId, UUID jobId) {

    this.candidateRepository.findById(candidateId).orElseThrow(() -> {
      throw new CandidateDoesNotExists();
    });

    this.jobRepository.findById(jobId).orElseThrow(() -> {
      throw new JobDoesNotExistsException();
    });

    var applyJob = ApplyJobEntity.builder()
        .candidateId(candidateId)
        .jobId(jobId)
        .build();

    applyJob = this.applyJobRepository.save(applyJob);

    return applyJob;
  }
}
