package com.example;
import java.time.LocalDateTime;

public class Ticket {
    private String ticketId;
    private String parkingSpot;
    private LocalDateTime parkedAt;
    private String attendantName;
    private String parkingRow; // The row in which the car is parked

    // Constructor
    public Ticket(String ticketId, String parkingSpot, LocalDateTime parkedAt, String attendantName, String parkingRow) {
        this.ticketId = ticketId;
        this.parkingSpot = parkingSpot;
        this.parkedAt = parkedAt;
        this.attendantName = attendantName;
        this.parkingRow = parkingRow;
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public String getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getParkedAt() {
        return parkedAt;
    }

    public String getAttendantName() {
        return attendantName;
    }

    public String getParkingRow() {
        return parkingRow;
    }
}
