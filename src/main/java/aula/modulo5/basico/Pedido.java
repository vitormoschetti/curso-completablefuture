package aula.modulo5.basico;

import java.math.BigDecimal;
import java.util.UUID;

public class Pedido {

    private final UUID id;
    private final BigDecimal valor;
    private StatusPedido status;

    public Pedido(BigDecimal valor) {
        this.id = UUID.randomUUID();
        this.valor = valor;
        this.status = StatusPedido.CRIADO;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public UUID getId() {
        return id;
    }

    public void cancelar() {
        this.status = StatusPedido.CANCELADO;
    }

    public void semSaldo() {
        this.status = StatusPedido.SEM_SALDO;
    }

    public void confirmar() {
        this.status = StatusPedido.CONFIRMADO;
    }

    public void preparar() {
        this.status = StatusPedido.EM_PREPARACAO;
    }

    public void rotaEntrega() {
        this.status = StatusPedido.ROTA_ENTREGA;
    }

    public void negado() {
        this.status = StatusPedido.NEGADO;
    }
}
