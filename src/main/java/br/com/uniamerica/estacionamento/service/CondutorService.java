package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.repositoriy.CondutorRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CondutorService {

    @Autowired
    private CondutorRepository condutorRepository;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Transactional
    public Condutor cadastrar(final Condutor condutor) {

        Assert.notNull(condutor.getNome(), "Nome do condutor não informado!");
        Assert.notNull(condutor.getCpf(), "CPF do condutor não informado!");
        Assert.hasText(condutor.getCpf(), "Campo de CPF vazio");
        Assert.hasText(condutor.getNome(), "Campo do nome vazio");
        final String formatoCpf = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        Assert.isTrue(condutor.getCpf().matches(formatoCpf), "CPF No formato incorreto!");

        final List<Condutor> condutorByCpf = this.condutorRepository.findByCpf(condutor.getCpf());
        Assert.isTrue(condutorByCpf.isEmpty(), String.format("Condutor com CPF [ %s ] já existe!", condutor.getCpf()));


        return this.condutorRepository.save(condutor);

    }


    @Transactional
    public Condutor editar(Long id, Condutor condutor) {

        //Verifica se o condutor existe
        final Condutor condutorBanco = this.condutorRepository.findById(id).orElse(null);
        Assert.notNull(condutorBanco, "Condutor inexistente!");
        //Verifica se os dois coincidem
        Assert.isTrue(condutorBanco.getId().equals(condutor.getId()), "Condutor informado não é o mesmo que o condutor a ser atualizado");
        //Verifica se não são nulos
        Assert.notNull(condutor.getNome(), "Nome do condutor não informado!");
        Assert.notNull(condutor.getCpf(), "CPF do condutor não informado!");
        Assert.hasText(condutor.getCpf(), "Campo de CPF vazio");
        Assert.hasText(condutor.getNome(), "Campo do nome vazio");


        //Não ter dois CPFs identicos
        final List<Condutor> condutorByCpf = this.condutorRepository.findByCpf(condutor.getCpf());
        Assert.isTrue(condutorByCpf.isEmpty(), "Condutor com CPF já existente!");

        return this.condutorRepository.save(condutorBanco);

    }


    @Transactional
    public ResponseEntity<String> deletar(Long id) {


        //Verifica se o condutor informado existe
        final Condutor condutorBanco = this.condutorRepository.findById(id).orElse(null);
        Assert.notNull(condutorBanco, "Condutor não encontrado!");


        //Verifica se o condutor está em movimentação para deletalo ou desativalo
        if (!this.movimentacaoRepository.findByVeiculoId(id).isEmpty()) {
            condutorBanco.setAtivo(false);
            this.condutorRepository.save(condutorBanco);
            return ResponseEntity.ok("Condutor DESATIVADO pois está relacionado a movimentações!");
        } else {
            this.condutorRepository.delete(condutorBanco);
            return ResponseEntity.ok("Condutor DELETADO pois NÂO está relacionado a movimentações!");
        }
    }
}