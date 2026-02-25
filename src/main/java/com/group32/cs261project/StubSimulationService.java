package com.group32.cs261project;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
/* Stub implementation of simulation backend.
*  It implements the SimulationService interface methods: start, updateConfig, stop, and isRunning.
*/
public class StubSimulationService implements SimulationService {

    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(); // Single background thread that runs scheduled tasks
    private volatile boolean running = false; // Flag to indicate if the simulation is running

    private final Object lock = new Object(); // Lock for synchronizing access to shared simulation state
    private SimulationData current;          // Current simulation state
    private Consumer<SimulationData> sink;   // Callback to deliver snapshots to the UI

    // Stats accumulation
    private long ticks = 0;
    private long sumHolding = 0;
    private long sumTakeoff = 0;

    private Random rng;

    // Start a simulation based on config
    @Override
    public void start(SimulationData config, Consumer<SimulationData> onSnapshot) {
        // Lock to ensure start is thread-safe
        synchronized (lock) {
            if (running) return; // Of already running, ignore start request

            // Copy config so caller can't mutate internal state after starting
            current = config.copy();
            current.ensureRunwayListSize();

            sink = onSnapshot; // Store the callback for later tick updates
            rng = new Random(current.stableConfigHash()); // Seed RNG with config hash for deterministic behavior
            running = true; // Mark simulation as running

            // Reset accumulators for a new run
            ticks = 0;
            sumHolding = 0;
            sumTakeoff = 0;
        }

        // Tick every 200ms, advance 1 simulated minute per tick
        exec.scheduleAtFixedRate(this::tick, 0, 200, TimeUnit.MILLISECONDS);
    }

    // Update config while running (not implemented yet)
    @Override
    public void updateConfig(SimulationData newConfig) {
        synchronized (lock) {
            if (!running) return;
            // Keep sim time + live state, but update config/runways/rates
            int simMinute = current.simMinute;
            int holding = current.holdingQueue;
            int takeoff = current.takeoffQueue;

            current = newConfig.copy();
            current.ensureRunwayListSize();

            current.simMinute = simMinute;
            current.holdingQueue = holding;
            current.takeoffQueue = takeoff;
        }
    }

    // ----- Simulation logic -----
    private void tick() {
        SimulationData snap; // Hold a copied snapshot to send to the UI after unlocking
        synchronized (lock) {
            if (!running) return; // If not running, do nothing

            current.ensureRunwayListSize();
            current.simMinute += 1;

            // Capacity depends on runways available + mode
            int landingCapacity = 0;
            int takeoffCapacity = 0;

            // Only available runways contribute to capacity
            for (SimulationData.RunwayConfig r : current.runways) {
                if (r.status != SimulationData.RunwayStatus.AVAILABLE) continue;

                // Capacity rules per runway per tick
                switch (r.mode) {
                    case LANDING -> landingCapacity += 2;
                    case TAKE_OFF -> takeoffCapacity += 2;
                    case MIXED -> { landingCapacity += 1; takeoffCapacity += 1; }
                }
            }

            // Generate new arrivals based on rates (using a simple poisson approximation)
            int inboundArrivals = poissonApprox(current.inboundRatePerHour / 60.0);
            int outboundReady = poissonApprox(current.outboundRatePerHour / 60.0);

            // Queue evolution
            current.holdingQueue += inboundArrivals;
            current.takeoffQueue += outboundReady;

            int landed = Math.min(current.holdingQueue, landingCapacity);
            int tookOff = Math.min(current.takeoffQueue, takeoffCapacity);

            // Remove landed/taken off from queues
            current.holdingQueue -= landed;
            current.takeoffQueue -= tookOff;

            // Cancellations/diversions (simple rule)
            if (current.holdingQueue > 50 && rng.nextDouble() < 0.10) current.divertedCount += 1;
            if (current.takeoffQueue > 50 && rng.nextDouble() < 0.08) current.cancelledCount += 1;

            // Delay proxies
            int inboundDelay = Math.min(120, current.holdingQueue);
            int outboundDelay = Math.min(120, current.takeoffQueue);

            current.maxInboundDelayMin = Math.max(current.maxInboundDelayMin, inboundDelay);
            current.maxOutboundDelayMin = Math.max(current.maxOutboundDelayMin, outboundDelay);

            // Accumulate averages
            ticks++;
            sumHolding += current.holdingQueue;
            sumTakeoff += current.takeoffQueue;

            current.maxHoldingQueue = Math.max(current.maxHoldingQueue, current.holdingQueue);
            current.maxTakeoffQueue = Math.max(current.maxTakeoffQueue, current.takeoffQueue);

            current.avgHoldingQueue = sumHolding / (double) ticks;
            current.avgTakeoffQueue = sumTakeoff / (double) ticks;

            current.avgInboundDelayMin = current.avgHoldingQueue;
            current.avgOutboundDelayMin = current.avgTakeoffQueue;

            // End automatically after 12 simulated hours (720 minutes) unless user stops
            if (current.simMinute >= 720) {
                running = false;
            }

            snap = current.copy();
        }

        if (sink != null) sink.accept(snap);
    }

    private int poissonApprox(double lambda) {
        // "Poisson-like” for small lambda
        double x = rng.nextDouble();
        if (lambda <= 0) return 0;
        if (lambda < 0.05) return x < lambda ? 1 : 0;
        if (lambda < 0.15) return x < lambda ? 1 : (x < lambda * 1.2 ? 0 : 0);
        // Otherwise jitter around lambda
        int base = (int) Math.floor(lambda);
        double frac = lambda - base;
        return base + (rng.nextDouble() < frac ? 1 : 0) + (rng.nextDouble() < 0.08 ? 1 : 0);
    }

    // Stop the simulation safely and return a final report snapshot
    @Override
    public SimulationData stop() {
        synchronized (lock) {
            running = false;
            // Final snapshot already has report fields; just return copy
            return current == null ? new SimulationData() : current.copy();
        }
    }

    // Check if the simulation is currently running
    @Override
    public boolean isRunning() {
        return running;
    }
}