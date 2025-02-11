package aula.modulo5.basico;


import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class SimuladorPedido {

    public static Pedido pedir(){
        return new Pedido(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10, 100)));
    }

}
