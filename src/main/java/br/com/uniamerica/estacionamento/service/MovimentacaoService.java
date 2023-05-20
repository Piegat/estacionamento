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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        movimentacao.setValorHoraMulta(configuracao.getValorMinutoMulta().multiply(new BigDecimal(60))); //Pega o valor Hora da multa





        return this.movimentacaoRepository.save(movimentacao);
    }


    @Transactional
    public Movimentacao editar(Long id, Movimentacao movimentacao) {

        final Configuracao configuracao = this.configuracaoRepository.findByConfiguracao();

        final Movimentacao movimentacaobyid = this.movimentacaoRepository.findById(id).orElse(null);




        // Verifica se a movimentação existe
        final Movimentacao movimentacaoBanco = this.movimentacaoRepository.findById(id).orElse(null);
        Assert.notNull(movimentacaoBanco, "Movimentação não existe!");




//          Verifica as movimentações coincidem
        Assert.isTrue(movimentacaoBanco.getId().equals(movimentacao.getId()), "Movimentação informado não é o mesmo que a Movimentação a ser atualizado");


//          Verifica os campos que são notNull
        Assert.notNull(movimentacao.getCondutor(), "Condutor não informado!!");
        Assert.notNull(movimentacao.getVeiculo(), "Veiculo não informado!");
        Assert.notNull(movimentacao.getEntrada(), "Data de Entrada não informada!");


//          Verifica se o condutor e o veiculo exitem
        final Condutor condutor = this.condutorRepository.findById(movimentacao.getCondutor().getId()).orElse(null);
        Assert.notNull(condutor, "Condutor não existe!");
        final Veiculo veiculo = this.veiculoRepository.findById(movimentacao.getVeiculo().getId()).orElse(null);
        Assert.notNull(veiculo, "Veiculo não existe!");


        movimentacao.setValorHora(configuracao.getValorHora());


        if (movimentacao.getSaida() != null){

            // ========================== ADICIONA TEMPO A ENTRADA E SAIDA ================================

        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();

        Duration duration = Duration.between(entrada, saida); // Armazena em duration a duração de tempo entre a entrada e da saida
        long duracao;
        duracao = duration.toSeconds();



            movimentacao.setHorastempo((int) (duracao / 3600));

            movimentacao.setMinutostempo((int)(duracao%3600)/60);

            movimentacao.setValorHoraMulta(configuracao.getValorMinutoMulta().multiply(new BigDecimal(60))); //Pega o valor Hora da multa


            condutor.setHoras((int) (duracao / 3600));

            condutor.setMinutos((int)(duracao/60));


        // ==================================== CALCULANDO MINUTOS E TRANSFORANDO EM HORAS ===========================================
        if (condutor.getMinutos() > 60){        //Calcula se os minutos no condutor é maior que 60 e se for divide por 60 e armazena o resto da divisão em minutos e soma as horas
            final int horasExtra =  condutor.getMinutos() /60;

            condutor.setMinutos(condutor.getMinutos() %60);
            condutor.setHoras(((int) (duracao/3600) + condutor.getHoras()) + horasExtra); // Setando no cadastro do condutor as horas que ele passou estacionado

        }

        // =====================================================================================================

            final BigDecimal valorTotal;

            valorTotal = new BigDecimal(((int) duracao/3600));

            movimentacao.setValorTotal(new BigDecimal((int) duracao/3600).multiply(configuracao.getValorHora()).add(new BigDecimal(movimentacao.getMinutostempo()/60).multiply(movimentacao.getValorHora())));
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

        Duration duration = Duration.between(entrada, saida); // Armazena em duration a duração de tempo entre a entrada e da saida
        long duracao;
        duracao = duration.toSeconds();



//                                                IF PARA ADICIONAR A QUANTIDADE DE HORAS/MINUTOS QUE ELE PASSOU APOS O HORARIO DE EXPEDIENTE SEM CONTABILIZAR OS DIAS

        if (LocalTime.from(entrada).isBefore(configuracao.getInicioExpediente()) || LocalTime.from(saida).isAfter(configuracao.getFimExpediente())) {
            long duracaoEntrada, duracaoSaida;

            if((LocalTime.from(entrada).isBefore(configuracao.getInicioExpediente()))) {

                Duration durationEntrada = Duration.between(entrada, saida);
                Duration durationAntesDoFim = Duration.between(saida, configuracao.getInicioExpediente());// Guarda o tempo permanecido antes de chegar ao fim do expediente.


                duracaoEntrada = durationEntrada.toMinutes() - durationAntesDoFim.toMinutes();


                movimentacao.setMinutosMulta((int)(movimentacao.getMinutosMulta()+(duracaoEntrada %60))); // Calculando e adicionando os minutos divindo o total de segundos 60 para transformar em minutos
                movimentacao.setHorasMulta((int) (movimentacao.getHorasMulta()+(duracaoEntrada/60))); //  Calculando e adicionando os horas divindo o total de segundos por 3600




            }
            if((LocalTime.from(saida).isAfter(configuracao.getFimExpediente()))) {

                Duration durationSaida = Duration.between(entrada, saida);

                Duration durationAntesDoFim = Duration.between(entrada, configuracao.getFimExpediente());// Guarda o tempo permanecido antes de chegar ao fim do expediente.

                duracaoSaida = durationSaida.toMinutes() - durationAntesDoFim.toMinutes();


                movimentacao.setMinutosMulta((int)(movimentacao.getMinutosMulta()+(duracaoSaida %60))); // Calculando e adicionando os minutos divindo o total de segundos 60 para transformar em minutos
                movimentacao.setHorasMulta((int) (movimentacao.getHorasMulta()+(duracaoSaida/60))); //  Calculando e adicionando os horas divindo o total de segundos por 3600

            }

        }


        if(duracao > 86400){

            duracao = (int) duracao/86400;


        }


    }

}

