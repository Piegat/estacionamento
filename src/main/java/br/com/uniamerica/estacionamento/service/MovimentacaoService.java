package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repositoriy.CondutorRepository;
import br.com.uniamerica.estacionamento.repositoriy.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.repositoriy.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.repositoriy.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MovimentacaoService {



    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private CondutorRepository condutorRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;




    public Movimentacao cadastrar(final Movimentacao movimentacao) {

        final Configuracao configuracao = this.configuracaoRepository.findByConfiguracao();

        //          Verifica os campos que são notNull
        Assert.notNull(movimentacao.getCondutor(), "Condutor não informado! Informe o ID do condutor!");
        Assert.notNull(movimentacao.getVeiculo(), "Veiculo não informado! Informe o ID do veiculo!");
        Assert.notNull(movimentacao.getEntrada(), "Data de Entrada não informada!");


        // Verifica se o condutor e o veiculo exitem

        final Condutor condutor = this.condutorRepository.findById(movimentacao.getCondutor().getId()).orElse(null);
        Assert.notNull(condutor, "Condutor não existe!");
        final Veiculo veiculo = this.veiculoRepository.findById(movimentacao.getVeiculo().getId()).orElse(null);
        Assert.notNull(veiculo, "Veiculo não existe!");


        Assert.isTrue(condutor.getAtivo(), "Condutor está inativo");
        Assert.isTrue(veiculo.getAtivo(), "Veiculos está inativo");


        movimentacao.setValorHora(configuracao.getValorHora()); //Pega valor hora e coloca na movimentação
        movimentacao.setValorHoraMulta(configuracao.getValorMulta()); //Pega o valor Hora da multa



        return this.movimentacaoRepository.save(movimentacao);
    }


    @Transactional
    public Movimentacao editar(Long id, Movimentacao movimentacao) {

        final Configuracao configuracao = this.configuracaoRepository.findByConfiguracao();





        // Verifica se a movimentação existe
        final Movimentacao movimentacaoBanco = this.movimentacaoRepository.findById(id).orElse(null);
        Assert.notNull(movimentacaoBanco, "Movimentação não existe!");

//          Verifica as movimentações coincidem
        Assert.isTrue(movimentacaoBanco.getId().equals(movimentacao.getId()), "Movimentação informado não é o mesmo que a Movimentação a ser atualizado");

//          Verifica os campos que são notNull
        Assert.notNull(movimentacao.getCadastro(), "Não informado a data de cadastro");
        Assert.notNull(movimentacao.getId(), "Não informado o ID");
        Assert.notNull(movimentacao.getAtivo(), "Não informado ATIVO = True / Else");
        Assert.notNull(movimentacao.getCondutor(), "Condutor não informado!!");
        Assert.notNull(movimentacao.getVeiculo(), "Veiculo não informado!");
        Assert.notNull(movimentacao.getEntrada(), "Data de Entrada não informada!");


//          Verifica se o condutor e o veiculo exitem
        final Condutor condutor = this.condutorRepository.findById(movimentacao.getCondutor().getId()).orElse(null);
        Assert.notNull(condutor, "Condutor não existe!");
        final Veiculo veiculo = this.veiculoRepository.findById(movimentacao.getVeiculo().getId()).orElse(null);
        Assert.notNull(veiculo, "Veiculo não existe!");


//        Setando Valor hora
        movimentacao.setValorHora(configuracao.getValorHora());


        if (movimentacao.getSaida() != null){ //Se a saida não for nula

            // ========================== ADICIONA TEMPO A ENTRADA E SAIDA ================================

        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();

        Duration duration = Duration.between(entrada, saida); // Armazena em duration a duração de tempo entre a entrada e da saida
        long duracao;
        duracao = duration.toMinutes();


        //=============================== Setando o tempo no condutor. ===============================

            condutor.setHoras(condutor.getHoras() + (int) (duracao / 60));

            condutor.setMinutos(condutor.getMinutos() + (int)(duracao%60));

            // ==================================== CALCULANDO MINUTOS DO CONDUTOR E TRANSFORANDO EM HORAS ===========================================

        if (condutor.getMinutos() > 60){        //Calcula se os minutos no condutor é maior que 60 e se for divide por 60 e armazena o resto da divisão em minutos e soma as horas
        final int horasExtra =  condutor.getMinutos() /60;

        condutor.setMinutos(condutor.getMinutos() %60);
        condutor.setHoras(((int) (duracao/60) + condutor.getHoras()) + horasExtra); // Setando no cadastro do condutor as horas que ele passou estacionado
        }




            // ============================================== CALCULANDO VALOR =======================================================

            final BigDecimal valorTotal;

            valorTotal = new BigDecimal(((int) duracao/60));

            // Setando na movimentação as Horas, Minutos e valorHoraMinuto
            movimentacao.setMinutostempo((int)duracao%60);
            movimentacao.setHorastempo((int)duracao/60);

            movimentacao.setValorTotal(

                    new BigDecimal(movimentacao.getHorastempo()).multiply(movimentacao.getValorHora())
                    .add(new BigDecimal(movimentacao.getMinutostempo()).multiply(movimentacao.getValorHora().divide(new BigDecimal(60), RoundingMode.HALF_UP)))
            );
            // Calcula o valor total

            calcMulta(movimentacao);


        }

        return this.movimentacaoRepository.save(movimentacao);

    }


    @Transactional
    public ResponseEntity<?> desativar(Long id) {

//          Verifica se a Movimentação informada existe

        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        Assert.notNull(movimentacao, "Movimentação não encontrada!");

        movimentacao.setAtivo(false);
        return ResponseEntity.ok("Movimentação DESATIVADA");
    }

    @Transactional
    public void calcMulta(Movimentacao movimentacao){

        final Configuracao configuracao = this.configuracaoRepository.findByConfiguracao();


        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();

        int ano = saida.getYear() - entrada.getYear();
        int dias = saida.getDayOfYear() - entrada.getDayOfYear();

        long tempoTotal = Duration.between(entrada, saida).toMinutes();

        int TempoTotalMinutos = 0;



        if (LocalTime.from(entrada).isBefore(configuracao.getInicioExpediente()) || LocalTime.from(saida).isAfter(configuracao.getFimExpediente())) {

            if(LocalTime.from(entrada).isBefore(configuracao.getInicioExpediente())) {

                TempoTotalMinutos += Duration.between(LocalTime.from(entrada), configuracao.getInicioExpediente()).toMinutes();
            }


            if(LocalTime.from(saida).isAfter(configuracao.getFimExpediente())) {

                TempoTotalMinutos += Duration.between(LocalTime.from(saida), configuracao.getFimExpediente()).toMinutes();

            }
        }

        if ( ano > 0 ) {
            dias += 365*ano;
        }
        if (dias > 0){
            int foraExpediente = (int) Duration.between(configuracao.getInicioExpediente(), configuracao.getFimExpediente()).toMinutes();
            int duracaoTotal = (int) tempoTotal;

            System.out.println(TempoTotalMinutos);

            TempoTotalMinutos += duracaoTotal - (dias * foraExpediente);
            System.out.println(TempoTotalMinutos);



            if (LocalTime.from(saida).isAfter(configuracao.getInicioExpediente()) && LocalTime.from(saida).isBefore(configuracao.getFimExpediente()) ) {
                int TempoSaida = (int) Duration.between(LocalTime.from(saida), configuracao.getInicioExpediente()).toMinutes();
                TempoSaida *= -1;
                 TempoTotalMinutos -= TempoSaida;

            }


        }


        movimentacao.setHorasMulta(TempoTotalMinutos/60);
        movimentacao.setMinutosMulta(TempoTotalMinutos%60);


        final BigDecimal valorMulta = new BigDecimal(TempoTotalMinutos/60).multiply(configuracao.getValorMulta());


        movimentacao.setValorMulta(valorMulta);
    }

    private void calcularTotal(Movimentacao movimentacao){





    }


//    private void gerarDesconto(Movimentacao movimentacao, Condutor condutor){
//        final Configuracao configuracao = this.configuracaoRepository.findByConfiguracao();
//
//
//        if (configuracao.getGerarDesconto() == true){
//
//
//        }
//
//    }
//

    }





