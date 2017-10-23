package part1.exercise;

import data.raw.Employee;
import data.raw.Generator;
import data.raw.Person;
import db.SlowCompletableFutureDb;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CompletableFutureBasics {

    private static SlowCompletableFutureDb<Employee> employeeDb;
    private static List<String> keys;

    @BeforeClass
    public static void before() {
        Map<String, Employee> employeeMap = Generator.generateEmployeeList(1000)
                                                     .stream()
                                                     .collect(toMap(e -> getKeyByPerson(e.getPerson()), Function.identity(), (a, b) -> a));
        employeeDb = new SlowCompletableFutureDb<>(employeeMap);
        keys = new ArrayList<>(employeeMap.keySet());
    }

    private static String getKeyByPerson(Person person) {
        return person.getFirstName() + "_" + person.getLastName() + "_" + person.getAge();
    }

    @AfterClass
    public static void after() {
        try {
            employeeDb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createNonEmpty() throws ExecutionException, InterruptedException {
        Person person = new Person("John", "Galt", 33);

        // TASK: Create non empty Optional (of Person)
        Optional<Person> optPerson = Optional.of(person);

        assertTrue(optPerson.isPresent());
        assertEquals(person, optPerson.get());

        // TASK: Create stream with a single element
        Stream<Person> streamPerson = Stream.of(person);

        List<Person> persons = streamPerson.collect(toList());
        assertThat(persons.size(), is(1));
        assertEquals(person, persons.get(0));

        // TASK: Create completed CompletableFuture
        CompletableFuture<Person> futurePerson =  CompletableFuture.completedFuture(person);

        assertTrue(futurePerson.isDone());
        assertEquals(person, futurePerson.get());
    }

    @Test
    public void createEmpty() throws ExecutionException, InterruptedException {
        // TASK: Create empty Optional
        Optional<Person> optPerson = Optional.empty();

        assertFalse(optPerson.isPresent());

        // TASK: Create empty stream
        Stream<Person> streamPerson = Stream.empty();

        List<Person> persons = streamPerson.collect(toList());
        assertThat(persons.size(), is(0));

        // TASK: Complete CompletableFuture with NoSuchElementException
        CompletableFuture<Person> futurePerson = new CompletableFuture<>();
        futurePerson.completeExceptionally(new NoSuchElementException());

        assertTrue(futurePerson.isCompletedExceptionally());
        assertTrue(futurePerson
                .thenApply(x -> false)
                .exceptionally(t -> t.getCause() instanceof NoSuchElementException).get());
    }

    @Test
    public void forEach() throws ExecutionException, InterruptedException {
        Person person = new Person("John", "Galt", 33);

        // TASK: Create non empty Optional
        Optional<Person> optPerson = Optional.of(person);

        CompletableFuture<Person> result1 = new CompletableFuture<>();

        // TASK: using optPerson.ifPresent complete result1 (with Person)
        optPerson.ifPresent(result1::complete);
        assertEquals(person, result1.get());

        // TASK: Create stream with a single element
        Stream<Person> streamPerson = Stream.of(person);

        CompletableFuture<Person> result2 = new CompletableFuture<>();

        // TASK: Using streamPerson.forEach complete result2
        streamPerson.forEach(result2::complete);
        assertEquals(person, result2.get());

        // TASK: Create completed CompletableFuture
        CompletableFuture<Person> futurePerson = CompletableFuture.completedFuture(person);

        CompletableFuture<Person> result3 = new CompletableFuture<>();

        // TASK: Using futurePerson.thenAccept complete result3
        futurePerson.thenAccept(result3::complete);
        assertEquals(person, result3.get());
    }

    @Test
    public void map() throws ExecutionException, InterruptedException {
        Person person = new Person("John", "Galt", 33);

        // TASK: Create non empty Optional
        Optional<Person> optPerson = Optional.of(person);

        // TASK: get Optional<first name> from optPerson
        Optional<String> optFirstName = Optional.of(optPerson.get().getFirstName());

        assertEquals(person.getFirstName(), optFirstName.get());

        // TASK: Create stream with a single element
        Stream<Person> streamPerson = Stream.of(person);

        // TASK: Get Stream<first name> from streamPerson
        Stream<String> streamFirstName = streamPerson.map(Person::getFirstName);

        assertEquals(person.getFirstName(), streamFirstName.collect(toList()).get(0));

        // TASK: Create completed CompletableFuture
        CompletableFuture<Person> futurePerson = CompletableFuture.completedFuture(person);

        // TASK: Get CompletableFuture<first name> from futurePerson
        CompletableFuture<String> futureFirstName = futurePerson.thenApply(Person::getFirstName);

        assertEquals(person.getFirstName(), futureFirstName.get());
    }

    @Test
    public void flatMap() throws ExecutionException, InterruptedException {
        Person person = employeeDb.get(keys.get(0)).thenApply(Employee::getPerson).get();

        // TASK: Create non empty Optional
        Optional<Person> optPerson = Optional.of(person);

        // TASK: Using flatMap and .getFirstName().codePoints().mapToObj(p -> p).findFirst()
        // TASK: get the first letter of first name if any
        Optional<Integer> optFirstCodePointOfFirstName =
                optPerson.flatMap(p -> p.getFirstName().codePoints().mapToObj(o -> o).findFirst());

        assertEquals(Integer.valueOf(65), optFirstCodePointOfFirstName.get());

        // TASK: Create stream with a single element
        Stream<Person> streamPerson = Stream.of(person);

        // TASK: Using flatMapToInt and .getFirstName().codePoints() get codepoints stream from streamPerson
        IntStream codePoints = streamPerson.flatMapToInt(p -> p.getFirstName().codePoints());

        int[] codePointsArray = codePoints.toArray();
        assertEquals(person.getFirstName(), new String(codePointsArray, 0, codePointsArray.length));

        // TASK: Create completed CompletableFuture
        CompletableFuture<Person> futurePerson = CompletableFuture.completedFuture(person);

        // TASK: Get CompletableFuture<Employee> from futurePerson using getKeyByPerson and employeeDb
        CompletableFuture<Employee> futureEmployee = futurePerson
                .thenApply(CompletableFutureBasics::getKeyByPerson)
                .thenCompose(employeeDb::get);

        assertEquals(person, futureEmployee.thenApply(Employee::getPerson).get());
    }
}
