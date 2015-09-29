package ch.afterglowing.doit.business.reminders.boundary;

import ch.afterglowing.doit.business.encoders.JsonEncoder;
import ch.afterglowing.doit.business.reminders.entity.Todo;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static javax.enterprise.event.TransactionPhase.AFTER_SUCCESS;

/**
 * Created by ben on 27.09.15.
 *
 * Gather events and exposes them via websockets.
 */
@Singleton
@ServerEndpoint(value = "/changes", encoders = JsonEncoder.class)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TodoChangeTracker {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnClose
    public void onClose() {
        this.session = null;
    }

    // triggered only for successful updates
    public void onTodoChange(@Observes(during = AFTER_SUCCESS) @ChangeEvent(ChangeEvent.Type.CREATION) Todo todo) {
        if (session != null && session.isOpen()) {
            try {
                JsonObject jsonObject = Json.createObjectBuilder()
                        .add("id", todo.getId())
                        .add("event", "creation").build();
                session.getBasicRemote().sendObject(jsonObject);
            } catch (IOException e) {
                // safely ignore it
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }
}