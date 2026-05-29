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

    public Destino avaliar(Long id, Integer nota) {
        if (nota < 1 || nota > 10) {
            throw new RuntimeException("Nota deve ser entre 1 e 10");
        }

        return destinoRepository.findById(id)
                .map(destino -> {
                    int novaQuantidade = destino.getQuantidadeAvaliacoes() != null
                            ? destino.getQuantidadeAvaliacoes() + 1
                            : 1;
                    int novaSoma = (destino.getSomaAvaliacoes() != null ? destino.getSomaAvaliacoes() : 0) + nota;
                    double novaMedia = (double) novaSoma / novaQuantidade;

                    destino.setQuantidadeAvaliacoes(novaQuantidade);
                    destino.setSomaAvaliacoes(novaSoma);
                    destino.setMediaAvaliacoes(Math.round(novaMedia * 10.0) / 10.0);

                    return destinoRepository.save(destino);
                })
                .orElseThrow(() -> new RuntimeException("Destino não encontrado"));
    }

    public List<Destino> pesquisar(String nome, String pais) {
        if (nome != null && !nome.isEmpty() && pais != null && !pais.isEmpty()) {
            return destinoRepository.findByNomeContainingIgnoreCaseAndPaisContainingIgnoreCase(nome, pais);
        } else if (nome != null && !nome.isEmpty()) {
            return destinoRepository.findByNomeContainingIgnoreCase(nome);
        } else if (pais != null && !pais.isEmpty()) {
            return destinoRepository.findByPaisContainingIgnoreCase(pais);
        } else {
            return destinoRepository.findAll();
        }
    }
}