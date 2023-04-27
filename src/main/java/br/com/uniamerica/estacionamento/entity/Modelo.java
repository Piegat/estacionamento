package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "Modelo", schema = "public")
@Audited
@AuditTable(value = "modelo_audit", schema = "audit")
public class Modelo extends AbstractEntity{


    @Getter @Setter
    @Column(name = "modelo", nullable = false, unique = true, length = 50)
    private String nome;
    @Getter @Setter
    @JoinColumn(name = "marca", nullable = false)
    @ManyToOne
    private Marca marca;


}
