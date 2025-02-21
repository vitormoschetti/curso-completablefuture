package aula.modulo5.avancado.model.record;

import aula.modulo5.avancado.model.Transacao;

public record TransacaoComSaldo(Transacao transacao, SaldoRecord saldo) {
}
