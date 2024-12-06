package br.com.thiago.gestao_vagas.modules.canditates.useCases.exceptions;

public class CandidateDoesNotExists extends RuntimeException {

  public CandidateDoesNotExists() {
    super("Candidate does not exists.");
  }
}
