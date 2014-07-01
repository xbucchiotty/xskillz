package fr.xebia.xskillz;

import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class Functions {

    public static <A, B> Function<Optional<A>, Optional<B>> liftForOptions(final Function<A, B> f) {
        return aOptional -> aOptional.map(f);
    }

    public static <T> Stream<T> stream(Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false);
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
