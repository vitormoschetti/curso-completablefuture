package aula.modulo5.avancado.model.record;

import aula.modulo5.avancado.model.Transacao;
import aula.modulo5.avancado.model.enums.StatusTransacao;

import java.math.BigDecimal;
import java.util.UUID;

public record TransacaoComSaldo(Transacao transacao, SaldoRecord saldoConta) {
    public boolean temValidacaoDeLimite() {
        return transacao().temValidacaoDeLimite();
    }

    public void registrarFalhaVerificacaoLimite() {
        transacao.registrarFalhaVerificacaoLimite();
    }

    public UUID transacaoId() {
        return transacao.getId();
    }

    public StatusTransacao transacaoStatus() {
        return transacao.getStatus();
    }

    public UUID usuarioId() {
        return transacao.getIdUsuario();
    }

    public boolean temLimiteParaTransacao(BigDecimal limiteDisponivel) {
        return transacao.getValor().compareTo(limiteDisponivel) <= 0;
    }

    public void registrarSemLimite() {
        transacao.registrarSemLimite();
    }

    public boolean temValidacaoDeSaldo() {
        return transacao().temValidacaoDeSaldo();
    }

    public boolean temSaldoParaTransacao() {
        return transacao.getValor().compareTo(saldoConta.valor()) >= 0;
    }

    public void registrarSemSaldo() {
        transacao.registrarSemSaldo();
    }

    public boolean podeSerProcessada() {
        return transacao.podeSerProcessada();
    }
}
