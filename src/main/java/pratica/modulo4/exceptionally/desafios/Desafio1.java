package pratica.modulo4.exceptionally.desafios;

import shared.SimuladorValor;

import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.*;

public class Desafio1 {

    /*
        Uma plataforma de e-commerce realiza múltiplas chamadas para verificar o status de estoque, preços e disponibilidade de um produto.
        Caso algum serviço falhe, a plataforma precisa registrar o erro e retornar uma mensagem informando que a operação falhou em algum serviço específico.

        1. Um serviço verifica o preço do produto.
        2. Outro serviço verifica a disponibilidade de estoque.
        3. Um terceiro serviço verifica a data de entrega.
        4. Caso algum dos serviços falhe, o erro precisa ser tratado e uma mensagem personalizada deve ser retornada.

        Tarefa:

        1. Use exceptionally para capturar a exceção de qualquer um dos serviços.
        2. Registre uma mensagem de erro para o serviço que falhou, indicando qual falha ocorreu.
        3. Use thenAccept para exibir o resultado, informando ao usuário se algum serviço falhou.
     */

    public static void main(String[] args) {

        /*
             Optei por criar um record ResultadoOperacao (ResultadoOperacao<T>(T value, StatusOperacao status))
             e um Enum StatusOperacao com SUCESSO e FALHA para controlar e organizar o fluxo de cada future.
         */

        // simulando sucesso/falha na comunicação com o catálogo de preços.
        final var precoProdutoFuture = CompletableFuture.supplyAsync(() -> {
                    System.out.println("Buscando catálogo de preços...");
                    delay();
                    if (simularFalha()) {
                        System.err.println("Falha na comunicação com o serviço de catálogo de preços");
                        throw new RuntimeException("Falha na comunicação com o serviço de catálogo de preços");
                    }
                    final var precoProduto = SimuladorValor.simularPreco();
                    System.out.println("Sucesso na comunicação com o serviço de catálogo de preços. R$" + precoProduto);
                    return precoProduto;
                })
                .thenApply(precoProduto -> {
                    final var precoFormatado = precoProduto.setScale(2, RoundingMode.HALF_UP);
                    return new ResultadoOperacao<>(precoFormatado.toString(), StatusOperacao.SUCESSO);
                })
                .exceptionally(ex -> new ResultadoOperacao<>("No momento nosso catálogo está indisponível", StatusOperacao.FALHA));

        // simulando sucesso/falha no estoque.
        final var estoqueFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Buscando disponibilidade no estoque...");
            delay();
            if (!simularDisponibilidade()) {
                System.err.println("Produto indisponível no estoque");
                throw new RuntimeException("Produto indisponível no estoque");
            }
            System.out.println("Produto disponível no estoque!");
            return new ResultadoOperacao<>("Estoque disponível", StatusOperacao.SUCESSO);
        }).exceptionally(ex -> new ResultadoOperacao<>("No momento nosso estoque está vazio", StatusOperacao.FALHA));

        // simulando sucesso/falha para entrega.
        final var entregaFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Verificando data de entrega...");
            delay();
            if (simularFalha()) {
                System.err.println("Falha na comunicação com o serviço de entrega");
                throw new RuntimeException("Falha na comunicação com o serviço de entrega");
            }
            final var dataEntrega = simularDataEntrega();
            System.out.println("Sucesso na comunicação com o serviço de entrega. Entrega prevista para: " + dataEntrega);
            return new ResultadoOperacao<>(dataEntrega.toString(), StatusOperacao.SUCESSO);
        }).exceptionally(ex -> new ResultadoOperacao<>("No momento não temos data de entrega disponível", StatusOperacao.FALHA));

        // Processando todos os futures com allOf.
        CompletableFuture.allOf(precoProdutoFuture, entregaFuture, estoqueFuture)
                .thenAccept(f -> {

                    final var preco = precoProdutoFuture.join();
                    final var entrega = entregaFuture.join();
                    final var estoque = estoqueFuture.join();

                    //Caso algum future retorne status de FALHA - operação falhou
                    if (preco.status() != StatusOperacao.SUCESSO ||
                            entrega.status() != StatusOperacao.SUCESSO ||
                            estoque.status() != StatusOperacao.SUCESSO) {
                        System.err.println("A operação falhou!");
                    } else { //Caso todos futures apresentem sucesso - Sucesso!
                        System.out.println("Sucesso!");
                    }
                    System.out.println(preco.value());
                    System.out.println(estoque.value());
                    System.out.println(entrega.value());

                });

        delayFinal();


    }


}
