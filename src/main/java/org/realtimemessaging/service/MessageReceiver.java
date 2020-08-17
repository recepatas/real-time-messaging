package org.realtimemessaging.service;

import org.realtimemessaging.dto.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    private Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    private EventSenderFactory eventSenderFactory;

    @JmsListener(destination = "${queue.name}", containerFactory = "eventQueueJmsListenerFactory")
    public void receiveMessage(Event event) {
        logger.info("Received event: " + event);

        EventSender eventSender = eventSenderFactory.getEventSender(event.getActionType());
        eventSender.send(event);
        eventSender.incrementCount();
    }


}
