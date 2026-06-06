package br.desenvolvimento.sistemas.web.desafio2.service;

import br.desenvolvimento.sistemas.web.desafio2.entity.Destino;
import br.desenvolvimento.sistemas.web.desafio2.entity.Nota;
import br.desenvolvimento.sistemas.web.desafio2.exception.DestinoNaoEncontradoException;
import br.desenvolvimento.sistemas.web.desafio2.repository.DestinoRepository;
import br.desenvolvimento.sistemas.web.desafio2.repository.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotaService {

    private final NotaRepository notaRepository;
    private final DestinoRepository destinoRepository;

    @Transactional
    public Destino avaliar(Long destinoId, Integer valorNota) {
        // Verificar se o destino existe
        Destino destino = destinoRepository.findById(destinoId)
                .orElseThrow(() -> new DestinoNaoEncontradoException(destinoId));

        // Criar e salvar a nova nota
        Nota nota = Nota.builder()
                .valor(valorNota)
                .dataAvaliacao(LocalDateTime.now())
                .destino(destino)
                .build();
        notaRepository.save(nota);

        // Recalcular a média
        Integer novoTotal = destino.getTotalNotas() + 1;
        Double somaAtual = destino.getNotaMedia() * destino.getTotalNotas();
        Double novaMedia = (somaAtual + valorNota) / novoTotal;

        // Atualizar destino
        destino.setNotaMedia(novaMedia);
        destino.setTotalNotas(novoTotal);

        return destinoRepository.save(destino);
    }
}

