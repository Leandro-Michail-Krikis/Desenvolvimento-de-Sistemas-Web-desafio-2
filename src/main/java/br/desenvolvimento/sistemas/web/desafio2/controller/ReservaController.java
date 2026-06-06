package br.desenvolvimento.sistemas.web.desafio2.controller;

import br.desenvolvimento.sistemas.web.desafio2.dto.request.ReservaRequest;
import br.desenvolvimento.sistemas.web.desafio2.entity.Reserva;
import br.desenvolvimento.sistemas.web.desafio2.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> criar(@Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.reservar(request));
    }

    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<Reserva>> listarPorDestino(@PathVariable Long destinoId) {
        return ResponseEntity.ok(reservaService.listarPorDestino(destinoId));
    }
}

