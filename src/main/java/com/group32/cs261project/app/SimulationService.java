package com.group32.cs261project.app;

import com.group32.cs261project.app.dto.ScenarioConfig;

public interface SimulationService {
	/** Start a simulation for the provided scenario config. Returns a handle to control/observe. */
	SimulationHandle start(ScenarioConfig config);
}
