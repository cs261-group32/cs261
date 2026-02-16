package com.group32.cs261project.sim;

import com.group32.cs261project.model.Airport;
import com.group32.cs261project.sim.events.Event;

public class SimulationEngine {

    private final EventQueue eventQueue;
    private final Airport airport; // single airport
    private final Scheduler scheduler;

    private boolean running = false;

    public SimulationEngine(Airport airport) {
        this.airport = airport; // assign airport to engine
        this.eventQueue = new EventQueue();
        this.scheduler = new Scheduler(this);
    }

    public void start() {
        this.running = true;

        while (running && !eventQueue.isEmpty()) {

            Event nextEvent = eventQueue.poll();

            if (nextEvent != null) {
                nextEvent.handle(this);
            }
        }
    }

    public void stop() {
        running = false;
    }

    public void scheduleEvent(Event event) {
        eventQueue.addEvent(event);
    }

    public Airport getAirport() {
        return airport;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
