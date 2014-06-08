package fr.xebia.xskillz;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static fr.xebia.xskillz.Functions2.liftForOptions;
import static org.fest.assertions.api.Assertions.assertThat;

public class Functions2Test {

    public static final Optional<String> ABSENT = Optional.absent();

    @Test
    public void chain_should_execute_f1_after_f2() {
        final AtomicBoolean f1Called = new AtomicBoolean();
        final AtomicBoolean f2Called = new AtomicBoolean();

        Function<Integer, Integer> f1 = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                assertThat(f1Called.getAndSet(true)).isFalse();
                assertThat(f2Called.get()).isFalse();
                return integer;
            }
        };

        Function<Integer, Integer> f2 = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                assertThat(f1Called.get()).isTrue();
                assertThat(f2Called.getAndSet(true)).isFalse();
                return integer;
            }
        };

        //When
        Functions2.chain(f1, f2);
    }

    @Test
    public void should_lift_function_with_non_null_result_to_some_output() {
        final String expectation = "expected";
        Function<String, String> functionWithOutput = new Function<String, String>() {
            @Override
            public String apply(String s) {
                return expectation;
            }
        };

        Function<Optional<String>, Optional<String>> liftedFWith = liftForOptions(functionWithOutput);

        assertThat(liftedFWith.apply(Optional.of("input"))).isEqualTo(Optional.of(expectation));
        assertThat(liftedFWith.apply(ABSENT)).isEqualTo(ABSENT);

    }

}