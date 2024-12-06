package br.com.thiago.gestao_vagas.modules.canditates.dto;

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
public class AuthCandidateResponseDTO {

  @Schema(example = "asdjahsdjhajdhahjdadhsj", requiredMode = RequiredMode.REQUIRED)
  private String access_token;

  @Schema(example = "1232132323", requiredMode = RequiredMode.REQUIRED)
  private Long expires_in;
}
