package br.desenvolvimento.sistemas.web.desafio2.repository;

import br.desenvolvimento.sistemas.web.desafio2.entity.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, Long> {

    List<Destino> findByPaisIgnoreCase(String pais);

    List<Destino> findByNomeContainingIgnoreCase(String nome);

    List<Destino> findByPaisContainingIgnoreCase(String pais);

    List<Destino> findByNomeContainingIgnoreCaseAndPaisContainingIgnoreCase(String nome, String pais);
}