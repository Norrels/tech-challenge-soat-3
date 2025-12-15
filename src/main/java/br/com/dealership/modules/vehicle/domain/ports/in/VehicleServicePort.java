package br.com.dealership.modules.vehicle.domain.ports.in;

import br.com.dealership.modules.vehicle.adapter.database.models.VehicleEntity;
import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;

import java.util.List;
import java.util.Optional;

public interface VehicleServicePort {
    List<Vehicle> getAllVehiclesByStatus(VehicleStatus status);
    Optional<Vehicle> getVehicleByVin(String vin);
    Vehicle createVehicle(Vehicle vehicle);
}
