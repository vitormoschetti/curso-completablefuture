package pratica.modulo4.handle.exercicios;

import shared.SimuladorDelay;
import shared.SimuladorEmpresa;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.simularFalha;
import static shared.SimuladorValor.simularTransacao;

public class Exercicio1 {

    /*

    Você está chamando uma API externa para verificar o saldo de um cliente em um sistema bancário.
    Se ocorrer uma falha ao tentar consultar o saldo, você precisa registrar a falha no log e retornar o saldo como 0, mas não interromper o fluxo.

    1. Simule uma falha ao tentar obter o saldo do cliente.
    2. Caso falhe, registre uma mensagem de erro no log e retorne o valor 0 para o saldo.
    3. Se não houver falha, apenas retorne o saldo obtido.

    Tarefa:

    1. Utilize o handle para tratar a falha, registrando um erro no log e retornando 0.
    2. Use thenAccept para exibir o saldo do cliente, seja ele real ou o valor padrão 0 após a falha.

     */

    public static void main(String[] args) {

        CompletableFuture.supplyAsync(() -> {
                    System.out.println("Recuperando o saldo do cliente...");
                    SimuladorDelay.delay();
                    if (simularFalha()) {
                        throw new RuntimeException("Falha na comunicação com serviço de saldo");
                    }
                    final var totalVendas = simularTransacao();
                    System.out.println("Sucesso ao recuperar o saldo.");
                    return totalVendas;
                })
                .handle((result, ex) -> {
                    if(ex != null) {
                        System.err.println("Falha ao recuperar o saldo. Utilizando o valor padrão para sequência do fluxo");
                        return BigDecimal.ZERO;
                    }
                    return result;
                })
                .thenAccept(totalVendas -> System.out.println("Total de vendas final: R$" + totalVendas));


        delayFinal();

    }

}
