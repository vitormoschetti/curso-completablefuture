package aula.modulo5.avancado.model;

import aula.modulo5.avancado.model.enums.StatusTransacao;
import aula.modulo5.avancado.model.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static aula.modulo5.avancado.model.enums.StatusTransacao.liberadaParaProcessamento;
import static aula.modulo5.avancado.model.enums.TipoTransacao.exigeValidacaoDeLimite;
import static aula.modulo5.avancado.model.enums.TipoTransacao.exigeValidacaoDeSaldo;

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

    public StatusTransacao getStatus() {
        return status;
    }

    public void registrarFalhaVerificacaoIdentidade() {
        status = StatusTransacao.FALHA_VERIFICACAO_IDENTIDADE;
        dataAtualizacao = LocalDateTime.now();
        dataCancelamento = LocalDateTime.now();
    }

    public void registrarPendenteSaldoAntigo() {
        status = StatusTransacao.PENDENTE_COM_SALDO_ANTERIOR;
        dataAtualizacao = LocalDateTime.now();
    }

    public Boolean temIdendidadeValida() {
        return !StatusTransacao.FALHA_VERIFICACAO_IDENTIDADE.equals(status);
    }

    public boolean temValidacaoDeLimite() {
        return exigeValidacaoDeLimite(tipo);
    }

    public void registrarFalhaVerificacaoLimite() {
        status = StatusTransacao.FALHA_VERIFICACAO_LIMITE;
        dataAtualizacao = LocalDateTime.now();
        dataCancelamento = LocalDateTime.now();
    }

    public void registrarSemLimite() {
        status = StatusTransacao.SEM_LIMITE;
        dataAtualizacao = LocalDateTime.now();
        dataCancelamento = LocalDateTime.now();
    }

    public boolean temValidacaoDeSaldo() {
        return exigeValidacaoDeSaldo(tipo);
    }

    public void registrarSemSaldo() {
        status = StatusTransacao.SEM_SALDO;
        dataAtualizacao = LocalDateTime.now();
        dataCancelamento = LocalDateTime.now();
    }

    public boolean podeSerProcessada() {
        return liberadaParaProcessamento(status);
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void registrarProcessamento() {
        status = StatusTransacao.PROCESSADA_COM_SUCESSO;
        dataAtualizacao = LocalDateTime.now();
    }
}
