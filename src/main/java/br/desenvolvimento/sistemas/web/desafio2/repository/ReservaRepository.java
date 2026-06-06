package br.desenvolvimento.sistemas.web.desafio2.repository;

import br.desenvolvimento.sistemas.web.desafio2.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByDestinoId(Long destinoId);
}

