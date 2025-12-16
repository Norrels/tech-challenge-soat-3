package br.com.dealership.modules.vehicle.domain.ports.in;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleServicePort {
    List<Vehicle> getAllVehiclesByStatus(VehicleStatus status);
    Optional<Vehicle> getVehicleByVin(String vin);
    Vehicle createVehicle(Vehicle vehicle);
    Vehicle updateVehicle(UUID id, Vehicle vehicle);
}
