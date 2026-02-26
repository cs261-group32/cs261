package com.group32.cs261project.model;

import java.time.LocalDateTime;
import java.util.List;

import com.group32.cs261project.model.enums.RunwayMode;
import com.group32.cs261project.model.enums.RunwayStatus;
import com.group32.cs261project.queues.HoldingPattern;
import com.group32.cs261project.queues.TakeoffQueue;

/**
 * Class for Airport
 */
public class Airport {

    private final List<Runway> runways;
    private final HoldingPattern holdingPattern;
    private final TakeoffQueue takeoffQueue;

    /**
     * Constructor
     * @param runways A list of runway objects
     */
    public Airport(List<Runway> runways) {
        this.runways = runways;
        this.holdingPattern = new HoldingPattern();
        this.takeoffQueue = new TakeoffQueue();
    }

    /**
     * Getter for list of runways
     * @return list of runways
     */
    public List<Runway> runways() {
        return this.runways;
    }

    /**
     * Getter for holding pattern
     * @return holding pattern object
     */
    public HoldingPattern holdingPattern() {
        return this.holdingPattern;
    }

    /**
     * Getter for take off queue
     * @return takeoff queue object
     */
    public TakeoffQueue takeoffQueue() {
        return this.takeoffQueue;
    }

    /**
     * Gets runway of given runway number in the airport
     * @param runwayNumber runway number
     * @return corresponding runway at the airport
     */
    public Runway getRunway(int runwayNumber) {
        return this.runways.get(runwayNumber);
    }

    /**
     * Set status of runway with given runway number
     * @param runwayNumber runway number
     * @param status one of LANDING, TAKEOFF, MIXED 
     */
    public void setRunwayStatus(int runwayNumber, RunwayStatus status) {
        this.getRunway(runwayNumber).setStatus(status);
    }

    /**
     * Set mode of runway with given runway number
     * @param runwayNumber runway number
     * @param mode one of AVAILABLE, INSPECTION, SNOW_CLEARANCE, EQUIPMENT_FAILURE
     */
    public void setRunwayMode(int runwayNumber, RunwayMode mode) {
        this.getRunway(runwayNumber).setMode(mode);
    }

    /**
     * Finds an available landing runway for landing at the given time
     * @param time datetime to check
     * @return an available runway or null if none available
     */
    public Runway findAvailableLandingRunway(LocalDateTime time) {
        for (Runway runway : this.runways) {
            if (runway.isAvailableForLanding(time)) {
                return runway;
            }
        }
        return null;
    }

    /**
     * Finds an available runway for takeoff at the given time
     * @param time datetime to check
     * @return an available runway or null if none available
     */
    public Runway findAvailableTakeoffRunway(LocalDateTime time) {
        for (Runway runway : this.runways) {
            if (runway.isAvailableForTakeOff(time)) {
                return runway;
            }
        }
        return null;
    }



    
}
