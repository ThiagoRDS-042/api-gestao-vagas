package br.com.thiago.gestao_vagas.modules.canditates.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.thiago.gestao_vagas.modules.canditates.dto.ProfileCandidateResponseDTO;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions.CandidateDoesNotExists;

@Service
public class ProfileCandidateUseCase {

  @Autowired
  private CandidateRepository candidateRepository;

  public ProfileCandidateResponseDTO execute(UUID candidateId) {
    var canditate = this.candidateRepository.findById(candidateId).orElseThrow(() -> {
      throw new CandidateDoesNotExists();
    });

    var candidateDTO = ProfileCandidateResponseDTO.builder()
        .id(canditate.getId())
        .name(canditate.getName())
        .email(canditate.getEmail())
        .username(canditate.getUsername())
        .description(canditate.getDescription())
        .build();

    return candidateDTO;
  }

}
