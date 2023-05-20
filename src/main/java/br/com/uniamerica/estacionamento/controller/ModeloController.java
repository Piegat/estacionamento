package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repositoriy.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/modelo")
public class ModeloController {



    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private ModeloService modeloService;


    //-------------------------------- ID ----------------------------------------
    @GetMapping
    public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Modelo modelo = this.modeloRepository.findById(id).orElse(null);
        return modelo == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(modelo);

    }

    // -------------------------------- ALL ----------------------------------------
    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){

      return ResponseEntity.ok(this.modeloRepository.findAll());
    }

    //-------------------------------- ATIVO ----------------------------------------

    @GetMapping("/ativo")
    public ResponseEntity<?> findbyAtivo (){

        return ResponseEntity.ok(this.modeloRepository.findByAtivo());

    }
    //-------------------------------- POST ----------------------------------------
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Validated final Modelo modelo){

        try {
            final Modelo newModelo = this.modeloService.cadastrar(modelo);
            return ResponseEntity.ok("Modelo cadastrado com sucesso");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //-------------------------------- PUT ----------------------------------------
    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody @Validated final Modelo modelo) {

        try {
            final Modelo modeloBanco = this.modeloService.editar(id, modelo);
            return ResponseEntity.ok(String.format("Modelo [ %s ] editado com sucesso", modeloBanco.getNome()));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    @DeleteMapping
    public ResponseEntity<?> deletar(
            @RequestParam("id") final Long id
    ){
        try{
            return this.modeloService.deletar(id);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
