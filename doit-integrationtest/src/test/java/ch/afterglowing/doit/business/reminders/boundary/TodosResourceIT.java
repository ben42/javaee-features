package ch.afterglowing.doit.business.reminders.boundary;

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ben on 24.09.15.
 */
public class TodosResourceIT {

    private String baseUri = "http://localhost:8081/doit/api/todos";

    private WebTarget target(String uri) {
        // TODO close the client instances
        return ClientBuilder.newClient().target(uri);
    }

    @Test
    public void crud() throws Exception {
        // add Todo
        JsonObjectBuilder todoBuilder = Json.createObjectBuilder();
        JsonObject buildedTodo = todoBuilder.add("caption", "Implement REST Service").add("priority", 9).build();
        Response postReponse = target(baseUri).request().post(Entity.json(buildedTodo));
        assertThat(postReponse.getStatus(), is(201));
        String location = postReponse.getHeaderString("Location");
        System.out.println("Got Location back for newly created Todo: " + location);

        // find all Todos
        Response response = target(baseUri).request(APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        JsonArray allTodos = response.readEntity(JsonArray.class);
        assertThat(allTodos.size(), is(not(0)));

        // find single Todo
        JsonObject todo = target(location).request(APPLICATION_JSON).get(JsonObject.class);
        assertThat(todo.getString("caption"), containsString("Implement REST Service"));

        // update the Todo
        JsonObject todoToUpdate = todoBuilder.add("caption", "Implemented!").build();
        response = target(location).request(APPLICATION_JSON).put(Entity.json(todoToUpdate));
        assertThat(response.getStatus(), is(200));

        // update the Todo again to test Optimistic Locking
        todoToUpdate = todoBuilder.add("priority", 9).build();
        response = target(location).request(APPLICATION_JSON).put(Entity.json(todoToUpdate));
        String cause = response.getHeaderString("cause");
        System.out.println(cause);
        assertThat(response.getStatus(), is(409));
        assertThat(cause, is(notNullValue()));

        // find the Todo again
        JsonObject updatedTodo = target(location).request(APPLICATION_JSON).get(JsonObject.class);
        assertThat(updatedTodo.getString("caption"), containsString("Implemented!"));

        // update the Status of the Todo
        JsonObjectBuilder statusBuilder = Json.createObjectBuilder();
        JsonObject todoToUpdateStatus = statusBuilder.add("done", true).build();
        target(location).path("status").request(APPLICATION_JSON).put(Entity.json(todoToUpdateStatus));

        // find the Todo again and verify the status
        updatedTodo = target(location).request(APPLICATION_JSON).get(JsonObject.class);
        assertThat(updatedTodo.getBoolean("done"), is(true));

        // update the Status of a non-existing Todo
        response = target(baseUri).path("666").path("status").request(APPLICATION_JSON).put(Entity.json(Json.createObjectBuilder().add("done", true).build()));
        assertThat(response.getStatus(), is(400));
        assertThat(response.getHeaderString("reason"), containsString("666"));

        // update the Status of the Todo with a wrong attribute name
        response = target(location).path("status").request(APPLICATION_JSON).put(Entity.json(Json.createObjectBuilder().add("wrong-attribute", true).build()));
        assertThat(response.getStatus(), is(400));
        assertThat(response.getHeaderString("reason"), containsString("Attribute done not provided"));

        // delete Todo
        Response deleteResponse = target(location).request(APPLICATION_JSON).delete();
        assertThat(deleteResponse.getStatus(), is(204));
    }

    @Test
    public void testBeanValidation() throws Exception {
        // mandatory field "caption" is missing
        JsonObjectBuilder todoBuilder = Json.createObjectBuilder();

        Response postReponse = target(baseUri).request().post(Entity.json(todoBuilder.add("priority", 1).build()));
        postReponse.getHeaders().entrySet().forEach(System.out::println);
        assertThat(postReponse.getStatus(), is(400));
    }

    @Test
    public void testBeanValidationCrossCheck() throws Exception {
        // mandatory field "caption" is missing
        JsonObjectBuilder todoBuilder = Json.createObjectBuilder();

        Response postReponse = target(baseUri).request().post(Entity.json(
                todoBuilder.add("priority", 666).add("caption", "Implemented!").build()));
        postReponse.getHeaders().entrySet().forEach(System.out::println);
        assertThat(postReponse.getStatus(), is(400));
    }
}
