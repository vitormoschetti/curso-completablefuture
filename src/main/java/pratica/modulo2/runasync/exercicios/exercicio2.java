package pratica.modulo2.runasync.exercicios;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class exercicio2 {

    private static final Random RANDOM = new Random();


    /*
        Implemente um programa que simule o envio de notificações para três clientes diferentes.
        Cada notificação deve ser processada por uma tarefa separada usando runAsync.
        Após criar as tarefas, use join em todas elas para garantir que o programa espere o envio de todas as notificações antes de exibir "Envio concluído!".
     */

    public static void main(String[] args) throws InterruptedException {

        //Simulando notificação para o cliente 1
        CompletableFuture<Void> cliente1 = CompletableFuture.runAsync(() -> {
            simulandoDelay();
            System.out.println("Notificando cliente 1");
        });

        //Simulando notificação para o cliente 2
        CompletableFuture<Void> cliente2 = CompletableFuture.runAsync(() -> {
            simulandoDelay();
            System.out.println("Notificando cliente 2");
        });

        //Simulando notificação para o cliente 3
        CompletableFuture<Void> cliente3 = CompletableFuture.runAsync(() -> {
            simulandoDelay();
            System.out.println("Notificando cliente 3");
        });

        //Bloqueando futures até o final do processamento
        cliente1.join();
        cliente2.join();
        cliente3.join();

    }

    // Simulando o delay para uma aplicação externa entre 500ms e 5500ms
    static void simulandoDelay() {

        int min = 500;
        int max = 5000;

        int randomTime = RANDOM.nextInt(max) + min;
        try {
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
