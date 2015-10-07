package ch.afterglowing.doit.business.reminders.boundary;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceClient;

import static com.jayway.restassured.path.json.JsonPath.from;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ben on 24.09.15.
 */
public class TodosResourceIT {

    private String baseUri = "http://localhost:8080/doit/api/todos";
    private Client client;

    @Before
    public void setup() {
        client = ClientBuilder.newClient();
    }

    @After
    public void teardown() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    public void crud() throws Exception {
        // add Todo
        JsonObjectBuilder todoBuilder = Json.createObjectBuilder();
        JsonObject buildedTodo = todoBuilder.add("caption", "Implement REST Service").add("priority", 9).build();
        Client client = ClientBuilder.newClient();
        Response postReponse = client.target(baseUri).request().post(Entity.json(buildedTodo));
        assertThat(postReponse.getStatus(), is(201));
        String location = postReponse.getHeaderString("Location");
        postReponse.close();

        // find all Todos
        Response response = client.target(baseUri).request(APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(200));
        JsonArray allTodos = response.readEntity(JsonArray.class);
        assertThat(allTodos.size(), is(not(0)));

        // find single Todo
        String todo = client.target(location).request(APPLICATION_JSON).get(String.class);
        // using JsonPath from rest assured to validate more complex JSON responses
        assertThat(from(todo).getString("caption"), containsString("Implement REST Service"));

        // update the Todo
        JsonObject todoToUpdate = todoBuilder.add("caption", "Implemented!").build();
        response = client.target(location).request(APPLICATION_JSON).put(Entity.json(todoToUpdate));
        response.close();
        assertThat(response.getStatus(), is(200));

        // update the Todo again to test Optimistic Locking
        todoToUpdate = todoBuilder.add("priority", 9).build();
        response = client.target(location).request(APPLICATION_JSON).put(Entity.json(todoToUpdate));
        response.close();
        String cause = response.getHeaderString("cause");
        assertThat(response.getStatus(), is(409));
        assertThat(cause, is(notNullValue()));

        // find the Todo again
        JsonObject updatedTodo = client.target(location).request(APPLICATION_JSON).get(JsonObject.class);
        assertThat(updatedTodo.getString("caption"), containsString("Implemented!"));

        // update the Status of the Todo
        JsonObjectBuilder statusBuilder = Json.createObjectBuilder();
        JsonObject todoToUpdateStatus = statusBuilder.add("done", true).build();
        Response putResponse = client.target(location).path("status").request(APPLICATION_JSON).put(Entity.json(todoToUpdateStatus));
        putResponse.close();

        // find the Todo again and verify the status
        updatedTodo = client.target(location).request(APPLICATION_JSON).get(JsonObject.class);
        assertThat(updatedTodo.getBoolean("done"), is(true));

        // update the Status of a non-existing Todo
        response = client.target(baseUri).path("666").path("status").request(APPLICATION_JSON).put(Entity.json(Json.createObjectBuilder().add("done", true).build()));
        response.close();
        assertThat(response.getStatus(), is(400));
        assertThat(response.getHeaderString("reason"), containsString("666"));

        // update the Status of the Todo with a wrong attribute name
        response = client.target(location).path("status").request(APPLICATION_JSON).put(Entity.json(Json.createObjectBuilder().add("wrong-attribute", true).build()));
        response.close();
        assertThat(response.getStatus(), is(400));
        assertThat(response.getHeaderString("reason"), containsString("Attribute done not provided"));

        // delete Todo
        Response deleteResponse = client.target(location).request(APPLICATION_JSON).delete();
        deleteResponse.close();
        assertThat(deleteResponse.getStatus(), is(204));
    }

    @Test
    public void testBeanValidation() throws Exception {
        // mandatory field "caption" is missing
        JsonObjectBuilder todoBuilder = Json.createObjectBuilder();

        Response postReponse = ClientBuilder.newClient().target(baseUri).request().post(Entity.json(todoBuilder.add("priority", 1).build()));
        postReponse.getHeaders().entrySet().forEach(System.out::println);
        assertThat(postReponse.getStatus(), is(400));
    }

    @Test
    public void testBeanValidationCrossCheck() throws Exception {
        // mandatory field "caption" is missing
        JsonObjectBuilder todoBuilder = Json.createObjectBuilder();

        Response postReponse = ClientBuilder.newClient().target(baseUri).request().post(Entity.json(
                todoBuilder.add("priority", 666).add("caption", "Implemented!").build()));
        postReponse.getHeaders().entrySet().forEach(System.out::println);
        assertThat(postReponse.getStatus(), is(400));
    }
}
