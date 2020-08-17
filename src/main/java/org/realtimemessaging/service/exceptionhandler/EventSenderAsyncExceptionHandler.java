package org.realtimemessaging.service.exceptionhandler;

import org.realtimemessaging.dto.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class EventSenderAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(EventSenderAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        Event event = (Event) obj[0];
        logger.error("Asynchronous exception for event: " + event.toString());
        logger.error(throwable.getMessage());
        // event can be sent to the queue for re-processing
        // ...
    }

}