package pratica.modulo3.thencompose.desafios;

import shared.SimuladorDelay;
import shared.SimuladorValor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

public class Desafio1 {

    /*
        Uma plataforma de viagens precisa calcular o preço de um pacote com base em diferentes serviços:

        1. O primeiro serviço retorna o custo do transporte (ex.: R$ 800).
        2. O segundo serviço retorna o custo da hospedagem (ex.: R$ 1200).
        3. Use esses valores para calcular o custo total do pacote e exibi-lo no console.

        Tarefa:

        1. Simule chamadas para os dois serviços.
        2. Use thenCompose para somar os valores do transporte e da hospedagem, retornando o total.
        3. Exiba o custo total no console.

     */

    public static void main(String[] args) {


        final var future = CompletableFuture.supplyAsync(() -> {
                    SimuladorDelay.delay();
                    final var custoTransporte = SimuladorValor.simularPreco();
                    System.out.println("Custo de Transporte: R$" + custoTransporte);
                    return custoTransporte;
                })
                .thenCompose(custoTransporte -> {
                    SimuladorDelay.delay();
                    final var custoHospedagem = SimuladorValor.simularPreco();
                    System.out.println("Custo de Hospedagem: R$" + custoHospedagem);
                    //Retornando um CompletableFuture com a soma do custo de transporte e custo de hospedagem
                    return CompletableFuture.supplyAsync(() -> custoTransporte.add(custoHospedagem));
                })
                .thenApply(custoTotal -> custoTotal.setScale(2, RoundingMode.HALF_UP)); // thenApply para formatar

        System.out.println("Calculando custo total...");

        final var custoTotal = future.join();

        System.out.println("Custo total: R$" + custoTotal);

    }

}
