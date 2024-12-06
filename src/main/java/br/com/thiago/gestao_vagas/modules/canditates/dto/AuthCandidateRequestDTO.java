package br.com.thiago.gestao_vagas.modules.canditates.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

public record AuthCandidateRequestDTO(
    @NotBlank @Schema(example = "jhon_doe", requiredMode = RequiredMode.REQUIRED) String username,
    @NotBlank @Schema(example = "Jhondoe@123", requiredMode = RequiredMode.REQUIRED) String password) {
}
