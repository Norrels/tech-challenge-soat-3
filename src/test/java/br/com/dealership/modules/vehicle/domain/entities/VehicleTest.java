package br.com.dealership.modules.vehicle.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    private Vehicle vehicle;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        vehicle = new Vehicle(
                vehicleId,
                "Honda",
                "Civic",
                2023,
                "1HGBH41JXMN109186",
                "Black",
                VehicleStatus.AVAILABLE,
                new BigDecimal("25000.00")
        );
    }

    @Test
    @DisplayName("Should create vehicle with valid data")
    void shouldCreateVehicleWithValidData() {
        assertNotNull(vehicle);
        assertEquals(vehicleId, vehicle.getId());
        assertEquals("Honda", vehicle.getMake());
        assertEquals("Civic", vehicle.getModel());
        assertEquals(2023, vehicle.getYear());
        assertEquals("1HGBH41JXMN109186", vehicle.getVin());
        assertEquals("Black", vehicle.getColor());
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
        assertEquals(new BigDecimal("25000.00"), vehicle.getPrice());
    }

    @Test
    @DisplayName("Should validate vehicle successfully with all valid data")
    void shouldValidateVehicleSuccessfullyWithAllValidData() {
        assertDoesNotThrow(() -> vehicle.validate());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when make is null")
    void shouldThrowIllegalArgumentExceptionWhenMakeIsNull() {
        vehicle.setMake(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle make cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when make is empty")
    void shouldThrowIllegalArgumentExceptionWhenMakeIsEmpty() {
        vehicle.setMake("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle make cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when make is blank")
    void shouldThrowIllegalArgumentExceptionWhenMakeIsBlank() {
        vehicle.setMake("   ");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle make cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when model is null")
    void shouldThrowIllegalArgumentExceptionWhenModelIsNull() {
        vehicle.setModel(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle model cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when model is empty")
    void shouldThrowIllegalArgumentExceptionWhenModelIsEmpty() {
        vehicle.setModel("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle model cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when model is blank")
    void shouldThrowIllegalArgumentExceptionWhenModelIsBlank() {
        vehicle.setModel("   ");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle model cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when year is zero")
    void shouldThrowIllegalArgumentExceptionWhenYearIsZero() {
        vehicle.setYear(0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle year must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when year is negative")
    void shouldThrowIllegalArgumentExceptionWhenYearIsNegative() {
        vehicle.setYear(-2023);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle year must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is null")
    void shouldThrowIllegalArgumentExceptionWhenVinIsNull() {
        vehicle.setVin(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle VIN cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is empty")
    void shouldThrowIllegalArgumentExceptionWhenVinIsEmpty() {
        vehicle.setVin("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle VIN cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is blank")
    void shouldThrowIllegalArgumentExceptionWhenVinIsBlank() {
        vehicle.setVin("   ");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle VIN cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when price is null")
    void shouldThrowIllegalArgumentExceptionWhenPriceIsNull() {
        vehicle.setPrice(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle price cannot be null or negative", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when price is negative")
    void shouldThrowIllegalArgumentExceptionWhenPriceIsNegative() {
        vehicle.setPrice(new BigDecimal("-1000.00"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle price cannot be null or negative", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept zero as valid price")
    void shouldAcceptZeroAsValidPrice() {
        vehicle.setPrice(BigDecimal.ZERO);

        assertDoesNotThrow(() -> vehicle.validate());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when status is null")
    void shouldThrowIllegalArgumentExceptionWhenStatusIsNull() {
        vehicle.setStatus(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicle.validate();
        });

        assertEquals("Vehicle status cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should mark vehicle as sold successfully when status is AVAILABLE")
    void shouldMarkVehicleAsSoldSuccessfullyWhenStatusIsAvailable() {
        assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());

        vehicle.markAsSold();

        assertEquals(VehicleStatus.SOLD, vehicle.getStatus());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when trying to mark as sold a vehicle already SOLD")
    void shouldThrowIllegalStateExceptionWhenTryingToMarkAsSoldAVehicleAlreadySold() {
        vehicle.setStatus(VehicleStatus.SOLD);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            vehicle.markAsSold();
        });

        assertEquals("Vehicle is already marked as sold", exception.getMessage());
        assertEquals(VehicleStatus.SOLD, vehicle.getStatus());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        UUID newId = UUID.randomUUID();
        String newMake = "Toyota";
        String newModel = "Camry";
        int newYear = 2024;
        String newVin = "NEWVIN123456789";
        String newColor = "White";
        VehicleStatus newStatus = VehicleStatus.SOLD;
        BigDecimal newPrice = new BigDecimal("30000.00");

        vehicle.setId(newId);
        vehicle.setMake(newMake);
        vehicle.setModel(newModel);
        vehicle.setYear(newYear);
        vehicle.setVin(newVin);
        vehicle.setColor(newColor);
        vehicle.setStatus(newStatus);
        vehicle.setPrice(newPrice);

        assertEquals(newId, vehicle.getId());
        assertEquals(newMake, vehicle.getMake());
        assertEquals(newModel, vehicle.getModel());
        assertEquals(newYear, vehicle.getYear());
        assertEquals(newVin, vehicle.getVin());
        assertEquals(newColor, vehicle.getColor());
        assertEquals(newStatus, vehicle.getStatus());
        assertEquals(newPrice, vehicle.getPrice());
    }

    @Test
    @DisplayName("Should validate vehicle with positive year")
    void shouldValidateVehicleWithPositiveYear() {
        vehicle.setYear(1900);
        assertDoesNotThrow(() -> vehicle.validate());

        vehicle.setYear(2050);
        assertDoesNotThrow(() -> vehicle.validate());

        vehicle.setYear(1);
        assertDoesNotThrow(() -> vehicle.validate());
    }

    @Test
    @DisplayName("Should validate vehicle with price greater than zero")
    void shouldValidateVehicleWithPriceGreaterThanZero() {
        vehicle.setPrice(new BigDecimal("0.01"));
        assertDoesNotThrow(() -> vehicle.validate());

        vehicle.setPrice(new BigDecimal("999999.99"));
        assertDoesNotThrow(() -> vehicle.validate());
    }
}
