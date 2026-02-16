package com.group32.cs261project.sim.events;

import com.group32.cs261project.model.Aircraft;
import com.group32.cs261project.sim.SimulationEngine;

/**
 * Airport must have 
 * public HoldingPattern getHoldingPattern()
 * public TakeOffQueue getTakeOffQueue()
 * 
 * SimEngine must have 
 * public Airport getAirport()
 * public Scheduler getScheduler()
 * 
 * 
 */

// to use this aircraft and engine needs to be implemented 
import java.time.LocalDateTime;

public class AircraftInboundArrivalEvent extends Event {

    private final Aircraft aircraft; // aircraft needs to be implemented

    public AircraftInboundArrivalEvent(LocalDateTime time, Aircraft aircraft) {
        super(time);
        this.aircraft = aircraft;
    }

    @Override
    public void handle(SimulationEngine engine) {

        engine.getAirport().getHoldingPattern().addAircraft(aircraft); // add aircraft to holding pattern

        engine.getScheduler().scheduleNext(); // trigger scheduler
    }
}
