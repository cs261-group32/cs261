package com.group32.cs261project.ui;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class StubSimulationService implements SimulationService {

    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean running = false;

    private final Object lock = new Object();
    private SimulationData current;          // mutable “truth”
    private Consumer<SimulationData> sink;   // snapshot consumer

    // stats accumulation
    private long ticks = 0;
    private long sumHolding = 0;
    private long sumTakeoff = 0;

    private Random rng;

    @Override
    public void start(SimulationData config, Consumer<SimulationData> onSnapshot) {
        synchronized (lock) {
            if (running) return;
            current = config.copy();
            current.ensureRunwayListSize();
            sink = onSnapshot;
            rng = new Random(current.stableConfigHash());
            running = true;

            ticks = 0;
            sumHolding = 0;
            sumTakeoff = 0;
        }

        // tick every 200ms, advance 1 simulated minute per tick
        exec.scheduleAtFixedRate(this::tick, 0, 200, TimeUnit.MILLISECONDS);
    }

    @Override
    public void updateConfig(SimulationData newConfig) {
        synchronized (lock) {
            if (!running) return;
            // keep sim time + live state, but update config/runways/rates
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

    private void tick() {
        SimulationData snap;
        synchronized (lock) {
            if (!running) return;

            current.ensureRunwayListSize();
            current.simMinute += 1;

            // capacity depends on runways available + mode
            int landingCapacity = 0;
            int takeoffCapacity = 0;

            for (SimulationData.RunwayConfig r : current.runways) {
                if (r.status != SimulationData.RunwayStatus.AVAILABLE) continue;

                switch (r.mode) {
                    case LANDING -> landingCapacity += 2;
                    case TAKE_OFF -> takeoffCapacity += 2;
                    case MIXED -> { landingCapacity += 1; takeoffCapacity += 1; }
                }
            }

            // arrivals/departures per minute based on rate per hour
            int inboundArrivals = poissonApprox(current.inboundRatePerHour / 60.0);
            int outboundReady = poissonApprox(current.outboundRatePerHour / 60.0);

            // queues evolve
            current.holdingQueue += inboundArrivals;
            current.takeoffQueue += outboundReady;

            int landed = Math.min(current.holdingQueue, landingCapacity);
            int tookOff = Math.min(current.takeoffQueue, takeoffCapacity);

            current.holdingQueue -= landed;
            current.takeoffQueue -= tookOff;

            // cancellations/diversions (simple rule)
            if (current.holdingQueue > 50 && rng.nextDouble() < 0.10) current.divertedCount += 1;
            if (current.takeoffQueue > 50 && rng.nextDouble() < 0.08) current.cancelledCount += 1;

            // “delay” proxies
            int inboundDelay = Math.min(120, current.holdingQueue);
            int outboundDelay = Math.min(120, current.takeoffQueue);

            current.maxInboundDelayMin = Math.max(current.maxInboundDelayMin, inboundDelay);
            current.maxOutboundDelayMin = Math.max(current.maxOutboundDelayMin, outboundDelay);

            // accumulate averages
            ticks++;
            sumHolding += current.holdingQueue;
            sumTakeoff += current.takeoffQueue;

            current.maxHoldingQueue = Math.max(current.maxHoldingQueue, current.holdingQueue);
            current.maxTakeoffQueue = Math.max(current.maxTakeoffQueue, current.takeoffQueue);

            current.avgHoldingQueue = sumHolding / (double) ticks;
            current.avgTakeoffQueue = sumTakeoff / (double) ticks;

            current.avgInboundDelayMin = current.avgHoldingQueue;
            current.avgOutboundDelayMin = current.avgTakeoffQueue;

            // end automatically after 12 simulated hours (720 minutes) unless user stops
            if (current.simMinute >= 720) {
                running = false;
            }

            snap = current.copy();
        }

        if (sink != null) sink.accept(snap);
    }

    private int poissonApprox(double lambda) {
        // cheap deterministic-ish “poisson-like” for small lambdas
        double x = rng.nextDouble();
        if (lambda <= 0) return 0;
        if (lambda < 0.05) return x < lambda ? 1 : 0;
        if (lambda < 0.15) return x < lambda ? 1 : (x < lambda * 1.2 ? 0 : 0);
        // otherwise jitter around lambda
        int base = (int) Math.floor(lambda);
        double frac = lambda - base;
        return base + (rng.nextDouble() < frac ? 1 : 0) + (rng.nextDouble() < 0.08 ? 1 : 0);
    }

    @Override
    public SimulationData stop() {
        synchronized (lock) {
            running = false;
            // final snapshot already has report fields; just return copy
            return current == null ? new SimulationData() : current.copy();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}