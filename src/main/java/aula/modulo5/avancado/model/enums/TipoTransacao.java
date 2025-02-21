package aula.modulo5.avancado.model.enums;

import java.util.List;
import java.util.Set;

public enum TipoTransacao {

    DEBITO,
    CREDITO,
    PIX;

    private static final Set<TipoTransacao> TIPO_VALIDA_SALDO = Set.of(CREDITO, PIX);

    public static boolean exigeValidacaoDeSaldo(TipoTransacao tipoTransacao) {
        return TIPO_VALIDA_SALDO.contains(tipoTransacao);
    }

}
