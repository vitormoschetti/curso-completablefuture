package aula.modulo5.basico;

import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.simularFalha;

public class DesafioBasico {

    public static void main(String[] args) {

        CompletableFuture.runAsync(() -> {
            System.out.println("Recebendo pedido do cliente...");
            delay();
            if (simularFalha()) {
                throw new RuntimeException("Falha ao confirmar pedido do cliente");
            }
            System.out.println("Pedido do cliente confirmado!");
            enviarNotificacao(TipoNotificacao.CONFIRMADO);
        }).exceptionally(ex -> {
            System.err.println("Falha ao confirmar pedido do cliente");
            enviarNotificacao(TipoNotificacao.CANCELADO);
            return null;
        });

        delayFinal();

    }

    private static void enviarNotificacao(TipoNotificacao tipoNotificacao) {
        if (tipoNotificacao == TipoNotificacao.CONFIRMADO) {
            System.out.println("Notificação: Seu pedido foi confirmado!");
        } else if (tipoNotificacao == TipoNotificacao.EM_PREPARACAO) {
            System.out.println("Notificação: Seu pedido está em preparação!");
        } else if (tipoNotificacao == TipoNotificacao.ROTA_ENTREGA) {
            System.out.println("Notificação: Seu pedido saiu para entrega!");
        } else if (tipoNotificacao == TipoNotificacao.ENTREGUE) {
            System.out.println("Notificação: Pedido entregue! Boa refeição!");
        } else if (tipoNotificacao == TipoNotificacao.CANCELADO) {
            System.out.println("Notificação: Pedido cancelado!");
        }
    }

}
