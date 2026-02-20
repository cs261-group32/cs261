package com.group32.cs261project.app.dto;

import java.util.ArrayList;
import java.util.List;

public class ScenarioConfig {
	private int inboundFlowRate; // aircraft per hour
	private int outboundFlowRate;
	private int runwayCount;
	private List<RunwayConfig> runways = new ArrayList<>();
	private int maxWaitMinutes = 30;

	public int getInboundFlowRate() { return inboundFlowRate; }
	public void setInboundFlowRate(int inboundFlowRate) { this.inboundFlowRate = inboundFlowRate; }
	public int getOutboundFlowRate() { return outboundFlowRate; }
	public void setOutboundFlowRate(int outboundFlowRate) { this.outboundFlowRate = outboundFlowRate; }
	public int getRunwayCount() { return runwayCount; }
	public void setRunwayCount(int runwayCount) { this.runwayCount = runwayCount; }
	public List<RunwayConfig> getRunways() { return runways; }
	public void setRunways(List<RunwayConfig> runways) { this.runways = runways; }
	public int getMaxWaitMinutes() { return maxWaitMinutes; }
	public void setMaxWaitMinutes(int maxWaitMinutes) { this.maxWaitMinutes = maxWaitMinutes; }
}
