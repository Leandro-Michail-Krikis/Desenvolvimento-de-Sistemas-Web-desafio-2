package br.desenvolvimento.sistemas.web.desafio2.service;

import br.desenvolvimento.sistemas.web.desafio2.dto.request.ReservaRequest;
import br.desenvolvimento.sistemas.web.desafio2.entity.Reserva;
import br.desenvolvimento.sistemas.web.desafio2.entity.StatusReserva;
import br.desenvolvimento.sistemas.web.desafio2.exception.DestinoNaoEncontradoException;
import br.desenvolvimento.sistemas.web.desafio2.exception.ReservaNaoEncontradaException;
import br.desenvolvimento.sistemas.web.desafio2.repository.DestinoRepository;
import br.desenvolvimento.sistemas.web.desafio2.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final DestinoRepository destinoRepository;

    public Reserva reservar(ReservaRequest request) {
        var destino = destinoRepository.findById(request.getDestinoId())
                .orElseThrow(() -> new DestinoNaoEncontradoException(request.getDestinoId()));

        Reserva reserva = Reserva.builder()
                .nomeCliente(request.getNomeCliente())
                .email(request.getEmail())
                .dataReserva(LocalDateTime.now())
                .dataViagem(request.getDataViagem())
                .numeroPessoas(request.getNumeroPessoas())
                .status(StatusReserva.CONFIRMADA)
                .destino(destino)
                .build();

        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarPorDestino(Long destinoId) {
        destinoRepository.findById(destinoId)
                .orElseThrow(() -> new DestinoNaoEncontradoException(destinoId));

        return reservaRepository.findByDestinoId(destinoId);
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNaoEncontradaException(id));
    }
}


