package data.raw;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Person {
    private final String firstName;
    private final String lastName;
    private final int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public Person withFirstName(String firstName) {
        return new Person(firstName, lastName, age);
    }

    public Person withLastName(String lastName) {
        return new Person(firstName, lastName, age);
    }

    public Person withAge(int age) {
        return new Person(firstName, lastName, age);
    }

    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "Person{firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", age=" + age + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return new EqualsBuilder()
                .append(age, person.age)
                .append(firstName, person.firstName)
                .append(lastName, person.lastName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(firstName)
                .append(lastName)
                .append(age)
                .toHashCode();
    }
}
