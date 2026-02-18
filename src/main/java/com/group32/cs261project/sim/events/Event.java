package com.group32.cs261project.sim.events;

import java.time.Instant;

import com.group32.cs261project.sim.SimulationEngine;

public interface Event<C> {
    Instant time();
    void handle(SimulationEngine<C> engine);
}
