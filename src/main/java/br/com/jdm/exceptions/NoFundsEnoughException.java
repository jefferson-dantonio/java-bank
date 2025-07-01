package br.com.jdm.exceptions;

public class NoFundsEnoughException extends RuntimeException {
    public NoFundsEnoughException(String message) {
        super(message);
    }
}
