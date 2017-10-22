package optional;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"Convert2MethodRef", "ExcessiveLambdaUsage", "ResultOfMethodCallIgnored", "OptionalIsPresent"})
public class OptionalExample {

    @Test
    public void get() {
        // Get empty optional. IsPresent always return false
        Optional<String> o1 = Optional.empty();

        o1.ifPresent(s -> System.out.println(s));

        assertEquals("t", o1.orElse("t"));
        // if not isPresent - return function result
        assertEquals("t", o1.orElseGet(() -> "t"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowException() {
        Optional<String> o1 = Optional.empty();
        o1.orElseThrow(() -> new UnsupportedOperationException());
    }

    @Test
    public void ifPresent() {
        Optional<String> o1 = getOptional();

        o1.ifPresent(System.out::println);

        if (o1.isPresent()) {
            System.out.println(o1.get());
        }
    }

    @Test
    public void map() {
        Optional<String> o1 = getOptional();

        Function<String, Integer> getLength = String::length;

        Optional<Integer> expected = o1.map(getLength);

        Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = Optional.ofNullable(getLength.apply(o1.get()));
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        Optional<String> optional = getOptional();

        // Create a flat mapper which add symbols from source string to optional
        Function<String, Optional<List<Character>>> flatMapper = str -> {
            Optional<List<Character>> chars = Optional.of(new ArrayList<Character>());
            str.chars().forEach(symbol -> chars.get().add((char) symbol));
            return chars;
        };

        // Apply function if value is present
        Optional<List<Character>> actual = optional.flatMap(flatMapper);

        // Check expected behavior
        Optional<List<Character>> expected;
        if (optional.isPresent()) {
            expected = flatMapper.apply(optional.get());
        } else {
            expected = Optional.empty();
        }
        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        Optional<String> optional = getOptional();
        Predicate<String> predicate = str -> str.contains("a");
        // Optional filter will check value by predicate if this value is present
        Optional<String> actual = optional.filter(predicate);
        // Check filter behavior
        Optional<String> expected;
        if (optional.isPresent()) {
            expected = predicate.test(optional.get()) ? optional : Optional.empty();
        } else {
            expected = Optional.empty();
        }
        assertEquals(expected, actual);
    }

    /**
     * Create pseudo-random Optional
     *
     * @return Pseude-random optional
     */
    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean() ? Optional.empty() : Optional.of("abc");
    }
}
