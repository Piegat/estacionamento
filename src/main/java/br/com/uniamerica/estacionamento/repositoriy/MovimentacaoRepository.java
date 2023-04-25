package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    @Query(value = "select * from Movimentacao where veiculo like :veiculo", nativeQuery = true)
    public List<Movimentacao> findByNomeLikeNative(@Param("veiculo") final Movimentacao veiculo);
}
