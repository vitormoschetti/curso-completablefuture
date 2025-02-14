package aula.modulo5.intermediario;

import aula.modulo5.intermediario.model.Pedido;
import aula.modulo5.intermediario.model.PrevisaoTempo;
import aula.modulo5.intermediario.model.Rastreamento;

import java.math.BigDecimal;

public class SimuladorDesafioIntermediario {

    private SimuladorDesafioIntermediario() {}

    public static Pedido pedir(String endereco, BigDecimal valor) {
        return new Pedido(endereco, valor);
    }

    public static Rastreamento rastreamento(Pedido pedido) {
        return new Rastreamento(pedido.getId(), Math.random() > 0.8);
    }

    public static PrevisaoTempo previsaoTempo(Pedido pedido) {
        return new PrevisaoTempo(pedido.getEndereco(), Math.random() > 0.4);
    }

    public static void salvar(Pedido pedido) {
        //sem implementacao
    }
}
