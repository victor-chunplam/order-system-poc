package com.victor.backend.projects.orderSystem.Validator;

import com.victor.backend.projects.orderSystem.annotation.IsNumericInRange;
import com.victor.backend.projects.orderSystem.util.StringUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Optional;

public class IsNumbericInRangeValidator implements
        ConstraintValidator<IsNumericInRange, String> {

    private Optional<BigDecimal> min = Optional.empty();
    private Optional<BigDecimal> max = Optional.empty();

    @Override
    public void initialize(IsNumericInRange isNumericInRange) {
        min = StringUtil.isNumberic(isNumericInRange.min())
                ? Optional.of(new BigDecimal(isNumericInRange.min()))
                : Optional.empty();
        max = StringUtil.isNumberic(isNumericInRange.max())
                ? Optional.of(new BigDecimal(isNumericInRange.max()))
                : Optional.empty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;

        boolean result = StringUtil.isNumberic(value);
        if(result) {
            BigDecimal bd = new BigDecimal(value);

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
