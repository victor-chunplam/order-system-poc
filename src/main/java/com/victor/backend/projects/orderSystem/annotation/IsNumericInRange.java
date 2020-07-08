package com.victor.backend.projects.orderSystem.annotation;

import com.victor.backend.projects.orderSystem.Validator.IsNumbericInRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsNumbericInRangeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNumericInRange {
    String min();
    String max();
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
