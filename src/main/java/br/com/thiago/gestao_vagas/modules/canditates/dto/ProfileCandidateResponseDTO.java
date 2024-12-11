package br.com.thiago.gestao_vagas.modules.canditates.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCandidateResponseDTO {

  private UUID id;

  @Schema(example = "jhon_doe", requiredMode = RequiredMode.REQUIRED)
  private String username;

  @Schema(example = "jhondoe@example.com.br", requiredMode = RequiredMode.REQUIRED)
  private String email;

  @Schema(example = "Jhon Doe", requiredMode = RequiredMode.REQUIRED)
  private String name;

  @Schema(example = "This is me", requiredMode = RequiredMode.REQUIRED)
  private String description;
}
