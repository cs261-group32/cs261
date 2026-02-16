package com.group32.cs261project.sim;

import com.group32.cs261project.sim.events.Event;

import java.util.PriorityQueue;

public class EventQueue {

    private final PriorityQueue<Event> queue;

    public EventQueue() {
        this.queue = new PriorityQueue<>();
    }

    // Add event to queue
    public void addEvent(Event event) {
        queue.add(event);
    }

    // Get and remove next event
    public Event poll() {
        return queue.poll();
    }

    // Look at next event without removing
    public Event peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}