package br.com.thiago.gestao_vagas.modules.companies.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobDTO {

  @NotBlank()
  @Schema(example = "Vaga to the dev jr", requiredMode = RequiredMode.REQUIRED)
  private String description;

  @NotBlank()
  @Schema(example = "GYMPass and health plan", requiredMode = RequiredMode.REQUIRED)
  private String level;

  @NotBlank()
  @Schema(example = "JR", requiredMode = RequiredMode.REQUIRED)
  private String benefits;
}
