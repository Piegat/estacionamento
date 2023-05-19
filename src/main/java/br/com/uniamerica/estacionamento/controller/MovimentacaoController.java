package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repositoriy.CondutorRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.MovimentacaoService;
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
    @Autowired
    private MovimentacaoService movimentacaoService;


    //-------------------------------- ID ----------------------------------------
    @GetMapping
    public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        return movimentacao == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(movimentacao);

    }


//    -------------------------------- ABERTA ----------------------------------------

    @GetMapping("/ativo")
    public ResponseEntity<?> findbyAtivo (@RequestParam(value = "ativo", required = false, defaultValue = "true") final boolean ativo){

        final Movimentacao movimentacao = this.movimentacaoRepository.findByAtivo(ativo);
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
            final Movimentacao movimentacaoBanco = this.movimentacaoService.cadastrar(movimentacao);
            return ResponseEntity.ok("Movimentação cadastrada com sucesso");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // -------------------------------- PUT ----------------------------------------
    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Movimentacao movimentacao) {
        {
            try {
                final Movimentacao movimentacaoBanco = this.movimentacaoService.editar(id, movimentacao);
                return ResponseEntity.ok("Movimentação editado com sucesso");
            } catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }


    @DeleteMapping
    public ResponseEntity<?> deletar(
            @RequestParam("id") final Long id
    ){
        try{
            return this.movimentacaoService.desativar(id);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}



