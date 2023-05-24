package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.*;
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
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
        Veiculo veiculo = this.veiculoRepository.findById(movimentacao.getVeiculo().getId()).orElse(null);
        Assert.notNull(veiculo, "Veiculo não existe!");


        Assert.isTrue(condutor.getAtivo(), "Condutor está inativo");
        Assert.isTrue(veiculo.getAtivo(), "Veiculos está inativo");


        movimentacao.setValorHora(configuracao.getValorHora()); //Pega valor hora e coloca na movimentação
        movimentacao.setValorMinutoMulta(configuracao.getValorMinutoMulta()); //Pega o valor Hora da multa




 //=======================================================================================================================================================
//        TipoVeiculos tipoveiculo = this.veiculoRepository.findByTipo(movimentacao.getVeiculo().getTipoVeiculos());
//
//        final List<Movimentacao> listaVagasUsadas = this.movimentacaoRepository.findByVeiculoCarro(tipoveiculo);
//        System.out.println(listaVagasUsadas);
//
//
//        int QtdeVagasUsadas = listaVagasUsadas.size();
//
//
//
//        switch (tipoveiculo) {
//            case CARRO ->
//                    Assert.isTrue(QtdeVagasUsadas == configuracao.getVagasCarro(), "Vagas insuficientes para carro");
//            case MOTO ->
//                    Assert.isTrue(QtdeVagasUsadas == configuracao.getVagasMoto(), "Vagas insuficientes para moto");
//            case VAN ->
//                    Assert.isTrue(QtdeVagasUsadas == configuracao.getVagasVan(), "Vagas insuficientes para van");
//
//        }

        return this.movimentacaoRepository.save(movimentacao);
    }


    @Transactional
    public ResponseEntity<String> editar(Long id, Movimentacao movimentacao) {

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


        String resposta = "";

        if (movimentacao.getSaida() != null) { //Se a saida não for nula

            setCondutor(movimentacao); //Setando dados no cadastro do condutor como Tempo Desconto, horas, minutos, etc...
            calcMulta(movimentacao);
            calcularTotal(movimentacao); //Calculando valores!


        }

        if (movimentacao.getSaida() != null) {

            resposta = String.format("\tESTACIONAMENTO DO PEDRO\t\n" +
                    "\n=======================\n" +
                    "Condutor: " + movimentacao.getCondutor().getNome() + "\n" +
                    "Veiculo: " + movimentacao.getVeiculo().getModelo().getNome() + " | " + movimentacao.getVeiculo().getPlaca() + " | " + movimentacao.getVeiculo().getCor() + "\n" +
                    "Tempo Estacionado: " + movimentacao.getHorastempo() + " Horas e " + movimentacao.getMinutostempo() + " Minutos \n" +
                    "\n=======================\n" +
                    "Tempo Multa: " + movimentacao.getHorasMulta() + " Horas e " + movimentacao.getMinutosMulta() + " Minutos \n" +
                    "Valor Multa: " + movimentacao.getValorMulta() + "\n" +
                    "\n=======================\n" +
                    "Tempo Desconto: " + movimentacao.getHorasDesconto() + " Horas e " + movimentacao.getMinutosDesconto() + " Minutos\n" +
                    "Valor Desconto: " + movimentacao.getValorDesconto() + "\n" +
                    "\n=======================\n" +
                    "\nValor Total: " + movimentacao.getValorTotal());
        } else {

            resposta = String.format("Movimentação editada com sucesso!");

        }

        this.movimentacaoRepository.save(movimentacao);

        return ResponseEntity.ok(resposta);
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
    public void calcMulta(Movimentacao movimentacao) {

        final Configuracao configuracao = this.configuracaoRepository.findByConfiguracao();


        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();

        int ano = saida.getYear() - entrada.getYear();
        int dias = saida.getDayOfYear() - entrada.getDayOfYear();

        long tempoTotal = Duration.between(entrada, saida).toMinutes();

        int TempoTotalMinutos = 0;


        if (LocalTime.from(entrada).isBefore(configuracao.getInicioExpediente()) || LocalTime.from(saida).isAfter(configuracao.getFimExpediente())) {

            if (LocalTime.from(entrada).isBefore(configuracao.getInicioExpediente())) {

                TempoTotalMinutos += Duration.between(LocalTime.from(entrada), configuracao.getInicioExpediente()).toMinutes();
            }


            if (LocalTime.from(saida).isAfter(configuracao.getFimExpediente())) {

                TempoTotalMinutos += Duration.between(LocalTime.from(saida), configuracao.getFimExpediente()).toMinutes();

            }
        }

        if (ano > 0) {
            dias += 365 * ano;
        }
        if (dias > 0) {
            int foraExpediente = (int) Duration.between(configuracao.getInicioExpediente(), configuracao.getFimExpediente()).toMinutes();
            int duracaoTotal = (int) tempoTotal;


            TempoTotalMinutos += duracaoTotal - (dias * foraExpediente);


            if (LocalTime.from(saida).isAfter(configuracao.getInicioExpediente()) && LocalTime.from(saida).isBefore(configuracao.getFimExpediente())) {
                int TempoSaida = (int) Duration.between(LocalTime.from(saida), configuracao.getInicioExpediente()).toMinutes();
                TempoSaida *= -1;
                TempoTotalMinutos -= TempoSaida;

            }


        }


        movimentacao.setHorasMulta(TempoTotalMinutos / 60);
        movimentacao.setMinutosMulta(TempoTotalMinutos % 60);


        BigDecimal minutoMulta = configuracao.getValorMinutoMulta();

        final BigDecimal valorMulta = minutoMulta // Calculando o valor total da multa
                .multiply(new BigDecimal(TempoTotalMinutos))
                .setScale(2, RoundingMode.UP);


        System.out.println(valorMulta + " VALOR MULTA");

        movimentacao.setValorMulta(valorMulta);


    }

    private void calcularTotal(Movimentacao movimentacao) {

        calculaDesconto(movimentacao);


        final Configuracao configuracao = this.configuracaoRepository.findByConfiguracao();

        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();

        Duration duration = Duration.between(entrada, saida); // Armazena em duration a duração de tempo entre a entrada e da saida
        long duracao;
        duracao = duration.toMinutes();


        BigDecimal desconto = movimentacao.getValorDesconto();

        BigDecimal valorMulta = movimentacao.getValorMulta();

        // Setando na movimentação as Horas, Minutos e valorHoraMinuto
        movimentacao.setMinutostempo((int) duracao % 60);
        movimentacao.setHorastempo((int) duracao / 60);

        int horasTempo = movimentacao.getHorastempo();
        int minutosTempo = movimentacao.getMinutostempo();
        BigDecimal valorHora = configuracao.getValorHora();


        // Calcula o valor total



        movimentacao.setValorTotal(                 //Calculando o valor total multiplicando as horas da movimentação com o valor hora e somando os minutos multiplicando pelo valor hora e dividindo por 60
                new BigDecimal(horasTempo).multiply(valorHora)
                        .add(new BigDecimal(minutosTempo).multiply(valorHora
                             .divide(new BigDecimal(60), RoundingMode.HALF_UP))
                                .add(valorMulta))
                                    .subtract(desconto)
        );//Somando o valor da multa


    }

    private void setCondutor(Movimentacao movimentacao) {

        Condutor condutor = this.condutorRepository.findById(movimentacao.getCondutor().getId()).orElse(null);

        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();

        Duration duration = Duration.between(entrada, saida); // Armazena em duration a duração de tempo entre a entrada e da saida
        long duracao;
        duracao = duration.toMinutes();


        //=============================== Setando o tempo no condutor. ===============================

        condutor.setHorasPagas(condutor.getHorasPagas() + (int) (duracao / 60));

        condutor.setMinutosPagos(condutor.getMinutosPagos() + (int) (duracao % 60));

        // ==================================== CALCULANDO MINUTOS DO CONDUTOR E TRANSFORANDO EM HORAS ===========================================

        if (condutor.getMinutosPagos() > 60) {        //Calcula se os minutos no condutor é maior que 60 e se for divide por 60 e armazena o resto da divisão em minutos e soma as horas
            final int horasExtra = condutor.getMinutosPagos() / 60;

            condutor.setMinutosPagos(condutor.getMinutosPagos() % 60);
            condutor.setHorasPagas(((int) (duracao / 60) + condutor.getHorasPagas()) + horasExtra); // Setando no cadastro do condutor as horas que ele passou estacionado
        }


    }

    private void calculaDesconto(Movimentacao movimentacao) {

        Condutor condutor = this.condutorRepository.findById(movimentacao.getCondutor().getId()).orElse(null);
        final Configuracao configuracao = this.configuracaoRepository.findByConfiguracao();

        int duracao = (int) Duracao(movimentacao);

        if (configuracao.getGerarDesconto()) {

            int minutosEstacionadas = duracao;
            int minutosPagosCondutor = (condutor.getHorasPagas() * 60) + condutor.getMinutosPagos();
            int totalDeMinutos = minutosEstacionadas + minutosPagosCondutor;
            int minutosParaDesconto = (configuracao.getHorasParaDesconto() * 60);
            int minutosDeDesconto = (configuracao.getHorasDeDesconto() * 60) + configuracao.getMinutosDeDesconto();
            int minutosdeDescontoCondutor = (condutor.getHorasDesconto() * 60)+condutor.getMinutosDesconto();

            int desconto = 0;


            System.out.println(minutosEstacionadas);
            System.out.println(minutosPagosCondutor);
            System.out.println(totalDeMinutos);
            System.out.println(minutosParaDesconto);
            System.out.println(minutosDeDesconto);
            System.out.println(minutosdeDescontoCondutor);

            System.out.println("\n");



//          Multiplicador atual guarda os minutos pagos e divide eles pelos minutos necessarios para gerar o desconto
            int multiplicadorAtual = minutosEstacionadas / minutosParaDesconto;

//          Multiplicador proximo guarda os minutos pagos e divide eles pelos minutos necessarios para gerar o desconto
            int multiplicadorProximo = minutosPagosCondutor / minutosParaDesconto ;


            System.out.println(multiplicadorAtual);

            System.out.println(multiplicadorProximo);


            if (multiplicadorProximo > multiplicadorAtual) {
//          Subtraindo o mult.Proximo pelo mult.Atual
                int multiplicadorTotal = multiplicadorProximo - multiplicadorAtual;
//          desconto vai guardar o resultado da subtração do mult.Total e multiplicar pelo desconto predefinido nas configurações
                 desconto = multiplicadorTotal * minutosDeDesconto;

                System.out.println(desconto);

                condutor.setHorasDesconto((minutosdeDescontoCondutor + desconto) / 60);
                condutor.setMinutosDesconto((minutosdeDescontoCondutor + desconto) % 60);


                if (condutor.getMinutosDesconto() > 60) { //Se os minutos ultrapassar 60 transforma em 1 hora.
                    final int horasExtra = condutor.getMinutosDesconto() / 60;

                    condutor.setMinutosDesconto(condutor.getMinutosPagos() % 60); //Setando os minutos com o resto da divisão por 60
                    condutor.setHorasDesconto(( (duracao / 60) + condutor.getHorasDesconto()) + horasExtra); //Setando as horas divindo duracao por 60 e somando as horas existes e a hora gerada pelos minutos /60
                }



            }
            BigDecimal valorDesconto = BigDecimal.valueOf(desconto/60).multiply(configuracao.getValorHora()).setScale(2);
            System.out.println( "\n" + valorDesconto);
            movimentacao.setValorDesconto(valorDesconto);

            movimentacao.setHorasDesconto(desconto/60);
            movimentacao.setMinutosDesconto(desconto%60);

            } else {

            movimentacao.setValorDesconto(new BigDecimal(0));

        }
    }



    private long Duracao(Movimentacao movimentacao){

        LocalDateTime entrada = movimentacao.getEntrada();
        LocalDateTime saida = movimentacao.getSaida();
        Duration duration = Duration.between(entrada, saida); // Armazena em duration a duração de tempo entre a entrada e da saida
        long duracao;
        duracao = duration.toMinutes();

        return duracao;

    }


}




