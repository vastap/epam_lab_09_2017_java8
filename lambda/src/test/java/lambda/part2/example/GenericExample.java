package lambda.part2.example;

import data.Person;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GenericExample {
    @Test
    public void genericExamples() {

//        Integer i = 42;
        //Class<Integer> intClass = i.getClass();
        // так правильно, т.к. возвращает Integer и его наследников, неважно, что он final
//        Class<? extends Integer> intClass = i.getClass();
//        i.getClass();

//        List list = new ArrayList();
//        list.add("1");
//
//        List<String> list = new ArrayList();
//        list.add("1");
//
//        List<String> hardCheckedList = Collections.checkedList(list, String.class);


        // чекд коллекция не даст положить элемент другого типа
//        List rawReference = hardCheckedList;
//        rawReference.add(1);
//        System.out.println(list.get(1));

        // сырая коллекция даст положить элемент другого типа, упадет при изъятии и приведения типа
//        List rawReference = list;
//        rawReference.add(1);
//        System.out.println(list.get(1));

        // стирание типов!!


        FunctionCombination.MyClass ref = new FunctionCombination.InheritedMyClass();
        Class<? extends FunctionCombination.MyClass> myClassClazz = ref.getClass();
        Class<? extends Integer> integerClazz = Integer.valueOf(10).getClass();
        Class<FunctionCombination.MyClass> reallyIntegerClazz = FunctionCombination.MyClass.class;

        List<String> list = new ArrayList<>();
        List<Integer> listIntegers = new ArrayList<>();
        list.add("1");

        String val = (String) list.get(0);

        List rawReference = list;
        rawReference.add(new FunctionCombination.MyClass());

        System.out.println(list.size());

        List<String> hardCheckedList = Collections.checkedList(list, String.class);

        List rawReference2 = hardCheckedList;
//        rawReference2.add(new MyClass());  <- Упадем с ClassCastException

        System.out.println(list.size());

//        hardCheckedList

        for (Method method : Person.class.getDeclaredMethods()) {
            System.out.println(method);
        }
        try {
            Method compareToPerson = Person.class.getMethod("compareTo", Person.class); // синтетический метод, bridge
            Method compareToObject = Person.class.getMethod("compareTo", Object.class);
            System.out.println(compareToPerson.isSynthetic());
            System.out.println(compareToObject.isSynthetic());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Person.compareTo both methods has same erasure
        // GenericException

        // Эффекты Стирания Типа заключаются в том, что все дженерики в коде выглядят как типизированные Object

//        List<String>[] arrayOfLists = new List<String>[10];
        Integer[] integers = new Integer[10];
        integers[0] = 100;

        Number[] numbers = integers;
//        numbers[1] = 200d; <- ArrayStoreException

        Number numVal = 10;
//        List<Object> objectList = new ArrayList<String>();

        // <? extends T> компилятор не может в эту коллекцию положить что-то, т.к. не может гарантировать, что добавляется допустимый тип
        // в такую коллекцию можно положить только null, из нее можно получать объекты типа T
        List<? extends Number> listNumbers = new ArrayList<Double>();
//        Integer numberVal = listNumbers.get(0); - нельзя, т.к. возвращать может только ссылки NUMBER и его родителей
        Number numberVal = listNumbers.get(0);// - пустой лист
        // listNumbers.add(1);
        listNumbers.add(null);

        while (true) {
            try {
                Integer a = new Integer(300);
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (false) {
                break;
            }
        }

        // <? super T> компилятор позволяет класть в такую коллекцию любых типов класса T и его подклассов
        // гарантируется возвращение объекта только как тип Object
        List<? super Number> listSuperNumbers = new ArrayList<Object>();
        listSuperNumbers.add(1);
        listSuperNumbers.add(1L);
        listSuperNumbers.add(1d);
        listSuperNumbers.add(1f);
        listSuperNumbers.add((short) 1);

        // Number obj = listSuperNumbers.get(0);
        // Array generics
        // Raw types list (Int -> Double)


        //Producer - ? extends T -- collection in Collection.max()
        //Extends
        //Consumer - ? super T -- comparator in Collection.max()
        //Super
        List<Integer> intList = Arrays.asList(1, 22, 4);
        Comparator<Integer> intComparator = Integer::compare;
        Comparator<Object> objectComparator = (o1, o2) -> 0;
        int maxValue = findMax(intList, objectComparator);
        Number[] numberArray = new Integer[10];
        List<String> first = new ArrayList<>(Arrays.asList("1", "2"));
        List<String> second = new ArrayList<>(Arrays.asList("a", "b"));
        varArgsMethod(first, second);
        //Array generics
        //Raw types list (Int -> Double)
    }

    public static void varArgsMethod(List<String>... lists) {
        Object[] objects = lists;
        objects[0] = new ArrayList<>(Arrays.asList(100, 200, 300));
        //String str = lists[0].get(0); - падаем
        Object str = lists[0].get(0); // обманули
        System.out.println(str);
    }

    // нужно делать как ниже, иначе мы строго типизируем все
//    public static <T> T findMax(List<T> list, Comparator<T> comparator) {
//        return null;
//    }

    // так правильно
    public static <T> T findMax(List<? extends T> list, Comparator<? super T> comparator) {
        return null;
    }

    class MyException extends RuntimeException {

    }
}
