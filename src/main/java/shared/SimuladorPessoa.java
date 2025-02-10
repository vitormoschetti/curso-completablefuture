package shared;

import java.util.List;
import java.util.Random;

public class SimuladorPessoa {

    private static final Random random = new Random();

    private SimuladorPessoa(){}

    public static String simularNome() {

        final var nomes = List.of("Carlos", "Marcio", "Julia", "Ana");

        return nomes.get(random.nextInt(nomes.size()));

    }

    public static boolean cpfEhValido(String cpf) {

        return random.nextBoolean();

    }


}
