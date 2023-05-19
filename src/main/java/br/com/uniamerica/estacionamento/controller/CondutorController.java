package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repositoriy.CondutorRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.CondutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/condutor")
public class CondutorController {

        @Autowired
        private CondutorRepository condutorRepository;
        @Autowired
        private MovimentacaoRepository movimentacaoRepository;
        @Autowired
        private CondutorService condutorService;



    //-------------------------------- ID ----------------------------------------

        @GetMapping
        public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Condutor condutor = this.condutorRepository.findById(id).orElse(null);
        return condutor == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(condutor);

    }

    //-------------------------------- ALL  ----------------------------------------

    @GetMapping("/lista")
        public ResponseEntity<?> listaCompleta(){

        return ResponseEntity.ok(this.condutorRepository.findAll());
    }


    //-------------------------------- ATIVO ----------------------------------------

    @GetMapping("/ativo")
    public ResponseEntity<?> findbyAtivo(){
        return ResponseEntity.ok(this.condutorRepository.findByAtivo());


    }


    //-------------------------------- POST  ----------------------------------------


    @PostMapping
        public ResponseEntity<?> cadastrar(@RequestBody @Validated final Condutor condutor){
        try {
            final Condutor newCondutor = this.condutorService.cadastrar(condutor);
            return ResponseEntity.ok("Condutor cadastrado com sucesso");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //-------------------------------- PUT ----------------------------------------

    @PutMapping
        public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody @Validated final Condutor condutor) {
        try {
            final Condutor condutorBanco = this.condutorService.editar(id, condutor);
            return ResponseEntity.ok("Condutor  editado com sucesso");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //-------------------------------- DELETE ----------------------------------------


    @DeleteMapping
    public ResponseEntity<?> deletar(
            @RequestParam("id") final Long id
    ){
        try{
            return this.condutorService.deletar(id);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }





}