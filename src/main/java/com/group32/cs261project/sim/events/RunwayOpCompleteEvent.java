package com.group32.cs261project.sim.events;

import com.group32.cs261project.model.Aircraft;
import com.group32.cs261project.sim.SimulationEngine;

import java.time.LocalDateTime;

public class RunwayOpCompleteEvent extends Event {

    private final Aircraft aircraft;

    public RunwayOpCompleteEvent(LocalDateTime time, Aircraft aircraft) {
        super(time);
        this.aircraft = aircraft;
    }

    @Override
    public void handle(SimulationEngine engine) { // what needs to be done when a runway operation is done

        engine.getAirport().getRunway().setAvailable(true); // free runway

        aircraft.setCompleted(true); // update aircraft state? not sure if we will need this probably a good idea

        engine.getScheduler().scheduleNext(); // make scheduler get next event to schedule
    }
}
