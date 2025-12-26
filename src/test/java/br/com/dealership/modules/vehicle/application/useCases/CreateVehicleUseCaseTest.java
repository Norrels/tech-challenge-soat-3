package br.com.dealership.modules.vehicle.application.useCases;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.exception.DuplicateVinException;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CreateVehicleUseCaseTest {

    @Mock
    private VehicleRepositoryPort vehicleRepositoryPort;

    @InjectMocks
    private CreateVehicleUseCase useCase;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = mock(Vehicle.class);
        lenient().when(vehicle.getVin()).thenReturn("1HGBH41JXMN109186");
    }

    @Test
    @DisplayName("Should create vehicle successfully when VIN is unique")
    void shouldCreateVehicleSuccessfullyWhenVinIsUnique() {
        when(vehicleRepositoryPort.getVehicleByVin("1HGBH41JXMN109186")).thenReturn(Optional.empty());
        when(vehicleRepositoryPort.saveVehicle(vehicle)).thenReturn(vehicle);
        doNothing().when(vehicle).validate();

        Vehicle result = useCase.execute(vehicle);

        assertNotNull(result);
        assertEquals(vehicle, result);
        verify(vehicle).validate();
        verify(vehicleRepositoryPort).getVehicleByVin("1HGBH41JXMN109186");
        verify(vehicleRepositoryPort).saveVehicle(vehicle);
    }

    @Test
    @DisplayName("Should throw DuplicateVinException when VIN already exists")
    void shouldThrowDuplicateVinExceptionWhenVinAlreadyExists() {
        Vehicle existingVehicle = mock(Vehicle.class);
        when(vehicleRepositoryPort.getVehicleByVin("1HGBH41JXMN109186")).thenReturn(Optional.of(existingVehicle));
        doNothing().when(vehicle).validate();

        Exception exception = assertThrows(DuplicateVinException.class, () -> {
            useCase.execute(vehicle);
        });

        assertTrue(exception.getMessage().contains("1HGBH41JXMN109186"));
        verify(vehicle).validate();
        verify(vehicleRepositoryPort).getVehicleByVin("1HGBH41JXMN109186");
        verify(vehicleRepositoryPort, never()).saveVehicle(any());
    }

    @Test
    @DisplayName("Should call validate before checking VIN uniqueness")
    void shouldCallValidateBeforeCheckingVinUniqueness() {
        when(vehicleRepositoryPort.getVehicleByVin("1HGBH41JXMN109186")).thenReturn(Optional.empty());
        when(vehicleRepositoryPort.saveVehicle(vehicle)).thenReturn(vehicle);
        doNothing().when(vehicle).validate();

        useCase.execute(vehicle);

        verify(vehicle).validate();
        verify(vehicleRepositoryPort).getVehicleByVin("1HGBH41JXMN109186");
    }

    @Test
    @DisplayName("Should propagate validation exception when vehicle is invalid")
    void shouldPropagateValidationExceptionWhenVehicleIsInvalid() {
        doThrow(new IllegalArgumentException("Invalid vehicle data")).when(vehicle).validate();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(vehicle);
        });

        assertEquals("Invalid vehicle data", exception.getMessage());
        verify(vehicle).validate();
        verify(vehicleRepositoryPort, never()).getVehicleByVin(any());
        verify(vehicleRepositoryPort, never()).saveVehicle(any());
    }
}
