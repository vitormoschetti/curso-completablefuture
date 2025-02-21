package aula.modulo5.avancado;

import aula.modulo5.avancado.model.Transacao;
import aula.modulo5.avancado.model.enums.TipoTransacao;
import aula.modulo5.avancado.model.record.SaldoRecord;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

public class SimuladorDesafioAvancado {

    private static final Random RANDOM = new Random();

    public static Transacao criarTransacao(BigDecimal valor, TipoTransacao tipo) {

        return new Transacao(valor, tipo);

    }

    public static boolean simularIdentidadeUsuario() {
        return Math.random() > 0.2;
    }

    public static boolean simularFalha() {
        return Math.random() > 0.4;
    }

    public static BigDecimal simularValorTransacao() {
        return BigDecimal.valueOf(RANDOM.nextDouble(250.0));
    }

    public static SaldoRecord simularSaldo() {
        return new SaldoRecord(BigDecimal.valueOf(RANDOM.nextDouble(50.0)));
    }

    public static BigDecimal simularLimite() {
        return BigDecimal.valueOf(RANDOM.nextDouble(500.0));
    }

}
