package br.com.thiago.gestao_vagas.modules.candidates.useCases;

import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import br.com.thiago.gestao_vagas.exceptions.InvalidCredentialsException;
import br.com.thiago.gestao_vagas.modules.canditates.dto.AuthCandidateRequestDTO;
import br.com.thiago.gestao_vagas.modules.canditates.entities.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.canditates.useCases.AuthCandidateUseCase;
import br.com.thiago.gestao_vagas.providers.JWTCandidateProvider;

@ExtendWith(MockitoExtension.class)
public class AuthCandidateUseCaseTest {

  @InjectMocks
  private AuthCandidateUseCase authCandidateUseCase;

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JWTCandidateProvider jwtProvider;

  @Test
  @DisplayName("Should not be able to authenticate a candidate with invalid email address")
  public void invalidEmailAddress() {
    var authCandidateDTO = new AuthCandidateRequestDTO("invalid-email", "password");

    try {
      this.authCandidateUseCase.execute(authCandidateDTO);
    } catch (Exception e) {
      Assert.isInstanceOf(InvalidCredentialsException.class, e);
    }
  }

  @Test
  @DisplayName("Should not be able to authenticate a candidate with invalid password")
  public void invalidPassword() {
    var authCandidateDTO = new AuthCandidateRequestDTO("email@example.com.br", "invalid-password");

    when(this.candidateRepository.findByUsername(authCandidateDTO.username()))
        .thenReturn(Optional.of(new CandidateEntity()));

    try {
      this.authCandidateUseCase.execute(authCandidateDTO);
    } catch (Exception e) {
      Assert.isInstanceOf(InvalidCredentialsException.class, e);
    }
  }

  @Test
  @DisplayName("Should be able to authenticate a candidate")
  public void authenticate() {
    var authCandidateDTO = new AuthCandidateRequestDTO("email@example.com.br", "P@ssword123");

    var candidate = CandidateEntity.builder()
        .id(UUID.randomUUID())
        .password("P@ssword123")
        .build();

    var expiresIn = Instant.now().plus(Duration.ofMinutes(10)).truncatedTo(ChronoUnit.SECONDS);

    when(this.candidateRepository.findByUsername(authCandidateDTO.username()))
        .thenReturn(Optional.of(candidate));
    when(this.passwordEncoder.matches(authCandidateDTO.password(), candidate.getPassword()))
        .thenReturn(true);
    when(this.jwtProvider.createTokenWithClains(candidate.getId().toString(), Arrays.asList("CANDIDATE"),
        expiresIn))
        .thenReturn("access_token");

    var token = this.authCandidateUseCase.execute(authCandidateDTO);

    Assert.isInstanceOf(String.class, token.getAccess_token());
    Assert.isInstanceOf(Long.class, token.getExpires_in());
  }
}
