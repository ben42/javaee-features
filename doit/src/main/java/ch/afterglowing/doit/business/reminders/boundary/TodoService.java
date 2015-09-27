package ch.afterglowing.doit.business.reminders.boundary;

import ch.afterglowing.doit.business.auditing.boundary.BoundaryLogger;
import ch.afterglowing.doit.business.reminders.entity.Todo;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by ben on 25.09.15.
 */
@Stateless
@Interceptors(BoundaryLogger.class)
public class TodoService {

    @PersistenceContext
    EntityManager em;

    public Todo findById(long id) {
        System.out.println("Finding Todo with id = " + id);
        return em.find(Todo.class, id);
    }

    public List<Todo> findAll() {
        System.out.println("Finding all Todos...");
        return em.createNamedQuery(Todo.findAll, Todo.class).getResultList();
    }

    public Todo save(Todo todo) {
        System.out.println("Saving todo = " + todo);
        return em.merge(todo);
    }

    public void delete(long id) {
        System.out.println("Deleting Todo with id = " + id);
        try {
            Todo todo = em.getReference(Todo.class, id);
            em.remove(todo);
        } catch (EntityNotFoundException e) {
            // possible state
        }
    }

    public Todo updateStatus(long id, boolean status) {
        Todo todo = findById(id);
        if (todo != null) {
            todo.setDone(status);
        }
        return todo;
    }
}
