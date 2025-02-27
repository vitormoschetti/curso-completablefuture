package pratica.modulo3.thencompose.exercicios;

import shared.SimuladorPessoa;

import java.util.concurrent.CompletableFuture;

import static shared.SimuladorValor.simularSaldo;

public class Exercicio2 {

    /*
        Um sistema bancário simula a validação do CPF de um cliente e, caso ele seja válido, busca o saldo de sua conta em outro serviço.
            1. O primeiro serviço valida o CPF e retorna um booleano (true ou false).
            2. Se o CPF for válido, o segundo serviço retorna o saldo disponível (ex.: 1250.75).
            3. Caso contrário, retorne uma mensagem de erro, como "CPF inválido".
     */

    public static void main(String[] args) {

        final var resultado = CompletableFuture.supplyAsync(() -> SimuladorPessoa.cpfEhValido("CPF"))
                .thenCompose(cpfValido -> {
                    if (Boolean.TRUE.equals(cpfValido)) {
                        return CompletableFuture.supplyAsync(() -> simularSaldo().toString());
                    }
                    return CompletableFuture.supplyAsync(() -> "CPF inválido");
                }).join();

        System.out.println("Resultado: " + resultado);


    }

}
