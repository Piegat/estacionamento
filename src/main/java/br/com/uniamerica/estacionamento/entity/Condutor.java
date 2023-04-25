package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
@Entity
@Table(name = "condutores", schema = "public")


public class Condutor extends AbstractEntity{


    @Getter @Setter
    @Column(name = "nome_Condutor", nullable = false, length = 100)
    private String nome;
    @Getter @Setter
    @Column(name = "cpf_condutor", nullable = false, unique = true, length = 15)
    private String cpf;
    @Getter @Setter
    @Column(name = "telefone_condutor", nullable = false, length = 20)
    private String telefone;
    @Getter @Setter
    @Column(name = "tempo_pago", nullable = false)
    private LocalTime tempoPago;
    @Getter @Setter
    @Column(name = "tempo_desconto", nullable = false)
    private LocalTime tempoDesconto;

}
