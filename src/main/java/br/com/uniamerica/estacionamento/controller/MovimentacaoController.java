package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repositoriy.CondutorRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/movimentacoes")
public class MovimentacaoController {


    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    //-------------------------------- ID ----------------------------------------
    @GetMapping
    public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(movimentacao);

    }


//    -------------------------------- ABERTA ----------------------------------------

    @GetMapping("/ativo")
    public ResponseEntity<?> findbyAtivo (@RequestParam(value = "ativo", required = false, defaultValue = "true") final Long id){

        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(movimentacao);

    }

    //-------------------------------- ALL ----------------------------------------


    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){

        return ResponseEntity.ok(this.movimentacaoRepository.findAll());
    }


    // -------------------------------- POST ----------------------------------------
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Movimentacao movimentacao){
        try {
            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("Registro cadastrado com sucesso");

        }

        catch(Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getStackTrace());
        }

    }

    // -------------------------------- PUT ----------------------------------------
    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Movimentacao movimentacao) {

        final Movimentacao movimentacaoBanco = this.movimentacaoRepository.findById(id).orElse(null);

        try {

            if (movimentacaoBanco == null || !movimentacaoBanco.getId().equals(movimentacao.getId())) {
                throw new RuntimeException("Não foi possivel identificar o registro informado");

            }
            this.movimentacaoRepository.save(movimentacao);
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
            final Movimentacao movimentacaoBanco = this.movimentacaoRepository.findById(id).orElse(null);
            assert movimentacaoBanco != null;
            this.movimentacaoRepository.delete(movimentacaoBanco);
            return ResponseEntity.ok("Registro atualizado com sucesso");


        }
        catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error " + e.getMessage());
        }



    }


}
