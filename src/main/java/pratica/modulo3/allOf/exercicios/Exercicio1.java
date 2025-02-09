package pratica.modulo3.allOf.exercicios;

import shared.SimuladorValor;

import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;

public class Exercicio1 {

    /*
        Uma loja online recebe três pedidos e precisa somar o valor de cada um deles para calcular o total.

            1. Um serviço retorna o valor do primeiro pedido (ex.: R$ 50,00).
            2. Outro serviço retorna o valor do segundo pedido (ex.: R$ 30,00).
            3. Um terceiro serviço retorna o valor do terceiro pedido (ex.: R$ 20,00).
            4. Combine os três valores e exiba o valor total no formato: "Valor total dos pedidos: R$ 100,00".

            Tarefa:
            1. Use allOf para esperar que os três valores sejam retornados.
            2. Use thenApply para combinar os valores e calcular o total.
            3. Exiba o resultado com thenAccept.
     */

    public static void main(String[] args) {

        final var pedido1Future = CompletableFuture.supplyAsync(() -> {
            delay();
            final var preco = SimuladorValor.simularPreco();
            System.out.println("Preço do pedido 1: R$" + preco);
            return preco;
        });

        final var pedido2Future = CompletableFuture.supplyAsync(() -> {
            delay();
            final var preco = SimuladorValor.simularPreco();
            System.out.println("Preço do pedido 2: R$" + preco);
            return preco;
        });

        final var pedido3Future = CompletableFuture.supplyAsync(() -> {
            delay();
            final var preco = SimuladorValor.simularPreco();
            System.out.println("Preço do pedido 3: R$" + preco);
            return preco;
        });

        // adicionando todos os futures
        CompletableFuture.allOf(pedido1Future, pedido2Future, pedido3Future)
                .thenApply(value -> {
                    //aguardando processamento de todos os futures
                    final var pedido1 = pedido1Future.join();
                    final var pedido2 = pedido2Future.join();
                    final var pedido3 = pedido3Future.join();
                    //somando valores dos pedidos
                    return pedido1.add(pedido2).add(pedido3);
                }).thenApply(valorTotal -> valorTotal.setScale(2, RoundingMode.HALF_UP)) //formatando valor
                .thenAccept(valorTotal -> System.out.println("Valor total: R$" + valorTotal)); //logando valor

        delayFinal();
    }

}
