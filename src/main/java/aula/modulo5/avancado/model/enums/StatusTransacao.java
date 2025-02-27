package aula.modulo5.avancado.model.enums;

import java.util.Set;

public enum StatusTransacao {

    FALHA_VERIFICACAO_IDENTIDADE,
    PENDENTE_COM_SALDO_ANTERIOR,
    FALHA_VERIFICACAO_LIMITE,
    SEM_LIMITE,
    SEM_SALDO,
    PENDENTE,
    PROCESSADA_COM_SUCESSO;

    private static final Set<StatusTransacao> LIBERADO_PARA_PROCESSAMENTO = Set.of(PENDENTE, PENDENTE_COM_SALDO_ANTERIOR);

    public static boolean liberadaParaProcessamento(StatusTransacao statusTransacao) {
        return LIBERADO_PARA_PROCESSAMENTO.contains(statusTransacao);
    }

}
