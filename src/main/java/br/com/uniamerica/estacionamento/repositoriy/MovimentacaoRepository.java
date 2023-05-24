package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.TipoVeiculos;
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

    @Query ("from Movimentacao  where saida = null and veiculo.tipoVeiculos = :tipoVeiculo ")
    public List <Movimentacao> findByVeiculoCarro(@Param("tipoVeiculo") String tipoVeiculo);

}
