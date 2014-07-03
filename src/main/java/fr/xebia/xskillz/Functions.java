package fr.xebia.xskillz;

import java.util.Collection;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class Functions {

    public static <A, B> Function<Optional<A>, Optional<B>> liftO(final Function<? super A, ? extends B> f) {
        return aOptional -> aOptional.map(f);
    }

    public static <A, B> Function<Stream<A>, Stream<B>> liftS(final Function<? super A, ? extends B> f) {
        return stream -> stream.map(f);
    }

    public static <T> Stream<T> stream(Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false);
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Function<Stream<T>, Collection<T>> toList() {
        return stream -> stream.collect(Collectors.toList());
    }

    public static <T> Function<Optional<T>, T> getOptionalOr(Supplier<T> or) {
        return optionalT -> optionalT.orElseGet(or);
    }
}
