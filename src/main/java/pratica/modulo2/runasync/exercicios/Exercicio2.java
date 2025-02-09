package pratica.modulo2.runasync.exercicios;

import shared.SimuladorDelay;

import java.util.concurrent.CompletableFuture;

public class Exercicio2 {


    /*
        Implemente um programa que simule o envio de notificações para três clientes diferentes.
        Cada notificação deve ser processada por uma tarefa separada usando runAsync.
        Após criar as tarefas, use join em todas elas para garantir que o programa espere o envio de todas as notificações antes de exibir "Envio concluído!".
     */

    public static void main(String[] args) throws InterruptedException {

        //Simulando notificação para o cliente 1
        CompletableFuture<Void> cliente1 = CompletableFuture.runAsync(() -> {
            SimuladorDelay.delay(); // Simulando o delay para uma aplicação externa entre 500ms e 5500ms
            System.out.println("Notificando cliente 1");
        });

        //Simulando notificação para o cliente 2
        CompletableFuture<Void> cliente2 = CompletableFuture.runAsync(() -> {
            SimuladorDelay.delay(); // Simulando o delay para uma aplicação externa entre 500ms e 5500ms
            System.out.println("Notificando cliente 2");
        });

        //Simulando notificação para o cliente 3
        CompletableFuture<Void> cliente3 = CompletableFuture.runAsync(() -> {
            SimuladorDelay.delay(); // Simulando o delay para uma aplicação externa entre 500ms e 5500ms
            System.out.println("Notificando cliente 3");
        });

        //Bloqueando futures até o final do processamento
        cliente1.join();
        cliente2.join();
        cliente3.join();

    }



}
