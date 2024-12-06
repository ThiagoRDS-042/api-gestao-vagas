package br.com.thiago.gestao_vagas.modules.canditates.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthCandidateRequestDTO(@NotBlank String username, @NotBlank String password) {
}
