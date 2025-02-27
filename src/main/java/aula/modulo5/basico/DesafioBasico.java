package aula.modulo5.basico;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadLocalRandom;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.simularFalha;

public class DesafioBasico {

    public static void main(String[] args) {

        final var pedido = SimuladorPedido.pedir();

        validarSaldo(pedido)
                .thenCompose(ignored -> confirmar(pedido))
                .thenCompose(ignored -> notificarCliente(pedido))
                .thenCompose(ignored -> preparar(pedido))
                .thenCompose(ignored -> notificarCliente(pedido))
                .thenCompose(ignored -> entregar(pedido))
                .thenCompose(ignored -> notificarCliente(pedido))
                .exceptionallyCompose(ex -> {
                    System.err.println("Falha ao processar pedido! " + pedido.getId());
                    return notificarCliente(pedido);
                });

        delayFinal();

    }

    private static CompletionStage<Void> entregar(Pedido pedido) {
        return CompletableFuture.runAsync(() -> {
            delay();
            pedido.rotaEntrega();
            System.out.printf("Pedido %s em rota de entrega%n", pedido.getId());
        });
    }

    private static CompletableFuture<Void> preparar(Pedido pedido) {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Preparando pedido " + pedido.getId());
            delay();
            if (simularFalha()) {
                pedido.cancelar();
                throw new RuntimeException("Erro ao acessar o sistema de validação de saldo");
            }
            pedido.preparar();
            delay();
            System.out.printf("Pedido %s preparado!%n", pedido.getId());
        });
    }

    private static CompletableFuture<Void> notificarCliente(Pedido pedido) {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Notificando cliente... " + pedido.getStatus());
            System.out.println("Notificação: " + pedido.getStatus().notificacao());
        });
    }

    private static CompletableFuture<Void> confirmar(Pedido pedido) {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Recebendo um novo pedido");
            delay();
            if (simularFalha()) {
                pedido.negado();
                throw new RuntimeException("Pedido negado pela loja!");
            }
            pedido.confirmar();
            System.out.printf("Pedido %s confirmado!%n", pedido.getId());
        });
    }

    private static CompletableFuture<Void> validarSaldo(Pedido pedido) {
        return CompletableFuture.runAsync(() -> {
        System.out.println("Verificando saldo do cliente");
        if (simularFalha()) {
            pedido.cancelar();
            throw new RuntimeException("Erro ao acessar o sistema de validação de saldo");
        }
        final var saldoInsuficiente = pedido.getValor().compareTo(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10, 30))) < 0;
        if (saldoInsuficiente) {
            pedido.semSaldo();
            System.err.printf("Pedido %s sem saldo!%n", pedido.getId());
            throw new RuntimeException("Saldo insuficiente");
        }
        System.out.println("Saldo validado com sucesso!");
        });
    }


}
