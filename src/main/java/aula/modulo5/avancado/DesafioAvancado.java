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

        final var transacao = criarTransacao(simularValorTransacao(), TipoTransacao.PIX);

        verificarIdentidadeComRetry(transacao)
                .thenCompose(__ -> {
                    if (Boolean.FALSE.equals(transacao.temIdendidadeValida())) {
                        System.err.printf("[Transação %s] - Transação encerrada! Status: %s\n", transacao.getId(), transacao.getStatus());
                        return CompletableFuture.completedFuture(null);
                    }
                    return consultarSaldo(transacao);
                }).thenApply(saldo -> new TransacaoComSaldo(transacao, saldo)).thenCompose(transacaoComSaldo -> {
                    if (transacaoComSaldo.temValidacaoDeSaldo()) {
                        return validarLimiteComRetry(transacaoComSaldo);
                    }
                    return CompletableFuture.completedFuture(transacaoComSaldo);
                });


        delayFinal();

    }

    private static CompletableFuture<TransacaoComSaldo> validarLimiteComRetry(TransacaoComSaldo transacaoComSaldo) {

        return retry(() -> validarLimite(transacaoComSaldo))
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        transacaoComSaldo.registrarFalhaVerificacaoLimite();
                        System.err.printf("[Transação %s] - Transação encerrada! Status: %s\n", transacaoComSaldo.transacaoId(), transacaoComSaldo.transacaoStatus());
                    }
                }).thenApply(__ -> transacaoComSaldo);
    }

    private static CompletableFuture<Void> validarLimite(TransacaoComSaldo transacaoComSaldo) {
        return CompletableFuture.runAsync(() -> {
            delay();
            final var comunicacaoFalhou = simularFalha();
            if (comunicacaoFalhou) {
                System.err.printf("[Transação %s] - Falha de comunicação com o sistema de validação de limite!\n", transacaoComSaldo.transacaoId());
                throw new RuntimeException(String.format("[Transação %s] - Falha de comunicação!\n", transacaoComSaldo.transacaoId()));
            }
            final var limiteDisponivel = simularLimite();
            validarLimiteDoUsuario(transacaoComSaldo, limiteDisponivel);
        });
    }

    private static void validarLimiteDoUsuario(TransacaoComSaldo transacaoComSaldo, BigDecimal limiteDisponivel) {
        if (Boolean.FALSE.equals(transacaoComSaldo.temLimiteParaTransacao(limiteDisponivel))) {
            System.err.printf("[Transação %s] - Usuário %s sem limite! Transação encerrada!\n", transacaoComSaldo.transacaoId(), transacaoComSaldo.usuarioId());
            transacaoComSaldo.registrarSemLimite();
            return;
        }
        System.out.printf("[Transação %s] - Usuário %s com limite! Transação continuada!\n", transacaoComSaldo.transacaoId(), transacaoComSaldo.usuarioId());
    }

    private static CompletableFuture<SaldoRecord> consultarSaldo(Transacao transacao) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.printf("[Transação %s] - buscando saldoConta do usuário %s\n", transacao.getId(), transacao.getIdUsuario());
            delay();
            final var comunicacaoFalhou = simularFalha();
            if (comunicacaoFalhou) {
                System.err.printf("[Transação %s] - Falha de comunicação com o sistema de saldoConta!\n", transacao.getId());
                System.out.printf("[Transação %s] - buscando saldoConta do usuário %s no cache\n", transacao.getId(), transacao.getIdUsuario());
                transacao.registrarPendenteSaldoAntigo();
                return simularSaldo();
            }
            System.out.printf("[Transação %s] - sucesso na busca do saldoConta do usuário %s\n", transacao.getId(), transacao.getIdUsuario());
            return simularSaldo();
        });

    }

    private static CompletableFuture<Boolean> verificarIdentidadeComRetry(Transacao transacao) {
        return retry(() -> verificarIdentidadeUsuario(transacao))
                .handle((result, ex) -> {
                    if(ex != null) {
                        transacao.registrarFalhaVerificacaoIdentidade();
                        return false;
                    }
                    return true;
                });
    }

    private static CompletableFuture<Void> verificarIdentidadeUsuario(Transacao transacao) {
        return CompletableFuture.runAsync(() -> {
            delay();
            final var comunicacaoFalhou = simularFalha();
            if (comunicacaoFalhou) {
                System.err.printf("[Transação %s] - Falha de comunicação com o sistema de verificação de identidade!\n", transacao.getId());
                throw new RuntimeException(String.format("[Transação %s] - Falha de comunicação!\n", transacao.getId()));
            }
            ehUsuarioValido(transacao);
        });
    }

    private static void ehUsuarioValido(Transacao transacao) {
        System.out.printf("[Transação %s] - verificando identidade do usuário %s\n", transacao.getId(), transacao.getIdUsuario());
        final var ehUsuarioValido = simularIdentidadeUsuario();
        if (Boolean.FALSE.equals(ehUsuarioValido)) {
            System.err.printf("[Transação %s] - Usuário %s com identidade inválida. Transação encerrada!\n", transacao.getId(), transacao.getIdUsuario());
            transacao.registrarFalhaVerificacaoIdentidade();
        }
        System.out.printf("[Transação %s] - Usuário %s com identidade válida. Transação iniciada!\n", transacao.getId(), transacao.getIdUsuario());
    }

}
