package br.desenvolvimento.sistemas.web.desafio2.controller;

import br.desenvolvimento.sistemas.web.desafio2.dto.request.AvaliacaoRequest;
import br.desenvolvimento.sistemas.web.desafio2.dto.request.DestinoRequest;
import br.desenvolvimento.sistemas.web.desafio2.entity.Destino;
import br.desenvolvimento.sistemas.web.desafio2.service.DestinoService;
import br.desenvolvimento.sistemas.web.desafio2.service.NotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinos")
@RequiredArgsConstructor
public class DestinoController {

    private final DestinoService destinoService;
    private final NotaService notaService;

    @GetMapping
    public ResponseEntity<List<Destino>> listarTodos() {
        return ResponseEntity.ok(destinoService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Destino>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String pais) {
        if (nome != null && !nome.isEmpty()) {
            return ResponseEntity.ok(destinoService.buscarPorNome(nome));
        }
        if (pais != null && !pais.isEmpty()) {
            return ResponseEntity.ok(destinoService.buscarPorPais(pais));
        }
        return ResponseEntity.ok(destinoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destino> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(destinoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Destino> criar(@Valid @RequestBody DestinoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(destinoService.salvar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destino> atualizar(@PathVariable Long id, @Valid @RequestBody DestinoRequest request) {
        return ResponseEntity.ok(destinoService.atualizar(id, request));
    }

    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<Destino> avaliar(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequest request) {
        return ResponseEntity.ok(notaService.avaliar(id, request.getValor()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        destinoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

