package pratica.modulo3.thenapply.desafios;

import shared.SimuladorValor;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class Desafio1 {

    /*
        Uma fintech precisa processar transações financeiras em um pipeline assíncrono que envolve múltiplas etapas:
            1. Uma API retorna o valor bruto da transação (ex.: R$ 10.000,00).
            2. Um sistema aplica uma taxa administrativa de 2,5% ao valor bruto.
            3. Após calcular a taxa, o sistema converte o valor líquido para dólares usando a cotação atual (ex.: R$ 1 = $0.20).
            4. O resultado deve exibir o valor líquido em dólares no formato: "Transação concluída: $xxxx.xx".

       Tarefa:
            1. Use supplyAsync para obter o valor bruto da transação.
            2. Use uma sequência de thenApply para calcular a taxa, aplicar a conversão para dólares e formatar a mensagem final.
            3. Exiba a mensagem final no console.

        Dica: divida cada etapa em funções separadas para simular o pipeline.
     */

    public static void main(String[] args) {

        final var valorFormatado = CompletableFuture.supplyAsync(() -> {
                    BigDecimal transacao = SimuladorValor.simularTransacao();
                    System.out.println("Valor bruto da transação: R$" + transacao);
                    return transacao; //valor da transação
                })
                .thenApply(transacao -> transacao.multiply(BigDecimal.ONE.subtract(new BigDecimal("0.025"))))//thenApply - aplicando taxa administrativa
                .thenApply(valor -> valor.multiply(SimuladorValor.simularDolar())) //thenApply - transformando em dólar
                .thenApply(valor -> String.format("$%.2f", valor)) //thenApply - formatando valor
                .join(); //bloqueando

        System.out.println("Valor líquido em dolar: " + valorFormatado);


    }

}
