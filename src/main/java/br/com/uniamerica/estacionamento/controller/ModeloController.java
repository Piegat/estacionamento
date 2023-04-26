package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/modelo")
public class ModeloController {



    @Autowired
    private ModeloRepository modeloRepository;



    /*

    http://localhost:8080/api/modelo/1


    @param id
    @return
    */






    /*

    http://localhost:8080/api/modelo?id=1


    @param id
    @return
     */

    @GetMapping
    public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Modelo modelo = this.modeloRepository.findById(id).orElse(null);


        return modelo == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(modelo);

    }


    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){

      return ResponseEntity.ok(this.modeloRepository.findAll());

    }

    @PostMapping

    public ResponseEntity<?> cadastrar(@RequestBody final Modelo modelo){
        this.modeloRepository.save(modelo);

        return ResponseEntity.ok("Registro Cadastrado com Sucesso");


    }
    /*
    @PutMapping
    @DeleteMapping*/

}
