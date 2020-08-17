package org.realtimemessaging.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.realtimemessaging.dto.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventSenderFactoryTest {

    @Autowired
    private EventSenderFactory eventSenderFactory;

    @Test
    public void should_return_email_event_sender() {
        Assert.assertTrue(eventSenderFactory.getEventSender(ActionType.EMAIL) instanceof EmailSender);
    }

    @Test
    public void should_return_email_sms_sender() {
        Assert.assertTrue(eventSenderFactory.getEventSender(ActionType.SMS) instanceof SMSSender);
    }

    @Test
    public void should_return_push_event_sender() {
        Assert.assertTrue(eventSenderFactory.getEventSender(ActionType.PUSH) instanceof PushNotificationSender);
    }

}
