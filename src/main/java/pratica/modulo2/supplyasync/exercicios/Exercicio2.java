package pratica.modulo2.supplyasync.exercicios;

import shared.SimuladorDelay;
import shared.SimuladorPreco;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Exercicio2 {

    /*
    Implemente um programa que simule a consulta de preços de três fornecedores diferentes para um produto.
    Cada consulta deve ser executada em uma tarefa separada usando supplyAsync.
    Após obter os preços, use join para recuperá-los e calcule o menor preço.
     */

    private static final Random random = new Random();

    public static void main(String[] args) {

        //Simulando busca no fornecedor e retornando preço buscando
        CompletableFuture<BigDecimal> futureFornecedorA = CompletableFuture.supplyAsync(() -> {
            SimuladorDelay.delay();
            final var preco = SimuladorPreco.simularPreco();
            System.out.println("Preço fornecedor A: " + preco);
            return preco;
        });
        //Simulando busca no fornecedor e retornando preço buscando
        CompletableFuture<BigDecimal> futureFornecedorB = CompletableFuture.supplyAsync(() -> {
            SimuladorDelay.delay();
            final var preco = SimuladorPreco.simularPreco();
            System.out.println("Preço fornecedor B: " + preco);
            return preco;
        });

        //Simulando busca no fornecedor e retornando preço buscando
        CompletableFuture<BigDecimal> futureFornecedorC = CompletableFuture.supplyAsync(() -> {
            SimuladorDelay.delay();
            final var preco = SimuladorPreco.simularPreco();
            System.out.println("Preço fornecedor C: " + preco);
            return preco;
        });

        //Bloqueando o fluxo para aguardar até que todas as buscas ao fornecedor sejam processadas
        final var fornecedorA = futureFornecedorA.join();
        final var fornecedorB = futureFornecedorB.join();
        final var fornecedorC = futureFornecedorC.join();

        //calculando menor preço retornado
        BigDecimal menorPreco = fornecedorA.min(fornecedorB).min(fornecedorC);

        System.out.println(menorPreco);


    }

}
