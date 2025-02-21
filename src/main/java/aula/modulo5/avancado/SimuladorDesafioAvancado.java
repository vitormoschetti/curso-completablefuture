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

    public static boolean simularIdentidadeUsuario(UUID idUsuario) {
        return Math.random() > 0;
    }

    public static boolean simularFalha() {
        return Math.random() > 0.1;
    }

    public static SaldoRecord simularSaldo() {
        return new SaldoRecord(BigDecimal.valueOf(RANDOM.nextDouble(50.0)));
    }
}
