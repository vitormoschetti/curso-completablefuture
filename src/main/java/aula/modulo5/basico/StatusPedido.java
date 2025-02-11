package aula.modulo5.basico;

public enum StatusPedido {

    CRIADO("Pedido criado com sucesso!"),
    CANCELADO("Pedido cancelado por erro interno"),
    CONFIRMADO("Pedido confirmado com sucesso!"),
    SEM_SALDO("Sem saldo suficiente na carteira");

    private final String notificacao;

    StatusPedido(String notificacao) {
        this.notificacao = notificacao;
    }

    public String notificacao() {
        return notificacao;
    }
}
