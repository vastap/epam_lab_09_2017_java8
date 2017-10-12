package part1.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
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
                        .collect(Collectors.toMap(JobHistoryEntry::getEmployer,
                                (x) -> e.getPerson())).entrySet()
                        .stream()).distinct()
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));// TODO
        System.out.println(employersStuffLists);

//     in employersStuffLists:
//          {abc=
//            [Person-2068529915:{firstName='John', lastName='Doe', age=21},
//            Person-2068529912:{firstName='John', lastName='Doe', age=24},
//            Person66518773:{firstName='Bob', lastName='Doe', age=27},
//            Person-2068529906:{firstName='John', lastName='Doe', age=30}],
//            yandex=
//            [Person-2068529915:{firstName='John', lastName='Doe', age=21},
//            Person-2068529912:{firstName='John', lastName='Doe', age=24},
//            Person66518773:{firstName='Bob', lastName='Doe', age=27},
//            Person-2068529906:{firstName='John', lastName='Doe', age=30}],
//            epam=
//            [Person-2002098404:{firstName='John', lastName='Galt', age=20},
//            Person-2068529915:{firstName='John', lastName='Doe', age=21},
//            Person519359479:{firstName='John', lastName='White', age=22},
//            Person-2002098401:{firstName='John', lastName='Galt', age=23},
//            Person-2068529912:{firstName='John', lastName='Doe', age=24},
//            Person519359482:{firstName='John', lastName='White', age=25},
//            Person-2002098398:{firstName='John', lastName='Galt', age=26},
//            Person66518773:{firstName='Bob', lastName='Doe', age=27},
//            Person519359485:{firstName='John', lastName='White', age=28},
//            Person-2002098395:{firstName='John', lastName='Galt', age=29},
//            Person-2068529906:{firstName='John', lastName='Doe', age=30},
//            Person-1640559126:{firstName='Bob', lastName='White', age=31}],
//            google=
//            [Person-2002098404:{firstName='John', lastName='Galt', age=20},
//            Person-2002098401:{firstName='John', lastName='Galt', age=23},
//            Person-2002098398:{firstName='John', lastName='Galt', age=26},
//            Person-2002098395:{firstName='John', lastName='Galt', age=29}]}

        throw new UnsupportedOperationException();
    }

    @Test
    public void indexByFirstEmployer() {
        Map<String, List<Person>> employeesIndex = null;



        throw new UnsupportedOperationException();
    }

    @Test
    public void greatestExperiencePerEmployer() {
        Map<String, Person> employeesIndex = null;// TODO

        assertEquals(new Person("John", "White", 28), employeesIndex.get("epam"));
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
