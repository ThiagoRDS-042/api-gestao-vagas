package br.com.thiago.gestao_vagas.modules.companies.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCompanyDTO {

  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
