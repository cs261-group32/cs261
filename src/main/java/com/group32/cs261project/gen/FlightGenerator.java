package com.group32.cs261project.gen;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.group32.cs261project.model.Aircraft;
import com.group32.cs261project.sim.SimConfig;

public class FlightGenerator {

    private final Random rng;
    private final double sigmaMinutes = 5.0;
    private final Instant startTime;
    private final Instant endTime;
    private final int inboundRatePerHour;
    private final int outboundRatePerHour;
    
    /**
     * Constructor
     * @param config simulation config object
     */
    public FlightGenerator(SimConfig config) {
        this.rng = new Random(config.randomSeed());
        this.startTime = config.startTime();
        this.endTime = config.endTime();
        this.inboundRatePerHour = config.inboundRatePerHour();
        this.outboundRatePerHour = config.outboundRatePerHour();
    }

    /**
     * Helper method to get entry time from scheduled time
     * @param scheduledTime scheduled time
     * @return entry time + N(0, 5^2) random variable
     */
    private Instant entryTime(Instant scheduledTime) {
        double delayMinutes = rng.nextGaussian() * 5.0;
        long millis = Math.round(delayMinutes * 60_000.0);
        return scheduledTime.plusMillis(millis);
    }

    /**
     * Helper method to get inter flight duration from rate per hour
     * @param ratePerHour rate per hour (in or out)
     * @return duration object
     */
    private Duration interFlightDuration(int ratePerHour) {
        double spacingMinutes = 60.0 / (double) ratePerHour;
        long spacingMillis = Math.round(spacingMinutes * 60_000.0);
        return Duration.ofMillis(spacingMillis);
    }

    /**
     * Generate flights based on type
     * @param type one of INBOUND, OUTBOUND
     * @return list of generated flight objects
     */
    public List<GeneratedFlight> generateFlights(FlightType type) {

        List<GeneratedFlight> flights = new ArrayList<>();

        Duration spacing = interFlightDuration( switch (type) {
            case INBOUND -> this.inboundRatePerHour;
            case OUTBOUND -> this.outboundRatePerHour;
        });

        Instant t = this.startTime;
        
        while (!t.isAfter(endTime)) {
            flights.add(
                new GeneratedFlight(new Aircraft(
                    null,
                    null,
                    null,
                    null,
                    t,
                    null,
                    sigmaMinutes,
                    null
                ), t, this.entryTime(t))
            );
            t = t.plus(spacing);
        }

        return flights;
    }
    
}
