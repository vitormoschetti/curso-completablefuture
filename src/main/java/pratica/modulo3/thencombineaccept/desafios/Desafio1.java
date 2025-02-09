package pratica.modulo3.thencombineaccept.desafios;

import shared.SimuladorValor;

import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;

public class Desafio1 {

    /*
    Uma empresa precisa consolidar informações financeiras e gerar um relatório formatado.

    1. Um serviço retorna a receita total de vendas (ex.: R$ 500.000,00).
    2. Outro serviço retorna o total de despesas (ex.: R$ 300.000,00).
    3. Combine os dois valores para calcular o lucro líquido (receita - despesas).
    4. Após calcular o lucro, formate o valor no formato monetário (ex.: "R$ 200.000,00").
    5. Exiba a mensagem formatada no console.

    Tarefa:
    1. Use thenCombine para combinar os resultados da receita e despesas.
    2. Use thenApply para formatar o valor calculado como uma mensagem no formato monetário.
    3. Finalize com thenAccept para exibir a mensagem no console.
    */

    public static void main(String[] args) {

        final var receitaVendasFuture = CompletableFuture.supplyAsync(() -> {
            delay();
            final var receitaVendas = SimuladorValor.simularTransacao();
            System.out.println("Receita Vendas: R$" + receitaVendas);
            return receitaVendas;
        });

        final var totalDespesasFuture = CompletableFuture.supplyAsync(() -> {
            delay();
            final var totalDespesas = SimuladorValor.simularTransacao();
            System.out.println("Total de despesas: R$" + totalDespesas);
            return totalDespesas;
        });


        receitaVendasFuture.thenCombine(totalDespesasFuture, (receita, despesa) -> {
                    System.out.println("Calculando o lucro líquido...");
                    return receita.subtract(despesa);
                })
                .thenApply(lucroLiquido -> lucroLiquido.setScale(2, RoundingMode.HALF_UP))
                .thenAccept(lucroLiquido -> System.out.println("Lucro Líquido: R$" + lucroLiquido));

        delayFinal();

    }

}
