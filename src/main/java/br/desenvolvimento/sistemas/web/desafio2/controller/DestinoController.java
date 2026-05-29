package br.desenvolvimento.sistemas.web.desafio2.controller;

import br.desenvolvimento.sistemas.web.desafio2.entity.Destino;
import br.desenvolvimento.sistemas.web.desafio2.service.DestinoService;
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

    @GetMapping
    public ResponseEntity<List<Destino>> listarTodos() {
        return ResponseEntity.ok(destinoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destino> buscarPorId(@PathVariable Long id) {
        return destinoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Destino> criar(@RequestBody Destino destino) {
        return ResponseEntity.status(HttpStatus.CREATED).body(destinoService.salvar(destino));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destino> atualizar(@PathVariable Long id, @RequestBody Destino destino) {
        try {
            return ResponseEntity.ok(destinoService.atualizar(id, destino));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        destinoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<Destino> avaliar(@PathVariable Long id, @RequestParam Integer nota) {
        try {
            return ResponseEntity.ok(destinoService.avaliar(id, nota));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Destino>> pesquisar(@RequestParam(required = false) String nome,
            @RequestParam(required = false) String pais) {
        return ResponseEntity.ok(destinoService.pesquisar(nome, pais));
    }

}