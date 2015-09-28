package ch.afterglowing.doit.business.reminders.entity;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;

/**
 * Created by ben on 27.09.15.
 */
public class TodoAuditor {

    // @Inject
    // BeanManager beanManager;

    // does not work under wildfly due to bug WFLY-2387
    // @Inject
    Event<Todo> todoUpdated;

    @PostPersist
    public void onTodoUpdate(Todo todo) {
        System.out.println("Updating " + todo);

        //todoUpdated.fire(todo);
    }
}
