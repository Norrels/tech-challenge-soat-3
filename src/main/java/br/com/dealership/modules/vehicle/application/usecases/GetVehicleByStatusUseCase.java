package br.com.dealership.modules.vehicle.application.usecases;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;

import java.util.List;

public class GetVehicleByStatusUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;

    public GetVehicleByStatusUseCase(VehicleRepositoryPort vehicleRepositoryPort) {
        this.vehicleRepositoryPort = vehicleRepositoryPort;
    }

    public List<Vehicle> execute(VehicleStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        return vehicleRepositoryPort.getAllByStatus(status);
    }
}