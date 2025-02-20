package aula.modulo5.avancado.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class RetryUtil {

    public static <T> CompletableFuture<T> retry(Supplier<CompletableFuture<T>> execucao) {
        return execucao.get()
                .exceptionallyCompose(__ -> execucao.get());
    }
}
