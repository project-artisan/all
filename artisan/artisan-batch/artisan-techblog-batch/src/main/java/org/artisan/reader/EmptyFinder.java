package org.artisan.reader;


import java.util.Optional;
import java.util.function.Supplier;

public class EmptyFinder {
    public static <R> Optional<R> get(Supplier<R> result) {
        try {
            return Optional.of(result.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
