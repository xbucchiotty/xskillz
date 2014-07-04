package fr.xebia.xskillz;

import org.junit.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static fr.xebia.xskillz.Functions.liftO;
import static fr.xebia.xskillz.Functions.liftS;
import static java.util.stream.Collectors.toList;
import static org.fest.assertions.api.Assertions.assertThat;

public class FunctionsTest {

    public static final Optional<String> EMPTY = Optional.empty();

    @Test
    public void liftO_lifted_function_with_non_null_input_should_return_to_some_output() {
        Function<String, String> f = aFunctionReturning("output");
        Optional<String> input = Optional.of("input");

        Function<Optional<String>, Optional<String>> liftedF = liftO(f);
        Optional<String> result = liftedF.apply(input);

        assertThat(result).isEqualTo(Optional.of("output"));
    }

    @Test
    public void liftO_lifted_function_with_empty_input_should_not_return_to_output() {
        Function<String, String> f = aFunctionReturning("output");
        Optional<String> input = EMPTY;

        Optional<String> result = liftO(f).apply(input);

        assertThat(result).isEqualTo(EMPTY);
    }

    @Test
    public void liftS_should_lift_function_into_stream_domain() {
        Function<String, String> f = aFunctionAppending("-output");
        Stream<String> input = newArrayList("1", "2").stream();

        Stream<String> result = liftS(f).apply(input);

        assertThat(result.collect(toList())).isEqualTo(newArrayList("1-output", "2-output"));
    }

    @Test
    public void toList_should_collect_a_stream_to_list() {
        Stream<String> input = newArrayList("1", "2").stream();

        Collection<String> result = Functions.<String>toList().apply(input);

        assertThat(result).isEqualTo(newArrayList("1", "2"));
    }

    @Test
    public void getOptionalOr_should_return_the_input_when_non_empty() {
        Optional<String> someInput = Optional.of("someInput");
        Supplier<String> defaultValue = () -> "default";

        String result = Functions.getOptionalOr(defaultValue).apply(someInput);

        assertThat(result).isEqualTo("someInput");
    }

    @Test
    public void getOptionalOr_should_return_default_value_when_empty() {
        Optional<String> emptyInput = EMPTY;
        Supplier<String> defaultValue = () -> "default";

        String result = Functions.getOptionalOr(defaultValue).apply(emptyInput);

        assertThat(result).isEqualTo("default");
    }


    public Function<String, String> aFunctionReturning(String expectation) {
        return s -> expectation;
    }

    public Function<String, String> aFunctionAppending(String expectation) {
        return s -> s + expectation;
    }

}