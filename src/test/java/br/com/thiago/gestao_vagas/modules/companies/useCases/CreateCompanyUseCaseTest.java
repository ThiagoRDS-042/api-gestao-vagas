package br.com.thiago.gestao_vagas.modules.companies.useCases;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import br.com.thiago.gestao_vagas.modules.companies.entities.CompanyEntity;
import br.com.thiago.gestao_vagas.modules.companies.repositories.CompanyRepository;
import br.com.thiago.gestao_vagas.modules.companies.useCases.exceptions.CompanyAlreadExistsException;

@ExtendWith(MockitoExtension.class)
public class CreateCompanyUseCaseTest {
  @InjectMocks
  private CreateCompanyUseCase createCompanyUseCase;

  @Mock
  private CompanyRepository companyRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("Should not be able to create a new company with the same username")
  public void usernameAlreadyExists() {

    var company = CompanyEntity.builder()
        .description("company")
        .name("company")
        .password("Company@123")
        .username("Company")
        .website("http://example.com")
        .build();

    when(this.companyRepository.findByUsernameOrEmail(company.getUsername(), null))
        .thenReturn(Optional.of(new CompanyEntity()));

    try {
      this.createCompanyUseCase.execute(company);
    } catch (Exception e) {
      Assert.isInstanceOf(CompanyAlreadExistsException.class, e);
    }

  }

  @Test
  @DisplayName("Should not be able to create a new company with the same email")
  public void emailAlreadyExists() {

    var company = CompanyEntity.builder()
        .description("company")
        .email("company@example.com.br")
        .name("company")
        .password("Company@123")
        .website("http://example.com")
        .build();

    when(this.companyRepository.findByUsernameOrEmail(null, company.getEmail()))
        .thenReturn(Optional.of(new CompanyEntity()));

    try {
      this.createCompanyUseCase.execute(company);
    } catch (Exception e) {
      Assert.isInstanceOf(CompanyAlreadExistsException.class, e);
    }

  }

  @Test
  @DisplayName("Should be able to create a new company")
  public void createNewCompany() {
    var company = CompanyEntity.builder()
        .description("company")
        .email("company@example.com.br")
        .name("company")
        .password("Company@123")
        .website("http://example.com")
        .build();

    when(this.companyRepository.save(company))
        .thenReturn(company);

    var companyCreated = this.createCompanyUseCase.execute(company);

    assertEquals(companyCreated, company);
  }

}
