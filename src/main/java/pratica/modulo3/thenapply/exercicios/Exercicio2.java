package pratica.modulo3.thenapply.exercicios;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Exercicio2 {

    /*
        Um sistema meteorológico fornece a temperatura em graus Celsius (ex.: 25°C). Sua tarefa é:
            1. Converter a temperatura para Fahrenheit usando a fórmula: F = C * 9/5 + 32.
            2. Exibir o resultado no console.
     */

    public static void main(String[] args) {

        final var random = new Random();
        final var grausCelsius = random.nextInt(50);

        CompletableFuture<BigDecimal> futureFahrenheit = CompletableFuture.supplyAsync(() -> new BigDecimal(grausCelsius))
                .thenApply(temperature -> temperature.multiply((new BigDecimal("9").divide(new BigDecimal("5")))))
                .thenApply(temperature -> temperature.add(new BigDecimal("32")));

        final var grausFahrenheit = futureFahrenheit.join();

        System.out.println(grausCelsius + "°C equivale a " + grausFahrenheit + "°F");

    }

}
