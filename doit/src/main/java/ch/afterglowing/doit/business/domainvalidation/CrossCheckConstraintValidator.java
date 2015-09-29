package ch.afterglowing.doit.business.domainvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by ben on 27.09.15.
 */
public class CrossCheckConstraintValidator implements ConstraintValidator<CrossCheck, ValidEntity> {

    @Override
    public void initialize(CrossCheck priorityConstraint) {

    }

    @Override
    public boolean isValid(ValidEntity entity, ConstraintValidatorContext constraintValidatorContext) {
        return entity.isValid();
    }
}
