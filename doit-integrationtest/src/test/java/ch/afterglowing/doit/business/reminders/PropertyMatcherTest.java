package ch.afterglowing.doit.business.reminders;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ben on 07.10.15.
 */
public class PropertyMatcherTest {

    @Test
    public void shouldMatchWithLambda() throws Exception {
        List<Person> persons = Arrays.asList(new Person("Foo"), new Person("Bar"));
        persons.forEach(System.out::println);
        assertThat(persons, hasItem(hasProperty("name", is("Foo"))));
    }

    public static class Person {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Person(String name) {
            this.name = name;
        }
    }
}