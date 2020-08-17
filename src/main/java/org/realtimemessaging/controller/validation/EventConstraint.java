package org.realtimemessaging.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventConstraint {
    String message() default "Customer address for given action type is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
