package com.group32.cs261project.sim.events;

import com.group32.cs261project.model.Aircraft;
import com.group32.cs261project.sim.SimulationEngine;

import java.time.LocalDateTime;

public class AircraftInboundArrivalEvent extends Event {

    private final Aircraft aircraft;

    public AircraftInboundArrivalEvent(LocalDateTime time, Aircraft aircraft) {
        super(time);
        this.aircraft = aircraft;
    }

    @Override
    public void handle(SimulationEngine engine) {

        // Add aircraft to holding pattern
        engine.getAirport().getHoldingPattern().addAircraft(aircraft);

        // After adding, trigger scheduler
        engine.getScheduler().scheduleNext();
    }
}
