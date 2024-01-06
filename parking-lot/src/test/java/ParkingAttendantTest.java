import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import com.example.Car;
import com.example.ParkingAttendant;
import com.example.ParkingLot;
import com.example.Ticket;

class ParkingAttendantTest {

    @Test
    void testParkingAttendantParksCar() {
        List<ParkingLot> parkingLots = Collections.singletonList(new ParkingLot(10));
        ParkingAttendant attendant = new ParkingAttendant(parkingLots);
        Car car = new Car("YZA456", "AnyColor");  // Added color parameter

        Ticket ticket = attendant.parkCar(car, false, false); // false for handicap, false for large car
        assertNotNull(ticket, "Parking attendant should successfully park the car and return a ticket.");
    }

    @Test
    void testEvenDistributionOfCars() {
        List<ParkingLot> parkingLots = Arrays.asList(new ParkingLot(10), new ParkingLot(10));
        ParkingAttendant attendant = new ParkingAttendant(parkingLots);

        attendant.parkCar(new Car("CAR1", "AnyColor"), false, false); // false for handicap, false for large car
        attendant.parkCar(new Car("CAR2", "AnyColor"), false, false); // false for handicap, false for large car

        assertEquals(1, parkingLots.get(0).getNumberOfParkedCars(), "First lot should have 1 car.");
        assertEquals(1, parkingLots.get(1).getNumberOfParkedCars(), "Second lot should have 1 car.");
    }

    @Test
    void testHandicapParking() {
        List<ParkingLot> parkingLots = Arrays.asList(new ParkingLot(10), new ParkingLot(10));
        ParkingAttendant attendant = new ParkingAttendant(parkingLots);
        Car handicapCar = new Car("HANDICAP1", "AnyColor");  // Added color parameter

        Ticket ticket = attendant.parkCar(handicapCar, true, false); // true for handicap, false for large car
        assertNotNull(ticket, "Parking attendant should successfully park the handicap car in the nearest space.");
    }

    @Test
    void testLargeCarParking() {
        List<ParkingLot> parkingLots = Arrays.asList(new ParkingLot(20), new ParkingLot(10));
        ParkingAttendant attendant = new ParkingAttendant(parkingLots);
        Car largeCar = new Car("LARGE1", "AnyColor");  // Added color parameter

        Ticket ticket = attendant.parkCar(largeCar, false, true); // false for handicap, true for large car
        assertNotNull(ticket, "Parking attendant should successfully park the large car in the lot with most spaces.");
        assertEquals(1, parkingLots.get(0).getNumberOfParkedCars(), "Large car should be parked in the larger lot.");
    }
}
