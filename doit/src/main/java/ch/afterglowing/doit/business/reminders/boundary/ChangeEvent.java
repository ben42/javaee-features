package ch.afterglowing.doit.business.reminders.boundary;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ben on 29.09.15.
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeEvent {

    Type value();

    enum Type {
        CREATION, UPDATE,
    }
}
