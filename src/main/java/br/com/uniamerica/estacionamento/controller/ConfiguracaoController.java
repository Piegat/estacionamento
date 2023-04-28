package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repositoriy.CondutorRepository;
import br.com.uniamerica.estacionamento.repositoriy.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/configuracao")
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;
    @GetMapping
    public ResponseEntity<?> findbyId (@RequestParam("id") final Long id){

        final Configuracao configuracaoBanco = this.configuracaoRepository.findById(id).orElse(null);
        return configuracaoBanco == null ? ResponseEntity.badRequest().body("Nenhum valor encontrado. ") : ResponseEntity.ok(configuracaoBanco);

    }
    @PostMapping

    public ResponseEntity<?> cadastrar(@RequestBody final Configuracao configuracao){
        try {
            this.configuracaoRepository.save(configuracao);
            return ResponseEntity.ok("Registro cadastrado com sucesso");

        }

        catch(Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getStackTrace());
        }

    }
    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Configuracao configuracao) {

        final Configuracao configuracaoBanco = this.configuracaoRepository.findById(id).orElse(null);

        try {

            if (configuracaoBanco == null || !configuracaoBanco.getId().equals(configuracao.getId())) {
                throw new RuntimeException("Não foi possivel identificar o registro informado");

            }
            this.configuracaoRepository.save(configuracao);
            return ResponseEntity.ok("Registro atualizado com sucesso");
        }

        catch (DataIntegrityViolationException e) {

            return ResponseEntity.internalServerError().body("Error: " + e.getCause().getMessage());

        }

        catch (RuntimeException e) {

            return ResponseEntity.internalServerError().body("Error " + e.getMessage());

        }



    }


}
