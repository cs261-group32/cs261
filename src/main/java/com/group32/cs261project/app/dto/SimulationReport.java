package com.group32.cs261project.app.dto;

import java.util.List;

public class SimulationReport {
	private ScenarioConfig inputs;
	private int maxTakeoffQueue;
	private double avgWaitingTimeMinutes;
	private int maxHolding;
	private double avgHoldingTimeMinutes;
	private int maxInboundDelay;
	private double avgInboundDelay;
	private int maxOutboundDelay;
	private double avgOutboundDelay;
	private int cancelledCount;
	private int divertedCount;

	public ScenarioConfig getInputs() { return inputs; }
	public void setInputs(ScenarioConfig inputs) { this.inputs = inputs; }
	public int getMaxTakeoffQueue() { return maxTakeoffQueue; }
	public void setMaxTakeoffQueue(int maxTakeoffQueue) { this.maxTakeoffQueue = maxTakeoffQueue; }
	public double getAvgWaitingTimeMinutes() { return avgWaitingTimeMinutes; }
	public void setAvgWaitingTimeMinutes(double avgWaitingTimeMinutes) { this.avgWaitingTimeMinutes = avgWaitingTimeMinutes; }
	public int getMaxHolding() { return maxHolding; }
	public void setMaxHolding(int maxHolding) { this.maxHolding = maxHolding; }
	public double getAvgHoldingTimeMinutes() { return avgHoldingTimeMinutes; }
	public void setAvgHoldingTimeMinutes(double avgHoldingTimeMinutes) { this.avgHoldingTimeMinutes = avgHoldingTimeMinutes; }
	public int getMaxInboundDelay() { return maxInboundDelay; }
	public void setMaxInboundDelay(int maxInboundDelay) { this.maxInboundDelay = maxInboundDelay; }
	public double getAvgInboundDelay() { return avgInboundDelay; }
	public void setAvgInboundDelay(double avgInboundDelay) { this.avgInboundDelay = avgInboundDelay; }
	public int getMaxOutboundDelay() { return maxOutboundDelay; }
	public void setMaxOutboundDelay(int maxOutboundDelay) { this.maxOutboundDelay = maxOutboundDelay; }
	public double getAvgOutboundDelay() { return avgOutboundDelay; }
	public void setAvgOutboundDelay(double avgOutboundDelay) { this.avgOutboundDelay = avgOutboundDelay; }
	public int getCancelledCount() { return cancelledCount; }
	public void setCancelledCount(int cancelledCount) { this.cancelledCount = cancelledCount; }
	public int getDivertedCount() { return divertedCount; }
	public void setDivertedCount(int divertedCount) { this.divertedCount = divertedCount; }
}
