package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repositoriy.ModeloRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repositoriy.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Transactional
    public Veiculo cadastrar(Veiculo veiculo){
        Assert.notNull(veiculo.getPlaca(), "Placa não foi informada!");
        Assert.hasText(veiculo.getPlaca(), "Placa informada em branco!");
        Assert.notNull(veiculo.getCor(), "Cor não informada!");
        Assert.notNull(veiculo.getTipoVeiculos(), "Tipo do veículo não foi informado!");


        final List<Veiculo> veiculosByPlaca = this.veiculoRepository.findByPlaca(veiculo.getPlaca());
        Assert.isTrue(veiculosByPlaca.isEmpty(),  String.format("Veiculo com placa [ %s ] já existe!", veiculo.getPlaca()));

        Assert.notNull(veiculo.getModelo(), "Modelo não informado!");

        final Modelo modelo = this.modeloRepository.findById(veiculo.getModelo().getId()).orElse(null);
        Assert.notNull(modelo, "Modelo informado não existe!");

        return this.veiculoRepository.save(veiculo);


    }
    @Transactional
    public Veiculo editar(Long id, Veiculo veiculo){

         // Verifica se o veiculo existe
        final Veiculo veiculoBanco = this.veiculoRepository.findById(id).orElse(null);
        Assert.notNull(veiculoBanco, "Veiculo não existe!");

        // Verifica campos notnull
        Assert.notNull(veiculo.getCadastro(), "Não informado a data de cadastro");
        Assert.notNull(veiculo.getId(), "Não informado o ID");
        Assert.notNull(veiculo.getAtivo(), "Não informado ATIVO = True / Else");

        // Verifica os veiculos coincidem
        Assert.isTrue(veiculoBanco.getId().equals(veiculo.getId()), "Veiculo informado não é o mesmo que o veiculo a ser atualizado");



//         * Verifica se modelo existe
        Assert.notNull(veiculo.getModelo(), "Modelo não informado!");
        final Modelo modelo = this.modeloRepository.findById(veiculo.getModelo().getId()).orElse(null);
        Assert.notNull(modelo, "Modelo não existe!");

        return this.veiculoRepository.save(veiculo);
    }


    @Transactional
    public ResponseEntity<?> desativar(Long id){

//         * Verifica se o Veiculo informado existe

        final Veiculo veiculoBanco = this.veiculoRepository.findById(id).orElse(null);
        Assert.notNull(veiculoBanco, "Modelo não encontrado!");

//         * Verifica se o Veiculo informado está relacionado a um Veiculo,

        if(!this.movimentacaoRepository.findByVeiculoId(id).isEmpty()){
            veiculoBanco.setAtivo(false);
            this.veiculoRepository.save(veiculoBanco);
            return ResponseEntity.ok( "Veiculo com placa  DESATIVADO pois está relacionado a movimentações!");
        }else{
            this.veiculoRepository.delete(veiculoBanco);
            return ResponseEntity.ok("Veiculo com placa  DELETADO com sucesso!");
        }
    }
}
