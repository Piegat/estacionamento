package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repositoriy.MarcaRepository;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import br.com.uniamerica.estacionamento.repositoriy.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ModeloService {
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;

    @Transactional
    public Modelo cadastrar(final Modelo modelo){
        Assert.notNull(modelo.getNome(), "Nome do modelo não informado");
        Assert.notNull(modelo.getMarca(), "Marca não informada");
        Assert.hasText(modelo.getNome(), "Nome do modelo não informado");


        final Marca marca = this.marcaRepository.findById(modelo.getMarca().getId()).orElse(null);
        Assert.notNull(modelo.getMarca().getId(), "Marca inexistente");

            return this.modeloRepository.save(modelo);
    }


    @Transactional
    public Modelo editar(Long id, final Modelo modelo){
        // Verifica se o modelo existe
        final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);
        Assert.notNull(modeloBanco, "Modelo inexistente!");

        // Consula se é o mesmo modelo
        Assert.isTrue(modeloBanco.getId().equals(modelo.getId()), "Modelo informado não é o mesmo que o modelo a ser atualizado");

        // Verificando campos NotNull
        Assert.notNull(modelo.getCadastro(), "Data do cadastro não informada!");

        Assert.notNull(modelo.getNome(), "Nome do modelo não informado!");
        Assert.hasText(modelo.getNome(), "Nome do modelo não informado!");

        Assert.notNull(modelo.getMarca(), "Marca não informada!");

        // Verifica se o modelo já existe!
        final List<Modelo> modelosByNome = this.modeloRepository.findByNome(modelo.getNome());
        if (!modelosByNome.isEmpty()){
            Assert.isTrue(modelosByNome.get(0).getId().equals(modelo.getId()), "Modelo já existe!");
        }
        // Verifica se a marca já existe
        final Marca marca = this.marcaRepository.findById(modelo.getMarca().getId()).orElse(null);
        Assert.notNull(marca, "Marca inexistente");

        return this.modeloRepository.save(modelo);

    }


    @Transactional
    public ResponseEntity<String> deletar(Long id){

        final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);
        Assert.notNull(modeloBanco, "Modelo inexistente");


        // if se é desativado ou deletado
        if (!this.veiculoRepository.findByModeloId(id).isEmpty()){
        modeloBanco.setAtivo(false);
        this.modeloRepository.save(modeloBanco);
        return ResponseEntity.ok("Modelo DESATIVADO!!");
        }else{

        this.modeloRepository.delete(modeloBanco);
        return ResponseEntity.ok("Modelo DELETADO!!");
        }
    }
}
