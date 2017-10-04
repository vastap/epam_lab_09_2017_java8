package lambda.part1.example;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import data.Person;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings({"Guava", "Convert2MethodRef", "ComparatorCombinators"})
public class Lambdas02 {

    @Test
    public void sortPersons() {
        Person[] persons = {
                new Person("name 3", "lastName 3", 20),
                new Person("name 1", "lastName 2", 40),
                new Person("name 2", "lastName 1", 30)
        };
        // Expression lambda
        Arrays.sort(persons, (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));
        Arrays.sort(persons, (o1, o2) -> {
            System.out.println("sss");
            return o1.getLastName().compareTo(o2.getLastName());
        });
//        Arrays.sort(persons, Comparator.comparing(p -> p.getLastName()));
        assertArrayEquals(new Person[]{
                new Person("name 2", "lastName 1", 30),
                new Person("name 1", "lastName 2", 40),
                new Person("name 3", "lastName 3", 20),
        }, persons);
    }

    @Test
    public void findFirstByName_guava() {
        List<Person> persons = ImmutableList.of(
                new Person("name 3", "lastName 3", 20),
                new Person("name 1", "lastName 2", 40),
                new Person("name 2", "lastName 1", 30),
                new Person("name 1", "lastName 3", 40)
        );

        final Optional<Person> personOptional =
                FluentIterable.from(persons)
                        .firstMatch(p -> p != null && p.getFirstName().equals("name 1"));
        if (personOptional.isPresent()) {
            personOptional.get().print();
            assertNotNull(personOptional.get());
            assertEquals(new Person("name 1", "lastName 2", 40), personOptional.get());
        }
    }

    @Test
    public void lastNamesSet() {
        List<Person> persons = ImmutableList.of(
                new Person("name 3", "lastName 3", 20),
                new Person("name 1", "lastName 2", 40),
                new Person("name 2", "lastName 1", 30)
        );

        final ImmutableMap<String, Person> personByLastName =
                FluentIterable.from(persons)
                        .uniqueIndex(person -> person.getLastName());
        assertEquals(new Person("name 1", "lastName 2", 40), personByLastName.get("lastName 2"));
    }
}
