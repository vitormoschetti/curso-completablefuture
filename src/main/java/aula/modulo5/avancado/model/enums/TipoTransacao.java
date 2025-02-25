package aula.modulo5.avancado.model.enums;

import java.util.Set;

public enum TipoTransacao {

    DEBITO,
    CREDITO,
    PIX;

    private static final Set<TipoTransacao> TIPO_VALIDA_LIMIT = Set.of(CREDITO, PIX);
    private static final Set<TipoTransacao> TIPO_VALIDA_SALDO = Set.of(DEBITO, PIX);

    public static boolean exigeValidacaoDeLimite(TipoTransacao tipoTransacao) {
        return TIPO_VALIDA_LIMIT.contains(tipoTransacao);
    }

    public static boolean exigeValidacaoDeSaldo(TipoTransacao tipoTransacao) {
        return TIPO_VALIDA_SALDO.contains(tipoTransacao);
    }

}
