package pratica.modulo2.supplyasync.exercicios;

import java.util.concurrent.CompletableFuture;

public class Exercicio1 {

    /*
    Crie um programa que use supplyAsync para executar uma tarefa que retorne uma mensagem "Tarefa concluída!".
    Após a execução, use join para obter o resultado e exiba-o no console.
     */

    public static void main(String[] args) {

        //supplyAsync retorna a string que definimos
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Tarefa concluída!");

        //bloqueio da Thread principal
        String result = future.join();

        System.out.println(result);

    }

}
