package br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions;

public class JobDoesNotExistsException extends RuntimeException {

  public JobDoesNotExistsException() {
    super("Job does not exists.");
  }
}
