package br.com.thiago.gestao_vagas.modules.companies.useCases;

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
import br.com.thiago.gestao_vagas.modules.companies.dto.AuthCompanyDTO;
import br.com.thiago.gestao_vagas.modules.companies.entities.CompanyEntity;
import br.com.thiago.gestao_vagas.modules.companies.repositories.CompanyRepository;
import br.com.thiago.gestao_vagas.providers.JWTCompanyProvider;

@ExtendWith(MockitoExtension.class)
public class AuthCompanyUseCaseTest {
  @InjectMocks
  private AuthCompanyUseCase authCompanyUseCase;

  @Mock
  private JWTCompanyProvider jwtProvider;

  @Mock
  private CompanyRepository companyRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("Should not be able to authenticate a company with invalid username")
  public void invalidUsername() {

    var auth = AuthCompanyDTO.builder()
        .username("invalid-username")
        .password("Company@123")
        .build();

    try {
      this.authCompanyUseCase.execute(auth);
    } catch (Exception e) {
      Assert.isInstanceOf(InvalidCredentialsException.class, e);
    }
  }

  @Test
  @DisplayName("Should not be able to authenticate a company with invalid password")
  public void invalidPassword() {

    var auth = AuthCompanyDTO.builder()
        .username("company@example.com.br")
        .password("Company@123")
        .build();

    when(this.companyRepository.findByUsername(auth.getUsername())).thenReturn(Optional.of(new CompanyEntity()));

    try {
      this.authCompanyUseCase.execute(auth);
    } catch (Exception e) {
      Assert.isInstanceOf(InvalidCredentialsException.class, e);
    }
  }

  @Test
  @DisplayName("Should be able to authenticate a company")
  public void authenticateCompany() {

    var auth = AuthCompanyDTO.builder()
        .username("company@example.com.br")
        .password("Company@123")
        .build();

    var company = CompanyEntity.builder()
        .id(UUID.randomUUID())
        .username("Company")
        .password("Company@123")
        .build();

    var expiresIn = Instant.now().plus(Duration.ofHours(2)).truncatedTo(ChronoUnit.SECONDS);

    when(this.companyRepository.findByUsername(auth.getUsername())).thenReturn(Optional.of(company));
    when(this.passwordEncoder.matches(auth.getPassword(), company.getPassword())).thenReturn(true);
    when(this.jwtProvider.createTokenWithClains(company.getId().toString(), Arrays.asList("COMPANY"),
        expiresIn))
        .thenReturn("access_token");

    var token = this.authCompanyUseCase.execute(auth);

    Assert.isInstanceOf(String.class, token.getAccess_token());
    Assert.isInstanceOf(Long.class, token.getExpires_in());
  }
}
