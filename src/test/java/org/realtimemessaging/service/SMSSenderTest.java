package org.realtimemessaging.service;

import org.junit.Test;
import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.Event;

public class SMSSenderTest {

    private SMSSender smsSender = new SMSSender();

    @Test
    public void should_send_sms() {
        Event testEvent = new Event("05418173433", "information text", ActionType.SMS);
        smsSender.send(testEvent);
    }

}
