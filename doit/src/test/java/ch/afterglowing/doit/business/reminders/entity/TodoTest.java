package ch.afterglowing.doit.business.reminders.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by ben on 27.09.15.
 */
public class TodoTest {

    @Test
    public void validTodo() throws Exception {
        Todo valid = new Todo("", "available", 11);
        assertThat(valid.isValid(), is(true));
    }

    @Test
    public void invalidTodo() throws Exception {
        Todo valid = new Todo("", null, 11);
        assertThat(valid.isValid(), is(false));
    }
}