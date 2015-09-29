package ch.afterglowing.doit.business.reminders.boundary;

import ch.afterglowing.doit.business.reminders.entity.Todo;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;


/**
 * Created by ben on 24.09.15.
 */
@javax.ejb.Stateless
@Path("todos")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class TodosResource {


    @Inject
    TodoService service;

    @Path("{id}")
    public TodoResource find(@PathParam("id") long id) {
        return new TodoResource(id, service);
    }

    @GET
    public List<Todo> findAll() {
        return service.findAll();
    }

    @POST
    public Response save(@Valid Todo todo, @Context UriInfo uriInfo) {
        Todo saved = service.save(todo);
        URI build = uriInfo.getAbsolutePathBuilder().path("/" + saved.getId()).build();
        return Response.created(build).build();
    }
}