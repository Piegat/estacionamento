package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalTime;

@Entity
@Table(name = "condutores", schema = "public")

@Audited
@AuditTable( value = "condutores_audit", schema = "audit")
public class Condutor extends AbstractEntity{


    @Size(min = 1, max = 100, message = "Tamanho não respeitado, minimo: 5 | maximo: 100")
    @Getter @Setter
    @Column(name = "nome_Condutor", nullable = false, length = 100)
    private String nome;
    @CPF(message = "Digite um CPF valido!")
    @NotNull
    @Getter @Setter
    @Column(name = "cpf_condutor", nullable = false, unique = true, length = 15)
    private String cpf;
    @Getter @Setter
    @Column(name = "telefone_condutor", nullable = false, length = 20)
    private String telefone;

    @NotNull
    @Getter @Setter
    @Column(name = "tempo_desconto", nullable = false)
    private LocalTime tempoDesconto;

    @NotNull
    @Getter @Setter
    @Column(name = "tempo_horas", nullable = false)
    private int horas;

    @NotNull
    @Getter @Setter
    @Column(name = "tempo_minutos", nullable = false)
    private int minutos;

}


