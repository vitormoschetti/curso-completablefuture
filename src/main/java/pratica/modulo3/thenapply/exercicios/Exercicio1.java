package pratica.modulo3.thenapply.exercicios;

import shared.SimuladorValor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

public class Exercicio1 {

    /*
        Um e-commerce simula o cálculo do preço de um produto, retornando o valor original (ex.: 200.0). Sua tarefa é:
            1. Aplicar um desconto de 15% ao preço.
            2. Exibir o preço final com desconto.
     */

    public static void main(String[] args) {

        // supplyAsync - buscando preço
        final var future = CompletableFuture.supplyAsync(() -> {
                    final var preco = SimuladorValor.simularPreco();
                    System.out.println("Preço original: " + preco);
                    return preco;
                })
                .thenApply(preco -> preco.multiply(BigDecimal.ONE.subtract(new BigDecimal("0.15")))) // thenApply - multiplicando o preço original por 0.85 (85%) (desconto de 0.15/15%)
                .thenApply(preco -> preco.setScale(2, RoundingMode.HALF_UP)); // configurando para 2 casas decimais

        BigDecimal precoDescontado = future.join();

        System.out.println("Preço descontado: " + precoDescontado);


    }

}
