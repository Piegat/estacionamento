package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.controller.MarcaController;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.repositoriy.MarcaRepository;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private ModeloRepository modeloRepository;

    public Marca cadastrar(final Marca marca){

        Assert.notNull(marca.getMarca(), "Marca não informada");
        Assert.hasText(marca.getMarca(), "Marca foi deixada em branco");

        final List<Marca> marcasByNome = this.marcaRepository.findByNome(marca.getMarca());
        Assert.isTrue(marcasByNome.isEmpty(), "Marca já existe!");

        return this.marcaRepository.save(marca);
    }



    public Marca editar(Long id, Marca marca){

        //Verifica se a marca já existe e se é a mesma no body
        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);
        Assert.notNull(marcaBanco, "Marca não encontrada!");
        Assert.isTrue(marcaBanco.getId().equals(marca.getId()), "ID da Marca informada não é a mesmo que o ID da Marca a ser atualizado!");

        Assert.hasText(marca.getMarca(), "Marca foi deixada em branco");
        //Verifica se marca ja existe
        final List<Marca> marcasByNome = this.marcaRepository.findByNome(marca.getMarca());
        if (!marcasByNome.isEmpty()){

            Assert.isTrue(marcasByNome.get(0).getId().equals(marca.getId()), "Marca já existe!");

        }


        Assert.notNull(marca.getMarca(), "Marca não informada");


        return this.marcaRepository.save(marca);
    }


    public ResponseEntity<String> deletar(Long id){

        //Verifica se o condutor informado existe
        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);
        Assert.notNull(marcaBanco, "Condutor não encontrado!");


        //Verifica se o condutor está em movimentação para deletalo ou desativalo
        if (!this.modeloRepository.findByMarcaId(id).isEmpty()) {
            marcaBanco.setAtivo(false);
            this.marcaRepository.save(marcaBanco);
            return ResponseEntity.ok("Marca DESATIVADO pois está relacionado a movimentações!");
        } else {
            this.marcaRepository.delete(marcaBanco);
            return ResponseEntity.ok("Marca DELETADO pois NÂO está relacionado a movimentações!");
        }

    }


}
