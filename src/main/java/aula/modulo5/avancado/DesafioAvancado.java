package aula.modulo5.avancado;

import aula.modulo5.avancado.model.Transacao;
import aula.modulo5.avancado.model.enums.TipoTransacao;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static aula.modulo5.avancado.SimuladorDesafioAvancado.*;
import static shared.SimuladorDelay.delayFinal;

public class DesafioAvancado {

    public static void main(String[] args) {

        final var transacao = criarTransacao(BigDecimal.TEN, TipoTransacao.PIX);

        delayFinal();

    }


    private static CompletableFuture<Boolean> verificarIdentidadeUsuario(Transacao transacao) {
        return CompletableFuture.supplyAsync(() -> {
            final var comunicacaoFalhou = simularFalha();
            if (Boolean.TRUE.equals(comunicacaoFalhou)) {
                System.err.printf("[Transação %s] - Falha de comunicação com o sistema de verificação de identidade!\n", transacao.getId());
                throw new RuntimeException(String.format("[Transação %s] - Falha de comunicação! Iniciando retentativa...\n", transacao.getId()));
            }
            return ehUsuarioValido(transacao);
        });
    }

    private static boolean ehUsuarioValido(Transacao transacao) {
        System.out.printf("[Transação %s] - verificando identidade do usuário %s\n", transacao.getId(), transacao.getIdUsuario());
        final var ehUsuarioValido = simularIdentidadeUsuario(transacao.getIdUsuario());
        if (Boolean.FALSE.equals(ehUsuarioValido)) {
            System.err.printf("[Transação %s] - Usuário %s com identidade inválida. Transação rejeitada!\n", transacao.getId(), transacao.getIdUsuario());
            return false;
        }
        System.out.printf("[Transação %s] - Usuário %s com identidade válida. Transação aceita!\n", transacao.getId(), transacao.getIdUsuario());
        return true;
    }

}
