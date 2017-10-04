package lambda.part1.example;

import data.Person;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

@SuppressWarnings("Convert2MethodRef")
public class Lambdas05 {
    private <T> void printResult(T t, Function<T, String> f) {
        System.out.println(f.apply(t));
    }

    private final Person person = new Person("John", "Galt", 33);

    @Test
    public void printField() {
        // person неявно (это реализовано в JAVA 8) выступает первым параметром в Function<T, String> (неявное this)
        // именно объект person передается в виде this-объекта в getLastName
        // функцию можно представить в виде public String getLastName(Person this) - т.е. туда передается this
        // в данном случае в роли this выступает person, который является первым параметром printResult(...)
        printResult(person, Person::getLastName);
        // тоже самое развернуто:
//        printResult(person, new Function<Person, String>() {
//            @Override
//            public String apply(Person person) {
//                return person.getLastName();
//            }
//        });
//        BiFunction<Person, String, Person> changeFirstName = Person::withFirstName;
//        printResult(changeFirstName.apply(person, "newName"), Person::getFirstName);
    }

    private static class PersonHelper {
        public static String stringRepresentation(Person person) {
            return person.toString();
        }
    }

    @Test
    public void printStringRepresentation() {
        // пример статика и нестатика с неявным this
        printResult(person, PersonHelper::stringRepresentation);
        printResult(person, Person::toString);
    }

    // нельзя добавлять проверяемые исключения в сигнатуру переопределенного метода,
    // которые не выбрасываются методом в родителе/интерфейсе
    @Test
    public void exception() {
        Runnable r = () -> {
            //Thread.sleep(100);
            person.print();
        };
        r.run();
    }

    @FunctionalInterface
    private interface DoSomething {
        void doSmth();
    }

    private void conflict(Runnable r) {
        System.out.println("Runnable");
        r.run();
    }

    private void conflict(DoSomething d) {
        System.out.println("DoSomething");
        d.doSmth();
    }

    private String printAndReturn() {
        person.print();
        return person.toString();
    }

    @Test
    public void callConflict() {
        //conflict(this::printAndReturn);
        conflict((DoSomething) this::printAndReturn);
        conflict((Runnable) this::printAndReturn);
    }

    class ComparatorPersons implements Comparator<Person>, Serializable {
        public int compare(Person o1, Person o2) {
            return o1.getAge() - o2.getAge();
        }
    }

    @Test
    public void serializeTree() {
        //падаем, компаратор не сериалайзбл
//        Set<Person> treeSet = new TreeSet<>(new Comparator<Person>() {
//            @Override
//            public int compare(Person o1, Person o2) {
//                return Integer.compare(o1.getAge(), o2.getAge());
//            }
//        });
        // падаем, поляснения ЗАВТРА
        //Set<Person> treeSet = new TreeSet<>(new ComparatorPersons());
        // можно добавить маркерный интерфейс к интерфейсу, который формирует лямбду
        Set<Person> treeSet = new TreeSet<>((Comparator<Person> & Serializable) (o1, o2) -> o1.getAge() - o2.getAge());
        treeSet.add(new Person("b", "b", 2));
        treeSet.add(new Person("a", "a", 1));
        treeSet.add(new Person("c", "c", 3));
        System.out.println(treeSet);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
            stream.writeObject(treeSet);
            System.out.println(new String(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    private interface PersonFactory {
        Person create(String name, String lastName, int age);
    }

    private void withFactory(PersonFactory pf) {
        pf.create("name", "lastName", 33).print();
    }

    @Test
    public void factory() {
        // * фабричный метод createPerson в Person, который заменяется лямбдой для конструктора
        // constructor-reference lambda
        withFactory(Person::new);
    }
}
