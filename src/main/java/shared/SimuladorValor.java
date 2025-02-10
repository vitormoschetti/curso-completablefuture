package shared;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class SimuladorValor {

    private static final Random random = new Random();

    private SimuladorValor() {
    }

    public static BigDecimal simularPreco() {
        return BigDecimal.valueOf(random.nextInt(20)+1)
                .multiply(BigDecimal.valueOf(random.nextDouble(10) + 1))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal simularTransacao() {
        return BigDecimal.valueOf(random.nextInt(2000)+1000)
                .multiply(BigDecimal.valueOf(random.nextDouble(100) + 1))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal simularSaldo() {
        return BigDecimal.valueOf(random.nextInt(200)+10)
                .multiply(BigDecimal.valueOf(random.nextDouble(100) + 1))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal simularDolar() {
        return BigDecimal.valueOf(random.nextDouble(0.2)+ 0.1);
    }
}
