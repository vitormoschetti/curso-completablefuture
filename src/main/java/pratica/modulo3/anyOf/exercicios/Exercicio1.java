package pratica.modulo3.anyOf.exercicios;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;

public class Exercicio1 {

    /*

    Uma loja online recebe pedidos de três fornecedores e precisa saber qual fornecedor processou o pedido primeiro para atualizar o status.

    1. O primeiro fornecedor envia a confirmação de pedido (ex.: true para processado, false para não processado).
    2. O segundo fornecedor envia a confirmação de pedido (ex.: true ou false).
    3. O terceiro fornecedor envia a confirmação de pedido (ex.: true ou false).
    4. A plataforma precisa exibir o status assim que o primeiro fornecedor concluir seu processamento.

    Tarefa:
    1. Use anyOf para aguardar o primeiro fornecedor a concluir a tarefa.
    2. Use thenAccept para exibir o resultado da confirmação do primeiro fornecedor processado.

    */

    public static void main(String[] args) {

        final var random = new Random();

        final var fornecedor1Future = CompletableFuture.supplyAsync(() -> {
            delay();
            final var resultado = random.nextBoolean();
            System.out.println("Resultado do fornecedor 1: " + resultado);
            return "Primeiro future concluído - fornecedor 1";
        });

        final var fornecedor2Future = CompletableFuture.supplyAsync(() -> {
            delay();
            final var resultado = random.nextBoolean();
            System.out.println("Resultado do fornecedor 2: " + resultado);
            return "Primeiro future concluído - fornecedor 2";
        });

        final var fornecedor3Future = CompletableFuture.supplyAsync(() -> {
            delay();
            final var resultado = random.nextBoolean();
            System.out.println("Resultado do fornecedor 3: " + resultado);
            return "Primeiro future concluído - fornecedor 3";
        });

        // utilizando anyOf para agrupar os futures e retornar o resultado do primeiro future que concluir a execução
        CompletableFuture.anyOf(fornecedor1Future, fornecedor2Future, fornecedor3Future)
                .thenAccept(resultado -> System.out.println("Resultado final: " + resultado));

        delayFinal();


    }

}
