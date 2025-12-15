package br.com.dealership.modules.vehicle.application.usecases;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.exception.DuplicateVinException;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;

import java.util.UUID;

public class CreateVehicleUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;

    public CreateVehicleUseCase(VehicleRepositoryPort vehicleRepositoryPort) {
        this.vehicleRepositoryPort = vehicleRepositoryPort;
    }

    public Vehicle execute(Vehicle vehicle) {
        vehicle.validate();

        if (vehicle.getId() == null) {
            vehicle.setId(UUID.randomUUID());
        }

        if (vehicleRepositoryPort.getVehicleByVin(vehicle.getVin()).isPresent()) {
            throw new DuplicateVinException(vehicle.getVin());
        }

        return vehicleRepositoryPort.saveVehicle(vehicle);
    }
}