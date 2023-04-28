package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import br.com.uniamerica.estacionamento.repositoriy.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/api/veiculo")
public class VeiculoController {





    @Autowired
    private VeiculoRepository veiculoRepository;
    /*
    http://localhost:8080/api/modelo?id=1

    @param id
    @return
     */



    //-------------------------------- ID ----------------------------------------

    @GetMapping
    public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Veiculo veiculo = this.veiculoRepository.findById(id).orElse(null);
        return veiculo == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(veiculo);

    }


    //-------------------------------- ALL  ----------------------------------------

    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){

        return ResponseEntity.ok(this.veiculoRepository.findAll());
    }

    //-------------------------------- ATIVO ----------------------------------------

    @GetMapping("/ativo")
    public ResponseEntity<?> findbyAtivo (){
        return ResponseEntity.ok(this.veiculoRepository.findByAtivo());
    }

    //-------------------------------- POST----------------------------------------


    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Veiculo veiculo){
        try {
            this.veiculoRepository.save(veiculo);
            return ResponseEntity.ok("Registro cadastrado com sucesso");

        }

        catch(Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

    }



    //-------------------------------- PUT ----------------------------------------

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Veiculo veiculo) {

        final Veiculo veiculoBanco = this.veiculoRepository.findById(id).orElse(null);

        try {

            if (veiculoBanco == null || !veiculoBanco.getId().equals(veiculo.getId())) {
                throw new RuntimeException("Não foi possivel identificar o registro informado");

            }
            this.veiculoRepository.save(veiculo);
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
            final Veiculo veiculoBanco = this.veiculoRepository.findById(id).orElse(null);
            assert veiculoBanco != null;
            this.veiculoRepository.delete(veiculoBanco);
            return ResponseEntity.ok("Registro atualizado com sucesso");

        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error " + e.getMessage());
        }



    }

}
