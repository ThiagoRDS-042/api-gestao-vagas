package br.com.thiago.gestao_vagas.modules.companies.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity(name = "companies")
@Data
public class CompanyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank()
  @Column(nullable = false)
  private String name;

  @Pattern(regexp = "\\S+", message = "Non use spaces.")
  @Column(nullable = false, unique = true)
  private String username;

  @Email(message = "Use a valid email address.")
  @Column(nullable = false, unique = true)
  private String email;

  @Length(min = 8, max = 100)
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String website;

  @Column(nullable = true)
  private String description;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
