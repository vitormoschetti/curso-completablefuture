package aula.modulo5.intermediario.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pedido {

    private final UUID id;
    private final String endereco;
    private final BigDecimal valor;
    private StatusPedido status;
    private boolean reagendado;
    private LocalDate previsaoEntrega;
    private final LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Pedido(String endereco, BigDecimal valor) {
        this.id = UUID.randomUUID();
        this.endereco = endereco;
        this.valor = valor;
        this.status = StatusPedido.RECEBIDO;
        this.reagendado = false;
        this.criadoEm = LocalDateTime.now();
        this.previsaoEntrega = LocalDate.now().plusDays(3);
    }

    public UUID getId() {
        return id;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void reagendar() {
        status = StatusPedido.REAGENDADO;
        atualizadoEm = LocalDateTime.now();
    }

    public void emTransporte() {
        status = StatusPedido.EM_TRANSPORTE;
        atualizadoEm = LocalDateTime.now();
    }

    public boolean estaEmTransporte() {
        return StatusPedido.EM_TRANSPORTE.equals(status);
    }

    public void entregue() {
        status = StatusPedido.ENTREGUE;
        atualizadoEm = LocalDateTime.now();
    }

    public boolean foiEntregue() {
        return StatusPedido.ENTREGUE.equals(status);
    }

    public boolean deveSerReagendado() {
        return StatusPedido.REAGENDADO.equals(status);
    }

    public void reagendarEntrega() {
        reagendado = true;
        status = StatusPedido.RECEBIDO;
        previsaoEntrega = previsaoEntrega.plusDays(2);
        atualizadoEm = LocalDateTime.now();
    }

    public void cancelar() {
        status = StatusPedido.CANCELADO;
        atualizadoEm = LocalDateTime.now();
    }
}
