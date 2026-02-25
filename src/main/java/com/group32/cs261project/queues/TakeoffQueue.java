package com.group32.cs261project.queues;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

import com.group32.cs261project.model.Aircraft;

public class TakeoffQueue {

    private final Queue<Aircraft> queue;

    public TakeoffQueue() {
        this.queue = new LinkedList<>();
    }

    public void enqueue(Aircraft aircraft, LocalDateTime time) {
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
