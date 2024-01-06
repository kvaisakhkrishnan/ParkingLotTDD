package com.example;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Comparator;


public class ParkingAttendant {
    private List<ParkingLot> parkingLots;
    private String name;

    public ParkingAttendant(String name, List<ParkingLot> parkingLots) {
        this.name = name;
        this.parkingLots = parkingLots;
    }

    public Ticket parkCar(Car car, boolean isHandicap, boolean isLarge) {
        ParkingLot selectedLot;

        if (isHandicap) {
            selectedLot = findLotWithNearestFreeSpace();
        } else if (isLarge) {
            selectedLot = findLotWithMostFreeSpaces();
        } else {
            selectedLot = findLotWithLeastCars();
        }

        if (selectedLot != null) {
            String parkingSpot = "Spot_" + (selectedLot.getNumberOfParkedCars() + 1); // Example parking spot assignment
            return new Ticket(UUID.randomUUID().toString(), parkingSpot, LocalDateTime.now(), this.name);
        }
        return null; // Or handle this case as per the requirement
    }

    private ParkingLot findLotWithLeastCars() {
        return parkingLots.stream()
                          .min(Comparator.comparing(ParkingLot::getNumberOfParkedCars))
                          .orElse(null);
    }

    private ParkingLot findLotWithNearestFreeSpace() {
        return parkingLots.stream()
                          .filter(ParkingLot::hasNearestFreeSpace)
                          .findFirst()
                          .orElse(null);
    }

    private ParkingLot findLotWithMostFreeSpaces() {
        return parkingLots.stream()
                          .max(Comparator.comparing(ParkingLot::getNumberOfFreeSpaces))
                          .orElse(null);
    }
}


