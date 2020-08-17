package org.realtimemessaging.service;

import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value = ActionType.EMAIL)
public class EmailSender extends EventSender {

    private Logger logger = LoggerFactory.getLogger(EmailSender.class);

    @Override
    public void send(Event event) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Email is sent to: " + event.getCustomerAddress());
    }
}
