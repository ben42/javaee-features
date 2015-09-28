package ch.afterglowing.doit.business.reminders.boundary;

import ch.afterglowing.doit.business.reminders.entity.Todo;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static javax.enterprise.event.TransactionPhase.AFTER_SUCCESS;

/**
 * Created by ben on 27.09.15.
 */
@Singleton
@ServerEndpoint("/changes")
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

    // triggered only by successful updates
    public void onTodoChange(@Observes(during = AFTER_SUCCESS) Todo todo) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(todo.toString());
            } catch (IOException e) {
                // safely ignore it
            }
        }
    }
}