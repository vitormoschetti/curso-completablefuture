package pratica.modulo4.handle.desafios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.simularFalha;
import static shared.SimuladorValor.simularPreco;

public class Desafio1 {

    /*

    Em uma plataforma de e-commerce, você precisa consultar o estoque e o preço de um produto, mas os dois serviços podem falhar.
    Se algum dos serviços falhar, você precisa retornar um valor padrão e registrar o erro.
    Após todas as tarefas, calcule o valor total de um pedido (preço * quantidade), considerando as falhas nas tarefas assíncronas.

    1. O primeiro serviço consulta o preço do produto.
    2. O segundo serviço consulta a disponibilidade de estoque.
    3. Caso qualquer um dos serviços falhe, você deve tratar a exceção e retornar o valor padrão: preço 10,0 e estoque 0.
    4. Após os serviços concluírem (com ou sem falhas), calcule o valor total do pedido (preço * quantidade).
    5. Registre o erro, se ocorrer, para cada serviço.

    Tarefa:

    1. Utilize o handle para capturar a exceção de cada serviço (preço e estoque) e retornar os valores padrões caso haja falha.
    2. Use thenApply para calcular o valor total do pedido.
    3. Utilize thenAccept para exibir o valor total do pedido, com ou sem erros.

     */

    public static void main(String[] args) {

        final var precoFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Consultando preço do produto");
            delay();
            if (simularFalha()) {
                System.err.println("Falha ao consultar produto");
                throw new RuntimeException("Falha ao consultar produto");
            }
            System.out.println("Produto encontrado com sucesso!");
            return simularPreco();
        }).handle((result, ex) -> {
            if (ex != null) {
                System.err.println("Falha ao consultar produto");
                return BigDecimal.TEN;
            }
            return result;
        });

        final var estoqueFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Consultando estoque do produto");
            delay();
            if (simularFalha()) {
                System.err.println("Falha ao consultar estoque");
                throw new RuntimeException("Falha ao consultar estoque");
            }
            System.out.println("Produto encontrado no estoque com sucesso!");
            return simularPreco();
        }).handle((result, ex) -> {
            if (ex != null) {
                System.err.println("Falha ao consultar estoque");
                return BigDecimal.ZERO;
            }
            return result;
        });

        //Combinando os futures e multiplicando seus valores
        precoFuture.thenCombine(estoqueFuture, BigDecimal::multiply)
                .thenApply(totalPedido -> totalPedido.setScale(2, RoundingMode.HALF_UP))
                .thenAccept(totalPedido -> System.out.println("Valor total do pedido R$" + totalPedido));

        delayFinal();


    }

}
