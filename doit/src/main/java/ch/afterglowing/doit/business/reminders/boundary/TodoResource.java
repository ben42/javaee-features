package ch.afterglowing.doit.business.reminders.boundary;

import ch.afterglowing.doit.business.reminders.entity.Todo;

import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * Created by ben on 25.09.15.
 *
 * REST Subresource (missing @Path on class definition)
 */
public class TodoResource {

    long id;
    TodoService service;

    public TodoResource(long id, TodoService service) {
        this.id = id;
        this.service = service;
    }

    @GET
    public Todo find() {
        return service.findById(id);
    }


    @DELETE
    public void delete() {
        service.delete(id);
    }

    @PUT
    @Path("status")
    public Response statusUpdate(JsonObject statusUpdate) {
        if (!statusUpdate.containsKey("done")) {
            return Response.status(BAD_REQUEST).header("reason", "Attribute done not provided").build();
        }
        boolean done = statusUpdate.getBoolean("done");
        System.out.println("Setting Status of Todo with id = " + id + " to " + statusUpdate);
        Todo todo = service.updateStatus(id, done);
        if (todo == null) {
            return Response.status(BAD_REQUEST).header("reason", "todo with id " + id + " does not exist").build();
        }
        return Response.ok(todo).build();
    }

    @PUT
    public Todo update(Todo todo) {
        System.out.println("Updating Todo with id = " + id);
        todo.setId(id);
        return service.save(todo);
    }
}
