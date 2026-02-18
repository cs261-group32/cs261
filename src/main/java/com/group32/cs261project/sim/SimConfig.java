package com.group32.cs261project.sim;

import java.time.Instant;
import java.util.Objects;

public final class SimConfig {
    private final Instant startTime;
    private final Instant endTime;
    private final long randomSeed;

    public SimConfig(Instant startTime, Instant endTime, long randomSeed) {
        this.startTime = Objects.requireNonNull(startTime, "startTime");
        this.endTime = Objects.requireNonNull(endTime, "endTime");
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("endTime must be >= startTime");
        }
        this.randomSeed = randomSeed;
    }

    public Instant startTime() { return startTime; }
    public Instant endTime() { return endTime; }
    public long randomSeed() { return randomSeed; }
}