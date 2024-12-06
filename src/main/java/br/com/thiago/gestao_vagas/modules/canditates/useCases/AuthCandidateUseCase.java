package br.com.thiago.gestao_vagas.modules.canditates.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.thiago.gestao_vagas.exceptions.InvalidCredentialsException;
import br.com.thiago.gestao_vagas.modules.canditates.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.canditates.dto.AuthCandidateRequestDTO;
import br.com.thiago.gestao_vagas.modules.canditates.dto.AuthCandidateResponseDTO;
import br.com.thiago.gestao_vagas.providers.JWTCandidateProvider;

@Service
public class AuthCandidateUseCase {

  @Autowired
  private CandidateRepository candidateRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JWTCandidateProvider jwtProvider;

  public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) {
    var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username()).orElseThrow(() -> {
      throw new InvalidCredentialsException();
    });

    var passwordDoesMatch = this.passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

    if (!passwordDoesMatch) {
      throw new InvalidCredentialsException();
    }

    Instant expiresIn = Instant.now().plus(Duration.ofMinutes(10));

    var token = this.jwtProvider.createTokenWithClains(candidate.getId().toString(), Arrays.asList("CANDIDATE"),
        expiresIn);

    var authCandidateResponseDTO = AuthCandidateResponseDTO.builder()
        .access_token(token)
        .expires_in(expiresIn.toEpochMilli())
        .build();

    return authCandidateResponseDTO;
  }
}
