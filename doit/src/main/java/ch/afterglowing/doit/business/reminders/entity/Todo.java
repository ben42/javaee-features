package ch.afterglowing.doit.business.reminders.entity;

import ch.afterglowing.doit.business.domainvalidation.CrossCheck;
import ch.afterglowing.doit.business.domainvalidation.ValidEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ben on 24.09.15.
 */
@Entity
@NamedQuery(name = Todo.findAll, query = "select t from Todo t")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@CrossCheck
@EntityListeners(TodoAuditor.class)
public class Todo implements ValidEntity {
    public final static String PREFIX = "reminders.entity.Todo.";
    public final static String findAll = PREFIX + "findAll";

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 2, max = 255)
    private String caption;
    private String description;
    private int priority;
    private boolean done;
    @Version
    private long version;

    public Todo() {
    }

    public Todo(String caption, String description, int priority) {
        this.caption = caption;
        this.description = description;
        this.priority = priority;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", done=" + done +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean isValid() {
        if(priority < 10) {
            return true;
        }
        return description != null && !description.isEmpty();
    }
}
