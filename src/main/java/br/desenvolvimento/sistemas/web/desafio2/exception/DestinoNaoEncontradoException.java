package br.desenvolvimento.sistemas.web.desafio2.exception;

public class DestinoNaoEncontradoException extends RuntimeException {
    public DestinoNaoEncontradoException(Long id) {
        super("Destino não encontrado com id: " + id);
    }
}

