package lambda.part3.exercise;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class FilterMap {

    public static class Container<T, R> {
        private final Predicate<T> predicate;
        private final Function<T, R> function;

        public Container(Predicate<T> predicate) {
            this.predicate = predicate;
            this.function = null;
        }

        public Container(Function<T, R> function) {
            this.function = function;
            this.predicate = null;
        }

        public Predicate<T> getPredicate() {
            return predicate;
        }

        public Function<T, R> getFunction() {
            return function;
        }
    }

    public static class LazyCollectionHelper<T> {

        private final List<Container<Object, Object>> actions;

        private final List<T> list;

        public LazyCollectionHelper(List<T> list, List<Container<Object, Object>> actions) {
            this.actions = actions;
            this.list = list;
        }

        public LazyCollectionHelper(List<T> list) {
            this(list, new ArrayList<>());
        }

        public LazyCollectionHelper<T> filter(Predicate<T> condition) {
            List<Container<Object, Object>> newActions = new ArrayList<>(actions);
            newActions.add(new Container<>((Predicate<Object>) condition));
            return new LazyCollectionHelper<>(list, newActions);
        }

        public <R> LazyCollectionHelper<R> map(Function<T, R> function) {
            List<Container<Object, Object>> newActions = new ArrayList<>(actions);
            newActions.add(new Container<>((Function<Object, Object>) function));
            return new LazyCollectionHelper<R>((List<R>) list, newActions);
        }

        public List<T> force() {
            if (actions.isEmpty()) {
                return new ArrayList<>(list);
            }
            List<T> result = new ArrayList<>();
            nextValue:
            for (Object obj : list) {
                for (Container<Object, Object> action : actions) {
                    if (action.getPredicate() != null) {
                        if (!action.getPredicate().test(obj)) {
                            continue nextValue;
                        }
                    } else {
                        obj = action.function.apply(obj);
                    }
                }
                result.add((T) obj);
            }
            return result;
        }
    }

    @Test
    public void test() {
        List<Integer> integers = Arrays.asList(1, 2, 100, 110, 200, 300, 500);
        List<String> result = new LazyCollectionHelper<>(integers).filter(val -> val > 10)
                .filter(val -> val < 400)
                .map(Object::toString)
                .filter(str -> str.startsWith("1"))
                .force();
        assertEquals(Arrays.asList("100", "110"), result);
    }
}
