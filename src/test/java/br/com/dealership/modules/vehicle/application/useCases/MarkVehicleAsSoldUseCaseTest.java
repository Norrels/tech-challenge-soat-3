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
class MarkVehicleAsSoldUseCaseTest {

    @Mock
    private VehicleRepositoryPort repository;

    @InjectMocks
    private MarkVehicleAsSoldUseCase useCase;

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
    @DisplayName("Should mark vehicle as sold successfully when vehicle exists")
    void shouldMarkVehicleAsSoldSuccessfullyWhenVehicleExists() {
        Vehicle mockVehicle = mock(Vehicle.class);
        when(repository.getVehicleById(vehicleId)).thenReturn(Optional.of(mockVehicle));
        when(repository.saveVehicle(any(Vehicle.class))).thenReturn(mockVehicle);

        useCase.execute(vehicleId);

        verify(repository).getVehicleById(vehicleId);
        verify(mockVehicle).markAsSold();
        verify(repository).saveVehicle(mockVehicle);
    }

    @Test
    @DisplayName("Should not save when vehicle does not exist")
    void shouldNotSaveWhenVehicleDoesNotExist() {
        when(repository.getVehicleById(vehicleId)).thenReturn(Optional.empty());

        useCase.execute(vehicleId);

        verify(repository).getVehicleById(vehicleId);
        verify(repository, never()).saveVehicle(any());
    }

    @Test
    @DisplayName("Should call markAsSold method on vehicle")
    void shouldCallMarkAsSoldMethodOnVehicle() {
        Vehicle mockVehicle = mock(Vehicle.class);
        when(repository.getVehicleById(vehicleId)).thenReturn(Optional.of(mockVehicle));
        when(repository.saveVehicle(mockVehicle)).thenReturn(mockVehicle);

        useCase.execute(vehicleId);

        verify(mockVehicle).markAsSold();
        verify(repository).saveVehicle(mockVehicle);
    }

    @Test
    @DisplayName("Should complete successfully without error when vehicle not found")
    void shouldCompleteSuccessfullyWithoutErrorWhenVehicleNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(repository.getVehicleById(nonExistentId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> useCase.execute(nonExistentId));

        verify(repository).getVehicleById(nonExistentId);
        verify(repository, never()).saveVehicle(any());
    }
}
