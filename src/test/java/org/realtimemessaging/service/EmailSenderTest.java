package org.realtimemessaging.service;

import org.junit.Test;
import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.Event;

public class EmailSenderTest {

    private EmailSender emailSender = new EmailSender();

    @Test
    public void should_send_email() {
        Event testEvent = new Event("recepatas@gmail.com", "information text", ActionType.EMAIL);
        emailSender.send(testEvent);
    }

}
