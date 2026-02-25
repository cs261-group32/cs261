package com.group32.cs261project.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.group32.cs261project.model.enums.RunwayMode;
import com.group32.cs261project.model.enums.RunwayStatus;
import com.group32.cs261project.queues.HoldingPattern;
import com.group32.cs261project.queues.TakeoffQueue;

public class Airport {

    private final List<Runway> runways;
    private final HoldingPattern holdingPattern;
    private final TakeoffQueue takeoffQueue;

    public Airport(List<Runway> runways) {
        this.runways = runways;
        this.holdingPattern = new HoldingPattern();
        this.takeoffQueue = new TakeoffQueue();
    }

    public List<Runway> runways() {
        return this.runways;
    }

    public HoldingPattern holdingPattern() {
        return this.holdingPattern;
    }

    public TakeoffQueue takeoffQueue() {
        return this.takeoffQueue;
    }

    public Runway getRunway(int runwayNumber) {
        return this.runways.get(runwayNumber);
    }

    public void setRunwayStatus(int runwayNumber, RunwayStatus status) {
        this.getRunway(runwayNumber).setStatus(status);
    }

    public void setRunwayMode(int runwayNumber, RunwayMode mode) {
        this.getRunway(runwayNumber).setMode(mode);
    }

    public Runway findAvailableLandingRunway(LocalDateTime time) {
        return null;
    }

    public Runway findAvailableTakeoffRunway(LocalDateTime time) {
        return null;
    }



    
}
