package fr.xebia.xskillz;

import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;

import static fr.xebia.xskillz.Functions.liftO;
import static org.fest.assertions.api.Assertions.assertThat;

public class FunctionsTest {

    public static final Optional<String> ABSENT = Optional.empty();

    @Test
    public void should_lift_function_with_non_null_result_to_some_output() {
        final String expectation = "expected";
        Function<String, String> functionWithOutput = s -> expectation;

        Function<Optional<String>, Optional<String>> liftedFWith = liftO(functionWithOutput);

        assertThat(liftedFWith.apply(Optional.of("input"))).isEqualTo(Optional.of(expectation));
        assertThat(liftedFWith.apply(ABSENT)).isEqualTo(ABSENT);

    }

}