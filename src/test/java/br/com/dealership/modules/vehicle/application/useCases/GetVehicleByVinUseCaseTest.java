package br.com.dealership.modules.vehicle.application.useCases;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetVehicleByVinUseCaseTest {

    @Mock
    private VehicleRepositoryPort vehicleRepositoryPort;

    @InjectMocks
    private GetVehicleByVinUseCase useCase;

    private Vehicle vehicle;
    private String vin;

    @BeforeEach
    void setUp() {
        vin = "1HGBH41JXMN109186";
        vehicle = new Vehicle(
                UUID.randomUUID(),
                "Honda",
                "Civic",
                2023,
                vin,
                "Black",
                VehicleStatus.AVAILABLE,
                new BigDecimal("25000.00")
        );
    }

    @Test
    @DisplayName("Should get vehicle by VIN successfully")
    void shouldGetVehicleByVinSuccessfully() {
        when(vehicleRepositoryPort.getVehicleByVin(vin)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> result = useCase.execute(vin);

        assertTrue(result.isPresent());
        assertEquals(vin, result.get().getVin());
        verify(vehicleRepositoryPort).getVehicleByVin(vin);
    }

    @Test
    @DisplayName("Should return empty optional when vehicle is not found")
    void shouldReturnEmptyOptionalWhenVehicleIsNotFound() {
        when(vehicleRepositoryPort.getVehicleByVin(vin)).thenReturn(Optional.empty());

        Optional<Vehicle> result = useCase.execute(vin);

        assertTrue(result.isEmpty());
        verify(vehicleRepositoryPort).getVehicleByVin(vin);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is null")
    void shouldThrowIllegalArgumentExceptionWhenVinIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(null);
        });

        assertEquals("VIN cannot be null or empty", exception.getMessage());
        verify(vehicleRepositoryPort, never()).getVehicleByVin(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is empty")
    void shouldThrowIllegalArgumentExceptionWhenVinIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute("");
        });

        assertEquals("VIN cannot be null or empty", exception.getMessage());
        verify(vehicleRepositoryPort, never()).getVehicleByVin(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is blank")
    void shouldThrowIllegalArgumentExceptionWhenVinIsBlank() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute("   ");
        });

        assertEquals("VIN cannot be null or empty", exception.getMessage());
        verify(vehicleRepositoryPort, never()).getVehicleByVin(any());
    }
}
