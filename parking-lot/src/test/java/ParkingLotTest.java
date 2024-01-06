import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.example.Car;
import com.example.ParkingLot;
import com.example.Ticket;
import com.example.DummySecurityObserver;
import com.example.ParkingAttendant;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

class ParkingLotTest {

    // Helper method to fill the parking lot
    private void fillParkingLot(ParkingLot parkingLot, int numberOfCars) {
        for (int i = 0; i < numberOfCars; i++) {
            parkingLot.parkCar(new Car("CAR" + i, "AnyColor")); 
        }
    }

    @Test
    void testParkingAvailability() {
        ParkingLot parkingLot = new ParkingLot(100);
        Car car = new Car("ABC123", "AnyColor");
        assertNotNull(parkingLot.parkCar(car), "Parking should be successful, but ticket was null.");
    }

    @Test
    void testParkingLotFull() {
        ParkingLot parkingLot = new ParkingLot(100);
        fillParkingLot(parkingLot, 100);
        Car newCar = new Car("XYZ789", "AnyColor"); 
        assertNull(parkingLot.parkCar(newCar), "Parking should fail, but a ticket was issued.");
    }

    @Test
    void testSuccessfulParking() {
        ParkingLot parkingLot = new ParkingLot(100);
        Car car = new Car("DEF456", "AnyColor");
        assertNotNull(parkingLot.parkCar(car), "Expected a valid ticket, but got null.");
    }

    @Test
    void testInvalidCar() {
        ParkingLot parkingLot = new ParkingLot(100);
        assertThrows(IllegalArgumentException.class, () -> parkingLot.parkCar(null));
    }

    @Test
    void testUnparkCar() {
        ParkingLot parkingLot = new ParkingLot(100);
        Car car = new Car("GHI789", "AnyColor"); 
        Ticket ticket = parkingLot.parkCar(car);
        assertNotNull(ticket, "Car should have been parked successfully.");

        Car unparkedCar = parkingLot.unparkCar(ticket);
        assertEquals(car, unparkedCar, "Unparked car should match the parked car.");
    }

    @Test
    void testUnparkCarWithInvalidTicket() {
        ParkingLot parkingLot = new ParkingLot(100);
        // Update the constructor call to include a dummy LocalDateTime value
        Ticket invalidTicket = new Ticket("Invalid123", "InvalidSpot", LocalDateTime.now());
        assertNull(parkingLot.unparkCar(invalidTicket), "Unparking should fail for an invalid ticket.");
    }

    @Test
    void testUnparkCarWithNullTicket() {
        ParkingLot parkingLot = new ParkingLot(100);
        assertNull(parkingLot.unparkCar(null), "Unparking should fail for a null ticket.");
    }

    @Test
    void testFullSignDisplayedWhenFull() {
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.parkCar(new Car("JKL012", "AnyColor")); 
        parkingLot.updateFullSign();
        assertTrue(parkingLot.isFullSignDisplayed(), "Full sign should be displayed when lot is full.");
    }

    @Test
    void testFullSignRemovedWhenSpaceAvailable() {
        ParkingLot parkingLot = new ParkingLot(1);
        Ticket ticket = parkingLot.parkCar(new Car("MNO345", "AnyColor")); 
        parkingLot.updateFullSign();
        parkingLot.unparkCar(ticket);
        parkingLot.updateFullSign();
        assertFalse(parkingLot.isFullSignDisplayed(), "Full sign should be removed when space is available.");
    }

    @Test
    void testNotifySecurityWhenFull() {
        ParkingLot parkingLot = new ParkingLot(1);
        DummySecurityObserver observer = new DummySecurityObserver();
        parkingLot.registerSecurityObserver(observer);

        parkingLot.parkCar(new Car("PQR678", "AnyColor")); 
        parkingLot.updateFullSign();

        assertTrue(observer.isNotifiedFull(), "Security should be notified that the lot is full.");
    }

    @Test
    void testNotifySecurityWhenNoLongerFull() {
        ParkingLot parkingLot = new ParkingLot(1);
        DummySecurityObserver observer = new DummySecurityObserver();
        parkingLot.registerSecurityObserver(observer);

        Ticket ticket = parkingLot.parkCar(new Car("STU901", "AnyColor")); 
        parkingLot.updateFullSign();
        parkingLot.unparkCar(ticket);
        parkingLot.updateFullSign();

        assertFalse(observer.isNotifiedFull(), "Security should be notified that the lot is no longer full.");
    }

    @Test
    void testNotificationWhenSpaceBecomesAvailable() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car("VWX123", "AnyColor"); // Added color parameter
        Ticket ticket = parkingLot.parkCar(car);

        // Initially, the parking lot is full
        parkingLot.updateFullSign();
        assertTrue(parkingLot.isFullSignDisplayed(), "Full sign should be displayed when lot is full.");

