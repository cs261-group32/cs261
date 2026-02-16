package com.group32.cs261project.sim.events;


import java.time.LocalDateTime;

public abstract class Event implements Comparable<Event> {

    private final LocalDateTime time;

    // constructor
    protected Event(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getTime() { // getter
        return time;
    }

    // Called when the event is executed by the SimulationEngine. When SimEngine is implemented there won't be an error.
    public abstract void handle(SimulationEngine engine); // this can be implemented by extensions of this class for it's specific functionality

    @Override
    public int compareTo(Event other) { // defines how events are ordered
        return this.time.compareTo(other.time);
    }
}
