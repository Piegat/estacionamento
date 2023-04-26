package br.com.uniamerica.estacionamento.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Modelo", schema = "public")
public class Modelo extends AbstractEntity{


    @Getter @Setter
    @Column(name = "modelo", nullable = false, unique = true, length = 50)
    private String nome;
    @Getter @Setter
    @JoinColumn(name = "marca", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Marca marca;


}
