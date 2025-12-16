package br.com.dealership.modules.vehicle.domain.ports.out;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;

import java.util.List;
import java.util.Optional;

public interface VehicleRepositoryPort {
    List<Vehicle> getAllByStatus(VehicleStatus status);
    Optional<Vehicle> getVehicleByVin(String id);
    Vehicle saveVehicle(Vehicle vehicle);
}
