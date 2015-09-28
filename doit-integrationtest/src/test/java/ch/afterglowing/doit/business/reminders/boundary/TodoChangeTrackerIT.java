package ch.afterglowing.doit.business.reminders.boundary;

import org.junit.Before;
import org.junit.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ben on 27.09.15.
 */
public class TodoChangeTrackerIT {

    WebSocketContainer container;
    ChangesListener listener;

    @Before
    public void init() throws URISyntaxException, IOException, DeploymentException {
        container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://localhost:8080/doit/changes");

        listener = new ChangesListener();
        container.connectToServer(listener, uri);
    }

    @Test
    public void shouldReceiveNotification() throws Exception {
        String message = listener.getMessage();
        assertThat(message, is(notNullValue()));
        System.out.println(" " + message);
    }
}
