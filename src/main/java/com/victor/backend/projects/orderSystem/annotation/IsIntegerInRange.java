package com.victor.backend.projects.orderSystem.annotation;

import com.victor.backend.projects.orderSystem.Validator.IsIntegerInRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsIntegerInRangeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsIntegerInRange {
    String min();
    String max();
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
