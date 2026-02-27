package com.group32.cs261project.airport.events;

import java.time.Instant;

import com.group32.cs261project.model.Airport;
import com.group32.cs261project.sim.SimulationEngine;
import com.group32.cs261project.sim.events.Event;

public class AircraftOutboundReadyEvent implements Event<Airport> {

    public AircraftOutboundReadyEvent() {

    }

    @Override
    public Instant time() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handle(SimulationEngine<Airport> engine) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    
}
