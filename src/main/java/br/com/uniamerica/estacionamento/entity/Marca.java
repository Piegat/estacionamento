package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "marca", schema = "public")

public class Marca extends AbstractEntity{


    @Getter @Setter
    @Column(name = "marca", nullable = false, unique = true, length = 50)
    private String marca;

}


