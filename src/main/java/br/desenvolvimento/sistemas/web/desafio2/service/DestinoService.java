package br.desenvolvimento.sistemas.web.desafio2.service;

import br.desenvolvimento.sistemas.web.desafio2.dto.request.DestinoRequest;
import br.desenvolvimento.sistemas.web.desafio2.entity.Destino;
import br.desenvolvimento.sistemas.web.desafio2.exception.DestinoNaoEncontradoException;
import br.desenvolvimento.sistemas.web.desafio2.repository.DestinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinoService {

    private final DestinoRepository destinoRepository;

    public List<Destino> listarTodos() {
        return destinoRepository.findAll();
    }

    public Destino buscarPorId(Long id) {
        return destinoRepository.findById(id)
                .orElseThrow(() -> new DestinoNaoEncontradoException(id));
    }

    public List<Destino> buscarPorNome(String nome) {
        return destinoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Destino> buscarPorPais(String pais) {
        return destinoRepository.findByPaisIgnoreCase(pais);
    }

    public Destino salvar(DestinoRequest request) {
        LocalDateTime agora = LocalDateTime.now();
        Destino destino = Destino.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .pais(request.getPais())
                .dataCriacao(agora)
                .dataAtualizacao(agora)
                .notaMedia(0.0)
                .totalNotas(0)
                .build();
        return destinoRepository.save(destino);
    }

    public Destino atualizar(Long id, DestinoRequest request) {
        Destino existente = buscarPorId(id);
        existente.setNome(request.getNome());
        existente.setDescricao(request.getDescricao());
        existente.setPais(request.getPais());
        existente.setDataAtualizacao(LocalDateTime.now());
        return destinoRepository.save(existente);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        destinoRepository.deleteById(id);
    }
}

