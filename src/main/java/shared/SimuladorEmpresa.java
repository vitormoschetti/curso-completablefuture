package shared;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimuladorEmpresa {

    private static final Random random = new Random();

    private SimuladorEmpresa() {
    }

    public static List<Double> simularAvaliacoes() {

        final var avaliacoes = new ArrayList<Double>();
        avaliacoes.add(random.nextDouble(5.0));
        avaliacoes.add(random.nextDouble(5.0));
        avaliacoes.add(random.nextDouble(5.0));
        avaliacoes.add(random.nextDouble(5.0));
        avaliacoes.add(random.nextDouble(5.0));
        return avaliacoes;
    }

    public static boolean simularFalha() {
        return random.nextInt(10) < 5;
    }

    public static boolean simularDisponibilidade() {
        return random.nextInt(10) > 1;
    }

    public static LocalDate simularDataEntrega() {
        return LocalDate.now().plusDays(random.nextInt(10)+2);
    }
}
