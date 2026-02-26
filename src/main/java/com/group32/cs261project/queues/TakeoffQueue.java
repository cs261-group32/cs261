package com.group32.cs261project.queues;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

import com.group32.cs261project.model.Aircraft;

public class TakeOffQueue {

    private final Queue<Aircraft> queue;

    public TakeOffQueue() {
        this.queue = new LinkedList<>();
    }

    public void enqueue(Aircraft aircraft, Instant time) {
        aircraft.markTakeoffQueue(time);
        this.queue.add(aircraft);
    }

    public Aircraft peek() {
        return this.queue.peek();
    }

    public Aircraft dequeue() {
        return this.queue.poll();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

}
