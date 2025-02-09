package pratica.modulo3.thencombineaccept.exercicios;

import shared.SimuladorDelay;
import shared.SimuladorValor;

import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorValor.simularPreco;

public class Exercicio1 {

    /*
        Uma loja online precisa calcular o preço total de um pedido, que inclui o preço do produto e o valor do frete.

            1. Um serviço retorna o preço do produto (ex.: R$ 150,00).
            2. Outro serviço retorna o valor do frete (ex.: R$ 20,00).
            3. Combine os dois valores para calcular o preço total e exibi-lo no console no formato: "Preço total: R$ 170,00".

        Tarefa:
            1. Use thenCombine para combinar os dois resultados.
            2. Use thenApply para formatar o preço total.
            3. Use thenAccept no final para exibir o preço total no console.
     */

    public static void main(String[] args) {

        //Simulando request para buscar preço do produto
        final var precoProdutoFuture = CompletableFuture.supplyAsync(() -> {
            delay();
            final var precoProduto = simularPreco();
            System.out.println("Preço do produto: " + precoProduto);
            return precoProduto;
        });

        //Simulando request para calcular valor do frete
        final var valorFreteFuture = CompletableFuture.supplyAsync(() -> {
            delay();
            final var valorFrete = simularPreco();
            System.out.println("Valor do frete: " + valorFrete);
            return valorFrete;
        });

        // As duas requests foram feitas em paralelo, logo, thenCombine para combinar os valores
        precoProdutoFuture.thenCombine(valorFreteFuture, (precoProduto, valorFrete) -> {
                    System.out.println("Somando preço do produto e valor do frete...");
                    return precoProduto.add(valorFrete); // somando preço produto + valor do frete
                })
                .thenApply(valor -> valor.setScale(2, RoundingMode.HALF_UP)) // formatando valor
                .thenAccept(valor -> System.out.println("Preço total da compra: R$" + valor)); // logando valor final

        delayFinal(); // delay para segurar a Thread principal até que os CompletableFutures processem

    }

}
