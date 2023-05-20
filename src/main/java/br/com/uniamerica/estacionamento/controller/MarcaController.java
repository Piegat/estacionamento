package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repositoriy.MarcaRepository;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/marca")
public class MarcaController {




    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private MarcaService marcaService;

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
    public ResponseEntity<?> cadastrar(@RequestBody @Validated final Marca marca){
        try{
            final Marca newMarca = this.marcaService.cadastrar(marca);
            return ResponseEntity.ok("Marca cadastrada com sucesso!");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    //-------------------------------- PUT ----------------------------------------

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody @Validated final Marca marca) {


        try {
            final Marca marcaAtualizada = this.marcaService.editar(id, marca);
            return ResponseEntity.ok( "Marca  atualizada com sucesso");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    //-------------------------------- DELETE----------------------------------------

    @DeleteMapping
    public ResponseEntity<?> deletar(
            @RequestParam("id") final Long id
    ){
        try{
        return this.marcaService.deletar(id);
    }catch (Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

}
