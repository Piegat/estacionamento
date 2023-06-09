package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.controller.ConfiguracaoController;
import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repositoriy.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class ConfiguracaoService {


    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Transactional
    public Configuracao cadastrar(final Configuracao configuracao){
        Assert.notNull(configuracao.getValorHora(), "Valor hora não informada!");
        Assert.notNull(configuracao.getInicioExpediente(), "Hora de abertura não informada!");
        Assert.notNull(configuracao.getFimExpediente(), "Hora de fechamento não informada!");
        return this.configuracaoRepository.save(configuracao);
    }

    @Transactional
    public Configuracao editar(Long id, Configuracao configuracao){

        Assert.notNull(configuracao.getCadastro(), "Não informado a data de cadastro");
        Assert.notNull(configuracao.getId(), "Não informado o ID");
        Assert.notNull(configuracao.getAtivo(), "Não informado ATIVO = True / Else");


        // Verifica se a configuracao existe

        final Configuracao configuracaoBanco = this.configuracaoRepository.findById(id).orElse(null);
        Assert.notNull(configuracaoBanco, "Configuração não existe!");

         //* Verifica as configurações coincidem

        Assert.isTrue(configuracao.getId().equals(configuracaoBanco.getId()), "Configuração informada não é o mesma que a configuração a ser atualizada");


        Assert.notNull(configuracao.getValorHora(), "Valor hora não informada!");
        Assert.notNull(configuracao.getInicioExpediente(), "Hora de abertura não informada!");
        Assert.notNull(configuracao.getFimExpediente(), "Hora de fechamento não informada!");

        return this.configuracaoRepository.save(configuracao);
    }



}
