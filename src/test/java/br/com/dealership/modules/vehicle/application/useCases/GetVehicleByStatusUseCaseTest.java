package br.com.dealership.modules.vehicle.application.useCases;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetVehicleByStatusUseCaseTest {

    @Mock
    private VehicleRepositoryPort vehicleRepositoryPort;

    @InjectMocks
    private GetVehicleByStatusUseCase useCase;

    @Test
    @DisplayName("Should get vehicles by status AVAILABLE successfully")
    void shouldGetVehiclesByStatusAvailableSuccessfully() {
        Vehicle vehicle1 = new Vehicle(UUID.randomUUID(), "Honda", "Civic", 2023, "VIN1", "Black", VehicleStatus.AVAILABLE, new BigDecimal("25000"));
        Vehicle vehicle2 = new Vehicle(UUID.randomUUID(), "Toyota", "Camry", 2023, "VIN2", "White", VehicleStatus.AVAILABLE, new BigDecimal("30000"));
        List<Vehicle> expectedVehicles = Arrays.asList(vehicle1, vehicle2);

        when(vehicleRepositoryPort.getAllByStatus(VehicleStatus.AVAILABLE)).thenReturn(expectedVehicles);

        List<Vehicle> result = useCase.execute(VehicleStatus.AVAILABLE);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedVehicles, result);
        verify(vehicleRepositoryPort).getAllByStatus(VehicleStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Should get vehicles by status SOLD successfully")
    void shouldGetVehiclesByStatusSoldSuccessfully() {
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), "Honda", "Civic", 2023, "VIN1", "Black", VehicleStatus.SOLD, new BigDecimal("25000"));
        List<Vehicle> expectedVehicles = Collections.singletonList(vehicle);

        when(vehicleRepositoryPort.getAllByStatus(VehicleStatus.SOLD)).thenReturn(expectedVehicles);

        List<Vehicle> result = useCase.execute(VehicleStatus.SOLD);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(VehicleStatus.SOLD, result.get(0).getStatus());
        verify(vehicleRepositoryPort).getAllByStatus(VehicleStatus.SOLD);
    }

    @Test
    @DisplayName("Should return empty list when no vehicles found with given status")
    void shouldReturnEmptyListWhenNoVehiclesFoundWithGivenStatus() {
        when(vehicleRepositoryPort.getAllByStatus(VehicleStatus.AVAILABLE)).thenReturn(Collections.emptyList());

        List<Vehicle> result = useCase.execute(VehicleStatus.AVAILABLE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(vehicleRepositoryPort).getAllByStatus(VehicleStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when status is null")
    void shouldThrowIllegalArgumentExceptionWhenStatusIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(null);
        });

        assertEquals("Status cannot be null", exception.getMessage());
        verify(vehicleRepositoryPort, never()).getAllByStatus(any());
    }
}
