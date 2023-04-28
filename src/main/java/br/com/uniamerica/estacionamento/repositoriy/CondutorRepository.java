package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Condutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CondutorRepository extends JpaRepository<Condutor, Long> {
    @Query("select nome from Condutor where nome Like :nome")
    public List<Condutor> findByLike(@Param("nome")final String nome);

    @Query(value = "select * from condutores where ativo = true", nativeQuery = true)
    public List<Condutor> findByAtivo();

}
