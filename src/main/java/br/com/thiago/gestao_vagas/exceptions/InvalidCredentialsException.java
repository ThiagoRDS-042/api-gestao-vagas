package br.com.thiago.gestao_vagas.exceptions;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid credentials.");
  }
}
