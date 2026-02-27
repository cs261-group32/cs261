package com.group32.cs261project.gen;

import java.util.List;
import java.util.Random;

import com.group32.cs261project.sim.SimConfig;

public class FlightGenerator {

    private final Random rng;
    private final double sigmaMinutes = 5.0;
    
    public FlightGenerator(SimConfig config) {
        this.rng = new Random(config.randomSeed());
    }

    public List<GeneratedFlight> generateInbound() {
        return null;
    }

    public List<GeneratedFlight> generateOutbound() {
        return null;
    }
    
}
