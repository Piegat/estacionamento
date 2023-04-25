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

    @GetMapping("/{id}")
    public ResponseEntity<Modelo> findbyIdPatch (@PathVariable("id") final Long id){

      return ResponseEntity.ok(new Modelo());

    }




    /*

    http://localhost:8080/api/modelo?id=1


    @param id
    @return
     */

    @GetMapping
    public ResponseEntity<Modelo> findbyId (@RequestParam("id") final Long id){

        return ResponseEntity.ok(new Modelo());

    }


    /*@GetMapping

    @PostMapping
    @PutMapping
    @DeleteMapping*/

}
