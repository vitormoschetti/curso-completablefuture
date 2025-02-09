package pratica.modulo3.thencombineaccept.exercicios;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static shared.SimuladorDelay.delay;
import static shared.SimuladorDelay.delayFinal;
import static shared.SimuladorEmpresa.simularAvaliacoes;

public class Exercicio2 {

    /*
        Um e-commerce precisa calcular a média de avaliações de um produto com base em diferentes fontes:

        1. Uma API retorna a lista de avaliações do site (ex.: 4.5, 4.5, 4.0, 5.0, 3).
        2. Outra API retorna a lista de avaliações de um parceiro (ex.: 4.2, 3.5, 4.1, 5.0, 3.7).
        3. Combine os dois resultados para calcular a média geral e exiba no console no formato: "Média geral: X".

        Tarefa:
        1. Use thenCombine para combinar as avaliações.
        2. Use thenAccept no final para exibir a média geral no console.
     */

    public static void main(String[] args) {

        final var avaliacoesSiteFuture = CompletableFuture.supplyAsync(() -> {
            delay();
            return simularAvaliacoes();
        });

        final var avaliacoesParceiroFuture = CompletableFuture.supplyAsync(() -> {
            delay();
            return simularAvaliacoes();
        });

        avaliacoesSiteFuture.thenCombine(avaliacoesParceiroFuture, (site, parceiro) -> {
                    //adicionando a lista de avaliacoes do parceiro e a lista de avaliacoes do site na nova lista
                    final var avaliacoes = new ArrayList<Double>(site.size() + parceiro.size());
                    avaliacoes.addAll(site);
                    avaliacoes.addAll(parceiro);
                    return avaliacoes;
                }).thenApply(avaliacoes -> avaliacoes.stream().mapToDouble(Double::doubleValue).average()) // utilizando java stream para calcular a media
                .thenAccept(mediaAvaliacao -> mediaAvaliacao.ifPresent(m -> System.out.printf("Media: %.2f%n", m))); //formatando e exibindo com thenAccept


        delayFinal();


    }

}
