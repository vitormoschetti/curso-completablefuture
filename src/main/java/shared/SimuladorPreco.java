package shared;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class SimuladorPreco {

    private static final Random random = new Random();

    private SimuladorPreco() {
    }

    //Simula o preco entre 1,00 e 220,00
    public static BigDecimal simularPreco() {
        return BigDecimal.valueOf(random.nextInt(20)+1)
                .multiply(BigDecimal.valueOf(random.nextDouble(10) + 1))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
