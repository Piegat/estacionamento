package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "Modelo", schema = "public")
@Audited
@AuditTable(value = "modelo_audit", schema = "audit")
public class Modelo extends AbstractEntity{


    @NotNull(message = "Nome do modelo não pode ser nulo!")
    @Size(min = 1, max = 50, message = "Tamanho do nome do modelo está incorreto, minimo: 1 | maximo: 50")
    @Getter @Setter
    @Column(name = "modelo", nullable = false, unique = true, length = 50)
    private String nome;

    @NotNull(message = "Marca não informada")
    @Getter @Setter
    @JoinColumn(name = "marca", nullable = false)
    @ManyToOne
    private Marca marca;


}
