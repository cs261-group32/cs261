package aircraft;

public class Aircraft {
    String callsign;
    String operator;
    String origin;
    String destination;
    int arrivalTime;
    int departureTime;
    int altitude;
    int groundSpeed;
    int fuelLevel;
    String emergencyStatus;

    // Constructor
    public Aircraft(String callsign, String operator, String origin, String destination, int arrivalTime,
            int departureTime, int fuelLevel, int altitude, int groundSpeed, String emergencyStatus) {
        this.callsign = callsign;
        this.operator = operator;
        this.origin = origin;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.altitude = altitude;
        this.groundSpeed = groundSpeed;
        this.fuelLevel = fuelLevel;
        this.emergencyStatus = emergencyStatus;

    }
}