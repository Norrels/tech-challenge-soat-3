package br.com.dealership.modules.vehicle.application.useCases;

import br.com.dealership.modules.shared.dtos.FindVehicleDTO;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAvailableVehicleByIdUseCaseTest {

    @Mock
    private VehicleRepositoryPort repository;

    @InjectMocks
    private FindAvailableVehicleByIdUseCase useCase;

    private Vehicle vehicle;
    private UUID vehicleId;
    private String vin;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        vin = "1HGBH41JXMN109186";
        vehicle = new Vehicle(
                vehicleId,
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
    @DisplayName("Should find vehicle by VIN and return DTO successfully")
    void shouldFindVehicleByVinAndReturnDtoSuccessfully() {
        when(repository.getVehicleByVin(vin)).thenReturn(Optional.of(vehicle));

        Optional<FindVehicleDTO> result = useCase.execute(vin);

        assertTrue(result.isPresent());
        assertEquals(vehicleId, result.get().vehicleId());
        assertEquals(vin, result.get().vin());
        assertEquals(VehicleStatus.AVAILABLE, result.get().status());
        verify(repository).getVehicleByVin(vin);
    }

    @Test
    @DisplayName("Should return empty optional when vehicle is not found")
    void shouldReturnEmptyOptionalWhenVehicleIsNotFound() {
        when(repository.getVehicleByVin("NONEXISTENT")).thenReturn(Optional.empty());

        Optional<FindVehicleDTO> result = useCase.execute("NONEXISTENT");

        assertTrue(result.isEmpty());
        verify(repository).getVehicleByVin("NONEXISTENT");
    }

    @Test
    @DisplayName("Should map vehicle fields correctly to DTO")
    void shouldMapVehicleFieldsCorrectlyToDto() {
        Vehicle soldVehicle = new Vehicle(
                vehicleId,
                "Toyota",
                "Camry",
                2022,
                vin,
                "White",
                VehicleStatus.SOLD,
                new BigDecimal("30000.00")
        );
        when(repository.getVehicleByVin(vin)).thenReturn(Optional.of(soldVehicle));

        Optional<FindVehicleDTO> result = useCase.execute(vin);

        assertTrue(result.isPresent());
        assertEquals(vehicleId, result.get().vehicleId());
        assertEquals(vin, result.get().vin());
        assertEquals(VehicleStatus.SOLD, result.get().status());
    }
}
