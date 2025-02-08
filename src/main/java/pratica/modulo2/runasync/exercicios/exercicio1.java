package pratica.modulo2.runasync.exercicios;

import java.util.concurrent.CompletableFuture;

public class exercicio1 {

    /*
        Crie um programa que use runAsync para executar uma tarefa que exiba "Processando tarefa..." no console.
        Após a execução, use join para garantir que a tarefa seja concluída antes de exibir "Tarefa concluída!".
     */

    public static void main(String[] args) {

        // runAsync retorna um CompletableFuture<Void>
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println("Processando tarefa"));

        // bloqueando a Thread principal até que o conteúdo do runAsync seja executado
        future.join();

        System.out.println("Tarefa concluída!");

        /*
            Saída:
            Processando tarefa
            Tarefa concluída!
         */

    }

}
