package br.com.thiago.gestao_vagas.modules.companies.useCases.exceptions;

public class CompanyDoesNotExistsException extends RuntimeException {
  public CompanyDoesNotExistsException() {
    super("Company does not exists.");
  }
}
