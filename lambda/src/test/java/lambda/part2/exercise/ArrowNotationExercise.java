package lambda.part2.exercise;

import data.Person;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ArrowNotationExercise {

    @Test
    public void getAge() {
        // Person -> Integer
        final Function<Person, Integer> getAge = Person::getAge; // TODO

        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    @Test
    public void compareAges() {
        // TODO use BiPredicate
        // compareAges: (Person, Person) -> boolean
        final BiFunction<Person, Person, Boolean> compareAges = (x, y) -> x.getAge()==y.getAge();
//        throw new UnsupportedOperationException("Not implemented");
//        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
        assertEquals(true, compareAges.apply(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // TODO
    // getFullName: Person -> String
    final Function<Person,String> getFullName =(x)-> x.getFirstName() +" "+ x.getLastName();

    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> int
    final public BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName(Function<Person, String> x) {
        return (y1,y2)-> x.apply(y1).length() > x.apply(y2).length() ? y1.getAge() : y2.getAge();
    }

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // Person -> String
        final Function<Person, String> getFullName = (x)-> x.getFirstName() +" "+ x.getLastName();; // TODO

        // (Person, Person) -> Integer
        // TODO use ageOfPersonWithTheLongestFullName(getFullName)
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName
                = ageOfPersonWithTheLongestFullName(getFullName);

        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
