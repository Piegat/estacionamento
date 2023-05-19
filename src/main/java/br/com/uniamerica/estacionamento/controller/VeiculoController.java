package br.com.uniamerica.estacionamento.controller;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repositoriy.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.VeiculoService;
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
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private VeiculoService veiculoService;



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
            final Veiculo newVeiculo = this.veiculoService.cadastrar(veiculo);
            return ResponseEntity.ok(String.format("Veiulo placa [ %s ] cadastrado com sucesso!", newVeiculo.getPlaca()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    //-------------------------------- PUT ----------------------------------------

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Veiculo veiculo) {
        try {
            final Veiculo newVeiculo = this.veiculoService.editar(id, veiculo);
            return ResponseEntity.ok(String.format("Veiulo placa [ %s ] editado com sucesso!", newVeiculo.getPlaca()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping
    public ResponseEntity<?> deletar(
            @RequestParam("id") final Long id
    ){{
        try{
            return this.veiculoService.desativar(id);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}}
