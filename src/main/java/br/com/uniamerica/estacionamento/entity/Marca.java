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

@Entity
@Table(name = "marca", schema = "public")
@Audited
@AuditTable( value = "marca_audit", schema = "audit")
public class Marca extends AbstractEntity{


    @NotNull
    @Getter @Setter
    @Size(min = 1, max = 25, message = "Tamanho indevido para nomear uma marca! Respeite o minimo de 1 caractere e o maximo de 50")
    @Column(name = "marca", nullable = false, unique = true, length = 25)
    private String marca;

}


