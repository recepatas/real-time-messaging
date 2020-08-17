package org.realtimemessaging.controller.validation;

import org.junit.Test;
import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.Event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventValidatorTest {

    EventValidator eventValidator = new EventValidator();

    @Test
    public void should_return_false_when_customer_address_is_null() {
        assertFalse(
                eventValidator.isValid(new Event(null, "test", ActionType.SMS), null)
        );
    }

    @Test
    public void should_return_false_when_action_type_is_null() {
        assertFalse(
                eventValidator.isValid(new Event("05418173433", "test", null), null)
        );
    }

    @Test
    public void should_return_true_for_valid_phone_number() {
        assertTrue(
                eventValidator.isValid(new Event("05418173433", "test", ActionType.SMS), null)
        );
    }

    @Test
    public void should_return_false_for_incomplete_phone_number() {
        assertFalse(
                eventValidator.isValid(new Event("8173433", "test", ActionType.SMS), null)
        );
    }

    @Test
    public void should_return_false_for_invalid_phone_number() {
        assertFalse(
                eventValidator.isValid(new Event("invalid", "test", ActionType.SMS), null)
        );
    }

    @Test
    public void should_return_true_for_valid_email() {
        assertTrue(
                eventValidator.isValid(new Event("recepatas@gmail.com", "test", ActionType.EMAIL), null)
        );
    }

    @Test
    public void should_return_true_for_invalid_email() {
        assertFalse(
                eventValidator.isValid(new Event("recepatas@", "test", ActionType.EMAIL), null)
        );
    }

    @Test
    public void should_return_true_for_valid_mobile_id() {
        assertTrue(
                eventValidator.isValid(new Event("mobilecellid123", "test", ActionType.PUSH), null)
        );
    }

    @Test
    public void should_return_true_for_invalid_mobile_id() {
        assertFalse(
                eventValidator.isValid(new Event("mobilecellid_123", "test", ActionType.PUSH), null)
        );
    }


}
