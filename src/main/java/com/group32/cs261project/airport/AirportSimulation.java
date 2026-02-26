package com.group32.cs261project.airport;

import java.time.Instant;
import java.util.List;

import com.group32.cs261project.model.Airport;
import com.group32.cs261project.model.Runway;
import com.group32.cs261project.sim.SimConfig;
import com.group32.cs261project.sim.SimulationEngine;

/**
 * Main class for the backend airport simulation
 */
public class AirportSimulation {

    private final SimConfig config;
    private final Airport airport;
    private final SimulationEngine<Airport> engine;

    /**
     * Constructor
     * @param config Configuration for the airport simulation
     * @param runways List of runways
     */
    public AirportSimulation(SimConfig config, List<Runway> runways) {
        this.config = config;
        this.airport = new Airport(runways);
        this.engine = new SimulationEngine<>(config, airport, null);
    }

    /**
     * Getter for airport
     * @return airport object
     */
    public Airport airport() {
        return this.airport;
    }

    /**
     * Getter for engine
     * @return simulation engine object
     */
    public SimulationEngine<Airport> engine() {
        return this.engine;
    }

    /**
     * Getter for simulation configuration
     * @return config object
     */
    public SimConfig config() {
        return this.config;
    }

    /**
     * Run until config end time
     */
    public void run() {
        this.engine.runUntil(this.config.endTime());
    }

    /**
     * Run until a specific end time
     * @param endTime specific end time
     */
    public void runUntil(Instant endTime) {
        this.engine.runUntil(endTime);
    }

    /**
     * Step the simulation one step
     * @return true if can step, false if completed
     */
    public boolean step() {
        return this.engine.step();
    }
    
}
