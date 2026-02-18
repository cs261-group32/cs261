package com.group32.cs261project.sim.queue;

import java.time.Instant;

import com.group32.cs261project.sim.events.Event;

/**
 * Interface for the event queue
 */
public interface EventQueue<C> {
    void push(Event<C> event, long sequenceNumber);
    Event<C> pop();
    Event<C> peek();
    Instant peekTime();
    boolean isEmpty();
    int size();
}
