package pratica.modulo4.exceptionallycompose.exercicios;

import shared.SimuladorValor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.simularFalha;
import static shared.SimuladorValor.simularTransacao;

public class Exercicio1 {

    /*

        Você está chamando um serviço para obter as informações de um cliente e,
        em seguida, outro serviço para calcular um desconto. Caso ocorra uma falha em qualquer uma das chamadas,
        você precisa compor uma nova tarefa que retorna valores padrão para continuar o fluxo.

        1. O primeiro serviço obtém as informações do cliente.
        2. O segundo serviço calcula o desconto do cliente.
        3. Caso uma falha ocorra, o fluxo deve compor uma tarefa alternativa com valores padrão (informações fictícias e desconto fixo de 10%).

        Tarefa:

        1. Utilize o exceptionallyCompose para tratar falhas e compor uma nova tarefa que retorna valores padrão para o cliente e desconto.
        2. Após o tratamento de falhas, utilize thenApply para calcular o valor final do pedido, incluindo o desconto aplicado.

     */

    public static void main(String[] args) {

        CompletableFuture.supplyAsync(() -> {
                    System.out.println("Buscando informações do cliente");
                    delay();
                    if (simularFalha()) {
                        throw new RuntimeException("Falha ao buscar cliente");
                    }
                    return simularTransacao();
                }).exceptionallyCompose(ex -> {
                    System.err.println("Falha ao buscar cliente. Retornando informações fictícias do cliente");
                    return CompletableFuture.supplyAsync(() -> BigDecimal.TEN);
                }).thenCompose(transacaoCliente -> CompletableFuture.supplyAsync(() -> {
                            System.out.println("Calculando desconto para transação do cliente");
                            delay();
                            if (simularFalha()) {
                                throw new RuntimeException("Falha ao calcular desconto");
                            }
                            return new BigDecimal("0.25");
                        }).exceptionallyCompose(ex -> {
                            System.err.println("Falha ao calcular desconto do cliente. Retornando informações fictícias do desconto");
                            return CompletableFuture.supplyAsync(() -> new BigDecimal("0.1"));
                        }).thenApply(desconto -> {
                            System.out.println("Calculando desconto do cliente");
                            System.out.println("Valor da transação antes do desconto: " + transacaoCliente);
                            System.out.println("% de desconto do cliente: " + desconto);
                            return transacaoCliente.multiply(BigDecimal.ONE.subtract(desconto));
                        })
                ).thenApply(total -> total.setScale(2, RoundingMode.HALF_UP))
                .thenAccept(total -> System.out.println("Valor final do pedido: R$" + total));

        delayFinal();

    }

}
