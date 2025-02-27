package aula.modulo5.avancado;

import aula.modulo5.avancado.model.Transacao;
import aula.modulo5.avancado.model.enums.StatusTransacao;
import aula.modulo5.avancado.model.record.SaldoRecord;
import aula.modulo5.avancado.model.record.TransacaoComSaldo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static aula.modulo5.avancado.SimuladorDesafioAvancado.*;
import static aula.modulo5.avancado.util.RetryUtil.retry;
import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;

public class DesafioAvancado {

    public static void main(String[] args) {

        final var transacoes = new ArrayList<Transacao>();
        for (int i = 0; i < 10; i++) {
            transacoes.add(SimuladorDesafioAvancado.criarTransacao(simularValorTransacao(), simularTipoTransacao()));
        }

        final var transacoesFuture =
                transacoes
                .stream()
                .map(DesafioAvancado::processarTransacao)
                        .toList();

        CompletableFuture.allOf(transacoesFuture.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    Map<StatusTransacao, Long> relatorio = transacoesFuture.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.groupingBy(Transacao::getStatus, Collectors.counting()));

                    // Exibe o relatório
                    System.out.println("=== RELATÓRIO DE TRANSAÇÕES ===");
                    relatorio.forEach((status, quantidade) ->
                            System.out.printf("Status: %s | Quantidade: %d\n", status, quantidade)
                    );
                }).join();


        delayFinal();

    }

    private static CompletableFuture<Transacao> processarTransacao(Transacao transacao) {
        return verificarIdentidadeComRetry(transacao)
                .thenCompose(__ -> {
                    if (Boolean.FALSE.equals(transacao.temIdendidadeValida())) {
                        System.err.printf("[Transação %s] - Transação encerrada! Status: %s\n", transacao.getId(), transacao.getStatus());
                        throw new RuntimeException(String.format("[Transação %s] - Transação encerrada! Status: %s\n", transacao.getId(), transacao.getStatus()));
                    }
                    return consultarSaldo(transacao);
                }).thenApply(saldo -> new TransacaoComSaldo(transacao, saldo))
                .thenCompose(transacaoComSaldo -> {
                    if (transacaoComSaldo.temValidacaoDeLimite()) {
                        return validarLimiteComRetry(transacaoComSaldo);
                    }
                    return CompletableFuture.completedFuture(transacaoComSaldo);
                }).thenCompose(transacaoComSaldo -> {
                    if (transacaoComSaldo.podeSerProcessada()) {
                        if (transacaoComSaldo.temValidacaoDeSaldo()) {
                            return validarSaldo(transacaoComSaldo);
                        }
                        return CompletableFuture.completedFuture(transacaoComSaldo);
                    }
                    throw new RuntimeException(String.format("[Transação %s] - Transação encerrada! Status: %s\n", transacao.getId(), transacao.getStatus()));
                }).thenApply(DesafioAvancado::processamento)
                .exceptionally(ex -> {
                    System.err.printf("[Transação %s] - Processamento encerrado com falha!\n", transacao.getId());
                    return transacao;
                });
    }

    private static Transacao processamento(TransacaoComSaldo transacaoComSaldo) {
        if (transacaoComSaldo.podeSerProcessada()) {
            System.out.printf("[Transação %s] - Enviando a transação %s para liquidação!\n", transacaoComSaldo.transacaoId(), transacaoComSaldo.tipo());
            simularLiquidacao(transacaoComSaldo.transacao());
            transacaoComSaldo.registrarProcessamento();
        }
        return transacaoComSaldo.transacao();
    }

    private static CompletableFuture<TransacaoComSaldo> validarSaldo(TransacaoComSaldo transacaoComSaldo) {
        return CompletableFuture.supplyAsync(() -> {
            if (Boolean.FALSE.equals(transacaoComSaldo.temSaldoParaTransacao())) {
                System.err.printf("[Transação %s] - Usuário %s sem saldo! Transação encerrada!\n", transacaoComSaldo.transacaoId(), transacaoComSaldo.usuarioId());
                transacaoComSaldo.registrarSemSaldo();
                return transacaoComSaldo;
            }
            System.out.printf("[Transação %s] - Usuário %s com saldo! Transação continuada!\n", transacaoComSaldo.transacaoId(), transacaoComSaldo.usuarioId());
            return transacaoComSaldo;
        });
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
            System.out.printf("[Transação %s] - buscando saldo do usuário %s\n", transacao.getId(), transacao.getIdUsuario());
            delay();
            final var comunicacaoFalhou = simularFalha();
            if (comunicacaoFalhou) {
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
