package com.group32.cs261project.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.group32.cs261project.model.enums.RunwayMode;
import com.group32.cs261project.model.enums.RunwayStatus;

public class Runway {

    private final int runwayNumber;
    private final int lengthM;
    private final int bearingDeg;
    private RunwayMode mode;
    private RunwayStatus status;
    private LocalDateTime occupiedUntil;

    public Runway(
        int runwayNumber, 
        int lengthM, 
        int bearingDeg, 
        RunwayMode mode,
        RunwayStatus status
    ) {
        this.runwayNumber = Objects.requireNonNull(runwayNumber);
        this.lengthM = Objects.requireNonNull(lengthM);
        this.bearingDeg = Objects.requireNonNull(bearingDeg);
        this.mode = Objects.requireNonNull(mode);
        this.status = Objects.requireNonNull(status);
        this.occupiedUntil = null;
    }

    public int runwayNumber() {
        return this.runwayNumber;
    }

    public int lengthM() {
        return this.lengthM;
    }

    public int bearingDeg() {
        return this.bearingDeg;
    }

    public RunwayMode mode() {
        return this.mode;
    }

    public RunwayStatus status() {
        return this.status;
    }

    public boolean isAvailableForLanding(LocalDateTime time) {
        return false;
    }

    public boolean isAvailableForTakeOff(LocalDateTime time) {
        return false;
    }

    public LocalDateTime occupiedUntil() {
        return this.occupiedUntil;
    }

    public void occupyUntil(LocalDateTime time) {
        if (!this.isOccupied(time))
            this.occupiedUntil = time;
    }
    
    public boolean isOccupied(LocalDateTime time) {
        return (this.occupiedUntil == null || this.occupiedUntil.isBefore(time));
    }
    
    public void clearOccupancy() {
        this.occupiedUntil = null;
    }

    public LocalDateTime nextAvailableTime(LocalDateTime time) {
        return this.isOccupied(time) ? this.occupiedUntil : time;
    }

    public void setMode(RunwayMode newMode) {
        this.mode = newMode;
    }

    public void setStatus(RunwayStatus newStatus) {
        this.status = newStatus;
    }
}
