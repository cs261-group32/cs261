package com.group32.cs261project.app.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.group32.cs261project.app.SimulationHandle;
import com.group32.cs261project.app.SimulationService;
import com.group32.cs261project.app.dto.RunwayConfig;
import com.group32.cs261project.app.dto.ScenarioConfig;
import com.group32.cs261project.app.dto.SimulationReport;
import com.group32.cs261project.app.dto.SimulationSnapshot;

public class StubSimulationService implements SimulationService {

	@Override
	public SimulationHandle start(ScenarioConfig config) {
		return new StubHandle(config);
	}

	private static class StubHandle implements SimulationHandle {
		private final ScenarioConfig config;
		private final ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		private final List<Consumer<SimulationSnapshot>> consumers = new CopyOnWriteArrayList<>();
		private final List<SimulationSnapshot> history = new ArrayList<>();
		private final Random rnd;
		private long simulatedMinutes = 0;
		private ScheduledFuture<?> future;

		private StubHandle(ScenarioConfig config) {
			this.config = config;
			long seed = computeSeed(config);
			this.rnd = new Random(seed);

			// schedule ticks every 200ms
			this.future = ex.scheduleAtFixedRate(this::tick, 0, 200, TimeUnit.MILLISECONDS);
		}

		private long computeSeed(ScenarioConfig cfg) {
			int h = cfg.getInboundFlowRate() * 31 + cfg.getOutboundFlowRate() * 17 + cfg.getRunwayCount() * 13;
			for (RunwayConfig r : cfg.getRunways()) {
				h = h * 31 + (r.getOperationalStatus().ordinal() << 2) + r.getOperatingMode().ordinal();
			}
			return h;
		}

		private void tick() {
			try {
				// advance simulated time by 1 minute per tick
				simulatedMinutes += 1;

				SimulationSnapshot snap = new SimulationSnapshot();
				snap.setSimulatedMinutes(simulatedMinutes);

				// Simple deterministic dynamics: queue sizes influenced by flows and runway availability
				int availLanding = 0, availTakeoff = 0;
				List<String> rstates = new ArrayList<>();
				for (RunwayConfig r : config.getRunways()) {
					String state = r.getOperationalStatus().name() + ":" + r.getOperatingMode().name();
					rstates.add(state);
					if (r.getOperationalStatus() == RunwayConfig.OperationalStatus.AVAILABLE) {
						if (r.getOperatingMode() == RunwayConfig.OperatingMode.LANDING) availLanding++;
						else if (r.getOperatingMode() == RunwayConfig.OperatingMode.TAKE_OFF) availTakeoff++;
						else { availLanding++; availTakeoff++; }
					}
				}

				// baseline arrival/departure rates scaled to per-minute
				double inboundPerMin = config.getInboundFlowRate() / 60.0;
				double outboundPerMin = config.getOutboundFlowRate() / 60.0;

				// use random noise
				int inboundDelta = (int)Math.round(inboundPerMin + (rnd.nextDouble()-0.5) * 0.5);
				int outboundDelta = (int)Math.round(outboundPerMin + (rnd.nextDouble()-0.5) * 0.5);
				inboundDelta = Math.max(0, inboundDelta);
				outboundDelta = Math.max(0, outboundDelta);

				// compute queues: simple accumulation with service depending on available runways
				int lastInbound = history.isEmpty() ? 0 : history.get(history.size()-1).getInboundQueue();
				int lastOutbound = history.isEmpty() ? 0 : history.get(history.size()-1).getOutboundQueue();
				int lastHolding = history.isEmpty() ? 0 : history.get(history.size()-1).getHoldingQueue();

				int landingServed = Math.max(0, availLanding);
				int takeoffServed = Math.max(0, availTakeoff);

				int inboundNow = Math.max(0, lastInbound + inboundDelta - landingServed);
				int outboundNow = Math.max(0, lastOutbound + outboundDelta - takeoffServed);

				// holding increases if inbound backlog and no landing runways
				int holdingNow = Math.max(0, lastHolding + Math.max(0, inboundNow - landingServed/2));

				snap.setInboundQueue(inboundNow);
				snap.setOutboundQueue(outboundNow);
				snap.setHoldingQueue(holdingNow);
				snap.setRunwayStates(rstates);

				history.add(snap);

				// publish to consumers
				for (Consumer<SimulationSnapshot> c : consumers) {
					try { c.accept(snap); } catch (Exception ex) { /* ignore */ }
				}

				// optional end condition: stop after 24*60 minutes simulated
				if (simulatedMinutes >= 24*60) {
					stop();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onSnapshot(Consumer<SimulationSnapshot> consumer) {
			consumers.add(consumer);
		}

		@Override
		public void stop() {
			if (future != null) future.cancel(false);
			ex.shutdownNow();
		}

		@Override
		public void updateRunway(int index, RunwayConfig cfg) {
			if (index >=0 && index < config.getRunways().size()) {
				config.getRunways().set(index, cfg);
			}
		}

		@Override
		public SimulationReport getReport() {
			SimulationReport r = new SimulationReport();
			r.setInputs(config);
			// simple metrics from history
			int maxTO = 0, maxHolding = 0, maxInbound = 0, maxOutbound = 0;
			double sumWaiting = 0, sumHolding = 0, sumInboundDelay = 0, sumOutboundDelay = 0;
			for (SimulationSnapshot s : history) {
				maxTO = Math.max(maxTO, s.getOutboundQueue());
				maxHolding = Math.max(maxHolding, s.getHoldingQueue());
				maxInbound = Math.max(maxInbound, s.getInboundQueue());
				maxOutbound = Math.max(maxOutbound, s.getOutboundQueue());
				sumWaiting += s.getInboundQueue();
				sumHolding += s.getHoldingQueue();
			}
			int n = Math.max(1, history.size());
			r.setMaxTakeoffQueue(maxTO);
			r.setAvgWaitingTimeMinutes(sumWaiting / n);
			r.setMaxHolding(maxHolding);
			r.setAvgHoldingTimeMinutes(sumHolding / n);
			r.setMaxInboundDelay(maxInbound);
			r.setAvgInboundDelay(sumWaiting / n);
			r.setMaxOutboundDelay(maxOutbound);
			r.setAvgOutboundDelay(sumOutboundDelay / Math.max(1,n));
			r.setCancelledCount(0);
			r.setDivertedCount(0);
			return r;
		}
	}
}
