package br.com.dealership.modules.vehicle.domain.ports.out;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepositoryPort {
    List<Vehicle> getAllByStatus(VehicleStatus status);
    Optional<Vehicle> getVehicleByVin(String id);
    Optional<Vehicle> getVehicleById(UUID id);
    Vehicle saveVehicle(Vehicle vehicle);
}
