package com.victor.backend.projects.orderSystem.annotation;

import com.victor.backend.projects.orderSystem.Validator.CheckStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckStringValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckString {
    String[] checkingValues();
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
