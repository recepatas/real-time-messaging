package org.realtimemessaging.service;

import org.realtimemessaging.dto.Event;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class EventSender {

    private AtomicInteger count = new AtomicInteger(0);

    public int getCount() {
        return count.get();
    }

    public void incrementCount() {
        count.incrementAndGet();
    }

    abstract void send(Event event);

}
