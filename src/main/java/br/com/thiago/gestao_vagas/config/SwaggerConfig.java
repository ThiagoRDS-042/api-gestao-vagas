package br.com.thiago.gestao_vagas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

  @Bean
  OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info().title("Gestão de vagas").description("API Responsável pela gestão de vagas").version("1.0"))
        .schemaRequirement("jwt_auth", this.createSecurityScheme());
  }

  private SecurityScheme createSecurityScheme() {
    return new SecurityScheme().name("jwt_auth").scheme("bearer").bearerFormat("JWT").type(SecurityScheme.Type.HTTP)
        .in(SecurityScheme.In.HEADER);
  }
}
