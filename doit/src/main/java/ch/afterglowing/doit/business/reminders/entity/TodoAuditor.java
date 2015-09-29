package ch.afterglowing.doit.business.reminders.entity;

import ch.afterglowing.doit.business.reminders.boundary.ChangeEvent;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

/**
 * Created by ben on 27.09.15.
 */
public class TodoAuditor {


    // does not work under wildfly due to bug WFLY-2387
    @Inject
    @ChangeEvent(ChangeEvent.Type.CREATION)
    Event<Todo> create;


    // does not work under wildfly due to bug WFLY-2387
    @Inject
    @ChangeEvent(ChangeEvent.Type.UPDATE)
    Event<Todo> update;

    // @Inject
    // BeanManager beanManager;

    @PostPersist
    public void onPersist(Todo todo) {
        System.out.println("Persisting " + todo);
        create.fire(todo);
    }

    @PostUpdate
    public void onUpdate(Todo todo) {
        System.out.println("Updating " + todo);
        update.fire(todo);
    }
}
