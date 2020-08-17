package org.realtimemessaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EventSenderFactory {

    @Autowired
    private ApplicationContext ac;

    public EventSender getEventSender(String actionType) {
        return ac.getBean(actionType, EventSender.class);
    }
}
