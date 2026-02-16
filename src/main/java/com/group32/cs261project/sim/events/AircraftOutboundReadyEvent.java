package com.group32.cs261project.sim.events;

import com.group32.cs261project.model.Aircraft;
import com.group32.cs261project.sim.SimulationEngine;

import java.time.LocalDateTime;


/**
 * Airport must have 
 * public HoldingPattern getHoldingPattern()
 * public TakeOffQueue getTakeOffQueue()
 */
public class AircraftOutboundReadyEvent extends Event {

    private final Aircraft aircraft;

    public AircraftOutboundReadyEvent(LocalDateTime time, Aircraft aircraft) {
        super(time);
        this.aircraft = aircraft;
    }

    @Override
    public void handle(SimulationEngine engine) {

        // Add aircraft to take-off queue
        engine.getAirport().getTakeOffQueue().addAircraft(aircraft);

        // Trigger scheduler
        engine.getScheduler().scheduleNext();
    }
}
