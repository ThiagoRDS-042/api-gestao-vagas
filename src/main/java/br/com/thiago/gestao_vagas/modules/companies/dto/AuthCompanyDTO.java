package br.com.thiago.gestao_vagas.modules.companies.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCompanyDTO {

  @NotBlank
  @Schema(example = "jhon_doe", requiredMode = RequiredMode.REQUIRED)
  private String username;

  @NotBlank
  @Schema(example = "Jhondoe@123", requiredMode = RequiredMode.REQUIRED)
  private String password;
}
