package com.group32.cs261project.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import com.group32.cs261project.model.enums.AircraftState;
import com.group32.cs261project.model.enums.EmergencyStatus;

/**
 * Aircraft class, contains details about aircraft
 */
public class Aircraft {
    
    private final String callsign;
    private final String operator;
    private final String origin;
    private final String destination;
    private final LocalDateTime scheduledTime;
    private final EmergencyStatus emergency;
    private float fuelMinutesRemaining;
    private AircraftState state;
    private LocalDateTime holdingEntryTime;
    private LocalDateTime takeoffQueueEntrytIme;

    /**
     * Constructor
     * @param callsign
     * @param operator
     * @param origin
     * @param destination
     * @param scheduledTime
     * @param emergency
     * @param fuelMinutesRemaining
     * @param state
     * @param holdingEntryTime
     * @param takeoffQueueEntryTime
     */
    public Aircraft(
        String callsign, 
        String operator, 
        String origin, 
        String destination, 
        LocalDateTime scheduledTime,
        EmergencyStatus emergency,
        float fuelMinutesRemaining,
        AircraftState state,
        LocalDateTime holdingEntryTime,
        LocalDateTime takeoffQueueEntryTime
    ) {
        this.callsign = Objects.requireNonNull(callsign);
        this.operator = Objects.requireNonNull(operator);
        this.origin = Objects.requireNonNull(origin);
        this.destination = Objects.requireNonNull(destination);
        this.scheduledTime = Objects.requireNonNull(scheduledTime);
        this.emergency = Objects.requireNonNull(emergency);
        this.fuelMinutesRemaining = fuelMinutesRemaining;
        this.state = Objects.requireNonNull(state);
        this.holdingEntryTime = holdingEntryTime;
        this.takeoffQueueEntrytIme = takeoffQueueEntryTime;

    }

    // getters
    public String callsign() {
        return this.callsign;
    }

    public String operator() {
        return this.operator;
    }

    public String origin() {
        return this.origin;
    }

    public String destination() {
        return this.destination;
    }

    public LocalDateTime scheduledTime() {
        return this.scheduledTime;
    }

    // extra function
    public double delayMinutes(LocalDateTime actualTime) {
        return Duration.between(actualTime, this.scheduledTime).toMinutes();
    }

    // state transition
    public void markHolding(LocalDateTime entryTime) {
        this.state = AircraftState.HOLDING;
        this.holdingEntryTime = entryTime;
    }

    public void markTakeoffQueue(LocalDateTime entryTime) {
        this.state = AircraftState.TAKEOFF_QUEUE;
        this.takeoffQueueEntrytIme = entryTime;
    }

    public void markRunwayZone() {
        this.state = AircraftState.RUNWAY_ZONE;
    }

    public void markCompleted() {
        this.state = AircraftState.COMPLETED;
    }

    public void markDiverted() {
        this.state = AircraftState.DIVERTED;
    }

    public void markCancelled() {
        this.state = AircraftState.CANCELLED;
    }

    // fuel logic (for later)
    public void consumeFuelMinutes(double minutes) {
        if (this.fuelMinutesRemaining > minutes)
            this.fuelMinutesRemaining -= minutes;
    }

    public boolean isLowFuel(double thresholdMinutes) {
        return this.fuelMinutesRemaining <= thresholdMinutes;
    }
    
}
