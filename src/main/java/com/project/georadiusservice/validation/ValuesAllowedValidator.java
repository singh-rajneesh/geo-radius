package com.project.georadiusservice.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValuesAllowedValidator implements ConstraintValidator<ValuesAllowed, List<String>> {

    private List<String> expectedValues;
    private String returnMessage;

    @Override
    public void initialize(ValuesAllowed requiredIfChecked) {
        expectedValues = Arrays.asList(requiredIfChecked.values());
        returnMessage = requiredIfChecked.message().concat(expectedValues.toString());
    }

    @Override
    public boolean isValid(List<String> testValues, ConstraintValidatorContext context) {
        List<Boolean> isValidValues = testValues.stream().map(testValue -> expectedValues.contains(testValue))
                .collect(Collectors.toList());
        boolean isValid = isValidValues.stream().noneMatch(Boolean.FALSE::equals);


        if (Boolean.FALSE.equals(isValid)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(returnMessage)
                    .addConstraintViolation();
        }
        return isValid;
    }
}