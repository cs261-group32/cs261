package com.group32.cs261project.app;

import java.util.function.Consumer;

import com.group32.cs261project.app.dto.RunwayConfig;
import com.group32.cs261project.app.dto.SimulationReport;
import com.group32.cs261project.app.dto.SimulationSnapshot;

public interface SimulationHandle {
	/** Subscribe to periodic snapshots; may be called multiple times. */
	void onSnapshot(Consumer<SimulationSnapshot> consumer);

	/** Stop the running simulation (graceful). */
	void stop();

	/** Update a single runway configuration during a run; index is 0-based. */
	void updateRunway(int index, RunwayConfig config);

	/** After stop, obtain the simulation report. */
	SimulationReport getReport();
}
