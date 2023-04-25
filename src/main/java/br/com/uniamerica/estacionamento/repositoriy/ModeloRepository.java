package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {


    @Query("from Modelo where nome Like :nome")
    public List<Modelo> findByLike(@Param("nome")final String nome);


}
