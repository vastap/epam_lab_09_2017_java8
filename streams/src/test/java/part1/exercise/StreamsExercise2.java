package part1.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import jdk.nashorn.internal.objects.annotations.Function;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static data.Generator.generateEmployeeList;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

public class StreamsExercise2 {
    // https://youtu.be/kxgo7Y4cdA8 Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 1
    // https://youtu.be/JRBWBJ6S4aU Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 2

    // https://youtu.be/O8oN4KSZEXE Сергей Куксенко — Stream API, часть 1
    // https://youtu.be/i0Jr2l3jrDA Сергей Куксенко — Stream API, часть 2

    // TODO class PersonEmployerPair

    @Test
    public void employersStuffLists() {
        Map<String, List<Person>> employersStuffLists =
                getEmployees().stream()
                .flatMap(e -> e.getJobHistory().stream()
                        .collect(toMap(JobHistoryEntry::getEmployer,
                                (x) -> e.getPerson(), (x, y)-> x)).entrySet()
                        .stream())
                .collect(groupingBy(Map.Entry::getKey,
                        mapping(Map.Entry::getValue, toList())));// TODO
        System.out.println(employersStuffLists);

        throw new UnsupportedOperationException();
    }

    @Test
    public void indexByFirstEmployer() {
        Map<String, List<Person>> employeesIndex =
                getEmployees().stream()
                .flatMap(e -> e.getJobHistory().stream().limit(1)
                        .collect(toMap(JobHistoryEntry::getEmployer,
                                (x) -> e.getPerson(), (x, y)-> x)).entrySet()
                        .stream().peek(System.out::println))
                .collect(groupingBy(Map.Entry::getKey,
                        mapping(Map.Entry::getValue, toList())));
        System.out.println(employeesIndex);


//        throw new UnsupportedOperationException();
    }

    @Test
    public void greatestExperiencePerEmployer() {
        Map<String, Person> employeesIndex =
                getEmployees().stream().flatMap(e->
                        e.getJobHistory().stream().collect(groupingBy(JobHistoryEntry::getEmployer,
                                        summingInt(JobHistoryEntry::getDuration)))
                                .entrySet().stream()
                                .map((jhe)->new Triple(e.getPerson(),jhe.getKey(),jhe.getValue()))
                        ).collect(groupingBy(Triple::getEmployer,
                        collectingAndThen(Collectors.maxBy(Comparator.comparing(Triple::getDuration)), (x)-> x.get().getPerson())));


        assertEquals(new Person("John", "White", 28), employeesIndex.get("epam"));
    }

    class Triple {
        final Person person;
        final String employer;
        final int duration;

        public Triple(Person person, String employer, int duration) {
            this.person = person;
            this.employer = employer;
            this.duration = duration;
        }

        public Person getPerson() {
            return person;
        }

        public String getEmployer() {
            return employer;
        }

        public int getDuration() {
            return duration;
        }

        public Triple changeDuration(int newDur){return new Triple(person,employer, newDur); }
    }

    private List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("John", "Galt", 20),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 21),
                        Arrays.asList(
                                new JobHistoryEntry(4, "BA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 22),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 23),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 24),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "BA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 25),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 26),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Bob", "Doe", 27),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 28),
                        Collections.singletonList(
                                new JobHistoryEntry(666, "BA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 29),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 30),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(5, "dev", "abc")
                        )),
                new Employee(
                        new Person("Bob", "White", 31),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        ))
        );
    }

}
