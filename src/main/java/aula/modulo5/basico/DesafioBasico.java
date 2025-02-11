package aula.modulo5.basico;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.simularFalha;

public class DesafioBasico {

    public static void main(String[] args) {

        final var pedido = SimuladorPedido.pedir();

        CompletableFuture.supplyAsync(() -> {
                    System.out.println("Recebendo um novo pedido");
                    delay();
                    validarSaldo(pedido);
                    pedido.confirmar();
                    System.out.printf("Pedido %s confirmado!%n", pedido.getId());
                    return true;
                }).thenCompose(confirmado ->
                        notificarCliente(pedido))
                .exceptionally(ex -> {
                    System.err.println("Falha ao processar pedido! " + pedido.getId());
                    notificarCliente(pedido);
                    return null;
                });

        delayFinal();

    }

    private static CompletableFuture<Void> notificarCliente(Pedido pedido) {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Notificando cliente...");
            delay();
            System.out.println("Notificação: " + pedido.getStatus().notificacao());
        });
    }

    private static void validarSaldo(Pedido pedido) {
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
    }


}
