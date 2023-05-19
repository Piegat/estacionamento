package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.util.List;
@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {


    @Query("from Modelo where nome Like :nome")
    public List<Modelo> findByLike(@Param("nome")final String nome);

    @Query(value = "select * from Modelo where ativo = true", nativeQuery = true)
    public List<Modelo> findByAtivo();

    @Query("from Modelo where nome = :nome")
    public List<Modelo> findByNome(@Param("nome") final String nome);

    @Query("from Modelo where marca.id = :id")
    public List<Modelo> findByMarcaId(@Param("id") final Long id);


}
