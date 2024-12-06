package br.com.thiago.gestao_vagas.modules.canditates;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity(name = "candidates")
public class CandidateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank()
  @Column(nullable = false)
  @Schema(example = "Jhon Doe", requiredMode = RequiredMode.REQUIRED)
  private String name;

  @Pattern(regexp = "\\S+", message = "Non use spaces.")
  @Column(nullable = false, unique = true)
  @Schema(example = "jhon_doe", requiredMode = RequiredMode.REQUIRED)
  private String username;

  @Email(message = "Use a valid email address.")
  @Column(nullable = false, unique = true)
  @Schema(example = "jhondoe@example.com.br", requiredMode = RequiredMode.REQUIRED)
  private String email;

  @Length(min = 8, max = 100)
  @Column(nullable = false)
  @Schema(example = "Jhondoe@123", minLength = 8, maxLength = 100, requiredMode = RequiredMode.REQUIRED)
  private String password;

  @Column(nullable = true)
  @Schema(example = "This is my candidate", requiredMode = RequiredMode.NOT_REQUIRED)
  private String description;

  @Column(nullable = true)
  @Schema(example = "curriculum", requiredMode = RequiredMode.NOT_REQUIRED)
  private String curriculum;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
