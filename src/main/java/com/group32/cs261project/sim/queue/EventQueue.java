package com.group32.cs261project.sim.queue;

import java.time.Instant;

import com.group32.cs261project.sim.events.Event;

public interface EventQueue<C> {
    void push(Event<C> event, long sequence);
    Event<C> pop();
    Event<C> peek();
    Instant peekTime();
    boolean isEmpty();
    int size();
}
