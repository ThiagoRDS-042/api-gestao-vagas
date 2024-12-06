package br.com.thiago.gestao_vagas.modules.canditates.dto;

import java.util.UUID;

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
  private String username;
  private String email;
  private String name;
  private String description;
}
