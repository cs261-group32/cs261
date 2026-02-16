package com.group32.cs261project.sim;

import com.group32.cs261project.model.Aircraft;
import com.group32.cs261project.model.Runway;
import com.group32.cs261project.sim.events.RunwayOpCompleteEvent;
// import airport and runway too

import java.time.LocalDateTime;

public class Scheduler {

    private final SimulationEngine engine;

    public Scheduler(SimulationEngine engine) {
        this.engine = engine;
    }

    public void scheduleNext() {

        Airport airport = engine.getAirport();
        Runway runway = airport.getRunway();

        if (!runway.isAvailable()) {
            return;
        }

        Aircraft aircraft = null;

        switch (runway.getMode()) {

            case LANDING_ONLY:
                if (!airport.getHoldingPattern().isEmpty()) {
                    aircraft = airport.getHoldingPattern().pollNext();
                }
                break;

            case TAKEOFF_ONLY:
                if (!airport.getTakeOffQueue().isEmpty()) {
                    aircraft = airport.getTakeOffQueue().pollNext();
                }
                break;

            case MIXED:

                boolean hasLanding = !airport.getHoldingPattern().isEmpty();
                boolean hasTakeoff = !airport.getTakeOffQueue().isEmpty();

                if (hasLanding && !hasTakeoff) {
                    aircraft = airport.getHoldingPattern().pollNext();
                }
                else if (!hasLanding && hasTakeoff) {
                    aircraft = airport.getTakeOffQueue().pollNext();
                }
                else if (hasLanding && hasTakeoff) {
                    // Simple allocation policy (can improve later)
                    aircraft = airport.getHoldingPattern().pollNext();
                }

                break;
        }

        if (aircraft != null) {

            runway.setAvailable(false);

            LocalDateTime completionTime = engine.getAirport().getCurrentTime().plusMinutes(aircraft.getOperationDuration());

            RunwayOpCompleteEvent completeEvent = new RunwayOpCompleteEvent(completionTime, aircraft);

            engine.scheduleEvent(completeEvent);
        }
    }
}
