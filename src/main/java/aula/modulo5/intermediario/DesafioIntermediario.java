package aula.modulo5.intermediario;

import aula.modulo5.intermediario.model.Pedido;
import aula.modulo5.intermediario.model.PrevisaoTempo;
import aula.modulo5.intermediario.model.Rastreamento;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static aula.modulo5.intermediario.SimuladorDesafioIntermediario.*;
import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;

public class DesafioIntermediario {

    public static void main(String[] args) {

        final var pedido = pedir("Rua completableFuture, 01", BigDecimal.TEN);

        final var rastreamentoFuture = obterRastreamento(pedido);

        final var previsaoTempoFuture = obterPrevisaoTempo(pedido);

        rastreamentoFuture
                .thenCombine(previsaoTempoFuture, (rastreamento, previsaoTempo)
                        -> decidirEntrega(rastreamento, previsaoTempo, pedido))
                .exceptionallyCompose(ex -> {
                    System.err.println("Erro ao retornar rastreamento do pedido " + pedido.getId() + ". Nova tentativa...");
                    return obterRastreamento(pedido)
                            .thenCombine(previsaoTempoFuture, (rastreamento, previsaoTempo)
                                    -> decidirEntrega(rastreamento, previsaoTempo, pedido));
                })
                .thenCompose(ignored -> notificarCliente(pedido))
                .thenCompose(ignored -> processarEntrega(pedido))
                .thenCompose(ignored -> pedido.foiEntregue() ? notificarCliente(pedido) : CompletableFuture.completedFuture(null))
                .whenComplete((result, ex) -> {
                    if(ex != null) {
                        System.err.println("Pedido " + pedido.getId() + " cancelado.");
                        pedido.cancelar();
                    }
                    if (pedido.deveSerReagendado()) {
                        System.out.println("Reagendando pedido " + pedido.getId());
                        pedido.reagendarEntrega();
                        System.out.println("Pedido " + pedido.getId() + " reagendado! Nova entrega em: " + pedido.getPrevisaoEntrega());
                    }
                    System.out.println("Salvando pedido " + pedido.getId());
                    salvar(pedido);
                });

        delayFinal();

    }

    private static CompletableFuture<Void> processarEntrega(Pedido pedido) {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Processando pedido " + pedido.getId());
            if (pedido.estaEmTransporte()) {
                pedido.entregue();
                System.out.println("Pedido " + pedido.getId() + " entregue com sucesso!");
            } else {
                System.out.println("Pedido " + pedido.getId() + " será reagendado!");
            }
        });
    }

    private static CompletableFuture<Void> notificarCliente(Pedido pedido) {
        return CompletableFuture.runAsync(() -> {
            delay();
            System.out.println("Notificação: Pedido " + pedido.getId() + " com status: " + pedido.getStatus());
        });
    }

    private static boolean decidirEntrega(Rastreamento rastreamento, PrevisaoTempo previsaoTempo, Pedido pedido) {
        if (!rastreamento.isEmTransporte()) {
            System.err.println("Pedido " + pedido.getId() + " não encontrado");
            throw new RuntimeException("Não foi possível rastrear o pedido");
        }

        System.out.println("Rastreamento do pedido " + pedido.getId() + " encontrado!");

        if (previsaoTempo.riscoTempestade()) {
            System.err.println("Risco de tempestade na região de entrega do pedido " + pedido.getId());
            pedido.reagendar();
            return false;
        }

        System.out.println("Pedido " + pedido.getId() + " pode ser entregue hoje!");
        pedido.emTransporte();
        return true;
    }

    private static CompletableFuture<PrevisaoTempo> obterPrevisaoTempo(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Buscando informações sobre a previsão do tempo para o pedido " + pedido.getId());
            delay();
            return previsaoTempo(pedido);
        });
    }

    private static CompletableFuture<Rastreamento> obterRastreamento(Pedido pedido) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Buscando rastreamento do pedido " + pedido.getId());
            delay();
            return rastreamento(pedido);
        });
    }

}
