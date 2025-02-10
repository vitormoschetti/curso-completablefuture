package pratica.modulo4.whencomplete.exercicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.simularFalha;
import static shared.SimuladorValor.simularTransacao;

public class Exercicio1 {

    /*

    Você está realizando um processamento assíncrono para consultar as transações de um cliente em uma plataforma bancária.
    Após obter a lista de transações, você deve registrar o sucesso ou a falha na consulta e,
    independentemente disso, calcular o total das transações processadas.

    1. A tarefa consulta as transações do cliente e pode falhar.
    2. Após a execução da consulta, registre se a operação foi bem-sucedida ou falhou.
    3. Depois, calcule o total das transações, considerando um valor fixo para as falhas.

    Tarefa:

    1. Utilize o whenComplete para registrar se a operação foi bem-sucedida ou se houve falha.
    2. Depois de concluir a consulta, calcule o total das transações.
    3. Exiba o total de transações processadas, considerando um valor fixo em caso de falha.

     */

    public static void main(String[] args) {

        CompletableFuture.supplyAsync(() -> {
                    System.out.println("Buscando transações do cliente");
                    delay();
                    if (simularFalha()) {
                        throw new RuntimeException("Falha na comunicação");
                    }
                    return simularTransacoes();
                }).whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.err.println("Falha ao buscar transação com o cliente");
                    } else {
                        System.out.println("Busca realizada com sucesso!");
                    }
                })
                .exceptionally(ex -> List.of(BigDecimal.ZERO))
                .thenApply(transacoes -> transacoes.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                .thenApply(total -> total.setScale(2, RoundingMode.HALF_UP))
                .thenAccept(total -> System.out.println("Total transacionado: R$" + total));

        delayFinal();

    }


    public static List<BigDecimal> simularTransacoes() {
        return List.of(simularTransacao(), simularTransacao(), simularTransacao());
    }

}
