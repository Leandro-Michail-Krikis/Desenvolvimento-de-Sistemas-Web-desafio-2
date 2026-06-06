package br.desenvolvimento.sistemas.web.desafio2.repository;

import br.desenvolvimento.sistemas.web.desafio2.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByDestinoId(Long destinoId);

    long countByDestinoId(Long destinoId);
}

