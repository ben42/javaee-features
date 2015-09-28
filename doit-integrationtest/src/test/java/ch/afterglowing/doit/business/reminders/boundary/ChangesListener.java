package ch.afterglowing.doit.business.reminders.boundary;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ben on 27.09.15.
 */
public class ChangesListener extends Endpoint {

    String message;
    CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

        session.addMessageHandler(String.class, s -> {
            message = s;
            latch.countDown();
            System.out.println("Message: " + s);
        });
    }

    public String getMessage() throws InterruptedException {
        latch.await(1, TimeUnit.MINUTES);
        return message;
    }
}
