package com.group32.cs261project.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationData {

    // ----- Config inputs -----
    public int runwayCount = 2;             // 1-10
    public int inboundRatePerHour = 10;     // 0-30
    public int outboundRatePerHour = 10;    // 0-30
    public int maxWaitMinutes = 30;         // default 30

    public final List<RunwayConfig> runways = new ArrayList<>();

    // ----- Live snapshot fields (while running) -----
    public int simMinute = 0;     // simulated minutes since start
    public int holdingQueue = 0;
    public int takeoffQueue = 0;

    // ----- Results/report fields (filled at end) -----
    public int maxHoldingQueue = 0;
    public int maxTakeoffQueue = 0;
    public double avgHoldingQueue = 0;
    public double avgTakeoffQueue = 0;

    public int cancelledCount = 0;
    public int divertedCount = 0;

    // Delay metrics
    public double avgInboundDelayMin = 0;
    public double avgOutboundDelayMin = 0;
    public int maxInboundDelayMin = 0;
    public int maxOutboundDelayMin = 0;

    // Enums - allowed values for runway config
    public enum RunwayMode { LANDING, TAKE_OFF, MIXED }
    public enum RunwayStatus { AVAILABLE, RUNWAY_INSPECTION, SNOW_CLEARANCE, EQUIPMENT_FAILURE }

    // --- Runway Config Inner Class -----
    public static class RunwayConfig {
        public final int id; // Runway identifier
        public RunwayMode mode = RunwayMode.MIXED; // Default to MIXED
        public RunwayStatus status = RunwayStatus.AVAILABLE; // Default to AVAILABLE

        public RunwayConfig(int id) { this.id = id; }

        // Debug-friendly string representation
        @Override public String toString() {
            return "Runway " + id + " [" + status + ", " + mode + "]";
        }
    }

    // Ensures the runwa list length always matches runway count
    public void ensureRunwayListSize() {
        while (runways.size() < runwayCount) runways.add(new RunwayConfig(runways.size() + 1)); // If the list is too small: add new RunwayConfig objects 
        while (runways.size() > runwayCount) runways.remove(runways.size() - 1); // If list is too big: remove entries from end 
    }

    // Creates a deep copy of this SimulationData (for safely passing snapshots to the UI)
    public SimulationData copy() {
        SimulationData d = new SimulationData();  // Create a separate SimulationData object 
        d.runwayCount = runwayCount;
        d.inboundRatePerHour = inboundRatePerHour;
        d.outboundRatePerHour = outboundRatePerHour;
        d.maxWaitMinutes = maxWaitMinutes;

        d.simMinute = simMinute;
        d.holdingQueue = holdingQueue;
        d.takeoffQueue = takeoffQueue;

        d.maxHoldingQueue = maxHoldingQueue;
        d.maxTakeoffQueue = maxTakeoffQueue;
        d.avgHoldingQueue = avgHoldingQueue;
        d.avgTakeoffQueue = avgTakeoffQueue;

        d.cancelledCount = cancelledCount;
        d.divertedCount = divertedCount;

        d.avgInboundDelayMin = avgInboundDelayMin;
        d.avgOutboundDelayMin = avgOutboundDelayMin;
        d.maxInboundDelayMin = maxInboundDelayMin;
        d.maxOutboundDelayMin = maxOutboundDelayMin;

        // Deep copy runway configs 
        // (important to create new RunwayConfig objects so that the UI can safely modify them without affecting the simulation's internal state)
        d.runways.clear();
        for (RunwayConfig r : runways) {
            RunwayConfig rr = new RunwayConfig(r.id);
            rr.mode = r.mode;
            rr.status = r.status;
            d.runways.add(rr);
        }

        return d;
    }

    // Generate hash based on config fields
    public int stableConfigHash() {
        ensureRunwayListSize();
        int h = Objects.hash(runwayCount, inboundRatePerHour, outboundRatePerHour, maxWaitMinutes);
        for (RunwayConfig r : runways) {
            h = 31 * h + Objects.hash(r.id, r.mode, r.status);
        }
        return h;
    }
}