package shared;


import java.util.Random;

public class SimuladorDelay {

    private static final Random random = new Random();

    private SimuladorDelay(){}

    public static void delay() {

        int min = 500;
        int max = 1500;

        int randomTime = random.nextInt(max) + min;
        try {
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delayFinal() {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
