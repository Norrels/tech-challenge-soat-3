package br.com.dealership.modules.vehicle.application.useCases;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.exception.VehicleNotFoundException;
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
class UpdateVehicleUseCaseTest {

    @Mock
    private VehicleRepositoryPort vehicleRepositoryPort;

    @InjectMocks
    private UpdateVehicleUseCase useCase;

    private UUID vehicleId;
    private Vehicle existingVehicle;
    private Vehicle updatedVehicle;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        String vin = "1HGBH41JXMN109186";

        existingVehicle = new Vehicle(
                vehicleId,
                "Honda",
                "Civic",
                2023,
                vin,
                "Black",
                VehicleStatus.AVAILABLE,
                new BigDecimal("25000.00")
        );

        updatedVehicle = new Vehicle(
                null,
                "Honda",
                "Accord",
                2024,
                "DIFFERENTVIN",
                "White",
                VehicleStatus.AVAILABLE,
                new BigDecimal("30000.00")
        );
    }

    @Test
    @DisplayName("Should update vehicle successfully when vehicle exists")
    void shouldUpdateVehicleSuccessfullyWhenVehicleExists() {
        Vehicle mockUpdatedVehicle = mock(Vehicle.class);
        when(vehicleRepositoryPort.getVehicleById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepositoryPort.saveVehicle(any(Vehicle.class))).thenReturn(mockUpdatedVehicle);

        Vehicle result = useCase.execute(vehicleId, mockUpdatedVehicle);

        assertNotNull(result);
        verify(vehicleRepositoryPort).getVehicleById(vehicleId);
        verify(mockUpdatedVehicle).setId(vehicleId);
        verify(mockUpdatedVehicle).setVin(existingVehicle.getVin());
        verify(mockUpdatedVehicle).validate();
        verify(vehicleRepositoryPort).saveVehicle(mockUpdatedVehicle);
    }

    @Test
    @DisplayName("Should preserve original VIN when updating vehicle")
    void shouldPreserveOriginalVinWhenUpdatingVehicle() {
        Vehicle mockUpdatedVehicle = mock(Vehicle.class);
        when(vehicleRepositoryPort.getVehicleById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepositoryPort.saveVehicle(mockUpdatedVehicle)).thenReturn(mockUpdatedVehicle);

        useCase.execute(vehicleId, mockUpdatedVehicle);

        verify(mockUpdatedVehicle).setVin(existingVehicle.getVin());
    }

    @Test
    @DisplayName("Should set vehicle ID to the provided ID")
    void shouldSetVehicleIdToTheProvidedId() {
        Vehicle mockUpdatedVehicle = mock(Vehicle.class);
        when(vehicleRepositoryPort.getVehicleById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepositoryPort.saveVehicle(mockUpdatedVehicle)).thenReturn(mockUpdatedVehicle);

        useCase.execute(vehicleId, mockUpdatedVehicle);

        verify(mockUpdatedVehicle).setId(vehicleId);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when vehicle ID is null")
    void shouldThrowIllegalArgumentExceptionWhenVehicleIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(null, updatedVehicle);
        });

        assertEquals("Vehicle ID cannot be null", exception.getMessage());
        verify(vehicleRepositoryPort, never()).getVehicleById(any());
        verify(vehicleRepositoryPort, never()).saveVehicle(any());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when vehicle is null")
    void shouldThrowIllegalArgumentExceptionWhenVehicleIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(vehicleId, null);
        });

        assertEquals("Vehicle cannot be null", exception.getMessage());
        verify(vehicleRepositoryPort, never()).getVehicleById(any());
        verify(vehicleRepositoryPort, never()).saveVehicle(any());
    }

    @Test
    @DisplayName("Should throw VehicleNotFoundException when vehicle does not exist")
    void shouldThrowVehicleNotFoundExceptionWhenVehicleDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();
        when(vehicleRepositoryPort.getVehicleById(nonExistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(VehicleNotFoundException.class, () -> {
            useCase.execute(nonExistentId, updatedVehicle);
        });

        assertEquals("Vehicle not found with ID: " + nonExistentId, exception.getMessage());
        verify(vehicleRepositoryPort).getVehicleById(nonExistentId);
        verify(vehicleRepositoryPort, never()).saveVehicle(any());
    }

    @Test
    @DisplayName("Should call validate on updated vehicle")
    void shouldCallValidateOnUpdatedVehicle() {
        Vehicle mockVehicle = mock(Vehicle.class);
        when(vehicleRepositoryPort.getVehicleById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepositoryPort.saveVehicle(mockVehicle)).thenReturn(mockVehicle);

        useCase.execute(vehicleId, mockVehicle);

        verify(mockVehicle).validate();
    }

    @Test
    @DisplayName("Should propagate validation exception when vehicle is invalid")
    void shouldPropagateValidationExceptionWhenVehicleIsInvalid() {
        Vehicle mockVehicle = mock(Vehicle.class);
        when(vehicleRepositoryPort.getVehicleById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        doThrow(new IllegalArgumentException("Invalid vehicle data")).when(mockVehicle).validate();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(vehicleId, mockVehicle);
        });

        assertEquals("Invalid vehicle data", exception.getMessage());
        verify(mockVehicle).validate();
        verify(vehicleRepositoryPort, never()).saveVehicle(any());
    }
}
