package com.group32.cs261project.ui;

import com.group32.cs261project.app.SimulationService;
import com.group32.cs261project.app.SimulationHandle;
import com.group32.cs261project.app.dto.ScenarioConfig;

public class UiState {
	private final SimulationService simulationService;
	private ScenarioConfig lastConfig;
	private SimulationHandle currentHandle;

	public UiState(SimulationService service) {
		this.simulationService = service;
	}

	public SimulationService getSimulationService() { return simulationService; }
	public ScenarioConfig getLastConfig() { return lastConfig; }
	public void setLastConfig(ScenarioConfig lastConfig) { this.lastConfig = lastConfig; }
	public SimulationHandle getCurrentHandle() { return currentHandle; }
	public void setCurrentHandle(SimulationHandle currentHandle) { this.currentHandle = currentHandle; }
}
