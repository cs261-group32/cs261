package com.group32.cs261project.app.dto;

import java.util.ArrayList;
import java.util.List;

public class SimulationSnapshot {
	private long simulatedMinutes;
	private int inboundQueue;
	private int outboundQueue;
	private int holdingQueue;
	private List<String> runwayStates = new ArrayList<>();

	public long getSimulatedMinutes() { return simulatedMinutes; }
	public void setSimulatedMinutes(long simulatedMinutes) { this.simulatedMinutes = simulatedMinutes; }
	public int getInboundQueue() { return inboundQueue; }
	public void setInboundQueue(int inboundQueue) { this.inboundQueue = inboundQueue; }
	public int getOutboundQueue() { return outboundQueue; }
	public void setOutboundQueue(int outboundQueue) { this.outboundQueue = outboundQueue; }
	public int getHoldingQueue() { return holdingQueue; }
	public void setHoldingQueue(int holdingQueue) { this.holdingQueue = holdingQueue; }
	public List<String> getRunwayStates() { return runwayStates; }
	public void setRunwayStates(List<String> runwayStates) { this.runwayStates = runwayStates; }
}
