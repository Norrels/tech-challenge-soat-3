package br.com.dealership.modules.vehicle.application.usecases;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.exception.VehicleNotFoundException;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;

import java.util.UUID;

public class UpdateVehicleUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;

    public UpdateVehicleUseCase(VehicleRepositoryPort vehicleRepositoryPort) {
        this.vehicleRepositoryPort = vehicleRepositoryPort;
    }

    public Vehicle execute(UUID id, Vehicle vehicle) {
        if (id == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        Vehicle existingVehicle = vehicleRepositoryPort.getVehicleById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + id));

        vehicle.setId(id);
        vehicle.setVin(existingVehicle.getVin());
        vehicle.validate();

        return vehicleRepositoryPort.saveVehicle(vehicle);
    }
}
