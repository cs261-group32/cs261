package com.group32.cs261project.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.group32.cs261project.model.enums.RunwayMode;
import com.group32.cs261project.model.enums.RunwayStatus;

/**
 * Runway class
 */
public class Runway {

    private final int runwayNumber;
    private final int lengthM;
    private final int bearingDeg;
    private RunwayMode mode;
    private RunwayStatus status;
    private LocalDateTime occupiedUntil;

    /**
     * Constructor
     * @param runwayNumber
     * @param lengthM
     * @param bearingDeg
     * @param mode
     * @param status
     */
    public Runway(int runwayNumber, int lengthM, int bearingDeg, RunwayMode mode, RunwayStatus status) {
        this.runwayNumber = Objects.requireNonNull(runwayNumber);
        this.lengthM = Objects.requireNonNull(lengthM);
        this.bearingDeg = Objects.requireNonNull(bearingDeg);
        this.mode = Objects.requireNonNull(mode);
        this.status = Objects.requireNonNull(status);
        this.occupiedUntil = null;
    }

    /**
     * Getter for runway number
     * @return the runway number
     */
    public int runwayNumber() {
        return this.runwayNumber;
    }

    /**
     * Getter for runway length
     * @return runway length in metres
     */
    public int lengthM() {
        return this.lengthM;
    }

    /**
     * Getter for bearing of runway
     * @return bearing in degrees
     */
    public int bearingDeg() {
        return this.bearingDeg;
    }

    /**
     * Getter for runway mode
     * @return one of MIXED, LANDING, TAKEOFF
     */
    public RunwayMode mode() {
        return this.mode;
    }

    /**
     * Getter for runway status
     * @return one of AVAILABLE, INSPECTION, SNOW_CLEARANCE, EQUIPMENT_FAILURE
     */
    public RunwayStatus status() {
        return this.status;
    }

    /**
     * Setter for runway mode
     * @param newMode one of LANDING, TAKEOFF, MIXED
     */
    public void setMode(RunwayMode newMode) {
        this.mode = newMode;
    }

    /**
     * Setter for runway status
     * @param newStatus one of AVAILABLE, INSPECTION, SNOW_CLEARANCE, EQUIPMENT_FAILURE
     */
    public void setStatus(RunwayStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Checks if the runway is available for landing at the given time
     * @param time time to check
     * @return true if available
     */
    public boolean isAvailableForLanding(LocalDateTime time) {
        if (this.mode == RunwayMode.LANDING || this.mode == RunwayMode.MIXED) {
            return !this.isOccupied(time);
        }
        return false;
    }

    /**
     * Checks if the runway is available for takeoff at the given time
     * @param time time to check
     * @return true if available
     */
    public boolean isAvailableForTakeOff(LocalDateTime time) {
        if (this.mode == RunwayMode.TAKEOFF || this.mode == RunwayMode.MIXED) {
            return !this.isOccupied(time);
        }
        return false;
    }

    /**
     * Getter for time the runway is occupied until
     * @return a time if the runway is occupied, null otherwise
     */
    public LocalDateTime occupiedUntil() {
        return this.occupiedUntil;
    }

    /**
     * set runway to be occupied until a given time
     * @param time time to set
     */
    public void occupyUntil(LocalDateTime time) {
        if (!this.isOccupied(time))
            this.occupiedUntil = time;
    }
    
    /**
     * Checks if runway is occupied at a given time
     * @param time time to check
     * @return true if occupied
     */
    public boolean isOccupied(LocalDateTime time) {
        return (this.occupiedUntil == null || this.occupiedUntil.isBefore(time));
    }
    
    /**
     * Marks runway is not occupied
     */
    public void clearOccupancy() {
        this.occupiedUntil = null;
    }

    /**
     * Gets gets next available time runway is available
     * @param time current time
     * @return current time if available, otherwise next available time
     */
    public LocalDateTime nextAvailableTime(LocalDateTime time) {
        return this.isOccupied(time) ? this.occupiedUntil : time;
    }
}
