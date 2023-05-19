package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repositoriy.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.service.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/configuracao")
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @GetMapping
    public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Configuracao configuracaoBanco = this.configuracaoRepository.findById(id).orElse(null);
        return configuracaoBanco == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(configuracaoBanco);

    }
    @PostMapping

    public ResponseEntity<?> cadastrar(@RequestBody final Configuracao configuracao){
        try{
            final Configuracao newConfig = this.configuracaoService.cadastrar(configuracao);
            return ResponseEntity.ok("Configuração cadastrada com sucesso!");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Configuracao configuracao) {

        try {
            final Configuracao configuracaoAtualizada = this.configuracaoService.editar(id, configuracao);
            return ResponseEntity.ok( "Configuracao  atualizada com sucesso");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
