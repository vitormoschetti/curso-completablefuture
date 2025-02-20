package aula.modulo5.avancado;

import aula.modulo5.avancado.model.Transacao;
import aula.modulo5.avancado.model.enums.TipoTransacao;

import java.math.BigDecimal;
import java.util.UUID;

public class SimuladorDesafioAvancado {

    public static Transacao criarTransacao(BigDecimal valor, TipoTransacao tipo) {

        return new Transacao(valor, tipo);

    }

    public static boolean simularIdentidadeUsuario(UUID idUsuario) {
        return Math.random() > 0;
    }

    public static boolean simularFalha() {
        return Math.random() > 0.7;
    }
}
