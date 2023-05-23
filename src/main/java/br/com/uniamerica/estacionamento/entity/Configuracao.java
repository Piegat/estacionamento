package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "configuracoes", schema = "public")
@Audited
@AuditTable( value = "configuracao_audit", schema = "audit")
public class Configuracao extends AbstractEntity{

    @Getter @Setter
    @Column(name = "valor_hora")
    private BigDecimal valorHora;
    @Getter @Setter
    @Column(name = "valor_multa_minuto")
    private BigDecimal valorMulta;
    @Getter @Setter
    @Column(name = "inicio_expediente")
    private LocalTime inicioExpediente;
    @Getter @Setter
    @Column(name = "fim_expediente")
    private LocalTime fimExpediente;
    @Getter @Setter
    @Column(name = "horas_para_desconto")
    private int horasParaDesconto;
    @Getter @Setter
    @Column(name = "minutos_para_desconto")
    private int minutosParaDesconto;
    @Getter @Setter
    @Column(name = "horas_de_desconto")
    private int horasDeDesconto;
    @Getter @Setter
    @Column(name = "minutos_de_desconto")
    private int minutosDeDesconto;
    @Getter @Setter
    @Column(name = "gerar_desconto")
    private Boolean gerarDesconto;
    @Getter @Setter
    @Column(name = "vagas_moto")
    private int vagasMoto;
    @Getter @Setter
    @Column(name = "vagas_carro")
    private int vagasCarro;
    @Getter @Setter
    @Column(name = "vagas_van")
    private int vagasVan;


}
