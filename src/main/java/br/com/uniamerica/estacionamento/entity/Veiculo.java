package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "veiculos", schema = "public")
@Audited
@AuditTable( value = "veiculos_audit", schema = "audit")
public class Veiculo extends AbstractEntity{


    @Getter @Setter
    @JoinColumn(name = "modelo", nullable = false)
    @ManyToOne
    private Modelo modelo;
    @Getter @Setter
    @Column(name = "placa", nullable = false, unique = true, length = 10)
    private String placa;
    @Getter @Setter
    @Column(name = "cor", nullable = false)
    @Enumerated(EnumType.STRING)
    private Cor cor;

    @Getter @Setter
    @Column(name = "tipo_veiculo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoVeiculos tipoVeiculos;

    @Getter @Setter
    @Column(name = "ano")
    private int  ano;

}
