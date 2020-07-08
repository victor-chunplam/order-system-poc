package com.victor.backend.projects.orderSystem.Validator;

import com.victor.backend.projects.orderSystem.annotation.CheckString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CheckStringValidator implements
        ConstraintValidator<CheckString, String> {

    private List<String> validVals = Collections.emptyList();

    @Override
    public void initialize(CheckString checkOrderStatus) {
        validVals = Arrays.asList(checkOrderStatus.checkingValues());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;

        return validVals.contains(value);
    }
}
