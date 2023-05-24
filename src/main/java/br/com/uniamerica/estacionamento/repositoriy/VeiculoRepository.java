package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.TipoVeiculos;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query(value = "select * from Veiculo where Modelo like :modelo", nativeQuery = true)
    public List<Veiculo> findByNomeLikeNative(@Param("modelo") final Veiculo modelo);


    @Query(value = "select * from veiculos where ativo = :true", nativeQuery = true)
    public List<Veiculo> findByAtivo();

    @Query("from Veiculo where modelo.id = :id")
    public List<Veiculo> findByModeloId(@Param("id") final Long id);

//    @Query("from Veiculo where tipoVeiculos = :tipoVeiculo")
//    public TipoVeiculos findByTipo(@Param("tipoVeiculo") TipoVeiculos tipoVeiculo);


    @Query("from Veiculo where placa = :placa")
    public List<Veiculo> findByPlaca(@Param("placa") final String placa);



}

