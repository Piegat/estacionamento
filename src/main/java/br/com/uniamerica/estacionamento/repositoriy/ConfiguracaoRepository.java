package br.com.uniamerica.estacionamento.repositoriy;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
    @Query(value = "select * from Configuracao where valorHora like :valorHora", nativeQuery = true)
    public List<Configuracao> findByNomeLikeNative(@Param("valorHora") final Configuracao valorHora);}
