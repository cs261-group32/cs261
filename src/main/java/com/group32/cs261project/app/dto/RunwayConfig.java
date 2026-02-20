package com.group32.cs261project.app.dto;

public class RunwayConfig {
	public enum OperationalStatus { AVAILABLE, RUNWAY_INSPECTION, SNOW_CLEARANCE, EQUIPMENT_FAILURE }
	public enum OperatingMode { LANDING, TAKE_OFF, MIXED }

	private final int id;
	private OperationalStatus operationalStatus;
	private OperatingMode operatingMode;

	public RunwayConfig(int id, OperationalStatus opStatus, OperatingMode mode) {
		this.id = id;
		this.operationalStatus = opStatus;
		this.operatingMode = mode;
	}

	public int getId() { return id; }
	public OperationalStatus getOperationalStatus() { return operationalStatus; }
	public void setOperationalStatus(OperationalStatus operationalStatus) { this.operationalStatus = operationalStatus; }
	public OperatingMode getOperatingMode() { return operatingMode; }
	public void setOperatingMode(OperatingMode operatingMode) { this.operatingMode = operatingMode; }
}
