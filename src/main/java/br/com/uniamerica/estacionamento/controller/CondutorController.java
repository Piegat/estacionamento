package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repositoriy.CondutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/condutor")
public class CondutorController {

        @Autowired
        private CondutorRepository condutorRepository;
    /*
    http://localhost:8080/api/modelo?id=1

    @param id
    @return
     */
        @GetMapping("/{id}")
        public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Condutor condutor = this.condutorRepository.findById(id).orElse(null);
        return condutor == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(condutor);

    }

        @GetMapping
        public ResponseEntity<?> findbyAtivo (@RequestParam("ativo") final Long id){

        final Condutor condutor = this.condutorRepository.findById(id).orElse(null);
        return condutor == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(condutor);

    }
        @GetMapping("/lista")
        public ResponseEntity<?> listaCompleta(){

        return ResponseEntity.ok(this.condutorRepository.findAll());
    }
        @PostMapping
        public ResponseEntity<?> cadastrar(@RequestBody final Condutor condutor){
        try {
            this.condutorRepository.save(condutor);
            return ResponseEntity.ok("Registro cadastrado com sucesso");

        }

        catch(Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getStackTrace());
        }

    }
        @PutMapping
        public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Condutor condutor) {

        final Condutor condutorBanco = this.condutorRepository.findById(id).orElse(null);

        try {

            if (condutorBanco == null || !condutorBanco.getId().equals(condutor.getId())) {
                throw new RuntimeException("Não foi possivel identificar o registro informado");

            }
            this.condutorRepository.save(condutor);
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
            final Condutor condutorBanco = this.condutorRepository.findById(id).orElse(null);
            assert condutorBanco != null;
            this.condutorRepository.delete(condutorBanco);
            return ResponseEntity.ok("Registro atualizado com sucesso");

        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error " + e.getMessage());
        }



    }


    }
