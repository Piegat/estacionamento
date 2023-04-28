package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repositoriy.MarcaRepository;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/marca")
public class MarcaController {




    @Autowired
    private MarcaRepository marcaRepository;
    private ModeloRepository modeloRepository;

    //-------------------------------- ID ----------------------------------------

    @GetMapping
    public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Marca marca = this.marcaRepository.findById(id).orElse(null);
        return marca == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(marca);

    }

    //-------------------------------- ALL ----------------------------------------

    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){

        return ResponseEntity.ok(this.marcaRepository.findAll());
    }


    //-------------------------------- ATIVO ----------------------------------------

    @GetMapping("/ativo")
    public ResponseEntity<?> findbyAtivo (){

        return ResponseEntity.ok(this.marcaRepository.findByAtivo());

    }



    //-------------------------------- POST ----------------------------------------

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Marca marca){
        try {
            this.marcaRepository.save(marca);
            return ResponseEntity.ok("Registro cadastrado com sucesso");

        }

        catch(Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getStackTrace());
        }

    }


    //-------------------------------- PUT ----------------------------------------

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Marca marca) {

        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);

        try {

            if (marcaBanco == null || !marcaBanco.getId().equals(marca.getId())) {
                throw new RuntimeException("Não foi possivel identificar o registro informado");

            }
            this.marcaRepository.save(marca);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        }

        catch (DataIntegrityViolationException e) {

            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getMessage());

        }

        catch (RuntimeException e) {

            return ResponseEntity.internalServerError().body("Error " + e.getMessage());

        }



    }

    //-------------------------------- DELETE----------------------------------------

    @DeleteMapping
    public ResponseEntity<?> deletar(
            @RequestParam("id") final Long id
    ){
        try{
            final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);
            if(marcaBanco == null){
                throw new RuntimeException("Condutor nao encontrado");
            }
            if(!this.modeloRepository.findByMarcaId(id).isEmpty()){
                marcaBanco.setAtivo(false);
                this.marcaRepository.save(marcaBanco);
                return ResponseEntity.ok("Registro desativado com sucesso!");
            }else{
                this.marcaRepository.delete(marcaBanco);
                return ResponseEntity.ok("Registro apagado com sucesso!");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
}}

}