        // Unpark the car, making space available
        parkingLot.unparkCar(ticket);
        parkingLot.updateFullSign();
        assertFalse(parkingLot.isFullSignDisplayed(), "Full sign should be removed when space is available.");
    }

    @Test
    void testFindCar() {
        ParkingLot parkingLot = new ParkingLot(10);
        Car car = new Car("BCD789", "AnyColor"); // Added color parameter
        Ticket ticket = parkingLot.parkCar(car);

        String parkingSpot = parkingLot.findCar(ticket);
        assertEquals(ticket.getParkingSpot(), parkingSpot, "Should be able to find the parked car and get its parking spot.");
    }

    @Test
    void testFindCarWithInvalidTicket() {
        ParkingLot parkingLot = new ParkingLot(10);
        Ticket invalidTicket = new Ticket("InvalidTicket", "InvalidSpot", LocalDateTime.now());

        String result = parkingLot.findCar(invalidTicket);
        assertEquals("Car not found", result, "Should not find a car with an invalid ticket.");
    }

    @Test
    void testCalculateParkingCharge() {
        ParkingLot parkingLot = new ParkingLot(10);
        Car car = new Car("EFG123", "AnyColor"); // Added color parameter
        // Simulate parking the car one hour ago
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        Ticket ticket = new Ticket("TestTicket", "TestSpot", oneHourAgo);

        double charge = parkingLot.calculateCharge(ticket);
        assertEquals(ParkingLot.RATE_PER_HOUR, charge, "Charge should be for one hour of parking.");
    }

    @Test
    void testFindWhiteCars() {
        ParkingLot parkingLot = new ParkingLot(10);
        parkingLot.parkCar(new Car("WHITE1", "White"));
        parkingLot.parkCar(new Car("BLUE1", "Blue"));
        parkingLot.parkCar(new Car("WHITE2", "White"));

        Map<Car, String> whiteCars = parkingLot.findCarsByColor("White");
        assertEquals(2, whiteCars.size(), "There should be 2 white cars in the lot.");
    }

    @Test
    void testFindBlueToyotaCars() {
        ParkingLot parkingLot = new ParkingLot(10);
        ParkingAttendant attendant = new ParkingAttendant("John Doe", Collections.singletonList(parkingLot));
        parkingLot.parkCar(new Car("BLUE1", "Blue", "Toyota", "Camry"), false, false);
        parkingLot.parkCar(new Car("RED1", "Red", "Toyota", "Corolla"), false, false);

        Map<Car, String> blueToyotas = parkingLot.findCarsByMakeAndColor("Toyota", "Blue");
        assertEquals(1, blueToyotas.size(), "There should be 1 blue Toyota car in the lot.");

        // Assuming a method in ParkingLot to get ticket details by car
        Ticket ticket = parkingLot.getTicketDetails(blueToyotas.keySet().iterator().next());
        assertEquals("John Doe", ticket.getAttendantName(), "Attendant's name should be John Doe.");
    }

    @Test
    void testFindParkedBMWCars() {
        ParkingLot parkingLot = new ParkingLot(10);
        parkingLot.parkCar(new Car("BMW001", "Black", "BMW", "M3"));
        parkingLot.parkCar(new Car("AUDI001", "Red", "Audi", "A4"));
        parkingLot.parkCar(new Car("BMW002", "Blue", "BMW", "X5"));

        Map<Car, String> parkedBMWs = parkingLot.findCarsByMake("BMW");
        assertEquals(2, parkedBMWs.size(), "There should be 2 BMW cars in the lot.");
    }

    @Test
    void testFindCarsParkedInLast30Minutes() {
        ParkingLot parkingLot = new ParkingLot(10);
        Car car1 = new Car("CAR123", "Blue", "Brand", "Model");
        LocalDateTime twentyMinutesAgo = LocalDateTime.now().minusMinutes(20);
        parkingLot.parkCar(car1, twentyMinutesAgo); // Park a car 20 minutes ago

        Car car2 = new Car("CAR456", "Red", "Brand", "Model");
        LocalDateTime fortyMinutesAgo = LocalDateTime.now().minusMinutes(40);
        parkingLot.parkCar(car2, fortyMinutesAgo); // Park a car 40 minutes ago

        Map<Car, String> recentCars = parkingLot.findCarsParkedWithinLastMinutes(30);
        assertEquals(1, recentCars.size(), "There should be 1 car parked in the last 30 minutes.");
    }

    @Test
    void testFindSmallHandicapCarsInRowsBAndD() {
        ParkingLot parkingLot = new ParkingLot(10);
        parkingLot.parkCar(new Car("HANDICAP1", "Blue", "Brand", "Model", "small", true), "B");
        parkingLot.parkCar(new Car("HANDICAP2", "Red", "Brand", "Model", "large", true), "D");
        parkingLot.parkCar(new Car("HANDICAP3", "Green", "Brand", "Model", "small", true), "D");

        Map<Car, Ticket> cars = parkingLot.findSmallHandicapCarsInRows("B", "D");
        assertEquals(2, cars.size(), "There should be 2 small handicap cars in rows B and D.");
    }

    @Test
    void testGetAllParkedCars() {
        ParkingLot parkingLot = new ParkingLot(10);
        parkingLot.parkCar(new Car("PLATE1", "Blue", "Make", "Model", "Size", false));
        parkingLot.parkCar(new Car("PLATE2", "Red", "Make", "Model", "Size", false));
        parkingLot.parkCar(new Car("PLATE3", "Green", "Make", "Model", "Size", false));

        Map<Car, Ticket> parkedCars = parkingLot.getAllParkedCars();
        assertEquals(3, parkedCars.size(), "There should be 3 cars parked in the lot.");
    }

    // Helper method
    private void parkCar(ParkingLot parkingLot, Car car) {
        String parkingSpot = "Spot_" + (parkingLot.getNumberOfParkedCars() + 1);
        parkingLot.getParkedCars().put(car, new Ticket(UUID.randomUUID().toString(), parkingSpot, LocalDateTime.now(), "Test Attendant", "Row"));
    }

}