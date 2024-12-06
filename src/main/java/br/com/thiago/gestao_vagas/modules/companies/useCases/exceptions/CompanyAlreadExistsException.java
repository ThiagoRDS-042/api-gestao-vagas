package br.com.thiago.gestao_vagas.modules.companies.useCases.exceptions;

public class CompanyAlreadExistsException extends RuntimeException {
  public CompanyAlreadExistsException() {
    super("Company already exists.");
  }
}
