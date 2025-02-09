package pratica.modulo3.thencompose.exercicios;

import shared.SimuladorPessoa;

import java.util.concurrent.CompletableFuture;

public class Exercicio1 {

    /*
        Uma aplicação precisa obter o nome de um usuário de um banco de dados e, em seguida, buscar detalhes adicionais sobre ele em outro serviço.
            1. O primeiro serviço retorna o nome do usuário (ex.: "Carlos").
            2. O segundo serviço retorna uma mensagem de boas-vindas personalizada, como "Bem-vindo, Carlos!".
     */

    public static void main(String[] args) {

        final var future = CompletableFuture.supplyAsync(SimuladorPessoa::simularNome)
                .thenCompose(nome -> CompletableFuture.supplyAsync(() -> String.format("Bem-vindo(a), %s", nome)));

        System.out.println(future.join());


    }

}
