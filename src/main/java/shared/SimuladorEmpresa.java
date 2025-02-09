package shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimuladorEmpresa {

    private SimuladorEmpresa() {
    }

    public static List<Double> simularAvaliacoes() {

        final var random = new Random();

        final var avaliacoes = new ArrayList<Double>();
        avaliacoes.add(random.nextDouble(5.0));
        avaliacoes.add(random.nextDouble(5.0));
        avaliacoes.add(random.nextDouble(5.0));
        avaliacoes.add(random.nextDouble(5.0));
        avaliacoes.add(random.nextDouble(5.0));
        return avaliacoes;
    }

}
