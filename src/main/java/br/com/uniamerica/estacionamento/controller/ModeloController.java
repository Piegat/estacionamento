package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/modelo")
public class ModeloController {



    @Autowired
    private ModeloRepository modeloRepository;


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
    public ResponseEntity<?> cadastrar(@RequestBody final Modelo modelo){

        try {

            this.modeloRepository.save(modelo);
            return ResponseEntity.ok("Registro cadastrado com sucesso");

        }

        catch(Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

    }

    //-------------------------------- PUT ----------------------------------------
    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Modelo modelo) {

        final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);

        try {

            if (modeloBanco == null || !modeloBanco.getId().equals(modelo.getId())) {
                throw new RuntimeException("Não foi possivel identificar o registro informado");

            }
                this.modeloRepository.save(modelo);
                return ResponseEntity.ok("Registro atualizado com sucesso");
        }

        catch (DataIntegrityViolationException e) {

            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getMessage());

        }

        catch (RuntimeException e) {

            return ResponseEntity.internalServerError().body("Error " + e.getMessage());

        }



    }


    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("id") final Long id) {


        try {
            final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);
            assert modeloBanco != null;
            this.modeloRepository.delete(modeloBanco);
            return ResponseEntity.ok("Registro atualizado com sucesso");

        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error " + e.getMessage());
        }



    }

}
