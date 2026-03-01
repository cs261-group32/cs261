package com.group32.cs261project.sim;

import java.time.Instant;
import java.util.Objects;

/**
 * Configuration class for the simulation
 */
public final class SimConfig {

    private final Instant startTime;
    private final Instant endTime;
    private final long randomSeed;
    private final int inboundRatePerHour;
    private final int outboundRatePerHour;

    /**
     * Constructor
     * @param startTime
     * @param endTime
     * @param randomSeed
     */
    public SimConfig(Instant startTime, Instant endTime, long randomSeed, int inboundRatePerHour, int outboundRatePerHour) {
        this.startTime = Objects.requireNonNull(startTime, "startTime");
        this.endTime = Objects.requireNonNull(endTime, "endTime");
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("endTime must be >= startTime");
        }
        this.randomSeed = randomSeed;
        this.inboundRatePerHour = inboundRatePerHour;
        this.outboundRatePerHour = outboundRatePerHour;
    }

    public Instant startTime() {
        return this.startTime;
    }

    public Instant endTime() {
        return this.endTime;
    }

    public long randomSeed() {
        return this.randomSeed;
    }

    public int inboundRatePerHour() {
        return this.inboundRatePerHour;
    }

    public int outboundRatePerHour() {
        return this.outboundRatePerHour;
    }
}