package br.desenvolvimento.sistemas.web.desafio2.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}