package com.victor.backend.projects.orderSystem.Validator;

import com.victor.backend.projects.orderSystem.annotation.IsIntegerInRange;
import com.victor.backend.projects.orderSystem.util.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;
import java.util.Optional;

public class IsIntegerInRangeValidator implements
        ConstraintValidator<IsIntegerInRange, String> {

    private Optional<BigInteger> min = Optional.empty();
    private Optional<BigInteger> max = Optional.empty();

    @Override
    public void initialize(IsIntegerInRange isIntegerInRange) {
        min = StringUtil.isNumberic(isIntegerInRange.min())
                ? Optional.of(new BigInteger(isIntegerInRange.min()))
                : Optional.empty();
        max = StringUtil.isNumberic(isIntegerInRange.max())
                ? Optional.of(new BigInteger(isIntegerInRange.max()))
                : Optional.empty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        boolean result = StringUtil.isInt(value);
        if (result) {
            BigInteger bd = new BigInteger(value);

            if (min.isPresent()) {
                result = bd.compareTo(min.get()) > -1;
            }
            if (max.isPresent()) {
                result = result && bd.compareTo(max.get()) < 1;
            }
        }

        return result;
    }
}
