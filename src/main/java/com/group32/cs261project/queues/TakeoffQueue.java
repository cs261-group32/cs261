package com.group32.cs261project.queues;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

import com.group32.cs261project.model.Aircraft;

public class TakeoffQueue {

    private Queue<Aircraft> queue;

    public TakeoffQueue() {
        this.queue = new LinkedList<>();

    }

    public void enqueue(Aircraft a, LocalDateTime t) {

    }

    public Aircraft peek() {
        return null;
    }

    public Aircraft dequeue() {
        return null;
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
}
