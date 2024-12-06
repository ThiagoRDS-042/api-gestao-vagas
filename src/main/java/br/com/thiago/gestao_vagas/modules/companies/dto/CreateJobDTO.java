package br.com.thiago.gestao_vagas.modules.companies.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateJobDTO {

  private String description;

  @NotBlank()
  private String level;

  @NotBlank()
  private String benefits;
}
