package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import static com.google.common.base.Functions.compose;

public abstract class Functions2 {

    public static <A, B, C> Function<A, C> chain(Function<A, B> f1, Function<B, C> f2) {
        return compose(f2, f1);
    }

    public static <A, B> Function<Optional<A>, Optional<B>> liftForOptions(final Function<A, B> f) {
        return new Function<Optional<A>, Optional<B>>() {
            @Override
            public Optional<B> apply(Optional<A> aOptional) {
                return aOptional.transform(f);
            }
        };
    }
}
