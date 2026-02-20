package com.group32.cs261project;

import java.util.function.Consumer;

public interface SimulationService {
    /** Start a simulation based on config. Snapshots are delivered via onSnapshot. */
    void start(SimulationData config, Consumer<SimulationData> onSnapshot);

    /** Apply updated config while running (optional, but we support it). */
    void updateConfig(SimulationData newConfig);

    /** Stop simulation and return a final report snapshot (SimulationData contains report fields too). */
    SimulationData stop();

    boolean isRunning();
}