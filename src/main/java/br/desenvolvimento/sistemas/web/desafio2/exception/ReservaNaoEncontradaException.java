package br.desenvolvimento.sistemas.web.desafio2.exception;

public class ReservaNaoEncontradaException extends RuntimeException {
    public ReservaNaoEncontradaException(Long id) {
        super("Reserva não encontrada com id: " + id);
    }
}

