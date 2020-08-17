package org.realtimemessaging.service;

import org.realtimemessaging.dto.ActionType;
import org.realtimemessaging.dto.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component(value = ActionType.SMS)
public class SMSSender extends EventSender {

    private Logger logger = LoggerFactory.getLogger(SMSSender.class);

    /**
     * send email to the customer address asynchronously. fire and forget.
     * exceptions can be caught in EventSenderAsyncExceptionHandler
     * @see org.realtimemessaging.service.exceptionhandler.EventSenderAsyncExceptionHandler
     * @param event
     */
    @Override
    @Async
    public void send(Event event) {
        // send sms to the customer address
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("SMS is sent to: " + event.getCustomerAddress());
    }
}
