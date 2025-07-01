package br.com.jdm.exceptions;

public class AccountNotFoundException extends RuntimeException {


  public AccountNotFoundException(String message) {
    super(message);
  }
}
