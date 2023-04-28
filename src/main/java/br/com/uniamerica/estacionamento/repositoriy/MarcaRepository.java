package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MarcaRepository  extends JpaRepository<Marca, Long> {

    @Query("from Marca where marca Like :marca")
    public List<Marca> findByLike(@Param("marca")final String marca);

    @Query(value = "select * from marca where ativo = true", nativeQuery = true)
    public List<Marca> findByAtivo();

}
