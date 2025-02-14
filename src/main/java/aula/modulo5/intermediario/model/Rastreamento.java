package aula.modulo5.intermediario.model;

import java.util.UUID;

public class Rastreamento {

    private final UUID rastreioId;
    private final UUID pedidoId;
    private final boolean emTransporte;

    public Rastreamento(UUID pedidoId, boolean emTransporte) {
        this.rastreioId = UUID.randomUUID();
        this.pedidoId = pedidoId;
        this.emTransporte = emTransporte;
    }

    public UUID getRastreioId() {
        return rastreioId;
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public boolean isEmTransporte() {
        return emTransporte;
    }
}
