// src/main/java/com/group32/cs261project/sim/queue/PriorityEventQueue.java
package com.group32.cs261project.sim.queue;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

import com.group32.cs261project.sim.events.Event;

public final class PriorityEventQueue<C> implements EventQueue<C> {

    private static final class QueuedEvent<C> {
        final Event<C> event;
        final Instant time;
        final long sequence;

        QueuedEvent(Event<C> event, long sequence) {
            this.event = event;
            this.time = event.time();
            this.sequence = sequence;
        }
    }

    private final PriorityQueue<QueuedEvent<C>> pq = new PriorityQueue<>(
            Comparator.<QueuedEvent<C>, Instant>comparing(q -> q.time)
                    .thenComparingLong(q -> q.sequence)
    );

    @Override
    public void push(Event<C> event, long sequence) {
        Objects.requireNonNull(event, "event");
        pq.add(new QueuedEvent<>(event, sequence));
    }

    @Override
    public Event<C> pop() {
        QueuedEvent<C> q = pq.poll();
        return q == null ? null : q.event;
    }

    @Override
    public Event<C> peek() {
        QueuedEvent<C> q = pq.peek();
        return q == null ? null : q.event;
    }

    @Override
    public Instant peekTime() {
        QueuedEvent<C> q = pq.peek();
        return q == null ? null : q.time;
    }

    @Override
    public boolean isEmpty() {
        return pq.isEmpty();
    }

    @Override
    public int size() {
        return pq.size();
    }
}
