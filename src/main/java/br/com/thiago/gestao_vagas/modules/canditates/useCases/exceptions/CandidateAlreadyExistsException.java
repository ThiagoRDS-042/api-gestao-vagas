package br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions;

public class CandidateAlreadyExistsException extends RuntimeException {

  public CandidateAlreadyExistsException() {
    super("Candidate already exists.");
  }
}
