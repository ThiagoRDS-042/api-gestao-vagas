package br.com.thiago.gestao_vagas.modules.companies.useCases;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.thiago.gestao_vagas.exceptions.InvalidCredentialsException;
import br.com.thiago.gestao_vagas.modules.companies.dto.AuthCompanyDTO;
import br.com.thiago.gestao_vagas.modules.companies.dto.AuthCompanyResponseDTO;
import br.com.thiago.gestao_vagas.modules.companies.repositories.CompanyRepository;
import br.com.thiago.gestao_vagas.providers.JWTCompanyProvider;

@Service
public class AuthCompanyUseCase {

  @Autowired
  private JWTCompanyProvider jwtProvider;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) {
    var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(() -> {
      throw new InvalidCredentialsException();
    });

    var passwordDoesMatch = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

    if (!passwordDoesMatch) {
      throw new InvalidCredentialsException();
    }

    Instant expiresIn = Instant.now().plus(Duration.ofHours(2)).truncatedTo(ChronoUnit.SECONDS);
    ;

    var token = this.jwtProvider.createTokenWithClains(company.getId().toString(), Arrays.asList("COMPANY"),
        expiresIn);

    var authCompanyResponseDTO = AuthCompanyResponseDTO.builder()
        .access_token(token)
        .expires_in(expiresIn.toEpochMilli())
        .build();

    return authCompanyResponseDTO;
  }
}
