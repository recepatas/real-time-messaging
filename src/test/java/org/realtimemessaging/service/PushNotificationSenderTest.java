package org.realtimemessaging.service;

import org.junit.Test;
import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.Event;

public class PushNotificationSenderTest {

    private PushNotificationSender pushNotificationSender = new PushNotificationSender();

    @Test
    public void should_send_push() {
        Event testEvent = new Event("mobileid", "information text", ActionType.PUSH);
        pushNotificationSender.send(testEvent);
    }

}
