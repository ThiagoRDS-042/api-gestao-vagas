package br.com.thiago.gestao_vagas.modules.canditates.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.thiago.gestao_vagas.modules.canditates.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateAlreadyExistsException;

@Service
public class CreateCandidateUseCase {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private CandidateRepository candidateRepository;

  public CandidateEntity execute(CandidateEntity candidateEntity) {
    this.candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail())
        .ifPresent((candidate) -> {
          throw new CandidateAlreadyExistsException();
        });

    var password = this.passwordEncoder.encode(candidateEntity.getPassword());

    candidateEntity.setPassword(password);

    return this.candidateRepository.save(candidateEntity);
  }
}
