package br.desenvolvimento.sistemas.web.desafio2.service;

import br.desenvolvimento.sistemas.web.desafio2.entity.Destino;
import br.desenvolvimento.sistemas.web.desafio2.repository.DestinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinoService {

    private final DestinoRepository destinoRepository;

    public List<Destino> listarTodos() {
        return destinoRepository.findAll();
    }

    public Optional<Destino> buscarPorId(Long id) {
        return destinoRepository.findById(id);
    }

    public Destino salvar(Destino destino) {
        LocalDateTime agora = LocalDateTime.now();
        destino.setDataCriacao(agora);
        destino.setDataAtualizacao(agora);
        return destinoRepository.save(destino);
    }

    public Destino atualizar(Long id, Destino destino) {
        return destinoRepository.findById(id)
                .map(existente -> {
                    existente.setNome(destino.getNome());
                    existente.setDescricao(destino.getDescricao());
                    existente.setPais(destino.getPais());
                    existente.setDataAtualizacao(LocalDateTime.now());
                    return destinoRepository.save(existente);
                })
                .orElseThrow(() -> new RuntimeException("Destino não encontrado com id: " + id));
    }

    public void deletar(Long id) {
        destinoRepository.deleteById(id);
    }
}

