package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "Movimentacoes", schema = "public")
@Audited
@AuditTable( value = "movimentacoes_audit", schema = "audit")
public class Movimentacao extends AbstractEntity{

    @NotNull(message = "Veiculo não pode ser nulo para realizar uma movimentação")
    @Getter @Setter
    @JoinColumn(name = "veiculo", nullable = false)
    @ManyToOne
    private Veiculo veiculo;
    @NotNull(message = "Condutor não pode ser nulo para realizar uma movimentação")
    @Getter @Setter
    @JoinColumn(name = "condutor", nullable = false)
    @ManyToOne
    private Condutor condutor;
    @Getter @Setter
    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime entrada;
    @Getter @Setter
    @Column(name = "data_saida")
    private LocalDateTime saida;
    @Getter @Setter
    @Column(name = "horas_tempo_total")
    private int horastempo;

    @Getter @Setter
    @Column(name = "minutos_tempo_total")
    private int minutostempo;

    @Getter @Setter
    @Column(name = "tempo_desconto")
    private LocalTime tempoDesconto;
    @Getter @Setter
    @Column(name = "horas_multa")
    private int horasMulta;
    @Getter @Setter
    @Column(name = "minutos_multa")
    private int minutosMulta;
    @Getter @Setter
    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;
    @Getter @Setter
    @Column(name = "valor_multa")
    private BigDecimal valorMulta;
    @Getter @Setter
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Getter @Setter
    @Column(name = "valor_hora")
    private BigDecimal valorHora;
    @Getter @Setter
    @Column(name = "valorMinutoMulta")
    private BigDecimal valorMinutoMulta;

}
