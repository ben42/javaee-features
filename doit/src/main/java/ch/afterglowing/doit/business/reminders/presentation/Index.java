package ch.afterglowing.doit.business.reminders.presentation;

import ch.afterglowing.doit.business.reminders.boundary.TodoService;
import ch.afterglowing.doit.business.reminders.entity.Todo;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Created by ben on 27.09.15.
 */
@Model
public class Index {

    @Inject
    TodoService service;

    @Inject
    Validator validator;

    Todo todo;

    @PostConstruct
    public void init() {
        this.todo = new Todo();
    }

    public Todo getTodo() {
        return todo;
    }

    public Object save() {
        Set<ConstraintViolation<Todo>> violations = validator.validate(todo);
        for (ConstraintViolation<Todo> violation : violations) {
            showValidationError(violation.getMessage());
        }

        if(violations.isEmpty()) {
            service.save(todo);
        }
        return null;
    }

    public void showValidationError(String content) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, content, content);
        FacesContext.getCurrentInstance().addMessage("", message);
    }
}
