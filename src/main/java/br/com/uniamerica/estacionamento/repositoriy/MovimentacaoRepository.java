package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    @Query(value = "select * from Movimentacoes where ativo = true", nativeQuery = true)
    public List<Movimentacao> findByNomeLikeNative(@Param("veiculo") final Movimentacao veiculo);

    @Query("from Movimentacao where condutor.id = :id")
    public List<Movimentacao> findByCondutorId(@Param("id") final Long id);

    @Query("from Movimentacao where veiculo.id = :id")
    public List<Movimentacao> findByVeiculoId(@Param("id") final Long id);

    @Query("from Movimentacao where ativo = true")
    public Movimentacao findByAtivo(@Param("ativo") final boolean ativo);

    @Query("SELECT m FROM Movimentacao m ORDER BY m.id DESC LIMIT 1")
    public Movimentacao findByMovimentacao();



}
