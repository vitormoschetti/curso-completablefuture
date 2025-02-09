package shared;


import java.util.Random;

public class SimuladorDelay {

    private static final Random random = new Random();

    private SimuladorDelay(){}

    // Simulando o delay para uma aplicação externa entre 500ms e 5500ms
    public static void delay() {

        int min = 500;
        int max = 5000;

        int randomTime = random.nextInt(max) + min;
        try {
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
