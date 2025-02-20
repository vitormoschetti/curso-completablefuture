package aula.modulo5.avancado.model;

import aula.modulo5.avancado.model.enums.StatusTransacao;
import aula.modulo5.avancado.model.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transacao {

    private final UUID id;
    private final UUID idUsuario;
    private final BigDecimal valor;
    private final TipoTransacao tipo;
    private StatusTransacao status;
    private final LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime dataCancelamento;


    public Transacao(BigDecimal valor, TipoTransacao tipo) {
        this.id = UUID.randomUUID();
        this.idUsuario = UUID.randomUUID();
        this.valor = valor;
        this.tipo = tipo;
        this.status = StatusTransacao.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public void rejeitarIdentidade() {
        this.status = StatusTransacao.FALHA_VERIFICACAO_IDENTIDADE;
        this.dataAtualizacao = LocalDateTime.now();
        this.dataCancelamento = LocalDateTime.now();
    }
}
