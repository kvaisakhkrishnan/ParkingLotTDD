package com.example;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class ParkingLot {
    private int capacity;
    private Map<Car, Ticket> parkedCars;
    private boolean isFullSignDisplayed = false;
    private List<SecurityObserver> securityObservers = new ArrayList<>();
    public static final double RATE_PER_HOUR = 5.0;  //rate of parking

    public void registerSecurityObserver(SecurityObserver observer) {
        securityObservers.add(observer);
    }

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.parkedCars = new HashMap<>();
    }

    public String findCar(Ticket ticket) {
        if (ticket == null || !parkedCars.containsValue(ticket)) {
            return "Car not found";
        }

        return ticket.getParkingSpot();
    }

    public Map<Car, String> findCarsByColor(String color) {
        return parkedCars.entrySet().stream()
                         .filter(entry -> color.equalsIgnoreCase(entry.getKey().getColor()))
                         .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getParkingSpot()));
    }

    public Map<Car, String> findCarsByMake(String make) {
        return parkedCars.entrySet().stream()
                         .filter(entry -> make.equalsIgnoreCase(entry.getKey().getMake()))
                         .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getParkingSpot()));
    }

    public Map<Car, String> findCarsParkedWithinLastMinutes(int minutes) {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(minutes);
        return parkedCars.entrySet().stream()
                         .filter(entry -> entry.getValue().getParkedAt().isAfter(thirtyMinutesAgo))
                         .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getParkingSpot()));
    }

    public Map<Car, Ticket> findSmallHandicapCarsInRows(String... rows) {
        return parkedCars.entrySet().stream()
                         .filter(entry -> entry.getKey().isHandicap() && "small".equalsIgnoreCase(entry.getKey().getSize()))
                         .filter(entry -> Arrays.asList(rows).contains(entry.getValue().getParkingRow()))
                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Ticket parkCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }

        if (isFull()) {
            //parking lot is full or not
            return null;
        }

        String parkingSpot = "Spot_" + (parkedCars.size() + 1);
        String attendantName = "Unknown Attendant"; // or fetch the actual attendant name if available
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), parkingSpot, parkedAt, attendantName);
        parkedCars.put(car, ticket);
        return ticket;
    }

    public Car unparkCar(Ticket ticket) {
        if (ticket == null || !parkedCars.containsValue(ticket)) {
            return null;
        }

        Car carToUnpark = parkedCars.entrySet().stream()
            .filter(entry -> ticket.equals(entry.getValue()))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);

        if (carToUnpark != null) {
            parkedCars.remove(carToUnpark);
        }

        return carToUnpark;
    }

    public boolean hasNearestFreeSpace() {
        return !isFull(); // Simplified; in reality, this would involve more complex logic
    }

    public int getNumberOfFreeSpaces() {
        return capacity - parkedCars.size(); // Number of free spaces is total capacity minus the number of parked cars
    }

    public boolean isFull() {
        return parkedCars.size() >= capacity;
    }

    public boolean isFullSignDisplayed() {
        return isFullSignDisplayed;
    }

    public void updateFullSign() {
        if (isFull() && !isFullSignDisplayed) {
            displayFullSign();
            isFullSignDisplayed = true;
        } else if (!isFull() && isFullSignDisplayed) {
            removeFullSign();
            isFullSignDisplayed = false;
        }
        notifySecurityObservers();
    }

    public int getNumberOfParkedCars() {
        return parkedCars.size();
    }

    private void notifySecurityObservers() {
        for (SecurityObserver observer : securityObservers) {
            observer.notify(isFull());
        }
    }

    private void displayFullSign() {
        // Simulate displaying the "Full" sign
        System.out.println("Parking Lot is Full. Full sign displayed.");
    }

    private void removeFullSign() {
        // Simulate removing the "Full" sign
        System.out.println("Parking Lot is not full anymore. Full sign removed.");
    }

    public double calculateCharge(Ticket ticket) {
        if (ticket == null) {
            return 0.0;
        }

        long hoursParked = java.time.Duration.between(ticket.getParkedAt(), LocalDateTime.now()).toHours();
        return hoursParked * RATE_PER_HOUR;
    }

    public Map<Car, Ticket> getAllParkedCars() {
        return new HashMap<>(parkedCars);
    }
}

interface SecurityObserver {
    void notify(boolean isFull);
}