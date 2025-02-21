package aula.modulo5.avancado;

import aula.modulo5.avancado.model.Transacao;
import aula.modulo5.avancado.model.enums.TipoTransacao;
import aula.modulo5.avancado.model.record.SaldoRecord;
import aula.modulo5.avancado.model.record.TransacaoComSaldo;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static aula.modulo5.avancado.SimuladorDesafioAvancado.*;
import static aula.modulo5.avancado.util.RetryUtil.retry;
import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;

public class DesafioAvancado {

    public static void main(String[] args) {

        final var transacao = criarTransacao(BigDecimal.TEN, TipoTransacao.PIX);

        verificarIdentidadeComRetry(transacao)
                .thenCompose(__ -> {
                    if (Boolean.FALSE.equals(transacao.temIdendidadeValida())) {
                        System.err.printf("[Transação %s] - Transação encerrada! Status: %s\n", transacao.getId(), transacao.getStatus());
                        return CompletableFuture.completedFuture(null);
                    }
                    return consultarSaldo(transacao);
                })
                .thenApply(saldo -> new TransacaoComSaldo(transacao, saldo));


        delayFinal();

    }

    private static CompletableFuture<SaldoRecord> consultarSaldo(Transacao transacao) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.printf("[Transação %s] - buscando saldo do usuário %s\n", transacao.getId(), transacao.getIdUsuario());
            delay();
            final var comunicacaoFalhou = simularFalha();
            if (Boolean.TRUE.equals(comunicacaoFalhou)) {
                System.err.printf("[Transação %s] - Falha de comunicação com o sistema de saldo!\n", transacao.getId());
                System.out.printf("[Transação %s] - buscando saldo do usuário %s no cache\n", transacao.getId(), transacao.getIdUsuario());
                transacao.registrarPendenteSaldoAntigo();
                return simularSaldo();
            }
            System.out.printf("[Transação %s] - sucesso na busca do saldo do usuário %s\n", transacao.getId(), transacao.getIdUsuario());
            return simularSaldo();
        });

    }

    private static CompletableFuture<Boolean> verificarIdentidadeComRetry(Transacao transacao) {
        return retry(() -> verificarIdentidadeUsuario(transacao))
                .handle((result, ex) -> {
                    if(ex != null) {
                        transacao.rejeitarIdentidade();
                        return false;
                    }
                    return true;
                });
    }

    private static CompletableFuture<Void> verificarIdentidadeUsuario(Transacao transacao) {
        return CompletableFuture.runAsync(() -> {
            delay();
            final var comunicacaoFalhou = simularFalha();
            if (Boolean.TRUE.equals(comunicacaoFalhou)) {
                System.err.printf("[Transação %s] - Falha de comunicação com o sistema de verificação de identidade!\n", transacao.getId());
                throw new RuntimeException(String.format("[Transação %s] - Falha de comunicação!\n", transacao.getId()));
            }
            ehUsuarioValido(transacao);
        });
    }

    private static void ehUsuarioValido(Transacao transacao) {
        System.out.printf("[Transação %s] - verificando identidade do usuário %s\n", transacao.getId(), transacao.getIdUsuario());
        final var ehUsuarioValido = simularIdentidadeUsuario(transacao.getIdUsuario());
        if (Boolean.FALSE.equals(ehUsuarioValido)) {
            System.err.printf("[Transação %s] - Usuário %s com identidade inválida. Transação encerrada!\n", transacao.getId(), transacao.getIdUsuario());
            transacao.rejeitarIdentidade();
        }
        System.out.printf("[Transação %s] - Usuário %s com identidade válida. Transação iniciada!\n", transacao.getId(), transacao.getIdUsuario());
    }

}
