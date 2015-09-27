package ch.afterglowing.doit.business;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by ben on 27.09.15.
 */
@Documented
@Constraint(validatedBy = CrossCheckConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CrossCheck {
    String message() default "Cross Check failed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
