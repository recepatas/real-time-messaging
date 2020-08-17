package org.realtimemessaging.controller.validation;

import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.Event;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EventValidator implements ConstraintValidator<EventConstraint, Event> {

    @Override
    public void initialize(EventConstraint eventConstraint) {
    }

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext cxt) {
        if(event.getCustomerAddress() == null || event.getActionType() == null)
            return false;

        String phoneNumberRgx = "^\\d{10,14}$";
        String emailRgx = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        String alphanumRgx = "(?i)^[A-Z0-9]+$";

        switch (event.getActionType()) {
            case ActionType.EMAIL:
                return Pattern.matches(emailRgx, event.getCustomerAddress());
            case ActionType.SMS:
                return Pattern.matches(phoneNumberRgx, event.getCustomerAddress());
            case ActionType.PUSH:
                return Pattern.matches(alphanumRgx, event.getCustomerAddress());
            default:
                return false;
        }
    }

}