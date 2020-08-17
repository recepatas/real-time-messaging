package org.realtimemessaging.service;

import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value = ActionType.PUSH)
public class PushNotificationSender extends EventSender {

    private Logger logger = LoggerFactory.getLogger(PushNotificationSender.class);

    @Override
    public void send(Event event) {
        // send push notification to the customer address
        logger.info("Push notification is sent to: " + event.getCustomerAddress());
    }
}
