package pratica.modulo4.exceptionally.exercicios;

import shared.SimuladorDelay;
import shared.SimuladorValor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorValor.simularTransacao;

public class Exercicio1 {

    /*
        Uma função que calcula o total de vendas de um mês pode falhar caso o serviço externo não esteja disponível.
        A tarefa precisa tratar a exceção e retornar um valor padrão em caso de erro.

        1. A tarefa simula uma falha ao tentar obter o valor de vendas (ex.: gera uma exceção).
        2. Caso ocorra uma exceção, a função deve retornar 0 como valor de vendas.
        3. Exiba o valor de vendas ou o valor padrão caso ocorra erro.

        Tarefa:

        1. Use exceptionally para capturar a exceção e retornar o valor padrão 0 caso ocorra falha.
        2. Exiba o valor de vendas ou o valor padrão com thenAccept.
     */

    public static void main(String[] args) {

        final var random = new Random();

        CompletableFuture.supplyAsync(() -> {
                    SimuladorDelay.delay();
                    if (random.nextInt(10) < 2) {
                        throw new RuntimeException("Falha na comunicação com serviço externo");
                    }
                    System.out.println("Recuperando o total de vendas...");
                    final var totalVendas = simularTransacao();
                    System.out.println("Sucesso ao recuperar o total de vendas.");
                    return totalVendas;
                })
                .exceptionally(ex -> {
                    System.out.println("Falha ao recuperar o total de vendas.");
                    return BigDecimal.ZERO;
                })
                .thenAccept(totalVendas -> System.out.println("Total de vendas final: R$" + totalVendas));


        delayFinal();


    }

}
